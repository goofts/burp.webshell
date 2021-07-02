package net.miginfocom.layout;

import java.util.HashMap;
import java.util.WeakHashMap;

public final class LinkHandler {
    public static final int HEIGHT = 3;
    private static final WeakHashMap<Object, HashMap<String, int[]>[]> LAYOUTS = new WeakHashMap<>();
    private static final int VALUES = 0;
    private static final int VALUES_TEMP = 1;
    public static final int WIDTH = 2;
    public static final int X = 0;
    public static final int X2 = 4;
    public static final int Y = 1;
    public static final int Y2 = 5;

    private LinkHandler() {
    }

    public static synchronized Integer getValue(Object layout, String key, int type) {
        Integer ret;
        synchronized (LinkHandler.class) {
            ret = null;
            HashMap<String, int[]>[] layoutValues = LAYOUTS.get(layout);
            if (layoutValues != null) {
                int[] rect = layoutValues[1].get(key);
                if (rect == null || rect[type] == -2147471302) {
                    int[] rect2 = layoutValues[0].get(key);
                    ret = (rect2 == null || rect2[type] == -2147471302) ? null : Integer.valueOf(rect2[type]);
                } else {
                    ret = Integer.valueOf(rect[type]);
                }
            }
        }
        return ret;
    }

    public static synchronized boolean setBounds(Object layout, String key, int x, int y, int width, int height) {
        boolean bounds;
        synchronized (LinkHandler.class) {
            bounds = setBounds(layout, key, x, y, width, height, false, false);
        }
        return bounds;
    }

    static synchronized boolean setBounds(Object layout, String key, int x, int y, int width, int height, boolean temporary, boolean incCur) {
        boolean changed;
        synchronized (LinkHandler.class) {
            HashMap<String, int[]>[] layoutValues = LAYOUTS.get(layout);
            if (layoutValues != null) {
                HashMap<String, int[]> map = layoutValues[temporary ? (char) 1 : 0];
                int[] old = map.get(key);
                if (old != null && old[0] == x && old[1] == y && old[2] == width && old[3] == height) {
                    changed = false;
                } else if (old == null || !incCur) {
                    map.put(key, new int[]{x, y, width, height, x + width, y + height});
                    changed = true;
                } else {
                    changed = false;
                    if (x != -2147471302) {
                        if (old[0] == -2147471302 || x < old[0]) {
                            old[0] = x;
                            old[2] = old[4] - x;
                            changed = true;
                        }
                        if (width != -2147471302) {
                            int x2 = x + width;
                            if (old[4] == -2147471302 || x2 > old[4]) {
                                old[4] = x2;
                                old[2] = x2 - old[0];
                                changed = true;
                            }
                        }
                    }
                    if (y != -2147471302) {
                        if (old[1] == -2147471302 || y < old[1]) {
                            old[1] = y;
                            old[3] = old[5] - y;
                            changed = true;
                        }
                        if (height != -2147471302) {
                            int y2 = y + height;
                            if (old[5] == -2147471302 || y2 > old[5]) {
                                old[5] = y2;
                                old[3] = y2 - old[1];
                                changed = true;
                            }
                        }
                    }
                }
            } else {
                int[] bounds = {x, y, width, height, x + width, y + height};
                HashMap<String, int[]> values_temp = new HashMap<>(4);
                if (temporary) {
                    values_temp.put(key, bounds);
                }
                HashMap<String, int[]> values = new HashMap<>(4);
                if (!temporary) {
                    values.put(key, bounds);
                }
                LAYOUTS.put(layout, new HashMap[]{values, values_temp});
                changed = true;
            }
        }
        return changed;
    }

    public static synchronized void clearWeakReferencesNow() {
        synchronized (LinkHandler.class) {
            LAYOUTS.clear();
        }
    }

    public static synchronized boolean clearBounds(Object layout, String key) {
        boolean z = false;
        synchronized (LinkHandler.class) {
            HashMap<String, int[]>[] layoutValues = LAYOUTS.get(layout);
            if (!(layoutValues == null || layoutValues[0].remove(key) == null)) {
                z = true;
            }
        }
        return z;
    }

    static synchronized void clearTemporaryBounds(Object layout) {
        synchronized (LinkHandler.class) {
            HashMap<String, int[]>[] layoutValues = LAYOUTS.get(layout);
            if (layoutValues != null) {
                layoutValues[1].clear();
            }
        }
    }
}
