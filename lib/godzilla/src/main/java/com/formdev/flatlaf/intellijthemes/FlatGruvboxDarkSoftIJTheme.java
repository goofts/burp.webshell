package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatGruvboxDarkSoftIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Gruvbox Dark Soft";

    public static boolean install() {
        try {
            return install(new FlatGruvboxDarkSoftIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatGruvboxDarkSoftIJTheme.class);
    }

    public FlatGruvboxDarkSoftIJTheme() {
        super(Utils.loadTheme("gruvbox_dark_soft.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
