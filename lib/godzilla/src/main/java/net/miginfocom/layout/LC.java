package net.miginfocom.layout;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import javassist.compiler.TokenId;

public final class LC implements Externalizable {
    private UnitValue alignX = null;
    private UnitValue alignY = null;
    private int debugMillis = 0;
    private boolean fillX = false;
    private boolean fillY = false;
    private boolean flowX = true;
    private BoundSize gridGapX = null;
    private BoundSize gridGapY = null;
    private BoundSize height = BoundSize.NULL_SIZE;
    private int hideMode = 0;
    private UnitValue[] insets = null;
    private Boolean leftToRight = null;
    private boolean noCache = false;
    private boolean noGrid = false;
    private BoundSize packH = BoundSize.NULL_SIZE;
    private BoundSize packW = BoundSize.NULL_SIZE;
    private float phAlign = 1.0f;
    private float pwAlign = 0.5f;
    private boolean topToBottom = true;
    private boolean visualPadding = true;
    private BoundSize width = BoundSize.NULL_SIZE;
    private int wrapAfter = LayoutUtil.INF;

    public boolean isNoCache() {
        return this.noCache;
    }

    public void setNoCache(boolean b) {
        this.noCache = b;
    }

    public final UnitValue getAlignX() {
        return this.alignX;
    }

    public final void setAlignX(UnitValue uv) {
        this.alignX = uv;
    }

    public final UnitValue getAlignY() {
        return this.alignY;
    }

    public final void setAlignY(UnitValue uv) {
        this.alignY = uv;
    }

    public final int getDebugMillis() {
        return this.debugMillis;
    }

    public final void setDebugMillis(int millis) {
        this.debugMillis = millis;
    }

    public final boolean isFillX() {
        return this.fillX;
    }

    public final void setFillX(boolean b) {
        this.fillX = b;
    }

    public final boolean isFillY() {
        return this.fillY;
    }

    public final void setFillY(boolean b) {
        this.fillY = b;
    }

    public final boolean isFlowX() {
        return this.flowX;
    }

    public final void setFlowX(boolean b) {
        this.flowX = b;
    }

    public final BoundSize getGridGapX() {
        return this.gridGapX;
    }

    public final void setGridGapX(BoundSize x) {
        this.gridGapX = x;
    }

    public final BoundSize getGridGapY() {
        return this.gridGapY;
    }

    public final void setGridGapY(BoundSize y) {
        this.gridGapY = y;
    }

    public final int getHideMode() {
        return this.hideMode;
    }

    public final void setHideMode(int mode) {
        if (mode < 0 || mode > 3) {
            throw new IllegalArgumentException("Wrong hideMode: " + mode);
        }
        this.hideMode = mode;
    }

    public final UnitValue[] getInsets() {
        if (this.insets == null) {
            return null;
        }
        return new UnitValue[]{this.insets[0], this.insets[1], this.insets[2], this.insets[3]};
    }

    public final void setInsets(UnitValue[] ins) {
        this.insets = ins != null ? new UnitValue[]{ins[0], ins[1], ins[2], ins[3]} : null;
    }

    public final Boolean getLeftToRight() {
        return this.leftToRight;
    }

    public final void setLeftToRight(Boolean b) {
        this.leftToRight = b;
    }

    public final boolean isNoGrid() {
        return this.noGrid;
    }

    public final void setNoGrid(boolean b) {
        this.noGrid = b;
    }

    public final boolean isTopToBottom() {
        return this.topToBottom;
    }

    public final void setTopToBottom(boolean b) {
        this.topToBottom = b;
    }

    public final boolean isVisualPadding() {
        return this.visualPadding;
    }

    public final void setVisualPadding(boolean b) {
        this.visualPadding = b;
    }

    public final int getWrapAfter() {
        return this.wrapAfter;
    }

    public final void setWrapAfter(int count) {
        this.wrapAfter = count;
    }

    public final BoundSize getPackWidth() {
        return this.packW;
    }

    public final void setPackWidth(BoundSize size) {
        if (size == null) {
            size = BoundSize.NULL_SIZE;
        }
        this.packW = size;
    }

    public final BoundSize getPackHeight() {
        return this.packH;
    }

    public final void setPackHeight(BoundSize size) {
        if (size == null) {
            size = BoundSize.NULL_SIZE;
        }
        this.packH = size;
    }

    public final float getPackHeightAlign() {
        return this.phAlign;
    }

    public final void setPackHeightAlign(float align) {
        this.phAlign = Math.max(0.0f, Math.min(1.0f, align));
    }

    public final float getPackWidthAlign() {
        return this.pwAlign;
    }

    public final void setPackWidthAlign(float align) {
        this.pwAlign = Math.max(0.0f, Math.min(1.0f, align));
    }

    public final BoundSize getWidth() {
        return this.width;
    }

    public final void setWidth(BoundSize size) {
        if (size == null) {
            size = BoundSize.NULL_SIZE;
        }
        this.width = size;
    }

    public final BoundSize getHeight() {
        return this.height;
    }

    public final void setHeight(BoundSize size) {
        if (size == null) {
            size = BoundSize.NULL_SIZE;
        }
        this.height = size;
    }

    public final LC pack() {
        return pack("pref", "pref");
    }

    public final LC pack(String width2, String height2) {
        setPackWidth(width2 != null ? ConstraintParser.parseBoundSize(width2, false, true) : BoundSize.NULL_SIZE);
        setPackHeight(height2 != null ? ConstraintParser.parseBoundSize(height2, false, false) : BoundSize.NULL_SIZE);
        return this;
    }

    public final LC packAlign(float alignX2, float alignY2) {
        setPackWidthAlign(alignX2);
        setPackHeightAlign(alignY2);
        return this;
    }

    public final LC wrap() {
        setWrapAfter(0);
        return this;
    }

    public final LC wrapAfter(int count) {
        setWrapAfter(count);
        return this;
    }

    public final LC noCache() {
        setNoCache(true);
        return this;
    }

    public final LC flowY() {
        setFlowX(false);
        return this;
    }

    public final LC flowX() {
        setFlowX(true);
        return this;
    }

    public final LC fill() {
        setFillX(true);
        setFillY(true);
        return this;
    }

    public final LC fillX() {
        setFillX(true);
        return this;
    }

    public final LC fillY() {
        setFillY(true);
        return this;
    }

    public final LC leftToRight(boolean b) {
        setLeftToRight(b ? Boolean.TRUE : Boolean.FALSE);
        return this;
    }

    public final LC rightToLeft() {
        setLeftToRight(Boolean.FALSE);
        return this;
    }

    public final LC bottomToTop() {
        setTopToBottom(false);
        return this;
    }

    public final LC topToBottom() {
        setTopToBottom(true);
        return this;
    }

    public final LC noGrid() {
        setNoGrid(true);
        return this;
    }

    public final LC noVisualPadding() {
        setVisualPadding(false);
        return this;
    }

    public final LC insetsAll(String allSides) {
        UnitValue insH = ConstraintParser.parseUnitValue(allSides, true);
        UnitValue insV = ConstraintParser.parseUnitValue(allSides, false);
        this.insets = new UnitValue[]{insV, insH, insV, insH};
        return this;
    }

    public final LC insets(String s) {
        this.insets = ConstraintParser.parseInsets(s, true);
        return this;
    }

    public final LC insets(String top, String left, String bottom, String right) {
        this.insets = new UnitValue[]{ConstraintParser.parseUnitValue(top, false), ConstraintParser.parseUnitValue(left, true), ConstraintParser.parseUnitValue(bottom, false), ConstraintParser.parseUnitValue(right, true)};
        return this;
    }

    public final LC alignX(String align) {
        setAlignX(ConstraintParser.parseUnitValueOrAlign(align, true, null));
        return this;
    }

    public final LC alignY(String align) {
        setAlignY(ConstraintParser.parseUnitValueOrAlign(align, false, null));
        return this;
    }

    public final LC align(String ax, String ay) {
        if (ax != null) {
            alignX(ax);
        }
        if (ay != null) {
            alignY(ay);
        }
        return this;
    }

    public final LC gridGapX(String boundsSize) {
        setGridGapX(ConstraintParser.parseBoundSize(boundsSize, true, true));
        return this;
    }

    public final LC gridGapY(String boundsSize) {
        setGridGapY(ConstraintParser.parseBoundSize(boundsSize, true, false));
        return this;
    }

    public final LC gridGap(String gapx, String gapy) {
        if (gapx != null) {
            gridGapX(gapx);
        }
        if (gapy != null) {
            gridGapY(gapy);
        }
        return this;
    }

    public final LC debug() {
        setDebugMillis(TokenId.ABSTRACT);
        return this;
    }

    public final LC debug(int repaintMillis) {
        setDebugMillis(repaintMillis);
        return this;
    }

    public final LC hideMode(int mode) {
        setHideMode(mode);
        return this;
    }

    public final LC minWidth(String width2) {
        setWidth(LayoutUtil.derive(getWidth(), ConstraintParser.parseUnitValue(width2, true), null, null));
        return this;
    }

    public final LC width(String width2) {
        setWidth(ConstraintParser.parseBoundSize(width2, false, true));
        return this;
    }

    public final LC maxWidth(String width2) {
        setWidth(LayoutUtil.derive(getWidth(), null, null, ConstraintParser.parseUnitValue(width2, true)));
        return this;
    }

    public final LC minHeight(String height2) {
        setHeight(LayoutUtil.derive(getHeight(), ConstraintParser.parseUnitValue(height2, false), null, null));
        return this;
    }

    public final LC height(String height2) {
        setHeight(ConstraintParser.parseBoundSize(height2, false, false));
        return this;
    }

    public final LC maxHeight(String height2) {
        setHeight(LayoutUtil.derive(getHeight(), null, null, ConstraintParser.parseUnitValue(height2, false)));
        return this;
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
        if (getClass() == LC.class) {
            LayoutUtil.writeAsXML(out, this);
        }
    }
}
