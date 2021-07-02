package com.formdev.flatlaf.icons;

import com.formdev.flatlaf.ui.FlatButtonUI;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import javax.swing.UIManager;

public class FlatWindowCloseIcon extends FlatWindowAbstractIcon {
    private final Color hoverForeground = UIManager.getColor("TitlePane.closeHoverForeground");
    private final Color pressedForeground = UIManager.getColor("TitlePane.closePressedForeground");

    public FlatWindowCloseIcon() {
        super(UIManager.getDimension("TitlePane.buttonSize"), UIManager.getColor("TitlePane.closeHoverBackground"), UIManager.getColor("TitlePane.closePressedBackground"));
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatWindowAbstractIcon
    public void paintIconAt1x(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
        int iwh = (int) (10.0d * scaleFactor);
        int ix = x + ((width - iwh) / 2);
        int iy = y + ((height - iwh) / 2);
        int ix2 = (ix + iwh) - 1;
        int iy2 = (iy + iwh) - 1;
        Path2D path = new Path2D.Float(0);
        path.append(new Line2D.Float((float) ix, (float) iy, (float) ix2, (float) iy2), false);
        path.append(new Line2D.Float((float) ix, (float) iy2, (float) ix2, (float) iy), false);
        g.setStroke(new BasicStroke((float) ((int) scaleFactor)));
        g.draw(path);
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatWindowAbstractIcon
    public Color getForeground(Component c) {
        return FlatButtonUI.buttonStateColor(c, c.getForeground(), null, null, this.hoverForeground, this.pressedForeground);
    }
}
