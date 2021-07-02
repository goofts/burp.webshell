package com.formdev.flatlaf;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

public class FlatPropertiesLaf extends FlatLaf {
    private final String baseTheme;
    private final boolean dark;
    private final String name;
    private final Properties properties;

    public FlatPropertiesLaf(String name2, File propertiesFile) throws IOException {
        this(name2, new FileInputStream(propertiesFile));
    }

    public FlatPropertiesLaf(String name2, InputStream in) throws IOException {
        this(name2, loadProperties(in));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x001d, code lost:
        r2 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x001e, code lost:
        if (r5 != null) goto L_0x0020;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0020, code lost:
        if (r3 != null) goto L_0x0022;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:?, code lost:
        r5.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0025, code lost:
        throw r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0026, code lost:
        r4 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0027, code lost:
        r3.addSuppressed(r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x002b, code lost:
        r5.close();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.util.Properties loadProperties(java.io.InputStream r5) throws java.io.IOException {
        /*
            java.util.Properties r1 = new java.util.Properties
            r1.<init>()
            r0 = r5
            r3 = 0
            r1.load(r0)     // Catch:{ Throwable -> 0x001b }
            if (r0 == 0) goto L_0x0011
            if (r3 == 0) goto L_0x0017
            r0.close()     // Catch:{ Throwable -> 0x0012 }
        L_0x0011:
            return r1
        L_0x0012:
            r2 = move-exception
            r3.addSuppressed(r2)
            goto L_0x0011
        L_0x0017:
            r0.close()
            goto L_0x0011
        L_0x001b:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x001d }
        L_0x001d:
            r2 = move-exception
            if (r0 == 0) goto L_0x0025
            if (r3 == 0) goto L_0x002b
            r0.close()     // Catch:{ Throwable -> 0x0026 }
        L_0x0025:
            throw r2
        L_0x0026:
            r4 = move-exception
            r3.addSuppressed(r4)
            goto L_0x0025
        L_0x002b:
            r0.close()
            goto L_0x0025
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.FlatPropertiesLaf.loadProperties(java.io.InputStream):java.util.Properties");
    }

    public FlatPropertiesLaf(String name2, Properties properties2) {
        this.name = name2;
        this.properties = properties2;
        this.baseTheme = properties2.getProperty("@baseTheme", "light");
        this.dark = "dark".equalsIgnoreCase(this.baseTheme) || "darcula".equalsIgnoreCase(this.baseTheme);
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.name;
    }

    @Override // com.formdev.flatlaf.FlatLaf
    public boolean isDark() {
        return this.dark;
    }

    public Properties getProperties() {
        return this.properties;
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.FlatLaf
    public ArrayList<Class<?>> getLafClassesForDefaultsLoading() {
        ArrayList<Class<?>> lafClasses = new ArrayList<>();
        lafClasses.add(FlatLaf.class);
        String lowerCase = this.baseTheme.toLowerCase();
        char c = 65535;
        switch (lowerCase.hashCode()) {
            case 3075958:
                if (lowerCase.equals("dark")) {
                    c = 2;
                    break;
                }
                break;
            case 102970646:
                if (lowerCase.equals("light")) {
                    c = 1;
                    break;
                }
                break;
            case 570230263:
                if (lowerCase.equals("intellij")) {
                    c = 3;
                    break;
                }
                break;
            case 1441429116:
                if (lowerCase.equals("darcula")) {
                    c = 4;
                    break;
                }
                break;
        }
        switch (c) {
            case 2:
                lafClasses.add(FlatDarkLaf.class);
                break;
            case 3:
                lafClasses.add(FlatLightLaf.class);
                lafClasses.add(FlatIntelliJLaf.class);
                break;
            case 4:
                lafClasses.add(FlatDarkLaf.class);
                lafClasses.add(FlatDarculaLaf.class);
                break;
            default:
                lafClasses.add(FlatLightLaf.class);
                break;
        }
        return lafClasses;
    }

    /* access modifiers changed from: protected */
    @Override // com.formdev.flatlaf.FlatLaf
    public Properties getAdditionalDefaults() {
        return this.properties;
    }
}
