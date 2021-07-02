package javassist;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Set;

/* access modifiers changed from: package-private */
/* compiled from: ClassPoolTail */
public final class JarClassPath implements ClassPath {
    Set<String> jarfileEntries;
    String jarfileURL;

    /* JADX WARNING: Removed duplicated region for block: B:13:0x0042 A[SYNTHETIC, Splitter:B:13:0x0042] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x006b A[SYNTHETIC, Splitter:B:23:0x006b] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    JarClassPath(java.lang.String r7) throws javassist.NotFoundException {
        /*
        // Method dump skipped, instructions count: 122
        */
        throw new UnsupportedOperationException("Method not decompiled: javassist.JarClassPath.<init>(java.lang.String):void");
    }

    @Override // javassist.ClassPath
    public InputStream openClassfile(String classname) throws NotFoundException {
        URL jarURL = find(classname);
        if (jarURL == null) {
            return null;
        }
        try {
            if (ClassPool.cacheOpenedJarFile) {
                return jarURL.openConnection().getInputStream();
            }
            URLConnection con = jarURL.openConnection();
            con.setUseCaches(false);
            return con.getInputStream();
        } catch (IOException e) {
            throw new NotFoundException("broken jar file?: " + classname);
        }
    }

    @Override // javassist.ClassPath
    public URL find(String classname) {
        String jarname = classname.replace('.', '/') + ".class";
        if (this.jarfileEntries.contains(jarname)) {
            try {
                return new URL(String.format("jar:%s!/%s", this.jarfileURL, jarname));
            } catch (MalformedURLException e) {
            }
        }
        return null;
    }

    public String toString() {
        return this.jarfileURL == null ? "<null>" : this.jarfileURL.toString();
    }
}
