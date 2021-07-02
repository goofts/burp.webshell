package com.formdev.flatlaf;

import com.formdev.flatlaf.util.StringUtils;
import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import javax.swing.UIDefaults;
import javax.swing.plaf.ColorUIResource;

public class IntelliJTheme {
    private static Map<String, String> checkboxDuplicateColors = new HashMap();
    private static Map<String, String> checkboxKeyMapping = new HashMap();
    private static Map<String, String> uiKeyCopying = new HashMap();
    private static Map<String, String> uiKeyInverseMapping = new HashMap();
    private static Map<String, String> uiKeyMapping = new HashMap();
    public final String author;
    private final Map<String, String> colors;
    public final boolean dark;
    private final Map<String, Object> icons;
    private final boolean isMaterialUILite;
    public final String name;
    private Map<String, ColorUIResource> namedColors = Collections.emptyMap();
    private final Map<String, Object> ui;

    public static boolean install(InputStream in) {
        try {
            return FlatLaf.install(createLaf(in));
        } catch (Exception ex) {
            FlatLaf.LOG.log(Level.SEVERE, "FlatLaf: Failed to load IntelliJ theme", (Throwable) ex);
            return false;
        }
    }

    public static FlatLaf createLaf(InputStream in) throws IOException {
        return createLaf(new IntelliJTheme(in));
    }

    public static FlatLaf createLaf(IntelliJTheme theme) {
        return new ThemeLaf(theme);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x007f, code lost:
        r4 = th;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0080, code lost:
        r5 = r3;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0092, code lost:
        r3 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0093, code lost:
        r4 = r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public IntelliJTheme(java.io.InputStream r7) throws java.io.IOException {
        /*
        // Method dump skipped, instructions count: 149
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.IntelliJTheme.<init>(java.io.InputStream):void");
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void applyProperties(UIDefaults defaults) {
        if (this.ui != null) {
            defaults.put("Component.isIntelliJTheme", true);
            defaults.put("Button.paintShadow", true);
            defaults.put("Button.shadowWidth", Integer.valueOf(this.dark ? 2 : 1));
            Map<Object, Object> themeSpecificDefaults = removeThemeSpecificDefaults(defaults);
            loadNamedColors(defaults);
            ArrayList<Object> defaultsKeysCache = new ArrayList<>();
            Set<String> uiKeys = new HashSet<>();
            for (Map.Entry<String, Object> e : this.ui.entrySet()) {
                apply(e.getKey(), e.getValue(), defaults, defaultsKeysCache, uiKeys);
            }
            applyColorPalette(defaults);
            applyCheckBoxColors(defaults);
            for (Map.Entry<String, String> e2 : uiKeyCopying.entrySet()) {
                defaults.put(e2.getKey(), defaults.get(e2.getValue()));
            }
            Object panelBackground = defaults.get("Panel.background");
            defaults.put("Button.disabledBackground", panelBackground);
            defaults.put("ToggleButton.disabledBackground", panelBackground);
            copyIfNotSet(defaults, "Button.focusedBorderColor", "Component.focusedBorderColor", uiKeys);
            defaults.put("Button.hoverBorderColor", defaults.get("Button.focusedBorderColor"));
            defaults.put("HelpButton.hoverBorderColor", defaults.get("Button.focusedBorderColor"));
            Object helpButtonBackground = defaults.get("Button.startBackground");
            Object helpButtonBorderColor = defaults.get("Button.startBorderColor");
            if (helpButtonBackground == null) {
                helpButtonBackground = defaults.get("Button.background");
            }
            if (helpButtonBorderColor == null) {
                helpButtonBorderColor = defaults.get("Button.borderColor");
            }
            defaults.put("HelpButton.background", helpButtonBackground);
            defaults.put("HelpButton.borderColor", helpButtonBorderColor);
            defaults.put("HelpButton.disabledBackground", panelBackground);
            defaults.put("HelpButton.disabledBorderColor", defaults.get("Button.disabledBorderColor"));
            defaults.put("HelpButton.focusedBorderColor", defaults.get("Button.focusedBorderColor"));
            defaults.put("HelpButton.focusedBackground", defaults.get("Button.focusedBackground"));
            defaults.put("ComboBox.editableBackground", defaults.get("TextField.background"));
            defaults.put("Spinner.background", defaults.get("TextField.background"));
            defaults.put("Spinner.buttonBackground", defaults.get("ComboBox.buttonEditableBackground"));
            defaults.put("Spinner.buttonArrowColor", defaults.get("ComboBox.buttonArrowColor"));
            defaults.put("Spinner.buttonDisabledArrowColor", defaults.get("ComboBox.buttonDisabledArrowColor"));
            if (uiKeys.contains("TextField.background")) {
                Object textFieldBackground = defaults.get("TextField.background");
                if (!uiKeys.contains("FormattedTextField.background")) {
                    defaults.put("FormattedTextField.background", textFieldBackground);
                }
                if (!uiKeys.contains("PasswordField.background")) {
                    defaults.put("PasswordField.background", textFieldBackground);
                }
                if (!uiKeys.contains("EditorPane.background")) {
                    defaults.put("EditorPane.background", textFieldBackground);
                }
                if (!uiKeys.contains("TextArea.background")) {
                    defaults.put("TextArea.background", textFieldBackground);
                }
                if (!uiKeys.contains("TextPane.background")) {
                    defaults.put("TextPane.background", textFieldBackground);
                }
                if (!uiKeys.contains("Spinner.background")) {
                    defaults.put("Spinner.background", textFieldBackground);
                }
            }
            if (!uiKeys.contains("ToggleButton.startBackground") && !uiKeys.contains("*.startBackground")) {
                defaults.put("ToggleButton.startBackground", defaults.get("Button.startBackground"));
            }
            if (!uiKeys.contains("ToggleButton.endBackground") && !uiKeys.contains("*.endBackground")) {
                defaults.put("ToggleButton.endBackground", defaults.get("Button.endBackground"));
            }
            if (!uiKeys.contains("ToggleButton.foreground") && uiKeys.contains("Button.foreground")) {
                defaults.put("ToggleButton.foreground", defaults.get("Button.foreground"));
            }
            if (this.isMaterialUILite) {
                defaults.put("List.background", defaults.get("Tree.background"));
                defaults.put("Table.background", defaults.get("Tree.background"));
            }
            if (defaults.getInt("Tree.rowHeight") > 22) {
                defaults.put("Tree.rowHeight", 22);
            }
            defaults.putAll(themeSpecificDefaults);
        }
    }

    private Map<Object, Object> removeThemeSpecificDefaults(UIDefaults defaults) {
        ArrayList<String> themeSpecificKeys = new ArrayList<>();
        for (Object key : defaults.keySet()) {
            if ((key instanceof String) && ((String) key).startsWith("[")) {
                themeSpecificKeys.add((String) key);
            }
        }
        Map<Object, Object> themeSpecificDefaults = new HashMap<>();
        String[] prefixes = {'[' + this.name.replace(' ', '_') + ']', "[author-" + this.author.replace(' ', '_') + ']', "[*]"};
        Iterator<String> it = themeSpecificKeys.iterator();
        while (it.hasNext()) {
            String key2 = it.next();
            Object value = defaults.remove(key2);
            int length = prefixes.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                }
                String prefix = prefixes[i];
                if (key2.startsWith(prefix)) {
                    themeSpecificDefaults.put(key2.substring(prefix.length()), value);
                    break;
                }
                i++;
            }
        }
        return themeSpecificDefaults;
    }

    private void loadNamedColors(UIDefaults defaults) {
        if (this.colors != null) {
            this.namedColors = new HashMap();
            for (Map.Entry<String, String> e : this.colors.entrySet()) {
                ColorUIResource color = UIDefaultsLoader.parseColor(e.getValue());
                if (color != null) {
                    String key = e.getKey();
                    this.namedColors.put(key, color);
                    defaults.put("ColorPalette." + key, color);
                }
            }
        }
    }

    private void apply(String key, Object value, UIDefaults defaults, ArrayList<Object> defaultsKeysCache, Set<String> uiKeys) {
        if (value instanceof Map) {
            for (Map.Entry<String, Object> e : ((Map) value).entrySet()) {
                apply(key + '.' + e.getKey(), e.getValue(), defaults, defaultsKeysCache, uiKeys);
            }
        } else if (!"".equals(value)) {
            uiKeys.add(key);
            if (!this.isMaterialUILite || (!key.equals("ComboBox.padding") && !key.equals("Spinner.border"))) {
                String key2 = uiKeyMapping.getOrDefault(key, key);
                if (!key2.isEmpty()) {
                    String valueStr = value.toString();
                    Object uiValue = this.namedColors.get(valueStr);
                    if (uiValue == null) {
                        if (!valueStr.startsWith("#") && (key2.endsWith("ground") || key2.endsWith("Color"))) {
                            valueStr = fixColorIfValid("#" + valueStr, valueStr);
                        } else if (valueStr.startsWith("##")) {
                            valueStr = fixColorIfValid(valueStr.substring(1), valueStr);
                        } else if (key2.endsWith(".border") || key2.endsWith("Border")) {
                            List<String> parts = StringUtils.split(valueStr, ',');
                            if (parts.size() == 5 && !parts.get(4).startsWith("#")) {
                                parts.set(4, "#" + parts.get(4));
                                valueStr = String.join(",", parts);
                            }
                        }
                        try {
                            uiValue = UIDefaultsLoader.parseValue(key2, valueStr);
                        } catch (RuntimeException ex) {
                            UIDefaultsLoader.logParseError(Level.CONFIG, key2, valueStr, ex);
                            return;
                        }
                    }
                    if (key2.startsWith("*.")) {
                        String tail = key2.substring(1);
                        if (defaultsKeysCache.size() != defaults.size()) {
                            defaultsKeysCache.clear();
                            Enumeration<Object> e2 = defaults.keys();
                            while (e2.hasMoreElements()) {
                                defaultsKeysCache.add(e2.nextElement());
                            }
                        }
                        Iterator<Object> it = defaultsKeysCache.iterator();
                        while (it.hasNext()) {
                            Object k = it.next();
                            if ((k instanceof String) && uiKeyInverseMapping.getOrDefault(k, (String) k).endsWith(tail) && !((String) k).startsWith("CheckBox.icon.")) {
                                defaults.put(k, uiValue);
                            }
                        }
                        return;
                    }
                    defaults.put(key2, uiValue);
                }
            }
        }
    }

    private String fixColorIfValid(String newColorStr, String colorStr) {
        try {
            UIDefaultsLoader.parseColorRGBA(newColorStr);
            return newColorStr;
        } catch (IllegalArgumentException e) {
            return colorStr;
        }
    }

    private void applyColorPalette(UIDefaults defaults) {
        if (this.icons != null) {
            Object palette = this.icons.get("ColorPalette");
            if (palette instanceof Map) {
                for (Map.Entry<String, Object> e : ((Map) palette).entrySet()) {
                    String key = e.getKey();
                    Object value = e.getValue();
                    if (!key.startsWith("Checkbox.") && (value instanceof String)) {
                        if (this.dark) {
                            key = StringUtils.removeTrailing(key, ".Dark");
                        }
                        ColorUIResource color = toColor((String) value);
                        if (color != null) {
                            defaults.put(key, color);
                        }
                    }
                }
            }
        }
    }

    private ColorUIResource toColor(String value) {
        ColorUIResource color = this.namedColors.get(value);
        return color != null ? color : UIDefaultsLoader.parseColor(value);
    }

    private void applyCheckBoxColors(UIDefaults defaults) {
        if (this.icons != null) {
            Object palette = this.icons.get("ColorPalette");
            if (palette instanceof Map) {
                boolean checkboxModified = false;
                for (Map.Entry<String, Object> e : ((Map) palette).entrySet()) {
                    String key = e.getKey();
                    Object value = e.getValue();
                    if (key.startsWith("Checkbox.") && (value instanceof String)) {
                        if (key.equals("Checkbox.Background.Default") || key.equals("Checkbox.Foreground.Selected")) {
                            value = "#ffffff";
                        }
                        String key2 = checkboxDuplicateColors.get(key);
                        if (this.dark) {
                            key = StringUtils.removeTrailing(key, ".Dark");
                        }
                        String newKey = checkboxKeyMapping.get(key);
                        if (newKey != null) {
                            if (!this.dark && newKey.startsWith("CheckBox.icon.")) {
                                newKey = "CheckBox.icon[filled].".concat(newKey.substring("CheckBox.icon.".length()));
                            }
                            ColorUIResource color = toColor((String) value);
                            if (color != null) {
                                defaults.put(newKey, color);
                                if (key2 != null) {
                                    if (this.dark) {
                                        key2 = StringUtils.removeTrailing(key2, ".Dark");
                                    }
                                    String newKey2 = checkboxKeyMapping.get(key2);
                                    if (newKey2 != null) {
                                        defaults.put(newKey2, color);
                                    }
                                }
                            }
                            checkboxModified = true;
                        }
                    }
                }
                if (checkboxModified) {
                    defaults.remove("CheckBox.icon.focusWidth");
                    defaults.put("CheckBox.icon.hoverBorderColor", defaults.get("CheckBox.icon.focusedBorderColor"));
                    defaults.remove("CheckBox.icon[filled].focusWidth");
                    defaults.put("CheckBox.icon[filled].hoverBorderColor", defaults.get("CheckBox.icon[filled].focusedBorderColor"));
                    defaults.put("CheckBox.icon[filled].selectedFocusedBackground", defaults.get("CheckBox.icon[filled].selectedBackground"));
                    if (this.dark) {
                        String[] focusedBorderColorKeys = {"CheckBox.icon.focusedBorderColor", "CheckBox.icon.selectedFocusedBorderColor", "CheckBox.icon[filled].focusedBorderColor", "CheckBox.icon[filled].selectedFocusedBorderColor"};
                        for (String key3 : focusedBorderColorKeys) {
                            Color color2 = defaults.getColor(key3);
                            if (color2 != null) {
                                defaults.put(key3, new ColorUIResource(new Color((color2.getRGB() & 16777215) | -1509949440, true)));
                            }
                        }
                    }
                }
            }
        }
    }

    private void copyIfNotSet(UIDefaults defaults, String destKey, String srcKey, Set<String> uiKeys) {
        if (!uiKeys.contains(destKey)) {
            defaults.put(destKey, defaults.get(srcKey));
        }
    }

    /* JADX DEBUG: Multi-variable search result rejected for r4v56, resolved type: java.util.Map<java.lang.String, java.lang.String> */
    /* JADX WARN: Multi-variable type inference failed */
    static {
        uiKeyMapping.put("ComboBox.background", "");
        uiKeyMapping.put("ComboBox.nonEditableBackground", "ComboBox.background");
        uiKeyMapping.put("ComboBox.ArrowButton.background", "ComboBox.buttonEditableBackground");
        uiKeyMapping.put("ComboBox.ArrowButton.disabledIconColor", "ComboBox.buttonDisabledArrowColor");
        uiKeyMapping.put("ComboBox.ArrowButton.iconColor", "ComboBox.buttonArrowColor");
        uiKeyMapping.put("ComboBox.ArrowButton.nonEditableBackground", "ComboBox.buttonBackground");
        uiKeyMapping.put("Component.inactiveErrorFocusColor", "Component.error.borderColor");
        uiKeyMapping.put("Component.errorFocusColor", "Component.error.focusedBorderColor");
        uiKeyMapping.put("Component.inactiveWarningFocusColor", "Component.warning.borderColor");
        uiKeyMapping.put("Component.warningFocusColor", "Component.warning.focusedBorderColor");
        uiKeyMapping.put("Link.activeForeground", "Component.linkColor");
        uiKeyMapping.put("Menu.border", "Menu.margin");
        uiKeyMapping.put("MenuItem.border", "MenuItem.margin");
        uiKeyCopying.put("CheckBoxMenuItem.margin", "MenuItem.margin");
        uiKeyCopying.put("RadioButtonMenuItem.margin", "MenuItem.margin");
        uiKeyMapping.put("PopupMenu.border", "PopupMenu.borderInsets");
        uiKeyCopying.put("MenuItem.underlineSelectionColor", "TabbedPane.underlineColor");
        uiKeyCopying.put("Menu.selectionBackground", "List.selectionBackground");
        uiKeyCopying.put("MenuItem.selectionBackground", "List.selectionBackground");
        uiKeyCopying.put("CheckBoxMenuItem.selectionBackground", "List.selectionBackground");
        uiKeyCopying.put("RadioButtonMenuItem.selectionBackground", "List.selectionBackground");
        uiKeyMapping.put("ProgressBar.background", "");
        uiKeyMapping.put("ProgressBar.foreground", "");
        uiKeyMapping.put("ProgressBar.trackColor", "ProgressBar.background");
        uiKeyMapping.put("ProgressBar.progressColor", "ProgressBar.foreground");
        uiKeyCopying.put("ProgressBar.selectionForeground", "ProgressBar.background");
        uiKeyCopying.put("ProgressBar.selectionBackground", "ProgressBar.foreground");
        uiKeyMapping.put("ScrollBar.trackColor", "ScrollBar.track");
        uiKeyMapping.put("ScrollBar.thumbColor", "ScrollBar.thumb");
        uiKeyMapping.put("Separator.separatorColor", "Separator.foreground");
        uiKeyMapping.put("Slider.trackWidth", "");
        uiKeyCopying.put("Slider.trackValueColor", "ProgressBar.foreground");
        uiKeyCopying.put("Slider.thumbColor", "ProgressBar.foreground");
        uiKeyCopying.put("Slider.trackColor", "ProgressBar.background");
        uiKeyCopying.put("TitlePane.inactiveBackground", "TitlePane.background");
        uiKeyMapping.put("TitlePane.infoForeground", "TitlePane.foreground");
        uiKeyMapping.put("TitlePane.inactiveInfoForeground", "TitlePane.inactiveForeground");
        for (Map.Entry<String, String> e : uiKeyMapping.entrySet()) {
            uiKeyInverseMapping.put(e.getValue(), e.getKey());
        }
        uiKeyCopying.put("ToggleButton.tab.underlineColor", "TabbedPane.underlineColor");
        uiKeyCopying.put("ToggleButton.tab.disabledUnderlineColor", "TabbedPane.disabledUnderlineColor");
        uiKeyCopying.put("ToggleButton.tab.selectedBackground", "TabbedPane.selectedBackground");
        uiKeyCopying.put("ToggleButton.tab.hoverBackground", "TabbedPane.hoverColor");
        uiKeyCopying.put("ToggleButton.tab.focusBackground", "TabbedPane.focusColor");
        checkboxKeyMapping.put("Checkbox.Background.Default", "CheckBox.icon.background");
        checkboxKeyMapping.put("Checkbox.Background.Disabled", "CheckBox.icon.disabledBackground");
        checkboxKeyMapping.put("Checkbox.Border.Default", "CheckBox.icon.borderColor");
        checkboxKeyMapping.put("Checkbox.Border.Disabled", "CheckBox.icon.disabledBorderColor");
        checkboxKeyMapping.put("Checkbox.Focus.Thin.Default", "CheckBox.icon.focusedBorderColor");
        checkboxKeyMapping.put("Checkbox.Focus.Wide", "CheckBox.icon.focusColor");
        checkboxKeyMapping.put("Checkbox.Foreground.Disabled", "CheckBox.icon.disabledCheckmarkColor");
        checkboxKeyMapping.put("Checkbox.Background.Selected", "CheckBox.icon.selectedBackground");
        checkboxKeyMapping.put("Checkbox.Border.Selected", "CheckBox.icon.selectedBorderColor");
        checkboxKeyMapping.put("Checkbox.Foreground.Selected", "CheckBox.icon.checkmarkColor");
        checkboxKeyMapping.put("Checkbox.Focus.Thin.Selected", "CheckBox.icon.selectedFocusedBorderColor");
        checkboxDuplicateColors.put("Checkbox.Background.Default.Dark", "Checkbox.Background.Selected.Dark");
        checkboxDuplicateColors.put("Checkbox.Border.Default.Dark", "Checkbox.Border.Selected.Dark");
        checkboxDuplicateColors.put("Checkbox.Focus.Thin.Default.Dark", "Checkbox.Focus.Thin.Selected.Dark");
        Map.Entry<String, String>[] entries = (Map.Entry[]) checkboxDuplicateColors.entrySet().toArray(new Map.Entry[checkboxDuplicateColors.size()]);
        for (Map.Entry<String, String> e2 : entries) {
            checkboxDuplicateColors.put(e2.getValue(), e2.getKey());
        }
    }

    public static class ThemeLaf extends FlatLaf {
        private final IntelliJTheme theme;

        public ThemeLaf(IntelliJTheme theme2) {
            this.theme = theme2;
        }

        public String getName() {
            return this.theme.name;
        }

        public String getDescription() {
            return getName();
        }

        @Override // com.formdev.flatlaf.FlatLaf
        public boolean isDark() {
            return this.theme.dark;
        }

        public IntelliJTheme getTheme() {
            return this.theme;
        }

        /* access modifiers changed from: package-private */
        @Override // com.formdev.flatlaf.FlatLaf
        public void applyAdditionalDefaults(UIDefaults defaults) {
            this.theme.applyProperties(defaults);
        }

        /* JADX DEBUG: Multi-variable search result rejected for r0v0, resolved type: java.util.ArrayList<java.lang.Class<?>> */
        /* JADX WARN: Multi-variable type inference failed */
        /* access modifiers changed from: protected */
        @Override // com.formdev.flatlaf.FlatLaf
        public ArrayList<Class<?>> getLafClassesForDefaultsLoading() {
            ArrayList<Class<?>> lafClasses = new ArrayList<>();
            lafClasses.add(FlatLaf.class);
            lafClasses.add(this.theme.dark ? FlatDarkLaf.class : FlatLightLaf.class);
            lafClasses.add(this.theme.dark ? FlatDarculaLaf.class : FlatIntelliJLaf.class);
            lafClasses.add(ThemeLaf.class);
            return lafClasses;
        }
    }
}
