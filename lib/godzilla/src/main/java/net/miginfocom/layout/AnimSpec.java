package net.miginfocom.layout;

import java.io.Serializable;

public class AnimSpec implements Serializable {
    public static final AnimSpec DEF = new AnimSpec(0, 0, 0.2f, 0.2f);
    private final int durMillis;
    private final float easeIn;
    private final float easeOut;
    private final int prio;

    public AnimSpec(int prio2, int durMillis2, float easeIn2, float easeOut2) {
        this.prio = prio2;
        this.durMillis = durMillis2;
        this.easeIn = LayoutUtil.clamp(easeIn2, 0.0f, 1.0f);
        this.easeOut = LayoutUtil.clamp(easeOut2, 0.0f, 1.0f);
    }

    public int getPriority() {
        return this.prio;
    }

    public int getDurationMillis(int defMillis) {
        return this.durMillis > 0 ? this.durMillis : defMillis;
    }

    public int getDurationMillis() {
        return this.durMillis;
    }

    public float getEaseIn() {
        return this.easeIn;
    }

    public float getEaseOut() {
        return this.easeOut;
    }
}
