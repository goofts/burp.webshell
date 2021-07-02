package javassist.convert;

import javassist.CannotCompileException;
import javassist.CtClass;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;

public final class TransformNewClass extends Transformer {
    private String classname;
    private int nested;
    private int newClassIndex;
    private String newClassName;
    private int newMethodIndex;
    private int newMethodNTIndex;

    public TransformNewClass(Transformer next, String classname2, String newClassName2) {
        super(next);
        this.classname = classname2;
        this.newClassName = newClassName2;
    }

    @Override // javassist.convert.Transformer
    public void initialize(ConstPool cp, CodeAttribute attr) {
        this.nested = 0;
        this.newMethodIndex = 0;
        this.newMethodNTIndex = 0;
        this.newClassIndex = 0;
    }

    @Override // javassist.convert.Transformer
    public int transform(CtClass clazz, int pos, CodeIterator iterator, ConstPool cp) throws CannotCompileException {
        int c = iterator.byteAt(pos);
        if (c == 187) {
            if (cp.getClassInfo(iterator.u16bitAt(pos + 1)).equals(this.classname)) {
                if (iterator.byteAt(pos + 3) != 89) {
                    throw new CannotCompileException("NEW followed by no DUP was found");
                }
                if (this.newClassIndex == 0) {
                    this.newClassIndex = cp.addClassInfo(this.newClassName);
                }
                iterator.write16bit(this.newClassIndex, pos + 1);
                this.nested++;
            }
        } else if (c == 183) {
            int index = iterator.u16bitAt(pos + 1);
            if (cp.isConstructor(this.classname, index) != 0 && this.nested > 0) {
                int nt = cp.getMethodrefNameAndType(index);
                if (this.newMethodNTIndex != nt) {
                    this.newMethodNTIndex = nt;
                    this.newMethodIndex = cp.addMethodrefInfo(this.newClassIndex, nt);
                }
                iterator.write16bit(this.newMethodIndex, pos + 1);
                this.nested--;
            }
        }
        return pos;
    }
}
