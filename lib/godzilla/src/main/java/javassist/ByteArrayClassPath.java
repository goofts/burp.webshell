package javassist;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

public class ByteArrayClassPath implements ClassPath {
    protected byte[] classfile;
    protected String classname;

    public ByteArrayClassPath(String name, byte[] classfile2) {
        this.classname = name;
        this.classfile = classfile2;
    }

    public String toString() {
        return "byte[]:" + this.classname;
    }

    @Override // javassist.ClassPath
    public InputStream openClassfile(String classname2) {
        if (this.classname.equals(classname2)) {
            return new ByteArrayInputStream(this.classfile);
        }
        return null;
    }

    @Override // javassist.ClassPath
    public URL find(String classname2) {
        if (this.classname.equals(classname2)) {
            try {
                return new URL((URL) null, "file:/ByteArrayClassPath/" + (classname2.replace('.', '/') + ".class"), new BytecodeURLStreamHandler());
            } catch (MalformedURLException e) {
            }
        }
        return null;
    }

    private class BytecodeURLStreamHandler extends URLStreamHandler {
        private BytecodeURLStreamHandler() {
        }

        /* access modifiers changed from: protected */
        @Override // java.net.URLStreamHandler
        public URLConnection openConnection(URL u) {
            return new BytecodeURLConnection(u);
        }
    }

    private class BytecodeURLConnection extends URLConnection {
        protected BytecodeURLConnection(URL url) {
            super(url);
        }

        @Override // java.net.URLConnection
        public void connect() throws IOException {
        }

        @Override // java.net.URLConnection
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(ByteArrayClassPath.this.classfile);
        }

        public int getContentLength() {
            return ByteArrayClassPath.this.classfile.length;
        }
    }
}
