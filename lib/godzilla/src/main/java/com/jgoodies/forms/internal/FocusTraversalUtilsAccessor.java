package com.jgoodies.forms.internal;

import com.kitfox.svg.Group;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import javax.swing.AbstractButton;

public final class FocusTraversalUtilsAccessor {
    private static final String FOCUS_TRAVERSAL_UTILS_NAME = "com.jgoodies.jsdl.common.focus.FocusTraversalUtils";
    private static Method groupMethod;

    static {
        groupMethod = null;
        groupMethod = getGroupMethod();
    }

    private FocusTraversalUtilsAccessor() {
    }

    public static void tryToBuildAFocusGroup(AbstractButton... buttons) {
        if (groupMethod != null) {
            try {
                groupMethod.invoke(null, buttons);
            } catch (IllegalAccessException | InvocationTargetException e) {
            }
        }
    }

    private static Method getGroupMethod() {
        try {
            return Class.forName(FOCUS_TRAVERSAL_UTILS_NAME).getMethod(Group.TAG_NAME, AbstractButton[].class);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
            return null;
        }
    }
}
