package com.formdev.flatlaf.extras.components;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.JScrollBar;

public class FlatScrollBar extends JScrollBar implements FlatComponentExtension {
    public boolean isShowButtons() {
        return getClientPropertyBoolean(FlatClientProperties.SCROLL_BAR_SHOW_BUTTONS, "ScrollBar.showButtons");
    }

    public void setShowButtons(boolean showButtons) {
        putClientProperty(FlatClientProperties.SCROLL_BAR_SHOW_BUTTONS, Boolean.valueOf(showButtons));
    }
}
