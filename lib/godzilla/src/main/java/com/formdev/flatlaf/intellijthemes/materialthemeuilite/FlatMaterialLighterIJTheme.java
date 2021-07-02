package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatMaterialLighterIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Material Lighter (Material)";

    public static boolean install() {
        try {
            return install(new FlatMaterialLighterIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatMaterialLighterIJTheme.class);
    }

    public FlatMaterialLighterIJTheme() {
        super(Utils.loadTheme("Material Lighter.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
