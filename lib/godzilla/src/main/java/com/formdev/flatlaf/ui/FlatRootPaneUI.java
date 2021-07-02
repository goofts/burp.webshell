package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.ui.FlatWindowResizer;
import com.formdev.flatlaf.util.HiDPIUtils;
import com.formdev.flatlaf.util.SystemInfo;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.LayoutManager2;
import java.awt.Window;
import java.beans.PropertyChangeEvent;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.BorderUIResource;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicRootPaneUI;

public class FlatRootPaneUI extends BasicRootPaneUI {
    protected static final Integer TITLE_PANE_LAYER = Integer.valueOf(JLayeredPane.FRAME_CONTENT_LAYER.intValue() - 1);
    static final boolean canUseJBRCustomDecorations;
    protected final Color borderColor = UIManager.getColor("TitlePane.borderColor");
    private LayoutManager oldLayout;
    protected JRootPane rootPane;
    protected FlatTitlePane titlePane;
    protected FlatWindowResizer windowResizer;

    static {
        boolean z;
        if (!SystemInfo.isJetBrainsJVM_11_orLater || !SystemInfo.isWindows_10_orLater) {
            z = false;
        } else {
            z = true;
        }
        canUseJBRCustomDecorations = z;
    }

    public static ComponentUI createUI(JComponent c) {
        return new FlatRootPaneUI();
    }

    public void installUI(JComponent c) {
        FlatRootPaneUI.super.installUI(c);
        this.rootPane = (JRootPane) c;
        if (this.rootPane.getWindowDecorationStyle() != 0) {
            installClientDecorations();
        } else {
            installBorder();
        }
        if (canUseJBRCustomDecorations) {
            JBRCustomDecorations.install(this.rootPane);
        }
    }

    /* access modifiers changed from: protected */
    public void installBorder() {
        if (this.borderColor != null) {
            Border b = this.rootPane.getBorder();
            if (b == null || (b instanceof UIResource)) {
                this.rootPane.setBorder(new FlatWindowTitleBorder(this.borderColor));
            }
        }
    }

    public void uninstallUI(JComponent c) {
        FlatRootPaneUI.super.uninstallUI(c);
        uninstallClientDecorations();
        this.rootPane = null;
    }

    /* access modifiers changed from: protected */
    public void installDefaults(JRootPane c) {
        Color background;
        FlatRootPaneUI.super.installDefaults(c);
        Container parent = c.getParent();
        if (((parent instanceof JFrame) || (parent instanceof JDialog)) && ((background = parent.getBackground()) == null || (background instanceof UIResource))) {
            parent.setBackground(UIManager.getColor("control"));
        }
        if (SystemInfo.isJetBrainsJVM && SystemInfo.isMacOS_10_14_Mojave_orLater) {
            c.putClientProperty("jetbrains.awt.windowDarkAppearance", Boolean.valueOf(FlatLaf.isLafDark()));
        }
    }

    /* access modifiers changed from: protected */
    public void installClientDecorations() {
        boolean isJBRSupported = canUseJBRCustomDecorations && JBRCustomDecorations.isSupported();
        if (this.rootPane.getWindowDecorationStyle() == 0 || isJBRSupported) {
            LookAndFeel.uninstallBorder(this.rootPane);
        } else {
            LookAndFeel.installBorder(this.rootPane, "RootPane.border");
        }
        setTitlePane(createTitlePane());
        this.oldLayout = this.rootPane.getLayout();
        this.rootPane.setLayout(createRootLayout());
        if (!isJBRSupported) {
            this.windowResizer = createWindowResizer();
        }
    }

    /* access modifiers changed from: protected */
    public void uninstallClientDecorations() {
        LookAndFeel.uninstallBorder(this.rootPane);
        setTitlePane(null);
        if (this.windowResizer != null) {
            this.windowResizer.uninstall();
            this.windowResizer = null;
        }
        if (this.oldLayout != null) {
            this.rootPane.setLayout(this.oldLayout);
            this.oldLayout = null;
        }
        if (this.rootPane.getWindowDecorationStyle() == 0) {
            this.rootPane.revalidate();
            this.rootPane.repaint();
        }
    }

    /* access modifiers changed from: protected */
    public FlatRootLayout createRootLayout() {
        return new FlatRootLayout();
    }

    /* access modifiers changed from: protected */
    public FlatWindowResizer createWindowResizer() {
        return new FlatWindowResizer.WindowResizer(this.rootPane);
    }

    /* access modifiers changed from: protected */
    public FlatTitlePane createTitlePane() {
        return new FlatTitlePane(this.rootPane);
    }

    /* access modifiers changed from: protected */
    public void setTitlePane(FlatTitlePane newTitlePane) {
        JLayeredPane layeredPane = this.rootPane.getLayeredPane();
        if (this.titlePane != null) {
            layeredPane.remove(this.titlePane);
        }
        if (newTitlePane != null) {
            layeredPane.add(newTitlePane, TITLE_PANE_LAYER);
        }
        this.titlePane = newTitlePane;
    }

    public void propertyChange(PropertyChangeEvent e) {
        FlatRootPaneUI.super.propertyChange(e);
        String propertyName = e.getPropertyName();
        char c = 65535;
        switch (propertyName.hashCode()) {
            case -1091051311:
                if (propertyName.equals("windowDecorationStyle")) {
                    c = 0;
                    break;
                }
                break;
            case -793947964:
                if (propertyName.equals(FlatClientProperties.MENU_BAR_EMBEDDED)) {
                    c = 1;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                uninstallClientDecorations();
                if (this.rootPane.getWindowDecorationStyle() != 0) {
                    installClientDecorations();
                    return;
                } else {
                    installBorder();
                    return;
                }
            case 1:
                if (this.titlePane != null) {
                    this.titlePane.menuBarChanged();
                    this.rootPane.revalidate();
                    this.rootPane.repaint();
                    return;
                }
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public class FlatRootLayout implements LayoutManager2 {
        protected FlatRootLayout() {
        }

        public void addLayoutComponent(String name, Component comp) {
        }

        public void addLayoutComponent(Component comp, Object constraints) {
        }

        public void removeLayoutComponent(Component comp) {
        }

        public Dimension preferredLayoutSize(Container parent) {
            return computeLayoutSize(parent, 

            public static class FlatWindowBorder extends BorderUIResource.EmptyBorderUIResource {
                protected final Color activeBorderColor = UIManager.getColor("RootPane.activeBorderColor");
                protected final Color baseBorderColor = UIManager.getColor("Panel.background");
                protected final Color inactiveBorderColor = UIManager.getColor("RootPane.inactiveBorderColor");

                public FlatWindowBorder() {
                    super(1, 1, 1, 1);
                }

                public Insets getBorderInsets(Component c, Insets insets) {
                    if (!isWindowMaximized(c) && !FlatUIUtils.isFullScreen(c)) {
                        return FlatRootPaneUI.super.getBorderInsets(c, insets);
                    }
                    insets.right = 0;
                    insets.bottom = 0;
                    insets.left = 0;
                    insets.top = 0;
                    return insets;
                }

                public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                    if (!isWindowMaximized(c) && !FlatUIUtils.isFullScreen(c)) {
                        Container parent = c.getParent();
                        g.setColor(FlatUIUtils.deriveColor(parent instanceof Window ? ((Window) parent).isActive() : false ? this.activeBorderColor : this.inactiveBorderColor, this.baseBorderColor));
                        HiDPIUtils.paintAtScale1x((Graphics2D) g, x, y, width, height, 

                        /* access modifiers changed from: private */
                        public static class FlatWindowTitleBorder extends BorderUIResource.EmptyBorderUIResource {
                            private final Color borderColor;

                            FlatWindowTitleBorder(Color borderColor2) {
                                super(0, 0, 0, 0);
                                this.borderColor = borderColor2;
                            }

                            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                                if (showBorder(c)) {
                                    FlatUIUtils.paintFilledRectangle(g, this.borderColor, (float) x, (float) y, (float) width, UIScale.scale(1.0f));
                                }
                            }

                            public Insets getBorderInsets(Component c, Insets insets) {
                                int i;
                                if (showBorder(c)) {
                                    i = 1;
                                } else {
                                    i = 0;
                                }
                                insets.set(i, 0, 0, 0);
                                return insets;
                            }

                            private boolean showBorder(Component c) {
                                Container parent = c.getParent();
                                return ((parent instanceof JFrame) && (((JFrame) parent).getJMenuBar() == null || !((JFrame) parent).getJMenuBar().isVisible())) || (parent instanceof JDialog);
                            }
                        }
                    }
