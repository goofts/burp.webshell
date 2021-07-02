package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatArcOrangeIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Arc - Orange";

    public static boolean install() {
        try {
            return install(new FlatArcOrangeIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatArcOrangeIJTheme.class);
    }

    public FlatArcOrangeIJTheme() {
        super(Utils.loadTheme("arc-theme-orange.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
