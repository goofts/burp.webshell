package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatAtomOneLightContrastIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Atom One Light Contrast (Material)";

    public static boolean install() {
        try {
            return install(new FlatAtomOneLightContrastIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatAtomOneLightContrastIJTheme.class);
    }

    public FlatAtomOneLightContrastIJTheme() {
        super(Utils.loadTheme("Atom One Light Contrast.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
