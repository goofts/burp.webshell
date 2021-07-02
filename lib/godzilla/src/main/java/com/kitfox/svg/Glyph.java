package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;

public class Glyph extends MissingGlyph {
    public static final String TAG_NAME = "missingglyph";
    String unicode;

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.MissingGlyph
    public String getTagName() {
        return "missingglyph";
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.RenderableElement, com.kitfox.svg.SVGElement, com.kitfox.svg.MissingGlyph, com.kitfox.svg.TransformableElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("unicode"))) {
            this.unicode = sty.getStringValue();
        }
    }

    public String getUnicode() {
        return this.unicode;
    }

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.MissingGlyph, com.kitfox.svg.TransformableElement
    public boolean updateTime(double curTime) throws SVGException {
        return false;
    }
}
