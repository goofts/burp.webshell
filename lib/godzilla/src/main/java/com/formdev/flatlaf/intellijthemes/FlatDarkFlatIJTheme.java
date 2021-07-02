package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatDarkFlatIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Dark Flat";

    public static boolean install() {
        try {
            return install(new FlatDarkFlatIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatDarkFlatIJTheme.class);
    }

    public FlatDarkFlatIJTheme() {
        super(Utils.loadTheme("DarkFlatTheme.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
