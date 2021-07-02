package javassist.scopedpool;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.security.ProtectionDomain;
import java.util.Map;
import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.LoaderClassPath;
import javassist.NotFoundException;

public class ScopedClassPool extends ClassPool {
    protected Reference<ClassLoader> classLoader;
    protected LoaderClassPath classPath;
    boolean isBootstrapCl;
    protected ScopedClassPoolRepository repository;
    protected Map<String, CtClass> softcache;

    static {
        ClassPool.doPruning = false;
        ClassPool.releaseUnmodifiedClassFile = false;
    }

    protected ScopedClassPool(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository2) {
        this(cl, src, repository2, false);
    }

    protected ScopedClassPool(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository2, boolean isTemp) {
        super(src);
        this.softcache = new SoftValueHashMap();
        this.isBootstrapCl = true;
        this.repository = repository2;
        this.classLoader = new WeakReference(cl);
        if (cl != null) {
            this.classPath = new LoaderClassPath(cl);
            insertClassPath(this.classPath);
        }
        this.childFirstLookup = true;
        if (!isTemp && cl == null) {
            this.isBootstrapCl = true;
        }
    }

    @Override // javassist.ClassPool
    public ClassLoader getClassLoader() {
        ClassLoader cl = getClassLoader0();
        if (cl != null || this.isBootstrapCl) {
            return cl;
        }
        throw new IllegalStateException("ClassLoader has been garbage collected");
    }

    /* access modifiers changed from: protected */
    public ClassLoader getClassLoader0() {
        return this.classLoader.get();
    }

    public void close() {
        removeClassPath(this.classPath);
        this.classes.clear();
        this.softcache.clear();
    }

    public synchronized void flushClass(String classname) {
        this.classes.remove(classname);
        this.softcache.remove(classname);
    }

    public synchronized void soften(CtClass clazz) {
        if (this.repository.isPrune()) {
            clazz.prune();
        }
        this.classes.remove(clazz.getName());
        this.softcache.put(clazz.getName(), clazz);
    }

    public boolean isUnloadedClassLoader() {
        return false;
    }

    /* access modifiers changed from: protected */
    @Override // javassist.ClassPool
    public CtClass getCached(String classname) {
        CtClass clazz = getCachedLocally(classname);
        if (clazz == null) {
            boolean isLocal = false;
            ClassLoader dcl = getClassLoader0();
            if (dcl != null) {
                int lastIndex = classname.lastIndexOf(36);
                isLocal = dcl.getResource(lastIndex < 0 ? classname.replaceAll("[\\.]", "/") + ".class" : classname.substring(0, lastIndex).replaceAll("[\\.]", "/") + classname.substring(lastIndex) + ".class") != null;
            }
            if (!isLocal) {
                Map<ClassLoader, ScopedClassPool> registeredCLs = this.repository.getRegisteredCLs();
                synchronized (registeredCLs) {
                    for (ScopedClassPool pool : registeredCLs.values()) {
                        if (pool.isUnloadedClassLoader()) {
                            this.repository.unregisterClassLoader(pool.getClassLoader());
                        } else {
                            clazz = pool.getCachedLocally(classname);
                            if (clazz != null) {
                                return clazz;
                            }
                        }
                    }
                }
            }
        }
        return clazz;
    }

    /* access modifiers changed from: protected */
    @Override // javassist.ClassPool
    public void cacheCtClass(String classname, CtClass c, boolean dynamic) {
        if (dynamic) {
            super.cacheCtClass(classname, c, dynamic);
            return;
        }
        if (this.repository.isPrune()) {
            c.prune();
        }
        this.softcache.put(classname, c);
    }

    public void lockInCache(CtClass c) {
        super.cacheCtClass(c.getName(), c, false);
    }

    /* access modifiers changed from: protected */
    public CtClass getCachedLocally(String classname) {
        CtClass cached;
        CtClass cached2 = (CtClass) this.classes.get(classname);
        if (cached2 != null) {
            return cached2;
        }
        synchronized (this.softcache) {
            cached = this.softcache.get(classname);
        }
        return cached;
    }

    public synchronized CtClass getLocally(String classname) throws NotFoundException {
        CtClass clazz;
        this.softcache.remove(classname);
        clazz = (CtClass) this.classes.get(classname);
        if (clazz == null) {
            clazz = createCtClass(classname, true);
            if (clazz == null) {
                throw new NotFoundException(classname);
            }
            super.cacheCtClass(classname, clazz, false);
        }
        return clazz;
    }

    @Override // javassist.ClassPool
    public Class<?> toClass(CtClass ct, ClassLoader loader, ProtectionDomain domain) throws CannotCompileException {
        lockInCache(ct);
        return super.toClass(ct, getClassLoader0(), domain);
    }
}
