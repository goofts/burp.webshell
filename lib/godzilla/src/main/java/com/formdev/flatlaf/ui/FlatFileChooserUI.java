package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.ScaledImageIcon;
import com.formdev.flatlaf.util.UIScale;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.io.File;
import javax.swing.AbstractButton;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.filechooser.FileView;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicFileChooserUI;
import javax.swing.plaf.metal.MetalFileChooserUI;

public class FlatFileChooserUI extends MetalFileChooserUI {
    private final FlatFileView fileView = new FlatFileView();

    public static ComponentUI createUI(JComponent c) {
        return new FlatFileChooserUI((JFileChooser) c);
    }

    public FlatFileChooserUI(JFileChooser filechooser) {
        super(filechooser);
    }

    public void installComponents(JFileChooser fc) {
        FlatFileChooserUI.super.installComponents(fc);
        patchUI(fc);
    }

    private void patchUI(JFileChooser fc) {
        int maximumRowCount;
        Component topPanel = fc.getComponent(0);
        if ((topPanel instanceof JPanel) && (((JPanel) topPanel).getLayout() instanceof BorderLayout)) {
            Component topButtonPanel = ((JPanel) topPanel).getComponent(0);
            if ((topButtonPanel instanceof JPanel) && (((JPanel) topButtonPanel).getLayout() instanceof BoxLayout)) {
                Insets margin = UIManager.getInsets("Button.margin");
                Component[] comps = ((JPanel) topButtonPanel).getComponents();
                for (int i = comps.length - 1; i >= 0; i--) {
                    Component c = comps[i];
                    if ((c instanceof JButton) || (c instanceof JToggleButton)) {
                        AbstractButton b = (AbstractButton) c;
                        b.putClientProperty(FlatClientProperties.BUTTON_TYPE, FlatClientProperties.BUTTON_TYPE_TOOLBAR_BUTTON);
                        b.setMargin(margin);
                        b.setFocusable(false);
                    } else if (c instanceof Box.Filler) {
                        ((JPanel) topButtonPanel).remove(i);
                    }
                }
            }
        }
        try {
            Component directoryComboBox = ((JPanel) topPanel).getComponent(2);
            if ((directoryComboBox instanceof JComboBox) && (maximumRowCount = UIManager.getInt("ComboBox.maximumRowCount")) > 0) {
                ((JComboBox) directoryComboBox).setMaximumRowCount(maximumRowCount);
            }
        } catch (ArrayIndexOutOfBoundsException e) {
        }
    }

    public Dimension getPreferredSize(JComponent c) {
        return UIScale.scale(FlatFileChooserUI.super.getPreferredSize(c));
    }

    public Dimension getMinimumSize(JComponent c) {
        return UIScale.scale(FlatFileChooserUI.super.getMinimumSize(c));
    }

    public FileView getFileView(JFileChooser fc) {
        return this.fileView;
    }

    public void clearIconCache() {
        this.fileView.clearIconCache();
    }

    private class FlatFileView extends BasicFileChooserUI.BasicFileView {
        private FlatFileView() {
            super(FlatFileChooserUI.this);
        }

        public Icon getIcon(File f) {
            Icon icon;
            Icon icon2 = getCachedIcon(f);
            if (icon2 != null) {
                return icon2;
            }
            if (f == null || (icon = FlatFileChooserUI.this.getFileChooser().getFileSystemView().getSystemIcon(f)) == null) {
                Icon icon3 = FlatFileChooserUI.super.getIcon(f);
                if (icon3 instanceof ImageIcon) {
                    Icon icon4 = new ScaledImageIcon((ImageIcon) icon3);
                    cacheIcon(f, icon4);
                    icon3 = icon4;
                }
                return icon3;
            }
            if (icon instanceof ImageIcon) {
                icon = new ScaledImageIcon((ImageIcon) icon);
            }
            cacheIcon(f, icon);
            return icon;
        }
    }
}
