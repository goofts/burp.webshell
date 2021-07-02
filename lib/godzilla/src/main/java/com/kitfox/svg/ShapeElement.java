package com.kitfox.svg;

import com.formdev.flatlaf.FlatClientProperties;
import com.kitfox.svg.Marker;
import com.kitfox.svg.xml.StyleAttribute;
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public abstract class ShapeElement extends RenderableElement {
    protected float strokeWidthScalar = 1.0f;

    public abstract Shape getShape();

    @Override // com.kitfox.svg.RenderableElement
    public abstract void render(Graphics2D graphics2D) throws SVGException;

    /* access modifiers changed from: package-private */
    @Override // com.kitfox.svg.RenderableElement
    public void pick(Point2D point, boolean boundingBox, List<List<SVGElement>> retVec) throws SVGException {
        if ((boundingBox ? getBoundingBox() : getShape()).contains(point)) {
            retVec.add(getPath(null));
        }
    }

    /* access modifiers changed from: package-private */
    @Override // com.kitfox.svg.RenderableElement
    public void pick(Rectangle2D pickArea, AffineTransform ltw, boolean boundingBox, List<List<SVGElement>> retVec) throws SVGException {
        if (ltw.createTransformedShape(boundingBox ? getBoundingBox() : getShape()).intersects(pickArea)) {
            retVec.add(getPath(null));
        }
    }

    private Paint handleCurrentColor(StyleAttribute styleAttrib) throws SVGException {
        if (!styleAttrib.getStringValue().equals("currentColor")) {
            return styleAttrib.getColorValue();
        }
        StyleAttribute currentColorAttrib = new StyleAttribute();
        if (!getStyle(currentColorAttrib.setName("color")) || currentColorAttrib.getStringValue().equals("none")) {
            return null;
        }
        return currentColorAttrib.getColorValue();
    }

    /* access modifiers changed from: protected */
    public void renderShape(Graphics2D g, Shape shape) throws SVGException {
        BasicStroke stroke;
        Shape strokeShape;
        URI uri;
        URI uri2;
        StyleAttribute styleAttrib = new StyleAttribute();
        if (getStyle(styleAttrib.setName("visibility")) && !styleAttrib.getStringValue().equals("visible")) {
            return;
        }
        if (!getStyle(styleAttrib.setName("display")) || !styleAttrib.getStringValue().equals("none")) {
            Paint fillPaint = Color.black;
            if (getStyle(styleAttrib.setName(FlatClientProperties.TABBED_PANE_ALIGN_FILL))) {
                if (styleAttrib.getStringValue().equals("none")) {
                    fillPaint = null;
                } else {
                    fillPaint = handleCurrentColor(styleAttrib);
                    if (fillPaint == null && (uri2 = styleAttrib.getURIValue(getXMLBase())) != null) {
                        Rectangle2D bounds = shape.getBounds2D();
                        AffineTransform xform = g.getTransform();
                        SVGElement ele = this.diagram.getUniverse().getElement(uri2);
                        if (ele != null) {
                            try {
                                fillPaint = ((FillElement) ele).getPaint(bounds, xform);
                            } catch (IllegalArgumentException e) {
                                throw new SVGException(e);
                            }
                        }
                    }
                }
            }
            float opacity = 1.0f;
            if (getStyle(styleAttrib.setName("opacity"))) {
                opacity = styleAttrib.getRatioValue();
            }
            float fillOpacity = opacity;
            if (getStyle(styleAttrib.setName("fill-opacity"))) {
                fillOpacity *= styleAttrib.getRatioValue();
            }
            Paint strokePaint = null;
            if (getStyle(styleAttrib.setName("stroke"))) {
                if (styleAttrib.getStringValue().equals("none")) {
                    strokePaint = null;
                } else {
                    strokePaint = handleCurrentColor(styleAttrib);
                    if (strokePaint == null && (uri = styleAttrib.getURIValue(getXMLBase())) != null) {
                        Rectangle2D bounds2 = shape.getBounds2D();
                        AffineTransform xform2 = g.getTransform();
                        SVGElement ele2 = this.diagram.getUniverse().getElement(uri);
                        if (ele2 != null) {
                            strokePaint = ((FillElement) ele2).getPaint(bounds2, xform2);
                        }
                    }
                }
            }
            float[] strokeDashArray = null;
            if (getStyle(styleAttrib.setName("stroke-dasharray"))) {
                strokeDashArray = styleAttrib.getFloatList();
                if (strokeDashArray.length == 0) {
                    strokeDashArray = null;
                }
            }
            float strokeDashOffset = 0.0f;
            if (getStyle(styleAttrib.setName("stroke-dashoffset"))) {
                strokeDashOffset = styleAttrib.getFloatValueWithUnits();
            }
            int strokeLinecap = 0;
            if (getStyle(styleAttrib.setName("stroke-linecap"))) {
                String val = styleAttrib.getStringValue();
                if (val.equals("round")) {
                    strokeLinecap = 1;
                } else if (val.equals(FlatClientProperties.BUTTON_TYPE_SQUARE)) {
                    strokeLinecap = 2;
                }
            }
            int strokeLinejoin = 0;
            if (getStyle(styleAttrib.setName("stroke-linejoin"))) {
                String val2 = styleAttrib.getStringValue();
                if (val2.equals("round")) {
                    strokeLinejoin = 1;
                } else if (val2.equals("bevel")) {
                    strokeLinejoin = 2;
                }
            }
            float strokeMiterLimit = 4.0f;
            if (getStyle(styleAttrib.setName("stroke-miterlimit"))) {
                strokeMiterLimit = Math.max(styleAttrib.getFloatValueWithUnits(), 1.0f);
            }
            float strokeOpacity = opacity;
            if (getStyle(styleAttrib.setName("stroke-opacity"))) {
                strokeOpacity *= styleAttrib.getRatioValue();
            }
            float strokeWidth = 1.0f;
            if (getStyle(styleAttrib.setName("stroke-width"))) {
                strokeWidth = styleAttrib.getFloatValueWithUnits();
            }
            float strokeWidth2 = strokeWidth * this.strokeWidthScalar;
            Marker markerStart = null;
            if (getStyle(styleAttrib.setName("marker-start")) && !styleAttrib.getStringValue().equals("none")) {
                markerStart = (Marker) this.diagram.getUniverse().getElement(styleAttrib.getURIValue(getXMLBase()));
            }
            Marker markerMid = null;
            if (getStyle(styleAttrib.setName("marker-mid")) && !styleAttrib.getStringValue().equals("none")) {
                markerMid = (Marker) this.diagram.getUniverse().getElement(styleAttrib.getURIValue(getXMLBase()));
            }
            Marker markerEnd = null;
            if (getStyle(styleAttrib.setName("marker-end")) && !styleAttrib.getStringValue().equals("none")) {
                markerEnd = (Marker) this.diagram.getUniverse().getElement(styleAttrib.getURIValue(getXMLBase()));
            }
            if (!(fillPaint == null || fillOpacity == 0.0f || fillOpacity <= 0.0f)) {
                if (fillOpacity < 1.0f) {
                    Composite cachedComposite = g.getComposite();
                    g.setComposite(AlphaComposite.getInstance(3, fillOpacity));
                    g.setPaint(fillPaint);
                    g.fill(shape);
                    g.setComposite(cachedComposite);
                } else {
                    g.setPaint(fillPaint);
                    g.fill(shape);
                }
            }
            if (!(strokePaint == null || strokeOpacity == 0.0f)) {
                if (strokeDashArray == null) {
                    stroke = new BasicStroke(strokeWidth2, strokeLinecap, strokeLinejoin, strokeMiterLimit);
                } else {
                    stroke = new BasicStroke(strokeWidth2, strokeLinecap, strokeLinejoin, strokeMiterLimit, strokeDashArray, strokeDashOffset);
                }
                AffineTransform cacheXform = g.getTransform();
                if (this.vectorEffect == 1) {
                    strokeShape = stroke.createStrokedShape(cacheXform.createTransformedShape(shape));
                } else {
                    strokeShape = stroke.createStrokedShape(shape);
                }
                if (strokeOpacity > 0.0f) {
                    Composite cachedComposite2 = g.getComposite();
                    if (strokeOpacity < 1.0f) {
                        g.setComposite(AlphaComposite.getInstance(3, strokeOpacity));
                    }
                    if (this.vectorEffect == 1) {
                        g.setTransform(new AffineTransform());
                    }
                    g.setPaint(strokePaint);
                    g.fill(strokeShape);
                    if (this.vectorEffect == 1) {
                        g.setTransform(cacheXform);
                    }
                    if (strokeOpacity < 1.0f) {
                        g.setComposite(cachedComposite2);
                    }
                }
            }
            if (!(markerStart == null && markerMid == null && markerEnd == null)) {
                Marker.MarkerLayout layout = new Marker.MarkerLayout();
                layout.layout(shape);
                ArrayList<Marker.MarkerPos> list = layout.getMarkerList();
                for (int i = 0; i < list.size(); i++) {
                    Marker.MarkerPos pos = list.get(i);
                    switch (pos.type) {
                        case 0:
                            if (markerStart != null) {
                                markerStart.render(g, pos, strokeWidth2);
                                break;
                            } else {
                                break;
                            }
                        case 1:
                            if (markerMid != null) {
                                markerMid.render(g, pos, strokeWidth2);
                                break;
                            } else {
                                break;
                            }
                        case 2:
                            if (markerEnd != null) {
                                markerEnd.render(g, pos, strokeWidth2);
                                break;
                            } else {
                                break;
                            }
                    }
                }
            }
        }
    }

    /* access modifiers changed from: protected */
    public Rectangle2D includeStrokeInBounds(Rectangle2D rect) throws SVGException {
        StyleAttribute styleAttrib = new StyleAttribute();
        if (getStyle(styleAttrib.setName("stroke"))) {
            double strokeWidth = 1.0d;
            if (getStyle(styleAttrib.setName("stroke-width"))) {
                strokeWidth = styleAttrib.getDoubleValue();
            }
            rect.setRect(rect.getX() - (strokeWidth / 2.0d), rect.getY() - (strokeWidth / 2.0d), rect.getWidth() + strokeWidth, rect.getHeight() + strokeWidth);
        }
        return rect;
    }
}
