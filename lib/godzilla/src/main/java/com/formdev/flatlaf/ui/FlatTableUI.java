package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.Graphics2DProxy;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.geom.Rectangle2D;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTableUI;

public class FlatTableUI extends BasicTableUI {
    protected Dimension intercellSpacing;
    private Dimension oldIntercellSpacing;
    private boolean oldShowHorizontalLines;
    private boolean oldShowVerticalLines;
    protected Color selectionBackground;
    protected Color selectionForeground;
    protected Color selectionInactiveBackground;
    protected Color selectionInactiveForeground;
    protected boolean showHorizontalLines;
    protected boolean showVerticalLines;

    public static ComponentUI createUI(JComponent c) {
        return new FlatTableUI();
    }

    /* access modifiers changed from: protected */
    public void installDefaults() {
        FlatTableUI.super.installDefaults();
        this.showHorizontalLines = UIManager.getBoolean("Table.showHorizontalLines");
        this.showVerticalLines = UIManager.getBoolean("Table.showVerticalLines");
        this.intercellSpacing = UIManager.getDimension("Table.intercellSpacing");
        this.selectionBackground = UIManager.getColor("Table.selectionBackground");
        this.selectionForeground = UIManager.getColor("Table.selectionForeground");
        this.selectionInactiveBackground = UIManager.getColor("Table.selectionInactiveBackground");
        this.selectionInactiveForeground = UIManager.getColor("Table.selectionInactiveForeground");
        toggleSelectionColors();
        int rowHeight = FlatUIUtils.getUIInt("Table.rowHeight", 16);
        if (rowHeight > 0) {
            LookAndFeel.installProperty(this.table, "rowHeight", Integer.valueOf(UIScale.scale(rowHeight)));
        }
        if (!this.showHorizontalLines) {
            this.oldShowHorizontalLines = this.table.getShowHorizontalLines();
            this.table.setShowHorizontalLines(false);
        }
        if (!this.showVerticalLines) {
            this.oldShowVerticalLines = this.table.getShowVerticalLines();
            this.table.setShowVerticalLines(false);
        }
        if (this.intercellSpacing != null) {
            this.oldIntercellSpacing = this.table.getIntercellSpacing();
            this.table.setIntercellSpacing(this.intercellSpacing);
        }
    }

    /* access modifiers changed from: protected */
    public void uninstallDefaults() {
        FlatTableUI.super.uninstallDefaults();
        this.selectionBackground = null;
        this.selectionForeground = null;
        this.selectionInactiveBackground = null;
        this.selectionInactiveForeground = null;
        if (!this.showHorizontalLines && this.oldShowHorizontalLines && !this.table.getShowHorizontalLines()) {
            this.table.setShowHorizontalLines(true);
        }
        if (!this.showVerticalLines && this.oldShowVerticalLines && !this.table.getShowVerticalLines()) {
            this.table.setShowVerticalLines(true);
        }
        if (this.intercellSpacing != null && this.table.getIntercellSpacing().equals(this.intercellSpacing)) {
            this.table.setIntercellSpacing(this.oldIntercellSpacing);
        }
    }

    /* access modifiers changed from: protected */
    public FocusListener createFocusListener() {
        return new BasicTableUI.FocusHandler() {
            /* class com.formdev.flatlaf.ui.FlatTableUI.AnonymousClass1 */

            public void focusGained(FocusEvent e) {
                FlatTableUI.super.focusGained(e);
                FlatTableUI.this.toggleSelectionColors();
            }

            /* JADX WARN: Type inference failed for: r0v0, types: [void, java.lang.Runnable] */
            /* JADX WARNING: Unknown variable types count: 1 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public void focusLost(java.awt.event.FocusEvent r2) {
                /*
                    r1 = this;
                    void r0 = com.formdev.flatlaf.ui.FlatTableUI.super.focusLost(r2)
                    java.awt.EventQueue.invokeLater(r0)
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatTableUI.AnonymousClass1.focusLost(java.awt.event.FocusEvent):void");
            }

            private /* synthetic */ void lambda$focusLost$0() {
                FlatTableUI.this.toggleSelectionColors();
            }
        };
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void toggleSelectionColors() {
        if (this.table != null) {
            if (FlatUIUtils.isPermanentFocusOwner(this.table)) {
                if (this.table.getSelectionBackground() == this.selectionInactiveBackground) {
                    this.table.setSelectionBackground(this.selectionBackground);
                }
                if (this.table.getSelectionForeground() == this.selectionInactiveForeground) {
                    this.table.setSelectionForeground(this.selectionForeground);
                    return;
                }
                return;
            }
            if (this.table.getSelectionBackground() == this.selectionBackground) {
                this.table.setSelectionBackground(this.selectionInactiveBackground);
            }
            if (this.table.getSelectionForeground() == this.selectionForeground) {
                this.table.setSelectionForeground(this.selectionInactiveForeground);
            }
        }
    }

    public void paint(Graphics g, JComponent c) {
        final boolean horizontalLines = this.table.getShowHorizontalLines();
        final boolean verticalLines = this.table.getShowVerticalLines();
        if (horizontalLines || verticalLines) {
            final boolean hideLastVerticalLine = hideLastVerticalLine();
            final int tableWidth = this.table.getWidth();
            double systemScaleFactor = UIScale.getSystemScaleFactor((Graphics2D) g);
            final double lineThickness = (1.0d / systemScaleFactor) * ((double) ((int) systemScaleFactor));
            g = new Graphics2DProxy((Graphics2D) g) {
                /* class com.formdev.flatlaf.ui.FlatTableUI.AnonymousClass2 */

                @Override // com.formdev.flatlaf.util.Graphics2DProxy
                public void drawLine(int x1, int y1, int x2, int y2) {
                    if (!hideLastVerticalLine || !verticalLines || x1 != x2 || y1 != 0 || x1 != tableWidth - 1 || !wasInvokedFromPaintGrid()) {
                        super.drawLine(x1, y1, x2, y2);
                    }
                }

                @Override // com.formdev.flatlaf.util.Graphics2DProxy
                public void fillRect(int x, int y, int width, int height) {
                    if (!hideLastVerticalLine || !verticalLines || width != 1 || y != 0 || x != tableWidth - 1 || !wasInvokedFromPaintGrid()) {
                        if (lineThickness != 1.0d) {
                            if (horizontalLines && height == 1 && wasInvokedFromPaintGrid()) {
                                super.fill(new Rectangle2D.Double((double) x, (double) y, (double) width, lineThickness));
                                return;
                            } else if (verticalLines && width == 1 && y == 0 && wasInvokedFromPaintGrid()) {
                                super.fill(new Rectangle2D.Double((double) x, (double) y, lineThickness, (double) height));
                                return;
                            }
                        }
                        super.fillRect(x, y, width, height);
                    }
                }

                private boolean wasInvokedFromPaintGrid() {
                    StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
                    int i = 0;
                    while (true) {
                        if (i >= 10 && i >= stackTrace.length) {
                            return false;
                        }
                        if ("javax.swing.plaf.basic.BasicTableUI".equals(stackTrace[i].getClassName()) && "paintGrid".equals(stackTrace[i].getMethodName())) {
                            return true;
                        }
                        i++;
                    }
                }
            };
        }
        FlatTableUI.super.paint(g, c);
    }

    /* access modifiers changed from: protected */
    public boolean hideLastVerticalLine() {
        boolean z = true;
        JViewport viewport = SwingUtilities.getUnwrappedParent(this.table);
        Container viewportParent = viewport != null ? viewport.getParent() : null;
        if (!(viewportParent instanceof JScrollPane) || this.table.getX() + this.table.getWidth() < viewport.getWidth()) {
            return false;
        }
        JScrollPane scrollPane = (JScrollPane) viewportParent;
        JViewport rowHeader = scrollPane.getRowHeader();
        if (scrollPane.getComponentOrientation().isLeftToRight()) {
            if (viewport == rowHeader) {
                z = false;
            }
        } else if (!(viewport == rowHeader || rowHeader == null)) {
            z = false;
        }
        return z;
    }
}
