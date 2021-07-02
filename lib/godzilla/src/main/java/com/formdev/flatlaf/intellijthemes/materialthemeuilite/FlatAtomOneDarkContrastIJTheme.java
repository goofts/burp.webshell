package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatAtomOneDarkContrastIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Atom One Dark Contrast (Material)";

    public static boolean install() {
        try {
            return install(new FlatAtomOneDarkContrastIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatAtomOneDarkContrastIJTheme.class);
    }

    public FlatAtomOneDarkContrastIJTheme() {
        super(Utils.loadTheme("Atom One Dark Contrast.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
