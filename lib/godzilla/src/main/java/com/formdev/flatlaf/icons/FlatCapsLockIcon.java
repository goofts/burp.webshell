package com.formdev.flatlaf.icons;

import com.formdev.flatlaf.ui.FlatUIUtils;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;
import javax.swing.UIManager;

public class FlatCapsLockIcon extends FlatAbstractIcon {
    public FlatCapsLockIcon() {
        super(16, 16, UIManager.getColor("PasswordField.capsLockIconColor"));
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatAbstractIcon
    public void paintIcon(Component c, Graphics2D g) {
        Path2D path = new Path2D.Float(0);
        path.append(new RoundRectangle2D.Float(0.0f, 0.0f, 16.0f, 16.0f, 6.0f, 6.0f), false);
        path.append(new Rectangle2D.Float(5.0f, 12.0f, 6.0f, 2.0f), false);
        path.append(FlatUIUtils.createPath(2.0d, 8.0d, 8.0d, 2.0d, 14.0d, 8.0d, 11.0d, 8.0d, 11.0d, 10.0d, 5.0d, 10.0d, 5.0d, 8.0d), false);
        g.fill(path);
    }
}
