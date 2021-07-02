package com.kitfox.svg;

import com.kitfox.svg.pathcmd.BuildHistory;
import com.kitfox.svg.pathcmd.PathCommand;
import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.Iterator;

public class MissingGlyph extends ShapeElement {
    public static final String TAG_NAME = "missingglyph";
    private float horizAdvX = -1.0f;
    private Shape path = null;
    private float vertAdvY = -1.0f;
    private float vertOriginX = -1.0f;
    private float vertOriginY = -1.0f;

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return "missingglyph";
    }

    @Override // com.kitfox.svg.SVGElement
    public void loaderAddChild(SVGLoaderHelper helper, SVGElement child) throws SVGElementException {
        super.loaderAddChild(helper, child);
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.RenderableElement, com.kitfox.svg.SVGElement, com.kitfox.svg.TransformableElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        String commandList = "";
        if (getPres(sty.setName("d"))) {
            commandList = sty.getStringValue();
        }
        if (commandList != null) {
            String fillRule = getStyle(sty.setName("fill-rule")) ? sty.getStringValue() : "nonzero";
            PathCommand[] commands = parsePathList(commandList);
            GeneralPath buildPath = new GeneralPath(fillRule.equals("evenodd") ? 0 : 1, commands.length);
            BuildHistory hist = new BuildHistory();
            for (PathCommand cmd : commands) {
                cmd.appendPath(buildPath, hist);
            }
            AffineTransform at = new AffineTransform();
            at.scale(1.0d, -1.0d);
            this.path = at.createTransformedShape(buildPath);
        }
        if (getPres(sty.setName("horiz-adv-x"))) {
            this.horizAdvX = sty.getFloatValue();
        }
        if (getPres(sty.setName("vert-origin-x"))) {
            this.vertOriginX = sty.getFloatValue();
        }
        if (getPres(sty.setName("vert-origin-y"))) {
            this.vertOriginY = sty.getFloatValue();
        }
        if (getPres(sty.setName("vert-adv-y"))) {
            this.vertAdvY = sty.getFloatValue();
        }
    }

    public Shape getPath() {
        return this.path;
    }

    @Override // com.kitfox.svg.ShapeElement, com.kitfox.svg.RenderableElement
    public void render(Graphics2D g) throws SVGException {
        if (this.path != null) {
            renderShape(g, this.path);
        }
        Iterator<SVGElement> it = this.children.iterator();
        while (it.hasNext()) {
            SVGElement ele = it.next();
            if (ele instanceof RenderableElement) {
                ((RenderableElement) ele).render(g);
            }
        }
    }

    public float getHorizAdvX() {
        if (this.horizAdvX == -1.0f) {
            this.horizAdvX = (float) ((Font) this.parent).getHorizAdvX();
        }
        return this.horizAdvX;
    }

    public float getVertOriginX() {
        if (this.vertOriginX == -1.0f) {
            this.vertOriginX = getHorizAdvX() / 2.0f;
        }
        return this.vertOriginX;
    }

    public float getVertOriginY() {
        if (this.vertOriginY == -1.0f) {
            this.vertOriginY = (float) ((Font) this.parent).getFontFace().getAscent();
        }
        return this.vertOriginY;
    }

    public float getVertAdvY() {
        if (this.vertAdvY == -1.0f) {
            this.vertAdvY = (float) ((Font) this.parent).getFontFace().getUnitsPerEm();
        }
        return this.vertAdvY;
    }

    @Override // com.kitfox.svg.ShapeElement
    public Shape getShape() {
        if (this.path != null) {
            return shapeToParent(this.path);
        }
        return null;
    }

    @Override // com.kitfox.svg.RenderableElement
    public Rectangle2D getBoundingBox() throws SVGException {
        if (this.path != null) {
            return boundsToParent(includeStrokeInBounds(this.path.getBounds2D()));
        }
        return null;
    }

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.TransformableElement
    public boolean updateTime(double curTime) throws SVGException {
        return false;
    }

    public void setPath(Shape path2) {
        this.path = path2;
    }

    public void setHorizAdvX(float horizAdvX2) {
        this.horizAdvX = horizAdvX2;
    }

    public void setVertOriginX(float vertOriginX2) {
        this.vertOriginX = vertOriginX2;
    }

    public void setVertOriginY(float vertOriginY2) {
        this.vertOriginY = vertOriginY2;
    }

    public void setVertAdvY(float vertAdvY2) {
        this.vertAdvY = vertAdvY2;
    }
}
