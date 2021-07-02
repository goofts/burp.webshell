package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatMaterialLighterContrastIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Material Lighter Contrast (Material)";

    public static boolean install() {
        try {
            return install(new FlatMaterialLighterContrastIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatMaterialLighterContrastIJTheme.class);
    }

    public FlatMaterialLighterContrastIJTheme() {
        super(Utils.loadTheme("Material Lighter Contrast.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
