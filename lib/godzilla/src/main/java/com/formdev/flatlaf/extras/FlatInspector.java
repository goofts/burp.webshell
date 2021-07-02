package com.formdev.flatlaf.extras;

import com.formdev.flatlaf.ui.FlatToolTipUI;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.LayoutManager;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseMotionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Field;
import javassist.compiler.TokenId;
import javax.swing.AbstractButton;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JRootPane;
import javax.swing.JToolBar;
import javax.swing.JToolTip;
import javax.swing.KeyStroke;
import javax.swing.RootPaneContainer;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.UIResource;
import javax.swing.text.JTextComponent;

public class FlatInspector {
    private static final Integer HIGHLIGHT_LAYER = Integer.valueOf((int) TokenId.CharConstant);
    private static final int KEY_MODIFIERS_MASK = 960;
    private static final Integer TOOLTIP_LAYER = Integer.valueOf((int) TokenId.IntConstant);
    private boolean enabled;
    private JComponent highlightFigure;
    private int inspectParentLevel;
    private final AWTEventListener keyListener;
    private Component lastComponent;
    private int lastX;
    private int lastY;
    private final MouseMotionListener mouseMotionListener;
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);
    private final JRootPane rootPane;
    private JToolTip tip;
    private boolean wasCtrlOrShiftKeyPressed;

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
    public static void install(java.lang.String r6) {
        /*
            javax.swing.KeyStroke r0 = javax.swing.KeyStroke.getKeyStroke(r6)
            java.awt.Toolkit r1 = java.awt.Toolkit.getDefaultToolkit()
            r2 = move-result
            r4 = 8
            r1.addAWTEventListener(r2, r4)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.extras.FlatInspector.install(java.lang.String):void");
    }

    private static /* synthetic */ void lambda$install$0(KeyStroke keyStroke, AWTEvent e) {
        if (e.getID() == 402 && ((KeyEvent) e).getKeyCode() == keyStroke.getKeyCode() && (((KeyEvent) e).getModifiersEx() & KEY_MODIFIERS_MASK) == (keyStroke.getModifiers() & KEY_MODIFIERS_MASK)) {
            Window activeWindow = KeyboardFocusManager.getCurrentKeyboardFocusManager().getActiveWindow();
            if (activeWindow instanceof RootPaneContainer) {
                JRootPane rootPane2 = ((RootPaneContainer) activeWindow).getRootPane();
                FlatInspector inspector = (FlatInspector) rootPane2.getClientProperty(FlatInspector.class);
                if (inspector == null) {
                    FlatInspector inspector2 = new FlatInspector(rootPane2);
                    rootPane2.putClientProperty(FlatInspector.class, inspector2);
                    inspector2.setEnabled(true);
                    return;
                }
                inspector.uninstall();
                rootPane2.putClientProperty(FlatInspector.class, (Object) null);
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [void, java.awt.event.AWTEventListener] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public FlatInspector(javax.swing.JRootPane r3) {
        /*
            r2 = this;
            r2.<init>()
            java.beans.PropertyChangeSupport r0 = new java.beans.PropertyChangeSupport
            r0.<init>(r2)
            r2.propertyChangeSupport = r0
            r2.rootPane = r3
            com.formdev.flatlaf.extras.FlatInspector$1 r0 = new com.formdev.flatlaf.extras.FlatInspector$1
            r0.<init>()
            r2.mouseMotionListener = r0
            java.awt.Component r0 = r3.getGlassPane()
            java.awt.event.MouseMotionListener r1 = r2.mouseMotionListener
            void r0 = r0.addMouseMotionListener(r1)
            r2.keyListener = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.extras.FlatInspector.<init>(javax.swing.JRootPane):void");
    }

    private /* synthetic */ void lambda$new$1(JRootPane rootPane2, AWTEvent e) {
        KeyEvent keyEvent = (KeyEvent) e;
        int keyCode = keyEvent.getKeyCode();
        int id = e.getID();
        if (id == 401) {
            if (keyCode == 17 || keyCode == 16) {
                this.wasCtrlOrShiftKeyPressed = true;
            }
        } else if (id == 402 && this.wasCtrlOrShiftKeyPressed) {
            if (keyCode == 17) {
                this.inspectParentLevel++;
                int parentLevel = inspect(this.lastX, this.lastY);
                if (this.inspectParentLevel > parentLevel) {
                    this.inspectParentLevel = parentLevel;
                }
            } else if (keyCode == 16 && this.inspectParentLevel > 0) {
                this.inspectParentLevel--;
                int parentLevel2 = inspect(this.lastX, this.lastY);
                if (this.inspectParentLevel > parentLevel2) {
                    this.inspectParentLevel = Math.max(parentLevel2 - 1, 0);
                    inspect(this.lastX, this.lastY);
                }
            }
        }
        if (keyCode == 27) {
            keyEvent.consume();
            if (id != 401) {
                return;
            }
            if (((FlatInspector) rootPane2.getClientProperty(FlatInspector.class)) == this) {
                uninstall();
                rootPane2.putClientProperty(FlatInspector.class, (Object) null);
                return;
            }
            setEnabled(false);
        }
    }

    private void uninstall() {
        setEnabled(false);
        this.rootPane.getGlassPane().setVisible(false);
        this.rootPane.getGlassPane().removeMouseMotionListener(this.mouseMotionListener);
    }

    public void addPropertyChangeListener(PropertyChangeListener l) {
        this.propertyChangeSupport.addPropertyChangeListener(l);
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        this.propertyChangeSupport.removePropertyChangeListener(l);
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public void setEnabled(boolean enabled2) {
        boolean z;
        if (this.enabled != enabled2) {
            this.enabled = enabled2;
            this.rootPane.getGlassPane().setOpaque(false);
            this.rootPane.getGlassPane().setVisible(enabled2);
            Toolkit toolkit = Toolkit.getDefaultToolkit();
            if (enabled2) {
                toolkit.addAWTEventListener(this.keyListener, 8);
            } else {
                toolkit.removeAWTEventListener(this.keyListener);
            }
            if (enabled2) {
                Point pt = new Point(MouseInfo.getPointerInfo().getLocation());
                SwingUtilities.convertPointFromScreen(pt, this.rootPane);
                this.lastX = pt.x;
                this.lastY = pt.y;
                inspect(this.lastX, this.lastY);
            } else {
                this.lastComponent = null;
                this.inspectParentLevel = 0;
                if (this.highlightFigure != null) {
                    this.highlightFigure.getParent().remove(this.highlightFigure);
                }
                this.highlightFigure = null;
                if (this.tip != null) {
                    this.tip.getParent().remove(this.tip);
                }
                this.tip = null;
            }
            PropertyChangeSupport propertyChangeSupport2 = this.propertyChangeSupport;
            if (!enabled2) {
                z = true;
            } else {
                z = false;
            }
            propertyChangeSupport2.firePropertyChange("enabled", z, enabled2);
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockSplitter
        jadx.core.utils.exceptions.JadxRuntimeException: Missing block: 13
        	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.getBlock(BlockSplitter.java:307)
        	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.setupConnections(BlockSplitter.java:236)
        	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.splitBasicBlocks(BlockSplitter.java:129)
        	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.visit(BlockSplitter.java:52)
        */
    public void update() {
        /*
            r1 = this;
            javax.swing.JRootPane r0 = r1.rootPane
            java.awt.Component r0 = r0.getGlassPane()
            boolean r0 = r0.isVisible()
            if (r0 != 0) goto L_0x000d
        L_0x000c:
            return
        L_?:
            r0 = move-result
            java.awt.EventQueue.invokeLater(r0)
            goto L_0x000c
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.extras.FlatInspector.update():void");
    }

    private /* synthetic */ void lambda$update$2() {
        setEnabled(false);
        setEnabled(true);
        inspect(this.lastX, this.lastY);
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private int inspect(int x, int y) {
        Component parent;
        Point pt = SwingUtilities.convertPoint(this.rootPane.getGlassPane(), x, y, this.rootPane);
        Component c = getDeepestComponentAt(this.rootPane, pt.x, pt.y);
        int parentLevel = 0;
        for (int i = 0; i < this.inspectParentLevel && c != null && (parent = c.getParent()) != null; i++) {
            c = parent;
            parentLevel++;
        }
        if (c != this.lastComponent) {
            this.lastComponent = c;
            highlight(c);
            showToolTip(c, x, y, parentLevel);
        }
        return parentLevel;
    }

    private Component getDeepestComponentAt(Component parent, int x, int y) {
        Component c;
        if (!parent.contains(x, y)) {
            return null;
        }
        if (parent instanceof Container) {
            Component[] components = ((Container) parent).getComponents();
            for (Component child : components) {
                if (child != null && child.isVisible()) {
                    int cx = x - child.getX();
                    int cy = y - child.getY();
                    if (child instanceof Container) {
                        c = getDeepestComponentAt(child, cx, cy);
                    } else {
                        c = child.getComponentAt(cx, cy);
                    }
                    if (!(c == null || !c.isVisible() || c == this.highlightFigure || c == this.tip || (((c.getParent() instanceof JRootPane) && c == c.getParent().getGlassPane()) || "com.formdev.flatlaf.ui.FlatWindowResizer".equals(c.getClass().getName())))) {
                        return c;
                    }
                }
            }
        }
        return parent;
    }

    private void highlight(Component c) {
        if (this.highlightFigure == null) {
            this.highlightFigure = createHighlightFigure();
            this.rootPane.getLayeredPane().add(this.highlightFigure, HIGHLIGHT_LAYER);
        }
        this.highlightFigure.setVisible(c != null);
        if (c != null) {
            Insets insets = this.rootPane.getInsets();
            this.highlightFigure.setBounds(new Rectangle(SwingUtilities.convertPoint(c, -insets.left, -insets.top, this.rootPane), c.getSize()));
        }
    }

    private JComponent createHighlightFigure() {
        JComponent c = new JComponent() {
            /* class com.formdev.flatlaf.extras.FlatInspector.AnonymousClass2 */

            /* access modifiers changed from: protected */
            public void paintComponent(Graphics g) {
                g.setColor(getBackground());
                g.fillRect(0, 0, getWidth(), getHeight());
            }

            /* access modifiers changed from: protected */
            public void paintBorder(Graphics g) {
                Object[] oldRenderingHints = FlatUIUtils.setRenderingHints(g);
                FlatInspector.super.paintBorder(g);
                FlatUIUtils.resetRenderingHints(g, oldRenderingHints);
            }
        };
        c.setBackground(new Color(255, 0, 0, 32));
        c.setBorder(new LineBorder(Color.red));
        return c;
    }

    private void showToolTip(Component c, int x, int y, int parentLevel) {
        if (c != null) {
            if (this.tip == null) {
                this.tip = new JToolTip() {
                    /* class com.formdev.flatlaf.extras.FlatInspector.AnonymousClass3 */

                    public void updateUI() {
                        setUI(FlatToolTipUI.createUI(this));
                    }
                };
                this.rootPane.getLayeredPane().add(this.tip, TOOLTIP_LAYER);
            } else {
                this.tip.setVisible(true);
            }
            this.tip.setTipText(buildToolTipText(c, parentLevel));
            int tx = x + UIScale.scale(8);
            int ty = y + UIScale.scale(16);
            Dimension size = this.tip.getPreferredSize();
            Rectangle visibleRect = this.rootPane.getVisibleRect();
            if (size.width + tx > visibleRect.x + visibleRect.width) {
                tx -= size.width + UIScale.scale(16);
            }
            if (size.height + ty > visibleRect.y + visibleRect.height) {
                ty -= size.height + UIScale.scale(32);
            }
            if (tx < visibleRect.x) {
                tx = visibleRect.x;
            }
            if (ty < visibleRect.y) {
                ty = visibleRect.y;
            }
            this.tip.setBounds(tx, ty, size.width, size.height);
            this.tip.repaint();
        } else if (this.tip != null) {
            this.tip.setVisible(false);
        }
    }

    private static String buildToolTipText(Component c, int parentLevel) {
        LayoutManager layout;
        String name = c.getClass().getName();
        String text = "Class: " + name.substring(name.lastIndexOf(46) + 1) + " (" + c.getClass().getPackage().getName() + ")\nSize: " + c.getWidth() + ',' + c.getHeight() + "  @ " + c.getX() + ',' + c.getY() + '\n';
        if (c instanceof Container) {
            text = text + "Insets: " + toString(((Container) c).getInsets()) + '\n';
        }
        Insets margin = null;
        if (c instanceof AbstractButton) {
            margin = ((AbstractButton) c).getMargin();
        } else if (c instanceof JTextComponent) {
            margin = ((JTextComponent) c).getMargin();
        } else if (c instanceof JMenuBar) {
            margin = ((JMenuBar) c).getMargin();
        } else if (c instanceof JToolBar) {
            margin = ((JToolBar) c).getMargin();
        }
        if (margin != null) {
            text = text + "Margin: " + toString(margin) + '\n';
        }
        Dimension prefSize = c.getPreferredSize();
        Dimension minSize = c.getMinimumSize();
        Dimension maxSize = c.getMaximumSize();
        String text2 = text + "Pref size: " + prefSize.width + ',' + prefSize.height + '\n' + "Min size: " + minSize.width + ',' + minSize.height + '\n' + "Max size: " + maxSize.width + ',' + maxSize.height + '\n';
        if (c instanceof JComponent) {
            text2 = text2 + "Border: " + toString(((JComponent) c).getBorder()) + '\n';
        }
        String text3 = text2 + "Background: " + toString(c.getBackground()) + '\n' + "Foreground: " + toString(c.getForeground()) + '\n' + "Font: " + toString(c.getFont()) + '\n';
        if (c instanceof JComponent) {
            try {
                Field f = JComponent.class.getDeclaredField("ui");
                f.setAccessible(true);
                Object ui = f.get(c);
                text3 = text3 + "UI: " + (ui != null ? ui.getClass().getName() : "null") + '\n';
            } catch (IllegalAccessException | IllegalArgumentException | NoSuchFieldException | SecurityException e) {
            }
        }
        if ((c instanceof Container) && (layout = ((Container) c).getLayout()) != null) {
            text3 = text3 + "Layout: " + layout.getClass().getName() + '\n';
        }
        String text4 = (text3 + "Enabled: " + c.isEnabled() + '\n') + "Opaque: " + c.isOpaque() + ((!(c instanceof JComponent) || !FlatUIUtils.hasOpaqueBeenExplicitlySet((JComponent) c)) ? "" : " EXPLICIT") + '\n';
        if (c instanceof AbstractButton) {
            text4 = text4 + "ContentAreaFilled: " + ((AbstractButton) c).isContentAreaFilled() + '\n';
        }
        String text5 = ((text4 + "Focusable: " + c.isFocusable() + '\n') + "Left-to-right: " + c.getComponentOrientation().isLeftToRight() + '\n') + "Parent: " + (c.getParent() != null ? c.getParent().getClass().getName() : "null");
        if (parentLevel > 0) {
            text5 = text5 + "\n\nParent level: " + parentLevel;
        }
        if (parentLevel > 0) {
            return text5 + "\n(press Ctrl/Shift to increase/decrease level)";
        }
        return text5 + "\n\n(press Ctrl key to inspect parent)";
    }

    private static String toString(Insets insets) {
        if (insets == null) {
            return "null";
        }
        return insets.top + "," + insets.left + ',' + insets.bottom + ',' + insets.right + (insets instanceof UIResource ? " UI" : "");
    }

    private static String toString(Color c) {
        if (c == null) {
            return "null";
        }
        String s = Long.toString(((long) c.getRGB()) & 4294967295L, 16);
        if (c instanceof UIResource) {
            return s + " UI";
        }
        return s;
    }

    private static String toString(Font f) {
        if (f == null) {
            return "null";
        }
        return f.getFamily() + " " + f.getSize() + " " + f.getStyle() + (f instanceof UIResource ? " UI" : "");
    }

    private static String toString(Border b) {
        if (b == null) {
            return "null";
        }
        String s = b.getClass().getName();
        if (b instanceof EmptyBorder) {
            s = s + '(' + toString(((EmptyBorder) b).getBorderInsets()) + ')';
        }
        if (b instanceof UIResource) {
            return s + " UI";
        }
        return s;
    }
}
