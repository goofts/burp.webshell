package javassist;

import javassist.bytecode.AccessFlag;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.AttributeInfo;
import javassist.bytecode.Bytecode;
import javassist.bytecode.ClassFile;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.SignatureAttribute;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;
import javassist.compiler.SymbolTable;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.DoubleConst;
import javassist.compiler.ast.IntConst;
import javassist.compiler.ast.StringL;

public class CtField extends CtMember {
    static final String javaLangString = "java.lang.String";
    protected FieldInfo fieldInfo;

    public CtField(CtClass type, String name, CtClass declaring) throws CannotCompileException {
        this(Descriptor.of(type), name, declaring);
    }

    public CtField(CtField src, CtClass declaring) throws CannotCompileException {
        this(src.fieldInfo.getDescriptor(), src.fieldInfo.getName(), declaring);
        FieldInfo fi = this.fieldInfo;
        fi.setAccessFlags(src.fieldInfo.getAccessFlags());
        ConstPool cp = fi.getConstPool();
        for (AttributeInfo ainfo : src.fieldInfo.getAttributes()) {
            fi.addAttribute(ainfo.copy(cp, null));
        }
    }

    private CtField(String typeDesc, String name, CtClass clazz) throws CannotCompileException {
        super(clazz);
        ClassFile cf = clazz.getClassFile2();
        if (cf == null) {
            throw new CannotCompileException("bad declaring class: " + clazz.getName());
        }
        this.fieldInfo = new FieldInfo(cf.getConstPool(), name, typeDesc);
    }

    CtField(FieldInfo fi, CtClass clazz) {
        super(clazz);
        this.fieldInfo = fi;
    }

    @Override // javassist.CtMember
    public String toString() {
        return getDeclaringClass().getName() + "." + getName() + ":" + this.fieldInfo.getDescriptor();
    }

    /* access modifiers changed from: protected */
    @Override // javassist.CtMember
    public void extendToString(StringBuffer buffer) {
        buffer.append(' ');
        buffer.append(getName());
        buffer.append(' ');
        buffer.append(this.fieldInfo.getDescriptor());
    }

    /* access modifiers changed from: protected */
    public ASTree getInitAST() {
        return null;
    }

    /* access modifiers changed from: package-private */
    public Initializer getInit() {
        ASTree tree = getInitAST();
        if (tree == null) {
            return null;
        }
        return Initializer.byExpr(tree);
    }

    public static CtField make(String src, CtClass declaring) throws CannotCompileException {
        try {
            CtMember obj = new Javac(declaring).compile(src);
            if (obj instanceof CtField) {
                return (CtField) obj;
            }
            throw new CannotCompileException("not a field");
        } catch (CompileError e) {
            throw new CannotCompileException(e);
        }
    }

    public FieldInfo getFieldInfo() {
        this.declaringClass.checkModify();
        return this.fieldInfo;
    }

    public FieldInfo getFieldInfo2() {
        return this.fieldInfo;
    }

    @Override // javassist.CtMember
    public CtClass getDeclaringClass() {
        return super.getDeclaringClass();
    }

    @Override // javassist.CtMember
    public String getName() {
        return this.fieldInfo.getName();
    }

    public void setName(String newName) {
        this.declaringClass.checkModify();
        this.fieldInfo.setName(newName);
    }

    @Override // javassist.CtMember
    public int getModifiers() {
        return AccessFlag.toModifier(this.fieldInfo.getAccessFlags());
    }

    @Override // javassist.CtMember
    public void setModifiers(int mod) {
        this.declaringClass.checkModify();
        this.fieldInfo.setAccessFlags(AccessFlag.of(mod));
    }

    @Override // javassist.CtMember
    public boolean hasAnnotation(String typeName) {
        FieldInfo fi = getFieldInfo2();
        return CtClassType.hasAnnotationType(typeName, getDeclaringClass().getClassPool(), (AnnotationsAttribute) fi.getAttribute(AnnotationsAttribute.invisibleTag), (AnnotationsAttribute) fi.getAttribute(AnnotationsAttribute.visibleTag));
    }

    @Override // javassist.CtMember
    public Object getAnnotation(Class<?> clz) throws ClassNotFoundException {
        FieldInfo fi = getFieldInfo2();
        return CtClassType.getAnnotationType(clz, getDeclaringClass().getClassPool(), (AnnotationsAttribute) fi.getAttribute(AnnotationsAttribute.invisibleTag), (AnnotationsAttribute) fi.getAttribute(AnnotationsAttribute.visibleTag));
    }

    @Override // javassist.CtMember
    public Object[] getAnnotations() throws ClassNotFoundException {
        return getAnnotations(false);
    }

    @Override // javassist.CtMember
    public Object[] getAvailableAnnotations() {
        try {
            return getAnnotations(true);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unexpected exception", e);
        }
    }

    private Object[] getAnnotations(boolean ignoreNotFound) throws ClassNotFoundException {
        FieldInfo fi = getFieldInfo2();
        return CtClassType.toAnnotationType(ignoreNotFound, getDeclaringClass().getClassPool(), (AnnotationsAttribute) fi.getAttribute(AnnotationsAttribute.invisibleTag), (AnnotationsAttribute) fi.getAttribute(AnnotationsAttribute.visibleTag));
    }

    @Override // javassist.CtMember
    public String getSignature() {
        return this.fieldInfo.getDescriptor();
    }

    @Override // javassist.CtMember
    public String getGenericSignature() {
        SignatureAttribute sa = (SignatureAttribute) this.fieldInfo.getAttribute(SignatureAttribute.tag);
        if (sa == null) {
            return null;
        }
        return sa.getSignature();
    }

    @Override // javassist.CtMember
    public void setGenericSignature(String sig) {
        this.declaringClass.checkModify();
        this.fieldInfo.addAttribute(new SignatureAttribute(this.fieldInfo.getConstPool(), sig));
    }

    public CtClass getType() throws NotFoundException {
        return Descriptor.toCtClass(this.fieldInfo.getDescriptor(), this.declaringClass.getClassPool());
    }

    public void setType(CtClass clazz) {
        this.declaringClass.checkModify();
        this.fieldInfo.setDescriptor(Descriptor.of(clazz));
    }

    public Object getConstantValue() {
        int index = this.fieldInfo.getConstantValue();
        if (index == 0) {
            return null;
        }
        ConstPool cp = this.fieldInfo.getConstPool();
        switch (cp.getTag(index)) {
            case 3:
                int value = cp.getIntegerInfo(index);
                if (!"Z".equals(this.fieldInfo.getDescriptor())) {
                    return Integer.valueOf(value);
                }
                return Boolean.valueOf(value != 0);
            case 4:
                return Float.valueOf(cp.getFloatInfo(index));
            case 5:
                return Long.valueOf(cp.getLongInfo(index));
            case 6:
                return Double.valueOf(cp.getDoubleInfo(index));
            case 7:
            default:
                throw new RuntimeException("bad tag: " + cp.getTag(index) + " at " + index);
            case 8:
                return cp.getStringInfo(index);
        }
    }

    @Override // javassist.CtMember
    public byte[] getAttribute(String name) {
        AttributeInfo ai = this.fieldInfo.getAttribute(name);
        if (ai == null) {
            return null;
        }
        return ai.get();
    }

    @Override // javassist.CtMember
    public void setAttribute(String name, byte[] data) {
        this.declaringClass.checkModify();
        this.fieldInfo.addAttribute(new AttributeInfo(this.fieldInfo.getConstPool(), name, data));
    }

    public static abstract class Initializer {
        /* access modifiers changed from: package-private */
        public abstract int compile(CtClass ctClass, String str, Bytecode bytecode, CtClass[] ctClassArr, Javac javac) throws CannotCompileException;

        /* access modifiers changed from: package-private */
        public abstract int compileIfStatic(CtClass ctClass, String str, Bytecode bytecode, Javac javac) throws CannotCompileException;

        public static Initializer constant(int i) {
            return new IntInitializer(i);
        }

        public static Initializer constant(boolean b) {
            return new IntInitializer(b ? 1 : 0);
        }

        public static Initializer constant(long l) {
            return new LongInitializer(l);
        }

        public static Initializer constant(float l) {
            return new FloatInitializer(l);
        }

        public static Initializer constant(double d) {
            return new DoubleInitializer(d);
        }

        public static Initializer constant(String s) {
            return new StringInitializer(s);
        }

        public static Initializer byParameter(int nth) {
            ParamInitializer i = new ParamInitializer();
            i.nthParam = nth;
            return i;
        }

        public static Initializer byNew(CtClass objectType) {
            NewInitializer i = new NewInitializer();
            i.objectType = objectType;
            i.stringParams = null;
            i.withConstructorParams = false;
            return i;
        }

        public static Initializer byNew(CtClass objectType, String[] stringParams) {
            NewInitializer i = new NewInitializer();
            i.objectType = objectType;
            i.stringParams = stringParams;
            i.withConstructorParams = false;
            return i;
        }

        public static Initializer byNewWithParams(CtClass objectType) {
            NewInitializer i = new NewInitializer();
            i.objectType = objectType;
            i.stringParams = null;
            i.withConstructorParams = true;
            return i;
        }

        public static Initializer byNewWithParams(CtClass objectType, String[] stringParams) {
            NewInitializer i = new NewInitializer();
            i.objectType = objectType;
            i.stringParams = stringParams;
            i.withConstructorParams = true;
            return i;
        }

        public static Initializer byCall(CtClass methodClass, String methodName) {
            MethodInitializer i = new MethodInitializer();
            i.objectType = methodClass;
            i.methodName = methodName;
            i.stringParams = null;
            i.withConstructorParams = false;
            return i;
        }

        public static Initializer byCall(CtClass methodClass, String methodName, String[] stringParams) {
            MethodInitializer i = new MethodInitializer();
            i.objectType = methodClass;
            i.methodName = methodName;
            i.stringParams = stringParams;
            i.withConstructorParams = false;
            return i;
        }

        public static Initializer byCallWithParams(CtClass methodClass, String methodName) {
            MethodInitializer i = new MethodInitializer();
            i.objectType = methodClass;
            i.methodName = methodName;
            i.stringParams = null;
            i.withConstructorParams = true;
            return i;
        }

        public static Initializer byCallWithParams(CtClass methodClass, String methodName, String[] stringParams) {
            MethodInitializer i = new MethodInitializer();
            i.objectType = methodClass;
            i.methodName = methodName;
            i.stringParams = stringParams;
            i.withConstructorParams = true;
            return i;
        }

        public static Initializer byNewArray(CtClass type, int size) throws NotFoundException {
            return new ArrayInitializer(type.getComponentType(), size);
        }

        public static Initializer byNewArray(CtClass type, int[] sizes) {
            return new MultiArrayInitializer(type, sizes);
        }

        public static Initializer byExpr(String source) {
            return new CodeInitializer(source);
        }

        static Initializer byExpr(ASTree source) {
            return new PtreeInitializer(source);
        }

        /* access modifiers changed from: package-private */
        public void check(String desc) throws CannotCompileException {
        }

        /* access modifiers changed from: package-private */
        public int getConstantValue(ConstPool cp, CtClass type) {
            return 0;
        }
    }

    static abstract class CodeInitializer0 extends Initializer {
        /* access modifiers changed from: package-private */
        public abstract void compileExpr(Javac javac) throws CompileError;

        CodeInitializer0() {
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
            try {
                code.addAload(0);
                compileExpr(drv);
                code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
                return code.getMaxStack();
            } catch (CompileError e) {
                throw new CannotCompileException(e);
            }
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
            try {
                compileExpr(drv);
                code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
                return code.getMaxStack();
            } catch (CompileError e) {
                throw new CannotCompileException(e);
            }
        }

        /* access modifiers changed from: package-private */
        public int getConstantValue2(ConstPool cp, CtClass type, ASTree tree) {
            if (type.isPrimitive()) {
                if (tree instanceof IntConst) {
                    long value = ((IntConst) tree).get();
                    if (type == CtClass.doubleType) {
                        return cp.addDoubleInfo((double) value);
                    }
                    if (type == CtClass.floatType) {
                        return cp.addFloatInfo((float) value);
                    }
                    if (type == CtClass.longType) {
                        return cp.addLongInfo(value);
                    }
                    if (type != CtClass.voidType) {
                        return cp.addIntegerInfo((int) value);
                    }
                } else if (tree instanceof DoubleConst) {
                    double value2 = ((DoubleConst) tree).get();
                    if (type == CtClass.floatType) {
                        return cp.addFloatInfo((float) value2);
                    }
                    if (type == CtClass.doubleType) {
                        return cp.addDoubleInfo(value2);
                    }
                }
            } else if ((tree instanceof StringL) && type.getName().equals(CtField.javaLangString)) {
                return cp.addStringInfo(((StringL) tree).get());
            }
            return 0;
        }
    }

    /* access modifiers changed from: package-private */
    public static class CodeInitializer extends CodeInitializer0 {
        private String expression;

        CodeInitializer(String expr) {
            this.expression = expr;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.CodeInitializer0
        public void compileExpr(Javac drv) throws CompileError {
            drv.compileExpr(this.expression);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int getConstantValue(ConstPool cp, CtClass type) {
            try {
                return getConstantValue2(cp, type, Javac.parseExpr(this.expression, new SymbolTable()));
            } catch (CompileError e) {
                return 0;
            }
        }
    }

    /* access modifiers changed from: package-private */
    public static class PtreeInitializer extends CodeInitializer0 {
        private ASTree expression;

        PtreeInitializer(ASTree expr) {
            this.expression = expr;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.CodeInitializer0
        public void compileExpr(Javac drv) throws CompileError {
            drv.compileExpr(this.expression);
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int getConstantValue(ConstPool cp, CtClass type) {
            return getConstantValue2(cp, type, this.expression);
        }
    }

    static class ParamInitializer extends Initializer {
        int nthParam;

        ParamInitializer() {
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
            if (parameters == null || this.nthParam >= parameters.length) {
                return 0;
            }
            code.addAload(0);
            int s = code.addLoad(nthParamToLocal(this.nthParam, parameters, false), type) + 1;
            code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
            return s;
        }

        static int nthParamToLocal(int nth, CtClass[] params, boolean isStatic) {
            int k;
            CtClass longType = CtClass.longType;
            CtClass doubleType = CtClass.doubleType;
            if (isStatic) {
                k = 0;
            } else {
                k = 1;
            }
            for (int i = 0; i < nth; i++) {
                CtClass type = params[i];
                if (type == longType || type == doubleType) {
                    k += 2;
                } else {
                    k++;
                }
            }
            return k;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
            return 0;
        }
    }

    static class NewInitializer extends Initializer {
        CtClass objectType;
        String[] stringParams;
        boolean withConstructorParams;

        NewInitializer() {
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
            int stacksize;
            code.addAload(0);
            code.addNew(this.objectType);
            code.add(89);
            code.addAload(0);
            if (this.stringParams == null) {
                stacksize = 4;
            } else {
                stacksize = compileStringParameter(code) + 4;
            }
            if (this.withConstructorParams) {
                stacksize += CtNewWrappedMethod.compileParameterList(code, parameters, 1);
            }
            code.addInvokespecial(this.objectType, "<init>", getDescriptor());
            code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
            return stacksize;
        }

        private String getDescriptor() {
            if (this.stringParams == null) {
                if (this.withConstructorParams) {
                    return "(Ljava/lang/Object;[Ljava/lang/Object;)V";
                }
                return "(Ljava/lang/Object;)V";
            } else if (this.withConstructorParams) {
                return "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)V";
            } else {
                return "(Ljava/lang/Object;[Ljava/lang/String;)V";
            }
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
            String desc;
            code.addNew(this.objectType);
            code.add(89);
            int stacksize = 2;
            if (this.stringParams == null) {
                desc = "()V";
            } else {
                desc = "([Ljava/lang/String;)V";
                stacksize = 2 + compileStringParameter(code);
            }
            code.addInvokespecial(this.objectType, "<init>", desc);
            code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
            return stacksize;
        }

        /* access modifiers changed from: protected */
        public final int compileStringParameter(Bytecode code) throws CannotCompileException {
            int nparam = this.stringParams.length;
            code.addIconst(nparam);
            code.addAnewarray(CtField.javaLangString);
            for (int j = 0; j < nparam; j++) {
                code.add(89);
                code.addIconst(j);
                code.addLdc(this.stringParams[j]);
                code.add(83);
            }
            return 4;
        }
    }

    static class MethodInitializer extends NewInitializer {
        String methodName;

        MethodInitializer() {
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.NewInitializer, javassist.CtField.Initializer
        public int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
            int stacksize;
            code.addAload(0);
            code.addAload(0);
            if (this.stringParams == null) {
                stacksize = 2;
            } else {
                stacksize = compileStringParameter(code) + 2;
            }
            if (this.withConstructorParams) {
                stacksize += CtNewWrappedMethod.compileParameterList(code, parameters, 1);
            }
            String typeDesc = Descriptor.of(type);
            code.addInvokestatic(this.objectType, this.methodName, getDescriptor() + typeDesc);
            code.addPutfield(Bytecode.THIS, name, typeDesc);
            return stacksize;
        }

        private String getDescriptor() {
            if (this.stringParams == null) {
                if (this.withConstructorParams) {
                    return "(Ljava/lang/Object;[Ljava/lang/Object;)";
                }
                return "(Ljava/lang/Object;)";
            } else if (this.withConstructorParams) {
                return "(Ljava/lang/Object;[Ljava/lang/String;[Ljava/lang/Object;)";
            } else {
                return "(Ljava/lang/Object;[Ljava/lang/String;)";
            }
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.NewInitializer, javassist.CtField.Initializer
        public int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
            String desc;
            int stacksize = 1;
            if (this.stringParams == null) {
                desc = "()";
            } else {
                desc = "([Ljava/lang/String;)";
                stacksize = 1 + compileStringParameter(code);
            }
            String typeDesc = Descriptor.of(type);
            code.addInvokestatic(this.objectType, this.methodName, desc + typeDesc);
            code.addPutstatic(Bytecode.THIS, name, typeDesc);
            return stacksize;
        }
    }

    static class IntInitializer extends Initializer {
        int value;

        IntInitializer(int v) {
            this.value = v;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public void check(String desc) throws CannotCompileException {
            char c = desc.charAt(0);
            if (c != 'I' && c != 'S' && c != 'B' && c != 'C' && c != 'Z') {
                throw new CannotCompileException("type mismatch");
            }
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
            code.addAload(0);
            code.addIconst(this.value);
            code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
            return 2;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
            code.addIconst(this.value);
            code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
            return 1;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int getConstantValue(ConstPool cp, CtClass type) {
            return cp.addIntegerInfo(this.value);
        }
    }

    static class LongInitializer extends Initializer {
        long value;

        LongInitializer(long v) {
            this.value = v;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public void check(String desc) throws CannotCompileException {
            if (!desc.equals("J")) {
                throw new CannotCompileException("type mismatch");
            }
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
            code.addAload(0);
            code.addLdc2w(this.value);
            code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
            return 3;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
            code.addLdc2w(this.value);
            code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
            return 2;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int getConstantValue(ConstPool cp, CtClass type) {
            if (type == CtClass.longType) {
                return cp.addLongInfo(this.value);
            }
            return 0;
        }
    }

    static class FloatInitializer extends Initializer {
        float value;

        FloatInitializer(float v) {
            this.value = v;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public void check(String desc) throws CannotCompileException {
            if (!desc.equals("F")) {
                throw new CannotCompileException("type mismatch");
            }
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
            code.addAload(0);
            code.addFconst(this.value);
            code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
            return 3;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
            code.addFconst(this.value);
            code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
            return 2;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int getConstantValue(ConstPool cp, CtClass type) {
            if (type == CtClass.floatType) {
                return cp.addFloatInfo(this.value);
            }
            return 0;
        }
    }

    static class DoubleInitializer extends Initializer {
        double value;

        DoubleInitializer(double v) {
            this.value = v;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public void check(String desc) throws CannotCompileException {
            if (!desc.equals("D")) {
                throw new CannotCompileException("type mismatch");
            }
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
            code.addAload(0);
            code.addLdc2w(this.value);
            code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
            return 3;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
            code.addLdc2w(this.value);
            code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
            return 2;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int getConstantValue(ConstPool cp, CtClass type) {
            if (type == CtClass.doubleType) {
                return cp.addDoubleInfo(this.value);
            }
            return 0;
        }
    }

    static class StringInitializer extends Initializer {
        String value;

        StringInitializer(String v) {
            this.value = v;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int compile(CtClass type, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
            code.addAload(0);
            code.addLdc(this.value);
            code.addPutfield(Bytecode.THIS, name, Descriptor.of(type));
            return 2;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int compileIfStatic(CtClass type, String name, Bytecode code, Javac drv) throws CannotCompileException {
            code.addLdc(this.value);
            code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type));
            return 1;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int getConstantValue(ConstPool cp, CtClass type) {
            if (type.getName().equals(CtField.javaLangString)) {
                return cp.addStringInfo(this.value);
            }
            return 0;
        }
    }

    static class ArrayInitializer extends Initializer {
        int size;
        CtClass type;

        ArrayInitializer(CtClass t, int s) {
            this.type = t;
            this.size = s;
        }

        private void addNewarray(Bytecode code) {
            if (this.type.isPrimitive()) {
                code.addNewarray(((CtPrimitiveType) this.type).getArrayType(), this.size);
            } else {
                code.addAnewarray(this.type, this.size);
            }
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int compile(CtClass type2, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
            code.addAload(0);
            addNewarray(code);
            code.addPutfield(Bytecode.THIS, name, Descriptor.of(type2));
            return 2;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int compileIfStatic(CtClass type2, String name, Bytecode code, Javac drv) throws CannotCompileException {
            addNewarray(code);
            code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type2));
            return 1;
        }
    }

    static class MultiArrayInitializer extends Initializer {
        int[] dim;
        CtClass type;

        MultiArrayInitializer(CtClass t, int[] d) {
            this.type = t;
            this.dim = d;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public void check(String desc) throws CannotCompileException {
            if (desc.charAt(0) != '[') {
                throw new CannotCompileException("type mismatch");
            }
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int compile(CtClass type2, String name, Bytecode code, CtClass[] parameters, Javac drv) throws CannotCompileException {
            code.addAload(0);
            int s = code.addMultiNewarray(type2, this.dim);
            code.addPutfield(Bytecode.THIS, name, Descriptor.of(type2));
            return s + 1;
        }

        /* access modifiers changed from: package-private */
        @Override // javassist.CtField.Initializer
        public int compileIfStatic(CtClass type2, String name, Bytecode code, Javac drv) throws CannotCompileException {
            int s = code.addMultiNewarray(type2, this.dim);
            code.addPutstatic(Bytecode.THIS, name, Descriptor.of(type2));
            return s;
        }
    }
}
