package net.miginfocom.layout;

import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

public class BoundSize implements Serializable {
    public static final BoundSize NULL_SIZE = new BoundSize(null, null);
    public static final BoundSize ZERO_PIXEL = new BoundSize(UnitValue.ZERO, "0px");
    private static final long serialVersionUID = 1;
    private final transient boolean gapPush;
    private final transient UnitValue max;
    private final transient UnitValue min;
    private final transient UnitValue pref;

    static {
        if (LayoutUtil.HAS_BEANS) {
            LayoutUtil.setDelegate(BoundSize.class, new PersistenceDelegate() {
                /* class net.miginfocom.layout.BoundSize.AnonymousClass1 */

                /* access modifiers changed from: protected */
                public Expression instantiate(Object oldInstance, Encoder out) {
                    BoundSize bs = (BoundSize) oldInstance;
                    return new Expression(oldInstance, BoundSize.class, "new", new Object[]{bs.getMin(), bs.getPreferred(), bs.getMax(), Boolean.valueOf(bs.getGapPush()), bs.getConstraintString()});
                }
            });
        }
    }

    public BoundSize(UnitValue minMaxPref, String createString) {
        this(minMaxPref, minMaxPref, minMaxPref, createString);
    }

    public BoundSize(UnitValue min2, UnitValue preferred, UnitValue max2, String createString) {
        this(min2, preferred, max2, false, createString);
    }

    public BoundSize(UnitValue min2, UnitValue preferred, UnitValue max2, boolean gapPush2, String createString) {
        this.min = min2;
        this.pref = preferred;
        this.max = max2;
        this.gapPush = gapPush2;
        LayoutUtil.putCCString(this, createString);
    }

    public final UnitValue getMin() {
        return this.min;
    }

    public final UnitValue getPreferred() {
        return this.pref;
    }

    public final UnitValue getMax() {
        return this.max;
    }

    public boolean getGapPush() {
        return this.gapPush;
    }

    public boolean isUnset() {
        return this == ZERO_PIXEL || (this.pref == null && this.min == null && this.max == null && !this.gapPush);
    }

    public int constrain(int size, float refValue, ContainerWrapper parent) {
        if (this.max != null) {
            size = Math.min(size, this.max.getPixels(refValue, parent, parent));
        }
        if (this.min != null) {
            return Math.max(size, this.min.getPixels(refValue, parent, parent));
        }
        return size;
    }

    /* access modifiers changed from: package-private */
    public final UnitValue getSize(int sizeType) {
        switch (sizeType) {
            case 0:
                return this.min;
            case 1:
                return this.pref;
            case 2:
                return this.max;
            default:
                throw new IllegalArgumentException("Unknown size: " + sizeType);
        }
    }

    /* access modifiers changed from: package-private */
    public final int[] getPixelSizes(float refSize, ContainerWrapper parent, ComponentWrapper comp) {
        int i;
        int i2 = 0;
        int[] iArr = new int[3];
        if (this.min != null) {
            i = this.min.getPixels(refSize, parent, comp);
        } else {
            i = 0;
        }
        iArr[0] = i;
        if (this.pref != null) {
            i2 = this.pref.getPixels(refSize, parent, comp);
        }
        iArr[1] = i2;
        iArr[2] = this.max != null ? this.max.getPixels(refSize, parent, comp) : LayoutUtil.INF;
        return iArr;
    }

    /* access modifiers changed from: package-private */
    public String getConstraintString() {
        String cs = LayoutUtil.getCCString(this);
        if (cs != null) {
            return cs;
        }
        if (this.min == this.pref && this.pref == this.max) {
            return this.min != null ? this.min.getConstraintString() + "!" : "null";
        }
        StringBuilder sb = new StringBuilder(16);
        if (this.min != null) {
            sb.append(this.min.getConstraintString()).append(':');
        }
        if (this.pref != null) {
            if (this.min == null && this.max != null) {
                sb.append(":");
            }
            sb.append(this.pref.getConstraintString());
        } else if (this.min != null) {
            sb.append('n');
        }
        if (this.max != null) {
            sb.append(sb.length() == 0 ? "::" : ":").append(this.max.getConstraintString());
        }
        if (this.gapPush) {
            if (sb.length() > 0) {
                sb.append(':');
            }
            sb.append("push");
        }
        return sb.toString();
    }

    /* access modifiers changed from: package-private */
    public void checkNotLinked() {
        if (isLinked()) {
            throw new IllegalArgumentException("Size may not contain links");
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isLinked() {
        return (this.min != null && this.min.isLinkedDeep()) || (this.pref != null && this.pref.isLinkedDeep()) || (this.max != null && this.max.isLinkedDeep());
    }

    /* access modifiers changed from: package-private */
    public boolean isAbsolute() {
        return (this.min == null || this.min.isAbsoluteDeep()) && (this.pref == null || this.pref.isAbsoluteDeep()) && (this.max == null || this.max.isAbsoluteDeep());
    }

    public String toString() {
        return "BoundSize{min=" + this.min + ", pref=" + this.pref + ", max=" + this.max + ", gapPush=" + this.gapPush + '}';
    }

    /* access modifiers changed from: protected */
    public Object readResolve() throws ObjectStreamException {
        return LayoutUtil.getSerializedObject(this);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        if (getClass() == BoundSize.class) {
            LayoutUtil.writeAsXML(out, this);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(in));
    }
}
