package com.formdev.flatlaf.icons;

import com.formdev.flatlaf.ui.FlatUIUtils;
import java.awt.Component;
import java.awt.Graphics2D;
import javax.swing.UIManager;

public class FlatTreeOpenIcon extends FlatAbstractIcon {
    public FlatTreeOpenIcon() {
        super(16, 16, UIManager.getColor("Tree.icon.openColor"));
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatAbstractIcon
    public void paintIcon(Component c, Graphics2D g) {
        g.fill(FlatUIUtils.createPath(1.0d, 2.0d, 6.0d, 2.0d, 8.0d, 4.0d, 14.0d, 4.0d, 14.0d, 6.0d, 3.5d, 6.0d, 1.0d, 11.0d));
        g.fill(FlatUIUtils.createPath(4.0d, 7.0d, 16.0d, 7.0d, 13.0d, 13.0d, 1.0d, 13.0d));
    }
}
