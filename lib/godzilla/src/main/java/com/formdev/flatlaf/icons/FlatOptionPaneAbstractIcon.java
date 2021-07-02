package com.formdev.flatlaf.icons;

import com.formdev.flatlaf.ui.FlatUIUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Path2D;
import javax.swing.UIManager;

public abstract class FlatOptionPaneAbstractIcon extends FlatAbstractIcon {
    protected final Color foreground = UIManager.getColor("OptionPane.icon.foreground");

    /* access modifiers changed from: protected */
    public abstract Shape createInside();

    /* access modifiers changed from: protected */
    public abstract Shape createOutside();

    protected FlatOptionPaneAbstractIcon(String colorKey, String defaultColorKey) {
        super(32, 32, FlatUIUtils.getUIColor(colorKey, defaultColorKey));
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatAbstractIcon
    public void paintIcon(Component c, Graphics2D g) {
        if (this.foreground != null) {
            g.fill(createOutside());
            g.setColor(this.foreground);
            g.fill(createInside());
            return;
        }
        Path2D path = new Path2D.Float(0);
        path.append(createOutside(), false);
        path.append(createInside(), false);
        g.fill(path);
    }
}
