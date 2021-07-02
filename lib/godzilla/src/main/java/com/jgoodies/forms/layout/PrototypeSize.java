package com.jgoodies.forms.layout;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.util.DefaultUnitConverter;
import java.awt.Container;
import java.io.Serializable;
import java.util.List;

public final class PrototypeSize implements Size, Serializable {
    private final String prototype;

    public PrototypeSize(String prototype2) {
        this.prototype = prototype2;
    }

    public String getPrototype() {
        return this.prototype;
    }

    @Override // com.jgoodies.forms.layout.Size
    public int maximumSize(Container container, List components, FormLayout.Measure minMeasure, FormLayout.Measure prefMeasure, FormLayout.Measure defaultMeasure) {
        return container.getFontMetrics(DefaultUnitConverter.getInstance().getDefaultDialogFont()).stringWidth(getPrototype());
    }

    @Override // com.jgoodies.forms.layout.Size
    public boolean compressible() {
        return false;
    }

    @Override // com.jgoodies.forms.layout.Size
    public String encode() {
        return "'" + this.prototype + "'";
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PrototypeSize)) {
            return false;
        }
        return this.prototype.equals(((PrototypeSize) o).prototype);
    }

    public int hashCode() {
        return this.prototype.hashCode();
    }

    public String toString() {
        return encode();
    }
}
