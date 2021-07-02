package com.formdev.flatlaf.icons;

import com.formdev.flatlaf.ui.FlatUIUtils;
import java.awt.Shape;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class FlatOptionPaneWarningIcon extends FlatOptionPaneAbstractIcon {
    public FlatOptionPaneWarningIcon() {
        super("OptionPane.icon.warningColor", "Actions.Yellow");
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatOptionPaneAbstractIcon
    public Shape createOutside() {
        return FlatUIUtils.createPath(16.0d, 2.0d, 31.0d, 28.0d, 1.0d, 28.0d);
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatOptionPaneAbstractIcon
    public Shape createInside() {
        Path2D inside = new Path2D.Float(0);
        inside.append(new Rectangle2D.Float(14.0f, 10.0f, 4.0f, 8.0f), false);
        inside.append(new Rectangle2D.Float(14.0f, 21.0f, 4.0f, 4.0f), false);
        return inside;
    }
}
