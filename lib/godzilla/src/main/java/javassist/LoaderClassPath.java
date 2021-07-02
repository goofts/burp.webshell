package javassist;

import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.URL;

public class LoaderClassPath implements ClassPath {
    private Reference<ClassLoader> clref;

    public LoaderClassPath(ClassLoader cl) {
        this.clref = new WeakReference(cl);
    }

    public String toString() {
        return this.clref.get() == null ? "<null>" : this.clref.get().toString();
    }

    @Override // javassist.ClassPath
    public InputStream openClassfile(String classname) throws NotFoundException {
        String cname = classname.replace('.', '/') + ".class";
        ClassLoader cl = this.clref.get();
        if (cl == null) {
            return null;
        }
        return cl.getResourceAsStream(cname);
    }

    @Override // javassist.ClassPath
    public URL find(String classname) {
        String cname = classname.replace('.', '/') + ".class";
        ClassLoader cl = this.clref.get();
        if (cl == null) {
            return null;
        }
        return cl.getResource(cname);
    }
}
