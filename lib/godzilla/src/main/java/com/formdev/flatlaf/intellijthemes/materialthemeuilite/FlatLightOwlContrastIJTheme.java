package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatLightOwlContrastIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Light Owl Contrast (Material)";

    public static boolean install() {
        try {
            return install(new FlatLightOwlContrastIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatLightOwlContrastIJTheme.class);
    }

    public FlatLightOwlContrastIJTheme() {
        super(Utils.loadTheme("Light Owl Contrast.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
