package com.formdev.flatlaf.demo;

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.demo.HintManager;
import com.formdev.flatlaf.demo.intellijthemes.IJThemesPanel;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.extras.FlatUIDefaultsInspector;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.prefs.Preferences;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.text.StyleContext;

class DemoFrame extends JFrame {
    private JCheckBoxMenuItem alwaysShowMnemonicsMenuItem;
    private JCheckBoxMenuItem animatedLafChangeMenuItem;
    private final String[] availableFontFamilyNames;
    private ControlBar controlBar;
    private JMenu fontMenu;
    private int initialFontMenuItemCount;
    private JCheckBoxMenuItem menuBarEmbeddedCheckBoxMenuItem;
    private JMenu optionsMenu;
    private JTabbedPane tabbedPane;
    IJThemesPanel themesPanel;
    private JCheckBoxMenuItem underlineMenuSelectionMenuItem;
    private JCheckBoxMenuItem windowDecorationsCheckBoxMenuItem;

    DemoFrame() {
        /*
            r4 = this;
            r4.<init>()
            r1 = -1
            r4.initialFontMenuItemCount = r1
            java.util.prefs.Preferences r1 = com.formdev.flatlaf.demo.DemoPrefs.getState()
            java.lang.String r2 = "tab"
            r3 = 0
            int r0 = r1.getInt(r2, r3)
            java.awt.GraphicsEnvironment r1 = java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment()
            java.lang.String[] r1 = r1.getAvailableFontFamilyNames()
            java.lang.Object r1 = r1.clone()
            java.lang.String[] r1 = (java.lang.String[]) r1
            r4.availableFontFamilyNames = r1
            java.lang.String[] r1 = r4.availableFontFamilyNames
            java.util.Arrays.sort(r1)
            r4.initComponents()
            r4.updateFontMenuItems()
            com.formdev.flatlaf.demo.ControlBar r1 = r4.controlBar
            javax.swing.JTabbedPane r2 = r4.tabbedPane
            r1.initialize(r4, r2)
            java.lang.String r1 = "/com/formdev/flatlaf/demo/FlatLaf.svg"
            java.util.List r1 = com.formdev.flatlaf.extras.FlatSVGUtils.createWindowIconImages(r1)
            r4.setIconImages(r1)
            if (r0 < 0) goto L_0x0053
            javax.swing.JTabbedPane r1 = r4.tabbedPane
            int r1 = r1.getTabCount()
            if (r0 >= r1) goto L_0x0053
            javax.swing.JTabbedPane r1 = r4.tabbedPane
            int r1 = r1.getSelectedIndex()
            if (r0 == r1) goto L_0x0053
            javax.swing.JTabbedPane r1 = r4.tabbedPane
            void r1 = r1.setSelectedIndex(r0)
        L_?:
            javax.swing.SwingUtilities.invokeLater(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.demo.DemoFrame.<init>():void");
    }

    public void dispose() {
        DemoFrame.super.dispose();
        FlatUIDefaultsInspector.hide();
    }

    /* access modifiers changed from: private */
    /* renamed from: showHints */
    public void lambda$new$0() {
        HintManager.showHint(new HintManager.Hint("Use 'Themes' list to try out various themes.", this.themesPanel, 2, "hint.themesPanel", new HintManager.Hint("Use 'Options' menu to try out various FlatLaf options.", this.optionsMenu, 3, "hint.optionsMenu", new HintManager.Hint("Use 'Font' menu to increase/decrease font size or try different fonts.", this.fontMenu, 3, "hint.fontMenu", null))));
    }

    private void clearHints() {
        HintManager.hideAllHints();
        Preferences state = DemoPrefs.getState();
        state.remove("hint.fontMenu");
        state.remove("hint.optionsMenu");
        state.remove("hint.themesPanel");
    }

    private void showUIDefaultsInspector() {
        FlatUIDefaultsInspector.show();
    }

    private void newActionPerformed() {
        new NewDialog(this).setVisible(true);
    }

    private void openActionPerformed() {
        new JFileChooser().showOpenDialog(this);
    }

    private void saveAsActionPerformed() {
        new JFileChooser().showSaveDialog(this);
    }

    private void exitActionPerformed() {
        dispose();
    }

    private void aboutActionPerformed() {
        JOptionPane.showMessageDialog(this, "FlatLaf Demo", "About", -1);
    }

    private void selectedTabChanged() {
        DemoPrefs.getState().putInt(FlatClientProperties.BUTTON_TYPE_TAB, this.tabbedPane.getSelectedIndex());
    }

    /* access modifiers changed from: private */
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
    /* renamed from: menuItemActionPerformed */
    public void lambda$initComponents$9(java.awt.event.ActionEvent r2) {
        /*
            r1 = this;
            r0 = move-result
            javax.swing.SwingUtilities.invokeLater(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.demo.DemoFrame.lambda$initComponents$9(java.awt.event.ActionEvent):void");
    }

    private /* synthetic */ void lambda$menuItemActionPerformed$1(ActionEvent e) {
        JOptionPane.showMessageDialog(this, e.getActionCommand(), "Menu Item", -1);
    }

    private void windowDecorationsChanged() {
        boolean windowDecorations = this.windowDecorationsCheckBoxMenuItem.isSelected();
        dispose();
        setUndecorated(windowDecorations);
        getRootPane().setWindowDecorationStyle(windowDecorations ? 1 : 0);
        this.menuBarEmbeddedCheckBoxMenuItem.setEnabled(windowDecorations);
        setVisible(true);
        JFrame.setDefaultLookAndFeelDecorated(windowDecorations);
        JDialog.setDefaultLookAndFeelDecorated(windowDecorations);
    }

    private void menuBarEmbeddedChanged() {
        getRootPane().putClientProperty(FlatClientProperties.MENU_BAR_EMBEDDED, this.menuBarEmbeddedCheckBoxMenuItem.isSelected() ? null : false);
    }

    private void underlineMenuSelection() {
        UIManager.put("MenuItem.selectionType", this.underlineMenuSelectionMenuItem.isSelected() ? "underline" : null);
    }

    private void alwaysShowMnemonics() {
        UIManager.put("Component.hideMnemonics", Boolean.valueOf(!this.alwaysShowMnemonicsMenuItem.isSelected()));
        repaint();
    }

    private void animatedLafChangeChanged() {
        System.setProperty("flatlaf.animatedLafChange", String.valueOf(this.animatedLafChangeMenuItem.isSelected()));
    }

    private void showHintsChanged() {
        clearHints();
        lambda$new$0();
    }

    private void fontFamilyChanged(ActionEvent e) {
        String fontFamily = e.getActionCommand();
        FlatAnimatedLafChange.showSnapshot();
        Font font = UIManager.getFont("defaultFont");
        UIManager.put("defaultFont", StyleContext.getDefaultStyleContext().getFont(fontFamily, font.getStyle(), font.getSize()));
        FlatLaf.updateUI();
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    private void fontSizeChanged(ActionEvent e) {
        UIManager.put("defaultFont", UIManager.getFont("defaultFont").deriveFont((float) Integer.parseInt(e.getActionCommand())));
        FlatLaf.updateUI();
    }

    private void restoreFont() {
        UIManager.put("defaultFont", (Object) null);
        updateFontMenuItems();
        FlatLaf.updateUI();
    }

    private void incrFont() {
        Font font = UIManager.getFont("defaultFont");
        UIManager.put("defaultFont", font.deriveFont((float) (font.getSize() + 1)));
        updateFontMenuItems();
        FlatLaf.updateUI();
    }

    private void decrFont() {
        Font font = UIManager.getFont("defaultFont");
        UIManager.put("defaultFont", font.deriveFont((float) Math.max(font.getSize() - 1, 10)));
        updateFontMenuItems();
        FlatLaf.updateUI();
    }

    /* JADX WARN: Type inference failed for: r13v28, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r13v33, types: [java.awt.event.ActionListener, void] */
    /* access modifiers changed from: package-private */
    /* JADX WARNING: Unknown variable types count: 2 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateFontMenuItems() {
        /*
        // Method dump skipped, instructions count: 373
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.demo.DemoFrame.updateFontMenuItems():void");
    }

    /* JADX WARN: Type inference failed for: r60v19, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v24, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v29, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v34, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v39, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v46, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v51, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v68, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v74, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v83, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v86, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v89, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v92, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v95, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v102, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v105, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v108, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v113, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v118, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v123, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v146, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v149, types: [java.awt.event.ActionListener, void] */
    /* JADX WARN: Type inference failed for: r60v156, types: [java.awt.event.ActionListener, void] */
    /* JADX WARNING: Unknown variable types count: 23 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void initComponents() {
        /*
        // Method dump skipped, instructions count: 2738
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.demo.DemoFrame.initComponents():void");
    }

    private /* synthetic */ void lambda$initComponents$2(ActionEvent e) {
        newActionPerformed();
    }

    private /* synthetic */ void lambda$initComponents$3(ActionEvent e) {
        openActionPerformed();
    }

    private /* synthetic */ void lambda$initComponents$4(ActionEvent e) {
        saveAsActionPerformed();
    }

    private /* synthetic */ void lambda$initComponents$6(ActionEvent e) {
        exitActionPerformed();
    }

    private /* synthetic */ void lambda$initComponents$19(ActionEvent e) {
        restoreFont();
    }

    private /* synthetic */ void lambda$initComponents$20(ActionEvent e) {
        incrFont();
    }

    private /* synthetic */ void lambda$initComponents$21(ActionEvent e) {
        decrFont();
    }

    private /* synthetic */ void lambda$initComponents$22(ActionEvent e) {
        windowDecorationsChanged();
    }

    private /* synthetic */ void lambda$initComponents$23(ActionEvent e) {
        menuBarEmbeddedChanged();
    }

    private /* synthetic */ void lambda$initComponents$24(ActionEvent e) {
        underlineMenuSelection();
    }

    private /* synthetic */ void lambda$initComponents$25(ActionEvent e) {
        alwaysShowMnemonics();
    }

    private /* synthetic */ void lambda$initComponents$26(ActionEvent e) {
        animatedLafChangeChanged();
    }

    private /* synthetic */ void lambda$initComponents$27(ActionEvent e) {
        showHintsChanged();
    }

    private /* synthetic */ void lambda$initComponents$28(ActionEvent e) {
        showUIDefaultsInspector();
    }

    private /* synthetic */ void lambda$initComponents$29(ActionEvent e) {
        aboutActionPerformed();
    }

    private /* synthetic */ void lambda$initComponents$30(ChangeEvent e) {
        selectedTabChanged();
    }
}
