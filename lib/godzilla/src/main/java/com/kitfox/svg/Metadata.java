package com.kitfox.svg;

public class Metadata extends SVGElement {
    public static final String TAG_NAME = "metadata";

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    @Override // com.kitfox.svg.SVGElement
    public boolean updateTime(double curTime) {
        return false;
    }
}
