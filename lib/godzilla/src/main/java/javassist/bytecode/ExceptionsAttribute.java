package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class ExceptionsAttribute extends AttributeInfo {
    public static final String tag = "Exceptions";

    ExceptionsAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
        super(cp, n, in);
    }

    private ExceptionsAttribute(ConstPool cp, ExceptionsAttribute src, Map<String, String> classnames) {
        super(cp, tag);
        copyFrom(src, classnames);
    }

    public ExceptionsAttribute(ConstPool cp) {
        super(cp, tag);
        byte[] data = new byte[2];
        data[1] = 0;
        data[0] = 0;
        this.info = data;
    }

    @Override // javassist.bytecode.AttributeInfo
    public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
        return new ExceptionsAttribute(newCp, this, classnames);
    }

    private void copyFrom(ExceptionsAttribute srcAttr, Map<String, String> classnames) {
        ConstPool srcCp = srcAttr.constPool;
        ConstPool destCp = this.constPool;
        byte[] src = srcAttr.info;
        int num = src.length;
        byte[] dest = new byte[num];
        dest[0] = src[0];
        dest[1] = src[1];
        for (int i = 2; i < num; i += 2) {
            ByteArray.write16bit(srcCp.copy(ByteArray.readU16bit(src, i), destCp, classnames), dest, i);
        }
        this.info = dest;
    }

    public int[] getExceptionIndexes() {
        byte[] blist = this.info;
        int n = blist.length;
        if (n <= 2) {
            return null;
        }
        int[] elist = new int[((n / 2) - 1)];
        int j = 2;
        int k = 0;
        while (j < n) {
            elist[k] = ((blist[j] & 255) << 8) | (blist[j + 1] & 255);
            j += 2;
            k++;
        }
        return elist;
    }

    public String[] getExceptions() {
        byte[] blist = this.info;
        int n = blist.length;
        if (n <= 2) {
            return null;
        }
        String[] elist = new String[((n / 2) - 1)];
        int j = 2;
        int k = 0;
        while (j < n) {
            elist[k] = this.constPool.getClassInfo(((blist[j] & 255) << 8) | (blist[j + 1] & 255));
            j += 2;
            k++;
        }
        return elist;
    }

    public void setExceptionIndexes(int[] elist) {
        int n = elist.length;
        byte[] blist = new byte[((n * 2) + 2)];
        ByteArray.write16bit(n, blist, 0);
        for (int i = 0; i < n; i++) {
            ByteArray.write16bit(elist[i], blist, (i * 2) + 2);
        }
        this.info = blist;
    }

    public void setExceptions(String[] elist) {
        int n = elist.length;
        byte[] blist = new byte[((n * 2) + 2)];
        ByteArray.write16bit(n, blist, 0);
        for (int i = 0; i < n; i++) {
            ByteArray.write16bit(this.constPool.addClassInfo(elist[i]), blist, (i * 2) + 2);
        }
        this.info = blist;
    }

    public int tableLength() {
        return (this.info.length / 2) - 1;
    }

    public int getException(int nth) {
        int index = (nth * 2) + 2;
        return ((this.info[index] & 255) << 8) | (this.info[index + 1] & 255);
    }
}
