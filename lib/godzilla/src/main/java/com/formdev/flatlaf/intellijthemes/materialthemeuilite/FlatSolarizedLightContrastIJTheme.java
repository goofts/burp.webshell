package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatSolarizedLightContrastIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Solarized Light Contrast (Material)";

    public static boolean install() {
        try {
            return install(new FlatSolarizedLightContrastIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatSolarizedLightContrastIJTheme.class);
    }

    public FlatSolarizedLightContrastIJTheme() {
        super(Utils.loadTheme("Solarized Light Contrast.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
