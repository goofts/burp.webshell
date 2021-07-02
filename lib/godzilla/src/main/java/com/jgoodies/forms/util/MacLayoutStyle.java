package com.jgoodies.forms.util;

import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.Size;
import com.jgoodies.forms.layout.Sizes;

public final class MacLayoutStyle extends LayoutStyle {
    private static final ConstantSize BUTTON_BAR_PAD = Sizes.DLUY4;
    private static final Size BUTTON_HEIGHT = Sizes.dluY(14);
    private static final Size BUTTON_WIDTH = Sizes.dluX(55);
    private static final ConstantSize DIALOG_MARGIN_X = Sizes.DLUX9;
    private static final ConstantSize DIALOG_MARGIN_Y = Sizes.DLUY9;
    static final MacLayoutStyle INSTANCE = new MacLayoutStyle();
    private static final ConstantSize LABEL_COMPONENT_PADX = Sizes.DLUX1;
    private static final ConstantSize LABEL_COMPONENT_PADY = Sizes.DLUY2;
    private static final ConstantSize LINE_PAD = Sizes.DLUY3;
    private static final ConstantSize NARROW_LINE_PAD = Sizes.DLUY2;
    private static final ConstantSize PARAGRAPH_PAD = Sizes.DLUY9;
    private static final ConstantSize RELATED_COMPONENTS_PADX = Sizes.DLUX2;
    private static final ConstantSize RELATED_COMPONENTS_PADY = Sizes.DLUY3;
    private static final ConstantSize TABBED_DIALOG_MARGIN_X = Sizes.DLUX4;
    private static final ConstantSize TABBED_DIALOG_MARGIN_Y = Sizes.DLUY4;
    private static final ConstantSize UNRELATED_COMPONENTS_PADX = Sizes.DLUX4;
    private static final ConstantSize UNRELATED_COMPONENTS_PADY = Sizes.DLUY6;

    private MacLayoutStyle() {
    }

    @Override // com.jgoodies.forms.util.LayoutStyle
    public Size getDefaultButtonWidth() {
        return BUTTON_WIDTH;
    }

    @Override // com.jgoodies.forms.util.LayoutStyle
    public Size getDefaultButtonHeight() {
        return BUTTON_HEIGHT;
    }

    @Override // com.jgoodies.forms.util.LayoutStyle
    public ConstantSize getDialogMarginX() {
        return DIALOG_MARGIN_X;
    }

    @Override // com.jgoodies.forms.util.LayoutStyle
    public ConstantSize getDialogMarginY() {
        return DIALOG_MARGIN_Y;
    }

    @Override // com.jgoodies.forms.util.LayoutStyle
    public ConstantSize getTabbedDialogMarginX() {
        return TABBED_DIALOG_MARGIN_X;
    }

    @Override // com.jgoodies.forms.util.LayoutStyle
    public ConstantSize getTabbedDialogMarginY() {
        return TABBED_DIALOG_MARGIN_Y;
    }

    @Override // com.jgoodies.forms.util.LayoutStyle
    public ConstantSize getLabelComponentPadX() {
        return LABEL_COMPONENT_PADX;
    }

    @Override // com.jgoodies.forms.util.LayoutStyle
    public ConstantSize getLabelComponentPadY() {
        return LABEL_COMPONENT_PADY;
    }

    @Override // com.jgoodies.forms.util.LayoutStyle
    public ConstantSize getRelatedComponentsPadX() {
        return RELATED_COMPONENTS_PADX;
    }

    @Override // com.jgoodies.forms.util.LayoutStyle
    public ConstantSize getRelatedComponentsPadY() {
        return RELATED_COMPONENTS_PADY;
    }

    @Override // com.jgoodies.forms.util.LayoutStyle
    public ConstantSize getUnrelatedComponentsPadX() {
        return UNRELATED_COMPONENTS_PADX;
    }

    @Override // com.jgoodies.forms.util.LayoutStyle
    public ConstantSize getUnrelatedComponentsPadY() {
        return UNRELATED_COMPONENTS_PADY;
    }

    @Override // com.jgoodies.forms.util.LayoutStyle
    public ConstantSize getNarrowLinePad() {
        return NARROW_LINE_PAD;
    }

    @Override // com.jgoodies.forms.util.LayoutStyle
    public ConstantSize getLinePad() {
        return LINE_PAD;
    }

    @Override // com.jgoodies.forms.util.LayoutStyle
    public ConstantSize getParagraphPad() {
        return PARAGRAPH_PAD;
    }

    @Override // com.jgoodies.forms.util.LayoutStyle
    public ConstantSize getButtonBarPad() {
        return BUTTON_BAR_PAD;
    }
}
