package com.jgoodies.forms.layout;

import com.jgoodies.common.base.Preconditions;
import com.jgoodies.forms.layout.FormLayout;
import java.awt.Container;
import java.io.Serializable;
import java.util.List;

public final class BoundedSize implements Size, Serializable {
    private final Size basis;
    private final Size lowerBound;
    private final Size upperBound;

    public BoundedSize(Size basis2, Size lowerBound2, Size upperBound2) {
        this.basis = (Size) Preconditions.checkNotNull(basis2, "The basis must not be null.");
        this.lowerBound = lowerBound2;
        this.upperBound = upperBound2;
        if (lowerBound2 == null && upperBound2 == null) {
            throw new IllegalArgumentException("A bounded size must have a non-null lower or upper bound.");
        }
    }

    public Size getBasis() {
        return this.basis;
    }

    public Size getLowerBound() {
        return this.lowerBound;
    }

    public Size getUpperBound() {
        return this.upperBound;
    }

    @Override // com.jgoodies.forms.layout.Size
    public int maximumSize(Container container, List components, FormLayout.Measure minMeasure, FormLayout.Measure prefMeasure, FormLayout.Measure defaultMeasure) {
        int size = this.basis.maximumSize(container, components, minMeasure, prefMeasure, defaultMeasure);
        if (this.lowerBound != null) {
            size = Math.max(size, this.lowerBound.maximumSize(container, components, minMeasure, prefMeasure, defaultMeasure));
        }
        if (this.upperBound != null) {
            return Math.min(size, this.upperBound.maximumSize(container, components, minMeasure, prefMeasure, defaultMeasure));
        }
        return size;
    }

    @Override // com.jgoodies.forms.layout.Size
    public boolean compressible() {
        return getBasis().compressible();
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof BoundedSize)) {
            return false;
        }
        BoundedSize size = (BoundedSize) object;
        if (this.basis.equals(size.basis) && ((this.lowerBound == null && size.lowerBound == null) || (this.lowerBound != null && this.lowerBound.equals(size.lowerBound)))) {
            if (this.upperBound == null && size.upperBound == null) {
                return true;
            }
            if (this.upperBound != null && this.upperBound.equals(size.upperBound)) {
                return true;
            }
        }
        return false;
    }

    public int hashCode() {
        int hashValue = this.basis.hashCode();
        if (this.lowerBound != null) {
            hashValue = (hashValue * 37) + this.lowerBound.hashCode();
        }
        if (this.upperBound != null) {
            return (hashValue * 37) + this.upperBound.hashCode();
        }
        return hashValue;
    }

    public String toString() {
        return encode();
    }

    @Override // com.jgoodies.forms.layout.Size
    public String encode() {
        StringBuffer buffer = new StringBuffer("[");
        if (this.lowerBound != null) {
            buffer.append(this.lowerBound.encode());
            buffer.append(',');
        }
        buffer.append(this.basis.encode());
        if (this.upperBound != null) {
            buffer.append(',');
            buffer.append(this.upperBound.encode());
        }
        buffer.append(']');
        return buffer.toString();
    }
}
