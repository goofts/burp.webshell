package net.miginfocom.layout;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import java.util.ArrayList;

public final class CC implements Externalizable {
    private static final BoundSize DEF_GAP = BoundSize.NULL_SIZE;
    static final String[] DOCK_SIDES = {"north", "west", "south", "east"};
    private static final String[] EMPTY_ARR = new String[0];
    private AnimSpec animSpec = AnimSpec.DEF;
    private boolean boundsInGrid = true;
    private int cellX = -1;
    private int cellY = 0;
    private int dock = -1;
    private boolean external = false;
    private Boolean flowX = null;
    private int hideMode = -1;
    private DimConstraint hor = new DimConstraint();
    private String id = null;
    private transient String[] linkTargets = null;
    private BoundSize newline = null;
    private UnitValue[] padding = null;
    private UnitValue[] pos = null;
    private Float pushX = null;
    private Float pushY = null;
    private int skip = 0;
    private int spanX = 1;
    private int spanY = 1;
    private int split = 1;
    private String tag = null;
    private DimConstraint ver = new DimConstraint();
    private UnitValue[] visualPadding = null;
    private BoundSize wrap = null;

    /* access modifiers changed from: package-private */
    public String[] getLinkTargets() {
        if (this.linkTargets == null) {
            ArrayList<String> targets = new ArrayList<>(2);
            if (this.pos != null) {
                for (int i = 0; i < this.pos.length; i++) {
                    addLinkTargetIDs(targets, this.pos[i]);
                }
            }
            this.linkTargets = targets.size() == 0 ? EMPTY_ARR : (String[]) targets.toArray(new String[targets.size()]);
        }
        return this.linkTargets;
    }

    private void addLinkTargetIDs(ArrayList<String> targets, UnitValue uv) {
        if (uv != null) {
            String linkId = uv.getLinkTargetId();
            if (linkId != null) {
                targets.add(linkId);
                return;
            }
            for (int i = uv.getSubUnitCount() - 1; i >= 0; i--) {
                UnitValue subUv = uv.getSubUnitValue(i);
                if (subUv.isLinkedDeep()) {
                    addLinkTargetIDs(targets, subUv);
                }
            }
        }
    }

    public final CC endGroupX(String s) {
        this.hor.setEndGroup(s);
        return this;
    }

    public final CC sizeGroupX(String s) {
        this.hor.setSizeGroup(s);
        return this;
    }

    public final CC minWidth(String size) {
        this.hor.setSize(LayoutUtil.derive(this.hor.getSize(), ConstraintParser.parseUnitValue(size, true), null, null));
        return this;
    }

    public final CC width(String size) {
        this.hor.setSize(ConstraintParser.parseBoundSize(size, false, true));
        return this;
    }

    public final CC maxWidth(String size) {
        this.hor.setSize(LayoutUtil.derive(this.hor.getSize(), null, null, ConstraintParser.parseUnitValue(size, true)));
        return this;
    }

    public final CC gapX(String before, String after) {
        if (before != null) {
            this.hor.setGapBefore(ConstraintParser.parseBoundSize(before, true, true));
        }
        if (after != null) {
            this.hor.setGapAfter(ConstraintParser.parseBoundSize(after, true, true));
        }
        return this;
    }

    public final CC alignX(String align) {
        this.hor.setAlign(ConstraintParser.parseUnitValueOrAlign(align, true, null));
        return this;
    }

    public final CC growPrioX(int p) {
        this.hor.setGrowPriority(p);
        return this;
    }

    public final CC growPrio(int... widthHeight) {
        switch (widthHeight.length) {
            case 1:
                break;
            default:
                throw new IllegalArgumentException("Illegal argument count: " + widthHeight.length);
            case 2:
                growPrioY(widthHeight[1]);
                break;
        }
        growPrioX(widthHeight[0]);
        return this;
    }

    public final CC growX() {
        this.hor.setGrow(ResizeConstraint.WEIGHT_100);
        return this;
    }

    public final CC growX(float w) {
        this.hor.setGrow(new Float(w));
        return this;
    }

    public final CC grow(float... widthHeight) {
        switch (widthHeight.length) {
            case 1:
                break;
            default:
                throw new IllegalArgumentException("Illegal argument count: " + widthHeight.length);
            case 2:
                growY(widthHeight[1]);
                break;
        }
        growX(widthHeight[0]);
        return this;
    }

    public final CC shrinkPrioX(int p) {
        this.hor.setShrinkPriority(p);
        return this;
    }

    public final CC shrinkPrio(int... widthHeight) {
        switch (widthHeight.length) {
            case 1:
                break;
            default:
                throw new IllegalArgumentException("Illegal argument count: " + widthHeight.length);
            case 2:
                shrinkPrioY(widthHeight[1]);
                break;
        }
        shrinkPrioX(widthHeight[0]);
        return this;
    }

    public final CC shrinkX(float w) {
        this.hor.setShrink(new Float(w));
        return this;
    }

    public final CC shrink(float... widthHeight) {
        switch (widthHeight.length) {
            case 1:
                break;
            default:
                throw new IllegalArgumentException("Illegal argument count: " + widthHeight.length);
            case 2:
                shrinkY(widthHeight[1]);
                break;
        }
        shrinkX(widthHeight[0]);
        return this;
    }

    public final CC endGroupY(String s) {
        this.ver.setEndGroup(s);
        return this;
    }

    public final CC endGroup(String... xy) {
        switch (xy.length) {
            case 1:
                break;
            default:
                throw new IllegalArgumentException("Illegal argument count: " + xy.length);
            case 2:
                endGroupY(xy[1]);
                break;
        }
        endGroupX(xy[0]);
        return this;
    }

    public final CC sizeGroupY(String s) {
        this.ver.setSizeGroup(s);
        return this;
    }

    public final CC sizeGroup(String... xy) {
        switch (xy.length) {
            case 1:
                break;
            default:
                throw new IllegalArgumentException("Illegal argument count: " + xy.length);
            case 2:
                sizeGroupY(xy[1]);
                break;
        }
        sizeGroupX(xy[0]);
        return this;
    }

    public final CC minHeight(String size) {
        this.ver.setSize(LayoutUtil.derive(this.ver.getSize(), ConstraintParser.parseUnitValue(size, false), null, null));
        return this;
    }

    public final CC height(String size) {
        this.ver.setSize(ConstraintParser.parseBoundSize(size, false, false));
        return this;
    }

    public final CC maxHeight(String size) {
        this.ver.setSize(LayoutUtil.derive(this.ver.getSize(), null, null, ConstraintParser.parseUnitValue(size, false)));
        return this;
    }

    public final CC gapY(String before, String after) {
        if (before != null) {
            this.ver.setGapBefore(ConstraintParser.parseBoundSize(before, true, false));
        }
        if (after != null) {
            this.ver.setGapAfter(ConstraintParser.parseBoundSize(after, true, false));
        }
        return this;
    }

    public final CC alignY(String align) {
        this.ver.setAlign(ConstraintParser.parseUnitValueOrAlign(align, false, null));
        return this;
    }

    public final CC growPrioY(int p) {
        this.ver.setGrowPriority(p);
        return this;
    }

    public final CC growY() {
        this.ver.setGrow(ResizeConstraint.WEIGHT_100);
        return this;
    }

    public final CC growY(float w) {
        this.ver.setGrow(Float.valueOf(w));
        return this;
    }

    @Deprecated
    public final CC growY(Float w) {
        this.ver.setGrow(w);
        return this;
    }

    public final CC shrinkPrioY(int p) {
        this.ver.setShrinkPriority(p);
        return this;
    }

    public final CC shrinkY(float w) {
        this.ver.setShrink(new Float(w));
        return this;
    }

    public final CC hideMode(int mode) {
        setHideMode(mode);
        return this;
    }

    public final CC id(String s) {
        setId(s);
        return this;
    }

    public final CC tag(String tag2) {
        setTag(tag2);
        return this;
    }

    public final CC cell(int... colRowWidthHeight) {
        switch (colRowWidthHeight.length) {
            case 1:
                break;
            case 4:
                setSpanY(colRowWidthHeight[3]);
            case 3:
                setSpanX(colRowWidthHeight[2]);
            case 2:
                setCellY(colRowWidthHeight[1]);
                break;
            default:
                throw new IllegalArgumentException("Illegal argument count: " + colRowWidthHeight.length);
        }
        setCellX(colRowWidthHeight[0]);
        return this;
    }

    public final CC span(int... cells) {
        if (cells == null || cells.length == 0) {
            setSpanX(LayoutUtil.INF);
            setSpanY(1);
        } else if (cells.length == 1) {
            setSpanX(cells[0]);
            setSpanY(1);
        } else {
            setSpanX(cells[0]);
            setSpanY(cells[1]);
        }
        return this;
    }

    public final CC gap(String... args) {
        switch (args.length) {
            case 1:
                break;
            case 4:
                gapBottom(args[3]);
            case 3:
                gapTop(args[2]);
            case 2:
                gapRight(args[1]);
                break;
            default:
                throw new IllegalArgumentException("Illegal argument count: " + args.length);
        }
        gapLeft(args[0]);
        return this;
    }

    public final CC gapBefore(String boundsSize) {
        this.hor.setGapBefore(ConstraintParser.parseBoundSize(boundsSize, true, true));
        return this;
    }

    public final CC gapAfter(String boundsSize) {
        this.hor.setGapAfter(ConstraintParser.parseBoundSize(boundsSize, true, true));
        return this;
    }

    public final CC gapTop(String boundsSize) {
        this.ver.setGapBefore(ConstraintParser.parseBoundSize(boundsSize, true, false));
        return this;
    }

    public final CC gapLeft(String boundsSize) {
        this.hor.setGapBefore(ConstraintParser.parseBoundSize(boundsSize, true, true));
        return this;
    }

    public final CC gapBottom(String boundsSize) {
        this.ver.setGapAfter(ConstraintParser.parseBoundSize(boundsSize, true, false));
        return this;
    }

    public final CC gapRight(String boundsSize) {
        this.hor.setGapAfter(ConstraintParser.parseBoundSize(boundsSize, true, true));
        return this;
    }

    public final CC spanY() {
        return spanY(LayoutUtil.INF);
    }

    public final CC spanY(int cells) {
        setSpanY(cells);
        return this;
    }

    public final CC spanX() {
        return spanX(LayoutUtil.INF);
    }

    public final CC spanX(int cells) {
        setSpanX(cells);
        return this;
    }

    public final CC push() {
        return pushX().pushY();
    }

    public final CC push(Float weightX, Float weightY) {
        return pushX(weightX).pushY(weightY);
    }

    public final CC pushY() {
        return pushY(ResizeConstraint.WEIGHT_100);
    }

    public final CC pushY(Float weight) {
        setPushY(weight);
        return this;
    }

    public final CC pushX() {
        return pushX(ResizeConstraint.WEIGHT_100);
    }

    public final CC pushX(Float weight) {
        setPushX(weight);
        return this;
    }

    public final CC split(int parts) {
        setSplit(parts);
        return this;
    }

    public final CC split() {
        setSplit(LayoutUtil.INF);
        return this;
    }

    public final CC skip(int cells) {
        setSkip(cells);
        return this;
    }

    public final CC skip() {
        setSkip(1);
        return this;
    }

    public final CC external() {
        setExternal(true);
        return this;
    }

    public final CC flowX() {
        setFlowX(Boolean.TRUE);
        return this;
    }

    public final CC flowY() {
        setFlowX(Boolean.FALSE);
        return this;
    }

    public final CC grow() {
        growX();
        growY();
        return this;
    }

    public final CC newline() {
        setNewline(true);
        return this;
    }

    public final CC newline(String gapSize) {
        BoundSize bs = ConstraintParser.parseBoundSize(gapSize, true, this.flowX != null && !this.flowX.booleanValue());
        if (bs != null) {
            setNewlineGapSize(bs);
        } else {
            setNewline(true);
        }
        return this;
    }

    public final CC wrap() {
        setWrap(true);
        return this;
    }

    public final CC wrap(String gapSize) {
        BoundSize bs = ConstraintParser.parseBoundSize(gapSize, true, this.flowX != null && !this.flowX.booleanValue());
        if (bs != null) {
            setWrapGapSize(bs);
        } else {
            setWrap(true);
        }
        return this;
    }

    public final CC dockNorth() {
        setDockSide(0);
        return this;
    }

    public final CC dockWest() {
        setDockSide(1);
        return this;
    }

    public final CC dockSouth() {
        setDockSide(2);
        return this;
    }

    public final CC dockEast() {
        setDockSide(3);
        return this;
    }

    public final CC x(String x) {
        return corrPos(x, 0);
    }

    public final CC y(String y) {
        return corrPos(y, 1);
    }

    public final CC x2(String x2) {
        return corrPos(x2, 2);
    }

    public final CC y2(String y2) {
        return corrPos(y2, 3);
    }

    private final CC corrPos(String uv, int ix) {
        UnitValue[] b = getPos();
        if (b == null) {
            b = new UnitValue[4];
        }
        b[ix] = ConstraintParser.parseUnitValue(uv, ix % 2 == 0);
        setPos(b);
        setBoundsInGrid(true);
        return this;
    }

    public final CC pos(String x, String y) {
        UnitValue[] b = getPos();
        if (b == null) {
            b = new UnitValue[4];
        }
        b[0] = ConstraintParser.parseUnitValue(x, true);
        b[1] = ConstraintParser.parseUnitValue(y, false);
        setPos(b);
        setBoundsInGrid(false);
        return this;
    }

    public final CC pos(String x, String y, String x2, String y2) {
        setPos(new UnitValue[]{ConstraintParser.parseUnitValue(x, true), ConstraintParser.parseUnitValue(y, false), ConstraintParser.parseUnitValue(x2, true), ConstraintParser.parseUnitValue(y2, false)});
        setBoundsInGrid(false);
        return this;
    }

    public final CC pad(int top, int left, int bottom, int right) {
        setPadding(new UnitValue[]{new UnitValue((float) top), new UnitValue((float) left), new UnitValue((float) bottom), new UnitValue((float) right)});
        return this;
    }

    public final CC pad(String pad) {
        setPadding(pad != null ? ConstraintParser.parseInsets(pad, false) : null);
        return this;
    }

    public DimConstraint getHorizontal() {
        return this.hor;
    }

    public void setHorizontal(DimConstraint h) {
        if (h == null) {
            h = new DimConstraint();
        }
        this.hor = h;
    }

    public DimConstraint getVertical() {
        return this.ver;
    }

    public void setVertical(DimConstraint v) {
        if (v == null) {
            v = new DimConstraint();
        }
        this.ver = v;
    }

    public DimConstraint getDimConstraint(boolean isHor) {
        return isHor ? this.hor : this.ver;
    }

    public UnitValue[] getPos() {
        if (this.pos == null) {
            return null;
        }
        return new UnitValue[]{this.pos[0], this.pos[1], this.pos[2], this.pos[3]};
    }

    public void setPos(UnitValue[] pos2) {
        this.pos = pos2 != null ? new UnitValue[]{pos2[0], pos2[1], pos2[2], pos2[3]} : null;
        this.linkTargets = null;
    }

    public boolean isBoundsInGrid() {
        return this.boundsInGrid;
    }

    public void setBoundsInGrid(boolean b) {
        this.boundsInGrid = b;
    }

    public int getCellX() {
        return this.cellX;
    }

    public void setCellX(int x) {
        this.cellX = x;
    }

    public int getCellY() {
        if (this.cellX < 0) {
            return -1;
        }
        return this.cellY;
    }

    public void setCellY(int y) {
        if (y < 0) {
            this.cellX = -1;
        }
        this.cellY = Math.max(y, 0);
    }

    public int getDockSide() {
        return this.dock;
    }

    public void setDockSide(int side) {
        if (side < -1 || side > 3) {
            throw new IllegalArgumentException("Illegal dock side: " + side);
        }
        this.dock = side;
    }

    public boolean isExternal() {
        return this.external;
    }

    public void setExternal(boolean b) {
        this.external = b;
    }

    public Boolean getFlowX() {
        return this.flowX;
    }

    public void setFlowX(Boolean b) {
        this.flowX = b;
    }

    public int getHideMode() {
        return this.hideMode;
    }

    public void setHideMode(int mode) {
        if (mode < -1 || mode > 3) {
            throw new IllegalArgumentException("Wrong hideMode: " + mode);
        }
        this.hideMode = mode;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id2) {
        this.id = id2;
    }

    public UnitValue[] getPadding() {
        if (this.padding == null) {
            return null;
        }
        return new UnitValue[]{this.padding[0], this.padding[1], this.padding[2], this.padding[3]};
    }

    public void setPadding(UnitValue[] sides) {
        this.padding = sides != null ? new UnitValue[]{sides[0], sides[1], sides[2], sides[3]} : null;
    }

    public UnitValue[] getVisualPadding() {
        if (this.visualPadding == null) {
            return null;
        }
        return new UnitValue[]{this.visualPadding[0], this.visualPadding[1], this.visualPadding[2], this.visualPadding[3]};
    }

    public void setVisualPadding(UnitValue[] sides) {
        this.visualPadding = sides != null ? new UnitValue[]{sides[0], sides[1], sides[2], sides[3]} : null;
    }

    public int getSkip() {
        return this.skip;
    }

    public void setSkip(int cells) {
        this.skip = cells;
    }

    public int getSpanX() {
        return this.spanX;
    }

    public void setSpanX(int cells) {
        this.spanX = cells;
    }

    public int getSpanY() {
        return this.spanY;
    }

    public void setSpanY(int cells) {
        this.spanY = cells;
    }

    public Float getPushX() {
        return this.pushX;
    }

    public void setPushX(Float weight) {
        this.pushX = weight;
    }

    public Float getPushY() {
        return this.pushY;
    }

    public void setPushY(Float weight) {
        this.pushY = weight;
    }

    public int getSplit() {
        return this.split;
    }

    public void setSplit(int parts) {
        this.split = parts;
    }

    public String getTag() {
        return this.tag;
    }

    public void setTag(String tag2) {
        this.tag = tag2;
    }

    public boolean isWrap() {
        return this.wrap != null;
    }

    public void setWrap(boolean b) {
        this.wrap = b ? this.wrap == null ? DEF_GAP : this.wrap : null;
    }

    public BoundSize getWrapGapSize() {
        if (this.wrap == DEF_GAP) {
            return null;
        }
        return this.wrap;
    }

    public void setWrapGapSize(BoundSize s) {
        if (s == null) {
            s = this.wrap != null ? DEF_GAP : null;
        }
        this.wrap = s;
    }

    public boolean isNewline() {
        return this.newline != null;
    }

    public void setNewline(boolean b) {
        this.newline = b ? this.newline == null ? DEF_GAP : this.newline : null;
    }

    public BoundSize getNewlineGapSize() {
        if (this.newline == DEF_GAP) {
            return null;
        }
        return this.newline;
    }

    public void setNewlineGapSize(BoundSize s) {
        if (s == null) {
            s = this.newline != null ? DEF_GAP : null;
        }
        this.newline = s;
    }

    public AnimSpec getAnimSpec() {
        return this.animSpec;
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
        if (getClass() == CC.class) {
            LayoutUtil.writeAsXML(out, this);
        }
    }
}
