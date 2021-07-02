package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.FlatSystemProperties;
import com.formdev.flatlaf.util.HiDPIUtils;
import com.formdev.flatlaf.util.SystemInfo;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;
import java.beans.PropertyChangeEvent;
import java.lang.reflect.Method;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.BorderUIResource;

public class JBRCustomDecorations {
    private static Method AWTAccessor_ComponentAccessor_getPeer;
    private static Method AWTAccessor_getComponentAccessor;
    private static Method WWindowPeer_setCustomDecorationHitTestSpots;
    private static Method WWindowPeer_setCustomDecorationTitleBarHeight;
    private static Method Window_hasCustomDecoration;
    private static Method Window_setHasCustomDecoration;
    private static boolean initialized;

    public static boolean isSupported() {
        initialize();
        return Window_setHasCustomDecoration != null;
    }

    static void install(final JRootPane rootPane) {
        if (isSupported() && rootPane.getParent() == null) {
            rootPane.addHierarchyListener(new HierarchyListener() {
                /* class com.formdev.flatlaf.ui.JBRCustomDecorations.AnonymousClass1 */

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
                    	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:99)
                    	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:143)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:63)
                    	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:93)
                    	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:59)
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
                public void hierarchyChanged(java.awt.event.HierarchyEvent r7) {
                    /*
                        r6 = this;
                        java.awt.Component r1 = r7.getChanged()
                        javax.swing.JRootPane r2 = r2
                        if (r1 != r2) goto L_0x0015
                        long r2 = r7.getChangeFlags()
                        r4 = 1
                        long r2 = r2 & r4
                        r4 = 0
                        int r1 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
                        if (r1 != 0) goto L_0x0016
                    L_0x0015:
                        return
                    L_0x0016:
                        java.awt.Container r0 = r7.getChangedParent()
                        boolean r1 = r0 instanceof java.awt.Window
                        if (r1 == 0) goto L_0x0023
                        java.awt.Window r0 = (java.awt.Window) r0
                        com.formdev.flatlaf.ui.JBRCustomDecorations.install(r0)
                    L_0x0023:
                        javax.swing.JRootPane r1 = r2
                        r1 = move-result
                        java.awt.EventQueue.invokeLater(r1)
                        goto L_0x0015
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.JBRCustomDecorations.AnonymousClass1.hierarchyChanged(java.awt.event.HierarchyEvent):void");
                }

                private /* synthetic */ void lambda$hierarchyChanged$0(JRootPane rootPane) {
                    rootPane.removeHierarchyListener(this);
                }
            });
        }
    }

    static void install(Window window) {
        if (!isSupported() || UIManager.getLookAndFeel().getSupportsWindowDecorations()) {
            return;
        }
        if (window instanceof JFrame) {
            JFrame frame = (JFrame) window;
            if ((JFrame.isDefaultLookAndFeelDecorated() || FlatSystemProperties.getBoolean(FlatSystemProperties.USE_JETBRAINS_CUSTOM_DECORATIONS, false)) && !frame.isUndecorated()) {
                setHasCustomDecoration(frame);
                frame.getRootPane().setWindowDecorationStyle(1);
            }
        } else if (window instanceof JDialog) {
            JDialog dialog = (JDialog) window;
            if ((JDialog.isDefaultLookAndFeelDecorated() || FlatSystemProperties.getBoolean(FlatSystemProperties.USE_JETBRAINS_CUSTOM_DECORATIONS, false)) && !dialog.isUndecorated()) {
                setHasCustomDecoration(dialog);
                dialog.getRootPane().setWindowDecorationStyle(2);
            }
        }
    }

    static boolean hasCustomDecoration(Window window) {
        if (!isSupported()) {
            return false;
        }
        try {
            return ((Boolean) Window_hasCustomDecoration.invoke(window, new Object[0])).booleanValue();
        } catch (Exception ex) {
            Logger.getLogger(FlatLaf.class.getName()).log(Level.SEVERE, (String) null, (Throwable) ex);
            return false;
        }
    }

    static void setHasCustomDecoration(Window window) {
        if (isSupported()) {
            try {
                Window_setHasCustomDecoration.invoke(window, new Object[0]);
            } catch (Exception ex) {
                Logger.getLogger(FlatLaf.class.getName()).log(Level.SEVERE, (String) null, (Throwable) ex);
            }
        }
    }

    static void setHitTestSpotsAndTitleBarHeight(Window window, List<Rectangle> hitTestSpots, int titleBarHeight) {
        if (isSupported()) {
            try {
                Object compAccessor = AWTAccessor_getComponentAccessor.invoke(null, new Object[0]);
                Object peer = AWTAccessor_ComponentAccessor_getPeer.invoke(compAccessor, window);
                WWindowPeer_setCustomDecorationHitTestSpots.invoke(peer, hitTestSpots);
                WWindowPeer_setCustomDecorationTitleBarHeight.invoke(peer, Integer.valueOf(titleBarHeight));
            } catch (Exception ex) {
                Logger.getLogger(FlatLaf.class.getName()).log(Level.SEVERE, (String) null, (Throwable) ex);
            }
        }
    }

    private static void initialize() {
        if (!initialized) {
            initialized = true;
            if (SystemInfo.isJetBrainsJVM_11_orLater && SystemInfo.isWindows_10_orLater && FlatSystemProperties.getBoolean(FlatSystemProperties.USE_JETBRAINS_CUSTOM_DECORATIONS, true)) {
                try {
                    Class<?> awtAcessorClass = Class.forName("sun.awt.AWTAccessor");
                    Class<?> compAccessorClass = Class.forName("sun.awt.AWTAccessor$ComponentAccessor");
                    AWTAccessor_getComponentAccessor = awtAcessorClass.getDeclaredMethod("getComponentAccessor", new Class[0]);
                    AWTAccessor_ComponentAccessor_getPeer = compAccessorClass.getDeclaredMethod("getPeer", Component.class);
                    Class<?> peerClass = Class.forName("sun.awt.windows.WWindowPeer");
                    WWindowPeer_setCustomDecorationHitTestSpots = peerClass.getDeclaredMethod("setCustomDecorationHitTestSpots", List.class);
                    WWindowPeer_setCustomDecorationTitleBarHeight = peerClass.getDeclaredMethod("setCustomDecorationTitleBarHeight", Integer.TYPE);
                    WWindowPeer_setCustomDecorationHitTestSpots.setAccessible(true);
                    WWindowPeer_setCustomDecorationTitleBarHeight.setAccessible(true);
                    Window_hasCustomDecoration = Window.class.getDeclaredMethod("hasCustomDecoration", new Class[0]);
                    Window_setHasCustomDecoration = Window.class.getDeclaredMethod("setHasCustomDecoration", new Class[0]);
                    Window_hasCustomDecoration.setAccessible(true);
                    Window_setHasCustomDecoration.setAccessible(true);
                } catch (Exception e) {
                }
            }
        }
    }

    static class JBRWindowTopBorder extends BorderUIResource.EmptyBorderUIResource {
        private static JBRWindowTopBorder instance;
        private Color activeColor;
        private boolean colorizationAffectsBorders;
        private final Color defaultActiveBorder;
        private final Color inactiveLightColor;

        static JBRWindowTopBorder getInstance() {
            if (instance == null) {
                instance = new JBRWindowTopBorder();
            }
            return instance;
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [java.beans.PropertyChangeListener, void] */
        /* JADX WARNING: Unknown variable types count: 1 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private JBRWindowTopBorder() {
            /*
                r4 = this;
                r3 = 0
                r2 = 1
                r4.<init>(r2, r3, r3, r3)
                java.awt.Color r2 = new java.awt.Color
                r3 = 7368816(0x707070, float:1.032591E-38)
                r2.<init>(r3)
                r4.defaultActiveBorder = r2
                java.awt.Color r2 = new java.awt.Color
                r3 = 11184810(0xaaaaaa, float:1.5673257E-38)
                r2.<init>(r3)
                r4.inactiveLightColor = r2
                java.awt.Color r2 = r4.defaultActiveBorder
                r4.activeColor = r2
                boolean r2 = r4.calculateAffectsBorders()
                r4.colorizationAffectsBorders = r2
                java.awt.Color r2 = r4.calculateActiveBorderColor()
                r4.activeColor = r2
                java.awt.Toolkit r1 = java.awt.Toolkit.getDefaultToolkit()
                java.lang.String r2 = "win.dwm.colorizationColor.affects.borders"
                r3 = move-result
                void r0 = r1.addPropertyChangeListener(r2, r3)
                java.lang.String r2 = "win.dwm.colorizationColor"
                r1.addPropertyChangeListener(r2, r0)
                java.lang.String r2 = "win.dwm.colorizationColorBalance"
                r1.addPropertyChangeListener(r2, r0)
                java.lang.String r2 = "win.frame.activeBorderColor"
                r1.addPropertyChangeListener(r2, r0)
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.JBRCustomDecorations.JBRWindowTopBorder.<init>():void");
        }

        private /* synthetic */ void lambda$new$0(PropertyChangeEvent e) {
            this.colorizationAffectsBorders = calculateAffectsBorders();
            this.activeColor = calculateActiveBorderColor();
        }

        private /* synthetic */ void lambda$new$1(PropertyChangeEvent e) {
            this.activeColor = calculateActiveBorderColor();
        }

        private boolean calculateAffectsBorders() {
            Object value = Toolkit.getDefaultToolkit().getDesktopProperty("win.dwm.colorizationColor.affects.borders");
            if (value instanceof Boolean) {
                return ((Boolean) value).booleanValue();
            }
            return true;
        }

        private Color calculateActiveBorderColor() {
            if (!this.colorizationAffectsBorders) {
                return this.defaultActiveBorder;
            }
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            Color colorizationColor = (Color) toolkit.getDesktopProperty("win.dwm.colorizationColor");
            if (colorizationColor != null) {
                Object colorizationColorBalanceObj = toolkit.getDesktopProperty("win.dwm.colorizationColorBalance");
                if (!(colorizationColorBalanceObj instanceof Integer)) {
                    return colorizationColor;
                }
                int colorizationColorBalance = ((Integer) colorizationColorBalanceObj).intValue();
                if (colorizationColorBalance < 0 || colorizationColorBalance > 100) {
                    colorizationColorBalance = 100;
                }
                if (colorizationColorBalance == 0) {
                    return new Color(14277081);
                }
                if (colorizationColorBalance == 100) {
                    return colorizationColor;
                }
                float alpha = ((float) colorizationColorBalance) / 100.0f;
                float remainder = 1.0f - alpha;
                return new Color(Math.min(Math.max(Math.round((((float) colorizationColor.getRed()) * alpha) + (217.0f * remainder)), 0), 255), Math.min(Math.max(Math.round((((float) colorizationColor.getGreen()) * alpha) + (217.0f * remainder)), 0), 255), Math.min(Math.max(Math.round((((float) colorizationColor.getBlue()) * alpha) + (217.0f * remainder)), 0), 255));
            }
            Color activeBorderColor = (Color) toolkit.getDesktopProperty("win.frame.activeBorderColor");
            if (activeBorderColor == null) {
                activeBorderColor = UIManager.getColor("MenuBar.borderColor");
            }
            return activeBorderColor;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            boolean active;
            boolean paintTopBorder;
            Window window = SwingUtilities.windowForComponent(c);
            if (window != null) {
                active = window.isActive();
            } else {
                active = false;
            }
            if (!FlatLaf.isLafDark() || (active && this.colorizationAffectsBorders)) {
                paintTopBorder = true;
            } else {
                paintTopBorder = false;
            }
            if (paintTopBorder) {
                g.setColor(active ? this.activeColor : this.inactiveLightColor);
                HiDPIUtils.paintAtScale1x((Graphics2D) g, x, y, width, height, 
            }
