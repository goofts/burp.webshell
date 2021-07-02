package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.ui.FlatWindowResizer;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicInternalFrameUI;

public class FlatInternalFrameUI extends BasicInternalFrameUI {
    protected FlatWindowResizer windowResizer;

    public static ComponentUI createUI(JComponent c) {
        return new FlatInternalFrameUI((JInternalFrame) c);
    }

    public FlatInternalFrameUI(JInternalFrame b) {
        super(b);
    }

    public void installUI(JComponent c) {
        FlatInternalFrameUI.super.installUI(c);
        LookAndFeel.installProperty(this.frame, "opaque", false);
        this.windowResizer = createWindowResizer();
    }

    public void uninstallUI(JComponent c) {
        FlatInternalFrameUI.super.uninstallUI(c);
        if (this.windowResizer != null) {
            this.windowResizer.uninstall();
            this.windowResizer = null;
        }
    }

    /* access modifiers changed from: protected */
    public JComponent createNorthPane(JInternalFrame w) {
        return new FlatInternalFrameTitlePane(w);
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
        	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:663)
        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:363)
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
    public com.formdev.flatlaf.ui.FlatWindowResizer createWindowResizer() {
        /*
            r3 = this;
            com.formdev.flatlaf.ui.FlatWindowResizer$InternalFrameResizer r0 = new com.formdev.flatlaf.ui.FlatWindowResizer$InternalFrameResizer
            javax.swing.JInternalFrame r1 = r3.frame
            r2 = move-result
            r0.<init>(r1, r2)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatInternalFrameUI.createWindowResizer():com.formdev.flatlaf.ui.FlatWindowResizer");
    }

    public static class FlatInternalFrameBorder extends FlatEmptyBorder {
        private final Color activeBorderColor = UIManager.getColor("InternalFrame.activeBorderColor");
        private final FlatDropShadowBorder activeDropShadowBorder = new FlatDropShadowBorder(UIManager.getColor("InternalFrame.activeDropShadowColor"), UIManager.getInsets("InternalFrame.activeDropShadowInsets"), FlatUIUtils.getUIFloat("InternalFrame.activeDropShadowOpacity", 0.5f));
        private final int borderLineWidth = FlatUIUtils.getUIInt("InternalFrame.borderLineWidth", 1);
        private final boolean dropShadowPainted = UIManager.getBoolean("InternalFrame.dropShadowPainted");
        private final Color inactiveBorderColor = UIManager.getColor("InternalFrame.inactiveBorderColor");
        private final FlatDropShadowBorder inactiveDropShadowBorder = new FlatDropShadowBorder(UIManager.getColor("InternalFrame.inactiveDropShadowColor"), UIManager.getInsets("InternalFrame.inactiveDropShadowInsets"), FlatUIUtils.getUIFloat("InternalFrame.inactiveDropShadowOpacity", 0.5f));

        public FlatInternalFrameBorder() {
            super(UIManager.getInsets("InternalFrame.borderMargins"));
        }

        @Override // com.formdev.flatlaf.ui.FlatEmptyBorder
        public Insets getBorderInsets(Component c, Insets insets) {
            if (!(c instanceof JInternalFrame) || !((JInternalFrame) c).isMaximum()) {
                return super.getBorderInsets(c, insets);
            }
            insets.left = UIScale.scale(Math.min(this.borderLineWidth, this.left));
            insets.top = UIScale.scale(Math.min(this.borderLineWidth, this.top));
            insets.right = UIScale.scale(Math.min(this.borderLineWidth, this.right));
            insets.bottom = UIScale.scale(Math.min(this.borderLineWidth, this.bottom));
            return insets;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            JInternalFrame f = (JInternalFrame) c;
            Insets insets = getBorderInsets(c);
            float lineWidth = UIScale.scale((float) this.borderLineWidth);
            float rx = ((float) (insets.left + x)) - lineWidth;
            float ry = ((float) (insets.top + y)) - lineWidth;
            float rwidth = ((float) ((width - insets.left) - insets.right)) + (2.0f * lineWidth);
            float rheight = ((float) ((height - insets.top) - insets.bottom)) + (2.0f * lineWidth);
            Graphics2D g2 = g.create();
            try {
                FlatUIUtils.setRenderingHints(g2);
                g2.setColor(f.isSelected() ? this.activeBorderColor : this.inactiveBorderColor);
                if (this.dropShadowPainted) {
                    FlatDropShadowBorder dropShadowBorder = f.isSelected() ? this.activeDropShadowBorder : this.inactiveDropShadowBorder;
                    Insets dropShadowInsets = dropShadowBorder.getBorderInsets();
                    dropShadowBorder.paintBorder(c, g2, ((int) rx) - dropShadowInsets.left, ((int) ry) - dropShadowInsets.top, dropShadowInsets.right + ((int) rwidth) + dropShadowInsets.left, dropShadowInsets.bottom + ((int) rheight) + dropShadowInsets.top);
                }
                g2.fill(FlatUIUtils.createRectangle(rx, ry, rwidth, rheight, lineWidth));
            } finally {
                g2.dispose();
            }
        }
    }
}
