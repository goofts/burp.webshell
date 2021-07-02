package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatArcDarkIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Arc Dark";

    public static boolean install() {
        try {
            return install(new FlatArcDarkIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatArcDarkIJTheme.class);
    }

    public FlatArcDarkIJTheme() {
        super(Utils.loadTheme("arc_theme_dark.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
