package com.formdev.flatlaf.icons;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class FlatOptionPaneInformationIcon extends FlatOptionPaneAbstractIcon {
    public FlatOptionPaneInformationIcon() {
        super("OptionPane.icon.informationColor", "Actions.Blue");
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatOptionPaneAbstractIcon
    public Shape createOutside() {
        return new Ellipse2D.Float(2.0f, 2.0f, 28.0f, 28.0f);
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatOptionPaneAbstractIcon
    public Shape createInside() {
        Path2D inside = new Path2D.Float(0);
        inside.append(new Rectangle2D.Float(14.0f, 14.0f, 4.0f, 11.0f), false);
        inside.append(new Rectangle2D.Float(14.0f, 7.0f, 4.0f, 4.0f), false);
        return inside;
    }
}
