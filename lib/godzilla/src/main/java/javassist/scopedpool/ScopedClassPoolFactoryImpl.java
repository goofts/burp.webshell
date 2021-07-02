package javassist.scopedpool;

import javassist.ClassPool;

public class ScopedClassPoolFactoryImpl implements ScopedClassPoolFactory {
    @Override // javassist.scopedpool.ScopedClassPoolFactory
    public ScopedClassPool create(ClassLoader cl, ClassPool src, ScopedClassPoolRepository repository) {
        return new ScopedClassPool(cl, src, repository, false);
    }

    @Override // javassist.scopedpool.ScopedClassPoolFactory
    public ScopedClassPool create(ClassPool src, ScopedClassPoolRepository repository) {
        return new ScopedClassPool(null, src, repository, true);
    }
}
