package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Rectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSpinnerUI;

public class FlatSpinnerUI extends BasicSpinnerUI {
    protected String arrowType;
    protected Color borderColor;
    protected Color buttonArrowColor;
    protected Color buttonBackground;
    protected Color buttonDisabledArrowColor;
    protected Color buttonHoverArrowColor;
    protected Color buttonPressedArrowColor;
    protected String buttonStyle;
    protected Color disabledBackground;
    protected Color disabledBorderColor;
    protected Color disabledForeground;
    private Handler handler;
    protected boolean isIntelliJTheme;
    protected int minimumWidth;
    protected Insets padding;

    public static ComponentUI createUI(JComponent c) {
        return new FlatSpinnerUI();
    }

    /* access modifiers changed from: protected */
    public void installDefaults() {
        FlatSpinnerUI.super.installDefaults();
        LookAndFeel.installProperty(this.spinner, "opaque", false);
        this.minimumWidth = UIManager.getInt("Component.minimumWidth");
        this.buttonStyle = UIManager.getString("Spinner.buttonStyle");
        this.arrowType = UIManager.getString("Component.arrowType");
        this.isIntelliJTheme = UIManager.getBoolean("Component.isIntelliJTheme");
        this.borderColor = UIManager.getColor("Component.borderColor");
        this.disabledBorderColor = UIManager.getColor("Component.disabledBorderColor");
        this.disabledBackground = UIManager.getColor("Spinner.disabledBackground");
        this.disabledForeground = UIManager.getColor("Spinner.disabledForeground");
        this.buttonBackground = UIManager.getColor("Spinner.buttonBackground");
        this.buttonArrowColor = UIManager.getColor("Spinner.buttonArrowColor");
        this.buttonDisabledArrowColor = UIManager.getColor("Spinner.buttonDisabledArrowColor");
        this.buttonHoverArrowColor = UIManager.getColor("Spinner.buttonHoverArrowColor");
        this.buttonPressedArrowColor = UIManager.getColor("Spinner.buttonPressedArrowColor");
        this.padding = UIManager.getInsets("Spinner.padding");
        this.padding = UIScale.scale(this.padding);
        MigLayoutVisualPadding.install(this.spinner);
    }

    /* access modifiers changed from: protected */
    public void uninstallDefaults() {
        FlatSpinnerUI.super.uninstallDefaults();
        this.borderColor = null;
        this.disabledBorderColor = null;
        this.disabledBackground = null;
        this.disabledForeground = null;
        this.buttonBackground = null;
        this.buttonArrowColor = null;
        this.buttonDisabledArrowColor = null;
        this.buttonHoverArrowColor = null;
        this.buttonPressedArrowColor = null;
        this.padding = null;
        MigLayoutVisualPadding.uninstall(this.spinner);
    }

    /* access modifiers changed from: protected */
    public void installListeners() {
        FlatSpinnerUI.super.installListeners();
        addEditorFocusListener(this.spinner.getEditor());
        this.spinner.addFocusListener(getHandler());
        this.spinner.addPropertyChangeListener(getHandler());
    }

    /* access modifiers changed from: protected */
    public void uninstallListeners() {
        FlatSpinnerUI.super.uninstallListeners();
        removeEditorFocusListener(this.spinner.getEditor());
        this.spinner.removeFocusListener(getHandler());
        this.spinner.removePropertyChangeListener(getHandler());
        this.handler = null;
    }

    private Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    /* access modifiers changed from: protected */
    public JComponent createEditor() {
        JComponent editor = FlatSpinnerUI.super.createEditor();
        editor.setOpaque(false);
        JTextField textField = getEditorTextField(editor);
        if (textField != null) {
            textField.setOpaque(false);
        }
        updateEditorColors();
        return editor;
    }

    /* access modifiers changed from: protected */
    public void replaceEditor(JComponent oldEditor, JComponent newEditor) {
        FlatSpinnerUI.super.replaceEditor(oldEditor, newEditor);
        removeEditorFocusListener(oldEditor);
        addEditorFocusListener(newEditor);
        updateEditorColors();
    }

    private void addEditorFocusListener(JComponent editor) {
        JTextField textField = getEditorTextField(editor);
        if (textField != null) {
            textField.addFocusListener(getHandler());
        }
    }

    private void removeEditorFocusListener(JComponent editor) {
        JTextField textField = getEditorTextField(editor);
        if (textField != null) {
            textField.removeFocusListener(getHandler());
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateEditorColors() {
        JTextField textField = getEditorTextField(this.spinner.getEditor());
        if (textField != null) {
            textField.setForeground(FlatUIUtils.nonUIResource(getForeground(true)));
            textField.setDisabledTextColor(FlatUIUtils.nonUIResource(getForeground(false)));
        }
    }

    /* access modifiers changed from: private */
    public static JTextField getEditorTextField(JComponent editor) {
        if (editor instanceof JSpinner.DefaultEditor) {
            return ((JSpinner.DefaultEditor) editor).getTextField();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public Color getBackground(boolean enabled) {
        if (enabled) {
            return this.spinner.getBackground();
        }
        return this.isIntelliJTheme ? FlatUIUtils.getParentBackground(this.spinner) : this.disabledBackground;
    }

    /* access modifiers changed from: protected */
    public Color getForeground(boolean enabled) {
        return enabled ? this.spinner.getForeground() : this.disabledForeground;
    }

    /* access modifiers changed from: protected */
    public LayoutManager createLayout() {
        return getHandler();
    }

    /* access modifiers changed from: protected */
    public Component createNextButton() {
        return createArrowButton(1, "Spinner.nextButton");
    }

    /* access modifiers changed from: protected */
    public Component createPreviousButton() {
        return createArrowButton(5, "Spinner.previousButton");
    }

    private Component createArrowButton(int direction, String name) {
        FlatArrowButton button = new FlatArrowButton(direction, this.arrowType, this.buttonArrowColor, this.buttonDisabledArrowColor, this.buttonHoverArrowColor, null, this.buttonPressedArrowColor, null);
        button.setName(name);
        button.setYOffset(direction == 1 ? 1 : -1);
        if (direction == 1) {
            installNextButtonListeners(button);
        } else {
            installPreviousButtonListeners(button);
        }
        return button;
    }

    public void update(Graphics g, JComponent c) {
        float focusWidth = FlatUIUtils.getBorderFocusWidth(c);
        float arc = FlatUIUtils.getBorderArc(c);
        if (c.isOpaque() && (focusWidth > 0.0f || arc > 0.0f)) {
            FlatUIUtils.paintParentBackground(g, c);
        }
        Graphics2D g2 = (Graphics2D) g;
        Object[] oldRenderingHints = FlatUIUtils.setRenderingHints(g2);
        int width = c.getWidth();
        int height = c.getHeight();
        boolean enabled = this.spinner.isEnabled();
        g2.setColor(getBackground(enabled));
        FlatUIUtils.paintComponentBackground(g2, 0, 0, width, height, focusWidth, arc);
        boolean paintButton = !"none".equals(this.buttonStyle);
        Handler handler2 = getHandler();
        if (paintButton && !(handler2.nextButton == null && handler2.previousButton == null)) {
            Component button = handler2.nextButton != null ? handler2.nextButton : handler2.previousButton;
            int arrowX = button.getX();
            int arrowWidth = button.getWidth();
            boolean isLeftToRight = this.spinner.getComponentOrientation().isLeftToRight();
            if (enabled) {
                g2.setColor(this.buttonBackground);
                Shape oldClip = g2.getClip();
                if (isLeftToRight) {
                    g2.clipRect(arrowX, 0, width - arrowX, height);
                } else {
                    g2.clipRect(0, 0, arrowX + arrowWidth, height);
                }
                FlatUIUtils.paintComponentBackground(g2, 0, 0, width, height, focusWidth, arc);
                g2.setClip(oldClip);
            }
            g2.setColor(enabled ? this.borderColor : this.disabledBorderColor);
            float lw = UIScale.scale(1.0f);
            g2.fill(new Rectangle2D.Float(isLeftToRight ? (float) arrowX : ((float) (arrowX + arrowWidth)) - lw, focusWidth, lw, ((float) (height - 1)) - (2.0f * focusWidth)));
        }
        paint(g, c);
        FlatUIUtils.resetRenderingHints(g, oldRenderingHints);
    }

    /* access modifiers changed from: private */
    public class Handler implements LayoutManager, FocusListener, PropertyChangeListener {
        private Component editor;
        private Component nextButton;
        private Component previousButton;

        private Handler() {
            this.editor = null;
        }

        public void addLayoutComponent(String name, Component c) {
            char c2 = 65535;
            switch (name.hashCode()) {
                case -1209131241:
                    if (name.equals("Previous")) {
                        c2 = 2;
                        break;
                    }
                    break;
                case 2424595:
                    if (name.equals("Next")) {
                        c2 = 1;
                        break;
                    }
                    break;
                case 2071006605:
                    if (name.equals("Editor")) {
                        c2 = 0;
                        break;
                    }
                    break;
            }
            switch (c2) {
                case 0:
                    this.editor = c;
                    return;
                case 1:
                    this.nextButton = c;
                    return;
                case 2:
                    this.previousButton = c;
                    return;
                default:
                    return;
            }
        }

        public void removeLayoutComponent(Component c) {
            if (c == this.editor) {
                this.editor = null;
            } else if (c == this.nextButton) {
                this.nextButton = null;
            } else if (c == this.previousButton) {
                this.previousButton = null;
            }
        }

        public Dimension preferredLayoutSize(Container parent) {
            Insets insets = parent.getInsets();
            Dimension editorSize = this.editor != null ? this.editor.getPreferredSize() : new Dimension(0, 0);
            int minimumWidth = FlatUIUtils.minimumWidth(FlatSpinnerUI.this.spinner, FlatSpinnerUI.this.minimumWidth);
            int innerHeight = editorSize.height + FlatSpinnerUI.this.padding.top + FlatSpinnerUI.this.padding.bottom;
            return new Dimension(Math.max(insets.left + insets.right + editorSize.width + FlatSpinnerUI.this.padding.left + FlatSpinnerUI.this.padding.right + innerHeight, UIScale.scale(minimumWidth) + Math.round(2.0f * FlatUIUtils.getBorderFocusWidth(FlatSpinnerUI.this.spinner))), insets.top + insets.bottom + innerHeight);
        }

        public Dimension minimumLayoutSize(Container parent) {
            return preferredLayoutSize(parent);
        }

        public void layoutContainer(Container parent) {
            Rectangle r = FlatUIUtils.subtractInsets(new Rectangle(parent.getSize()), parent.getInsets());
            if (this.nextButton != null || this.previousButton != null) {
                Rectangle editorRect = new Rectangle(r);
                Rectangle buttonsRect = new Rectangle(r);
                int buttonsWidth = r.height;
                buttonsRect.width = buttonsWidth;
                if (parent.getComponentOrientation().isLeftToRight()) {
                    editorRect.width -= buttonsWidth;
                    buttonsRect.x += editorRect.width;
                } else {
                    editorRect.x += buttonsWidth;
                    editorRect.width -= buttonsWidth;
                }
                if (this.editor != null) {
                    this.editor.setBounds(FlatUIUtils.subtractInsets(editorRect, FlatSpinnerUI.this.padding));
                }
                int nextHeight = (buttonsRect.height / 2) + (buttonsRect.height % 2);
                if (this.nextButton != null) {
                    this.nextButton.setBounds(buttonsRect.x, buttonsRect.y, buttonsRect.width, nextHeight);
                }
                if (this.previousButton != null) {
                    this.previousButton.setBounds(buttonsRect.x, (buttonsRect.y + buttonsRect.height) - nextHeight, buttonsRect.width, nextHeight);
                }
            } else if (this.editor != null) {
                this.editor.setBounds(FlatUIUtils.subtractInsets(r, FlatSpinnerUI.this.padding));
            }
        }

        public void focusGained(FocusEvent e) {
            JTextField textField;
            FlatSpinnerUI.this.spinner.repaint();
            if (e.getComponent() == FlatSpinnerUI.this.spinner && (textField = FlatSpinnerUI.getEditorTextField(FlatSpinnerUI.this.spinner.getEditor())) != null) {
                textField.requestFocusInWindow();
            }
        }

        public void focusLost(FocusEvent e) {
            FlatSpinnerUI.this.spinner.repaint();
        }

        public void propertyChange(PropertyChangeEvent e) {
            String propertyName = e.getPropertyName();
            char c = 65535;
            switch (propertyName.hashCode()) {
                case -1609594047:
                    if (propertyName.equals("enabled")) {
                        c = 1;
                        break;
                    }
                    break;
                case -1302441837:
                    if (propertyName.equals(FlatClientProperties.MINIMUM_WIDTH)) {
                        c = 3;
                        break;
                    }
                    break;
                case -742334409:
                    if (propertyName.equals(FlatClientProperties.COMPONENT_ROUND_RECT)) {
                        c = 2;
                        break;
                    }
                    break;
                case 1984457027:
                    if (propertyName.equals("foreground")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                case 1:
                    FlatSpinnerUI.this.updateEditorColors();
                    return;
                case 2:
                    FlatSpinnerUI.this.spinner.repaint();
                    return;
                case 3:
                    FlatSpinnerUI.this.spinner.revalidate();
                    return;
                default:
                    return;
            }
        }
    }
}
