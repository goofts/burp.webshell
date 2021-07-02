package javassist;

import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.MethodInfo;
import javassist.compiler.CompileError;
import javassist.compiler.Javac;

public final class CtConstructor extends CtBehavior {
    protected CtConstructor(MethodInfo minfo, CtClass declaring) {
        super(declaring, minfo);
    }

    public CtConstructor(CtClass[] parameters, CtClass declaring) {
        this((MethodInfo) null, declaring);
        this.methodInfo = new MethodInfo(declaring.getClassFile2().getConstPool(), "<init>", Descriptor.ofConstructor(parameters));
        setModifiers(1);
    }

    public CtConstructor(CtConstructor src, CtClass declaring, ClassMap map) throws CannotCompileException {
        this((MethodInfo) null, declaring);
        copy(src, true, map);
    }

    public boolean isConstructor() {
        return this.methodInfo.isConstructor();
    }

    public boolean isClassInitializer() {
        return this.methodInfo.isStaticInitializer();
    }

    @Override // javassist.CtBehavior
    public String getLongName() {
        String str;
        StringBuilder append = new StringBuilder().append(getDeclaringClass().getName());
        if (isConstructor()) {
            str = Descriptor.toString(getSignature());
        } else {
            str = ".<clinit>()";
        }
        return append.append(str).toString();
    }

    @Override // javassist.CtMember
    public String getName() {
        if (this.methodInfo.isStaticInitializer()) {
            return MethodInfo.nameClinit;
        }
        return this.declaringClass.getSimpleName();
    }

    @Override // javassist.CtBehavior
    public boolean isEmpty() {
        int desc;
        CodeAttribute ca = getMethodInfo2().getCodeAttribute();
        if (ca == null) {
            return false;
        }
        ConstPool cp = ca.getConstPool();
        CodeIterator it = ca.iterator();
        try {
            int op0 = it.byteAt(it.next());
            if (op0 != 177) {
                if (op0 != 42) {
                    return false;
                }
                int pos = it.next();
                if (it.byteAt(pos) != 183 || (desc = cp.isConstructor(getSuperclassName(), it.u16bitAt(pos + 1))) == 0 || !"()V".equals(cp.getUtf8Info(desc)) || it.byteAt(it.next()) != 177 || it.hasNext()) {
                    return false;
                }
            }
            return true;
        } catch (BadBytecode e) {
            return false;
        }
    }

    private String getSuperclassName() {
        return this.declaringClass.getClassFile2().getSuperclass();
    }

    public boolean callsSuper() throws CannotCompileException {
        CodeAttribute codeAttr = this.methodInfo.getCodeAttribute();
        if (codeAttr == null) {
            return false;
        }
        try {
            if (codeAttr.iterator().skipSuperConstructor() >= 0) {
                return true;
            }
            return false;
        } catch (BadBytecode e) {
            throw new CannotCompileException(e);
        }
    }

    @Override // javassist.CtBehavior
    public void setBody(String src) throws CannotCompileException {
        if (src == null) {
            if (isClassInitializer()) {
                src = ";";
            } else {
                src = "super();";
            }
        }
        super.setBody(src);
    }

    public void setBody(CtConstructor src, ClassMap map) throws CannotCompileException {
        setBody0(src.declaringClass, src.methodInfo, this.declaringClass, this.methodInfo, map);
    }

    public void insertBeforeBody(String src) throws CannotCompileException {
        CtClass cc = this.declaringClass;
        cc.checkModify();
        if (isClassInitializer()) {
            throw new CannotCompileException("class initializer");
        }
        CodeAttribute ca = this.methodInfo.getCodeAttribute();
        CodeIterator iterator = ca.iterator();
        Bytecode b = new Bytecode(this.methodInfo.getConstPool(), ca.getMaxStack(), ca.getMaxLocals());
        b.setStackDepth(ca.getMaxStack());
        Javac jv = new Javac(b, cc);
        try {
            jv.recordParams(getParameterTypes(), false);
            jv.compileStmnt(src);
            ca.setMaxStack(b.getMaxStack());
            ca.setMaxLocals(b.getMaxLocals());
            iterator.skipConstructor();
            iterator.insert(b.getExceptionTable(), iterator.insertEx(b.get()));
            this.methodInfo.rebuildStackMapIf6(cc.getClassPool(), cc.getClassFile2());
        } catch (NotFoundException e) {
            throw new CannotCompileException(e);
        } catch (CompileError e2) {
            throw new CannotCompileException(e2);
        } catch (BadBytecode e3) {
            throw new CannotCompileException(e3);
        }
    }

    /* access modifiers changed from: package-private */
    @Override // javassist.CtBehavior
    public int getStartPosOfBody(CodeAttribute ca) throws CannotCompileException {
        CodeIterator ci = ca.iterator();
        try {
            ci.skipConstructor();
            return ci.next();
        } catch (BadBytecode e) {
            throw new CannotCompileException(e);
        }
    }

    public CtMethod toMethod(String name, CtClass declaring) throws CannotCompileException {
        return toMethod(name, declaring, null);
    }

    public CtMethod toMethod(String name, CtClass declaring, ClassMap map) throws CannotCompileException {
        CodeAttribute ca;
        CtMethod method = new CtMethod(null, declaring);
        method.copy(this, false, map);
        if (isConstructor() && (ca = method.getMethodInfo2().getCodeAttribute()) != null) {
            removeConsCall(ca);
            try {
                this.methodInfo.rebuildStackMapIf6(declaring.getClassPool(), declaring.getClassFile2());
            } catch (BadBytecode e) {
                throw new CannotCompileException(e);
            }
        }
        method.setName(name);
        return method;
    }

    private static void removeConsCall(CodeAttribute ca) throws CannotCompileException {
        int pos;
        CodeIterator iterator = ca.iterator();
        try {
            int pos2 = iterator.skipConstructor();
            if (pos2 >= 0) {
                String desc = ca.getConstPool().getMethodrefType(iterator.u16bitAt(pos2 + 1));
                int num = Descriptor.numOfParameters(desc) + 1;
                if (num > 3) {
                    pos = iterator.insertGapAt(pos2, num - 3, false).position;
                } else {
                    pos = pos2;
                }
                int pos3 = pos + 1;
                iterator.writeByte(87, pos);
                iterator.writeByte(0, pos3);
                iterator.writeByte(0, pos3 + 1);
                Descriptor.Iterator it = new Descriptor.Iterator(desc);
                while (true) {
                    it.next();
                    if (it.isParameter()) {
                        pos3++;
                        iterator.writeByte(it.is2byte() ? 88 : 87, pos3);
                    } else {
                        return;
                    }
                }
            }
        } catch (BadBytecode e) {
            throw new CannotCompileException(e);
        }
    }
}
