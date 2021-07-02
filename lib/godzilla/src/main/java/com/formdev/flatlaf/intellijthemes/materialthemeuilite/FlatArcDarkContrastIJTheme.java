package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatArcDarkContrastIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Arc Dark Contrast (Material)";

    public static boolean install() {
        try {
            return install(new FlatArcDarkContrastIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatArcDarkContrastIJTheme.class);
    }

    public FlatArcDarkContrastIJTheme() {
        super(Utils.loadTheme("Arc Dark Contrast.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
