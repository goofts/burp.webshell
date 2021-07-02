package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatNordIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Nord";

    public static boolean install() {
        try {
            return install(new FlatNordIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatNordIJTheme.class);
    }

    public FlatNordIJTheme() {
        super(Utils.loadTheme("nord.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
