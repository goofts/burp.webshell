package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

public class Marker extends Group {
    public static final int MARKER_END = 2;
    public static final int MARKER_MID = 1;
    public static final int MARKER_START = 0;
    public static final String TAG_NAME = "marker";
    float markerHeight = 1.0f;
    boolean markerUnitsStrokeWidth = true;
    float markerWidth = 1.0f;
    AffineTransform markerXform;
    float orient = Float.NaN;
    float refX;
    float refY;
    Rectangle2D viewBox;
    AffineTransform viewXform;

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.Group
    public String getTagName() {
        return TAG_NAME;
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.RenderableElement, com.kitfox.svg.SVGElement, com.kitfox.svg.TransformableElement
    public void build() throws SVGException {
        String markerUnits;
        super.build();
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("refX"))) {
            this.refX = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("refY"))) {
            this.refY = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("markerWidth"))) {
            this.markerWidth = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("markerHeight"))) {
            this.markerHeight = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("orient"))) {
            if ("auto".equals(sty.getStringValue())) {
                this.orient = Float.NaN;
            } else {
                this.orient = sty.getFloatValue();
            }
        }
        if (getPres(sty.setName("viewBox"))) {
            float[] dim = sty.getFloatList();
            this.viewBox = new Rectangle2D.Float(dim[0], dim[1], dim[2], dim[3]);
        }
        if (this.viewBox == null) {
            this.viewBox = new Rectangle(0, 0, 1, 1);
        }
        if (getPres(sty.setName("markerUnits")) && (markerUnits = sty.getStringValue()) != null && markerUnits.equals("userSpaceOnUse")) {
            this.markerUnitsStrokeWidth = false;
        }
        this.viewXform = new AffineTransform();
        this.viewXform.scale(1.0d / this.viewBox.getWidth(), 1.0d / this.viewBox.getHeight());
        this.viewXform.translate(-this.viewBox.getX(), -this.viewBox.getY());
        this.markerXform = new AffineTransform();
        this.markerXform.scale((double) this.markerWidth, (double) this.markerHeight);
        this.markerXform.concatenate(this.viewXform);
        this.markerXform.translate((double) (-this.refX), (double) (-this.refY));
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
        g.transform(this.markerXform);
        super.render(g);
        g.setTransform(oldXform);
    }

    public void render(Graphics2D g, MarkerPos pos, float strokeWidth) throws SVGException {
        AffineTransform cacheXform = g.getTransform();
        g.translate(pos.x, pos.y);
        if (this.markerUnitsStrokeWidth) {
            g.scale((double) strokeWidth, (double) strokeWidth);
        }
        g.rotate(Math.atan2(pos.dy, pos.dx));
        g.transform(this.markerXform);
        super.render(g);
        g.setTransform(cacheXform);
    }

    @Override // com.kitfox.svg.ShapeElement, com.kitfox.svg.Group
    public Shape getShape() {
        return this.markerXform.createTransformedShape(super.getShape());
    }

    @Override // com.kitfox.svg.RenderableElement, com.kitfox.svg.Group
    public Rectangle2D getBoundingBox() throws SVGException {
        return this.markerXform.createTransformedShape(super.getBoundingBox()).getBounds2D();
    }

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.Group, com.kitfox.svg.TransformableElement
    public boolean updateTime(double curTime) throws SVGException {
        boolean changeState = super.updateTime(curTime);
        build();
        return changeState;
    }

    public static class MarkerPos {
        double dx;
        double dy;
        int type;
        double x;
        double y;

        public MarkerPos(int type2, double x2, double y2, double dx2, double dy2) {
            this.type = type2;
            this.x = x2;
            this.y = y2;
            this.dx = dx2;
            this.dy = dy2;
        }
    }

    public static class MarkerLayout {
        private ArrayList<MarkerPos> markerList = new ArrayList<>();
        boolean started = false;

        public void layout(Shape shape) {
            double px = 0.0d;
            double py = 0.0d;
            double[] coords = new double[6];
            PathIterator it = shape.getPathIterator((AffineTransform) null);
            while (!it.isDone()) {
                switch (it.currentSegment(coords)) {
                    case 0:
                        px = coords[0];
                        py = coords[1];
                        this.started = false;
                        break;
                    case 1:
                        double x = coords[0];
                        double y = coords[1];
                        markerIn(px, py, x - px, y - py);
                        markerOut(x, y, x - px, y - py);
                        px = x;
                        py = y;
                        break;
                    case 2:
                        double k0x = coords[0];
                        double k0y = coords[1];
                        double x2 = coords[2];
                        double y2 = coords[3];
                        if (px == k0x && py == k0y) {
                            markerIn(px, py, x2 - px, y2 - py);
                        } else {
                            markerIn(px, py, k0x - px, k0y - py);
                        }
                        if (x2 == k0x && y2 == k0y) {
                            markerOut(x2, y2, x2 - px, y2 - py);
                        } else {
                            markerOut(x2, y2, x2 - k0x, y2 - k0y);
                        }
                        markerIn(px, py, k0x - px, k0y - py);
                        markerOut(x2, y2, x2 - k0x, y2 - k0y);
                        px = x2;
                        py = y2;
                        break;
                    case 3:
                        double k0x2 = coords[0];
                        double k0y2 = coords[1];
                        double k1x = coords[2];
                        double k1y = coords[3];
                        double x3 = coords[4];
                        double y3 = coords[5];
                        if (px != k0x2 || py != k0y2) {
                            markerIn(px, py, k0x2 - px, k0y2 - py);
                        } else if (px == k1x && py == k1y) {
                            markerIn(px, py, x3 - px, y3 - py);
                        } else {
                            markerIn(px, py, k1x - px, k1y - py);
                        }
                        if (x3 != k1x || y3 != k1y) {
                            markerOut(x3, y3, x3 - k1x, y3 - k1y);
                        } else if (x3 == k0x2 && y3 == k0y2) {
                            markerOut(x3, y3, x3 - px, y3 - py);
                        } else {
                            markerOut(x3, y3, x3 - k0x2, y3 - k0y2);
                        }
                        px = x3;
                        py = y3;
                        break;
                    case 4:
                        this.started = false;
                        break;
                }
                it.next();
            }
            for (int i = 1; i < this.markerList.size(); i++) {
                MarkerPos prev = this.markerList.get(i - 1);
                if (this.markerList.get(i).type == 0) {
                    prev.type = 2;
                }
            }
            this.markerList.get(this.markerList.size() - 1).type = 2;
        }

        private void markerIn(double x, double y, double dx, double dy) {
            if (!this.started) {
                this.started = true;
                this.markerList.add(new MarkerPos(0, x, y, dx, dy));
            }
        }

        private void markerOut(double x, double y, double dx, double dy) {
            this.markerList.add(new MarkerPos(1, x, y, dx, dy));
        }

        public ArrayList<MarkerPos> getMarkerList() {
            return this.markerList;
        }
    }
}
