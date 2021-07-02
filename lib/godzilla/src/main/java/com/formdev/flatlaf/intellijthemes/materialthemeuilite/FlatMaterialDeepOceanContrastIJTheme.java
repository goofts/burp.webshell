package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatMaterialDeepOceanContrastIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Material Deep Ocean Contrast (Material)";

    public static boolean install() {
        try {
            return install(new FlatMaterialDeepOceanContrastIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatMaterialDeepOceanContrastIJTheme.class);
    }

    public FlatMaterialDeepOceanContrastIJTheme() {
        super(Utils.loadTheme("Material Deep Ocean Contrast.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
