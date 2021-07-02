package javassist.bytecode;

import java.io.IOException;
import java.io.OutputStream;

final class ByteStream extends OutputStream {
    private byte[] buf;
    private int count;

    public ByteStream() {
        this(32);
    }

    public ByteStream(int size) {
        this.buf = new byte[size];
        this.count = 0;
    }

    public int getPos() {
        return this.count;
    }

    public int size() {
        return this.count;
    }

    public void writeBlank(int len) {
        enlarge(len);
        this.count += len;
    }

    @Override // java.io.OutputStream
    public void write(byte[] data) {
        write(data, 0, data.length);
    }

    @Override // java.io.OutputStream
    public void write(byte[] data, int off, int len) {
        enlarge(len);
        System.arraycopy(data, off, this.buf, this.count, len);
        this.count += len;
    }

    @Override // java.io.OutputStream
    public void write(int b) {
        enlarge(1);
        int oldCount = this.count;
        this.buf[oldCount] = (byte) b;
        this.count = oldCount + 1;
    }

    public void writeShort(int s) {
        enlarge(2);
        int oldCount = this.count;
        this.buf[oldCount] = (byte) (s >>> 8);
        this.buf[oldCount + 1] = (byte) s;
        this.count = oldCount + 2;
    }

    public void writeInt(int i) {
        enlarge(4);
        int oldCount = this.count;
        this.buf[oldCount] = (byte) (i >>> 24);
        this.buf[oldCount + 1] = (byte) (i >>> 16);
        this.buf[oldCount + 2] = (byte) (i >>> 8);
        this.buf[oldCount + 3] = (byte) i;
        this.count = oldCount + 4;
    }

    public void writeLong(long i) {
        enlarge(8);
        int oldCount = this.count;
        this.buf[oldCount] = (byte) ((int) (i >>> 56));
        this.buf[oldCount + 1] = (byte) ((int) (i >>> 48));
        this.buf[oldCount + 2] = (byte) ((int) (i >>> 40));
        this.buf[oldCount + 3] = (byte) ((int) (i >>> 32));
        this.buf[oldCount + 4] = (byte) ((int) (i >>> 24));
        this.buf[oldCount + 5] = (byte) ((int) (i >>> 16));
        this.buf[oldCount + 6] = (byte) ((int) (i >>> 8));
        this.buf[oldCount + 7] = (byte) ((int) i);
        this.count = oldCount + 8;
    }

    public void writeFloat(float v) {
        writeInt(Float.floatToIntBits(v));
    }

    public void writeDouble(double v) {
        writeLong(Double.doubleToLongBits(v));
    }

    public void writeUTF(String s) {
        int sLen = s.length();
        int pos = this.count;
        enlarge(sLen + 2);
        byte[] buffer = this.buf;
        int pos2 = pos + 1;
        buffer[pos] = (byte) (sLen >>> 8);
        buffer[pos2] = (byte) sLen;
        int i = 0;
        int pos3 = pos2 + 1;
        while (i < sLen) {
            char c = s.charAt(i);
            if (1 > c || c > 127) {
                writeUTF2(s, sLen, i);
                return;
            }
            buffer[pos3] = (byte) c;
            i++;
            pos3++;
        }
        this.count = pos3;
    }

    private void writeUTF2(String s, int sLen, int offset) {
        int pos;
        int size = sLen;
        for (int i = offset; i < sLen; i++) {
            int c = s.charAt(i);
            if (c > 2047) {
                size += 2;
            } else if (c == 0 || c > 127) {
                size++;
            }
        }
        if (size > 65535) {
            throw new RuntimeException("encoded string too long: " + sLen + size + " bytes");
        }
        enlarge(size + 2);
        int pos2 = this.count;
        byte[] buffer = this.buf;
        buffer[pos2] = (byte) (size >>> 8);
        buffer[pos2 + 1] = (byte) size;
        int j = offset;
        int pos3 = pos2 + offset + 2;
        while (j < sLen) {
            int c2 = s.charAt(j);
            if (1 <= c2 && c2 <= 127) {
                pos = pos3 + 1;
                buffer[pos3] = (byte) c2;
            } else if (c2 > 2047) {
                buffer[pos3] = (byte) (((c2 >> 12) & 15) | 224);
                buffer[pos3 + 1] = (byte) (((c2 >> 6) & 63) | 128);
                buffer[pos3 + 2] = (byte) ((c2 & 63) | 128);
                pos = pos3 + 3;
            } else {
                buffer[pos3] = (byte) (((c2 >> 6) & 31) | Opcode.CHECKCAST);
                buffer[pos3 + 1] = (byte) ((c2 & 63) | 128);
                pos = pos3 + 2;
            }
            j++;
            pos3 = pos;
        }
        this.count = pos3;
    }

    public void write(int pos, int value) {
        this.buf[pos] = (byte) value;
    }

    public void writeShort(int pos, int value) {
        this.buf[pos] = (byte) (value >>> 8);
        this.buf[pos + 1] = (byte) value;
    }

    public void writeInt(int pos, int value) {
        this.buf[pos] = (byte) (value >>> 24);
        this.buf[pos + 1] = (byte) (value >>> 16);
        this.buf[pos + 2] = (byte) (value >>> 8);
        this.buf[pos + 3] = (byte) value;
    }

    public byte[] toByteArray() {
        byte[] buf2 = new byte[this.count];
        System.arraycopy(this.buf, 0, buf2, 0, this.count);
        return buf2;
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(this.buf, 0, this.count);
    }

    public void enlarge(int delta) {
        int newCount = this.count + delta;
        if (newCount > this.buf.length) {
            int newLen = this.buf.length << 1;
            if (newLen <= newCount) {
                newLen = newCount;
            }
            byte[] newBuf = new byte[newLen];
            System.arraycopy(this.buf, 0, newBuf, 0, this.count);
            this.buf = newBuf;
        }
    }
}
