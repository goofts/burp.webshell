package javassist.compiler;

import java.lang.ref.Reference;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.ClassFile;
import javassist.bytecode.Descriptor;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;
import javassist.compiler.ast.ASTList;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.Declarator;
import javassist.compiler.ast.Keyword;
import javassist.compiler.ast.Symbol;

public class MemberResolver implements TokenId {
    private static final String INVALID = "<invalid>";
    private static final int NO = -1;
    private static final int YES = 0;
    private static Map<ClassPool, Reference<Map<String, String>>> invalidNamesMap = new WeakHashMap();
    private ClassPool classPool;
    private Map<String, String> invalidNames = null;

    public MemberResolver(ClassPool cp) {
        this.classPool = cp;
    }

    public ClassPool getClassPool() {
        return this.classPool;
    }

    private static void fatal() throws CompileError {
        throw new CompileError("fatal");
    }

    public static class Method {
        public CtClass declaring;
        public MethodInfo info;
        public int notmatch;

        public Method(CtClass c, MethodInfo i, int n) {
            this.declaring = c;
            this.info = i;
            this.notmatch = n;
        }

        public boolean isStatic() {
            return (this.info.getAccessFlags() & 8) != 0;
        }
    }

    public Method lookupMethod(CtClass clazz, CtClass currentClass, MethodInfo current, String methodName, int[] argTypes, int[] argDims, String[] argClassNames) throws CompileError {
        Method maybe;
        int res;
        if (current == null || clazz != currentClass || !current.getName().equals(methodName) || (res = compareSignature(current.getDescriptor(), argTypes, argDims, argClassNames)) == -1) {
            maybe = null;
        } else {
            Method r = new Method(clazz, current, res);
            if (res == 0) {
                return r;
            }
            maybe = r;
        }
        Method m = lookupMethod(clazz, methodName, argTypes, argDims, argClassNames, maybe != null);
        return m != null ? m : maybe;
    }

    private Method lookupMethod(CtClass clazz, String methodName, int[] argTypes, int[] argDims, String[] argClassNames, boolean onlyExact) throws CompileError {
        CtClass pclazz;
        Method r;
        Method r2;
        Method maybe = null;
        ClassFile cf = clazz.getClassFile2();
        if (cf != null) {
            for (MethodInfo minfo : cf.getMethods()) {
                if (minfo.getName().equals(methodName) && (minfo.getAccessFlags() & 64) == 0) {
                    int res = compareSignature(minfo.getDescriptor(), argTypes, argDims, argClassNames);
                    if (res != -1) {
                        Method r3 = new Method(clazz, minfo, res);
                        if (res == 0) {
                            return r3;
                        }
                        if (maybe == null || maybe.notmatch > res) {
                            maybe = r3;
                        }
                    } else {
                        continue;
                    }
                }
            }
        }
        if (onlyExact) {
            maybe = null;
        } else if (maybe != null) {
            return maybe;
        }
        boolean isIntf = Modifier.isInterface(clazz.getModifiers());
        if (!isIntf) {
            try {
                CtClass pclazz2 = clazz.getSuperclass();
                if (!(pclazz2 == null || (r2 = lookupMethod(pclazz2, methodName, argTypes, argDims, argClassNames, onlyExact)) == null)) {
                    return r2;
                }
            } catch (NotFoundException e) {
            }
        }
        try {
            for (CtClass intf : clazz.getInterfaces()) {
                Method r4 = lookupMethod(intf, methodName, argTypes, argDims, argClassNames, onlyExact);
                if (r4 != null) {
                    return r4;
                }
            }
            if (!(!isIntf || (pclazz = clazz.getSuperclass()) == null || (r = lookupMethod(pclazz, methodName, argTypes, argDims, argClassNames, onlyExact)) == null)) {
                return r;
            }
        } catch (NotFoundException e2) {
        }
        return maybe;
    }

    private int compareSignature(String desc, int[] argTypes, int[] argDims, String[] argClassNames) throws CompileError {
        int i;
        int result = 0;
        int nArgs = argTypes.length;
        if (nArgs != Descriptor.numOfParameters(desc)) {
            return -1;
        }
        int len = desc.length();
        int n = 0;
        int i2 = 1;
        while (i2 < len) {
            int i3 = i2 + 1;
            char c = desc.charAt(i2);
            if (c == ')') {
                if (n == nArgs) {
                    return result;
                }
                return -1;
            } else if (n >= nArgs) {
                return -1;
            } else {
                int dim = 0;
                while (c == '[') {
                    dim++;
                    i3++;
                    c = desc.charAt(i3);
                }
                if (argTypes[n] == 412) {
                    if (dim == 0 && c != 'L') {
                        return -1;
                    }
                    if (c == 'L') {
                        i = desc.indexOf(59, i3) + 1;
                    }
                    i = i3;
                } else if (argDims[n] != dim) {
                    if (dim != 0 || c != 'L' || !desc.startsWith("java/lang/Object;", i3)) {
                        return -1;
                    }
                    i = desc.indexOf(59, i3) + 1;
                    result++;
                    if (i <= 0) {
                        return -1;
                    }
                } else if (c == 'L') {
                    int j = desc.indexOf(59, i3);
                    if (j < 0 || argTypes[n] != 307) {
                        return -1;
                    }
                    String cname = desc.substring(i3, j);
                    if (!cname.equals(argClassNames[n])) {
                        try {
                            if (!lookupClassByJvmName(argClassNames[n]).subtypeOf(lookupClassByJvmName(cname))) {
                                return -1;
                            }
                            result++;
                        } catch (NotFoundException e) {
                            result++;
                        }
                    }
                    i = j + 1;
                } else {
                    int t = descToType(c);
                    int at = argTypes[n];
                    if (t != at) {
                        if (t != 324 || (at != 334 && at != 303 && at != 306)) {
                            return -1;
                        }
                        result++;
                        i = i3;
                    }
                    i = i3;
                }
                n++;
                i2 = i;
            }
        }
        return -1;
    }

    public CtField lookupFieldByJvmName2(String jvmClassName, Symbol fieldSym, ASTree expr) throws NoFieldException {
        String field = fieldSym.get();
        try {
            CtClass cc = lookupClass(jvmToJavaName(jvmClassName), true);
            try {
                return cc.getField(field);
            } catch (NotFoundException e) {
                throw new NoFieldException(javaToJvmName(cc.getName()) + "$" + field, expr);
            }
        } catch (CompileError e2) {
            throw new NoFieldException(jvmClassName + "/" + field, expr);
        }
    }

    public CtField lookupFieldByJvmName(String jvmClassName, Symbol fieldName) throws CompileError {
        return lookupField(jvmToJavaName(jvmClassName), fieldName);
    }

    public CtField lookupField(String className, Symbol fieldName) throws CompileError {
        try {
            return lookupClass(className, false).getField(fieldName.get());
        } catch (NotFoundException e) {
            throw new CompileError("no such field: " + fieldName.get());
        }
    }

    public CtClass lookupClassByName(ASTList name) throws CompileError {
        return lookupClass(Declarator.astToClassName(name, '.'), false);
    }

    public CtClass lookupClassByJvmName(String jvmName) throws CompileError {
        return lookupClass(jvmToJavaName(jvmName), false);
    }

    public CtClass lookupClass(Declarator decl) throws CompileError {
        return lookupClass(decl.getType(), decl.getArrayDim(), decl.getClassName());
    }

    public CtClass lookupClass(int type, int dim, String classname) throws CompileError {
        String cname;
        if (type == 307) {
            CtClass clazz = lookupClassByJvmName(classname);
            if (dim <= 0) {
                return clazz;
            }
            cname = clazz.getName();
        } else {
            cname = getTypeName(type);
        }
        while (true) {
            dim--;
            if (dim <= 0) {
                return lookupClass(cname, false);
            }
            cname = cname + "[]";
        }
    }

    static String getTypeName(int type) throws CompileError {
        switch (type) {
            case TokenId.BOOLEAN /*{ENCODED_INT: 301}*/:
                return "boolean";
            case TokenId.BYTE /*{ENCODED_INT: 303}*/:
                return "byte";
            case TokenId.CHAR /*{ENCODED_INT: 306}*/:
                return "char";
            case TokenId.DOUBLE /*{ENCODED_INT: 312}*/:
                return "double";
            case TokenId.FLOAT /*{ENCODED_INT: 317}*/:
                return "float";
            case TokenId.INT /*{ENCODED_INT: 324}*/:
                return "int";
            case TokenId.LONG /*{ENCODED_INT: 326}*/:
                return "long";
            case TokenId.SHORT /*{ENCODED_INT: 334}*/:
                return "short";
            case TokenId.VOID /*{ENCODED_INT: 344}*/:
                return "void";
            default:
                fatal();
                return "";
        }
    }

    public CtClass lookupClass(String name, boolean notCheckInner) throws CompileError {
        CtClass cc;
        Map<String, String> cache = getInvalidNames();
        String found = cache.get(name);
        if (found == INVALID) {
            throw new CompileError("no such class: " + name);
        }
        if (found != null) {
            try {
                return this.classPool.get(found);
            } catch (NotFoundException e) {
            }
        }
        try {
            cc = lookupClass0(name, notCheckInner);
        } catch (NotFoundException e2) {
            cc = searchImports(name);
        }
        cache.put(name, cc.getName());
        return cc;
    }

    public static int getInvalidMapSize() {
        return invalidNamesMap.size();
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0030, code lost:
        r8.invalidNames = r1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.Map<java.lang.String, java.lang.String> getInvalidNames() {
        /*
            r8 = this;
            java.util.Map<java.lang.String, java.lang.String> r1 = r8.invalidNames
            if (r1 != 0) goto L_0x0032
            java.lang.Class<javassist.compiler.MemberResolver> r5 = javassist.compiler.MemberResolver.class
            monitor-enter(r5)
            java.util.Map<javassist.ClassPool, java.lang.ref.Reference<java.util.Map<java.lang.String, java.lang.String>>> r4 = javassist.compiler.MemberResolver.invalidNamesMap     // Catch:{ all -> 0x0033 }
            javassist.ClassPool r6 = r8.classPool     // Catch:{ all -> 0x0033 }
            java.lang.Object r3 = r4.get(r6)     // Catch:{ all -> 0x0033 }
            java.lang.ref.Reference r3 = (java.lang.ref.Reference) r3     // Catch:{ all -> 0x0033 }
            if (r3 == 0) goto L_0x003b
            java.lang.Object r4 = r3.get()     // Catch:{ all -> 0x0033 }
            r0 = r4
            java.util.Map r0 = (java.util.Map) r0     // Catch:{ all -> 0x0033 }
            r1 = r0
            r2 = r1
        L_0x001c:
            if (r2 != 0) goto L_0x0039
            java.util.Hashtable r1 = new java.util.Hashtable     // Catch:{ all -> 0x0036 }
            r1.<init>()     // Catch:{ all -> 0x0036 }
            java.util.Map<javassist.ClassPool, java.lang.ref.Reference<java.util.Map<java.lang.String, java.lang.String>>> r4 = javassist.compiler.MemberResolver.invalidNamesMap
            javassist.ClassPool r6 = r8.classPool
            java.lang.ref.WeakReference r7 = new java.lang.ref.WeakReference
            r7.<init>(r1)
            r4.put(r6, r7)
        L_0x002f:
            monitor-exit(r5)
            r8.invalidNames = r1
        L_0x0032:
            return r1
        L_0x0033:
            r4 = move-exception
        L_0x0034:
            monitor-exit(r5)
            throw r4
        L_0x0036:
            r4 = move-exception
            r1 = r2
            goto L_0x0034
        L_0x0039:
            r1 = r2
            goto L_0x002f
        L_0x003b:
            r2 = r1
            goto L_0x001c
        */
        throw new UnsupportedOperationException("Method not decompiled: javassist.compiler.MemberResolver.getInvalidNames():java.util.Map");
    }

    private CtClass searchImports(String orgName) throws CompileError {
        if (orgName.indexOf(46) < 0) {
            Iterator<String> it = this.classPool.getImportedPackages();
            while (it.hasNext()) {
                String pac = it.next();
                try {
                    return this.classPool.get(pac.replaceAll("\\.$", "") + "." + orgName);
                } catch (NotFoundException e) {
                    try {
                        if (pac.endsWith("." + orgName)) {
                            return this.classPool.get(pac);
                        }
                    } catch (NotFoundException e2) {
                    }
                }
            }
        }
        getInvalidNames().put(orgName, INVALID);
        throw new CompileError("no such class: " + orgName);
    }

    private CtClass lookupClass0(String classname, boolean notCheckInner) throws NotFoundException {
        CtClass cc = null;
        do {
            try {
                cc = this.classPool.get(classname);
                continue;
            } catch (NotFoundException e) {
                int i = classname.lastIndexOf(46);
                if (notCheckInner || i < 0) {
                    throw e;
                }
                StringBuffer sbuf = new StringBuffer(classname);
                sbuf.setCharAt(i, '$');
                classname = sbuf.toString();
                continue;
            }
        } while (cc == null);
        return cc;
    }

    public String resolveClassName(ASTList name) throws CompileError {
        if (name == null) {
            return null;
        }
        return javaToJvmName(lookupClassByName(name).getName());
    }

    public String resolveJvmClassName(String jvmName) throws CompileError {
        if (jvmName == null) {
            return null;
        }
        return javaToJvmName(lookupClassByJvmName(jvmName).getName());
    }

    public static CtClass getSuperclass(CtClass c) throws CompileError {
        try {
            CtClass sc = c.getSuperclass();
            if (sc != null) {
                return sc;
            }
        } catch (NotFoundException e) {
        }
        throw new CompileError("cannot find the super class of " + c.getName());
    }

    public static CtClass getSuperInterface(CtClass c, String interfaceName) throws CompileError {
        try {
            CtClass[] intfs = c.getInterfaces();
            for (int i = 0; i < intfs.length; i++) {
                if (intfs[i].getName().equals(interfaceName)) {
                    return intfs[i];
                }
            }
        } catch (NotFoundException e) {
        }
        throw new CompileError("cannot find the super interface " + interfaceName + " of " + c.getName());
    }

    public static String javaToJvmName(String classname) {
        return classname.replace('.', '/');
    }

    public static String jvmToJavaName(String classname) {
        return classname.replace('/', '.');
    }

    public static int descToType(char c) throws CompileError {
        switch (c) {
            case 'B':
                return TokenId.BYTE;
            case 'C':
                return TokenId.CHAR;
            case 'D':
                return TokenId.DOUBLE;
            case 'E':
            case Opcode.DSTORE_0:
            case Opcode.DSTORE_1:
            case Opcode.ASTORE_0:
            case Opcode.ASTORE_2:
            case Opcode.ASTORE_3:
            case Opcode.IASTORE:
            case Opcode.LASTORE:
            case Opcode.FASTORE:
            case Opcode.DASTORE:
            case Opcode.BASTORE:
            case Opcode.CASTORE:
            case Opcode.POP:
            case Opcode.POP2:
            case Opcode.DUP:
            default:
                fatal();
                return TokenId.VOID;
            case Opcode.FSTORE_3:
                return TokenId.FLOAT;
            case Opcode.DSTORE_2:
                return TokenId.INT;
            case Opcode.DSTORE_3:
                return TokenId.LONG;
            case 'L':
            case Opcode.DUP_X2:
                return TokenId.CLASS;
            case Opcode.AASTORE:
                return TokenId.SHORT;
            case Opcode.SASTORE:
                return TokenId.VOID;
            case Opcode.DUP_X1:
                return TokenId.BOOLEAN;
        }
    }

    public static int getModifiers(ASTList mods) {
        int m = 0;
        while (mods != null) {
            mods = mods.tail();
            switch (((Keyword) mods.head()).get()) {
                case TokenId.ABSTRACT /*{ENCODED_INT: 300}*/:
                    m |= 1024;
                    break;
                case TokenId.FINAL /*{ENCODED_INT: 315}*/:
                    m |= 16;
                    break;
                case TokenId.PRIVATE /*{ENCODED_INT: 330}*/:
                    m |= 2;
                    break;
                case TokenId.PROTECTED /*{ENCODED_INT: 331}*/:
                    m |= 4;
                    break;
                case TokenId.PUBLIC /*{ENCODED_INT: 332}*/:
                    m |= 1;
                    break;
                case TokenId.STATIC /*{ENCODED_INT: 335}*/:
                    m |= 8;
                    break;
                case TokenId.SYNCHRONIZED /*{ENCODED_INT: 338}*/:
                    m |= 32;
                    break;
                case TokenId.TRANSIENT /*{ENCODED_INT: 342}*/:
                    m |= 128;
                    break;
                case TokenId.VOLATILE /*{ENCODED_INT: 345}*/:
                    m |= 64;
                    break;
                case TokenId.STRICT /*{ENCODED_INT: 347}*/:
                    m |= 2048;
                    break;
            }
        }
        return m;
    }
}
