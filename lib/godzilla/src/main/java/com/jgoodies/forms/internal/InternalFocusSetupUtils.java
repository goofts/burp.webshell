package com.jgoodies.forms.internal;

import com.jgoodies.common.base.Preconditions;
import com.jgoodies.forms.util.FocusTraversalType;
import java.awt.Component;
import java.awt.ContainerOrderFocusTraversalPolicy;
import java.awt.FocusTraversalPolicy;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import javax.swing.JComponent;
import javax.swing.LayoutFocusTraversalPolicy;

public final class InternalFocusSetupUtils {
    private static final String JGContainerOrderFocusTraversalPolicy_NAME = "com.jgoodies.jsdl.common.focus.JGContainerOrderFocusTraversalPolicy";
    private static final String JGLayoutFocusTraversalPolicy_NAME = "com.jgoodies.jsdl.common.focus.JGLayoutFocusTraversalPolicy";
    private static Constructor<FocusTraversalPolicy> containerOrderFTPConstructor;
    private static Constructor<FocusTraversalPolicy> layoutFTPConstructor;

    static {
        containerOrderFTPConstructor = null;
        layoutFTPConstructor = null;
        containerOrderFTPConstructor = getContainerOrderFTPConstructor();
        layoutFTPConstructor = getLayoutFTPConstructor();
    }

    private InternalFocusSetupUtils() {
    }

    public static void checkValidFocusTraversalSetup(FocusTraversalPolicy policy, FocusTraversalType type, Component initialComponent) {
        Preconditions.checkState((policy != null && type == null && initialComponent == null) || policy == null, "Either use #focusTraversalPolicy or #focusTraversalType plus optional #initialComponent); don't mix them.");
    }

    public static void setupFocusTraversalPolicyAndProvider(JComponent container, FocusTraversalPolicy policy, FocusTraversalType type, Component initialComponent) {
        container.setFocusTraversalPolicy(getOrCreateFocusTraversalPolicy(policy, type, initialComponent));
        container.setFocusTraversalPolicyProvider(true);
    }

    public static FocusTraversalPolicy getOrCreateFocusTraversalPolicy(FocusTraversalPolicy policy, FocusTraversalType type, Component initialComponent) {
        if (policy != null) {
            return policy;
        }
        if (type == FocusTraversalType.CONTAINER_ORDER) {
            return createContainerOrderFocusTraversalPolicy(initialComponent);
        }
        return createLayoutFocusTraversalPolicy(initialComponent);
    }

    private static FocusTraversalPolicy createContainerOrderFocusTraversalPolicy(Component initialComponent) {
        if (containerOrderFTPConstructor != null) {
            try {
                return containerOrderFTPConstructor.newInstance(initialComponent);
            } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException e) {
            }
        }
        return new ContainerOrderFocusTraversalPolicy();
    }

    private static FocusTraversalPolicy createLayoutFocusTraversalPolicy(Component initialComponent) {
        if (layoutFTPConstructor != null) {
            try {
                return layoutFTPConstructor.newInstance(initialComponent);
            } catch (IllegalAccessException | IllegalArgumentException | InstantiationException | InvocationTargetException e) {
            }
        }
        return new LayoutFocusTraversalPolicy();
    }

    private static Constructor<FocusTraversalPolicy> getContainerOrderFTPConstructor() {
        try {
            return Class.forName(JGContainerOrderFocusTraversalPolicy_NAME).getConstructor(Component.class);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
            return null;
        }
    }

    private static Constructor<FocusTraversalPolicy> getLayoutFTPConstructor() {
        try {
            return Class.forName(JGLayoutFocusTraversalPolicy_NAME).getConstructor(Component.class);
        } catch (ClassNotFoundException | NoSuchMethodException | SecurityException e) {
            return null;
        }
    }
}
