package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;

public class Line extends ShapeElement {
    public static final String TAG_NAME = "line";
    Line2D.Float line;
    float x1 = 0.0f;
    float x2 = 0.0f;
    float y1 = 0.0f;
    float y2 = 0.0f;

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.RenderableElement, com.kitfox.svg.SVGElement, com.kitfox.svg.TransformableElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("x1"))) {
            this.x1 = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("y1"))) {
            this.y1 = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("x2"))) {
            this.x2 = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("y2"))) {
            this.y2 = sty.getFloatValueWithUnits();
        }
        this.line = new Line2D.Float(this.x1, this.y1, this.x2, this.y2);
    }

    @Override // com.kitfox.svg.ShapeElement, com.kitfox.svg.RenderableElement
    public void render(Graphics2D g) throws SVGException {
        beginLayer(g);
        renderShape(g, this.line);
        finishLayer(g);
    }

    @Override // com.kitfox.svg.ShapeElement
    public Shape getShape() {
        return shapeToParent(this.line);
    }

    @Override // com.kitfox.svg.RenderableElement
    public Rectangle2D getBoundingBox() throws SVGException {
        return boundsToParent(includeStrokeInBounds(this.line.getBounds2D()));
    }

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.TransformableElement
    public boolean updateTime(double curTime) throws SVGException {
        boolean changeState = super.updateTime(curTime);
        StyleAttribute sty = new StyleAttribute();
        boolean shapeChange = false;
        if (getPres(sty.setName("x1"))) {
            float newVal = sty.getFloatValueWithUnits();
            if (newVal != this.x1) {
                this.x1 = newVal;
                shapeChange = true;
            }
        }
        if (getPres(sty.setName("y1"))) {
            float newVal2 = sty.getFloatValueWithUnits();
            if (newVal2 != this.y1) {
                this.y1 = newVal2;
                shapeChange = true;
            }
        }
        if (getPres(sty.setName("x2"))) {
            float newVal3 = sty.getFloatValueWithUnits();
            if (newVal3 != this.x2) {
                this.x2 = newVal3;
                shapeChange = true;
            }
        }
        if (getPres(sty.setName("y2"))) {
            float newVal4 = sty.getFloatValueWithUnits();
            if (newVal4 != this.y2) {
                this.y2 = newVal4;
                shapeChange = true;
            }
        }
        if (shapeChange) {
            build();
        }
        return changeState || shapeChange;
    }
}
