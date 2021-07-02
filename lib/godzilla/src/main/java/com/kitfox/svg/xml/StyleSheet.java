package com.kitfox.svg.xml;

import com.kitfox.svg.SVGConst;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StyleSheet {
    HashMap<StyleSheetRule, String> ruleMap = new HashMap<>();

    public static StyleSheet parseSheet(String src) {
        Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "CSS parser not implemented yet");
        return null;
    }

    public void addStyleRule(StyleSheetRule rule, String value) {
        this.ruleMap.put(rule, value);
    }

    public boolean getStyle(StyleAttribute attrib, String tagName, String cssClass) {
        String value = this.ruleMap.get(new StyleSheetRule(attrib.getName(), tagName, cssClass));
        if (value != null) {
            attrib.setStringValue(value);
            return true;
        }
        String value2 = this.ruleMap.get(new StyleSheetRule(attrib.getName(), null, cssClass));
        if (value2 != null) {
            attrib.setStringValue(value2);
            return true;
        }
        String value3 = this.ruleMap.get(new StyleSheetRule(attrib.getName(), tagName, null));
        if (value3 == null) {
            return false;
        }
        attrib.setStringValue(value3);
        return true;
    }
}
