package com.formdev.flatlaf.icons;

import com.formdev.flatlaf.ui.FlatUIUtils;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import javax.swing.UIManager;

public class FlatFileViewFloppyDriveIcon extends FlatAbstractIcon {
    public FlatFileViewFloppyDriveIcon() {
        super(16, 16, UIManager.getColor("Objects.Grey"));
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatAbstractIcon
    public void paintIcon(Component c, Graphics2D g) {
        Path2D path = new Path2D.Float(0);
        path.append(FlatUIUtils.createPath(11.0d, 14.0d, 11.0d, 11.0d, 5.0d, 11.0d, 5.0d, 14.0d, 2.0d, 14.0d, 2.0d, 2.0d, 14.0d, 2.0d, 14.0d, 14.0d, 11.0d, 14.0d), false);
        path.append(FlatUIUtils.createPath(4.0d, 4.0d, 4.0d, 8.0d, 12.0d, 8.0d, 12.0d, 4.0d, 4.0d, 4.0d), false);
        g.fill(path);
        g.fillRect(6, 12, 4, 2);
    }
}
