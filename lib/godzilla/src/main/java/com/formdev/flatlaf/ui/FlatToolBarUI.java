package com.formdev.flatlaf.ui;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarUI;

public class FlatToolBarUI extends BasicToolBarUI {
    public static ComponentUI createUI(JComponent c) {
        return new FlatToolBarUI();
    }

    /* access modifiers changed from: protected */
    public ContainerListener createToolBarContListener() {
        return new BasicToolBarUI.ToolBarContListener() {
            /* class com.formdev.flatlaf.ui.FlatToolBarUI.AnonymousClass1 */

            public void componentAdded(ContainerEvent e) {
                FlatToolBarUI.super.componentAdded(e);
                Component c = e.getChild();
                if (c instanceof AbstractButton) {
                    c.setFocusable(false);
                }
            }

            public void componentRemoved(ContainerEvent e) {
                FlatToolBarUI.super.componentRemoved(e);
                Component c = e.getChild();
                if (c instanceof AbstractButton) {
                    c.setFocusable(true);
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    public void setBorderToRollover(Component c) {
    }

    /* access modifiers changed from: protected */
    public void setBorderToNonRollover(Component c) {
    }

    /* access modifiers changed from: protected */
    public void setBorderToNormal(Component c) {
    }

    /* access modifiers changed from: protected */
    public void installRolloverBorders(JComponent c) {
    }

    /* access modifiers changed from: protected */
    public void installNonRolloverBorders(JComponent c) {
    }

    /* access modifiers changed from: protected */
    public void installNormalBorders(JComponent c) {
    }

    /* access modifiers changed from: protected */
    public Border createRolloverBorder() {
        return null;
    }

    /* access modifiers changed from: protected */
    public Border createNonRolloverBorder() {
        return null;
    }

    public void setOrientation(int orientation) {
        if (orientation != this.toolBar.getOrientation()) {
            Insets margin = this.toolBar.getMargin();
            Insets newMargin = new Insets(margin.left, margin.top, margin.right, margin.bottom);
            if (!newMargin.equals(margin)) {
                this.toolBar.setMargin(newMargin);
            }
        }
        FlatToolBarUI.super.setOrientation(orientation);
    }
}
