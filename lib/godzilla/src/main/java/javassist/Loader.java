package javassist;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.security.ProtectionDomain;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;
import javassist.bytecode.ClassFile;

public class Loader extends ClassLoader {
    public boolean doDelegation;
    private ProtectionDomain domain;
    private HashMap<String, ClassLoader> notDefinedHere;
    private Vector<String> notDefinedPackages;
    private ClassPool source;
    private Translator translator;

    public static class Simple extends ClassLoader {
        public Simple() {
        }

        public Simple(ClassLoader parent) {
            super(parent);
        }

        public Class<?> invokeDefineClass(CtClass cc) throws IOException, CannotCompileException {
            byte[] code = cc.toBytecode();
            return defineClass(cc.getName(), code, 0, code.length);
        }
    }

    public Loader() {
        this(null);
    }

    public Loader(ClassPool cp) {
        this.doDelegation = true;
        init(cp);
    }

    public Loader(ClassLoader parent, ClassPool cp) {
        super(parent);
        this.doDelegation = true;
        init(cp);
    }

    private void init(ClassPool cp) {
        this.notDefinedHere = new HashMap<>();
        this.notDefinedPackages = new Vector<>();
        this.source = cp;
        this.translator = null;
        this.domain = null;
        delegateLoadingOf("javassist.Loader");
    }

    public void delegateLoadingOf(String classname) {
        if (classname.endsWith(".")) {
            this.notDefinedPackages.addElement(classname);
        } else {
            this.notDefinedHere.put(classname, this);
        }
    }

    public void setDomain(ProtectionDomain d) {
        this.domain = d;
    }

    public void setClassPool(ClassPool cp) {
        this.source = cp;
    }

    public void addTranslator(ClassPool cp, Translator t) throws NotFoundException, CannotCompileException {
        this.source = cp;
        this.translator = t;
        t.start(cp);
    }

    public static void main(String[] args) throws Throwable {
        new Loader().run(args);
    }

    public void run(String[] args) throws Throwable {
        if (args.length >= 1) {
            run(args[0], (String[]) Arrays.copyOfRange(args, 1, args.length));
        }
    }

    public void run(String classname, String[] args) throws Throwable {
        try {
            loadClass(classname).getDeclaredMethod("main", String[].class).invoke(null, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        }
    }

    /* access modifiers changed from: protected */
    @Override // java.lang.ClassLoader
    public Class<?> loadClass(String name, boolean resolve) throws ClassFormatError, ClassNotFoundException {
        Class<?> c;
        String name2 = name.intern();
        synchronized (name2) {
            c = findLoadedClass(name2);
            if (c == null) {
                c = loadClassByDelegation(name2);
            }
            if (c == null) {
                c = findClass(name2);
            }
            if (c == null) {
                c = delegateToParent(name2);
            }
            if (resolve) {
                resolveClass(c);
            }
        }
        return c;
    }

    /* access modifiers changed from: protected */
    @Override // java.lang.ClassLoader
    public Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classfile;
        try {
            if (this.source != null) {
                if (this.translator != null) {
                    this.translator.onLoad(this.source, name);
                }
                try {
                    classfile = this.source.get(name).toBytecode();
                } catch (NotFoundException e) {
                    return null;
                }
            } else {
                InputStream in = getClass().getResourceAsStream("/" + name.replace('.', '/') + ".class");
                if (in == null) {
                    return null;
                }
                classfile = ClassPoolTail.readStream(in);
            }
            int i = name.lastIndexOf(46);
            if (i != -1) {
                String pname = name.substring(0, i);
                if (isDefinedPackage(pname)) {
                    try {
                        definePackage(pname, null, null, null, null, null, null, null);
                    } catch (IllegalArgumentException e2) {
                    }
                }
            }
            if (this.domain == null) {
                return defineClass(name, classfile, 0, classfile.length);
            }
            return defineClass(name, classfile, 0, classfile.length, this.domain);
        } catch (Exception e3) {
            throw new ClassNotFoundException("caught an exception while obtaining a class file for " + name, e3);
        }
    }

    private boolean isDefinedPackage(String name) {
        return ClassFile.MAJOR_VERSION >= 53 ? getDefinedPackage(name) == null : getPackage(name) == null;
    }

    /* access modifiers changed from: protected */
    public Class<?> loadClassByDelegation(String name) throws ClassNotFoundException {
        if (!this.doDelegation) {
            return null;
        }
        if (name.startsWith("java.") || name.startsWith("javax.") || name.startsWith("sun.") || name.startsWith("com.sun.") || name.startsWith("org.w3c.") || name.startsWith("org.xml.") || notDelegated(name)) {
            return delegateToParent(name);
        }
        return null;
    }

    private boolean notDelegated(String name) {
        if (this.notDefinedHere.containsKey(name)) {
            return true;
        }
        Iterator<String> it = this.notDefinedPackages.iterator();
        while (it.hasNext()) {
            if (name.startsWith(it.next())) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public Class<?> delegateToParent(String classname) throws ClassNotFoundException {
        ClassLoader cl = getParent();
        if (cl != null) {
            return cl.loadClass(classname);
        }
        return findSystemClass(classname);
    }
}
