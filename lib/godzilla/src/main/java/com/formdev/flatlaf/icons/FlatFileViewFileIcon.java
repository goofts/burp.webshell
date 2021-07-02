package com.formdev.flatlaf.icons;

import com.formdev.flatlaf.ui.FlatUIUtils;
import java.awt.Component;
import java.awt.Graphics2D;
import javax.swing.UIManager;

public class FlatFileViewFileIcon extends FlatAbstractIcon {
    public FlatFileViewFileIcon() {
        super(16, 16, UIManager.getColor("Objects.Grey"));
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatAbstractIcon
    public void paintIcon(Component c, Graphics2D g) {
        g.fill(FlatUIUtils.createPath(8.0d, 6.0d, 8.0d, 1.0d, 13.0d, 1.0d, 13.0d, 15.0d, 3.0d, 15.0d, 3.0d, 6.0d));
        g.fill(FlatUIUtils.createPath(3.0d, 5.0d, 7.0d, 5.0d, 7.0d, 1.0d));
    }
}
