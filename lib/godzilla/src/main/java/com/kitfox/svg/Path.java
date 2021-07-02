package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

public class Path extends ShapeElement {
    public static final String TAG_NAME = "path";
    String d = "";
    int fillRule = 1;
    GeneralPath path;

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.RenderableElement, com.kitfox.svg.SVGElement, com.kitfox.svg.TransformableElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        this.fillRule = (getStyle(sty.setName("fill-rule")) ? sty.getStringValue() : "nonzero").equals("evenodd") ? 0 : 1;
        if (getPres(sty.setName("d"))) {
            this.d = sty.getStringValue();
        }
        this.path = buildPath(this.d, this.fillRule);
    }

    @Override // com.kitfox.svg.ShapeElement, com.kitfox.svg.RenderableElement
    public void render(Graphics2D g) throws SVGException {
        beginLayer(g);
        renderShape(g, this.path);
        finishLayer(g);
    }

    @Override // com.kitfox.svg.ShapeElement
    public Shape getShape() {
        return shapeToParent(this.path);
    }

    @Override // com.kitfox.svg.RenderableElement
    public Rectangle2D getBoundingBox() throws SVGException {
        return boundsToParent(includeStrokeInBounds(this.path.getBounds2D()));
    }

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.TransformableElement
    public boolean updateTime(double curTime) throws SVGException {
        boolean changeState = super.updateTime(curTime);
        StyleAttribute sty = new StyleAttribute();
        boolean shapeChange = false;
        if (getStyle(sty.setName("fill-rule"))) {
            int newVal = sty.getStringValue().equals("evenodd") ? 0 : 1;
            if (newVal != this.fillRule) {
                this.fillRule = newVal;
                changeState = true;
            }
        }
        if (getPres(sty.setName("d"))) {
            String newVal2 = sty.getStringValue();
            if (!newVal2.equals(this.d)) {
                this.d = newVal2;
                shapeChange = true;
            }
        }
        if (shapeChange) {
            build();
        }
        return changeState || shapeChange;
    }
}
