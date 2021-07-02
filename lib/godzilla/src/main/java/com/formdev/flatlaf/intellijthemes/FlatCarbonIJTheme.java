package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatCarbonIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Carbon";

    public static boolean install() {
        try {
            return install(new FlatCarbonIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatCarbonIJTheme.class);
    }

    public FlatCarbonIJTheme() {
        super(Utils.loadTheme("Carbon.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
