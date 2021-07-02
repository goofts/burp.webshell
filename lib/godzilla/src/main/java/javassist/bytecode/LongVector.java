package javassist.bytecode;

/* access modifiers changed from: package-private */
public final class LongVector {
    static final int ABITS = 7;
    static final int ASIZE = 128;
    static final int VSIZE = 8;
    private int elements;
    private ConstInfo[][] objects;

    public LongVector() {
        this.objects = new ConstInfo[8][];
        this.elements = 0;
    }

    public LongVector(int initialSize) {
        this.objects = new ConstInfo[(((initialSize >> 7) & -8) + 8)][];
        this.elements = 0;
    }

    public int size() {
        return this.elements;
    }

    public int capacity() {
        return this.objects.length * 128;
    }

    public ConstInfo elementAt(int i) {
        if (i < 0 || this.elements <= i) {
            return null;
        }
        return this.objects[i >> 7][i & Opcode.LAND];
    }

    public void addElement(ConstInfo value) {
        int nth = this.elements >> 7;
        int offset = this.elements & Opcode.LAND;
        int len = this.objects.length;
        if (nth >= len) {
            ConstInfo[][] newObj = new ConstInfo[(len + 8)][];
            System.arraycopy(this.objects, 0, newObj, 0, len);
            this.objects = newObj;
        }
        if (this.objects[nth] == null) {
            this.objects[nth] = new ConstInfo[128];
        }
        this.objects[nth][offset] = value;
        this.elements++;
    }
}
