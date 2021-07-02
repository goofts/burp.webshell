package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.UIScale;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyVetoException;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.JToolTip;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicDesktopIconUI;

public class FlatDesktopIconUI extends BasicDesktopIconUI {
    private JButton closeButton;
    private ActionListener closeListener;
    private Dimension closeSize;
    private JLabel dockIcon;
    private Dimension iconSize;
    private MouseInputListener mouseInputListener;
    private JToolTip titleTip;

    public static ComponentUI createUI(JComponent c) {
        return new FlatDesktopIconUI();
    }

    public void uninstallUI(JComponent c) {
        FlatDesktopIconUI.super.uninstallUI(c);
        this.dockIcon = null;
        this.closeButton = null;
    }

    /* access modifiers changed from: protected */
    public void installComponents() {
        this.dockIcon = new JLabel();
        this.dockIcon.setHorizontalAlignment(0);
        this.closeButton = new JButton();
        this.closeButton.setIcon(UIManager.getIcon("DesktopIcon.closeIcon"));
        this.closeButton.setFocusable(false);
        this.closeButton.setBorder(BorderFactory.createEmptyBorder());
        this.closeButton.setOpaque(true);
        this.closeButton.setBackground(FlatUIUtils.nonUIResource(this.desktopIcon.getBackground()));
        this.closeButton.setForeground(FlatUIUtils.nonUIResource(this.desktopIcon.getForeground()));
        this.closeButton.setVisible(false);
        this.desktopIcon.setLayout(new FlatDesktopIconLayout());
        this.desktopIcon.add(this.closeButton);
        this.desktopIcon.add(this.dockIcon);
    }

    /* access modifiers changed from: protected */
    public void uninstallComponents() {
        hideTitleTip();
        this.desktopIcon.remove(this.dockIcon);
        this.desktopIcon.remove(this.closeButton);
        this.desktopIcon.setLayout((LayoutManager) null);
    }

    /* access modifiers changed from: protected */
    public void installDefaults() {
        FlatDesktopIconUI.super.installDefaults();
        LookAndFeel.installColors(this.desktopIcon, "DesktopIcon.background", "DesktopIcon.foreground");
        this.iconSize = UIManager.getDimension("DesktopIcon.iconSize");
        this.closeSize = UIManager.getDimension("DesktopIcon.closeSize");
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void installListeners() {
        /*
            r2 = this;
            void r0 = com.formdev.flatlaf.ui.FlatDesktopIconUI.super.installListeners()
            r2.closeListener = r0
            javax.swing.JButton r0 = r2.closeButton
            java.awt.event.ActionListener r1 = r2.closeListener
            r0.addActionListener(r1)
            javax.swing.JButton r0 = r2.closeButton
            javax.swing.event.MouseInputListener r1 = r2.mouseInputListener
            r0.addMouseListener(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatDesktopIconUI.installListeners():void");
    }

    private /* synthetic */ void lambda$installListeners$0(ActionEvent e) {
        if (this.frame.isClosable()) {
            this.frame.doDefaultCloseAction();
        }
    }

    /* access modifiers changed from: protected */
    public void uninstallListeners() {
        FlatDesktopIconUI.super.uninstallListeners();
        this.closeButton.removeActionListener(this.closeListener);
        this.closeButton.removeMouseListener(this.mouseInputListener);
        this.closeListener = null;
        this.mouseInputListener = null;
    }

    /* access modifiers changed from: protected */
    public MouseInputListener createMouseInputListener() {
        this.mouseInputListener = new MouseInputAdapter() {
            /* class com.formdev.flatlaf.ui.FlatDesktopIconUI.AnonymousClass1 */

            public void mouseReleased(MouseEvent e) {
                if (FlatDesktopIconUI.this.frame.isIcon() && FlatDesktopIconUI.this.desktopIcon.contains(e.getX(), e.getY())) {
                    FlatDesktopIconUI.this.hideTitleTip();
                    FlatDesktopIconUI.this.closeButton.setVisible(false);
                    try {
                        FlatDesktopIconUI.this.frame.setIcon(false);
                    } catch (PropertyVetoException e2) {
                    }
                }
            }

            public void mouseEntered(MouseEvent e) {
                FlatDesktopIconUI.this.showTitleTip();
                if (FlatDesktopIconUI.this.frame.isClosable()) {
                    FlatDesktopIconUI.this.closeButton.setVisible(true);
                }
            }

            public void mouseExited(MouseEvent e) {
                FlatDesktopIconUI.this.hideTitleTip();
                FlatDesktopIconUI.this.closeButton.setVisible(false);
            }
        };
        return this.mouseInputListener;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void showTitleTip() {
        JRootPane rootPane = SwingUtilities.getRootPane(this.desktopIcon);
        if (rootPane != null) {
            if (this.titleTip == null) {
                this.titleTip = new JToolTip();
                rootPane.getLayeredPane().add(this.titleTip, JLayeredPane.POPUP_LAYER);
            }
            this.titleTip.setTipText(this.frame.getTitle());
            this.titleTip.setSize(this.titleTip.getPreferredSize());
            Point pt = SwingUtilities.convertPoint(this.desktopIcon, (this.desktopIcon.getWidth() - this.titleTip.getWidth()) / 2, -(this.titleTip.getHeight() + UIScale.scale(4)), this.titleTip.getParent());
            if (pt.x + this.titleTip.getWidth() > rootPane.getWidth()) {
                pt.x = rootPane.getWidth() - this.titleTip.getWidth();
            }
            if (pt.x < 0) {
                pt.x = 0;
            }
            this.titleTip.setLocation(pt);
            this.titleTip.repaint();
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void hideTitleTip() {
        if (this.titleTip != null) {
            this.titleTip.setVisible(false);
            this.titleTip.getParent().remove(this.titleTip);
            this.titleTip = null;
        }
    }

    public Dimension getPreferredSize(JComponent c) {
        return UIScale.scale(this.iconSize);
    }

    public Dimension getMinimumSize(JComponent c) {
        return getPreferredSize(c);
    }

    public Dimension getMaximumSize(JComponent c) {
        return getPreferredSize(c);
    }

    /* access modifiers changed from: package-private */
    /*  JADX ERROR: MOVE_RESULT instruction can be used only in fallback mode
        jadx.core.utils.exceptions.CodegenException: MOVE_RESULT instruction can be used only in fallback mode
        	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:604)
        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:542)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:230)
        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:119)
        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:103)
        	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:806)
        	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:746)
        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:367)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:249)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:217)
        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:110)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:56)
        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:93)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:59)
        	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:244)
        	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:237)
        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:342)
        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:295)
        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:264)
        	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:184)
        	at java.util.ArrayList.forEach(ArrayList.java:1257)
        	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:390)
        	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
        */
    public void updateDockIcon() {
        /*
            r1 = this;
            r0 = move-result
            java.awt.EventQueue.invokeLater(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatDesktopIconUI.updateDockIcon():void");
    }

    private /* synthetic */ void lambda$updateDockIcon$1() {
        if (this.dockIcon != null) {
            updateDockIconLater();
        }
    }

    /* JADX INFO: finally extract failed */
    private void updateDockIconLater() {
        if (this.frame.isSelected()) {
            try {
                this.frame.setSelected(false);
            } catch (PropertyVetoException e) {
            }
        }
        int frameWidth = Math.max(this.frame.getWidth(), 1);
        int frameHeight = Math.max(this.frame.getHeight(), 1);
        BufferedImage frameImage = new BufferedImage(frameWidth, frameHeight, 2);
        Graphics2D g = frameImage.createGraphics();
        try {
            this.frame.paint(g);
            g.dispose();
            Insets insets = this.desktopIcon.getInsets();
            int previewWidth = (UIScale.scale(this.iconSize.width) - insets.left) - insets.right;
            int previewHeight = (UIScale.scale(this.iconSize.height) - insets.top) - insets.bottom;
            float frameRatio = ((float) frameHeight) / ((float) frameWidth);
            if (((float) previewWidth) / ((float) frameWidth) > ((float) previewHeight) / ((float) frameHeight)) {
                previewWidth = Math.round(((float) previewHeight) / frameRatio);
            } else {
                previewHeight = Math.round(((float) previewWidth) * frameRatio);
            }
            this.dockIcon.setIcon(new ImageIcon(frameImage.getScaledInstance(previewWidth, previewHeight, 4)));
        } catch (Throwable th) {
            g.dispose();
            throw th;
        }
    }

    private class FlatDesktopIconLayout implements LayoutManager {
        private FlatDesktopIconLayout() {
        }

        public void addLayoutComponent(String name, Component comp) {
        }

        public void removeLayoutComponent(Component comp) {
        }

        public Dimension preferredLayoutSize(Container parent) {
            return FlatDesktopIconUI.this.dockIcon.getPreferredSize();
        }

        public Dimension minimumLayoutSize(Container parent) {
            return FlatDesktopIconUI.this.dockIcon.getMinimumSize();
        }

        public void layoutContainer(Container parent) {
            Insets insets = parent.getInsets();
            FlatDesktopIconUI.this.dockIcon.setBounds(insets.left, insets.top, (parent.getWidth() - insets.left) - insets.right, (parent.getHeight() - insets.top) - insets.bottom);
            Dimension cSize = UIScale.scale(FlatDesktopIconUI.this.closeSize);
            FlatDesktopIconUI.this.closeButton.setBounds(parent.getWidth() - cSize.width, 0, cSize.width, cSize.height);
        }
    }
}
