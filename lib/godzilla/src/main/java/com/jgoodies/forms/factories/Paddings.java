package com.jgoodies.forms.factories;

import com.jgoodies.common.base.Preconditions;
import com.jgoodies.common.base.Strings;
import com.jgoodies.forms.layout.ConstantSize;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.forms.util.LayoutStyle;
import java.awt.Component;
import java.awt.Insets;
import javax.swing.border.EmptyBorder;

public final class Paddings {
    public static final Padding BUTTON_BAR_PAD = createPadding(LayoutStyle.getCurrent().getButtonBarPad(), Sizes.dluX(0), Sizes.dluY(0), Sizes.dluX(0));
    public static final Padding DIALOG = createPadding(LayoutStyle.getCurrent().getDialogMarginY(), LayoutStyle.getCurrent().getDialogMarginX(), LayoutStyle.getCurrent().getDialogMarginY(), LayoutStyle.getCurrent().getDialogMarginX());
    public static final Padding DLU14 = createPadding(Sizes.DLUY14, Sizes.DLUX14, Sizes.DLUY14, Sizes.DLUX14);
    public static final Padding DLU2 = createPadding(Sizes.DLUY2, Sizes.DLUX2, Sizes.DLUY2, Sizes.DLUX2);
    public static final Padding DLU21 = createPadding(Sizes.DLUY21, Sizes.DLUX21, Sizes.DLUY21, Sizes.DLUX21);
    public static final Padding DLU4 = createPadding(Sizes.DLUY4, Sizes.DLUX4, Sizes.DLUY4, Sizes.DLUX4);
    public static final Padding DLU7 = createPadding(Sizes.DLUY7, Sizes.DLUX7, Sizes.DLUY7, Sizes.DLUX7);
    public static final Padding DLU9 = createPadding(Sizes.DLUY9, Sizes.DLUX9, Sizes.DLUY9, Sizes.DLUX9);
    public static final EmptyBorder EMPTY = new EmptyBorder(0, 0, 0, 0);
    public static final Padding TABBED_DIALOG = createPadding(LayoutStyle.getCurrent().getTabbedDialogMarginY(), LayoutStyle.getCurrent().getTabbedDialogMarginX(), LayoutStyle.getCurrent().getTabbedDialogMarginY(), LayoutStyle.getCurrent().getTabbedDialogMarginX());

    private Paddings() {
    }

    public static Padding createPadding(ConstantSize top, ConstantSize left, ConstantSize bottom, ConstantSize right) {
        return new Padding(top, left, bottom, right);
    }

    public static Padding createPadding(String encodedSizes, Object... args) {
        boolean z;
        String formattedSizes = Strings.get(encodedSizes, args);
        String[] token = formattedSizes.split("\\s*,\\s*");
        int tokenCount = token.length;
        if (token.length == 4) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "The padding requires 4 sizes, but \"%s\" has %d.", formattedSizes, Integer.valueOf(tokenCount));
        return createPadding(Sizes.constant(token[0], false), Sizes.constant(token[1], true), Sizes.constant(token[2], false), Sizes.constant(token[3], true));
    }

    public static final class Padding extends EmptyBorder {
        private final ConstantSize bottomMargin;
        private final ConstantSize leftMargin;
        private final ConstantSize rightMargin;
        private final ConstantSize topMargin;

        private Padding(ConstantSize top, ConstantSize left, ConstantSize bottom, ConstantSize right) {
            super(0, 0, 0, 0);
            if (top == null || left == null || bottom == null || right == null) {
                throw new NullPointerException("The top, left, bottom, and right must not be null.");
            }
            this.topMargin = top;
            this.leftMargin = left;
            this.bottomMargin = bottom;
            this.rightMargin = right;
        }

        public Insets getBorderInsets() {
            return getBorderInsets(null);
        }

        public Insets getBorderInsets(Component c) {
            return getBorderInsets(c, new Insets(0, 0, 0, 0));
        }

        public Insets getBorderInsets(Component c, Insets insets) {
            insets.top = this.topMargin.getPixelSize(c);
            insets.left = this.leftMargin.getPixelSize(c);
            insets.bottom = this.bottomMargin.getPixelSize(c);
            insets.right = this.rightMargin.getPixelSize(c);
            return insets;
        }
    }
}
