package com.kitfox.svg.animation;

import com.kitfox.svg.SVGElement;
import com.kitfox.svg.SVGLoaderHelper;
import java.awt.geom.AffineTransform;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public abstract class AnimateXform extends AnimateBase {
    public abstract AffineTransform eval(AffineTransform affineTransform, double d);

    @Override // com.kitfox.svg.animation.AnimationElement, com.kitfox.svg.SVGElement, com.kitfox.svg.animation.AnimateBase
    public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent) throws SAXException {
        super.loaderStartElement(helper, attrs, parent);
    }
}
