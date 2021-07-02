package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.IntelliJTheme;
import com.formdev.flatlaf.demo.intellijthemes.IJThemesPanel;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

class Utils {
    static final Logger LOG = Logger.getLogger(FlatLaf.class.getName());

    Utils() {
    }

    static IntelliJTheme loadTheme(String name) {
        try {
            return new IntelliJTheme(Utils.class.getResourceAsStream(IJThemesPanel.THEMES_PACKAGE + name));
        } catch (IOException ex) {
            String msg = "FlatLaf: Failed to load IntelliJ theme '" + name + "'";
            LOG.log(Level.SEVERE, msg, (Throwable) ex);
            throw new RuntimeException(msg, ex);
        }
    }
}
