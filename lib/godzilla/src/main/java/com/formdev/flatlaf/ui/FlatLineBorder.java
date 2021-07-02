package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;

public class FlatLineBorder extends FlatEmptyBorder {
    private final Color lineColor;
    private final float lineThickness;

    public FlatLineBorder(Insets insets, Color lineColor2) {
        this(insets, lineColor2, 1.0f);
    }

    public FlatLineBorder(Insets insets, Color lineColor2, float lineThickness2) {
        super(insets);
        this.lineColor = lineColor2;
        this.lineThickness = lineThickness2;
    }

    public Color getLineColor() {
        return this.lineColor;
    }

    public float getLineThickness() {
        return this.lineThickness;
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = g.create();
        try {
            FlatUIUtils.setRenderingHints(g2);
            g2.setColor(this.lineColor);
            FlatUIUtils.paintComponentBorder(g2, x, y, width, height, 0.0f, UIScale.scale(this.lineThickness), 0.0f);
        } finally {
            g2.dispose();
        }
    }
}
