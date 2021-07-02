package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatAtomOneLightIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Atom One Light (Material)";

    public static boolean install() {
        try {
            return install(new FlatAtomOneLightIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatAtomOneLightIJTheme.class);
    }

    public FlatAtomOneLightIJTheme() {
        super(Utils.loadTheme("Atom One Light.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
