package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.UIScale;
import java.awt.Component;
import java.awt.Insets;
import javax.swing.JMenuBar;
import javax.swing.UIManager;

public class FlatMenuItemBorder extends FlatMarginBorder {
    private final Insets menuBarItemMargins = UIManager.getInsets("MenuBar.itemMargins");

    @Override // com.formdev.flatlaf.ui.FlatMarginBorder
    public Insets getBorderInsets(Component c, Insets insets) {
        if (!(c.getParent() instanceof JMenuBar)) {
            return super.getBorderInsets(c, insets);
        }
        insets.top = UIScale.scale(this.menuBarItemMargins.top);
        insets.left = UIScale.scale(this.menuBarItemMargins.left);
        insets.bottom = UIScale.scale(this.menuBarItemMargins.bottom);
        insets.right = UIScale.scale(this.menuBarItemMargins.right);
        return insets;
    }
}
