package javassist.bytecode.stackmap;

import javassist.ClassPool;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.ByteArray;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.Opcode;
import javassist.bytecode.stackmap.TypeData;

public abstract class Tracer implements TypeTag {
    protected ClassPool classPool;
    protected ConstPool cpool;
    protected TypeData[] localsTypes;
    protected String returnType;
    protected int stackTop;
    protected TypeData[] stackTypes;

    public Tracer(ClassPool classes, ConstPool cp, int maxStack, int maxLocals, String retType) {
        this.classPool = classes;
        this.cpool = cp;
        this.returnType = retType;
        this.stackTop = 0;
        this.stackTypes = TypeData.make(maxStack);
        this.localsTypes = TypeData.make(maxLocals);
    }

    public Tracer(Tracer t) {
        this.classPool = t.classPool;
        this.cpool = t.cpool;
        this.returnType = t.returnType;
        this.stackTop = t.stackTop;
        this.stackTypes = TypeData.make(t.stackTypes.length);
        this.localsTypes = TypeData.make(t.localsTypes.length);
    }

    /* access modifiers changed from: protected */
    public int doOpcode(int pos, byte[] code) throws BadBytecode {
        try {
            int op = code[pos] & 255;
            if (op < 54) {
                return doOpcode0_53(pos, code, op);
            }
            if (op < 96) {
                return doOpcode54_95(pos, code, op);
            }
            if (op < 148) {
                return doOpcode96_147(pos, code, op);
            }
            return doOpcode148_201(pos, code, op);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new BadBytecode("inconsistent stack height " + e.getMessage(), e);
        }
    }

    /* access modifiers changed from: protected */
    public void visitBranch(int pos, byte[] code, int offset) throws BadBytecode {
    }

    /* access modifiers changed from: protected */
    public void visitGoto(int pos, byte[] code, int offset) throws BadBytecode {
    }

    /* access modifiers changed from: protected */
    public void visitReturn(int pos, byte[] code) throws BadBytecode {
    }

    /* access modifiers changed from: protected */
    public void visitThrow(int pos, byte[] code) throws BadBytecode {
    }

    /* access modifiers changed from: protected */
    public void visitTableSwitch(int pos, byte[] code, int n, int offsetPos, int defaultOffset) throws BadBytecode {
    }

    /* access modifiers changed from: protected */
    public void visitLookupSwitch(int pos, byte[] code, int n, int pairsPos, int defaultOffset) throws BadBytecode {
    }

    /* access modifiers changed from: protected */
    public void visitJSR(int pos, byte[] code) throws BadBytecode {
    }

    /* access modifiers changed from: protected */
    public void visitRET(int pos, byte[] code) throws BadBytecode {
    }

    private int doOpcode0_53(int pos, byte[] code, int op) throws BadBytecode {
        TypeData[] stackTypes2 = this.stackTypes;
        switch (op) {
            case 0:
                break;
            case 1:
                int i = this.stackTop;
                this.stackTop = i + 1;
                stackTypes2[i] = new TypeData.NullType();
                break;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                int i2 = this.stackTop;
                this.stackTop = i2 + 1;
                stackTypes2[i2] = INTEGER;
                break;
            case 9:
            case 10:
                int i3 = this.stackTop;
                this.stackTop = i3 + 1;
                stackTypes2[i3] = LONG;
                int i4 = this.stackTop;
                this.stackTop = i4 + 1;
                stackTypes2[i4] = TOP;
                break;
            case 11:
            case 12:
            case 13:
                int i5 = this.stackTop;
                this.stackTop = i5 + 1;
                stackTypes2[i5] = FLOAT;
                break;
            case 14:
            case 15:
                int i6 = this.stackTop;
                this.stackTop = i6 + 1;
                stackTypes2[i6] = DOUBLE;
                int i7 = this.stackTop;
                this.stackTop = i7 + 1;
                stackTypes2[i7] = TOP;
                break;
            case 16:
            case 17:
                int i8 = this.stackTop;
                this.stackTop = i8 + 1;
                stackTypes2[i8] = INTEGER;
                return op != 17 ? 2 : 3;
            case 18:
                doLDC(code[pos + 1] & 255);
                return 2;
            case 19:
            case 20:
                doLDC(ByteArray.readU16bit(code, pos + 1));
                return 3;
            case 21:
                return doXLOAD(INTEGER, code, pos);
            case 22:
                return doXLOAD(LONG, code, pos);
            case 23:
                return doXLOAD(FLOAT, code, pos);
            case 24:
                return doXLOAD(DOUBLE, code, pos);
            case 25:
                return doALOAD(code[pos + 1] & 255);
            case 26:
            case 27:
            case Opcode.ILOAD_2:
            case Opcode.ILOAD_3:
                int i9 = this.stackTop;
                this.stackTop = i9 + 1;
                stackTypes2[i9] = INTEGER;
                break;
            case Opcode.LLOAD_0:
            case Opcode.LLOAD_1:
            case 32:
            case Opcode.LLOAD_3:
                int i10 = this.stackTop;
                this.stackTop = i10 + 1;
                stackTypes2[i10] = LONG;
                int i11 = this.stackTop;
                this.stackTop = i11 + 1;
                stackTypes2[i11] = TOP;
                break;
            case Opcode.FLOAD_0:
            case 35:
            case Opcode.FLOAD_2:
            case Opcode.FLOAD_3:
                int i12 = this.stackTop;
                this.stackTop = i12 + 1;
                stackTypes2[i12] = FLOAT;
                break;
            case Opcode.DLOAD_0:
            case Opcode.DLOAD_1:
            case Opcode.DLOAD_2:
            case Opcode.DLOAD_3:
                int i13 = this.stackTop;
                this.stackTop = i13 + 1;
                stackTypes2[i13] = DOUBLE;
                int i14 = this.stackTop;
                this.stackTop = i14 + 1;
                stackTypes2[i14] = TOP;
                break;
            case Opcode.ALOAD_0:
            case Opcode.ALOAD_1:
            case Opcode.ALOAD_2:
            case 45:
                int i15 = this.stackTop;
                this.stackTop = i15 + 1;
                stackTypes2[i15] = this.localsTypes[op - 42];
                break;
            case 46:
                int i16 = this.stackTop - 1;
                this.stackTop = i16;
                stackTypes2[i16 - 1] = INTEGER;
                break;
            case 47:
                stackTypes2[this.stackTop - 2] = LONG;
                stackTypes2[this.stackTop - 1] = TOP;
                break;
            case 48:
                int i17 = this.stackTop - 1;
                this.stackTop = i17;
                stackTypes2[i17 - 1] = FLOAT;
                break;
            case 49:
                stackTypes2[this.stackTop - 2] = DOUBLE;
                stackTypes2[this.stackTop - 1] = TOP;
                break;
            case 50:
                int i18 = this.stackTop - 1;
                this.stackTop = i18;
                int s = i18 - 1;
                stackTypes2[s] = TypeData.ArrayElement.make(stackTypes2[s]);
                break;
            case 51:
            case 52:
            case 53:
                int i19 = this.stackTop - 1;
                this.stackTop = i19;
                stackTypes2[i19 - 1] = INTEGER;
                break;
            default:
                throw new RuntimeException("fatal");
        }
        return 1;
    }

    private void doLDC(int index) {
        TypeData[] stackTypes2 = this.stackTypes;
        int tag = this.cpool.getTag(index);
        if (tag == 8) {
            int i = this.stackTop;
            this.stackTop = i + 1;
            stackTypes2[i] = new TypeData.ClassName("java.lang.String");
        } else if (tag == 3) {
            int i2 = this.stackTop;
            this.stackTop = i2 + 1;
            stackTypes2[i2] = INTEGER;
        } else if (tag == 4) {
            int i3 = this.stackTop;
            this.stackTop = i3 + 1;
            stackTypes2[i3] = FLOAT;
        } else if (tag == 5) {
            int i4 = this.stackTop;
            this.stackTop = i4 + 1;
            stackTypes2[i4] = LONG;
            int i5 = this.stackTop;
            this.stackTop = i5 + 1;
            stackTypes2[i5] = TOP;
        } else if (tag == 6) {
            int i6 = this.stackTop;
            this.stackTop = i6 + 1;
            stackTypes2[i6] = DOUBLE;
            int i7 = this.stackTop;
            this.stackTop = i7 + 1;
            stackTypes2[i7] = TOP;
        } else if (tag == 7) {
            int i8 = this.stackTop;
            this.stackTop = i8 + 1;
            stackTypes2[i8] = new TypeData.ClassName("java.lang.Class");
        } else if (tag == 17) {
            pushMemberType(this.cpool.getDynamicType(index));
        } else {
            throw new RuntimeException("bad LDC: " + tag);
        }
    }

    private int doXLOAD(TypeData type, byte[] code, int pos) {
        return doXLOAD(code[pos + 1] & 255, type);
    }

    private int doXLOAD(int localVar, TypeData type) {
        TypeData[] typeDataArr = this.stackTypes;
        int i = this.stackTop;
        this.stackTop = i + 1;
        typeDataArr[i] = type;
        if (!type.is2WordType()) {
            return 2;
        }
        TypeData[] typeDataArr2 = this.stackTypes;
        int i2 = this.stackTop;
        this.stackTop = i2 + 1;
        typeDataArr2[i2] = TOP;
        return 2;
    }

    private int doALOAD(int localVar) {
        TypeData[] typeDataArr = this.stackTypes;
        int i = this.stackTop;
        this.stackTop = i + 1;
        typeDataArr[i] = this.localsTypes[localVar];
        return 2;
    }

    private int doOpcode54_95(int pos, byte[] code, int op) throws BadBytecode {
        switch (op) {
            case 54:
                return doXSTORE(pos, code, INTEGER);
            case 55:
                return doXSTORE(pos, code, LONG);
            case Opcode.FSTORE:
                return doXSTORE(pos, code, FLOAT);
            case Opcode.DSTORE:
                return doXSTORE(pos, code, DOUBLE);
            case Opcode.ASTORE:
                return doASTORE(code[pos + 1] & 255);
            case Opcode.ISTORE_0:
            case Opcode.ISTORE_1:
            case Opcode.ISTORE_2:
            case Opcode.ISTORE_3:
                this.localsTypes[op - 59] = INTEGER;
                this.stackTop--;
                break;
            case Opcode.LSTORE_0:
            case 64:
            case 65:
            case 66:
                int var = op - 63;
                this.localsTypes[var] = LONG;
                this.localsTypes[var + 1] = TOP;
                this.stackTop -= 2;
                break;
            case 67:
            case 68:
            case 69:
            case Opcode.FSTORE_3:
                this.localsTypes[op - 67] = FLOAT;
                this.stackTop--;
                break;
            case Opcode.DSTORE_0:
            case Opcode.DSTORE_1:
            case Opcode.DSTORE_2:
            case Opcode.DSTORE_3:
                int var2 = op - 71;
                this.localsTypes[var2] = DOUBLE;
                this.localsTypes[var2 + 1] = TOP;
                this.stackTop -= 2;
                break;
            case Opcode.ASTORE_0:
            case 76:
            case Opcode.ASTORE_2:
            case Opcode.ASTORE_3:
                doASTORE(op - 75);
                break;
            case Opcode.IASTORE:
            case Opcode.LASTORE:
            case Opcode.FASTORE:
            case Opcode.DASTORE:
                this.stackTop -= (op == 80 || op == 82) ? 4 : 3;
                break;
            case Opcode.AASTORE:
                TypeData.ArrayElement.aastore(this.stackTypes[this.stackTop - 3], this.stackTypes[this.stackTop - 1], this.classPool);
                this.stackTop -= 3;
                break;
            case Opcode.BASTORE:
            case Opcode.CASTORE:
            case Opcode.SASTORE:
                this.stackTop -= 3;
                break;
            case Opcode.POP:
                this.stackTop--;
                break;
            case Opcode.POP2:
                this.stackTop -= 2;
                break;
            case Opcode.DUP:
                int sp = this.stackTop;
                this.stackTypes[sp] = this.stackTypes[sp - 1];
                this.stackTop = sp + 1;
                break;
            case Opcode.DUP_X1:
            case Opcode.DUP_X2:
                int len = (op - 90) + 2;
                doDUP_XX(1, len);
                int sp2 = this.stackTop;
                this.stackTypes[sp2 - len] = this.stackTypes[sp2];
                this.stackTop = sp2 + 1;
                break;
            case Opcode.DUP2:
                doDUP_XX(2, 2);
                this.stackTop += 2;
                break;
            case Opcode.DUP2_X1:
            case Opcode.DUP2_X2:
                int len2 = (op - 93) + 3;
                doDUP_XX(2, len2);
                int sp3 = this.stackTop;
                this.stackTypes[sp3 - len2] = this.stackTypes[sp3];
                this.stackTypes[(sp3 - len2) + 1] = this.stackTypes[sp3 + 1];
                this.stackTop = sp3 + 2;
                break;
            case Opcode.SWAP:
                int sp4 = this.stackTop - 1;
                TypeData t = this.stackTypes[sp4];
                this.stackTypes[sp4] = this.stackTypes[sp4 - 1];
                this.stackTypes[sp4 - 1] = t;
                break;
            default:
                throw new RuntimeException("fatal");
        }
        return 1;
    }

    private int doXSTORE(int pos, byte[] code, TypeData type) {
        return doXSTORE(code[pos + 1] & 255, type);
    }

    private int doXSTORE(int index, TypeData type) {
        this.stackTop--;
        this.localsTypes[index] = type;
        if (!type.is2WordType()) {
            return 2;
        }
        this.stackTop--;
        this.localsTypes[index + 1] = TOP;
        return 2;
    }

    private int doASTORE(int index) {
        this.stackTop--;
        this.localsTypes[index] = this.stackTypes[this.stackTop];
        return 2;
    }

    private void doDUP_XX(int delta, int len) {
        TypeData[] types = this.stackTypes;
        int sp = this.stackTop - 1;
        int end = sp - len;
        while (sp > end) {
            types[sp + delta] = types[sp];
            sp--;
        }
    }

    private int doOpcode96_147(int pos, byte[] code, int op) {
        if (op <= 131) {
            this.stackTop += Opcode.STACK_GROW[op];
            return 1;
        }
        switch (op) {
            case Opcode.IINC:
                return 3;
            case Opcode.I2L:
                this.stackTypes[this.stackTop - 1] = LONG;
                this.stackTypes[this.stackTop] = TOP;
                this.stackTop++;
                return 1;
            case Opcode.I2F:
                this.stackTypes[this.stackTop - 1] = FLOAT;
                return 1;
            case Opcode.I2D:
                this.stackTypes[this.stackTop - 1] = DOUBLE;
                this.stackTypes[this.stackTop] = TOP;
                this.stackTop++;
                return 1;
            case Opcode.L2I:
                TypeData[] typeDataArr = this.stackTypes;
                int i = this.stackTop - 1;
                this.stackTop = i;
                typeDataArr[i - 1] = INTEGER;
                return 1;
            case Opcode.L2F:
                TypeData[] typeDataArr2 = this.stackTypes;
                int i2 = this.stackTop - 1;
                this.stackTop = i2;
                typeDataArr2[i2 - 1] = FLOAT;
                return 1;
            case Opcode.L2D:
                this.stackTypes[this.stackTop - 2] = DOUBLE;
                return 1;
            case Opcode.F2I:
                this.stackTypes[this.stackTop - 1] = INTEGER;
                return 1;
            case Opcode.F2L:
                this.stackTypes[this.stackTop - 1] = LONG;
                this.stackTypes[this.stackTop] = TOP;
                this.stackTop++;
                return 1;
            case Opcode.F2D:
                this.stackTypes[this.stackTop - 1] = DOUBLE;
                this.stackTypes[this.stackTop] = TOP;
                this.stackTop++;
                return 1;
            case Opcode.D2I:
                TypeData[] typeDataArr3 = this.stackTypes;
                int i3 = this.stackTop - 1;
                this.stackTop = i3;
                typeDataArr3[i3 - 1] = INTEGER;
                return 1;
            case Opcode.D2L:
                this.stackTypes[this.stackTop - 2] = LONG;
                return 1;
            case Opcode.D2F:
                TypeData[] typeDataArr4 = this.stackTypes;
                int i4 = this.stackTop - 1;
                this.stackTop = i4;
                typeDataArr4[i4 - 1] = FLOAT;
                return 1;
            case Opcode.I2B:
            case Opcode.I2C:
            case Opcode.I2S:
                return 1;
            default:
                throw new RuntimeException("fatal");
        }
    }

    private int doOpcode148_201(int pos, byte[] code, int op) throws BadBytecode {
        String type;
        switch (op) {
            case Opcode.LCMP:
                this.stackTypes[this.stackTop - 4] = INTEGER;
                this.stackTop -= 3;
                break;
            case Opcode.FCMPL:
            case Opcode.FCMPG:
                TypeData[] typeDataArr = this.stackTypes;
                int i = this.stackTop - 1;
                this.stackTop = i;
                typeDataArr[i - 1] = INTEGER;
                break;
            case Opcode.DCMPL:
            case Opcode.DCMPG:
                this.stackTypes[this.stackTop - 4] = INTEGER;
                this.stackTop -= 3;
                break;
            case Opcode.IFEQ:
            case Opcode.IFNE:
            case Opcode.IFLT:
            case Opcode.IFGE:
            case Opcode.IFGT:
            case Opcode.IFLE:
                this.stackTop--;
                visitBranch(pos, code, ByteArray.readS16bit(code, pos + 1));
                return 3;
            case Opcode.IF_ICMPEQ:
            case Opcode.IF_ICMPNE:
            case Opcode.IF_ICMPLT:
            case Opcode.IF_ICMPGE:
            case Opcode.IF_ICMPGT:
            case Opcode.IF_ICMPLE:
            case Opcode.IF_ACMPEQ:
            case Opcode.IF_ACMPNE:
                this.stackTop -= 2;
                visitBranch(pos, code, ByteArray.readS16bit(code, pos + 1));
                return 3;
            case Opcode.GOTO:
                visitGoto(pos, code, ByteArray.readS16bit(code, pos + 1));
                return 3;
            case Opcode.JSR:
                visitJSR(pos, code);
                return 3;
            case Opcode.RET:
                visitRET(pos, code);
                return 2;
            case Opcode.TABLESWITCH:
                this.stackTop--;
                int pos2 = (pos & -4) + 8;
                int n = (ByteArray.read32bit(code, pos2 + 4) - ByteArray.read32bit(code, pos2)) + 1;
                visitTableSwitch(pos, code, n, pos2 + 8, ByteArray.read32bit(code, pos2 - 4));
                return ((n * 4) + 16) - (pos & 3);
            case Opcode.LOOKUPSWITCH:
                this.stackTop--;
                int pos22 = (pos & -4) + 8;
                int n2 = ByteArray.read32bit(code, pos22);
                visitLookupSwitch(pos, code, n2, pos22 + 4, ByteArray.read32bit(code, pos22 - 4));
                return ((n2 * 8) + 12) - (pos & 3);
            case Opcode.IRETURN:
                this.stackTop--;
                visitReturn(pos, code);
                break;
            case Opcode.LRETURN:
                this.stackTop -= 2;
                visitReturn(pos, code);
                break;
            case Opcode.FRETURN:
                this.stackTop--;
                visitReturn(pos, code);
                break;
            case Opcode.DRETURN:
                this.stackTop -= 2;
                visitReturn(pos, code);
                break;
            case Opcode.ARETURN:
                TypeData[] typeDataArr2 = this.stackTypes;
                int i2 = this.stackTop - 1;
                this.stackTop = i2;
                typeDataArr2[i2].setType(this.returnType, this.classPool);
                visitReturn(pos, code);
                break;
            case Opcode.RETURN:
                visitReturn(pos, code);
                break;
            case Opcode.GETSTATIC:
                return doGetField(pos, code, false);
            case Opcode.PUTSTATIC:
                return doPutField(pos, code, false);
            case Opcode.GETFIELD:
                return doGetField(pos, code, true);
            case Opcode.PUTFIELD:
                return doPutField(pos, code, true);
            case Opcode.INVOKEVIRTUAL:
            case Opcode.INVOKESPECIAL:
                return doInvokeMethod(pos, code, true);
            case Opcode.INVOKESTATIC:
                return doInvokeMethod(pos, code, false);
            case Opcode.INVOKEINTERFACE:
                return doInvokeIntfMethod(pos, code);
            case Opcode.INVOKEDYNAMIC:
                return doInvokeDynamic(pos, code);
            case Opcode.NEW:
                int i3 = ByteArray.readU16bit(code, pos + 1);
                TypeData[] typeDataArr3 = this.stackTypes;
                int i4 = this.stackTop;
                this.stackTop = i4 + 1;
                typeDataArr3[i4] = new TypeData.UninitData(pos, this.cpool.getClassInfo(i3));
                return 3;
            case Opcode.NEWARRAY:
                return doNEWARRAY(pos, code);
            case Opcode.ANEWARRAY:
                String type2 = this.cpool.getClassInfo(ByteArray.readU16bit(code, pos + 1)).replace('.', '/');
                if (type2.charAt(0) == '[') {
                    type = "[" + type2;
                } else {
                    type = "[L" + type2 + ";";
                }
                this.stackTypes[this.stackTop - 1] = new TypeData.ClassName(type);
                return 3;
            case Opcode.ARRAYLENGTH:
                this.stackTypes[this.stackTop - 1].setType("[Ljava.lang.Object;", this.classPool);
                this.stackTypes[this.stackTop - 1] = INTEGER;
                break;
            case Opcode.ATHROW:
                TypeData[] typeDataArr4 = this.stackTypes;
                int i5 = this.stackTop - 1;
                this.stackTop = i5;
                typeDataArr4[i5].setType("java.lang.Throwable", this.classPool);
                visitThrow(pos, code);
                break;
            case Opcode.CHECKCAST:
                String type3 = this.cpool.getClassInfo(ByteArray.readU16bit(code, pos + 1));
                if (type3.charAt(0) == '[') {
                    type3 = type3.replace('.', '/');
                }
                this.stackTypes[this.stackTop - 1] = new TypeData.ClassName(type3);
                return 3;
            case Opcode.INSTANCEOF:
                this.stackTypes[this.stackTop - 1] = INTEGER;
                return 3;
            case Opcode.MONITORENTER:
            case Opcode.MONITOREXIT:
                this.stackTop--;
                break;
            case Opcode.WIDE:
                return doWIDE(pos, code);
            case Opcode.MULTIANEWARRAY:
                return doMultiANewArray(pos, code);
            case Opcode.IFNULL:
            case Opcode.IFNONNULL:
                this.stackTop--;
                visitBranch(pos, code, ByteArray.readS16bit(code, pos + 1));
                return 3;
            case Opcode.GOTO_W:
                visitGoto(pos, code, ByteArray.read32bit(code, pos + 1));
                return 5;
            case Opcode.JSR_W:
                visitJSR(pos, code);
                return 5;
        }
        return 1;
    }

    private int doWIDE(int pos, byte[] code) throws BadBytecode {
        int op = code[pos + 1] & 255;
        switch (op) {
            case 21:
                doWIDE_XLOAD(pos, code, INTEGER);
                break;
            case 22:
                doWIDE_XLOAD(pos, code, LONG);
                break;
            case 23:
                doWIDE_XLOAD(pos, code, FLOAT);
                break;
            case 24:
                doWIDE_XLOAD(pos, code, DOUBLE);
                break;
            case 25:
                doALOAD(ByteArray.readU16bit(code, pos + 2));
                break;
            case 54:
                doWIDE_STORE(pos, code, INTEGER);
                break;
            case 55:
                doWIDE_STORE(pos, code, LONG);
                break;
            case Opcode.FSTORE:
                doWIDE_STORE(pos, code, FLOAT);
                break;
            case Opcode.DSTORE:
                doWIDE_STORE(pos, code, DOUBLE);
                break;
            case Opcode.ASTORE:
                doASTORE(ByteArray.readU16bit(code, pos + 2));
                break;
            case Opcode.IINC:
                return 6;
            case Opcode.RET:
                visitRET(pos, code);
                break;
            default:
                throw new RuntimeException("bad WIDE instruction: " + op);
        }
        return 4;
    }

    private void doWIDE_XLOAD(int pos, byte[] code, TypeData type) {
        doXLOAD(ByteArray.readU16bit(code, pos + 2), type);
    }

    private void doWIDE_STORE(int pos, byte[] code, TypeData type) {
        doXSTORE(ByteArray.readU16bit(code, pos + 2), type);
    }

    private int doPutField(int pos, byte[] code, boolean notStatic) throws BadBytecode {
        int index = ByteArray.readU16bit(code, pos + 1);
        String desc = this.cpool.getFieldrefType(index);
        this.stackTop -= Descriptor.dataSize(desc);
        char c = desc.charAt(0);
        if (c == 'L') {
            this.stackTypes[this.stackTop].setType(getFieldClassName(desc, 0), this.classPool);
        } else if (c == '[') {
            this.stackTypes[this.stackTop].setType(desc, this.classPool);
        }
        setFieldTarget(notStatic, index);
        return 3;
    }

    private int doGetField(int pos, byte[] code, boolean notStatic) throws BadBytecode {
        int index = ByteArray.readU16bit(code, pos + 1);
        setFieldTarget(notStatic, index);
        pushMemberType(this.cpool.getFieldrefType(index));
        return 3;
    }

    private void setFieldTarget(boolean notStatic, int index) throws BadBytecode {
        if (notStatic) {
            String className = this.cpool.getFieldrefClassName(index);
            TypeData[] typeDataArr = this.stackTypes;
            int i = this.stackTop - 1;
            this.stackTop = i;
            typeDataArr[i].setType(className, this.classPool);
        }
    }

    private int doNEWARRAY(int pos, byte[] code) {
        String type;
        int s = this.stackTop - 1;
        switch (code[pos + 1] & 255) {
            case 4:
                type = "[Z";
                break;
            case 5:
                type = "[C";
                break;
            case 6:
                type = "[F";
                break;
            case 7:
                type = "[D";
                break;
            case 8:
                type = "[B";
                break;
            case 9:
                type = "[S";
                break;
            case 10:
                type = "[I";
                break;
            case 11:
                type = "[J";
                break;
            default:
                throw new RuntimeException("bad newarray");
        }
        this.stackTypes[s] = new TypeData.ClassName(type);
        return 2;
    }

    private int doMultiANewArray(int pos, byte[] code) {
        int i = ByteArray.readU16bit(code, pos + 1);
        this.stackTop -= (code[pos + 3] & 255) - 1;
        this.stackTypes[this.stackTop - 1] = new TypeData.ClassName(this.cpool.getClassInfo(i).replace('.', '/'));
        return 4;
    }

    private int doInvokeMethod(int pos, byte[] code, boolean notStatic) throws BadBytecode {
        int i = ByteArray.readU16bit(code, pos + 1);
        String desc = this.cpool.getMethodrefType(i);
        checkParamTypes(desc, 1);
        if (notStatic) {
            String className = this.cpool.getMethodrefClassName(i);
            TypeData[] typeDataArr = this.stackTypes;
            int i2 = this.stackTop - 1;
            this.stackTop = i2;
            TypeData target = typeDataArr[i2];
            if ((target instanceof TypeData.UninitTypeVar) && target.isUninit()) {
                constructorCalled(target, ((TypeData.UninitTypeVar) target).offset());
            } else if (target instanceof TypeData.UninitData) {
                constructorCalled(target, ((TypeData.UninitData) target).offset());
            }
            target.setType(className, this.classPool);
        }
        pushMemberType(desc);
        return 3;
    }

    private void constructorCalled(TypeData target, int offset) {
        target.constructorCalled(offset);
        for (int i = 0; i < this.stackTop; i++) {
            this.stackTypes[i].constructorCalled(offset);
        }
        for (int i2 = 0; i2 < this.localsTypes.length; i2++) {
            this.localsTypes[i2].constructorCalled(offset);
        }
    }

    private int doInvokeIntfMethod(int pos, byte[] code) throws BadBytecode {
        int i = ByteArray.readU16bit(code, pos + 1);
        String desc = this.cpool.getInterfaceMethodrefType(i);
        checkParamTypes(desc, 1);
        String className = this.cpool.getInterfaceMethodrefClassName(i);
        TypeData[] typeDataArr = this.stackTypes;
        int i2 = this.stackTop - 1;
        this.stackTop = i2;
        typeDataArr[i2].setType(className, this.classPool);
        pushMemberType(desc);
        return 5;
    }

    private int doInvokeDynamic(int pos, byte[] code) throws BadBytecode {
        String desc = this.cpool.getInvokeDynamicType(ByteArray.readU16bit(code, pos + 1));
        checkParamTypes(desc, 1);
        pushMemberType(desc);
        return 5;
    }

    private void pushMemberType(String descriptor) {
        int top = 0;
        if (descriptor.charAt(0) != '(' || (top = descriptor.indexOf(41) + 1) >= 1) {
            TypeData[] types = this.stackTypes;
            int index = this.stackTop;
            switch (descriptor.charAt(top)) {
                case 'D':
                    types[index] = DOUBLE;
                    types[index + 1] = TOP;
                    this.stackTop += 2;
                    return;
                case Opcode.FSTORE_3:
                    types[index] = FLOAT;
                    break;
                case Opcode.DSTORE_3:
                    types[index] = LONG;
                    types[index + 1] = TOP;
                    this.stackTop += 2;
                    return;
                case 'L':
                    types[index] = new TypeData.ClassName(getFieldClassName(descriptor, top));
                    break;
                case Opcode.SASTORE:
                    return;
                case Opcode.DUP_X2:
                    types[index] = new TypeData.ClassName(descriptor.substring(top));
                    break;
                default:
                    types[index] = INTEGER;
                    break;
            }
            this.stackTop++;
            return;
        }
        throw new IndexOutOfBoundsException("bad descriptor: " + descriptor);
    }

    private static String getFieldClassName(String desc, int index) {
        return desc.substring(index + 1, desc.length() - 1).replace('/', '.');
    }

    private void checkParamTypes(String desc, int i) throws BadBytecode {
        int k;
        char c = desc.charAt(i);
        if (c != ')') {
            int k2 = i;
            boolean array = false;
            while (c == '[') {
                array = true;
                k2++;
                c = desc.charAt(k2);
            }
            if (c == 'L') {
                k = desc.indexOf(59, k2) + 1;
                if (k <= 0) {
                    throw new IndexOutOfBoundsException("bad descriptor");
                }
            } else {
                k = k2 + 1;
            }
            checkParamTypes(desc, k);
            if (array || !(c == 'J' || c == 'D')) {
                this.stackTop--;
            } else {
                this.stackTop -= 2;
            }
            if (array) {
                this.stackTypes[this.stackTop].setType(desc.substring(i, k), this.classPool);
            } else if (c == 'L') {
                this.stackTypes[this.stackTop].setType(desc.substring(i + 1, k - 1).replace('/', '.'), this.classPool);
            }
        }
    }
}
