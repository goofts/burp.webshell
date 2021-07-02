package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.JToolBar;
import javax.swing.UIManager;

public class FlatToolBarBorder extends FlatMarginBorder {
    private static final int DOT_COUNT = 4;
    private static final int DOT_SIZE = 2;
    private static final int GRIP_SIZE = 6;
    protected final Color gripColor = UIManager.getColor("ToolBar.gripColor");

    public FlatToolBarBorder() {
        super(UIManager.getInsets("ToolBar.borderMargins"));
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        if ((c instanceof JToolBar) && ((JToolBar) c).isFloatable()) {
            Graphics2D g2 = g.create();
            try {
                FlatUIUtils.setRenderingHints(g2);
                g2.setColor(this.gripColor);
                paintGrip(c, g2, x, y, width, height);
            } finally {
                g2.dispose();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void paintGrip(Component c, Graphics g, int x, int y, int width, int height) {
        Rectangle r = calculateGripBounds(c, x, y, width, height);
        FlatUIUtils.paintGrip(g, r.x, r.y, r.width, r.height, ((JToolBar) c).getOrientation() == 1, 4, 2, 2, false);
    }

    /* access modifiers changed from: protected */
    public Rectangle calculateGripBounds(Component c, int x, int y, int width, int height) {
        Rectangle r = FlatUIUtils.subtractInsets(new Rectangle(x, y, width, height), super.getBorderInsets(c, new Insets(0, 0, 0, 0)));
        int gripSize = UIScale.scale(6);
        if (((JToolBar) c).getOrientation() == 0) {
            if (!c.getComponentOrientation().isLeftToRight()) {
                r.x = (r.x + r.width) - gripSize;
            }
            r.width = gripSize;
        } else {
            r.height = gripSize;
        }
        return r;
    }

    @Override // com.formdev.flatlaf.ui.FlatMarginBorder
    public Insets getBorderInsets(Component c, Insets insets) {
        Insets insets2 = super.getBorderInsets(c, insets);
        if ((c instanceof JToolBar) && ((JToolBar) c).isFloatable()) {
            int gripInset = UIScale.scale(6);
            if (((JToolBar) c).getOrientation() != 0) {
                insets2.top += gripInset;
            } else if (c.getComponentOrientation().isLeftToRight()) {
                insets2.left += gripInset;
            } else {
                insets2.right += gripInset;
            }
        }
        return insets2;
    }
}
