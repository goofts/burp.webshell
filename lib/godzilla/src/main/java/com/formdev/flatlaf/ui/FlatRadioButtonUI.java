package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.icons.FlatCheckBoxIcon;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicRadioButtonUI;

public class FlatRadioButtonUI extends BasicRadioButtonUI {
    private static Insets tempInsets = new Insets(0, 0, 0, 0);
    private Color defaultBackground;
    private boolean defaults_initialized = false;
    protected Color disabledText;
    protected int iconTextGap;

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
            java.lang.Class<com.formdev.flatlaf.ui.FlatRadioButtonUI> r0 = com.formdev.flatlaf.ui.FlatRadioButtonUI.class
            r1 = move-result
            javax.swing.plaf.ComponentUI r0 = com.formdev.flatlaf.ui.FlatUIUtils.createSharedUI(r0, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatRadioButtonUI.createUI(javax.swing.JComponent):javax.swing.plaf.ComponentUI");
    }

    public void installDefaults(AbstractButton b) {
        FlatRadioButtonUI.super.installDefaults(b);
        if (!this.defaults_initialized) {
            String prefix = getPropertyPrefix();
            this.iconTextGap = FlatUIUtils.getUIInt(prefix + "iconTextGap", 4);
            this.disabledText = UIManager.getColor(prefix + "disabledText");
            this.defaultBackground = UIManager.getColor(prefix + "background");
            this.defaults_initialized = true;
        }
        LookAndFeel.installProperty(b, "opaque", false);
        LookAndFeel.installProperty(b, "iconTextGap", Integer.valueOf(UIScale.scale(this.iconTextGap)));
        MigLayoutVisualPadding.install(b, null);
    }

    /* access modifiers changed from: protected */
    public void uninstallDefaults(AbstractButton b) {
        FlatRadioButtonUI.super.uninstallDefaults(b);
        MigLayoutVisualPadding.uninstall(b);
        this.defaults_initialized = false;
    }

    public Dimension getPreferredSize(JComponent c) {
        Dimension size = FlatRadioButtonUI.super.getPreferredSize(c);
        if (size == null) {
            return null;
        }
        int focusWidth = getIconFocusWidth(c);
        if (focusWidth <= 0) {
            return size;
        }
        Insets insets = c.getInsets(tempInsets);
        size.width += Math.max(focusWidth - insets.left, 0) + Math.max(focusWidth - insets.right, 0);
        size.height += Math.max(focusWidth - insets.top, 0) + Math.max(focusWidth - insets.bottom, 0);
        return size;
    }

    public void paint(Graphics g, JComponent c) {
        if (!c.isOpaque() && ((AbstractButton) c).isContentAreaFilled() && c.getBackground() != this.defaultBackground) {
            g.setColor(c.getBackground());
            g.fillRect(0, 0, c.getWidth(), c.getHeight());
        }
        int focusWidth = getIconFocusWidth(c);
        if (focusWidth > 0) {
            boolean ltr = c.getComponentOrientation().isLeftToRight();
            Insets insets = c.getInsets(tempInsets);
            int leftOrRightInset = ltr ? insets.left : insets.right;
            if (focusWidth > leftOrRightInset) {
                int offset = focusWidth - leftOrRightInset;
                if (!ltr) {
                    offset = -offset;
                }
                g.translate(offset, 0);
                FlatRadioButtonUI.super.paint(g, c);
                g.translate(-offset, 0);
                return;
            }
        }
        FlatRadioButtonUI.super.paint(FlatLabelUI.createGraphicsHTMLTextYCorrection(g, c), c);
    }

    /* access modifiers changed from: protected */
    public void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
        FlatButtonUI.paintText(g, b, textRect, text, b.isEnabled() ? b.getForeground() : this.disabledText);
    }

    private int getIconFocusWidth(JComponent c) {
        if (((AbstractButton) c).getIcon() != null || !(getDefaultIcon() instanceof FlatCheckBoxIcon)) {
            return 0;
        }
        return UIScale.scale(getDefaultIcon().focusWidth);
    }
}
