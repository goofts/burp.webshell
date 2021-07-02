package com.formdev.flatlaf.icons;

import java.awt.Component;
import java.awt.Graphics2D;
import javax.swing.UIManager;

public class FlatTreeExpandedIcon extends FlatTreeCollapsedIcon {
    public FlatTreeExpandedIcon() {
        super(UIManager.getColor("Tree.icon.expandedColor"));
    }

    /* access modifiers changed from: package-private */
    @Override // com.formdev.flatlaf.icons.FlatTreeCollapsedIcon
    public void rotate(Component c, Graphics2D g) {
        g.rotate(Math.toRadians(90.0d), ((double) this.width) / 2.0d, ((double) this.height) / 2.0d);
    }
}
