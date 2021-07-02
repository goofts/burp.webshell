package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.SystemInfo;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.MouseInfo;
import java.awt.Panel;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.JComponent;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JToolTip;
import javax.swing.JWindow;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;

public class FlatPopupFactory extends PopupFactory {
    private Method java8getPopupMethod;
    private Method java9getPopupMethod;

    public Popup getPopup(Component owner, Component contents, int x, int y) throws IllegalArgumentException {
        Point pt = fixToolTipLocation(owner, contents, x, y);
        if (pt != null) {
            x = pt.x;
            y = pt.y;
        }
        boolean forceHeavyWeight = isOptionEnabled(owner, contents, FlatClientProperties.POPUP_FORCE_HEAVY_WEIGHT, FlatClientProperties.POPUP_FORCE_HEAVY_WEIGHT);
        if (!isOptionEnabled(owner, contents, FlatClientProperties.POPUP_DROP_SHADOW_PAINTED, FlatClientProperties.POPUP_DROP_SHADOW_PAINTED)) {
            return new NonFlashingPopup(getPopupForScreenOfOwner(owner, contents, x, y, forceHeavyWeight), contents);
        }
        if (SystemInfo.isMacOS || SystemInfo.isLinux) {
            return new NonFlashingPopup(getPopupForScreenOfOwner(owner, contents, x, y, true), contents);
        }
        return new DropShadowPopup(getPopupForScreenOfOwner(owner, contents, x, y, forceHeavyWeight), owner, contents);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private Popup getPopupForScreenOfOwner(Component owner, Component contents, int x, int y, boolean forceHeavyWeight) throws IllegalArgumentException {
        Popup popup;
        int count = 0;
        do {
            if (forceHeavyWeight) {
                popup = getHeavyWeightPopup(owner, contents, x, y);
            } else {
                popup = FlatPopupFactory.super.getPopup(owner, contents, x, y);
            }
            Window popupWindow = SwingUtilities.windowForComponent(contents);
            if (popupWindow == null || popupWindow.getGraphicsConfiguration() == owner.getGraphicsConfiguration()) {
                return popup;
            }
            if (popupWindow instanceof JWindow) {
                ((JWindow) popupWindow).getContentPane().removeAll();
            }
            popupWindow.dispose();
            count++;
        } while (count <= 10);
        return popup;
    }

    /* access modifiers changed from: private */
    public static void showPopupAndFixLocation(Popup popup, Window popupWindow) {
        if (popupWindow != null) {
            int x = popupWindow.getX();
            int y = popupWindow.getY();
            popup.show();
            if (popupWindow.getX() != x || popupWindow.getY() != y) {
                popupWindow.setLocation(x, y);
                return;
            }
            return;
        }
        popup.show();
    }

    private boolean isOptionEnabled(Component owner, Component contents, String clientKey, String uiKey) {
        Boolean b;
        Boolean b2;
        if ((owner instanceof JComponent) && (b2 = FlatClientProperties.clientPropertyBooleanStrict((JComponent) owner, clientKey, null)) != null) {
            return b2.booleanValue();
        }
        if (!(contents instanceof JComponent) || (b = FlatClientProperties.clientPropertyBooleanStrict((JComponent) contents, clientKey, null)) == null) {
            return UIManager.getBoolean(uiKey);
        }
        return b.booleanValue();
    }

    private Popup getHeavyWeightPopup(Component owner, Component contents, int x, int y) throws IllegalArgumentException {
        try {
            if (SystemInfo.isJava_9_orLater) {
                if (this.java9getPopupMethod == null) {
                    this.java9getPopupMethod = PopupFactory.class.getDeclaredMethod("getPopup", Component.class, Component.class, Integer.TYPE, Integer.TYPE, Boolean.TYPE);
                }
                return (Popup) this.java9getPopupMethod.invoke(this, owner, contents, Integer.valueOf(x), Integer.valueOf(y), true);
            }
            if (this.java8getPopupMethod == null) {
                this.java8getPopupMethod = PopupFactory.class.getDeclaredMethod("getPopup", Component.class, Component.class, Integer.TYPE, Integer.TYPE, Integer.TYPE);
                this.java8getPopupMethod.setAccessible(true);
            }
            return (Popup) this.java8getPopupMethod.invoke(this, owner, contents, Integer.valueOf(x), Integer.valueOf(y), 2);
        } catch (IllegalAccessException | NoSuchMethodException | SecurityException | InvocationTargetException e) {
            return null;
        }
    }

    private Point fixToolTipLocation(Component owner, Component contents, int x, int y) {
        if (!(contents instanceof JToolTip) || !wasInvokedFromToolTipManager()) {
            return null;
        }
        Point mouseLocation = MouseInfo.getPointerInfo().getLocation();
        Dimension tipSize = contents.getPreferredSize();
        if (new Rectangle(x, y, tipSize.width, tipSize.height).contains(mouseLocation)) {
            return new Point(x, (mouseLocation.y - tipSize.height) - UIScale.scale(20));
        }
        return null;
    }

    private boolean wasInvokedFromToolTipManager() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement stackTraceElement : stackTrace) {
            if ("javax.swing.ToolTipManager".equals(stackTraceElement.getClassName()) && "showTipWindow".equals(stackTraceElement.getMethodName())) {
                return true;
            }
        }
        return false;
    }

    private class NonFlashingPopup extends Popup {
        private Component contents;
        private Popup delegate;
        private Color oldPopupWindowBackground;
        protected Window popupWindow;

        NonFlashingPopup(Popup delegate2, Component contents2) {
            this.delegate = delegate2;
            this.contents = contents2;
            this.popupWindow = SwingUtilities.windowForComponent(contents2);
            if (this.popupWindow != null) {
                this.oldPopupWindowBackground = this.popupWindow.getBackground();
                this.popupWindow.setBackground(contents2.getBackground());
            }
        }

        public void show() {
            Container c;
            if (this.delegate != null) {
                FlatPopupFactory.showPopupAndFixLocation(this.delegate, this.popupWindow);
                if ((this.contents instanceof JToolTip) && this.popupWindow == null) {
                    Container parent = this.contents.getParent();
                    if (parent instanceof JPanel) {
                        Dimension prefSize = parent.getPreferredSize();
                        if (!prefSize.equals(parent.getSize())) {
                            Container mediumWeightPanel = SwingUtilities.getAncestorOfClass(Panel.class, parent);
                            if (mediumWeightPanel != null) {
                                c = mediumWeightPanel;
                            } else {
                                c = parent;
                            }
                            c.setSize(prefSize);
                            c.validate();
                        }
                    }
                }
            }
        }

        public void hide() {
            if (this.delegate != null) {
                this.delegate.hide();
                this.delegate = null;
                this.contents = null;
            }
            if (this.popupWindow != null) {
                this.popupWindow.setBackground(this.oldPopupWindowBackground);
                this.popupWindow = null;
            }
        }
    }

    private class DropShadowPopup extends NonFlashingPopup {
        private Popup dropShadowDelegate;
        private JPanel dropShadowPanel;
        private Window dropShadowWindow;
        private JComponent lightComp;
        private ComponentListener mediumPanelListener;
        private Panel mediumWeightPanel;
        private boolean mediumWeightShown;
        private Border oldBorder;
        private Color oldDropShadowWindowBackground;
        private boolean oldOpaque;
        private final Component owner;

        DropShadowPopup(Popup delegate, Component owner2, Component contents) {
            super(delegate, contents);
            this.owner = owner2;
            Dimension size = contents.getPreferredSize();
            if (size.width > 0 && size.height > 0) {
                if (this.popupWindow != null) {
                    JPanel dropShadowPanel2 = new JPanel();
                    dropShadowPanel2.setBorder(createDropShadowBorder());
                    dropShadowPanel2.setOpaque(false);
                    Dimension prefSize = this.popupWindow.getPreferredSize();
                    Insets insets = dropShadowPanel2.getInsets();
                    dropShadowPanel2.setPreferredSize(new Dimension(prefSize.width + insets.left + insets.right, prefSize.height + insets.top + insets.bottom));
                    this.dropShadowDelegate = FlatPopupFactory.this.getPopupForScreenOfOwner(owner2, dropShadowPanel2, this.popupWindow.getX() - insets.left, this.popupWindow.getY() - insets.top, true);
                    this.dropShadowWindow = SwingUtilities.windowForComponent(dropShadowPanel2);
                    if (this.dropShadowWindow != null) {
                        this.oldDropShadowWindowBackground = this.dropShadowWindow.getBackground();
                        this.dropShadowWindow.setBackground(new Color(0, true));
                        return;
                    }
                    return;
                }
                this.mediumWeightPanel = SwingUtilities.getAncestorOfClass(Panel.class, contents);
                if (this.mediumWeightPanel != null) {
                    this.dropShadowPanel = new JPanel();
                    this.dropShadowPanel.setBorder(createDropShadowBorder());
                    this.dropShadowPanel.setOpaque(false);
                    this.dropShadowPanel.setSize(FlatUIUtils.addInsets(this.mediumWeightPanel.getSize(), this.dropShadowPanel.getInsets()));
                    return;
                }
                Container p = contents.getParent();
                if (p instanceof JComponent) {
                    this.lightComp = (JComponent) p;
                    this.oldBorder = this.lightComp.getBorder();
                    this.oldOpaque = this.lightComp.isOpaque();
                    this.lightComp.setBorder(createDropShadowBorder());
                    this.lightComp.setOpaque(false);
                    this.lightComp.setSize(this.lightComp.getPreferredSize());
                }
            }
        }

        private Border createDropShadowBorder() {
            return new FlatDropShadowBorder(UIManager.getColor("Popup.dropShadowColor"), UIManager.getInsets("Popup.dropShadowInsets"), FlatUIUtils.getUIFloat("Popup.dropShadowOpacity", 0.5f));
        }

        @Override // com.formdev.flatlaf.ui.FlatPopupFactory.NonFlashingPopup
        public void show() {
            if (this.dropShadowDelegate != null) {
                FlatPopupFactory.showPopupAndFixLocation(this.dropShadowDelegate, this.dropShadowWindow);
            }
            if (this.mediumWeightPanel != null) {
                showMediumWeightDropShadow();
            }
            super.show();
            if (this.lightComp != null) {
                Insets insets = this.lightComp.getInsets();
                if (insets.left != 0 || insets.top != 0) {
                    this.lightComp.setLocation(this.lightComp.getX() - insets.left, this.lightComp.getY() - insets.top);
                }
            }
        }

        @Override // com.formdev.flatlaf.ui.FlatPopupFactory.NonFlashingPopup
        public void hide() {
            if (this.dropShadowDelegate != null) {
                this.dropShadowDelegate.hide();
                this.dropShadowDelegate = null;
            }
            if (this.mediumWeightPanel != null) {
                hideMediumWeightDropShadow();
                this.dropShadowPanel = null;
                this.mediumWeightPanel = null;
            }
            super.hide();
            if (this.dropShadowWindow != null) {
                this.dropShadowWindow.setBackground(this.oldDropShadowWindowBackground);
                this.dropShadowWindow = null;
            }
            if (this.lightComp != null) {
                this.lightComp.setBorder(this.oldBorder);
                this.lightComp.setOpaque(this.oldOpaque);
                this.lightComp = null;
            }
        }

        private void showMediumWeightDropShadow() {
            if (!this.mediumWeightShown) {
                this.mediumWeightShown = true;
                Window window = SwingUtilities.windowForComponent(this.owner);
                if (window != null && (window instanceof RootPaneContainer)) {
                    this.dropShadowPanel.setVisible(false);
                    ((RootPaneContainer) window).getLayeredPane().add(this.dropShadowPanel, JLayeredPane.POPUP_LAYER, 0);
                    this.mediumPanelListener = new ComponentListener() {
                        /* class com.formdev.flatlaf.ui.FlatPopupFactory.DropShadowPopup.AnonymousClass1 */

                        public void componentShown(ComponentEvent e) {
                            if (DropShadowPopup.this.dropShadowPanel != null) {
                                DropShadowPopup.this.dropShadowPanel.setVisible(true);
                            }
                        }

                        public void componentHidden(ComponentEvent e) {
                            if (DropShadowPopup.this.dropShadowPanel != null) {
                                DropShadowPopup.this.dropShadowPanel.setVisible(false);
                            }
                        }

                        public void componentMoved(ComponentEvent e) {
                            if (DropShadowPopup.this.dropShadowPanel != null && DropShadowPopup.this.mediumWeightPanel != null) {
                                Point location = DropShadowPopup.this.mediumWeightPanel.getLocation();
                                Insets insets = DropShadowPopup.this.dropShadowPanel.getInsets();
                                DropShadowPopup.this.dropShadowPanel.setLocation(location.x - insets.left, location.y - insets.top);
                            }
                        }

                        public void componentResized(ComponentEvent e) {
                            if (DropShadowPopup.this.dropShadowPanel != null) {
                                DropShadowPopup.this.dropShadowPanel.setSize(FlatUIUtils.addInsets(DropShadowPopup.this.mediumWeightPanel.getSize(), DropShadowPopup.this.dropShadowPanel.getInsets()));
                            }
                        }
                    };
                    this.mediumWeightPanel.addComponentListener(this.mediumPanelListener);
                }
            }
        }

        private void hideMediumWeightDropShadow() {
            this.mediumWeightPanel.removeComponentListener(this.mediumPanelListener);
            Container parent = this.dropShadowPanel.getParent();
            if (parent != null) {
                Rectangle bounds = this.dropShadowPanel.getBounds();
                parent.remove(this.dropShadowPanel);
                parent.repaint(bounds.x, bounds.y, bounds.width, bounds.height);
            }
        }
    }
}
