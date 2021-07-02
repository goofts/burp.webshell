package com.jgoodies.forms.util;

import com.jgoodies.common.bean.Bean;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Toolkit;

public abstract class AbstractUnitConverter extends Bean implements UnitConverter {
    private static final int DTP_RESOLUTION = 72;
    private static int defaultScreenResolution = -1;

    /* access modifiers changed from: protected */
    public abstract double getDialogBaseUnitsX(Component component);

    /* access modifiers changed from: protected */
    public abstract double getDialogBaseUnitsY(Component component);

    @Override // com.jgoodies.forms.util.UnitConverter
    public int inchAsPixel(double in, Component component) {
        return inchAsPixel(in, getScreenResolution(component));
    }

    @Override // com.jgoodies.forms.util.UnitConverter
    public int millimeterAsPixel(double mm, Component component) {
        return millimeterAsPixel(mm, getScreenResolution(component));
    }

    @Override // com.jgoodies.forms.util.UnitConverter
    public int centimeterAsPixel(double cm, Component component) {
        return centimeterAsPixel(cm, getScreenResolution(component));
    }

    @Override // com.jgoodies.forms.util.UnitConverter
    public int pointAsPixel(int pt, Component component) {
        return pointAsPixel((double) pt, getScreenResolution(component));
    }

    @Override // com.jgoodies.forms.util.UnitConverter
    public int dialogUnitXAsPixel(int dluX, Component c) {
        return dialogUnitXAsPixel(dluX, getDialogBaseUnitsX(c));
    }

    @Override // com.jgoodies.forms.util.UnitConverter
    public int dialogUnitYAsPixel(int dluY, Component c) {
        return dialogUnitYAsPixel(dluY, getDialogBaseUnitsY(c));
    }

    protected static final int inchAsPixel(double in, int dpi) {
        return (int) Math.round(((double) dpi) * in);
    }

    protected static final int millimeterAsPixel(double mm, int dpi) {
        return (int) Math.round(((((double) dpi) * mm) * 10.0d) / 254.0d);
    }

    protected static final int centimeterAsPixel(double cm, int dpi) {
        return (int) Math.round(((((double) dpi) * cm) * 100.0d) / 254.0d);
    }

    protected static final int pointAsPixel(double pt, int dpi) {
        return (int) Math.round((((double) dpi) * pt) / 72.0d);
    }

    /* access modifiers changed from: protected */
    public int dialogUnitXAsPixel(int dluX, double dialogBaseUnitsX) {
        return (int) Math.round((((double) dluX) * dialogBaseUnitsX) / 4.0d);
    }

    /* access modifiers changed from: protected */
    public int dialogUnitYAsPixel(int dluY, double dialogBaseUnitsY) {
        return (int) Math.round((((double) dluY) * dialogBaseUnitsY) / 8.0d);
    }

    /* access modifiers changed from: protected */
    public double computeAverageCharWidth(FontMetrics metrics, String testString) {
        return ((double) metrics.stringWidth(testString)) / ((double) testString.length());
    }

    /* access modifiers changed from: protected */
    public int getScreenResolution(Component c) {
        if (c == null) {
            return getDefaultScreenResolution();
        }
        Toolkit toolkit = c.getToolkit();
        return toolkit != null ? toolkit.getScreenResolution() : getDefaultScreenResolution();
    }

    /* access modifiers changed from: protected */
    public int getDefaultScreenResolution() {
        if (defaultScreenResolution == -1) {
            defaultScreenResolution = Toolkit.getDefaultToolkit().getScreenResolution();
        }
        return defaultScreenResolution;
    }
}
