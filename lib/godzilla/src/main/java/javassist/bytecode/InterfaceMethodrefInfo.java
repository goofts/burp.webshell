package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;

/* access modifiers changed from: package-private */
/* compiled from: ConstPool */
public class InterfaceMethodrefInfo extends MemberrefInfo {
    static final int tag = 11;

    public InterfaceMethodrefInfo(int cindex, int ntindex, int thisIndex) {
        super(cindex, ntindex, thisIndex);
    }

    public InterfaceMethodrefInfo(DataInputStream in, int thisIndex) throws IOException {
        super(in, thisIndex);
    }

    @Override // javassist.bytecode.ConstInfo
    public int getTag() {
        return 11;
    }

    @Override // javassist.bytecode.MemberrefInfo
    public String getTagName() {
        return "Interface";
    }

    /* access modifiers changed from: protected */
    @Override // javassist.bytecode.MemberrefInfo
    public int copy2(ConstPool dest, int cindex, int ntindex) {
        return dest.addInterfaceMethodrefInfo(cindex, ntindex);
    }
}
