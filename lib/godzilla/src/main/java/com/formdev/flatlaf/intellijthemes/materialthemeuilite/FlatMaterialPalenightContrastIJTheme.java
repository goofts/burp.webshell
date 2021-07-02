package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatMaterialPalenightContrastIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Material Palenight Contrast (Material)";

    public static boolean install() {
        try {
            return install(new FlatMaterialPalenightContrastIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatMaterialPalenightContrastIJTheme.class);
    }

    public FlatMaterialPalenightContrastIJTheme() {
        super(Utils.loadTheme("Material Palenight Contrast.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
