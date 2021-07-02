package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;
import java.net.URL;

public class FilterEffects extends SVGElement {
    public static final int FP_BACKGROUND_ALPHA = 3;
    public static final int FP_BACKGROUND_IMAGE = 2;
    public static final int FP_CUSTOM = 5;
    public static final int FP_FILL_PAINT = 4;
    public static final int FP_SOURCE_ALPHA = 1;
    public static final int FP_SOURCE_GRAPHIC = 0;
    public static final int FP_STROKE_PAINT = 5;
    public static final String TAG_NAME = "filtereffects";
    private String filterPrimitiveRefIn;
    private int filterPrimitiveTypeIn;
    float height = 1.0f;
    URL href = null;
    String result = "defaultFilterName";
    float width = 1.0f;
    float x = 0.0f;
    float y = 0.0f;

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    @Override // com.kitfox.svg.SVGElement
    public void loaderAddChild(SVGLoaderHelper helper, SVGElement child) throws SVGElementException {
        super.loaderAddChild(helper, child);
        if (child instanceof FilterEffects) {
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.SVGElement
    public void build() throws SVGException {
        super.build();
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    @Override // com.kitfox.svg.SVGElement
    public boolean updateTime(double curTime) throws SVGException {
        StyleAttribute sty = new StyleAttribute();
        boolean stateChange = false;
        if (getPres(sty.setName("x"))) {
            float newVal = sty.getFloatValueWithUnits();
            if (newVal != this.x) {
                this.x = newVal;
                stateChange = true;
            }
        }
        if (getPres(sty.setName("y"))) {
            float newVal2 = sty.getFloatValueWithUnits();
            if (newVal2 != this.y) {
                this.y = newVal2;
                stateChange = true;
            }
        }
        if (getPres(sty.setName("width"))) {
            float newVal3 = sty.getFloatValueWithUnits();
            if (newVal3 != this.width) {
                this.width = newVal3;
                stateChange = true;
            }
        }
        if (getPres(sty.setName("height"))) {
            float newVal4 = sty.getFloatValueWithUnits();
            if (newVal4 != this.height) {
                this.height = newVal4;
                stateChange = true;
            }
        }
        try {
            if (!getPres(sty.setName("xlink:href"))) {
                return stateChange;
            }
            URL newVal5 = sty.getURIValue(getXMLBase()).toURL();
            if (newVal5.equals(this.href)) {
                return stateChange;
            }
            this.href = newVal5;
            return true;
        } catch (Exception e) {
            throw new SVGException(e);
        }
    }
}
