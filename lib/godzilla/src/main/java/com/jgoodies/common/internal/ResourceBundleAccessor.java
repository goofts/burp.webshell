package com.jgoodies.common.internal;

import com.jgoodies.common.base.Preconditions;
import com.jgoodies.common.base.Strings;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.Icon;

public final class ResourceBundleAccessor implements StringAndIconResourceAccessor {
    private final ResourceBundle bundle;

    public ResourceBundleAccessor(ResourceBundle bundle2) {
        this.bundle = (ResourceBundle) Preconditions.checkNotNull(bundle2, Messages.MUST_NOT_BE_NULL, "resource bundle");
    }

    @Override // com.jgoodies.common.internal.IconResourceAccessor
    public Icon getIcon(String key) {
        return (Icon) this.bundle.getObject(key);
    }

    @Override // com.jgoodies.common.internal.StringResourceAccessor
    public String getString(String key, Object... args) {
        try {
            return Strings.get(this.bundle.getString(key), args);
        } catch (MissingResourceException e) {
            return key;
        }
    }
}
