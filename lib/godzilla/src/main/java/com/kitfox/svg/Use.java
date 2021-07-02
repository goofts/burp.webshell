package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.net.URI;

public class Use extends ShapeElement {
    public static final String TAG_NAME = "use";
    float height = 1.0f;
    URI href = null;
    AffineTransform refXform;
    float width = 1.0f;
    float x = 0.0f;
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
        if (getPres(sty.setName("xlink:href"))) {
            this.href = sty.getURIValue(getXMLBase());
        }
        this.refXform = new AffineTransform();
        this.refXform.translate((double) this.x, (double) this.y);
    }

    @Override // com.kitfox.svg.ShapeElement, com.kitfox.svg.RenderableElement
    public void render(Graphics2D g) throws SVGException {
        beginLayer(g);
        AffineTransform oldXform = g.getTransform();
        g.transform(this.refXform);
        SVGElement ref = this.diagram.getUniverse().getElement(this.href);
        if (ref != null && (ref instanceof RenderableElement)) {
            RenderableElement rendEle = (RenderableElement) ref;
            rendEle.pushParentContext(this);
            rendEle.render(g);
            rendEle.popParentContext();
            g.setTransform(oldXform);
            finishLayer(g);
        }
    }

    @Override // com.kitfox.svg.ShapeElement
    public Shape getShape() {
        SVGElement ref = this.diagram.getUniverse().getElement(this.href);
        if (!(ref instanceof ShapeElement)) {
            return null;
        }
        return shapeToParent(this.refXform.createTransformedShape(((ShapeElement) ref).getShape()));
    }

    @Override // com.kitfox.svg.RenderableElement
    public Rectangle2D getBoundingBox() throws SVGException {
        SVGElement ref = this.diagram.getUniverse().getElement(this.href);
        if (!(ref instanceof ShapeElement)) {
            return null;
        }
        ShapeElement shapeEle = (ShapeElement) ref;
        shapeEle.pushParentContext(this);
        Rectangle2D bounds = shapeEle.getBoundingBox();
        shapeEle.popParentContext();
        return boundsToParent(this.refXform.createTransformedShape(bounds).getBounds2D());
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
        if (getPres(sty.setName("xlink:href"))) {
            URI src = sty.getURIValue(getXMLBase());
            if (!src.equals(this.href)) {
                this.href = src;
                shapeChange = true;
            }
        }
        if (shapeChange) {
            build();
        }
        return changeState || shapeChange;
    }
}
