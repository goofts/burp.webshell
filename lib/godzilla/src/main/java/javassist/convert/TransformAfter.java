package javassist.convert;

import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.BadBytecode;
import javassist.bytecode.CodeIterator;
import javassist.bytecode.Opcode;

public class TransformAfter extends TransformBefore {
    public TransformAfter(Transformer next, CtMethod origMethod, CtMethod afterMethod) throws NotFoundException {
        super(next, origMethod, afterMethod);
    }

    /* access modifiers changed from: protected */
    @Override // javassist.convert.TransformBefore
    public int match2(int pos, CodeIterator iterator) throws BadBytecode {
        iterator.move(pos);
        iterator.insert(this.saveCode);
        iterator.insert(this.loadCode);
        iterator.setMark(iterator.insertGap(3));
        iterator.insert(this.loadCode);
        int pos2 = iterator.next();
        int p = iterator.getMark();
        iterator.writeByte(iterator.byteAt(pos2), p);
        iterator.write16bit(iterator.u16bitAt(pos2 + 1), p + 1);
        iterator.writeByte(Opcode.INVOKESTATIC, pos2);
        iterator.write16bit(this.newIndex, pos2 + 1);
        iterator.move(p);
        return iterator.next();
    }
}
