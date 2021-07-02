package com.kitfox.svg.pattern;

import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

public class PatternPaint implements Paint {
    BufferedImage source;
    AffineTransform xform;

    public PatternPaint(BufferedImage source2, AffineTransform xform2) {
        this.source = source2;
        this.xform = xform2;
    }

    public PaintContext createContext(ColorModel cm, Rectangle deviceBounds, Rectangle2D userBounds, AffineTransform xform2, RenderingHints hints) {
        return new PatternPaintContext(this.source, deviceBounds, xform2, this.xform);
    }

    public int getTransparency() {
        return this.source.getColorModel().getTransparency();
    }
}
