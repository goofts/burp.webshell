package javassist;

import java.io.InputStream;
import java.net.URL;

public class ClassClassPath implements ClassPath {
    private Class<?> thisClass;

    public ClassClassPath(Class<?> c) {
        this.thisClass = c;
    }

    ClassClassPath() {
        this(Object.class);
    }

    @Override // javassist.ClassPath
    public InputStream openClassfile(String classname) throws NotFoundException {
        return this.thisClass.getResourceAsStream('/' + classname.replace('.', '/') + ".class");
    }

    @Override // javassist.ClassPath
    public URL find(String classname) {
        return this.thisClass.getResource('/' + classname.replace('.', '/') + ".class");
    }

    public String toString() {
        return this.thisClass.getName() + ".class";
    }
}
