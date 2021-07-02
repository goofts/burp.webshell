package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;

public class FlatSplitPaneUI extends BasicSplitPaneUI {
    protected String arrowType;
    private Boolean continuousLayout;
    protected Color oneTouchArrowColor;
    protected Color oneTouchHoverArrowColor;
    protected Color oneTouchPressedArrowColor;

    public static ComponentUI createUI(JComponent c) {
        return new FlatSplitPaneUI();
    }

    /* access modifiers changed from: protected */
    public void installDefaults() {
        this.arrowType = UIManager.getString("Component.arrowType");
        this.oneTouchArrowColor = UIManager.getColor("SplitPaneDivider.oneTouchArrowColor");
        this.oneTouchHoverArrowColor = UIManager.getColor("SplitPaneDivider.oneTouchHoverArrowColor");
        this.oneTouchPressedArrowColor = UIManager.getColor("SplitPaneDivider.oneTouchPressedArrowColor");
        FlatSplitPaneUI.super.installDefaults();
        this.continuousLayout = (Boolean) UIManager.get("SplitPane.continuousLayout");
    }

    /* access modifiers changed from: protected */
    public void uninstallDefaults() {
        FlatSplitPaneUI.super.uninstallDefaults();
        this.oneTouchArrowColor = null;
        this.oneTouchHoverArrowColor = null;
        this.oneTouchPressedArrowColor = null;
    }

    public boolean isContinuousLayout() {
        return FlatSplitPaneUI.super.isContinuousLayout() || (this.continuousLayout != null && Boolean.TRUE.equals(this.continuousLayout));
    }

    public BasicSplitPaneDivider createDefaultDivider() {
        return new FlatSplitPaneDivider(this);
    }

    protected class FlatSplitPaneDivider extends BasicSplitPaneDivider {
        protected final Color gripColor = UIManager.getColor("SplitPaneDivider.gripColor");
        protected final int gripDotCount = FlatUIUtils.getUIInt("SplitPaneDivider.gripDotCount", 3);
        protected final int gripDotSize = FlatUIUtils.getUIInt("SplitPaneDivider.gripDotSize", 3);
        protected final int gripGap = FlatUIUtils.getUIInt("SplitPaneDivider.gripGap", 2);
        protected final String style = UIManager.getString("SplitPaneDivider.style");

        protected FlatSplitPaneDivider(BasicSplitPaneUI ui) {
            super(ui);
            setLayout(new FlatDividerLayout());
        }

        public void setDividerSize(int newSize) {
            FlatSplitPaneUI.super.setDividerSize(UIScale.scale(newSize));
        }

        /* access modifiers changed from: protected */
        public JButton createLeftOneTouchButton() {
            return new FlatOneTouchButton(true);
        }

        /* access modifiers changed from: protected */
        public JButton createRightOneTouchButton() {
            return new FlatOneTouchButton(false);
        }

        public void propertyChange(PropertyChangeEvent e) {
            FlatSplitPaneUI.super.propertyChange(e);
            String propertyName = e.getPropertyName();
            char c = 65535;
            switch (propertyName.hashCode()) {
                case -605950482:
                    if (propertyName.equals("dividerLocation")) {
                        c = 0;
                        break;
                    }
                    break;
            }
            switch (c) {
                case 0:
                    revalidate();
                    return;
                default:
                    return;
            }
        }

        public void paint(Graphics g) {
            FlatSplitPaneUI.super.paint(g);
            if (!"plain".equals(this.style)) {
                Object[] oldRenderingHints = FlatUIUtils.setRenderingHints(g);
                g.setColor(this.gripColor);
                paintGrip(g, 0, 0, getWidth(), getHeight());
                FlatUIUtils.resetRenderingHints(g, oldRenderingHints);
            }
        }

        /* access modifiers changed from: protected */
        public void paintGrip(Graphics g, int x, int y, int width, int height) {
            FlatUIUtils.paintGrip(g, x, y, width, height, this.splitPane.getOrientation() == 0, this.gripDotCount, this.gripDotSize, this.gripGap, true);
        }

        /* access modifiers changed from: protected */
        public boolean isLeftCollapsed() {
            int location = this.splitPane.getDividerLocation();
            Insets insets = this.splitPane.getInsets();
            return this.orientation == 0 ? location == insets.top : location == insets.left;
        }

        /* access modifiers changed from: protected */
        public boolean isRightCollapsed() {
            int location = this.splitPane.getDividerLocation();
            Insets insets = this.splitPane.getInsets();
            return this.orientation == 0 ? location == (this.splitPane.getHeight() - getHeight()) - insets.bottom : location == (this.splitPane.getWidth() - getWidth()) - insets.right;
        }

        protected class FlatOneTouchButton extends FlatArrowButton {
            protected final boolean left;

            protected FlatOneTouchButton(boolean left2) {
                super(1, FlatSplitPaneUI.this.arrowType, FlatSplitPaneUI.this.oneTouchArrowColor, null, FlatSplitPaneUI.this.oneTouchHoverArrowColor, null, FlatSplitPaneUI.this.oneTouchPressedArrowColor, null);
                setCursor(Cursor.getPredefinedCursor(0));
                ToolTipManager.sharedInstance().registerComponent(this);
                this.left = left2;
            }

            public int getDirection() {
                return FlatSplitPaneDivider.this.orientation == 0 ? this.left ? 1 : 5 : this.left ? 7 : 3;
            }

            public String getToolTipText(MouseEvent e) {
                String key;
                if (FlatSplitPaneDivider.this.orientation == 0) {
                    key = this.left ? FlatSplitPaneDivider.this.isRightCollapsed() ? "SplitPaneDivider.expandBottomToolTipText" : "SplitPaneDivider.collapseTopToolTipText" : FlatSplitPaneDivider.this.isLeftCollapsed() ? "SplitPaneDivider.expandTopToolTipText" : "SplitPaneDivider.collapseBottomToolTipText";
                } else if (this.left) {
                    key = FlatSplitPaneDivider.this.isRightCollapsed() ? "SplitPaneDivider.expandRightToolTipText" : "SplitPaneDivider.collapseLeftToolTipText";
                } else {
                    key = FlatSplitPaneDivider.this.isLeftCollapsed() ? "SplitPaneDivider.expandLeftToolTipText" : "SplitPaneDivider.collapseRightToolTipText";
                }
                Object value = FlatSplitPaneDivider.this.splitPane.getClientProperty(key);
                if (value instanceof String) {
                    return (String) value;
                }
                return UIManager.getString(key, getLocale());
            }
        }

        protected class FlatDividerLayout extends BasicSplitPaneDivider.DividerLayout {
            protected FlatDividerLayout() {
                super(FlatSplitPaneDivider.this);
            }

            public void layoutContainer(Container c) {
                boolean z;
                boolean z2 = true;
                FlatSplitPaneDivider.super.layoutContainer(c);
                if (FlatSplitPaneDivider.this.leftButton != null && FlatSplitPaneDivider.this.rightButton != null && FlatSplitPaneDivider.this.splitPane.isOneTouchExpandable()) {
                    int extraSize = UIScale.scale(4);
                    if (FlatSplitPaneDivider.this.orientation == 0) {
                        FlatSplitPaneDivider.this.leftButton.setSize(FlatSplitPaneDivider.this.leftButton.getWidth() + extraSize, FlatSplitPaneDivider.this.leftButton.getHeight());
                        FlatSplitPaneDivider.this.rightButton.setBounds(FlatSplitPaneDivider.this.leftButton.getX() + FlatSplitPaneDivider.this.leftButton.getWidth(), FlatSplitPaneDivider.this.rightButton.getY(), FlatSplitPaneDivider.this.rightButton.getWidth() + extraSize, FlatSplitPaneDivider.this.rightButton.getHeight());
                    } else {
                        FlatSplitPaneDivider.this.leftButton.setSize(FlatSplitPaneDivider.this.leftButton.getWidth(), FlatSplitPaneDivider.this.leftButton.getHeight() + extraSize);
                        FlatSplitPaneDivider.this.rightButton.setBounds(FlatSplitPaneDivider.this.rightButton.getX(), FlatSplitPaneDivider.this.leftButton.getY() + FlatSplitPaneDivider.this.leftButton.getHeight(), FlatSplitPaneDivider.this.rightButton.getWidth(), FlatSplitPaneDivider.this.rightButton.getHeight() + extraSize);
                    }
                    boolean leftCollapsed = FlatSplitPaneDivider.this.isLeftCollapsed();
                    if (leftCollapsed) {
                        FlatSplitPaneDivider.this.rightButton.setLocation(FlatSplitPaneDivider.this.leftButton.getLocation());
                    }
                    JButton jButton = FlatSplitPaneDivider.this.leftButton;
                    if (!leftCollapsed) {
                        z = true;
                    } else {
                        z = false;
                    }
                    jButton.setVisible(z);
                    JButton jButton2 = FlatSplitPaneDivider.this.rightButton;
                    if (FlatSplitPaneDivider.this.isRightCollapsed()) {
                        z2 = false;
                    }
                    jButton2.setVisible(z2);
                }
            }
        }
    }
}
