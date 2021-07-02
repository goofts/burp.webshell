package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.HiDPIUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.beans.PropertyChangeEvent;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTextAreaUI;
import javax.swing.text.JTextComponent;

public class FlatTextAreaUI extends BasicTextAreaUI {
    protected Color background;
    protected Color disabledBackground;
    protected Color inactiveBackground;
    protected boolean isIntelliJTheme;
    protected int minimumWidth;

    public static ComponentUI createUI(JComponent c) {
        return new FlatTextAreaUI();
    }

    public void installUI(JComponent c) {
        FlatTextAreaUI.super.installUI(c);
        updateBackground();
    }

    /* access modifiers changed from: protected */
    public void installDefaults() {
        FlatTextAreaUI.super.installDefaults();
        this.minimumWidth = UIManager.getInt("Component.minimumWidth");
        this.isIntelliJTheme = UIManager.getBoolean("Component.isIntelliJTheme");
        this.background = UIManager.getColor("TextArea.background");
        this.disabledBackground = UIManager.getColor("TextArea.disabledBackground");
        this.inactiveBackground = UIManager.getColor("TextArea.inactiveBackground");
    }

    /* access modifiers changed from: protected */
    public void uninstallDefaults() {
        FlatTextAreaUI.super.uninstallDefaults();
        this.background = null;
        this.disabledBackground = null;
        this.inactiveBackground = null;
    }

    /* access modifiers changed from: protected */
    public void propertyChange(PropertyChangeEvent e) {
        FlatTextAreaUI.super.propertyChange(e);
        FlatEditorPaneUI.propertyChange(getComponent(), e);
        String propertyName = e.getPropertyName();
        char c = 65535;
        switch (propertyName.hashCode()) {
            case -1609594047:
                if (propertyName.equals("enabled")) {
                    c = 1;
                    break;
                }
                break;
            case 1602416228:
                if (propertyName.equals("editable")) {
                    c = 0;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
                updateBackground();
                return;
            default:
                return;
        }
    }

    private void updateBackground() {
        Color newBackground;
        JTextComponent c = getComponent();
        Color background2 = c.getBackground();
        if (background2 instanceof UIResource) {
            if (background2 == this.background || background2 == this.disabledBackground || background2 == this.inactiveBackground) {
                if (!c.isEnabled()) {
                    newBackground = this.disabledBackground;
                } else {
                    newBackground = !c.isEditable() ? this.inactiveBackground : this.background;
                }
                if (newBackground != background2) {
                    c.setBackground(newBackground);
                }
            }
        }
    }

    public Dimension getPreferredSize(JComponent c) {
        return applyMinimumWidth(c, FlatTextAreaUI.super.getPreferredSize(c));
    }

    public Dimension getMinimumSize(JComponent c) {
        return applyMinimumWidth(c, FlatTextAreaUI.super.getMinimumSize(c));
    }

    private Dimension applyMinimumWidth(JComponent c, Dimension size) {
        return (!(c instanceof JTextArea) || ((JTextArea) c).getColumns() <= 0) ? FlatEditorPaneUI.applyMinimumWidth(c, size, this.minimumWidth) : size;
    }

    /* access modifiers changed from: protected */
    public void paintSafely(Graphics g) {
        FlatTextAreaUI.super.paintSafely(HiDPIUtils.createGraphicsTextYCorrection((Graphics2D) g));
    }

    /* access modifiers changed from: protected */
    public void paintBackground(Graphics g) {
        JTextComponent c = getComponent();
        if (!this.isIntelliJTheme || ((c.isEnabled() && c.isEditable()) || !(c.getBackground() instanceof UIResource))) {
            FlatTextAreaUI.super.paintBackground(g);
        } else {
            FlatUIUtils.paintParentBackground(g, c);
        }
    }
}
