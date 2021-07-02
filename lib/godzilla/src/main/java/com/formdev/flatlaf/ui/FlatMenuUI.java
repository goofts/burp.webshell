package com.formdev.flatlaf.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicMenuUI;

public class FlatMenuUI extends BasicMenuUI {
    private Color hoverBackground;
    private FlatMenuItemRenderer renderer;

    public static ComponentUI createUI(JComponent c) {
        return new FlatMenuUI();
    }

    /* access modifiers changed from: protected */
    public void installDefaults() {
        FlatMenuUI.super.installDefaults();
        LookAndFeel.installProperty(this.menuItem, "iconTextGap", Integer.valueOf(FlatUIUtils.getUIInt("MenuItem.iconTextGap", 4)));
        this.menuItem.setRolloverEnabled(true);
        this.hoverBackground = UIManager.getColor("MenuBar.hoverBackground");
        this.renderer = createRenderer();
    }

    /* access modifiers changed from: protected */
    public void uninstallDefaults() {
        FlatMenuUI.super.uninstallDefaults();
        this.hoverBackground = null;
        this.renderer = null;
    }

    /* access modifiers changed from: protected */
    public FlatMenuItemRenderer createRenderer() {
        return new FlatMenuRenderer(this.menuItem, this.checkIcon, this.arrowIcon, this.acceleratorFont, this.acceleratorDelimiter);
    }

    /* access modifiers changed from: protected */
    public MouseInputListener createMouseInputListener(JComponent c) {
        return new BasicMenuUI.MouseInputHandler() {
            /* class com.formdev.flatlaf.ui.FlatMenuUI.AnonymousClass1 */

            public void mouseEntered(MouseEvent e) {
                FlatMenuUI.super.mouseEntered(e);
                rollover(e, true);
            }

            public void mouseExited(MouseEvent e) {
                FlatMenuUI.super.mouseExited(e);
                rollover(e, false);
            }

            private void rollover(MouseEvent e, boolean rollover) {
                JMenu menu = (JMenu) e.getSource();
                if (menu.isTopLevelMenu() && menu.isRolloverEnabled()) {
                    menu.getModel().setRollover(rollover);
                    menu.repaint();
                }
            }
        };
    }

    public Dimension getMinimumSize(JComponent c) {
        if (this.menuItem.isTopLevelMenu()) {
            return c.getPreferredSize();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public Dimension getPreferredMenuItemSize(JComponent c, Icon checkIcon, Icon arrowIcon, int defaultTextIconGap) {
        return this.renderer.getPreferredMenuItemSize();
    }

    public void paint(Graphics g, JComponent c) {
        this.renderer.paintMenuItem(g, this.selectionBackground, this.selectionForeground, this.disabledForeground, this.acceleratorForeground, this.acceleratorSelectionForeground);
    }

    /* access modifiers changed from: protected */
    public class FlatMenuRenderer extends FlatMenuItemRenderer {
        protected final Color menuBarUnderlineSelectionBackground = FlatUIUtils.getUIColor("MenuBar.underlineSelectionBackground", this.underlineSelectionBackground);
        protected final Color menuBarUnderlineSelectionColor = FlatUIUtils.getUIColor("MenuBar.underlineSelectionColor", this.underlineSelectionColor);
        protected final int menuBarUnderlineSelectionHeight = FlatUIUtils.getUIInt("MenuBar.underlineSelectionHeight", this.underlineSelectionHeight);

        protected FlatMenuRenderer(JMenuItem menuItem, Icon checkIcon, Icon arrowIcon, Font acceleratorFont, String acceleratorDelimiter) {
            super(menuItem, checkIcon, arrowIcon, acceleratorFont, acceleratorDelimiter);
        }

        /* access modifiers changed from: protected */
        @Override // com.formdev.flatlaf.ui.FlatMenuItemRenderer
        public void paintBackground(Graphics g, Color selectionBackground) {
            if (isUnderlineSelection() && this.menuItem.isTopLevelMenu()) {
                selectionBackground = this.menuBarUnderlineSelectionBackground;
            }
            ButtonModel model = this.menuItem.getModel();
            if (!model.isRollover() || model.isArmed() || model.isSelected() || !model.isEnabled() || !this.menuItem.isTopLevelMenu()) {
                super.paintBackground(g, selectionBackground);
                return;
            }
            g.setColor(deriveBackground(FlatMenuUI.this.hoverBackground));
            g.fillRect(0, 0, this.menuItem.getWidth(), this.menuItem.getHeight());
        }

        /* access modifiers changed from: protected */
        @Override // com.formdev.flatlaf.ui.FlatMenuItemRenderer
        public void paintUnderlineSelection(Graphics g, Color underlineSelectionColor, int underlineSelectionHeight) {
            if (this.menuItem.isTopLevelMenu()) {
                underlineSelectionColor = this.menuBarUnderlineSelectionColor;
                underlineSelectionHeight = this.menuBarUnderlineSelectionHeight;
            }
            super.paintUnderlineSelection(g, underlineSelectionColor, underlineSelectionHeight);
        }
    }
}
