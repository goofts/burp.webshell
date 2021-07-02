package com.kitfox.svg.animation;

import com.kitfox.svg.SVGElement;
import com.kitfox.svg.SVGElementException;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.xml.StyleAttribute;
import java.awt.geom.AffineTransform;
import java.util.Iterator;

public class TrackTransform extends TrackBase {
    public TrackTransform(AnimationElement ele) throws SVGElementException {
        super(ele.getParent(), ele);
    }

    @Override // com.kitfox.svg.animation.TrackBase
    public boolean getValue(StyleAttribute attrib, double curTime) throws SVGException {
        double[] mat = new double[6];
        getValue(new AffineTransform(), curTime).getMatrix(mat);
        attrib.setStringValue("matrix(" + mat[0] + " " + mat[1] + " " + mat[2] + " " + mat[3] + " " + mat[4] + " " + mat[5] + ")");
        return true;
    }

    public AffineTransform getValue(AffineTransform retVal, double curTime) throws SVGException {
        switch (this.attribType) {
            case 0:
                retVal.setTransform(SVGElement.parseSingleTransform(this.parent.getStyleAbsolute(this.attribName).getStringValue()));
                break;
            case 1:
                retVal.setTransform(SVGElement.parseSingleTransform(this.parent.getPresAbsolute(this.attribName).getStringValue()));
                break;
            case 2:
                StyleAttribute attr = this.parent.getStyleAbsolute(this.attribName);
                if (attr == null) {
                    attr = this.parent.getPresAbsolute(this.attribName);
                }
                retVal.setTransform(SVGElement.parseSingleTransform(attr.getStringValue()));
                break;
        }
        AnimationTimeEval state = new AnimationTimeEval();
        AffineTransform xform = new AffineTransform();
        Iterator it = this.animEvents.iterator();
        while (it.hasNext()) {
            AnimateXform ele = (AnimateXform) ((AnimationElement) it.next());
            ele.evalParametric(state, curTime);
            if (!Double.isNaN(state.interp)) {
                switch (ele.getAdditiveType()) {
                    case 0:
                        retVal.setTransform(ele.eval(xform, state.interp));
                        continue;
                    case 1:
                        retVal.concatenate(ele.eval(xform, state.interp));
                        continue;
                }
            }
        }
        return retVal;
    }
}
