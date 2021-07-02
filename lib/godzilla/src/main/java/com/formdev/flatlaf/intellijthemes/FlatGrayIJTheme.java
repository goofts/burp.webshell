package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatGrayIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Gray";

    public static boolean install() {
        try {
            return install(new FlatGrayIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatGrayIJTheme.class);
    }

    public FlatGrayIJTheme() {
        super(Utils.loadTheme("Gray.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
