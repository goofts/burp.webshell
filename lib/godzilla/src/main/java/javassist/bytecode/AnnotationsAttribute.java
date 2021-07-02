package javassist.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationMemberValue;
import javassist.bytecode.annotation.AnnotationsWriter;
import javassist.bytecode.annotation.ArrayMemberValue;
import javassist.bytecode.annotation.BooleanMemberValue;
import javassist.bytecode.annotation.ByteMemberValue;
import javassist.bytecode.annotation.CharMemberValue;
import javassist.bytecode.annotation.ClassMemberValue;
import javassist.bytecode.annotation.DoubleMemberValue;
import javassist.bytecode.annotation.EnumMemberValue;
import javassist.bytecode.annotation.FloatMemberValue;
import javassist.bytecode.annotation.IntegerMemberValue;
import javassist.bytecode.annotation.LongMemberValue;
import javassist.bytecode.annotation.MemberValue;
import javassist.bytecode.annotation.ShortMemberValue;
import javassist.bytecode.annotation.StringMemberValue;

public class AnnotationsAttribute extends AttributeInfo {
    public static final String invisibleTag = "RuntimeInvisibleAnnotations";
    public static final String visibleTag = "RuntimeVisibleAnnotations";

    public AnnotationsAttribute(ConstPool cp, String attrname, byte[] info) {
        super(cp, attrname, info);
    }

    public AnnotationsAttribute(ConstPool cp, String attrname) {
        this(cp, attrname, new byte[]{0, 0});
    }

    AnnotationsAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
        super(cp, n, in);
    }

    public int numAnnotations() {
        return ByteArray.readU16bit(this.info, 0);
    }

    @Override // javassist.bytecode.AttributeInfo
    public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
        Copier copier = new Copier(this.info, this.constPool, newCp, classnames);
        try {
            copier.annotationArray();
            return new AnnotationsAttribute(newCp, getName(), copier.close());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Annotation getAnnotation(String type) {
        Annotation[] annotations = getAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            if (annotations[i].getTypeName().equals(type)) {
                return annotations[i];
            }
        }
        return null;
    }

    public void addAnnotation(Annotation annotation) {
        String type = annotation.getTypeName();
        Annotation[] annotations = getAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            if (annotations[i].getTypeName().equals(type)) {
                annotations[i] = annotation;
                setAnnotations(annotations);
                return;
            }
        }
        Annotation[] newlist = new Annotation[(annotations.length + 1)];
        System.arraycopy(annotations, 0, newlist, 0, annotations.length);
        newlist[annotations.length] = annotation;
        setAnnotations(newlist);
    }

    public boolean removeAnnotation(String type) {
        Annotation[] annotations = getAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            if (annotations[i].getTypeName().equals(type)) {
                Annotation[] newlist = new Annotation[(annotations.length - 1)];
                System.arraycopy(annotations, 0, newlist, 0, i);
                if (i < annotations.length - 1) {
                    System.arraycopy(annotations, i + 1, newlist, i, (annotations.length - i) - 1);
                }
                setAnnotations(newlist);
                return true;
            }
        }
        return false;
    }

    public Annotation[] getAnnotations() {
        try {
            return new Parser(this.info, this.constPool).parseAnnotations();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void setAnnotations(Annotation[] annotations) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        AnnotationsWriter writer = new AnnotationsWriter(output, this.constPool);
        try {
            int n = annotations.length;
            writer.numAnnotations(n);
            for (Annotation annotation : annotations) {
                annotation.write(writer);
            }
            writer.close();
            set(output.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setAnnotation(Annotation annotation) {
        setAnnotations(new Annotation[]{annotation});
    }

    /* access modifiers changed from: package-private */
    @Override // javassist.bytecode.AttributeInfo
    public void renameClass(String oldname, String newname) {
        Map<String, String> map = new HashMap<>();
        map.put(oldname, newname);
        renameClass(map);
    }

    /* access modifiers changed from: package-private */
    @Override // javassist.bytecode.AttributeInfo
    public void renameClass(Map<String, String> classnames) {
        try {
            new Renamer(this.info, getConstPool(), classnames).annotationArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /* access modifiers changed from: package-private */
    @Override // javassist.bytecode.AttributeInfo
    public void getRefClasses(Map<String, String> classnames) {
        renameClass(classnames);
    }

    public String toString() {
        Annotation[] a = getAnnotations();
        StringBuilder sbuf = new StringBuilder();
        int i = 0;
        while (i < a.length) {
            int i2 = i + 1;
            sbuf.append(a[i].toString());
            if (i2 != a.length) {
                sbuf.append(", ");
                i = i2;
            } else {
                i = i2;
            }
        }
        return sbuf.toString();
    }

    static class Walker {
        byte[] info;

        Walker(byte[] attrInfo) {
            this.info = attrInfo;
        }

        /* access modifiers changed from: package-private */
        public final void parameters() throws Exception {
            parameters(this.info[0] & 255, 1);
        }

        /* access modifiers changed from: package-private */
        public void parameters(int numParam, int pos) throws Exception {
            for (int i = 0; i < numParam; i++) {
                pos = annotationArray(pos);
            }
        }

        /* access modifiers changed from: package-private */
        public final void annotationArray() throws Exception {
            annotationArray(0);
        }

        /* access modifiers changed from: package-private */
        public final int annotationArray(int pos) throws Exception {
            return annotationArray(pos + 2, ByteArray.readU16bit(this.info, pos));
        }

        /* access modifiers changed from: package-private */
        public int annotationArray(int pos, int num) throws Exception {
            for (int i = 0; i < num; i++) {
                pos = annotation(pos);
            }
            return pos;
        }

        /* access modifiers changed from: package-private */
        public final int annotation(int pos) throws Exception {
            return annotation(pos + 4, ByteArray.readU16bit(this.info, pos), ByteArray.readU16bit(this.info, pos + 2));
        }

        /* access modifiers changed from: package-private */
        public int annotation(int pos, int type, int numPairs) throws Exception {
            for (int j = 0; j < numPairs; j++) {
                pos = memberValuePair(pos);
            }
            return pos;
        }

        /* access modifiers changed from: package-private */
        public final int memberValuePair(int pos) throws Exception {
            return memberValuePair(pos + 2, ByteArray.readU16bit(this.info, pos));
        }

        /* access modifiers changed from: package-private */
        public int memberValuePair(int pos, int nameIndex) throws Exception {
            return memberValue(pos);
        }

        /* access modifiers changed from: package-private */
        public final int memberValue(int pos) throws Exception {
            int tag = this.info[pos] & 255;
            if (tag == 101) {
                enumMemberValue(pos, ByteArray.readU16bit(this.info, pos + 1), ByteArray.readU16bit(this.info, pos + 3));
                return pos + 5;
            } else if (tag == 99) {
                classMemberValue(pos, ByteArray.readU16bit(this.info, pos + 1));
                return pos + 3;
            } else if (tag == 64) {
                return annotationMemberValue(pos + 1);
            } else {
                if (tag == 91) {
                    return arrayMemberValue(pos + 3, ByteArray.readU16bit(this.info, pos + 1));
                }
                constValueMember(tag, ByteArray.readU16bit(this.info, pos + 1));
                return pos + 3;
            }
        }

        /* access modifiers changed from: package-private */
        public void constValueMember(int tag, int index) throws Exception {
        }

        /* access modifiers changed from: package-private */
        public void enumMemberValue(int pos, int typeNameIndex, int constNameIndex) throws Exception {
        }

        /* access modifiers changed from: package-private */
        public void classMemberValue(int pos, int index) throws Exception {
        }

        /* access modifiers changed from: package-private */
        public int annotationMemberValue(int pos) throws Exception {
            return annotation(pos);
        }

        /* access modifiers changed from: package-private */
        public int arrayMemberValue(int pos, int num) throws Exception {
            for (int i = 0; i < num; i++) {
                pos = memberValue(pos);
            }
            return pos;
        }
    }

    /* access modifiers changed from: package-private */
    public static class Renamer extends Walker {
        Map<String, String> classnames;
        ConstPool cpool;

        Renamer(byte[] info, ConstPool cp, Map<String, String> map) {
            super(info);
            this.cpool = cp;
            this.classnames = map;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public int annotation(int pos, int type, int numPairs) throws Exception {
            renameType(pos - 4, type);
            return super.annotation(pos, type, numPairs);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public void enumMemberValue(int pos, int typeNameIndex, int constNameIndex) throws Exception {
            renameType(pos + 1, typeNameIndex);
            super.enumMemberValue(pos, typeNameIndex, constNameIndex);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public void classMemberValue(int pos, int index) throws Exception {
            renameType(pos + 1, index);
            super.classMemberValue(pos, index);
        }

        private void renameType(int pos, int index) {
            String name = this.cpool.getUtf8Info(index);
            String newName = Descriptor.rename(name, this.classnames);
            if (!name.equals(newName)) {
                ByteArray.write16bit(this.cpool.addUtf8Info(newName), this.info, pos);
            }
        }
    }

    static class Copier extends Walker {
        Map<String, String> classnames;
        ConstPool destPool;
        ByteArrayOutputStream output;
        ConstPool srcPool;
        AnnotationsWriter writer;

        Copier(byte[] info, ConstPool src, ConstPool dest, Map<String, String> map) {
            this(info, src, dest, map, true);
        }

        Copier(byte[] info, ConstPool src, ConstPool dest, Map<String, String> map, boolean makeWriter) {
            super(info);
            this.output = new ByteArrayOutputStream();
            if (makeWriter) {
                this.writer = new AnnotationsWriter(this.output, dest);
            }
            this.srcPool = src;
            this.destPool = dest;
            this.classnames = map;
        }

        /* access modifiers changed from: package-private */
        public byte[] close() throws IOException {
            this.writer.close();
            return this.output.toByteArray();
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public void parameters(int numParam, int pos) throws Exception {
            this.writer.numParameters(numParam);
            super.parameters(numParam, pos);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public int annotationArray(int pos, int num) throws Exception {
            this.writer.numAnnotations(num);
            return super.annotationArray(pos, num);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public int annotation(int pos, int type, int numPairs) throws Exception {
            this.writer.annotation(copyType(type), numPairs);
            return super.annotation(pos, type, numPairs);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public int memberValuePair(int pos, int nameIndex) throws Exception {
            this.writer.memberValuePair(copy(nameIndex));
            return super.memberValuePair(pos, nameIndex);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public void constValueMember(int tag, int index) throws Exception {
            this.writer.constValueIndex(tag, copy(index));
            super.constValueMember(tag, index);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public void enumMemberValue(int pos, int typeNameIndex, int constNameIndex) throws Exception {
            this.writer.enumConstValue(copyType(typeNameIndex), copy(constNameIndex));
            super.enumMemberValue(pos, typeNameIndex, constNameIndex);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public void classMemberValue(int pos, int index) throws Exception {
            this.writer.classInfoIndex(copyType(index));
            super.classMemberValue(pos, index);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public int annotationMemberValue(int pos) throws Exception {
            this.writer.annotationValue();
            return super.annotationMemberValue(pos);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public int arrayMemberValue(int pos, int num) throws Exception {
            this.writer.arrayValue(num);
            return super.arrayMemberValue(pos, num);
        }

        /* access modifiers changed from: package-private */
        public int copy(int srcIndex) {
            return this.srcPool.copy(srcIndex, this.destPool, this.classnames);
        }

        /* access modifiers changed from: package-private */
        public int copyType(int srcIndex) {
            return this.destPool.addUtf8Info(Descriptor.rename(this.srcPool.getUtf8Info(srcIndex), this.classnames));
        }
    }

    /* access modifiers changed from: package-private */
    public static class Parser extends Walker {
        Annotation[] allAnno;
        Annotation[][] allParams;
        Annotation currentAnno;
        MemberValue currentMember;
        ConstPool pool;

        Parser(byte[] info, ConstPool cp) {
            super(info);
            this.pool = cp;
        }

        /* access modifiers changed from: package-private */
        public Annotation[][] parseParameters() throws Exception {
            parameters();
            return this.allParams;
        }

        /* access modifiers changed from: package-private */
        public Annotation[] parseAnnotations() throws Exception {
            annotationArray();
            return this.allAnno;
        }

        /* access modifiers changed from: package-private */
        public MemberValue parseMemberValue() throws Exception {
            memberValue(0);
            return this.currentMember;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public void parameters(int numParam, int pos) throws Exception {
            Annotation[][] params = new Annotation[numParam][];
            for (int i = 0; i < numParam; i++) {
                pos = annotationArray(pos);
                params[i] = this.allAnno;
            }
            this.allParams = params;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public int annotationArray(int pos, int num) throws Exception {
            Annotation[] array = new Annotation[num];
            for (int i = 0; i < num; i++) {
                pos = annotation(pos);
                array[i] = this.currentAnno;
            }
            this.allAnno = array;
            return pos;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public int annotation(int pos, int type, int numPairs) throws Exception {
            this.currentAnno = new Annotation(type, this.pool);
            return super.annotation(pos, type, numPairs);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public int memberValuePair(int pos, int nameIndex) throws Exception {
            int pos2 = super.memberValuePair(pos, nameIndex);
            this.currentAnno.addMemberValue(nameIndex, this.currentMember);
            return pos2;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public void constValueMember(int tag, int index) throws Exception {
            MemberValue m;
            ConstPool cp = this.pool;
            switch (tag) {
                case 66:
                    m = new ByteMemberValue(index, cp);
                    break;
                case 67:
                    m = new CharMemberValue(index, cp);
                    break;
                case 68:
                    m = new DoubleMemberValue(index, cp);
                    break;
                case Opcode.FSTORE_3 /*{ENCODED_INT: 70}*/:
                    m = new FloatMemberValue(index, cp);
                    break;
                case Opcode.DSTORE_2 /*{ENCODED_INT: 73}*/:
                    m = new IntegerMemberValue(index, cp);
                    break;
                case Opcode.DSTORE_3 /*{ENCODED_INT: 74}*/:
                    m = new LongMemberValue(index, cp);
                    break;
                case Opcode.AASTORE /*{ENCODED_INT: 83}*/:
                    m = new ShortMemberValue(index, cp);
                    break;
                case Opcode.DUP_X1 /*{ENCODED_INT: 90}*/:
                    m = new BooleanMemberValue(index, cp);
                    break;
                case Opcode.DREM /*{ENCODED_INT: 115}*/:
                    m = new StringMemberValue(index, cp);
                    break;
                default:
                    throw new RuntimeException("unknown tag:" + tag);
            }
            this.currentMember = m;
            super.constValueMember(tag, index);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public void enumMemberValue(int pos, int typeNameIndex, int constNameIndex) throws Exception {
            this.currentMember = new EnumMemberValue(typeNameIndex, constNameIndex, this.pool);
            super.enumMemberValue(pos, typeNameIndex, constNameIndex);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public void classMemberValue(int pos, int index) throws Exception {
            this.currentMember = new ClassMemberValue(index, this.pool);
            super.classMemberValue(pos, index);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public int annotationMemberValue(int pos) throws Exception {
            Annotation anno = this.currentAnno;
            int pos2 = super.annotationMemberValue(pos);
            this.currentMember = new AnnotationMemberValue(this.currentAnno, this.pool);
            this.currentAnno = anno;
            return pos2;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public int arrayMemberValue(int pos, int num) throws Exception {
            ArrayMemberValue amv = new ArrayMemberValue(this.pool);
            MemberValue[] elements = new MemberValue[num];
            for (int i = 0; i < num; i++) {
                pos = memberValue(pos);
                elements[i] = this.currentMember;
            }
            amv.setValue(elements);
            this.currentMember = amv;
            return pos;
        }
    }
}
