package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatGradiantoMidnightBlueIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Gradianto Midnight Blue";

    public static boolean install() {
        try {
            return install(new FlatGradiantoMidnightBlueIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatGradiantoMidnightBlueIJTheme.class);
    }

    public FlatGradiantoMidnightBlueIJTheme() {
        super(Utils.loadTheme("Gradianto_midnight_blue.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
