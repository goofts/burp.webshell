package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RectangularShape;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Rect extends ShapeElement {
    public static final String TAG_NAME = "rect";
    float height = 0.0f;
    RectangularShape rect;
    float rx = 0.0f;
    float ry = 0.0f;
    float width = 0.0f;
    float x = 0.0f;
    float y = 0.0f;

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.writeFloat(this.x);
        out.writeFloat(this.y);
        out.writeFloat(this.width);
        out.writeFloat(this.height);
        out.writeFloat(this.rx);
        out.writeFloat(this.ry);
    }

    private void readObject(ObjectInputStream in) throws IOException {
        this.x = in.readFloat();
        this.y = in.readFloat();
        this.width = in.readFloat();
        this.height = in.readFloat();
        this.rx = in.readFloat();
        this.ry = in.readFloat();
        if (this.rx == 0.0f && this.ry == 0.0f) {
            this.rect = new Rectangle2D.Float(this.x, this.y, this.width, this.height);
        } else {
            this.rect = new RoundRectangle2D.Float(this.x, this.y, this.width, this.height, this.rx * 2.0f, this.ry * 2.0f);
        }
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
        boolean rxSet = false;
        if (getPres(sty.setName("rx"))) {
            this.rx = sty.getFloatValueWithUnits();
            rxSet = true;
        }
        boolean rySet = false;
        if (getPres(sty.setName("ry"))) {
            this.ry = sty.getFloatValueWithUnits();
            rySet = true;
        }
        if (!rxSet) {
            this.rx = this.ry;
        }
        if (!rySet) {
            this.ry = this.rx;
        }
        if (this.rx == 0.0f && this.ry == 0.0f) {
            this.rect = new Rectangle2D.Float(this.x, this.y, this.width, this.height);
        } else {
            this.rect = new RoundRectangle2D.Float(this.x, this.y, this.width, this.height, this.rx * 2.0f, this.ry * 2.0f);
        }
    }

    @Override // com.kitfox.svg.ShapeElement, com.kitfox.svg.RenderableElement
    public void render(Graphics2D g) throws SVGException {
        beginLayer(g);
        renderShape(g, this.rect);
        finishLayer(g);
    }

    @Override // com.kitfox.svg.ShapeElement
    public Shape getShape() {
        return shapeToParent(this.rect);
    }

    @Override // com.kitfox.svg.RenderableElement
    public Rectangle2D getBoundingBox() throws SVGException {
        return boundsToParent(includeStrokeInBounds(this.rect.getBounds2D()));
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
        if (getPres(sty.setName("rx"))) {
            float newVal5 = sty.getFloatValueWithUnits();
            if (newVal5 != this.rx) {
                this.rx = newVal5;
                shapeChange = true;
            }
        }
        if (getPres(sty.setName("ry"))) {
            float newVal6 = sty.getFloatValueWithUnits();
            if (newVal6 != this.ry) {
                this.ry = newVal6;
                shapeChange = true;
            }
        }
        if (shapeChange) {
            build();
        }
        return changeState || shapeChange;
    }
}
