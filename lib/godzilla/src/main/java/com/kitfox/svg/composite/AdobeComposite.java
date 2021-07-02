package com.kitfox.svg.composite;

import com.kitfox.svg.SVGConst;
import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdobeComposite implements Composite {
    public static final int CT_LAST = 2;
    public static final int CT_MULTIPLY = 1;
    public static final int CT_NORMAL = 0;
    final int compositeType;
    final float extraAlpha;

    public AdobeComposite(int compositeType2, float extraAlpha2) {
        this.compositeType = compositeType2;
        this.extraAlpha = extraAlpha2;
        if (compositeType2 < 0 || compositeType2 >= 2) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Invalid composite type");
        }
        if (extraAlpha2 < 0.0f || extraAlpha2 > 1.0f) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Invalid alpha");
        }
    }

    public int getCompositeType() {
        return this.compositeType;
    }

    public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
        return new AdobeCompositeContext(this.compositeType, this.extraAlpha);
    }
}
