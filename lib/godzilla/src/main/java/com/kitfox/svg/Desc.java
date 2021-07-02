package com.kitfox.svg;

public class Desc extends SVGElement {
    public static final String TAG_NAME = "desc";
    StringBuffer text = new StringBuffer();

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    @Override // com.kitfox.svg.SVGElement
    public void loaderAddText(SVGLoaderHelper helper, String text2) {
        this.text.append(text2);
    }

    public String getText() {
        return this.text.toString();
    }

    @Override // com.kitfox.svg.SVGElement
    public boolean updateTime(double curTime) {
        return false;
    }
}
