package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.UIScale;
import java.awt.Component;
import java.awt.Container;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

public class FlatPopupMenuBorder extends FlatLineBorder {
    public FlatPopupMenuBorder() {
        super(UIManager.getInsets("PopupMenu.borderInsets"), UIManager.getColor("PopupMenu.borderColor"));
    }

    @Override // com.formdev.flatlaf.ui.FlatEmptyBorder
    public Insets getBorderInsets(Component c, Insets insets) {
        if (!(c instanceof Container) || ((Container) c).getComponentCount() <= 0 || !(((Container) c).getComponent(0) instanceof JScrollPane)) {
            return super.getBorderInsets(c, insets);
        }
        int scale = UIScale.scale(1);
        insets.bottom = scale;
        insets.right = scale;
        insets.top = scale;
        insets.left = scale;
        return insets;
    }
}
