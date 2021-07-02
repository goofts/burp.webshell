package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public abstract class TransformableElement extends SVGElement {
    AffineTransform xform = null;

    public TransformableElement() {
    }

    public TransformableElement(String id, SVGElement parent) {
        super(id, parent);
    }

    public AffineTransform getXForm() {
        if (this.xform == null) {
            return null;
        }
        return new AffineTransform(this.xform);
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.SVGElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("transform"))) {
            this.xform = parseTransform(sty.getStringValue());
        }
    }

    /* access modifiers changed from: protected */
    public Shape shapeToParent(Shape shape) {
        return this.xform == null ? shape : this.xform.createTransformedShape(shape);
    }

    /* access modifiers changed from: protected */
    public Rectangle2D boundsToParent(Rectangle2D rect) {
        return (this.xform == null || rect == null) ? rect : this.xform.createTransformedShape(rect).getBounds2D();
    }

    @Override // com.kitfox.svg.SVGElement
    public boolean updateTime(double curTime) throws SVGException {
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("transform"))) {
            AffineTransform newXform = parseTransform(sty.getStringValue());
            if (!newXform.equals(this.xform)) {
                this.xform = newXform;
                return true;
            }
        }
        return false;
    }
}
