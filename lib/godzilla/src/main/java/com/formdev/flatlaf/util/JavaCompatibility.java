package com.formdev.flatlaf.util;

import com.formdev.flatlaf.FlatLaf;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;

public class JavaCompatibility {
    private static Method drawStringUnderlineCharAtMethod;
    private static Method getClippedStringMethod;

    public static void drawStringUnderlineCharAt(JComponent c, Graphics g, String text, int underlinedIndex, int x, int y) {
        synchronized (JavaCompatibility.class) {
            if (drawStringUnderlineCharAtMethod == null) {
                try {
                    drawStringUnderlineCharAtMethod = Class.forName(SystemInfo.isJava_9_orLater ? "javax.swing.plaf.basic.BasicGraphicsUtils" : "sun.swing.SwingUtilities2").getMethod("drawStringUnderlineCharAt", SystemInfo.isJava_9_orLater ? new Class[]{JComponent.class, Graphics2D.class, String.class, Integer.TYPE, Float.TYPE, Float.TYPE} : new Class[]{JComponent.class, Graphics.class, String.class, Integer.TYPE, Integer.TYPE, Integer.TYPE});
                } catch (Exception ex) {
                    Logger.getLogger(FlatLaf.class.getName()).log(Level.SEVERE, (String) null, (Throwable) ex);
                    throw new RuntimeException(ex);
                }
            }
        }
        try {
            if (SystemInfo.isJava_9_orLater) {
                drawStringUnderlineCharAtMethod.invoke(null, c, g, text, Integer.valueOf(underlinedIndex), Float.valueOf((float) x), Float.valueOf((float) y));
            } else {
                drawStringUnderlineCharAtMethod.invoke(null, c, g, text, Integer.valueOf(underlinedIndex), Integer.valueOf(x), Integer.valueOf(y));
            }
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex2) {
            Logger.getLogger(FlatLaf.class.getName()).log(Level.SEVERE, (String) null, (Throwable) ex2);
            throw new RuntimeException(ex2);
        }
    }

    public static String getClippedString(JComponent c, FontMetrics fm, String string, int availTextWidth) {
        synchronized (JavaCompatibility.class) {
            if (getClippedStringMethod == null) {
                try {
                    getClippedStringMethod = Class.forName(SystemInfo.isJava_9_orLater ? "javax.swing.plaf.basic.BasicGraphicsUtils" : "sun.swing.SwingUtilities2").getMethod(SystemInfo.isJava_9_orLater ? "getClippedString" : "clipStringIfNecessary", JComponent.class, FontMetrics.class, String.class, Integer.TYPE);
                } catch (Exception ex) {
                    Logger.getLogger(FlatLaf.class.getName()).log(Level.SEVERE, (String) null, (Throwable) ex);
                    throw new RuntimeException(ex);
                }
            }
        }
        try {
            return (String) getClippedStringMethod.invoke(null, c, fm, string, Integer.valueOf(availTextWidth));
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex2) {
            Logger.getLogger(FlatLaf.class.getName()).log(Level.SEVERE, (String) null, (Throwable) ex2);
            throw new RuntimeException(ex2);
        }
    }
}
