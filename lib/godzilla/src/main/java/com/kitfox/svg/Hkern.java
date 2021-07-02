package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;

public class Hkern extends SVGElement {
    public static final String TAG_NAME = "hkern";
    int k;
    String u1;
    String u2;

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.SVGElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("u1"))) {
            this.u1 = sty.getStringValue();
        }
        if (getPres(sty.setName("u2"))) {
            this.u2 = sty.getStringValue();
        }
        if (getPres(sty.setName("k"))) {
            this.k = sty.getIntValue();
        }
    }

    @Override // com.kitfox.svg.SVGElement
    public boolean updateTime(double curTime) throws SVGException {
        return false;
    }
}
