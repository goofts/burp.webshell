package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Component;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Paint;
import javax.swing.AbstractButton;
import javax.swing.UIManager;
import javax.swing.plaf.UIResource;

public class FlatButtonBorder extends FlatBorder {
    protected final int arc = UIManager.getInt("Button.arc");
    protected final Color borderColor = FlatUIUtils.getUIColor("Button.startBorderColor", "Button.borderColor");
    protected final int borderWidth = UIManager.getInt("Button.borderWidth");
    protected final Color defaultBorderColor = FlatUIUtils.getUIColor("Button.default.startBorderColor", "Button.default.borderColor");
    protected final int defaultBorderWidth = UIManager.getInt("Button.default.borderWidth");
    protected final Color defaultEndBorderColor = UIManager.getColor("Button.default.endBorderColor");
    protected final Color defaultFocusColor = UIManager.getColor("Button.default.focusColor");
    protected final Color defaultFocusedBorderColor = UIManager.getColor("Button.default.focusedBorderColor");
    protected final Color defaultHoverBorderColor = UIManager.getColor("Button.default.hoverBorderColor");
    protected final Color disabledBorderColor = UIManager.getColor("Button.disabledBorderColor");
    protected final Color endBorderColor = UIManager.getColor("Button.endBorderColor");
    protected final Color focusedBorderColor = UIManager.getColor("Button.focusedBorderColor");
    protected final Color hoverBorderColor = UIManager.getColor("Button.hoverBorderColor");
    protected final Insets toolbarMargin = UIManager.getInsets("Button.toolbar.margin");
    protected final Insets toolbarSpacingInsets = UIManager.getInsets("Button.toolbar.spacingInsets");

    @Override // com.formdev.flatlaf.ui.FlatBorder
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if (FlatButtonUI.isContentAreaFilled(c) && !FlatButtonUI.isToolBarButton(c) && !FlatButtonUI.isHelpButton(c) && !FlatToggleButtonUI.isTabButton(c)) {
            super.paintBorder(c, g, x, y, width, height);
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.ui.FlatBorder
    public Color getFocusColor(Component c) {
        return FlatButtonUI.isDefaultButton(c) ? this.defaultFocusColor : super.getFocusColor(c);
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.ui.FlatBorder
    public boolean isFocused(Component c) {
        return FlatButtonUI.isFocusPainted(c) && super.isFocused(c);
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.ui.FlatBorder
    public Paint getBorderColor(Component c) {
        boolean def = FlatButtonUI.isDefaultButton(c);
        Color color = FlatButtonUI.buttonStateColor(c, def ? this.defaultBorderColor : this.borderColor, this.disabledBorderColor, def ? this.defaultFocusedBorderColor : this.focusedBorderColor, def ? this.defaultHoverBorderColor : this.hoverBorderColor, null);
        Color startBg = def ? this.defaultBorderColor : this.borderColor;
        Color endBg = def ? this.defaultEndBorderColor : this.endBorderColor;
        if (color != startBg || endBg == null || startBg.equals(endBg)) {
            return color;
        }
        return new GradientPaint(0.0f, 0.0f, startBg, 0.0f, (float) c.getHeight(), endBg);
    }

    @Override // com.formdev.flatlaf.ui.FlatBorder
    public Insets getBorderInsets(Component c, Insets insets) {
        if (FlatButtonUI.isToolBarButton(c)) {
            Insets margin = c instanceof AbstractButton ? ((AbstractButton) c).getMargin() : null;
            Insets insets2 = this.toolbarSpacingInsets;
            if (margin == null || (margin instanceof UIResource)) {
                margin = this.toolbarMargin;
            }
            FlatUIUtils.setInsets(insets, UIScale.scale(FlatUIUtils.addInsets(insets2, margin)));
        } else {
            insets = super.getBorderInsets(c, insets);
            if (FlatButtonUI.isIconOnlyOrSingleCharacterButton(c) && (((AbstractButton) c).getMargin() instanceof UIResource)) {
                int min = Math.min(insets.top, insets.bottom);
                insets.right = min;
                insets.left = min;
            }
        }
        return insets;
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.ui.FlatBorder
    public int getFocusWidth(Component c) {
        if (FlatToggleButtonUI.isTabButton(c)) {
            return 0;
        }
        return super.getFocusWidth(c);
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.ui.FlatBorder
    public int getBorderWidth(Component c) {
        return FlatButtonUI.isDefaultButton(c) ? this.defaultBorderWidth : this.borderWidth;
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.ui.FlatBorder
    public int getArc(Component c) {
        if (isCellEditor(c)) {
            return 0;
        }
        switch (FlatButtonUI.getButtonType(c)) {
            case 0:
                return 0;
            case 1:
                return 32767;
            default:
                return this.arc;
        }
    }
}
