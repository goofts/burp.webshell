package com.formdev.flatlaf;

import com.formdev.flatlaf.util.StringUtils;
import com.formdev.flatlaf.util.SystemInfo;
import com.formdev.flatlaf.util.UIScale;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;

/* access modifiers changed from: package-private */
public class LinuxFontPolicy {
    LinuxFontPolicy() {
    }

    static Font getFont() {
        return SystemInfo.isKDE ? getKDEFont() : getGnomeFont();
    }

    private static Font getGnomeFont() {
        Object fontName = Toolkit.getDefaultToolkit().getDesktopProperty("gnome.Gtk/FontName");
        if (!(fontName instanceof String)) {
            fontName = "sans 10";
        }
        String family = "";
        int style = 0;
        int size = 10;
        StringTokenizer st = new StringTokenizer((String) fontName);
        while (st.hasMoreTokens()) {
            String word = st.nextToken();
            if (word.equalsIgnoreCase("italic")) {
                style |= 2;
            } else if (word.equalsIgnoreCase("bold")) {
                style |= 1;
            } else if (Character.isDigit(word.charAt(0))) {
                try {
                    size = Integer.parseInt(word);
                } catch (NumberFormatException e) {
                }
            } else {
                family = family.isEmpty() ? word : family + ' ' + word;
            }
        }
        if (family.startsWith("Ubuntu") && !SystemInfo.isJetBrainsJVM && !FlatSystemProperties.getBoolean(FlatSystemProperties.USE_UBUNTU_FONT, false)) {
            family = "Liberation Sans";
        }
        double dsize = ((double) size) * getGnomeFontScale();
        int size2 = (int) (0.5d + dsize);
        if (size2 < 1) {
            size2 = 1;
        }
        String logicalFamily = mapFcName(family.toLowerCase());
        if (logicalFamily != null) {
            family = logicalFamily;
        }
        return createFont(family, style, size2, dsize);
    }

    private static Font createFont(String family, int style, int size, double dsize) {
        return FlatLaf.createCompositeFont(family, style, size).deriveFont(style, (float) dsize);
    }

    private static double getGnomeFontScale() {
        if (isSystemScaling()) {
            return 1.3333333333333333d;
        }
        Object value = Toolkit.getDefaultToolkit().getDesktopProperty("gnome.Xft/DPI");
        if (!(value instanceof Integer)) {
            return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration().getNormalizingTransform().getScaleY();
        }
        int dpi = ((Integer) value).intValue() / 1024;
        if (dpi == -1) {
            dpi = 96;
        }
        if (dpi < 50) {
            dpi = 50;
        }
        return ((double) dpi) / 72.0d;
    }

    private static String mapFcName(String name) {
        char c = 65535;
        switch (name.hashCode()) {
            case -1536685117:
                if (name.equals("sans-serif")) {
                    c = 1;
                    break;
                }
                break;
            case -1431958525:
                if (name.equals("monospace")) {
                    c = 3;
                    break;
                }
                break;
            case 3522707:
                if (name.equals("sans")) {
                    c = 0;
                    break;
                }
                break;
            case 109326717:
                if (name.equals("serif")) {
                    c = 2;
                    break;
                }
                break;
        }
        switch (c) {
            case 0:
                return "sansserif";
            case 1:
                return "sansserif";
            case 2:
                return "serif";
            case 3:
                return "monospaced";
            default:
                return null;
        }
    }

    private static Font getKDEFont() {
        List<String> kdeglobals = readConfig("kdeglobals");
        List<String> kcmfonts = readConfig("kcmfonts");
        String generalFont = getConfigEntry(kdeglobals, "General", com.kitfox.svg.Font.TAG_NAME);
        String forceFontDPI = getConfigEntry(kcmfonts, "General", "forceFontDPI");
        String family = "sansserif";
        int style = 0;
        int size = 10;
        if (generalFont != null) {
            List<String> strs = StringUtils.split(generalFont, ',');
            try {
                family = strs.get(0);
                size = Integer.parseInt(strs.get(1));
                if ("75".equals(strs.get(4))) {
                    style = 0 | 1;
                }
                if ("1".equals(strs.get(5))) {
                    style |= 2;
                }
            } catch (RuntimeException ex) {
                FlatLaf.LOG.log(Level.CONFIG, "FlatLaf: Failed to parse 'font=" + generalFont + "'.", (Throwable) ex);
            }
        }
        int dpi = 96;
        if (forceFontDPI != null && !isSystemScaling()) {
            try {
                dpi = Integer.parseInt(forceFontDPI);
                if (dpi <= 0) {
                    dpi = 96;
                }
                if (dpi < 50) {
                    dpi = 50;
                }
            } catch (NumberFormatException ex2) {
                FlatLaf.LOG.log(Level.CONFIG, "FlatLaf: Failed to parse 'forceFontDPI=" + forceFontDPI + "'.", (Throwable) ex2);
            }
        }
        double dsize = ((double) size) * (((double) dpi) / 72.0d);
        int size2 = (int) (0.5d + dsize);
        if (size2 < 1) {
            size2 = 1;
        }
        return createFont(family, style, size2, dsize);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0072, code lost:
        r9 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0073, code lost:
        r10 = r8;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:38:0x00b8, code lost:
        r8 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:39:0x00b9, code lost:
        r9 = r8;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.util.List<java.lang.String> readConfig(java.lang.String r12) {
        /*
        // Method dump skipped, instructions count: 187
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.LinuxFontPolicy.readConfig(java.lang.String):java.util.List");
    }

    private static String getConfigEntry(List<String> config, String group, String key) {
        int groupLength = group.length();
        int keyLength = key.length();
        boolean inGroup = false;
        for (String line : config) {
            if (!inGroup) {
                if (line.length() >= groupLength + 2 && line.charAt(0) == '[' && line.charAt(groupLength + 1) == ']' && line.indexOf(group) == 1) {
                    inGroup = true;
                }
            } else if (line.startsWith("[")) {
                return null;
            } else {
                if (line.length() >= keyLength + 2 && line.charAt(keyLength) == '=' && line.startsWith(key)) {
                    return line.substring(keyLength + 1);
                }
            }
        }
        return null;
    }

    private static boolean isSystemScaling() {
        return UIScale.getSystemScaleFactor(GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration()) > 1.0d;
    }
}
