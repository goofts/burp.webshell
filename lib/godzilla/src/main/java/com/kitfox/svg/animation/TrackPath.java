package com.kitfox.svg.animation;

import com.kitfox.svg.SVGElementException;
import com.kitfox.svg.pathcmd.PathUtil;
import com.kitfox.svg.xml.StyleAttribute;
import java.awt.geom.GeneralPath;
import java.util.Iterator;

public class TrackPath extends TrackBase {
    public TrackPath(AnimationElement ele) throws SVGElementException {
        super(ele.getParent(), ele);
    }

    @Override // com.kitfox.svg.animation.TrackBase
    public boolean getValue(StyleAttribute attrib, double curTime) {
        GeneralPath path = getValue(curTime);
        if (path == null) {
            return false;
        }
        attrib.setStringValue(PathUtil.buildPathString(path));
        return true;
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    public GeneralPath getValue(double curTime) {
        GeneralPath retVal = null;
        AnimationTimeEval state = new AnimationTimeEval();
        Iterator it = this.animEvents.iterator();
        while (it.hasNext()) {
            AnimateBase ele = (AnimateBase) ((AnimationElement) it.next());
            Animate eleAnim = (Animate) ele;
            ele.evalParametric(state, curTime);
            if (!Double.isNaN(state.interp)) {
                if (retVal == null) {
                    retVal = eleAnim.evalPath(state.interp);
                } else {
                    GeneralPath curPath = eleAnim.evalPath(state.interp);
                    switch (ele.getAdditiveType()) {
                        case 0:
                            retVal = curPath;
                            continue;
                        case 1:
                            throw new RuntimeException("Not implemented");
                        default:
                            throw new RuntimeException();
                    }
                }
            }
        }
        return retVal;
    }
}
