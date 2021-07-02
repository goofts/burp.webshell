package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Shape;
import java.awt.geom.Area;
import java.util.Iterator;

public class ClipPath extends SVGElement {
    public static final int CP_OBJECT_BOUNDING_BOX = 1;
    public static final int CP_USER_SPACE_ON_USE = 0;
    public static final String TAG_NAME = "clippath";
    int clipPathUnits = 0;

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    @Override // com.kitfox.svg.SVGElement
    public void loaderAddChild(SVGLoaderHelper helper, SVGElement child) throws SVGElementException {
        super.loaderAddChild(helper, child);
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.SVGElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        this.clipPathUnits = (!getPres(sty.setName("clipPathUnits")) || !sty.getStringValue().equals("objectBoundingBox")) ? 0 : 1;
    }

    public int getClipPathUnits() {
        return this.clipPathUnits;
    }

    public Shape getClipPathShape() {
        if (this.children.isEmpty()) {
            return null;
        }
        if (this.children.size() == 1) {
            return ((ShapeElement) this.children.get(0)).getShape();
        }
        Area clipArea = null;
        Iterator it = this.children.iterator();
        while (it.hasNext()) {
            ShapeElement se = (ShapeElement) ((SVGElement) it.next());
            if (clipArea != null) {
                Shape shape = se.getShape();
                if (shape != null) {
                    clipArea.intersect(new Area(shape));
                }
            } else if (se.getShape() != null) {
                clipArea = new Area(se.getShape());
            }
        }
        return clipArea;
    }

    @Override // com.kitfox.svg.SVGElement
    public boolean updateTime(double curTime) throws SVGException {
        StyleAttribute sty = new StyleAttribute();
        boolean shapeChange = false;
        if (getPres(sty.setName("clipPathUnits"))) {
            int newUnits = sty.getStringValue().equals("objectBoundingBox") ? 1 : 0;
            if (newUnits != this.clipPathUnits) {
                this.clipPathUnits = newUnits;
                shapeChange = true;
            }
        }
        if (shapeChange) {
            build();
        }
        for (int i = 0; i < this.children.size(); i++) {
            ((SVGElement) this.children.get(i)).updateTime(curTime);
        }
        return shapeChange;
    }
}
