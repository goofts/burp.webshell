package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class NestMembersAttribute extends AttributeInfo {
    public static final String tag = "NestMembers";

    NestMembersAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
        super(cp, n, in);
    }

    private NestMembersAttribute(ConstPool cp, byte[] info) {
        super(cp, tag, info);
    }

    @Override // javassist.bytecode.AttributeInfo
    public AttributeInfo copy(ConstPool newCp, Map<String, String> classnames) {
        byte[] src = get();
        byte[] dest = new byte[src.length];
        ConstPool cp = getConstPool();
        int n = ByteArray.readU16bit(src, 0);
        ByteArray.write16bit(n, dest, 0);
        int i = 0;
        int j = 2;
        while (i < n) {
            ByteArray.write16bit(cp.copy(ByteArray.readU16bit(src, j), newCp, classnames), dest, j);
            i++;
            j += 2;
        }
        return new NestMembersAttribute(newCp, dest);
    }

    public int numberOfClasses() {
        return ByteArray.readU16bit(this.info, 0);
    }

    public int memberClass(int index) {
        return ByteArray.readU16bit(this.info, (index * 2) + 2);
    }
}
