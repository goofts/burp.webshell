package javassist;

import javassist.CtField;

/* access modifiers changed from: package-private */
/* compiled from: CtClassType */
public class FieldInitLink {
    CtField field;
    CtField.Initializer init;
    FieldInitLink next = null;

    FieldInitLink(CtField f, CtField.Initializer i) {
        this.field = f;
        this.init = i;
    }
}
