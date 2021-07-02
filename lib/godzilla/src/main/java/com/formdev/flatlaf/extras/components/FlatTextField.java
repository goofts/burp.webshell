package com.formdev.flatlaf.extras.components;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.JTextField;

public class FlatTextField extends JTextField implements FlatComponentExtension {

    public enum SelectAllOnFocusPolicy {
        never,
        once,
        always
    }

    public String getPlaceholderText() {
        return (String) getClientProperty(FlatClientProperties.PLACEHOLDER_TEXT);
    }

    public void setPlaceholderText(String placeholderText) {
        putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholderText);
    }

    public SelectAllOnFocusPolicy getSelectAllOnFocusPolicy() {
        return (SelectAllOnFocusPolicy) getClientPropertyEnumString(FlatClientProperties.SELECT_ALL_ON_FOCUS_POLICY, SelectAllOnFocusPolicy.class, "TextComponent.selectAllOnFocusPolicy", SelectAllOnFocusPolicy.once);
    }

    public void setSelectAllOnFocusPolicy(SelectAllOnFocusPolicy selectAllOnFocusPolicy) {
        putClientPropertyEnumString(FlatClientProperties.SELECT_ALL_ON_FOCUS_POLICY, selectAllOnFocusPolicy);
    }

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
