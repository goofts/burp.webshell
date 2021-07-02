package com.jgoodies.common.internal;

import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.PrintGraphics;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.print.PrinterGraphics;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.plaf.basic.BasicGraphicsUtils;

public final class RenderingUtils {
    private static final String PROP_DESKTOPHINTS = "awt.font.desktophints";
    private static final String SWING_UTILITIES2_NAME = "sun.swing.SwingUtilities2";
    private static Method drawStringMethod;
    private static Method drawStringUnderlineCharAtMethod;
    private static Method getFontMetricsMethod;

    static {
        drawStringMethod = null;
        drawStringUnderlineCharAtMethod = null;
        getFontMetricsMethod = null;
        drawStringMethod = getMethodDrawString();
        drawStringUnderlineCharAtMethod = getMethodDrawStringUnderlineCharAt();
        getFontMetricsMethod = getMethodGetFontMetrics();
    }

    private RenderingUtils() {
    }

    public static void drawString(JComponent c, Graphics g, String text, int x, int y) {
        if (drawStringMethod != null) {
            try {
                drawStringMethod.invoke(null, c, g, text, Integer.valueOf(x), Integer.valueOf(y));
                return;
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            }
        }
        Graphics2D g2 = (Graphics2D) g;
        Map<?, ?> oldRenderingHints = installDesktopHints(g2);
        BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, -1, x, y);
        if (oldRenderingHints != null) {
            g2.addRenderingHints(oldRenderingHints);
        }
    }

    public static void drawStringUnderlineCharAt(JComponent c, Graphics g, String text, int underlinedIndex, int x, int y) {
        if (drawStringUnderlineCharAtMethod != null) {
            try {
                drawStringUnderlineCharAtMethod.invoke(null, c, g, text, new Integer(underlinedIndex), new Integer(x), new Integer(y));
                return;
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            }
        }
        Graphics2D g2 = (Graphics2D) g;
        Map oldRenderingHints = installDesktopHints(g2);
        BasicGraphicsUtils.drawStringUnderlineCharAt(g, text, underlinedIndex, x, y);
        if (oldRenderingHints != null) {
            g2.addRenderingHints(oldRenderingHints);
        }
    }

    public static FontMetrics getFontMetrics(JComponent c, Graphics g) {
        if (getFontMetricsMethod != null) {
            try {
                return (FontMetrics) getFontMetricsMethod.invoke(null, c, g);
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            }
        }
        return c.getFontMetrics(g.getFont());
    }

    private static Method getMethodDrawString() {
        try {
            return Class.forName(SWING_UTILITIES2_NAME).getMethod("drawString", JComponent.class, Graphics.class, String.class, Integer.TYPE, Integer.TYPE);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
            return null;
        }
    }

    private static Method getMethodDrawStringUnderlineCharAt() {
        try {
            return Class.forName(SWING_UTILITIES2_NAME).getMethod("drawStringUnderlineCharAt", JComponent.class, Graphics.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
            return null;
        }
    }

    private static Method getMethodGetFontMetrics() {
        try {
            return Class.forName(SWING_UTILITIES2_NAME).getMethod("getFontMetrics", JComponent.class, Graphics.class);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
            return null;
        }
    }

    private static Map installDesktopHints(Graphics2D g2) {
        Map oldRenderingHints = null;
        Map desktopHints = desktopHints(g2);
        if (desktopHints != null && !desktopHints.isEmpty()) {
            oldRenderingHints = new HashMap(desktopHints.size());
            for (RenderingHints.Key key : desktopHints.keySet()) {
                oldRenderingHints.put(key, g2.getRenderingHint(key));
            }
            g2.addRenderingHints(desktopHints);
        }
        return oldRenderingHints;
    }

    private static Map desktopHints(Graphics2D g2) {
        if (isPrinting(g2)) {
            return null;
        }
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Map desktopHints = (Map) toolkit.getDesktopProperty("awt.font.desktophints." + g2.getDeviceConfiguration().getDevice().getIDstring());
        if (desktopHints == null) {
            desktopHints = (Map) toolkit.getDesktopProperty(PROP_DESKTOPHINTS);
        }
        if (desktopHints == null) {
            return desktopHints;
        }
        Object aaHint = desktopHints.get(RenderingHints.KEY_TEXT_ANTIALIASING);
        if (aaHint == RenderingHints.VALUE_TEXT_ANTIALIAS_OFF || aaHint == RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT) {
            return null;
        }
        return desktopHints;
    }

    private static boolean isPrinting(Graphics g) {
        return (g instanceof PrintGraphics) || (g instanceof PrinterGraphics);
    }
}
