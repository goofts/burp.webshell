package javassist.scopedpool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import javassist.ClassPool;
import javassist.LoaderClassPath;

public class ScopedClassPoolRepositoryImpl implements ScopedClassPoolRepository {
    private static final ScopedClassPoolRepositoryImpl instance = new ScopedClassPoolRepositoryImpl();
    protected ClassPool classpool = ClassPool.getDefault();
    protected ScopedClassPoolFactory factory = new ScopedClassPoolFactoryImpl();
    private boolean prune = true;
    boolean pruneWhenCached;
    protected Map<ClassLoader, ScopedClassPool> registeredCLs = Collections.synchronizedMap(new WeakHashMap());

    public static ScopedClassPoolRepository getInstance() {
        return instance;
    }

    private ScopedClassPoolRepositoryImpl() {
        this.classpool.insertClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
    }

    @Override // javassist.scopedpool.ScopedClassPoolRepository
    public boolean isPrune() {
        return this.prune;
    }

    @Override // javassist.scopedpool.ScopedClassPoolRepository
    public void setPrune(boolean prune2) {
        this.prune = prune2;
    }

    @Override // javassist.scopedpool.ScopedClassPoolRepository
    public ScopedClassPool createScopedClassPool(ClassLoader cl, ClassPool src) {
        return this.factory.create(cl, src, this);
    }

    @Override // javassist.scopedpool.ScopedClassPoolRepository
    public ClassPool findClassPool(ClassLoader cl) {
        if (cl == null) {
            return registerClassLoader(ClassLoader.getSystemClassLoader());
        }
        return registerClassLoader(cl);
    }

    @Override // javassist.scopedpool.ScopedClassPoolRepository
    public ClassPool registerClassLoader(ClassLoader ucl) {
        synchronized (this.registeredCLs) {
            if (this.registeredCLs.containsKey(ucl)) {
                return this.registeredCLs.get(ucl);
            }
            ScopedClassPool pool = createScopedClassPool(ucl, this.classpool);
            this.registeredCLs.put(ucl, pool);
            return pool;
        }
    }

    @Override // javassist.scopedpool.ScopedClassPoolRepository
    public Map<ClassLoader, ScopedClassPool> getRegisteredCLs() {
        clearUnregisteredClassLoaders();
        return this.registeredCLs;
    }

    @Override // javassist.scopedpool.ScopedClassPoolRepository
    public void clearUnregisteredClassLoaders() {
        List<ClassLoader> toUnregister = null;
        synchronized (this.registeredCLs) {
            try {
                for (Map.Entry<ClassLoader, ScopedClassPool> reg : this.registeredCLs.entrySet()) {
                    try {
                        if (reg.getValue().isUnloadedClassLoader()) {
                            ClassLoader cl = reg.getValue().getClassLoader();
                            if (cl != null) {
                                if (toUnregister == null) {
                                    toUnregister = new ArrayList<>();
                                } else {
                                    toUnregister = toUnregister;
                                }
                                toUnregister.add(cl);
                            } else {
                                toUnregister = toUnregister;
                            }
                            this.registeredCLs.remove(reg.getKey());
                        } else {
                            toUnregister = toUnregister;
                        }
                    } catch (Throwable th) {
                        th = th;
                        throw th;
                    }
                }
                if (toUnregister != null) {
                    for (ClassLoader cl2 : toUnregister) {
                        unregisterClassLoader(cl2);
                    }
                }
            } catch (Throwable th2) {
                th = th2;
            }
        }
    }

    @Override // javassist.scopedpool.ScopedClassPoolRepository
    public void unregisterClassLoader(ClassLoader cl) {
        synchronized (this.registeredCLs) {
            ScopedClassPool pool = this.registeredCLs.remove(cl);
            if (pool != null) {
                pool.close();
            }
        }
    }

    public void insertDelegate(ScopedClassPoolRepository delegate) {
    }

    @Override // javassist.scopedpool.ScopedClassPoolRepository
    public void setClassPoolFactory(ScopedClassPoolFactory factory2) {
        this.factory = factory2;
    }

    @Override // javassist.scopedpool.ScopedClassPoolRepository
    public ScopedClassPoolFactory getClassPoolFactory() {
        return this.factory;
    }
}
