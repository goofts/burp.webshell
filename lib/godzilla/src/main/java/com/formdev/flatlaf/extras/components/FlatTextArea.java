package com.formdev.flatlaf.extras.components;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.JTextArea;

public class FlatTextArea extends JTextArea implements FlatComponentExtension {
    public int getMinimumWidth() {
        return getClientPropertyInt(FlatClientProperties.MINIMUM_WIDTH, "Component.minimumWidth");
    }

    public void setMinimumWidth(int minimumWidth) {
        putClientProperty(FlatClientProperties.MINIMUM_WIDTH, minimumWidth >= 0 ? Integer.valueOf(minimumWidth) : null);
    }
}
