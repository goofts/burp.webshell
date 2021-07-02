package com.kitfox.svg;

import java.util.Iterator;

public class Defs extends TransformableElement {
    public static final String TAG_NAME = "defs";

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    @Override // com.kitfox.svg.SVGElement
    public void loaderAddChild(SVGLoaderHelper helper, SVGElement child) throws SVGElementException {
        super.loaderAddChild(helper, child);
    }

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.TransformableElement
    public boolean updateTime(double curTime) throws SVGException {
        boolean stateChange = false;
        Iterator it = this.children.iterator();
        while (it.hasNext()) {
            SVGElement ele = (SVGElement) it.next();
            if (stateChange || ele.updateTime(curTime)) {
                stateChange = true;
            } else {
                stateChange = false;
            }
        }
        return super.updateTime(curTime) || stateChange;
    }
}
