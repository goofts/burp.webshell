package javassist.util.proxy;

import java.io.InvalidClassException;
import java.io.InvalidObjectException;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

class SerializedProxy implements Serializable {
    private static final long serialVersionUID = 1;
    private byte[] filterSignature;
    private MethodHandler handler;
    private String[] interfaces;
    private String superClass;

    SerializedProxy(Class<?> proxy, byte[] sig, MethodHandler h) {
        this.filterSignature = sig;
        this.handler = h;
        this.superClass = proxy.getSuperclass().getName();
        Class<?>[] infs = proxy.getInterfaces();
        int n = infs.length;
        this.interfaces = new String[(n - 1)];
        String setterInf = ProxyObject.class.getName();
        String setterInf2 = Proxy.class.getName();
        for (int i = 0; i < n; i++) {
            String name = infs[i].getName();
            if (!name.equals(setterInf) && !name.equals(setterInf2)) {
                this.interfaces[i] = name;
            }
        }
    }

    /* access modifiers changed from: protected */
    public Class<?> loadClass(final String className) throws ClassNotFoundException {
        try {
            return (Class) AccessController.doPrivileged(new PrivilegedExceptionAction<Class<?>>() {
                /* class javassist.util.proxy.SerializedProxy.AnonymousClass1 */

                @Override // java.security.PrivilegedExceptionAction
                public Class<?> run() throws Exception {
                    return Class.forName(className, true, Thread.currentThread().getContextClassLoader());
                }
            });
        } catch (PrivilegedActionException pae) {
            throw new RuntimeException("cannot load the class: " + className, pae.getException());
        }
    }

    /* access modifiers changed from: package-private */
    public Object readResolve() throws ObjectStreamException {
        try {
            int n = this.interfaces.length;
            Class<?>[] infs = new Class[n];
            for (int i = 0; i < n; i++) {
                infs[i] = loadClass(this.interfaces[i]);
            }
            ProxyFactory f = new ProxyFactory();
            f.setSuperclass(loadClass(this.superClass));
            f.setInterfaces(infs);
            Proxy proxy = (Proxy) f.createClass(this.filterSignature).getConstructor(new Class[0]).newInstance(new Object[0]);
            proxy.setHandler(this.handler);
            return proxy;
        } catch (NoSuchMethodException e) {
            throw new InvalidClassException(e.getMessage());
        } catch (InvocationTargetException e2) {
            throw new InvalidClassException(e2.getMessage());
        } catch (ClassNotFoundException e3) {
            throw new InvalidClassException(e3.getMessage());
        } catch (InstantiationException e22) {
            throw new InvalidObjectException(e22.getMessage());
        } catch (IllegalAccessException e32) {
            throw new InvalidClassException(e32.getMessage());
        }
    }
}
