package com.kitfox.svg;

import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public abstract class FillElement extends SVGElement {
    public abstract Paint getPaint(Rectangle2D rectangle2D, AffineTransform affineTransform);
}
