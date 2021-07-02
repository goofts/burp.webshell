package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;

public class FeDistantLight extends FeLight {
    public static final String TAG_NAME = "fedistantlight";
    float azimuth = 0.0f;
    float elevation = 0.0f;

    @Override // com.kitfox.svg.FilterEffects, com.kitfox.svg.SVGElement, com.kitfox.svg.FeLight
    public String getTagName() {
        return TAG_NAME;
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.FilterEffects, com.kitfox.svg.SVGElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("azimuth"))) {
            this.azimuth = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("elevation"))) {
            this.elevation = sty.getFloatValueWithUnits();
        }
    }

    public float getAzimuth() {
        return this.azimuth;
    }

    public float getElevation() {
        return this.elevation;
    }

    @Override // com.kitfox.svg.FilterEffects, com.kitfox.svg.SVGElement
    public boolean updateTime(double curTime) throws SVGException {
        StyleAttribute sty = new StyleAttribute();
        boolean stateChange = false;
        if (getPres(sty.setName("azimuth"))) {
            float newVal = sty.getFloatValueWithUnits();
            if (newVal != this.azimuth) {
                this.azimuth = newVal;
                stateChange = true;
            }
        }
        if (!getPres(sty.setName("elevation"))) {
            return stateChange;
        }
        float newVal2 = sty.getFloatValueWithUnits();
        if (newVal2 == this.elevation) {
            return stateChange;
        }
        this.elevation = newVal2;
        return true;
    }
}
