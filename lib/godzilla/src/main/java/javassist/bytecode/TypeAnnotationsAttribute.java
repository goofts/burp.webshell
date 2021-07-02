package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.annotation.TypeAnnotationsWriter;

public class TypeAnnotationsAttribute extends AttributeInfo {
    public static final String invisibleTag = "RuntimeInvisibleTypeAnnotations";
    public static final String visibleTag = "RuntimeVisibleTypeAnnotations";

    public TypeAnnotationsAttribute(ConstPool cp, String attrname, byte[] info) {
        super(cp, attrname, info);
    }

    TypeAnnotationsAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
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
            return new TypeAnnotationsAttribute(newCp, getName(), copier.close());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

    static class TAWalker extends AnnotationsAttribute.Walker {
        SubWalker subWalker;

        TAWalker(byte[] attrInfo) {
            super(attrInfo);
            this.subWalker = new SubWalker(attrInfo);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public int annotationArray(int pos, int num) throws Exception {
            for (int i = 0; i < num; i++) {
                pos = annotation(this.subWalker.typePath(this.subWalker.targetInfo(pos + 1, this.info[pos] & 255)));
            }
            return pos;
        }
    }

    static class SubWalker {
        byte[] info;

        SubWalker(byte[] attrInfo) {
            this.info = attrInfo;
        }

        /* access modifiers changed from: package-private */
        public final int targetInfo(int pos, int type) throws Exception {
            switch (type) {
                case 0:
                case 1:
                    typeParameterTarget(pos, type, this.info[pos] & 255);
                    return pos + 1;
                case 16:
                    supertypeTarget(pos, ByteArray.readU16bit(this.info, pos));
                    return pos + 2;
                case 17:
                case 18:
                    typeParameterBoundTarget(pos, type, this.info[pos] & 255, this.info[pos + 1] & 255);
                    return pos + 2;
                case 19:
                case 20:
                case 21:
                    emptyTarget(pos, type);
                    return pos;
                case 22:
                    formalParameterTarget(pos, this.info[pos] & 255);
                    return pos + 1;
                case 23:
                    throwsTarget(pos, ByteArray.readU16bit(this.info, pos));
                    return pos + 2;
                case 64:
                case 65:
                    return localvarTarget(pos + 2, type, ByteArray.readU16bit(this.info, pos));
                case 66:
                    catchTarget(pos, ByteArray.readU16bit(this.info, pos));
                    return pos + 2;
                case 67:
                case 68:
                case 69:
                case Opcode.FSTORE_3:
                    offsetTarget(pos, type, ByteArray.readU16bit(this.info, pos));
                    return pos + 2;
                case Opcode.DSTORE_0:
                case Opcode.DSTORE_1:
                case Opcode.DSTORE_2:
                case Opcode.DSTORE_3:
                case Opcode.ASTORE_0:
                    typeArgumentTarget(pos, type, ByteArray.readU16bit(this.info, pos), this.info[pos + 2] & 255);
                    return pos + 3;
                default:
                    throw new RuntimeException("invalid target type: " + type);
            }
        }

        /* access modifiers changed from: package-private */
        public void typeParameterTarget(int pos, int targetType, int typeParameterIndex) throws Exception {
        }

        /* access modifiers changed from: package-private */
        public void supertypeTarget(int pos, int superTypeIndex) throws Exception {
        }

        /* access modifiers changed from: package-private */
        public void typeParameterBoundTarget(int pos, int targetType, int typeParameterIndex, int boundIndex) throws Exception {
        }

        /* access modifiers changed from: package-private */
        public void emptyTarget(int pos, int targetType) throws Exception {
        }

        /* access modifiers changed from: package-private */
        public void formalParameterTarget(int pos, int formalParameterIndex) throws Exception {
        }

        /* access modifiers changed from: package-private */
        public void throwsTarget(int pos, int throwsTypeIndex) throws Exception {
        }

        /* access modifiers changed from: package-private */
        public int localvarTarget(int pos, int targetType, int tableLength) throws Exception {
            for (int i = 0; i < tableLength; i++) {
                localvarTarget(pos, targetType, ByteArray.readU16bit(this.info, pos), ByteArray.readU16bit(this.info, pos + 2), ByteArray.readU16bit(this.info, pos + 4));
                pos += 6;
            }
            return pos;
        }

        /* access modifiers changed from: package-private */
        public void localvarTarget(int pos, int targetType, int startPc, int length, int index) throws Exception {
        }

        /* access modifiers changed from: package-private */
        public void catchTarget(int pos, int exceptionTableIndex) throws Exception {
        }

        /* access modifiers changed from: package-private */
        public void offsetTarget(int pos, int targetType, int offset) throws Exception {
        }

        /* access modifiers changed from: package-private */
        public void typeArgumentTarget(int pos, int targetType, int offset, int typeArgumentIndex) throws Exception {
        }

        /* access modifiers changed from: package-private */
        public final int typePath(int pos) throws Exception {
            return typePath(pos + 1, this.info[pos] & 255);
        }

        /* access modifiers changed from: package-private */
        public int typePath(int pos, int pathLength) throws Exception {
            for (int i = 0; i < pathLength; i++) {
                typePath(pos, this.info[pos] & 255, this.info[pos + 1] & 255);
                pos += 2;
            }
            return pos;
        }

        /* access modifiers changed from: package-private */
        public void typePath(int pos, int typePathKind, int typeArgumentIndex) throws Exception {
        }
    }

    /* access modifiers changed from: package-private */
    public static class Renamer extends AnnotationsAttribute.Renamer {
        SubWalker sub;

        Renamer(byte[] attrInfo, ConstPool cp, Map<String, String> map) {
            super(attrInfo, cp, map);
            this.sub = new SubWalker(attrInfo);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Walker
        public int annotationArray(int pos, int num) throws Exception {
            for (int i = 0; i < num; i++) {
                pos = annotation(this.sub.typePath(this.sub.targetInfo(pos + 1, this.info[pos] & 255)));
            }
            return pos;
        }
    }

    static class Copier extends AnnotationsAttribute.Copier {
        SubCopier sub;

        Copier(byte[] attrInfo, ConstPool src, ConstPool dest, Map<String, String> map) {
            super(attrInfo, src, dest, map, false);
            TypeAnnotationsWriter w = new TypeAnnotationsWriter(this.output, dest);
            this.writer = w;
            this.sub = new SubCopier(attrInfo, src, dest, map, w);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.AnnotationsAttribute.Copier, javassist.bytecode.AnnotationsAttribute.Walker
        public int annotationArray(int pos, int num) throws Exception {
            this.writer.numAnnotations(num);
            for (int i = 0; i < num; i++) {
                pos = annotation(this.sub.typePath(this.sub.targetInfo(pos + 1, this.info[pos] & 255)));
            }
            return pos;
        }
    }

    static class SubCopier extends SubWalker {
        Map<String, String> classnames;
        ConstPool destPool;
        ConstPool srcPool;
        TypeAnnotationsWriter writer;

        SubCopier(byte[] attrInfo, ConstPool src, ConstPool dest, Map<String, String> map, TypeAnnotationsWriter w) {
            super(attrInfo);
            this.srcPool = src;
            this.destPool = dest;
            this.classnames = map;
            this.writer = w;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        public void typeParameterTarget(int pos, int targetType, int typeParameterIndex) throws Exception {
            this.writer.typeParameterTarget(targetType, typeParameterIndex);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        public void supertypeTarget(int pos, int superTypeIndex) throws Exception {
            this.writer.supertypeTarget(superTypeIndex);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        public void typeParameterBoundTarget(int pos, int targetType, int typeParameterIndex, int boundIndex) throws Exception {
            this.writer.typeParameterBoundTarget(targetType, typeParameterIndex, boundIndex);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        public void emptyTarget(int pos, int targetType) throws Exception {
            this.writer.emptyTarget(targetType);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        public void formalParameterTarget(int pos, int formalParameterIndex) throws Exception {
            this.writer.formalParameterTarget(formalParameterIndex);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        public void throwsTarget(int pos, int throwsTypeIndex) throws Exception {
            this.writer.throwsTarget(throwsTypeIndex);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        public int localvarTarget(int pos, int targetType, int tableLength) throws Exception {
            this.writer.localVarTarget(targetType, tableLength);
            return super.localvarTarget(pos, targetType, tableLength);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        public void localvarTarget(int pos, int targetType, int startPc, int length, int index) throws Exception {
            this.writer.localVarTargetTable(startPc, length, index);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        public void catchTarget(int pos, int exceptionTableIndex) throws Exception {
            this.writer.catchTarget(exceptionTableIndex);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        public void offsetTarget(int pos, int targetType, int offset) throws Exception {
            this.writer.offsetTarget(targetType, offset);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        public void typeArgumentTarget(int pos, int targetType, int offset, int typeArgumentIndex) throws Exception {
            this.writer.typeArgumentTarget(targetType, offset, typeArgumentIndex);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        public int typePath(int pos, int pathLength) throws Exception {
            this.writer.typePath(pathLength);
            return super.typePath(pos, pathLength);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.bytecode.TypeAnnotationsAttribute.SubWalker
        public void typePath(int pos, int typePathKind, int typeArgumentIndex) throws Exception {
            this.writer.typePathPath(typePathKind, typeArgumentIndex);
        }
    }
}
