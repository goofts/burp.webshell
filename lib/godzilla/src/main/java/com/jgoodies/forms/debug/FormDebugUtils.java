package com.jgoodies.forms.debug;

import com.jgoodies.common.base.Preconditions;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Component;
import java.awt.Container;
import javax.swing.JLabel;

public final class FormDebugUtils {
    private FormDebugUtils() {
    }

    public static void dumpAll(Container container) {
        if (!(container.getLayout() instanceof FormLayout)) {
            System.out.println("The container's layout is not a FormLayout.");
            return;
        }
        FormLayout layout = container.getLayout();
        dumpColumnSpecs(layout);
        dumpRowSpecs(layout);
        System.out.println();
        dumpColumnGroups(layout);
        dumpRowGroups(layout);
        System.out.println();
        dumpConstraints(container);
        dumpGridBounds(container);
    }

    public static void dumpColumnSpecs(FormLayout layout) {
        System.out.print("COLUMN SPECS:");
        for (int col = 1; col <= layout.getColumnCount(); col++) {
            System.out.print(layout.getColumnSpec(col).toShortString());
            if (col < layout.getColumnCount()) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    public static void dumpRowSpecs(FormLayout layout) {
        System.out.print("ROW SPECS:   ");
        for (int row = 1; row <= layout.getRowCount(); row++) {
            System.out.print(layout.getRowSpec(row).toShortString());
            if (row < layout.getRowCount()) {
                System.out.print(", ");
            }
        }
        System.out.println();
    }

    public static void dumpColumnGroups(FormLayout layout) {
        dumpGroups("COLUMN GROUPS: ", layout.getColumnGroups());
    }

    public static void dumpRowGroups(FormLayout layout) {
        dumpGroups("ROW GROUPS:    ", layout.getRowGroups());
    }

    public static void dumpGridBounds(Container container) {
        System.out.println("GRID BOUNDS");
        dumpGridBounds(getLayoutInfo(container));
    }

    public static void dumpGridBounds(FormLayout.LayoutInfo layoutInfo) {
        System.out.print("COLUMN ORIGINS: ");
        int[] arr$ = layoutInfo.columnOrigins;
        int len$ = arr$.length;
        for (int i$ = 0; i$ < len$; i$++) {
            System.out.print(arr$[i$] + " ");
        }
        System.out.println();
        System.out.print("ROW ORIGINS:    ");
        int[] arr$2 = layoutInfo.rowOrigins;
        int len$2 = arr$2.length;
        for (int i$2 = 0; i$2 < len$2; i$2++) {
            System.out.print(arr$2[i$2] + " ");
        }
        System.out.println();
    }

    public static void dumpConstraints(Container container) {
        System.out.println("COMPONENT CONSTRAINTS");
        if (!(container.getLayout() instanceof FormLayout)) {
            System.out.println("The container's layout is not a FormLayout.");
            return;
        }
        FormLayout layout = container.getLayout();
        int childCount = container.getComponentCount();
        for (int i = 0; i < childCount; i++) {
            Component child = container.getComponent(i);
            CellConstraints cc = layout.getConstraints(child);
            System.out.print(cc == null ? "no constraints" : cc.toShortString(layout));
            System.out.print("; ");
            System.out.print(child.getClass().getName());
            if (child instanceof JLabel) {
                System.out.print("      \"" + ((JLabel) child).getText() + "\"");
            }
            if (child.getName() != null) {
                System.out.print("; name=");
                System.out.print(child.getName());
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void dumpGroups(String title, int[][] allGroups) {
        System.out.print(title + " {");
        for (int group = 0; group < allGroups.length; group++) {
            int[] groupIndices = allGroups[group];
            System.out.print(" {");
            for (int i = 0; i < groupIndices.length; i++) {
                System.out.print(groupIndices[i]);
                if (i < groupIndices.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.print("} ");
            if (group < allGroups.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("}");
    }

    public static FormLayout.LayoutInfo getLayoutInfo(Container container) {
        Preconditions.checkNotNull(container, "The container must not be null.");
        Preconditions.checkArgument(container.getLayout() instanceof FormLayout, "The container must use an instance of FormLayout.");
        return container.getLayout().getLayoutInfo(container);
    }
}
