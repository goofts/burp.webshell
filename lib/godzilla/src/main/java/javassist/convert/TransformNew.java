package javassist.convert;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.Opcode;
import javassist.bytecode.StackMap;
import javassist.bytecode.StackMapTable;

public final class TransformNew extends Transformer {
    private String classname;
    private int nested;
    private String trapClass;
    private String trapMethod;

    public TransformNew(Transformer next, String classname2, String trapClass2, String trapMethod2) {
        super(next);
        this.classname = classname2;
        this.trapClass = trapClass2;
        this.trapMethod = trapMethod2;
    }

    @Override // javassist.convert.Transformer
    public void initialize(ConstPool cp, CodeAttribute attr) {
        this.nested = 0;
    }

    @Override // javassist.convert.Transformer
    public int transform(CtClass clazz, int pos, CodeIterator iterator, ConstPool cp) throws CannotCompileException {
        int typedesc;
        int c = iterator.byteAt(pos);
        if (c == 187) {
            if (cp.getClassInfo(iterator.u16bitAt(pos + 1)).equals(this.classname)) {
                if (iterator.byteAt(pos + 3) != 89) {
                    throw new CannotCompileException("NEW followed by no DUP was found");
                }
                iterator.writeByte(0, pos);
                iterator.writeByte(0, pos + 1);
                iterator.writeByte(0, pos + 2);
                iterator.writeByte(0, pos + 3);
                this.nested++;
                StackMapTable smt = (StackMapTable) iterator.get().getAttribute(StackMapTable.tag);
                if (smt != null) {
                    smt.removeNew(pos);
                }
                StackMap sm = (StackMap) iterator.get().getAttribute(StackMap.tag);
                if (sm != null) {
                    sm.removeNew(pos);
                }
            }
        } else if (c == 183 && (typedesc = cp.isConstructor(this.classname, iterator.u16bitAt(pos + 1))) != 0 && this.nested > 0) {
            int methodref = computeMethodref(typedesc, cp);
            iterator.writeByte(Opcode.INVOKESTATIC, pos);
            iterator.write16bit(methodref, pos + 1);
            this.nested--;
        }
        return pos;
    }

    private int computeMethodref(int typedesc, ConstPool cp) {
        return cp.addMethodrefInfo(cp.addClassInfo(this.trapClass), cp.addNameAndTypeInfo(cp.addUtf8Info(this.trapMethod), cp.addUtf8Info(Descriptor.changeReturnType(this.classname, cp.getUtf8Info(typedesc)))));
    }
}
