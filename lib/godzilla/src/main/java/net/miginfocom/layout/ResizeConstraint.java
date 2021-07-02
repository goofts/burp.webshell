package net.miginfocom.layout;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;

/* access modifiers changed from: package-private */
public final class ResizeConstraint implements Externalizable {
    static final Float WEIGHT_100 = Float.valueOf(100.0f);
    Float grow = null;
    int growPrio = 100;
    Float shrink = WEIGHT_100;
    int shrinkPrio = 100;

    public ResizeConstraint() {
    }

    ResizeConstraint(int shrinkPrio2, Float shrinkWeight, int growPrio2, Float growWeight) {
        this.shrinkPrio = shrinkPrio2;
        this.shrink = shrinkWeight;
        this.growPrio = growPrio2;
        this.grow = growWeight;
    }

    private Object readResolve() throws ObjectStreamException {
        return LayoutUtil.getSerializedObject(this);
    }

    @Override // java.io.Externalizable
    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        LayoutUtil.setSerializedObject(this, LayoutUtil.readAsXML(in));
    }

    @Override // java.io.Externalizable
    public void writeExternal(ObjectOutput out) throws IOException {
        if (getClass() == ResizeConstraint.class) {
            LayoutUtil.writeAsXML(out, this);
        }
    }
}
