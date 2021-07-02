package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatMaterialDesignDarkIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Material Design Dark";

    public static boolean install() {
        try {
            return install(new FlatMaterialDesignDarkIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatMaterialDesignDarkIJTheme.class);
    }

    public FlatMaterialDesignDarkIJTheme() {
        super(Utils.loadTheme("MaterialTheme.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
