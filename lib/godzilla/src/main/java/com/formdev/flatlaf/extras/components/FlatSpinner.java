package com.formdev.flatlaf.extras.components;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.JSpinner;

public class FlatSpinner extends JSpinner implements FlatComponentExtension {
    public int getMinimumWidth() {
        return getClientPropertyInt(FlatClientProperties.MINIMUM_WIDTH, "Component.minimumWidth");
    }

    public void setMinimumWidth(int minimumWidth) {
        putClientProperty(FlatClientProperties.MINIMUM_WIDTH, minimumWidth >= 0 ? Integer.valueOf(minimumWidth) : null);
    }

    public boolean isRoundRect() {
        return getClientPropertyBoolean(FlatClientProperties.COMPONENT_ROUND_RECT, false);
    }

    public void setRoundRect(boolean roundRect) {
        putClientPropertyBoolean(FlatClientProperties.COMPONENT_ROUND_RECT, roundRect, false);
    }

    public Object getOutline() {
        return getClientProperty(FlatClientProperties.OUTLINE);
    }

    public void setOutline(Object outline) {
        putClientProperty(FlatClientProperties.OUTLINE, outline);
    }
}
