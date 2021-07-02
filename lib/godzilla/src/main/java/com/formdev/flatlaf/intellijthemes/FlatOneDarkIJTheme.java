package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatOneDarkIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "One Dark";

    public static boolean install() {
        try {
            return install(new FlatOneDarkIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatOneDarkIJTheme.class);
    }

    public FlatOneDarkIJTheme() {
        super(Utils.loadTheme("one_dark.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
