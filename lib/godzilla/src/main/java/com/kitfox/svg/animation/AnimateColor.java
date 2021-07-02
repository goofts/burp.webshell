package com.kitfox.svg.animation;

import com.kitfox.svg.SVGElement;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGLoaderHelper;
import com.kitfox.svg.animation.parser.AnimTimeParser;
import com.kitfox.svg.xml.ColorTable;
import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Color;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class AnimateColor extends AnimateBase implements AnimateColorIface {
    public static final String TAG_NAME = "animateColor";
    private Color fromValue;
    private Color toValue;

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    @Override // com.kitfox.svg.animation.AnimationElement, com.kitfox.svg.SVGElement, com.kitfox.svg.animation.AnimateBase
    public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent) throws SAXException {
        super.loaderStartElement(helper, attrs, parent);
        this.fromValue = ColorTable.parseColor(attrs.getValue("from"));
        this.toValue = ColorTable.parseColor(attrs.getValue("to"));
    }

    @Override // com.kitfox.svg.animation.AnimateColorIface
    public Color evalColor(double interp) {
        int r1 = this.fromValue.getRed();
        int g1 = this.fromValue.getGreen();
        int b1 = this.fromValue.getBlue();
        double invInterp = 1.0d - interp;
        return new Color((int) ((((double) r1) * invInterp) + (((double) this.toValue.getRed()) * interp)), (int) ((((double) g1) * invInterp) + (((double) this.toValue.getGreen()) * interp)), (int) ((((double) b1) * invInterp) + (((double) this.toValue.getBlue()) * interp)));
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.animation.AnimationElement, com.kitfox.svg.animation.AnimateBase
    public void rebuild(AnimTimeParser animTimeParser) throws SVGException {
        super.rebuild(animTimeParser);
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("from"))) {
            this.fromValue = ColorTable.parseColor(sty.getStringValue());
        }
        if (getPres(sty.setName("to"))) {
            this.toValue = ColorTable.parseColor(sty.getStringValue());
        }
    }

    public Color getFromValue() {
        return this.fromValue;
    }

    public void setFromValue(Color fromValue2) {
        this.fromValue = fromValue2;
    }

    public Color getToValue() {
        return this.toValue;
    }

    public void setToValue(Color toValue2) {
        this.toValue = toValue2;
    }
}
