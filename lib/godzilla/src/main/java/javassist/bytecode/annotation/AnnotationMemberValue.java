package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;

public class AnnotationMemberValue extends MemberValue {
    Annotation value;

    public AnnotationMemberValue(ConstPool cp) {
        this(null, cp);
    }

    public AnnotationMemberValue(Annotation a, ConstPool cp) {
        super('@', cp);
        this.value = a;
    }

    /* access modifiers changed from: package-private */
    @Override // javassist.bytecode.annotation.MemberValue
    public Object getValue(ClassLoader cl, ClassPool cp, Method m) throws ClassNotFoundException {
        return AnnotationImpl.make(cl, getType(cl), cp, this.value);
    }

    /* access modifiers changed from: package-private */
    @Override // javassist.bytecode.annotation.MemberValue
    public Class<?> getType(ClassLoader cl) throws ClassNotFoundException {
        if (this.value != null) {
            return loadClass(cl, this.value.getTypeName());
        }
        throw new ClassNotFoundException("no type specified");
    }

    public Annotation getValue() {
        return this.value;
    }

    public void setValue(Annotation newValue) {
        this.value = newValue;
    }

    public String toString() {
        return this.value.toString();
    }

    @Override // javassist.bytecode.annotation.MemberValue
    public void write(AnnotationsWriter writer) throws IOException {
        writer.annotationValue();
        this.value.write(writer);
    }

    @Override // javassist.bytecode.annotation.MemberValue
    public void accept(MemberValueVisitor visitor) {
        visitor.visitAnnotationMemberValue(this);
    }
}
