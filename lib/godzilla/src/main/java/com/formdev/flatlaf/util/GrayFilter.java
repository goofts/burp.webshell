package com.formdev.flatlaf.util;

import java.awt.image.RGBImageFilter;

public class GrayFilter extends RGBImageFilter {
    private final int alpha;
    private final float brightness;
    private final float contrast;
    private final int origBrightness;
    private final int origContrast;

    public static GrayFilter createDisabledIconFilter(boolean dark) {
        return dark ? new GrayFilter(-20, -70, 100) : new GrayFilter(25, -25, 100);
    }

    public GrayFilter(int brightness2, int contrast2, int alpha2) {
        this.origBrightness = Math.max(-100, Math.min(100, brightness2));
        this.origContrast = Math.max(-100, Math.min(100, contrast2));
        this.alpha = Math.max(0, Math.min(100, alpha2));
        this.brightness = (float) (Math.pow((double) this.origBrightness, 3.0d) / 10000.0d);
        this.contrast = ((float) this.origContrast) / 100.0f;
        this.canFilterIndexColorModel = true;
    }

    public GrayFilter() {
        this(0, 0, 100);
    }

    public int getBrightness() {
        return this.origBrightness;
    }

    public int getContrast() {
        return this.origContrast;
    }

    public int getAlpha() {
        return this.alpha;
    }

    public int filterRGB(int x, int y, int rgb) {
        int gray;
        int gray2;
        int gray3 = (int) ((0.3d * ((double) ((rgb >> 16) & 255))) + (0.59d * ((double) ((rgb >> 8) & 255))) + (0.11d * ((double) (rgb & 255))));
        if (this.brightness >= 0.0f) {
            gray = (int) ((((float) gray3) + (this.brightness * 255.0f)) / (this.brightness + 1.0f));
        } else {
            gray = (int) (((float) gray3) / (1.0f - this.brightness));
        }
        if (this.contrast < 0.0f) {
            gray2 = (int) (127.0f + (((float) (gray - 127)) * (this.contrast + 1.0f)));
        } else if (gray >= 127) {
            gray2 = (int) (((float) gray) + (((float) (255 - gray)) * this.contrast));
        } else {
            gray2 = (int) (((float) gray) - (((float) gray) * this.contrast));
        }
        return (gray2 << 16) | (this.alpha != 100 ? ((((rgb >> 24) & 255) * this.alpha) / 100) << 24 : rgb & -16777216) | (gray2 << 8) | gray2;
    }
}
