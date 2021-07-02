package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatMonokaiProIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Monokai Pro (Material)";

    public static boolean install() {
        try {
            return install(new FlatMonokaiProIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatMonokaiProIJTheme.class);
    }

    public FlatMonokaiProIJTheme() {
        super(Utils.loadTheme("Monokai Pro.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
