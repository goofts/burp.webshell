package com.kitfox.svg.app.data;

import com.kitfox.svg.SVGConst;
import com.kitfox.svg.util.Base64InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Handler extends URLStreamHandler {

    class Connection extends URLConnection {
        byte[] buf;
        String mime;

        public Connection(URL url) {
            super(url);
            String path = url.getPath();
            int idx = path.indexOf(59);
            this.mime = path.substring(0, idx);
            String content = path.substring(idx + 1);
            if (content.startsWith("base64,")) {
                try {
                    Base64InputStream b64is = new Base64InputStream(new ByteArrayInputStream(content.substring(7).getBytes()));
                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    byte[] tmp = new byte[2056];
                    for (int size = b64is.read(tmp); size != -1; size = b64is.read(tmp)) {
                        bout.write(tmp, 0, size);
                    }
                    this.buf = bout.toByteArray();
                } catch (IOException e) {
                    Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, (String) null, (Throwable) e);
                }
            }
        }

        @Override // java.net.URLConnection
        public void connect() throws IOException {
        }

        @Override // java.net.URLConnection
        public String getHeaderField(String name) {
            if ("content-type".equals(name)) {
                return this.mime;
            }
            return super.getHeaderField(name);
        }

        @Override // java.net.URLConnection
        public InputStream getInputStream() throws IOException {
            return new ByteArrayInputStream(this.buf);
        }
    }

    /* access modifiers changed from: protected */
    @Override // java.net.URLStreamHandler
    public URLConnection openConnection(URL u) throws IOException {
        return new Connection(u);
    }
}
