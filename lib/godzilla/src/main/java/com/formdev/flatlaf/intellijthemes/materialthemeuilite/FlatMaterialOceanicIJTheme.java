package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatMaterialOceanicIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Material Oceanic (Material)";

    public static boolean install() {
        try {
            return install(new FlatMaterialOceanicIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatMaterialOceanicIJTheme.class);
    }

    public FlatMaterialOceanicIJTheme() {
        super(Utils.loadTheme("Material Oceanic.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
