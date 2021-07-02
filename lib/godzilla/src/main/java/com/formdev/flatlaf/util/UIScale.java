package com.formdev.flatlaf.util;

import com.formdev.flatlaf.FlatSystemProperties;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.plaf.DimensionUIResource;
import javax.swing.plaf.FontUIResource;
import javax.swing.plaf.InsetsUIResource;
import javax.swing.plaf.UIResource;

public class UIScale {
    private static final boolean DEBUG = false;
    private static PropertyChangeSupport changeSupport;
    private static boolean initialized;
    private static Boolean jreHiDPI;
    private static float scaleFactor = 1.0f;

    public static void addPropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport == null) {
            changeSupport = new PropertyChangeSupport(UIScale.class);
        }
        changeSupport.addPropertyChangeListener(listener);
    }

    public static void removePropertyChangeListener(PropertyChangeListener listener) {
        if (changeSupport != null) {
            changeSupport.removePropertyChangeListener(listener);
        }
    }

    public static boolean isSystemScalingEnabled() {
        if (jreHiDPI != null) {
            return jreHiDPI.booleanValue();
        }
        jreHiDPI = false;
        if (SystemInfo.isJava_9_orLater) {
            jreHiDPI = true;
        } else if (SystemInfo.isJetBrainsJVM) {
            try {
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                Class<?> sunGeClass = Class.forName("sun.java2d.SunGraphicsEnvironment");
                if (sunGeClass.isInstance(ge)) {
                    jreHiDPI = (Boolean) sunGeClass.getDeclaredMethod("isUIScaleOn", new Class[0]).invoke(ge, new Object[0]);
                }
            } catch (Throwable th) {
            }
        }
        return jreHiDPI.booleanValue();
    }

    public static double getSystemScaleFactor(Graphics2D g) {
        if (isSystemScalingEnabled()) {
            return getSystemScaleFactor(g.getDeviceConfiguration());
        }
        return 1.0d;
    }

    public static double getSystemScaleFactor(GraphicsConfiguration gc) {
        if (!isSystemScalingEnabled() || gc == null) {
            return 1.0d;
        }
        return gc.getDefaultTransform().getScaleX();
    }

    private static void initialize() {
        if (!initialized) {
            initialized = true;
            if (isUserScalingEnabled()) {
                PropertyChangeListener listener = new PropertyChangeListener() {
                    /* class com.formdev.flatlaf.util.UIScale.AnonymousClass1 */

                    public void propertyChange(PropertyChangeEvent e) {
                        String propertyName = e.getPropertyName();
                        char c = 65535;
                        switch (propertyName.hashCode()) {
                            case -1595277186:
                                if (propertyName.equals("lookAndFeel")) {
                                    c = 0;
                                    break;
                                }
                                break;
                            case -437367248:
                                if (propertyName.equals("defaultFont")) {
                                    c = 1;
                                    break;
                                }
                                break;
                            case 298310441:
                                if (propertyName.equals("Label.font")) {
                                    c = 2;
                                    break;
                                }
                                break;
                        }
                        switch (c) {
                            case 0:
                                if (e.getNewValue() instanceof LookAndFeel) {
                                    UIManager.getLookAndFeelDefaults().addPropertyChangeListener(this);
                                }
                                UIScale.updateScaleFactor();
                                return;
                            case 1:
                            case 2:
                                UIScale.updateScaleFactor();
                                return;
                            default:
                                return;
                        }
                    }
                };
                UIManager.addPropertyChangeListener(listener);
                UIManager.getDefaults().addPropertyChangeListener(listener);
                UIManager.getLookAndFeelDefaults().addPropertyChangeListener(listener);
                updateScaleFactor();
            }
        }
    }

    /* access modifiers changed from: private */
    public static void updateScaleFactor() {
        float newScaleFactor;
        if (isUserScalingEnabled()) {
            float customScaleFactor = getCustomScaleFactor();
            if (customScaleFactor > 0.0f) {
                setUserScaleFactor(customScaleFactor);
                return;
            }
            Font font = UIManager.getFont("defaultFont");
            if (font == null) {
                font = UIManager.getFont("Label.font");
            }
            if (!SystemInfo.isWindows) {
                newScaleFactor = computeScaleFactor(font);
            } else if (!(font instanceof UIResource)) {
                newScaleFactor = computeScaleFactor(font);
            } else if (isSystemScalingEnabled()) {
                newScaleFactor = 1.0f;
            } else {
                Font winFont = (Font) Toolkit.getDefaultToolkit().getDesktopProperty("win.defaultGUI.font");
                if (winFont == null) {
                    winFont = font;
                }
                newScaleFactor = computeScaleFactor(winFont);
            }
            setUserScaleFactor(newScaleFactor);
        }
    }

    private static float computeScaleFactor(Font font) {
        float fontSizeDivider = 12.0f;
        if (SystemInfo.isWindows) {
            if ("Tahoma".equals(font.getFamily())) {
                fontSizeDivider = 11.0f;
            }
        } else if (SystemInfo.isMacOS) {
            fontSizeDivider = 13.0f;
        } else if (SystemInfo.isLinux) {
            fontSizeDivider = SystemInfo.isKDE ? 13.0f : 15.0f;
        }
        return ((float) font.getSize()) / fontSizeDivider;
    }

    private static boolean isUserScalingEnabled() {
        return FlatSystemProperties.getBoolean(FlatSystemProperties.UI_SCALE_ENABLED, true);
    }

    public static FontUIResource applyCustomScaleFactor(FontUIResource font) {
        if (!isUserScalingEnabled()) {
            return font;
        }
        float scaleFactor2 = getCustomScaleFactor();
        if (scaleFactor2 <= 0.0f) {
            return font;
        }
        float fontScaleFactor = computeScaleFactor(font);
        return scaleFactor2 != fontScaleFactor ? new FontUIResource(font.deriveFont((float) Math.round((((float) font.getSize()) / fontScaleFactor) * scaleFactor2))) : font;
    }

    private static float getCustomScaleFactor() {
        return parseScaleFactor(System.getProperty(FlatSystemProperties.UI_SCALE));
    }

    private static float parseScaleFactor(String s) {
        if (s == null) {
            return -1.0f;
        }
        float units = 1.0f;
        if (s.endsWith("x")) {
            s = s.substring(0, s.length() - 1);
        } else if (s.endsWith("dpi")) {
            units = 96.0f;
            s = s.substring(0, s.length() - 3);
        } else if (s.endsWith("%")) {
            units = 100.0f;
            s = s.substring(0, s.length() - 1);
        }
        try {
            float scale = Float.parseFloat(s);
            if (scale > 0.0f) {
                return scale / units;
            }
            return -1.0f;
        } catch (NumberFormatException e) {
            return -1.0f;
        }
    }

    public static float getUserScaleFactor() {
        initialize();
        return scaleFactor;
    }

    private static void setUserScaleFactor(float scaleFactor2) {
        float scaleFactor3;
        if (scaleFactor2 <= 1.0f) {
            scaleFactor3 = 1.0f;
        } else {
            scaleFactor3 = ((float) Math.round(scaleFactor2 * 4.0f)) / 4.0f;
        }
        float oldScaleFactor = scaleFactor;
        scaleFactor = scaleFactor3;
        if (changeSupport != null) {
            changeSupport.firePropertyChange("userScaleFactor", Float.valueOf(oldScaleFactor), Float.valueOf(scaleFactor3));
        }
    }

    public static float scale(float value) {
        initialize();
        return scaleFactor == 1.0f ? value : value * scaleFactor;
    }

    public static int scale(int value) {
        initialize();
        return scaleFactor == 1.0f ? value : Math.round(((float) value) * scaleFactor);
    }

    public static int scale2(int value) {
        initialize();
        return scaleFactor == 1.0f ? value : (int) (((float) value) * scaleFactor);
    }

    public static float unscale(float value) {
        initialize();
        return scaleFactor == 1.0f ? value : value / scaleFactor;
    }

    public static int unscale(int value) {
        initialize();
        return scaleFactor == 1.0f ? value : Math.round(((float) value) / scaleFactor);
    }

    public static void scaleGraphics(Graphics2D g) {
        initialize();
        if (scaleFactor != 1.0f) {
            g.scale((double) scaleFactor, (double) scaleFactor);
        }
    }

    public static Dimension scale(Dimension dimension) {
        initialize();
        if (dimension == null || scaleFactor == 1.0f) {
            return dimension;
        }
        if (dimension instanceof UIResource) {
            return new DimensionUIResource(scale(dimension.width), scale(dimension.height));
        }
        return new Dimension(scale(dimension.width), scale(dimension.height));
    }

    public static Insets scale(Insets insets) {
        initialize();
        if (insets == null || scaleFactor == 1.0f) {
            return insets;
        }
        if (insets instanceof UIResource) {
            return new InsetsUIResource(scale(insets.top), scale(insets.left), scale(insets.bottom), scale(insets.right));
        }
        return new Insets(scale(insets.top), scale(insets.left), scale(insets.bottom), scale(insets.right));
    }
}
