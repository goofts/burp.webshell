package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatGruvboxDarkHardIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Gruvbox Dark Hard";

    public static boolean install() {
        try {
            return install(new FlatGruvboxDarkHardIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatGruvboxDarkHardIJTheme.class);
    }

    public FlatGruvboxDarkHardIJTheme() {
        super(Utils.loadTheme("gruvbox_dark_hard.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
