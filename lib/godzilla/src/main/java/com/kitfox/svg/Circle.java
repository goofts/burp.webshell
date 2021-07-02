package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Circle extends ShapeElement {
    public static final String TAG_NAME = "circle";
    Ellipse2D.Float circle = new Ellipse2D.Float();
    float cx = 0.0f;
    float cy = 0.0f;
    float r = 0.0f;

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.RenderableElement, com.kitfox.svg.SVGElement, com.kitfox.svg.TransformableElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("cx"))) {
            this.cx = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("cy"))) {
            this.cy = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("r"))) {
            this.r = sty.getFloatValueWithUnits();
        }
        this.circle.setFrame(this.cx - this.r, this.cy - this.r, this.r * 2.0f, this.r * 2.0f);
    }

    @Override // com.kitfox.svg.ShapeElement, com.kitfox.svg.RenderableElement
    public void render(Graphics2D g) throws SVGException {
        beginLayer(g);
        renderShape(g, this.circle);
        finishLayer(g);
    }

    @Override // com.kitfox.svg.ShapeElement
    public Shape getShape() {
        return shapeToParent(this.circle);
    }

    @Override // com.kitfox.svg.RenderableElement
    public Rectangle2D getBoundingBox() throws SVGException {
        return boundsToParent(includeStrokeInBounds(this.circle.getBounds2D()));
    }

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.TransformableElement
    public boolean updateTime(double curTime) throws SVGException {
        boolean changeState = super.updateTime(curTime);
        StyleAttribute sty = new StyleAttribute();
        boolean shapeChange = false;
        if (getPres(sty.setName("cx"))) {
            float newVal = sty.getFloatValueWithUnits();
            if (newVal != this.cx) {
                this.cx = newVal;
                shapeChange = true;
            }
        }
        if (getPres(sty.setName("cy"))) {
            float newVal2 = sty.getFloatValueWithUnits();
            if (newVal2 != this.cy) {
                this.cy = newVal2;
                shapeChange = true;
            }
        }
        if (getPres(sty.setName("r"))) {
            float newVal3 = sty.getFloatValueWithUnits();
            if (newVal3 != this.r) {
                this.r = newVal3;
                shapeChange = true;
            }
        }
        if (shapeChange) {
            build();
        }
        return changeState || shapeChange;
    }
}
