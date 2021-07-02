package com.formdev.flatlaf.extras.components;

import com.formdev.flatlaf.FlatClientProperties;
import javax.swing.JTree;

public class FlatTree extends JTree implements FlatComponentExtension {
    public boolean isWideSelection() {
        return getClientPropertyBoolean(FlatClientProperties.TREE_WIDE_SELECTION, "Tree.wideSelection");
    }

    public void setWideSelection(boolean wideSelection) {
        putClientProperty(FlatClientProperties.TREE_WIDE_SELECTION, Boolean.valueOf(wideSelection));
    }

    public boolean isPaintSelection() {
        return getClientPropertyBoolean(FlatClientProperties.TREE_PAINT_SELECTION, true);
    }

    public void setPaintSelection(boolean paintSelection) {
        putClientProperty(FlatClientProperties.TREE_PAINT_SELECTION, Boolean.valueOf(paintSelection));
    }
}
