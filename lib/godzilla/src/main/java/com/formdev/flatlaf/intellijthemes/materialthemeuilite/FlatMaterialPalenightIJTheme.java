package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatMaterialPalenightIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Material Palenight (Material)";

    public static boolean install() {
        try {
            return install(new FlatMaterialPalenightIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatMaterialPalenightIJTheme.class);
    }

    public FlatMaterialPalenightIJTheme() {
        super(Utils.loadTheme("Material Palenight.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
