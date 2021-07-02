package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatMaterialDarkerContrastIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Material Darker Contrast (Material)";

    public static boolean install() {
        try {
            return install(new FlatMaterialDarkerContrastIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatMaterialDarkerContrastIJTheme.class);
    }

    public FlatMaterialDarkerContrastIJTheme() {
        super(Utils.loadTheme("Material Darker Contrast.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
