package com.kitfox.svg.xml;

import java.io.Serializable;
import javassist.bytecode.Opcode;

public class NumberWithUnits implements Serializable {
    public static final int UT_CM = 2;
    public static final int UT_EM = 5;
    public static final int UT_EX = 6;
    public static final int UT_IN = 4;
    public static final int UT_MM = 3;
    public static final int UT_PC = 8;
    public static final int UT_PERCENT = 9;
    public static final int UT_PT = 7;
    public static final int UT_PX = 1;
    public static final int UT_UNITLESS = 0;
    public static final long serialVersionUID = 0;
    int unitType = 0;
    float value = 0.0f;

    public NumberWithUnits() {
    }

    public NumberWithUnits(String value2) {
        set(value2);
    }

    public NumberWithUnits(float value2, int unitType2) {
        this.value = value2;
        this.unitType = unitType2;
    }

    public float getValue() {
        return this.value;
    }

    public int getUnits() {
        return this.unitType;
    }

    public void set(String value2) {
        this.value = XMLParseUtil.findFloat(value2);
        this.unitType = 0;
        if (value2.indexOf("px") != -1) {
            this.unitType = 1;
        } else if (value2.indexOf("cm") != -1) {
            this.unitType = 2;
        } else if (value2.indexOf("mm") != -1) {
            this.unitType = 3;
        } else if (value2.indexOf("in") != -1) {
            this.unitType = 4;
        } else if (value2.indexOf("em") != -1) {
            this.unitType = 5;
        } else if (value2.indexOf("ex") != -1) {
            this.unitType = 6;
        } else if (value2.indexOf("pt") != -1) {
            this.unitType = 7;
        } else if (value2.indexOf("pc") != -1) {
            this.unitType = 8;
        } else if (value2.indexOf("%") != -1) {
            this.unitType = 9;
        }
    }

    public static String unitsAsString(int unitIdx) {
        switch (unitIdx) {
            case 1:
                return "px";
            case 2:
                return "cm";
            case 3:
                return "mm";
            case 4:
                return "in";
            case 5:
                return "em";
            case 6:
                return "ex";
            case 7:
                return "pt";
            case 8:
                return "pc";
            case 9:
                return "%";
            default:
                return "";
        }
    }

    public String toString() {
        return "" + this.value + unitsAsString(this.unitType);
    }

    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        NumberWithUnits other = (NumberWithUnits) obj;
        if (Float.floatToIntBits(this.value) == Float.floatToIntBits(other.value) && this.unitType == other.unitType) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        return ((Float.floatToIntBits(this.value) + Opcode.INVOKEINTERFACE) * 37) + this.unitType;
    }
}
