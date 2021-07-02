package javassist.bytecode.annotation;

import java.io.IOException;
import java.lang.reflect.Method;
import javassist.ClassPool;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;

public abstract class MemberValue {
    ConstPool cp;
    char tag;

    public abstract void accept(MemberValueVisitor memberValueVisitor);

    /* access modifiers changed from: package-private */
    public abstract Class<?> getType(ClassLoader classLoader) throws ClassNotFoundException;

    /* access modifiers changed from: package-private */
    public abstract Object getValue(ClassLoader classLoader, ClassPool classPool, Method method) throws ClassNotFoundException;

    public abstract void write(AnnotationsWriter annotationsWriter) throws IOException;

    MemberValue(char tag2, ConstPool cp2) {
        this.cp = cp2;
        this.tag = tag2;
    }

    static Class<?> loadClass(ClassLoader cl, String classname) throws ClassNotFoundException, NoSuchClassError {
        try {
            return Class.forName(convertFromArray(classname), true, cl);
        } catch (LinkageError e) {
            throw new NoSuchClassError(classname, e);
        }
    }

    private static String convertFromArray(String classname) {
        int index = classname.indexOf("[]");
        if (index == -1) {
            return classname;
        }
        StringBuffer sb = new StringBuffer(Descriptor.of(classname.substring(0, index)));
        while (index != -1) {
            sb.insert(0, "[");
            index = classname.indexOf("[]", index + 1);
        }
        return sb.toString().replace('/', '.');
    }
}
