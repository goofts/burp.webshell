package com.jgoodies.forms.debug;

import com.jgoodies.forms.layout.FormLayout;
import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JPanel;

public class FormDebugPanel extends JPanel {
    private static final Color DEFAULT_GRID_COLOR = Color.red;
    public static boolean paintRowsDefault = true;
    private Color gridColor;
    private boolean paintDiagonals;
    private boolean paintInBackground;
    private boolean paintRows;

    public FormDebugPanel() {
        this(null);
    }

    public FormDebugPanel(FormLayout layout) {
        this(layout, false, false);
    }

    public FormDebugPanel(boolean paintInBackground2, boolean paintDiagonals2) {
        this(null, paintInBackground2, paintDiagonals2);
    }

    public FormDebugPanel(FormLayout layout, boolean paintInBackground2, boolean paintDiagonals2) {
        super(layout);
        this.paintRows = paintRowsDefault;
        this.gridColor = DEFAULT_GRID_COLOR;
        setPaintInBackground(paintInBackground2);
        setPaintDiagonals(paintDiagonals2);
        setGridColor(DEFAULT_GRID_COLOR);
    }

    public void setPaintInBackground(boolean b) {
        this.paintInBackground = b;
    }

    public void setPaintDiagonals(boolean b) {
        this.paintDiagonals = b;
    }

    public void setPaintRows(boolean b) {
        this.paintRows = b;
    }

    public void setGridColor(Color color) {
        this.gridColor = color;
    }

    /* access modifiers changed from: protected */
    public void paintComponent(Graphics g) {
        FormDebugPanel.super.paintComponent(g);
        if (this.paintInBackground) {
            paintGrid(g);
        }
    }

    public void paint(Graphics g) {
        FormDebugPanel.super.paint(g);
        if (!this.paintInBackground) {
            paintGrid(g);
        }
    }

    private void paintGrid(Graphics g) {
        int start;
        int start2;
        if (getLayout() instanceof FormLayout) {
            FormLayout.LayoutInfo layoutInfo = FormDebugUtils.getLayoutInfo(this);
            int left = layoutInfo.getX();
            int top = layoutInfo.getY();
            int width = layoutInfo.getWidth();
            int height = layoutInfo.getHeight();
            g.setColor(this.gridColor);
            int last = layoutInfo.columnOrigins.length - 1;
            int col = 0;
            while (col <= last) {
                boolean firstOrLast = col == 0 || col == last;
                int x = layoutInfo.columnOrigins[col];
                if (firstOrLast) {
                    start2 = 0;
                } else {
                    start2 = top;
                }
                int stop = firstOrLast ? getHeight() : top + height;
                for (int i = start2; i < stop; i += 5) {
                    g.fillRect(x, i, 1, Math.min(3, stop - i));
                }
                col++;
            }
            int last2 = layoutInfo.rowOrigins.length - 1;
            int row = 0;
            while (row <= last2) {
                boolean firstOrLast2 = row == 0 || row == last2;
                int y = layoutInfo.rowOrigins[row];
                if (firstOrLast2) {
                    start = 0;
                } else {
                    start = left;
                }
                int stop2 = firstOrLast2 ? getWidth() : left + width;
                if (firstOrLast2 || this.paintRows) {
                    for (int i2 = start; i2 < stop2; i2 += 5) {
                        g.fillRect(i2, y, Math.min(3, stop2 - i2), 1);
                    }
                }
                row++;
            }
            if (this.paintDiagonals) {
                g.drawLine(left, top, left + width, top + height);
                g.drawLine(left, top + height, left + width, top);
            }
        }
    }
}
