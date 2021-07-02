package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatVuesionIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Vuesion";

    public static boolean install() {
        try {
            return install(new FlatVuesionIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatVuesionIJTheme.class);
    }

    public FlatVuesionIJTheme() {
        super(Utils.loadTheme("vuesion_theme.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
