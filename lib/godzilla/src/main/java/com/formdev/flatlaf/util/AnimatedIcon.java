package com.formdev.flatlaf.util;

import com.formdev.flatlaf.util.Animator;
import java.awt.Component;
import java.awt.Graphics;
import javassist.bytecode.Opcode;
import javax.swing.Icon;
import javax.swing.JComponent;

public interface AnimatedIcon extends Icon {
    float getValue(Component component);

    void paintIconAnimated(Component component, Graphics graphics, int i, int i2, float f);

    default void paintIcon(Component c, Graphics g, int x, int y) {
        AnimationSupport.paintIcon(this, c, g, x, y);
    }

    default boolean isAnimationEnabled() {
        return true;
    }

    default int getAnimationDuration() {
        return Opcode.FCMPG;
    }

    default int getAnimationResolution() {
        return 10;
    }

    default Animator.Interpolator getAnimationInterpolator() {
        return CubicBezierEasing.STANDARD_EASING;
    }

    default Object getClientPropertyKey() {
        return getClass();
    }

    public static class AnimationSupport {
        private float animatedValue;
        private Animator animator;
        private float fraction;
        private float startValue;
        private float targetValue;
        private int x;
        private int y;

        public static void paintIcon(AnimatedIcon icon, Component c, Graphics g, int x2, int y2) {
            if (!isAnimationEnabled(icon, c)) {
                paintIconImpl(icon, c, g, x2, y2, null);
                return;
            }
            JComponent jc = (JComponent) c;
            Object key = icon.getClientPropertyKey();
            AnimationSupport as = (AnimationSupport) jc.getClientProperty(key);
            if (as == null) {
                as = new AnimationSupport();
                float value = icon.getValue(c);
                as.animatedValue = value;
                as.targetValue = value;
                as.startValue = value;
                as.x = x2;
                as.y = y2;
                jc.putClientProperty(key, as);
            } else {
                float value2 = icon.getValue(c);
                if (value2 != as.targetValue) {
                    if (as.animator == null) {
                        as.animator = new Animator(icon.getAnimationDuration(), 
                    }
