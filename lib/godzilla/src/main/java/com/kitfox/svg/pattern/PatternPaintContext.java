package com.kitfox.svg.pattern;

import com.kitfox.svg.SVGConst;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.Raster;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatternPaintContext implements PaintContext {
    BufferedImage buf;
    Rectangle deviceBounds;
    BufferedImage source;
    int sourceHeight;
    int sourceWidth;
    AffineTransform xform;

    public PatternPaintContext(BufferedImage source2, Rectangle deviceBounds2, AffineTransform userXform, AffineTransform distortXform) {
        this.source = source2;
        this.deviceBounds = deviceBounds2;
        try {
            this.xform = distortXform.createInverse();
            this.xform.concatenate(userXform.createInverse());
        } catch (Exception e) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, (String) null, (Throwable) e);
        }
        this.sourceWidth = source2.getWidth();
        this.sourceHeight = source2.getHeight();
    }

    public void dispose() {
    }

    public ColorModel getColorModel() {
        return this.source.getColorModel();
    }

    public Raster getRaster(int x, int y, int w, int h) {
        if (!(this.buf != null && this.buf.getWidth() == w && this.buf.getHeight() == h)) {
            this.buf = new BufferedImage(w, h, this.source.getType());
        }
        Point2D.Float srcPt = new Point2D.Float();
        Point2D.Float destPt = new Point2D.Float();
        for (int j = 0; j < h; j++) {
            for (int i = 0; i < w; i++) {
                destPt.setLocation((float) (i + x), (float) (j + y));
                this.xform.transform(destPt, srcPt);
                int ii = ((int) srcPt.x) % this.sourceWidth;
                if (ii < 0) {
                    ii += this.sourceWidth;
                }
                int jj = ((int) srcPt.y) % this.sourceHeight;
                if (jj < 0) {
                    jj += this.sourceHeight;
                }
                this.buf.setRGB(i, j, this.source.getRGB(ii, jj));
            }
        }
        return this.buf.getData();
    }

    public static void main(String[] argv) {
        System.err.println("Hello " + 0);
    }
}
