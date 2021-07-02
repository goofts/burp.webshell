package javassist.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javassist.CannotCompileException;

public class StackMap extends AttributeInfo {
    public static final int DOUBLE = 3;
    public static final int FLOAT = 2;
    public static final int INTEGER = 1;
    public static final int LONG = 4;
    public static final int NULL = 5;
    public static final int OBJECT = 7;
    public static final int THIS = 6;
    public static final int TOP = 0;
    public static final int UNINIT = 8;
    public static final String tag = "StackMap";

    StackMap(ConstPool cp, byte[] newInfo) {
        super(cp, tag, newInfo);
    }

    StackMap(ConstPool cp, int name_id, DataInputStream in) throws IOException {
        super(cp, name_id, in);
    }

    public int numOfEntries() {
        return ByteArray.readU16bit(this.info, 0);
    }

    @Override // javassist.bytecode.AttributeInfo
    public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
        Copier copier = new Copier(this, newCp, classnames);
        copier.visit();
        return copier.getStackMap();
    }

    public static class Walker {
        byte[] info;

        public Walker(StackMap sm) {
            this.info = sm.get();
        }

        public void visit() {
            int num = ByteArray.readU16bit(this.info, 0);
            int pos = 2;
            for (int i = 0; i < num; i++) {
                int offset = ByteArray.readU16bit(this.info, pos);
                int pos2 = locals(pos + 4, offset, ByteArray.readU16bit(this.info, pos + 2));
                pos = stack(pos2 + 2, offset, ByteArray.readU16bit(this.info, pos2));
            }
        }

        public int locals(int pos, int offset, int num) {
            return typeInfoArray(pos, offset, num, true);
        }

        public int stack(int pos, int offset, int num) {
            return typeInfoArray(pos, offset, num, false);
        }

        public int typeInfoArray(int pos, int offset, int num, boolean isLocals) {
            for (int k = 0; k < num; k++) {
                pos = typeInfoArray2(k, pos);
            }
            return pos;
        }

        /* access modifiers changed from: package-private */
        public int typeInfoArray2(int k, int pos) {
            byte tag = this.info[pos];
            if (tag == 7) {
                objectVariable(pos, ByteArray.readU16bit(this.info, pos + 1));
                return pos + 3;
            } else if (tag == 8) {
                uninitialized(pos, ByteArray.readU16bit(this.info, pos + 1));
                return pos + 3;
            } else {
                typeInfo(pos, tag);
                return pos + 1;
            }
        }

        public void typeInfo(int pos, byte tag) {
        }

        public void objectVariable(int pos, int clazz) {
        }

        public void uninitialized(int pos, int offset) {
        }
    }

    static class Copier extends Walker {
        Map<String, String> classnames;
        byte[] dest = new byte[this.info.length];
        ConstPool destCp;
        ConstPool srcCp;

        Copier(StackMap map, ConstPool newCp, Map<String, String> classnames2) {
            super(map);
            this.srcCp = map.getConstPool();
            this.destCp = newCp;
            this.classnames = classnames2;
        }

        @Override // javassist.bytecode.StackMap.Walker
        public void visit() {
            ByteArray.write16bit(ByteArray.readU16bit(this.info, 0), this.dest, 0);
            super.visit();
        }

        @Override // javassist.bytecode.StackMap.Walker
        public int locals(int pos, int offset, int num) {
            ByteArray.write16bit(offset, this.dest, pos - 4);
            return super.locals(pos, offset, num);
        }

        @Override // javassist.bytecode.StackMap.Walker
        public int typeInfoArray(int pos, int offset, int num, boolean isLocals) {
            ByteArray.write16bit(num, this.dest, pos - 2);
            return super.typeInfoArray(pos, offset, num, isLocals);
        }

        @Override // javassist.bytecode.StackMap.Walker
        public void typeInfo(int pos, byte tag) {
            this.dest[pos] = tag;
        }

        @Override // javassist.bytecode.StackMap.Walker
        public void objectVariable(int pos, int clazz) {
            this.dest[pos] = 7;
            ByteArray.write16bit(this.srcCp.copy(clazz, this.destCp, this.classnames), this.dest, pos + 1);
        }

        @Override // javassist.bytecode.StackMap.Walker
        public void uninitialized(int pos, int offset) {
            this.dest[pos] = 8;
            ByteArray.write16bit(offset, this.dest, pos + 1);
        }

        public StackMap getStackMap() {
            return new StackMap(this.destCp, this.dest);
        }
    }

    public void insertLocal(int index, int tag2, int classInfo) throws BadBytecode {
        set(new InsertLocal(this, index, tag2, classInfo).doit());
    }

    static class SimpleCopy extends Walker {
        Writer writer = new Writer();

        SimpleCopy(StackMap map) {
            super(map);
        }

        /* access modifiers changed from: package-private */
        public byte[] doit() {
            visit();
            return this.writer.toByteArray();
        }

        @Override // javassist.bytecode.StackMap.Walker
        public void visit() {
            this.writer.write16bit(ByteArray.readU16bit(this.info, 0));
            super.visit();
        }

        @Override // javassist.bytecode.StackMap.Walker
        public int locals(int pos, int offset, int num) {
            this.writer.write16bit(offset);
            return super.locals(pos, offset, num);
        }

        @Override // javassist.bytecode.StackMap.Walker
        public int typeInfoArray(int pos, int offset, int num, boolean isLocals) {
            this.writer.write16bit(num);
            return super.typeInfoArray(pos, offset, num, isLocals);
        }

        @Override // javassist.bytecode.StackMap.Walker
        public void typeInfo(int pos, byte tag) {
            this.writer.writeVerifyTypeInfo(tag, 0);
        }

        @Override // javassist.bytecode.StackMap.Walker
        public void objectVariable(int pos, int clazz) {
            this.writer.writeVerifyTypeInfo(7, clazz);
        }

        @Override // javassist.bytecode.StackMap.Walker
        public void uninitialized(int pos, int offset) {
            this.writer.writeVerifyTypeInfo(8, offset);
        }
    }

    static class InsertLocal extends SimpleCopy {
        private int varData;
        private int varIndex;
        private int varTag;

        InsertLocal(StackMap map, int varIndex2, int varTag2, int varData2) {
            super(map);
            this.varIndex = varIndex2;
            this.varTag = varTag2;
            this.varData = varData2;
        }

        @Override // javassist.bytecode.StackMap.SimpleCopy, javassist.bytecode.StackMap.Walker
        public int typeInfoArray(int pos, int offset, int num, boolean isLocals) {
            if (!isLocals || num < this.varIndex) {
                return super.typeInfoArray(pos, offset, num, isLocals);
            }
            this.writer.write16bit(num + 1);
            int k = 0;
            int pos2 = pos;
            while (k < num) {
                if (k == this.varIndex) {
                    writeVarTypeInfo();
                }
                int pos3 = typeInfoArray2(k, pos2);
                k++;
                pos2 = pos3;
            }
            if (num == this.varIndex) {
                writeVarTypeInfo();
            }
            return pos2;
        }

        private void writeVarTypeInfo() {
            if (this.varTag == 7) {
                this.writer.writeVerifyTypeInfo(7, this.varData);
            } else if (this.varTag == 8) {
                this.writer.writeVerifyTypeInfo(8, this.varData);
            } else {
                this.writer.writeVerifyTypeInfo(this.varTag, 0);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void shiftPc(int where, int gapSize, boolean exclusive) throws BadBytecode {
        new Shifter(this, where, gapSize, exclusive).visit();
    }

    /* access modifiers changed from: package-private */
    public static class Shifter extends Walker {
        private boolean exclusive;
        private int gap;
        private int where;

        public Shifter(StackMap smt, int where2, int gap2, boolean exclusive2) {
            super(smt);
            this.where = where2;
            this.gap = gap2;
            this.exclusive = exclusive2;
        }

        @Override // javassist.bytecode.StackMap.Walker
        public int locals(int pos, int offset, int num) {
            if (!this.exclusive ? this.where < offset : this.where <= offset) {
                ByteArray.write16bit(this.gap + offset, this.info, pos - 4);
            }
            return super.locals(pos, offset, num);
        }

        @Override // javassist.bytecode.StackMap.Walker
        public void uninitialized(int pos, int offset) {
            if (this.where <= offset) {
                ByteArray.write16bit(this.gap + offset, this.info, pos + 1);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void shiftForSwitch(int where, int gapSize) throws BadBytecode {
        new SwitchShifter(this, where, gapSize).visit();
    }

    /* access modifiers changed from: package-private */
    public static class SwitchShifter extends Walker {
        private int gap;
        private int where;

        public SwitchShifter(StackMap smt, int where2, int gap2) {
            super(smt);
            this.where = where2;
            this.gap = gap2;
        }

        @Override // javassist.bytecode.StackMap.Walker
        public int locals(int pos, int offset, int num) {
            if (this.where == pos + offset) {
                ByteArray.write16bit(offset - this.gap, this.info, pos - 4);
            } else if (this.where == pos) {
                ByteArray.write16bit(this.gap + offset, this.info, pos - 4);
            }
            return super.locals(pos, offset, num);
        }
    }

    public void removeNew(int where) throws CannotCompileException {
        set(new NewRemover(this, where).doit());
    }

    /* access modifiers changed from: package-private */
    public static class NewRemover extends SimpleCopy {
        int posOfNew;

        NewRemover(StackMap map, int where) {
            super(map);
            this.posOfNew = where;
        }

        @Override // javassist.bytecode.StackMap.Walker
        public int stack(int pos, int offset, int num) {
            return stackTypeInfoArray(pos, offset, num);
        }

        private int stackTypeInfoArray(int pos, int offset, int num) {
            int p = pos;
            int count = 0;
            for (int k = 0; k < num; k++) {
                byte tag = this.info[p];
                if (tag == 7) {
                    p += 3;
                } else if (tag == 8) {
                    if (ByteArray.readU16bit(this.info, p + 1) == this.posOfNew) {
                        count++;
                    }
                    p += 3;
                } else {
                    p++;
                }
            }
            this.writer.write16bit(num - count);
            for (int k2 = 0; k2 < num; k2++) {
                byte tag2 = this.info[pos];
                if (tag2 == 7) {
                    objectVariable(pos, ByteArray.readU16bit(this.info, pos + 1));
                    pos += 3;
                } else if (tag2 == 8) {
                    int offsetOfNew = ByteArray.readU16bit(this.info, pos + 1);
                    if (offsetOfNew != this.posOfNew) {
                        uninitialized(pos, offsetOfNew);
                    }
                    pos += 3;
                } else {
                    typeInfo(pos, tag2);
                    pos++;
                }
            }
            return pos;
        }
    }

    public void print(PrintWriter out) {
        new Printer(this, out).print();
    }

    static class Printer extends Walker {
        private PrintWriter writer;

        public Printer(StackMap map, PrintWriter out) {
            super(map);
            this.writer = out;
        }

        public void print() {
            this.writer.println(ByteArray.readU16bit(this.info, 0) + " entries");
            visit();
        }

        @Override // javassist.bytecode.StackMap.Walker
        public int locals(int pos, int offset, int num) {
            this.writer.println("  * offset " + offset);
            return super.locals(pos, offset, num);
        }
    }

    public static class Writer {
        private ByteArrayOutputStream output = new ByteArrayOutputStream();

        public byte[] toByteArray() {
            return this.output.toByteArray();
        }

        public StackMap toStackMap(ConstPool cp) {
            return new StackMap(cp, this.output.toByteArray());
        }

        public void writeVerifyTypeInfo(int tag, int data) {
            this.output.write(tag);
            if (tag == 7 || tag == 8) {
                write16bit(data);
            }
        }

        public void write16bit(int value) {
            this.output.write((value >>> 8) & 255);
            this.output.write(value & 255);
        }
    }
}
