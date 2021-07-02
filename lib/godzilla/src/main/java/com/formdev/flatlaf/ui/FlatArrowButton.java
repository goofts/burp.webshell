package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.UIScale;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;
import javax.swing.JComponent;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicArrowButton;

public class FlatArrowButton extends BasicArrowButton implements UIResource {
    public static final int DEFAULT_ARROW_WIDTH = 8;
    private int arrowWidth = 8;
    protected final boolean chevron;
    protected final Color disabledForeground;
    protected final Color foreground;
    private boolean hover;
    protected final Color hoverBackground;
    protected final Color hoverForeground;
    private boolean pressed;
    protected final Color pressedBackground;
    protected final Color pressedForeground;
    private int xOffset = 0;
    private int yOffset = 0;

    public FlatArrowButton(int direction, String type, Color foreground2, Color disabledForeground2, Color hoverForeground2, Color hoverBackground2, Color pressedForeground2, Color pressedBackground2) {
        super(direction, Color.WHITE, Color.WHITE, Color.WHITE, Color.WHITE);
        this.chevron = FlatUIUtils.isChevron(type);
        this.foreground = foreground2;
        this.disabledForeground = disabledForeground2;
        this.hoverForeground = hoverForeground2;
        this.hoverBackground = hoverBackground2;
        this.pressedForeground = pressedForeground2;
        this.pressedBackground = pressedBackground2;
        setOpaque(false);
        setBorder(null);
        if (hoverForeground2 != null || hoverBackground2 != null || pressedForeground2 != null || pressedBackground2 != null) {
            addMouseListener(new MouseAdapter() {
                /* class com.formdev.flatlaf.ui.FlatArrowButton.AnonymousClass1 */

                public void mouseEntered(MouseEvent e) {
                    FlatArrowButton.this.hover = true;
                    FlatArrowButton.this.repaint();
                }

                public void mouseExited(MouseEvent e) {
                    FlatArrowButton.this.hover = false;
                    FlatArrowButton.this.repaint();
                }

                public void mousePressed(MouseEvent e) {
                    FlatArrowButton.this.pressed = true;
                    FlatArrowButton.this.repaint();
                }

                public void mouseReleased(MouseEvent e) {
                    FlatArrowButton.this.pressed = false;
                    FlatArrowButton.this.repaint();
                }
            });
        }
    }

    public int getArrowWidth() {
        return this.arrowWidth;
    }

    public void setArrowWidth(int arrowWidth2) {
        this.arrowWidth = arrowWidth2;
    }

    /* access modifiers changed from: protected */
    public boolean isHover() {
        return this.hover;
    }

    /* access modifiers changed from: protected */
    public boolean isPressed() {
        return this.pressed;
    }

    public int getXOffset() {
        return this.xOffset;
    }

    public void setXOffset(int xOffset2) {
        this.xOffset = xOffset2;
    }

    public int getYOffset() {
        return this.yOffset;
    }

    public void setYOffset(int yOffset2) {
        this.yOffset = yOffset2;
    }

    /* access modifiers changed from: protected */
    public Color deriveBackground(Color background) {
        return background;
    }

    /* access modifiers changed from: protected */
    public Color deriveForeground(Color foreground2) {
        return FlatUIUtils.deriveColor(foreground2, this.foreground);
    }

    public Dimension getPreferredSize() {
        return UIScale.scale(FlatArrowButton.super.getPreferredSize());
    }

    public Dimension getMinimumSize() {
        return UIScale.scale(FlatArrowButton.super.getMinimumSize());
    }

    public void paint(Graphics g) {
        Color background;
        Object[] oldRenderingHints = FlatUIUtils.setRenderingHints(g);
        if (isEnabled()) {
            if (this.pressedBackground == null || !isPressed()) {
                background = (this.hoverBackground == null || !isHover()) ? null : this.hoverBackground;
            } else {
                background = this.pressedBackground;
            }
            if (background != null) {
                g.setColor(deriveBackground(background));
                paintBackground((Graphics2D) g);
            }
        }
        g.setColor(deriveForeground(isEnabled() ? (this.pressedForeground == null || !isPressed()) ? (this.hoverForeground == null || !isHover()) ? this.foreground : this.hoverForeground : this.pressedForeground : this.disabledForeground));
        paintArrow((Graphics2D) g);
        FlatUIUtils.resetRenderingHints(g, oldRenderingHints);
    }

    /* access modifiers changed from: protected */
    public void paintBackground(Graphics2D g) {
        g.fillRect(0, 0, getWidth(), getHeight());
    }

    /* access modifiers changed from: protected */
    public void paintArrow(Graphics2D g) {
        int rw;
        int rh;
        int direction = getDirection();
        boolean vert = direction == 1 || direction == 5;
        int w = UIScale.scale((this.chevron ? 0 : 1) + this.arrowWidth);
        int h = UIScale.scale((this.chevron ? 0 : 1) + (this.arrowWidth / 2));
        if (vert) {
            rw = w;
        } else {
            rw = h;
        }
        if (vert) {
            rh = h;
        } else {
            rh = w;
        }
        if (this.chevron) {
            rw++;
            rh++;
        }
        int x = Math.round((((float) (getWidth() - rw)) / 2.0f) + UIScale.scale((float) this.xOffset));
        int y = Math.round((((float) (getHeight() - rh)) / 2.0f) + UIScale.scale((float) this.yOffset));
        Container parent = getParent();
        if (vert && (parent instanceof JComponent) && FlatUIUtils.hasRoundBorder((JComponent) parent)) {
            x -= UIScale.scale(parent.getComponentOrientation().isLeftToRight() ? 1 : -1);
        }
        g.translate(x, y);
        Shape arrowShape = createArrowShape(direction, this.chevron, (float) w, (float) h);
        if (this.chevron) {
            g.setStroke(new BasicStroke(UIScale.scale(1.0f)));
            g.draw(arrowShape);
        } else {
            g.fill(arrowShape);
        }
        g.translate(-x, -y);
    }

    public static Shape createArrowShape(int direction, boolean chevron2, float w, float h) {
        switch (direction) {
            case 1:
                return FlatUIUtils.createPath(!chevron2, 0.0d, (double) h, (double) (w / 2.0f), 0.0d, (double) w, (double) h);
            case 2:
            case 4:
            case 6:
            default:
                return new Path2D.Float();
            case 3:
                return FlatUIUtils.createPath(!chevron2, 0.0d, 0.0d, (double) h, (double) (w / 2.0f), 0.0d, (double) w);
            case 5:
                return FlatUIUtils.createPath(!chevron2, 0.0d, 0.0d, (double) (w / 2.0f), (double) h, (double) w, 0.0d);
            case 7:
                return FlatUIUtils.createPath(!chevron2, (double) h, 0.0d, 0.0d, (double) (w / 2.0f), (double) h, (double) w);
        }
    }
}
