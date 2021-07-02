package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.CellRendererPane;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

public class FlatTreeUI extends BasicTreeUI {
    protected Color selectionBackground;
    protected Color selectionBorderColor;
    protected Color selectionForeground;
    protected Color selectionInactiveBackground;
    protected Color selectionInactiveForeground;
    protected boolean showCellFocusIndicator;
    protected boolean wideSelection;

    public static ComponentUI createUI(JComponent c) {
        return new FlatTreeUI();
    }

    /* access modifiers changed from: protected */
    public void installDefaults() {
        FlatTreeUI.super.installDefaults();
        LookAndFeel.installBorder(this.tree, "Tree.border");
        this.selectionBackground = UIManager.getColor("Tree.selectionBackground");
        this.selectionForeground = UIManager.getColor("Tree.selectionForeground");
        this.selectionInactiveBackground = UIManager.getColor("Tree.selectionInactiveBackground");
        this.selectionInactiveForeground = UIManager.getColor("Tree.selectionInactiveForeground");
        this.selectionBorderColor = UIManager.getColor("Tree.selectionBorderColor");
        this.wideSelection = UIManager.getBoolean("Tree.wideSelection");
        this.showCellFocusIndicator = UIManager.getBoolean("Tree.showCellFocusIndicator");
        int rowHeight = FlatUIUtils.getUIInt("Tree.rowHeight", 16);
        if (rowHeight > 0) {
            LookAndFeel.installProperty(this.tree, "rowHeight", Integer.valueOf(UIScale.scale(rowHeight)));
        }
        setLeftChildIndent(UIScale.scale(getLeftChildIndent()));
        setRightChildIndent(UIScale.scale(getRightChildIndent()));
    }

    /* access modifiers changed from: protected */
    public void uninstallDefaults() {
        FlatTreeUI.super.uninstallDefaults();
        LookAndFeel.uninstallBorder(this.tree);
        this.selectionBackground = null;
        this.selectionForeground = null;
        this.selectionInactiveBackground = null;
        this.selectionInactiveForeground = null;
        this.selectionBorderColor = null;
    }

    /* access modifiers changed from: protected */
    public MouseListener createMouseListener() {
        return new BasicTreeUI.MouseHandler() {
            /* class com.formdev.flatlaf.ui.FlatTreeUI.AnonymousClass1 */

            public void mousePressed(MouseEvent e) {
                FlatTreeUI.super.mousePressed(handleWideMouseEvent(e));
            }

            public void mouseReleased(MouseEvent e) {
                FlatTreeUI.super.mouseReleased(handleWideMouseEvent(e));
            }

            public void mouseDragged(MouseEvent e) {
                FlatTreeUI.super.mouseDragged(handleWideMouseEvent(e));
            }

            private MouseEvent handleWideMouseEvent(MouseEvent e) {
                int x;
                int y;
                TreePath path;
                Rectangle bounds;
                int newX;
                if (!FlatTreeUI.this.isWideSelection() || !FlatTreeUI.this.tree.isEnabled() || !SwingUtilities.isLeftMouseButton(e) || e.isConsumed() || (path = FlatTreeUI.this.getClosestPathForLocation(FlatTreeUI.this.tree, (x = e.getX()), (y = e.getY()))) == null || FlatTreeUI.this.isLocationInExpandControl(path, x, y) || (bounds = FlatTreeUI.this.getPathBounds(FlatTreeUI.this.tree, path)) == null || y < bounds.y || y >= bounds.y + bounds.height || (newX = Math.max(bounds.x, Math.min(x, (bounds.x + bounds.width) - 1))) == x) {
                    return e;
                }
                return new MouseEvent(e.getComponent(), e.getID(), e.getWhen(), e.getModifiers() | e.getModifiersEx(), newX, e.getY(), e.getClickCount(), e.isPopupTrigger(), e.getButton());
            }
        };
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.formdev.flatlaf.ui.FlatTreeUI$2, java.beans.PropertyChangeListener] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.beans.PropertyChangeListener createPropertyChangeListener() {
        /*
            r1 = this;
            com.formdev.flatlaf.ui.FlatTreeUI$2 r0 = new com.formdev.flatlaf.ui.FlatTreeUI$2
            r0.<init>()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatTreeUI.createPropertyChangeListener():java.beans.PropertyChangeListener");
    }

    /* access modifiers changed from: protected */
    public void paintRow(Graphics g, Rectangle clipBounds, Insets insets, Rectangle bounds, TreePath path, int row, boolean isExpanded, boolean hasBeenExpanded, boolean isLeaf) {
        Color color;
        boolean isEditing = this.editingComponent != null && this.editingRow == row;
        boolean isSelected = this.tree.isRowSelected(row);
        boolean isDropRow = isDropRow(row);
        boolean needsSelectionPainting = (isSelected || isDropRow) && isPaintSelection();
        if (!isEditing || needsSelectionPainting) {
            boolean hasFocus = FlatUIUtils.isPermanentFocusOwner(this.tree);
            boolean cellHasFocus = hasFocus && row == getLeadSelectionRow();
            if (!hasFocus && isSelected && (this.tree.getParent() instanceof CellRendererPane)) {
                hasFocus = FlatUIUtils.isPermanentFocusOwner(this.tree.getParent().getParent());
            }
            Component rendererComponent = this.currentCellRenderer.getTreeCellRendererComponent(this.tree, path.getLastPathComponent(), isSelected, isExpanded, isLeaf, row, cellHasFocus);
            Color oldBackgroundSelectionColor = null;
            if (isSelected && !hasFocus && !isDropRow) {
                if (rendererComponent instanceof DefaultTreeCellRenderer) {
                    DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) rendererComponent;
                    if (renderer.getBackgroundSelectionColor() == this.selectionBackground) {
                        oldBackgroundSelectionColor = renderer.getBackgroundSelectionColor();
                        renderer.setBackgroundSelectionColor(this.selectionInactiveBackground);
                    }
                } else if (rendererComponent.getBackground() == this.selectionBackground) {
                    rendererComponent.setBackground(this.selectionInactiveBackground);
                }
                if (rendererComponent.getForeground() == this.selectionForeground) {
                    rendererComponent.setForeground(this.selectionInactiveForeground);
                }
            }
            Color oldBorderSelectionColor = null;
            if (isSelected && hasFocus && ((!this.showCellFocusIndicator || this.tree.getMinSelectionRow() == this.tree.getMaxSelectionRow()) && (rendererComponent instanceof DefaultTreeCellRenderer))) {
                DefaultTreeCellRenderer renderer2 = (DefaultTreeCellRenderer) rendererComponent;
                if (renderer2.getBorderSelectionColor() == this.selectionBorderColor) {
                    oldBorderSelectionColor = renderer2.getBorderSelectionColor();
                    renderer2.setBorderSelectionColor((Color) null);
                }
            }
            if (needsSelectionPainting) {
                Color oldColor = g.getColor();
                if (isDropRow) {
                    color = UIManager.getColor("Tree.dropCellBackground");
                } else if (rendererComponent instanceof DefaultTreeCellRenderer) {
                    color = ((DefaultTreeCellRenderer) rendererComponent).getBackgroundSelectionColor();
                } else {
                    color = hasFocus ? this.selectionBackground : this.selectionInactiveBackground;
                }
                g.setColor(color);
                if (isWideSelection()) {
                    g.fillRect(0, bounds.y, this.tree.getWidth(), bounds.height);
                    if (shouldPaintExpandControl(path, row, isExpanded, hasBeenExpanded, isLeaf)) {
                        paintExpandControl(g, clipBounds, insets, bounds, path, row, isExpanded, hasBeenExpanded, isLeaf);
                    }
                } else {
                    int xOffset = 0;
                    int imageOffset = 0;
                    if (rendererComponent instanceof JLabel) {
                        JLabel label = (JLabel) rendererComponent;
                        Icon icon = label.getIcon();
                        imageOffset = (icon == null || label.getText() == null) ? 0 : icon.getIconWidth() + Math.max(label.getIconTextGap() - 1, 0);
                        xOffset = label.getComponentOrientation().isLeftToRight() ? imageOffset : 0;
                    }
                    g.fillRect(bounds.x + xOffset, bounds.y, bounds.width - imageOffset, bounds.height);
                }
                g.setColor(oldColor);
            }
            if (!isEditing) {
                this.rendererPane.paintComponent(g, rendererComponent, this.tree, bounds.x, bounds.y, bounds.width, bounds.height, true);
            }
            if (oldBackgroundSelectionColor != null) {
                ((DefaultTreeCellRenderer) rendererComponent).setBackgroundSelectionColor(oldBackgroundSelectionColor);
            }
            if (oldBorderSelectionColor != null) {
                ((DefaultTreeCellRenderer) rendererComponent).setBorderSelectionColor(oldBorderSelectionColor);
            }
        }
    }

    private boolean isDropRow(int row) {
        JTree.DropLocation dropLocation = this.tree.getDropLocation();
        return dropLocation != null && dropLocation.getChildIndex() == -1 && this.tree.getRowForPath(dropLocation.getPath()) == row;
    }

    /* access modifiers changed from: protected */
    public Rectangle getDropLineRect(JTree.DropLocation loc) {
        Rectangle r = FlatTreeUI.super.getDropLineRect(loc);
        return isWideSelection() ? new Rectangle(0, r.y, this.tree.getWidth(), r.height) : r;
    }

    /* access modifiers changed from: protected */
    public boolean isWideSelection() {
        return FlatClientProperties.clientPropertyBoolean(this.tree, FlatClientProperties.TREE_WIDE_SELECTION, this.wideSelection);
    }

    /* access modifiers changed from: protected */
    public boolean isPaintSelection() {
        return FlatClientProperties.clientPropertyBoolean(this.tree, FlatClientProperties.TREE_PAINT_SELECTION, true);
    }
}
