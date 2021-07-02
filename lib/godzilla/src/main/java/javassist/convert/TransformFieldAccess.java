package javassist.convert;

import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;

public final class TransformFieldAccess extends Transformer {
    private ConstPool constPool = null;
    private CtClass fieldClass;
    private String fieldname;
    private boolean isPrivate;
    private String newClassname;
    private String newFieldname;
    private int newIndex;

    public TransformFieldAccess(Transformer next, CtField field, String newClassname2, String newFieldname2) {
        super(next);
        this.fieldClass = field.getDeclaringClass();
        this.fieldname = field.getName();
        this.isPrivate = Modifier.isPrivate(field.getModifiers());
        this.newClassname = newClassname2;
        this.newFieldname = newFieldname2;
    }

    @Override // javassist.convert.Transformer
    public void initialize(ConstPool cp, CodeAttribute attr) {
        if (this.constPool != cp) {
            this.newIndex = 0;
        }
    }

    @Override // javassist.convert.Transformer
    public int transform(CtClass clazz, int pos, CodeIterator iterator, ConstPool cp) {
        String typedesc;
        int c = iterator.byteAt(pos);
        if ((c == 180 || c == 178 || c == 181 || c == 179) && (typedesc = TransformReadField.isField(clazz.getClassPool(), cp, this.fieldClass, this.fieldname, this.isPrivate, iterator.u16bitAt(pos + 1))) != null) {
            if (this.newIndex == 0) {
                this.newIndex = cp.addFieldrefInfo(cp.addClassInfo(this.newClassname), cp.addNameAndTypeInfo(this.newFieldname, typedesc));
                this.constPool = cp;
            }
            iterator.write16bit(this.newIndex, pos + 1);
        }
        return pos;
    }
}
