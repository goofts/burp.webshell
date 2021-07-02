package javassist;

import java.util.HashMap;
import javassist.bytecode.Descriptor;

public class ClassMap extends HashMap<String, String> {
    private static final long serialVersionUID = 1;
    private ClassMap parent;

    public ClassMap() {
        this.parent = null;
    }

    ClassMap(ClassMap map) {
        this.parent = map;
    }

    public void put(CtClass oldname, CtClass newname) {
        put(oldname.getName(), newname.getName());
    }

    public String put(String oldname, String newname) {
        if (oldname == newname) {
            return oldname;
        }
        String oldname2 = toJvmName(oldname);
        String s = get((Object) oldname2);
        if (s == null || !s.equals(oldname2)) {
            return (String) super.put((Object) oldname2, (Object) toJvmName(newname));
        }
        return s;
    }

    public void putIfNone(String oldname, String newname) {
        if (oldname != newname) {
            String oldname2 = toJvmName(oldname);
            if (get((Object) oldname2) == null) {
                super.put((Object) oldname2, (Object) toJvmName(newname));
            }
        }
    }

    /* access modifiers changed from: protected */
    public final String put0(String oldname, String newname) {
        return (String) super.put((Object) oldname, (Object) newname);
    }

    @Override // java.util.AbstractMap, java.util.Map, java.util.HashMap
    public String get(Object jvmClassName) {
        String found = (String) super.get(jvmClassName);
        if (found != null || this.parent == null) {
            return found;
        }
        return this.parent.get(jvmClassName);
    }

    public void fix(CtClass clazz) {
        fix(clazz.getName());
    }

    public void fix(String name) {
        String name2 = toJvmName(name);
        super.put((Object) name2, (Object) name2);
    }

    public static String toJvmName(String classname) {
        return Descriptor.toJvmName(classname);
    }

    public static String toJavaName(String classname) {
        return Descriptor.toJavaName(classname);
    }
}
