package com.kitfox.svg.animation;

import com.kitfox.svg.SVGElementException;
import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Color;
import java.util.Iterator;

public class TrackColor extends TrackBase {
    public TrackColor(AnimationElement ele) throws SVGElementException {
        super(ele.getParent(), ele);
    }

    @Override // com.kitfox.svg.animation.TrackBase
    public boolean getValue(StyleAttribute attrib, double curTime) {
        Color col = getValue(curTime);
        if (col == null) {
            return false;
        }
        attrib.setStringValue("#" + Integer.toHexString(col.getRGB()));
        return true;
    }

    public Color getValue(double curTime) {
        Color retVal = null;
        AnimationTimeEval state = new AnimationTimeEval();
        Iterator it = this.animEvents.iterator();
        while (it.hasNext()) {
            AnimateBase ele = (AnimateBase) ((AnimationElement) it.next());
            AnimateColorIface eleColor = (AnimateColorIface) ele;
            ele.evalParametric(state, curTime);
            if (!Double.isNaN(state.interp)) {
                if (retVal == null) {
                    retVal = eleColor.evalColor(state.interp);
                } else {
                    Color curCol = eleColor.evalColor(state.interp);
                    switch (ele.getAdditiveType()) {
                        case 0:
                            retVal = curCol;
                            continue;
                        case 1:
                            retVal = new Color(curCol.getRed() + retVal.getRed(), curCol.getGreen() + retVal.getGreen(), curCol.getBlue() + retVal.getBlue());
                            continue;
                    }
                }
            }
        }
        return retVal;
    }
}
