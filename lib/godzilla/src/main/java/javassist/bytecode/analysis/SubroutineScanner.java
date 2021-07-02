package javassist.bytecode.analysis;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ExceptionTable;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;

public class SubroutineScanner implements Opcode {
    Set<Integer> done = new HashSet();
    Map<Integer, Subroutine> subTable = new HashMap();
    private Subroutine[] subroutines;

    public Subroutine[] scan(MethodInfo method) throws BadBytecode {
        CodeAttribute code = method.getCodeAttribute();
        CodeIterator iter = code.iterator();
        this.subroutines = new Subroutine[code.getCodeLength()];
        this.subTable.clear();
        this.done.clear();
        scan(0, iter, null);
        ExceptionTable exceptions = code.getExceptionTable();
        for (int i = 0; i < exceptions.size(); i++) {
            scan(exceptions.handlerPc(i), iter, this.subroutines[exceptions.startPc(i)]);
        }
        return this.subroutines;
    }

    private void scan(int pos, CodeIterator iter, Subroutine sub) throws BadBytecode {
        boolean next;
        if (!this.done.contains(Integer.valueOf(pos))) {
            this.done.add(Integer.valueOf(pos));
            int old = iter.lookAhead();
            iter.move(pos);
            do {
                if (!scanOp(iter.next(), iter, sub) || !iter.hasNext()) {
                    next = false;
                    continue;
                } else {
                    next = true;
                    continue;
                }
            } while (next);
            iter.move(old);
        }
    }

    private boolean scanOp(int pos, CodeIterator iter, Subroutine sub) throws BadBytecode {
        this.subroutines[pos] = sub;
        int opcode = iter.byteAt(pos);
        if (opcode == 170) {
            scanTableSwitch(pos, iter, sub);
            return false;
        } else if (opcode == 171) {
            scanLookupSwitch(pos, iter, sub);
            return false;
        } else if (Util.isReturn(opcode) || opcode == 169 || opcode == 191) {
            return false;
        } else {
            if (Util.isJumpInstruction(opcode)) {
                int target = Util.getJumpTarget(pos, iter);
                if (opcode == 168 || opcode == 201) {
                    Subroutine s = this.subTable.get(Integer.valueOf(target));
                    if (s == null) {
                        Subroutine s2 = new Subroutine(target, pos);
                        this.subTable.put(Integer.valueOf(target), s2);
                        scan(target, iter, s2);
                    } else {
                        s.addCaller(pos);
                    }
                } else {
                    scan(target, iter, sub);
                    if (Util.isGoto(opcode)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }

    private void scanLookupSwitch(int pos, CodeIterator iter, Subroutine sub) throws BadBytecode {
        int index = (pos & -4) + 4;
        scan(iter.s32bitAt(index) + pos, iter, sub);
        int index2 = index + 4;
        int index3 = index2 + 4;
        int end = (iter.s32bitAt(index2) * 8) + index3;
        for (int index4 = index3 + 4; index4 < end; index4 += 8) {
            scan(iter.s32bitAt(index4) + pos, iter, sub);
        }
    }

    private void scanTableSwitch(int pos, CodeIterator iter, Subroutine sub) throws BadBytecode {
        int index = (pos & -4) + 4;
        scan(iter.s32bitAt(index) + pos, iter, sub);
        int index2 = index + 4;
        int low = iter.s32bitAt(index2);
        int index3 = index2 + 4;
        int index4 = index3 + 4;
        int end = (((iter.s32bitAt(index3) - low) + 1) * 4) + index4;
        while (index4 < end) {
            scan(iter.s32bitAt(index4) + pos, iter, sub);
            index4 += 4;
        }
    }
}
