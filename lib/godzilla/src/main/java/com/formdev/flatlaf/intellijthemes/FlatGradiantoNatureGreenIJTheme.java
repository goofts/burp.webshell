package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatGradiantoNatureGreenIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Gradianto Nature Green";

    public static boolean install() {
        try {
            return install(new FlatGradiantoNatureGreenIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatGradiantoNatureGreenIJTheme.class);
    }

    public FlatGradiantoNatureGreenIJTheme() {
        super(Utils.loadTheme("Gradianto_Nature_Green.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
