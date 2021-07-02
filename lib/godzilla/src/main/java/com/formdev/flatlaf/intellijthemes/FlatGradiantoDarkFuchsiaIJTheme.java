package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatGradiantoDarkFuchsiaIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Gradianto Dark Fuchsia";

    public static boolean install() {
        try {
            return install(new FlatGradiantoDarkFuchsiaIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatGradiantoDarkFuchsiaIJTheme.class);
    }

    public FlatGradiantoDarkFuchsiaIJTheme() {
        super(Utils.loadTheme("Gradianto_dark_fuchsia.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
