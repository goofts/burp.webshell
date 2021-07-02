package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Objects;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicTableHeaderUI;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class FlatTableHeaderUI extends BasicTableHeaderUI {
    protected Color bottomSeparatorColor;
    protected int height;
    protected Color separatorColor;
    protected int sortIconPosition;

    public static ComponentUI createUI(JComponent c) {
        return new FlatTableHeaderUI();
    }

    /* access modifiers changed from: protected */
    public void installDefaults() {
        FlatTableHeaderUI.super.installDefaults();
        this.separatorColor = UIManager.getColor("TableHeader.separatorColor");
        this.bottomSeparatorColor = UIManager.getColor("TableHeader.bottomSeparatorColor");
        this.height = UIManager.getInt("TableHeader.height");
        String objects = Objects.toString(UIManager.getString("TableHeader.sortIconPosition"), "right");
        char c = 65535;
        switch (objects.hashCode()) {
            case -1383228885:
                if (objects.equals("bottom")) {
                    c = 4;
                    break;
                }
                break;
            case 115029:
                if (objects.equals("top")) {
                    c = 3;
                    break;
                }
                break;
            case 3317767:
                if (objects.equals("left")) {
                    c = 2;
                    break;
                }
                break;
            case 108511772:
                if (objects.equals("right")) {
                    c = 1;
                    break;
                }
                break;
        }
        switch (c) {
            case 2:
                this.sortIconPosition = 2;
                return;
            case 3:
                this.sortIconPosition = 1;
                return;
            case 4:
                this.sortIconPosition = 3;
                return;
            default:
                this.sortIconPosition = 4;
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void uninstallDefaults() {
        FlatTableHeaderUI.super.uninstallDefaults();
        this.separatorColor = null;
        this.bottomSeparatorColor = null;
    }

    public void paint(Graphics g, JComponent c) {
        if (this.header.getColumnModel().getColumnCount() > 0) {
            TableCellRenderer defaultRenderer = this.header.getDefaultRenderer();
            boolean paintBorders = isSystemDefaultRenderer(defaultRenderer);
            if (!paintBorders) {
                paintBorders = isSystemDefaultRenderer(defaultRenderer.getTableCellRendererComponent(this.header.getTable(), "", false, false, -1, 0));
            }
            if (paintBorders) {
                paintColumnBorders(g, c);
            }
            FlatTableCellHeaderRenderer sortIconRenderer = null;
            if (this.sortIconPosition != 4) {
                sortIconRenderer = new FlatTableCellHeaderRenderer(this.header.getDefaultRenderer());
                this.header.setDefaultRenderer(sortIconRenderer);
            }
            FlatTableHeaderUI.super.paint(g, c);
            if (sortIconRenderer != null) {
                sortIconRenderer.reset();
                this.header.setDefaultRenderer(sortIconRenderer.delegate);
            }
            if (paintBorders) {
                paintDraggedColumnBorders(g, c);
            }
        }
    }

    private boolean isSystemDefaultRenderer(Object headerRenderer) {
        String rendererClassName = headerRenderer.getClass().getName();
        return rendererClassName.equals("sun.swing.table.DefaultTableCellHeaderRenderer") || rendererClassName.equals("sun.swing.FilePane$AlignableTableHeaderRenderer");
    }

    /* access modifiers changed from: protected */
    public void paintColumnBorders(Graphics g, JComponent c) {
        int width = c.getWidth();
        int height2 = c.getHeight();
        float lineWidth = UIScale.scale(1.0f);
        float bottomLineIndent = lineWidth * 3.0f;
        TableColumnModel columnModel = this.header.getColumnModel();
        int sepCount = columnModel.getColumnCount();
        if (hideLastVerticalLine()) {
            sepCount--;
        }
        Graphics2D g2 = g.create();
        try {
            FlatUIUtils.setRenderingHints(g2);
            g2.setColor(this.bottomSeparatorColor);
            g2.fill(new Rectangle2D.Float(0.0f, ((float) height2) - lineWidth, (float) width, lineWidth));
            g2.setColor(this.separatorColor);
            float h = ((float) height2) - bottomLineIndent;
            if (this.header.getComponentOrientation().isLeftToRight()) {
                int x = 0;
                for (int i = 0; i < sepCount; i++) {
                    x += columnModel.getColumn(i).getWidth();
                    g2.fill(new Rectangle2D.Float(((float) x) - lineWidth, lineWidth, lineWidth, h));
                }
                if (!hideTrailingVerticalLine()) {
                    g2.fill(new Rectangle2D.Float(((float) this.header.getWidth()) - lineWidth, lineWidth, lineWidth, h));
                }
            } else {
                Rectangle cellRect = this.header.getHeaderRect(0);
                int x2 = cellRect.x + cellRect.width;
                int i2 = 0;
                while (i2 < sepCount) {
                    x2 -= columnModel.getColumn(i2).getWidth();
                    g2.fill(new Rectangle2D.Float(((float) x2) - (i2 < sepCount + -1 ? lineWidth : 0.0f), lineWidth, lineWidth, h));
                    i2++;
                }
                if (!hideTrailingVerticalLine()) {
                    g2.fill(new Rectangle2D.Float(0.0f, lineWidth, lineWidth, h));
                }
            }
        } finally {
            g2.dispose();
        }
    }

    private void paintDraggedColumnBorders(Graphics g, JComponent c) {
        TableColumn draggedColumn = this.header.getDraggedColumn();
        if (draggedColumn != null) {
            TableColumnModel columnModel = this.header.getColumnModel();
            int columnCount = columnModel.getColumnCount();
            int draggedColumnIndex = -1;
            int i = 0;
            while (true) {
                if (i >= columnCount) {
                    break;
                } else if (columnModel.getColumn(i) == draggedColumn) {
                    draggedColumnIndex = i;
                    break;
                } else {
                    i++;
                }
            }
            if (draggedColumnIndex >= 0) {
                float lineWidth = UIScale.scale(1.0f);
                float bottomLineIndent = lineWidth * 3.0f;
                Rectangle r = this.header.getHeaderRect(draggedColumnIndex);
                r.x += this.header.getDraggedDistance();
                Graphics2D g2 = g.create();
                try {
                    FlatUIUtils.setRenderingHints(g2);
                    g2.setColor(this.bottomSeparatorColor);
                    g2.fill(new Rectangle2D.Float((float) r.x, ((float) (r.y + r.height)) - lineWidth, (float) r.width, lineWidth));
                    g2.setColor(this.separatorColor);
                    g2.fill(new Rectangle2D.Float((float) r.x, lineWidth, lineWidth, ((float) r.height) - bottomLineIndent));
                    g2.fill(new Rectangle2D.Float(((float) (r.x + r.width)) - lineWidth, ((float) r.y) + lineWidth, lineWidth, ((float) r.height) - bottomLineIndent));
                } finally {
                    g2.dispose();
                }
            }
        }
    }

    public Dimension getPreferredSize(JComponent c) {
        Dimension size = FlatTableHeaderUI.super.getPreferredSize(c);
        if (size.height > 0) {
            size.height = Math.max(size.height, UIScale.scale(this.height));
        }
        return size;
    }

    /* access modifiers changed from: protected */
    public boolean hideLastVerticalLine() {
        boolean z = true;
        Container viewport = this.header.getParent();
        Container viewportParent = viewport != null ? viewport.getParent() : null;
        if (!(viewportParent instanceof JScrollPane)) {
            return false;
        }
        Rectangle cellRect = this.header.getHeaderRect(this.header.getColumnModel().getColumnCount() - 1);
        if (((JScrollPane) viewportParent).getComponentOrientation().isLeftToRight()) {
            if (cellRect.x + cellRect.width < viewport.getWidth()) {
                z = false;
            }
        } else if (cellRect.x > 0) {
            z = false;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public boolean hideTrailingVerticalLine() {
        Container viewport = this.header.getParent();
        Container viewportParent = viewport != null ? viewport.getParent() : null;
        if (!(viewportParent instanceof JScrollPane)) {
            return false;
        }
        JScrollPane scrollPane = (JScrollPane) viewportParent;
        if (viewport == scrollPane.getColumnHeader() && scrollPane.getCorner("UPPER_TRAILING_CORNER") == null) {
            return true;
        }
        return false;
    }

    private class FlatTableCellHeaderRenderer implements TableCellRenderer, Border, UIResource {
        private final TableCellRenderer delegate;
        private JLabel l;
        private int oldHorizontalTextPosition = -1;
        private Border origBorder;
        private Icon sortIcon;

        FlatTableCellHeaderRenderer(TableCellRenderer delegate2) {
            this.delegate = delegate2;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component c = this.delegate.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            if (!(c instanceof JLabel)) {
                return c;
            }
            this.l = (JLabel) c;
            if (FlatTableHeaderUI.this.sortIconPosition == 2) {
                if (this.oldHorizontalTextPosition < 0) {
                    this.oldHorizontalTextPosition = this.l.getHorizontalTextPosition();
                }
                this.l.setHorizontalTextPosition(4);
            } else {
                this.sortIcon = this.l.getIcon();
                this.origBorder = this.l.getBorder();
                this.l.setIcon((Icon) null);
                this.l.setBorder(this);
            }
            return this.l;
        }

        /* access modifiers changed from: package-private */
        public void reset() {
            if (this.l != null && FlatTableHeaderUI.this.sortIconPosition == 2 && this.oldHorizontalTextPosition >= 0) {
                this.l.setHorizontalTextPosition(this.oldHorizontalTextPosition);
            }
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            int yi;
            if (this.origBorder != null) {
                this.origBorder.paintBorder(c, g, x, y, width, height);
            }
            if (this.sortIcon != null) {
                int xi = x + ((width - this.sortIcon.getIconWidth()) / 2);
                if (FlatTableHeaderUI.this.sortIconPosition == 1) {
                    yi = y + UIScale.scale(1);
                } else {
                    yi = (((y + height) - this.sortIcon.getIconHeight()) - 1) - ((int) (1.0f * UIScale.getUserScaleFactor()));
                }
                this.sortIcon.paintIcon(c, g, xi, yi);
            }
        }

        public Insets getBorderInsets(Component c) {
            return this.origBorder != null ? this.origBorder.getBorderInsets(c) : new Insets(0, 0, 0, 0);
        }

        public boolean isBorderOpaque() {
            if (this.origBorder != null) {
                return this.origBorder.isBorderOpaque();
            }
            return false;
        }
    }
}
