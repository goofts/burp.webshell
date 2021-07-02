package com.formdev.flatlaf.icons;

import com.formdev.flatlaf.ui.FlatUIUtils;
import java.awt.Component;
import java.awt.Graphics2D;

public class FlatInternalFrameMaximizeIcon extends FlatInternalFrameAbstractIcon {
    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatAbstractIcon
    public void paintIcon(Component c, Graphics2D g) {
        paintBackground(c, g);
        g.setColor(c.getForeground());
        g.fill(FlatUIUtils.createRectangle((float) ((this.width / 2) - 4), (float) ((this.height / 2) - 4), 8.0f, 8.0f, 1.0f));
    }
}
