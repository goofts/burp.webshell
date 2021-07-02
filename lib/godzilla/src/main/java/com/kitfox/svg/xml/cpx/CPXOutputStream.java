package com.kitfox.svg.xml.cpx;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.Deflater;

public class CPXOutputStream extends FilterOutputStream implements CPXConsts {
    byte[] deflateBuffer = new byte[2048];
    Deflater deflater = new Deflater(9);

    public CPXOutputStream(OutputStream os) throws IOException {
        super(os);
        os.write(MAGIC_NUMBER);
    }

    @Override // java.io.OutputStream, java.io.FilterOutputStream
    public void write(int b) throws IOException {
        write(new byte[]{(byte) b}, 0, 1);
    }

    @Override // java.io.OutputStream, java.io.FilterOutputStream
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override // java.io.OutputStream, java.io.FilterOutputStream
    public void write(byte[] b, int off, int len) throws IOException {
        this.deflater.setInput(b, off, len);
        processAllData();
    }

    /* access modifiers changed from: protected */
    public void processAllData() throws IOException {
        while (true) {
            int numDeflatedBytes = this.deflater.deflate(this.deflateBuffer);
            if (numDeflatedBytes != 0) {
                this.out.write(this.deflateBuffer, 0, numDeflatedBytes);
            } else {
                return;
            }
        }
    }

    @Override // java.io.OutputStream, java.io.FilterOutputStream, java.io.Flushable
    public void flush() throws IOException {
        this.out.flush();
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.io.FilterOutputStream, java.lang.AutoCloseable
    public void close() throws IOException {
        this.deflater.finish();
        processAllData();
        try {
            flush();
        } catch (IOException e) {
        }
        this.out.close();
    }
}
