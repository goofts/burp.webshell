package com.kitfox.svg.animation;

public class TimeLookup extends TimeBase {
    String event;
    String node;
    String paramList;
    private AnimationElement parent;

    public TimeLookup(AnimationElement parent2, String node2, String event2, String paramList2) {
        this.parent = parent2;
        this.node = node2;
        this.event = event2;
        this.paramList = paramList2;
    }

    @Override // com.kitfox.svg.animation.TimeBase
    public double evalTime() {
        return 0.0d;
    }

    @Override // com.kitfox.svg.animation.TimeBase
    public void setParentElement(AnimationElement ele) {
        this.parent = ele;
    }
}
