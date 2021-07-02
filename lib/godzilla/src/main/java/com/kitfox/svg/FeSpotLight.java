package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;

public class FeSpotLight extends FeLight {
    public static final String TAG_NAME = "fespotlight";
    float limitingConeAngle = 0.0f;
    float pointsAtX = 0.0f;
    float pointsAtY = 0.0f;
    float pointsAtZ = 0.0f;
    float specularComponent = 0.0f;
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
        if (getPres(sty.setName("pointsAtX"))) {
            this.pointsAtX = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("pointsAtY"))) {
            this.pointsAtY = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("pointsAtZ"))) {
            this.pointsAtZ = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("specularComponent"))) {
            this.specularComponent = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("limitingConeAngle"))) {
            this.limitingConeAngle = sty.getFloatValueWithUnits();
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

    public float getPointsAtX() {
        return this.pointsAtX;
    }

    public float getPointsAtY() {
        return this.pointsAtY;
    }

    public float getPointsAtZ() {
        return this.pointsAtZ;
    }

    public float getSpecularComponent() {
        return this.specularComponent;
    }

    public float getLimitingConeAngle() {
        return this.limitingConeAngle;
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
        if (getPres(sty.setName("z"))) {
            float newVal3 = sty.getFloatValueWithUnits();
            if (newVal3 != this.z) {
                this.z = newVal3;
                stateChange = true;
            }
        }
        if (getPres(sty.setName("pointsAtX"))) {
            float newVal4 = sty.getFloatValueWithUnits();
            if (newVal4 != this.pointsAtX) {
                this.pointsAtX = newVal4;
                stateChange = true;
            }
        }
        if (getPres(sty.setName("pointsAtY"))) {
            float newVal5 = sty.getFloatValueWithUnits();
            if (newVal5 != this.pointsAtY) {
                this.pointsAtY = newVal5;
                stateChange = true;
            }
        }
        if (getPres(sty.setName("pointsAtZ"))) {
            float newVal6 = sty.getFloatValueWithUnits();
            if (newVal6 != this.pointsAtZ) {
                this.pointsAtZ = newVal6;
                stateChange = true;
            }
        }
        if (getPres(sty.setName("specularComponent"))) {
            float newVal7 = sty.getFloatValueWithUnits();
            if (newVal7 != this.specularComponent) {
                this.specularComponent = newVal7;
                stateChange = true;
            }
        }
        if (!getPres(sty.setName("limitingConeAngle"))) {
            return stateChange;
        }
        float newVal8 = sty.getFloatValueWithUnits();
        if (newVal8 == this.limitingConeAngle) {
            return stateChange;
        }
        this.limitingConeAngle = newVal8;
        return true;
    }
}
