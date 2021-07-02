package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.UIScale;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.JSeparator;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSeparatorUI;

public class FlatSeparatorUI extends BasicSeparatorUI {
    private boolean defaults_initialized = false;
    protected int height;
    protected int stripeIndent;
    protected int stripeWidth;

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
            java.lang.Class<com.formdev.flatlaf.ui.FlatSeparatorUI> r0 = com.formdev.flatlaf.ui.FlatSeparatorUI.class
            r1 = move-result
            javax.swing.plaf.ComponentUI r0 = com.formdev.flatlaf.ui.FlatUIUtils.createSharedUI(r0, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatSeparatorUI.createUI(javax.swing.JComponent):javax.swing.plaf.ComponentUI");
    }

    /* access modifiers changed from: protected */
    public void installDefaults(JSeparator s) {
        FlatSeparatorUI.super.installDefaults(s);
        if (!this.defaults_initialized) {
            String prefix = getPropertyPrefix();
            this.height = UIManager.getInt(prefix + ".height");
            this.stripeWidth = UIManager.getInt(prefix + ".stripeWidth");
            this.stripeIndent = UIManager.getInt(prefix + ".stripeIndent");
            this.defaults_initialized = true;
        }
    }

    /* access modifiers changed from: protected */
    public void uninstallDefaults(JSeparator s) {
        FlatSeparatorUI.super.uninstallDefaults(s);
        this.defaults_initialized = false;
    }

    /* access modifiers changed from: protected */
    public String getPropertyPrefix() {
        return "Separator";
    }

    public void paint(Graphics g, JComponent c) {
        Graphics2D g2 = g.create();
        try {
            FlatUIUtils.setRenderingHints(g2);
            g2.setColor(c.getForeground());
            float width = UIScale.scale((float) this.stripeWidth);
            float indent = UIScale.scale((float) this.stripeIndent);
            if (((JSeparator) c).getOrientation() == 1) {
                g2.fill(new Rectangle2D.Float(indent, 0.0f, width, (float) c.getHeight()));
            } else {
                g2.fill(new Rectangle2D.Float(0.0f, indent, (float) c.getWidth(), width));
            }
        } finally {
            g2.dispose();
        }
    }

    public Dimension getPreferredSize(JComponent c) {
        if (((JSeparator) c).getOrientation() == 1) {
            return new Dimension(UIScale.scale(this.height), 0);
        }
        return new Dimension(0, UIScale.scale(this.height));
    }
}
