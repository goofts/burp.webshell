package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.Paint;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.basic.BasicBorders;
import javax.swing.text.JTextComponent;

public class FlatBorder extends BasicBorders.MarginBorder {
    protected final Color borderColor = UIManager.getColor("Component.borderColor");
    protected final Color customBorderColor = UIManager.getColor("Component.custom.borderColor");
    protected final Color disabledBorderColor = UIManager.getColor("Component.disabledBorderColor");
    protected final Color errorBorderColor = UIManager.getColor("Component.error.borderColor");
    protected final Color errorFocusedBorderColor = UIManager.getColor("Component.error.focusedBorderColor");
    protected final Color focusColor = UIManager.getColor("Component.focusColor");
    protected final int focusWidth = UIManager.getInt("Component.focusWidth");
    protected final Color focusedBorderColor = UIManager.getColor("Component.focusedBorderColor");
    protected final float innerFocusWidth = FlatUIUtils.getUIFloat("Component.innerFocusWidth", 0.0f);
    protected final float innerOutlineWidth = FlatUIUtils.getUIFloat("Component.innerOutlineWidth", 0.0f);
    protected final Color warningBorderColor = UIManager.getColor("Component.warning.borderColor");
    protected final Color warningFocusedBorderColor = UIManager.getColor("Component.warning.focusedBorderColor");

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = g.create();
        try {
            FlatUIUtils.setRenderingHints(g2);
            float focusWidth2 = UIScale.scale((float) getFocusWidth(c));
            float borderWidth = UIScale.scale((float) getBorderWidth(c));
            float arc = UIScale.scale((float) getArc(c));
            Color outlineColor = getOutlineColor(c);
            if (outlineColor != null || isFocused(c)) {
                float innerWidth = (isCellEditor(c) || (c instanceof JScrollPane)) ? 0.0f : outlineColor != null ? this.innerOutlineWidth : this.innerFocusWidth;
                g2.setColor(outlineColor != null ? outlineColor : getFocusColor(c));
                FlatUIUtils.paintComponentOuterBorder(g2, x, y, width, height, focusWidth2, borderWidth + UIScale.scale(innerWidth), arc);
            }
            if (outlineColor == null) {
                outlineColor = getBorderColor(c);
            }
            g2.setPaint(outlineColor);
            FlatUIUtils.paintComponentBorder(g2, x, y, width, height, focusWidth2, borderWidth, arc);
        } finally {
            g2.dispose();
        }
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x002c, code lost:
        if (r1.equals(com.formdev.flatlaf.FlatClientProperties.OUTLINE_ERROR) != false) goto L_0x0021;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.awt.Color getOutlineColor(java.awt.Component r7) {
        /*
        // Method dump skipped, instructions count: 158
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatBorder.getOutlineColor(java.awt.Component):java.awt.Color");
    }

    /* access modifiers changed from: protected */
    public Color getFocusColor(Component c) {
        return this.focusColor;
    }

    /* access modifiers changed from: protected */
    public Paint getBorderColor(Component c) {
        if (isEnabled(c)) {
            return isFocused(c) ? this.focusedBorderColor : this.borderColor;
        }
        return this.disabledBorderColor;
    }

    /* access modifiers changed from: protected */
    public boolean isEnabled(Component c) {
        if (c instanceof JScrollPane) {
            JViewport viewport = ((JScrollPane) c).getViewport();
            Component view = viewport != null ? viewport.getView() : null;
            if (view != null && !isEnabled(view)) {
                return false;
            }
        }
        return c.isEnabled() && (!(c instanceof JTextComponent) || ((JTextComponent) c).isEditable());
    }

    /* access modifiers changed from: protected */
    public boolean isFocused(Component c) {
        JTextField textField;
        Component focusOwner;
        if (c instanceof JScrollPane) {
            JViewport viewport = ((JScrollPane) c).getViewport();
            Component view = viewport != null ? viewport.getView() : null;
            if (view == null) {
                return false;
            }
            if (FlatUIUtils.isPermanentFocusOwner(view)) {
                return true;
            }
            if (((!(view instanceof JTable) || !((JTable) view).isEditing()) && (!(view instanceof JTree) || !((JTree) view).isEditing())) || (focusOwner = KeyboardFocusManager.getCurrentKeyboardFocusManager().getFocusOwner()) == null) {
                return false;
            }
            return SwingUtilities.isDescendingFrom(focusOwner, view);
        } else if ((c instanceof JComboBox) && ((JComboBox) c).isEditable()) {
            Component editorComponent = ((JComboBox) c).getEditor().getEditorComponent();
            return editorComponent != null ? FlatUIUtils.isPermanentFocusOwner(editorComponent) : false;
        } else if (!(c instanceof JSpinner)) {
            return FlatUIUtils.isPermanentFocusOwner(c);
        } else {
            if (FlatUIUtils.isPermanentFocusOwner(c)) {
                return true;
            }
            JComponent editor = ((JSpinner) c).getEditor();
            if (!(editor instanceof JSpinner.DefaultEditor) || (textField = ((JSpinner.DefaultEditor) editor).getTextField()) == null) {
                return false;
            }
            return FlatUIUtils.isPermanentFocusOwner(textField);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isCellEditor(Component c) {
        return FlatUIUtils.isCellEditor(c);
    }

    public Insets getBorderInsets(Component c, Insets insets) {
        float ow = UIScale.scale((float) getFocusWidth(c)) + UIScale.scale((float) getLineWidth(c));
        Insets insets2 = FlatBorder.super.getBorderInsets(c, insets);
        insets2.top = Math.round(UIScale.scale((float) insets2.top) + ow);
        insets2.left = Math.round(UIScale.scale((float) insets2.left) + ow);
        insets2.bottom = Math.round(UIScale.scale((float) insets2.bottom) + ow);
        insets2.right = Math.round(UIScale.scale((float) insets2.right) + ow);
        if (isCellEditor(c)) {
            insets2.bottom = 0;
            insets2.top = 0;
            if (c.getComponentOrientation().isLeftToRight()) {
                insets2.right = 0;
            } else {
                insets2.left = 0;
            }
        }
        return insets2;
    }

    /* access modifiers changed from: protected */
    public int getFocusWidth(Component c) {
        if (isCellEditor(c)) {
            return 0;
        }
        return this.focusWidth;
    }

    /* access modifiers changed from: protected */
    public int getLineWidth(Component c) {
        return 1;
    }

    /* access modifiers changed from: protected */
    public int getBorderWidth(Component c) {
        return getLineWidth(c);
    }

    /* access modifiers changed from: protected */
    public int getArc(Component c) {
        return 0;
    }
}
