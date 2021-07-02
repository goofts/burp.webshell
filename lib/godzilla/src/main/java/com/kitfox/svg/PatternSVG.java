package com.kitfox.svg;

import com.kitfox.svg.pattern.PatternPaint;
import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PatternSVG extends FillElement {
    public static final int GU_OBJECT_BOUNDING_BOX = 0;
    public static final int GU_USER_SPACE_ON_USE = 1;
    public static final String TAG_NAME = "pattern";
    int gradientUnits = 0;
    float height;
    AffineTransform patternXform = new AffineTransform();
    Paint texPaint;
    Rectangle2D.Float viewBox;
    float width;
    float x;
    float y;

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    @Override // com.kitfox.svg.SVGElement
    public void loaderAddChild(SVGLoaderHelper helper, SVGElement child) throws SVGElementException {
        super.loaderAddChild(helper, child);
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.SVGElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        String href = null;
        if (getPres(sty.setName("xlink:href"))) {
            href = sty.getStringValue();
        }
        if (href != null) {
            try {
                PatternSVG patSrc = (PatternSVG) this.diagram.getUniverse().getElement(getXMLBase().resolve(href));
                this.gradientUnits = patSrc.gradientUnits;
                this.x = patSrc.x;
                this.y = patSrc.y;
                this.width = patSrc.width;
                this.height = patSrc.height;
                this.viewBox = patSrc.viewBox;
                this.patternXform.setTransform(patSrc.patternXform);
                this.children.addAll(patSrc.children);
            } catch (Exception e) {
                Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not parse xlink:href", (Throwable) e);
            }
        }
        String gradientUnits2 = "";
        if (getPres(sty.setName("gradientUnits"))) {
            gradientUnits2 = sty.getStringValue().toLowerCase();
        }
        if (gradientUnits2.equals("userspaceonuse")) {
            this.gradientUnits = 1;
        } else {
            this.gradientUnits = 0;
        }
        String patternTransform = "";
        if (getPres(sty.setName("patternTransform"))) {
            patternTransform = sty.getStringValue();
        }
        this.patternXform = parseTransform(patternTransform);
        if (getPres(sty.setName("x"))) {
            this.x = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("y"))) {
            this.y = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("width"))) {
            this.width = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("height"))) {
            this.height = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("viewBox"))) {
            float[] dim = sty.getFloatList();
            this.viewBox = new Rectangle2D.Float(dim[0], dim[1], dim[2], dim[3]);
        }
        preparePattern();
    }

    /* access modifiers changed from: protected */
    public void preparePattern() throws SVGException {
        int tileWidth = (int) this.width;
        int tileHeight = (int) this.height;
        float stretchX = 1.0f;
        float stretchY = 1.0f;
        if (!this.patternXform.isIdentity()) {
            float xlateX = (float) this.patternXform.getTranslateX();
            float xlateY = (float) this.patternXform.getTranslateY();
            Point2D.Float pt = new Point2D.Float();
            Point2D.Float pt2 = new Point2D.Float();
            pt.setLocation(this.width, 0.0f);
            this.patternXform.transform(pt, pt2);
            pt2.x -= xlateX;
            pt2.y -= xlateY;
            stretchX = (((float) Math.sqrt((double) ((pt2.x * pt2.x) + (pt2.y * pt2.y)))) * 1.5f) / this.width;
            pt.setLocation(this.height, 0.0f);
            this.patternXform.transform(pt, pt2);
            pt2.x -= xlateX;
            pt2.y -= xlateY;
            stretchY = (((float) Math.sqrt((double) ((pt2.x * pt2.x) + (pt2.y * pt2.y)))) * 1.5f) / this.height;
            tileWidth = (int) (((float) tileWidth) * stretchX);
            tileHeight = (int) (((float) tileHeight) * stretchY);
        }
        if (tileWidth != 0 && tileHeight != 0) {
            BufferedImage buf = new BufferedImage(tileWidth, tileHeight, 2);
            Graphics2D g = buf.createGraphics();
            g.setClip(0, 0, tileWidth, tileHeight);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            Iterator it = this.children.iterator();
            while (it.hasNext()) {
                SVGElement ele = (SVGElement) it.next();
                if (ele instanceof RenderableElement) {
                    AffineTransform xform = new AffineTransform();
                    if (this.viewBox == null) {
                        xform.translate((double) (-this.x), (double) (-this.y));
                    } else {
                        xform.scale((double) (((float) tileWidth) / this.viewBox.width), (double) (((float) tileHeight) / this.viewBox.height));
                        xform.translate((double) (-this.viewBox.x), (double) (-this.viewBox.y));
                    }
                    g.setTransform(xform);
                    ((RenderableElement) ele).render(g);
                }
            }
            g.dispose();
            if (this.patternXform.isIdentity()) {
                this.texPaint = new TexturePaint(buf, new Rectangle2D.Float(this.x, this.y, this.width, this.height));
                return;
            }
            this.patternXform.scale((double) (1.0f / stretchX), (double) (1.0f / stretchY));
            this.texPaint = new PatternPaint(buf, this.patternXform);
        }
    }

    @Override // com.kitfox.svg.FillElement
    public Paint getPaint(Rectangle2D bounds, AffineTransform xform) {
        return this.texPaint;
    }

    @Override // com.kitfox.svg.SVGElement
    public boolean updateTime(double curTime) throws SVGException {
        return false;
    }
}
