package com.formdev.flatlaf.ui;

import java.awt.Component;
import javax.swing.UIManager;

public class FlatTextBorder extends FlatBorder {
    protected final int arc = UIManager.getInt("TextComponent.arc");

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.ui.FlatBorder
    public int getArc(Component c) {
        if (isCellEditor(c)) {
            return 0;
        }
        Boolean roundRect = FlatUIUtils.isRoundRect(c);
        if (roundRect == null) {
            return this.arc;
        }
        if (roundRect.booleanValue()) {
            return 32767;
        }
        return 0;
    }
}
