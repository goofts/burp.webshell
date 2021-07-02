package javassist.scopedpool;

import java.util.Map;
import javassist.ClassPool;

public interface ScopedClassPoolRepository {
    void clearUnregisteredClassLoaders();

    ScopedClassPool createScopedClassPool(ClassLoader classLoader, ClassPool classPool);

    ClassPool findClassPool(ClassLoader classLoader);

    ScopedClassPoolFactory getClassPoolFactory();

    Map<ClassLoader, ScopedClassPool> getRegisteredCLs();

    boolean isPrune();

    ClassPool registerClassLoader(ClassLoader classLoader);

    void setClassPoolFactory(ScopedClassPoolFactory scopedClassPoolFactory);

    void setPrune(boolean z);

    void unregisterClassLoader(ClassLoader classLoader);
}
