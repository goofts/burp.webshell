package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.util.DerivedColor;
import com.formdev.flatlaf.util.Graphics2DProxy;
import com.formdev.flatlaf.util.HiDPIUtils;
import com.formdev.flatlaf.util.SystemInfo;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.AttributedCharacterIterator;
import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.View;

public class FlatMenuItemRenderer {
    private static final char commandGlyph = 8984;
    private static final char controlGlyph = 8963;
    private static final char optionGlyph = 8997;
    private static final char shiftGlyph = 8679;
    protected final int acceleratorArrowGap = FlatUIUtils.getUIInt("MenuItem.acceleratorArrowGap", 2);
    protected final String acceleratorDelimiter;
    protected final Font acceleratorFont;
    protected final Icon arrowIcon;
    private KeyStroke cachedAccelerator;
    private boolean cachedAcceleratorLeftToRight;
    private String cachedAcceleratorText;
    protected final Color checkBackground = UIManager.getColor("MenuItem.checkBackground");
    protected final Icon checkIcon;
    protected final Insets checkMargins = UIManager.getInsets("MenuItem.checkMargins");
    protected final JMenuItem menuItem;
    protected final Dimension minimumIconSize;
    protected final int minimumWidth = UIManager.getInt("MenuItem.minimumWidth");
    protected final Color selectionBackground = UIManager.getColor("MenuItem.selectionBackground");
    protected final int textAcceleratorGap = FlatUIUtils.getUIInt("MenuItem.textAcceleratorGap", 28);
    protected final int textNoAcceleratorGap = FlatUIUtils.getUIInt("MenuItem.textNoAcceleratorGap", 6);
    protected final Color underlineSelectionBackground = UIManager.getColor("MenuItem.underlineSelectionBackground");
    protected final Color underlineSelectionCheckBackground = UIManager.getColor("MenuItem.underlineSelectionCheckBackground");
    protected final Color underlineSelectionColor = UIManager.getColor("MenuItem.underlineSelectionColor");
    protected final int underlineSelectionHeight = UIManager.getInt("MenuItem.underlineSelectionHeight");

    protected FlatMenuItemRenderer(JMenuItem menuItem2, Icon checkIcon2, Icon arrowIcon2, Font acceleratorFont2, String acceleratorDelimiter2) {
        this.menuItem = menuItem2;
        this.checkIcon = checkIcon2;
        this.arrowIcon = arrowIcon2;
        this.acceleratorFont = acceleratorFont2;
        this.acceleratorDelimiter = acceleratorDelimiter2;
        Dimension minimumIconSize2 = UIManager.getDimension("MenuItem.minimumIconSize");
        this.minimumIconSize = minimumIconSize2 == null ? new Dimension(16, 16) : minimumIconSize2;
    }

    /* access modifiers changed from: protected */
    public Dimension getPreferredMenuItemSize() {
        boolean isTopLevelMenu = isTopLevelMenu(this.menuItem);
        Rectangle viewRect = new Rectangle(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
        Rectangle iconRect = new Rectangle();
        Rectangle textRect = new Rectangle();
        SwingUtilities.layoutCompoundLabel(this.menuItem, this.menuItem.getFontMetrics(this.menuItem.getFont()), this.menuItem.getText(), getIconForLayout(), this.menuItem.getVerticalAlignment(), this.menuItem.getHorizontalAlignment(), this.menuItem.getVerticalTextPosition(), this.menuItem.getHorizontalTextPosition(), viewRect, iconRect, textRect, UIScale.scale(this.menuItem.getIconTextGap()));
        Rectangle labelRect = iconRect.union(textRect);
        int width = 0 + labelRect.width;
        int height = Math.max(labelRect.height, 0);
        String accelText = getAcceleratorText();
        if (accelText != null) {
            int width2 = width + UIScale.scale(!isTopLevelMenu ? this.textAcceleratorGap : this.menuItem.getIconTextGap());
            FontMetrics accelFm = this.menuItem.getFontMetrics(this.acceleratorFont);
            width = width2 + SwingUtilities.computeStringWidth(accelFm, accelText);
            height = Math.max(accelFm.getHeight(), height);
        }
        if (!isTopLevelMenu && this.arrowIcon != null) {
            if (accelText == null) {
                width += UIScale.scale(this.textNoAcceleratorGap);
            }
            width = width + UIScale.scale(this.acceleratorArrowGap) + this.arrowIcon.getIconWidth();
            height = Math.max(this.arrowIcon.getIconHeight(), height);
        }
        Insets insets = this.menuItem.getInsets();
        int width3 = width + insets.left + insets.right;
        int height2 = height + insets.top + insets.bottom;
        if (!isTopLevelMenu) {
            width3 = Math.max(width3, UIScale.scale(FlatUIUtils.minimumWidth(this.menuItem, this.minimumWidth)));
        }
        return new Dimension(width3, height2);
    }

    private void layout(Rectangle viewRect, Rectangle iconRect, Rectangle textRect, Rectangle accelRect, Rectangle arrowRect, Rectangle labelRect) {
        boolean isTopLevelMenu = isTopLevelMenu(this.menuItem);
        if (isTopLevelMenu || this.arrowIcon == null) {
            arrowRect.setSize(0, 0);
        } else {
            arrowRect.width = this.arrowIcon.getIconWidth();
            arrowRect.height = this.arrowIcon.getIconHeight();
        }
        arrowRect.y = viewRect.y + centerOffset(viewRect.height, arrowRect.height);
        String accelText = getAcceleratorText();
        if (accelText != null) {
            FontMetrics accelFm = this.menuItem.getFontMetrics(this.acceleratorFont);
            accelRect.width = SwingUtilities.computeStringWidth(accelFm, accelText);
            accelRect.height = accelFm.getHeight();
            accelRect.y = viewRect.y + centerOffset(viewRect.height, accelRect.height);
        } else {
            accelRect.setBounds(0, 0, 0, 0);
        }
        int accelArrowGap = !isTopLevelMenu ? UIScale.scale(this.acceleratorArrowGap) : 0;
        if (this.menuItem.getComponentOrientation().isLeftToRight()) {
            arrowRect.x = (viewRect.x + viewRect.width) - arrowRect.width;
            accelRect.x = (arrowRect.x - accelArrowGap) - accelRect.width;
        } else {
            arrowRect.x = viewRect.x;
            accelRect.x = arrowRect.x + accelArrowGap + arrowRect.width;
        }
        int accelArrowWidth = accelRect.width + arrowRect.width;
        if (accelText != null) {
            accelArrowWidth += UIScale.scale(!isTopLevelMenu ? this.textAcceleratorGap : this.menuItem.getIconTextGap());
        }
        if (!isTopLevelMenu && this.arrowIcon != null) {
            if (accelText == null) {
                accelArrowWidth += UIScale.scale(this.textNoAcceleratorGap);
            }
            accelArrowWidth += UIScale.scale(this.acceleratorArrowGap);
        }
        labelRect.setBounds(viewRect);
        labelRect.width -= accelArrowWidth;
        if (!this.menuItem.getComponentOrientation().isLeftToRight()) {
            labelRect.x += accelArrowWidth;
        }
        SwingUtilities.layoutCompoundLabel(this.menuItem, this.menuItem.getFontMetrics(this.menuItem.getFont()), this.menuItem.getText(), getIconForLayout(), this.menuItem.getVerticalAlignment(), this.menuItem.getHorizontalAlignment(), this.menuItem.getVerticalTextPosition(), this.menuItem.getHorizontalTextPosition(), labelRect, iconRect, textRect, UIScale.scale(this.menuItem.getIconTextGap()));
    }

    private static int centerOffset(int wh1, int wh2) {
        return (wh1 / 2) - (wh2 / 2);
    }

    /* access modifiers changed from: protected */
    public void paintMenuItem(Graphics g, Color selectionBackground2, Color selectionForeground, Color disabledForeground, Color acceleratorForeground, Color acceleratorSelectionForeground) {
        Rectangle viewRect = new Rectangle(this.menuItem.getWidth(), this.menuItem.getHeight());
        Insets insets = this.menuItem.getInsets();
        viewRect.x += insets.left;
        viewRect.y += insets.top;
        viewRect.width -= insets.left + insets.right;
        viewRect.height -= insets.top + insets.bottom;
        Rectangle iconRect = new Rectangle();
        Rectangle textRect = new Rectangle();
        Rectangle accelRect = new Rectangle();
        Rectangle arrowRect = new Rectangle();
        layout(viewRect, iconRect, textRect, accelRect, arrowRect, new Rectangle());
        boolean underlineSelection = isUnderlineSelection();
        if (underlineSelection) {
            selectionBackground2 = this.underlineSelectionBackground;
        }
        paintBackground(g, selectionBackground2);
        if (underlineSelection && isArmedOrSelected(this.menuItem)) {
            paintUnderlineSelection(g, this.underlineSelectionColor, this.underlineSelectionHeight);
        }
        paintIcon(g, iconRect, getIconForPainting(), underlineSelection ? this.underlineSelectionCheckBackground : this.checkBackground);
        paintText(g, textRect, this.menuItem.getText(), selectionForeground, disabledForeground);
        paintAccelerator(g, accelRect, getAcceleratorText(), acceleratorForeground, acceleratorSelectionForeground, disabledForeground);
        if (!isTopLevelMenu(this.menuItem)) {
            paintArrowIcon(g, arrowRect, this.arrowIcon);
        }
    }

    /* access modifiers changed from: protected */
    public void paintBackground(Graphics g, Color selectionBackground2) {
        Color background;
        boolean armedOrSelected = isArmedOrSelected(this.menuItem);
        if (this.menuItem.isOpaque() || armedOrSelected) {
            if (armedOrSelected) {
                background = deriveBackground(selectionBackground2);
            } else {
                background = this.menuItem.getBackground();
            }
            g.setColor(background);
            g.fillRect(0, 0, this.menuItem.getWidth(), this.menuItem.getHeight());
        }
    }

    /* access modifiers changed from: protected */
    public void paintUnderlineSelection(Graphics g, Color underlineSelectionColor2, int underlineSelectionHeight2) {
        int width = this.menuItem.getWidth();
        int height = this.menuItem.getHeight();
        int underlineHeight = UIScale.scale(underlineSelectionHeight2);
        g.setColor(underlineSelectionColor2);
        if (isTopLevelMenu(this.menuItem)) {
            g.fillRect(0, height - underlineHeight, width, underlineHeight);
        } else if (this.menuItem.getComponentOrientation().isLeftToRight()) {
            g.fillRect(0, 0, underlineHeight, height);
        } else {
            g.fillRect(width - underlineHeight, 0, underlineHeight, height);
        }
    }

    /* access modifiers changed from: protected */
    public Color deriveBackground(Color background) {
        Color baseColor;
        if (!(background instanceof DerivedColor)) {
            return background;
        }
        if (this.menuItem.isOpaque()) {
            baseColor = this.menuItem.getBackground();
        } else {
            baseColor = FlatUIUtils.getParentBackground(this.menuItem);
        }
        return FlatUIUtils.deriveColor(background, baseColor);
    }

    /* access modifiers changed from: protected */
    public void paintIcon(Graphics g, Rectangle iconRect, Icon icon, Color checkBackground2) {
        if (!(!this.menuItem.isSelected() || this.checkIcon == null || icon == this.checkIcon)) {
            Rectangle r = FlatUIUtils.addInsets(iconRect, UIScale.scale(this.checkMargins));
            g.setColor(FlatUIUtils.deriveColor(checkBackground2, this.selectionBackground));
            g.fillRect(r.x, r.y, r.width, r.height);
        }
        paintIcon(g, this.menuItem, icon, iconRect);
    }

    /* access modifiers changed from: protected */
    public void paintText(Graphics g, Rectangle textRect, String text, Color selectionForeground, Color disabledForeground) {
        Color color;
        View htmlView = (View) this.menuItem.getClientProperty("html");
        if (htmlView != null) {
            JMenuItem jMenuItem = this.menuItem;
            if (isUnderlineSelection()) {
                selectionForeground = null;
            }
            paintHTMLText(g, jMenuItem, textRect, htmlView, selectionForeground);
            return;
        }
        int mnemonicIndex = FlatLaf.isShowMnemonics() ? this.menuItem.getDisplayedMnemonicIndex() : -1;
        Color foreground = (isTopLevelMenu(this.menuItem) ? this.menuItem.getParent() : this.menuItem).getForeground();
        JMenuItem jMenuItem2 = this.menuItem;
        Font font = this.menuItem.getFont();
        if (isUnderlineSelection()) {
            color = foreground;
        } else {
            color = selectionForeground;
        }
        paintText(g, jMenuItem2, textRect, text, mnemonicIndex, font, foreground, color, disabledForeground);
    }

    /* access modifiers changed from: protected */
    public void paintAccelerator(Graphics g, Rectangle accelRect, String accelText, Color foreground, Color selectionForeground, Color disabledForeground) {
        Color color;
        JMenuItem jMenuItem = this.menuItem;
        Font font = this.acceleratorFont;
        if (isUnderlineSelection()) {
            color = foreground;
        } else {
            color = selectionForeground;
        }
        paintText(g, jMenuItem, accelRect, accelText, -1, font, foreground, color, disabledForeground);
    }

    /* access modifiers changed from: protected */
    public void paintArrowIcon(Graphics g, Rectangle arrowRect, Icon arrowIcon2) {
        paintIcon(g, this.menuItem, arrowIcon2, arrowRect);
    }

    protected static void paintIcon(Graphics g, JMenuItem menuItem2, Icon icon, Rectangle iconRect) {
        if (icon != null) {
            icon.paintIcon(menuItem2, g, iconRect.x + centerOffset(iconRect.width, icon.getIconWidth()), iconRect.y + centerOffset(iconRect.height, icon.getIconHeight()));
        }
    }

    protected static void paintText(Graphics g, JMenuItem menuItem2, Rectangle textRect, String text, int mnemonicIndex, Font font, Color foreground, Color selectionForeground, Color disabledForeground) {
        if (text != null && !text.isEmpty()) {
            FontMetrics fm = menuItem2.getFontMetrics(font);
            Font oldFont = g.getFont();
            g.setFont(font);
            if (menuItem2.isEnabled()) {
                disabledForeground = isArmedOrSelected(menuItem2) ? selectionForeground : foreground;
            }
            g.setColor(disabledForeground);
            FlatUIUtils.drawStringUnderlineCharAt(menuItem2, g, text, mnemonicIndex, textRect.x, textRect.y + fm.getAscent());
            g.setFont(oldFont);
        }
    }

    protected static void paintHTMLText(Graphics g, JMenuItem menuItem2, Rectangle textRect, View htmlView, Color selectionForeground) {
        if (isArmedOrSelected(menuItem2) && selectionForeground != null) {
            g = new GraphicsProxyWithTextColor((Graphics2D) g, selectionForeground);
        }
        htmlView.paint(HiDPIUtils.createGraphicsTextYCorrection((Graphics2D) g), textRect);
    }

    protected static boolean isArmedOrSelected(JMenuItem menuItem2) {
        return menuItem2.isArmed() || ((menuItem2 instanceof JMenu) && menuItem2.isSelected());
    }

    protected static boolean isTopLevelMenu(JMenuItem menuItem2) {
        return (menuItem2 instanceof JMenu) && ((JMenu) menuItem2).isTopLevelMenu();
    }

    /* access modifiers changed from: protected */
    public boolean isUnderlineSelection() {
        return "underline".equals(UIManager.getString("MenuItem.selectionType"));
    }

    private Icon getIconForPainting() {
        Icon pressedIcon;
        Icon icon = this.menuItem.getIcon();
        if (icon == null && this.checkIcon != null && !isTopLevelMenu(this.menuItem)) {
            return this.checkIcon;
        }
        if (icon == null) {
            return null;
        }
        if (!this.menuItem.isEnabled()) {
            return this.menuItem.getDisabledIcon();
        }
        return (!this.menuItem.getModel().isPressed() || !this.menuItem.isArmed() || (pressedIcon = this.menuItem.getPressedIcon()) == null) ? icon : pressedIcon;
    }

    private Icon getIconForLayout() {
        Icon icon = this.menuItem.getIcon();
        if (!isTopLevelMenu(this.menuItem)) {
            if (icon == null) {
                icon = this.checkIcon;
            }
            return new MinSizeIcon(icon);
        } else if (icon != null) {
            return new MinSizeIcon(icon);
        } else {
            return null;
        }
    }

    private String getAcceleratorText() {
        KeyStroke accelerator = this.menuItem.getAccelerator();
        if (accelerator == null) {
            return null;
        }
        boolean leftToRight = this.menuItem.getComponentOrientation().isLeftToRight();
        if (accelerator == this.cachedAccelerator && leftToRight == this.cachedAcceleratorLeftToRight) {
            return this.cachedAcceleratorText;
        }
        this.cachedAccelerator = accelerator;
        this.cachedAcceleratorText = getTextForAccelerator(accelerator);
        this.cachedAcceleratorLeftToRight = leftToRight;
        return this.cachedAcceleratorText;
    }

    /* access modifiers changed from: protected */
    public String getTextForAccelerator(KeyStroke accelerator) {
        StringBuilder buf = new StringBuilder();
        boolean leftToRight = this.menuItem.getComponentOrientation().isLeftToRight();
        int modifiers = accelerator.getModifiers();
        if (modifiers != 0) {
            if (!SystemInfo.isMacOS) {
                buf.append(InputEvent.getModifiersExText(modifiers)).append(this.acceleratorDelimiter);
            } else if (leftToRight) {
                buf.append(getMacOSModifiersExText(modifiers, leftToRight));
            }
        }
        int keyCode = accelerator.getKeyCode();
        if (keyCode != 0) {
            buf.append(KeyEvent.getKeyText(keyCode));
        } else {
            buf.append(accelerator.getKeyChar());
        }
        if (modifiers != 0 && !leftToRight && SystemInfo.isMacOS) {
            buf.append(getMacOSModifiersExText(modifiers, leftToRight));
        }
        return buf.toString();
    }

    /* access modifiers changed from: protected */
    public String getMacOSModifiersExText(int modifiers, boolean leftToRight) {
        StringBuilder buf = new StringBuilder();
        if ((modifiers & 128) != 0) {
            buf.append(controlGlyph);
        }
        if ((modifiers & 8704) != 0) {
            buf.append(optionGlyph);
        }
        if ((modifiers & 64) != 0) {
            buf.append(shiftGlyph);
        }
        if ((modifiers & 256) != 0) {
            buf.append(commandGlyph);
        }
        if (!leftToRight) {
            buf.reverse();
        }
        return buf.toString();
    }

    /* access modifiers changed from: private */
    public class MinSizeIcon implements Icon {
        private final Icon delegate;

        MinSizeIcon(Icon delegate2) {
            this.delegate = delegate2;
        }

        public int getIconWidth() {
            return Math.max(this.delegate != null ? this.delegate.getIconWidth() : 0, UIScale.scale(FlatMenuItemRenderer.this.minimumIconSize.width));
        }

        public int getIconHeight() {
            return Math.max(this.delegate != null ? this.delegate.getIconHeight() : 0, UIScale.scale(FlatMenuItemRenderer.this.minimumIconSize.height));
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
        }
    }

    /* access modifiers changed from: private */
    public static class GraphicsProxyWithTextColor extends Graphics2DProxy {
        private final Color textColor;

        GraphicsProxyWithTextColor(Graphics2D delegate, Color textColor2) {
            super(delegate);
            this.textColor = textColor2;
        }

        @Override // com.formdev.flatlaf.util.Graphics2DProxy
        public void drawString(String str, int x, int y) {
            Paint oldPaint = getPaint();
            setPaint(this.textColor);
            super.drawString(str, x, y);
            setPaint(oldPaint);
        }

        @Override // com.formdev.flatlaf.util.Graphics2DProxy
        public void drawString(String str, float x, float y) {
            Paint oldPaint = getPaint();
            setPaint(this.textColor);
            super.drawString(str, x, y);
            setPaint(oldPaint);
        }

        @Override // com.formdev.flatlaf.util.Graphics2DProxy
        public void drawString(AttributedCharacterIterator iterator, int x, int y) {
            Paint oldPaint = getPaint();
            setPaint(this.textColor);
            super.drawString(iterator, x, y);
            setPaint(oldPaint);
        }

        @Override // com.formdev.flatlaf.util.Graphics2DProxy
        public void drawString(AttributedCharacterIterator iterator, float x, float y) {
            Paint oldPaint = getPaint();
            setPaint(this.textColor);
            super.drawString(iterator, x, y);
            setPaint(oldPaint);
        }

        @Override // com.formdev.flatlaf.util.Graphics2DProxy
        public void drawChars(char[] data, int offset, int length, int x, int y) {
            Paint oldPaint = getPaint();
            setPaint(this.textColor);
            super.drawChars(data, offset, length, x, y);
            setPaint(oldPaint);
        }
    }
}
