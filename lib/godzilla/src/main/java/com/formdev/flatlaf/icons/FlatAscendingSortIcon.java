package com.formdev.flatlaf.icons;

import com.formdev.flatlaf.ui.FlatUIUtils;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import javax.swing.UIManager;

public class FlatAscendingSortIcon extends FlatAbstractIcon {
    protected final boolean chevron = FlatUIUtils.isChevron(UIManager.getString("Component.arrowType"));
    protected final Color sortIconColor = UIManager.getColor("Table.sortIconColor");

    public FlatAscendingSortIcon() {
        super(10, 5, null);
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatAbstractIcon
    public void paintIcon(Component c, Graphics2D g) {
        g.setColor(this.sortIconColor);
        if (this.chevron) {
            Path2D path = FlatUIUtils.createPath(false, 1.0d, 4.0d, 5.0d, 0.0d, 9.0d, 4.0d);
            g.setStroke(new BasicStroke(1.0f));
            g.draw(path);
            return;
        }
        g.fill(FlatUIUtils.createPath(0.5d, 5.0d, 5.0d, 0.0d, 9.5d, 5.0d));
    }
}
