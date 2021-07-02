package com.kitfox.svg.animation;

public class TimeSum extends TimeBase {
    boolean add;
    TimeBase t1;
    TimeBase t2;

    public TimeSum(TimeBase t12, TimeBase t22, boolean add2) {
        this.t1 = t12;
        this.t2 = t22;
        this.add = add2;
    }

    @Override // com.kitfox.svg.animation.TimeBase
    public double evalTime() {
        return this.add ? this.t1.evalTime() + this.t2.evalTime() : this.t1.evalTime() - this.t2.evalTime();
    }

    @Override // com.kitfox.svg.animation.TimeBase
    public void setParentElement(AnimationElement ele) {
        this.t1.setParentElement(ele);
        this.t2.setParentElement(ele);
    }
}
