package com.formdev.flatlaf.extras.components;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.JScrollPane;

public class FlatScrollPane extends JScrollPane implements FlatComponentExtension {
    public boolean isShowButtons() {
        return getClientPropertyBoolean(FlatClientProperties.SCROLL_BAR_SHOW_BUTTONS, "ScrollBar.showButtons");
    }

    public void setShowButtons(boolean showButtons) {
        putClientProperty(FlatClientProperties.SCROLL_BAR_SHOW_BUTTONS, Boolean.valueOf(showButtons));
    }

    public boolean isSmoothScrolling() {
        return getClientPropertyBoolean(FlatClientProperties.SCROLL_PANE_SMOOTH_SCROLLING, "ScrollPane.smoothScrolling");
    }

    public void setSmoothScrolling(boolean smoothScrolling) {
        putClientProperty(FlatClientProperties.SCROLL_PANE_SMOOTH_SCROLLING, Boolean.valueOf(smoothScrolling));
    }

    public Object getOutline() {
        return getClientProperty(FlatClientProperties.OUTLINE);
    }

    public void setOutline(Object outline) {
        putClientProperty(FlatClientProperties.OUTLINE, outline);
    }
}
