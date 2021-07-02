package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatLightFlatIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Light Flat";

    public static boolean install() {
        try {
            return install(new FlatLightFlatIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatLightFlatIJTheme.class);
    }

    public FlatLightFlatIJTheme() {
        super(Utils.loadTheme("LightFlatTheme.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
