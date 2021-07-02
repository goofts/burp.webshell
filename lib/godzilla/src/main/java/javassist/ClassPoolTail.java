package javassist;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import javassist.bytecode.ClassFile;

final class ClassPoolTail {
    protected ClassPathList pathList = null;

    public String toString() {
        StringBuffer buf = new StringBuffer();
        buf.append("[class path: ");
        for (ClassPathList list = this.pathList; list != null; list = list.next) {
            buf.append(list.path.toString());
            buf.append(File.pathSeparatorChar);
        }
        buf.append(']');
        return buf.toString();
    }

    public synchronized ClassPath insertClassPath(ClassPath cp) {
        this.pathList = new ClassPathList(cp, this.pathList);
        return cp;
    }

    public synchronized ClassPath appendClassPath(ClassPath cp) {
        ClassPathList tail = new ClassPathList(cp, null);
        ClassPathList list = this.pathList;
        if (list == null) {
            this.pathList = tail;
        } else {
            while (list.next != null) {
                list = list.next;
            }
            list.next = tail;
        }
        return cp;
    }

    public synchronized void removeClassPath(ClassPath cp) {
        ClassPathList list = this.pathList;
        if (list != null) {
            if (list.path == cp) {
                this.pathList = list.next;
            } else {
                while (list.next != null) {
                    if (list.next.path == cp) {
                        list.next = list.next.next;
                    } else {
                        list = list.next;
                    }
                }
            }
        }
    }

    public ClassPath appendSystemPath() {
        if (ClassFile.MAJOR_VERSION < 53) {
            return appendClassPath(new ClassClassPath());
        }
        return appendClassPath(new LoaderClassPath(Thread.currentThread().getContextClassLoader()));
    }

    public ClassPath insertClassPath(String pathname) throws NotFoundException {
        return insertClassPath(makePathObject(pathname));
    }

    public ClassPath appendClassPath(String pathname) throws NotFoundException {
        return appendClassPath(makePathObject(pathname));
    }

    private static ClassPath makePathObject(String pathname) throws NotFoundException {
        String lower = pathname.toLowerCase();
        if (lower.endsWith(".jar") || lower.endsWith(".zip")) {
            return new JarClassPath(pathname);
        }
        int len = pathname.length();
        if (len > 2 && pathname.charAt(len - 1) == '*' && (pathname.charAt(len - 2) == '/' || pathname.charAt(len - 2) == File.separatorChar)) {
            return new JarDirClassPath(pathname.substring(0, len - 2));
        }
        return new DirClassPath(pathname);
    }

    /* access modifiers changed from: package-private */
    public void writeClassfile(String classname, OutputStream out) throws NotFoundException, IOException, CannotCompileException {
        InputStream fin = openClassfile(classname);
        if (fin == null) {
            throw new NotFoundException(classname);
        }
        try {
            copyStream(fin, out);
        } finally {
            fin.close();
        }
    }

    /* access modifiers changed from: package-private */
    public InputStream openClassfile(String classname) throws NotFoundException {
        InputStream ins;
        ClassPathList list = this.pathList;
        InputStream ins2 = null;
        NotFoundException error = null;
        while (list != null) {
            try {
                ins = list.path.openClassfile(classname);
            } catch (NotFoundException e) {
                if (error == null) {
                    error = e;
                    ins = ins2;
                } else {
                    ins = ins2;
                }
            }
            if (ins != null) {
                return ins;
            }
            list = list.next;
            ins2 = ins;
        }
        if (error == null) {
            return null;
        }
        throw error;
    }

    public URL find(String classname) {
        URL url = null;
        for (ClassPathList list = this.pathList; list != null; list = list.next) {
            url = list.path.find(classname);
            if (url != null) {
                return url;
            }
        }
        return null;
    }

    public static byte[] readStream(InputStream fin) throws IOException {
        byte[][] bufs = new byte[8][];
        int bufsize = 4096;
        for (int i = 0; i < 8; i++) {
            bufs[i] = new byte[bufsize];
            int size = 0;
            do {
                int len = fin.read(bufs[i], size, bufsize - size);
                if (len >= 0) {
                    size += len;
                } else {
                    byte[] result = new byte[((bufsize - 4096) + size)];
                    int s = 0;
                    for (int j = 0; j < i; j++) {
                        System.arraycopy(bufs[j], 0, result, s, s + 4096);
                        s = s + s + 4096;
                    }
                    System.arraycopy(bufs[i], 0, result, s, size);
                    return result;
                }
            } while (size < bufsize);
            bufsize *= 2;
        }
        throw new IOException("too much data");
    }

    public static void copyStream(InputStream fin, OutputStream fout) throws IOException {
        int bufsize = 4096;
        byte[] buf = null;
        for (int i = 0; i < 64; i++) {
            if (i < 8) {
                bufsize *= 2;
                buf = new byte[bufsize];
            }
            int size = 0;
            do {
                int len = fin.read(buf, size, bufsize - size);
                if (len >= 0) {
                    size += len;
                } else {
                    fout.write(buf, 0, size);
                    return;
                }
            } while (size < bufsize);
            fout.write(buf);
        }
        throw new IOException("too much data");
    }
}
