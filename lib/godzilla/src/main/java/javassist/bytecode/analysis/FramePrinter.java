package javassist.bytecode.analysis;

import java.io.PrintStream;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.InstructionPrinter;
import javassist.bytecode.MethodInfo;

public final class FramePrinter {
    private final PrintStream stream;

    public FramePrinter(PrintStream stream2) {
        this.stream = stream2;
    }

    public static void print(CtClass clazz, PrintStream stream2) {
        new FramePrinter(stream2).print(clazz);
    }

    public void print(CtClass clazz) {
        CtMethod[] methods;
        for (CtMethod ctMethod : clazz.getDeclaredMethods()) {
            print(ctMethod);
        }
    }

    private String getMethodString(CtMethod method) {
        try {
            return Modifier.toString(method.getModifiers()) + " " + method.getReturnType().getName() + " " + method.getName() + Descriptor.toString(method.getSignature()) + ";";
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void print(CtMethod method) {
        this.stream.println("\n" + getMethodString(method));
        MethodInfo info = method.getMethodInfo2();
        ConstPool pool = info.getConstPool();
        CodeAttribute code = info.getCodeAttribute();
        if (code != null) {
            try {
                Frame[] frames = new Analyzer().analyze(method.getDeclaringClass(), info);
                int spacing = String.valueOf(code.getCodeLength()).length();
                CodeIterator iterator = code.iterator();
                while (iterator.hasNext()) {
                    try {
                        int pos = iterator.next();
                        this.stream.println(pos + ": " + InstructionPrinter.instructionString(iterator, pos, pool));
                        addSpacing(spacing + 3);
                        Frame frame = frames[pos];
                        if (frame == null) {
                            this.stream.println("--DEAD CODE--");
                        } else {
                            printStack(frame);
                            addSpacing(spacing + 3);
                            printLocals(frame);
                        }
                    } catch (BadBytecode e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch (BadBytecode e2) {
                throw new RuntimeException(e2);
            }
        }
    }

    private void printStack(Frame frame) {
        this.stream.print("stack [");
        int top = frame.getTopIndex();
        for (int i = 0; i <= top; i++) {
            if (i > 0) {
                this.stream.print(", ");
            }
            this.stream.print(frame.getStack(i));
        }
        this.stream.println("]");
    }

    private void printLocals(Frame frame) {
        this.stream.print("locals [");
        int length = frame.localsLength();
        for (int i = 0; i < length; i++) {
            if (i > 0) {
                this.stream.print(", ");
            }
            Type type = frame.getLocal(i);
            this.stream.print(type == null ? "empty" : type.toString());
        }
        this.stream.println("]");
    }

    private void addSpacing(int count) {
        while (true) {
            count--;
            if (count > 0) {
                this.stream.print(' ');
            } else {
                return;
            }
        }
    }
}
