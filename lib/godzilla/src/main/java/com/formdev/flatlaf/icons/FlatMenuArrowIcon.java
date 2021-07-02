package com.formdev.flatlaf.icons;

import com.formdev.flatlaf.ui.FlatUIUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import javax.swing.JMenu;
import javax.swing.UIManager;

public class FlatMenuArrowIcon extends FlatAbstractIcon {
    protected final Color arrowColor = UIManager.getColor("Menu.icon.arrowColor");
    protected final boolean chevron = FlatUIUtils.isChevron(UIManager.getString("Component.arrowType"));
    protected final Color disabledArrowColor = UIManager.getColor("Menu.icon.disabledArrowColor");
    protected final Color selectionForeground = UIManager.getColor("Menu.selectionForeground");

    public FlatMenuArrowIcon() {
        super(6, 10, null);
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatAbstractIcon
    public void paintIcon(Component c, Graphics2D g) {
        if (!c.getComponentOrientation().isLeftToRight()) {
            g.rotate(Math.toRadians(180.0d), ((double) this.width) / 2.0d, ((double) this.height) / 2.0d);
        }
        g.setColor(getArrowColor(c));
        if (this.chevron) {
            Path2D path = FlatUIUtils.createPath(false, 1.0d, 1.0d, 5.0d, 5.0d, 1.0d, 9.0d);
            g.setStroke(new BasicStroke(1.0f));
            g.draw(path);
            return;
        }
        g.fill(FlatUIUtils.createPath(0.0d, 0.5d, 5.0d, 5.0d, 0.0d, 9.5d));
    }

    /* access modifiers changed from: protected */
    public Color getArrowColor(Component c) {
        if (!(c instanceof JMenu) || !((JMenu) c).isSelected() || isUnderlineSelection()) {
            return c.isEnabled() ? this.arrowColor : this.disabledArrowColor;
        }
        return this.selectionForeground;
    }

    /* access modifiers changed from: protected */
    public boolean isUnderlineSelection() {
        return "underline".equals(UIManager.getString("MenuItem.selectionType"));
    }
}
