package javassist.tools.reflect;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.NotFoundException;

public class Loader extends javassist.Loader {
    protected Reflection reflection = new Reflection();

    public static void main(String[] args) throws Throwable {
        new Loader().run(args);
    }

    public Loader() throws CannotCompileException, NotFoundException {
        delegateLoadingOf("javassist.tools.reflect.Loader");
        addTranslator(ClassPool.getDefault(), this.reflection);
    }

    public boolean makeReflective(String clazz, String metaobject, String metaclass) throws CannotCompileException, NotFoundException {
        return this.reflection.makeReflective(clazz, metaobject, metaclass);
    }
}
