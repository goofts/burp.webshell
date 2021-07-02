package com.kitfox.svg.xml;

import com.kitfox.svg.SVGConst;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XMLParseUtil {
    static final Matcher fpMatch = Pattern.compile("([-+]?((\\d*\\.\\d+)|(\\d+))([eE][+-]?\\d+)?)(\\%|in|cm|mm|pt|pc|px|em|ex)?").matcher("");
    static final Matcher intMatch = Pattern.compile("[-+]?\\d+").matcher("");
    static final Matcher quoteMatch = Pattern.compile("^'|'$").matcher("");

    private XMLParseUtil() {
    }

    public static String getTagText(Element ele) {
        NodeList nl = ele.getChildNodes();
        int size = nl.getLength();
        Node node = null;
        int i = 0;
        while (i < size) {
            node = nl.item(i);
            if (node instanceof Text) {
                break;
            }
            i++;
        }
        if (i == size || node == null) {
            return null;
        }
        return ((Text) node).getData();
    }

    public static Element getFirstChild(Element root, String name) {
        NodeList nl = root.getChildNodes();
        int size = nl.getLength();
        for (int i = 0; i < size; i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                if (ele.getTagName().equals(name)) {
                    return ele;
                }
            }
        }
        return null;
    }

    public static String[] parseStringList(String list) {
        Matcher matchWs = Pattern.compile("[^\\s]+").matcher("");
        matchWs.reset(list);
        LinkedList<String> matchList = new LinkedList<>();
        while (matchWs.find()) {
            matchList.add(matchWs.group());
        }
        return (String[]) matchList.toArray(new String[matchList.size()]);
    }

    public static boolean isDouble(String val) {
        fpMatch.reset(val);
        return fpMatch.matches();
    }

    public static double parseDouble(String val) {
        return findDouble(val);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0017, code lost:
        if (com.kitfox.svg.xml.XMLParseUtil.fpMatch.find() != false) goto L_0x0019;
     */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0041  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0072  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static synchronized double findDouble(java.lang.String r13) {
        /*
        // Method dump skipped, instructions count: 191
        */
        throw new UnsupportedOperationException("Method not decompiled: com.kitfox.svg.xml.XMLParseUtil.findDouble(java.lang.String):double");
    }

    public static synchronized double[] parseDoubleList(String list) {
        double[] retArr;
        synchronized (XMLParseUtil.class) {
            if (list == null) {
                retArr = null;
            } else {
                fpMatch.reset(list);
                LinkedList<Double> doubList = new LinkedList<>();
                while (fpMatch.find()) {
                    doubList.add(Double.valueOf(fpMatch.group(1)));
                }
                retArr = new double[doubList.size()];
                Iterator<Double> it = doubList.iterator();
                int idx = 0;
                while (it.hasNext()) {
                    idx++;
                    retArr[idx] = it.next().doubleValue();
                }
            }
        }
        return retArr;
    }

    public static float parseFloat(String val) {
        return findFloat(val);
    }

    public static synchronized float findFloat(String val) {
        float retVal = 0.0f;
        synchronized (XMLParseUtil.class) {
            if (val != null) {
                fpMatch.reset(val);
                if (fpMatch.find()) {
                    retVal = 0.0f;
                    try {
                        retVal = Float.parseFloat(fpMatch.group(1));
                        if ("%".equals(fpMatch.group(6))) {
                            retVal /= 100.0f;
                        }
                    } catch (Exception e) {
                    }
                }
            }
        }
        return retVal;
    }

    public static synchronized float[] parseFloatList(String list) {
        float[] retArr;
        synchronized (XMLParseUtil.class) {
            if (list == null) {
                retArr = null;
            } else {
                fpMatch.reset(list);
                LinkedList<Float> floatList = new LinkedList<>();
                while (fpMatch.find()) {
                    floatList.add(Float.valueOf(fpMatch.group(1)));
                }
                retArr = new float[floatList.size()];
                Iterator<Float> it = floatList.iterator();
                int idx = 0;
                while (it.hasNext()) {
                    idx++;
                    retArr[idx] = it.next().floatValue();
                }
            }
        }
        return retArr;
    }

    public static int parseInt(String val) {
        if (val == null) {
            return 0;
        }
        try {
            return Integer.parseInt(val);
        } catch (Exception e) {
            return 0;
        }
    }

    public static int findInt(String val) {
        if (val == null) {
            return 0;
        }
        intMatch.reset(val);
        if (!intMatch.find()) {
            return 0;
        }
        try {
            return Integer.parseInt(intMatch.group());
        } catch (Exception e) {
            return 0;
        }
    }

    public static int[] parseIntList(String list) {
        if (list == null) {
            return null;
        }
        intMatch.reset(list);
        LinkedList<Integer> intList = new LinkedList<>();
        while (intMatch.find()) {
            intList.add(Integer.valueOf(intMatch.group()));
        }
        int[] retArr = new int[intList.size()];
        Iterator<Integer> it = intList.iterator();
        int idx = 0;
        while (it.hasNext()) {
            retArr[idx] = it.next().intValue();
            idx++;
        }
        return retArr;
    }

    public static double parseRatio(String val) {
        if (val == null || val.equals("")) {
            return 0.0d;
        }
        if (val.charAt(val.length() - 1) == '%') {
            parseDouble(val.substring(0, val.length() - 1));
        }
        return parseDouble(val);
    }

    public static NumberWithUnits parseNumberWithUnits(String val) {
        if (val == null) {
            return null;
        }
        return new NumberWithUnits(val);
    }

    public static String getAttribString(Element ele, String name) {
        return ele.getAttribute(name);
    }

    public static int getAttribInt(Element ele, String name) {
        try {
            return Integer.parseInt(ele.getAttribute(name));
        } catch (Exception e) {
            return 0;
        }
    }

    public static int getAttribIntHex(Element ele, String name) {
        try {
            return Integer.parseInt(ele.getAttribute(name), 16);
        } catch (Exception e) {
            return 0;
        }
    }

    public static float getAttribFloat(Element ele, String name) {
        try {
            return Float.parseFloat(ele.getAttribute(name));
        } catch (Exception e) {
            return 0.0f;
        }
    }

    public static double getAttribDouble(Element ele, String name) {
        try {
            return Double.parseDouble(ele.getAttribute(name));
        } catch (Exception e) {
            return 0.0d;
        }
    }

    public static boolean getAttribBoolean(Element ele, String name) {
        return ele.getAttribute(name).toLowerCase().equals("true");
    }

    public static URL getAttribURL(Element ele, String name, URL docRoot) {
        try {
            return new URL(docRoot, ele.getAttribute(name));
        } catch (Exception e) {
            return null;
        }
    }

    public static ReadableXMLElement getElement(Class<?> classType, Element root, String name, URL docRoot) {
        if (root == null) {
            return null;
        }
        if (!ReadableXMLElement.class.isAssignableFrom(classType)) {
            return null;
        }
        NodeList nl = root.getChildNodes();
        int size = nl.getLength();
        for (int i = 0; i < size; i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                if (ele.getTagName().equals(name)) {
                    try {
                        ReadableXMLElement newObj = (ReadableXMLElement) classType.newInstance();
                        newObj.read(ele, docRoot);
                        if (newObj != null) {
                            return newObj;
                        }
                    } catch (Exception e) {
                        Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, (String) null, (Throwable) e);
                    }
                } else {
                    continue;
                }
            }
        }
        return null;
    }

    public static HashMap<String, ReadableXMLElement> getElementHashMap(Class<?> classType, Element root, String name, String key, URL docRoot) {
        if (root == null || !ReadableXMLElement.class.isAssignableFrom(classType)) {
            return null;
        }
        HashMap<String, ReadableXMLElement> retMap = new HashMap<>();
        NodeList nl = root.getChildNodes();
        int size = nl.getLength();
        for (int i = 0; i < size; i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                if (ele.getTagName().equals(name)) {
                    try {
                        ReadableXMLElement newObj = (ReadableXMLElement) classType.newInstance();
                        newObj.read(ele, docRoot);
                        if (newObj != null) {
                            retMap.put(getAttribString(ele, key), newObj);
                        }
                    } catch (Exception e) {
                        Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, (String) null, (Throwable) e);
                    }
                }
            }
        }
        return retMap;
    }

    public static HashSet<ReadableXMLElement> getElementHashSet(Class<?> classType, Element root, String name, URL docRoot) {
        if (root == null) {
            return null;
        }
        if (!ReadableXMLElement.class.isAssignableFrom(classType)) {
            return null;
        }
        HashSet<ReadableXMLElement> retSet = new HashSet<>();
        NodeList nl = root.getChildNodes();
        int size = nl.getLength();
        for (int i = 0; i < size; i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                if (ele.getTagName().equals(name)) {
                    try {
                        ReadableXMLElement newObj = (ReadableXMLElement) classType.newInstance();
                        newObj.read(ele, docRoot);
                        if (newObj != null) {
                            retSet.add(newObj);
                        }
                    } catch (Exception e) {
                        Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, (String) null, (Throwable) e);
                    }
                }
            }
        }
        return retSet;
    }

    public static LinkedList<ReadableXMLElement> getElementLinkedList(Class<?> classType, Element root, String name, URL docRoot) {
        if (root == null) {
            return null;
        }
        if (!ReadableXMLElement.class.isAssignableFrom(classType)) {
            return null;
        }
        NodeList nl = root.getChildNodes();
        LinkedList<ReadableXMLElement> elementCache = new LinkedList<>();
        int size = nl.getLength();
        for (int i = 0; i < size; i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                if (ele.getTagName().equals(name)) {
                    try {
                        ReadableXMLElement newObj = (ReadableXMLElement) classType.newInstance();
                        newObj.read(ele, docRoot);
                        elementCache.addLast(newObj);
                    } catch (Exception e) {
                        Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, (String) null, (Throwable) e);
                    }
                }
            }
        }
        return elementCache;
    }

    public static Object[] getElementArray(Class<?> classType, Element root, String name, URL docRoot) {
        if (root == null || !ReadableXMLElement.class.isAssignableFrom(classType)) {
            return null;
        }
        LinkedList<ReadableXMLElement> elementCache = getElementLinkedList(classType, root, name, docRoot);
        return elementCache.toArray((Object[]) Array.newInstance(classType, elementCache.size()));
    }

    public static int[] getElementArrayInt(Element root, String name, String attrib) {
        if (root == null) {
            return null;
        }
        NodeList nl = root.getChildNodes();
        LinkedList<Integer> elementCache = new LinkedList<>();
        int size = nl.getLength();
        for (int i = 0; i < size; i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                if (ele.getTagName().equals(name)) {
                    int eleVal = 0;
                    try {
                        eleVal = Integer.parseInt(ele.getAttribute(attrib));
                    } catch (Exception e) {
                    }
                    elementCache.addLast(new Integer(eleVal));
                }
            }
        }
        int[] retArr = new int[elementCache.size()];
        Iterator<Integer> it = elementCache.iterator();
        int idx = 0;
        while (it.hasNext()) {
            retArr[idx] = it.next().intValue();
            idx++;
        }
        return retArr;
    }

    public static String[] getElementArrayString(Element root, String name, String attrib) {
        if (root == null) {
            return null;
        }
        NodeList nl = root.getChildNodes();
        LinkedList<String> elementCache = new LinkedList<>();
        int size = nl.getLength();
        for (int i = 0; i < size; i++) {
            Node node = nl.item(i);
            if (node instanceof Element) {
                Element ele = (Element) node;
                if (ele.getTagName().equals(name)) {
                    elementCache.addLast(ele.getAttribute(attrib));
                }
            }
        }
        String[] retArr = new String[elementCache.size()];
        Iterator<String> it = elementCache.iterator();
        int idx = 0;
        while (it.hasNext()) {
            retArr[idx] = it.next();
            idx++;
        }
        return retArr;
    }

    public static HashMap<String, StyleAttribute> parseStyle(String styleString) {
        return parseStyle(styleString, new HashMap());
    }

    public static HashMap<String, StyleAttribute> parseStyle(String styleString, HashMap<String, StyleAttribute> map) {
        int colon;
        String[] styles = Pattern.compile(";").split(styleString);
        for (int i = 0; i < styles.length; i++) {
            if (!(styles[i].length() == 0 || (colon = styles[i].indexOf(58)) == -1)) {
                String key = styles[i].substring(0, colon).trim().intern();
                map.put(key, new StyleAttribute(key, quoteMatch.reset(styles[i].substring(colon + 1).trim()).replaceAll("").intern()));
            }
        }
        return map;
    }
}
