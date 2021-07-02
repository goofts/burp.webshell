package javassist.convert;

import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.Bytecode;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.Opcode;

public class TransformBefore extends TransformCall {
    protected byte[] loadCode = null;
    protected int locals = 0;
    protected int maxLocals = 0;
    protected CtClass[] parameterTypes;
    protected byte[] saveCode = null;

    public TransformBefore(Transformer next, CtMethod origMethod, CtMethod beforeMethod) throws NotFoundException {
        super(next, origMethod, beforeMethod);
        this.methodDescriptor = origMethod.getMethodInfo2().getDescriptor();
        this.parameterTypes = origMethod.getParameterTypes();
    }

    @Override // javassist.convert.Transformer, javassist.convert.TransformCall
    public void initialize(ConstPool cp, CodeAttribute attr) {
        super.initialize(cp, attr);
        this.locals = 0;
        this.maxLocals = attr.getMaxLocals();
        this.loadCode = null;
        this.saveCode = null;
    }

    /* access modifiers changed from: protected */
    @Override // javassist.convert.TransformCall
    public int match(int c, int pos, CodeIterator iterator, int typedesc, ConstPool cp) throws BadBytecode {
        if (this.newIndex == 0) {
            this.newIndex = cp.addMethodrefInfo(cp.addClassInfo(this.newClassname), cp.addNameAndTypeInfo(this.newMethodname, Descriptor.insertParameter(this.classname, Descriptor.ofParameters(this.parameterTypes) + 'V')));
            this.constPool = cp;
        }
        if (this.saveCode == null) {
            makeCode(this.parameterTypes, cp);
        }
        return match2(pos, iterator);
    }

    /* access modifiers changed from: protected */
    public int match2(int pos, CodeIterator iterator) throws BadBytecode {
        iterator.move(pos);
        iterator.insert(this.saveCode);
        iterator.insert(this.loadCode);
        int p = iterator.insertGap(3);
        iterator.writeByte(Opcode.INVOKESTATIC, p);
        iterator.write16bit(this.newIndex, p + 1);
        iterator.insert(this.loadCode);
        return iterator.next();
    }

    @Override // javassist.convert.Transformer
    public int extraLocals() {
        return this.locals;
    }

    /* access modifiers changed from: protected */
    public void makeCode(CtClass[] paramTypes, ConstPool cp) {
        Bytecode save = new Bytecode(cp, 0, 0);
        Bytecode load = new Bytecode(cp, 0, 0);
        int var = this.maxLocals;
        int len = paramTypes == null ? 0 : paramTypes.length;
        load.addAload(var);
        makeCode2(save, load, 0, len, paramTypes, var + 1);
        save.addAstore(var);
        this.saveCode = save.get();
        this.loadCode = load.get();
    }

    private void makeCode2(Bytecode save, Bytecode load, int i, int n, CtClass[] paramTypes, int var) {
        if (i < n) {
            makeCode2(save, load, i + 1, n, paramTypes, var + load.addLoad(var, paramTypes[i]));
            save.addStore(var, paramTypes[i]);
            return;
        }
        this.locals = var - this.maxLocals;
    }
}
