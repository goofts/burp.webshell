package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatHighContrastIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "High contrast";

    public static boolean install() {
        try {
            return install(new FlatHighContrastIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatHighContrastIJTheme.class);
    }

    public FlatHighContrastIJTheme() {
        super(Utils.loadTheme("HighContrast.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
