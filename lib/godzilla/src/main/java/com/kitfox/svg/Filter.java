package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;
import java.awt.geom.Point2D;
import java.net.URL;
import java.util.ArrayList;

public class Filter extends SVGElement {
    public static final int FU_OBJECT_BOUNDING_BOX = 0;
    public static final int FU_USER_SPACE_ON_USE = 1;
    public static final int PU_OBJECT_BOUNDING_BOX = 0;
    public static final int PU_USER_SPACE_ON_USE = 1;
    public static final String TAG_NAME = "filter";
    final ArrayList<SVGElement> filterEffects = new ArrayList<>();
    Point2D filterRes = new Point2D.Double();
    protected int filterUnits = 0;
    float height = 1.0f;
    URL href = null;
    protected int primitiveUnits = 0;
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
            this.filterEffects.add(child);
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.SVGElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("filterUnits"))) {
            if (sty.getStringValue().toLowerCase().equals("userspaceonuse")) {
                this.filterUnits = 1;
            } else {
                this.filterUnits = 0;
            }
        }
        if (getPres(sty.setName("primitiveUnits"))) {
            if (sty.getStringValue().toLowerCase().equals("userspaceonuse")) {
                this.primitiveUnits = 1;
            } else {
                this.primitiveUnits = 0;
            }
        }
        if (getPres(sty.setName("x"))) {
            this.x = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("y"))) {
            this.y = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("width"))) {
            this.width = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("height"))) {
            this.height = sty.getFloatValueWithUnits();
        }
        try {
            if (getPres(sty.setName("xlink:href"))) {
                this.href = sty.getURIValue(getXMLBase()).toURL();
            }
        } catch (Exception e) {
            throw new SVGException(e);
        }
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
        int newVal;
        int newVal2;
        StyleAttribute sty = new StyleAttribute();
        boolean stateChange = false;
        if (getPres(sty.setName("x"))) {
            float newVal3 = sty.getFloatValueWithUnits();
            if (newVal3 != this.x) {
                this.x = newVal3;
                stateChange = true;
            }
        }
        if (getPres(sty.setName("y"))) {
            float newVal4 = sty.getFloatValueWithUnits();
            if (newVal4 != this.y) {
                this.y = newVal4;
                stateChange = true;
            }
        }
        if (getPres(sty.setName("width"))) {
            float newVal5 = sty.getFloatValueWithUnits();
            if (newVal5 != this.width) {
                this.width = newVal5;
                stateChange = true;
            }
        }
        if (getPres(sty.setName("height"))) {
            float newVal6 = sty.getFloatValueWithUnits();
            if (newVal6 != this.height) {
                this.height = newVal6;
                stateChange = true;
            }
        }
        try {
            if (getPres(sty.setName("xlink:href"))) {
                URL newVal7 = sty.getURIValue(getXMLBase()).toURL();
                if (!newVal7.equals(this.href)) {
                    this.href = newVal7;
                    stateChange = true;
                }
            }
            if (getPres(sty.setName("filterUnits"))) {
                if (sty.getStringValue().toLowerCase().equals("userspaceonuse")) {
                    newVal2 = 1;
                } else {
                    newVal2 = 0;
                }
                if (newVal2 != this.filterUnits) {
                    this.filterUnits = newVal2;
                    stateChange = true;
                }
            }
            if (!getPres(sty.setName("primitiveUnits"))) {
                return stateChange;
            }
            if (sty.getStringValue().toLowerCase().equals("userspaceonuse")) {
                newVal = 1;
            } else {
                newVal = 0;
            }
            if (newVal == this.filterUnits) {
                return stateChange;
            }
            this.primitiveUnits = newVal;
            return true;
        } catch (Exception e) {
            throw new SVGException(e);
        }
    }
}
