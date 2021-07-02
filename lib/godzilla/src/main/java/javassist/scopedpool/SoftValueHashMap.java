package javassist.scopedpool;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class SoftValueHashMap<K, V> implements Map<K, V> {
    private Map<K, SoftValueRef<K, V>> hash;
    private ReferenceQueue<V> queue;

    /* access modifiers changed from: private */
    public static class SoftValueRef<K, V> extends SoftReference<V> {
        public K key;

        private SoftValueRef(K key2, V val, ReferenceQueue<V> q) {
            super(val, q);
            this.key = key2;
        }

        /* access modifiers changed from: private */
        public static <K, V> SoftValueRef<K, V> create(K key2, V val, ReferenceQueue<V> q) {
            if (val == null) {
                return null;
            }
            return new SoftValueRef<>(key2, val, q);
        }
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        processQueue();
        Set<Map.Entry<K, V>> ret = new HashSet<>();
        for (Map.Entry<K, SoftValueRef<K, V>> e : this.hash.entrySet()) {
            ret.add(new AbstractMap.SimpleImmutableEntry<>(e.getKey(), e.getValue().get()));
        }
        return ret;
    }

    private void processQueue() {
        if (!this.hash.isEmpty()) {
            while (true) {
                Reference ref = this.queue.poll();
                if (ref == null) {
                    return;
                }
                if (ref instanceof SoftValueRef) {
                    SoftValueRef que = (SoftValueRef) ref;
                    if (ref == this.hash.get(que.key)) {
                        this.hash.remove(que.key);
                    }
                }
            }
        }
    }

    public SoftValueHashMap(int initialCapacity, float loadFactor) {
        this.queue = new ReferenceQueue<>();
        this.hash = new ConcurrentHashMap(initialCapacity, loadFactor);
    }

    public SoftValueHashMap(int initialCapacity) {
        this.queue = new ReferenceQueue<>();
        this.hash = new ConcurrentHashMap(initialCapacity);
    }

    public SoftValueHashMap() {
        this.queue = new ReferenceQueue<>();
        this.hash = new ConcurrentHashMap();
    }

    public SoftValueHashMap(Map<K, V> t) {
        this(Math.max(t.size() * 2, 11), 0.75f);
        putAll(t);
    }

    public int size() {
        processQueue();
        return this.hash.size();
    }

    public boolean isEmpty() {
        processQueue();
        return this.hash.isEmpty();
    }

    public boolean containsKey(Object key) {
        processQueue();
        return this.hash.containsKey(key);
    }

    @Override // java.util.Map
    public V get(Object key) {
        processQueue();
        return valueOrNull(this.hash.get(key));
    }

    @Override // java.util.Map
    public V put(K key, V value) {
        processQueue();
        return valueOrNull(this.hash.put(key, SoftValueRef.create(key, value, this.queue)));
    }

    @Override // java.util.Map
    public V remove(Object key) {
        processQueue();
        return valueOrNull(this.hash.remove(key));
    }

    public void clear() {
        processQueue();
        this.hash.clear();
    }

    public boolean containsValue(Object arg0) {
        processQueue();
        if (arg0 == null) {
            return false;
        }
        for (SoftValueRef<K, V> e : this.hash.values()) {
            if (e != null && arg0.equals(e.get())) {
                return true;
            }
        }
        return false;
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        processQueue();
        return this.hash.keySet();
    }

    /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: javassist.scopedpool.SoftValueHashMap<K, V> */
    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> arg0) {
        processQueue();
        for (Object obj : arg0.keySet()) {
            put(obj, arg0.get(obj));
        }
    }

    @Override // java.util.Map
    public Collection<V> values() {
        processQueue();
        ArrayList arrayList = new ArrayList();
        for (SoftValueRef<K, V> e : this.hash.values()) {
            arrayList.add(e.get());
        }
        return arrayList;
    }

    private V valueOrNull(SoftValueRef<K, V> rtn) {
        if (rtn == null) {
            return null;
        }
        return (V) rtn.get();
    }
}
