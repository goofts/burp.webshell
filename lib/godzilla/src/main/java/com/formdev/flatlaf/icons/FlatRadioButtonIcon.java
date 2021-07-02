package com.formdev.flatlaf.icons;

import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;

public class FlatRadioButtonIcon extends FlatCheckBoxIcon {
    protected final int centerDiameter = getUIInt("RadioButton.icon.centerDiameter", 8, this.style);

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatCheckBoxIcon
    public void paintFocusBorder(Component c, Graphics2D g) {
        int wh = (this.focusWidth * 2) + 15;
        g.fillOval(-this.focusWidth, -this.focusWidth, wh, wh);
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatCheckBoxIcon
    public void paintBorder(Component c, Graphics2D g) {
        g.fillOval(0, 0, 15, 15);
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatCheckBoxIcon
    public void paintBackground(Component c, Graphics2D g) {
        g.fillOval(1, 1, 13, 13);
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatCheckBoxIcon
    public void paintCheckmark(Component c, Graphics2D g) {
        float xy = ((float) (15 - this.centerDiameter)) / 2.0f;
        g.fill(new Ellipse2D.Float(xy, xy, (float) this.centerDiameter, (float) this.centerDiameter));
    }
}
