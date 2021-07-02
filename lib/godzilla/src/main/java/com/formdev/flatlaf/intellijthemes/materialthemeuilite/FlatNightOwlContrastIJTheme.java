package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatNightOwlContrastIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Night Owl Contrast (Material)";

    public static boolean install() {
        try {
            return install(new FlatNightOwlContrastIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatNightOwlContrastIJTheme.class);
    }

    public FlatNightOwlContrastIJTheme() {
        super(Utils.loadTheme("Night Owl Contrast.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
