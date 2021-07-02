package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatMoonlightContrastIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Moonlight Contrast (Material)";

    public static boolean install() {
        try {
            return install(new FlatMoonlightContrastIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatMoonlightContrastIJTheme.class);
    }

    public FlatMoonlightContrastIJTheme() {
        super(Utils.loadTheme("Moonlight Contrast.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
