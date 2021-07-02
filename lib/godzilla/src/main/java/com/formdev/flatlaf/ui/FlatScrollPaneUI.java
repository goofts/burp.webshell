package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.PropertyChangeEvent;
import javax.swing.JComponent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.LookAndFeel;
import javax.swing.Scrollable;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicScrollPaneUI;

public class FlatScrollPaneUI extends BasicScrollPaneUI {
    private Handler handler;

    public static ComponentUI createUI(JComponent c) {
        return new FlatScrollPaneUI();
    }

    public void installUI(JComponent c) {
        FlatScrollPaneUI.super.installUI(c);
        LookAndFeel.installProperty(c, "opaque", Boolean.valueOf(UIManager.getInt("Component.focusWidth") == 0));
        MigLayoutVisualPadding.install(this.scrollpane);
    }

    public void uninstallUI(JComponent c) {
        MigLayoutVisualPadding.uninstall(this.scrollpane);
        FlatScrollPaneUI.super.uninstallUI(c);
    }

    /* access modifiers changed from: protected */
    public void installListeners(JScrollPane c) {
        FlatScrollPaneUI.super.installListeners(c);
        addViewportListeners(this.scrollpane.getViewport());
    }

    /* access modifiers changed from: protected */
    public void uninstallListeners(JComponent c) {
        FlatScrollPaneUI.super.uninstallListeners(c);
        removeViewportListeners(this.scrollpane.getViewport());
        this.handler = null;
    }

    /* access modifiers changed from: protected */
    public MouseWheelListener createMouseWheelListener() {
        return new BasicScrollPaneUI.MouseWheelHandler() {
            /* class com.formdev.flatlaf.ui.FlatScrollPaneUI.AnonymousClass1 */

            public void mouseWheelMoved(MouseWheelEvent e) {
                if (!FlatScrollPaneUI.this.isSmoothScrollingEnabled() || !FlatScrollPaneUI.this.scrollpane.isWheelScrollingEnabled() || e.getScrollType() != 0 || e.getPreciseWheelRotation() == 0.0d || e.getPreciseWheelRotation() == ((double) e.getWheelRotation())) {
                    FlatScrollPaneUI.super.mouseWheelMoved(e);
                } else {
                    FlatScrollPaneUI.this.mouseWheelMovedSmooth(e);
                }
            }
        };
    }

    /* access modifiers changed from: protected */
    public boolean isSmoothScrollingEnabled() {
        Object smoothScrolling = this.scrollpane.getClientProperty(FlatClientProperties.SCROLL_PANE_SMOOTH_SCROLLING);
        if (smoothScrolling instanceof Boolean) {
            return ((Boolean) smoothScrolling).booleanValue();
        }
        return UIManager.getBoolean("ScrollPane.smoothScrolling");
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void mouseWheelMovedSmooth(MouseWheelEvent e) {
        int unitIncrement;
        int viewportWH;
        JViewport viewport = this.scrollpane.getViewport();
        if (viewport != null) {
            JScrollBar scrollbar = this.scrollpane.getVerticalScrollBar();
            if ((scrollbar != null && scrollbar.isVisible() && !e.isShiftDown()) || ((scrollbar = this.scrollpane.getHorizontalScrollBar()) != null && scrollbar.isVisible())) {
                e.consume();
                double rotation = e.getPreciseWheelRotation();
                int orientation = scrollbar.getOrientation();
                Component view = viewport.getView();
                if (view instanceof Scrollable) {
                    Scrollable scrollable = (Scrollable) view;
                    Rectangle visibleRect = new Rectangle(viewport.getViewSize());
                    unitIncrement = scrollable.getScrollableUnitIncrement(visibleRect, orientation, 1);
                    if (unitIncrement > 0) {
                        if (orientation == 1) {
                            visibleRect.y += unitIncrement;
                            visibleRect.height -= unitIncrement;
                        } else {
                            visibleRect.x += unitIncrement;
                            visibleRect.width -= unitIncrement;
                        }
                        int unitIncrement2 = scrollable.getScrollableUnitIncrement(visibleRect, orientation, 1);
                        if (unitIncrement2 > 0) {
                            unitIncrement = Math.min(unitIncrement, unitIncrement2);
                        }
                    }
                } else {
                    unitIncrement = scrollbar.getUnitIncrement(rotation < 0.0d ? -1 : 1);
                }
                if (orientation == 1) {
                    viewportWH = viewport.getHeight();
                } else {
                    viewportWH = viewport.getWidth();
                }
                int idelta = (int) Math.round(rotation * ((double) Math.min(e.getScrollAmount() * unitIncrement, viewportWH)));
                if (idelta == 0) {
                    if (rotation > 0.0d) {
                        idelta = 1;
                    } else if (rotation < 0.0d) {
                        idelta = -1;
                    }
                }
                int value = scrollbar.getValue();
                int newValue = Math.max(scrollbar.getMinimum(), Math.min(value + idelta, scrollbar.getMaximum() - scrollbar.getModel().getExtent()));
                if (newValue != value) {
                    scrollbar.setValue(newValue);
                }
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.formdev.flatlaf.ui.FlatScrollPaneUI$2, java.beans.PropertyChangeListener] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.beans.PropertyChangeListener createPropertyChangeListener() {
        /*
            r1 = this;
            com.formdev.flatlaf.ui.FlatScrollPaneUI$2 r0 = new com.formdev.flatlaf.ui.FlatScrollPaneUI$2
            r0.<init>()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatScrollPaneUI.createPropertyChangeListener():java.beans.PropertyChangeListener");
    }

    private Handler getHandler() {
        if (this.handler == null) {
            this.handler = new Handler();
        }
        return this.handler;
    }

    /* access modifiers changed from: protected */
    public void updateViewport(PropertyChangeEvent e) {
        FlatScrollPaneUI.super.updateViewport(e);
        removeViewportListeners((JViewport) e.getOldValue());
        addViewportListeners((JViewport) e.getNewValue());
    }

    private void addViewportListeners(JViewport viewport) {
        if (viewport != null) {
            viewport.addContainerListener(getHandler());
            Component view = viewport.getView();
            if (view != null) {
                view.addFocusListener(getHandler());
            }
        }
    }

    private void removeViewportListeners(JViewport viewport) {
        if (viewport != null) {
            viewport.removeContainerListener(getHandler());
            Component view = viewport.getView();
            if (view != null) {
                view.removeFocusListener(getHandler());
            }
        }
    }

    public void update(Graphics g, JComponent c) {
        if (c.isOpaque()) {
            FlatUIUtils.paintParentBackground(g, c);
            Insets insets = c.getInsets();
            g.setColor(c.getBackground());
            g.fillRect(insets.left, insets.top, (c.getWidth() - insets.left) - insets.right, (c.getHeight() - insets.top) - insets.bottom);
        }
        paint(g, c);
    }

    /* access modifiers changed from: private */
    public class Handler implements ContainerListener, FocusListener {
        private Handler() {
        }

        public void componentAdded(ContainerEvent e) {
            e.getChild().addFocusListener(this);
        }

        public void componentRemoved(ContainerEvent e) {
            e.getChild().removeFocusListener(this);
        }

        public void focusGained(FocusEvent e) {
            FlatScrollPaneUI.this.scrollpane.repaint();
        }

        public void focusLost(FocusEvent e) {
            FlatScrollPaneUI.this.scrollpane.repaint();
        }
    }
}
