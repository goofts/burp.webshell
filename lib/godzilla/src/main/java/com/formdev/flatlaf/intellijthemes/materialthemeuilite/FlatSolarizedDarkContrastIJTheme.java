package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatSolarizedDarkContrastIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Solarized Dark Contrast (Material)";

    public static boolean install() {
        try {
            return install(new FlatSolarizedDarkContrastIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatSolarizedDarkContrastIJTheme.class);
    }

    public FlatSolarizedDarkContrastIJTheme() {
        super(Utils.loadTheme("Solarized Dark Contrast.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
