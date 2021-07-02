package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.util.HiDPIUtils;
import com.formdev.flatlaf.util.UIScale;
import com.kitfox.svg.Font;
import com.kitfox.svg.Text;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.beans.PropertyChangeEvent;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicLabelUI;

public class FlatLabelUI extends BasicLabelUI {
    private boolean defaults_initialized = false;
    private Color disabledForeground;

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
            java.lang.Class<com.formdev.flatlaf.ui.FlatLabelUI> r0 = com.formdev.flatlaf.ui.FlatLabelUI.class
            r1 = move-result
            javax.swing.plaf.ComponentUI r0 = com.formdev.flatlaf.ui.FlatUIUtils.createSharedUI(r0, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatLabelUI.createUI(javax.swing.JComponent):javax.swing.plaf.ComponentUI");
    }

    /* access modifiers changed from: protected */
    public void installDefaults(JLabel c) {
        FlatLabelUI.super.installDefaults(c);
        if (!this.defaults_initialized) {
            this.disabledForeground = UIManager.getColor("Label.disabledForeground");
            this.defaults_initialized = true;
        }
    }

    /* access modifiers changed from: protected */
    public void uninstallDefaults(JLabel c) {
        FlatLabelUI.super.uninstallDefaults(c);
        this.defaults_initialized = false;
    }

    /* access modifiers changed from: protected */
    public void installComponents(JLabel c) {
        FlatLabelUI.super.installComponents(c);
        updateHTMLRenderer(c, c.getText(), false);
    }

    public void propertyChange(PropertyChangeEvent e) {
        String name = e.getPropertyName();
        if (name == Text.TAG_NAME || name == Font.TAG_NAME || name == "foreground") {
            JLabel label = (JLabel) e.getSource();
            updateHTMLRenderer(label, label.getText(), true);
            return;
        }
        FlatLabelUI.super.propertyChange(e);
    }

    static void updateHTMLRenderer(JComponent c, String text, boolean always) {
        if (BasicHTML.isHTMLString(text) && c.getClientProperty("html.disable") != Boolean.TRUE && text.contains("<h") && (text.contains("<h1") || text.contains("<h2") || text.contains("<h3") || text.contains("<h4") || text.contains("<h5") || text.contains("<h6"))) {
            int headIndex = text.indexOf("<head>");
            String style = "<style>BASE_SIZE " + c.getFont().getSize() + "</style>";
            if (headIndex < 0) {
                style = "<head>" + style + "</head>";
            }
            int insertIndex = headIndex >= 0 ? headIndex + "<head>".length() : "<html>".length();
            text = text.substring(0, insertIndex) + style + text.substring(insertIndex);
        } else if (!always) {
            return;
        }
        BasicHTML.updateRenderer(c, text);
    }

    static Graphics createGraphicsHTMLTextYCorrection(Graphics g, JComponent c) {
        if (c.getClientProperty("html") != null) {
            return HiDPIUtils.createGraphicsTextYCorrection((Graphics2D) g);
        }
        return g;
    }

    public void paint(Graphics g, JComponent c) {
        FlatLabelUI.super.paint(createGraphicsHTMLTextYCorrection(g, c), c);
    }

    /* access modifiers changed from: protected */
    public void paintEnabledText(JLabel l, Graphics g, String s, int textX, int textY) {
        int mnemIndex = FlatLaf.isShowMnemonics() ? l.getDisplayedMnemonicIndex() : -1;
        g.setColor(l.getForeground());
        FlatUIUtils.drawStringUnderlineCharAt(l, g, s, mnemIndex, textX, textY);
    }

    /* access modifiers changed from: protected */
    public void paintDisabledText(JLabel l, Graphics g, String s, int textX, int textY) {
        int mnemIndex = FlatLaf.isShowMnemonics() ? l.getDisplayedMnemonicIndex() : -1;
        g.setColor(this.disabledForeground);
        FlatUIUtils.drawStringUnderlineCharAt(l, g, s, mnemIndex, textX, textY);
    }

    /* access modifiers changed from: protected */
    public String layoutCL(JLabel label, FontMetrics fontMetrics, String text, Icon icon, Rectangle viewR, Rectangle iconR, Rectangle textR) {
        return SwingUtilities.layoutCompoundLabel(label, fontMetrics, text, icon, label.getVerticalAlignment(), label.getHorizontalAlignment(), label.getVerticalTextPosition(), label.getHorizontalTextPosition(), viewR, iconR, textR, UIScale.scale(label.getIconTextGap()));
    }
}
