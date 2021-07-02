package javassist.bytecode;

class CodeAnalyzer implements Opcode {
    private CodeAttribute codeAttr;
    private ConstPool constPool;

    public CodeAnalyzer(CodeAttribute ca) {
        this.codeAttr = ca;
        this.constPool = ca.getConstPool();
    }

    public int computeMaxStack() throws BadBytecode {
        boolean repeat;
        CodeIterator ci = this.codeAttr.iterator();
        int length = ci.getCodeLength();
        int[] stack = new int[length];
        this.constPool = this.codeAttr.getConstPool();
        initStack(stack, this.codeAttr);
        do {
            repeat = false;
            for (int i = 0; i < length; i++) {
                if (stack[i] < 0) {
                    repeat = true;
                    visitBytecode(ci, stack, i);
                }
            }
        } while (repeat);
        int maxStack = 1;
        for (int i2 = 0; i2 < length; i2++) {
            if (stack[i2] > maxStack) {
                maxStack = stack[i2];
            }
        }
        return maxStack - 1;
    }

    private void initStack(int[] stack, CodeAttribute ca) {
        stack[0] = -1;
        ExceptionTable et = ca.getExceptionTable();
        if (et != null) {
            int size = et.size();
            for (int i = 0; i < size; i++) {
                stack[et.handlerPc(i)] = -2;
            }
        }
    }

    private void visitBytecode(CodeIterator ci, int[] stack, int index) throws BadBytecode {
        int codeLength = stack.length;
        ci.move(index);
        int stackDepth = -stack[index];
        int[] jsrDepth = {-1};
        while (ci.hasNext()) {
            int index2 = ci.next();
            stack[index2] = stackDepth;
            int op = ci.byteAt(index2);
            stackDepth = visitInst(op, ci, index2, stackDepth);
            if (stackDepth < 1) {
                throw new BadBytecode("stack underflow at " + index2);
            } else if (processBranch(op, ci, index2, codeLength, stack, stackDepth, jsrDepth) || isEnd(op)) {
                return;
            } else {
                if (op == 168 || op == 201) {
                    stackDepth--;
                }
            }
        }
    }

    private boolean processBranch(int opcode, CodeIterator ci, int index, int codeLength, int[] stack, int stackDepth, int[] jsrDepth) throws BadBytecode {
        int target;
        if ((153 <= opcode && opcode <= 166) || opcode == 198 || opcode == 199) {
            checkTarget(index, index + ci.s16bitAt(index + 1), codeLength, stack, stackDepth);
        } else {
            switch (opcode) {
                case Opcode.GOTO /*{ENCODED_INT: 167}*/:
                    checkTarget(index, index + ci.s16bitAt(index + 1), codeLength, stack, stackDepth);
                    return true;
                case Opcode.JSR /*{ENCODED_INT: 168}*/:
                case Opcode.JSR_W /*{ENCODED_INT: 201}*/:
                    if (opcode == 168) {
                        target = index + ci.s16bitAt(index + 1);
                    } else {
                        target = index + ci.s32bitAt(index + 1);
                    }
                    checkTarget(index, target, codeLength, stack, stackDepth);
                    if (jsrDepth[0] < 0) {
                        jsrDepth[0] = stackDepth;
                        return false;
                    } else if (stackDepth == jsrDepth[0]) {
                        return false;
                    } else {
                        throw new BadBytecode("sorry, cannot compute this data flow due to JSR: " + stackDepth + "," + jsrDepth[0]);
                    }
                case Opcode.RET /*{ENCODED_INT: 169}*/:
                    if (jsrDepth[0] < 0) {
                        jsrDepth[0] = stackDepth + 1;
                        return false;
                    } else if (stackDepth + 1 == jsrDepth[0]) {
                        return true;
                    } else {
                        throw new BadBytecode("sorry, cannot compute this data flow due to RET: " + stackDepth + "," + jsrDepth[0]);
                    }
                case Opcode.TABLESWITCH /*{ENCODED_INT: 170}*/:
                case Opcode.LOOKUPSWITCH /*{ENCODED_INT: 171}*/:
                    int index2 = (index & -4) + 4;
                    checkTarget(index, index + ci.s32bitAt(index2), codeLength, stack, stackDepth);
                    if (opcode == 171) {
                        int npairs = ci.s32bitAt(index2 + 4);
                        int index22 = index2 + 12;
                        for (int i = 0; i < npairs; i++) {
                            checkTarget(index, index + ci.s32bitAt(index22), codeLength, stack, stackDepth);
                            index22 += 8;
                        }
                    } else {
                        int n = (ci.s32bitAt(index2 + 8) - ci.s32bitAt(index2 + 4)) + 1;
                        int index23 = index2 + 12;
                        for (int i2 = 0; i2 < n; i2++) {
                            checkTarget(index, index + ci.s32bitAt(index23), codeLength, stack, stackDepth);
                            index23 += 4;
                        }
                    }
                    return true;
                case Opcode.GOTO_W /*{ENCODED_INT: 200}*/:
                    checkTarget(index, index + ci.s32bitAt(index + 1), codeLength, stack, stackDepth);
                    return true;
            }
        }
        return false;
    }

    private void checkTarget(int opIndex, int target, int codeLength, int[] stack, int stackDepth) throws BadBytecode {
        if (target < 0 || codeLength <= target) {
            throw new BadBytecode("bad branch offset at " + opIndex);
        }
        int d = stack[target];
        if (d == 0) {
            stack[target] = -stackDepth;
        } else if (d != stackDepth && d != (-stackDepth)) {
            throw new BadBytecode("verification error (" + stackDepth + "," + d + ") at " + opIndex);
        }
    }

    private static boolean isEnd(int opcode) {
        return (172 <= opcode && opcode <= 177) || opcode == 191;
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    private int visitInst(int op, CodeIterator ci, int index, int stack) throws BadBytecode {
        switch (op) {
            case Opcode.GETSTATIC /*{ENCODED_INT: 178}*/:
                return stack + getFieldSize(ci, index);
            case Opcode.PUTSTATIC /*{ENCODED_INT: 179}*/:
                return stack - getFieldSize(ci, index);
            case Opcode.GETFIELD /*{ENCODED_INT: 180}*/:
                return stack + (getFieldSize(ci, index) - 1);
            case Opcode.PUTFIELD /*{ENCODED_INT: 181}*/:
                return stack - (getFieldSize(ci, index) + 1);
            case Opcode.INVOKEVIRTUAL /*{ENCODED_INT: 182}*/:
            case Opcode.INVOKESPECIAL /*{ENCODED_INT: 183}*/:
                return stack + (Descriptor.dataSize(this.constPool.getMethodrefType(ci.u16bitAt(index + 1))) - 1);
            case Opcode.INVOKESTATIC /*{ENCODED_INT: 184}*/:
                return stack + Descriptor.dataSize(this.constPool.getMethodrefType(ci.u16bitAt(index + 1)));
            case Opcode.INVOKEINTERFACE /*{ENCODED_INT: 185}*/:
                return stack + (Descriptor.dataSize(this.constPool.getInterfaceMethodrefType(ci.u16bitAt(index + 1))) - 1);
            case Opcode.INVOKEDYNAMIC /*{ENCODED_INT: 186}*/:
                return stack + Descriptor.dataSize(this.constPool.getInvokeDynamicType(ci.u16bitAt(index + 1)));
            case Opcode.ATHROW /*{ENCODED_INT: 191}*/:
                return 1;
            case Opcode.WIDE /*{ENCODED_INT: 196}*/:
                op = ci.byteAt(index + 1);
                break;
            case Opcode.MULTIANEWARRAY /*{ENCODED_INT: 197}*/:
                return stack + (1 - ci.byteAt(index + 3));
        }
        return stack + STACK_GROW[op];
    }

    private int getFieldSize(CodeIterator ci, int index) {
        return Descriptor.dataSize(this.constPool.getFieldrefType(ci.u16bitAt(index + 1)));
    }
}
