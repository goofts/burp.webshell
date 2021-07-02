package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Color;
import java.awt.MultipleGradientPaint;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class RadialGradient extends Gradient {
    public static final String TAG_NAME = "radialgradient";
    float cx = 0.5f;
    float cy = 0.5f;
    float fx = 0.0f;
    float fy = 0.0f;
    boolean hasFocus = false;
    float r = 0.5f;

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.Gradient
    public String getTagName() {
        return TAG_NAME;
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.Gradient
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("cx"))) {
            this.cx = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("cy"))) {
            this.cy = sty.getFloatValueWithUnits();
        }
        this.hasFocus = false;
        if (getPres(sty.setName("fx"))) {
            this.fx = sty.getFloatValueWithUnits();
            this.hasFocus = true;
        }
        if (getPres(sty.setName("fy"))) {
            this.fy = sty.getFloatValueWithUnits();
            this.hasFocus = true;
        }
        if (getPres(sty.setName("r"))) {
            this.r = sty.getFloatValueWithUnits();
        }
    }

    @Override // com.kitfox.svg.FillElement
    public Paint getPaint(Rectangle2D bounds, AffineTransform xform) {
        MultipleGradientPaint.CycleMethod method;
        Point2D.Float pt2;
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
        Point2D.Float pt1 = new Point2D.Float(this.cx, this.cy);
        if (this.hasFocus) {
            pt2 = new Point2D.Float(this.fx, this.fy);
        } else {
            pt2 = pt1;
        }
        float[] stopFractions = getStopFractions();
        Color[] stopColors = getStopColors();
        if (this.gradientUnits == 1) {
            return new RadialGradientPaint(pt1, this.r, pt2, stopFractions, stopColors, method, MultipleGradientPaint.ColorSpaceType.SRGB, this.gradientTransform);
        }
        AffineTransform viewXform = new AffineTransform();
        viewXform.translate(bounds.getX(), bounds.getY());
        viewXform.scale(bounds.getWidth(), bounds.getHeight());
        viewXform.concatenate(this.gradientTransform);
        return new RadialGradientPaint(pt1, this.r, pt2, stopFractions, stopColors, method, MultipleGradientPaint.ColorSpaceType.SRGB, viewXform);
    }

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.Gradient
    public boolean updateTime(double curTime) throws SVGException {
        boolean changeState = super.updateTime(curTime);
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("cx"))) {
            float newVal = sty.getFloatValueWithUnits();
            if (newVal != this.cx) {
                this.cx = newVal;
            }
        }
        if (getPres(sty.setName("cy"))) {
            float newVal2 = sty.getFloatValueWithUnits();
            if (newVal2 != this.cy) {
                this.cy = newVal2;
            }
        }
        if (getPres(sty.setName("fx"))) {
            float newVal3 = sty.getFloatValueWithUnits();
            if (newVal3 != this.fx) {
                this.fx = newVal3;
            }
        }
        if (getPres(sty.setName("fy"))) {
            float newVal4 = sty.getFloatValueWithUnits();
            if (newVal4 != this.fy) {
                this.fy = newVal4;
            }
        }
        if (getPres(sty.setName("r"))) {
            float newVal5 = sty.getFloatValueWithUnits();
            if (newVal5 != this.r) {
                this.r = newVal5;
            }
        }
        return changeState;
    }
}
