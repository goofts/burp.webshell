package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatCyanLightIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Cyan light";

    public static boolean install() {
        try {
            return install(new FlatCyanLightIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatCyanLightIJTheme.class);
    }

    public FlatCyanLightIJTheme() {
        super(Utils.loadTheme("Cyan.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
