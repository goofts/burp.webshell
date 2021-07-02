package com.formdev.flatlaf.util;

import com.formdev.flatlaf.FlatSystemProperties;
import java.awt.Graphics2D;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.text.AttributedCharacterIterator;
import javax.swing.JComponent;

public class HiDPIUtils {
    private static Boolean useTextYCorrection;

    public interface Painter {
        void paint(Graphics2D graphics2D, int i, int i2, int i3, int i4, double d);
    }

    public static void paintAtScale1x(Graphics2D g, JComponent c, Painter painter) {
        paintAtScale1x(g, 0, 0, c.getWidth(), c.getHeight(), painter);
    }

    /* JADX INFO: finally extract failed */
    public static void paintAtScale1x(Graphics2D g, int x, int y, int width, int height, Painter painter) {
        AffineTransform transform = g.getTransform();
        if (transform.getScaleX() == 1.0d && transform.getScaleY() == 1.0d) {
            painter.paint(g, x, y, width, height, 1.0d);
            return;
        }
        Rectangle2D.Double scaledRect = scale(transform, x, y, width, height);
        try {
            g.setTransform(new AffineTransform(1.0d, 0.0d, 0.0d, 1.0d, Math.floor(scaledRect.x), Math.floor(scaledRect.y)));
            painter.paint(g, 0, 0, (int) scaledRect.width, (int) scaledRect.height, transform.getScaleX());
            g.setTransform(transform);
        } catch (Throwable th) {
            g.setTransform(transform);
            throw th;
        }
    }

    private static Rectangle2D.Double scale(AffineTransform transform, int x, int y, int width, int height) {
        double dx1 = transform.getScaleX();
        double dy2 = transform.getScaleY();
        double px = (((double) x) * dx1) + transform.getTranslateX();
        double py = (((double) y) * dy2) + transform.getTranslateY();
        double dx12 = dx1 * ((double) width);
        double dy22 = dy2 * ((double) height);
        double newx = normalize(px);
        double newy = normalize(py);
        return new Rectangle2D.Double(newx, newy, normalize(px + dx12) - newx, normalize(py + dy22) - newy);
    }

    private static double normalize(double value) {
        return Math.floor(value + 0.25d) + 0.25d;
    }

    private static boolean useTextYCorrection() {
        if (useTextYCorrection == null) {
            useTextYCorrection = Boolean.valueOf(FlatSystemProperties.getBoolean(FlatSystemProperties.USE_TEXT_Y_CORRECTION, true));
        }
        return useTextYCorrection.booleanValue();
    }

    public static float computeTextYCorrection(Graphics2D g) {
        if (!useTextYCorrection() || !SystemInfo.isWindows) {
            return 0.0f;
        }
        if (SystemInfo.isJava_9_orLater) {
            double scaleY = g.getTransform().getScaleY();
            if (scaleY < 1.25d) {
                return 0.0f;
            }
            if (scaleY <= 1.25d) {
                return -0.875f;
            }
            if (scaleY <= 1.5d) {
                return -0.625f;
            }
            if (scaleY <= 1.75d) {
                return -0.875f;
            }
            if (scaleY <= 2.0d) {
                return -0.75f;
            }
            if (scaleY <= 2.25d) {
                return -0.875f;
            }
            return scaleY <= 3.5d ? -0.75f : -0.875f;
        } else if (UIScale.getUserScaleFactor() > 1.0f) {
            return -UIScale.scale(0.625f);
        } else {
            return 0.0f;
        }
    }

    public static void drawStringWithYCorrection(JComponent c, Graphics2D g, String text, int x, int y) {
        drawStringUnderlineCharAtWithYCorrection(c, g, text, -1, x, y);
    }

    public static void drawStringUnderlineCharAtWithYCorrection(JComponent c, Graphics2D g, String text, int underlinedIndex, int x, int y) {
        float yCorrection = computeTextYCorrection(g);
        if (yCorrection != 0.0f) {
            g.translate(0.0d, (double) yCorrection);
            JavaCompatibility.drawStringUnderlineCharAt(c, g, text, underlinedIndex, x, y);
            g.translate(0.0d, (double) (-yCorrection));
            return;
        }
        JavaCompatibility.drawStringUnderlineCharAt(c, g, text, underlinedIndex, x, y);
    }

    public static Graphics2D createGraphicsTextYCorrection(Graphics2D g) {
        final float yCorrection = computeTextYCorrection(g);
        return yCorrection == 0.0f ? g : new Graphics2DProxy(g) {
            /* class com.formdev.flatlaf.util.HiDPIUtils.AnonymousClass1 */

            @Override // com.formdev.flatlaf.util.Graphics2DProxy
            public void drawString(String str, int x, int y) {
                super.drawString(str, (float) x, ((float) y) + yCorrection);
            }

            @Override // com.formdev.flatlaf.util.Graphics2DProxy
            public void drawString(String str, float x, float y) {
                super.drawString(str, x, yCorrection + y);
            }

            @Override // com.formdev.flatlaf.util.Graphics2DProxy
            public void drawString(AttributedCharacterIterator iterator, int x, int y) {
                super.drawString(iterator, (float) x, ((float) y) + yCorrection);
            }

            @Override // com.formdev.flatlaf.util.Graphics2DProxy
            public void drawString(AttributedCharacterIterator iterator, float x, float y) {
                super.drawString(iterator, x, yCorrection + y);
            }

            @Override // com.formdev.flatlaf.util.Graphics2DProxy
            public void drawChars(char[] data, int offset, int length, int x, int y) {
                super.drawChars(data, offset, length, x, Math.round(((float) y) + yCorrection));
            }

            @Override // com.formdev.flatlaf.util.Graphics2DProxy
            public void drawBytes(byte[] data, int offset, int length, int x, int y) {
                super.drawBytes(data, offset, length, x, Math.round(((float) y) + yCorrection));
            }

            @Override // com.formdev.flatlaf.util.Graphics2DProxy
            public void drawGlyphVector(GlyphVector g, float x, float y) {
                super.drawGlyphVector(g, x, yCorrection + y);
            }
        };
    }
}
