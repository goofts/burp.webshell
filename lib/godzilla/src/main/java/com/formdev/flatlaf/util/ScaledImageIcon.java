package com.formdev.flatlaf.util;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import javax.swing.Icon;
import javax.swing.ImageIcon;

public class ScaledImageIcon implements Icon {
    private final int iconHeight;
    private final int iconWidth;
    private final ImageIcon imageIcon;
    private Image lastImage;
    private double lastSystemScaleFactor;
    private float lastUserScaleFactor;

    public ScaledImageIcon(ImageIcon imageIcon2) {
        this(imageIcon2, imageIcon2.getIconWidth(), imageIcon2.getIconHeight());
    }

    public ScaledImageIcon(ImageIcon imageIcon2, int iconWidth2, int iconHeight2) {
        this.imageIcon = imageIcon2;
        this.iconWidth = iconWidth2;
        this.iconHeight = iconHeight2;
    }

    public int getIconWidth() {
        return UIScale.scale(this.iconWidth);
    }

    public int getIconHeight() {
        return UIScale.scale(this.iconHeight);
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        double systemScaleFactor = UIScale.getSystemScaleFactor((Graphics2D) g);
        float userScaleFactor = UIScale.getUserScaleFactor();
        double scaleFactor = systemScaleFactor * ((double) userScaleFactor);
        if (scaleFactor == 1.0d && this.iconWidth == this.imageIcon.getIconWidth() && this.iconHeight == this.imageIcon.getIconHeight()) {
            this.imageIcon.paintIcon(c, g, x, y);
        } else if (systemScaleFactor == this.lastSystemScaleFactor && userScaleFactor == this.lastUserScaleFactor && this.lastImage != null) {
            paintLastImage(g, x, y);
        } else {
            int destImageWidth = (int) Math.round(((double) this.iconWidth) * scaleFactor);
            int destImageHeight = (int) Math.round(((double) this.iconHeight) * scaleFactor);
            Image image = getResolutionVariant(destImageWidth, destImageHeight);
            int imageWidth = image.getWidth((ImageObserver) null);
            int imageHeight = image.getHeight((ImageObserver) null);
            if (!(imageWidth == destImageWidth && imageHeight == destImageHeight)) {
                Object scalingInterpolation = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
                float imageScaleFactor = ((float) destImageWidth) / ((float) imageWidth);
                if (((float) ((int) imageScaleFactor)) == imageScaleFactor && imageScaleFactor > 1.0f && imageWidth <= 16 && imageHeight <= 16) {
                    scalingInterpolation = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
                }
                image = scaleImage(image2bufferedImage(image), destImageWidth, destImageHeight, scalingInterpolation);
            }
            this.lastSystemScaleFactor = systemScaleFactor;
            this.lastUserScaleFactor = userScaleFactor;
            this.lastImage = image;
            paintLastImage(g, x, y);
        }
    }

    /* access modifiers changed from: protected */
    public Image getResolutionVariant(int destImageWidth, int destImageHeight) {
        return MultiResolutionImageSupport.getResolutionVariant(this.imageIcon.getImage(), destImageWidth, destImageHeight);
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
    private void paintLastImage(java.awt.Graphics r7, int r8, int r9) {
        /*
            r6 = this;
            r3 = 100
            double r0 = r6.lastSystemScaleFactor
            r4 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            int r0 = (r0 > r4 ? 1 : (r0 == r4 ? 0 : -1))
            if (r0 <= 0) goto L_0x0018
            r0 = r7
            java.awt.Graphics2D r0 = (java.awt.Graphics2D) r0
            r5 = move-result
            r1 = r8
            r2 = r9
            r4 = r3
            com.formdev.flatlaf.util.HiDPIUtils.paintAtScale1x(r0, r1, r2, r3, r4, r5)
        L_0x0017:
            return
        L_0x0018:
            java.awt.Image r0 = r6.lastImage
            r1 = 0
            r7.drawImage(r0, r8, r9, r1)
            goto L_0x0017
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.util.ScaledImageIcon.paintLastImage(java.awt.Graphics, int, int):void");
    }

    private /* synthetic */ void lambda$paintLastImage$0(Graphics2D g2, int x2, int y2, int width2, int height2, double scaleFactor2) {
        g2.drawImage(this.lastImage, x2, y2, (ImageObserver) null);
    }

    private BufferedImage scaleImage(BufferedImage image, int targetWidth, int targetHeight, Object scalingInterpolation) {
        BufferedImage bufferedImage = new BufferedImage(targetWidth, targetHeight, 2);
        Graphics2D g = bufferedImage.createGraphics();
        try {
            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, scalingInterpolation);
            g.drawImage(image, 0, 0, targetWidth, targetHeight, (ImageObserver) null);
            return bufferedImage;
        } finally {
            g.dispose();
        }
    }

    private BufferedImage image2bufferedImage(Image image) {
        if (image instanceof BufferedImage) {
            return (BufferedImage) image;
        }
        BufferedImage bufferedImage = new BufferedImage(image.getWidth((ImageObserver) null), image.getHeight((ImageObserver) null), 2);
        Graphics2D g = bufferedImage.createGraphics();
        try {
            g.drawImage(image, 0, 0, (ImageObserver) null);
            return bufferedImage;
        } finally {
            g.dispose();
        }
    }
}
