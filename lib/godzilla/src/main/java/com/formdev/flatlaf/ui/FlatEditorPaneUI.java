package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.HiDPIUtils;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import javax.swing.JComponent;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicEditorPaneUI;
import javax.swing.text.JTextComponent;

public class FlatEditorPaneUI extends BasicEditorPaneUI {
    protected boolean isIntelliJTheme;
    protected int minimumWidth;
    private Object oldHonorDisplayProperties;

    public static ComponentUI createUI(JComponent c) {
        return new FlatEditorPaneUI();
    }

    /* access modifiers changed from: protected */
    public void installDefaults() {
        FlatEditorPaneUI.super.installDefaults();
        this.minimumWidth = UIManager.getInt("Component.minimumWidth");
        this.isIntelliJTheme = UIManager.getBoolean("Component.isIntelliJTheme");
        this.oldHonorDisplayProperties = getComponent().getClientProperty("JEditorPane.honorDisplayProperties");
        getComponent().putClientProperty("JEditorPane.honorDisplayProperties", true);
    }

    /* access modifiers changed from: protected */
    public void uninstallDefaults() {
        FlatEditorPaneUI.super.uninstallDefaults();
        getComponent().putClientProperty("JEditorPane.honorDisplayProperties", this.oldHonorDisplayProperties);
    }

    /* access modifiers changed from: protected */
    public void propertyChange(PropertyChangeEvent e) {
        FlatEditorPaneUI.super.propertyChange(e);
        propertyChange(getComponent(), e);
    }

    static void propertyChange(JTextComponent c, PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();
        char c2 = 65535;
        switch (propertyName.hashCode()) {
            case -1302441837:
                if (propertyName.equals(FlatClientProperties.MINIMUM_WIDTH)) {
                    c2 = 0;
                    break;
                }
                break;
        }
        switch (c2) {
            case 0:
                c.revalidate();
                return;
            default:
                return;
        }
    }

    public Dimension getPreferredSize(JComponent c) {
        return applyMinimumWidth(c, FlatEditorPaneUI.super.getPreferredSize(c), this.minimumWidth);
    }

    public Dimension getMinimumSize(JComponent c) {
        return applyMinimumWidth(c, FlatEditorPaneUI.super.getMinimumSize(c), this.minimumWidth);
    }

    static Dimension applyMinimumWidth(JComponent c, Dimension size, int minimumWidth2) {
        size.width = Math.max(size.width, UIScale.scale(FlatUIUtils.minimumWidth(c, minimumWidth2)) - (UIScale.scale(1) * 2));
        return size;
    }

    /* access modifiers changed from: protected */
    public void paintSafely(Graphics g) {
        FlatEditorPaneUI.super.paintSafely(HiDPIUtils.createGraphicsTextYCorrection((Graphics2D) g));
    }

    /* access modifiers changed from: protected */
    public void paintBackground(Graphics g) {
        JTextComponent c = getComponent();
        if (!this.isIntelliJTheme || ((c.isEnabled() && c.isEditable()) || !(c.getBackground() instanceof UIResource))) {
            FlatEditorPaneUI.super.paintBackground(g);
        } else {
            FlatUIUtils.paintParentBackground(g, c);
        }
    }
}
