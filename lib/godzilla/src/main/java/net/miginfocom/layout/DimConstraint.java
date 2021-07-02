package net.miginfocom.layout;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;

public final class DimConstraint implements Externalizable {
    private UnitValue align = null;
    private String endGroup = null;
    private boolean fill = false;
    private BoundSize gapAfter = null;
    private BoundSize gapBefore = null;
    private boolean noGrid = false;
    final ResizeConstraint resize = new ResizeConstraint();
    private BoundSize size = BoundSize.NULL_SIZE;
    private String sizeGroup = null;

    public int getGrowPriority() {
        return this.resize.growPrio;
    }

    public void setGrowPriority(int p) {
        this.resize.growPrio = p;
    }

    public Float getGrow() {
        return this.resize.grow;
    }

    public void setGrow(Float weight) {
        this.resize.grow = weight;
    }

    public int getShrinkPriority() {
        return this.resize.shrinkPrio;
    }

    public void setShrinkPriority(int p) {
        this.resize.shrinkPrio = p;
    }

    public Float getShrink() {
        return this.resize.shrink;
    }

    public void setShrink(Float weight) {
        this.resize.shrink = weight;
    }

    public UnitValue getAlignOrDefault(boolean isCols) {
        if (this.align != null) {
            return this.align;
        }
        if (isCols) {
            return UnitValue.LEADING;
        }
        return (this.fill || !PlatformDefaults.getDefaultRowAlignmentBaseline()) ? UnitValue.CENTER : UnitValue.BASELINE_IDENTITY;
    }

    public UnitValue getAlign() {
        return this.align;
    }

    public void setAlign(UnitValue uv) {
        this.align = uv;
    }

    public BoundSize getGapAfter() {
        return this.gapAfter;
    }

    public void setGapAfter(BoundSize size2) {
        this.gapAfter = size2;
    }

    /* access modifiers changed from: package-private */
    public boolean hasGapAfter() {
        return this.gapAfter != null && !this.gapAfter.isUnset();
    }

    /* access modifiers changed from: package-private */
    public boolean isGapAfterPush() {
        return this.gapAfter != null && this.gapAfter.getGapPush();
    }

    public BoundSize getGapBefore() {
        return this.gapBefore;
    }

    public void setGapBefore(BoundSize size2) {
        this.gapBefore = size2;
    }

    /* access modifiers changed from: package-private */
    public boolean hasGapBefore() {
        return this.gapBefore != null && !this.gapBefore.isUnset();
    }

    /* access modifiers changed from: package-private */
    public boolean isGapBeforePush() {
        return this.gapBefore != null && this.gapBefore.getGapPush();
    }

    public BoundSize getSize() {
        return this.size;
    }

    public void setSize(BoundSize size2) {
        if (size2 != null) {
            size2.checkNotLinked();
        }
        this.size = size2;
    }

    public String getSizeGroup() {
        return this.sizeGroup;
    }

    public void setSizeGroup(String s) {
        this.sizeGroup = s;
    }

    public String getEndGroup() {
        return this.endGroup;
    }

    public void setEndGroup(String s) {
        this.endGroup = s;
    }

    public boolean isFill() {
        return this.fill;
    }

    public void setFill(boolean b) {
        this.fill = b;
    }

    public boolean isNoGrid() {
        return this.noGrid;
    }

    public void setNoGrid(boolean b) {
        this.noGrid = b;
    }

    /* access modifiers changed from: package-private */
    public int[] getRowGaps(ContainerWrapper parent, BoundSize defGap, int refSize, boolean before) {
        BoundSize gap = before ? this.gapBefore : this.gapAfter;
        if (gap == null || gap.isUnset()) {
            gap = defGap;
        }
        if (gap == null || gap.isUnset()) {
            return null;
        }
        int[] ret = new int[3];
        for (int i = 0; i <= 2; i++) {
            UnitValue uv = gap.getSize(i);
            ret[i] = uv != null ? uv.getPixels((float) refSize, parent, null) : -2147471302;
        }
        return ret;
    }

    /* access modifiers changed from: package-private */
    public int[] getComponentGaps(ContainerWrapper parent, ComponentWrapper comp, BoundSize adjGap, ComponentWrapper adjacentComp, String tag, int refSize, int adjacentSide, boolean isLTR) {
        BoundSize gap = adjacentSide < 2 ? this.gapBefore : this.gapAfter;
        boolean hasGap = gap != null && gap.getGapPush();
        if ((gap == null || gap.isUnset()) && ((adjGap == null || adjGap.isUnset()) && comp != null)) {
            gap = PlatformDefaults.getDefaultComponentGap(comp, adjacentComp, adjacentSide + 1, tag, isLTR);
        }
        if (gap != null) {
            int[] ret = new int[3];
            for (int i = 0; i <= 2; i++) {
                UnitValue uv = gap.getSize(i);
                ret[i] = uv != null ? uv.getPixels((float) refSize, parent, null) : -2147471302;
            }
            return ret;
        } else if (hasGap) {
            return new int[]{0, 0, -2147471302};
        } else {
            return null;
        }
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
        if (getClass() == DimConstraint.class) {
            LayoutUtil.writeAsXML(out, this);
        }
    }
}
