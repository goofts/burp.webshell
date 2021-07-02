package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatMoonlightIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Moonlight (Material)";

    public static boolean install() {
        try {
            return install(new FlatMoonlightIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatMoonlightIJTheme.class);
    }

    public FlatMoonlightIJTheme() {
        super(Utils.loadTheme("Moonlight.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
