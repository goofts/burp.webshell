package com.formdev.flatlaf.demo.intellijthemes;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class IJThemesUpdater {
    public static void main(String[] args) {
        IJThemesManager themesManager = new IJThemesManager();
        themesManager.loadBundledThemes();
        for (IJThemeInfo ti : themesManager.bundledThemes) {
            if (!(ti.sourceCodeUrl == null || ti.sourceCodePath == null)) {
                String fromUrl = ti.sourceCodeUrl + "/" + ti.sourceCodePath;
                if (fromUrl.contains("github.com")) {
                    fromUrl = fromUrl + "?raw=true";
                } else if (fromUrl.contains("gitlab.com")) {
                    fromUrl = fromUrl.replace("/blob/", "/raw/");
                }
                download(fromUrl, "../flatlaf-intellij-themes/src/main/resources/com/formdev/flatlaf/intellijthemes/themes/" + ti.resourceName);
            }
        }
    }

    private static void download(String fromUrl, String toPath) {
        System.out.println("Download " + fromUrl);
        Path out = new File(toPath).toPath();
        try {
            Files.copy(new URL(fromUrl.replace(" ", "%20")).openConnection().getInputStream(), out, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
