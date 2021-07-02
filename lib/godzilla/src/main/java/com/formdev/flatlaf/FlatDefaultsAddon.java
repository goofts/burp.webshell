package com.formdev.flatlaf;

import java.io.InputStream;
import javax.swing.LookAndFeel;
import javax.swing.UIDefaults;

public abstract class FlatDefaultsAddon {
    public InputStream getDefaults(Class<?> lafClass) {
        Class<?> addonClass = getClass();
        return addonClass.getResourceAsStream('/' + addonClass.getPackage().getName().replace('.', '/') + '/' + lafClass.getSimpleName() + ".properties");
    }

    public void afterDefaultsLoading(LookAndFeel laf, UIDefaults defaults) {
    }

    public int getPriority() {
        return 10000;
    }
}
