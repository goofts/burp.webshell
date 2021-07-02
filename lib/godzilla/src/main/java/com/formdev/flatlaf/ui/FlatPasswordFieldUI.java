package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.HiDPIUtils;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPasswordFieldUI;
import javax.swing.text.Caret;
import javax.swing.text.JTextComponent;

public class FlatPasswordFieldUI extends BasicPasswordFieldUI {
    protected Icon capsLockIcon;
    private KeyListener capsLockListener;
    private FocusListener focusListener;
    protected boolean isIntelliJTheme;
    protected int minimumWidth;
    protected Color placeholderForeground;
    protected boolean showCapsLock;

    public static ComponentUI createUI(JComponent c) {
        return new FlatPasswordFieldUI();
    }

    /* access modifiers changed from: protected */
    public void installDefaults() {
        FlatPasswordFieldUI.super.installDefaults();
        String prefix = getPropertyPrefix();
        this.minimumWidth = UIManager.getInt("Component.minimumWidth");
        this.isIntelliJTheme = UIManager.getBoolean("Component.isIntelliJTheme");
        this.placeholderForeground = UIManager.getColor(prefix + ".placeholderForeground");
        this.showCapsLock = UIManager.getBoolean("PasswordField.showCapsLock");
        this.capsLockIcon = UIManager.getIcon("PasswordField.capsLockIcon");
        LookAndFeel.installProperty(getComponent(), "opaque", false);
        MigLayoutVisualPadding.install(getComponent());
    }

    /* access modifiers changed from: protected */
    public void uninstallDefaults() {
        FlatPasswordFieldUI.super.uninstallDefaults();
        this.placeholderForeground = null;
        this.capsLockIcon = null;
        MigLayoutVisualPadding.uninstall(getComponent());
    }

    /* access modifiers changed from: protected */
    public void installListeners() {
        FlatPasswordFieldUI.super.installListeners();
        this.focusListener = new FlatUIUtils.RepaintFocusListener(getComponent());
        this.capsLockListener = new KeyAdapter() {
            /* class com.formdev.flatlaf.ui.FlatPasswordFieldUI.AnonymousClass1 */

            public void keyPressed(KeyEvent e) {
                repaint(e);
            }

            public void keyReleased(KeyEvent e) {
                repaint(e);
            }

            private void repaint(KeyEvent e) {
                if (e.getKeyCode() == 20) {
                    e.getComponent().repaint();
                }
            }
        };
        getComponent().addFocusListener(this.focusListener);
        getComponent().addKeyListener(this.capsLockListener);
    }

    /* access modifiers changed from: protected */
    public void uninstallListeners() {
        FlatPasswordFieldUI.super.uninstallListeners();
        getComponent().removeFocusListener(this.focusListener);
        getComponent().removeKeyListener(this.capsLockListener);
        this.focusListener = null;
        this.capsLockListener = null;
    }

    /* access modifiers changed from: protected */
    public Caret createCaret() {
        return new FlatCaret(UIManager.getString("TextComponent.selectAllOnFocusPolicy"), UIManager.getBoolean("TextComponent.selectAllOnMouseClick"));
    }

    /* access modifiers changed from: protected */
    public void propertyChange(PropertyChangeEvent e) {
        FlatPasswordFieldUI.super.propertyChange(e);
        FlatTextFieldUI.propertyChange(getComponent(), e);
    }

    /* access modifiers changed from: protected */
    public void paintSafely(Graphics g) {
        FlatTextFieldUI.paintBackground(g, getComponent(), this.isIntelliJTheme);
        FlatTextFieldUI.paintPlaceholder(g, getComponent(), this.placeholderForeground);
        paintCapsLock(g);
        FlatPasswordFieldUI.super.paintSafely(HiDPIUtils.createGraphicsTextYCorrection((Graphics2D) g));
    }

    /* access modifiers changed from: protected */
    public void paintCapsLock(Graphics g) {
        if (this.showCapsLock) {
            JTextComponent c = getComponent();
            if (FlatUIUtils.isPermanentFocusOwner(c) && Toolkit.getDefaultToolkit().getLockingKeyState(20)) {
                int y = (c.getHeight() - this.capsLockIcon.getIconHeight()) / 2;
                this.capsLockIcon.paintIcon(c, g, (c.getWidth() - this.capsLockIcon.getIconWidth()) - y, y);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void paintBackground(Graphics g) {
    }

    public Dimension getPreferredSize(JComponent c) {
        return FlatTextFieldUI.applyMinimumWidth(c, FlatPasswordFieldUI.super.getPreferredSize(c), this.minimumWidth);
    }

    public Dimension getMinimumSize(JComponent c) {
        return FlatTextFieldUI.applyMinimumWidth(c, FlatPasswordFieldUI.super.getMinimumSize(c), this.minimumWidth);
    }
}
