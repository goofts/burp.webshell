package com.formdev.flatlaf.intellijthemes.materialthemeuilite;

import com.formdev.flatlaf.IntelliJTheme;

public class FlatGitHubContrastIJTheme extends IntelliJTheme.ThemeLaf {
    public static final String NAME = "GitHub Contrast (Material)";

    public static boolean install() {
        try {
            return install(new FlatGitHubContrastIJTheme());
        } catch (RuntimeException e) {
            return false;
        }
    }

    public static void installLafInfo() {
        installLafInfo(NAME, FlatGitHubContrastIJTheme.class);
    }

    public FlatGitHubContrastIJTheme() {
        super(Utils.loadTheme("GitHub Contrast.theme.json"));
    }

    @Override // com.formdev.flatlaf.IntelliJTheme.ThemeLaf
    public String getName() {
        return NAME;
    }
}
