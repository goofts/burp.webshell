package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatArcDarkIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Arc Dark (Material)";

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
        super(Utils.loadTheme("Arc Dark.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
