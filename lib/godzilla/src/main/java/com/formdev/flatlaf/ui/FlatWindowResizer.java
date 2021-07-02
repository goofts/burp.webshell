package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.UIScale;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowStateListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.function.Supplier;
import javassist.bytecode.Opcode;
import javax.swing.DesktopManager;
import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JRootPane;
import javax.swing.UIManager;

public abstract class FlatWindowResizer implements PropertyChangeListener, ComponentListener {
    protected static final Integer WINDOW_RESIZER_LAYER = Integer.valueOf(JLayeredPane.DRAG_LAYER.intValue() + 1);
    protected final int borderDragThickness = FlatUIUtils.getUIInt("RootPane.borderDragThickness", 5);
    protected final DragBorderComponent bottomDragComp;
    protected final int cornerDragWidth = FlatUIUtils.getUIInt("RootPane.cornerDragWidth", 16);
    protected final boolean honorDialogMinimumSizeOnResize = UIManager.getBoolean("RootPane.honorDialogMinimumSizeOnResize");
    protected final boolean honorFrameMinimumSizeOnResize = UIManager.getBoolean("RootPane.honorFrameMinimumSizeOnResize");
    protected final DragBorderComponent leftDragComp;
    protected final JComponent resizeComp;
    protected final DragBorderComponent rightDragComp;
    protected final DragBorderComponent topDragComp;

    /* access modifiers changed from: protected */
    public abstract Rectangle getWindowBounds();

    /* access modifiers changed from: protected */
    public abstract Dimension getWindowMinimumSize();

    /* access modifiers changed from: protected */
    public abstract boolean honorMinimumSizeOnResize();

    /* access modifiers changed from: protected */
    public abstract boolean isWindowResizable();

    /* access modifiers changed from: protected */
    public abstract void setWindowBounds(Rectangle rectangle);

    protected FlatWindowResizer(JComponent resizeComp2) {
        JComponent jComponent;
        this.resizeComp = resizeComp2;
        this.topDragComp = createDragBorderComponent(6, 8, 7);
        this.bottomDragComp = createDragBorderComponent(4, 9, 5);
        this.leftDragComp = createDragBorderComponent(6, 10, 4);
        this.rightDragComp = createDragBorderComponent(7, 11, 5);
        if (resizeComp2 instanceof JRootPane) {
            jComponent = ((JRootPane) resizeComp2).getLayeredPane();
        } else {
            jComponent = resizeComp2;
        }
        Integer cons = jComponent instanceof JLayeredPane ? WINDOW_RESIZER_LAYER : null;
        jComponent.add(this.topDragComp, cons, 0);
        jComponent.add(this.bottomDragComp, cons, 1);
        jComponent.add(this.leftDragComp, cons, 2);
        jComponent.add(this.rightDragComp, cons, 3);
        resizeComp2.addComponentListener(this);
        resizeComp2.addPropertyChangeListener("ancestor", this);
        if (resizeComp2.isDisplayable()) {
            addNotify();
        }
    }

    /* access modifiers changed from: protected */
    public DragBorderComponent createDragBorderComponent(int leadingResizeDir, int centerResizeDir, int trailingResizeDir) {
        return new DragBorderComponent(leadingResizeDir, centerResizeDir, trailingResizeDir);
    }

    public void uninstall() {
        removeNotify();
        this.resizeComp.removeComponentListener(this);
        this.resizeComp.removePropertyChangeListener("ancestor", this);
        Container cont = this.topDragComp.getParent();
        cont.remove(this.topDragComp);
        cont.remove(this.bottomDragComp);
        cont.remove(this.leftDragComp);
        cont.remove(this.rightDragComp);
    }

    public void doLayout() {
        if (this.topDragComp.isVisible()) {
            int width = this.resizeComp.getWidth();
            int height = this.resizeComp.getHeight();
            if (width != 0 && height != 0) {
                Insets resizeInsets = getResizeInsets();
                int thickness = UIScale.scale(this.borderDragThickness);
                int topThickness = Math.max(resizeInsets.top, thickness);
                int bottomThickness = Math.max(resizeInsets.bottom, thickness);
                int leftThickness = Math.max(resizeInsets.left, thickness);
                int rightThickness = Math.max(resizeInsets.right, thickness);
                int y2 = 0 + topThickness;
                int height2 = (height - topThickness) - bottomThickness;
                this.topDragComp.setBounds(0, 0, width, topThickness);
                this.bottomDragComp.setBounds(0, (0 + height) - bottomThickness, width, bottomThickness);
                this.leftDragComp.setBounds(0, y2, leftThickness, height2);
                this.rightDragComp.setBounds((0 + width) - rightThickness, y2, rightThickness, height2);
                int cornerDelta = UIScale.scale(this.cornerDragWidth - this.borderDragThickness);
                this.topDragComp.setCornerDragWidths(leftThickness + cornerDelta, rightThickness + cornerDelta);
                this.bottomDragComp.setCornerDragWidths(leftThickness + cornerDelta, rightThickness + cornerDelta);
                this.leftDragComp.setCornerDragWidths(cornerDelta, cornerDelta);
                this.rightDragComp.setCornerDragWidths(cornerDelta, cornerDelta);
            }
        }
    }

    /* access modifiers changed from: protected */
    public Insets getResizeInsets() {
        return new Insets(0, 0, 0, 0);
    }

    /* access modifiers changed from: protected */
    public void addNotify() {
        updateVisibility();
    }

    /* access modifiers changed from: protected */
    public void removeNotify() {
        updateVisibility();
    }

    /* access modifiers changed from: protected */
    public void updateVisibility() {
        boolean visible = isWindowResizable();
        if (visible != this.topDragComp.isVisible()) {
            this.topDragComp.setVisible(visible);
            this.bottomDragComp.setVisible(visible);
            this.leftDragComp.setVisible(visible);
            this.rightDragComp.setEnabled(visible);
            if (visible) {
                this.rightDragComp.setVisible(true);
                doLayout();
                return;
            }
            this.rightDragComp.setBounds(0, 0, 1, 1);
        }
    }

    /* access modifiers changed from: package-private */
    public boolean isDialog() {
        return false;
    }

    /* access modifiers changed from: protected */
    public void beginResizing(int direction) {
    }

    /* access modifiers changed from: protected */
    public void endResizing() {
    }

    public void propertyChange(PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();
        char c = 65535;
        switch (propertyName.hashCode()) {
            case -973829677:
                if (propertyName.equals("ancestor")) {
                    c = 0;
                    break;
                }
                break;
            case 2144232107:
                if (propertyName.equals("resizable")) {
                    c = 1;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                if (e.getNewValue() != null) {
                    addNotify();
                    return;
                } else {
                    removeNotify();
                    return;
                }
            case 1:
                updateVisibility();
                return;
            default:
                return;
        }
    }

    public void componentResized(ComponentEvent e) {
        doLayout();
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentShown(ComponentEvent e) {
    }

    public void componentHidden(ComponentEvent e) {
    }

    public static class WindowResizer extends FlatWindowResizer implements WindowStateListener {
        protected Window window;

        public WindowResizer(JRootPane rootPane) {
            super(rootPane);
        }

        /* access modifiers changed from: protected */
        @Override // com.formdev.flatlaf.ui.FlatWindowResizer
        public void addNotify() {
            Container parent = this.resizeComp.getParent();
            this.window = parent instanceof Window ? (Window) parent : null;
            if (this.window instanceof Frame) {
                this.window.addPropertyChangeListener("resizable", this);
                this.window.addWindowStateListener(this);
            }
            FlatWindowResizer.super.addNotify();
        }

        /* access modifiers changed from: protected */
        @Override // com.formdev.flatlaf.ui.FlatWindowResizer
        public void removeNotify() {
            if (this.window instanceof Frame) {
                this.window.removePropertyChangeListener("resizable", this);
                this.window.removeWindowStateListener(this);
            }
            this.window = null;
            FlatWindowResizer.super.removeNotify();
        }

        /* access modifiers changed from: protected */
        @Override // com.formdev.flatlaf.ui.FlatWindowResizer
        public boolean isWindowResizable() {
            if (FlatUIUtils.isFullScreen(this.resizeComp)) {
                return false;
            }
            if (this.window instanceof Frame) {
                return this.window.isResizable() && (this.window.getExtendedState() & 6) == 0;
            } else if (this.window instanceof Dialog) {
                return this.window.isResizable();
            } else {
                return false;
            }
        }

        /* access modifiers changed from: protected */
        @Override // com.formdev.flatlaf.ui.FlatWindowResizer
        public Rectangle getWindowBounds() {
            return this.window.getBounds();
        }

        /* access modifiers changed from: protected */
        @Override // com.formdev.flatlaf.ui.FlatWindowResizer
        public void setWindowBounds(Rectangle r) {
            this.window.setBounds(r);
            doLayout();
            if (Toolkit.getDefaultToolkit().isDynamicLayoutActive()) {
                this.window.validate();
                this.resizeComp.repaint();
            }
        }

        /* access modifiers changed from: protected */
        @Override // com.formdev.flatlaf.ui.FlatWindowResizer
        public boolean honorMinimumSizeOnResize() {
            return (this.honorFrameMinimumSizeOnResize && (this.window instanceof Frame)) || (this.honorDialogMinimumSizeOnResize && (this.window instanceof Dialog));
        }

        /* access modifiers changed from: protected */
        @Override // com.formdev.flatlaf.ui.FlatWindowResizer
        public Dimension getWindowMinimumSize() {
            return this.window.getMinimumSize();
        }

        /* access modifiers changed from: package-private */
        @Override // com.formdev.flatlaf.ui.FlatWindowResizer
        public boolean isDialog() {
            return this.window instanceof Dialog;
        }

        public void windowStateChanged(WindowEvent e) {
            updateVisibility();
        }
    }

    public static class InternalFrameResizer extends FlatWindowResizer {
        protected final Supplier<DesktopManager> desktopManager;

        public InternalFrameResizer(JInternalFrame frame, Supplier<DesktopManager> desktopManager2) {
            super(frame);
            this.desktopManager = desktopManager2;
            frame.addPropertyChangeListener("resizable", this);
        }

        @Override // com.formdev.flatlaf.ui.FlatWindowResizer
        public void uninstall() {
            getFrame().removePropertyChangeListener("resizable", this);
            FlatWindowResizer.super.uninstall();
        }

        private JInternalFrame getFrame() {
            return this.resizeComp;
        }

        /* access modifiers changed from: protected */
        @Override // com.formdev.flatlaf.ui.FlatWindowResizer
        public Insets getResizeInsets() {
            return getFrame().getInsets();
        }

        /* access modifiers changed from: protected */
        @Override // com.formdev.flatlaf.ui.FlatWindowResizer
        public boolean isWindowResizable() {
            return getFrame().isResizable();
        }

        /* access modifiers changed from: protected */
        @Override // com.formdev.flatlaf.ui.FlatWindowResizer
        public Rectangle getWindowBounds() {
            return getFrame().getBounds();
        }

        /* access modifiers changed from: protected */
        @Override // com.formdev.flatlaf.ui.FlatWindowResizer
        public void setWindowBounds(Rectangle r) {
            this.desktopManager.get().resizeFrame(getFrame(), r.x, r.y, r.width, r.height);
        }

        /* access modifiers changed from: protected */
        @Override // com.formdev.flatlaf.ui.FlatWindowResizer
        public boolean honorMinimumSizeOnResize() {
            return true;
        }

        /* access modifiers changed from: protected */
        @Override // com.formdev.flatlaf.ui.FlatWindowResizer
        public Dimension getWindowMinimumSize() {
            return getFrame().getMinimumSize();
        }

        /* access modifiers changed from: protected */
        @Override // com.formdev.flatlaf.ui.FlatWindowResizer
        public void beginResizing(int direction) {
            this.desktopManager.get().beginResizingFrame(getFrame(), direction);
        }

        /* access modifiers changed from: protected */
        @Override // com.formdev.flatlaf.ui.FlatWindowResizer
        public void endResizing() {
            this.desktopManager.get().endResizingFrame(getFrame());
        }
    }

    /* access modifiers changed from: protected */
    public class DragBorderComponent extends JComponent implements MouseListener, MouseMotionListener {
        private final int centerResizeDir;
        private int dragBottomOffset;
        private int dragLeftOffset;
        private int dragRightOffset;
        private int dragTopOffset;
        private int leadingCornerDragWidth;
        private final int leadingResizeDir;
        private int resizeDir = -1;
        private int trailingCornerDragWidth;
        private final int trailingResizeDir;

        protected DragBorderComponent(int leadingResizeDir2, int centerResizeDir2, int trailingResizeDir2) {
            this.leadingResizeDir = leadingResizeDir2;
            this.centerResizeDir = centerResizeDir2;
            this.trailingResizeDir = trailingResizeDir2;
            setResizeDir(centerResizeDir2);
            setVisible(false);
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        /* access modifiers changed from: package-private */
        public void setCornerDragWidths(int leading, int trailing) {
            this.leadingCornerDragWidth = leading;
            this.trailingCornerDragWidth = trailing;
        }

        /* access modifiers changed from: protected */
        public void setResizeDir(int resizeDir2) {
            if (this.resizeDir != resizeDir2) {
                this.resizeDir = resizeDir2;
                setCursor(Cursor.getPredefinedCursor(resizeDir2));
            }
        }

        public Dimension getPreferredSize() {
            int thickness = UIScale.scale(FlatWindowResizer.this.borderDragThickness);
            return new Dimension(thickness, thickness);
        }

        /* access modifiers changed from: protected */
        public void paintComponent(Graphics g) {
            FlatWindowResizer.super.paintChildren(g);
            FlatWindowResizer.this.updateVisibility();
        }

        public void mouseClicked(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
            if (FlatWindowResizer.this.isWindowResizable()) {
                int xOnScreen = e.getXOnScreen();
                int yOnScreen = e.getYOnScreen();
                Rectangle windowBounds = FlatWindowResizer.this.getWindowBounds();
                this.dragLeftOffset = xOnScreen - windowBounds.x;
                this.dragTopOffset = yOnScreen - windowBounds.y;
                this.dragRightOffset = (windowBounds.x + windowBounds.width) - xOnScreen;
                this.dragBottomOffset = (windowBounds.y + windowBounds.height) - yOnScreen;
                int direction = 0;
                switch (this.resizeDir) {
                    case 4:
                        direction = 6;
                        break;
                    case 5:
                        direction = 4;
                        break;
                    case 6:
                        direction = 8;
                        break;
                    case 7:
                        direction = 2;
                        break;
                    case 8:
                        direction = 1;
                        break;
                    case 9:
                        direction = 5;
                        break;
                    case 10:
                        direction = 7;
                        break;
                    case 11:
                        direction = 3;
                        break;
                }
                FlatWindowResizer.this.beginResizing(direction);
            }
        }

        public void mouseReleased(MouseEvent e) {
            if (FlatWindowResizer.this.isWindowResizable()) {
                this.dragBottomOffset = 0;
                this.dragTopOffset = 0;
                this.dragRightOffset = 0;
                this.dragLeftOffset = 0;
                FlatWindowResizer.this.endResizing();
            }
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
            boolean topOrBottom = this.centerResizeDir == 8 || this.centerResizeDir == 9;
            int xy = topOrBottom ? e.getX() : e.getY();
            setResizeDir(xy <= this.leadingCornerDragWidth ? this.leadingResizeDir : xy >= (topOrBottom ? getWidth() : getHeight()) - this.trailingCornerDragWidth ? this.trailingResizeDir : this.centerResizeDir);
        }

        public void mouseDragged(MouseEvent e) {
            if (FlatWindowResizer.this.isWindowResizable()) {
                int xOnScreen = e.getXOnScreen();
                int yOnScreen = e.getYOnScreen();
                Rectangle oldBounds = FlatWindowResizer.this.getWindowBounds();
                Rectangle newBounds = new Rectangle(oldBounds);
                if (this.resizeDir == 8 || this.resizeDir == 6 || this.resizeDir == 7) {
                    newBounds.y = yOnScreen - this.dragTopOffset;
                    newBounds.height += oldBounds.y - newBounds.y;
                }
                if (this.resizeDir == 9 || this.resizeDir == 4 || this.resizeDir == 5) {
                    newBounds.height = (this.dragBottomOffset + yOnScreen) - newBounds.y;
                }
                if (this.resizeDir == 10 || this.resizeDir == 6 || this.resizeDir == 4) {
                    newBounds.x = xOnScreen - this.dragLeftOffset;
                    newBounds.width += oldBounds.x - newBounds.x;
                }
                if (this.resizeDir == 11 || this.resizeDir == 7 || this.resizeDir == 5) {
                    newBounds.width = (this.dragRightOffset + xOnScreen) - newBounds.x;
                }
                Dimension minimumSize = FlatWindowResizer.this.honorMinimumSizeOnResize() ? FlatWindowResizer.this.getWindowMinimumSize() : null;
                if (minimumSize == null) {
                    minimumSize = UIScale.scale(new Dimension((int) Opcode.FCMPG, 50));
                }
                if (newBounds.width < minimumSize.width) {
                    if (newBounds.x != oldBounds.x) {
                        newBounds.x -= minimumSize.width - newBounds.width;
                    }
                    newBounds.width = minimumSize.width;
                }
                if (newBounds.height < minimumSize.height) {
                    if (newBounds.y != oldBounds.y) {
                        newBounds.y -= minimumSize.height - newBounds.height;
                    }
                    newBounds.height = minimumSize.height;
                }
                if (!newBounds.equals(oldBounds)) {
                    FlatWindowResizer.this.setWindowBounds(newBounds);
                }
            }
        }
    }
}
