package com.formdev.flatlaf.icons;

import com.formdev.flatlaf.ui.FlatUIUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import javax.swing.UIManager;

public class FlatTreeCollapsedIcon extends FlatAbstractIcon {
    private final boolean chevron;

    public FlatTreeCollapsedIcon() {
        this(UIManager.getColor("Tree.icon.collapsedColor"));
    }

    FlatTreeCollapsedIcon(Color color) {
        super(11, 11, color);
        this.chevron = FlatUIUtils.isChevron(UIManager.getString("Component.arrowType"));
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatAbstractIcon
    public void paintIcon(Component c, Graphics2D g) {
        rotate(c, g);
        if (this.chevron) {
            g.fill(FlatUIUtils.createPath(3.0d, 1.0d, 3.0d, 2.5d, 6.0d, 5.5d, 3.0d, 8.5d, 3.0d, 10.0d, 4.5d, 10.0d, 9.0d, 5.5d, 4.5d, 1.0d));
        } else {
            g.fill(FlatUIUtils.createPath(2.0d, 1.0d, 2.0d, 10.0d, 10.0d, 5.5d));
        }
    }

    /* access modifiers changed from: package-private */
    public void rotate(Component c, Graphics2D g) {
        if (!c.getComponentOrientation().isLeftToRight()) {
            g.rotate(Math.toRadians(180.0d), ((double) this.width) / 2.0d, ((double) this.height) / 2.0d);
        }
    }
}
