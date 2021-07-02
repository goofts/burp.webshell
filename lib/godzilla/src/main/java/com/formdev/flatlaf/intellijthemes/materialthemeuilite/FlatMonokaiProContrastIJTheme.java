package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatMonokaiProContrastIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Monokai Pro Contrast (Material)";

    public static boolean install() {
        try {
            return install(new FlatMonokaiProContrastIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatMonokaiProContrastIJTheme.class);
    }

    public FlatMonokaiProContrastIJTheme() {
        super(Utils.loadTheme("Monokai Pro Contrast.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
