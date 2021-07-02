package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Symbol extends Group {
    public static final String TAG_NAME = "symbol";
    Rectangle2D viewBox;
    AffineTransform viewXform;

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.Group
    public String getTagName() {
        return TAG_NAME;
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.RenderableElement, com.kitfox.svg.SVGElement, com.kitfox.svg.TransformableElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("viewBox"))) {
            float[] dim = sty.getFloatList();
            this.viewBox = new Rectangle2D.Float(dim[0], dim[1], dim[2], dim[3]);
        }
        if (this.viewBox == null) {
            this.viewBox = new Rectangle(0, 0, 1, 1);
        }
        this.viewXform = new AffineTransform();
        this.viewXform.scale(1.0d / this.viewBox.getWidth(), 1.0d / this.viewBox.getHeight());
        this.viewXform.translate(-this.viewBox.getX(), -this.viewBox.getY());
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.Group
    public boolean outsideClip(Graphics2D g) throws SVGException {
        Shape clip = g.getClip();
        Rectangle2D rect = super.getBoundingBox();
        if (clip == null || clip.intersects(rect)) {
            return false;
        }
        return true;
    }

    @Override // com.kitfox.svg.ShapeElement, com.kitfox.svg.RenderableElement, com.kitfox.svg.Group
    public void render(Graphics2D g) throws SVGException {
        AffineTransform oldXform = g.getTransform();
        g.transform(this.viewXform);
        super.render(g);
        g.setTransform(oldXform);
    }

    @Override // com.kitfox.svg.ShapeElement, com.kitfox.svg.Group
    public Shape getShape() {
        return this.viewXform.createTransformedShape(super.getShape());
    }

    @Override // com.kitfox.svg.RenderableElement, com.kitfox.svg.Group
    public Rectangle2D getBoundingBox() throws SVGException {
        return this.viewXform.createTransformedShape(super.getBoundingBox()).getBounds2D();
    }

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.Group, com.kitfox.svg.TransformableElement
    public boolean updateTime(double curTime) throws SVGException {
        return super.updateTime(curTime);
    }
}
