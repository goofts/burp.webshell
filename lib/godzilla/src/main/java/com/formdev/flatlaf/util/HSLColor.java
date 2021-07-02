package com.formdev.flatlaf.util;

import java.awt.Color;

public class HSLColor {
    private final float alpha;
    private final float[] hsl;
    private final Color rgb;

    public HSLColor(Color rgb2) {
        this.rgb = rgb2;
        this.hsl = fromRGB(rgb2);
        this.alpha = ((float) rgb2.getAlpha()) / 255.0f;
    }

    public HSLColor(float h, float s, float l) {
        this(h, s, l, 1.0f);
    }

    public HSLColor(float h, float s, float l, float alpha2) {
        this.hsl = new float[]{h, s, l};
        this.alpha = alpha2;
        this.rgb = toRGB(this.hsl, alpha2);
    }

    public HSLColor(float[] hsl2) {
        this(hsl2, 1.0f);
    }

    public HSLColor(float[] hsl2, float alpha2) {
        this.hsl = hsl2;
        this.alpha = alpha2;
        this.rgb = toRGB(hsl2, alpha2);
    }

    public Color adjustHue(float degrees) {
        return toRGB(degrees, this.hsl[1], this.hsl[2], this.alpha);
    }

    public Color adjustLuminance(float percent) {
        return toRGB(this.hsl[0], this.hsl[1], percent, this.alpha);
    }

    public Color adjustSaturation(float percent) {
        return toRGB(this.hsl[0], percent, this.hsl[2], this.alpha);
    }

    public Color adjustShade(float percent) {
        return toRGB(this.hsl[0], this.hsl[1], Math.max(0.0f, this.hsl[2] * ((100.0f - percent) / 100.0f)), this.alpha);
    }

    public Color adjustTone(float percent) {
        return toRGB(this.hsl[0], this.hsl[1], Math.min(100.0f, this.hsl[2] * ((100.0f + percent) / 100.0f)), this.alpha);
    }

    public float getAlpha() {
        return this.alpha;
    }

    public Color getComplementary() {
        return toRGB((this.hsl[0] + 180.0f) % 360.0f, this.hsl[1], this.hsl[2]);
    }

    public float getHue() {
        return this.hsl[0];
    }

    public float[] getHSL() {
        return this.hsl;
    }

    public float getLuminance() {
        return this.hsl[2];
    }

    public Color getRGB() {
        return this.rgb;
    }

    public float getSaturation() {
        return this.hsl[1];
    }

    public String toString() {
        return "HSLColor[h=" + this.hsl[0] + ",s=" + this.hsl[1] + ",l=" + this.hsl[2] + ",alpha=" + this.alpha + "]";
    }

    public static float[] fromRGB(Color color) {
        float s;
        float[] rgb2 = color.getRGBColorComponents((float[]) null);
        float r = rgb2[0];
        float g = rgb2[1];
        float b = rgb2[2];
        float min = Math.min(r, Math.min(g, b));
        float max = Math.max(r, Math.max(g, b));
        float h = 0.0f;
        if (max == min) {
            h = 0.0f;
        } else if (max == r) {
            h = ((((g - b) * 60.0f) / (max - min)) + 360.0f) % 360.0f;
        } else if (max == g) {
            h = (((b - r) * 60.0f) / (max - min)) + 120.0f;
        } else if (max == b) {
            h = (((r - g) * 60.0f) / (max - min)) + 240.0f;
        }
        float l = (max + min) / 2.0f;
        if (max == min) {
            s = 0.0f;
        } else if (l <= 0.5f) {
            s = (max - min) / (max + min);
        } else {
            s = (max - min) / ((2.0f - max) - min);
        }
        return new float[]{h, s * 100.0f, l * 100.0f};
    }

    public static Color toRGB(float[] hsl2) {
        return toRGB(hsl2, 1.0f);
    }

    public static Color toRGB(float[] hsl2, float alpha2) {
        return toRGB(hsl2[0], hsl2[1], hsl2[2], alpha2);
    }

    public static Color toRGB(float h, float s, float l) {
        return toRGB(h, s, l, 1.0f);
    }

    public static Color toRGB(float h, float s, float l, float alpha2) {
        float q;
        if (s < 0.0f || s > 100.0f) {
            throw new IllegalArgumentException("Color parameter outside of expected range - Saturation");
        } else if (l < 0.0f || l > 100.0f) {
            throw new IllegalArgumentException("Color parameter outside of expected range - Luminance");
        } else if (alpha2 < 0.0f || alpha2 > 1.0f) {
            throw new IllegalArgumentException("Color parameter outside of expected range - Alpha");
        } else {
            float h2 = (h % 360.0f) / 360.0f;
            float s2 = s / 100.0f;
            float l2 = l / 100.0f;
            if (((double) l2) < 0.5d) {
                q = l2 * (1.0f + s2);
            } else {
                q = (l2 + s2) - (s2 * l2);
            }
            float p = (2.0f * l2) - q;
            return new Color(Math.min(Math.max(0.0f, HueToRGB(p, q, 0.33333334f + h2)), 1.0f), Math.min(Math.max(0.0f, HueToRGB(p, q, h2)), 1.0f), Math.min(Math.max(0.0f, HueToRGB(p, q, h2 - 0.33333334f)), 1.0f), alpha2);
        }
    }

    private static float HueToRGB(float p, float q, float h) {
        if (h < 0.0f) {
            h += 1.0f;
        }
        if (h > 1.0f) {
            h -= 1.0f;
        }
        if (6.0f * h < 1.0f) {
            return p + ((q - p) * 6.0f * h);
        }
        if (2.0f * h < 1.0f) {
            return q;
        }
        if (3.0f * h < 2.0f) {
            return p + ((q - p) * 6.0f * (0.6666667f - h));
        }
        return p;
    }
}
