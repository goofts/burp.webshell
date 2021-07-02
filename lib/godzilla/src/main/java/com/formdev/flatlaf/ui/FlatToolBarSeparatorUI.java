package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarSeparatorUI;

public class FlatToolBarSeparatorUI extends BasicToolBarSeparatorUI {
    private static final int LINE_WIDTH = 1;
    private boolean defaults_initialized = false;
    protected Color separatorColor;
    protected int separatorWidth;

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
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:230)
        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:119)
        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:103)
        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:313)
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
    public static javax.swing.plaf.ComponentUI createUI(javax.swing.JComponent r2) {
        /*
            java.lang.Class<com.formdev.flatlaf.ui.FlatToolBarSeparatorUI> r0 = com.formdev.flatlaf.ui.FlatToolBarSeparatorUI.class
            r1 = move-result
            javax.swing.plaf.ComponentUI r0 = com.formdev.flatlaf.ui.FlatUIUtils.createSharedUI(r0, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatToolBarSeparatorUI.createUI(javax.swing.JComponent):javax.swing.plaf.ComponentUI");
    }

    /* access modifiers changed from: protected */
    public void installDefaults(JSeparator c) {
        FlatToolBarSeparatorUI.super.installDefaults(c);
        if (!this.defaults_initialized) {
            this.separatorWidth = UIManager.getInt("ToolBar.separatorWidth");
            this.separatorColor = UIManager.getColor("ToolBar.separatorColor");
            this.defaults_initialized = true;
        }
        c.setAlignmentX(0.0f);
    }

    /* access modifiers changed from: protected */
    public void uninstallDefaults(JSeparator s) {
        FlatToolBarSeparatorUI.super.uninstallDefaults(s);
        this.defaults_initialized = false;
    }

    public Dimension getPreferredSize(JComponent c) {
        Dimension size = ((JToolBar.Separator) c).getSeparatorSize();
        if (size != null) {
            return UIScale.scale(size);
        }
        int sepWidth = (UIScale.scale((this.separatorWidth - 1) / 2) * 2) + UIScale.scale(1);
        boolean vertical = isVertical(c);
        int i = vertical ? sepWidth : 0;
        if (vertical) {
            sepWidth = 0;
        }
        return new Dimension(i, sepWidth);
    }

    public Dimension getMaximumSize(JComponent c) {
        Dimension size = getPreferredSize(c);
        if (isVertical(c)) {
            return new Dimension(size.width, 32767);
        }
        return new Dimension(32767, size.height);
    }

    public void paint(Graphics g, JComponent c) {
        int width = c.getWidth();
        int height = c.getHeight();
        float lineWidth = UIScale.scale(1.0f);
        float offset = UIScale.scale(2.0f);
        Object[] oldRenderingHints = FlatUIUtils.setRenderingHints(g);
        g.setColor(this.separatorColor);
        if (isVertical(c)) {
            ((Graphics2D) g).fill(new Rectangle2D.Float((float) Math.round((((float) width) - lineWidth) / 2.0f), offset, lineWidth, ((float) height) - (2.0f * offset)));
        } else {
            ((Graphics2D) g).fill(new Rectangle2D.Float(offset, (float) Math.round((((float) height) - lineWidth) / 2.0f), ((float) width) - (2.0f * offset), lineWidth));
        }
        FlatUIUtils.resetRenderingHints(g, oldRenderingHints);
    }

    private boolean isVertical(JComponent c) {
        return ((JToolBar.Separator) c).getOrientation() == 1;
    }
}
