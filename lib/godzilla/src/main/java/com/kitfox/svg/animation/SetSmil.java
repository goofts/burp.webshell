package com.kitfox.svg.animation;

import com.kitfox.svg.SVGElement;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGLoaderHelper;
import com.kitfox.svg.animation.parser.AnimTimeParser;
import com.kitfox.svg.xml.StyleAttribute;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class SetSmil extends AnimationElement {
    public static final String TAG_NAME = "set";
    private String toValue;

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    @Override // com.kitfox.svg.animation.AnimationElement, com.kitfox.svg.SVGElement
    public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent) throws SAXException {
        super.loaderStartElement(helper, attrs, parent);
        this.toValue = attrs.getValue("to");
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.animation.AnimationElement
    public void rebuild(AnimTimeParser animTimeParser) throws SVGException {
        super.rebuild(animTimeParser);
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("to"))) {
            this.toValue = sty.getStringValue();
        }
    }

    public String getToValue() {
        return this.toValue;
    }

    public void setToValue(String toValue2) {
        this.toValue = toValue2;
    }
}
