package com.formdev.flatlaf.extras.components;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.JComboBox;

public class FlatComboBox<E> extends JComboBox<E> implements FlatComponentExtension {
    public String getPlaceholderText() {
        return (String) getClientProperty(FlatClientProperties.PLACEHOLDER_TEXT);
    }

    public void setPlaceholderText(String placeholderText) {
        putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholderText);
    }

    public int getMinimumWidth() {
        return getClientPropertyInt(FlatClientProperties.MINIMUM_WIDTH, "ComboBox.minimumWidth");
    }

    public void setMinimumWidth(int minimumWidth) {
        putClientProperty(FlatClientProperties.MINIMUM_WIDTH, minimumWidth >= 0 ? Integer.valueOf(minimumWidth) : null);
    }

    public boolean isRoundRect() {
        return getClientPropertyBoolean((Object) FlatClientProperties.COMPONENT_ROUND_RECT, false);
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
