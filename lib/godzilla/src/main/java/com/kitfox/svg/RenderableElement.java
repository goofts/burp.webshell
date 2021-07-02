package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.net.URI;
import java.util.List;

public abstract class RenderableElement extends TransformableElement {
    public static final int VECTOR_EFFECT_NONE = 0;
    public static final int VECTOR_EFFECT_NON_SCALING_STROKE = 1;
    Shape cachedClip = null;
    AffineTransform cachedXform = null;
    int vectorEffect;

    public abstract Rectangle2D getBoundingBox() throws SVGException;

    /* access modifiers changed from: package-private */
    public abstract void pick(Point2D point2D, boolean z, List<List<SVGElement>> list) throws SVGException;

    /* access modifiers changed from: package-private */
    public abstract void pick(Rectangle2D rectangle2D, AffineTransform affineTransform, boolean z, List<List<SVGElement>> list) throws SVGException;

    public abstract void render(Graphics2D graphics2D) throws SVGException;

    public RenderableElement() {
    }

    public RenderableElement(String id, SVGElement parent) {
        super(id, parent);
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.TransformableElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        if (!getPres(sty.setName("vector-effect"))) {
            this.vectorEffect = 0;
        } else if ("non-scaling-stroke".equals(sty.getStringValue())) {
            this.vectorEffect = 1;
        } else {
            this.vectorEffect = 0;
        }
    }

    /* access modifiers changed from: protected */
    public void beginLayer(Graphics2D g) throws SVGException {
        URI uri;
        if (this.xform != null) {
            this.cachedXform = g.getTransform();
            g.transform(this.xform);
        }
        StyleAttribute styleAttrib = new StyleAttribute();
        Shape clipPath = null;
        int clipPathUnits = 0;
        if (getStyle(styleAttrib.setName("clip-path"), false) && !"none".equals(styleAttrib.getStringValue()) && (uri = styleAttrib.getURIValue(getXMLBase())) != null) {
            ClipPath ele = (ClipPath) this.diagram.getUniverse().getElement(uri);
            clipPath = ele.getClipPathShape();
            clipPathUnits = ele.getClipPathUnits();
        }
        if (clipPath != null) {
            if (clipPathUnits == 1 && (this instanceof ShapeElement)) {
                Rectangle2D rect = ((ShapeElement) this).getBoundingBox();
                AffineTransform at = new AffineTransform();
                at.scale(rect.getWidth(), rect.getHeight());
                clipPath = at.createTransformedShape(clipPath);
            }
            this.cachedClip = g.getClip();
            if (this.cachedClip == null) {
                g.setClip(clipPath);
                return;
            }
            Area newClip = new Area(this.cachedClip);
            newClip.intersect(new Area(clipPath));
            g.setClip(newClip);
        }
    }

    /* access modifiers changed from: protected */
    public void finishLayer(Graphics2D g) {
        if (this.cachedClip != null) {
            g.setClip(this.cachedClip);
        }
        if (this.cachedXform != null) {
            g.setTransform(this.cachedXform);
        }
    }
}
