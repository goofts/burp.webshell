package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;

public class FePointLight extends FeLight {
    public static final String TAG_NAME = "fepointlight";
    float x = 0.0f;
    float y = 0.0f;
    float z = 0.0f;

    @Override // com.kitfox.svg.FilterEffects, com.kitfox.svg.SVGElement, com.kitfox.svg.FeLight
    public String getTagName() {
        return TAG_NAME;
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.FilterEffects, com.kitfox.svg.SVGElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("x"))) {
            this.x = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("y"))) {
            this.y = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("z"))) {
            this.z = sty.getFloatValueWithUnits();
        }
    }

    @Override // com.kitfox.svg.FilterEffects
    public float getX() {
        return this.x;
    }

    @Override // com.kitfox.svg.FilterEffects
    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    @Override // com.kitfox.svg.FilterEffects, com.kitfox.svg.SVGElement
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
        if (!getPres(sty.setName("z"))) {
            return stateChange;
        }
        float newVal3 = sty.getFloatValueWithUnits();
        if (newVal3 == this.z) {
            return stateChange;
        }
        this.z = newVal3;
        return true;
    }
}
