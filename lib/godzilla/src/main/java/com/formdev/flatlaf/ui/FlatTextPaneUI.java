package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.HiDPIUtils;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTextPaneUI;
import javax.swing.text.JTextComponent;

public class FlatTextPaneUI extends BasicTextPaneUI {
    protected boolean isIntelliJTheme;
    protected int minimumWidth;
    private Object oldHonorDisplayProperties;

    public static ComponentUI createUI(JComponent c) {
        return new FlatTextPaneUI();
    }

    /* access modifiers changed from: protected */
    public void installDefaults() {
        FlatTextPaneUI.super.installDefaults();
        this.minimumWidth = UIManager.getInt("Component.minimumWidth");
        this.isIntelliJTheme = UIManager.getBoolean("Component.isIntelliJTheme");
        this.oldHonorDisplayProperties = getComponent().getClientProperty("JEditorPane.honorDisplayProperties");
        getComponent().putClientProperty("JEditorPane.honorDisplayProperties", true);
    }

    /* access modifiers changed from: protected */
    public void uninstallDefaults() {
        FlatTextPaneUI.super.uninstallDefaults();
        getComponent().putClientProperty("JEditorPane.honorDisplayProperties", this.oldHonorDisplayProperties);
    }

    /* access modifiers changed from: protected */
    public void propertyChange(PropertyChangeEvent e) {
        FlatTextPaneUI.super.propertyChange(e);
        FlatEditorPaneUI.propertyChange(getComponent(), e);
    }

    public Dimension getPreferredSize(JComponent c) {
        return FlatEditorPaneUI.applyMinimumWidth(c, FlatTextPaneUI.super.getPreferredSize(c), this.minimumWidth);
    }

    public Dimension getMinimumSize(JComponent c) {
        return FlatEditorPaneUI.applyMinimumWidth(c, FlatTextPaneUI.super.getMinimumSize(c), this.minimumWidth);
    }

    /* access modifiers changed from: protected */
    public void paintSafely(Graphics g) {
        FlatTextPaneUI.super.paintSafely(HiDPIUtils.createGraphicsTextYCorrection((Graphics2D) g));
    }

    /* access modifiers changed from: protected */
    public void paintBackground(Graphics g) {
        JTextComponent c = getComponent();
        if (!this.isIntelliJTheme || ((c.isEnabled() && c.isEditable()) || !(c.getBackground() instanceof UIResource))) {
            FlatTextPaneUI.super.paintBackground(g);
        } else {
            FlatUIUtils.paintParentBackground(g, c);
        }
    }
}
