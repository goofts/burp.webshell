package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatDarkPurpleIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Dark purple";

    public static boolean install() {
        try {
            return install(new FlatDarkPurpleIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatDarkPurpleIJTheme.class);
    }

    public FlatDarkPurpleIJTheme() {
        super(Utils.loadTheme("DarkPurple.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
