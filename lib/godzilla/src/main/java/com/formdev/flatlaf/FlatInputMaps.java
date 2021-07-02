package com.formdev.flatlaf;

import com.formdev.flatlaf.util.SystemInfo;
import java.util.function.BooleanSupplier;
import javassist.bytecode.Opcode;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;
import javax.swing.plaf.InputMapUIResource;

class FlatInputMaps {
    FlatInputMaps() {
    }

    static void initInputMaps(UIDefaults defaults) {
        initBasicInputMaps(defaults);
        initTextComponentInputMaps(defaults);
        if (SystemInfo.isMacOS) {
            initMacInputMaps(defaults);
        }
    }

    /* JADX WARN: Type inference failed for: r3v10, types: [void, java.util.function.BooleanSupplier] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void initBasicInputMaps(javax.swing.UIDefaults r13) {
        /*
        // Method dump skipped, instructions count: 418
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.FlatInputMaps.initBasicInputMaps(javax.swing.UIDefaults):void");
    }

    private static void initTextComponentInputMaps(UIDefaults defaults) {
        Object[] commonTextComponentBindings = {"LEFT", "caret-backward", "RIGHT", "caret-forward", "KP_LEFT", "caret-backward", "KP_RIGHT", "caret-forward", "shift LEFT", "selection-backward", "shift RIGHT", "selection-forward", "shift KP_LEFT", "selection-backward", "shift KP_RIGHT", "selection-forward", mac("ctrl LEFT", "alt LEFT"), "caret-previous-word", mac("ctrl RIGHT", "alt RIGHT"), "caret-next-word", mac("ctrl KP_LEFT", "alt KP_LEFT"), "caret-previous-word", mac("ctrl KP_RIGHT", "alt KP_RIGHT"), "caret-next-word", mac("ctrl shift LEFT", "shift alt LEFT"), "selection-previous-word", mac("ctrl shift RIGHT", "shift alt RIGHT"), "selection-next-word", mac("ctrl shift KP_LEFT", "shift alt KP_LEFT"), "selection-previous-word", mac("ctrl shift KP_RIGHT", "shift alt KP_RIGHT"), "selection-next-word", mac("HOME", "meta LEFT"), "caret-begin-line", mac("END", "meta RIGHT"), "caret-end-line", mac("shift HOME", "shift meta LEFT"), "selection-begin-line", mac("shift END", "shift meta RIGHT"), "selection-end-line", mac("ctrl A", "meta A"), "select-all", mac("ctrl BACK_SLASH", "meta BACK_SLASH"), "unselect", "BACK_SPACE", "delete-previous", "shift BACK_SPACE", "delete-previous", "ctrl H", "delete-previous", "DELETE", "delete-next", mac("ctrl BACK_SPACE", "alt BACK_SPACE"), "delete-previous-word", mac("ctrl DELETE", "alt DELETE"), "delete-next-word", mac("ctrl X", "meta X"), "cut-to-clipboard", mac("ctrl C", "meta C"), "copy-to-clipboard", mac("ctrl V", "meta V"), "paste-from-clipboard", "CUT", "cut-to-clipboard", "COPY", "copy-to-clipboard", "PASTE", "paste-from-clipboard", mac("shift DELETE", null), "cut-to-clipboard", mac("control INSERT", null), "copy-to-clipboard", mac("shift INSERT", null), "paste-from-clipboard", "control shift O", "toggle-componentOrientation"};
        Object[] macCommonTextComponentBindings = SystemInfo.isMacOS ? new Object[]{"ctrl B", "caret-backward", "ctrl F", "caret-forward", "HOME", "caret-begin", "END", "caret-end", "meta UP", "caret-begin", "meta DOWN", "caret-end", "meta KP_UP", "caret-begin", "meta KP_DOWN", "caret-end", "ctrl P", "caret-begin", "ctrl N", "caret-end", "ctrl V", "caret-end", "meta KP_LEFT", "caret-begin-line", "meta KP_RIGHT", "caret-end-line", "ctrl A", "caret-begin-line", "ctrl E", "caret-end-line", "shift meta UP", "selection-begin", "shift meta DOWN", "selection-end", "shift meta KP_UP", "selection-begin", "shift meta KP_DOWN", "selection-end", "shift HOME", "selection-begin", "shift END", "selection-end", "shift meta KP_LEFT", "selection-begin-line", "shift meta KP_RIGHT", "selection-end-line", "shift UP", "selection-begin-line", "shift DOWN", "selection-end-line", "shift KP_UP", "selection-begin-line", "shift KP_DOWN", "selection-end-line", "ctrl W", "delete-previous-word", "ctrl D", "delete-next"} : null;
        Object[] singleLineTextComponentBindings = {"ENTER", "notify-field-accept"};
        Object[] macSingleLineTextComponentBindings = SystemInfo.isMacOS ? new Object[]{"UP", "caret-begin-line", "DOWN", "caret-end-line", "KP_UP", "caret-begin-line", "KP_DOWN", "caret-end-line"} : null;
        Object[] formattedTextComponentBindings = {"ESCAPE", "reset-field-edit", "UP", "increment", "DOWN", "decrement", "KP_UP", "increment", "KP_DOWN", "decrement"};
        Object[] passwordTextComponentBindings = {mac("ctrl LEFT", "alt LEFT"), "caret-begin-line", mac("ctrl RIGHT", "alt RIGHT"), "caret-end-line", mac("ctrl KP_LEFT", "alt KP_LEFT"), "caret-begin-line", mac("ctrl KP_RIGHT", "alt KP_RIGHT"), "caret-end-line", mac("ctrl shift LEFT", "shift alt LEFT"), "selection-begin-line", mac("ctrl shift RIGHT", "shift alt RIGHT"), "selection-end-line", mac("ctrl shift KP_LEFT", "shift alt KP_LEFT"), "selection-begin-line", mac("ctrl shift KP_RIGHT", "shift alt KP_RIGHT"), "selection-end-line", mac("ctrl BACK_SPACE", "alt BACK_SPACE"), null, mac("ctrl DELETE", "alt DELETE"), null};
        Object[] multiLineTextComponentBindings = {"UP", "caret-up", "DOWN", "caret-down", "KP_UP", "caret-up", "KP_DOWN", "caret-down", "shift UP", "selection-up", "shift DOWN", "selection-down", "shift KP_UP", "selection-up", "shift KP_DOWN", "selection-down", "PAGE_UP", "page-up", "PAGE_DOWN", "page-down", "shift PAGE_UP", "selection-page-up", "shift PAGE_DOWN", "selection-page-down", mac("ctrl shift PAGE_UP", "shift meta PAGE_UP"), "selection-page-left", mac("ctrl shift PAGE_DOWN", "shift meta PAGE_DOWN"), "selection-page-right", mac("ctrl HOME", "meta UP"), "caret-begin", mac("ctrl END", "meta DOWN"), "caret-end", mac("ctrl shift HOME", "shift meta UP"), "selection-begin", mac("ctrl shift END", "shift meta DOWN"), "selection-end", "ENTER", "insert-break", "TAB", "insert-tab", mac("ctrl T", "meta T"), "next-link-action", mac("ctrl shift T", "shift meta T"), "previous-link-action", mac("ctrl SPACE", "meta SPACE"), "activate-link-action"};
        Object[] macMultiLineTextComponentBindings = SystemInfo.isMacOS ? new Object[]{"ctrl N", "caret-down", "ctrl P", "caret-up", "shift alt UP", "selection-begin-paragraph", "shift alt DOWN", "selection-end-paragraph", "shift alt KP_UP", "selection-begin-paragraph", "shift alt KP_DOWN", "selection-end-paragraph", "ctrl V", "page-down"} : null;
        defaults.put("TextField.focusInputMap", new LazyInputMapEx(commonTextComponentBindings, macCommonTextComponentBindings, singleLineTextComponentBindings, macSingleLineTextComponentBindings));
        defaults.put("FormattedTextField.focusInputMap", new LazyInputMapEx(commonTextComponentBindings, macCommonTextComponentBindings, singleLineTextComponentBindings, macSingleLineTextComponentBindings, formattedTextComponentBindings));
        defaults.put("PasswordField.focusInputMap", new LazyInputMapEx(commonTextComponentBindings, macCommonTextComponentBindings, singleLineTextComponentBindings, macSingleLineTextComponentBindings, passwordTextComponentBindings));
        LazyInputMapEx multiLineInputMap = new LazyInputMapEx(commonTextComponentBindings, macCommonTextComponentBindings, multiLineTextComponentBindings, macMultiLineTextComponentBindings);
        defaults.put("TextArea.focusInputMap", multiLineInputMap);
        defaults.put("TextPane.focusInputMap", multiLineInputMap);
        defaults.put("EditorPane.focusInputMap", multiLineInputMap);
    }

    private static void initMacInputMaps(UIDefaults defaults) {
        modifyInputMap(defaults, "List.focusInputMap", "meta A", "selectAll", "meta C", "copy", "meta V", "paste", "meta X", "cut", "HOME", null, "END", null, "PAGE_UP", null, "PAGE_DOWN", null, "ctrl A", null, "ctrl BACK_SLASH", null, "ctrl C", null, "ctrl DOWN", null, "ctrl END", null, "ctrl HOME", null, "ctrl INSERT", null, "ctrl KP_DOWN", null, "ctrl KP_LEFT", null, "ctrl KP_RIGHT", null, "ctrl KP_UP", null, "ctrl LEFT", null, "ctrl PAGE_DOWN", null, "ctrl PAGE_UP", null, "ctrl RIGHT", null, "ctrl SLASH", null, "ctrl SPACE", null, "ctrl UP", null, "ctrl V", null, "ctrl X", null, "SPACE", null, "shift ctrl DOWN", null, "shift ctrl END", null, "shift ctrl HOME", null, "shift ctrl KP_DOWN", null, "shift ctrl KP_LEFT", null, "shift ctrl KP_RIGHT", null, "shift ctrl KP_UP", null, "shift ctrl LEFT", null, "shift ctrl PAGE_DOWN", null, "shift ctrl PAGE_UP", null, "shift ctrl RIGHT", null, "shift ctrl SPACE", null, "shift ctrl UP", null, "shift DELETE", null, "shift INSERT", null, "shift SPACE", null);
        modifyInputMap(defaults, "List.focusInputMap.RightToLeft", "ctrl KP_LEFT", null, "ctrl KP_RIGHT", null, "ctrl LEFT", null, "ctrl RIGHT", null, "shift ctrl KP_LEFT", null, "shift ctrl KP_RIGHT", null, "shift ctrl LEFT", null, "shift ctrl RIGHT", null);
        modifyInputMap(defaults, "ScrollPane.ancestorInputMap", "END", "scrollEnd", "HOME", "scrollHome", "ctrl END", null, "ctrl HOME", null, "ctrl PAGE_DOWN", null, "ctrl PAGE_UP", null);
        modifyInputMap(defaults, "ScrollPane.ancestorInputMap.RightToLeft", "ctrl PAGE_DOWN", null, "ctrl PAGE_UP", null);
        modifyInputMap(defaults, "TabbedPane.ancestorInputMap", "ctrl UP", null, "ctrl KP_UP", null);
        modifyInputMap(defaults, "TabbedPane.focusInputMap", "ctrl DOWN", null, "ctrl KP_DOWN", null);
        modifyInputMap(defaults, "Table.ancestorInputMap", "alt TAB", "focusHeader", "shift alt TAB", "focusHeader", "meta A", "selectAll", "meta C", "copy", "meta V", "paste", "meta X", "cut", "HOME", null, "END", null, "PAGE_UP", null, "PAGE_DOWN", null, "ctrl A", null, "ctrl BACK_SLASH", null, "ctrl C", null, "ctrl DOWN", null, "ctrl END", null, "ctrl HOME", null, "ctrl INSERT", null, "ctrl KP_DOWN", null, "ctrl KP_LEFT", null, "ctrl KP_RIGHT", null, "ctrl KP_UP", null, "ctrl LEFT", null, "ctrl PAGE_DOWN", null, "ctrl PAGE_UP", null, "ctrl RIGHT", null, "ctrl SLASH", null, "ctrl SPACE", null, "ctrl UP", null, "ctrl V", null, "ctrl X", null, "F2", null, "F8", null, "SPACE", null, "shift ctrl DOWN", null, "shift ctrl END", null, "shift ctrl HOME", null, "shift ctrl KP_DOWN", null, "shift ctrl KP_LEFT", null, "shift ctrl KP_RIGHT", null, "shift ctrl KP_UP", null, "shift ctrl LEFT", null, "shift ctrl PAGE_DOWN", null, "shift ctrl PAGE_UP", null, "shift ctrl RIGHT", null, "shift ctrl SPACE", null, "shift ctrl UP", null, "shift DELETE", null, "shift INSERT", null, "shift SPACE", null);
        modifyInputMap(defaults, "Table.ancestorInputMap.RightToLeft", "ctrl KP_LEFT", null, "ctrl KP_RIGHT", null, "ctrl LEFT", null, "ctrl RIGHT", null, "shift ctrl KP_LEFT", null, "shift ctrl KP_RIGHT", null, "shift ctrl LEFT", null, "shift ctrl RIGHT", null);
        Object[] objArr = new Object[Opcode.INEG];
        objArr[0] = "LEFT";
        objArr[1] = "selectParent";
        objArr[2] = "RIGHT";
        objArr[3] = "selectChild";
        objArr[4] = "KP_LEFT";
        objArr[5] = "selectParent";
        objArr[6] = "KP_RIGHT";
        objArr[7] = "selectChild";
        objArr[8] = "shift LEFT";
        objArr[9] = "selectParent";
        objArr[10] = "shift RIGHT";
        objArr[11] = "selectChild";
        objArr[12] = "shift KP_LEFT";
        objArr[13] = "selectParent";
        objArr[14] = "shift KP_RIGHT";
        objArr[15] = "selectChild";
        objArr[16] = "alt LEFT";
        objArr[17] = "selectParent";
        objArr[18] = "alt RIGHT";
        objArr[19] = "selectChild";
        objArr[20] = "alt KP_LEFT";
        objArr[21] = "selectParent";
        objArr[22] = "alt KP_RIGHT";
        objArr[23] = "selectChild";
        objArr[24] = "shift HOME";
        objArr[25] = "selectFirstExtendSelection";
        objArr[26] = "shift END";
        objArr[27] = "selectLastExtendSelection";
        objArr[28] = "meta A";
        objArr[29] = "selectAll";
        objArr[30] = "meta C";
        objArr[31] = "copy";
        objArr[32] = "meta V";
        objArr[33] = "paste";
        objArr[34] = "meta X";
        objArr[35] = "cut";
        objArr[36] = "HOME";
        objArr[37] = null;
        objArr[38] = "END";
        objArr[39] = null;
        objArr[40] = "PAGE_UP";
        objArr[41] = null;
        objArr[42] = "PAGE_DOWN";
        objArr[43] = null;
        objArr[44] = "ctrl LEFT";
        objArr[45] = null;
        objArr[46] = "ctrl RIGHT";
        objArr[47] = null;
        objArr[48] = "ctrl KP_LEFT";
        objArr[49] = null;
        objArr[50] = "ctrl KP_RIGHT";
        objArr[51] = null;
        objArr[52] = "ctrl A";
        objArr[53] = null;
        objArr[54] = "ctrl BACK_SLASH";
        objArr[55] = null;
        objArr[56] = "ctrl C";
        objArr[57] = null;
        objArr[58] = "ctrl DOWN";
        objArr[59] = null;
        objArr[60] = "ctrl END";
        objArr[61] = null;
        objArr[62] = "ctrl HOME";
        objArr[63] = null;
        objArr[64] = "ctrl INSERT";
        objArr[65] = null;
        objArr[66] = "ctrl KP_DOWN";
        objArr[67] = null;
        objArr[68] = "ctrl KP_UP";
        objArr[69] = null;
        objArr[70] = "ctrl PAGE_DOWN";
        objArr[71] = null;
        objArr[72] = "ctrl PAGE_UP";
        objArr[73] = null;
        objArr[74] = "ctrl SLASH";
        objArr[75] = null;
        objArr[76] = "ctrl SPACE";
        objArr[77] = null;
        objArr[78] = "ctrl UP";
        objArr[79] = null;
        objArr[80] = "ctrl V";
        objArr[81] = null;
        objArr[82] = "ctrl X";
        objArr[83] = null;
        objArr[84] = "F2";
        objArr[85] = null;
        objArr[86] = "SPACE";
        objArr[87] = null;
        objArr[88] = "shift ctrl DOWN";
        objArr[89] = null;
        objArr[90] = "shift ctrl END";
        objArr[91] = null;
        objArr[92] = "shift ctrl HOME";
        objArr[93] = null;
        objArr[94] = "shift ctrl KP_DOWN";
        objArr[95] = null;
        objArr[96] = "shift ctrl KP_UP";
        objArr[97] = null;
        objArr[98] = "shift ctrl PAGE_DOWN";
        objArr[99] = null;
        objArr[100] = "shift ctrl PAGE_UP";
        objArr[101] = null;
        objArr[102] = "shift ctrl SPACE";
        objArr[103] = null;
        objArr[104] = "shift ctrl UP";
        objArr[105] = null;
        objArr[106] = "shift DELETE";
        objArr[107] = null;
        objArr[108] = "shift INSERT";
        objArr[109] = null;
        objArr[110] = "shift PAGE_DOWN";
        objArr[111] = null;
        objArr[112] = "shift PAGE_UP";
        objArr[113] = null;
        objArr[114] = "shift SPACE";
        objArr[115] = null;
        modifyInputMap(defaults, "Tree.focusInputMap", objArr);
        defaults.put("Tree.focusInputMap.RightToLeft", new UIDefaults.LazyInputMap(new Object[]{"LEFT", "selectChild", "RIGHT", "selectParent", "KP_LEFT", "selectChild", "KP_RIGHT", "selectParent", "shift LEFT", "selectChild", "shift RIGHT", "selectParent", "shift KP_LEFT", "selectChild", "shift KP_RIGHT", "selectParent", "alt LEFT", "selectChild", "alt RIGHT", "selectParent", "alt KP_LEFT", "selectChild", "alt KP_RIGHT", "selectParent"}));
    }

    private static void modifyInputMap(UIDefaults defaults, String key, Object... bindings) {
        modifyInputMap(null, defaults, key, bindings);
    }

    private static void modifyInputMap(BooleanSupplier condition, UIDefaults defaults, String key, Object... bindings) {
        defaults.put(key, new LazyModifyInputMap(condition, defaults.remove(key), bindings));
    }

    private static <T> T mac(T value, T macValue) {
        return SystemInfo.isMacOS ? macValue : value;
    }

    /* access modifiers changed from: private */
    public static class LazyInputMapEx implements UIDefaults.LazyValue {
        private final Object[][] bindingsArray;

        LazyInputMapEx(Object[]... bindingsArray2) {
            this.bindingsArray = bindingsArray2;
        }

        public Object createValue(UIDefaults table) {
            InputMap inputMap = new InputMapUIResource();
            for (Object[] bindings : this.bindingsArray) {
                LookAndFeel.loadKeyBindings(inputMap, bindings);
            }
            return inputMap;
        }
    }

    /* access modifiers changed from: private */
    public static class LazyModifyInputMap implements UIDefaults.LazyValue {
        private final Object baseInputMap;
        private final Object[] bindings;
        private final BooleanSupplier condition;

        LazyModifyInputMap(BooleanSupplier condition2, Object baseInputMap2, Object[] bindings2) {
            this.condition = condition2;
            this.baseInputMap = baseInputMap2;
            this.bindings = bindings2;
        }

        public Object createValue(UIDefaults table) {
            InputMap inputMap = this.baseInputMap instanceof UIDefaults.LazyValue ? (InputMap) ((UIDefaults.LazyValue) this.baseInputMap).createValue(table) : (InputMap) this.baseInputMap;
            if (this.condition == null || this.condition.getAsBoolean()) {
                for (int i = 0; i < this.bindings.length; i += 2) {
                    KeyStroke keyStroke = KeyStroke.getKeyStroke((String) this.bindings[i]);
                    if (this.bindings[i + 1] != null) {
                        inputMap.put(keyStroke, this.bindings[i + 1]);
                    } else {
                        inputMap.remove(keyStroke);
                    }
                }
            }
            return inputMap;
        }
    }
}
