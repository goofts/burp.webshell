package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatLightOwlIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Light Owl (Material)";

    public static boolean install() {
        try {
            return install(new FlatLightOwlIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatLightOwlIJTheme.class);
    }

    public FlatLightOwlIJTheme() {
        super(Utils.loadTheme("Light Owl.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
