package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatMaterialDeepOceanIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Material Deep Ocean (Material)";

    public static boolean install() {
        try {
            return install(new FlatMaterialDeepOceanIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatMaterialDeepOceanIJTheme.class);
    }

    public FlatMaterialDeepOceanIJTheme() {
        super(Utils.loadTheme("Material Deep Ocean.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
