package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.SystemInfo;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.lang.ref.WeakReference;
import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ComboBoxEditor;
import javax.swing.DefaultListCellRenderer;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.ListCellRenderer;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;
import javax.swing.text.JTextComponent;

public class FlatComboBoxUI extends BasicComboBoxUI {
    protected String arrowType;
    protected Color borderColor;
    protected Color buttonArrowColor;
    protected Color buttonBackground;
    protected Color buttonDisabledArrowColor;
    protected Color buttonEditableBackground;
    protected Color buttonHoverArrowColor;
    protected Color buttonPressedArrowColor;
    protected String buttonStyle;
    protected Color disabledBackground;
    protected Color disabledBorderColor;
    protected Color disabledForeground;
    protected Color editableBackground;
    protected int editorColumns;
    protected boolean hover;
    private MouseListener hoverListener;
    protected boolean isIntelliJTheme;
    private WeakReference<Component> lastRendererComponent;
    protected int minimumWidth;
    protected boolean pressed;

    public static ComponentUI createUI(JComponent c) {
        return new FlatComboBoxUI();
    }

    /* access modifiers changed from: protected */
    public void installListeners() {
        FlatComboBoxUI.super.installListeners();
        this.hoverListener = new MouseAdapter() {
            /* class com.formdev.flatlaf.ui.FlatComboBoxUI.AnonymousClass1 */

            public void mouseEntered(MouseEvent e) {
                FlatComboBoxUI.this.hover = true;
                repaintArrowButton();
            }

            public void mouseExited(MouseEvent e) {
                FlatComboBoxUI.this.hover = false;
                repaintArrowButton();
            }

            public void mousePressed(MouseEvent e) {
                FlatComboBoxUI.this.pressed = true;
                repaintArrowButton();
            }

            public void mouseReleased(MouseEvent e) {
                FlatComboBoxUI.this.pressed = false;
                repaintArrowButton();
            }

            private void repaintArrowButton() {
                if (FlatComboBoxUI.this.arrowButton != null && !FlatComboBoxUI.this.comboBox.isEditable()) {
                    FlatComboBoxUI.this.arrowButton.repaint();
                }
            }
        };
        this.comboBox.addMouseListener(this.hoverListener);
    }

    /* access modifiers changed from: protected */
    public void uninstallListeners() {
        FlatComboBoxUI.super.uninstallListeners();
        this.comboBox.removeMouseListener(this.hoverListener);
        this.hoverListener = null;
    }

    /* access modifiers changed from: protected */
    public void installDefaults() {
        FlatComboBoxUI.super.installDefaults();
        LookAndFeel.installProperty(this.comboBox, "opaque", false);
        this.minimumWidth = UIManager.getInt("ComboBox.minimumWidth");
        this.editorColumns = UIManager.getInt("ComboBox.editorColumns");
        this.buttonStyle = UIManager.getString("ComboBox.buttonStyle");
        this.arrowType = UIManager.getString("Component.arrowType");
        this.isIntelliJTheme = UIManager.getBoolean("Component.isIntelliJTheme");
        this.borderColor = UIManager.getColor("Component.borderColor");
        this.disabledBorderColor = UIManager.getColor("Component.disabledBorderColor");
        this.editableBackground = UIManager.getColor("ComboBox.editableBackground");
        this.disabledBackground = UIManager.getColor("ComboBox.disabledBackground");
        this.disabledForeground = UIManager.getColor("ComboBox.disabledForeground");
        this.buttonBackground = UIManager.getColor("ComboBox.buttonBackground");
        this.buttonEditableBackground = UIManager.getColor("ComboBox.buttonEditableBackground");
        this.buttonArrowColor = UIManager.getColor("ComboBox.buttonArrowColor");
        this.buttonDisabledArrowColor = UIManager.getColor("ComboBox.buttonDisabledArrowColor");
        this.buttonHoverArrowColor = UIManager.getColor("ComboBox.buttonHoverArrowColor");
        this.buttonPressedArrowColor = UIManager.getColor("ComboBox.buttonPressedArrowColor");
        int maximumRowCount = UIManager.getInt("ComboBox.maximumRowCount");
        if (maximumRowCount > 0 && maximumRowCount != 8 && this.comboBox.getMaximumRowCount() == 8) {
            this.comboBox.setMaximumRowCount(maximumRowCount);
        }
        this.padding = UIScale.scale(this.padding);
        MigLayoutVisualPadding.install(this.comboBox);
    }

    /* access modifiers changed from: protected */
    public void uninstallDefaults() {
        FlatComboBoxUI.super.uninstallDefaults();
        this.borderColor = null;
        this.disabledBorderColor = null;
        this.editableBackground = null;
        this.disabledBackground = null;
        this.disabledForeground = null;
        this.buttonBackground = null;
        this.buttonEditableBackground = null;
        this.buttonArrowColor = null;
        this.buttonDisabledArrowColor = null;
        this.buttonHoverArrowColor = null;
        this.buttonPressedArrowColor = null;
        MigLayoutVisualPadding.uninstall(this.comboBox);
    }

    /* access modifiers changed from: protected */
    public LayoutManager createLayoutManager() {
        return new BasicComboBoxUI.ComboBoxLayoutManager() {
            /* class com.formdev.flatlaf.ui.FlatComboBoxUI.AnonymousClass2 */

            public void layoutContainer(Container parent) {
                FlatComboBoxUI.super.layoutContainer(parent);
                if (FlatComboBoxUI.this.editor != null && FlatComboBoxUI.this.padding != null) {
                    FlatComboBoxUI.this.editor.setBounds(FlatUIUtils.subtractInsets(FlatComboBoxUI.this.editor.getBounds(), FlatComboBoxUI.this.padding));
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    public FocusListener createFocusListener() {
        return new BasicComboBoxUI.FocusHandler() {
            /* class com.formdev.flatlaf.ui.FlatComboBoxUI.AnonymousClass3 */

            public void focusGained(FocusEvent e) {
                FlatComboBoxUI.super.focusGained(e);
                if (FlatComboBoxUI.this.comboBox != null && FlatComboBoxUI.this.comboBox.isEditable()) {
                    FlatComboBoxUI.this.comboBox.repaint();
                }
            }

            public void focusLost(FocusEvent e) {
                FlatComboBoxUI.super.focusLost(e);
                if (FlatComboBoxUI.this.comboBox != null && FlatComboBoxUI.this.comboBox.isEditable()) {
                    FlatComboBoxUI.this.comboBox.repaint();
                }
            }
        };
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [java.beans.PropertyChangeListener, com.formdev.flatlaf.ui.FlatComboBoxUI$4] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.beans.PropertyChangeListener createPropertyChangeListener() {
        /*
            r1 = this;
            com.formdev.flatlaf.ui.FlatComboBoxUI$4 r0 = new com.formdev.flatlaf.ui.FlatComboBoxUI$4
            r0.<init>()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatComboBoxUI.createPropertyChangeListener():java.beans.PropertyChangeListener");
    }

    /* access modifiers changed from: protected */
    public ComboPopup createPopup() {
        return new FlatComboPopup(this.comboBox);
    }

    /* access modifiers changed from: protected */
    public ComboBoxEditor createEditor() {
        ComboBoxEditor comboBoxEditor = FlatComboBoxUI.super.createEditor();
        Component editor = comboBoxEditor.getEditorComponent();
        if (editor instanceof JTextField) {
            JTextField textField = (JTextField) editor;
            textField.setColumns(this.editorColumns);
            textField.setBorder(BorderFactory.createEmptyBorder());
        }
        return comboBoxEditor;
    }

    /* access modifiers changed from: protected */
    public void configureEditor() {
        FlatComboBoxUI.super.configureEditor();
        if ((this.editor instanceof JTextField) && (this.editor.getBorder() instanceof FlatTextBorder)) {
            this.editor.setBorder(BorderFactory.createEmptyBorder());
        }
        if (this.editor instanceof JComponent) {
            this.editor.setOpaque(false);
        }
        this.editor.applyComponentOrientation(this.comboBox.getComponentOrientation());
        updateEditorColors();
        if (SystemInfo.isMacOS && (this.editor instanceof JTextComponent)) {
            InputMap inputMap = this.editor.getInputMap();
            new EditorDelegateAction(inputMap, KeyStroke.getKeyStroke("UP"));
            new EditorDelegateAction(inputMap, KeyStroke.getKeyStroke("KP_UP"));
            new EditorDelegateAction(inputMap, KeyStroke.getKeyStroke("DOWN"));
            new EditorDelegateAction(inputMap, KeyStroke.getKeyStroke("KP_DOWN"));
            new EditorDelegateAction(inputMap, KeyStroke.getKeyStroke("HOME"));
            new EditorDelegateAction(inputMap, KeyStroke.getKeyStroke("END"));
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateEditorColors() {
        boolean z;
        boolean isTextComponent = this.editor instanceof JTextComponent;
        Component component = this.editor;
        if (isTextComponent || this.editor.isEnabled()) {
            z = true;
        } else {
            z = false;
        }
        component.setForeground(FlatUIUtils.nonUIResource(getForeground(z)));
        if (isTextComponent) {
            this.editor.setDisabledTextColor(FlatUIUtils.nonUIResource(getForeground(false)));
        }
    }

    /* access modifiers changed from: protected */
    public JButton createArrowButton() {
        return new FlatComboBoxButton(this);
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
        int arrowX = this.arrowButton.getX();
        int arrowWidth = this.arrowButton.getWidth();
        boolean paintButton = (this.comboBox.isEditable() || "button".equals(this.buttonStyle)) && !"none".equals(this.buttonStyle);
        boolean enabled = this.comboBox.isEnabled();
        boolean isLeftToRight = this.comboBox.getComponentOrientation().isLeftToRight();
        g2.setColor(getBackground(enabled));
        FlatUIUtils.paintComponentBackground(g2, 0, 0, width, height, focusWidth, arc);
        if (enabled) {
            g2.setColor(paintButton ? this.buttonEditableBackground : this.buttonBackground);
            Shape oldClip = g2.getClip();
            if (isLeftToRight) {
                g2.clipRect(arrowX, 0, width - arrowX, height);
            } else {
                g2.clipRect(0, 0, arrowX + arrowWidth, height);
            }
            FlatUIUtils.paintComponentBackground(g2, 0, 0, width, height, focusWidth, arc);
            g2.setClip(oldClip);
        }
        if (paintButton) {
            g2.setColor(enabled ? this.borderColor : this.disabledBorderColor);
            float lw = UIScale.scale(1.0f);
            g2.fill(new Rectangle2D.Float(isLeftToRight ? (float) arrowX : ((float) (arrowX + arrowWidth)) - lw, focusWidth, lw, ((float) (height - 1)) - (2.0f * focusWidth)));
        }
        FlatUIUtils.resetRenderingHints(g2, oldRenderingHints);
        paint(g, c);
    }

    public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
        ListCellRenderer<Object> renderer = this.comboBox.getRenderer();
        uninstallCellPaddingBorder(renderer);
        if (renderer == null) {
            renderer = new DefaultListCellRenderer<>();
        }
        Component c = renderer.getListCellRendererComponent(this.listBox, this.comboBox.getSelectedItem(), -1, false, false);
        c.setFont(this.comboBox.getFont());
        c.applyComponentOrientation(this.comboBox.getComponentOrientation());
        uninstallCellPaddingBorder(c);
        boolean enabled = this.comboBox.isEnabled();
        c.setBackground(getBackground(enabled));
        c.setForeground(getForeground(enabled));
        boolean shouldValidate = c instanceof JPanel;
        if (this.padding != null) {
            bounds = FlatUIUtils.subtractInsets(bounds, this.padding);
        }
        Insets rendererInsets = getRendererComponentInsets(c);
        if (rendererInsets != null) {
            bounds = FlatUIUtils.addInsets(bounds, rendererInsets);
        }
        this.currentValuePane.paintComponent(g, c, this.comboBox, bounds.x, bounds.y, bounds.width, bounds.height, shouldValidate);
    }

    public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
    }

    /* access modifiers changed from: protected */
    public Color getBackground(boolean enabled) {
        return enabled ? (this.editableBackground == null || !this.comboBox.isEditable()) ? this.comboBox.getBackground() : this.editableBackground : this.isIntelliJTheme ? FlatUIUtils.getParentBackground(this.comboBox) : this.disabledBackground;
    }

    /* access modifiers changed from: protected */
    public Color getForeground(boolean enabled) {
        return enabled ? this.comboBox.getForeground() : this.disabledForeground;
    }

    public Dimension getMinimumSize(JComponent c) {
        Dimension minimumSize = FlatComboBoxUI.super.getMinimumSize(c);
        minimumSize.width = Math.max(minimumSize.width, UIScale.scale(FlatUIUtils.minimumWidth(c, this.minimumWidth)));
        return minimumSize;
    }

    /* access modifiers changed from: protected */
    public Dimension getDefaultSize() {
        ListCellRenderer<Object> renderer = this.comboBox.getRenderer();
        uninstallCellPaddingBorder(renderer);
        Dimension size = FlatComboBoxUI.super.getDefaultSize();
        uninstallCellPaddingBorder(renderer);
        return size;
    }

    /* access modifiers changed from: protected */
    public Dimension getDisplaySize() {
        ListCellRenderer<Object> renderer = this.comboBox.getRenderer();
        uninstallCellPaddingBorder(renderer);
        Dimension displaySize = FlatComboBoxUI.super.getDisplaySize();
        if (displaySize.width == this.padding.left + 100 + this.padding.right && this.comboBox.isEditable() && this.comboBox.getItemCount() == 0 && this.comboBox.getPrototypeDisplayValue() == null) {
            displaySize = new Dimension(Math.max(getDefaultSize().width, this.editor.getPreferredSize().width) + this.padding.left + this.padding.right, displaySize.height);
        }
        uninstallCellPaddingBorder(renderer);
        return displaySize;
    }

    /* access modifiers changed from: protected */
    public Dimension getSizeForComponent(Component comp) {
        Dimension size = FlatComboBoxUI.super.getSizeForComponent(comp);
        Insets rendererInsets = getRendererComponentInsets(comp);
        if (rendererInsets != null) {
            return new Dimension(size.width, (size.height - rendererInsets.top) - rendererInsets.bottom);
        }
        return size;
    }

    private Insets getRendererComponentInsets(Component rendererComponent) {
        Border rendererBorder;
        if (!(rendererComponent instanceof JComponent) || (rendererBorder = ((JComponent) rendererComponent).getBorder()) == null) {
            return null;
        }
        return rendererBorder.getBorderInsets(rendererComponent);
    }

    private void uninstallCellPaddingBorder(Object o) {
        CellPaddingBorder.uninstall(o);
        if (this.lastRendererComponent != null) {
            CellPaddingBorder.uninstall(this.lastRendererComponent);
            this.lastRendererComponent = null;
        }
    }

    protected class FlatComboBoxButton extends FlatArrowButton {
        protected FlatComboBoxButton(FlatComboBoxUI this$02) {
            this(5, this$02.arrowType, this$02.buttonArrowColor, this$02.buttonDisabledArrowColor, this$02.buttonHoverArrowColor, null, this$02.buttonPressedArrowColor, null);
        }

        protected FlatComboBoxButton(int direction, String type, Color foreground, Color disabledForeground, Color hoverForeground, Color hoverBackground, Color pressedForeground, Color pressedBackground) {
            super(direction, type, foreground, disabledForeground, hoverForeground, hoverBackground, pressedForeground, pressedBackground);
        }

        /* access modifiers changed from: protected */
        @Override // com.formdev.flatlaf.ui.FlatArrowButton
        public boolean isHover() {
            return super.isHover() || (!FlatComboBoxUI.this.comboBox.isEditable() && FlatComboBoxUI.this.hover);
        }

        /* access modifiers changed from: protected */
        @Override // com.formdev.flatlaf.ui.FlatArrowButton
        public boolean isPressed() {
            return super.isPressed() || (!FlatComboBoxUI.this.comboBox.isEditable() && FlatComboBoxUI.this.pressed);
        }
    }

    protected class FlatComboPopup extends BasicComboPopup {
        private CellPaddingBorder paddingBorder;

        protected FlatComboPopup(JComboBox combo) {
            super(combo);
            ComponentOrientation o = this.comboBox.getComponentOrientation();
            this.list.setComponentOrientation(o);
            this.scroller.setComponentOrientation(o);
            setComponentOrientation(o);
        }

        /* access modifiers changed from: protected */
        public Rectangle computePopupBounds(int px, int py, int pw, int ph) {
            int displayWidth;
            int displayWidth2 = FlatComboBoxUI.this.getDisplaySize().width;
            Border[] borderArr = {this.scroller.getViewportBorder(), this.scroller.getBorder()};
            for (Border border : borderArr) {
                if (border != null) {
                    Insets borderInsets = border.getBorderInsets((Component) null);
                    displayWidth2 += borderInsets.left + borderInsets.right;
                }
            }
            JScrollBar verticalScrollBar = this.scroller.getVerticalScrollBar();
            if (verticalScrollBar != null) {
                displayWidth2 += verticalScrollBar.getPreferredSize().width;
            }
            if (displayWidth2 > pw) {
                GraphicsConfiguration gc = this.comboBox.getGraphicsConfiguration();
                if (gc != null) {
                    Rectangle screenBounds = gc.getBounds();
                    Insets screenInsets = Toolkit.getDefaultToolkit().getScreenInsets(gc);
                    displayWidth = Math.min(displayWidth2, (screenBounds.width - screenInsets.left) - screenInsets.right);
                } else {
                    displayWidth = Math.min(displayWidth2, Toolkit.getDefaultToolkit().getScreenSize().width);
                }
                int diff = displayWidth - pw;
                pw = displayWidth;
                if (!this.comboBox.getComponentOrientation().isLeftToRight()) {
                    px -= diff;
                }
            }
            return FlatComboBoxUI.super.computePopupBounds(px, py, pw, ph);
        }

        /* access modifiers changed from: protected */
        public void configurePopup() {
            FlatComboBoxUI.super.configurePopup();
            Border border = UIManager.getBorder("PopupMenu.border");
            if (border != null) {
                setBorder(border);
            }
        }

        /* access modifiers changed from: protected */
        public void configureList() {
            FlatComboBoxUI.super.configureList();
            this.list.setCellRenderer(new PopupListCellRenderer());
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [java.beans.PropertyChangeListener, com.formdev.flatlaf.ui.FlatComboBoxUI$FlatComboPopup$1] */
        /* access modifiers changed from: protected */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.beans.PropertyChangeListener createPropertyChangeListener() {
            /*
                r1 = this;
                com.formdev.flatlaf.ui.FlatComboBoxUI$FlatComboPopup$1 r0 = new com.formdev.flatlaf.ui.FlatComboBoxUI$FlatComboPopup$1
                r0.<init>()
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatComboBoxUI.FlatComboPopup.createPropertyChangeListener():java.beans.PropertyChangeListener");
        }

        private class PopupListCellRenderer implements ListCellRenderer {
            private PopupListCellRenderer() {
            }

            public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                ListCellRenderer renderer = FlatComboPopup.this.comboBox.getRenderer();
                CellPaddingBorder.uninstall(renderer);
                CellPaddingBorder.uninstall(FlatComboBoxUI.this.lastRendererComponent);
                if (renderer == null) {
                    renderer = new DefaultListCellRenderer();
                }
                ListCellRenderer c = renderer.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                c.applyComponentOrientation(FlatComboPopup.this.comboBox.getComponentOrientation());
                if (c instanceof JComponent) {
                    if (FlatComboPopup.this.paddingBorder == null) {
                        FlatComboPopup.this.paddingBorder = new CellPaddingBorder(FlatComboBoxUI.this.padding);
                    }
                    FlatComboPopup.this.paddingBorder.install((JComponent) c);
                }
                FlatComboBoxUI.this.lastRendererComponent = c != renderer ? new WeakReference(c) : null;
                return c;
            }
        }
    }

    /* access modifiers changed from: private */
    public static class CellPaddingBorder extends AbstractBorder {
        private final Insets padding;
        private Border rendererBorder;

        CellPaddingBorder(Insets padding2) {
            this.padding = padding2;
        }

        /* access modifiers changed from: package-private */
        public void install(JComponent rendererComponent) {
            Border oldBorder = rendererComponent.getBorder();
            if (!(oldBorder instanceof CellPaddingBorder)) {
                this.rendererBorder = oldBorder;
                rendererComponent.setBorder(this);
            }
        }

        static void uninstall(Object o) {
            if (o instanceof WeakReference) {
                o = ((WeakReference) o).get();
            }
            if (o instanceof JComponent) {
                JComponent rendererComponent = (JComponent) o;
                Border border = rendererComponent.getBorder();
                if (border instanceof CellPaddingBorder) {
                    CellPaddingBorder paddingBorder = (CellPaddingBorder) border;
                    rendererComponent.setBorder(paddingBorder.rendererBorder);
                    paddingBorder.rendererBorder = null;
                }
            }
        }

        public Insets getBorderInsets(Component c, Insets insets) {
            if (this.rendererBorder != null) {
                Insets insideInsets = this.rendererBorder.getBorderInsets(c);
                insets.top = Math.max(this.padding.top, insideInsets.top);
                insets.left = Math.max(this.padding.left, insideInsets.left);
                insets.bottom = Math.max(this.padding.bottom, insideInsets.bottom);
                insets.right = Math.max(this.padding.right, insideInsets.right);
            } else {
                insets.top = this.padding.top;
                insets.left = this.padding.left;
                insets.bottom = this.padding.bottom;
                insets.right = this.padding.right;
            }
            return insets;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            if (this.rendererBorder != null) {
                this.rendererBorder.paintBorder(c, g, x, y, width, height);
            }
        }
    }

    private class EditorDelegateAction extends AbstractAction {
        private final KeyStroke keyStroke;

        EditorDelegateAction(InputMap inputMap, KeyStroke keyStroke2) {
            this.keyStroke = keyStroke2;
            inputMap.put(keyStroke2, this);
        }

        public void actionPerformed(ActionEvent e) {
            ActionListener action = FlatComboBoxUI.this.comboBox.getActionForKeyStroke(this.keyStroke);
            if (action != null) {
                action.actionPerformed(new ActionEvent(FlatComboBoxUI.this.comboBox, e.getID(), e.getActionCommand(), e.getWhen(), e.getModifiers()));
            }
        }
    }
}
