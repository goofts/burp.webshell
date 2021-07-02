package com.formdev.flatlaf;

public class FlatDarkLaf extends FlatLaf {
    public static final String NAME = "FlatLaf Dark";

    public static boolean install() {
        return install(new FlatDarkLaf());
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatDarkLaf.class);
    }

    public String getName() {
        return NAME;
    }

    public String getDescription() {
        return "FlatLaf Dark Look and Feel";
    }

    @Override // com.formdev.flatlaf.FlatLaf
    public boolean isDark() {
        return true;
    }
}
