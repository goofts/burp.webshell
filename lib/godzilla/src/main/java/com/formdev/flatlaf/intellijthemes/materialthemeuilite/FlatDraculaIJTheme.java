package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatDraculaIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Dracula (Material)";

    public static boolean install() {
        try {
            return install(new FlatDraculaIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatDraculaIJTheme.class);
    }

    public FlatDraculaIJTheme() {
        super(Utils.loadTheme("Dracula.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
