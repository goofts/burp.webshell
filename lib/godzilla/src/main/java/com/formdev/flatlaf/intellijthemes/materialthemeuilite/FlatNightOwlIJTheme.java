package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatNightOwlIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Night Owl (Material)";

    public static boolean install() {
        try {
            return install(new FlatNightOwlIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatNightOwlIJTheme.class);
    }

    public FlatNightOwlIJTheme() {
        super(Utils.loadTheme("Night Owl.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
