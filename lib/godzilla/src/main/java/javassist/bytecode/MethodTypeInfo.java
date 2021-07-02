package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/* access modifiers changed from: package-private */
/* compiled from: ConstPool */
public class MethodTypeInfo extends ConstInfo {
    static final int tag = 16;
    int descriptor;

    public MethodTypeInfo(int desc, int index) {
        super(index);
        this.descriptor = desc;
    }

    public MethodTypeInfo(DataInputStream in, int index) throws IOException {
        super(index);
        this.descriptor = in.readUnsignedShort();
    }

    public int hashCode() {
        return this.descriptor;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof MethodTypeInfo) || ((MethodTypeInfo) obj).descriptor != this.descriptor) {
            return false;
        }
        return true;
    }

    @Override // javassist.bytecode.ConstInfo
    public int getTag() {
        return 16;
    }

    @Override // javassist.bytecode.ConstInfo
    public void renameClass(ConstPool cp, String oldName, String newName, Map<ConstInfo, ConstInfo> cache) {
        String desc = cp.getUtf8Info(this.descriptor);
        String desc2 = Descriptor.rename(desc, oldName, newName);
        if (desc == desc2) {
            return;
        }
        if (cache == null) {
            this.descriptor = cp.addUtf8Info(desc2);
            return;
        }
        cache.remove(this);
        this.descriptor = cp.addUtf8Info(desc2);
        cache.put(this, this);
    }

    @Override // javassist.bytecode.ConstInfo
    public void renameClass(ConstPool cp, Map<String, String> map, Map<ConstInfo, ConstInfo> cache) {
        String desc = cp.getUtf8Info(this.descriptor);
        String desc2 = Descriptor.rename(desc, map);
        if (desc == desc2) {
            return;
        }
        if (cache == null) {
            this.descriptor = cp.addUtf8Info(desc2);
            return;
        }
        cache.remove(this);
        this.descriptor = cp.addUtf8Info(desc2);
        cache.put(this, this);
    }

    @Override // javassist.bytecode.ConstInfo
    public int copy(ConstPool src, ConstPool dest, Map<String, String> map) {
        return dest.addMethodTypeInfo(dest.addUtf8Info(Descriptor.rename(src.getUtf8Info(this.descriptor), map)));
    }

    @Override // javassist.bytecode.ConstInfo
    public void write(DataOutputStream out) throws IOException {
        out.writeByte(16);
        out.writeShort(this.descriptor);
    }

    @Override // javassist.bytecode.ConstInfo
    public void print(PrintWriter out) {
        out.print("MethodType #");
        out.println(this.descriptor);
    }
}
