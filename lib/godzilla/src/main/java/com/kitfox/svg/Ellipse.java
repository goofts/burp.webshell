package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;

public class Ellipse extends ShapeElement {
    public static final String TAG_NAME = "ellipse";
    float cx = 0.0f;
    float cy = 0.0f;
    Ellipse2D.Float ellipse = new Ellipse2D.Float();
    float rx = 0.0f;
    float ry = 0.0f;

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
        if (getPres(sty.setName("rx"))) {
            this.rx = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("ry"))) {
            this.ry = sty.getFloatValueWithUnits();
        }
        this.ellipse.setFrame(this.cx - this.rx, this.cy - this.ry, this.rx * 2.0f, this.ry * 2.0f);
    }

    @Override // com.kitfox.svg.ShapeElement, com.kitfox.svg.RenderableElement
    public void render(Graphics2D g) throws SVGException {
        beginLayer(g);
        renderShape(g, this.ellipse);
        finishLayer(g);
    }

    @Override // com.kitfox.svg.ShapeElement
    public Shape getShape() {
        return shapeToParent(this.ellipse);
    }

    @Override // com.kitfox.svg.RenderableElement
    public Rectangle2D getBoundingBox() throws SVGException {
        return boundsToParent(includeStrokeInBounds(this.ellipse.getBounds2D()));
    }

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.TransformableElement
    public boolean updateTime(double curTime) throws SVGException {
        boolean changeState = super.updateTime(curTime);
        StyleAttribute sty = new StyleAttribute();
        boolean shapeChange = false;
        if (getPres(sty.setName("cx"))) {
            float newCx = sty.getFloatValueWithUnits();
            if (newCx != this.cx) {
                this.cx = newCx;
                shapeChange = true;
            }
        }
        if (getPres(sty.setName("cy"))) {
            float newCy = sty.getFloatValueWithUnits();
            if (newCy != this.cy) {
                this.cy = newCy;
                shapeChange = true;
            }
        }
        if (getPres(sty.setName("rx"))) {
            float newRx = sty.getFloatValueWithUnits();
            if (newRx != this.rx) {
                this.rx = newRx;
                shapeChange = true;
            }
        }
        if (getPres(sty.setName("ry"))) {
            float newRy = sty.getFloatValueWithUnits();
            if (newRy != this.ry) {
                this.ry = newRy;
                shapeChange = true;
            }
        }
        if (shapeChange) {
            build();
        }
        return changeState || shapeChange;
    }
}
