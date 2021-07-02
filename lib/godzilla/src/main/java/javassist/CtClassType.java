package javassist;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javassist.CtField;
import javassist.CtMember;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.ConstantAttribute;
import javassist.bytecode.Descriptor;
import javassist.bytecode.EnclosingMethodAttribute;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.InnerClassesAttribute;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.Opcode;
import javassist.bytecode.ParameterAnnotationsAttribute;
import javassist.bytecode.SignatureAttribute;
import javassist.bytecode.annotation.Annotation;
import javassist.bytecode.annotation.AnnotationImpl;
import javassist.compiler.AccessorMaker;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;
import javassist.expr.ExprEditor;

class CtClassType extends CtClass {
    private static final int GET_THRESHOLD = 2;
    private AccessorMaker accessors;
    ClassPool classPool;
    ClassFile classfile;
    private boolean doPruning;
    private FieldInitLink fieldInitializers;
    boolean gcConstPool;
    private int getCount;
    private Map<CtMethod, String> hiddenMethods;
    private Reference<CtMember.Cache> memberCache;
    byte[] rawClassfile;
    private int uniqueNumberSeed;
    boolean wasChanged;
    private boolean wasFrozen;
    boolean wasPruned;

    CtClassType(String name, ClassPool cp) {
        super(name);
        this.doPruning = ClassPool.doPruning;
        this.classPool = cp;
        this.gcConstPool = false;
        this.wasPruned = false;
        this.wasFrozen = false;
        this.wasChanged = false;
        this.classfile = null;
        this.rawClassfile = null;
        this.memberCache = null;
        this.accessors = null;
        this.fieldInitializers = null;
        this.hiddenMethods = null;
        this.uniqueNumberSeed = 0;
        this.getCount = 0;
    }

    CtClassType(InputStream ins, ClassPool cp) throws IOException {
        this((String) null, cp);
        this.classfile = new ClassFile(new DataInputStream(ins));
        this.qualifiedName = this.classfile.getName();
    }

    CtClassType(ClassFile cf, ClassPool cp) {
        this((String) null, cp);
        this.classfile = cf;
        this.qualifiedName = this.classfile.getName();
    }

    /* access modifiers changed from: protected */
    @Override // javassist.CtClass
    public void extendToString(StringBuffer buffer) {
        if (this.wasChanged) {
            buffer.append("changed ");
        }
        if (this.wasFrozen) {
            buffer.append("frozen ");
        }
        if (this.wasPruned) {
            buffer.append("pruned ");
        }
        buffer.append(Modifier.toString(getModifiers()));
        buffer.append(" class ");
        buffer.append(getName());
        try {
            CtClass ext = getSuperclass();
            if (ext != null && !ext.getName().equals("java.lang.Object")) {
                buffer.append(" extends " + ext.getName());
            }
        } catch (NotFoundException e) {
            buffer.append(" extends ??");
        }
        try {
            CtClass[] intf = getInterfaces();
            if (intf.length > 0) {
                buffer.append(" implements ");
            }
            for (CtClass ctClass : intf) {
                buffer.append(ctClass.getName());
                buffer.append(", ");
            }
        } catch (NotFoundException e2) {
            buffer.append(" extends ??");
        }
        CtMember.Cache memCache = getMembers();
        exToString(buffer, " fields=", memCache.fieldHead(), memCache.lastField());
        exToString(buffer, " constructors=", memCache.consHead(), memCache.lastCons());
        exToString(buffer, " methods=", memCache.methodHead(), memCache.lastMethod());
    }

    private void exToString(StringBuffer buffer, String msg, CtMember head, CtMember tail) {
        buffer.append(msg);
        while (head != tail) {
            head = head.next();
            buffer.append(head);
            buffer.append(", ");
        }
    }

    @Override // javassist.CtClass
    public AccessorMaker getAccessorMaker() {
        if (this.accessors == null) {
            this.accessors = new AccessorMaker(this);
        }
        return this.accessors;
    }

    @Override // javassist.CtClass
    public ClassFile getClassFile2() {
        return getClassFile3(true);
    }

    /* JADX WARNING: Removed duplicated region for block: B:23:0x005c A[SYNTHETIC, Splitter:B:23:0x005c] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public javassist.bytecode.ClassFile getClassFile3(boolean r11) {
        /*
        // Method dump skipped, instructions count: 227
        */
        throw new UnsupportedOperationException("Method not decompiled: javassist.CtClassType.getClassFile3(boolean):javassist.bytecode.ClassFile");
    }

    /* access modifiers changed from: package-private */
    @Override // javassist.CtClass
    public final void incGetCounter() {
        this.getCount++;
    }

    /* access modifiers changed from: package-private */
    @Override // javassist.CtClass
    public void compress() {
        if (this.getCount < 2) {
            if (!isModified() && ClassPool.releaseUnmodifiedClassFile) {
                removeClassFile();
            } else if (isFrozen() && !this.wasPruned) {
                saveClassFile();
            }
        }
        this.getCount = 0;
    }

    private synchronized void saveClassFile() {
        if (this.classfile != null && hasMemberCache() == null) {
            ByteArrayOutputStream barray = new ByteArrayOutputStream();
            try {
                this.classfile.write(new DataOutputStream(barray));
                barray.close();
                this.rawClassfile = barray.toByteArray();
                this.classfile = null;
            } catch (IOException e) {
            }
        }
    }

    private synchronized void removeClassFile() {
        if (this.classfile != null && !isModified() && hasMemberCache() == null) {
            this.classfile = null;
        }
    }

    private synchronized ClassFile setClassFile(ClassFile cf) {
        if (this.classfile == null) {
            this.classfile = cf;
        }
        return this.classfile;
    }

    @Override // javassist.CtClass
    public ClassPool getClassPool() {
        return this.classPool;
    }

    /* access modifiers changed from: package-private */
    public void setClassPool(ClassPool cp) {
        this.classPool = cp;
    }

    @Override // javassist.CtClass
    public URL getURL() throws NotFoundException {
        URL url = this.classPool.find(getName());
        if (url != null) {
            return url;
        }
        throw new NotFoundException(getName());
    }

    @Override // javassist.CtClass
    public boolean isModified() {
        return this.wasChanged;
    }

    @Override // javassist.CtClass
    public boolean isFrozen() {
        return this.wasFrozen;
    }

    @Override // javassist.CtClass
    public void freeze() {
        this.wasFrozen = true;
    }

    /* access modifiers changed from: package-private */
    @Override // javassist.CtClass
    public void checkModify() throws RuntimeException {
        if (isFrozen()) {
            String msg = getName() + " class is frozen";
            if (this.wasPruned) {
                msg = msg + " and pruned";
            }
            throw new RuntimeException(msg);
        }
        this.wasChanged = true;
    }

    @Override // javassist.CtClass
    public void defrost() {
        checkPruned("defrost");
        this.wasFrozen = false;
    }

    @Override // javassist.CtClass
    public boolean subtypeOf(CtClass clazz) throws NotFoundException {
        String cname = clazz.getName();
        if (this == clazz || getName().equals(cname)) {
            return true;
        }
        ClassFile file = getClassFile2();
        String supername = file.getSuperclass();
        if (supername != null && supername.equals(cname)) {
            return true;
        }
        String[] ifs = file.getInterfaces();
        for (String str : ifs) {
            if (str.equals(cname)) {
                return true;
            }
        }
        if (supername != null && this.classPool.get(supername).subtypeOf(clazz)) {
            return true;
        }
        for (String str2 : ifs) {
            if (this.classPool.get(str2).subtypeOf(clazz)) {
                return true;
            }
        }
        return false;
    }

    @Override // javassist.CtClass
    public void setName(String name) throws RuntimeException {
        String oldname = getName();
        if (!name.equals(oldname)) {
            this.classPool.checkNotFrozen(name);
            ClassFile cf = getClassFile2();
            super.setName(name);
            cf.setName(name);
            nameReplaced();
            this.classPool.classNameChanged(oldname, this);
        }
    }

    @Override // javassist.CtClass
    public String getGenericSignature() {
        SignatureAttribute sa = (SignatureAttribute) getClassFile2().getAttribute(SignatureAttribute.tag);
        if (sa == null) {
            return null;
        }
        return sa.getSignature();
    }

    @Override // javassist.CtClass
    public void setGenericSignature(String sig) {
        ClassFile cf = getClassFile();
        cf.addAttribute(new SignatureAttribute(cf.getConstPool(), sig));
    }

    @Override // javassist.CtClass
    public void replaceClassName(ClassMap classnames) throws RuntimeException {
        String oldClassName = getName();
        String newClassName = classnames.get((Object) Descriptor.toJvmName(oldClassName));
        if (newClassName != null) {
            newClassName = Descriptor.toJavaName(newClassName);
            this.classPool.checkNotFrozen(newClassName);
        }
        super.replaceClassName(classnames);
        getClassFile2().renameClass(classnames);
        nameReplaced();
        if (newClassName != null) {
            super.setName(newClassName);
            this.classPool.classNameChanged(oldClassName, this);
        }
    }

    @Override // javassist.CtClass
    public void replaceClassName(String oldname, String newname) throws RuntimeException {
        if (getName().equals(oldname)) {
            setName(newname);
            return;
        }
        super.replaceClassName(oldname, newname);
        getClassFile2().renameClass(oldname, newname);
        nameReplaced();
    }

    @Override // javassist.CtClass
    public boolean isInterface() {
        return Modifier.isInterface(getModifiers());
    }

    @Override // javassist.CtClass
    public boolean isAnnotation() {
        return Modifier.isAnnotation(getModifiers());
    }

    @Override // javassist.CtClass
    public boolean isEnum() {
        return Modifier.isEnum(getModifiers());
    }

    @Override // javassist.CtClass
    public int getModifiers() {
        ClassFile cf = getClassFile2();
        int acc = AccessFlag.clear(cf.getAccessFlags(), 32);
        int inner = cf.getInnerAccessFlags();
        if (inner != -1) {
            if ((inner & 8) != 0) {
                acc |= 8;
            }
            if ((inner & 1) != 0) {
                acc |= 1;
            } else {
                acc &= -2;
                if ((inner & 4) != 0) {
                    acc |= 4;
                } else if ((inner & 2) != 0) {
                    acc |= 2;
                }
            }
        }
        return AccessFlag.toModifier(acc);
    }

    @Override // javassist.CtClass
    public CtClass[] getNestedClasses() throws NotFoundException {
        ClassFile cf = getClassFile2();
        InnerClassesAttribute ica = (InnerClassesAttribute) cf.getAttribute(InnerClassesAttribute.tag);
        if (ica == null) {
            return new CtClass[0];
        }
        String thisName = cf.getName() + "$";
        int n = ica.tableLength();
        List<CtClass> list = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            String name = ica.innerClass(i);
            if (name != null && name.startsWith(thisName) && name.lastIndexOf(36) < thisName.length()) {
                list.add(this.classPool.get(name));
            }
        }
        return (CtClass[]) list.toArray(new CtClass[list.size()]);
    }

    @Override // javassist.CtClass
    public void setModifiers(int mod) {
        checkModify();
        updateInnerEntry(mod, getName(), this, true);
        getClassFile2().setAccessFlags(AccessFlag.of(mod & -9));
    }

    private static void updateInnerEntry(int newMod, String name, CtClass clazz, boolean outer) {
        int isStatic;
        InnerClassesAttribute ica = (InnerClassesAttribute) clazz.getClassFile2().getAttribute(InnerClassesAttribute.tag);
        if (ica != null) {
            int mod = newMod & -9;
            int i = ica.find(name);
            if (i >= 0 && ((isStatic = ica.accessFlags(i) & 8) != 0 || !Modifier.isStatic(newMod))) {
                clazz.checkModify();
                ica.setAccessFlags(i, AccessFlag.of(mod) | isStatic);
                String outName = ica.outerClass(i);
                if (outName != null && outer) {
                    try {
                        updateInnerEntry(mod, name, clazz.getClassPool().get(outName), false);
                        return;
                    } catch (NotFoundException e) {
                        throw new RuntimeException("cannot find the declaring class: " + outName);
                    }
                } else {
                    return;
                }
            }
        }
        if (Modifier.isStatic(newMod)) {
            throw new RuntimeException("cannot change " + Descriptor.toJavaName(name) + " into a static class");
        }
    }

    @Override // javassist.CtClass
    public boolean hasAnnotation(String annotationName) {
        ClassFile cf = getClassFile2();
        return hasAnnotationType(annotationName, getClassPool(), (AnnotationsAttribute) cf.getAttribute(AnnotationsAttribute.invisibleTag), (AnnotationsAttribute) cf.getAttribute(AnnotationsAttribute.visibleTag));
    }

    @Deprecated
    static boolean hasAnnotationType(Class<?> clz, ClassPool cp, AnnotationsAttribute a1, AnnotationsAttribute a2) {
        return hasAnnotationType(clz.getName(), cp, a1, a2);
    }

    static boolean hasAnnotationType(String annotationTypeName, ClassPool cp, AnnotationsAttribute a1, AnnotationsAttribute a2) {
        Annotation[] anno1;
        Annotation[] anno2;
        if (a1 == null) {
            anno1 = null;
        } else {
            anno1 = a1.getAnnotations();
        }
        if (a2 == null) {
            anno2 = null;
        } else {
            anno2 = a2.getAnnotations();
        }
        if (anno1 != null) {
            for (Annotation annotation : anno1) {
                if (annotation.getTypeName().equals(annotationTypeName)) {
                    return true;
                }
            }
        }
        if (anno2 != null) {
            for (Annotation annotation2 : anno2) {
                if (annotation2.getTypeName().equals(annotationTypeName)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override // javassist.CtClass
    public Object getAnnotation(Class<?> clz) throws ClassNotFoundException {
        ClassFile cf = getClassFile2();
        return getAnnotationType(clz, getClassPool(), (AnnotationsAttribute) cf.getAttribute(AnnotationsAttribute.invisibleTag), (AnnotationsAttribute) cf.getAttribute(AnnotationsAttribute.visibleTag));
    }

    static Object getAnnotationType(Class<?> clz, ClassPool cp, AnnotationsAttribute a1, AnnotationsAttribute a2) throws ClassNotFoundException {
        Annotation[] anno1;
        Annotation[] anno2;
        if (a1 == null) {
            anno1 = null;
        } else {
            anno1 = a1.getAnnotations();
        }
        if (a2 == null) {
            anno2 = null;
        } else {
            anno2 = a2.getAnnotations();
        }
        String typeName = clz.getName();
        if (anno1 != null) {
            for (int i = 0; i < anno1.length; i++) {
                if (anno1[i].getTypeName().equals(typeName)) {
                    return toAnnoType(anno1[i], cp);
                }
            }
        }
        if (anno2 != null) {
            for (int i2 = 0; i2 < anno2.length; i2++) {
                if (anno2[i2].getTypeName().equals(typeName)) {
                    return toAnnoType(anno2[i2], cp);
                }
            }
        }
        return null;
    }

    @Override // javassist.CtClass
    public Object[] getAnnotations() throws ClassNotFoundException {
        return getAnnotations(false);
    }

    @Override // javassist.CtClass
    public Object[] getAvailableAnnotations() {
        try {
            return getAnnotations(true);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unexpected exception ", e);
        }
    }

    private Object[] getAnnotations(boolean ignoreNotFound) throws ClassNotFoundException {
        ClassFile cf = getClassFile2();
        return toAnnotationType(ignoreNotFound, getClassPool(), (AnnotationsAttribute) cf.getAttribute(AnnotationsAttribute.invisibleTag), (AnnotationsAttribute) cf.getAttribute(AnnotationsAttribute.visibleTag));
    }

    static Object[] toAnnotationType(boolean ignoreNotFound, ClassPool cp, AnnotationsAttribute a1, AnnotationsAttribute a2) throws ClassNotFoundException {
        Annotation[] anno1;
        int size1;
        Annotation[] anno2;
        int size2;
        if (a1 == null) {
            anno1 = null;
            size1 = 0;
        } else {
            anno1 = a1.getAnnotations();
            size1 = anno1.length;
        }
        if (a2 == null) {
            anno2 = null;
            size2 = 0;
        } else {
            anno2 = a2.getAnnotations();
            size2 = anno2.length;
        }
        if (!ignoreNotFound) {
            Object[] result = new Object[(size1 + size2)];
            for (int i = 0; i < size1; i++) {
                result[i] = toAnnoType(anno1[i], cp);
            }
            for (int j = 0; j < size2; j++) {
                result[j + size1] = toAnnoType(anno2[j], cp);
            }
            return result;
        }
        List<Object> annotations = new ArrayList<>();
        for (int i2 = 0; i2 < size1; i2++) {
            try {
                annotations.add(toAnnoType(anno1[i2], cp));
            } catch (ClassNotFoundException e) {
            }
        }
        for (int j2 = 0; j2 < size2; j2++) {
            try {
                annotations.add(toAnnoType(anno2[j2], cp));
            } catch (ClassNotFoundException e2) {
            }
        }
        return annotations.toArray();
    }

    static Object[][] toAnnotationType(boolean ignoreNotFound, ClassPool cp, ParameterAnnotationsAttribute a1, ParameterAnnotationsAttribute a2, MethodInfo minfo) throws ClassNotFoundException {
        int numParameters;
        Annotation[] anno1;
        int size1;
        Annotation[] anno2;
        int size2;
        if (a1 != null) {
            numParameters = a1.numParameters();
        } else if (a2 != null) {
            numParameters = a2.numParameters();
        } else {
            numParameters = Descriptor.numOfParameters(minfo.getDescriptor());
        }
        Object[][] result = new Object[numParameters][];
        for (int i = 0; i < numParameters; i++) {
            if (a1 == null) {
                anno1 = null;
                size1 = 0;
            } else {
                anno1 = a1.getAnnotations()[i];
                size1 = anno1.length;
            }
            if (a2 == null) {
                anno2 = null;
                size2 = 0;
            } else {
                anno2 = a2.getAnnotations()[i];
                size2 = anno2.length;
            }
            if (!ignoreNotFound) {
                result[i] = new Object[(size1 + size2)];
                for (int j = 0; j < size1; j++) {
                    result[i][j] = toAnnoType(anno1[j], cp);
                }
                for (int j2 = 0; j2 < size2; j2++) {
                    result[i][j2 + size1] = toAnnoType(anno2[j2], cp);
                }
            } else {
                List<Object> annotations = new ArrayList<>();
                for (int j3 = 0; j3 < size1; j3++) {
                    try {
                        annotations.add(toAnnoType(anno1[j3], cp));
                    } catch (ClassNotFoundException e) {
                    }
                }
                for (int j4 = 0; j4 < size2; j4++) {
                    try {
                        annotations.add(toAnnoType(anno2[j4], cp));
                    } catch (ClassNotFoundException e2) {
                    }
                }
                result[i] = annotations.toArray();
            }
        }
        return result;
    }

    private static Object toAnnoType(Annotation anno, ClassPool cp) throws ClassNotFoundException {
        try {
            return anno.toAnnotationType(cp.getClassLoader(), cp);
        } catch (ClassNotFoundException e) {
            try {
                return anno.toAnnotationType(cp.getClass().getClassLoader(), cp);
            } catch (ClassNotFoundException e2) {
                Class<?> clazz = cp.get(anno.getTypeName()).toClass();
                return AnnotationImpl.make(clazz.getClassLoader(), clazz, cp, anno);
            } catch (Throwable th) {
                throw new ClassNotFoundException(anno.getTypeName());
            }
        }
    }

    @Override // javassist.CtClass
    public boolean subclassOf(CtClass superclass) {
        if (superclass == null) {
            return false;
        }
        String superName = superclass.getName();
        for (CtClass curr = this; curr != null; curr = curr.getSuperclass()) {
            try {
                if (curr.getName().equals(superName)) {
                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override // javassist.CtClass
    public CtClass getSuperclass() throws NotFoundException {
        String supername = getClassFile2().getSuperclass();
        if (supername == null) {
            return null;
        }
        return this.classPool.get(supername);
    }

    @Override // javassist.CtClass
    public void setSuperclass(CtClass clazz) throws CannotCompileException {
        checkModify();
        if (isInterface()) {
            addInterface(clazz);
        } else {
            getClassFile2().setSuperclass(clazz.getName());
        }
    }

    @Override // javassist.CtClass
    public CtClass[] getInterfaces() throws NotFoundException {
        String[] ifs = getClassFile2().getInterfaces();
        int num = ifs.length;
        CtClass[] ifc = new CtClass[num];
        for (int i = 0; i < num; i++) {
            ifc[i] = this.classPool.get(ifs[i]);
        }
        return ifc;
    }

    @Override // javassist.CtClass
    public void setInterfaces(CtClass[] list) {
        String[] ifs;
        checkModify();
        if (list == null) {
            ifs = new String[0];
        } else {
            int num = list.length;
            ifs = new String[num];
            for (int i = 0; i < num; i++) {
                ifs[i] = list[i].getName();
            }
        }
        getClassFile2().setInterfaces(ifs);
    }

    @Override // javassist.CtClass
    public void addInterface(CtClass anInterface) {
        checkModify();
        if (anInterface != null) {
            getClassFile2().addInterface(anInterface.getName());
        }
    }

    @Override // javassist.CtClass
    public CtClass getDeclaringClass() throws NotFoundException {
        ClassFile cf = getClassFile2();
        InnerClassesAttribute ica = (InnerClassesAttribute) cf.getAttribute(InnerClassesAttribute.tag);
        if (ica == null) {
            return null;
        }
        String name = getName();
        int n = ica.tableLength();
        for (int i = 0; i < n; i++) {
            if (name.equals(ica.innerClass(i))) {
                String outName = ica.outerClass(i);
                if (outName != null) {
                    return this.classPool.get(outName);
                }
                EnclosingMethodAttribute ema = (EnclosingMethodAttribute) cf.getAttribute(EnclosingMethodAttribute.tag);
                if (ema != null) {
                    return this.classPool.get(ema.className());
                }
            }
        }
        return null;
    }

    @Override // javassist.CtClass
    public CtBehavior getEnclosingBehavior() throws NotFoundException {
        EnclosingMethodAttribute ema = (EnclosingMethodAttribute) getClassFile2().getAttribute(EnclosingMethodAttribute.tag);
        if (ema == null) {
            return null;
        }
        CtClass enc = this.classPool.get(ema.className());
        String name = ema.methodName();
        if ("<init>".equals(name)) {
            return enc.getConstructor(ema.methodDescriptor());
        }
        if (MethodInfo.nameClinit.equals(name)) {
            return enc.getClassInitializer();
        }
        return enc.getMethod(name, ema.methodDescriptor());
    }

    @Override // javassist.CtClass
    public CtClass makeNestedClass(String name, boolean isStatic) {
        if (!isStatic) {
            throw new RuntimeException("sorry, only nested static class is supported");
        }
        checkModify();
        CtClass c = this.classPool.makeNestedClass(getName() + "$" + name);
        ClassFile cf = getClassFile2();
        ClassFile cf2 = c.getClassFile2();
        InnerClassesAttribute ica = (InnerClassesAttribute) cf.getAttribute(InnerClassesAttribute.tag);
        if (ica == null) {
            ica = new InnerClassesAttribute(cf.getConstPool());
            cf.addAttribute(ica);
        }
        ica.append(c.getName(), getName(), name, (cf2.getAccessFlags() & -33) | 8);
        cf2.addAttribute(ica.copy(cf2.getConstPool(), null));
        return c;
    }

    private void nameReplaced() {
        CtMember.Cache cache = hasMemberCache();
        if (cache != null) {
            CtMember mth = cache.methodHead();
            CtMember tail = cache.lastMethod();
            while (mth != tail) {
                mth = mth.next();
                mth.nameReplaced();
            }
        }
    }

    /* access modifiers changed from: protected */
    public CtMember.Cache hasMemberCache() {
        if (this.memberCache != null) {
            return this.memberCache.get();
        }
        return null;
    }

    /* access modifiers changed from: protected */
    public synchronized CtMember.Cache getMembers() {
        Throwable th;
        CtMember.Cache cache = null;
        try {
            if (this.memberCache == null || (cache = this.memberCache.get()) == null) {
                try {
                    cache = new CtMember.Cache(this);
                    makeFieldCache(cache);
                    makeBehaviorCache(cache);
                    this.memberCache = new WeakReference(cache);
                } catch (Throwable th2) {
                    th = th2;
                    throw th;
                }
            }
            return cache;
        } catch (Throwable th3) {
            th = th3;
            throw th;
        }
    }

    private void makeFieldCache(CtMember.Cache cache) {
        for (FieldInfo finfo : getClassFile3(false).getFields()) {
            cache.addField(new CtField(finfo, this));
        }
    }

    private void makeBehaviorCache(CtMember.Cache cache) {
        for (MethodInfo minfo : getClassFile3(false).getMethods()) {
            if (minfo.isMethod()) {
                cache.addMethod(new CtMethod(minfo, this));
            } else {
                cache.addConstructor(new CtConstructor(minfo, this));
            }
        }
    }

    @Override // javassist.CtClass
    public CtField[] getFields() {
        List<CtMember> alist = new ArrayList<>();
        getFields(alist, this);
        return (CtField[]) alist.toArray(new CtField[alist.size()]);
    }

    private static void getFields(List<CtMember> alist, CtClass cc) {
        if (cc != null) {
            try {
                getFields(alist, cc.getSuperclass());
            } catch (NotFoundException e) {
            }
            try {
                for (CtClass ctc : cc.getInterfaces()) {
                    getFields(alist, ctc);
                }
            } catch (NotFoundException e2) {
            }
            CtMember.Cache memCache = ((CtClassType) cc).getMembers();
            CtMember field = memCache.fieldHead();
            CtMember tail = memCache.lastField();
            while (field != tail) {
                field = field.next();
                if (!Modifier.isPrivate(field.getModifiers())) {
                    alist.add(field);
                }
            }
        }
    }

    @Override // javassist.CtClass
    public CtField getField(String name, String desc) throws NotFoundException {
        return checkGetField(getField2(name, desc), name, desc);
    }

    private CtField checkGetField(CtField f, String name, String desc) throws NotFoundException {
        if (f != null) {
            return f;
        }
        String msg = "field: " + name;
        if (desc != null) {
            msg = msg + " type " + desc;
        }
        throw new NotFoundException(msg + " in " + getName());
    }

    /* access modifiers changed from: package-private */
    @Override // javassist.CtClass
    public CtField getField2(String name, String desc) {
        CtField df = getDeclaredField2(name, desc);
        if (df != null) {
            return df;
        }
        try {
            for (CtClass ctc : getInterfaces()) {
                CtField f = ctc.getField2(name, desc);
                if (f != null) {
                    return f;
                }
            }
            CtClass s = getSuperclass();
            if (s != null) {
                return s.getField2(name, desc);
            }
        } catch (NotFoundException e) {
        }
        return null;
    }

    @Override // javassist.CtClass
    public CtField[] getDeclaredFields() {
        CtMember.Cache memCache = getMembers();
        CtMember field = memCache.fieldHead();
        CtMember tail = memCache.lastField();
        CtField[] cfs = new CtField[CtMember.Cache.count(field, tail)];
        int i = 0;
        while (field != tail) {
            field = field.next();
            cfs[i] = (CtField) field;
            i++;
        }
        return cfs;
    }

    @Override // javassist.CtClass
    public CtField getDeclaredField(String name) throws NotFoundException {
        return getDeclaredField(name, null);
    }

    @Override // javassist.CtClass
    public CtField getDeclaredField(String name, String desc) throws NotFoundException {
        return checkGetField(getDeclaredField2(name, desc), name, desc);
    }

    private CtField getDeclaredField2(String name, String desc) {
        CtMember.Cache memCache = getMembers();
        CtMember field = memCache.fieldHead();
        CtMember tail = memCache.lastField();
        while (field != tail) {
            field = field.next();
            if (field.getName().equals(name) && (desc == null || desc.equals(field.getSignature()))) {
                return (CtField) field;
            }
        }
        return null;
    }

    @Override // javassist.CtClass
    public CtBehavior[] getDeclaredBehaviors() {
        CtMember.Cache memCache = getMembers();
        CtMember cons = memCache.consHead();
        CtMember consTail = memCache.lastCons();
        int cnum = CtMember.Cache.count(cons, consTail);
        CtMember mth = memCache.methodHead();
        CtMember mthTail = memCache.lastMethod();
        CtBehavior[] cb = new CtBehavior[(cnum + CtMember.Cache.count(mth, mthTail))];
        int i = 0;
        while (cons != consTail) {
            cons = cons.next();
            cb[i] = (CtBehavior) cons;
            i++;
        }
        while (mth != mthTail) {
            mth = mth.next();
            cb[i] = (CtBehavior) mth;
            i++;
        }
        return cb;
    }

    @Override // javassist.CtClass
    public CtConstructor[] getConstructors() {
        int i;
        CtMember.Cache memCache = getMembers();
        CtMember cons = memCache.consHead();
        CtMember consTail = memCache.lastCons();
        int n = 0;
        CtMember mem = cons;
        while (mem != consTail) {
            mem = mem.next();
            if (isPubCons((CtConstructor) mem)) {
                n++;
            }
        }
        CtConstructor[] result = new CtConstructor[n];
        CtMember mem2 = cons;
        int i2 = 0;
        while (mem2 != consTail) {
            mem2 = mem2.next();
            CtConstructor cc = (CtConstructor) mem2;
            if (isPubCons(cc)) {
                i = i2 + 1;
                result[i2] = cc;
            } else {
                i = i2;
            }
            i2 = i;
        }
        return result;
    }

    private static boolean isPubCons(CtConstructor cons) {
        return !Modifier.isPrivate(cons.getModifiers()) && cons.isConstructor();
    }

    @Override // javassist.CtClass
    public CtConstructor getConstructor(String desc) throws NotFoundException {
        CtMember.Cache memCache = getMembers();
        CtMember cons = memCache.consHead();
        CtMember consTail = memCache.lastCons();
        while (cons != consTail) {
            cons = cons.next();
            CtConstructor cc = (CtConstructor) cons;
            if (cc.getMethodInfo2().getDescriptor().equals(desc) && cc.isConstructor()) {
                return cc;
            }
        }
        return super.getConstructor(desc);
    }

    @Override // javassist.CtClass
    public CtConstructor[] getDeclaredConstructors() {
        int i;
        CtMember.Cache memCache = getMembers();
        CtMember cons = memCache.consHead();
        CtMember consTail = memCache.lastCons();
        int n = 0;
        CtMember mem = cons;
        while (mem != consTail) {
            mem = mem.next();
            if (((CtConstructor) mem).isConstructor()) {
                n++;
            }
        }
        CtConstructor[] result = new CtConstructor[n];
        CtMember mem2 = cons;
        int i2 = 0;
        while (mem2 != consTail) {
            mem2 = mem2.next();
            CtConstructor cc = (CtConstructor) mem2;
            if (cc.isConstructor()) {
                i = i2 + 1;
                result[i2] = cc;
            } else {
                i = i2;
            }
            i2 = i;
        }
        return result;
    }

    @Override // javassist.CtClass
    public CtConstructor getClassInitializer() {
        CtMember.Cache memCache = getMembers();
        CtMember cons = memCache.consHead();
        CtMember consTail = memCache.lastCons();
        while (cons != consTail) {
            cons = cons.next();
            CtConstructor cc = (CtConstructor) cons;
            if (cc.isClassInitializer()) {
                return cc;
            }
        }
        return null;
    }

    @Override // javassist.CtClass
    public CtMethod[] getMethods() {
        Map<String, CtMember> h = new HashMap<>();
        getMethods0(h, this);
        return (CtMethod[]) h.values().toArray(new CtMethod[h.size()]);
    }

    private static void getMethods0(Map<String, CtMember> h, CtClass cc) {
        try {
            for (CtClass ctc : cc.getInterfaces()) {
                getMethods0(h, ctc);
            }
        } catch (NotFoundException e) {
        }
        try {
            CtClass s = cc.getSuperclass();
            if (s != null) {
                getMethods0(h, s);
            }
        } catch (NotFoundException e2) {
        }
        if (cc instanceof CtClassType) {
            CtMember.Cache memCache = ((CtClassType) cc).getMembers();
            CtMember mth = memCache.methodHead();
            CtMember mthTail = memCache.lastMethod();
            while (mth != mthTail) {
                mth = mth.next();
                if (!Modifier.isPrivate(mth.getModifiers())) {
                    h.put(((CtMethod) mth).getStringRep(), mth);
                }
            }
        }
    }

    @Override // javassist.CtClass
    public CtMethod getMethod(String name, String desc) throws NotFoundException {
        CtMethod m = getMethod0(this, name, desc);
        if (m != null) {
            return m;
        }
        throw new NotFoundException(name + "(..) is not found in " + getName());
    }

    private static CtMethod getMethod0(CtClass cc, String name, String desc) {
        CtMethod m;
        if (cc instanceof CtClassType) {
            CtMember.Cache memCache = ((CtClassType) cc).getMembers();
            CtMember mth = memCache.methodHead();
            CtMember mthTail = memCache.lastMethod();
            while (mth != mthTail) {
                mth = mth.next();
                if (mth.getName().equals(name) && ((CtMethod) mth).getMethodInfo2().getDescriptor().equals(desc)) {
                    return (CtMethod) mth;
                }
            }
        }
        try {
            CtClass s = cc.getSuperclass();
            if (!(s == null || (m = getMethod0(s, name, desc)) == null)) {
                return m;
            }
        } catch (NotFoundException e) {
        }
        try {
            for (CtClass ctc : cc.getInterfaces()) {
                CtMethod m2 = getMethod0(ctc, name, desc);
                if (m2 != null) {
                    return m2;
                }
            }
        } catch (NotFoundException e2) {
        }
        return null;
    }

    @Override // javassist.CtClass
    public CtMethod[] getDeclaredMethods() {
        CtMember.Cache memCache = getMembers();
        CtMember mth = memCache.methodHead();
        CtMember mthTail = memCache.lastMethod();
        List<CtMember> methods = new ArrayList<>();
        while (mth != mthTail) {
            mth = mth.next();
            methods.add(mth);
        }
        return (CtMethod[]) methods.toArray(new CtMethod[methods.size()]);
    }

    @Override // javassist.CtClass
    public CtMethod[] getDeclaredMethods(String name) throws NotFoundException {
        CtMember.Cache memCache = getMembers();
        CtMember mth = memCache.methodHead();
        CtMember mthTail = memCache.lastMethod();
        List<CtMember> methods = new ArrayList<>();
        while (mth != mthTail) {
            mth = mth.next();
            if (mth.getName().equals(name)) {
                methods.add(mth);
            }
        }
        return (CtMethod[]) methods.toArray(new CtMethod[methods.size()]);
    }

    @Override // javassist.CtClass
    public CtMethod getDeclaredMethod(String name) throws NotFoundException {
        CtMember.Cache memCache = getMembers();
        CtMember mth = memCache.methodHead();
        CtMember mthTail = memCache.lastMethod();
        while (mth != mthTail) {
            mth = mth.next();
            if (mth.getName().equals(name)) {
                return (CtMethod) mth;
            }
        }
        throw new NotFoundException(name + "(..) is not found in " + getName());
    }

    @Override // javassist.CtClass
    public CtMethod getDeclaredMethod(String name, CtClass[] params) throws NotFoundException {
        String desc = Descriptor.ofParameters(params);
        CtMember.Cache memCache = getMembers();
        CtMember mth = memCache.methodHead();
        CtMember mthTail = memCache.lastMethod();
        while (mth != mthTail) {
            mth = mth.next();
            if (mth.getName().equals(name) && ((CtMethod) mth).getMethodInfo2().getDescriptor().startsWith(desc)) {
                return (CtMethod) mth;
            }
        }
        throw new NotFoundException(name + "(..) is not found in " + getName());
    }

    @Override // javassist.CtClass
    public void addField(CtField f, String init) throws CannotCompileException {
        addField(f, CtField.Initializer.byExpr(init));
    }

    @Override // javassist.CtClass
    public void addField(CtField f, CtField.Initializer init) throws CannotCompileException {
        checkModify();
        if (f.getDeclaringClass() != this) {
            throw new CannotCompileException("cannot add");
        }
        if (init == null) {
            init = f.getInit();
        }
        if (init != null) {
            init.check(f.getSignature());
            int mod = f.getModifiers();
            if (Modifier.isStatic(mod) && Modifier.isFinal(mod)) {
                try {
                    ConstPool cp = getClassFile2().getConstPool();
                    int index = init.getConstantValue(cp, f.getType());
                    if (index != 0) {
                        f.getFieldInfo2().addAttribute(new ConstantAttribute(cp, index));
                        init = null;
                    }
                } catch (NotFoundException e) {
                }
            }
        }
        getMembers().addField(f);
        getClassFile2().addField(f.getFieldInfo2());
        if (init != null) {
            FieldInitLink fil = new FieldInitLink(f, init);
            FieldInitLink link = this.fieldInitializers;
            if (link == null) {
                this.fieldInitializers = fil;
                return;
            }
            while (link.next != null) {
                link = link.next;
            }
            link.next = fil;
        }
    }

    @Override // javassist.CtClass
    public void removeField(CtField f) throws NotFoundException {
        checkModify();
        if (getClassFile2().getFields().remove(f.getFieldInfo2())) {
            getMembers().remove(f);
            this.gcConstPool = true;
            return;
        }
        throw new NotFoundException(f.toString());
    }

    @Override // javassist.CtClass
    public CtConstructor makeClassInitializer() throws CannotCompileException {
        CtConstructor clinit = getClassInitializer();
        if (clinit != null) {
            return clinit;
        }
        checkModify();
        ClassFile cf = getClassFile2();
        modifyClassConstructor(cf, new Bytecode(cf.getConstPool(), 0, 0), 0, 0);
        return getClassInitializer();
    }

    @Override // javassist.CtClass
    public void addConstructor(CtConstructor c) throws CannotCompileException {
        checkModify();
        if (c.getDeclaringClass() != this) {
            throw new CannotCompileException("cannot add");
        }
        getMembers().addConstructor(c);
        getClassFile2().addMethod(c.getMethodInfo2());
    }

    @Override // javassist.CtClass
    public void removeConstructor(CtConstructor m) throws NotFoundException {
        checkModify();
        if (getClassFile2().getMethods().remove(m.getMethodInfo2())) {
            getMembers().remove(m);
            this.gcConstPool = true;
            return;
        }
        throw new NotFoundException(m.toString());
    }

    @Override // javassist.CtClass
    public void addMethod(CtMethod m) throws CannotCompileException {
        checkModify();
        if (m.getDeclaringClass() != this) {
            throw new CannotCompileException("bad declaring class");
        }
        int mod = m.getModifiers();
        if ((getModifiers() & 512) != 0) {
            if (Modifier.isProtected(mod) || Modifier.isPrivate(mod)) {
                throw new CannotCompileException("an interface method must be public: " + m.toString());
            }
            m.setModifiers(mod | 1);
        }
        getMembers().addMethod(m);
        getClassFile2().addMethod(m.getMethodInfo2());
        if ((mod & 1024) != 0) {
            setModifiers(getModifiers() | 1024);
        }
    }

    @Override // javassist.CtClass
    public void removeMethod(CtMethod m) throws NotFoundException {
        checkModify();
        if (getClassFile2().getMethods().remove(m.getMethodInfo2())) {
            getMembers().remove(m);
            this.gcConstPool = true;
            return;
        }
        throw new NotFoundException(m.toString());
    }

    @Override // javassist.CtClass
    public byte[] getAttribute(String name) {
        AttributeInfo ai = getClassFile2().getAttribute(name);
        if (ai == null) {
            return null;
        }
        return ai.get();
    }

    @Override // javassist.CtClass
    public void setAttribute(String name, byte[] data) {
        checkModify();
        ClassFile cf = getClassFile2();
        cf.addAttribute(new AttributeInfo(cf.getConstPool(), name, data));
    }

    @Override // javassist.CtClass
    public void instrument(CodeConverter converter) throws CannotCompileException {
        checkModify();
        ClassFile cf = getClassFile2();
        ConstPool cp = cf.getConstPool();
        List<MethodInfo> methods = cf.getMethods();
        for (MethodInfo minfo : (MethodInfo[]) methods.toArray(new MethodInfo[methods.size()])) {
            converter.doit(this, minfo, cp);
        }
    }

    @Override // javassist.CtClass
    public void instrument(ExprEditor editor) throws CannotCompileException {
        checkModify();
        List<MethodInfo> methods = getClassFile2().getMethods();
        for (MethodInfo minfo : (MethodInfo[]) methods.toArray(new MethodInfo[methods.size()])) {
            editor.doit(this, minfo);
        }
    }

    @Override // javassist.CtClass
    public void prune() {
        if (!this.wasPruned) {
            this.wasFrozen = true;
            this.wasPruned = true;
            getClassFile2().prune();
        }
    }

    @Override // javassist.CtClass
    public void rebuildClassFile() {
        this.gcConstPool = true;
    }

    @Override // javassist.CtClass
    public void toBytecode(DataOutputStream out) throws CannotCompileException, IOException {
        try {
            if (isModified()) {
                checkPruned("toBytecode");
                ClassFile cf = getClassFile2();
                if (this.gcConstPool) {
                    cf.compact();
                    this.gcConstPool = false;
                }
                modifyClassConstructor(cf);
                modifyConstructors(cf);
                if (debugDump != null) {
                    dumpClassFile(cf);
                }
                cf.write(out);
                out.flush();
                this.fieldInitializers = null;
                if (this.doPruning) {
                    cf.prune();
                    this.wasPruned = true;
                }
            } else {
                this.classPool.writeClassfile(getName(), out);
            }
            this.getCount = 0;
            this.wasFrozen = true;
        } catch (NotFoundException e) {
            throw new CannotCompileException(e);
        } catch (IOException e2) {
            throw new CannotCompileException(e2);
        }
    }

    private void dumpClassFile(ClassFile cf) throws IOException {
        DataOutputStream dump = makeFileOutput(debugDump);
        try {
            cf.write(dump);
        } finally {
            dump.close();
        }
    }

    private void checkPruned(String method) {
        if (this.wasPruned) {
            throw new RuntimeException(method + "(): " + getName() + " was pruned.");
        }
    }

    @Override // javassist.CtClass
    public boolean stopPruning(boolean stop) {
        boolean prev;
        boolean z = true;
        if (!this.doPruning) {
            prev = true;
        } else {
            prev = false;
        }
        if (stop) {
            z = false;
        }
        this.doPruning = z;
        return prev;
    }

    private void modifyClassConstructor(ClassFile cf) throws CannotCompileException, NotFoundException {
        if (this.fieldInitializers != null) {
            Bytecode code = new Bytecode(cf.getConstPool(), 0, 0);
            Javac jv = new Javac(code, this);
            int stacksize = 0;
            boolean doInit = false;
            for (FieldInitLink fi = this.fieldInitializers; fi != null; fi = fi.next) {
                CtField f = fi.field;
                if (Modifier.isStatic(f.getModifiers())) {
                    doInit = true;
                    int s = fi.init.compileIfStatic(f.getType(), f.getName(), code, jv);
                    if (stacksize < s) {
                        stacksize = s;
                    }
                }
            }
            if (doInit) {
                modifyClassConstructor(cf, code, stacksize, 0);
            }
        }
    }

    private void modifyClassConstructor(ClassFile cf, Bytecode code, int stacksize, int localsize) throws CannotCompileException {
        MethodInfo m = cf.getStaticInitializer();
        if (m == null) {
            code.add(Opcode.RETURN);
            code.setMaxStack(stacksize);
            code.setMaxLocals(localsize);
            m = new MethodInfo(cf.getConstPool(), MethodInfo.nameClinit, "()V");
            m.setAccessFlags(8);
            m.setCodeAttribute(code.toCodeAttribute());
            cf.addMethod(m);
            CtMember.Cache cache = hasMemberCache();
            if (cache != null) {
                cache.addConstructor(new CtConstructor(m, this));
            }
        } else {
            CodeAttribute codeAttr = m.getCodeAttribute();
            if (codeAttr == null) {
                throw new CannotCompileException("empty <clinit>");
            }
            try {
                CodeIterator it = codeAttr.iterator();
                it.insert(code.getExceptionTable(), it.insertEx(code.get()));
                if (codeAttr.getMaxStack() < stacksize) {
                    codeAttr.setMaxStack(stacksize);
                }
                if (codeAttr.getMaxLocals() < localsize) {
                    codeAttr.setMaxLocals(localsize);
                }
            } catch (BadBytecode e) {
                throw new CannotCompileException(e);
            }
        }
        try {
            m.rebuildStackMapIf6(this.classPool, cf);
        } catch (BadBytecode e2) {
            throw new CannotCompileException(e2);
        }
    }

    private void modifyConstructors(ClassFile cf) throws CannotCompileException, NotFoundException {
        CodeAttribute codeAttr;
        if (this.fieldInitializers != null) {
            ConstPool cp = cf.getConstPool();
            for (MethodInfo minfo : cf.getMethods()) {
                if (minfo.isConstructor() && (codeAttr = minfo.getCodeAttribute()) != null) {
                    try {
                        Bytecode init = new Bytecode(cp, 0, codeAttr.getMaxLocals());
                        insertAuxInitializer(codeAttr, init, makeFieldInitializer(init, Descriptor.getParameterTypes(minfo.getDescriptor(), this.classPool)));
                        minfo.rebuildStackMapIf6(this.classPool, cf);
                    } catch (BadBytecode e) {
                        throw new CannotCompileException(e);
                    }
                }
            }
        }
    }

    private static void insertAuxInitializer(CodeAttribute codeAttr, Bytecode initializer, int stacksize) throws BadBytecode {
        CodeIterator it = codeAttr.iterator();
        if (it.skipSuperConstructor() >= 0 || it.skipThisConstructor() < 0) {
            it.insert(initializer.getExceptionTable(), it.insertEx(initializer.get()));
            if (codeAttr.getMaxStack() < stacksize) {
                codeAttr.setMaxStack(stacksize);
            }
        }
    }

    private int makeFieldInitializer(Bytecode code, CtClass[] parameters) throws CannotCompileException, NotFoundException {
        int s;
        int stacksize = 0;
        Javac jv = new Javac(code, this);
        try {
            jv.recordParams(parameters, false);
            for (FieldInitLink fi = this.fieldInitializers; fi != null; fi = fi.next) {
                CtField f = fi.field;
                if (!Modifier.isStatic(f.getModifiers()) && stacksize < (s = fi.init.compile(f.getType(), f.getName(), code, parameters, jv))) {
                    stacksize = s;
                }
            }
            return stacksize;
        } catch (CompileError e) {
            throw new CannotCompileException(e);
        }
    }

    /* access modifiers changed from: package-private */
    public Map<CtMethod, String> getHiddenMethods() {
        if (this.hiddenMethods == null) {
            this.hiddenMethods = new Hashtable();
        }
        return this.hiddenMethods;
    }

    /* access modifiers changed from: package-private */
    public int getUniqueNumber() {
        int i = this.uniqueNumberSeed;
        this.uniqueNumberSeed = i + 1;
        return i;
    }

    @Override // javassist.CtClass
    public String makeUniqueName(String prefix) {
        Map<Object, CtClassType> table = new HashMap<>();
        makeMemberList(table);
        Set<Object> keys = table.keySet();
        String[] methods = new String[keys.size()];
        keys.toArray(methods);
        if (notFindInArray(prefix, methods)) {
            return prefix;
        }
        int i = 100;
        while (i <= 999) {
            i++;
            String name = prefix + i;
            if (notFindInArray(name, methods)) {
                return name;
            }
        }
        throw new RuntimeException("too many unique name");
    }

    private static boolean notFindInArray(String prefix, String[] values) {
        for (String str : values) {
            if (str.startsWith(prefix)) {
                return false;
            }
        }
        return true;
    }

    private void makeMemberList(Map<Object, CtClassType> table) {
        int mod = getModifiers();
        if (Modifier.isAbstract(mod) || Modifier.isInterface(mod)) {
            try {
                CtClass[] ifs = getInterfaces();
                for (CtClass ic : ifs) {
                    if (ic != null && (ic instanceof CtClassType)) {
                        ((CtClassType) ic).makeMemberList(table);
                    }
                }
            } catch (NotFoundException e) {
            }
        }
        try {
            CtClass s = getSuperclass();
            if (s != null && (s instanceof CtClassType)) {
                ((CtClassType) s).makeMemberList(table);
            }
        } catch (NotFoundException e2) {
        }
        for (MethodInfo minfo : getClassFile2().getMethods()) {
            table.put(minfo.getName(), this);
        }
        for (FieldInfo finfo : getClassFile2().getFields()) {
            table.put(finfo.getName(), this);
        }
    }
}
