package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatGitHubIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "GitHub (Material)";

    public static boolean install() {
        try {
            return install(new FlatGitHubIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatGitHubIJTheme.class);
    }

    public FlatGitHubIJTheme() {
        super(Utils.loadTheme("GitHub.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
