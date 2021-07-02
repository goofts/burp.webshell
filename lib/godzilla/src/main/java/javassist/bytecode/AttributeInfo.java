package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AttributeInfo {
    protected ConstPool constPool;
    byte[] info;
    int name;

    protected AttributeInfo(ConstPool cp, int attrname, byte[] attrinfo) {
        this.constPool = cp;
        this.name = attrname;
        this.info = attrinfo;
    }

    protected AttributeInfo(ConstPool cp, String attrname) {
        this(cp, attrname, (byte[]) null);
    }

    public AttributeInfo(ConstPool cp, String attrname, byte[] attrinfo) {
        this(cp, cp.addUtf8Info(attrname), attrinfo);
    }

    protected AttributeInfo(ConstPool cp, int n, DataInputStream in) throws IOException {
        this.constPool = cp;
        this.name = n;
        int len = in.readInt();
        this.info = new byte[len];
        if (len > 0) {
            in.readFully(this.info);
        }
    }

    static AttributeInfo read(ConstPool cp, DataInputStream in) throws IOException {
        int name2 = in.readUnsignedShort();
        String nameStr = cp.getUtf8Info(name2);
        char first = nameStr.charAt(0);
        if (first < 'E') {
            if (nameStr.equals(AnnotationDefaultAttribute.tag)) {
                return new AnnotationDefaultAttribute(cp, name2, in);
            }
            if (nameStr.equals(BootstrapMethodsAttribute.tag)) {
                return new BootstrapMethodsAttribute(cp, name2, in);
            }
            if (nameStr.equals(CodeAttribute.tag)) {
                return new CodeAttribute(cp, name2, in);
            }
            if (nameStr.equals(ConstantAttribute.tag)) {
                return new ConstantAttribute(cp, name2, in);
            }
            if (nameStr.equals(DeprecatedAttribute.tag)) {
                return new DeprecatedAttribute(cp, name2, in);
            }
        }
        if (first < 'M') {
            if (nameStr.equals(EnclosingMethodAttribute.tag)) {
                return new EnclosingMethodAttribute(cp, name2, in);
            }
            if (nameStr.equals(ExceptionsAttribute.tag)) {
                return new ExceptionsAttribute(cp, name2, in);
            }
            if (nameStr.equals(InnerClassesAttribute.tag)) {
                return new InnerClassesAttribute(cp, name2, in);
            }
            if (nameStr.equals(LineNumberAttribute.tag)) {
                return new LineNumberAttribute(cp, name2, in);
            }
            if (nameStr.equals(LocalVariableAttribute.tag)) {
                return new LocalVariableAttribute(cp, name2, in);
            }
            if (nameStr.equals("LocalVariableTypeTable")) {
                return new LocalVariableTypeAttribute(cp, name2, in);
            }
        }
        if (first < 'S') {
            if (nameStr.equals(MethodParametersAttribute.tag)) {
                return new MethodParametersAttribute(cp, name2, in);
            }
            if (nameStr.equals(NestHostAttribute.tag)) {
                return new NestHostAttribute(cp, name2, in);
            }
            if (nameStr.equals(NestMembersAttribute.tag)) {
                return new NestMembersAttribute(cp, name2, in);
            }
            if (nameStr.equals(AnnotationsAttribute.visibleTag) || nameStr.equals(AnnotationsAttribute.invisibleTag)) {
                return new AnnotationsAttribute(cp, name2, in);
            }
            if (nameStr.equals(ParameterAnnotationsAttribute.visibleTag) || nameStr.equals(ParameterAnnotationsAttribute.invisibleTag)) {
                return new ParameterAnnotationsAttribute(cp, name2, in);
            }
            if (nameStr.equals(TypeAnnotationsAttribute.visibleTag) || nameStr.equals(TypeAnnotationsAttribute.invisibleTag)) {
                return new TypeAnnotationsAttribute(cp, name2, in);
            }
        }
        if (first >= 'S') {
            if (nameStr.equals(SignatureAttribute.tag)) {
                return new SignatureAttribute(cp, name2, in);
            }
            if (nameStr.equals(SourceFileAttribute.tag)) {
                return new SourceFileAttribute(cp, name2, in);
            }
            if (nameStr.equals(SyntheticAttribute.tag)) {
                return new SyntheticAttribute(cp, name2, in);
            }
            if (nameStr.equals(StackMap.tag)) {
                return new StackMap(cp, name2, in);
            }
            if (nameStr.equals(StackMapTable.tag)) {
                return new StackMapTable(cp, name2, in);
            }
        }
        return new AttributeInfo(cp, name2, in);
    }

    public String getName() {
        return this.constPool.getUtf8Info(this.name);
    }

    public ConstPool getConstPool() {
        return this.constPool;
    }

    public int length() {
        return this.info.length + 6;
    }

    public byte[] get() {
        return this.info;
    }

    public void set(byte[] newinfo) {
        this.info = newinfo;
    }

    public AttributeInfo copy(ConstPool newCp, Map<String, String> map) {
        return new AttributeInfo(newCp, getName(), Arrays.copyOf(this.info, this.info.length));
    }

    /* access modifiers changed from: package-private */
    public void write(DataOutputStream out) throws IOException {
        out.writeShort(this.name);
        out.writeInt(this.info.length);
        if (this.info.length > 0) {
            out.write(this.info);
        }
    }

    static int getLength(List<AttributeInfo> attributes) {
        int size = 0;
        for (AttributeInfo attr : attributes) {
            size += attr.length();
        }
        return size;
    }

    static AttributeInfo lookup(List<AttributeInfo> attributes, String name2) {
        if (attributes == null) {
            return null;
        }
        for (AttributeInfo ai : attributes) {
            if (ai.getName().equals(name2)) {
                return ai;
            }
        }
        return null;
    }

    static synchronized AttributeInfo remove(List<AttributeInfo> attributes, String name2) {
        AttributeInfo ai;
        synchronized (AttributeInfo.class) {
            if (attributes != null) {
                Iterator<AttributeInfo> it = attributes.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        ai = null;
                        break;
                    }
                    ai = it.next();
                    if (ai.getName().equals(name2) && attributes.remove(ai)) {
                        break;
                    }
                }
            } else {
                ai = null;
            }
        }
        return ai;
    }

    static void writeAll(List<AttributeInfo> attributes, DataOutputStream out) throws IOException {
        if (attributes != null) {
            for (AttributeInfo attr : attributes) {
                attr.write(out);
            }
        }
    }

    static List<AttributeInfo> copyAll(List<AttributeInfo> attributes, ConstPool cp) {
        if (attributes == null) {
            return null;
        }
        List<AttributeInfo> newList = new ArrayList<>();
        for (AttributeInfo attr : attributes) {
            newList.add(attr.copy(cp, null));
        }
        return newList;
    }

    /* access modifiers changed from: package-private */
    public void renameClass(String oldname, String newname) {
    }

    /* access modifiers changed from: package-private */
    public void renameClass(Map<String, String> map) {
    }

    static void renameClass(List<AttributeInfo> attributes, String oldname, String newname) {
        if (attributes != null) {
            for (AttributeInfo ai : attributes) {
                ai.renameClass(oldname, newname);
            }
        }
    }

    static void renameClass(List<AttributeInfo> attributes, Map<String, String> classnames) {
        if (attributes != null) {
            for (AttributeInfo ai : attributes) {
                ai.renameClass(classnames);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void getRefClasses(Map<String, String> map) {
    }

    static void getRefClasses(List<AttributeInfo> attributes, Map<String, String> classnames) {
        if (attributes != null) {
            for (AttributeInfo ai : attributes) {
                ai.getRefClasses(classnames);
            }
        }
    }
}
