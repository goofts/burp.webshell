package com.formdev.flatlaf.intellijthemes;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatMonocaiIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "Monocai";

    public static boolean install() {
        try {
            return install(new FlatMonocaiIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatMonocaiIJTheme.class);
    }

    public FlatMonocaiIJTheme() {
        super(Utils.loadTheme("Monocai.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
