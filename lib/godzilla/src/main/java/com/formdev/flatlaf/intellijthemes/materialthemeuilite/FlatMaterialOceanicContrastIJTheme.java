package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatMaterialOceanicContrastIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Material Oceanic Contrast (Material)";

    public static boolean install() {
        try {
            return install(new FlatMaterialOceanicContrastIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatMaterialOceanicContrastIJTheme.class);
    }

    public FlatMaterialOceanicContrastIJTheme() {
        super(Utils.loadTheme("Material Oceanic Contrast.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
