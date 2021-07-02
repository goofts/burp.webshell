package com.formdev.flatlaf.icons;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.Icon;
import javax.swing.plaf.UIResource;

public abstract class FlatAbstractIcon implements Icon, UIResource {
    protected final Color color;
    protected final int height;
    protected final int width;

    /* access modifiers changed from: protected */
    public abstract void paintIcon(Component component, Graphics2D graphics2D);

    public FlatAbstractIcon(int width2, int height2, Color color2) {
        this.width = width2;
        this.height = height2;
        this.color = color2;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            FlatUIUtils.setRenderingHints(g2);
            g2.translate(x, y);
            UIScale.scaleGraphics(g2);
            if (this.color != null) {
                g2.setColor(this.color);
            }
            paintIcon(c, g2);
        } finally {
            g2.dispose();
        }
    }

    public int getIconWidth() {
        return UIScale.scale(this.width);
    }

    public int getIconHeight() {
        return UIScale.scale(this.height);
    }
}
