package com.formdev.flatlaf.ui;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.util.HiDPIUtils;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicProgressBarUI;

public class FlatProgressBarUI extends BasicProgressBarUI {
    protected int arc;
    protected Dimension horizontalSize;
    private PropertyChangeListener propertyChangeListener;
    protected Dimension verticalSize;

    public static ComponentUI createUI(JComponent c) {
        return new FlatProgressBarUI();
    }

    /* access modifiers changed from: protected */
    public void installDefaults() {
        FlatProgressBarUI.super.installDefaults();
        LookAndFeel.installProperty(this.progressBar, "opaque", false);
        this.arc = UIManager.getInt("ProgressBar.arc");
        this.horizontalSize = UIManager.getDimension("ProgressBar.horizontalSize");
        this.verticalSize = UIManager.getDimension("ProgressBar.verticalSize");
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [java.beans.PropertyChangeListener, void] */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void installListeners() {
        /*
            r2 = this;
            void r0 = com.formdev.flatlaf.ui.FlatProgressBarUI.super.installListeners()
            r2.propertyChangeListener = r0
            javax.swing.JProgressBar r0 = r2.progressBar
            java.beans.PropertyChangeListener r1 = r2.propertyChangeListener
            r0.addPropertyChangeListener(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.ui.FlatProgressBarUI.installListeners():void");
    }

    private /* synthetic */ void lambda$installListeners$0(PropertyChangeEvent e) {
        String propertyName = e.getPropertyName();
        char c = 65535;
        switch (propertyName.hashCode()) {
            case 904341840:
                if (propertyName.equals(FlatClientProperties.PROGRESS_BAR_LARGE_HEIGHT)) {
                    c = 0;
                    break;
                }
                break;
            case 2133843791:
                if (propertyName.equals(FlatClientProperties.PROGRESS_BAR_SQUARE)) {
                    c = 1;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
            case 1:
                this.progressBar.revalidate();
                this.progressBar.repaint();
                return;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public void uninstallListeners() {
        FlatProgressBarUI.super.uninstallListeners();
        this.progressBar.removePropertyChangeListener(this.propertyChangeListener);
        this.propertyChangeListener = null;
    }

    public Dimension getPreferredSize(JComponent c) {
        Dimension size = FlatProgressBarUI.super.getPreferredSize(c);
        if (this.progressBar.isStringPainted() || FlatClientProperties.clientPropertyBoolean(c, FlatClientProperties.PROGRESS_BAR_LARGE_HEIGHT, false)) {
            Insets insets = this.progressBar.getInsets();
            FontMetrics fm = this.progressBar.getFontMetrics(this.progressBar.getFont());
            if (this.progressBar.getOrientation() == 0) {
                size.height = Math.max(fm.getHeight() + insets.top + insets.bottom, getPreferredInnerHorizontal().height);
            } else {
                size.width = Math.max(fm.getHeight() + insets.left + insets.right, getPreferredInnerVertical().width);
            }
        }
        return size;
    }

    /* access modifiers changed from: protected */
    public Dimension getPreferredInnerHorizontal() {
        return UIScale.scale(this.horizontalSize);
    }

    /* access modifiers changed from: protected */
    public Dimension getPreferredInnerVertical() {
        return UIScale.scale(this.verticalSize);
    }

    public void update(Graphics g, JComponent c) {
        if (c.isOpaque()) {
            FlatUIUtils.paintParentBackground(g, c);
        }
        paint(g, c);
    }

    public void paint(Graphics g, JComponent c) {
        int arc2;
        int amountFull;
        RoundRectangle2D.Float progressShape;
        int i;
        Insets insets = this.progressBar.getInsets();
        int x = insets.left;
        int y = insets.top;
        int width = this.progressBar.getWidth() - (insets.right + insets.left);
        int height = this.progressBar.getHeight() - (insets.top + insets.bottom);
        if (width > 0 && height > 0) {
            boolean horizontal = this.progressBar.getOrientation() == 0;
            if (FlatClientProperties.clientPropertyBoolean(c, FlatClientProperties.PROGRESS_BAR_SQUARE, false)) {
                arc2 = 0;
            } else {
                arc2 = Math.min(UIScale.scale(this.arc), horizontal ? height : width);
            }
            Object[] oldRenderingHints = FlatUIUtils.setRenderingHints(g);
            RoundRectangle2D.Float trackShape = new RoundRectangle2D.Float((float) x, (float) y, (float) width, (float) height, (float) arc2, (float) arc2);
            g.setColor(this.progressBar.getBackground());
            ((Graphics2D) g).fill(trackShape);
            int amountFull2 = 0;
            if (this.progressBar.isIndeterminate()) {
                this.boxRect = getBox(this.boxRect);
                if (this.boxRect != null) {
                    g.setColor(this.progressBar.getForeground());
                    ((Graphics2D) g).fill(new RoundRectangle2D.Float((float) this.boxRect.x, (float) this.boxRect.y, (float) this.boxRect.width, (float) this.boxRect.height, (float) arc2, (float) arc2));
                    amountFull = 0;
                }
                amountFull = amountFull2;
            } else {
                amountFull2 = getAmountFull(insets, width, height);
                if (horizontal) {
                    progressShape = new RoundRectangle2D.Float(c.getComponentOrientation().isLeftToRight() ? (float) x : (float) ((width - amountFull2) + x), (float) y, (float) amountFull2, (float) height, (float) arc2, (float) arc2);
                } else {
                    progressShape = new RoundRectangle2D.Float((float) x, (float) ((height - amountFull2) + y), (float) width, (float) amountFull2, (float) arc2, (float) arc2);
                }
                g.setColor(this.progressBar.getForeground());
                if (horizontal) {
                    i = height;
                } else {
                    i = width;
                }
                if (amountFull2 < i) {
                    Area area = new Area(trackShape);
                    area.intersect(new Area(progressShape));
                    ((Graphics2D) g).fill(area);
                    amountFull = amountFull2;
                } else {
                    ((Graphics2D) g).fill(progressShape);
                    amountFull = amountFull2;
                }
            }
            FlatUIUtils.resetRenderingHints(g, oldRenderingHints);
            if (this.progressBar.isStringPainted()) {
                paintString(g, x, y, width, height, amountFull, insets);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void paintString(Graphics g, int x, int y, int width, int height, int amountFull, Insets b) {
        FlatProgressBarUI.super.paintString(HiDPIUtils.createGraphicsTextYCorrection((Graphics2D) g), x, y, width, height, amountFull, b);
    }

    /* access modifiers changed from: protected */
    public void setAnimationIndex(int newValue) {
        FlatProgressBarUI.super.setAnimationIndex(newValue);
        double systemScaleFactor = UIScale.getSystemScaleFactor(this.progressBar.getGraphicsConfiguration());
        if (((double) ((int) systemScaleFactor)) != systemScaleFactor) {
            this.progressBar.repaint();
        }
    }
}
