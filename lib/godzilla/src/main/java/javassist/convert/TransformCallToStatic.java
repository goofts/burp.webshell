package javassist.convert;

import javassist.CtMethod;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.ConstPool;
import javassist.bytecode.Descriptor;
import javassist.bytecode.Opcode;

public class TransformCallToStatic extends TransformCall {
    public TransformCallToStatic(Transformer next, CtMethod origMethod, CtMethod substMethod) {
        super(next, origMethod, substMethod);
        this.methodDescriptor = origMethod.getMethodInfo2().getDescriptor();
    }

    /* access modifiers changed from: protected */
    @Override // javassist.convert.TransformCall
    public int match(int c, int pos, CodeIterator iterator, int typedesc, ConstPool cp) {
        if (this.newIndex == 0) {
            this.newIndex = cp.addMethodrefInfo(cp.addClassInfo(this.newClassname), cp.addNameAndTypeInfo(this.newMethodname, Descriptor.insertParameter(this.classname, this.methodDescriptor)));
            this.constPool = cp;
        }
        iterator.writeByte(Opcode.INVOKESTATIC, pos);
        iterator.write16bit(this.newIndex, pos + 1);
        return pos;
    }
}
