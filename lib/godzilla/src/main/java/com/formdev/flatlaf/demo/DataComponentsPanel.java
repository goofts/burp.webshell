package com.formdev.flatlaf.demo;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.ActionEvent;
import javassist.bytecode.Opcode;
import javax.swing.AbstractListModel;
import javax.swing.DefaultCellEditor;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DropMode;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.TransferHandler;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import net.miginfocom.swing.MigLayout;

/* access modifiers changed from: package-private */
public class DataComponentsPanel extends JPanel {
    private JCheckBox columnSelectionCheckBox;
    private JCheckBox dndCheckBox;
    private JCheckBox intercellSpacingCheckBox;
    private JList<String> list1;
    private JList<String> list2;
    private JCheckBox redGridColorCheckBox;
    private JCheckBox rowSelectionCheckBox;
    private JCheckBox showHorizontalLinesCheckBox;
    private JCheckBox showVerticalLinesCheckBox;
    private JTable table1;
    private JTree tree1;
    private JTree tree2;

    DataComponentsPanel() {
        initComponents();
    }

    private void dndChanged() {
        boolean dnd = this.dndCheckBox.isSelected();
        this.list1.setDragEnabled(dnd);
        this.list2.setDragEnabled(dnd);
        this.tree1.setDragEnabled(dnd);
        this.tree2.setDragEnabled(dnd);
        this.table1.setDragEnabled(dnd);
        DropMode dropMode = dnd ? DropMode.ON_OR_INSERT : DropMode.USE_SELECTION;
        this.list1.setDropMode(dropMode);
        this.tree1.setDropMode(dropMode);
        this.table1.setDropMode(dropMode);
        if (dnd) {
            this.list1.putClientProperty("FlatLaf.oldTransferHandler", this.list1.getTransferHandler());
            this.list1.setTransferHandler(new DummyTransferHandler());
            this.tree1.putClientProperty("FlatLaf.oldTransferHandler", this.tree1.getTransferHandler());
            this.tree1.setTransferHandler(new DummyTransferHandler());
            this.table1.putClientProperty("FlatLaf.oldTransferHandler", this.table1.getTransferHandler());
            this.table1.setTransferHandler(new DummyTransferHandler());
            return;
        }
        this.list1.setTransferHandler((TransferHandler) this.list1.getClientProperty("FlatLaf.oldTransferHandler"));
        this.tree1.setTransferHandler((TransferHandler) this.tree1.getClientProperty("FlatLaf.oldTransferHandler"));
        this.table1.setTransferHandler((TransferHandler) this.table1.getClientProperty("FlatLaf.oldTransferHandler"));
    }

    private void rowSelectionChanged() {
        this.table1.setRowSelectionAllowed(this.rowSelectionCheckBox.isSelected());
    }

    private void columnSelectionChanged() {
        this.table1.setColumnSelectionAllowed(this.columnSelectionCheckBox.isSelected());
    }

    private void showHorizontalLinesChanged() {
        this.table1.setShowHorizontalLines(this.showHorizontalLinesCheckBox.isSelected());
    }

    private void showVerticalLinesChanged() {
        this.table1.setShowVerticalLines(this.showVerticalLinesCheckBox.isSelected());
    }

    private void intercellSpacingChanged() {
        this.table1.setIntercellSpacing(this.intercellSpacingCheckBox.isSelected() ? new Dimension(1, 1) : new Dimension());
    }

    private void redGridColorChanged() {
        this.table1.setGridColor(this.redGridColorCheckBox.isSelected() ? Color.red : UIManager.getColor("Table.gridColor"));
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateUI() {
        /*
            r1 = this;
            void r0 = com.formdev.flatlaf.demo.DataComponentsPanel.super.updateUI()
            java.awt.EventQueue.invokeLater(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.demo.DataComponentsPanel.updateUI():void");
    }

    private /* synthetic */ void lambda$updateUI$0() {
        showHorizontalLinesChanged();
        showVerticalLinesChanged();
        intercellSpacingChanged();
    }

    /*  JADX ERROR: MOVE_RESULT instruction can be used only in fallback mode
        jadx.core.utils.exceptions.CodegenException: MOVE_RESULT instruction can be used only in fallback mode
        	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:604)
        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:542)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:230)
        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:119)
        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:103)
        	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:806)
        	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:746)
        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:367)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:249)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:217)
        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:110)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:56)
        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:93)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:59)
        	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:244)
        	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:237)
        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:342)
        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:295)
        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:264)
        	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:184)
        	at java.util.ArrayList.forEach(ArrayList.java:1257)
        	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:390)
        	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
        */
    private void initComponents() {
        /*
        // Method dump skipped, instructions count: 1822
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.demo.DataComponentsPanel.initComponents():void");
    }

    private /* synthetic */ void lambda$initComponents$1(ActionEvent e) {
        showHorizontalLinesChanged();
    }

    private /* synthetic */ void lambda$initComponents$2(ActionEvent e) {
        showVerticalLinesChanged();
    }

    private /* synthetic */ void lambda$initComponents$3(ActionEvent e) {
        intercellSpacingChanged();
    }

    private /* synthetic */ void lambda$initComponents$4(ActionEvent e) {
        redGridColorChanged();
    }

    private /* synthetic */ void lambda$initComponents$5(ActionEvent e) {
        rowSelectionChanged();
    }

    private /* synthetic */ void lambda$initComponents$6(ActionEvent e) {
        columnSelectionChanged();
    }

    private /* synthetic */ void lambda$initComponents$7(ActionEvent e) {
        dndChanged();
    }

    /* access modifiers changed from: private */
    public static class DummyTransferHandler extends TransferHandler {
        private DummyTransferHandler() {
        }

        /* access modifiers changed from: protected */
        public Transferable createTransferable(JComponent c) {
            if ((c instanceof JList) && ((JList) c).isSelectionEmpty()) {
                return null;
            }
            if ((c instanceof JTree) && ((JTree) c).isSelectionEmpty()) {
                return null;
            }
            if (!(c instanceof JTable) || !((JTable) c).getSelectionModel().isSelectionEmpty()) {
                return new StringSelection("dummy");
            }
            return null;
        }

        public int getSourceActions(JComponent c) {
            return 1;
        }

        public boolean canImport(TransferHandler.TransferSupport support) {
            return support.isDataFlavorSupported(DataFlavor.stringFlavor);
        }

        public boolean importData(TransferHandler.TransferSupport support) {
            String.valueOf(support.getDropLocation());
            SwingUtilities.invokeLater(
        }
