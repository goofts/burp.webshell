package com.formdev.flatlaf.icons;

import com.formdev.flatlaf.ui.FlatButtonUI;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import javax.swing.UIManager;

public class FlatHelpButtonIcon extends FlatAbstractIcon {
    protected final Color background = UIManager.getColor("HelpButton.background");
    protected final Color borderColor = UIManager.getColor("HelpButton.borderColor");
    protected final Color disabledBackground = UIManager.getColor("HelpButton.disabledBackground");
    protected final Color disabledBorderColor = UIManager.getColor("HelpButton.disabledBorderColor");
    protected final Color disabledQuestionMarkColor = UIManager.getColor("HelpButton.disabledQuestionMarkColor");
    protected final Color focusColor = UIManager.getColor("Component.focusColor");
    protected final int focusWidth = UIManager.getInt("Component.focusWidth");
    protected final Color focusedBackground = UIManager.getColor("HelpButton.focusedBackground");
    protected final Color focusedBorderColor = UIManager.getColor("HelpButton.focusedBorderColor");
    protected final Color hoverBackground = UIManager.getColor("HelpButton.hoverBackground");
    protected final Color hoverBorderColor = UIManager.getColor("HelpButton.hoverBorderColor");
    protected final int iconSize = ((this.focusWidth * 2) + 22);
    protected final Color pressedBackground = UIManager.getColor("HelpButton.pressedBackground");
    protected final Color questionMarkColor = UIManager.getColor("HelpButton.questionMarkColor");

    public FlatHelpButtonIcon() {
        super(0, 0, null);
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.icons.FlatAbstractIcon
    public void paintIcon(Component c, Graphics2D g2) {
        boolean enabled = c.isEnabled();
        if (FlatUIUtils.isPermanentFocusOwner(c) && FlatButtonUI.isFocusPainted(c)) {
            g2.setColor(this.focusColor);
            g2.fill(new Ellipse2D.Float(0.5f, 0.5f, (float) (this.iconSize - 1), (float) (this.iconSize - 1)));
        }
        g2.setColor(FlatButtonUI.buttonStateColor(c, this.borderColor, this.disabledBorderColor, this.focusedBorderColor, this.hoverBorderColor, null));
        g2.fill(new Ellipse2D.Float(((float) this.focusWidth) + 0.5f, ((float) this.focusWidth) + 0.5f, 21.0f, 21.0f));
        g2.setColor(FlatUIUtils.deriveColor(FlatButtonUI.buttonStateColor(c, this.background, this.disabledBackground, this.focusedBackground, this.hoverBackground, this.pressedBackground), this.background));
        g2.fill(new Ellipse2D.Float(((float) this.focusWidth) + 1.5f, ((float) this.focusWidth) + 1.5f, 19.0f, 19.0f));
        Path2D q = new Path2D.Float();
        q.moveTo(11.0d, 5.0d);
        q.curveTo(8.8d, 5.0d, 7.0d, 6.8d, 7.0d, 9.0d);
        q.lineTo(9.0d, 9.0d);
        q.curveTo(9.0d, 7.9d, 9.9d, 7.0d, 11.0d, 7.0d);
        q.curveTo(12.1d, 7.0d, 13.0d, 7.9d, 13.0d, 9.0d);
        q.curveTo(13.0d, 11.0d, 10.0d, 10.75d, 10.0d, 14.0d);
        q.lineTo(12.0d, 14.0d);
        q.curveTo(12.0d, 11.75d, 15.0d, 11.5d, 15.0d, 9.0d);
        q.curveTo(15.0d, 6.8d, 13.2d, 5.0d, 11.0d, 5.0d);
        q.closePath();
        g2.translate(this.focusWidth, this.focusWidth);
        g2.setColor(enabled ? this.questionMarkColor : this.disabledQuestionMarkColor);
        g2.fill(q);
        g2.fillRect(10, 15, 2, 2);
    }

    @Override // com.formdev.flatlaf.icons.FlatAbstractIcon
    public int getIconWidth() {
        return UIScale.scale(this.iconSize);
    }

    @Override // com.formdev.flatlaf.icons.FlatAbstractIcon
    public int getIconHeight() {
        return UIScale.scale(this.iconSize);
    }
}
