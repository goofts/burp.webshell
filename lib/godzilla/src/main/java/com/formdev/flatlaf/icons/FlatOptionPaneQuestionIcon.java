package com.formdev.flatlaf.icons;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class FlatOptionPaneQuestionIcon extends FlatOptionPaneAbstractIcon {
    public FlatOptionPaneQuestionIcon() {
        super("OptionPane.icon.questionColor", "Actions.Blue");
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatOptionPaneAbstractIcon
    public Shape createOutside() {
        return new Ellipse2D.Float(2.0f, 2.0f, 28.0f, 28.0f);
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatOptionPaneAbstractIcon
    public Shape createInside() {
        Path2D q = new Path2D.Float();
        q.moveTo(14.0d, 20.0d);
        q.lineTo(18.0d, 20.0d);
        q.curveTo(18.0d, 16.0d, 23.0d, 16.0d, 23.0d, 12.0d);
        q.curveTo(23.0d, 8.0d, 20.0d, 6.0d, 16.0d, 6.0d);
        q.curveTo(12.0d, 6.0d, 9.0d, 8.0d, 9.0d, 12.0d);
        q.curveTo(9.0d, 12.0d, 13.0d, 12.0d, 13.0d, 12.0d);
        q.curveTo(13.0d, 10.0d, 14.0d, 9.0d, 16.0d, 9.0d);
        q.curveTo(18.0d, 9.0d, 19.0d, 10.0d, 19.0d, 12.0d);
        q.curveTo(19.0d, 15.0d, 14.0d, 15.0d, 14.0d, 20.0d);
        q.closePath();
        Path2D inside = new Path2D.Float(0);
        inside.append(new Rectangle2D.Float(14.0f, 22.0f, 4.0f, 4.0f), false);
        inside.append(q, false);
        return inside;
    }
}
