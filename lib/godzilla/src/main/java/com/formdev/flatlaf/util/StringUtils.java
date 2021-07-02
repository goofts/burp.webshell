package com.formdev.flatlaf.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {
    public static boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static String removeLeading(String string, String leading) {
        if (string.startsWith(leading)) {
            return string.substring(leading.length());
        }
        return string;
    }

    public static String removeTrailing(String string, String trailing) {
        if (string.endsWith(trailing)) {
            return string.substring(0, string.length() - trailing.length());
        }
        return string;
    }

    public static List<String> split(String str, char delim) {
        ArrayList<String> strs = new ArrayList<>();
        int delimIndex = str.indexOf(delim);
        int index = 0;
        while (delimIndex >= 0) {
            strs.add(str.substring(index, delimIndex));
            index = delimIndex + 1;
            delimIndex = str.indexOf(delim, index);
        }
        strs.add(str.substring(index));
        return strs;
    }
}
