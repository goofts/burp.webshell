package net.miginfocom.layout;

import com.formdev.flatlaf.FlatClientProperties;
import java.beans.Encoder;
import java.beans.Expression;
import java.beans.PersistenceDelegate;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public final class UnitValue implements Serializable {
    public static final int ADD = 101;
    public static final int ALIGN = 12;
    static final UnitValue BASELINE_IDENTITY = new UnitValue(0.0f, null, -1, false, 100, null, null, "baseline");
    static final UnitValue BOTTOM = new UnitValue(100.0f, null, 6, false, 100, null, null, "bottom");
    public static final int BUTTON = 16;
    static final UnitValue CENTER = new UnitValue(50.0f, null, 6, true, 100, null, null, FlatClientProperties.TABBED_PANE_ALIGN_CENTER);
    public static final int CM = 4;
    private static final ArrayList<UnitConverter> CONVERTERS = new ArrayList<>();
    public static final int DIV = 104;
    private static final int IDENTITY = -1;
    public static final int INCH = 5;
    static final UnitValue INF = new UnitValue(2097051.0f, null, 0, true, 100, null, null, "inf");
    static final UnitValue LABEL = new UnitValue(0.0f, null, 27, false, 100, null, null, "label");
    public static final int LABEL_ALIGN = 27;
    static final UnitValue LEADING = new UnitValue(0.0f, null, 6, true, 100, null, null, FlatClientProperties.TABBED_PANE_ALIGN_LEADING);
    static final UnitValue LEFT = new UnitValue(0.0f, null, 6, true, 100, null, null, "left");
    public static final int LINK_H = 21;
    public static final int LINK_W = 20;
    public static final int LINK_X = 18;
    public static final int LINK_X2 = 22;
    public static final int LINK_XPOS = 24;
    public static final int LINK_Y = 19;
    public static final int LINK_Y2 = 23;
    public static final int LINK_YPOS = 25;
    public static final int LOOKUP = 26;
    public static final int LPX = 1;
    public static final int LPY = 2;
    public static final int MAX = 106;
    public static final int MAX_SIZE = 15;
    public static final int MID = 107;
    public static final int MIN = 105;
    public static final int MIN_SIZE = 13;
    public static final int MM = 3;
    public static final int MUL = 103;
    public static final int PERCENT = 6;
    public static final int PIXEL = 0;
    public static final int PREF_SIZE = 14;
    public static final int PT = 7;
    static final UnitValue RIGHT = new UnitValue(100.0f, null, 6, true, 100, null, null, "right");
    private static final float[] SCALE = {25.4f, 2.54f, 1.0f, 0.0f, 72.0f};
    public static final int SPX = 8;
    public static final int SPY = 9;
    public static final int STATIC = 100;
    public static final int SUB = 102;
    static final UnitValue TOP = new UnitValue(0.0f, null, 6, false, 100, null, null, "top");
    static final UnitValue TRAILING = new UnitValue(100.0f, null, 6, true, 100, null, null, "trailing");
    private static final HashMap<String, Integer> UNIT_MAP = new HashMap<>(32);
    static final UnitValue ZERO = new UnitValue(0.0f, null, 0, true, 100, null, null, "0px");
    private static final long serialVersionUID = 1;
    private final transient boolean isHor;
    private transient String linkId;
    private final transient int oper;
    private final transient UnitValue[] subUnits;
    private final transient int unit;
    private final transient String unitStr;
    private final transient float value;

    static {
        UNIT_MAP.put("px", 0);
        UNIT_MAP.put("lpx", 1);
        UNIT_MAP.put("lpy", 2);
        UNIT_MAP.put("%", 6);
        UNIT_MAP.put("cm", 4);
        UNIT_MAP.put("in", 5);
        UNIT_MAP.put("spx", 8);
        UNIT_MAP.put("spy", 9);
        UNIT_MAP.put("al", 12);
        UNIT_MAP.put("mm", 3);
        UNIT_MAP.put("pt", 7);
        UNIT_MAP.put("min", 13);
        UNIT_MAP.put("minimum", 13);
        UNIT_MAP.put("p", 14);
        UNIT_MAP.put("pref", 14);
        UNIT_MAP.put("max", 15);
        UNIT_MAP.put("maximum", 15);
        UNIT_MAP.put("button", 16);
        UNIT_MAP.put("label", 27);
        if (LayoutUtil.HAS_BEANS) {
            LayoutUtil.setDelegate(UnitValue.class, new PersistenceDelegate() {
                /* class net.miginfocom.layout.UnitValue.AnonymousClass1 */

                /* access modifiers changed from: protected */
                public Expression instantiate(Object oldInstance, Encoder out) {
                    UnitValue uv = (UnitValue) oldInstance;
                    if (uv.getConstraintString() == null) {
                        throw new IllegalStateException("Design time must be on to use XML persistence. See LayoutUtil.");
                    }
                    Object[] objArr = new Object[3];
                    objArr[0] = uv.getConstraintString();
                    objArr[1] = uv.isHorizontal() ? Boolean.TRUE : Boolean.FALSE;
                    objArr[2] = null;
                    return new Expression(oldInstance, ConstraintParser.class, "parseUnitValueOrAlign", objArr);
                }
            });
        }
    }

    public UnitValue(float value2) {
        this(value2, null, 0, true, 100, null, null, value2 + "px");
    }

    public UnitValue(float value2, int unit2, String createString) {
        this(value2, null, unit2, true, 100, null, null, createString);
    }

    public UnitValue(float value2, String unitStr2, boolean isHor2, int oper2, String createString) {
        this(value2, unitStr2, -1, isHor2, oper2, null, null, createString);
    }

    UnitValue(boolean isHor2, int oper2, UnitValue sub1, UnitValue sub2, String createString) {
        this(0.0f, "", -1, isHor2, oper2, sub1, sub2, createString);
        if (sub1 == null || sub2 == null) {
            throw new IllegalArgumentException("Sub units is null!");
        }
    }

    private UnitValue(float value2, String unitStr2, int unit2, boolean isHor2, int oper2, UnitValue sub1, UnitValue sub2, String createString) {
        UnitValue[] unitValueArr = null;
        this.linkId = null;
        if (oper2 < 100 || oper2 > 107) {
            throw new IllegalArgumentException("Unknown Operation: " + oper2);
        } else if (oper2 < 101 || oper2 > 107 || !(sub1 == null || sub2 == null)) {
            this.value = value2;
            this.oper = oper2;
            this.isHor = isHor2;
            this.unitStr = unitStr2;
            this.unit = unitStr2 != null ? parseUnitString() : unit2;
            if (!(sub1 == null || sub2 == null)) {
                unitValueArr = new UnitValue[]{sub1, sub2};
            }
            this.subUnits = unitValueArr;
            LayoutUtil.putCCString(this, createString);
        } else {
            throw new IllegalArgumentException(oper2 + " Operation may not have null sub-UnitValues.");
        }
    }

    public final int getPixels(float refValue, ContainerWrapper parent, ComponentWrapper comp) {
        return Math.round(getPixelsExact(refValue, parent, comp));
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    public final float getPixelsExact(float refValue, ContainerWrapper parent, ComponentWrapper comp) {
        if (parent == null) {
            return 1.0f;
        }
        if (this.oper == 100) {
            switch (this.unit) {
                case 0:
                    return this.value;
                case 1:
                case 2:
                    return parent.getPixelUnitFactor(this.unit == 1) * this.value;
                case 3:
                case 4:
                case 5:
                case 7:
                    float f = SCALE[this.unit - 3];
                    Float s = this.isHor ? PlatformDefaults.getHorizontalScaleFactor() : PlatformDefaults.getVerticalScaleFactor();
                    if (s != null) {
                        f *= s.floatValue();
                    }
                    return (((float) (this.isHor ? parent.getHorizontalScreenDPI() : parent.getVerticalScreenDPI())) * this.value) / f;
                case 6:
                    return this.value * refValue * 0.01f;
                case 8:
                case 9:
                    return ((float) (this.unit == 8 ? parent.getScreenWidth() : parent.getScreenHeight())) * this.value * 0.01f;
                case 10:
                case 11:
                case 17:
                default:
                    throw new IllegalArgumentException("Unknown/illegal unit: " + this.unit + ", unitStr: " + this.unitStr);
                case 12:
                    Integer st = LinkHandler.getValue(parent.getLayout(), "visual", this.isHor ? 0 : 1);
                    Integer sz = LinkHandler.getValue(parent.getLayout(), "visual", this.isHor ? 2 : 3);
                    if (st == null || sz == null) {
                        return 0.0f;
                    }
                    return (this.value * (((float) Math.max(0, sz.intValue())) - refValue)) + ((float) st.intValue());
                case 13:
                    if (comp == null) {
                        return 0.0f;
                    }
                    return this.isHor ? (float) comp.getMinimumWidth(comp.getHeight()) : (float) comp.getMinimumHeight(comp.getWidth());
                case 14:
                    if (comp == null) {
                        return 0.0f;
                    }
                    return this.isHor ? (float) comp.getPreferredWidth(comp.getHeight()) : (float) comp.getPreferredHeight(comp.getWidth());
                case 15:
                    if (comp == null) {
                        return 0.0f;
                    }
                    return this.isHor ? (float) comp.getMaximumWidth(comp.getHeight()) : (float) comp.getMaximumHeight(comp.getWidth());
                case 16:
                    return PlatformDefaults.getMinimumButtonWidthIncludingPadding(refValue, parent, comp);
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                    Integer v = LinkHandler.getValue(parent.getLayout(), getLinkTargetId(), this.unit - (this.unit >= 24 ? 24 : 18));
                    if (v == null) {
                        return 0.0f;
                    }
                    if (this.unit == 24) {
                        return (float) (parent.getScreenLocationX() + v.intValue());
                    }
                    if (this.unit == 25) {
                        return (float) (parent.getScreenLocationY() + v.intValue());
                    }
                    return (float) v.intValue();
                case 26:
                    float res = lookup(refValue, parent, comp);
                    if (res != -8.7654312E7f) {
                        return res;
                    }
                    break;
                case 27:
                    break;
            }
            return PlatformDefaults.getLabelAlignPercentage() * refValue;
        }
        if (this.subUnits != null && this.subUnits.length == 2) {
            float r1 = this.subUnits[0].getPixelsExact(refValue, parent, comp);
            float r2 = this.subUnits[1].getPixelsExact(refValue, parent, comp);
            switch (this.oper) {
                case 101:
                    return r1 + r2;
                case 102:
                    return r1 - r2;
                case 103:
                    return r1 * r2;
                case 104:
                    return r1 / r2;
                case 105:
                    if (r1 >= r2) {
                        r1 = r2;
                    }
                    return r1;
                case 106:
                    if (r1 <= r2) {
                        r1 = r2;
                    }
                    return r1;
                case 107:
                    return (r1 + r2) * 0.5f;
            }
        }
        throw new IllegalArgumentException("Internal: Unknown Oper: " + this.oper);
    }

    private float lookup(float refValue, ContainerWrapper parent, ComponentWrapper comp) {
        float res = -8.7654312E7f;
        for (int i = CONVERTERS.size() - 1; i >= 0; i--) {
            res = (float) CONVERTERS.get(i).convertToPixels(this.value, this.unitStr, this.isHor, refValue, parent, comp);
            if (res != -8.7654312E7f) {
                return res;
            }
        }
        return (float) PlatformDefaults.convertToPixels(this.value, this.unitStr, this.isHor, refValue, parent, comp);
    }

    private int parseUnitString() {
        if (this.unitStr.length() != 0) {
            Integer u = UNIT_MAP.get(this.unitStr);
            if (u != null) {
                if (this.isHor || (u.intValue() != 16 && u.intValue() != 27)) {
                    return u.intValue();
                }
                throw new IllegalArgumentException("Not valid in vertical contexts: '" + this.unitStr + "'");
            } else if (this.unitStr.equals("lp")) {
                return this.isHor ? 1 : 2;
            } else {
                if (this.unitStr.equals("sp")) {
                    return this.isHor ? 8 : 9;
                }
                if (lookup(0.0f, null, null) != -8.7654312E7f) {
                    return 26;
                }
                int pIx = this.unitStr.indexOf(46);
                if (pIx != -1) {
                    this.linkId = this.unitStr.substring(0, pIx);
                    String e = this.unitStr.substring(pIx + 1);
                    if (e.equals("x")) {
                        return 18;
                    }
                    if (e.equals("y")) {
                        return 19;
                    }
                    if (e.equals("w") || e.equals("width")) {
                        return 20;
                    }
                    if (e.equals("h") || e.equals("height")) {
                        return 21;
                    }
                    if (e.equals("x2")) {
                        return 22;
                    }
                    if (e.equals("y2")) {
                        return 23;
                    }
                    if (e.equals("xpos")) {
                        return 24;
                    }
                    if (e.equals("ypos")) {
                        return 25;
                    }
                }
                throw new IllegalArgumentException("Unknown keyword: " + this.unitStr);
            }
        } else if (this.isHor) {
            return PlatformDefaults.getDefaultHorizontalUnit();
        } else {
            return PlatformDefaults.getDefaultVerticalUnit();
        }
    }

    /* access modifiers changed from: package-private */
    public final boolean isAbsolute() {
        switch (this.unit) {
            case 0:
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
            case 7:
                return true;
            case 6:
            case 8:
            case 9:
            case 12:
            case 13:
            case 14:
            case 15:
            case 16:
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
                return false;
            case 10:
            case 11:
            case 17:
            default:
                throw new IllegalArgumentException("Unknown/illegal unit: " + this.unit + ", unitStr: " + this.unitStr);
        }
    }

    /* access modifiers changed from: package-private */
    public final boolean isAbsoluteDeep() {
        if (this.subUnits != null) {
            for (UnitValue subUnit : this.subUnits) {
                if (subUnit.isAbsoluteDeep()) {
                    return true;
                }
            }
        }
        return isAbsolute();
    }

    /* access modifiers changed from: package-private */
    public final boolean isLinked() {
        return this.linkId != null;
    }

    /* access modifiers changed from: package-private */
    public final boolean isLinkedDeep() {
        if (this.subUnits != null) {
            for (UnitValue subUnit : this.subUnits) {
                if (subUnit.isLinkedDeep()) {
                    return true;
                }
            }
        }
        return isLinked();
    }

    /* access modifiers changed from: package-private */
    public final String getLinkTargetId() {
        return this.linkId;
    }

    /* access modifiers changed from: package-private */
    public final UnitValue getSubUnitValue(int i) {
        return this.subUnits[i];
    }

    /* access modifiers changed from: package-private */
    public final int getSubUnitCount() {
        if (this.subUnits != null) {
            return this.subUnits.length;
        }
        return 0;
    }

    public final UnitValue[] getSubUnits() {
        if (this.subUnits != null) {
            return (UnitValue[]) this.subUnits.clone();
        }
        return null;
    }

    public final int getUnit() {
        return this.unit;
    }

    public final String getUnitString() {
        return this.unitStr;
    }

    public final int getOperation() {
        return this.oper;
    }

    public final float getValue() {
        return this.value;
    }

    public final boolean isHorizontal() {
        return this.isHor;
    }

    public final String toString() {
        return getClass().getName() + ". Value=" + this.value + ", unit=" + this.unit + ", unitString: " + this.unitStr + ", oper=" + this.oper + ", isHor: " + this.isHor;
    }

    public final String getConstraintString() {
        return LayoutUtil.getCCString(this);
    }

    public final int hashCode() {
        return ((((int) (this.value * 12345.0f)) + (this.oper >>> 5)) + this.unit) >>> 17;
    }

    public static synchronized void addGlobalUnitConverter(UnitConverter conv) {
        synchronized (UnitValue.class) {
            if (conv == null) {
                throw new NullPointerException();
            }
            CONVERTERS.add(conv);
        }
    }

    public static synchronized boolean removeGlobalUnitConverter(UnitConverter unit2) {
        boolean remove;
        synchronized (UnitValue.class) {
            remove = CONVERTERS.remove(unit2);
        }
        return remove;
    }

    public static synchronized UnitConverter[] getGlobalUnitConverters() {
        UnitConverter[] unitConverterArr;
        synchronized (UnitValue.class) {
            unitConverterArr = (UnitConverter[]) CONVERTERS.toArray(new UnitConverter[CONVERTERS.size()]);
        }
        return unitConverterArr;
    }

    public static int getDefaultUnit() {
        return PlatformDefaults.getDefaultHorizontalUnit();
    }

    public static void setDefaultUnit(int unit2) {
        PlatformDefaults.setDefaultHorizontalUnit(unit2);
        PlatformDefaults.setDefaultVerticalUnit(unit2);
    }

    private Object readResolve() throws ObjectStreamException {
        return LayoutUtil.getSerializedObject(this);
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        if (getClass() == UnitValue.class) {
            LayoutUtil.writeAsXML(out, this);
        }
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(in));
    }
}
