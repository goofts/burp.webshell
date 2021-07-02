package com.formdev.flatlaf.icons;

import com.formdev.flatlaf.ui.FlatUIUtils;
import java.awt.Component;
import java.awt.Graphics2D;
import javax.swing.UIManager;

public class FlatTreeClosedIcon extends FlatAbstractIcon {
    public FlatTreeClosedIcon() {
        super(16, 16, UIManager.getColor("Tree.icon.closedColor"));
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatAbstractIcon
    public void paintIcon(Component c, Graphics2D g) {
        g.fill(FlatUIUtils.createPath(1.0d, 2.0d, 6.0d, 2.0d, 8.0d, 4.0d, 15.0d, 4.0d, 15.0d, 13.0d, 1.0d, 13.0d));
    }
}
