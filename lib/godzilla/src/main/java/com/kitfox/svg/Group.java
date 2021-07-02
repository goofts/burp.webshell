package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;
import java.util.List;

public class Group extends ShapeElement {
    public static final String TAG_NAME = "group";
    Rectangle2D boundingBox;
    Shape cachedShape;

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    @Override // com.kitfox.svg.SVGElement
    public void loaderAddChild(SVGLoaderHelper helper, SVGElement child) throws SVGElementException {
        super.loaderAddChild(helper, child);
    }

    /* access modifiers changed from: protected */
    public boolean outsideClip(Graphics2D g) throws SVGException {
        Shape clip = g.getClip();
        if (clip != null && !clip.intersects(getBoundingBox())) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    @Override // com.kitfox.svg.ShapeElement, com.kitfox.svg.RenderableElement
    public void pick(Point2D point, boolean boundingBox2, List<List<SVGElement>> retVec) throws SVGException {
        Point2D xPoint = new Point2D.Double(point.getX(), point.getY());
        if (this.xform != null) {
            try {
                this.xform.inverseTransform(point, xPoint);
            } catch (NoninvertibleTransformException ex) {
                throw new SVGException((Throwable) ex);
            }
        }
        Iterator it = this.children.iterator();
        while (it.hasNext()) {
            SVGElement ele = (SVGElement) it.next();
            if (ele instanceof RenderableElement) {
                ((RenderableElement) ele).pick(xPoint, boundingBox2, retVec);
            }
        }
    }

    /* access modifiers changed from: package-private */
    @Override // com.kitfox.svg.ShapeElement, com.kitfox.svg.RenderableElement
    public void pick(Rectangle2D pickArea, AffineTransform ltw, boolean boundingBox2, List<List<SVGElement>> retVec) throws SVGException {
        if (this.xform != null) {
            AffineTransform ltw2 = new AffineTransform(ltw);
            ltw2.concatenate(this.xform);
            ltw = ltw2;
        }
        Iterator it = this.children.iterator();
        while (it.hasNext()) {
            SVGElement ele = (SVGElement) it.next();
            if (ele instanceof RenderableElement) {
                ((RenderableElement) ele).pick(pickArea, ltw, boundingBox2, retVec);
            }
        }
    }

    @Override // com.kitfox.svg.ShapeElement, com.kitfox.svg.RenderableElement
    public void render(Graphics2D g) throws SVGException {
        StyleAttribute styleAttrib = new StyleAttribute();
        if (!getStyle(styleAttrib.setName("display")) || !styleAttrib.getStringValue().equals("none")) {
            boolean ignoreClip = this.diagram.ignoringClipHeuristic();
            beginLayer(g);
            Iterator<SVGElement> it = this.children.iterator();
            Shape clip = g.getClip();
            while (it.hasNext()) {
                SVGElement ele = it.next();
                if (ele instanceof RenderableElement) {
                    RenderableElement rendEle = (RenderableElement) ele;
                    if ((ele instanceof Group) || ignoreClip || clip == null || clip.intersects(rendEle.getBoundingBox())) {
                        rendEle.render(g);
                    }
                }
            }
            finishLayer(g);
        }
    }

    @Override // com.kitfox.svg.ShapeElement
    public Shape getShape() {
        if (this.cachedShape == null) {
            calcShape();
        }
        return this.cachedShape;
    }

    public void calcShape() {
        Shape shape;
        Area retShape = new Area();
        Iterator it = this.children.iterator();
        while (it.hasNext()) {
            SVGElement ele = (SVGElement) it.next();
            if ((ele instanceof ShapeElement) && (shape = ((ShapeElement) ele).getShape()) != null) {
                retShape.add(new Area(shape));
            }
        }
        this.cachedShape = shapeToParent(retShape);
    }

    @Override // com.kitfox.svg.RenderableElement
    public Rectangle2D getBoundingBox() throws SVGException {
        if (this.boundingBox == null) {
            calcBoundingBox();
        }
        return this.boundingBox;
    }

    public void calcBoundingBox() throws SVGException {
        Rectangle2D bounds;
        Rectangle2D retRect = null;
        Iterator it = this.children.iterator();
        while (it.hasNext()) {
            SVGElement ele = (SVGElement) it.next();
            if ((ele instanceof RenderableElement) && (bounds = ((RenderableElement) ele).getBoundingBox()) != null) {
                if (bounds.getWidth() != 0.0d || bounds.getHeight() != 0.0d) {
                    if (retRect == null) {
                        retRect = bounds;
                    } else if (retRect.getWidth() != 0.0d || retRect.getHeight() != 0.0d) {
                        retRect = retRect.createUnion(bounds);
                    }
                }
            }
        }
        if (retRect == null) {
            retRect = new Rectangle2D.Float();
        }
        this.boundingBox = boundsToParent(retRect);
    }

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.TransformableElement
    public boolean updateTime(double curTime) throws SVGException {
        boolean changeState = super.updateTime(curTime);
        Iterator<SVGElement> it = this.children.iterator();
        while (it.hasNext()) {
            SVGElement ele = it.next();
            changeState = changeState || ele.updateTime(curTime);
            if (ele instanceof ShapeElement) {
                this.cachedShape = null;
            }
            if (ele instanceof RenderableElement) {
                this.boundingBox = null;
            }
        }
        return changeState;
    }
}
