package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;

/* access modifiers changed from: package-private */
/* compiled from: ConstPool */
public class FieldrefInfo extends MemberrefInfo {
    static final int tag = 9;

    public FieldrefInfo(int cindex, int ntindex, int thisIndex) {
        super(cindex, ntindex, thisIndex);
    }

    public FieldrefInfo(DataInputStream in, int thisIndex) throws IOException {
        super(in, thisIndex);
    }

    @Override // javassist.bytecode.ConstInfo
    public int getTag() {
        return 9;
    }

    @Override // javassist.bytecode.MemberrefInfo
    public String getTagName() {
        return "Field";
    }

    /* access modifiers changed from: protected */
    @Override // javassist.bytecode.MemberrefInfo
    public int copy2(ConstPool dest, int cindex, int ntindex) {
        return dest.addFieldrefInfo(cindex, ntindex);
    }
}
