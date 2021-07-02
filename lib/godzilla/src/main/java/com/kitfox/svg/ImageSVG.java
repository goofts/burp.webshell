package com.kitfox.svg;

import com.kitfox.svg.app.data.Handler;
import com.kitfox.svg.xml.StyleAttribute;
import java.awt.AlphaComposite;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ImageSVG extends RenderableElement {
    public static final String TAG_NAME = "image";
    Rectangle2D bounds;
    float height = 0.0f;
    URL imageSrc = null;
    float width = 0.0f;
    float x = 0.0f;
    AffineTransform xform;
    float y = 0.0f;

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.RenderableElement, com.kitfox.svg.SVGElement, com.kitfox.svg.TransformableElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
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
        try {
            if (getPres(sty.setName("xlink:href"))) {
                URI src = sty.getURIValue(getXMLBase());
                if ("data".equals(src.getScheme())) {
                    this.imageSrc = new URL((URL) null, src.toASCIIString(), new Handler());
                } else if (!this.diagram.getUniverse().isImageDataInlineOnly()) {
                    try {
                        this.imageSrc = src.toURL();
                    } catch (Exception e) {
                        Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not parse xlink:href " + src, (Throwable) e);
                        this.imageSrc = null;
                    }
                }
            }
            if (this.imageSrc != null) {
                this.diagram.getUniverse().registerImage(this.imageSrc);
                BufferedImage img = this.diagram.getUniverse().getImage(this.imageSrc);
                if (img == null) {
                    this.xform = new AffineTransform();
                    this.bounds = new Rectangle2D.Float();
                    return;
                }
                if (this.width == 0.0f) {
                    this.width = (float) img.getWidth();
                }
                if (this.height == 0.0f) {
                    this.height = (float) img.getHeight();
                }
                this.xform = new AffineTransform();
                this.xform.translate((double) this.x, (double) this.y);
                this.xform.scale((double) (this.width / ((float) img.getWidth())), (double) (this.height / ((float) img.getHeight())));
            }
            this.bounds = new Rectangle2D.Float(this.x, this.y, this.width, this.height);
        } catch (Exception e2) {
            throw new SVGException(e2);
        }
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    /* access modifiers changed from: package-private */
    @Override // com.kitfox.svg.RenderableElement
    public void pick(Point2D point, boolean boundingBox, List<List<SVGElement>> retVec) throws SVGException {
        if (getBoundingBox().contains(point)) {
            retVec.add(getPath(null));
        }
    }

    /* access modifiers changed from: package-private */
    @Override // com.kitfox.svg.RenderableElement
    public void pick(Rectangle2D pickArea, AffineTransform ltw, boolean boundingBox, List<List<SVGElement>> retVec) throws SVGException {
        if (ltw.createTransformedShape(getBoundingBox()).intersects(pickArea)) {
            retVec.add(getPath(null));
        }
    }

    @Override // com.kitfox.svg.RenderableElement
    public void render(Graphics2D g) throws SVGException {
        StyleAttribute styleAttrib = new StyleAttribute();
        if (getStyle(styleAttrib.setName("visibility")) && !styleAttrib.getStringValue().equals("visible")) {
            return;
        }
        if (!getStyle(styleAttrib.setName("display")) || !styleAttrib.getStringValue().equals("none")) {
            beginLayer(g);
            float opacity = 1.0f;
            if (getStyle(styleAttrib.setName("opacity"))) {
                opacity = styleAttrib.getRatioValue();
            }
            if (opacity > 0.0f) {
                Composite oldComp = null;
                if (opacity < 1.0f) {
                    oldComp = g.getComposite();
                    g.setComposite(AlphaComposite.getInstance(3, opacity));
                }
                BufferedImage img = this.diagram.getUniverse().getImage(this.imageSrc);
                if (img != null) {
                    AffineTransform curXform = g.getTransform();
                    g.transform(this.xform);
                    g.drawImage(img, 0, 0, (ImageObserver) null);
                    g.setTransform(curXform);
                    if (oldComp != null) {
                        g.setComposite(oldComp);
                    }
                    finishLayer(g);
                }
            }
        }
    }

    @Override // com.kitfox.svg.RenderableElement
    public Rectangle2D getBoundingBox() {
        return boundsToParent(this.bounds);
    }

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.TransformableElement
    public boolean updateTime(double curTime) throws SVGException {
        boolean changeState = super.updateTime(curTime);
        StyleAttribute sty = new StyleAttribute();
        boolean shapeChange = false;
        if (getPres(sty.setName("x"))) {
            float newVal = sty.getFloatValueWithUnits();
            if (newVal != this.x) {
                this.x = newVal;
                shapeChange = true;
            }
        }
        if (getPres(sty.setName("y"))) {
            float newVal2 = sty.getFloatValueWithUnits();
            if (newVal2 != this.y) {
                this.y = newVal2;
                shapeChange = true;
            }
        }
        if (getPres(sty.setName("width"))) {
            float newVal3 = sty.getFloatValueWithUnits();
            if (newVal3 != this.width) {
                this.width = newVal3;
                shapeChange = true;
            }
        }
        if (getPres(sty.setName("height"))) {
            float newVal4 = sty.getFloatValueWithUnits();
            if (newVal4 != this.height) {
                this.height = newVal4;
                shapeChange = true;
            }
        }
        try {
            if (getPres(sty.setName("xlink:href"))) {
                URI src = sty.getURIValue(getXMLBase());
                URL newVal5 = null;
                if ("data".equals(src.getScheme())) {
                    newVal5 = new URL((URL) null, src.toASCIIString(), new Handler());
                } else if (!this.diagram.getUniverse().isImageDataInlineOnly()) {
                    newVal5 = src.toURL();
                }
                if (newVal5 != null && !newVal5.equals(this.imageSrc)) {
                    this.imageSrc = newVal5;
                    shapeChange = true;
                }
            }
        } catch (IllegalArgumentException ie) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Image provided with illegal value for href: \"" + sty.getStringValue() + '\"', (Throwable) ie);
        } catch (Exception e) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not parse xlink:href", (Throwable) e);
        }
        if (shapeChange) {
            build();
        }
        if (changeState || shapeChange) {
            return true;
        }
        return false;
    }
}
