package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.beans.PropertyChangeEvent;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;

public class FlatToggleButtonUI extends FlatButtonUI {
    private boolean defaults_initialized = false;
    protected Color tabDisabledUnderlineColor;
    protected Color tabFocusBackground;
    protected Color tabHoverBackground;
    protected Color tabSelectedBackground;
    protected Color tabUnderlineColor;
    protected int tabUnderlineHeight;

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
            java.lang.Class<com.formdev.flatlaf.ui.FlatToggleButtonUI> r0 = com.formdev.flatlaf.ui.FlatToggleButtonUI.class
            r1 = move-result
            javax.swing.plaf.ComponentUI r0 = com.formdev.flatlaf.ui.FlatUIUtils.createSharedUI(r0, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatToggleButtonUI.createUI(javax.swing.JComponent):javax.swing.plaf.ComponentUI");
    }

    /* access modifiers changed from: protected */
    public String getPropertyPrefix() {
        return "ToggleButton.";
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.ui.FlatButtonUI
    public void installDefaults(AbstractButton b) {
        super.installDefaults(b);
        if (!this.defaults_initialized) {
            this.tabUnderlineHeight = UIManager.getInt("ToggleButton.tab.underlineHeight");
            this.tabUnderlineColor = UIManager.getColor("ToggleButton.tab.underlineColor");
            this.tabDisabledUnderlineColor = UIManager.getColor("ToggleButton.tab.disabledUnderlineColor");
            this.tabSelectedBackground = UIManager.getColor("ToggleButton.tab.selectedBackground");
            this.tabHoverBackground = UIManager.getColor("ToggleButton.tab.hoverBackground");
            this.tabFocusBackground = UIManager.getColor("ToggleButton.tab.focusBackground");
            this.defaults_initialized = true;
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.ui.FlatButtonUI
    public void uninstallDefaults(AbstractButton b) {
        super.uninstallDefaults(b);
        this.defaults_initialized = false;
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.ui.FlatButtonUI
    public void propertyChange(AbstractButton b, PropertyChangeEvent e) {
        super.propertyChange(b, e);
        String propertyName = e.getPropertyName();
        char c = 65535;
        switch (propertyName.hashCode()) {
            case -1405676274:
                if (propertyName.equals(FlatClientProperties.TAB_BUTTON_UNDERLINE_COLOR)) {
                    c = 2;
                    break;
                }
                break;
            case -1336690752:
                if (propertyName.equals(FlatClientProperties.TAB_BUTTON_SELECTED_BACKGROUND)) {
                    c = 3;
                    break;
                }
                break;
            case -492478244:
                if (propertyName.equals(FlatClientProperties.TAB_BUTTON_UNDERLINE_HEIGHT)) {
                    c = 1;
                    break;
                }
                break;
            case 1428734622:
                if (propertyName.equals(FlatClientProperties.BUTTON_TYPE)) {
                    c = 0;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                if (FlatClientProperties.BUTTON_TYPE_TAB.equals(e.getOldValue()) || FlatClientProperties.BUTTON_TYPE_TAB.equals(e.getNewValue())) {
                    MigLayoutVisualPadding.uninstall(b);
                    MigLayoutVisualPadding.install(b);
                    b.revalidate();
                }
                b.repaint();
                return;
            case 1:
            case 2:
            case 3:
                b.repaint();
                return;
            default:
                return;
        }
    }

    static boolean isTabButton(Component c) {
        return (c instanceof JToggleButton) && FlatClientProperties.clientPropertyEquals((JToggleButton) c, FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TAB);
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.ui.FlatButtonUI
    public void paintBackground(Graphics g, JComponent c) {
        Color enabledColor;
        if (isTabButton(c)) {
            int height = c.getHeight();
            int width = c.getWidth();
            boolean selected = ((AbstractButton) c).isSelected();
            if (selected) {
                enabledColor = FlatClientProperties.clientPropertyColor(c, FlatClientProperties.TAB_BUTTON_SELECTED_BACKGROUND, this.tabSelectedBackground);
            } else {
                enabledColor = null;
            }
            if (enabledColor == null) {
                Color bg = c.getBackground();
                if (isCustomBackground(bg)) {
                    enabledColor = bg;
                }
            }
            Color background = buttonStateColor(c, enabledColor, null, this.tabFocusBackground, this.tabHoverBackground, null);
            if (background != null) {
                g.setColor(background);
                g.fillRect(0, 0, width, height);
            }
            if (selected) {
                int underlineHeight = UIScale.scale(FlatClientProperties.clientPropertyInt(c, FlatClientProperties.TAB_BUTTON_UNDERLINE_HEIGHT, this.tabUnderlineHeight));
                g.setColor(c.isEnabled() ? FlatClientProperties.clientPropertyColor(c, FlatClientProperties.TAB_BUTTON_UNDERLINE_COLOR, this.tabUnderlineColor) : this.tabDisabledUnderlineColor);
                g.fillRect(0, height - underlineHeight, width, underlineHeight);
                return;
            }
            return;
        }
        super.paintBackground(g, c);
    }
}
