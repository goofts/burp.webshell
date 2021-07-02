package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class DeprecatedAttribute extends AttributeInfo {
    public static final String tag = "Deprecated";

    DeprecatedAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
        super(cp, n, in);
    }

    public DeprecatedAttribute(ConstPool cp) {
        super(cp, tag, new byte[0]);
    }

    @Override // javassist.bytecode.AttributeInfo
    public AttributeInfo copy(ConstPool newCp, Map<String, String> map) {
        return new DeprecatedAttribute(newCp);
    }
}
