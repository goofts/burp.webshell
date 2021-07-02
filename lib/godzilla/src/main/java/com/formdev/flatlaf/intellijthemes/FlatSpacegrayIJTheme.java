package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatSpacegrayIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Spacegray";

    public static boolean install() {
        try {
            return install(new FlatSpacegrayIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatSpacegrayIJTheme.class);
    }

    public FlatSpacegrayIJTheme() {
        super(Utils.loadTheme("Spacegray.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
