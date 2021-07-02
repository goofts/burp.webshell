package com.formdev.flatlaf;

public class FlatLightLaf extends FlatLaf {
    public static final String NAME = "FlatLaf Light";

    public static boolean install() {
        return install(new FlatLightLaf());
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatLightLaf.class);
    }

    public String getName() {
        return NAME;
    }

    public String getDescription() {
        return "FlatLaf Light Look and Feel";
    }

    @Override // com.formdev.flatlaf.FlatLaf
    public boolean isDark() {
        return false;
    }
}
