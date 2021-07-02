package javassist;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class URLClassPath implements ClassPath {
    protected String directory;
    protected String hostname;
    protected String packageName;
    protected int port;

    public URLClassPath(String host, int port2, String directory2, String packageName2) {
        this.hostname = host;
        this.port = port2;
        this.directory = directory2;
        this.packageName = packageName2;
    }

    public String toString() {
        return this.hostname + ":" + this.port + this.directory;
    }

    @Override // javassist.ClassPath
    public InputStream openClassfile(String classname) {
        try {
            URLConnection con = openClassfile0(classname);
            if (con != null) {
                return con.getInputStream();
            }
        } catch (IOException e) {
        }
        return null;
    }

    private URLConnection openClassfile0(String classname) throws IOException {
        if (this.packageName != null && !classname.startsWith(this.packageName)) {
            return null;
        }
        return fetchClass0(this.hostname, this.port, this.directory + classname.replace('.', '/') + ".class");
    }

    @Override // javassist.ClassPath
    public URL find(String classname) {
        try {
            URLConnection con = openClassfile0(classname);
            InputStream is = con.getInputStream();
            if (is != null) {
                is.close();
                return con.getURL();
            }
        } catch (IOException e) {
        }
        return null;
    }

    public static byte[] fetchClass(String host, int port2, String directory2, String classname) throws IOException {
        byte[] b;
        URLConnection con = fetchClass0(host, port2, directory2 + classname.replace('.', '/') + ".class");
        int size = con.getContentLength();
        InputStream s = con.getInputStream();
        if (size <= 0) {
            try {
                b = ClassPoolTail.readStream(s);
            } catch (Throwable th) {
                s.close();
                throw th;
            }
        } else {
            b = new byte[size];
            int len = 0;
            do {
                int n = s.read(b, len, size - len);
                if (n < 0) {
                    throw new IOException("the stream was closed: " + classname);
                }
                len += n;
            } while (len < size);
        }
        s.close();
        return b;
    }

    private static URLConnection fetchClass0(String host, int port2, String filename) throws IOException {
        try {
            URLConnection con = new URL("http", host, port2, filename).openConnection();
            con.connect();
            return con;
        } catch (MalformedURLException e) {
            throw new IOException("invalid URL?");
        }
    }
}
