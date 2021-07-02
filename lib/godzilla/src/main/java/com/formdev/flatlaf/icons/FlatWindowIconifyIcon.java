package com.formdev.flatlaf.icons;

import java.awt.Graphics2D;

public class FlatWindowIconifyIcon extends FlatWindowAbstractIcon {
    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatWindowAbstractIcon
    public void paintIconAt1x(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
        int iw = (int) (10.0d * scaleFactor);
        int ih = (int) scaleFactor;
        g.fillRect(x + ((width - iw) / 2), y + ((height - ih) / 2), iw, ih);
    }
}
