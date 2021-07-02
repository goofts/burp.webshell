package com.jgoodies.forms.factories;

import com.jgoodies.common.base.Preconditions;
import com.jgoodies.common.base.Strings;
import com.jgoodies.common.swing.MnemonicUtils;
import com.jgoodies.forms.layout.Sizes;
import com.jgoodies.forms.util.FormUtils;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.LayoutManager;
import javax.accessibility.AccessibleContext;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.UIManager;

public class DefaultComponentFactory implements ComponentFactory {
    private static final DefaultComponentFactory INSTANCE = new DefaultComponentFactory();

    public static DefaultComponentFactory getInstance() {
        return INSTANCE;
    }

    @Override // com.jgoodies.forms.factories.ComponentFactory
    public JLabel createLabel(String textWithMnemonic) {
        JLabel label = new FormsLabel();
        MnemonicUtils.configure(label, textWithMnemonic);
        return label;
    }

    @Override // com.jgoodies.forms.factories.ComponentFactory
    public JLabel createReadOnlyLabel(String textWithMnemonic) {
        JLabel label = new ReadOnlyLabel();
        MnemonicUtils.configure(label, textWithMnemonic);
        return label;
    }

    @Override // com.jgoodies.forms.factories.ComponentFactory
    public JButton createButton(Action action) {
        return new JButton(action);
    }

    @Override // com.jgoodies.forms.factories.ComponentFactory
    public JLabel createTitle(String textWithMnemonic) {
        JLabel label = new TitleLabel();
        MnemonicUtils.configure(label, textWithMnemonic);
        label.setVerticalAlignment(0);
        return label;
    }

    @Override // com.jgoodies.forms.factories.ComponentFactory
    public JLabel createHeaderLabel(String markedText) {
        return createTitle(markedText);
    }

    public JComponent createSeparator(String textWithMnemonic) {
        return createSeparator(textWithMnemonic, 2);
    }

    @Override // com.jgoodies.forms.factories.ComponentFactory
    public JComponent createSeparator(String textWithMnemonic, int alignment) {
        if (Strings.isBlank(textWithMnemonic)) {
            return new JSeparator();
        }
        JLabel title = createTitle(textWithMnemonic);
        title.setHorizontalAlignment(alignment);
        return createSeparator(title);
    }

    public JComponent createSeparator(JLabel label) {
        boolean z;
        boolean z2 = true;
        Preconditions.checkNotNull(label, "The label must not be null.");
        int horizontalAlignment = label.getHorizontalAlignment();
        if (horizontalAlignment == 2 || horizontalAlignment == 0 || horizontalAlignment == 4) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "The label's horizontal alignment must be one of: LEFT, CENTER, RIGHT.");
        if (FormUtils.isLafAqua()) {
            z2 = false;
        }
        JPanel panel = new JPanel(new TitledSeparatorLayout(z2));
        panel.setOpaque(false);
        panel.add(label);
        panel.add(new JSeparator());
        if (horizontalAlignment == 0) {
            panel.add(new JSeparator());
        }
        return panel;
    }

    private static class FormsLabel extends JLabel {
        private FormsLabel() {
        }

        public AccessibleContext getAccessibleContext() {
            if (this.accessibleContext == null) {
                this.accessibleContext = new AccessibleFormsLabel();
            }
            return this.accessibleContext;
        }

        private final class AccessibleFormsLabel extends JLabel.AccessibleJLabel {
            private AccessibleFormsLabel() {
                super(FormsLabel.this);
            }

            public String getAccessibleName() {
                if (this.accessibleName != null) {
                    return this.accessibleName;
                }
                String text = FormsLabel.this.getText();
                if (text == null) {
                    return FormsLabel.super.getAccessibleName();
                }
                return text.endsWith(":") ? text.substring(0, text.length() - 1) : text;
            }
        }
    }

    private static final class ReadOnlyLabel extends FormsLabel {
        private static final String[] UIMANAGER_KEYS = {"Label.disabledForeground", "Label.disabledText", "Label[Disabled].textForeground", "textInactiveText"};

        private ReadOnlyLabel() {
            super();
        }

        public void updateUI() {
            super.updateUI();
            setForeground(getDisabledForeground());
        }

        private static Color getDisabledForeground() {
            for (String key : UIMANAGER_KEYS) {
                Color foreground = UIManager.getColor(key);
                if (foreground != null) {
                    return foreground;
                }
            }
            return null;
        }
    }

    /* access modifiers changed from: private */
    public static final class TitleLabel extends FormsLabel {
        private TitleLabel() {
            super();
        }

        public void updateUI() {
            super.updateUI();
            Color foreground = getTitleColor();
            if (foreground != null) {
                setForeground(foreground);
            }
            setFont(getTitleFont());
        }

        private static Color getTitleColor() {
            return UIManager.getColor("TitledBorder.titleColor");
        }

        private static Font getTitleFont() {
            return FormUtils.isLafAqua() ? UIManager.getFont("Label.font").deriveFont(1) : UIManager.getFont("TitledBorder.font");
        }
    }

    /* access modifiers changed from: private */
    public static final class TitledSeparatorLayout implements LayoutManager {
        private final boolean centerSeparators;

        private TitledSeparatorLayout(boolean centerSeparators2) {
            this.centerSeparators = centerSeparators2;
        }

        public void addLayoutComponent(String name, Component comp) {
        }

        public void removeLayoutComponent(Component comp) {
        }

        public Dimension minimumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }

        public Dimension preferredLayoutSize(Container parent) {
            Dimension labelSize = getLabel(parent).getPreferredSize();
            Insets insets = parent.getInsets();
            return new Dimension(labelSize.width + insets.left + insets.right, labelSize.height + insets.top + insets.bottom);
        }

        public void layoutContainer(Container parent) {
            int vOffset;
            synchronized (parent.getTreeLock()) {
                Dimension size = parent.getSize();
                Insets insets = parent.getInsets();
                int width = (size.width - insets.left) - insets.right;
                JLabel label = getLabel(parent);
                Dimension labelSize = label.getPreferredSize();
                int labelWidth = labelSize.width;
                int labelHeight = labelSize.height;
                Component separator1 = parent.getComponent(1);
                int separatorHeight = separator1.getPreferredSize().height;
                int ascent = label.getFontMetrics(label.getFont()).getMaxAscent();
                int hGap = Sizes.dialogUnitXAsPixel(this.centerSeparators ? 3 : 1, label);
                if (this.centerSeparators) {
                    vOffset = ((labelHeight - separatorHeight) / 2) + 1;
                } else {
                    vOffset = ascent - (separatorHeight / 2);
                }
                int alignment = label.getHorizontalAlignment();
                int y = insets.top;
                if (alignment == 2) {
                    int x = insets.left;
                    label.setBounds(x, y, labelWidth, labelHeight);
                    int x2 = x + labelWidth + hGap;
                    separator1.setBounds(x2, y + vOffset, (size.width - insets.right) - x2, separatorHeight);
                } else if (alignment == 4) {
                    int x3 = (insets.left + width) - labelWidth;
                    label.setBounds(x3, y, labelWidth, labelHeight);
                    separator1.setBounds(insets.left, y + vOffset, ((x3 - hGap) - 1) - insets.left, separatorHeight);
                } else {
                    int xOffset = ((width - labelWidth) - (hGap * 2)) / 2;
                    int x4 = insets.left;
                    separator1.setBounds(x4, y + vOffset, xOffset - 1, separatorHeight);
                    int x5 = x4 + xOffset + hGap;
                    label.setBounds(x5, y, labelWidth, labelHeight);
                    int x6 = x5 + labelWidth + hGap;
                    parent.getComponent(2).setBounds(x6, y + vOffset, (size.width - insets.right) - x6, separatorHeight);
                }
            }
        }

        private static JLabel getLabel(Container parent) {
            return parent.getComponent(0);
        }
    }
}
