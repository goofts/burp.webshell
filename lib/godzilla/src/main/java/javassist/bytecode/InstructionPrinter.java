package javassist.bytecode;

import java.io.PrintStream;
import javassist.CtMethod;

public class InstructionPrinter implements Opcode {
    private static final String[] opcodes = Mnemonic.OPCODE;
    private final PrintStream stream;

    public InstructionPrinter(PrintStream stream2) {
        this.stream = stream2;
    }

    public static void print(CtMethod method, PrintStream stream2) {
        new InstructionPrinter(stream2).print(method);
    }

    public void print(CtMethod method) {
        MethodInfo info = method.getMethodInfo2();
        ConstPool pool = info.getConstPool();
        CodeAttribute code = info.getCodeAttribute();
        if (code != null) {
            CodeIterator iterator = code.iterator();
            while (iterator.hasNext()) {
                try {
                    int pos = iterator.next();
                    this.stream.println(pos + ": " + instructionString(iterator, pos, pool));
                } catch (BadBytecode e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public static String instructionString(CodeIterator iter, int pos, ConstPool pool) {
        int opcode = iter.byteAt(pos);
        if (opcode > opcodes.length || opcode < 0) {
            throw new IllegalArgumentException("Invalid opcode, opcode: " + opcode + " pos: " + pos);
        }
        String opstring = opcodes[opcode];
        switch (opcode) {
            case 16:
                return opstring + " " + iter.byteAt(pos + 1);
            case 17:
                return opstring + " " + iter.s16bitAt(pos + 1);
            case 18:
                return opstring + " " + ldc(pool, iter.byteAt(pos + 1));
            case 19:
            case 20:
                return opstring + " " + ldc(pool, iter.u16bitAt(pos + 1));
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 54:
            case 55:
            case Opcode.FSTORE /*{ENCODED_INT: 56}*/:
            case Opcode.DSTORE /*{ENCODED_INT: 57}*/:
            case Opcode.ASTORE /*{ENCODED_INT: 58}*/:
                return opstring + " " + iter.byteAt(pos + 1);
            case Opcode.IINC /*{ENCODED_INT: 132}*/:
                return opstring + " " + iter.byteAt(pos + 1) + ", " + iter.signedByteAt(pos + 2);
            case Opcode.IFEQ /*{ENCODED_INT: 153}*/:
            case Opcode.IFNE /*{ENCODED_INT: 154}*/:
            case Opcode.IFLT /*{ENCODED_INT: 155}*/:
            case Opcode.IFGE /*{ENCODED_INT: 156}*/:
            case Opcode.IFGT /*{ENCODED_INT: 157}*/:
            case Opcode.IFLE /*{ENCODED_INT: 158}*/:
            case Opcode.IF_ICMPEQ /*{ENCODED_INT: 159}*/:
            case Opcode.IF_ICMPNE /*{ENCODED_INT: 160}*/:
            case Opcode.IF_ICMPLT /*{ENCODED_INT: 161}*/:
            case Opcode.IF_ICMPGE /*{ENCODED_INT: 162}*/:
            case Opcode.IF_ICMPGT /*{ENCODED_INT: 163}*/:
            case Opcode.IF_ICMPLE /*{ENCODED_INT: 164}*/:
            case Opcode.IF_ACMPEQ /*{ENCODED_INT: 165}*/:
            case Opcode.IF_ACMPNE /*{ENCODED_INT: 166}*/:
            case Opcode.IFNULL /*{ENCODED_INT: 198}*/:
            case Opcode.IFNONNULL /*{ENCODED_INT: 199}*/:
                return opstring + " " + (iter.s16bitAt(pos + 1) + pos);
            case Opcode.GOTO /*{ENCODED_INT: 167}*/:
            case Opcode.JSR /*{ENCODED_INT: 168}*/:
                return opstring + " " + (iter.s16bitAt(pos + 1) + pos);
            case Opcode.RET /*{ENCODED_INT: 169}*/:
                return opstring + " " + iter.byteAt(pos + 1);
            case Opcode.TABLESWITCH /*{ENCODED_INT: 170}*/:
                return tableSwitch(iter, pos);
            case Opcode.LOOKUPSWITCH /*{ENCODED_INT: 171}*/:
                return lookupSwitch(iter, pos);
            case Opcode.GETSTATIC /*{ENCODED_INT: 178}*/:
            case Opcode.PUTSTATIC /*{ENCODED_INT: 179}*/:
            case Opcode.GETFIELD /*{ENCODED_INT: 180}*/:
            case Opcode.PUTFIELD /*{ENCODED_INT: 181}*/:
                return opstring + " " + fieldInfo(pool, iter.u16bitAt(pos + 1));
            case Opcode.INVOKEVIRTUAL /*{ENCODED_INT: 182}*/:
            case Opcode.INVOKESPECIAL /*{ENCODED_INT: 183}*/:
            case Opcode.INVOKESTATIC /*{ENCODED_INT: 184}*/:
                return opstring + " " + methodInfo(pool, iter.u16bitAt(pos + 1));
            case Opcode.INVOKEINTERFACE /*{ENCODED_INT: 185}*/:
                return opstring + " " + interfaceMethodInfo(pool, iter.u16bitAt(pos + 1));
            case Opcode.INVOKEDYNAMIC /*{ENCODED_INT: 186}*/:
                return opstring + " " + iter.u16bitAt(pos + 1);
            case Opcode.NEW /*{ENCODED_INT: 187}*/:
                return opstring + " " + classInfo(pool, iter.u16bitAt(pos + 1));
            case Opcode.NEWARRAY /*{ENCODED_INT: 188}*/:
                return opstring + " " + arrayInfo(iter.byteAt(pos + 1));
            case Opcode.ANEWARRAY /*{ENCODED_INT: 189}*/:
            case Opcode.CHECKCAST /*{ENCODED_INT: 192}*/:
                return opstring + " " + classInfo(pool, iter.u16bitAt(pos + 1));
            case Opcode.WIDE /*{ENCODED_INT: 196}*/:
                return wide(iter, pos);
            case Opcode.MULTIANEWARRAY /*{ENCODED_INT: 197}*/:
                return opstring + " " + classInfo(pool, iter.u16bitAt(pos + 1));
            case Opcode.GOTO_W /*{ENCODED_INT: 200}*/:
            case Opcode.JSR_W /*{ENCODED_INT: 201}*/:
                return opstring + " " + (iter.s32bitAt(pos + 1) + pos);
            default:
                return opstring;
        }
    }

    private static String wide(CodeIterator iter, int pos) {
        int opcode = iter.byteAt(pos + 1);
        int index = iter.u16bitAt(pos + 2);
        switch (opcode) {
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 54:
            case 55:
            case Opcode.FSTORE /*{ENCODED_INT: 56}*/:
            case Opcode.DSTORE /*{ENCODED_INT: 57}*/:
            case Opcode.ASTORE /*{ENCODED_INT: 58}*/:
            case Opcode.IINC /*{ENCODED_INT: 132}*/:
            case Opcode.RET /*{ENCODED_INT: 169}*/:
                return opcodes[opcode] + " " + index;
            default:
                throw new RuntimeException("Invalid WIDE operand");
        }
    }

    private static String arrayInfo(int type) {
        switch (type) {
            case 4:
                return "boolean";
            case 5:
                return "char";
            case 6:
                return "float";
            case 7:
                return "double";
            case 8:
                return "byte";
            case 9:
                return "short";
            case 10:
                return "int";
            case 11:
                return "long";
            default:
                throw new RuntimeException("Invalid array type");
        }
    }

    private static String classInfo(ConstPool pool, int index) {
        return "#" + index + " = Class " + pool.getClassInfo(index);
    }

    private static String interfaceMethodInfo(ConstPool pool, int index) {
        return "#" + index + " = Method " + pool.getInterfaceMethodrefClassName(index) + "." + pool.getInterfaceMethodrefName(index) + "(" + pool.getInterfaceMethodrefType(index) + ")";
    }

    private static String methodInfo(ConstPool pool, int index) {
        return "#" + index + " = Method " + pool.getMethodrefClassName(index) + "." + pool.getMethodrefName(index) + "(" + pool.getMethodrefType(index) + ")";
    }

    private static String fieldInfo(ConstPool pool, int index) {
        return "#" + index + " = Field " + pool.getFieldrefClassName(index) + "." + pool.getFieldrefName(index) + "(" + pool.getFieldrefType(index) + ")";
    }

    private static String lookupSwitch(CodeIterator iter, int pos) {
        StringBuffer buffer = new StringBuffer("lookupswitch {\n");
        int index = (pos & -4) + 4;
        buffer.append("\t\tdefault: ").append(iter.s32bitAt(index) + pos).append("\n");
        int index2 = index + 4;
        int index3 = index2 + 4;
        int end = (iter.s32bitAt(index2) * 8) + index3;
        while (index3 < end) {
            int match = iter.s32bitAt(index3);
            buffer.append("\t\t").append(match).append(": ").append(iter.s32bitAt(index3 + 4) + pos).append("\n");
            index3 += 8;
        }
        buffer.setCharAt(buffer.length() - 1, '}');
        return buffer.toString();
    }

    private static String tableSwitch(CodeIterator iter, int pos) {
        StringBuffer buffer = new StringBuffer("tableswitch {\n");
        int index = (pos & -4) + 4;
        buffer.append("\t\tdefault: ").append(iter.s32bitAt(index) + pos).append("\n");
        int index2 = index + 4;
        int low = iter.s32bitAt(index2);
        int index3 = index2 + 4;
        int index4 = index3 + 4;
        int end = (((iter.s32bitAt(index3) - low) + 1) * 4) + index4;
        int key = low;
        while (index4 < end) {
            buffer.append("\t\t").append(key).append(": ").append(iter.s32bitAt(index4) + pos).append("\n");
            index4 += 4;
            key++;
        }
        buffer.setCharAt(buffer.length() - 1, '}');
        return buffer.toString();
    }

    private static String ldc(ConstPool pool, int index) {
        int tag = pool.getTag(index);
        switch (tag) {
            case 3:
                return "#" + index + " = int " + pool.getIntegerInfo(index);
            case 4:
                return "#" + index + " = float " + pool.getFloatInfo(index);
            case 5:
                return "#" + index + " = long " + pool.getLongInfo(index);
            case 6:
                return "#" + index + " = double " + pool.getDoubleInfo(index);
            case 7:
                return classInfo(pool, index);
            case 8:
                return "#" + index + " = \"" + pool.getStringInfo(index) + "\"";
            default:
                throw new RuntimeException("bad LDC: " + tag);
        }
    }
}
