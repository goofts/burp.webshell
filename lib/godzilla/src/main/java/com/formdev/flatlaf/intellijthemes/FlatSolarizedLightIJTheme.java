package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatSolarizedLightIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Solarized Light";

    public static boolean install() {
        try {
            return install(new FlatSolarizedLightIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatSolarizedLightIJTheme.class);
    }

    public FlatSolarizedLightIJTheme() {
        super(Utils.loadTheme("SolarizedLight.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
