package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;
import com.kitfox.svg.xml.StyleSheet;

public class Style extends SVGElement {
    public static final String TAG_NAME = "style";
    StyleSheet styleSheet;
    StringBuffer text = new StringBuffer();
    String type;

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    @Override // com.kitfox.svg.SVGElement
    public void loaderAddText(SVGLoaderHelper helper, String text2) {
        this.text.append(text2);
        this.styleSheet = null;
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.SVGElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("type"))) {
            this.type = sty.getStringValue();
        }
    }

    @Override // com.kitfox.svg.SVGElement
    public boolean updateTime(double curTime) throws SVGException {
        return false;
    }

    public StyleSheet getStyleSheet() {
        if (this.styleSheet == null && this.text.length() > 0) {
            this.styleSheet = StyleSheet.parseSheet(this.text.toString());
        }
        return this.styleSheet;
    }
}
