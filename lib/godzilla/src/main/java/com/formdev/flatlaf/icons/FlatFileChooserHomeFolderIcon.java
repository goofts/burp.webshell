package com.formdev.flatlaf.icons;

import com.formdev.flatlaf.ui.FlatUIUtils;
import java.awt.Component;
import java.awt.Graphics2D;
import javax.swing.UIManager;

public class FlatFileChooserHomeFolderIcon extends FlatAbstractIcon {
    public FlatFileChooserHomeFolderIcon() {
        super(16, 16, UIManager.getColor("Actions.Grey"));
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatAbstractIcon
    public void paintIcon(Component c, Graphics2D g) {
        g.fill(FlatUIUtils.createPath(2.0d, 8.0d, 8.0d, 2.0d, 14.0d, 8.0d, 12.0d, 8.0d, 12.0d, 13.0d, 9.0d, 13.0d, 9.0d, 10.0d, 7.0d, 10.0d, 7.0d, 13.0d, 4.0d, 13.0d, 4.0d, 8.0d));
    }
}
