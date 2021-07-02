package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatArcIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Arc";

    public static boolean install() {
        try {
            return install(new FlatArcIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatArcIJTheme.class);
    }

    public FlatArcIJTheme() {
        super(Utils.loadTheme("arc-theme.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
