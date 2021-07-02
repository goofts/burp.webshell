package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;

public class FontFace extends SVGElement {
    public static final String TAG_NAME = "fontface";
    private int accentHeight = -1;
    private int ascent = -1;
    private int descent = -1;
    String fontFamily;
    private int overlinePosition = -1;
    private int overlineThickness = -1;
    private int strikethroughPosition = -1;
    private int strikethroughThickness = -1;
    private int underlinePosition = -1;
    private int underlineThickness = -1;
    private int unitsPerEm = 1000;

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.SVGElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("font-family"))) {
            this.fontFamily = sty.getStringValue();
        }
        if (getPres(sty.setName("units-per-em"))) {
            this.unitsPerEm = sty.getIntValue();
        }
        if (getPres(sty.setName("ascent"))) {
            this.ascent = sty.getIntValue();
        }
        if (getPres(sty.setName("descent"))) {
            this.descent = sty.getIntValue();
        }
        if (getPres(sty.setName("accent-height"))) {
            this.accentHeight = sty.getIntValue();
        }
        if (getPres(sty.setName("underline-position"))) {
            this.underlinePosition = sty.getIntValue();
        }
        if (getPres(sty.setName("underline-thickness"))) {
            this.underlineThickness = sty.getIntValue();
        }
        if (getPres(sty.setName("strikethrough-position"))) {
            this.strikethroughPosition = sty.getIntValue();
        }
        if (getPres(sty.setName("strikethrough-thickenss"))) {
            this.strikethroughThickness = sty.getIntValue();
        }
        if (getPres(sty.setName("overline-position"))) {
            this.overlinePosition = sty.getIntValue();
        }
        if (getPres(sty.setName("overline-thickness"))) {
            this.overlineThickness = sty.getIntValue();
        }
    }

    public String getFontFamily() {
        return this.fontFamily;
    }

    public int getUnitsPerEm() {
        return this.unitsPerEm;
    }

    public int getAscent() {
        if (this.ascent == -1) {
            this.ascent = this.unitsPerEm - ((Font) this.parent).getVertOriginY();
        }
        return this.ascent;
    }

    public int getDescent() {
        if (this.descent == -1) {
            this.descent = ((Font) this.parent).getVertOriginY();
        }
        return this.descent;
    }

    public int getAccentHeight() {
        if (this.accentHeight == -1) {
            this.accentHeight = getAscent();
        }
        return this.accentHeight;
    }

    public int getUnderlinePosition() {
        if (this.underlinePosition == -1) {
            this.underlinePosition = (this.unitsPerEm * 5) / 6;
        }
        return this.underlinePosition;
    }

    public int getUnderlineThickness() {
        if (this.underlineThickness == -1) {
            this.underlineThickness = this.unitsPerEm / 20;
        }
        return this.underlineThickness;
    }

    public int getStrikethroughPosition() {
        if (this.strikethroughPosition == -1) {
            this.strikethroughPosition = (this.unitsPerEm * 3) / 6;
        }
        return this.strikethroughPosition;
    }

    public int getStrikethroughThickness() {
        if (this.strikethroughThickness == -1) {
            this.strikethroughThickness = this.unitsPerEm / 20;
        }
        return this.strikethroughThickness;
    }

    public int getOverlinePosition() {
        if (this.overlinePosition == -1) {
            this.overlinePosition = (this.unitsPerEm * 5) / 6;
        }
        return this.overlinePosition;
    }

    public int getOverlineThickness() {
        if (this.overlineThickness == -1) {
            this.overlineThickness = this.unitsPerEm / 20;
        }
        return this.overlineThickness;
    }

    @Override // com.kitfox.svg.SVGElement
    public boolean updateTime(double curTime) {
        return false;
    }

    public void setUnitsPerEm(int unitsPerEm2) {
        this.unitsPerEm = unitsPerEm2;
    }

    public void setAscent(int ascent2) {
        this.ascent = ascent2;
    }

    public void setDescent(int descent2) {
        this.descent = descent2;
    }

    public void setAccentHeight(int accentHeight2) {
        this.accentHeight = accentHeight2;
    }

    public void setUnderlinePosition(int underlinePosition2) {
        this.underlinePosition = underlinePosition2;
    }

    public void setUnderlineThickness(int underlineThickness2) {
        this.underlineThickness = underlineThickness2;
    }

    public void setStrikethroughPosition(int strikethroughPosition2) {
        this.strikethroughPosition = strikethroughPosition2;
    }

    public void setStrikethroughThickness(int strikethroughThickness2) {
        this.strikethroughThickness = strikethroughThickness2;
    }

    public void setOverlinePosition(int overlinePosition2) {
        this.overlinePosition = overlinePosition2;
    }

    public void setOverlineThickness(int overlineThickness2) {
        this.overlineThickness = overlineThickness2;
    }
}
