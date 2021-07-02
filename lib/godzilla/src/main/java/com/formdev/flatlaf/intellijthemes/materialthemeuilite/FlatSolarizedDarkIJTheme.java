package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatSolarizedDarkIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Solarized Dark (Material)";

    public static boolean install() {
        try {
            return install(new FlatSolarizedDarkIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatSolarizedDarkIJTheme.class);
    }

    public FlatSolarizedDarkIJTheme() {
        super(Utils.loadTheme("Solarized Dark.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
