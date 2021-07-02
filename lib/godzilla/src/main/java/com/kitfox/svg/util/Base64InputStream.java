package com.kitfox.svg.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class Base64InputStream extends FilterInputStream implements Base64Consts {
    static final /* synthetic */ boolean $assertionsDisabled = (!Base64InputStream.class.desiredAssertionStatus());
    static final HashMap<Byte, Integer> lookup64 = new HashMap<>();
    int buf;
    int charsInBuf;

    static {
        byte[] ch = Base64Consts.BASE64_CHARS.getBytes();
        for (int i = 0; i < ch.length; i++) {
            lookup64.put(new Byte(ch[i]), new Integer(i));
        }
    }

    public Base64InputStream(InputStream in) {
        super(in);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        for (int i = 0; i < len; i++) {
            int val = read();
            if (val != -1) {
                b[off + i] = (byte) val;
            } else if (i == 0) {
                return -1;
            } else {
                return i;
            }
        }
        return len;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        if (this.charsInBuf == 0) {
            fillBuffer();
            if (this.charsInBuf == 0) {
                return -1;
            }
        }
        int i = this.buf;
        int i2 = this.charsInBuf - 1;
        this.charsInBuf = i2;
        return (i >> (i2 * 8)) & 255;
    }

    private void fillBuffer() throws IOException {
        int val;
        int bitsRead = 0;
        while (bitsRead < 24 && (val = this.in.read()) != -1 && val != 61) {
            Integer lval = lookup64.get(new Byte((byte) val));
            if (lval != null) {
                this.buf = (this.buf << 6) | lval.byteValue();
                bitsRead += 6;
            }
        }
        switch (bitsRead) {
            case 0:
            case 24:
                break;
            case 6:
                throw new RuntimeException("Invalid termination of base64 encoding.");
            case 12:
                this.buf >>= 4;
                bitsRead = 8;
                break;
            case 18:
                this.buf >>= 2;
                bitsRead = 16;
                break;
            default:
                if (!$assertionsDisabled) {
                    throw new AssertionError("Should never encounter other bit counts");
                }
                break;
        }
        this.charsInBuf = bitsRead / 8;
    }
}
