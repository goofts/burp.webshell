package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CodeAttribute extends AttributeInfo implements Opcode {
    public static final String tag = "Code";
    private List<AttributeInfo> attributes = new ArrayList();
    private ExceptionTable exceptions;
    private int maxLocals;
    private int maxStack;

    public CodeAttribute(ConstPool cp, int stack, int locals, byte[] code, ExceptionTable etable) {
        super(cp, tag);
        this.maxStack = stack;
        this.maxLocals = locals;
        this.info = code;
        this.exceptions = etable;
    }

    private CodeAttribute(ConstPool cp, CodeAttribute src, Map<String, String> classnames) throws BadBytecode {
        super(cp, tag);
        this.maxStack = src.getMaxStack();
        this.maxLocals = src.getMaxLocals();
        this.exceptions = src.getExceptionTable().copy(cp, classnames);
        List<AttributeInfo> src_attr = src.getAttributes();
        int num = src_attr.size();
        for (int i = 0; i < num; i++) {
            this.attributes.add(src_attr.get(i).copy(cp, classnames));
        }
        this.info = src.copyCode(cp, classnames, this.exceptions, this);
    }

    CodeAttribute(ConstPool cp, int name_id, DataInputStream in) throws IOException {
        super(cp, name_id, (byte[]) null);
        in.readInt();
        this.maxStack = in.readUnsignedShort();
        this.maxLocals = in.readUnsignedShort();
        this.info = new byte[in.readInt()];
        in.readFully(this.info);
        this.exceptions = new ExceptionTable(cp, in);
        int num = in.readUnsignedShort();
        for (int i = 0; i < num; i++) {
            this.attributes.add(AttributeInfo.read(cp, in));
        }
    }

    @Override // javassist.bytecode.AttributeInfo
    public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) throws RuntimeCopyException {
        try {
            return new CodeAttribute(newCp, this, classnames);
        } catch (BadBytecode e) {
            throw new RuntimeCopyException("bad bytecode. fatal?");
        }
    }

    public static class RuntimeCopyException extends RuntimeException {
        private static final long serialVersionUID = 1;

        public RuntimeCopyException(String s) {
            super(s);
        }
    }

    @Override // javassist.bytecode.AttributeInfo
    public int length() {
        return this.info.length + 18 + (this.exceptions.size() * 8) + AttributeInfo.getLength(this.attributes);
    }

    /* access modifiers changed from: package-private */
    @Override // javassist.bytecode.AttributeInfo
    public void write(DataOutputStream out) throws IOException {
        out.writeShort(this.name);
        out.writeInt(length() - 6);
        out.writeShort(this.maxStack);
        out.writeShort(this.maxLocals);
        out.writeInt(this.info.length);
        out.write(this.info);
        this.exceptions.write(out);
        out.writeShort(this.attributes.size());
        AttributeInfo.writeAll(this.attributes, out);
    }

    @Override // javassist.bytecode.AttributeInfo
    public byte[] get() {
        throw new UnsupportedOperationException("CodeAttribute.get()");
    }

    @Override // javassist.bytecode.AttributeInfo
    public void set(byte[] newinfo) {
        throw new UnsupportedOperationException("CodeAttribute.set()");
    }

    /* access modifiers changed from: package-private */
    @Override // javassist.bytecode.AttributeInfo
    public void renameClass(String oldname, String newname) {
        AttributeInfo.renameClass(this.attributes, oldname, newname);
    }

    /* access modifiers changed from: package-private */
    @Override // javassist.bytecode.AttributeInfo
    public void renameClass(Map<String, String> classnames) {
        AttributeInfo.renameClass(this.attributes, classnames);
    }

    /* access modifiers changed from: package-private */
    @Override // javassist.bytecode.AttributeInfo
    public void getRefClasses(Map<String, String> classnames) {
        AttributeInfo.getRefClasses(this.attributes, classnames);
    }

    public String getDeclaringClass() {
        return getConstPool().getClassName();
    }

    public int getMaxStack() {
        return this.maxStack;
    }

    public void setMaxStack(int value) {
        this.maxStack = value;
    }

    public int computeMaxStack() throws BadBytecode {
        this.maxStack = new CodeAnalyzer(this).computeMaxStack();
        return this.maxStack;
    }

    public int getMaxLocals() {
        return this.maxLocals;
    }

    public void setMaxLocals(int value) {
        this.maxLocals = value;
    }

    public int getCodeLength() {
        return this.info.length;
    }

    public byte[] getCode() {
        return this.info;
    }

    /* access modifiers changed from: package-private */
    public void setCode(byte[] newinfo) {
        super.set(newinfo);
    }

    public CodeIterator iterator() {
        return new CodeIterator(this);
    }

    public ExceptionTable getExceptionTable() {
        return this.exceptions;
    }

    public List<AttributeInfo> getAttributes() {
        return this.attributes;
    }

    public AttributeInfo getAttribute(String name) {
        return AttributeInfo.lookup(this.attributes, name);
    }

    public void setAttribute(StackMapTable smt) {
        AttributeInfo.remove(this.attributes, StackMapTable.tag);
        if (smt != null) {
            this.attributes.add(smt);
        }
    }

    public void setAttribute(StackMap sm) {
        AttributeInfo.remove(this.attributes, StackMap.tag);
        if (sm != null) {
            this.attributes.add(sm);
        }
    }

    private byte[] copyCode(ConstPool destCp, Map<String, String> classnames, ExceptionTable etable, CodeAttribute destCa) throws BadBytecode {
        int len = getCodeLength();
        byte[] newCode = new byte[len];
        destCa.info = newCode;
        return LdcEntry.doit(newCode, copyCode(this.info, 0, len, getConstPool(), newCode, destCp, classnames), etable, destCa);
    }

    private static LdcEntry copyCode(byte[] code, int beginPos, int endPos, ConstPool srcCp, byte[] newcode, ConstPool destCp, Map<String, String> classnameMap) throws BadBytecode {
        LdcEntry ldcEntry = null;
        int i = beginPos;
        while (i < endPos) {
            int i2 = CodeIterator.nextOpcode(code, i);
            byte c = code[i];
            newcode[i] = c;
            switch (c & 255) {
                case 18:
                    int index = srcCp.copy(code[i + 1] & 255, destCp, classnameMap);
                    if (index < 256) {
                        newcode[i + 1] = (byte) index;
                        continue;
                    } else {
                        newcode[i] = 0;
                        newcode[i + 1] = 0;
                        LdcEntry ldc = new LdcEntry();
                        ldc.where = i;
                        ldc.index = index;
                        ldc.next = ldcEntry;
                        ldcEntry = ldc;
                    }
                    i = i2;
                case 19:
                case 20:
                case Opcode.GETSTATIC /*{ENCODED_INT: 178}*/:
                case Opcode.PUTSTATIC /*{ENCODED_INT: 179}*/:
                case Opcode.GETFIELD /*{ENCODED_INT: 180}*/:
                case Opcode.PUTFIELD /*{ENCODED_INT: 181}*/:
                case Opcode.INVOKEVIRTUAL /*{ENCODED_INT: 182}*/:
                case Opcode.INVOKESPECIAL /*{ENCODED_INT: 183}*/:
                case Opcode.INVOKESTATIC /*{ENCODED_INT: 184}*/:
                case Opcode.NEW /*{ENCODED_INT: 187}*/:
                case Opcode.ANEWARRAY /*{ENCODED_INT: 189}*/:
                case Opcode.CHECKCAST /*{ENCODED_INT: 192}*/:
                case Opcode.INSTANCEOF /*{ENCODED_INT: 193}*/:
                    copyConstPoolInfo(i + 1, code, srcCp, newcode, destCp, classnameMap);
                    continue;
                    i = i2;
                case Opcode.INVOKEINTERFACE /*{ENCODED_INT: 185}*/:
                    copyConstPoolInfo(i + 1, code, srcCp, newcode, destCp, classnameMap);
                    newcode[i + 3] = code[i + 3];
                    newcode[i + 4] = code[i + 4];
                    continue;
                    i = i2;
                case Opcode.INVOKEDYNAMIC /*{ENCODED_INT: 186}*/:
                    copyConstPoolInfo(i + 1, code, srcCp, newcode, destCp, classnameMap);
                    newcode[i + 3] = 0;
                    newcode[i + 4] = 0;
                    continue;
                    i = i2;
                case Opcode.MULTIANEWARRAY /*{ENCODED_INT: 197}*/:
                    copyConstPoolInfo(i + 1, code, srcCp, newcode, destCp, classnameMap);
                    newcode[i + 3] = code[i + 3];
                    continue;
                    i = i2;
            }
            while (true) {
                i++;
                if (i < i2) {
                    newcode[i] = code[i];
                } else {
                    i = i2;
                }
            }
        }
        return ldcEntry;
    }

    private static void copyConstPoolInfo(int i, byte[] code, ConstPool srcCp, byte[] newcode, ConstPool destCp, Map<String, String> classnameMap) {
        int index = srcCp.copy(((code[i] & 255) << 8) | (code[i + 1] & 255), destCp, classnameMap);
        newcode[i] = (byte) (index >> 8);
        newcode[i + 1] = (byte) index;
    }

    /* access modifiers changed from: package-private */
    public static class LdcEntry {
        int index;
        LdcEntry next;
        int where;

        LdcEntry() {
        }

        static byte[] doit(byte[] code, LdcEntry ldc, ExceptionTable etable, CodeAttribute ca) throws BadBytecode {
            if (ldc != null) {
                return CodeIterator.changeLdcToLdcW(code, etable, ca, ldc);
            }
            return code;
        }
    }

    public void insertLocalVar(int where, int size) throws BadBytecode {
        CodeIterator ci = iterator();
        while (ci.hasNext()) {
            shiftIndex(ci, where, size);
        }
        setMaxLocals(getMaxLocals() + size);
    }

    private static void shiftIndex(CodeIterator ci, int lessThan, int delta) throws BadBytecode {
        int var;
        int index = ci.next();
        int opcode = ci.byteAt(index);
        if (opcode >= 21) {
            if (opcode < 79) {
                if (opcode < 26) {
                    shiftIndex8(ci, index, opcode, lessThan, delta);
                } else if (opcode < 46) {
                    shiftIndex0(ci, index, opcode, lessThan, delta, 26, 21);
                } else if (opcode < 54) {
                } else {
                    if (opcode < 59) {
                        shiftIndex8(ci, index, opcode, lessThan, delta);
                    } else {
                        shiftIndex0(ci, index, opcode, lessThan, delta, 59, 54);
                    }
                }
            } else if (opcode == 132) {
                int var2 = ci.byteAt(index + 1);
                if (var2 >= lessThan) {
                    int var3 = var2 + delta;
                    if (var3 < 256) {
                        ci.writeByte(var3, index + 1);
                        return;
                    }
                    int plus = (byte) ci.byteAt(index + 2);
                    int pos = ci.insertExGap(3);
                    ci.writeByte(Opcode.WIDE, pos - 3);
                    ci.writeByte(Opcode.IINC, pos - 2);
                    ci.write16bit(var3, pos - 1);
                    ci.write16bit(plus, pos + 1);
                }
            } else if (opcode == 169) {
                shiftIndex8(ci, index, opcode, lessThan, delta);
            } else if (opcode == 196 && (var = ci.u16bitAt(index + 2)) >= lessThan) {
                ci.write16bit(var + delta, index + 2);
            }
        }
    }

    private static void shiftIndex8(CodeIterator ci, int index, int opcode, int lessThan, int delta) throws BadBytecode {
        int var = ci.byteAt(index + 1);
        if (var >= lessThan) {
            int var2 = var + delta;
            if (var2 < 256) {
                ci.writeByte(var2, index + 1);
                return;
            }
            int pos = ci.insertExGap(2);
            ci.writeByte(Opcode.WIDE, pos - 2);
            ci.writeByte(opcode, pos - 1);
            ci.write16bit(var2, pos);
        }
    }

    private static void shiftIndex0(CodeIterator ci, int index, int opcode, int lessThan, int delta, int opcode_i_0, int opcode_i) throws BadBytecode {
        int var = (opcode - opcode_i_0) % 4;
        if (var >= lessThan) {
            int var2 = var + delta;
            if (var2 < 4) {
                ci.writeByte(opcode + delta, index);
                return;
            }
            int opcode2 = ((opcode - opcode_i_0) / 4) + opcode_i;
            if (var2 < 256) {
                int pos = ci.insertExGap(1);
                ci.writeByte(opcode2, pos - 1);
                ci.writeByte(var2, pos);
                return;
            }
            int pos2 = ci.insertExGap(3);
            ci.writeByte(Opcode.WIDE, pos2 - 1);
            ci.writeByte(opcode2, pos2);
            ci.write16bit(var2, pos2 + 1);
        }
    }
}
