package com.formdev.flatlaf.icons;

import com.formdev.flatlaf.ui.FlatUIUtils;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class FlatWindowRestoreIcon extends FlatWindowAbstractIcon {
    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatWindowAbstractIcon
    public void paintIconAt1x(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
        int iwh = (int) (10.0d * scaleFactor);
        int ix = x + ((width - iwh) / 2);
        int iy = y + ((height - iwh) / 2);
        int thickness = (int) scaleFactor;
        int rwh = (int) (8.0d * scaleFactor);
        int ro2 = iwh - rwh;
        Path2D r1 = FlatUIUtils.createRectangle((float) (ix + ro2), (float) iy, (float) rwh, (float) rwh, (float) thickness);
        Path2D r2 = FlatUIUtils.createRectangle((float) ix, (float) (iy + ro2), (float) rwh, (float) rwh, (float) thickness);
        Area area = new Area(r1);
        area.subtract(new Area(new Rectangle2D.Float((float) ix, (float) (iy + ro2), (float) rwh, (float) rwh)));
        g.fill(area);
        g.fill(r2);
    }
}
