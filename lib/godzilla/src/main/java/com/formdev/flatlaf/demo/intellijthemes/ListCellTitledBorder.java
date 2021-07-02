package com.formdev.flatlaf.demo.intellijthemes;

import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Component;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Rectangle2D;
import javax.swing.JList;
import javax.swing.UIManager;
import javax.swing.border.Border;

class ListCellTitledBorder implements Border {
    private final JList<?> list;
    private final String title;

    ListCellTitledBorder(JList<?> list2, String title2) {
        this.list = list2;
        this.title = title2;
    }

    public boolean isBorderOpaque() {
        return true;
    }

    public Insets getBorderInsets(Component c) {
        return new Insets(c.getFontMetrics(this.list.getFont()).getHeight(), 0, 0, 0);
    }

    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        FontMetrics fm = c.getFontMetrics(this.list.getFont());
        int titleWidth = fm.stringWidth(this.title);
        int titleHeight = fm.getHeight();
        g.setColor(this.list.getBackground());
        g.fillRect(x, y, width, titleHeight);
        int gap = UIScale.scale(4);
        Graphics2D g2 = g.create();
        try {
            FlatUIUtils.setRenderingHints(g2);
            g2.setColor(UIManager.getColor("Label.disabledForeground"));
            int sepWidth = (((width - titleWidth) / 2) - gap) - gap;
            if (sepWidth > 0) {
                int sy = y + Math.round(((float) titleHeight) / 2.0f);
                float sepHeight = UIScale.scale(1.0f);
                g2.fill(new Rectangle2D.Float((float) (x + gap), (float) sy, (float) sepWidth, sepHeight));
                g2.fill(new Rectangle2D.Float((float) (((x + width) - gap) - sepWidth), (float) sy, (float) sepWidth, sepHeight));
            }
            FlatUIUtils.drawString(this.list, g2, this.title, x + ((width - titleWidth) / 2), y + fm.getAscent());
        } finally {
            g2.dispose();
        }
    }
}
