package com.formdev.flatlaf.demo;

import java.beans.PropertyChangeEvent;
import java.util.prefs.Preferences;
import javax.swing.UIManager;

public class DemoPrefs {
    public static final String FILE_PREFIX = "file:";
    public static final String KEY_LAF = "laf";
    public static final String KEY_LAF_THEME = "lafTheme";
    public static final String RESOURCE_PREFIX = "res:";
    public static final String THEME_UI_KEY = "__FlatLaf.demo.theme";
    private static Preferences state;

    public static Preferences getState() {
        return state;
    }

    public static void init(String rootPath) {
        state = Preferences.userRoot().node(rootPath);
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockSplitter
        jadx.core.utils.exceptions.JadxRuntimeException: Missing block: 9
        	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.getBlock(BlockSplitter.java:307)
        	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.setupConnections(BlockSplitter.java:236)
        	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.splitBasicBlocks(BlockSplitter.java:129)
        	at jadx.core.dex.visitors.blocksmaker.BlockSplitter.visit(BlockSplitter.java:52)
        */
    public static void initLaf(java.lang.String[] r8) {
        /*
        // Method dump skipped, instructions count: 247
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.demo.DemoPrefs.initLaf(java.lang.String[]):void");
    }

    private static /* synthetic */ void lambda$initLaf$0(PropertyChangeEvent e) {
        if ("lookAndFeel".equals(e.getPropertyName())) {
            state.put(KEY_LAF, UIManager.getLookAndFeel().getClass().getName());
        }
    }
}
