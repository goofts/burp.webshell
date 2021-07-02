package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class ConstantAttribute extends AttributeInfo {
    public static final String tag = "ConstantValue";

    ConstantAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
        super(cp, n, in);
    }

    public ConstantAttribute(ConstPool cp, int index) {
        super(cp, tag);
        set(new byte[]{(byte) (index >>> 8), (byte) index});
    }

    public int getConstantValue() {
        return ByteArray.readU16bit(get(), 0);
    }

    @Override // javassist.bytecode.AttributeInfo
    public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
        return new ConstantAttribute(newCp, getConstPool().copy(getConstantValue(), newCp, classnames));
    }
}
