package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatHiberbeeDarkIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Hiberbee Dark";

    public static boolean install() {
        try {
            return install(new FlatHiberbeeDarkIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatHiberbeeDarkIJTheme.class);
    }

    public FlatHiberbeeDarkIJTheme() {
        super(Utils.loadTheme("HiberbeeDark.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
