package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class SourceFileAttribute extends AttributeInfo {
    public static final String tag = "SourceFile";

    SourceFileAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
        super(cp, n, in);
    }

    public SourceFileAttribute(ConstPool cp, String filename) {
        super(cp, tag);
        int index = cp.addUtf8Info(filename);
        set(new byte[]{(byte) (index >>> 8), (byte) index});
    }

    public String getFileName() {
        return getConstPool().getUtf8Info(ByteArray.readU16bit(get(), 0));
    }

    @Override // javassist.bytecode.AttributeInfo
    public AttributeInfo copy(ConstPool newCp, Map<String, String> map) {
        return new SourceFileAttribute(newCp, getFileName());
    }
}
