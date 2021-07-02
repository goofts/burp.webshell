package com.kitfox.svg.animation;

public class TimeIndefinite extends TimeBase {
    @Override // com.kitfox.svg.animation.TimeBase
    public double evalTime() {
        return Double.POSITIVE_INFINITY;
    }
}
