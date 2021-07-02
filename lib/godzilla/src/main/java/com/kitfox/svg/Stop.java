package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Color;

public class Stop extends SVGElement {
    public static final String TAG_NAME = "stop";
    Color color = Color.black;
    float offset = 0.0f;
    float opacity = 1.0f;

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.SVGElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("offset"))) {
            this.offset = sty.getFloatValue();
            String units = sty.getUnits();
            if (units != null && units.equals("%")) {
                this.offset /= 100.0f;
            }
            if (this.offset > 1.0f) {
                this.offset = 1.0f;
            }
            if (this.offset < 0.0f) {
                this.offset = 0.0f;
            }
        }
        if (getStyle(sty.setName("stop-color"))) {
            this.color = sty.getColorValue();
        }
        if (getStyle(sty.setName("stop-opacity"))) {
            this.opacity = sty.getRatioValue();
        }
    }

    @Override // com.kitfox.svg.SVGElement
    public boolean updateTime(double curTime) throws SVGException {
        Color newVal;
        StyleAttribute sty = new StyleAttribute();
        boolean shapeChange = false;
        if (getPres(sty.setName("offset"))) {
            float newVal2 = sty.getFloatValue();
            if (newVal2 != this.offset) {
                this.offset = newVal2;
                shapeChange = true;
            }
        }
        if (getStyle(sty.setName("stop-color")) && (newVal = sty.getColorValue()) != this.color) {
            this.color = newVal;
            shapeChange = true;
        }
        if (!getStyle(sty.setName("stop-opacity"))) {
            return shapeChange;
        }
        float newVal3 = sty.getFloatValue();
        if (newVal3 == this.opacity) {
            return shapeChange;
        }
        this.opacity = newVal3;
        return true;
    }
}
