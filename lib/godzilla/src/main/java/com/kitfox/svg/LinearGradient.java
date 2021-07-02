package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Color;
import java.awt.LinearGradientPaint;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class LinearGradient extends Gradient {
    public static final String TAG_NAME = "lineargradient";
    float x1 = 0.0f;
    float x2 = 1.0f;
    float y1 = 0.0f;
    float y2 = 0.0f;

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.Gradient
    public String getTagName() {
        return TAG_NAME;
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.Gradient
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("x1"))) {
            this.x1 = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("y1"))) {
            this.y1 = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("x2"))) {
            this.x2 = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("y2"))) {
            this.y2 = sty.getFloatValueWithUnits();
        }
    }

    @Override // com.kitfox.svg.FillElement
    public Paint getPaint(Rectangle2D bounds, AffineTransform xform) {
        MultipleGradientPaint.CycleMethod method;
        switch (this.spreadMethod) {
            case 1:
                method = MultipleGradientPaint.CycleMethod.REPEAT;
                break;
            case 2:
                method = MultipleGradientPaint.CycleMethod.REFLECT;
                break;
            default:
                method = MultipleGradientPaint.CycleMethod.NO_CYCLE;
                break;
        }
        Point2D.Float pt1 = new Point2D.Float(this.x1, this.y1);
        Point2D.Float pt2 = new Point2D.Float(this.x2, this.y2);
        if (pt1.equals(pt2)) {
            Paint[] colors = getStopColors();
            if (colors.length > 0) {
                return colors[0];
            }
            return Color.black;
        } else if (this.gradientUnits == 1) {
            return new LinearGradientPaint(pt1, pt2, getStopFractions(), getStopColors(), method, MultipleGradientPaint.ColorSpaceType.SRGB, this.gradientTransform == null ? new AffineTransform() : this.gradientTransform);
        } else {
            AffineTransform viewXform = new AffineTransform();
            viewXform.translate(bounds.getX(), bounds.getY());
            viewXform.scale(Math.max(1.0d, bounds.getWidth()), Math.max(1.0d, bounds.getHeight()));
            if (this.gradientTransform != null) {
                viewXform.concatenate(this.gradientTransform);
            }
            return new LinearGradientPaint(pt1, pt2, getStopFractions(), getStopColors(), method, MultipleGradientPaint.ColorSpaceType.SRGB, viewXform);
        }
    }

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.Gradient
    public boolean updateTime(double curTime) throws SVGException {
        boolean changeState = super.updateTime(curTime);
        StyleAttribute sty = new StyleAttribute();
        boolean shapeChange = false;
        if (getPres(sty.setName("x1"))) {
            float newVal = sty.getFloatValueWithUnits();
            if (newVal != this.x1) {
                this.x1 = newVal;
                shapeChange = true;
            }
        }
        if (getPres(sty.setName("y1"))) {
            float newVal2 = sty.getFloatValueWithUnits();
            if (newVal2 != this.y1) {
                this.y1 = newVal2;
                shapeChange = true;
            }
        }
        if (getPres(sty.setName("x2"))) {
            float newVal3 = sty.getFloatValueWithUnits();
            if (newVal3 != this.x2) {
                this.x2 = newVal3;
                shapeChange = true;
            }
        }
        if (getPres(sty.setName("y2"))) {
            float newVal4 = sty.getFloatValueWithUnits();
            if (newVal4 != this.y2) {
                this.y2 = newVal4;
                shapeChange = true;
            }
        }
        return changeState || shapeChange;
    }
}
