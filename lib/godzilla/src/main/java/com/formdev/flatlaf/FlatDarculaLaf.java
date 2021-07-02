package com.formdev.flatlaf;

public class FlatDarculaLaf extends FlatDarkLaf {
    public static final String NAME = "FlatLaf Darcula";

    public static boolean install() {
        return install(new FlatDarculaLaf());
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatDarculaLaf.class);
    }

    @Override // com.formdev.flatlaf.FlatDarkLaf
    public String getName() {
        return NAME;
    }

    @Override // com.formdev.flatlaf.FlatDarkLaf
    public String getDescription() {
        return "FlatLaf Darcula Look and Feel";
    }
}
