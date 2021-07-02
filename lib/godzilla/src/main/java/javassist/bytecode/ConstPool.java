package javassist.bytecode;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javassist.CtClass;

public final class ConstPool {
    public static final int CONST_Class = 7;
    public static final int CONST_Double = 6;
    public static final int CONST_Dynamic = 17;
    public static final int CONST_DynamicCallSite = 18;
    public static final int CONST_Fieldref = 9;
    public static final int CONST_Float = 4;
    public static final int CONST_Integer = 3;
    public static final int CONST_InterfaceMethodref = 11;
    public static final int CONST_InvokeDynamic = 18;
    public static final int CONST_Long = 5;
    public static final int CONST_MethodHandle = 15;
    public static final int CONST_MethodType = 16;
    public static final int CONST_Methodref = 10;
    public static final int CONST_Module = 19;
    public static final int CONST_NameAndType = 12;
    public static final int CONST_Package = 20;
    public static final int CONST_String = 8;
    public static final int CONST_Utf8 = 1;
    public static final int REF_getField = 1;
    public static final int REF_getStatic = 2;
    public static final int REF_invokeInterface = 9;
    public static final int REF_invokeSpecial = 7;
    public static final int REF_invokeStatic = 6;
    public static final int REF_invokeVirtual = 5;
    public static final int REF_newInvokeSpecial = 8;
    public static final int REF_putField = 3;
    public static final int REF_putStatic = 4;
    public static final CtClass THIS = null;
    LongVector items;
    Map<ConstInfo, ConstInfo> itemsCache;
    int numOfItems;
    int thisClassInfo;

    public ConstPool(String thisclass) {
        this.items = new LongVector();
        this.itemsCache = null;
        this.numOfItems = 0;
        addItem0(null);
        this.thisClassInfo = addClassInfo(thisclass);
    }

    public ConstPool(DataInputStream in) throws IOException {
        this.itemsCache = null;
        this.thisClassInfo = 0;
        read(in);
    }

    /* access modifiers changed from: package-private */
    public void prune() {
        this.itemsCache = null;
    }

    public int getSize() {
        return this.numOfItems;
    }

    public String getClassName() {
        return getClassInfo(this.thisClassInfo);
    }

    public int getThisClassInfo() {
        return this.thisClassInfo;
    }

    /* access modifiers changed from: package-private */
    public void setThisClassInfo(int i) {
        this.thisClassInfo = i;
    }

    /* access modifiers changed from: package-private */
    public ConstInfo getItem(int n) {
        return this.items.elementAt(n);
    }

    public int getTag(int index) {
        return getItem(index).getTag();
    }

    public String getClassInfo(int index) {
        ClassInfo c = (ClassInfo) getItem(index);
        if (c == null) {
            return null;
        }
        return Descriptor.toJavaName(getUtf8Info(c.name));
    }

    public String getClassInfoByDescriptor(int index) {
        ClassInfo c = (ClassInfo) getItem(index);
        if (c == null) {
            return null;
        }
        String className = getUtf8Info(c.name);
        return className.charAt(0) != '[' ? Descriptor.of(className) : className;
    }

    public int getNameAndTypeName(int index) {
        return ((NameAndTypeInfo) getItem(index)).memberName;
    }

    public int getNameAndTypeDescriptor(int index) {
        return ((NameAndTypeInfo) getItem(index)).typeDescriptor;
    }

    public int getMemberClass(int index) {
        return ((MemberrefInfo) getItem(index)).classIndex;
    }

    public int getMemberNameAndType(int index) {
        return ((MemberrefInfo) getItem(index)).nameAndTypeIndex;
    }

    public int getFieldrefClass(int index) {
        return ((FieldrefInfo) getItem(index)).classIndex;
    }

    public String getFieldrefClassName(int index) {
        FieldrefInfo f = (FieldrefInfo) getItem(index);
        if (f == null) {
            return null;
        }
        return getClassInfo(f.classIndex);
    }

    public int getFieldrefNameAndType(int index) {
        return ((FieldrefInfo) getItem(index)).nameAndTypeIndex;
    }

    public String getFieldrefName(int index) {
        NameAndTypeInfo n;
        FieldrefInfo f = (FieldrefInfo) getItem(index);
        if (f == null || (n = (NameAndTypeInfo) getItem(f.nameAndTypeIndex)) == null) {
            return null;
        }
        return getUtf8Info(n.memberName);
    }

    public String getFieldrefType(int index) {
        NameAndTypeInfo n;
        FieldrefInfo f = (FieldrefInfo) getItem(index);
        if (f == null || (n = (NameAndTypeInfo) getItem(f.nameAndTypeIndex)) == null) {
            return null;
        }
        return getUtf8Info(n.typeDescriptor);
    }

    public int getMethodrefClass(int index) {
        return ((MemberrefInfo) getItem(index)).classIndex;
    }

    public String getMethodrefClassName(int index) {
        MemberrefInfo minfo = (MemberrefInfo) getItem(index);
        if (minfo == null) {
            return null;
        }
        return getClassInfo(minfo.classIndex);
    }

    public int getMethodrefNameAndType(int index) {
        return ((MemberrefInfo) getItem(index)).nameAndTypeIndex;
    }

    public String getMethodrefName(int index) {
        NameAndTypeInfo n;
        MemberrefInfo minfo = (MemberrefInfo) getItem(index);
        if (minfo == null || (n = (NameAndTypeInfo) getItem(minfo.nameAndTypeIndex)) == null) {
            return null;
        }
        return getUtf8Info(n.memberName);
    }

    public String getMethodrefType(int index) {
        NameAndTypeInfo n;
        MemberrefInfo minfo = (MemberrefInfo) getItem(index);
        if (minfo == null || (n = (NameAndTypeInfo) getItem(minfo.nameAndTypeIndex)) == null) {
            return null;
        }
        return getUtf8Info(n.typeDescriptor);
    }

    public int getInterfaceMethodrefClass(int index) {
        return ((MemberrefInfo) getItem(index)).classIndex;
    }

    public String getInterfaceMethodrefClassName(int index) {
        return getClassInfo(((MemberrefInfo) getItem(index)).classIndex);
    }

    public int getInterfaceMethodrefNameAndType(int index) {
        return ((MemberrefInfo) getItem(index)).nameAndTypeIndex;
    }

    public String getInterfaceMethodrefName(int index) {
        NameAndTypeInfo n;
        MemberrefInfo minfo = (MemberrefInfo) getItem(index);
        if (minfo == null || (n = (NameAndTypeInfo) getItem(minfo.nameAndTypeIndex)) == null) {
            return null;
        }
        return getUtf8Info(n.memberName);
    }

    public String getInterfaceMethodrefType(int index) {
        NameAndTypeInfo n;
        MemberrefInfo minfo = (MemberrefInfo) getItem(index);
        if (minfo == null || (n = (NameAndTypeInfo) getItem(minfo.nameAndTypeIndex)) == null) {
            return null;
        }
        return getUtf8Info(n.typeDescriptor);
    }

    public Object getLdcValue(int index) {
        ConstInfo constInfo = getItem(index);
        if (constInfo instanceof StringInfo) {
            return getStringInfo(index);
        }
        if (constInfo instanceof FloatInfo) {
            return Float.valueOf(getFloatInfo(index));
        }
        if (constInfo instanceof IntegerInfo) {
            return Integer.valueOf(getIntegerInfo(index));
        }
        if (constInfo instanceof LongInfo) {
            return Long.valueOf(getLongInfo(index));
        }
        if (constInfo instanceof DoubleInfo) {
            return Double.valueOf(getDoubleInfo(index));
        }
        return null;
    }

    public int getIntegerInfo(int index) {
        return ((IntegerInfo) getItem(index)).value;
    }

    public float getFloatInfo(int index) {
        return ((FloatInfo) getItem(index)).value;
    }

    public long getLongInfo(int index) {
        return ((LongInfo) getItem(index)).value;
    }

    public double getDoubleInfo(int index) {
        return ((DoubleInfo) getItem(index)).value;
    }

    public String getStringInfo(int index) {
        return getUtf8Info(((StringInfo) getItem(index)).string);
    }

    public String getUtf8Info(int index) {
        return ((Utf8Info) getItem(index)).string;
    }

    public int getMethodHandleKind(int index) {
        return ((MethodHandleInfo) getItem(index)).refKind;
    }

    public int getMethodHandleIndex(int index) {
        return ((MethodHandleInfo) getItem(index)).refIndex;
    }

    public int getMethodTypeInfo(int index) {
        return ((MethodTypeInfo) getItem(index)).descriptor;
    }

    public int getInvokeDynamicBootstrap(int index) {
        return ((InvokeDynamicInfo) getItem(index)).bootstrap;
    }

    public int getInvokeDynamicNameAndType(int index) {
        return ((InvokeDynamicInfo) getItem(index)).nameAndType;
    }

    public String getInvokeDynamicType(int index) {
        NameAndTypeInfo n;
        InvokeDynamicInfo iv = (InvokeDynamicInfo) getItem(index);
        if (iv == null || (n = (NameAndTypeInfo) getItem(iv.nameAndType)) == null) {
            return null;
        }
        return getUtf8Info(n.typeDescriptor);
    }

    public int getDynamicBootstrap(int index) {
        return ((DynamicInfo) getItem(index)).bootstrap;
    }

    public int getDynamicNameAndType(int index) {
        return ((DynamicInfo) getItem(index)).nameAndType;
    }

    public String getDynamicType(int index) {
        NameAndTypeInfo n;
        DynamicInfo iv = (DynamicInfo) getItem(index);
        if (iv == null || (n = (NameAndTypeInfo) getItem(iv.nameAndType)) == null) {
            return null;
        }
        return getUtf8Info(n.typeDescriptor);
    }

    public String getModuleInfo(int index) {
        return getUtf8Info(((ModuleInfo) getItem(index)).name);
    }

    public String getPackageInfo(int index) {
        return getUtf8Info(((PackageInfo) getItem(index)).name);
    }

    public int isConstructor(String classname, int index) {
        return isMember(classname, "<init>", index);
    }

    public int isMember(String classname, String membername, int index) {
        MemberrefInfo minfo = (MemberrefInfo) getItem(index);
        if (getClassInfo(minfo.classIndex).equals(classname)) {
            NameAndTypeInfo ntinfo = (NameAndTypeInfo) getItem(minfo.nameAndTypeIndex);
            if (getUtf8Info(ntinfo.memberName).equals(membername)) {
                return ntinfo.typeDescriptor;
            }
        }
        return 0;
    }

    public String eqMember(String membername, String desc, int index) {
        MemberrefInfo minfo = (MemberrefInfo) getItem(index);
        NameAndTypeInfo ntinfo = (NameAndTypeInfo) getItem(minfo.nameAndTypeIndex);
        if (!getUtf8Info(ntinfo.memberName).equals(membername) || !getUtf8Info(ntinfo.typeDescriptor).equals(desc)) {
            return null;
        }
        return getClassInfo(minfo.classIndex);
    }

    private int addItem0(ConstInfo info) {
        this.items.addElement(info);
        int i = this.numOfItems;
        this.numOfItems = i + 1;
        return i;
    }

    private int addItem(ConstInfo info) {
        if (this.itemsCache == null) {
            this.itemsCache = makeItemsCache(this.items);
        }
        ConstInfo found = this.itemsCache.get(info);
        if (found != null) {
            return found.index;
        }
        this.items.addElement(info);
        this.itemsCache.put(info, info);
        int i = this.numOfItems;
        this.numOfItems = i + 1;
        return i;
    }

    public int copy(int n, ConstPool dest, Map<String, String> classnames) {
        if (n == 0) {
            return 0;
        }
        return getItem(n).copy(this, dest, classnames);
    }

    /* access modifiers changed from: package-private */
    public int addConstInfoPadding() {
        return addItem0(new ConstInfoPadding(this.numOfItems));
    }

    public int addClassInfo(CtClass c) {
        if (c == THIS) {
            return this.thisClassInfo;
        }
        if (!c.isArray()) {
            return addClassInfo(c.getName());
        }
        return addClassInfo(Descriptor.toJvmName(c));
    }

    public int addClassInfo(String qname) {
        return addItem(new ClassInfo(addUtf8Info(Descriptor.toJvmName(qname)), this.numOfItems));
    }

    public int addNameAndTypeInfo(String name, String type) {
        return addNameAndTypeInfo(addUtf8Info(name), addUtf8Info(type));
    }

    public int addNameAndTypeInfo(int name, int type) {
        return addItem(new NameAndTypeInfo(name, type, this.numOfItems));
    }

    public int addFieldrefInfo(int classInfo, String name, String type) {
        return addFieldrefInfo(classInfo, addNameAndTypeInfo(name, type));
    }

    public int addFieldrefInfo(int classInfo, int nameAndTypeInfo) {
        return addItem(new FieldrefInfo(classInfo, nameAndTypeInfo, this.numOfItems));
    }

    public int addMethodrefInfo(int classInfo, String name, String type) {
        return addMethodrefInfo(classInfo, addNameAndTypeInfo(name, type));
    }

    public int addMethodrefInfo(int classInfo, int nameAndTypeInfo) {
        return addItem(new MethodrefInfo(classInfo, nameAndTypeInfo, this.numOfItems));
    }

    public int addInterfaceMethodrefInfo(int classInfo, String name, String type) {
        return addInterfaceMethodrefInfo(classInfo, addNameAndTypeInfo(name, type));
    }

    public int addInterfaceMethodrefInfo(int classInfo, int nameAndTypeInfo) {
        return addItem(new InterfaceMethodrefInfo(classInfo, nameAndTypeInfo, this.numOfItems));
    }

    public int addStringInfo(String str) {
        return addItem(new StringInfo(addUtf8Info(str), this.numOfItems));
    }

    public int addIntegerInfo(int i) {
        return addItem(new IntegerInfo(i, this.numOfItems));
    }

    public int addFloatInfo(float f) {
        return addItem(new FloatInfo(f, this.numOfItems));
    }

    public int addLongInfo(long l) {
        int i = addItem(new LongInfo(l, this.numOfItems));
        if (i == this.numOfItems - 1) {
            addConstInfoPadding();
        }
        return i;
    }

    public int addDoubleInfo(double d) {
        int i = addItem(new DoubleInfo(d, this.numOfItems));
        if (i == this.numOfItems - 1) {
            addConstInfoPadding();
        }
        return i;
    }

    public int addUtf8Info(String utf8) {
        return addItem(new Utf8Info(utf8, this.numOfItems));
    }

    public int addMethodHandleInfo(int kind, int index) {
        return addItem(new MethodHandleInfo(kind, index, this.numOfItems));
    }

    public int addMethodTypeInfo(int desc) {
        return addItem(new MethodTypeInfo(desc, this.numOfItems));
    }

    public int addInvokeDynamicInfo(int bootstrap, int nameAndType) {
        return addItem(new InvokeDynamicInfo(bootstrap, nameAndType, this.numOfItems));
    }

    public int addDynamicInfo(int bootstrap, int nameAndType) {
        return addItem(new DynamicInfo(bootstrap, nameAndType, this.numOfItems));
    }

    public int addModuleInfo(int nameIndex) {
        return addItem(new ModuleInfo(nameIndex, this.numOfItems));
    }

    public int addPackageInfo(int nameIndex) {
        return addItem(new PackageInfo(nameIndex, this.numOfItems));
    }

    public Set<String> getClassNames() {
        Set<String> result = new HashSet<>();
        LongVector v = this.items;
        int size = this.numOfItems;
        for (int i = 1; i < size; i++) {
            String className = v.elementAt(i).getClassName(this);
            if (className != null) {
                result.add(className);
            }
        }
        return result;
    }

    public void renameClass(String oldName, String newName) {
        LongVector v = this.items;
        int size = this.numOfItems;
        for (int i = 1; i < size; i++) {
            v.elementAt(i).renameClass(this, oldName, newName, this.itemsCache);
        }
    }

    public void renameClass(Map<String, String> classnames) {
        LongVector v = this.items;
        int size = this.numOfItems;
        for (int i = 1; i < size; i++) {
            v.elementAt(i).renameClass(this, classnames, this.itemsCache);
        }
    }

    private void read(DataInputStream in) throws IOException {
        int n = in.readUnsignedShort();
        this.items = new LongVector(n);
        this.numOfItems = 0;
        addItem0(null);
        while (true) {
            n--;
            if (n > 0) {
                int tag = readOne(in);
                if (tag == 5 || tag == 6) {
                    addConstInfoPadding();
                    n--;
                }
            } else {
                return;
            }
        }
    }

    private static Map<ConstInfo, ConstInfo> makeItemsCache(LongVector items2) {
        Map<ConstInfo, ConstInfo> cache = new HashMap<>();
        int i = 1;
        while (true) {
            int i2 = i + 1;
            ConstInfo info = items2.elementAt(i);
            if (info == null) {
                return cache;
            }
            cache.put(info, info);
            i = i2;
        }
    }

    private int readOne(DataInputStream in) throws IOException {
        ConstInfo info;
        int tag = in.readUnsignedByte();
        switch (tag) {
            case 1:
                info = new Utf8Info(in, this.numOfItems);
                break;
            case 2:
            case 13:
            case 14:
            default:
                throw new IOException("invalid constant type: " + tag + " at " + this.numOfItems);
            case 3:
                info = new IntegerInfo(in, this.numOfItems);
                break;
            case 4:
                info = new FloatInfo(in, this.numOfItems);
                break;
            case 5:
                info = new LongInfo(in, this.numOfItems);
                break;
            case 6:
                info = new DoubleInfo(in, this.numOfItems);
                break;
            case 7:
                info = new ClassInfo(in, this.numOfItems);
                break;
            case 8:
                info = new StringInfo(in, this.numOfItems);
                break;
            case 9:
                info = new FieldrefInfo(in, this.numOfItems);
                break;
            case 10:
                info = new MethodrefInfo(in, this.numOfItems);
                break;
            case 11:
                info = new InterfaceMethodrefInfo(in, this.numOfItems);
                break;
            case 12:
                info = new NameAndTypeInfo(in, this.numOfItems);
                break;
            case 15:
                info = new MethodHandleInfo(in, this.numOfItems);
                break;
            case 16:
                info = new MethodTypeInfo(in, this.numOfItems);
                break;
            case 17:
                info = new DynamicInfo(in, this.numOfItems);
                break;
            case 18:
                info = new InvokeDynamicInfo(in, this.numOfItems);
                break;
            case 19:
                info = new ModuleInfo(in, this.numOfItems);
                break;
            case 20:
                info = new PackageInfo(in, this.numOfItems);
                break;
        }
        addItem0(info);
        return tag;
    }

    public void write(DataOutputStream out) throws IOException {
        out.writeShort(this.numOfItems);
        LongVector v = this.items;
        int size = this.numOfItems;
        for (int i = 1; i < size; i++) {
            v.elementAt(i).write(out);
        }
    }

    public void print() {
        print(new PrintWriter((OutputStream) System.out, true));
    }

    public void print(PrintWriter out) {
        int size = this.numOfItems;
        for (int i = 1; i < size; i++) {
            out.print(i);
            out.print(" ");
            this.items.elementAt(i).print(out);
        }
    }
}
