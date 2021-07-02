package javassist.bytecode.analysis;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;

public class Executor implements Opcode {
    private final Type CLASS_TYPE;
    private final Type STRING_TYPE;
    private final Type THROWABLE_TYPE;
    private final ClassPool classPool;
    private final ConstPool constPool;
    private int lastPos;

    public Executor(ClassPool classPool2, ConstPool constPool2) {
        this.constPool = constPool2;
        this.classPool = classPool2;
        try {
            this.STRING_TYPE = getType("java.lang.String");
            this.CLASS_TYPE = getType("java.lang.Class");
            this.THROWABLE_TYPE = getType("java.lang.Throwable");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void execute(MethodInfo method, int pos, CodeIterator iter, Frame frame, Subroutine subroutine) throws BadBytecode {
        this.lastPos = pos;
        int opcode = iter.byteAt(pos);
        switch (opcode) {
            case 0:
            case Opcode.GOTO:
            case Opcode.RETURN:
            case Opcode.GOTO_W:
            default:
                return;
            case 1:
                frame.push(Type.UNINIT);
                return;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
                frame.push(Type.INTEGER);
                return;
            case 9:
            case 10:
                frame.push(Type.LONG);
                frame.push(Type.TOP);
                return;
            case 11:
            case 12:
            case 13:
                frame.push(Type.FLOAT);
                return;
            case 14:
            case 15:
                frame.push(Type.DOUBLE);
                frame.push(Type.TOP);
                return;
            case 16:
            case 17:
                frame.push(Type.INTEGER);
                return;
            case 18:
                evalLDC(iter.byteAt(pos + 1), frame);
                return;
            case 19:
            case 20:
                evalLDC(iter.u16bitAt(pos + 1), frame);
                return;
            case 21:
                evalLoad(Type.INTEGER, iter.byteAt(pos + 1), frame, subroutine);
                return;
            case 22:
                evalLoad(Type.LONG, iter.byteAt(pos + 1), frame, subroutine);
                return;
            case 23:
                evalLoad(Type.FLOAT, iter.byteAt(pos + 1), frame, subroutine);
                return;
            case 24:
                evalLoad(Type.DOUBLE, iter.byteAt(pos + 1), frame, subroutine);
                return;
            case 25:
                evalLoad(Type.OBJECT, iter.byteAt(pos + 1), frame, subroutine);
                return;
            case 26:
            case 27:
            case Opcode.ILOAD_2:
            case Opcode.ILOAD_3:
                evalLoad(Type.INTEGER, opcode - 26, frame, subroutine);
                return;
            case Opcode.LLOAD_0:
            case Opcode.LLOAD_1:
            case 32:
            case Opcode.LLOAD_3:
                evalLoad(Type.LONG, opcode - 30, frame, subroutine);
                return;
            case Opcode.FLOAD_0:
            case 35:
            case Opcode.FLOAD_2:
            case Opcode.FLOAD_3:
                evalLoad(Type.FLOAT, opcode - 34, frame, subroutine);
                return;
            case Opcode.DLOAD_0:
            case Opcode.DLOAD_1:
            case Opcode.DLOAD_2:
            case Opcode.DLOAD_3:
                evalLoad(Type.DOUBLE, opcode - 38, frame, subroutine);
                return;
            case Opcode.ALOAD_0:
            case Opcode.ALOAD_1:
            case Opcode.ALOAD_2:
            case 45:
                evalLoad(Type.OBJECT, opcode - 42, frame, subroutine);
                return;
            case 46:
                evalArrayLoad(Type.INTEGER, frame);
                return;
            case 47:
                evalArrayLoad(Type.LONG, frame);
                return;
            case 48:
                evalArrayLoad(Type.FLOAT, frame);
                return;
            case 49:
                evalArrayLoad(Type.DOUBLE, frame);
                return;
            case 50:
                evalArrayLoad(Type.OBJECT, frame);
                return;
            case 51:
            case 52:
            case 53:
                evalArrayLoad(Type.INTEGER, frame);
                return;
            case 54:
                evalStore(Type.INTEGER, iter.byteAt(pos + 1), frame, subroutine);
                return;
            case 55:
                evalStore(Type.LONG, iter.byteAt(pos + 1), frame, subroutine);
                return;
            case Opcode.FSTORE:
                evalStore(Type.FLOAT, iter.byteAt(pos + 1), frame, subroutine);
                return;
            case Opcode.DSTORE:
                evalStore(Type.DOUBLE, iter.byteAt(pos + 1), frame, subroutine);
                return;
            case Opcode.ASTORE:
                evalStore(Type.OBJECT, iter.byteAt(pos + 1), frame, subroutine);
                return;
            case Opcode.ISTORE_0:
            case Opcode.ISTORE_1:
            case Opcode.ISTORE_2:
            case Opcode.ISTORE_3:
                evalStore(Type.INTEGER, opcode - 59, frame, subroutine);
                return;
            case Opcode.LSTORE_0:
            case 64:
            case 65:
            case 66:
                evalStore(Type.LONG, opcode - 63, frame, subroutine);
                return;
            case 67:
            case 68:
            case 69:
            case Opcode.FSTORE_3:
                evalStore(Type.FLOAT, opcode - 67, frame, subroutine);
                return;
            case Opcode.DSTORE_0:
            case Opcode.DSTORE_1:
            case Opcode.DSTORE_2:
            case Opcode.DSTORE_3:
                evalStore(Type.DOUBLE, opcode - 71, frame, subroutine);
                return;
            case Opcode.ASTORE_0:
            case 76:
            case Opcode.ASTORE_2:
            case Opcode.ASTORE_3:
                evalStore(Type.OBJECT, opcode - 75, frame, subroutine);
                return;
            case Opcode.IASTORE:
                evalArrayStore(Type.INTEGER, frame);
                return;
            case Opcode.LASTORE:
                evalArrayStore(Type.LONG, frame);
                return;
            case Opcode.FASTORE:
                evalArrayStore(Type.FLOAT, frame);
                return;
            case Opcode.DASTORE:
                evalArrayStore(Type.DOUBLE, frame);
                return;
            case Opcode.AASTORE:
                evalArrayStore(Type.OBJECT, frame);
                return;
            case Opcode.BASTORE:
            case Opcode.CASTORE:
            case Opcode.SASTORE:
                evalArrayStore(Type.INTEGER, frame);
                return;
            case Opcode.POP:
                if (frame.pop() == Type.TOP) {
                    throw new BadBytecode("POP can not be used with a category 2 value, pos = " + pos);
                }
                return;
            case Opcode.POP2:
                frame.pop();
                frame.pop();
                return;
            case Opcode.DUP:
                if (frame.peek() == Type.TOP) {
                    throw new BadBytecode("DUP can not be used with a category 2 value, pos = " + pos);
                }
                frame.push(frame.peek());
                return;
            case Opcode.DUP_X1:
            case Opcode.DUP_X2:
                Type type = frame.peek();
                if (type == Type.TOP) {
                    throw new BadBytecode("DUP can not be used with a category 2 value, pos = " + pos);
                }
                int end = frame.getTopIndex();
                int insert = (end - (opcode - 90)) - 1;
                frame.push(type);
                while (end > insert) {
                    frame.setStack(end, frame.getStack(end - 1));
                    end--;
                }
                frame.setStack(insert, type);
                return;
            case Opcode.DUP2:
                frame.push(frame.getStack(frame.getTopIndex() - 1));
                frame.push(frame.getStack(frame.getTopIndex() - 1));
                return;
            case Opcode.DUP2_X1:
            case Opcode.DUP2_X2:
                int end2 = frame.getTopIndex();
                int insert2 = (end2 - (opcode - 93)) - 1;
                Type type1 = frame.getStack(frame.getTopIndex() - 1);
                Type type2 = frame.peek();
                frame.push(type1);
                frame.push(type2);
                while (end2 > insert2) {
                    frame.setStack(end2, frame.getStack(end2 - 2));
                    end2--;
                }
                frame.setStack(insert2, type2);
                frame.setStack(insert2 - 1, type1);
                return;
            case Opcode.SWAP:
                Type type12 = frame.pop();
                Type type22 = frame.pop();
                if (type12.getSize() == 2 || type22.getSize() == 2) {
                    throw new BadBytecode("Swap can not be used with category 2 values, pos = " + pos);
                }
                frame.push(type12);
                frame.push(type22);
                return;
            case Opcode.IADD:
                evalBinaryMath(Type.INTEGER, frame);
                return;
            case Opcode.LADD:
                evalBinaryMath(Type.LONG, frame);
                return;
            case Opcode.FADD:
                evalBinaryMath(Type.FLOAT, frame);
                return;
            case Opcode.DADD:
                evalBinaryMath(Type.DOUBLE, frame);
                return;
            case 100:
                evalBinaryMath(Type.INTEGER, frame);
                return;
            case 101:
                evalBinaryMath(Type.LONG, frame);
                return;
            case 102:
                evalBinaryMath(Type.FLOAT, frame);
                return;
            case 103:
                evalBinaryMath(Type.DOUBLE, frame);
                return;
            case 104:
                evalBinaryMath(Type.INTEGER, frame);
                return;
            case 105:
                evalBinaryMath(Type.LONG, frame);
                return;
            case 106:
                evalBinaryMath(Type.FLOAT, frame);
                return;
            case 107:
                evalBinaryMath(Type.DOUBLE, frame);
                return;
            case Opcode.IDIV:
                evalBinaryMath(Type.INTEGER, frame);
                return;
            case Opcode.LDIV:
                evalBinaryMath(Type.LONG, frame);
                return;
            case Opcode.FDIV:
                evalBinaryMath(Type.FLOAT, frame);
                return;
            case Opcode.DDIV:
                evalBinaryMath(Type.DOUBLE, frame);
                return;
            case Opcode.IREM:
                evalBinaryMath(Type.INTEGER, frame);
                return;
            case Opcode.LREM:
                evalBinaryMath(Type.LONG, frame);
                return;
            case Opcode.FREM:
                evalBinaryMath(Type.FLOAT, frame);
                return;
            case Opcode.DREM:
                evalBinaryMath(Type.DOUBLE, frame);
                return;
            case Opcode.INEG:
                verifyAssignable(Type.INTEGER, simplePeek(frame));
                return;
            case Opcode.LNEG:
                verifyAssignable(Type.LONG, simplePeek(frame));
                return;
            case Opcode.FNEG:
                verifyAssignable(Type.FLOAT, simplePeek(frame));
                return;
            case Opcode.DNEG:
                verifyAssignable(Type.DOUBLE, simplePeek(frame));
                return;
            case Opcode.ISHL:
                evalShift(Type.INTEGER, frame);
                return;
            case Opcode.LSHL:
                evalShift(Type.LONG, frame);
                return;
            case Opcode.ISHR:
                evalShift(Type.INTEGER, frame);
                return;
            case Opcode.LSHR:
                evalShift(Type.LONG, frame);
                return;
            case Opcode.IUSHR:
                evalShift(Type.INTEGER, frame);
                return;
            case Opcode.LUSHR:
                evalShift(Type.LONG, frame);
                return;
            case Opcode.IAND:
                evalBinaryMath(Type.INTEGER, frame);
                return;
            case Opcode.LAND:
                evalBinaryMath(Type.LONG, frame);
                return;
            case 128:
                evalBinaryMath(Type.INTEGER, frame);
                return;
            case Opcode.LOR:
                evalBinaryMath(Type.LONG, frame);
                return;
            case Opcode.IXOR:
                evalBinaryMath(Type.INTEGER, frame);
                return;
            case Opcode.LXOR:
                evalBinaryMath(Type.LONG, frame);
                return;
            case Opcode.IINC:
                int index = iter.byteAt(pos + 1);
                verifyAssignable(Type.INTEGER, frame.getLocal(index));
                access(index, Type.INTEGER, subroutine);
                return;
            case Opcode.I2L:
                verifyAssignable(Type.INTEGER, simplePop(frame));
                simplePush(Type.LONG, frame);
                return;
            case Opcode.I2F:
                verifyAssignable(Type.INTEGER, simplePop(frame));
                simplePush(Type.FLOAT, frame);
                return;
            case Opcode.I2D:
                verifyAssignable(Type.INTEGER, simplePop(frame));
                simplePush(Type.DOUBLE, frame);
                return;
            case Opcode.L2I:
                verifyAssignable(Type.LONG, simplePop(frame));
                simplePush(Type.INTEGER, frame);
                return;
            case Opcode.L2F:
                verifyAssignable(Type.LONG, simplePop(frame));
                simplePush(Type.FLOAT, frame);
                return;
            case Opcode.L2D:
                verifyAssignable(Type.LONG, simplePop(frame));
                simplePush(Type.DOUBLE, frame);
                return;
            case Opcode.F2I:
                verifyAssignable(Type.FLOAT, simplePop(frame));
                simplePush(Type.INTEGER, frame);
                return;
            case Opcode.F2L:
                verifyAssignable(Type.FLOAT, simplePop(frame));
                simplePush(Type.LONG, frame);
                return;
            case Opcode.F2D:
                verifyAssignable(Type.FLOAT, simplePop(frame));
                simplePush(Type.DOUBLE, frame);
                return;
            case Opcode.D2I:
                verifyAssignable(Type.DOUBLE, simplePop(frame));
                simplePush(Type.INTEGER, frame);
                return;
            case Opcode.D2L:
                verifyAssignable(Type.DOUBLE, simplePop(frame));
                simplePush(Type.LONG, frame);
                return;
            case Opcode.D2F:
                verifyAssignable(Type.DOUBLE, simplePop(frame));
                simplePush(Type.FLOAT, frame);
                return;
            case Opcode.I2B:
            case Opcode.I2C:
            case Opcode.I2S:
                verifyAssignable(Type.INTEGER, frame.peek());
                return;
            case Opcode.LCMP:
                verifyAssignable(Type.LONG, simplePop(frame));
                verifyAssignable(Type.LONG, simplePop(frame));
                frame.push(Type.INTEGER);
                return;
            case Opcode.FCMPL:
            case Opcode.FCMPG:
                verifyAssignable(Type.FLOAT, simplePop(frame));
                verifyAssignable(Type.FLOAT, simplePop(frame));
                frame.push(Type.INTEGER);
                return;
            case Opcode.DCMPL:
            case Opcode.DCMPG:
                verifyAssignable(Type.DOUBLE, simplePop(frame));
                verifyAssignable(Type.DOUBLE, simplePop(frame));
                frame.push(Type.INTEGER);
                return;
            case Opcode.IFEQ:
            case Opcode.IFNE:
            case Opcode.IFLT:
            case Opcode.IFGE:
            case Opcode.IFGT:
            case Opcode.IFLE:
                verifyAssignable(Type.INTEGER, simplePop(frame));
                return;
            case Opcode.IF_ICMPEQ:
            case Opcode.IF_ICMPNE:
            case Opcode.IF_ICMPLT:
            case Opcode.IF_ICMPGE:
            case Opcode.IF_ICMPGT:
            case Opcode.IF_ICMPLE:
                verifyAssignable(Type.INTEGER, simplePop(frame));
                verifyAssignable(Type.INTEGER, simplePop(frame));
                return;
            case Opcode.IF_ACMPEQ:
            case Opcode.IF_ACMPNE:
                verifyAssignable(Type.OBJECT, simplePop(frame));
                verifyAssignable(Type.OBJECT, simplePop(frame));
                return;
            case Opcode.JSR:
                frame.push(Type.RETURN_ADDRESS);
                return;
            case Opcode.RET:
                verifyAssignable(Type.RETURN_ADDRESS, frame.getLocal(iter.byteAt(pos + 1)));
                return;
            case Opcode.TABLESWITCH:
            case Opcode.LOOKUPSWITCH:
            case Opcode.IRETURN:
                verifyAssignable(Type.INTEGER, simplePop(frame));
                return;
            case Opcode.LRETURN:
                verifyAssignable(Type.LONG, simplePop(frame));
                return;
            case Opcode.FRETURN:
                verifyAssignable(Type.FLOAT, simplePop(frame));
                return;
            case Opcode.DRETURN:
                verifyAssignable(Type.DOUBLE, simplePop(frame));
                return;
            case Opcode.ARETURN:
                try {
                    verifyAssignable(Type.get(Descriptor.getReturnType(method.getDescriptor(), this.classPool)), simplePop(frame));
                    return;
                } catch (NotFoundException e) {
                    throw new RuntimeException(e);
                }
            case Opcode.GETSTATIC:
                evalGetField(opcode, iter.u16bitAt(pos + 1), frame);
                return;
            case Opcode.PUTSTATIC:
                evalPutField(opcode, iter.u16bitAt(pos + 1), frame);
                return;
            case Opcode.GETFIELD:
                evalGetField(opcode, iter.u16bitAt(pos + 1), frame);
                return;
            case Opcode.PUTFIELD:
                evalPutField(opcode, iter.u16bitAt(pos + 1), frame);
                return;
            case Opcode.INVOKEVIRTUAL:
            case Opcode.INVOKESPECIAL:
            case Opcode.INVOKESTATIC:
                evalInvokeMethod(opcode, iter.u16bitAt(pos + 1), frame);
                return;
            case Opcode.INVOKEINTERFACE:
                evalInvokeIntfMethod(opcode, iter.u16bitAt(pos + 1), frame);
                return;
            case Opcode.INVOKEDYNAMIC:
                evalInvokeDynamic(opcode, iter.u16bitAt(pos + 1), frame);
                return;
            case Opcode.NEW:
                frame.push(resolveClassInfo(this.constPool.getClassInfo(iter.u16bitAt(pos + 1))));
                return;
            case Opcode.NEWARRAY:
                evalNewArray(pos, iter, frame);
                return;
            case Opcode.ANEWARRAY:
                evalNewObjectArray(pos, iter, frame);
                return;
            case Opcode.ARRAYLENGTH:
                Type array = simplePop(frame);
                if (array.isArray() || array == Type.UNINIT) {
                    frame.push(Type.INTEGER);
                    return;
                }
                throw new BadBytecode("Array length passed a non-array [pos = " + pos + "]: " + array);
            case Opcode.ATHROW:
                verifyAssignable(this.THROWABLE_TYPE, simplePop(frame));
                return;
            case Opcode.CHECKCAST:
                verifyAssignable(Type.OBJECT, simplePop(frame));
                frame.push(typeFromDesc(this.constPool.getClassInfoByDescriptor(iter.u16bitAt(pos + 1))));
                return;
            case Opcode.INSTANCEOF:
                verifyAssignable(Type.OBJECT, simplePop(frame));
                frame.push(Type.INTEGER);
                return;
            case Opcode.MONITORENTER:
            case Opcode.MONITOREXIT:
                verifyAssignable(Type.OBJECT, simplePop(frame));
                return;
            case Opcode.WIDE:
                evalWide(pos, iter, frame, subroutine);
                return;
            case Opcode.MULTIANEWARRAY:
                evalNewObjectArray(pos, iter, frame);
                return;
            case Opcode.IFNULL:
            case Opcode.IFNONNULL:
                verifyAssignable(Type.OBJECT, simplePop(frame));
                return;
            case Opcode.JSR_W:
                frame.push(Type.RETURN_ADDRESS);
                return;
        }
    }

    private Type zeroExtend(Type type) {
        if (type == Type.SHORT || type == Type.BYTE || type == Type.CHAR || type == Type.BOOLEAN) {
            return Type.INTEGER;
        }
        return type;
    }

    private void evalArrayLoad(Type expectedComponent, Frame frame) throws BadBytecode {
        Type index = frame.pop();
        Type array = frame.pop();
        if (array == Type.UNINIT) {
            verifyAssignable(Type.INTEGER, index);
            if (expectedComponent == Type.OBJECT) {
                simplePush(Type.UNINIT, frame);
            } else {
                simplePush(expectedComponent, frame);
            }
        } else {
            Type component = array.getComponent();
            if (component == null) {
                throw new BadBytecode("Not an array! [pos = " + this.lastPos + "]: " + component);
            }
            Type component2 = zeroExtend(component);
            verifyAssignable(expectedComponent, component2);
            verifyAssignable(Type.INTEGER, index);
            simplePush(component2, frame);
        }
    }

    private void evalArrayStore(Type expectedComponent, Frame frame) throws BadBytecode {
        Type value = simplePop(frame);
        Type index = frame.pop();
        Type array = frame.pop();
        if (array == Type.UNINIT) {
            verifyAssignable(Type.INTEGER, index);
            return;
        }
        Type component = array.getComponent();
        if (component == null) {
            throw new BadBytecode("Not an array! [pos = " + this.lastPos + "]: " + component);
        }
        Type component2 = zeroExtend(component);
        verifyAssignable(expectedComponent, component2);
        verifyAssignable(Type.INTEGER, index);
        if (expectedComponent == Type.OBJECT) {
            verifyAssignable(expectedComponent, value);
        } else {
            verifyAssignable(component2, value);
        }
    }

    private void evalBinaryMath(Type expected, Frame frame) throws BadBytecode {
        Type value2 = simplePop(frame);
        Type value1 = simplePop(frame);
        verifyAssignable(expected, value2);
        verifyAssignable(expected, value1);
        simplePush(value1, frame);
    }

    private void evalGetField(int opcode, int index, Frame frame) throws BadBytecode {
        Type type = zeroExtend(typeFromDesc(this.constPool.getFieldrefType(index)));
        if (opcode == 180) {
            verifyAssignable(resolveClassInfo(this.constPool.getFieldrefClassName(index)), simplePop(frame));
        }
        simplePush(type, frame);
    }

    private void evalInvokeIntfMethod(int opcode, int index, Frame frame) throws BadBytecode {
        String desc = this.constPool.getInterfaceMethodrefType(index);
        Type[] types = paramTypesFromDesc(desc);
        int i = types.length;
        while (i > 0) {
            i--;
            verifyAssignable(zeroExtend(types[i]), simplePop(frame));
        }
        verifyAssignable(resolveClassInfo(this.constPool.getInterfaceMethodrefClassName(index)), simplePop(frame));
        Type returnType = returnTypeFromDesc(desc);
        if (returnType != Type.VOID) {
            simplePush(zeroExtend(returnType), frame);
        }
    }

    private void evalInvokeMethod(int opcode, int index, Frame frame) throws BadBytecode {
        String desc = this.constPool.getMethodrefType(index);
        Type[] types = paramTypesFromDesc(desc);
        int i = types.length;
        while (i > 0) {
            i--;
            verifyAssignable(zeroExtend(types[i]), simplePop(frame));
        }
        if (opcode != 184) {
            verifyAssignable(resolveClassInfo(this.constPool.getMethodrefClassName(index)), simplePop(frame));
        }
        Type returnType = returnTypeFromDesc(desc);
        if (returnType != Type.VOID) {
            simplePush(zeroExtend(returnType), frame);
        }
    }

    private void evalInvokeDynamic(int opcode, int index, Frame frame) throws BadBytecode {
        String desc = this.constPool.getInvokeDynamicType(index);
        Type[] types = paramTypesFromDesc(desc);
        int i = types.length;
        while (i > 0) {
            i--;
            verifyAssignable(zeroExtend(types[i]), simplePop(frame));
        }
        Type returnType = returnTypeFromDesc(desc);
        if (returnType != Type.VOID) {
            simplePush(zeroExtend(returnType), frame);
        }
    }

    private void evalLDC(int index, Frame frame) throws BadBytecode {
        Type type;
        int tag = this.constPool.getTag(index);
        switch (tag) {
            case 3:
                type = Type.INTEGER;
                break;
            case 4:
                type = Type.FLOAT;
                break;
            case 5:
                type = Type.LONG;
                break;
            case 6:
                type = Type.DOUBLE;
                break;
            case 7:
                type = this.CLASS_TYPE;
                break;
            case 8:
                type = this.STRING_TYPE;
                break;
            default:
                throw new BadBytecode("bad LDC [pos = " + this.lastPos + "]: " + tag);
        }
        simplePush(type, frame);
    }

    private void evalLoad(Type expected, int index, Frame frame, Subroutine subroutine) throws BadBytecode {
        Type type = frame.getLocal(index);
        verifyAssignable(expected, type);
        simplePush(type, frame);
        access(index, type, subroutine);
    }

    private void evalNewArray(int pos, CodeIterator iter, Frame frame) throws BadBytecode {
        Type type;
        verifyAssignable(Type.INTEGER, simplePop(frame));
        int typeInfo = iter.byteAt(pos + 1);
        switch (typeInfo) {
            case 4:
                type = getType("boolean[]");
                break;
            case 5:
                type = getType("char[]");
                break;
            case 6:
                type = getType("float[]");
                break;
            case 7:
                type = getType("double[]");
                break;
            case 8:
                type = getType("byte[]");
                break;
            case 9:
                type = getType("short[]");
                break;
            case 10:
                type = getType("int[]");
                break;
            case 11:
                type = getType("long[]");
                break;
            default:
                throw new BadBytecode("Invalid array type [pos = " + pos + "]: " + typeInfo);
        }
        frame.push(type);
    }

    private void evalNewObjectArray(int pos, CodeIterator iter, Frame frame) throws BadBytecode {
        int dimensions;
        String name = resolveClassInfo(this.constPool.getClassInfo(iter.u16bitAt(pos + 1))).getCtClass().getName();
        if (iter.byteAt(pos) == 197) {
            dimensions = iter.byteAt(pos + 3);
        } else {
            name = name + "[]";
            dimensions = 1;
        }
        while (true) {
            dimensions--;
            if (dimensions > 0) {
                verifyAssignable(Type.INTEGER, simplePop(frame));
            } else {
                simplePush(getType(name), frame);
                return;
            }
        }
    }

    private void evalPutField(int opcode, int index, Frame frame) throws BadBytecode {
        verifyAssignable(zeroExtend(typeFromDesc(this.constPool.getFieldrefType(index))), simplePop(frame));
        if (opcode == 181) {
            verifyAssignable(resolveClassInfo(this.constPool.getFieldrefClassName(index)), simplePop(frame));
        }
    }

    private void evalShift(Type expected, Frame frame) throws BadBytecode {
        Type value2 = simplePop(frame);
        Type value1 = simplePop(frame);
        verifyAssignable(Type.INTEGER, value2);
        verifyAssignable(expected, value1);
        simplePush(value1, frame);
    }

    private void evalStore(Type expected, int index, Frame frame, Subroutine subroutine) throws BadBytecode {
        Type type = simplePop(frame);
        if (!(expected == Type.OBJECT && type == Type.RETURN_ADDRESS)) {
            verifyAssignable(expected, type);
        }
        simpleSetLocal(index, type, frame);
        access(index, type, subroutine);
    }

    private void evalWide(int pos, CodeIterator iter, Frame frame, Subroutine subroutine) throws BadBytecode {
        int opcode = iter.byteAt(pos + 1);
        int index = iter.u16bitAt(pos + 2);
        switch (opcode) {
            case 21:
                evalLoad(Type.INTEGER, index, frame, subroutine);
                return;
            case 22:
                evalLoad(Type.LONG, index, frame, subroutine);
                return;
            case 23:
                evalLoad(Type.FLOAT, index, frame, subroutine);
                return;
            case 24:
                evalLoad(Type.DOUBLE, index, frame, subroutine);
                return;
            case 25:
                evalLoad(Type.OBJECT, index, frame, subroutine);
                return;
            case 54:
                evalStore(Type.INTEGER, index, frame, subroutine);
                return;
            case 55:
                evalStore(Type.LONG, index, frame, subroutine);
                return;
            case Opcode.FSTORE:
                evalStore(Type.FLOAT, index, frame, subroutine);
                return;
            case Opcode.DSTORE:
                evalStore(Type.DOUBLE, index, frame, subroutine);
                return;
            case Opcode.ASTORE:
                evalStore(Type.OBJECT, index, frame, subroutine);
                return;
            case Opcode.IINC:
                verifyAssignable(Type.INTEGER, frame.getLocal(index));
                return;
            case Opcode.RET:
                verifyAssignable(Type.RETURN_ADDRESS, frame.getLocal(index));
                return;
            default:
                throw new BadBytecode("Invalid WIDE operand [pos = " + pos + "]: " + opcode);
        }
    }

    private Type getType(String name) throws BadBytecode {
        try {
            return Type.get(this.classPool.get(name));
        } catch (NotFoundException e) {
            throw new BadBytecode("Could not find class [pos = " + this.lastPos + "]: " + name);
        }
    }

    private Type[] paramTypesFromDesc(String desc) throws BadBytecode {
        try {
            CtClass[] classes = Descriptor.getParameterTypes(desc, this.classPool);
            if (classes == null) {
                throw new BadBytecode("Could not obtain parameters for descriptor [pos = " + this.lastPos + "]: " + desc);
            }
            Type[] types = new Type[classes.length];
            for (int i = 0; i < types.length; i++) {
                types[i] = Type.get(classes[i]);
            }
            return types;
        } catch (NotFoundException e) {
            throw new BadBytecode("Could not find class in descriptor [pos = " + this.lastPos + "]: " + e.getMessage());
        }
    }

    private Type returnTypeFromDesc(String desc) throws BadBytecode {
        try {
            CtClass clazz = Descriptor.getReturnType(desc, this.classPool);
            if (clazz != null) {
                return Type.get(clazz);
            }
            throw new BadBytecode("Could not obtain return type for descriptor [pos = " + this.lastPos + "]: " + desc);
        } catch (NotFoundException e) {
            throw new BadBytecode("Could not find class in descriptor [pos = " + this.lastPos + "]: " + e.getMessage());
        }
    }

    private Type simplePeek(Frame frame) {
        Type type = frame.peek();
        return type == Type.TOP ? frame.getStack(frame.getTopIndex() - 1) : type;
    }

    private Type simplePop(Frame frame) {
        Type type = frame.pop();
        return type == Type.TOP ? frame.pop() : type;
    }

    private void simplePush(Type type, Frame frame) {
        frame.push(type);
        if (type.getSize() == 2) {
            frame.push(Type.TOP);
        }
    }

    private void access(int index, Type type, Subroutine subroutine) {
        if (subroutine != null) {
            subroutine.access(index);
            if (type.getSize() == 2) {
                subroutine.access(index + 1);
            }
        }
    }

    private void simpleSetLocal(int index, Type type, Frame frame) {
        frame.setLocal(index, type);
        if (type.getSize() == 2) {
            frame.setLocal(index + 1, Type.TOP);
        }
    }

    private Type resolveClassInfo(String info) throws BadBytecode {
        CtClass clazz;
        try {
            if (info.charAt(0) == '[') {
                clazz = Descriptor.toCtClass(info, this.classPool);
            } else {
                clazz = this.classPool.get(info);
            }
            if (clazz != null) {
                return Type.get(clazz);
            }
            throw new BadBytecode("Could not obtain type for descriptor [pos = " + this.lastPos + "]: " + info);
        } catch (NotFoundException e) {
            throw new BadBytecode("Could not find class in descriptor [pos = " + this.lastPos + "]: " + e.getMessage());
        }
    }

    private Type typeFromDesc(String desc) throws BadBytecode {
        try {
            CtClass clazz = Descriptor.toCtClass(desc, this.classPool);
            if (clazz != null) {
                return Type.get(clazz);
            }
            throw new BadBytecode("Could not obtain type for descriptor [pos = " + this.lastPos + "]: " + desc);
        } catch (NotFoundException e) {
            throw new BadBytecode("Could not find class in descriptor [pos = " + this.lastPos + "]: " + e.getMessage());
        }
    }

    private void verifyAssignable(Type expected, Type type) throws BadBytecode {
        if (!expected.isAssignableFrom(type)) {
            throw new BadBytecode("Expected type: " + expected + " Got: " + type + " [pos = " + this.lastPos + "]");
        }
    }
}
