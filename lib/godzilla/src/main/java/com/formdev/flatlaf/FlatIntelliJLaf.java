package com.formdev.flatlaf;

public class FlatIntelliJLaf extends FlatLightLaf {
    public static final String NAME = "FlatLaf IntelliJ";

    public static boolean install() {
        return install(new FlatIntelliJLaf());
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatIntelliJLaf.class);
    }

    @Override // com.formdev.flatlaf.FlatLightLaf
    public String getName() {
        return NAME;
    }

    @Override // com.formdev.flatlaf.FlatLightLaf
    public String getDescription() {
        return "FlatLaf IntelliJ Look and Feel";
    }
}
