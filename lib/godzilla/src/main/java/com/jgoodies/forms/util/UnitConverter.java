package com.jgoodies.forms.util;

import java.awt.Component;

public interface UnitConverter {
    int centimeterAsPixel(double d, Component component);

    int dialogUnitXAsPixel(int i, Component component);

    int dialogUnitYAsPixel(int i, Component component);

    int inchAsPixel(double d, Component component);

    int millimeterAsPixel(double d, Component component);

    int pointAsPixel(int i, Component component);
}
