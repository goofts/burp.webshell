package com.kitfox.svg.xml;

import com.kitfox.svg.SVGConst;
import java.awt.Color;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StyleAttribute implements Serializable {
    static final Matcher matchFpNumUnits = Pattern.compile("\\s*([-+]?((\\d*\\.\\d+)|(\\d+))([-+]?[eE]\\d+)?)\\s*(px|cm|mm|in|pc|pt|em|ex)\\s*").matcher("");
    static final Pattern patternUrl = Pattern.compile("\\s*url\\((.*)\\)\\s*");
    public static final long serialVersionUID = 0;
    boolean colorCompatable;
    String name;
    String stringValue;
    boolean urlCompatable;

    public StyleAttribute() {
        this(null, null);
    }

    public StyleAttribute(String name2) {
        this.colorCompatable = false;
        this.urlCompatable = false;
        this.name = name2;
        this.stringValue = null;
    }

    public StyleAttribute(String name2, String stringValue2) {
        this.colorCompatable = false;
        this.urlCompatable = false;
        this.name = name2;
        this.stringValue = stringValue2;
    }

    public String getName() {
        return this.name;
    }

    public StyleAttribute setName(String name2) {
        this.name = name2;
        return this;
    }

    public String getStringValue() {
        return this.stringValue;
    }

    public String[] getStringList() {
        return XMLParseUtil.parseStringList(this.stringValue);
    }

    public void setStringValue(String value) {
        this.stringValue = value;
    }

    public boolean getBooleanValue() {
        return this.stringValue.toLowerCase().equals("true");
    }

    public int getIntValue() {
        return XMLParseUtil.findInt(this.stringValue);
    }

    public int[] getIntList() {
        return XMLParseUtil.parseIntList(this.stringValue);
    }

    public double getDoubleValue() {
        return XMLParseUtil.findDouble(this.stringValue);
    }

    public double[] getDoubleList() {
        return XMLParseUtil.parseDoubleList(this.stringValue);
    }

    public float getFloatValue() {
        return XMLParseUtil.findFloat(this.stringValue);
    }

    public float[] getFloatList() {
        return XMLParseUtil.parseFloatList(this.stringValue);
    }

    public float getRatioValue() {
        return (float) XMLParseUtil.parseRatio(this.stringValue);
    }

    public String getUnits() {
        matchFpNumUnits.reset(this.stringValue);
        if (!matchFpNumUnits.matches()) {
            return null;
        }
        return matchFpNumUnits.group(6);
    }

    public NumberWithUnits getNumberWithUnits() {
        return XMLParseUtil.parseNumberWithUnits(this.stringValue);
    }

    public float getFloatValueWithUnits() {
        NumberWithUnits number = getNumberWithUnits();
        return convertUnitsToPixels(number.getUnits(), number.getValue());
    }

    public static float convertUnitsToPixels(int unitType, float value) {
        float pixPerInch;
        if (unitType == 0 || unitType == 9) {
            return value;
        }
        try {
            pixPerInch = (float) Toolkit.getDefaultToolkit().getScreenResolution();
        } catch (HeadlessException e) {
            pixPerInch = 72.0f;
        }
        switch (unitType) {
            case 2:
                return value * 0.3936f * pixPerInch;
            case 3:
                return 0.1f * value * 0.3936f * pixPerInch;
            case 4:
                return value * pixPerInch;
            case 5:
            case 6:
            default:
                return value;
            case 7:
                return 0.013888889f * value * pixPerInch;
            case 8:
                return 0.16666667f * value * pixPerInch;
        }
    }

    public Color getColorValue() {
        return ColorTable.parseColor(this.stringValue);
    }

    public String parseURLFn() {
        Matcher matchUrl = patternUrl.matcher(this.stringValue);
        if (!matchUrl.matches()) {
            return null;
        }
        return matchUrl.group(1);
    }

    public URL getURLValue(URL docRoot) {
        String fragment = parseURLFn();
        if (fragment == null) {
            return null;
        }
        try {
            return new URL(docRoot, fragment);
        } catch (Exception e) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, (String) null, (Throwable) e);
            return null;
        }
    }

    public URL getURLValue(URI docRoot) {
        String fragment = parseURLFn();
        if (fragment == null) {
            return null;
        }
        try {
            return docRoot.resolve(fragment).toURL();
        } catch (Exception e) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, (String) null, (Throwable) e);
            return null;
        }
    }

    public URI getURIValue() {
        return getURIValue(null);
    }

    public URI getURIValue(URI base) {
        URI relUri;
        try {
            String fragment = parseURLFn();
            if (fragment == null) {
                fragment = this.stringValue.replaceAll("\\s+", "");
            }
            if (fragment == null) {
                return null;
            }
            if (Pattern.matches("[a-zA-Z]:!\\\\.*", fragment)) {
                return new File(fragment).toURI();
            }
            URI uriFrag = new URI(fragment);
            if (uriFrag.isAbsolute() || base == null) {
                return uriFrag;
            }
            URI relBase = new URI(null, base.getSchemeSpecificPart(), null);
            if (relBase.isOpaque()) {
                relUri = new URI(null, base.getSchemeSpecificPart(), uriFrag.getFragment());
            } else {
                relUri = relBase.resolve(uriFrag);
            }
            return new URI(base.getScheme() + ":" + relUri);
        } catch (Exception e) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, (String) null, (Throwable) e);
            return null;
        }
    }

    public static void main(String[] args) {
        try {
            System.err.println(new URI("jar:http://www.kitfox.com/jackal/jackal.jar!/res/doc/about.svg").resolve("#myFragment").toString());
            System.err.println(new URI("http://www.kitfox.com/jackal/jackal.html").resolve("#myFragment").toString());
        } catch (Exception e) {
            Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, (String) null, (Throwable) e);
        }
    }
}
