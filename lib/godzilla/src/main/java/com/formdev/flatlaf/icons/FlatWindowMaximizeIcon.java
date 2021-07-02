package com.formdev.flatlaf.icons;

import com.formdev.flatlaf.ui.FlatUIUtils;
import java.awt.Graphics2D;

public class FlatWindowMaximizeIcon extends FlatWindowAbstractIcon {
    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatWindowAbstractIcon
    public void paintIconAt1x(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
        int iwh = (int) (10.0d * scaleFactor);
        g.fill(FlatUIUtils.createRectangle((float) (x + ((width - iwh) / 2)), (float) (y + ((height - iwh) / 2)), (float) iwh, (float) iwh, (float) ((int) scaleFactor)));
    }
}
