package com.formdev.flatlaf.extras.components;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.extras.components.FlatButton;
import java.awt.Color;
import javax.swing.JToggleButton;

public class FlatToggleButton extends JToggleButton implements FlatComponentExtension {
    public FlatButton.ButtonType getButtonType() {
        return (FlatButton.ButtonType) getClientPropertyEnumString(FlatClientProperties.BUTTON_TYPE, FlatButton.ButtonType.class, null, FlatButton.ButtonType.none);
    }

    public void setButtonType(FlatButton.ButtonType buttonType) {
        if (buttonType == FlatButton.ButtonType.none) {
            buttonType = null;
        }
        putClientPropertyEnumString(FlatClientProperties.BUTTON_TYPE, buttonType);
    }

    public boolean isSquareSize() {
        return getClientPropertyBoolean(FlatClientProperties.SQUARE_SIZE, false);
    }

    public void setSquareSize(boolean squareSize) {
        putClientPropertyBoolean(FlatClientProperties.SQUARE_SIZE, squareSize, false);
    }

    public int getMinimumWidth() {
        return getClientPropertyInt(FlatClientProperties.MINIMUM_WIDTH, "ToggleButton.minimumWidth");
    }

    public void setMinimumWidth(int minimumWidth) {
        putClientProperty(FlatClientProperties.MINIMUM_WIDTH, minimumWidth >= 0 ? Integer.valueOf(minimumWidth) : null);
    }

    public int getMinimumHeight() {
        return getClientPropertyInt(FlatClientProperties.MINIMUM_HEIGHT, 0);
    }

    public void setMinimumHeight(int minimumHeight) {
        putClientProperty(FlatClientProperties.MINIMUM_HEIGHT, minimumHeight >= 0 ? Integer.valueOf(minimumHeight) : null);
    }

    public Object getOutline() {
        return getClientProperty(FlatClientProperties.OUTLINE);
    }

    public void setOutline(Object outline) {
        putClientProperty(FlatClientProperties.OUTLINE, outline);
    }

    public int getTabUnderlineHeight() {
        return getClientPropertyInt(FlatClientProperties.TAB_BUTTON_UNDERLINE_HEIGHT, "ToggleButton.tab.underlineHeight");
    }

    public void setTabUnderlineHeight(int tabUnderlineHeight) {
        putClientProperty(FlatClientProperties.TAB_BUTTON_UNDERLINE_HEIGHT, tabUnderlineHeight >= 0 ? Integer.valueOf(tabUnderlineHeight) : null);
    }

    public Color getTabUnderlineColor() {
        return getClientPropertyColor(FlatClientProperties.TAB_BUTTON_UNDERLINE_COLOR, "ToggleButton.tab.underlineColor");
    }

    public void setTabUnderlineColor(Color tabUnderlineColor) {
        putClientProperty(FlatClientProperties.TAB_BUTTON_UNDERLINE_COLOR, tabUnderlineColor);
    }

    public Color getTabSelectedBackground() {
        return getClientPropertyColor(FlatClientProperties.TAB_BUTTON_SELECTED_BACKGROUND, "ToggleButton.tab.selectedBackground");
    }

    public void setTabSelectedBackground(Color tabSelectedBackground) {
        putClientProperty(FlatClientProperties.TAB_BUTTON_SELECTED_BACKGROUND, tabSelectedBackground);
    }
}
