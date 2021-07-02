package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.HiDPIUtils;
import com.formdev.flatlaf.util.JavaCompatibility;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.FocusListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTextFieldUI;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;

public class FlatTextFieldUI extends BasicTextFieldUI {
    private FocusListener focusListener;
    protected boolean isIntelliJTheme;
    protected int minimumWidth;
    protected Color placeholderForeground;

    public static ComponentUI createUI(JComponent c) {
        return new FlatTextFieldUI();
    }

    /* access modifiers changed from: protected */
    public void installDefaults() {
        FlatTextFieldUI.super.installDefaults();
        String prefix = getPropertyPrefix();
        this.minimumWidth = UIManager.getInt("Component.minimumWidth");
        this.isIntelliJTheme = UIManager.getBoolean("Component.isIntelliJTheme");
        this.placeholderForeground = UIManager.getColor(prefix + ".placeholderForeground");
        LookAndFeel.installProperty(getComponent(), "opaque", false);
        MigLayoutVisualPadding.install(getComponent());
    }

    /* access modifiers changed from: protected */
    public void uninstallDefaults() {
        FlatTextFieldUI.super.uninstallDefaults();
        this.placeholderForeground = null;
        MigLayoutVisualPadding.uninstall(getComponent());
    }

    /* access modifiers changed from: protected */
    public void installListeners() {
        FlatTextFieldUI.super.installListeners();
        this.focusListener = new FlatUIUtils.RepaintFocusListener(getComponent());
        getComponent().addFocusListener(this.focusListener);
    }

    /* access modifiers changed from: protected */
    public void uninstallListeners() {
        FlatTextFieldUI.super.uninstallListeners();
        getComponent().removeFocusListener(this.focusListener);
        this.focusListener = null;
    }

    /* access modifiers changed from: protected */
    public Caret createCaret() {
        return new FlatCaret(UIManager.getString("TextComponent.selectAllOnFocusPolicy"), UIManager.getBoolean("TextComponent.selectAllOnMouseClick"));
    }

    /* access modifiers changed from: protected */
    public void propertyChange(PropertyChangeEvent e) {
        FlatTextFieldUI.super.propertyChange(e);
        propertyChange(getComponent(), e);
    }

    static void propertyChange(JTextComponent c, PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();
        char c2 = 65535;
        switch (propertyName.hashCode()) {
            case -1302441837:
                if (propertyName.equals(FlatClientProperties.MINIMUM_WIDTH)) {
                    c2 = 2;
                    break;
                }
                break;
            case -742334409:
                if (propertyName.equals(FlatClientProperties.COMPONENT_ROUND_RECT)) {
                    c2 = 1;
                    break;
                }
                break;
            case 151255029:
                if (propertyName.equals(FlatClientProperties.PLACEHOLDER_TEXT)) {
                    c2 = 0;
                    break;
                }
                break;
        }
        switch (c2) {
            case 0:
            case 1:
                c.repaint();
                return;
            case 2:
                c.revalidate();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void paintSafely(Graphics g) {
        paintBackground(g, getComponent(), this.isIntelliJTheme);
        paintPlaceholder(g, getComponent(), this.placeholderForeground);
        FlatTextFieldUI.super.paintSafely(HiDPIUtils.createGraphicsTextYCorrection((Graphics2D) g));
    }

    /* access modifiers changed from: protected */
    public void paintBackground(Graphics g) {
    }

    static void paintBackground(Graphics g, JTextComponent c, boolean isIntelliJTheme2) {
        if (c.isOpaque() || FlatUIUtils.getOutsideFlatBorder(c) != null || !FlatUIUtils.hasOpaqueBeenExplicitlySet(c)) {
            float focusWidth = FlatUIUtils.getBorderFocusWidth(c);
            float arc = FlatUIUtils.getBorderArc(c);
            if (c.isOpaque() && (focusWidth > 0.0f || arc > 0.0f)) {
                FlatUIUtils.paintParentBackground(g, c);
            }
            Graphics2D g2 = g.create();
            try {
                FlatUIUtils.setRenderingHints(g2);
                Color background = c.getBackground();
                if ((background instanceof UIResource) && isIntelliJTheme2 && (!c.isEnabled() || !c.isEditable())) {
                    background = FlatUIUtils.getParentBackground(c);
                }
                g2.setColor(background);
                FlatUIUtils.paintComponentBackground(g2, 0, 0, c.getWidth(), c.getHeight(), focusWidth, arc);
            } finally {
                g2.dispose();
            }
        }
    }

    static void paintPlaceholder(Graphics g, JTextComponent c, Color placeholderForeground2) {
        JTextComponent jTextComponent;
        if (c.getDocument().getLength() <= 0) {
            Container parent = c.getParent();
            if (parent instanceof JComboBox) {
                jTextComponent = (JComboBox) parent;
            } else {
                jTextComponent = c;
            }
            Object placeholder = jTextComponent.getClientProperty(FlatClientProperties.PLACEHOLDER_TEXT);
            if (placeholder instanceof String) {
                Insets insets = c.getInsets();
                FontMetrics fm = c.getFontMetrics(c.getFont());
                int x = insets.left;
                int y = insets.top + fm.getAscent() + ((((c.getHeight() - insets.top) - insets.bottom) - fm.getHeight()) / 2);
                g.setColor(placeholderForeground2);
                FlatUIUtils.drawString(c, g, JavaCompatibility.getClippedString(jTextComponent, fm, (String) placeholder, (c.getWidth() - insets.left) - insets.right), x, y);
            }
        }
    }

    public Dimension getPreferredSize(JComponent c) {
        return applyMinimumWidth(c, FlatTextFieldUI.super.getPreferredSize(c), this.minimumWidth);
    }

    public Dimension getMinimumSize(JComponent c) {
        return applyMinimumWidth(c, FlatTextFieldUI.super.getMinimumSize(c), this.minimumWidth);
    }

    static Dimension applyMinimumWidth(JComponent c, Dimension size, int minimumWidth2) {
        if (!(c instanceof JTextField) || ((JTextField) c).getColumns() <= 0) {
            Container parent = c.getParent();
            if (!(parent instanceof JComboBox) && !(parent instanceof JSpinner) && (parent == null || !(parent.getParent() instanceof JSpinner))) {
                size.width = Math.max(size.width, UIScale.scale(FlatUIUtils.minimumWidth(c, minimumWidth2)) + Math.round(2.0f * FlatUIUtils.getBorderFocusWidth(c)));
            }
        }
        return size;
    }
}
