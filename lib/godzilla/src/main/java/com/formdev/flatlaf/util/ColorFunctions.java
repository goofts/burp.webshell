package com.formdev.flatlaf.util;

import java.awt.Color;

public class ColorFunctions {

    public interface ColorFunction {
        void apply(float[] fArr);
    }

    public static Color applyFunctions(Color color, ColorFunction... functions) {
        float[] hsl = HSLColor.fromRGB(color);
        float[] hsla = {hsl[0], hsl[1], hsl[2], (((float) color.getAlpha()) / 255.0f) * 100.0f};
        for (ColorFunction function : functions) {
            function.apply(hsla);
        }
        return HSLColor.toRGB(hsla[0], hsla[1], hsla[2], hsla[3] / 100.0f);
    }

    public static float clamp(float value) {
        if (value < 0.0f) {
            return 0.0f;
        }
        if (value > 100.0f) {
            return 100.0f;
        }
        return value;
    }

    public static Color mix(Color color1, Color color2, float weight) {
        if (weight >= 1.0f) {
            return color1;
        }
        if (weight <= 0.0f) {
            return color2;
        }
        int r1 = color1.getRed();
        int g1 = color1.getGreen();
        int b1 = color1.getBlue();
        int a1 = color1.getAlpha();
        int r2 = color2.getRed();
        int g2 = color2.getGreen();
        int b2 = color2.getBlue();
        int a2 = color2.getAlpha();
        return new Color(Math.round(((float) r2) + (((float) (r1 - r2)) * weight)), Math.round(((float) g2) + (((float) (g1 - g2)) * weight)), Math.round(((float) b2) + (((float) (b1 - b2)) * weight)), Math.round(((float) a2) + (((float) (a1 - a2)) * weight)));
    }

    public static class HSLIncreaseDecrease implements ColorFunction {
        public final float amount;
        public final boolean autoInverse;
        public final int hslIndex;
        public final boolean increase;
        public final boolean relative;

        public HSLIncreaseDecrease(int hslIndex2, boolean increase2, float amount2, boolean relative2, boolean autoInverse2) {
            this.hslIndex = hslIndex2;
            this.increase = increase2;
            this.amount = amount2;
            this.relative = relative2;
            this.autoInverse = autoInverse2;
        }

        @Override // com.formdev.flatlaf.util.ColorFunctions.ColorFunction
        public void apply(float[] hsla) {
            float amount2 = this.increase ? this.amount : -this.amount;
            if (this.hslIndex == 0) {
                hsla[0] = (hsla[0] + amount2) % 360.0f;
                return;
            }
            if (this.autoInverse && shouldInverse(hsla)) {
                amount2 = -amount2;
            }
            hsla[this.hslIndex] = ColorFunctions.clamp(this.relative ? hsla[this.hslIndex] * ((100.0f + amount2) / 100.0f) : hsla[this.hslIndex] + amount2);
        }

        /* access modifiers changed from: protected */
        public boolean shouldInverse(float[] hsla) {
            return this.increase ? hsla[this.hslIndex] > 65.0f : hsla[this.hslIndex] < 35.0f;
        }
    }

    public static class Fade implements ColorFunction {
        public final float amount;

        public Fade(float amount2) {
            this.amount = amount2;
        }

        @Override // com.formdev.flatlaf.util.ColorFunctions.ColorFunction
        public void apply(float[] hsla) {
            hsla[3] = ColorFunctions.clamp(this.amount);
        }
    }
}
