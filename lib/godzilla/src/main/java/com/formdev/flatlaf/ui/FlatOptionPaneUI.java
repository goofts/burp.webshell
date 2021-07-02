package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.UIScale;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicOptionPaneUI;

public class FlatOptionPaneUI extends BasicOptionPaneUI {
    private int focusWidth;
    protected int iconMessageGap;
    protected int maxCharactersPerLine;
    protected int messagePadding;

    public static ComponentUI createUI(JComponent c) {
        return new FlatOptionPaneUI();
    }

    /* access modifiers changed from: protected */
    public void installDefaults() {
        FlatOptionPaneUI.super.installDefaults();
        this.iconMessageGap = UIManager.getInt("OptionPane.iconMessageGap");
        this.messagePadding = UIManager.getInt("OptionPane.messagePadding");
        this.maxCharactersPerLine = UIManager.getInt("OptionPane.maxCharactersPerLine");
        this.focusWidth = UIManager.getInt("Component.focusWidth");
    }

    /* access modifiers changed from: protected */
    public void installComponents() {
        FlatOptionPaneUI.super.installComponents();
        updateChildPanels(this.optionPane);
    }

    public Dimension getMinimumOptionPaneSize() {
        return UIScale.scale(FlatOptionPaneUI.super.getMinimumOptionPaneSize());
    }

    /* access modifiers changed from: protected */
    public int getMaxCharactersPerLineCount() {
        int max = FlatOptionPaneUI.super.getMaxCharactersPerLineCount();
        return (this.maxCharactersPerLine <= 0 || max != Integer.MAX_VALUE) ? max : this.maxCharactersPerLine;
    }

    /* access modifiers changed from: protected */
    public Container createMessageArea() {
        Component iconMessageSeparator;
        Container messageArea = FlatOptionPaneUI.super.createMessageArea();
        if (this.iconMessageGap > 0 && (iconMessageSeparator = findByName(messageArea, "OptionPane.separator")) != null) {
            iconMessageSeparator.setPreferredSize(new Dimension(UIScale.scale(this.iconMessageGap), 1));
        }
        return messageArea;
    }

    /* access modifiers changed from: protected */
    public Container createButtonArea() {
        Container buttonArea = FlatOptionPaneUI.super.createButtonArea();
        if (buttonArea.getLayout() instanceof BasicOptionPaneUI.ButtonAreaLayout) {
            BasicOptionPaneUI.ButtonAreaLayout layout = buttonArea.getLayout();
            layout.setPadding(UIScale.scale(layout.getPadding() - (this.focusWidth * 2)));
        }
        return buttonArea;
    }

    /* access modifiers changed from: protected */
    public void addMessageComponents(Container container, GridBagConstraints cons, Object msg, int maxll, boolean internallyCreated) {
        if (this.messagePadding > 0) {
            cons.insets.bottom = UIScale.scale(this.messagePadding);
        }
        if ((msg instanceof String) && BasicHTML.isHTMLString((String) msg)) {
            maxll = Integer.MAX_VALUE;
        }
        FlatOptionPaneUI.super.addMessageComponents(container, cons, msg, maxll, internallyCreated);
    }

    private void updateChildPanels(Container c) {
        Component[] components = c.getComponents();
        for (Component child : components) {
            if (child instanceof JPanel) {
                JPanel panel = (JPanel) child;
                panel.setOpaque(false);
                Border border = panel.getBorder();
                if (border instanceof UIResource) {
                    panel.setBorder(new NonUIResourceBorder(border));
                }
            }
            if (child instanceof Container) {
                updateChildPanels((Container) child);
            }
        }
    }

    private Component findByName(Container c, String name) {
        Component c2;
        Component[] components = c.getComponents();
        for (Component child : components) {
            if (name.equals(child.getName())) {
                return child;
            }
            if ((child instanceof Container) && (c2 = findByName((Container) child, name)) != null) {
                return c2;
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public static class NonUIResourceBorder implements Border {
        private final Border delegate;

        NonUIResourceBorder(Border delegate2) {
            this.delegate = delegate2;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            this.delegate.paintBorder(c, g, x, y, width, height);
        }

        public Insets getBorderInsets(Component c) {
            return this.delegate.getBorderInsets(c);
        }

        public boolean isBorderOpaque() {
            return this.delegate.isBorderOpaque();
        }
    }
}
