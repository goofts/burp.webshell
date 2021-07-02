package javassist.bytecode.analysis;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.ExceptionTable;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;

public class Analyzer implements Opcode {
    private CtClass clazz;
    private ExceptionInfo[] exceptions;
    private Frame[] frames;
    private final SubroutineScanner scanner = new SubroutineScanner();
    private Subroutine[] subroutines;

    /* access modifiers changed from: private */
    public static class ExceptionInfo {
        private int end;
        private int handler;
        private int start;
        private Type type;

        private ExceptionInfo(int start2, int end2, int handler2, Type type2) {
            this.start = start2;
            this.end = end2;
            this.handler = handler2;
            this.type = type2;
        }
    }

    public Frame[] analyze(CtClass clazz2, MethodInfo method) throws BadBytecode {
        this.clazz = clazz2;
        CodeAttribute codeAttribute = method.getCodeAttribute();
        if (codeAttribute == null) {
            return null;
        }
        int maxLocals = codeAttribute.getMaxLocals();
        int maxStack = codeAttribute.getMaxStack();
        int codeLength = codeAttribute.getCodeLength();
        CodeIterator iter = codeAttribute.iterator();
        IntQueue queue = new IntQueue();
        this.exceptions = buildExceptionInfo(method);
        this.subroutines = this.scanner.scan(method);
        Executor executor = new Executor(clazz2.getClassPool(), method.getConstPool());
        this.frames = new Frame[codeLength];
        this.frames[iter.lookAhead()] = firstFrame(method, maxLocals, maxStack);
        queue.add(iter.next());
        while (!queue.isEmpty()) {
            analyzeNextEntry(method, iter, queue, executor);
        }
        return this.frames;
    }

    public Frame[] analyze(CtMethod method) throws BadBytecode {
        return analyze(method.getDeclaringClass(), method.getMethodInfo2());
    }

    private void analyzeNextEntry(MethodInfo method, CodeIterator iter, IntQueue queue, Executor executor) throws BadBytecode {
        int pos = queue.take();
        iter.move(pos);
        iter.next();
        Frame frame = this.frames[pos].copy();
        Subroutine subroutine = this.subroutines[pos];
        try {
            executor.execute(method, pos, iter, frame, subroutine);
            int opcode = iter.byteAt(pos);
            if (opcode == 170) {
                mergeTableSwitch(queue, pos, iter, frame);
            } else if (opcode == 171) {
                mergeLookupSwitch(queue, pos, iter, frame);
            } else if (opcode == 169) {
                mergeRet(queue, iter, pos, frame, subroutine);
            } else if (Util.isJumpInstruction(opcode)) {
                int target = Util.getJumpTarget(pos, iter);
                if (Util.isJsr(opcode)) {
                    mergeJsr(queue, this.frames[pos], this.subroutines[target], pos, lookAhead(iter, pos));
                } else if (!Util.isGoto(opcode)) {
                    merge(queue, frame, lookAhead(iter, pos));
                }
                merge(queue, frame, target);
            } else if (opcode != 191 && !Util.isReturn(opcode)) {
                merge(queue, frame, lookAhead(iter, pos));
            }
            mergeExceptionHandlers(queue, method, pos, frame);
        } catch (RuntimeException e) {
            throw new BadBytecode(e.getMessage() + "[pos = " + pos + "]", e);
        }
    }

    private ExceptionInfo[] buildExceptionInfo(MethodInfo method) {
        Type type;
        ConstPool constPool = method.getConstPool();
        ClassPool classes = this.clazz.getClassPool();
        ExceptionTable table = method.getCodeAttribute().getExceptionTable();
        ExceptionInfo[] exceptions2 = new ExceptionInfo[table.size()];
        for (int i = 0; i < table.size(); i++) {
            int index = table.catchType(i);
            if (index == 0) {
                try {
                    type = Type.THROWABLE;
                } catch (NotFoundException e) {
                    throw new IllegalStateException(e.getMessage());
                }
            } else {
                type = Type.get(classes.get(constPool.getClassInfo(index)));
            }
            exceptions2[i] = new ExceptionInfo(table.startPc(i), table.endPc(i), table.handlerPc(i), type);
        }
        return exceptions2;
    }

    private Frame firstFrame(MethodInfo method, int maxLocals, int maxStack) {
        CtClass[] parameters;
        int pos = 0;
        Frame first = new Frame(maxLocals, maxStack);
        if ((method.getAccessFlags() & 8) == 0) {
            first.setLocal(0, Type.get(this.clazz));
            pos = 0 + 1;
        }
        try {
            for (CtClass ctClass : Descriptor.getParameterTypes(method.getDescriptor(), this.clazz.getClassPool())) {
                Type type = zeroExtend(Type.get(ctClass));
                int pos2 = pos + 1;
                first.setLocal(pos, type);
                if (type.getSize() == 2) {
                    pos = pos2 + 1;
                    first.setLocal(pos2, Type.TOP);
                } else {
                    pos = pos2;
                }
            }
            return first;
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private int getNext(CodeIterator iter, int of, int restore) throws BadBytecode {
        iter.move(of);
        iter.next();
        int next = iter.lookAhead();
        iter.move(restore);
        iter.next();
        return next;
    }

    private int lookAhead(CodeIterator iter, int pos) throws BadBytecode {
        if (iter.hasNext()) {
            return iter.lookAhead();
        }
        throw new BadBytecode("Execution falls off end! [pos = " + pos + "]");
    }

    private void merge(IntQueue queue, Frame frame, int target) {
        boolean changed;
        Frame old = this.frames[target];
        if (old == null) {
            this.frames[target] = frame.copy();
            changed = true;
        } else {
            changed = old.merge(frame);
        }
        if (changed) {
            queue.add(target);
        }
    }

    private void mergeExceptionHandlers(IntQueue queue, MethodInfo method, int pos, Frame frame) {
        for (int i = 0; i < this.exceptions.length; i++) {
            ExceptionInfo exception = this.exceptions[i];
            if (pos >= exception.start && pos < exception.end) {
                Frame newFrame = frame.copy();
                newFrame.clearStack();
                newFrame.push(exception.type);
                merge(queue, newFrame, exception.handler);
            }
        }
    }

    private void mergeJsr(IntQueue queue, Frame frame, Subroutine sub, int pos, int next) throws BadBytecode {
        if (sub == null) {
            throw new BadBytecode("No subroutine at jsr target! [pos = " + pos + "]");
        }
        Frame old = this.frames[next];
        boolean changed = false;
        if (old == null) {
            Frame[] frameArr = this.frames;
            old = frame.copy();
            frameArr[next] = old;
            changed = true;
        } else {
            for (int i = 0; i < frame.localsLength(); i++) {
                if (!sub.isAccessed(i)) {
                    Type oldType = old.getLocal(i);
                    Type newType = frame.getLocal(i);
                    if (oldType == null) {
                        old.setLocal(i, newType);
                        changed = true;
                    } else {
                        Type newType2 = oldType.merge(newType);
                        old.setLocal(i, newType2);
                        if (!newType2.equals(oldType) || newType2.popChanged()) {
                            changed = true;
                        }
                    }
                }
            }
        }
        if (!old.isJsrMerged()) {
            old.setJsrMerged(true);
            changed = true;
        }
        if (changed && old.isRetMerged()) {
            queue.add(next);
        }
    }

    private void mergeLookupSwitch(IntQueue queue, int pos, CodeIterator iter, Frame frame) throws BadBytecode {
        int index = (pos & -4) + 4;
        merge(queue, frame, iter.s32bitAt(index) + pos);
        int index2 = index + 4;
        int index3 = index2 + 4;
        int end = (iter.s32bitAt(index2) * 8) + index3;
        for (int index4 = index3 + 4; index4 < end; index4 += 8) {
            merge(queue, frame, iter.s32bitAt(index4) + pos);
        }
    }

    private void mergeRet(IntQueue queue, CodeIterator iter, int pos, Frame frame, Subroutine subroutine) throws BadBytecode {
        boolean changed;
        if (subroutine == null) {
            throw new BadBytecode("Ret on no subroutine! [pos = " + pos + "]");
        }
        for (Integer num : subroutine.callers()) {
            int returnLoc = getNext(iter, num.intValue(), pos);
            Frame old = this.frames[returnLoc];
            if (old == null) {
                Frame[] frameArr = this.frames;
                old = frame.copyStack();
                frameArr[returnLoc] = old;
                changed = true;
            } else {
                changed = old.mergeStack(frame);
            }
            for (Integer num2 : subroutine.accessed()) {
                int index = num2.intValue();
                Type oldType = old.getLocal(index);
                Type newType = frame.getLocal(index);
                if (oldType != newType) {
                    old.setLocal(index, newType);
                    changed = true;
                }
            }
            if (!old.isRetMerged()) {
                old.setRetMerged(true);
                changed = true;
            }
            if (changed && old.isJsrMerged()) {
                queue.add(returnLoc);
            }
        }
    }

    private void mergeTableSwitch(IntQueue queue, int pos, CodeIterator iter, Frame frame) throws BadBytecode {
        int index = (pos & -4) + 4;
        merge(queue, frame, iter.s32bitAt(index) + pos);
        int index2 = index + 4;
        int low = iter.s32bitAt(index2);
        int index3 = index2 + 4;
        int index4 = index3 + 4;
        int end = (((iter.s32bitAt(index3) - low) + 1) * 4) + index4;
        while (index4 < end) {
            merge(queue, frame, iter.s32bitAt(index4) + pos);
            index4 += 4;
        }
    }

    private Type zeroExtend(Type type) {
        if (type == Type.SHORT || type == Type.BYTE || type == Type.CHAR || type == Type.BOOLEAN) {
            return Type.INTEGER;
        }
        return type;
    }
}
