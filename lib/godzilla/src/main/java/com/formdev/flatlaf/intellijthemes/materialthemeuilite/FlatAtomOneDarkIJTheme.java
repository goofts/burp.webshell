package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatAtomOneDarkIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Atom One Dark (Material)";

    public static boolean install() {
        try {
            return install(new FlatAtomOneDarkIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatAtomOneDarkIJTheme.class);
    }

    public FlatAtomOneDarkIJTheme() {
        super(Utils.loadTheme("Atom One Dark.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
