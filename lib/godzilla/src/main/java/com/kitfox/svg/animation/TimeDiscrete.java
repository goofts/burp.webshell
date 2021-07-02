package com.kitfox.svg.animation;

public class TimeDiscrete extends TimeBase {
    double secs;

    public TimeDiscrete(double secs2) {
        this.secs = secs2;
    }

    @Override // com.kitfox.svg.animation.TimeBase
    public double evalTime() {
        return this.secs;
    }
}
