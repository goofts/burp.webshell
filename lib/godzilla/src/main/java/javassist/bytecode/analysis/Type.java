package javassist.bytecode.analysis;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class Type {
    public static final Type BOGUS = new Type(null, true);
    public static final Type BOOLEAN = new Type(CtClass.booleanType);
    public static final Type BYTE = new Type(CtClass.byteType);
    public static final Type CHAR = new Type(CtClass.charType);
    public static final Type CLONEABLE = lookupType("java.lang.Cloneable");
    public static final Type DOUBLE = new Type(CtClass.doubleType);
    public static final Type FLOAT = new Type(CtClass.floatType);
    public static final Type INTEGER = new Type(CtClass.intType);
    public static final Type LONG = new Type(CtClass.longType);
    public static final Type OBJECT = lookupType("java.lang.Object");
    public static final Type RETURN_ADDRESS = new Type(null, true);
    public static final Type SERIALIZABLE = lookupType("java.io.Serializable");
    public static final Type SHORT = new Type(CtClass.shortType);
    public static final Type THROWABLE = lookupType("java.lang.Throwable");
    public static final Type TOP = new Type(null, true);
    public static final Type UNINIT = new Type(null);
    public static final Type VOID = new Type(CtClass.voidType);
    private static final Map<CtClass, Type> prims = new IdentityHashMap();
    private final CtClass clazz;
    private final boolean special;

    static {
        prims.put(CtClass.doubleType, DOUBLE);
        prims.put(CtClass.longType, LONG);
        prims.put(CtClass.charType, CHAR);
        prims.put(CtClass.shortType, SHORT);
        prims.put(CtClass.intType, INTEGER);
        prims.put(CtClass.floatType, FLOAT);
        prims.put(CtClass.byteType, BYTE);
        prims.put(CtClass.booleanType, BOOLEAN);
        prims.put(CtClass.voidType, VOID);
    }

    public static Type get(CtClass clazz2) {
        Type type = prims.get(clazz2);
        return type != null ? type : new Type(clazz2);
    }

    private static Type lookupType(String name) {
        try {
            return new Type(ClassPool.getDefault().get(name));
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    Type(CtClass clazz2) {
        this(clazz2, false);
    }

    private Type(CtClass clazz2, boolean special2) {
        this.clazz = clazz2;
        this.special = special2;
    }

    /* access modifiers changed from: package-private */
    public boolean popChanged() {
        return false;
    }

    public int getSize() {
        return (this.clazz == CtClass.doubleType || this.clazz == CtClass.longType || this == TOP) ? 2 : 1;
    }

    public CtClass getCtClass() {
        return this.clazz;
    }

    public boolean isReference() {
        return !this.special && (this.clazz == null || !this.clazz.isPrimitive());
    }

    public boolean isSpecial() {
        return this.special;
    }

    public boolean isArray() {
        return this.clazz != null && this.clazz.isArray();
    }

    public int getDimensions() {
        if (!isArray()) {
            return 0;
        }
        String name = this.clazz.getName();
        int pos = name.length() - 1;
        int count = 0;
        while (name.charAt(pos) == ']') {
            pos -= 2;
            count++;
        }
        return count;
    }

    public Type getComponent() {
        if (this.clazz == null || !this.clazz.isArray()) {
            return null;
        }
        try {
            CtClass component = this.clazz.getComponentType();
            Type type = prims.get(component);
            return type == null ? new Type(component) : type;
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isAssignableFrom(Type type) {
        if (this == type) {
            return true;
        }
        if (type == UNINIT && isReference()) {
            return true;
        }
        if (this == UNINIT && type.isReference()) {
            return true;
        }
        if (type instanceof MultiType) {
            return ((MultiType) type).isAssignableTo(this);
        }
        if (type instanceof MultiArrayType) {
            return ((MultiArrayType) type).isAssignableTo(this);
        }
        if (this.clazz == null || this.clazz.isPrimitive()) {
            return false;
        }
        try {
            return type.clazz.subtypeOf(this.clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Type merge(Type type) {
        if (type == this || type == null || type == UNINIT) {
            return this;
        }
        if (this == UNINIT) {
            return type;
        }
        if (!type.isReference() || !isReference()) {
            return BOGUS;
        }
        if (type instanceof MultiType) {
            return type.merge(this);
        }
        if (type.isArray() && isArray()) {
            return mergeArray(type);
        }
        try {
            return mergeClasses(type);
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /* access modifiers changed from: package-private */
    public Type getRootComponent(Type type) {
        while (type.isArray()) {
            type = type.getComponent();
        }
        return type;
    }

    private Type createArray(Type rootComponent, int dims) {
        if (rootComponent instanceof MultiType) {
            return new MultiArrayType((MultiType) rootComponent, dims);
        }
        try {
            return get(getClassPool(rootComponent).get(arrayName(rootComponent.clazz.getName(), dims)));
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /* access modifiers changed from: package-private */
    public String arrayName(String component, int dims) {
        int i = component.length();
        int size = i + (dims * 2);
        char[] string = new char[size];
        component.getChars(0, i, string, 0);
        int i2 = i;
        while (i2 < size) {
            int i3 = i2 + 1;
            string[i2] = '[';
            i2 = i3 + 1;
            string[i3] = ']';
        }
        return new String(string);
    }

    private ClassPool getClassPool(Type rootComponent) {
        ClassPool pool = rootComponent.clazz.getClassPool();
        return pool != null ? pool : ClassPool.getDefault();
    }

    private Type mergeArray(Type type) {
        Type targetRoot;
        int targetDims;
        Type typeRoot = getRootComponent(type);
        Type thisRoot = getRootComponent(this);
        int typeDims = type.getDimensions();
        int thisDims = getDimensions();
        if (typeDims == thisDims) {
            Type mergedComponent = thisRoot.merge(typeRoot);
            if (mergedComponent == BOGUS) {
                return OBJECT;
            }
            return createArray(mergedComponent, thisDims);
        }
        if (typeDims < thisDims) {
            targetRoot = typeRoot;
            targetDims = typeDims;
        } else {
            targetRoot = thisRoot;
            targetDims = thisDims;
        }
        if (eq(CLONEABLE.clazz, targetRoot.clazz) || eq(SERIALIZABLE.clazz, targetRoot.clazz)) {
            return createArray(targetRoot, targetDims);
        }
        return createArray(OBJECT, targetDims);
    }

    private static CtClass findCommonSuperClass(CtClass one, CtClass two) throws NotFoundException {
        CtClass shallow;
        CtClass deep = one;
        CtClass shallow2 = two;
        CtClass backupDeep = deep;
        while (true) {
            if (eq(deep, shallow2) && deep.getSuperclass() != null) {
                return deep;
            }
            CtClass deepSuper = deep.getSuperclass();
            CtClass shallowSuper = shallow2.getSuperclass();
            if (shallowSuper == null) {
                shallow = shallow2;
                break;
            } else if (deepSuper == null) {
                backupDeep = shallow2;
                deep = shallow2;
                shallow = backupDeep;
                break;
            } else {
                deep = deepSuper;
                shallow2 = shallowSuper;
            }
        }
        while (true) {
            deep = deep.getSuperclass();
            if (deep == null) {
                break;
            }
            backupDeep = backupDeep.getSuperclass();
        }
        CtClass deep2 = backupDeep;
        while (!eq(deep2, shallow)) {
            deep2 = deep2.getSuperclass();
            shallow = shallow.getSuperclass();
        }
        return deep2;
    }

    private Type mergeClasses(Type type) throws NotFoundException {
        CtClass superClass = findCommonSuperClass(this.clazz, type.clazz);
        if (superClass.getSuperclass() == null) {
            Map<String, CtClass> interfaces = findCommonInterfaces(type);
            if (interfaces.size() == 1) {
                return new Type(interfaces.values().iterator().next());
            }
            if (interfaces.size() > 1) {
                return new MultiType(interfaces);
            }
            return new Type(superClass);
        }
        Map<String, CtClass> commonDeclared = findExclusiveDeclaredInterfaces(type, superClass);
        if (commonDeclared.size() > 0) {
            return new MultiType(commonDeclared, new Type(superClass));
        }
        return new Type(superClass);
    }

    private Map<String, CtClass> findCommonInterfaces(Type type) {
        return findCommonInterfaces(getAllInterfaces(type.clazz, null), getAllInterfaces(this.clazz, null));
    }

    private Map<String, CtClass> findExclusiveDeclaredInterfaces(Type type, CtClass exclude) {
        Map<String, CtClass> typeMap = getDeclaredInterfaces(type.clazz, null);
        Map<String, CtClass> thisMap = getDeclaredInterfaces(this.clazz, null);
        for (String intf : getAllInterfaces(exclude, null).keySet()) {
            typeMap.remove(intf);
            thisMap.remove(intf);
        }
        return findCommonInterfaces(typeMap, thisMap);
    }

    /* access modifiers changed from: package-private */
    public Map<String, CtClass> findCommonInterfaces(Map<String, CtClass> typeMap, Map<String, CtClass> alterMap) {
        if (alterMap == null) {
            alterMap = new HashMap<>();
        }
        if (typeMap == null || typeMap.isEmpty()) {
            alterMap.clear();
        }
        for (String name : alterMap.keySet()) {
            if (!typeMap.containsKey(name)) {
                alterMap.remove(name);
            }
        }
        for (CtClass intf : alterMap.values()) {
            try {
                for (CtClass c : intf.getInterfaces()) {
                    alterMap.remove(c.getName());
                }
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return alterMap;
    }

    /* access modifiers changed from: package-private */
    public Map<String, CtClass> getAllInterfaces(CtClass clazz2, Map<String, CtClass> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        if (clazz2.isInterface()) {
            map.put(clazz2.getName(), clazz2);
        }
        do {
            try {
                CtClass[] interfaces = clazz2.getInterfaces();
                for (CtClass intf : interfaces) {
                    map.put(intf.getName(), intf);
                    getAllInterfaces(intf, map);
                }
                clazz2 = clazz2.getSuperclass();
            } catch (NotFoundException e) {
                throw new RuntimeException(e);
            }
        } while (clazz2 != null);
        return map;
    }

    /* access modifiers changed from: package-private */
    public Map<String, CtClass> getDeclaredInterfaces(CtClass clazz2, Map<String, CtClass> map) {
        if (map == null) {
            map = new HashMap<>();
        }
        if (clazz2.isInterface()) {
            map.put(clazz2.getName(), clazz2);
        }
        try {
            CtClass[] interfaces = clazz2.getInterfaces();
            for (CtClass intf : interfaces) {
                map.put(intf.getName(), intf);
                getDeclaredInterfaces(intf, map);
            }
            return map;
        } catch (NotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public int hashCode() {
        return getClass().hashCode() + this.clazz.hashCode();
    }

    public boolean equals(Object o) {
        if ((o instanceof Type) && o.getClass() == getClass() && eq(this.clazz, ((Type) o).clazz)) {
            return true;
        }
        return false;
    }

    static boolean eq(CtClass one, CtClass two) {
        return one == two || !(one == null || two == null || !one.getName().equals(two.getName()));
    }

    public String toString() {
        if (this == BOGUS) {
            return "BOGUS";
        }
        if (this == UNINIT) {
            return "UNINIT";
        }
        if (this == RETURN_ADDRESS) {
            return "RETURN ADDRESS";
        }
        if (this == TOP) {
            return "TOP";
        }
        return this.clazz == null ? "null" : this.clazz.getName();
    }
}
