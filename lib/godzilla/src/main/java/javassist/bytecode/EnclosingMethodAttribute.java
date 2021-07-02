package javassist.bytecode;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Map;

public class EnclosingMethodAttribute extends AttributeInfo {
    public static final String tag = "EnclosingMethod";

    EnclosingMethodAttribute(ConstPool cp, int n, DataInputStream in) throws IOException {
        super(cp, n, in);
    }

    public EnclosingMethodAttribute(ConstPool cp, String className, String methodName, String methodDesc) {
        super(cp, tag);
        int ci = cp.addClassInfo(className);
        int ni = cp.addNameAndTypeInfo(methodName, methodDesc);
        set(new byte[]{(byte) (ci >>> 8), (byte) ci, (byte) (ni >>> 8), (byte) ni});
    }

    public EnclosingMethodAttribute(ConstPool cp, String className) {
        super(cp, tag);
        int ci = cp.addClassInfo(className);
        set(new byte[]{(byte) (ci >>> 8), (byte) ci, (byte) 0, (byte) 0});
    }

    public int classIndex() {
        return ByteArray.readU16bit(get(), 0);
    }

    public int methodIndex() {
        return ByteArray.readU16bit(get(), 2);
    }

    public String className() {
        return getConstPool().getClassInfo(classIndex());
    }

    public String methodName() {
        ConstPool cp = getConstPool();
        int mi = methodIndex();
        if (mi == 0) {
            return MethodInfo.nameClinit;
        }
        return cp.getUtf8Info(cp.getNameAndTypeName(mi));
    }

    public String methodDescriptor() {
        ConstPool cp = getConstPool();
        return cp.getUtf8Info(cp.getNameAndTypeDescriptor(methodIndex()));
    }

    @Override // javassist.bytecode.AttributeInfo
    public AttributeInfo copy(ConstPool newCp, Map<String, String> map) {
        if (methodIndex() == 0) {
            return new EnclosingMethodAttribute(newCp, className());
        }
        return new EnclosingMethodAttribute(newCp, className(), methodName(), methodDescriptor());
    }
}
