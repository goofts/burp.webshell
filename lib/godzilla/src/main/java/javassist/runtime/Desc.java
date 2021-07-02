package javassist.runtime;

import javassist.bytecode.Opcode;

public class Desc {
    private static final ThreadLocal<Boolean> USE_CONTEXT_CLASS_LOADER_LOCALLY = new ThreadLocal<Boolean>() {
        /* class javassist.runtime.Desc.AnonymousClass1 */

        /* access modifiers changed from: protected */
        @Override // java.lang.ThreadLocal
        public Boolean initialValue() {
            return false;
        }
    };
    public static boolean useContextClassLoader = false;

    public static void setUseContextClassLoaderLocally() {
        USE_CONTEXT_CLASS_LOADER_LOCALLY.set(true);
    }

    public static void resetUseContextClassLoaderLocally() {
        USE_CONTEXT_CLASS_LOADER_LOCALLY.remove();
    }

    private static Class<?> getClassObject(String name) throws ClassNotFoundException {
        if (useContextClassLoader || USE_CONTEXT_CLASS_LOADER_LOCALLY.get().booleanValue()) {
            return Class.forName(name, true, Thread.currentThread().getContextClassLoader());
        }
        return Class.forName(name);
    }

    public static Class<?> getClazz(String name) {
        try {
            return getClassObject(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("$class: internal error, could not find class '" + name + "' (Desc.useContextClassLoader: " + Boolean.toString(useContextClassLoader) + ")", e);
        }
    }

    public static Class<?>[] getParams(String desc) {
        if (desc.charAt(0) == '(') {
            return getType(desc, desc.length(), 1, 0);
        }
        throw new RuntimeException("$sig: internal error");
    }

    public static Class<?> getType(String desc) {
        Class<?>[] result = getType(desc, desc.length(), 0, 0);
        if (result != null && result.length == 1) {
            return result[0];
        }
        throw new RuntimeException("$type: internal error");
    }

    private static Class<?>[] getType(String desc, int descLen, int start, int num) {
        Class<?> clazz;
        if (start >= descLen) {
            return new Class[num];
        }
        switch (desc.charAt(start)) {
            case 'B':
                clazz = Byte.TYPE;
                break;
            case 'C':
                clazz = Character.TYPE;
                break;
            case 'D':
                clazz = Double.TYPE;
                break;
            case 'E':
            case Opcode.DSTORE_0:
            case Opcode.DSTORE_1:
            case Opcode.ASTORE_0:
            case Opcode.ASTORE_2:
            case Opcode.ASTORE_3:
            case Opcode.IASTORE:
            case Opcode.LASTORE:
            case Opcode.FASTORE:
            case Opcode.DASTORE:
            case Opcode.BASTORE:
            case Opcode.CASTORE:
            case Opcode.POP:
            case Opcode.POP2:
            case Opcode.DUP:
            default:
                return new Class[num];
            case Opcode.FSTORE_3:
                clazz = Float.TYPE;
                break;
            case Opcode.DSTORE_2:
                clazz = Integer.TYPE;
                break;
            case Opcode.DSTORE_3:
                clazz = Long.TYPE;
                break;
            case 'L':
            case Opcode.DUP_X2:
                return getClassType(desc, descLen, start, num);
            case Opcode.AASTORE:
                clazz = Short.TYPE;
                break;
            case Opcode.SASTORE:
                clazz = Void.TYPE;
                break;
            case Opcode.DUP_X1:
                clazz = Boolean.TYPE;
                break;
        }
        Class<?>[] result = getType(desc, descLen, start + 1, num + 1);
        result[num] = clazz;
        return result;
    }

    private static Class<?>[] getClassType(String desc, int descLen, int start, int num) {
        String cname;
        int end = start;
        while (desc.charAt(end) == '[') {
            end++;
        }
        if (desc.charAt(end) != 'L' || (end = desc.indexOf(59, end)) >= 0) {
            if (desc.charAt(start) == 'L') {
                cname = desc.substring(start + 1, end);
            } else {
                cname = desc.substring(start, end + 1);
            }
            Class<?>[] result = getType(desc, descLen, end + 1, num + 1);
            try {
                result[num] = getClassObject(cname.replace('/', '.'));
                return result;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e.getMessage());
            }
        } else {
            throw new IndexOutOfBoundsException("bad descriptor");
        }
    }
}
