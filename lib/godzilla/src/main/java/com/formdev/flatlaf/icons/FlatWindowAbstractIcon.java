package com.formdev.flatlaf.icons;

import com.formdev.flatlaf.ui.FlatButtonUI;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.HiDPIUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import javax.swing.UIManager;

public abstract class FlatWindowAbstractIcon extends FlatAbstractIcon {
    private final Color hoverBackground;
    private final Color pressedBackground;

    /* access modifiers changed from: protected */
    public abstract void paintIconAt1x(Graphics2D graphics2D, int i, int i2, int i3, int i4, double d);

    public FlatWindowAbstractIcon() {
        this(UIManager.getDimension("TitlePane.buttonSize"), UIManager.getColor("TitlePane.buttonHoverBackground"), UIManager.getColor("TitlePane.buttonPressedBackground"));
    }

    public FlatWindowAbstractIcon(Dimension size, Color hoverBackground2, Color pressedBackground2) {
        super(size.width, size.height, null);
        this.hoverBackground = hoverBackground2;
        this.pressedBackground = pressedBackground2;
    }

    /* access modifiers changed from: protected */
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
    @Override // com.formdev.flatlaf.icons.FlatAbstractIcon
    public void paintIcon(java.awt.Component r7, java.awt.Graphics2D r8) {
        /*
            r6 = this;
            r1 = 0
            r6.paintBackground(r7, r8)
            java.awt.Color r0 = r6.getForeground(r7)
            r8.setColor(r0)
            int r3 = r6.width
            int r4 = r6.height
            r5 = move-result
            r0 = r8
            r2 = r1
            com.formdev.flatlaf.util.HiDPIUtils.paintAtScale1x(r0, r1, r2, r3, r4, r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.icons.FlatWindowAbstractIcon.paintIcon(java.awt.Component, java.awt.Graphics2D):void");
    }

    /* access modifiers changed from: protected */
    public void paintBackground(Component c, Graphics2D g) {
        Color background = FlatButtonUI.buttonStateColor(c, null, null, null, this.hoverBackground, this.pressedBackground);
        if (background != null) {
            g.setColor(FlatUIUtils.deriveColor(background, c.getBackground()));
            g.fillRect(0, 0, this.width, this.height);
        }
    }

    /* access modifiers changed from: protected */
    public Color getForeground(Component c) {
        return c.getForeground();
    }
}
