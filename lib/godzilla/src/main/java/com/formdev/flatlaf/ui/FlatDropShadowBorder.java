package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.HiDPIUtils;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RadialGradientPaint;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

public class FlatDropShadowBorder extends FlatEmptyBorder {
    private Color lastShadowColor;
    private double lastSystemScaleFactor;
    private float lastUserScaleFactor;
    private final Color shadowColor;
    private Image shadowImage;
    private final Insets shadowInsets;
    private final float shadowOpacity;
    private final int shadowSize;

    public FlatDropShadowBorder() {
        this(null);
    }

    public FlatDropShadowBorder(Color shadowColor2) {
        this(shadowColor2, 4, 0.5f);
    }

    public FlatDropShadowBorder(Color shadowColor2, int shadowSize2, float shadowOpacity2) {
        this(shadowColor2, new Insets(-shadowSize2, -shadowSize2, shadowSize2, shadowSize2), shadowOpacity2);
    }

    public FlatDropShadowBorder(Color shadowColor2, Insets shadowInsets2, float shadowOpacity2) {
        super(Math.max(shadowInsets2.top, 0), Math.max(shadowInsets2.left, 0), Math.max(shadowInsets2.bottom, 0), Math.max(shadowInsets2.right, 0));
        this.shadowColor = shadowColor2;
        this.shadowInsets = shadowInsets2;
        this.shadowOpacity = shadowOpacity2;
        this.shadowSize = Math.max(Math.max(shadowInsets2.left, shadowInsets2.right), Math.max(shadowInsets2.top, shadowInsets2.bottom));
    }

    /*  JADX ERROR: MOVE_RESULT instruction can be used only in fallback mode
        jadx.core.utils.exceptions.CodegenException: MOVE_RESULT instruction can be used only in fallback mode
        	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:604)
        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:542)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:230)
        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:119)
        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:103)
        	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:806)
        	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:746)
        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:367)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:249)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:217)
        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:110)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:56)
        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:93)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:59)
        	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:99)
        	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:143)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:93)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:59)
        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:93)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:59)
        	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:244)
        	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:237)
        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:342)
        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:295)
        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:264)
        	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:184)
        	at java.util.ArrayList.forEach(ArrayList.java:1257)
        	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:390)
        	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
        */
    public void paintBorder(java.awt.Component r7, java.awt.Graphics r8, int r9, int r10, int r11, int r12) {
        /*
            r6 = this;
            int r0 = r6.shadowSize
            if (r0 > 0) goto L_0x0005
        L_0x0004:
            return
        L_0x0005:
            r0 = r8
            java.awt.Graphics2D r0 = (java.awt.Graphics2D) r0
            r5 = move-result
            r1 = r9
            r2 = r10
            r3 = r11
            r4 = r12
            com.formdev.flatlaf.util.HiDPIUtils.paintAtScale1x(r0, r1, r2, r3, r4, r5)
            goto L_0x0004
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatDropShadowBorder.paintBorder(java.awt.Component, java.awt.Graphics, int, int, int, int):void");
    }

    private void paintImpl(Graphics2D g, int x, int y, int width, int height, double scaleFactor) {
        Color shadowColor2 = this.shadowColor != null ? this.shadowColor : g.getColor();
        int shadowSize2 = scale(this.shadowSize, scaleFactor);
        float userScaleFactor = UIScale.getUserScaleFactor();
        if (this.shadowImage == null || !shadowColor2.equals(this.lastShadowColor) || this.lastSystemScaleFactor != scaleFactor || this.lastUserScaleFactor != userScaleFactor) {
            this.shadowImage = createShadowImage(shadowColor2, shadowSize2, this.shadowOpacity, (float) (((double) userScaleFactor) * scaleFactor));
            this.lastShadowColor = shadowColor2;
            this.lastSystemScaleFactor = scaleFactor;
            this.lastUserScaleFactor = userScaleFactor;
        }
        int left = scale(this.shadowInsets.left, scaleFactor);
        int right = scale(this.shadowInsets.right, scaleFactor);
        int top = scale(this.shadowInsets.top, scaleFactor);
        int bottom = scale(this.shadowInsets.bottom, scaleFactor);
        int x1o = x - Math.min(left, 0);
        int y1o = y - Math.min(top, 0);
        int x2o = x + width + Math.min(right, 0);
        int y2o = y + height + Math.min(bottom, 0);
        int x1i = x1o + shadowSize2;
        int y1i = y1o + shadowSize2;
        int x2i = x2o - shadowSize2;
        int y2i = y2o - shadowSize2;
        int wh = (shadowSize2 * 2) - 1;
        int center = shadowSize2 - 1;
        if (left > 0 || top > 0) {
            g.drawImage(this.shadowImage, x1o, y1o, x1i, y1i, 0, 0, center, center, (ImageObserver) null);
        }
        if (top > 0) {
            g.drawImage(this.shadowImage, x1i, y1o, x2i, y1i, center, 0, center + 1, center, (ImageObserver) null);
        }
        if (right > 0 || top > 0) {
            g.drawImage(this.shadowImage, x2i, y1o, x2o, y1i, center, 0, wh, center, (ImageObserver) null);
        }
        if (left > 0) {
            g.drawImage(this.shadowImage, x1o, y1i, x1i, y2i, 0, center, center, center + 1, (ImageObserver) null);
        }
        if (right > 0) {
            g.drawImage(this.shadowImage, x2i, y1i, x2o, y2i, center, center, wh, center + 1, (ImageObserver) null);
        }
        if (left > 0 || bottom > 0) {
            g.drawImage(this.shadowImage, x1o, y2i, x1i, y2o, 0, center, center, wh, (ImageObserver) null);
        }
        if (bottom > 0) {
            g.drawImage(this.shadowImage, x1i, y2i, x2i, y2o, center, center, center + 1, wh, (ImageObserver) null);
        }
        if (right > 0 || bottom > 0) {
            g.drawImage(this.shadowImage, x2i, y2i, x2o, y2o, center, center, wh, wh, (ImageObserver) null);
        }
    }

    private int scale(int value, double scaleFactor) {
        return (int) Math.ceil(((double) UIScale.scale(value)) * scaleFactor);
    }

    private static BufferedImage createShadowImage(Color shadowColor2, int shadowSize2, float shadowOpacity2, float scaleFactor) {
        int shadowRGB = shadowColor2.getRGB() & 16777215;
        int shadowAlpha = (int) (255.0f * shadowOpacity2);
        int wh = (shadowSize2 * 2) - 1;
        int center = shadowSize2 - 1;
        RadialGradientPaint p = new RadialGradientPaint((float) center, (float) center, ((float) shadowSize2) - (0.75f * scaleFactor), new float[]{0.0f, 0.35f, 1.0f}, new Color[]{new Color(((shadowAlpha & 255) << 24) | shadowRGB, true), new Color((((shadowAlpha / 2) & 255) << 24) | shadowRGB, true), new Color(shadowRGB, true)});
        BufferedImage image = new BufferedImage(wh, wh, 2);
        Graphics2D g = image.createGraphics();
        try {
            g.setPaint(p);
            g.fillRect(0, 0, wh, wh);
            return image;
        } finally {
            g.dispose();
        }
    }
}
