package com.jgoodies.forms.layout;

import com.jgoodies.common.base.Preconditions;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Component;
import java.awt.Container;
import java.io.Serializable;
import java.util.List;

public final class ConstantSize implements Size, Serializable {
    public static final Unit CENTIMETER = new Unit("Centimeter", "cm", null, false);
    public static final Unit CM = CENTIMETER;
    public static final Unit DIALOG_UNITS_X = new Unit("Dialog units X", "dluX", "dlu", true);
    public static final Unit DIALOG_UNITS_Y = new Unit("Dialog units Y", "dluY", "dlu", true);
    public static final Unit DLUX = DIALOG_UNITS_X;
    public static final Unit DLUY = DIALOG_UNITS_Y;
    public static final Unit IN = INCH;
    public static final Unit INCH = new Unit("Inch", "in", null, false);
    public static final Unit MILLIMETER = new Unit("Millimeter", "mm", null, false);
    public static final Unit MM = MILLIMETER;
    public static final Unit PIXEL = new Unit("Pixel", "px", null, true);
    public static final Unit POINT = new Unit("Point", "pt", null, true);
    public static final Unit PT = POINT;
    public static final Unit PX = PIXEL;
    private static final Unit[] VALUES = {PIXEL, POINT, DIALOG_UNITS_X, DIALOG_UNITS_Y, MILLIMETER, CENTIMETER, INCH};
    private final Unit unit;
    private final double value;

    public ConstantSize(int value2, Unit unit2) {
        this.value = (double) value2;
        this.unit = unit2;
    }

    public ConstantSize(double value2, Unit unit2) {
        this.value = value2;
        this.unit = unit2;
    }

    static ConstantSize valueOf(String encodedValueAndUnit, boolean horizontal) {
        boolean z;
        String[] split = splitValueAndUnit(encodedValueAndUnit);
        String encodedValue = split[0];
        Unit unit2 = Unit.valueOf(split[1], horizontal);
        double value2 = Double.parseDouble(encodedValue);
        if (unit2.requiresIntegers) {
            if (value2 == ((double) ((int) value2))) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z, "%s value %s must be an integer.", unit2, encodedValue);
        }
        return new ConstantSize(value2, unit2);
    }

    static ConstantSize dluX(int value2) {
        return new ConstantSize(value2, DLUX);
    }

    static ConstantSize dluY(int value2) {
        return new ConstantSize(value2, DLUY);
    }

    public double getValue() {
        return this.value;
    }

    public Unit getUnit() {
        return this.unit;
    }

    public int getPixelSize(Component component) {
        if (this.unit == PIXEL) {
            return intValue();
        }
        if (this.unit == DIALOG_UNITS_X) {
            return Sizes.dialogUnitXAsPixel(intValue(), component);
        }
        if (this.unit == DIALOG_UNITS_Y) {
            return Sizes.dialogUnitYAsPixel(intValue(), component);
        }
        if (this.unit == POINT) {
            return Sizes.pointAsPixel(intValue(), component);
        }
        if (this.unit == INCH) {
            return Sizes.inchAsPixel(this.value, component);
        }
        if (this.unit == MILLIMETER) {
            return Sizes.millimeterAsPixel(this.value, component);
        }
        if (this.unit == CENTIMETER) {
            return Sizes.centimeterAsPixel(this.value, component);
        }
        throw new IllegalStateException("Invalid unit " + this.unit);
    }

    @Override // com.jgoodies.forms.layout.Size
    public int maximumSize(Container container, List components, FormLayout.Measure minMeasure, FormLayout.Measure prefMeasure, FormLayout.Measure defaultMeasure) {
        return getPixelSize(container);
    }

    @Override // com.jgoodies.forms.layout.Size
    public boolean compressible() {
        return false;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ConstantSize)) {
            return false;
        }
        ConstantSize size = (ConstantSize) o;
        return this.value == size.value && this.unit == size.unit;
    }

    public int hashCode() {
        return new Double(this.value).hashCode() + (this.unit.hashCode() * 37);
    }

    public String toString() {
        return this.value == ((double) intValue()) ? Integer.toString(intValue()) + this.unit.abbreviation() : Double.toString(this.value) + this.unit.abbreviation();
    }

    @Override // com.jgoodies.forms.layout.Size
    public String encode() {
        return this.value == ((double) intValue()) ? Integer.toString(intValue()) + this.unit.encode() : Double.toString(this.value) + this.unit.encode();
    }

    private int intValue() {
        return (int) Math.round(this.value);
    }

    private static String[] splitValueAndUnit(String encodedValueAndUnit) {
        String[] result = new String[2];
        int firstLetterIndex = encodedValueAndUnit.length();
        while (firstLetterIndex > 0 && Character.isLetter(encodedValueAndUnit.charAt(firstLetterIndex - 1))) {
            firstLetterIndex--;
        }
        result[0] = encodedValueAndUnit.substring(0, firstLetterIndex);
        result[1] = encodedValueAndUnit.substring(firstLetterIndex);
        return result;
    }

    public static final class Unit implements Serializable {
        private static int nextOrdinal = 0;
        private final transient String abbreviation;
        private final transient String name;
        private final int ordinal;
        private final transient String parseAbbreviation;
        final transient boolean requiresIntegers;

        private Unit(String name2, String abbreviation2, String parseAbbreviation2, boolean requiresIntegers2) {
            int i = nextOrdinal;
            nextOrdinal = i + 1;
            this.ordinal = i;
            this.name = name2;
            this.abbreviation = abbreviation2;
            this.parseAbbreviation = parseAbbreviation2;
            this.requiresIntegers = requiresIntegers2;
        }

        static Unit valueOf(String name2, boolean horizontal) {
            if (name2.length() == 0) {
                Unit defaultUnit = Sizes.getDefaultUnit();
                if (defaultUnit != null) {
                    return defaultUnit;
                }
                return horizontal ? ConstantSize.DIALOG_UNITS_X : ConstantSize.DIALOG_UNITS_Y;
            } else if (name2.equals("px")) {
                return ConstantSize.PIXEL;
            } else {
                if (name2.equals("dlu")) {
                    return horizontal ? ConstantSize.DIALOG_UNITS_X : ConstantSize.DIALOG_UNITS_Y;
                }
                if (name2.equals("pt")) {
                    return ConstantSize.POINT;
                }
                if (name2.equals("in")) {
                    return ConstantSize.INCH;
                }
                if (name2.equals("mm")) {
                    return ConstantSize.MILLIMETER;
                }
                if (name2.equals("cm")) {
                    return ConstantSize.CENTIMETER;
                }
                throw new IllegalArgumentException("Invalid unit name '" + name2 + "'. Must be one of: " + "px, dlu, pt, mm, cm, in");
            }
        }

        public String toString() {
            return this.name;
        }

        public String encode() {
            return this.parseAbbreviation != null ? this.parseAbbreviation : this.abbreviation;
        }

        public String abbreviation() {
            return this.abbreviation;
        }

        private Object readResolve() {
            return ConstantSize.VALUES[this.ordinal];
        }
    }
}
