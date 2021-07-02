package com.formdev.flatlaf.extras.components;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.JProgressBar;

public class FlatProgressBar extends JProgressBar implements FlatComponentExtension {
    public boolean isLargeHeight() {
        return getClientPropertyBoolean(FlatClientProperties.PROGRESS_BAR_LARGE_HEIGHT, false);
    }

    public void setLargeHeight(boolean largeHeight) {
        putClientPropertyBoolean(FlatClientProperties.PROGRESS_BAR_LARGE_HEIGHT, largeHeight, false);
    }

    public boolean isSquare() {
        return getClientPropertyBoolean(FlatClientProperties.PROGRESS_BAR_SQUARE, false);
    }

    public void setSquare(boolean square) {
        putClientPropertyBoolean(FlatClientProperties.PROGRESS_BAR_SQUARE, square, false);
    }
}
