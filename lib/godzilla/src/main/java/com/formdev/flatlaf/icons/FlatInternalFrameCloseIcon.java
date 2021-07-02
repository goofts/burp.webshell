package com.formdev.flatlaf.icons;

import com.formdev.flatlaf.ui.FlatButtonUI;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Path2D;
import javax.swing.UIManager;

public class FlatInternalFrameCloseIcon extends FlatInternalFrameAbstractIcon {
    private final Color hoverForeground = UIManager.getColor("InternalFrame.closeHoverForeground");
    private final Color pressedForeground = UIManager.getColor("InternalFrame.closePressedForeground");

    public FlatInternalFrameCloseIcon() {
        super(UIManager.getDimension("InternalFrame.buttonSize"), UIManager.getColor("InternalFrame.closeHoverBackground"), UIManager.getColor("InternalFrame.closePressedBackground"));
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatAbstractIcon
    public void paintIcon(Component c, Graphics2D g) {
        paintBackground(c, g);
        g.setColor(FlatButtonUI.buttonStateColor(c, c.getForeground(), null, null, this.hoverForeground, this.pressedForeground));
        float mx = (float) (this.width / 2);
        float my = (float) (this.height / 2);
        Path2D path = new Path2D.Float(0);
        path.append(new Line2D.Float(mx - 3.25f, my - 3.25f, mx + 3.25f, my + 3.25f), false);
        path.append(new Line2D.Float(mx - 3.25f, my + 3.25f, mx + 3.25f, my - 3.25f), false);
        g.setStroke(new BasicStroke(1.0f));
        g.draw(path);
    }
}
