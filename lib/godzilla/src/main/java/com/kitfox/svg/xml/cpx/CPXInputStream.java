package com.kitfox.svg.xml.cpx;

import com.kitfox.svg.SVGConst;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class CPXInputStream extends FilterInputStream implements CPXConsts {
    byte[] decryptBuffer = new byte[2048];
    byte[] head = new byte[4];
    int headPtr = 0;
    int headSize = 0;
    byte[] inBuffer = new byte[2048];
    Inflater inflater = new Inflater();
    boolean reachedEOF = false;
    SecureRandom sec = new SecureRandom();
    int xlateMode;

    public CPXInputStream(InputStream in) throws IOException {
        super(in);
        for (int i = 0; i < 4; i++) {
            int val = in.read();
            this.head[i] = (byte) val;
            if (val == -1 || this.head[i] != MAGIC_NUMBER[i]) {
                this.headSize = i + 1;
                this.xlateMode = 0;
                return;
            }
        }
        this.xlateMode = 1;
    }

    public boolean markSupported() {
        return false;
    }

    @Override // java.io.FilterInputStream, java.io.Closeable, java.lang.AutoCloseable, java.io.InputStream
    public void close() throws IOException {
        this.reachedEOF = true;
        this.in.close();
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        byte[] b = new byte[1];
        if (read(b, 0, 1) == -1) {
            return -1;
        }
        return b[0];
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        if (this.reachedEOF) {
            return -1;
        }
        if (this.xlateMode == 0) {
            int count = 0;
            while (this.headPtr < this.headSize && len > 0) {
                byte[] bArr = this.head;
                int i = this.headPtr;
                this.headPtr = i + 1;
                b[off] = bArr[i];
                count++;
                len--;
                off++;
            }
            if (len != 0) {
                count += this.in.read(b, off, len);
            }
            return count;
        } else if (!this.inflater.needsInput() || decryptChunk()) {
            try {
                return this.inflater.inflate(b, off, len);
            } catch (DataFormatException e) {
                Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, (String) null, (Throwable) e);
                return -1;
            }
        } else {
            this.reachedEOF = true;
            try {
                int numRead = this.inflater.inflate(b, off, len);
                if (!this.inflater.finished()) {
                    Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Inflation imncomplete");
                }
                if (numRead == 0) {
                    numRead = -1;
                }
                return numRead;
            } catch (Exception e2) {
                Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, (String) null, (Throwable) e2);
                return -1;
            }
        }
    }

    /* access modifiers changed from: protected */
    public boolean decryptChunk() throws IOException {
        while (this.inflater.needsInput()) {
            int numInBytes = this.in.read(this.inBuffer);
            if (numInBytes == -1) {
                return false;
            }
            this.inflater.setInput(this.inBuffer, 0, numInBytes);
        }
        return true;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() {
        return this.reachedEOF ? 0 : 1;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long n) throws IOException {
        int skipSize = (int) n;
        if (skipSize > this.inBuffer.length) {
            skipSize = this.inBuffer.length;
        }
        return (long) read(this.inBuffer, 0, skipSize);
    }
}
