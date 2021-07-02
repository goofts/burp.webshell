package com.formdev.flatlaf.ui;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

public class FlatFormattedTextFieldUI extends FlatTextFieldUI {
    public static ComponentUI createUI(JComponent c) {
        return new FlatFormattedTextFieldUI();
    }

    /* access modifiers changed from: protected */
    public String getPropertyPrefix() {
        return "FormattedTextField";
    }
}
