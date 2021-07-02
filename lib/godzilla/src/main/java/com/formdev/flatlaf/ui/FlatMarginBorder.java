package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.UIScale;
import java.awt.Component;
import java.awt.Insets;
import javax.swing.plaf.basic.BasicBorders;

public class FlatMarginBorder extends BasicBorders.MarginBorder {
    private final int bottom;
    private final int left;
    private final int right;
    private final int top;

    public FlatMarginBorder() {
        this.bottom = 0;
        this.top = 0;
        this.right = 0;
        this.left = 0;
    }

    public FlatMarginBorder(Insets insets) {
        this.left = insets.left;
        this.top = insets.top;
        this.right = insets.right;
        this.bottom = insets.bottom;
    }

    public Insets getBorderInsets(Component c, Insets insets) {
        Insets insets2 = FlatMarginBorder.super.getBorderInsets(c, insets);
        insets2.top = UIScale.scale(insets2.top + this.top);
        insets2.left = UIScale.scale(insets2.left + this.left);
        insets2.bottom = UIScale.scale(insets2.bottom + this.bottom);
        insets2.right = UIScale.scale(insets2.right + this.right);
        return insets2;
    }
}
