package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Objects;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollBarUI;

public class FlatScrollBarUI extends BasicScrollBarUI {
    private static boolean isPressed;
    protected String arrowType;
    protected Color buttonArrowColor;
    protected Color buttonDisabledArrowColor;
    protected Color hoverButtonBackground;
    private MouseAdapter hoverListener;
    protected boolean hoverThumb;
    protected Color hoverThumbColor;
    protected boolean hoverThumbWithTrack;
    protected boolean hoverTrack;
    protected Color hoverTrackColor;
    protected Color pressedButtonBackground;
    protected Color pressedThumbColor;
    protected boolean pressedThumbWithTrack;
    protected Color pressedTrackColor;
    protected boolean showButtons;
    protected int thumbArc;
    protected Insets thumbInsets;
    protected int trackArc;
    protected Insets trackInsets;

    public static ComponentUI createUI(JComponent c) {
        return new FlatScrollBarUI();
    }

    /* access modifiers changed from: protected */
    public void installListeners() {
        FlatScrollBarUI.super.installListeners();
        this.hoverListener = new ScrollBarHoverListener();
        this.scrollbar.addMouseListener(this.hoverListener);
        this.scrollbar.addMouseMotionListener(this.hoverListener);
    }

    /* access modifiers changed from: protected */
    public void uninstallListeners() {
        FlatScrollBarUI.super.uninstallListeners();
        this.scrollbar.removeMouseListener(this.hoverListener);
        this.scrollbar.removeMouseMotionListener(this.hoverListener);
        this.hoverListener = null;
    }

    /* access modifiers changed from: protected */
    public void installDefaults() {
        FlatScrollBarUI.super.installDefaults();
        this.trackInsets = UIManager.getInsets("ScrollBar.trackInsets");
        this.thumbInsets = UIManager.getInsets("ScrollBar.thumbInsets");
        this.trackArc = UIManager.getInt("ScrollBar.trackArc");
        this.thumbArc = UIManager.getInt("ScrollBar.thumbArc");
        this.hoverTrackColor = UIManager.getColor("ScrollBar.hoverTrackColor");
        this.hoverThumbColor = UIManager.getColor("ScrollBar.hoverThumbColor");
        this.hoverThumbWithTrack = UIManager.getBoolean("ScrollBar.hoverThumbWithTrack");
        this.pressedTrackColor = UIManager.getColor("ScrollBar.pressedTrackColor");
        this.pressedThumbColor = UIManager.getColor("ScrollBar.pressedThumbColor");
        this.pressedThumbWithTrack = UIManager.getBoolean("ScrollBar.pressedThumbWithTrack");
        this.showButtons = UIManager.getBoolean("ScrollBar.showButtons");
        this.arrowType = UIManager.getString("Component.arrowType");
        this.buttonArrowColor = UIManager.getColor("ScrollBar.buttonArrowColor");
        this.buttonDisabledArrowColor = UIManager.getColor("ScrollBar.buttonDisabledArrowColor");
        this.hoverButtonBackground = UIManager.getColor("ScrollBar.hoverButtonBackground");
        this.pressedButtonBackground = UIManager.getColor("ScrollBar.pressedButtonBackground");
        if (this.trackInsets == null) {
            this.trackInsets = new Insets(0, 0, 0, 0);
        }
        if (this.thumbInsets == null) {
            this.thumbInsets = new Insets(0, 0, 0, 0);
        }
    }

    /* access modifiers changed from: protected */
    public void uninstallDefaults() {
        FlatScrollBarUI.super.uninstallDefaults();
        this.trackInsets = null;
        this.thumbInsets = null;
        this.hoverTrackColor = null;
        this.hoverThumbColor = null;
        this.pressedTrackColor = null;
        this.pressedThumbColor = null;
        this.buttonArrowColor = null;
        this.buttonDisabledArrowColor = null;
        this.hoverButtonBackground = null;
        this.pressedButtonBackground = null;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [java.beans.PropertyChangeListener, com.formdev.flatlaf.ui.FlatScrollBarUI$1] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.beans.PropertyChangeListener createPropertyChangeListener() {
        /*
            r1 = this;
            com.formdev.flatlaf.ui.FlatScrollBarUI$1 r0 = new com.formdev.flatlaf.ui.FlatScrollBarUI$1
            r0.<init>()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatScrollBarUI.createPropertyChangeListener():java.beans.PropertyChangeListener");
    }

    public Dimension getPreferredSize(JComponent c) {
        return UIScale.scale(FlatScrollBarUI.super.getPreferredSize(c));
    }

    /* access modifiers changed from: protected */
    public JButton createDecreaseButton(int orientation) {
        return new FlatScrollBarButton(this, orientation);
    }

    /* access modifiers changed from: protected */
    public JButton createIncreaseButton(int orientation) {
        return new FlatScrollBarButton(this, orientation);
    }

    /* access modifiers changed from: protected */
    public boolean isShowButtons() {
        Object showButtons2 = this.scrollbar.getClientProperty(FlatClientProperties.SCROLL_BAR_SHOW_BUTTONS);
        if (showButtons2 == null && (this.scrollbar.getParent() instanceof JScrollPane)) {
            showButtons2 = this.scrollbar.getParent().getClientProperty(FlatClientProperties.SCROLL_BAR_SHOW_BUTTONS);
        }
        return showButtons2 != null ? Objects.equals(showButtons2, true) : this.showButtons;
    }

    public void paint(Graphics g, JComponent c) {
        Object[] oldRenderingHints = FlatUIUtils.setRenderingHints(g);
        FlatScrollBarUI.super.paint(g, c);
        FlatUIUtils.resetRenderingHints(g, oldRenderingHints);
    }

    /* access modifiers changed from: protected */
    public void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
        g.setColor(getTrackColor(c, this.hoverTrack, isPressed && this.hoverTrack && !this.hoverThumb));
        paintTrackOrThumb(g, c, trackBounds, this.trackInsets, this.trackArc);
    }

    /* access modifiers changed from: protected */
    public void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
        boolean z;
        boolean z2 = true;
        if (!thumbBounds.isEmpty() && this.scrollbar.isEnabled()) {
            if (this.hoverThumb || (this.hoverThumbWithTrack && this.hoverTrack)) {
                z = true;
            } else {
                z = false;
            }
            if (!isPressed || (!this.hoverThumb && (!this.pressedThumbWithTrack || !this.hoverTrack))) {
                z2 = false;
            }
            g.setColor(getThumbColor(c, z, z2));
            paintTrackOrThumb(g, c, thumbBounds, this.thumbInsets, this.thumbArc);
        }
    }

    /* access modifiers changed from: protected */
    public void paintTrackOrThumb(Graphics g, JComponent c, Rectangle bounds, Insets insets, int arc) {
        if (this.scrollbar.getOrientation() == 0) {
            insets = new Insets(insets.right, insets.top, insets.left, insets.bottom);
        }
        Rectangle bounds2 = FlatUIUtils.subtractInsets(bounds, UIScale.scale(insets));
        if (arc <= 0) {
            g.fillRect(bounds2.x, bounds2.y, bounds2.width, bounds2.height);
            return;
        }
        int arc2 = Math.min(UIScale.scale(arc), Math.min(bounds2.width, bounds2.height));
        g.fillRoundRect(bounds2.x, bounds2.y, bounds2.width, bounds2.height, arc2, arc2);
    }

    /* access modifiers changed from: protected */
    public void paintDecreaseHighlight(Graphics g) {
    }

    /* access modifiers changed from: protected */
    public void paintIncreaseHighlight(Graphics g) {
    }

    /* access modifiers changed from: protected */
    public Color getTrackColor(JComponent c, boolean hover, boolean pressed) {
        Color trackColor = FlatUIUtils.deriveColor(this.trackColor, c.getBackground());
        if (pressed && this.pressedTrackColor != null) {
            return FlatUIUtils.deriveColor(this.pressedTrackColor, trackColor);
        }
        if (!hover || this.hoverTrackColor == null) {
            return trackColor;
        }
        return FlatUIUtils.deriveColor(this.hoverTrackColor, trackColor);
    }

    /* access modifiers changed from: protected */
    public Color getThumbColor(JComponent c, boolean hover, boolean pressed) {
        Color thumbColor = FlatUIUtils.deriveColor(this.thumbColor, FlatUIUtils.deriveColor(this.trackColor, c.getBackground()));
        if (pressed && this.pressedThumbColor != null) {
            return FlatUIUtils.deriveColor(this.pressedThumbColor, thumbColor);
        }
        if (!hover || this.hoverThumbColor == null) {
            return thumbColor;
        }
        return FlatUIUtils.deriveColor(this.hoverThumbColor, thumbColor);
    }

    /* access modifiers changed from: protected */
    public Dimension getMinimumThumbSize() {
        return UIScale.scale(FlatUIUtils.addInsets(FlatScrollBarUI.super.getMinimumThumbSize(), this.thumbInsets));
    }

    /* access modifiers changed from: protected */
    public Dimension getMaximumThumbSize() {
        return UIScale.scale(FlatUIUtils.addInsets(FlatScrollBarUI.super.getMaximumThumbSize(), this.thumbInsets));
    }

    private class ScrollBarHoverListener extends MouseAdapter {
        private ScrollBarHoverListener() {
        }

        public void mouseExited(MouseEvent e) {
            if (!FlatScrollBarUI.isPressed) {
                FlatScrollBarUI flatScrollBarUI = FlatScrollBarUI.this;
                FlatScrollBarUI.this.hoverThumb = false;
                flatScrollBarUI.hoverTrack = false;
                repaint();
            }
        }

        public void mouseMoved(MouseEvent e) {
            if (!FlatScrollBarUI.isPressed) {
                update(e.getX(), e.getY());
            }
        }

        public void mousePressed(MouseEvent e) {
            boolean unused = FlatScrollBarUI.isPressed = true;
            repaint();
        }

        public void mouseReleased(MouseEvent e) {
            boolean unused = FlatScrollBarUI.isPressed = false;
            repaint();
            update(e.getX(), e.getY());
        }

        private void update(int x, int y) {
            boolean inTrack = FlatScrollBarUI.this.getTrackBounds().contains(x, y);
            boolean inThumb = FlatScrollBarUI.this.getThumbBounds().contains(x, y);
            if (inTrack != FlatScrollBarUI.this.hoverTrack || inThumb != FlatScrollBarUI.this.hoverThumb) {
                FlatScrollBarUI.this.hoverTrack = inTrack;
                FlatScrollBarUI.this.hoverThumb = inThumb;
                repaint();
            }
        }

        private void repaint() {
            if (FlatScrollBarUI.this.scrollbar.isEnabled()) {
                FlatScrollBarUI.this.scrollbar.repaint();
            }
        }
    }

    protected class FlatScrollBarButton extends FlatArrowButton {
        protected FlatScrollBarButton(FlatScrollBarUI this$02, int direction) {
            this(direction, this$02.arrowType, this$02.buttonArrowColor, this$02.buttonDisabledArrowColor, null, this$02.hoverButtonBackground, null, this$02.pressedButtonBackground);
        }

        protected FlatScrollBarButton(int direction, String type, Color foreground, Color disabledForeground, Color hoverForeground, Color hoverBackground, Color pressedForeground, Color pressedBackground) {
            super(direction, type, foreground, disabledForeground, hoverForeground, hoverBackground, pressedForeground, pressedBackground);
            setArrowWidth(6);
            setFocusable(false);
            setRequestFocusEnabled(false);
        }

        /* access modifiers changed from: protected */
        @Override // com.formdev.flatlaf.ui.FlatArrowButton
        public Color deriveBackground(Color background) {
            return FlatUIUtils.deriveColor(background, FlatScrollBarUI.this.scrollbar.getBackground());
        }

        @Override // com.formdev.flatlaf.ui.FlatArrowButton
        public Dimension getPreferredSize() {
            if (!FlatScrollBarUI.this.isShowButtons()) {
                return new Dimension();
            }
            int w = UIScale.scale(FlatScrollBarUI.this.scrollBarWidth);
            return new Dimension(w, w);
        }

        @Override // com.formdev.flatlaf.ui.FlatArrowButton
        public Dimension getMinimumSize() {
            return FlatScrollBarUI.this.isShowButtons() ? super.getMinimumSize() : new Dimension();
        }

        public Dimension getMaximumSize() {
            return FlatScrollBarUI.this.isShowButtons() ? super.getMaximumSize() : new Dimension();
        }
    }
}
