package javassist.util.proxy;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;

public class ProxyObjectInputStream extends ObjectInputStream {
    private ClassLoader loader = Thread.currentThread().getContextClassLoader();

    public ProxyObjectInputStream(InputStream in) throws IOException {
        super(in);
        if (this.loader == null) {
            this.loader = ClassLoader.getSystemClassLoader();
        }
    }

    public void setClassLoader(ClassLoader loader2) {
        if (loader2 != null) {
            this.loader = loader2;
        } else {
            ClassLoader.getSystemClassLoader();
        }
    }

    /* access modifiers changed from: protected */
    @Override // java.io.ObjectInputStream
    public ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        if (!readBoolean()) {
            return super.readClassDescriptor();
        }
        Class<?> superClass = this.loader.loadClass((String) readObject());
        int length = readInt();
        Class<?>[] interfaces = new Class[length];
        for (int i = 0; i < length; i++) {
            interfaces[i] = this.loader.loadClass((String) readObject());
        }
        byte[] signature = new byte[readInt()];
        read(signature);
        ProxyFactory factory = new ProxyFactory();
        factory.setUseCache(true);
        factory.setUseWriteReplace(false);
        factory.setSuperclass(superClass);
        factory.setInterfaces(interfaces);
        return ObjectStreamClass.lookup(factory.createClass(signature));
    }
}
