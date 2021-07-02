package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.ScaledImageIcon;
import com.formdev.flatlaf.util.UIScale;
import com.kitfox.svg.Title;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.beans.PropertyChangeEvent;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LookAndFeel;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

public class FlatInternalFrameTitlePane extends BasicInternalFrameTitlePane {
    private JPanel buttonPanel;
    private JLabel titleLabel;

    public FlatInternalFrameTitlePane(JInternalFrame f) {
        super(f);
    }

    /* access modifiers changed from: protected */
    public void installDefaults() {
        FlatInternalFrameTitlePane.super.installDefaults();
        LookAndFeel.installBorder(this, "InternalFrameTitlePane.border");
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.formdev.flatlaf.ui.FlatInternalFrameTitlePane$FlatPropertyChangeHandler, java.beans.PropertyChangeListener] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.beans.PropertyChangeListener createPropertyChangeListener() {
        /*
            r1 = this;
            com.formdev.flatlaf.ui.FlatInternalFrameTitlePane$FlatPropertyChangeHandler r0 = new com.formdev.flatlaf.ui.FlatInternalFrameTitlePane$FlatPropertyChangeHandler
            r0.<init>()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatInternalFrameTitlePane.createPropertyChangeListener():java.beans.PropertyChangeListener");
    }

    /* access modifiers changed from: protected */
    public LayoutManager createLayout() {
        return new BorderLayout(UIScale.scale(4), 0);
    }

    /* access modifiers changed from: protected */
    public void createButtons() {
        FlatInternalFrameTitlePane.super.createButtons();
        this.iconButton.setContentAreaFilled(false);
        this.maxButton.setContentAreaFilled(false);
        this.closeButton.setContentAreaFilled(false);
        Border emptyBorder = BorderFactory.createEmptyBorder();
        this.iconButton.setBorder(emptyBorder);
        this.maxButton.setBorder(emptyBorder);
        this.closeButton.setBorder(emptyBorder);
        updateButtonsVisibility();
    }

    /* access modifiers changed from: protected */
    public void addSubComponents() {
        this.titleLabel = new JLabel(this.frame.getTitle());
        this.titleLabel.setFont(FlatUIUtils.nonUIResource(getFont()));
        this.titleLabel.setMinimumSize(new Dimension(UIScale.scale(32), 1));
        updateFrameIcon();
        updateColors();
        this.buttonPanel = new JPanel() {
            /* class com.formdev.flatlaf.ui.FlatInternalFrameTitlePane.AnonymousClass1 */

            public Dimension getPreferredSize() {
                Dimension size = FlatInternalFrameTitlePane.super.getPreferredSize();
                int height = size.height;
                if (!FlatInternalFrameTitlePane.this.iconButton.isVisible()) {
                    height = Math.max(height, FlatInternalFrameTitlePane.this.iconButton.getPreferredSize().height);
                }
                if (!FlatInternalFrameTitlePane.this.maxButton.isVisible()) {
                    height = Math.max(height, FlatInternalFrameTitlePane.this.maxButton.getPreferredSize().height);
                }
                if (!FlatInternalFrameTitlePane.this.closeButton.isVisible()) {
                    height = Math.max(height, FlatInternalFrameTitlePane.this.closeButton.getPreferredSize().height);
                }
                return new Dimension(size.width, height);
            }
        };
        this.buttonPanel.setLayout(new BoxLayout(this.buttonPanel, 2));
        this.buttonPanel.setOpaque(false);
        this.buttonPanel.add(this.iconButton);
        this.buttonPanel.add(this.maxButton);
        this.buttonPanel.add(this.closeButton);
        add(this.titleLabel, "Center");
        add(this.buttonPanel, "After");
    }

    /* access modifiers changed from: protected */
    public void updateFrameIcon() {
        Icon frameIcon = this.frame.getFrameIcon();
        if (frameIcon != null && (frameIcon.getIconWidth() == 0 || frameIcon.getIconHeight() == 0)) {
            frameIcon = null;
        } else if (frameIcon instanceof ImageIcon) {
            frameIcon = new ScaledImageIcon((ImageIcon) frameIcon);
        }
        this.titleLabel.setIcon(frameIcon);
    }

    /* access modifiers changed from: protected */
    public void updateColors() {
        Color background = FlatUIUtils.nonUIResource(this.frame.isSelected() ? this.selectedTitleColor : this.notSelectedTitleColor);
        Color foreground = FlatUIUtils.nonUIResource(this.frame.isSelected() ? this.selectedTextColor : this.notSelectedTextColor);
        this.titleLabel.setForeground(foreground);
        this.iconButton.setBackground(background);
        this.iconButton.setForeground(foreground);
        this.maxButton.setBackground(background);
        this.maxButton.setForeground(foreground);
        this.closeButton.setBackground(background);
        this.closeButton.setForeground(foreground);
    }

    /* access modifiers changed from: protected */
    public void updateButtonsVisibility() {
        this.iconButton.setVisible(this.frame.isIconifiable());
        this.maxButton.setVisible(this.frame.isMaximizable());
        this.closeButton.setVisible(this.frame.isClosable());
    }

    /* access modifiers changed from: protected */
    public void assembleSystemMenu() {
    }

    /* access modifiers changed from: protected */
    public void showSystemMenu() {
    }

    public void paintComponent(Graphics g) {
        paintTitleBackground(g);
    }

    protected class FlatPropertyChangeHandler extends BasicInternalFrameTitlePane.PropertyChangeHandler {
        protected FlatPropertyChangeHandler() {
            super(FlatInternalFrameTitlePane.this);
        }

        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            char c = 65535;
            switch (propertyName.hashCode()) {
                case -737546925:
                    if (propertyName.equals("iconable")) {
                        c = 3;
                        break;
                    }
                    break;
                case 110371416:
                    if (propertyName.equals(Title.TAG_NAME)) {
                        c = 0;
                        break;
                    }
                    break;
                case 544791430:
                    if (propertyName.equals("frameIcon")) {
                        c = 1;
                        break;
                    }
                    break;
                case 1092709095:
                    if (propertyName.equals("closable")) {
                        c = 5;
                        break;
                    }
                    break;
                case 1191572123:
                    if (propertyName.equals("selected")) {
                        c = 2;
                        break;
                    }
                    break;
                case 1247047827:
                    if (propertyName.equals("componentOrientation")) {
                        c = 6;
                        break;
                    }
                    break;
                case 1354515859:
                    if (propertyName.equals("maximizable")) {
                        c = 4;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    FlatInternalFrameTitlePane.this.titleLabel.setText(FlatInternalFrameTitlePane.this.frame.getTitle());
                    break;
                case 1:
                    FlatInternalFrameTitlePane.this.updateFrameIcon();
                    break;
                case 2:
                    FlatInternalFrameTitlePane.this.updateColors();
                    break;
                case 3:
                case 4:
                case 5:
                    FlatInternalFrameTitlePane.this.updateButtonsVisibility();
                    FlatInternalFrameTitlePane.this.enableActions();
                    FlatInternalFrameTitlePane.this.revalidate();
                    FlatInternalFrameTitlePane.this.repaint();
                    return;
                case 6:
                    FlatInternalFrameTitlePane.this.applyComponentOrientation(FlatInternalFrameTitlePane.this.frame.getComponentOrientation());
                    break;
            }
            FlatInternalFrameTitlePane.super.propertyChange(e);
        }
    }
}
