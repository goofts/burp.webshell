package com.kitfox.svg.animation;

import com.kitfox.svg.SVGElementException;
import com.kitfox.svg.xml.StyleAttribute;
import java.util.Iterator;

public class TrackDouble extends TrackBase {
    public TrackDouble(AnimationElement ele) throws SVGElementException {
        super(ele.getParent(), ele);
    }

    @Override // com.kitfox.svg.animation.TrackBase
    public boolean getValue(StyleAttribute attrib, double curTime) {
        double val = getValue(curTime);
        if (Double.isNaN(val)) {
            return false;
        }
        attrib.setStringValue("" + val);
        return true;
    }

    public double getValue(double curTime) {
        double retVal = Double.NaN;
        switch (this.attribType) {
            case 0:
                retVal = this.parent.getStyleAbsolute(this.attribName).getDoubleValue();
                break;
            case 1:
                retVal = this.parent.getPresAbsolute(this.attribName).getDoubleValue();
                break;
            case 2:
                StyleAttribute attr = this.parent.getStyleAbsolute(this.attribName);
                if (attr == null) {
                    attr = this.parent.getPresAbsolute(this.attribName);
                }
                retVal = attr.getDoubleValue();
                break;
        }
        AnimationTimeEval state = new AnimationTimeEval();
        Iterator it = this.animEvents.iterator();
        while (it.hasNext()) {
            Animate ele = (Animate) ((AnimationElement) it.next());
            ele.evalParametric(state, curTime);
            if (!Double.isNaN(state.interp)) {
                switch (ele.getAdditiveType()) {
                    case 0:
                        retVal = ele.eval(state.interp);
                        break;
                    case 1:
                        retVal += ele.eval(state.interp);
                        break;
                }
                if (state.rep > 0) {
                    switch (ele.getAccumulateType()) {
                        case 1:
                            retVal += ele.repeatSkipSize(state.rep);
                            continue;
                    }
                }
            }
        }
        return retVal;
    }
}
