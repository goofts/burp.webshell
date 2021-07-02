package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatDraculaContrastIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Dracula Contrast (Material)";

    public static boolean install() {
        try {
            return install(new FlatDraculaContrastIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatDraculaContrastIJTheme.class);
    }

    public FlatDraculaContrastIJTheme() {
        super(Utils.loadTheme("Dracula Contrast.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
