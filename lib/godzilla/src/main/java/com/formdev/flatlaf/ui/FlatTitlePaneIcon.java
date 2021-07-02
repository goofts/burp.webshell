package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.ScaledImageIcon;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.ImageObserver;
import java.util.List;
import javax.swing.ImageIcon;

public class FlatTitlePaneIcon extends ScaledImageIcon {
    private final List<Image> images;

    /*  JADX ERROR: JadxRuntimeException in pass: BlockSplitter
        jadx.core.utils.exceptions.JadxRuntimeException: Missing block: 39
        	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.getBlock(BlockSplitter.java:307)
        	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.setupConnections(BlockSplitter.java:236)
        	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.splitBasicBlocks(BlockSplitter.java:129)
        	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.visit(BlockSplitter.java:52)
        */
    public static javax.swing.Icon create(java.util.List<java.awt.Image> r4, java.awt.Dimension r5) {
        /*
            java.util.ArrayList r0 = new java.util.ArrayList
            r0.<init>()
            java.util.Iterator r2 = r4.iterator()
        L_0x0009:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0027
            java.lang.Object r1 = r2.next()
            java.awt.Image r1 = (java.awt.Image) r1
            boolean r3 = com.formdev.flatlaf.util.MultiResolutionImageSupport.isMultiResolutionImage(r1)
            if (r3 == 0) goto L_0x0023
            java.util.List r3 = com.formdev.flatlaf.util.MultiResolutionImageSupport.getResolutionVariants(r1)
            r0.addAll(r3)
            goto L_0x0009
        L_0x0023:
            r0.add(r1)
            goto L_0x0009
        L_?:
            r2 = move-result
            r0.sort(r2)
            com.formdev.flatlaf.ui.FlatTitlePaneIcon r2 = new com.formdev.flatlaf.ui.FlatTitlePaneIcon
            r2.<init>(r0, r5)
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatTitlePaneIcon.create(java.util.List, java.awt.Dimension):javax.swing.Icon");
    }

    private static /* synthetic */ int lambda$create$0(Image image1, Image image2) {
        return image1.getWidth((ImageObserver) null) - image2.getWidth((ImageObserver) null);
    }

    private FlatTitlePaneIcon(List<Image> images2, Dimension size) {
        super(new ImageIcon(images2.get(0)), size.width, size.height);
        this.images = images2;
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.util.ScaledImageIcon
    public Image getResolutionVariant(int destImageWidth, int destImageHeight) {
        for (Image image : this.images) {
            if (destImageWidth <= image.getWidth((ImageObserver) null) && destImageHeight <= image.getHeight((ImageObserver) null)) {
                return image;
            }
        }
        return this.images.get(this.images.size() - 1);
    }
}
