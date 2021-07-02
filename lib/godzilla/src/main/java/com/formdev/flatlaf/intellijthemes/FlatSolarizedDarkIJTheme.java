package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatSolarizedDarkIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Solarized Dark";

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
        super(Utils.loadTheme("SolarizedDark.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
