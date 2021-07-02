package com.kitfox.svg.animation;

import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class TimeCompound extends TimeBase {
    static final Pattern patPlus = Pattern.compile("\\+");
    final List<TimeBase> componentTimes;
    private AnimationElement parent;

    public TimeCompound(List<TimeBase> timeBases) {
        this.componentTimes = Collections.unmodifiableList(timeBases);
    }

    @Override // com.kitfox.svg.animation.TimeBase
    public double evalTime() {
        double agg = 0.0d;
        for (TimeBase timeEle : this.componentTimes) {
            agg += timeEle.evalTime();
        }
        return agg;
    }

    @Override // com.kitfox.svg.animation.TimeBase
    public void setParentElement(AnimationElement ele) {
        this.parent = ele;
        for (TimeBase timeEle : this.componentTimes) {
            timeEle.setParentElement(ele);
        }
    }
}
