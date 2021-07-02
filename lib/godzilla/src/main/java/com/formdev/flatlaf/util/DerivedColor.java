package com.formdev.flatlaf.util;

import com.formdev.flatlaf.util.ColorFunctions;
import java.awt.Color;
import javax.swing.plaf.ColorUIResource;

public class DerivedColor extends ColorUIResource {
    private int baseOfDefaultColorRGB;
    private final ColorFunctions.ColorFunction[] functions;
    private boolean hasBaseOfDefaultColor;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    public DerivedColor(Color defaultColor, ColorFunctions.ColorFunction... functions2) {
        super(defaultColor == null ? Color.red : defaultColor);
        this.functions = functions2;
    }

    public Color derive(Color baseColor) {
        if ((this.hasBaseOfDefaultColor && this.baseOfDefaultColorRGB == baseColor.getRGB()) || baseColor == this) {
            return this;
        }
        Color result = ColorFunctions.applyFunctions(baseColor, this.functions);
        if (!this.hasBaseOfDefaultColor && result.getRGB() == getRGB()) {
            this.hasBaseOfDefaultColor = true;
            this.baseOfDefaultColorRGB = baseColor.getRGB();
        }
        return result;
    }

    public ColorFunctions.ColorFunction[] getFunctions() {
        return this.functions;
    }
}
