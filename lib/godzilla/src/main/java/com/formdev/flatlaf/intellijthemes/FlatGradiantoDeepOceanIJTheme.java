package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatGradiantoDeepOceanIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Gradianto Deep Ocean";

    public static boolean install() {
        try {
            return install(new FlatGradiantoDeepOceanIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatGradiantoDeepOceanIJTheme.class);
    }

    public FlatGradiantoDeepOceanIJTheme() {
        super(Utils.loadTheme("Gradianto_deep_ocean.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
