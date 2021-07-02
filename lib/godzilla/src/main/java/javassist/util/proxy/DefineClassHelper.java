package javassist.util.proxy;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.security.ProtectionDomain;
import java.util.List;
import javassist.CannotCompileException;
import javassist.bytecode.ClassFile;
import javassist.util.proxy.SecurityActions;

public class DefineClassHelper {
    private static final Helper privileged;

    /* access modifiers changed from: private */
    public static abstract class Helper {
        /* access modifiers changed from: package-private */
        public abstract Class<?> defineClass(String str, byte[] bArr, int i, int i2, Class<?> cls, ClassLoader classLoader, ProtectionDomain protectionDomain) throws ClassFormatError, CannotCompileException;

        private Helper() {
        }
    }

    private static class Java11 extends JavaOther {
        private Java11() {
            super();
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.util.proxy.DefineClassHelper.JavaOther, javassist.util.proxy.DefineClassHelper.Helper
        public Class<?> defineClass(String name, byte[] bcode, int off, int len, Class<?> neighbor, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError, CannotCompileException {
            if (neighbor != null) {
                return DefineClassHelper.toClass(neighbor, bcode);
            }
            return super.defineClass(name, bcode, off, len, neighbor, loader, protectionDomain);
        }
    }

    /* access modifiers changed from: private */
    public static class Java9 extends Helper {
        private final Method getCallerClass;
        private final Object stack;
        private final ReferencedUnsafe sunMiscUnsafe = getReferencedUnsafe();

        /* access modifiers changed from: package-private */
        public final class ReferencedUnsafe {
            private final MethodHandle defineClass;
            private final SecurityActions.TheUnsafe sunMiscUnsafeTheUnsafe;

            ReferencedUnsafe(SecurityActions.TheUnsafe usf, MethodHandle meth) {
                this.sunMiscUnsafeTheUnsafe = usf;
                this.defineClass = meth;
            }

            /* access modifiers changed from: package-private */
            public Class<?> defineClass(String name, byte[] b, int off, int len, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError {
                try {
                    if (Java9.this.getCallerClass.invoke(Java9.this.stack, new Object[0]) != Java9.class) {
                        throw new IllegalAccessError("Access denied for caller.");
                    }
                    try {
                        return (Class) this.defineClass.invokeWithArguments(this.sunMiscUnsafeTheUnsafe.theUnsafe, name, b, Integer.valueOf(off), Integer.valueOf(len), loader, protectionDomain);
                    } catch (Throwable e) {
                        if (e instanceof RuntimeException) {
                            throw ((RuntimeException) e);
                        } else if (e instanceof ClassFormatError) {
                            throw ((ClassFormatError) e);
                        } else {
                            throw new ClassFormatError(e.getMessage());
                        }
                    }
                } catch (Exception e2) {
                    throw new RuntimeException("cannot initialize", e2);
                }
            }
        }

        /* JADX DEBUG: Multi-variable search result rejected for r7v1, resolved type: java.lang.Object[] */
        /* JADX WARN: Multi-variable type inference failed */
        Java9() {
            super();
            Class<?> stackWalkerClass = null;
            try {
                stackWalkerClass = Class.forName("java.lang.StackWalker");
            } catch (ClassNotFoundException e) {
            }
            if (stackWalkerClass != null) {
                try {
                    Class<?> optionClass = Class.forName("java.lang.StackWalker$Option");
                    this.stack = stackWalkerClass.getMethod("getInstance", optionClass).invoke(null, optionClass.getEnumConstants()[0]);
                    this.getCallerClass = stackWalkerClass.getMethod("getCallerClass", new Class[0]);
                } catch (Throwable e2) {
                    throw new RuntimeException("cannot initialize", e2);
                }
            } else {
                this.stack = null;
                this.getCallerClass = null;
            }
        }

        private final ReferencedUnsafe getReferencedUnsafe() {
            try {
                if (DefineClassHelper.privileged == null || this.getCallerClass.invoke(this.stack, new Object[0]) == getClass()) {
                    try {
                        SecurityActions.TheUnsafe usf = SecurityActions.getSunMiscUnsafeAnonymously();
                        List<Method> defineClassMethod = usf.methods.get("defineClass");
                        if (defineClassMethod == null) {
                            return null;
                        }
                        return new ReferencedUnsafe(usf, MethodHandles.lookup().unreflect(defineClassMethod.get(0)));
                    } catch (Throwable e) {
                        throw new RuntimeException("cannot initialize", e);
                    }
                } else {
                    throw new IllegalAccessError("Access denied for caller.");
                }
            } catch (Exception e2) {
                throw new RuntimeException("cannot initialize", e2);
            }
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.util.proxy.DefineClassHelper.Helper
        public Class<?> defineClass(String name, byte[] b, int off, int len, Class<?> cls, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError {
            try {
                if (this.getCallerClass.invoke(this.stack, new Object[0]) == DefineClassHelper.class) {
                    return this.sunMiscUnsafe.defineClass(name, b, off, len, loader, protectionDomain);
                }
                throw new IllegalAccessError("Access denied for caller.");
            } catch (Exception e) {
                throw new RuntimeException("cannot initialize", e);
            }
        }
    }

    private static class Java7 extends Helper {
        private final MethodHandle defineClass;
        private final SecurityActions stack;

        private Java7() {
            super();
            this.stack = SecurityActions.stack;
            this.defineClass = getDefineClassMethodHandle();
        }

        private final MethodHandle getDefineClassMethodHandle() {
            if (DefineClassHelper.privileged == null || this.stack.getCallerClass() == getClass()) {
                try {
                    return SecurityActions.getMethodHandle(ClassLoader.class, "defineClass", new Class[]{String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class});
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("cannot initialize", e);
                }
            } else {
                throw new IllegalAccessError("Access denied for caller.");
            }
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.util.proxy.DefineClassHelper.Helper
        public Class<?> defineClass(String name, byte[] b, int off, int len, Class<?> cls, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError {
            if (this.stack.getCallerClass() != DefineClassHelper.class) {
                throw new IllegalAccessError("Access denied for caller.");
            }
            try {
                return (Class) this.defineClass.invokeWithArguments(loader, name, b, Integer.valueOf(off), Integer.valueOf(len), protectionDomain);
            } catch (Throwable e) {
                if (e instanceof RuntimeException) {
                    throw ((RuntimeException) e);
                } else if (e instanceof ClassFormatError) {
                    throw ((ClassFormatError) e);
                } else {
                    throw new ClassFormatError(e.getMessage());
                }
            }
        }
    }

    private static class JavaOther extends Helper {
        private final Method defineClass;
        private final SecurityActions stack;

        private JavaOther() {
            super();
            this.defineClass = getDefineClassMethod();
            this.stack = SecurityActions.stack;
        }

        private final Method getDefineClassMethod() {
            if (DefineClassHelper.privileged == null || this.stack.getCallerClass() == getClass()) {
                try {
                    return SecurityActions.getDeclaredMethod(ClassLoader.class, "defineClass", new Class[]{String.class, byte[].class, Integer.TYPE, Integer.TYPE, ProtectionDomain.class});
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException("cannot initialize", e);
                }
            } else {
                throw new IllegalAccessError("Access denied for caller.");
            }
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.util.proxy.DefineClassHelper.Helper
        public Class<?> defineClass(String name, byte[] b, int off, int len, Class<?> cls, ClassLoader loader, ProtectionDomain protectionDomain) throws ClassFormatError, CannotCompileException {
            Class<?> klass = this.stack.getCallerClass();
            if (klass == DefineClassHelper.class || klass == getClass()) {
                try {
                    SecurityActions.setAccessible(this.defineClass, true);
                    return (Class) this.defineClass.invoke(loader, name, b, Integer.valueOf(off), Integer.valueOf(len), protectionDomain);
                } catch (Throwable e) {
                    if (e instanceof ClassFormatError) {
                        throw ((ClassFormatError) e);
                    } else if (e instanceof RuntimeException) {
                        throw ((RuntimeException) e);
                    } else {
                        throw new CannotCompileException(e);
                    }
                }
            } else {
                throw new IllegalAccessError("Access denied for caller.");
            }
        }
    }

    static {
        Helper java7;
        if (ClassFile.MAJOR_VERSION > 54) {
            java7 = new Java11();
        } else if (ClassFile.MAJOR_VERSION >= 53) {
            java7 = new Java9();
        } else {
            java7 = ClassFile.MAJOR_VERSION >= 51 ? new Java7() : new JavaOther();
        }
        privileged = java7;
    }

    public static Class<?> toClass(String className, Class<?> neighbor, ClassLoader loader, ProtectionDomain domain, byte[] bcode) throws CannotCompileException {
        try {
            return privileged.defineClass(className, bcode, 0, bcode.length, neighbor, loader, domain);
        } catch (RuntimeException e) {
            throw e;
        } catch (CannotCompileException e2) {
            throw e2;
        } catch (ClassFormatError e3) {
            e = e3;
            Throwable t = e.getCause();
            if (t != null) {
                e = t;
            }
            throw new CannotCompileException(e);
        } catch (Exception e4) {
            throw new CannotCompileException(e4);
        }
    }

    public static Class<?> toClass(Class<?> neighbor, byte[] bcode) throws CannotCompileException {
        try {
            DefineClassHelper.class.getModule().addReads(neighbor.getModule());
            return MethodHandles.privateLookupIn(neighbor, MethodHandles.lookup()).defineClass(bcode);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new CannotCompileException(e.getMessage() + ": " + neighbor.getName() + " has no permission to define the class");
        }
    }

    public static Class<?> toClass(MethodHandles.Lookup lookup, byte[] bcode) throws CannotCompileException {
        try {
            return lookup.defineClass(bcode);
        } catch (IllegalAccessException | IllegalArgumentException e) {
            throw new CannotCompileException(e.getMessage());
        }
    }

    static Class<?> toPublicClass(String className, byte[] bcode) throws CannotCompileException {
        try {
            return MethodHandles.lookup().dropLookupMode(2).defineClass(bcode);
        } catch (Throwable t) {
            throw new CannotCompileException(t);
        }
    }

    private DefineClassHelper() {
    }
}
