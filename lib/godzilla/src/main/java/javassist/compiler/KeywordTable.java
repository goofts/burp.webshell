package javassist.compiler;

import java.util.HashMap;

public final class KeywordTable extends HashMap<String, Integer> {
    private static final long serialVersionUID = 1;

    public int lookup(String name) {
        if (containsKey(name)) {
            return ((Integer) get(name)).intValue();
        }
        return -1;
    }

    public void append(String name, int t) {
        put(name, Integer.valueOf(t));
    }
}
