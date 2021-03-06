package javassist.tools.reflect;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Metaobject implements Serializable {
    private static final long serialVersionUID = 1;
    protected Metalevel baseobject;
    protected ClassMetaobject classmetaobject;
    protected Method[] methods;

    public Metaobject(Object self, Object[] args) {
        this.baseobject = (Metalevel) self;
        this.classmetaobject = this.baseobject._getClass();
        this.methods = this.classmetaobject.getReflectiveMethods();
    }

    protected Metaobject() {
        this.baseobject = null;
        this.classmetaobject = null;
        this.methods = null;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeObject(this.baseobject);
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        this.baseobject = (Metalevel) in.readObject();
        this.classmetaobject = this.baseobject._getClass();
        this.methods = this.classmetaobject.getReflectiveMethods();
    }

    public final ClassMetaobject getClassMetaobject() {
        return this.classmetaobject;
    }

    public final Object getObject() {
        return this.baseobject;
    }

    public final void setObject(Object self) {
        this.baseobject = (Metalevel) self;
        this.classmetaobject = this.baseobject._getClass();
        this.methods = this.classmetaobject.getReflectiveMethods();
        this.baseobject._setMetaobject(this);
    }

    public final String getMethodName(int identifier) {
        int j;
        String mname = this.methods[identifier].getName();
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
        return this.methods[identifier].getParameterTypes();
    }

    public final Class<?> getReturnType(int identifier) {
        return this.methods[identifier].getReturnType();
    }

    public Object trapFieldRead(String name) {
        try {
            return getClassMetaobject().getJavaClass().getField(name).get(getObject());
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e.toString());
        } catch (IllegalAccessException e2) {
            throw new RuntimeException(e2.toString());
        }
    }

    public void trapFieldWrite(String name, Object value) {
        try {
            getClassMetaobject().getJavaClass().getField(name).set(getObject(), value);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e.toString());
        } catch (IllegalAccessException e2) {
            throw new RuntimeException(e2.toString());
        }
    }

    public Object trapMethodcall(int identifier, Object[] args) throws Throwable {
        try {
            return this.methods[identifier].invoke(getObject(), args);
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (IllegalAccessException e2) {
            throw new CannotInvokeException(e2);
        }
    }
}
