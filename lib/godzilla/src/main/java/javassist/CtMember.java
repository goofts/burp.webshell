package javassist;

public abstract class CtMember {
    protected CtClass declaringClass;
    CtMember next = null;

    /* access modifiers changed from: protected */
    public abstract void extendToString(StringBuffer stringBuffer);

    public abstract Object getAnnotation(Class<?> cls) throws ClassNotFoundException;

    public abstract Object[] getAnnotations() throws ClassNotFoundException;

    public abstract byte[] getAttribute(String str);

    public abstract Object[] getAvailableAnnotations();

    public abstract String getGenericSignature();

    public abstract int getModifiers();

    public abstract String getName();

    public abstract String getSignature();

    public abstract boolean hasAnnotation(String str);

    public abstract void setAttribute(String str, byte[] bArr);

    public abstract void setGenericSignature(String str);

    public abstract void setModifiers(int i);

    /* access modifiers changed from: package-private */
    public static class Cache extends CtMember {
        private CtMember consTail = this;
        private CtMember fieldTail = this;
        private CtMember methodTail = this;

        /* access modifiers changed from: protected */
        @Override // javassist.CtMember
        public void extendToString(StringBuffer buffer) {
        }

        @Override // javassist.CtMember
        public boolean hasAnnotation(String clz) {
            return false;
        }

        @Override // javassist.CtMember
        public Object getAnnotation(Class<?> cls) throws ClassNotFoundException {
            return null;
        }

        @Override // javassist.CtMember
        public Object[] getAnnotations() throws ClassNotFoundException {
            return null;
        }

        @Override // javassist.CtMember
        public byte[] getAttribute(String name) {
            return null;
        }

        @Override // javassist.CtMember
        public Object[] getAvailableAnnotations() {
            return null;
        }

        @Override // javassist.CtMember
        public int getModifiers() {
            return 0;
        }

        @Override // javassist.CtMember
        public String getName() {
            return null;
        }

        @Override // javassist.CtMember
        public String getSignature() {
            return null;
        }

        @Override // javassist.CtMember
        public void setAttribute(String name, byte[] data) {
        }

        @Override // javassist.CtMember
        public void setModifiers(int mod) {
        }

        @Override // javassist.CtMember
        public String getGenericSignature() {
            return null;
        }

        @Override // javassist.CtMember
        public void setGenericSignature(String sig) {
        }

        Cache(CtClassType decl) {
            super(decl);
            this.fieldTail.next = this;
        }

        /* access modifiers changed from: package-private */
        public CtMember methodHead() {
            return this;
        }

        /* access modifiers changed from: package-private */
        public CtMember lastMethod() {
            return this.methodTail;
        }

        /* access modifiers changed from: package-private */
        public CtMember consHead() {
            return this.methodTail;
        }

        /* access modifiers changed from: package-private */
        public CtMember lastCons() {
            return this.consTail;
        }

        /* access modifiers changed from: package-private */
        public CtMember fieldHead() {
            return this.consTail;
        }

        /* access modifiers changed from: package-private */
        public CtMember lastField() {
            return this.fieldTail;
        }

        /* access modifiers changed from: package-private */
        public void addMethod(CtMember method) {
            method.next = this.methodTail.next;
            this.methodTail.next = method;
            if (this.methodTail == this.consTail) {
                this.consTail = method;
                if (this.methodTail == this.fieldTail) {
                    this.fieldTail = method;
                }
            }
            this.methodTail = method;
        }

        /* access modifiers changed from: package-private */
        public void addConstructor(CtMember cons) {
            cons.next = this.consTail.next;
            this.consTail.next = cons;
            if (this.consTail == this.fieldTail) {
                this.fieldTail = cons;
            }
            this.consTail = cons;
        }

        /* access modifiers changed from: package-private */
        public void addField(CtMember field) {
            field.next = this;
            this.fieldTail.next = field;
            this.fieldTail = field;
        }

        static int count(CtMember head, CtMember tail) {
            int n = 0;
            while (head != tail) {
                n++;
                head = head.next;
            }
            return n;
        }

        /* access modifiers changed from: package-private */
        public void remove(CtMember mem) {
            CtMember m = this;
            while (true) {
                CtMember node = m.next;
                if (node == this) {
                    return;
                }
                if (node == mem) {
                    m.next = node.next;
                    if (node == this.methodTail) {
                        this.methodTail = m;
                    }
                    if (node == this.consTail) {
                        this.consTail = m;
                    }
                    if (node == this.fieldTail) {
                        this.fieldTail = m;
                        return;
                    }
                    return;
                }
                m = m.next;
            }
        }
    }

    protected CtMember(CtClass clazz) {
        this.declaringClass = clazz;
    }

    /* access modifiers changed from: package-private */
    public final CtMember next() {
        return this.next;
    }

    /* access modifiers changed from: package-private */
    public void nameReplaced() {
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer(getClass().getName());
        buffer.append("@");
        buffer.append(Integer.toHexString(hashCode()));
        buffer.append("[");
        buffer.append(Modifier.toString(getModifiers()));
        extendToString(buffer);
        buffer.append("]");
        return buffer.toString();
    }

    public CtClass getDeclaringClass() {
        return this.declaringClass;
    }

    public boolean visibleFrom(CtClass clazz) {
        boolean visible;
        int mod = getModifiers();
        if (Modifier.isPublic(mod)) {
            return true;
        }
        if (Modifier.isPrivate(mod)) {
            return clazz == this.declaringClass;
        }
        String declName = this.declaringClass.getPackageName();
        String fromName = clazz.getPackageName();
        if (declName != null) {
            visible = declName.equals(fromName);
        } else if (fromName == null) {
            visible = true;
        } else {
            visible = false;
        }
        if (visible || !Modifier.isProtected(mod)) {
            return visible;
        }
        return clazz.subclassOf(this.declaringClass);
    }

    public boolean hasAnnotation(Class<?> clz) {
        return hasAnnotation(clz.getName());
    }
}
