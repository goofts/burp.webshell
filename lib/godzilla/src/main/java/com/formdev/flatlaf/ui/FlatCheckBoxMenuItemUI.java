package com.formdev.flatlaf.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicCheckBoxMenuItemUI;

public class FlatCheckBoxMenuItemUI extends BasicCheckBoxMenuItemUI {
    private FlatMenuItemRenderer renderer;

    public static ComponentUI createUI(JComponent c) {
        return new FlatCheckBoxMenuItemUI();
    }

    /* access modifiers changed from: protected */
    public void installDefaults() {
        FlatCheckBoxMenuItemUI.super.installDefaults();
        LookAndFeel.installProperty(this.menuItem, "iconTextGap", Integer.valueOf(FlatUIUtils.getUIInt("MenuItem.iconTextGap", 4)));
        this.renderer = createRenderer();
    }

    /* access modifiers changed from: protected */
    public void uninstallDefaults() {
        FlatCheckBoxMenuItemUI.super.uninstallDefaults();
        this.renderer = null;
    }

    /* access modifiers changed from: protected */
    public FlatMenuItemRenderer createRenderer() {
        return new FlatMenuItemRenderer(this.menuItem, this.checkIcon, this.arrowIcon, this.acceleratorFont, this.acceleratorDelimiter);
    }

    /* access modifiers changed from: protected */
    public Dimension getPreferredMenuItemSize(JComponent c, Icon checkIcon, Icon arrowIcon, int defaultTextIconGap) {
        return this.renderer.getPreferredMenuItemSize();
    }

    public void paint(Graphics g, JComponent c) {
        this.renderer.paintMenuItem(g, this.selectionBackground, this.selectionForeground, this.disabledForeground, this.acceleratorForeground, this.acceleratorSelectionForeground);
    }
}
