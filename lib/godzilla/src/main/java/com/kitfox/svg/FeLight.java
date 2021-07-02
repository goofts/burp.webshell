package com.kitfox.svg;

public abstract class FeLight extends FilterEffects {
    public static final String TAG_NAME = "feLight";

    @Override // com.kitfox.svg.FilterEffects, com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }
}
