package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatCobalt2IJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Cobalt 2";

    public static boolean install() {
        try {
            return install(new FlatCobalt2IJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatCobalt2IJTheme.class);
    }

    public FlatCobalt2IJTheme() {
        super(Utils.loadTheme("Cobalt_2.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
