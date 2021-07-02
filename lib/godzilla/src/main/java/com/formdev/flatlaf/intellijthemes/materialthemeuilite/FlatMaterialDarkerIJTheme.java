package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatMaterialDarkerIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Material Darker (Material)";

    public static boolean install() {
        try {
            return install(new FlatMaterialDarkerIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatMaterialDarkerIJTheme.class);
    }

    public FlatMaterialDarkerIJTheme() {
        super(Utils.loadTheme("Material Darker.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
