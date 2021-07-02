package javassist.bytecode;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/* access modifiers changed from: package-private */
/* compiled from: ConstPool */
public abstract class ConstInfo {
    int index;

    public abstract int copy(ConstPool constPool, ConstPool constPool2, Map<String, String> map);

    public abstract int getTag();

    public abstract void print(PrintWriter printWriter);

    public abstract void write(DataOutputStream dataOutputStream) throws IOException;

    public ConstInfo(int i) {
        this.index = i;
    }

    public String getClassName(ConstPool cp) {
        return null;
    }

    public void renameClass(ConstPool cp, String oldName, String newName, Map<ConstInfo, ConstInfo> map) {
    }

    public void renameClass(ConstPool cp, Map<String, String> map, Map<ConstInfo, ConstInfo> map2) {
    }

    public String toString() {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        print(new PrintWriter(bout));
        return bout.toString();
    }
}
