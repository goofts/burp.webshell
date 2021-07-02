package com.formdev.flatlaf.ui;

import java.awt.Component;
import java.awt.Graphics;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class FlatTableCellBorder extends FlatLineBorder {
    final boolean showCellFocusIndicator = UIManager.getBoolean("Table.showCellFocusIndicator");

    public static class Focused extends FlatTableCellBorder {
    }

    protected FlatTableCellBorder() {
        super(UIManager.getInsets("Table.cellMargins"), UIManager.getColor("Table.cellFocusColor"));
    }

    public static class Default extends FlatTableCellBorder {
        @Override // com.formdev.flatlaf.ui.FlatLineBorder
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        }
    }

    public static class Selected extends FlatTableCellBorder {
        @Override // com.formdev.flatlaf.ui.FlatLineBorder
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            JTable table;
            if (this.showCellFocusIndicator || (table = (JTable) SwingUtilities.getAncestorOfClass(JTable.class, c)) == null || isSelectionEditable(table)) {
                FlatTableCellBorder.super.paintBorder(c, g, x, y, width, height);
            }
        }

        /* access modifiers changed from: protected */
        public boolean isSelectionEditable(JTable table) {
            if (table.getRowSelectionAllowed()) {
                int columnCount = table.getColumnCount();
                int[] selectedRows = table.getSelectedRows();
                for (int selectedRow : selectedRows) {
                    for (int column = 0; column < columnCount; column++) {
                        if (table.isCellEditable(selectedRow, column)) {
                            return true;
                        }
                    }
                }
            }
            if (table.getColumnSelectionAllowed()) {
                int rowCount = table.getRowCount();
                int[] selectedColumns = table.getSelectedColumns();
                for (int selectedColumn : selectedColumns) {
                    for (int row = 0; row < rowCount; row++) {
                        if (table.isCellEditable(row, selectedColumn)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }
}
