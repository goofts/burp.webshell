package com.formdev.flatlaf.util;

import com.formdev.flatlaf.util.Animator;

public class CubicBezierEasing implements Animator.Interpolator {
    public static final CubicBezierEasing EASE = new CubicBezierEasing(0.25f, 0.1f, 0.25f, 1.0f);
    public static final CubicBezierEasing EASE_IN = new CubicBezierEasing(0.42f, 0.0f, 1.0f, 1.0f);
    public static final CubicBezierEasing EASE_IN_OUT = new CubicBezierEasing(0.42f, 0.0f, 0.58f, 1.0f);
    public static final CubicBezierEasing EASE_OUT = new CubicBezierEasing(0.0f, 0.0f, 0.58f, 1.0f);
    public static final CubicBezierEasing STANDARD_EASING = new CubicBezierEasing(0.4f, 0.0f, 0.2f, 1.0f);
    private final float x1;
    private final float x2;
    private final float y1;
    private final float y2;

    public CubicBezierEasing(float x12, float y12, float x22, float y22) {
        if (x12 < 0.0f || x12 > 1.0f || y12 < 0.0f || y12 > 1.0f || x22 < 0.0f || x22 > 1.0f || y22 < 0.0f || y22 > 1.0f) {
            throw new IllegalArgumentException("control points must be in range [0, 1]");
        }
        this.x1 = x12;
        this.y1 = y12;
        this.x2 = x22;
        this.y2 = y22;
    }

    @Override // com.formdev.flatlaf.util.Animator.Interpolator
    public float interpolate(float fraction) {
        if (fraction <= 0.0f || fraction >= 1.0f) {
            return fraction;
        }
        float low = 0.0f;
        float high = 1.0f;
        while (true) {
            float mid = (low + high) / 2.0f;
            float estimate = cubicBezier(mid, this.x1, this.x2);
            if (Math.abs(fraction - estimate) < 5.0E-4f) {
                return cubicBezier(mid, this.y1, this.y2);
            }
            if (estimate < fraction) {
                low = mid;
            } else {
                high = mid;
            }
        }
    }

    private static float cubicBezier(float t, float xy1, float xy2) {
        float invT = 1.0f - t;
        return (3.0f * t * invT * invT * xy1) + (t * t * 3.0f * invT * xy2) + (t * t * t);
    }
}
