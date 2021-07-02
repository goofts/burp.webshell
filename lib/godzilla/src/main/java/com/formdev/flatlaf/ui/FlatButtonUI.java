package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.util.UIScale;
import com.jgoodies.common.base.Strings;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicButtonListener;
import javax.swing.plaf.basic.BasicButtonUI;

public class FlatButtonUI extends BasicButtonUI {
    static final int TYPE_OTHER = -1;
    static final int TYPE_ROUND_RECT = 1;
    static final int TYPE_SQUARE = 0;
    protected Color background;
    protected Color defaultBackground;
    protected boolean defaultBoldText;
    protected Color defaultEndBackground;
    protected Color defaultFocusedBackground;
    protected Color defaultForeground;
    protected Color defaultHoverBackground;
    protected Color defaultPressedBackground;
    protected Color defaultShadowColor;
    private boolean defaults_initialized = false;
    protected Color disabledBackground;
    protected Color disabledSelectedBackground;
    protected Color disabledText;
    protected Color endBackground;
    protected Color focusedBackground;
    protected Color foreground;
    private Icon helpButtonIcon;
    protected Color hoverBackground;
    protected int iconTextGap;
    protected int minimumWidth;
    protected Color pressedBackground;
    protected Color selectedBackground;
    protected Color selectedForeground;
    protected Color shadowColor;
    protected int shadowWidth;
    protected Color startBackground;
    protected Color toolbarHoverBackground;
    protected Color toolbarPressedBackground;
    protected Color toolbarSelectedBackground;
    protected Insets toolbarSpacingInsets;

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
            java.lang.Class<com.formdev.flatlaf.ui.FlatButtonUI> r0 = com.formdev.flatlaf.ui.FlatButtonUI.class
            r1 = move-result
            javax.swing.plaf.ComponentUI r0 = com.formdev.flatlaf.ui.FlatUIUtils.createSharedUI(r0, r1)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatButtonUI.createUI(javax.swing.JComponent):javax.swing.plaf.ComponentUI");
    }

    /* access modifiers changed from: protected */
    public void installDefaults(AbstractButton b) {
        Color bg;
        FlatButtonUI.super.installDefaults(b);
        if (!this.defaults_initialized) {
            String prefix = getPropertyPrefix();
            this.minimumWidth = UIManager.getInt(prefix + "minimumWidth");
            this.iconTextGap = FlatUIUtils.getUIInt(prefix + "iconTextGap", 4);
            this.background = UIManager.getColor(prefix + "background");
            this.foreground = UIManager.getColor(prefix + "foreground");
            this.startBackground = UIManager.getColor(prefix + "startBackground");
            this.endBackground = UIManager.getColor(prefix + "endBackground");
            this.focusedBackground = UIManager.getColor(prefix + "focusedBackground");
            this.hoverBackground = UIManager.getColor(prefix + "hoverBackground");
            this.pressedBackground = UIManager.getColor(prefix + "pressedBackground");
            this.selectedBackground = UIManager.getColor(prefix + "selectedBackground");
            this.selectedForeground = UIManager.getColor(prefix + "selectedForeground");
            this.disabledBackground = UIManager.getColor(prefix + "disabledBackground");
            this.disabledText = UIManager.getColor(prefix + "disabledText");
            this.disabledSelectedBackground = UIManager.getColor(prefix + "disabledSelectedBackground");
            if (UIManager.getBoolean("Button.paintShadow")) {
                this.shadowWidth = FlatUIUtils.getUIInt("Button.shadowWidth", 2);
                this.shadowColor = UIManager.getColor("Button.shadowColor");
                this.defaultShadowColor = UIManager.getColor("Button.default.shadowColor");
            } else {
                this.shadowWidth = 0;
                this.shadowColor = null;
                this.defaultShadowColor = null;
            }
            this.defaultBackground = FlatUIUtils.getUIColor("Button.default.startBackground", "Button.default.background");
            this.defaultEndBackground = UIManager.getColor("Button.default.endBackground");
            this.defaultForeground = UIManager.getColor("Button.default.foreground");
            this.defaultFocusedBackground = UIManager.getColor("Button.default.focusedBackground");
            this.defaultHoverBackground = UIManager.getColor("Button.default.hoverBackground");
            this.defaultPressedBackground = UIManager.getColor("Button.default.pressedBackground");
            this.defaultBoldText = UIManager.getBoolean("Button.default.boldText");
            this.toolbarSpacingInsets = UIManager.getInsets("Button.toolbar.spacingInsets");
            this.toolbarHoverBackground = UIManager.getColor(prefix + "toolbar.hoverBackground");
            this.toolbarPressedBackground = UIManager.getColor(prefix + "toolbar.pressedBackground");
            this.toolbarSelectedBackground = UIManager.getColor(prefix + "toolbar.selectedBackground");
            this.helpButtonIcon = UIManager.getIcon("HelpButton.icon");
            this.defaults_initialized = true;
        }
        if (this.startBackground != null && ((bg = b.getBackground()) == null || (bg instanceof UIResource))) {
            b.setBackground(this.startBackground);
        }
        LookAndFeel.installProperty(b, "opaque", false);
        LookAndFeel.installProperty(b, "iconTextGap", Integer.valueOf(UIScale.scale(this.iconTextGap)));
        MigLayoutVisualPadding.install(b);
    }

    /* access modifiers changed from: protected */
    public void uninstallDefaults(AbstractButton b) {
        FlatButtonUI.super.uninstallDefaults(b);
        MigLayoutVisualPadding.uninstall(b);
        this.defaults_initialized = false;
    }

    /* access modifiers changed from: protected */
    public BasicButtonListener createButtonListener(AbstractButton b) {
        return new FlatButtonListener(b);
    }

    /* access modifiers changed from: protected */
    public void propertyChange(AbstractButton b, PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();
        char c = 65535;
        switch (propertyName.hashCode()) {
            case -1302441837:
                if (propertyName.equals(FlatClientProperties.MINIMUM_WIDTH)) {
                    c = 1;
                    break;
                }
                break;
            case -1134471216:
                if (propertyName.equals(FlatClientProperties.SQUARE_SIZE)) {
                    c = 0;
                    break;
                }
                break;
            case 1428734622:
                if (propertyName.equals(FlatClientProperties.BUTTON_TYPE)) {
                    c = 3;
                    break;
                }
                break;
            case 2140981242:
                if (propertyName.equals(FlatClientProperties.MINIMUM_HEIGHT)) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
            case 2:
                b.revalidate();
                return;
            case 3:
                b.revalidate();
                b.repaint();
                return;
            default:
                return;
        }
    }

    static boolean isContentAreaFilled(Component c) {
        return !(c instanceof AbstractButton) || ((AbstractButton) c).isContentAreaFilled();
    }

    public static boolean isFocusPainted(Component c) {
        return !(c instanceof AbstractButton) || ((AbstractButton) c).isFocusPainted();
    }

    static boolean isDefaultButton(Component c) {
        return (c instanceof JButton) && ((JButton) c).isDefaultButton();
    }

    static boolean isIconOnlyOrSingleCharacterButton(Component c) {
        boolean z;
        if (!(c instanceof JButton) && !(c instanceof JToggleButton)) {
            return false;
        }
        Icon icon = ((AbstractButton) c).getIcon();
        String text = ((AbstractButton) c).getText();
        if ((icon == null || (text != null && !text.isEmpty())) && (icon != null || text == null || (!Strings.NO_ELLIPSIS_STRING.equals(text) && text.length() != 1 && (text.length() != 2 || !Character.isSurrogatePair(text.charAt(0), text.charAt(1)))))) {
            z = false;
        } else {
            z = true;
        }
        return z;
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    static int getButtonType(Component c) {
        boolean z;
        if (!(c instanceof AbstractButton)) {
            return -1;
        }
        Object value = ((AbstractButton) c).getClientProperty(FlatClientProperties.BUTTON_TYPE);
        if (!(value instanceof String)) {
            return -1;
        }
        String str = (String) value;
        switch (str.hashCode()) {
            case -894674659:
                if (str.equals(FlatClientProperties.BUTTON_TYPE_SQUARE)) {
                    z = false;
                    break;
                }
                z = true;
                break;
            case -5109614:
                if (str.equals(FlatClientProperties.BUTTON_TYPE_ROUND_RECT)) {
                    z = true;
                    break;
                }
                z = true;
                break;
            default:
                z = true;
                break;
        }
        switch (z) {
            case false:
                return 0;
            case true:
                return 1;
            default:
                return -1;
        }
    }

    static boolean isHelpButton(Component c) {
        return (c instanceof JButton) && FlatClientProperties.clientPropertyEquals((JButton) c, FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_HELP);
    }

    static boolean isToolBarButton(Component c) {
        return (c.getParent() instanceof JToolBar) || ((c instanceof AbstractButton) && FlatClientProperties.clientPropertyEquals((AbstractButton) c, FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON));
    }

    public void update(Graphics g, JComponent c) {
        if (c.isOpaque()) {
            FlatUIUtils.paintParentBackground(g, c);
        }
        if (isHelpButton(c)) {
            this.helpButtonIcon.paintIcon(c, g, 0, 0);
            return;
        }
        if (isContentAreaFilled(c)) {
            paintBackground(g, c);
        }
        paint(g, c);
    }

    /* access modifiers changed from: protected */
    public void paintBackground(Graphics g, JComponent c) {
        Color background2 = getBackground(c);
        if (background2 != null) {
            Graphics2D g2 = g.create();
            try {
                FlatUIUtils.setRenderingHints(g2);
                boolean isToolBarButton = isToolBarButton(c);
                float focusWidth = isToolBarButton ? 0.0f : FlatUIUtils.getBorderFocusWidth(c);
                float arc = FlatUIUtils.getBorderArc(c);
                boolean def = isDefaultButton(c);
                int x = 0;
                int y = 0;
                int width = c.getWidth();
                int height = c.getHeight();
                if (isToolBarButton) {
                    Insets spacing = UIScale.scale(this.toolbarSpacingInsets);
                    x = 0 + spacing.left;
                    y = 0 + spacing.top;
                    width -= spacing.left + spacing.right;
                    height -= spacing.top + spacing.bottom;
                }
                Color shadowColor2 = def ? this.defaultShadowColor : this.shadowColor;
                if (!isToolBarButton && shadowColor2 != null && this.shadowWidth > 0 && focusWidth > 0.0f && ((!isFocusPainted(c) || !FlatUIUtils.isPermanentFocusOwner(c)) && c.isEnabled())) {
                    g2.setColor(shadowColor2);
                    g2.fill(new RoundRectangle2D.Float(focusWidth, UIScale.scale((float) this.shadowWidth) + focusWidth, ((float) width) - (2.0f * focusWidth), ((float) height) - (2.0f * focusWidth), arc, arc));
                }
                Color startBg = def ? this.defaultBackground : this.startBackground;
                Color endBg = def ? this.defaultEndBackground : this.endBackground;
                if (background2 != startBg || endBg == null || startBg.equals(endBg)) {
                    g2.setColor(FlatUIUtils.deriveColor(background2, getBackgroundBase(c, def)));
                } else {
                    g2.setPaint(new GradientPaint(0.0f, 0.0f, startBg, 0.0f, (float) height, endBg));
                }
                FlatUIUtils.paintComponentBackground(g2, x, y, width, height, focusWidth, arc);
            } finally {
                g2.dispose();
            }
        }
    }

    public void paint(Graphics g, JComponent c) {
        FlatButtonUI.super.paint(FlatLabelUI.createGraphicsHTMLTextYCorrection(g, c), c);
    }

    /* access modifiers changed from: protected */
    public void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text) {
        if (!isHelpButton(b)) {
            if (this.defaultBoldText && isDefaultButton(b) && (b.getFont() instanceof UIResource)) {
                Font boldFont = g.getFont().deriveFont(1);
                g.setFont(boldFont);
                int boldWidth = b.getFontMetrics(boldFont).stringWidth(text);
                if (boldWidth > textRect.width) {
                    textRect.x -= (boldWidth - textRect.width) / 2;
                    textRect.width = boldWidth;
                }
            }
            paintText(g, b, textRect, text, getForeground(b));
        }
    }

    public static void paintText(Graphics g, AbstractButton b, Rectangle textRect, String text, Color foreground2) {
        FontMetrics fm = b.getFontMetrics(b.getFont());
        int mnemonicIndex = FlatLaf.isShowMnemonics() ? b.getDisplayedMnemonicIndex() : -1;
        g.setColor(foreground2);
        FlatUIUtils.drawStringUnderlineCharAt(b, g, text, mnemonicIndex, textRect.x, textRect.y + fm.getAscent());
    }

    /* access modifiers changed from: protected */
    public Color getBackground(JComponent c) {
        Color color;
        Color color2;
        Color color3;
        if (((AbstractButton) c).isSelected()) {
            boolean toolBarButton = isToolBarButton(c);
            if (toolBarButton) {
                color = this.toolbarSelectedBackground;
            } else {
                color = this.selectedBackground;
            }
            if (toolBarButton) {
                color2 = this.toolbarSelectedBackground;
            } else {
                color2 = this.disabledSelectedBackground;
            }
            if (toolBarButton) {
                color3 = this.toolbarPressedBackground;
            } else {
                color3 = this.pressedBackground;
            }
            return buttonStateColor(c, color, color2, null, null, color3);
        } else if (!c.isEnabled()) {
            return this.disabledBackground;
        } else {
            if (isToolBarButton(c)) {
                ButtonModel model = ((AbstractButton) c).getModel();
                if (model.isPressed()) {
                    return this.toolbarPressedBackground;
                }
                if (model.isRollover()) {
                    return this.toolbarHoverBackground;
                }
                Color bg = c.getBackground();
                if (!isCustomBackground(bg)) {
                    return null;
                }
                return bg;
            }
            boolean def = isDefaultButton(c);
            return buttonStateColor(c, getBackgroundBase(c, def), null, isCustomBackground(c.getBackground()) ? null : def ? this.defaultFocusedBackground : this.focusedBackground, def ? this.defaultHoverBackground : this.hoverBackground, def ? this.defaultPressedBackground : this.pressedBackground);
        }
    }

    /* access modifiers changed from: protected */
    public Color getBackgroundBase(JComponent c, boolean def) {
        Color bg = c.getBackground();
        return (!isCustomBackground(bg) && def) ? this.defaultBackground : bg;
    }

    /* access modifiers changed from: protected */
    public boolean isCustomBackground(Color bg) {
        return bg != this.background && (this.startBackground == null || bg != this.startBackground);
    }

    public static Color buttonStateColor(Component c, Color enabledColor, Color disabledColor, Color focusedColor, Color hoverColor, Color pressedColor) {
        AbstractButton b = c instanceof AbstractButton ? (AbstractButton) c : null;
        if (!c.isEnabled()) {
            return disabledColor;
        }
        if (pressedColor != null && b != null && b.getModel().isPressed()) {
            return pressedColor;
        }
        if (hoverColor == null || b == null || !b.getModel().isRollover()) {
            return (focusedColor == null || !isFocusPainted(c) || !FlatUIUtils.isPermanentFocusOwner(c)) ? enabledColor : focusedColor;
        }
        return hoverColor;
    }

    /* access modifiers changed from: protected */
    public Color getForeground(JComponent c) {
        if (!c.isEnabled()) {
            return this.disabledText;
        }
        if (((AbstractButton) c).isSelected() && !isToolBarButton(c)) {
            return this.selectedForeground;
        }
        Color fg = c.getForeground();
        return (isCustomForeground(fg) || !isDefaultButton(c)) ? fg : this.defaultForeground;
    }

    /* access modifiers changed from: protected */
    public boolean isCustomForeground(Color fg) {
        return fg != this.foreground;
    }

    public Dimension getPreferredSize(JComponent c) {
        if (isHelpButton(c)) {
            return new Dimension(this.helpButtonIcon.getIconWidth(), this.helpButtonIcon.getIconHeight());
        }
        Dimension prefSize = FlatButtonUI.super.getPreferredSize(c);
        if (prefSize == null) {
            return null;
        }
        boolean isIconOnlyOrSingleCharacter = isIconOnlyOrSingleCharacterButton(c);
        if (FlatClientProperties.clientPropertyBoolean(c, FlatClientProperties.SQUARE_SIZE, false)) {
            int max = Math.max(prefSize.width, prefSize.height);
            prefSize.height = max;
            prefSize.width = max;
            return prefSize;
        } else if (isIconOnlyOrSingleCharacter && ((AbstractButton) c).getIcon() == null) {
            prefSize.width = Math.max(prefSize.width, prefSize.height);
            return prefSize;
        } else if (isIconOnlyOrSingleCharacter || isToolBarButton(c) || !(c.getBorder() instanceof FlatButtonBorder)) {
            return prefSize;
        } else {
            float focusWidth = FlatUIUtils.getBorderFocusWidth(c);
            prefSize.width = Math.max(prefSize.width, UIScale.scale(FlatUIUtils.minimumWidth(c, this.minimumWidth)) + Math.round(focusWidth * 2.0f));
            prefSize.height = Math.max(prefSize.height, UIScale.scale(FlatUIUtils.minimumHeight(c, 0)) + Math.round(focusWidth * 2.0f));
            return prefSize;
        }
    }

    protected class FlatButtonListener extends BasicButtonListener {
        private final AbstractButton b;

        protected FlatButtonListener(AbstractButton b2) {
            super(b2);
            this.b = b2;
        }

        public void propertyChange(PropertyChangeEvent e) {
            FlatButtonUI.super.propertyChange(e);
            FlatButtonUI.this.propertyChange(this.b, e);
        }
    }
}
