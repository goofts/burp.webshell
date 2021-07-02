package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.SignatureAttribute;

public class ClassMemberValue extends MemberValue {
    int valueIndex;

    public ClassMemberValue(int index, ConstPool cp) {
        super('c', cp);
        this.valueIndex = index;
    }

    public ClassMemberValue(String className, ConstPool cp) {
        super('c', cp);
        setValue(className);
    }

    public ClassMemberValue(ConstPool cp) {
        super('c', cp);
        setValue("java.lang.Class");
    }

    /* access modifiers changed from: package-private */
    @Override // javassist.bytecode.annotation.MemberValue
    public Object getValue(ClassLoader cl, ClassPool cp, Method m) throws ClassNotFoundException {
        String classname = getValue();
        if (classname.equals("void")) {
            return Void.TYPE;
        }
        if (classname.equals("int")) {
            return Integer.TYPE;
        }
        if (classname.equals("byte")) {
            return Byte.TYPE;
        }
        if (classname.equals("long")) {
            return Long.TYPE;
        }
        if (classname.equals("double")) {
            return Double.TYPE;
        }
        if (classname.equals("float")) {
            return Float.TYPE;
        }
        if (classname.equals("char")) {
            return Character.TYPE;
        }
        if (classname.equals("short")) {
            return Short.TYPE;
        }
        if (classname.equals("boolean")) {
            return Boolean.TYPE;
        }
        return loadClass(cl, classname);
    }

    /* access modifiers changed from: package-private */
    @Override // javassist.bytecode.annotation.MemberValue
    public Class<?> getType(ClassLoader cl) throws ClassNotFoundException {
        return loadClass(cl, "java.lang.Class");
    }

    public String getValue() {
        try {
            return SignatureAttribute.toTypeSignature(this.cp.getUtf8Info(this.valueIndex)).jvmTypeName();
        } catch (BadBytecode e) {
            throw new RuntimeException(e);
        }
    }

    public void setValue(String newClassName) {
        this.valueIndex = this.cp.addUtf8Info(Descriptor.of(newClassName));
    }

    public String toString() {
        return getValue().replace('$', '.') + ".class";
    }

    @Override // javassist.bytecode.annotation.MemberValue
    public void write(AnnotationsWriter writer) throws IOException {
        writer.classInfoIndex(this.cp.getUtf8Info(this.valueIndex));
    }

    @Override // javassist.bytecode.annotation.MemberValue
    public void accept(MemberValueVisitor visitor) {
        visitor.visitClassMemberValue(this);
    }
}
