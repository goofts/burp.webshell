package com.formdev.flatlaf.icons;

import com.formdev.flatlaf.util.AnimatedIcon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

public abstract class FlatAnimatedIcon extends FlatAbstractIcon implements AnimatedIcon {
    public FlatAnimatedIcon(int width, int height, Color color) {
        super(width, height, color);
    }

    @Override // com.formdev.flatlaf.icons.FlatAbstractIcon, com.formdev.flatlaf.util.AnimatedIcon
    public void paintIcon(Component c, Graphics g, int x, int y) {
        super.paintIcon(c, g, x, y);
        AnimatedIcon.AnimationSupport.saveIconLocation(this, c, x, y);
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatAbstractIcon
    public void paintIcon(Component c, Graphics2D g) {
        AnimatedIcon.AnimationSupport.paintIcon(this, c, g, 0, 0);
    }
}
