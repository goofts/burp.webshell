package com.formdev.flatlaf.demo;

import java.awt.Dimension;
import javax.swing.JDialog;
import javax.swing.JFrame;

public class FlatLafDemo {
    static final String KEY_TAB = "tab";
    static final String PREFS_ROOT_PATH = "/flatlaf-demo";
    static boolean screenshotsMode = Boolean.parseBoolean(System.getProperty("flatlaf.demo.screenshotsMode"));

    /*  JADX ERROR: JadxRuntimeException in pass: BlockSplitter
        jadx.core.utils.exceptions.JadxRuntimeException: Missing block: 42
        	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.getBlock(BlockSplitter.java:307)
        	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.setupConnections(BlockSplitter.java:236)
        	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.splitBasicBlocks(BlockSplitter.java:129)
        	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.visit(BlockSplitter.java:52)
        */
    public static void main(java.lang.String[] r2) {
        /*
            boolean r0 = com.formdev.flatlaf.util.SystemInfo.isMacOS
            if (r0 == 0) goto L_0x0013
            java.lang.String r0 = "apple.laf.useScreenMenuBar"
            java.lang.String r0 = java.lang.System.getProperty(r0)
            if (r0 != 0) goto L_0x0013
            java.lang.String r0 = "apple.laf.useScreenMenuBar"
            java.lang.String r1 = "true"
            java.lang.System.setProperty(r0, r1)
        L_0x0013:
            boolean r0 = com.formdev.flatlaf.demo.FlatLafDemo.screenshotsMode
            if (r0 == 0) goto L_0x002a
            boolean r0 = com.formdev.flatlaf.util.SystemInfo.isJava_9_orLater
            if (r0 != 0) goto L_0x002a
            java.lang.String r0 = "flatlaf.uiScale"
            java.lang.String r0 = java.lang.System.getProperty(r0)
            if (r0 != 0) goto L_0x002a
            java.lang.String r0 = "flatlaf.uiScale"
            java.lang.String r1 = "2x"
            java.lang.String r0 = java.lang.System.setProperty(r0, r1)
        L_?:
            javax.swing.SwingUtilities.invokeLater(r0)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.demo.FlatLafDemo.main(java.lang.String[]):void");
    }

    private static /* synthetic */ void lambda$main$0(String[] args) {
        DemoPrefs.init(PREFS_ROOT_PATH);
        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);
        DemoPrefs.initLaf(args);
        DemoFrame frame = new DemoFrame();
        if (screenshotsMode) {
            frame.setPreferredSize(new Dimension(1660, 840));
        }
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
