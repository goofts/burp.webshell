package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;
import java.net.URI;

public class A extends Group {
    public static final String TAG_NAME = "a";
    URI href;
    String title;

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.Group
    public String getTagName() {
        return TAG_NAME;
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.RenderableElement, com.kitfox.svg.SVGElement, com.kitfox.svg.TransformableElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("xlink:href"))) {
            this.href = sty.getURIValue(getXMLBase());
        }
        if (getPres(sty.setName("xlink:title"))) {
            this.title = sty.getStringValue();
        }
    }

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.Group, com.kitfox.svg.TransformableElement
    public boolean updateTime(double curTime) throws SVGException {
        boolean changeState = super.updateTime(curTime);
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("xlink:href"))) {
            this.href = sty.getURIValue(getXMLBase());
        }
        if (getPres(sty.setName("xlink:title"))) {
            this.title = sty.getStringValue();
        }
        return changeState;
    }
}
