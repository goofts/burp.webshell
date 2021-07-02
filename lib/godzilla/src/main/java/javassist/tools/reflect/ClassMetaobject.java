package javassist.tools.reflect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class ClassMetaobject implements Serializable {
    static final String methodPrefix = "_m_";
    static final int methodPrefixLen = 3;
    private static final long serialVersionUID = 1;
    public static boolean useContextClassLoader = false;
    private Constructor<?>[] constructors;
    private Class<?> javaClass;
    private Method[] methods;

    public ClassMetaobject(String[] params) {
        try {
            this.javaClass = getClassObject(params[0]);
            this.constructors = this.javaClass.getConstructors();
            this.methods = null;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("not found: " + params[0] + ", useContextClassLoader: " + Boolean.toString(useContextClassLoader), e);
        }
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeUTF(this.javaClass.getName());
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.javaClass = getClassObject(in.readUTF());
        this.constructors = this.javaClass.getConstructors();
        this.methods = null;
    }

    private Class<?> getClassObject(String name) throws ClassNotFoundException {
        if (useContextClassLoader) {
            return Thread.currentThread().getContextClassLoader().loadClass(name);
        }
        return Class.forName(name);
    }

    public final Class<?> getJavaClass() {
        return this.javaClass;
    }

    public final String getName() {
        return this.javaClass.getName();
    }

    public final boolean isInstance(Object obj) {
        return this.javaClass.isInstance(obj);
    }

    public final Object newInstance(Object[] args) throws CannotCreateException {
        for (int i = 0; i < this.constructors.length; i++) {
            try {
                return this.constructors[i].newInstance(args);
            } catch (IllegalArgumentException e) {
            } catch (InstantiationException e2) {
                throw new CannotCreateException(e2);
            } catch (IllegalAccessException e3) {
                throw new CannotCreateException(e3);
            } catch (InvocationTargetException e4) {
                throw new CannotCreateException(e4);
            }
        }
        throw new CannotCreateException("no constructor matches");
    }

    public Object trapFieldRead(String name) {
        try {
            return getJavaClass().getField(name).get(null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e.toString());
        } catch (IllegalAccessException e2) {
            throw new RuntimeException(e2.toString());
        }
    }

    public void trapFieldWrite(String name, Object value) {
        try {
            getJavaClass().getField(name).set(null, value);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e.toString());
        } catch (IllegalAccessException e2) {
            throw new RuntimeException(e2.toString());
        }
    }

    public static Object invoke(Object target, int identifier, Object[] args) throws Throwable {
        Method[] allmethods = target.getClass().getMethods();
        int n = allmethods.length;
        String head = methodPrefix + identifier;
        for (int i = 0; i < n; i++) {
            if (allmethods[i].getName().startsWith(head)) {
                try {
                    return allmethods[i].invoke(target, args);
                } catch (InvocationTargetException e) {
                    throw e.getTargetException();
                } catch (IllegalAccessException e2) {
                    throw new CannotInvokeException(e2);
                }
            }
        }
        throw new CannotInvokeException("cannot find a method");
    }

    public Object trapMethodcall(int identifier, Object[] args) throws Throwable {
        try {
            return getReflectiveMethods()[identifier].invoke(null, args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (IllegalAccessException e2) {
            throw new CannotInvokeException(e2);
        }
    }

    public final Method[] getReflectiveMethods() {
        if (this.methods != null) {
            return this.methods;
        }
        Method[] allmethods = getJavaClass().getDeclaredMethods();
        int n = allmethods.length;
        int[] index = new int[n];
        int max = 0;
        for (int i = 0; i < n; i++) {
            String mname = allmethods[i].getName();
            if (mname.startsWith(methodPrefix)) {
                int k = 0;
                int j = 3;
                while (true) {
                    char c = mname.charAt(j);
                    if ('0' > c || c > '9') {
                        int k2 = k + 1;
                        index[i] = k2;
                    } else {
                        k = ((k * 10) + c) - 48;
                        j++;
                    }
                }
                int k22 = k + 1;
                index[i] = k22;
                if (k22 > max) {
                    max = k22;
                }
            }
        }
        this.methods = new Method[max];
        for (int i2 = 0; i2 < n; i2++) {
            if (index[i2] > 0) {
                this.methods[index[i2] - 1] = allmethods[i2];
            }
        }
        return this.methods;
    }

    public final Method getMethod(int identifier) {
        return getReflectiveMethods()[identifier];
    }

    public final String getMethodName(int identifier) {
        int j;
        String mname = getReflectiveMethods()[identifier].getName();
        int j2 = 3;
        while (true) {
            j = j2 + 1;
            char c = mname.charAt(j2);
            if (c >= '0' && '9' >= c) {
                j2 = j;
            }
        }
        return mname.substring(j);
    }

    public final Class<?>[] getParameterTypes(int identifier) {
        return getReflectiveMethods()[identifier].getParameterTypes();
    }

    public final Class<?> getReturnType(int identifier) {
        return getReflectiveMethods()[identifier].getReturnType();
    }

    public final int getMethodIndex(String originalName, Class<?>[] argTypes) throws NoSuchMethodException {
        Method[] mthds = getReflectiveMethods();
        for (int i = 0; i < mthds.length; i++) {
            if (mthds[i] != null && getMethodName(i).equals(originalName) && Arrays.equals(argTypes, mthds[i].getParameterTypes())) {
                return i;
            }
        }
        throw new NoSuchMethodException("Method " + originalName + " not found");
    }
}
