package com.formdev.flatlaf.extras.components;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.JButton;

public class FlatButton extends JButton implements FlatComponentExtension {

    public enum ButtonType {
        none,
        square,
        roundRect,
        tab,
        help,
        toolBarButton
    }

    public ButtonType getButtonType() {
        return (ButtonType) getClientPropertyEnumString(FlatClientProperties.BUTTON_TYPE, ButtonType.class, null, ButtonType.none);
    }

    public void setButtonType(ButtonType buttonType) {
        if (buttonType == ButtonType.none) {
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
        return getClientPropertyInt(FlatClientProperties.MINIMUM_WIDTH, "Button.minimumWidth");
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
}
