package net.miginfocom.layout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeSet;
import java.util.WeakHashMap;
import javassist.bytecode.Opcode;

public final class Grid {
    private static final CC DEF_CC = new CC();
    private static final DimConstraint DOCK_DIM_CONSTRAINT = new DimConstraint();
    private static final ResizeConstraint GAP_RC_CONST = new ResizeConstraint(Opcode.GOTO_W, ResizeConstraint.WEIGHT_100, 50, null);
    private static final ResizeConstraint GAP_RC_CONST_PUSH = new ResizeConstraint(Opcode.GOTO_W, ResizeConstraint.WEIGHT_100, 50, ResizeConstraint.WEIGHT_100);
    private static final Float[] GROW_100 = {ResizeConstraint.WEIGHT_100};
    private static final int MAX_DOCK_GRID = 32767;
    private static final int MAX_GRID = 30000;
    private static WeakHashMap<Object, ArrayList<WeakCell>> PARENT_GRIDPOS_MAP = null;
    private static WeakHashMap<Object, int[][]>[] PARENT_ROWCOL_SIZES_MAP = null;
    public static final boolean TEST_GAPS = true;
    private final ArrayList<LayoutCallback> callbackList;
    private final AC colConstr;
    private FlowSizeSpec colFlowSpecs = null;
    private final ArrayList<LinkedDimGroup>[] colGroupLists;
    private final TreeSet<Integer> colIndexes = new TreeSet<>();
    private final ContainerWrapper container;
    private ArrayList<int[]> debugRects = null;
    private final int dockOffX;
    private final int dockOffY;
    private final LinkedHashMap<Integer, Cell> grid = new LinkedHashMap<>();
    private int[] height = null;
    private int lastRefHeight = 0;
    private int lastRefWidth = 0;
    private final LC lc;
    private HashMap<String, Boolean> linkTargetIDs = null;
    private final Float[] pushXs;
    private final Float[] pushYs;
    private final AC rowConstr;
    private FlowSizeSpec rowFlowSpecs = null;
    private final ArrayList<LinkedDimGroup>[] rowGroupLists;
    private final TreeSet<Integer> rowIndexes = new TreeSet<>();
    private int[] width = null;
    private HashMap<Integer, BoundSize> wrapGapMap = null;

    static {
        DOCK_DIM_CONSTRAINT.setGrowPriority(0);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:197:0x0447, code lost:
        if (r9.isWrap() == false) goto L_0x045a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:198:0x0449, code lost:
        wrap(r0, r9.getWrapGapSize());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:199:0x0454, code lost:
        r54 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:201:0x045a, code lost:
        r31 = true;
     */
    /* JADX WARNING: Removed duplicated region for block: B:114:0x031d  */
    /* JADX WARNING: Removed duplicated region for block: B:117:0x032b  */
    /* JADX WARNING: Removed duplicated region for block: B:120:0x0334  */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x0352  */
    /* JADX WARNING: Removed duplicated region for block: B:136:0x0375  */
    /* JADX WARNING: Removed duplicated region for block: B:137:0x0379  */
    /* JADX WARNING: Removed duplicated region for block: B:138:0x037e  */
    /* JADX WARNING: Removed duplicated region for block: B:139:0x0381  */
    /* JADX WARNING: Removed duplicated region for block: B:168:0x03cd  */
    /* JADX WARNING: Removed duplicated region for block: B:171:0x03d7  */
    /* JADX WARNING: Removed duplicated region for block: B:174:0x03e4  */
    /* JADX WARNING: Removed duplicated region for block: B:202:0x045d  */
    /* JADX WARNING: Removed duplicated region for block: B:203:0x0463  */
    /* JADX WARNING: Removed duplicated region for block: B:204:0x0468  */
    /* JADX WARNING: Removed duplicated region for block: B:68:0x0219  */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x0272 A[LOOP:3: B:84:0x0272->B:88:0x0299, LOOP_START] */
    /* JADX WARNING: Removed duplicated region for block: B:95:0x02ae  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Grid(net.miginfocom.layout.ContainerWrapper r56, net.miginfocom.layout.LC r57, net.miginfocom.layout.AC r58, net.miginfocom.layout.AC r59, java.util.Map<? extends net.miginfocom.layout.ComponentWrapper, net.miginfocom.layout.CC> r60, java.util.ArrayList<net.miginfocom.layout.LayoutCallback> r61) {
        /*
        // Method dump skipped, instructions count: 1666
        */
        throw new UnsupportedOperationException("Method not decompiled: net.miginfocom.layout.Grid.<init>(net.miginfocom.layout.ContainerWrapper, net.miginfocom.layout.LC, net.miginfocom.layout.AC, net.miginfocom.layout.AC, java.util.Map, java.util.ArrayList):void");
    }

    private void ensureIndexSizes(int colCount, int rowCount) {
        for (int i = 0; i < colCount; i++) {
            this.colIndexes.add(Integer.valueOf(i));
        }
        for (int i2 = 0; i2 < rowCount; i2++) {
            this.rowIndexes.add(Integer.valueOf(i2));
        }
    }

    private static CC getCC(ComponentWrapper comp, Map<? extends ComponentWrapper, CC> ccMap) {
        CC cc = ccMap.get(comp);
        return cc != null ? cc : DEF_CC;
    }

    private void addLinkIDs(CC cc) {
        String[] linkIDs = cc.getLinkTargets();
        for (String linkID : linkIDs) {
            if (this.linkTargetIDs == null) {
                this.linkTargetIDs = new HashMap<>();
            }
            this.linkTargetIDs.put(linkID, null);
        }
    }

    public void invalidateContainerSize() {
        this.colFlowSpecs = null;
        invalidateComponentSizes();
    }

    private void invalidateComponentSizes() {
        for (Cell cell : this.grid.values()) {
            Iterator it = cell.compWraps.iterator();
            while (it.hasNext()) {
                ((CompWrap) it.next()).invalidateSizes();
            }
        }
    }

    public boolean layout(int[] bounds, UnitValue alignX, UnitValue alignY, boolean debug, boolean notUsed) {
        return layoutImpl(bounds, alignX, alignY, debug, false);
    }

    public boolean layout(int[] bounds, UnitValue alignX, UnitValue alignY, boolean debug) {
        return layoutImpl(bounds, alignX, alignY, debug, false);
    }

    private boolean layoutImpl(int[] bounds, UnitValue alignX, UnitValue alignY, boolean debug, boolean trialRun) {
        if (debug) {
            this.debugRects = new ArrayList<>();
        }
        if (this.colFlowSpecs == null) {
            checkSizeCalcs(bounds[2], bounds[3]);
        }
        resetLinkValues(true, true);
        layoutInOneDim(bounds[2], alignX, false, this.pushXs);
        layoutInOneDim(bounds[3], alignY, true, this.pushYs);
        HashMap<String, Integer> endGrpXMap = null;
        HashMap<String, Integer> endGrpYMap = null;
        int compCount = this.container.getComponentCount();
        boolean addVisualPadding = this.lc.isVisualPadding();
        boolean layoutAgain = false;
        if (compCount > 0) {
            int j = 0;
            while (true) {
                if (j >= (this.linkTargetIDs != null ? 2 : 1)) {
                    break;
                }
                int count = 0;
                while (true) {
                    boolean doAgain = false;
                    for (Cell cell : this.grid.values()) {
                        Iterator it = cell.compWraps.iterator();
                        while (it.hasNext()) {
                            CompWrap cw = (CompWrap) it.next();
                            if (j == 0) {
                                doAgain |= doAbsoluteCorrections(cw, bounds);
                                if (!doAgain) {
                                    if (cw.cc.getHorizontal().getEndGroup() != null) {
                                        endGrpXMap = addToEndGroup(endGrpXMap, cw.cc.getHorizontal().getEndGroup(), cw.x + cw.w);
                                    }
                                    if (cw.cc.getVertical().getEndGroup() != null) {
                                        endGrpYMap = addToEndGroup(endGrpYMap, cw.cc.getVertical().getEndGroup(), cw.y + cw.h);
                                    }
                                }
                                if (this.linkTargetIDs != null && (this.linkTargetIDs.containsKey("visual") || this.linkTargetIDs.containsKey("container"))) {
                                    layoutAgain = true;
                                }
                            }
                            if (this.linkTargetIDs == null || j == 1) {
                                if (cw.cc.getHorizontal().getEndGroup() != null) {
                                    cw.w = endGrpXMap.get(cw.cc.getHorizontal().getEndGroup()).intValue() - cw.x;
                                }
                                if (cw.cc.getVertical().getEndGroup() != null) {
                                    cw.h = endGrpYMap.get(cw.cc.getVertical().getEndGroup()).intValue() - cw.y;
                                }
                                cw.x += bounds[0];
                                cw.y += bounds[1];
                                if (!trialRun) {
                                    cw.transferBounds(addVisualPadding);
                                }
                                if (this.callbackList != null) {
                                    Iterator<LayoutCallback> it2 = this.callbackList.iterator();
                                    while (it2.hasNext()) {
                                        it2.next().correctBounds(cw.comp);
                                    }
                                }
                            }
                        }
                    }
                    clearGroupLinkBounds();
                    count++;
                    if (count <= (compCount << 3) + 10) {
                        if (!doAgain) {
                            break;
                        }
                    } else {
                        System.err.println("Unstable cyclic dependency in absolute linked values.");
                        break;
                    }
                }
                j++;
            }
        }
        if (debug) {
            for (Cell cell2 : this.grid.values()) {
                Iterator<CompWrap> it3 = cell2.compWraps.iterator();
                while (it3.hasNext()) {
                    CompWrap cw2 = it3.next();
                    LinkedDimGroup hGrp = getGroupContaining(this.colGroupLists, cw2);
                    LinkedDimGroup vGrp = getGroupContaining(this.rowGroupLists, cw2);
                    if (!(hGrp == null || vGrp == null)) {
                        ArrayList<int[]> arrayList = this.debugRects;
                        int[] iArr = new int[4];
                        iArr[0] = (bounds[0] + hGrp.lStart) - (hGrp.fromEnd ? hGrp.lSize : 0);
                        iArr[1] = (bounds[1] + vGrp.lStart) - (vGrp.fromEnd ? vGrp.lSize : 0);
                        iArr[2] = hGrp.lSize;
                        iArr[3] = vGrp.lSize;
                        arrayList.add(iArr);
                    }
                }
            }
        }
        return layoutAgain;
    }

    public void paintDebug() {
        if (this.debugRects != null) {
            this.container.paintDebugOutline(this.lc.isVisualPadding());
            ArrayList<int[]> painted = new ArrayList<>();
            Iterator<int[]> it = this.debugRects.iterator();
            while (it.hasNext()) {
                int[] r = it.next();
                if (!painted.contains(r)) {
                    this.container.paintDebugCell(r[0], r[1], r[2], r[3]);
                    painted.add(r);
                }
            }
            for (Cell cell : this.grid.values()) {
                Iterator<CompWrap> it2 = cell.compWraps.iterator();
                while (it2.hasNext()) {
                    it2.next().comp.paintDebugOutline(this.lc.isVisualPadding());
                }
            }
        }
    }

    public ContainerWrapper getContainer() {
        return this.container;
    }

    public final int[] getWidth() {
        return getWidth(this.lastRefHeight);
    }

    public final int[] getWidth(int refHeight) {
        checkSizeCalcs(this.lastRefWidth, refHeight);
        return (int[]) this.width.clone();
    }

    public final int[] getHeight() {
        return getHeight(this.lastRefWidth);
    }

    public final int[] getHeight(int refWidth) {
        checkSizeCalcs(refWidth, this.lastRefHeight);
        return (int[]) this.height.clone();
    }

    private void checkSizeCalcs(int refWidth, int refHeight) {
        if (this.colFlowSpecs == null) {
            calcGridSizes(refWidth, refHeight);
        }
        if ((refWidth > 0 && refWidth != this.lastRefWidth) || (refHeight > 0 && refHeight != this.lastRefHeight)) {
            int[] refBounds = new int[4];
            refBounds[0] = 0;
            refBounds[1] = 0;
            refBounds[2] = refWidth > 0 ? refWidth : this.width[1];
            refBounds[3] = refHeight > 0 ? refHeight : this.height[1];
            layoutImpl(refBounds, null, null, false, true);
            calcGridSizes(refWidth, refHeight);
        }
        this.lastRefWidth = refWidth;
        this.lastRefHeight = refHeight;
    }

    private void calcGridSizes(int refWidth, int refHeight) {
        FlowSizeSpec colSpecs = calcRowsOrColsSizes(true, refWidth);
        FlowSizeSpec rowSpecs = calcRowsOrColsSizes(false, refHeight);
        this.colFlowSpecs = colSpecs;
        this.rowFlowSpecs = rowSpecs;
        this.width = getMinPrefMaxSumSize(true, colSpecs.sizes);
        this.height = getMinPrefMaxSumSize(false, rowSpecs.sizes);
        if (this.linkTargetIDs == null) {
            resetLinkValues(false, true);
        } else {
            layout(new int[]{0, 0, refWidth, refHeight}, null, null, false);
            resetLinkValues(false, false);
        }
        adjustSizeForAbsolute(true);
        adjustSizeForAbsolute(false);
    }

    private UnitValue[] getPos(ComponentWrapper cw, CC cc) {
        UnitValue[] callbackPos = null;
        if (this.callbackList != null) {
            for (int i = 0; i < this.callbackList.size() && callbackPos == null; i++) {
                callbackPos = this.callbackList.get(i).getPosition(cw);
            }
        }
        UnitValue[] ccPos = cc.getPos();
        if (callbackPos == null || ccPos == null) {
            if (callbackPos == null) {
                callbackPos = ccPos;
            }
            return callbackPos;
        }
        for (int i2 = 0; i2 < 4; i2++) {
            UnitValue cbUv = callbackPos[i2];
            if (cbUv != null) {
                ccPos[i2] = cbUv;
            }
        }
        return ccPos;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private BoundSize[] getCallbackSize(ComponentWrapper cw) {
        if (this.callbackList != null) {
            Iterator<LayoutCallback> it = this.callbackList.iterator();
            while (it.hasNext()) {
                BoundSize[] bs = it.next().getSize(cw);
                if (bs != null) {
                    return bs;
                }
            }
        }
        return null;
    }

    private static int getDockInsets(TreeSet<Integer> set) {
        int c = 0;
        Iterator<Integer> it = set.iterator();
        while (it.hasNext() && it.next().intValue() < -30000) {
            c++;
        }
        return c;
    }

    private boolean setLinkedBounds(ComponentWrapper cw, CC cc, int x, int y, int w, int h, boolean external) {
        boolean z;
        String id = cc.getId() != null ? cc.getId() : cw.getLinkId();
        if (id == null) {
            return false;
        }
        String gid = null;
        int grIx = id.indexOf(46);
        if (grIx != -1) {
            gid = id.substring(0, grIx);
            id = id.substring(grIx + 1);
        }
        Object lay = this.container.getLayout();
        boolean changed = false;
        if (external || (this.linkTargetIDs != null && this.linkTargetIDs.containsKey(id))) {
            changed = LinkHandler.setBounds(lay, id, x, y, w, h, !external, false);
        }
        if (gid == null) {
            return changed;
        }
        if (!external && (this.linkTargetIDs == null || !this.linkTargetIDs.containsKey(gid))) {
            return changed;
        }
        if (this.linkTargetIDs == null) {
            this.linkTargetIDs = new HashMap<>(4);
        }
        this.linkTargetIDs.put(gid, Boolean.TRUE);
        if (!external) {
            z = true;
        } else {
            z = false;
        }
        return changed | LinkHandler.setBounds(lay, gid, x, y, w, h, z, true);
    }

    private int increase(int[] p, int cnt) {
        if (this.lc.isFlowX()) {
            int i = p[0] + cnt;
            p[0] = i;
            return i;
        }
        int i2 = p[1] + cnt;
        p[1] = i2;
        return i2;
    }

    private void wrap(int[] cellXY, BoundSize gapSize) {
        int i;
        char c;
        boolean flowx = this.lc.isFlowX();
        cellXY[0] = flowx ? 0 : cellXY[0] + 1;
        if (flowx) {
            i = cellXY[1] + 1;
        } else {
            i = 0;
        }
        cellXY[1] = i;
        if (gapSize != null) {
            if (this.wrapGapMap == null) {
                this.wrapGapMap = new HashMap<>(8);
            }
            HashMap<Integer, BoundSize> hashMap = this.wrapGapMap;
            if (flowx) {
                c = 1;
            } else {
                c = 0;
            }
            hashMap.put(Integer.valueOf(cellXY[c]), gapSize);
        }
        if (flowx) {
            this.rowIndexes.add(Integer.valueOf(cellXY[1]));
        } else {
            this.colIndexes.add(Integer.valueOf(cellXY[0]));
        }
    }

    private static void sortCellsByPlatform(Collection<Cell> cells, ContainerWrapper parent) {
        CompWrap compWrap;
        boolean z;
        String order = PlatformDefaults.getButtonOrder();
        String orderLo = order.toLowerCase();
        int unrelSize = PlatformDefaults.convertToPixels(1.0f, "u", true, 0.0f, parent, null);
        if (unrelSize == -87654312) {
            throw new IllegalArgumentException("'unrelated' not recognized by PlatformDefaults!");
        }
        int[] gapUnrel = {unrelSize, unrelSize, -2147471302};
        int[] flGap = {0, 0, -2147471302};
        for (Cell cell : cells) {
            if (cell.hasTagged) {
                CompWrap prevCW = null;
                boolean nextUnrel = false;
                boolean nextPush = false;
                ArrayList<CompWrap> sortedList = new ArrayList<>(cell.compWraps.size());
                int iSz = orderLo.length();
                for (int i = 0; i < iSz; i++) {
                    char c = orderLo.charAt(i);
                    if (c == '+' || c == '_') {
                        nextUnrel = true;
                        if (c == '+') {
                            nextPush = true;
                        }
                    } else {
                        String tag = PlatformDefaults.getTagForChar(c);
                        if (tag != null) {
                            int jSz = cell.compWraps.size();
                            for (int j = 0; j < jSz; j++) {
                                CompWrap cw = (CompWrap) cell.compWraps.get(j);
                                if (tag.equals(cw.cc.getTag())) {
                                    if (Character.isUpperCase(order.charAt(i))) {
                                        cw.adjustMinHorSizeUp((int) PlatformDefaults.getMinimumButtonWidthIncludingPadding(0.0f, parent, cw.comp));
                                    }
                                    sortedList.add(cw);
                                    if (nextUnrel) {
                                        if (prevCW != null) {
                                            compWrap = prevCW;
                                        } else {
                                            compWrap = cw;
                                        }
                                        boolean z2 = cell.flowx;
                                        if (prevCW == null) {
                                            z = true;
                                        } else {
                                            z = false;
                                        }
                                        compWrap.mergeGapSizes(gapUnrel, z2, z);
                                        if (nextPush) {
                                            cw.forcedPushGaps = 1;
                                            nextUnrel = false;
                                            nextPush = false;
                                        }
                                    }
                                    if (c == 'u') {
                                        nextUnrel = true;
                                    }
                                    prevCW = cw;
                                }
                            }
                        }
                    }
                }
                if (sortedList.size() > 0) {
                    CompWrap cw2 = sortedList.get(sortedList.size() - 1);
                    if (nextUnrel) {
                        cw2.mergeGapSizes(gapUnrel, cell.flowx, false);
                        if (nextPush) {
                            cw2.forcedPushGaps |= 2;
                        }
                    }
                    if (cw2.cc.getHorizontal().getGapAfter() == null) {
                        cw2.setGaps(flGap, 3);
                    }
                    CompWrap cw3 = sortedList.get(0);
                    if (cw3.cc.getHorizontal().getGapBefore() == null) {
                        cw3.setGaps(flGap, 1);
                    }
                }
                if (cell.compWraps.size() == sortedList.size()) {
                    cell.compWraps.clear();
                } else {
                    cell.compWraps.removeAll(sortedList);
                }
                cell.compWraps.addAll(sortedList);
            }
        }
    }

    private Float[] getDefaultPushWeights(boolean isRows) {
        ArrayList<LinkedDimGroup>[] groupLists = isRows ? this.rowGroupLists : this.colGroupLists;
        Float[] pushWeightArr = GROW_100;
        int i = 0;
        int ix = 1;
        while (i < groupLists.length) {
            Float rowPushWeight = null;
            Iterator<LinkedDimGroup> it = groupLists[i].iterator();
            while (it.hasNext()) {
                LinkedDimGroup grp = it.next();
                for (int c = 0; c < grp._compWraps.size(); c++) {
                    CompWrap cw = (CompWrap) grp._compWraps.get(c);
                    Float pushWeight = (cw.comp.isVisible() ? -1 : cw.cc.getHideMode() != -1 ? cw.cc.getHideMode() : this.lc.getHideMode()) < 2 ? isRows ? cw.cc.getPushY() : cw.cc.getPushX() : null;
                    if (rowPushWeight == null || (pushWeight != null && pushWeight.floatValue() > rowPushWeight.floatValue())) {
                        rowPushWeight = pushWeight;
                    }
                }
            }
            if (rowPushWeight != null) {
                if (pushWeightArr == GROW_100) {
                    pushWeightArr = new Float[((groupLists.length << 1) + 1)];
                }
                pushWeightArr[ix] = rowPushWeight;
            }
            i++;
            ix += 2;
        }
        return pushWeightArr;
    }

    private void clearGroupLinkBounds() {
        if (this.linkTargetIDs != null) {
            for (Map.Entry<String, Boolean> o : this.linkTargetIDs.entrySet()) {
                if (o.getValue() == Boolean.TRUE) {
                    LinkHandler.clearBounds(this.container.getLayout(), o.getKey());
                }
            }
        }
    }

    private void resetLinkValues(boolean parentSize, boolean compLinks) {
        Object lay = this.container.getLayout();
        if (compLinks) {
            LinkHandler.clearTemporaryBounds(lay);
        }
        boolean defIns = !hasDocks();
        int parW = parentSize ? this.lc.getWidth().constrain(this.container.getWidth(), (float) getParentSize(this.container, true), this.container) : 0;
        int parH = parentSize ? this.lc.getHeight().constrain(this.container.getHeight(), (float) getParentSize(this.container, false), this.container) : 0;
        int insX = LayoutUtil.getInsets(this.lc, 0, defIns).getPixels(0.0f, this.container, null);
        int insY = LayoutUtil.getInsets(this.lc, 1, defIns).getPixels(0.0f, this.container, null);
        LinkHandler.setBounds(lay, "visual", insX, insY, (parW - insX) - LayoutUtil.getInsets(this.lc, 2, defIns).getPixels(0.0f, this.container, null), (parH - insY) - LayoutUtil.getInsets(this.lc, 3, defIns).getPixels(0.0f, this.container, null), true, false);
        LinkHandler.setBounds(lay, "container", 0, 0, parW, parH, true, false);
    }

    private static LinkedDimGroup getGroupContaining(ArrayList<LinkedDimGroup>[] groupLists, CompWrap cw) {
        for (ArrayList<LinkedDimGroup> groups : groupLists) {
            Iterator<LinkedDimGroup> it = groups.iterator();
            while (it.hasNext()) {
                LinkedDimGroup group = it.next();
                Iterator<CompWrap> it2 = group._compWraps.iterator();
                while (true) {
                    if (it2.hasNext()) {
                        if (it2.next() == cw) {
                            return group;
                        }
                    }
                }
            }
        }
        return null;
    }

    private boolean doAbsoluteCorrections(CompWrap cw, int[] bounds) {
        int[] stSz = getAbsoluteDimBounds(cw, bounds[2], true);
        if (stSz != null) {
            cw.setDimBounds(stSz[0], stSz[1], true);
        }
        int[] stSz2 = getAbsoluteDimBounds(cw, bounds[3], false);
        if (stSz2 != null) {
            cw.setDimBounds(stSz2[0], stSz2[1], false);
        }
        if (this.linkTargetIDs != null) {
            return setLinkedBounds(cw.comp, cw.cc, cw.x, cw.y, cw.w, cw.h, false);
        }
        return false;
    }

    private void adjustSizeForAbsolute(boolean isHor) {
        int[] curSizes = isHor ? this.width : this.height;
        Cell absCell = this.grid.get(null);
        if (!(absCell == null || absCell.compWraps.size() == 0)) {
            ArrayList<CompWrap> cws = absCell.compWraps;
            int maxEnd = 0;
            int cwSz = absCell.compWraps.size();
            for (int j = 0; j < cwSz + 3; j++) {
                boolean doAgain = false;
                for (int i = 0; i < cwSz; i++) {
                    CompWrap cw = cws.get(i);
                    int[] stSz = getAbsoluteDimBounds(cw, 0, isHor);
                    int end = stSz[0] + stSz[1];
                    if (maxEnd < end) {
                        maxEnd = end;
                    }
                    if (this.linkTargetIDs != null) {
                        doAgain |= setLinkedBounds(cw.comp, cw.cc, stSz[0], stSz[0], stSz[1], stSz[1], false);
                    }
                }
                if (!doAgain) {
                    break;
                }
                maxEnd = 0;
                clearGroupLinkBounds();
            }
            int maxEnd2 = maxEnd + LayoutUtil.getInsets(this.lc, isHor ? 3 : 2, !hasDocks()).getPixels(0.0f, this.container, null);
            if (curSizes[0] < maxEnd2) {
                curSizes[0] = maxEnd2;
            }
            if (curSizes[1] < maxEnd2) {
                curSizes[1] = maxEnd2;
            }
        }
    }

    private int[] getAbsoluteDimBounds(CompWrap cw, int refSize, boolean isHor) {
        if (!cw.cc.isExternal()) {
            UnitValue[] pad = cw.cc.getPadding();
            UnitValue[] pos = getPos(cw.comp, cw.cc);
            if (pos == null && pad == null) {
                return null;
            }
            int st = isHor ? cw.x : cw.y;
            int sz = isHor ? cw.w : cw.h;
            if (pos != null) {
                UnitValue stUV = pos[isHor ? (char) 0 : 1];
                UnitValue endUV = pos[isHor ? (char) 2 : 3];
                int minSz = cw.getSize(0, isHor);
                int maxSz = cw.getSize(2, isHor);
                sz = Math.min(Math.max(cw.getSize(1, isHor), minSz), maxSz);
                if (stUV != null) {
                    st = stUV.getPixels(stUV.getUnit() == 12 ? (float) sz : (float) refSize, this.container, cw.comp);
                    if (endUV != null) {
                        sz = Math.min(Math.max((isHor ? cw.x + cw.w : cw.y + cw.h) - st, minSz), maxSz);
                    }
                }
                if (endUV != null) {
                    if (stUV != null) {
                        sz = Math.min(Math.max(endUV.getPixels((float) refSize, this.container, cw.comp) - st, minSz), maxSz);
                    } else {
                        st = endUV.getPixels((float) refSize, this.container, cw.comp) - sz;
                    }
                }
            }
            if (pad != null) {
                UnitValue uv = pad[isHor ? (char) 1 : 0];
                int p = uv != null ? uv.getPixels((float) refSize, this.container, cw.comp) : 0;
                st += p;
                UnitValue uv2 = pad[isHor ? (char) 3 : 2];
                sz += (uv2 != null ? uv2.getPixels((float) refSize, this.container, cw.comp) : 0) + (-p);
            }
            return new int[]{st, sz};
        } else if (isHor) {
            return new int[]{cw.comp.getX(), cw.comp.getWidth()};
        } else {
            return new int[]{cw.comp.getY(), cw.comp.getHeight()};
        }
    }

    private void layoutInOneDim(int refSize, UnitValue align, boolean isRows, Float[] defaultPushWeights) {
        DimConstraint primDC;
        boolean fromEnd = !isRows ? !LayoutUtil.isLeftToRight(this.lc, this.container) : !this.lc.isTopToBottom();
        DimConstraint[] primDCs = (isRows ? this.rowConstr : this.colConstr).getConstaints();
        FlowSizeSpec fss = isRows ? this.rowFlowSpecs : this.colFlowSpecs;
        ArrayList<LinkedDimGroup>[] rowCols = isRows ? this.rowGroupLists : this.colGroupLists;
        int[] rowColSizes = LayoutUtil.calculateSerial(fss.sizes, fss.resConstsInclGaps, defaultPushWeights, 1, refSize);
        if (LayoutUtil.isDesignTime(this.container)) {
            TreeSet<Integer> indexes = isRows ? this.rowIndexes : this.colIndexes;
            int[] ixArr = new int[indexes.size()];
            int ix = 0;
            Iterator<Integer> it = indexes.iterator();
            while (it.hasNext()) {
                ixArr[ix] = it.next().intValue();
                ix++;
            }
            putSizesAndIndexes(this.container.getComponent(), rowColSizes, ixArr, isRows);
        }
        int curPos = align != null ? align.getPixels((float) (refSize - LayoutUtil.sum(rowColSizes)), this.container, null) : 0;
        if (fromEnd) {
            curPos = refSize - curPos;
        }
        for (int i = 0; i < rowCols.length; i++) {
            ArrayList<LinkedDimGroup> linkedGroups = rowCols[i];
            int scIx = i - (isRows ? this.dockOffY : this.dockOffX);
            int bIx = i << 1;
            int bIx2 = bIx + 1;
            int curPos2 = curPos + (fromEnd ? -rowColSizes[bIx] : rowColSizes[bIx]);
            if (scIx >= 0) {
                if (scIx >= primDCs.length) {
                    scIx = primDCs.length - 1;
                }
                primDC = primDCs[scIx];
            } else {
                primDC = DOCK_DIM_CONSTRAINT;
            }
            int rowSize = rowColSizes[bIx2];
            Iterator<LinkedDimGroup> it2 = linkedGroups.iterator();
            while (it2.hasNext()) {
                LinkedDimGroup group = it2.next();
                int groupSize = rowSize;
                if (group.span > 1) {
                    groupSize = LayoutUtil.sum(rowColSizes, bIx2, Math.min((group.span << 1) - 1, (rowColSizes.length - bIx2) - 1));
                }
                group.layout(primDC, curPos2, groupSize, group.span);
            }
            if (fromEnd) {
                rowSize = -rowSize;
            }
            curPos = curPos2 + rowSize;
        }
    }

    private static void addToSizeGroup(HashMap<String, int[]> sizeGroups, String sizeGroup, int[] size) {
        int[] sgSize = sizeGroups.get(sizeGroup);
        if (sgSize == null) {
            sizeGroups.put(sizeGroup, new int[]{size[0], size[1], size[2]});
            return;
        }
        sgSize[0] = Math.max(size[0], sgSize[0]);
        sgSize[1] = Math.max(size[1], sgSize[1]);
        sgSize[2] = Math.min(size[2], sgSize[2]);
    }

    private static HashMap<String, Integer> addToEndGroup(HashMap<String, Integer> endGroups, String endGroup, int end) {
        if (endGroup != null) {
            if (endGroups == null) {
                endGroups = new HashMap<>(4);
            }
            Integer oldEnd = endGroups.get(endGroup);
            if (oldEnd == null || end > oldEnd.intValue()) {
                endGroups.put(endGroup, Integer.valueOf(end));
            }
        }
        return endGroups;
    }

    private FlowSizeSpec calcRowsOrColsSizes(boolean isHor, int containerSize) {
        ArrayList<LinkedDimGroup>[] groupsLists = isHor ? this.colGroupLists : this.rowGroupLists;
        Float[] defPush = isHor ? this.pushXs : this.pushYs;
        if (containerSize <= 0) {
            containerSize = isHor ? this.container.getWidth() : this.container.getHeight();
        }
        BoundSize cSz = isHor ? this.lc.getWidth() : this.lc.getHeight();
        if (!cSz.isUnset()) {
            containerSize = cSz.constrain(containerSize, (float) getParentSize(this.container, isHor), this.container);
        }
        DimConstraint[] primDCs = (isHor ? this.colConstr : this.rowConstr).getConstaints();
        TreeSet<Integer> primIndexes = isHor ? this.colIndexes : this.rowIndexes;
        int[][] rowColBoundSizes = new int[primIndexes.size()][];
        HashMap<String, int[]> sizeGroupMap = new HashMap<>(4);
        DimConstraint[] allDCs = new DimConstraint[primIndexes.size()];
        Iterator<Integer> primIt = primIndexes.iterator();
        for (int r = 0; r < rowColBoundSizes.length; r++) {
            int cellIx = primIt.next().intValue();
            int[] rowColSizes = new int[3];
            if (cellIx < -30000 || cellIx > MAX_GRID) {
                allDCs[r] = DOCK_DIM_CONSTRAINT;
            } else {
                allDCs[r] = primDCs[cellIx >= primDCs.length ? primDCs.length - 1 : cellIx];
            }
            ArrayList<LinkedDimGroup> groups = groupsLists[r];
            int[] groupSizes = {getTotalGroupsSizeParallel(groups, 0, false), getTotalGroupsSizeParallel(groups, 1, false), 2097051};
            correctMinMax(groupSizes);
            BoundSize dimSize = allDCs[r].getSize();
            for (int sType = 0; sType <= 2; sType++) {
                int rowColSize = groupSizes[sType];
                UnitValue uv = dimSize.getSize(sType);
                if (uv != null) {
                    int unit = uv.getUnit();
                    rowColSize = unit == 14 ? groupSizes[1] : unit == 13 ? groupSizes[0] : unit == 15 ? groupSizes[2] : uv.getPixels((float) containerSize, this.container, null);
                } else if (cellIx >= -30000 && cellIx <= MAX_GRID && rowColSize == 0) {
                    rowColSize = LayoutUtil.isDesignTime(this.container) ? LayoutUtil.getDesignTimeEmptySize() : 0;
                }
                rowColSizes[sType] = rowColSize;
            }
            correctMinMax(rowColSizes);
            addToSizeGroup(sizeGroupMap, allDCs[r].getSizeGroup(), rowColSizes);
            rowColBoundSizes[r] = rowColSizes;
        }
        if (sizeGroupMap.size() > 0) {
            for (int r2 = 0; r2 < rowColBoundSizes.length; r2++) {
                if (allDCs[r2].getSizeGroup() != null) {
                    rowColBoundSizes[r2] = sizeGroupMap.get(allDCs[r2].getSizeGroup());
                }
            }
        }
        ResizeConstraint[] resConstrs = getRowResizeConstraints(allDCs);
        boolean[] fillInPushGaps = new boolean[(allDCs.length + 1)];
        FlowSizeSpec fss = mergeSizesGapsAndResConstrs(resConstrs, fillInPushGaps, rowColBoundSizes, getRowGaps(allDCs, containerSize, isHor, fillInPushGaps));
        adjustMinPrefForSpanningComps(allDCs, defPush, fss, groupsLists);
        return fss;
    }

    private static int getParentSize(ComponentWrapper cw, boolean isHor) {
        if (cw.getParent() != null) {
            return isHor ? cw.getWidth() : cw.getHeight();
        }
        return 0;
    }

    private int[] getMinPrefMaxSumSize(boolean isHor, int[][] sizes) {
        int[] retSizes = new int[3];
        BoundSize sz = isHor ? this.lc.getWidth() : this.lc.getHeight();
        for (int i = 0; i < sizes.length; i++) {
            if (sizes[i] != null) {
                int[] size = sizes[i];
                for (int sType = 0; sType <= 2; sType++) {
                    if (sz.getSize(sType) == null) {
                        int s = size[sType];
                        if (s != -2147471302) {
                            if (sType == 1) {
                                int bnd = size[2];
                                if (bnd != -2147471302 && bnd < s) {
                                    s = bnd;
                                }
                                int bnd2 = size[0];
                                if (bnd2 > s) {
                                    s = bnd2;
                                }
                            }
                            retSizes[sType] = retSizes[sType] + s;
                        }
                        if (size[2] == -2147471302 || retSizes[2] > 2097051) {
                            retSizes[2] = 2097051;
                        }
                    } else if (i == 0) {
                        retSizes[sType] = sz.getSize(sType).getPixels((float) getParentSize(this.container, isHor), this.container, null);
                    }
                }
            }
        }
        correctMinMax(retSizes);
        return retSizes;
    }

    private static ResizeConstraint[] getRowResizeConstraints(DimConstraint[] specs) {
        ResizeConstraint[] resConsts = new ResizeConstraint[specs.length];
        for (int i = 0; i < resConsts.length; i++) {
            resConsts[i] = specs[i].resize;
        }
        return resConsts;
    }

    private static ResizeConstraint[] getComponentResizeConstraints(ArrayList<CompWrap> compWraps, boolean isHor) {
        ResizeConstraint[] resConsts = new ResizeConstraint[compWraps.size()];
        for (int i = 0; i < resConsts.length; i++) {
            CC fc = compWraps.get(i).cc;
            resConsts[i] = fc.getDimConstraint(isHor).resize;
            int dock = fc.getDockSide();
            if (isHor) {
                if (!(dock == 0 || dock == 2)) {
                }
            } else if (!(dock == 1 || dock == 3)) {
            }
            ResizeConstraint dc = resConsts[i];
            resConsts[i] = new ResizeConstraint(dc.shrinkPrio, dc.shrink, dc.growPrio, ResizeConstraint.WEIGHT_100);
        }
        return resConsts;
    }

    private static boolean[] getComponentGapPush(ArrayList<CompWrap> compWraps, boolean isHor) {
        boolean[] barr = new boolean[(compWraps.size() + 1)];
        for (int i = 0; i < barr.length; i++) {
            boolean push = i > 0 && compWraps.get(i - 1).isPushGap(isHor, false);
            if (!push && i < barr.length - 1) {
                push = compWraps.get(i).isPushGap(isHor, true);
            }
            barr[i] = push;
        }
        return barr;
    }

    private int[][] getRowGaps(DimConstraint[] specs, int refSize, boolean isHor, boolean[] fillInPushGaps) {
        BoundSize wrapGapSize;
        BoundSize defGap = isHor ? this.lc.getGridGapX() : this.lc.getGridGapY();
        if (defGap == null) {
            defGap = isHor ? PlatformDefaults.getGridGapX() : PlatformDefaults.getGridGapY();
        }
        int[] defGapArr = defGap.getPixelSizes((float) refSize, this.container, null);
        boolean defIns = !hasDocks();
        UnitValue firstGap = LayoutUtil.getInsets(this.lc, isHor ? 1 : 0, defIns);
        UnitValue lastGap = LayoutUtil.getInsets(this.lc, isHor ? 3 : 2, defIns);
        int[][] retValues = new int[(specs.length + 1)][];
        int i = 0;
        int wgIx = 0;
        while (i < retValues.length) {
            DimConstraint specBefore = i > 0 ? specs[i - 1] : null;
            DimConstraint specAfter = i < specs.length ? specs[i] : null;
            boolean edgeBefore = specBefore == DOCK_DIM_CONSTRAINT || specBefore == null;
            boolean edgeAfter = specAfter == DOCK_DIM_CONSTRAINT || specAfter == null;
            if (!edgeBefore || !edgeAfter) {
                if (this.wrapGapMap == null || isHor == this.lc.isFlowX()) {
                    wrapGapSize = null;
                } else {
                    wrapGapSize = this.wrapGapMap.get(Integer.valueOf(wgIx));
                    wgIx++;
                }
                if (wrapGapSize == null) {
                    int[] gapBefore = specBefore != null ? specBefore.getRowGaps(this.container, null, refSize, false) : null;
                    int[] gapAfter = specAfter != null ? specAfter.getRowGaps(this.container, null, refSize, true) : null;
                    if (edgeBefore && gapAfter == null && firstGap != null) {
                        int bef = firstGap.getPixels((float) refSize, this.container, null);
                        retValues[i] = new int[]{bef, bef, bef};
                    } else if (!edgeAfter || gapBefore != null || firstGap == null) {
                        retValues[i] = gapAfter != gapBefore ? mergeSizes(gapAfter, gapBefore) : new int[]{defGapArr[0], defGapArr[1], defGapArr[2]};
                    } else {
                        int aft = lastGap.getPixels((float) refSize, this.container, null);
                        retValues[i] = new int[]{aft, aft, aft};
                    }
                    if ((specBefore != null && specBefore.isGapAfterPush()) || (specAfter != null && specAfter.isGapBeforePush())) {
                        fillInPushGaps[i] = true;
                    }
                } else {
                    if (wrapGapSize.isUnset()) {
                        retValues[i] = new int[]{defGapArr[0], defGapArr[1], defGapArr[2]};
                    } else {
                        retValues[i] = wrapGapSize.getPixelSizes((float) refSize, this.container, null);
                    }
                    fillInPushGaps[i] = wrapGapSize.getGapPush();
                }
            }
            i++;
        }
        return retValues;
    }

    private static int[][] getGaps(ArrayList<CompWrap> compWraps, boolean isHor) {
        int compCount = compWraps.size();
        int[][] retValues = new int[(compCount + 1)][];
        retValues[0] = compWraps.get(0).getGaps(isHor, true);
        int i = 0;
        while (i < compCount) {
            retValues[i + 1] = mergeSizes(compWraps.get(i).getGaps(isHor, false), i < compCount + -1 ? compWraps.get(i + 1).getGaps(isHor, true) : null);
            i++;
        }
        return retValues;
    }

    private boolean hasDocks() {
        return this.dockOffX > 0 || this.dockOffY > 0 || this.rowIndexes.last().intValue() > MAX_GRID || this.colIndexes.last().intValue() > MAX_GRID;
    }

    private void adjustMinPrefForSpanningComps(DimConstraint[] specs, Float[] defPush, FlowSizeSpec fss, ArrayList<LinkedDimGroup>[] groupsLists) {
        for (int r = groupsLists.length - 1; r >= 0; r--) {
            Iterator<LinkedDimGroup> it = groupsLists[r].iterator();
            while (it.hasNext()) {
                LinkedDimGroup group = it.next();
                if (group.span != 1) {
                    int[] sizes = group.getMinPrefMax();
                    for (int s = 0; s <= 1; s++) {
                        int cSize = sizes[s];
                        if (cSize != -2147471302) {
                            int rowSize = 0;
                            int sIx = (r << 1) + 1;
                            int len = Math.min(group.span << 1, fss.sizes.length - sIx) - 1;
                            for (int j = sIx; j < sIx + len; j++) {
                                int sz = fss.sizes[j][s];
                                if (sz != -2147471302) {
                                    rowSize += sz;
                                }
                            }
                            if (rowSize < cSize && len > 0) {
                                int newRowSize = 0;
                                for (int eagerness = 0; eagerness < 4 && newRowSize < cSize; eagerness++) {
                                    newRowSize = fss.expandSizes(specs, defPush, cSize, sIx, len, s, eagerness);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:100:0x01dd  */
    /* JADX WARNING: Removed duplicated region for block: B:114:0x01d1 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:117:0x01b5 A[EDGE_INSN: B:117:0x01b5->B:89:0x01b5 ?: BREAK  , SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:79:0x017e  */
    /* JADX WARNING: Removed duplicated region for block: B:83:0x0190  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x01b7  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.util.ArrayList<net.miginfocom.layout.Grid.LinkedDimGroup>[] divideIntoLinkedGroups(boolean r38) {
        /*
        // Method dump skipped, instructions count: 491
        */
        throw new UnsupportedOperationException("Method not decompiled: net.miginfocom.layout.Grid.divideIntoLinkedGroups(boolean):java.util.ArrayList[]");
    }

    private static int convertSpanToSparseGrid(int curIx, int span, TreeSet<Integer> indexes) {
        int lastIx = curIx + span;
        int retSpan = 1;
        Iterator<Integer> it = indexes.iterator();
        while (it.hasNext()) {
            Integer ix = it.next();
            if (ix.intValue() > curIx) {
                if (ix.intValue() >= lastIx) {
                    break;
                }
                retSpan++;
            }
        }
        return retSpan;
    }

    private boolean isCellFree(int r, int c, ArrayList<int[]> occupiedRects) {
        if (getCell(r, c) != null) {
            return false;
        }
        Iterator<int[]> it = occupiedRects.iterator();
        while (it.hasNext()) {
            int[] rect = it.next();
            if (rect[0] <= c && rect[1] <= r && rect[0] + rect[2] > c && rect[1] + rect[3] > r) {
                return false;
            }
        }
        return true;
    }

    private Cell getCell(int r, int c) {
        return this.grid.get(Integer.valueOf((r << 16) + (65535 & c)));
    }

    private void setCell(int r, int c, Cell cell) {
        if (c < 0 || r < 0) {
            throw new IllegalArgumentException("Cell position cannot be negative. row: " + r + ", col: " + c);
        } else if (c > MAX_GRID || r > MAX_GRID) {
            throw new IllegalArgumentException("Cell position out of bounds. Out of cells. row: " + r + ", col: " + c);
        } else {
            this.rowIndexes.add(Integer.valueOf(r));
            this.colIndexes.add(Integer.valueOf(c));
            this.grid.put(Integer.valueOf((r << 16) + (65535 & c)), cell);
        }
    }

    private void addDockingCell(int[] dockInsets, int side, CompWrap cw) {
        int c;
        int r;
        boolean z = true;
        int spanx = 1;
        int spany = 1;
        switch (side) {
            case 0:
            case 2:
                if (side == 0) {
                    r = dockInsets[0];
                    dockInsets[0] = r + 1;
                } else {
                    r = dockInsets[2];
                    dockInsets[2] = r - 1;
                }
                c = dockInsets[1];
                spanx = (dockInsets[3] - dockInsets[1]) + 1;
                this.colIndexes.add(Integer.valueOf(dockInsets[3]));
                break;
            case 1:
            case 3:
                if (side == 1) {
                    c = dockInsets[1];
                    dockInsets[1] = c + 1;
                } else {
                    c = dockInsets[3];
                    dockInsets[3] = c - 1;
                }
                r = dockInsets[0];
                spany = (dockInsets[2] - dockInsets[0]) + 1;
                this.rowIndexes.add(Integer.valueOf(dockInsets[2]));
                break;
            default:
                throw new IllegalArgumentException("Internal error 123.");
        }
        this.rowIndexes.add(Integer.valueOf(r));
        this.colIndexes.add(Integer.valueOf(c));
        LinkedHashMap<Integer, Cell> linkedHashMap = this.grid;
        Integer valueOf = Integer.valueOf((r << 16) + (65535 & c));
        if (spanx <= 1) {
            z = false;
        }
        linkedHashMap.put(valueOf, new Cell(cw, spanx, spany, z));
    }

    /* access modifiers changed from: private */
    public static class Cell {
        private final ArrayList<CompWrap> compWraps;
        private final boolean flowx;
        private boolean hasTagged;
        private final int spanx;
        private final int spany;

        private Cell(CompWrap cw) {
            this(cw, 1, 1, true);
        }

        private Cell(int spanx2, int spany2, boolean flowx2) {
            this((CompWrap) null, spanx2, spany2, flowx2);
        }

        private Cell(CompWrap cw, int spanx2, int spany2, boolean flowx2) {
            this.compWraps = new ArrayList<>(2);
            this.hasTagged = false;
            if (cw != null) {
                this.compWraps.add(cw);
            }
            this.spanx = spanx2;
            this.spany = spany2;
            this.flowx = flowx2;
        }
    }

    /* access modifiers changed from: private */
    public static class LinkedDimGroup {
        private static final int TYPE_BASELINE = 2;
        private static final int TYPE_PARALLEL = 1;
        private static final int TYPE_SERIAL = 0;
        private final ArrayList<CompWrap> _compWraps;
        private final boolean fromEnd;
        private final boolean isHor;
        private int lSize;
        private int lStart;
        private final String linkCtx;
        private final int linkType;
        private final int span;

        private LinkedDimGroup(String linkCtx2, int span2, int linkType2, boolean isHor2, boolean fromEnd2) {
            this._compWraps = new ArrayList<>(4);
            this.lStart = 0;
            this.lSize = 0;
            this.linkCtx = linkCtx2;
            this.span = span2;
            this.linkType = linkType2;
            this.isHor = isHor2;
            this.fromEnd = fromEnd2;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void addCompWrap(CompWrap cw) {
            this._compWraps.add(cw);
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void setCompWraps(ArrayList<CompWrap> cws) {
            if (this._compWraps != cws) {
                this._compWraps.clear();
                this._compWraps.addAll(cws);
            }
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void layout(DimConstraint dc, int start, int size, int spanCount) {
            this.lStart = start;
            this.lSize = size;
            if (!this._compWraps.isEmpty()) {
                ContainerWrapper parent = this._compWraps.get(0).comp.getParent();
                if (this.linkType == 1) {
                    Grid.layoutParallel(parent, this._compWraps, dc, start, size, this.isHor, this.fromEnd);
                } else if (this.linkType == 2) {
                    Grid.layoutBaseline(parent, this._compWraps, dc, start, size, 1, spanCount);
                } else {
                    Grid.layoutSerial(parent, this._compWraps, dc, start, size, this.isHor, spanCount, this.fromEnd);
                }
            }
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private int[] getMinPrefMax() {
            int[] sizes = new int[3];
            if (!this._compWraps.isEmpty()) {
                for (int sType = 0; sType <= 1; sType++) {
                    if (this.linkType == 1) {
                        sizes[sType] = Grid.getTotalSizeParallel(this._compWraps, sType, this.isHor);
                    } else if (this.linkType == 2) {
                        sizes[sType] = Grid.getBaselineAboveBelow(this._compWraps, sType, false).sum();
                    } else {
                        sizes[sType] = Grid.getTotalSizeSerial(this._compWraps, sType, this.isHor);
                    }
                }
                sizes[2] = 2097051;
            }
            return sizes;
        }
    }

    /* access modifiers changed from: private */
    public final class CompWrap {
        private final CC cc;
        private final ComponentWrapper comp;
        private final int eHideMode;
        private int forcedPushGaps;
        private int[][] gaps;
        private int h;
        private final int[] horSizes;
        private boolean isAbsolute;
        private boolean sizesOk;
        final /* synthetic */ Grid this$0;
        private final boolean useVisualPadding;
        private final int[] verSizes;
        private int w;
        private int x;
        private int y;

        private CompWrap(Grid grid, ComponentWrapper c, CC cc2, int eHideMode2, boolean useVisualPadding2) {
            boolean z = false;
            this.this$0 = grid;
            this.sizesOk = false;
            this.horSizes = new int[3];
            this.verSizes = new int[3];
            this.x = -2147471302;
            this.y = -2147471302;
            this.w = -2147471302;
            this.h = -2147471302;
            this.forcedPushGaps = 0;
            this.comp = c;
            this.cc = cc2;
            this.eHideMode = eHideMode2;
            this.useVisualPadding = useVisualPadding2;
            if (cc2.getHorizontal().getSize().isAbsolute() && cc2.getVertical().getSize().isAbsolute()) {
                z = true;
            }
            this.isAbsolute = z;
            if (eHideMode2 > 1) {
                this.gaps = new int[4][];
                for (int i = 0; i < this.gaps.length; i++) {
                    this.gaps[i] = new int[3];
                }
            }
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private int[] getSizes(boolean isHor) {
            validateSize();
            return isHor ? this.horSizes : this.verSizes;
        }

        private void validateSize() {
            BoundSize[] callbackSz = this.this$0.getCallbackSize(this.comp);
            if (!this.isAbsolute || !this.sizesOk || callbackSz != null) {
                if (this.eHideMode <= 0) {
                    int contentBias = this.comp.getContentBias();
                    int sizeHint = contentBias == -1 ? -1 : contentBias == 0 ? this.w != -2147471302 ? this.w : this.comp.getWidth() : this.h != -2147471302 ? this.h : this.comp.getHeight();
                    BoundSize hBS = (callbackSz == null || callbackSz[0] == null) ? this.cc.getHorizontal().getSize() : callbackSz[0];
                    BoundSize vBS = (callbackSz == null || callbackSz[1] == null) ? this.cc.getVertical().getSize() : callbackSz[1];
                    for (int i = 0; i <= 2; i++) {
                        switch (contentBias) {
                            case 0:
                                this.horSizes[i] = getSize(hBS, i, true, this.useVisualPadding, -1);
                                this.verSizes[i] = getSize(vBS, i, false, this.useVisualPadding, sizeHint > 0 ? sizeHint : this.horSizes[i]);
                                break;
                            case 1:
                                this.verSizes[i] = getSize(vBS, i, false, this.useVisualPadding, -1);
                                this.horSizes[i] = getSize(hBS, i, true, this.useVisualPadding, sizeHint > 0 ? sizeHint : this.verSizes[i]);
                                break;
                            default:
                                this.horSizes[i] = getSize(hBS, i, true, this.useVisualPadding, -1);
                                this.verSizes[i] = getSize(vBS, i, false, this.useVisualPadding, -1);
                                break;
                        }
                    }
                    Grid.correctMinMax(this.horSizes);
                    Grid.correctMinMax(this.verSizes);
                } else {
                    Arrays.fill(this.horSizes, 0);
                    Arrays.fill(this.verSizes, 0);
                }
                this.sizesOk = true;
            }
        }

        private int getSize(BoundSize uvs, int sizeType, boolean isHor, boolean useVP, int sizeHint) {
            int size;
            int[] visualPadding;
            float refValue;
            if (uvs == null || uvs.getSize(sizeType) == null) {
                switch (sizeType) {
                    case 0:
                        if (!isHor) {
                            size = this.comp.getMinimumHeight(sizeHint);
                            break;
                        } else {
                            size = this.comp.getMinimumWidth(sizeHint);
                            break;
                        }
                    case 1:
                        if (!isHor) {
                            size = this.comp.getPreferredHeight(sizeHint);
                            break;
                        } else {
                            size = this.comp.getPreferredWidth(sizeHint);
                            break;
                        }
                    default:
                        if (!isHor) {
                            size = this.comp.getMaximumHeight(sizeHint);
                            break;
                        } else {
                            size = this.comp.getMaximumWidth(sizeHint);
                            break;
                        }
                }
                if (!useVP || (visualPadding = this.comp.getVisualPadding()) == null || visualPadding.length <= 0) {
                    return size;
                }
                return size - (isHor ? visualPadding[1] + visualPadding[3] : visualPadding[0] + visualPadding[2]);
            }
            ContainerWrapper par = this.comp.getParent();
            if (isHor) {
                refValue = (float) par.getWidth();
            } else {
                refValue = (float) par.getHeight();
            }
            return uvs.getSize(sizeType).getPixels(refValue, par, this.comp);
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void calcGaps(ComponentWrapper before, CC befCC, ComponentWrapper after, CC aftCC, String tag, boolean flowX, boolean isLTR) {
            BoundSize befGap;
            BoundSize aftGap;
            ComponentWrapper componentWrapper;
            ComponentWrapper componentWrapper2;
            ContainerWrapper par = this.comp.getParent();
            int parW = par.getWidth();
            int parH = par.getHeight();
            if (before != null) {
                befGap = (flowX ? befCC.getHorizontal() : befCC.getVertical()).getGapAfter();
            } else {
                befGap = null;
            }
            if (after != null) {
                aftGap = (flowX ? aftCC.getHorizontal() : aftCC.getVertical()).getGapBefore();
            } else {
                aftGap = null;
            }
            DimConstraint vertical = this.cc.getVertical();
            ComponentWrapper componentWrapper3 = this.comp;
            if (flowX) {
                componentWrapper = null;
            } else {
                componentWrapper = before;
            }
            mergeGapSizes(vertical.getComponentGaps(par, componentWrapper3, befGap, componentWrapper, tag, parH, 0, isLTR), false, true);
            mergeGapSizes(this.cc.getHorizontal().getComponentGaps(par, this.comp, befGap, flowX ? before : null, tag, parW, 1, isLTR), true, true);
            DimConstraint vertical2 = this.cc.getVertical();
            ComponentWrapper componentWrapper4 = this.comp;
            if (flowX) {
                componentWrapper2 = null;
            } else {
                componentWrapper2 = after;
            }
            mergeGapSizes(vertical2.getComponentGaps(par, componentWrapper4, aftGap, componentWrapper2, tag, parH, 2, isLTR), false, false);
            mergeGapSizes(this.cc.getHorizontal().getComponentGaps(par, this.comp, aftGap, flowX ? after : null, tag, parW, 3, isLTR), true, false);
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void setDimBounds(int start, int size, boolean isHor) {
            if (isHor) {
                if (start != this.x || this.w != size) {
                    this.x = start;
                    this.w = size;
                    if (this.comp.getContentBias() == 0) {
                        invalidateSizes();
                    }
                }
            } else if (start != this.y || this.h != size) {
                this.y = start;
                this.h = size;
                if (this.comp.getContentBias() == 1) {
                    invalidateSizes();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void invalidateSizes() {
            this.sizesOk = false;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private boolean isPushGap(boolean isHor, boolean isBefore) {
            if (isHor) {
                if (((isBefore ? 1 : 2) & this.forcedPushGaps) != 0) {
                    return true;
                }
            }
            DimConstraint dc = this.cc.getDimConstraint(isHor);
            BoundSize s = isBefore ? dc.getGapBefore() : dc.getGapAfter();
            if (s == null || !s.getGapPush()) {
                return false;
            }
            return true;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void transferBounds(boolean addVisualPadding) {
            int[] visualPadding;
            if (!this.cc.isExternal()) {
                int compX = this.x;
                int compY = this.y;
                int compW = this.w;
                int compH = this.h;
                if (addVisualPadding && (visualPadding = this.comp.getVisualPadding()) != null) {
                    compX -= visualPadding[1];
                    compY -= visualPadding[0];
                    compW += visualPadding[1] + visualPadding[3];
                    compH += visualPadding[0] + visualPadding[2];
                }
                this.comp.setBounds(compX, compY, compW, compH);
            }
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void setForcedSizes(int[] sizes, boolean isHor) {
            if (sizes != null) {
                System.arraycopy(sizes, 0, getSizes(isHor), 0, 3);
                this.sizesOk = true;
            }
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void setGaps(int[] minPrefMax, int ix) {
            if (this.gaps == null) {
                this.gaps = new int[][]{null, null, null, null};
            }
            this.gaps[ix] = minPrefMax;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private void mergeGapSizes(int[] sizes, boolean isHor, boolean isTL) {
            if (this.gaps == null) {
                this.gaps = new int[][]{null, null, null, null};
            }
            if (sizes != null) {
                int gapIX = getGapIx(isHor, isTL);
                int[] oldGaps = this.gaps[gapIX];
                if (oldGaps == null) {
                    oldGaps = new int[]{0, 0, LayoutUtil.INF};
                    this.gaps[gapIX] = oldGaps;
                }
                oldGaps[0] = Math.max(sizes[0], oldGaps[0]);
                oldGaps[1] = Math.max(sizes[1], oldGaps[1]);
                oldGaps[2] = Math.min(sizes[2], oldGaps[2]);
            }
        }

        private int getGapIx(boolean isHor, boolean isTL) {
            return isHor ? isTL ? 1 : 3 : isTL ? 0 : 2;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private int getSizeInclGaps(int sizeType, boolean isHor) {
            return filter(sizeType, getGapBefore(sizeType, isHor) + getSize(sizeType, isHor) + getGapAfter(sizeType, isHor));
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private int getSize(int sizeType, boolean isHor) {
            return filter(sizeType, getSizes(isHor)[sizeType]);
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private int getGapBefore(int sizeType, boolean isHor) {
            int[] gaps2 = getGaps(isHor, true);
            if (gaps2 != null) {
                return filter(sizeType, gaps2[sizeType]);
            }
            return 0;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private int getGapAfter(int sizeType, boolean isHor) {
            int[] gaps2 = getGaps(isHor, false);
            if (gaps2 != null) {
                return filter(sizeType, gaps2[sizeType]);
            }
            return 0;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private int[] getGaps(boolean isHor, boolean isTL) {
            return this.gaps[getGapIx(isHor, isTL)];
        }

        private int filter(int sizeType, int size) {
            if (size != -2147471302) {
                return Grid.constrainSize(size);
            }
            if (sizeType != 2) {
                return 0;
            }
            return LayoutUtil.INF;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private boolean isBaselineAlign(boolean defValue) {
            Float g = this.cc.getVertical().getGrow();
            if (g != null && g.intValue() != 0) {
                return false;
            }
            UnitValue al = this.cc.getVertical().getAlign();
            if (al != null) {
                if (al != UnitValue.BASELINE_IDENTITY) {
                    return false;
                }
            } else if (!defValue) {
                return false;
            }
            if (this.comp.hasBaseline()) {
                return true;
            }
            return false;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private int getBaseline(int sizeType) {
            return this.comp.getBaseline(getSize(sizeType, true), getSize(sizeType, false));
        }

        /* access modifiers changed from: package-private */
        public void adjustMinHorSizeUp(int minSize) {
            int[] sz = getSizes(true);
            if (sz[0] < minSize) {
                sz[0] = minSize;
            }
            Grid.correctMinMax(sz);
        }
    }

    /* access modifiers changed from: private */
    public static void layoutBaseline(ContainerWrapper parent, ArrayList<CompWrap> compWraps, DimConstraint dc, int start, int size, int sizeType, int spanCount) {
        AboveBelow aboveBelow = getBaselineAboveBelow(compWraps, sizeType, true);
        int blRowSize = aboveBelow.sum();
        UnitValue align = compWraps.get(0).cc.getVertical().getAlign();
        if (spanCount == 1 && align == null) {
            align = dc.getAlignOrDefault(false);
        }
        if (align == UnitValue.BASELINE_IDENTITY) {
            align = UnitValue.CENTER;
        }
        int offset = start + aboveBelow.maxAbove + (align != null ? Math.max(0, align.getPixels((float) (size - blRowSize), parent, null)) : 0);
        Iterator<CompWrap> it = compWraps.iterator();
        while (it.hasNext()) {
            CompWrap cw = it.next();
            cw.y += offset;
            if (cw.y + cw.h > start + size) {
                cw.h = (start + size) - cw.y;
            }
        }
    }

    /* access modifiers changed from: private */
    public static void layoutSerial(ContainerWrapper parent, ArrayList<CompWrap> compWraps, DimConstraint dc, int start, int size, boolean isHor, int spanCount, boolean fromEnd) {
        FlowSizeSpec fss = mergeSizesGapsAndResConstrs(getComponentResizeConstraints(compWraps, isHor), getComponentGapPush(compWraps, isHor), getComponentSizes(compWraps, isHor), getGaps(compWraps, isHor));
        setCompWrapBounds(parent, LayoutUtil.calculateSerial(fss.sizes, fss.resConstsInclGaps, dc.isFill() ? GROW_100 : null, 1, size), compWraps, dc.getAlignOrDefault(isHor), start, size, isHor, fromEnd);
    }

    private static void setCompWrapBounds(ContainerWrapper parent, int[] allSizes, ArrayList<CompWrap> compWraps, UnitValue rowAlign, int start, int size, boolean isHor, boolean fromEnd) {
        int bIx;
        int totSize = LayoutUtil.sum(allSizes);
        UnitValue align = correctAlign(compWraps.get(0).cc, rowAlign, isHor, fromEnd);
        int cSt = start;
        int slack = size - totSize;
        if (slack > 0 && align != null) {
            int al = Math.min(slack, Math.max(0, align.getPixels((float) slack, parent, null)));
            if (fromEnd) {
                al = -al;
            }
            cSt += al;
        }
        int i = 0;
        int iSz = compWraps.size();
        int bIx2 = 0;
        while (i < iSz) {
            CompWrap cw = compWraps.get(i);
            if (fromEnd) {
                int bIx3 = bIx2 + 1;
                int cSt2 = cSt - allSizes[bIx2];
                cw.setDimBounds(cSt2 - allSizes[bIx3], allSizes[bIx3], isHor);
                bIx = bIx3 + 1;
                cSt = cSt2 - allSizes[bIx3];
            } else {
                int bIx4 = bIx2 + 1;
                int cSt3 = cSt + allSizes[bIx2];
                cw.setDimBounds(cSt3, allSizes[bIx4], isHor);
                bIx = bIx4 + 1;
                cSt = cSt3 + allSizes[bIx4];
            }
            i++;
            bIx2 = bIx;
        }
    }

    /* access modifiers changed from: private */
    public static void layoutParallel(ContainerWrapper parent, ArrayList<CompWrap> compWraps, DimConstraint dc, int start, int size, boolean isHor, boolean fromEnd) {
        int[][] sizes = new int[compWraps.size()][];
        for (int i = 0; i < sizes.length; i++) {
            CompWrap cw = compWraps.get(i);
            DimConstraint cDc = cw.cc.getDimConstraint(isHor);
            ResizeConstraint[] resConstr = new ResizeConstraint[3];
            resConstr[0] = cw.isPushGap(isHor, true) ? GAP_RC_CONST_PUSH : GAP_RC_CONST;
            resConstr[1] = cDc.resize;
            resConstr[2] = cw.isPushGap(isHor, false) ? GAP_RC_CONST_PUSH : GAP_RC_CONST;
            sizes[i] = LayoutUtil.calculateSerial(new int[][]{cw.getGaps(isHor, true), cw.getSizes(isHor), cw.getGaps(isHor, false)}, resConstr, dc.isFill() ? GROW_100 : null, 1, size);
        }
        setCompWrapBounds(parent, sizes, compWraps, dc.getAlignOrDefault(isHor), start, size, isHor, fromEnd);
    }

    private static void setCompWrapBounds(ContainerWrapper parent, int[][] sizes, ArrayList<CompWrap> compWraps, UnitValue rowAlign, int start, int size, boolean isHor, boolean fromEnd) {
        for (int i = 0; i < sizes.length; i++) {
            CompWrap cw = compWraps.get(i);
            UnitValue align = correctAlign(cw.cc, rowAlign, isHor, fromEnd);
            int[] cSizes = sizes[i];
            int gapBef = cSizes[0];
            int cSize = cSizes[1];
            int gapAft = cSizes[2];
            int cSt = fromEnd ? start - gapBef : start + gapBef;
            int slack = ((size - cSize) - gapBef) - gapAft;
            if (slack > 0 && align != null) {
                int al = Math.min(slack, Math.max(0, align.getPixels((float) slack, parent, null)));
                if (fromEnd) {
                    al = -al;
                }
                cSt += al;
            }
            if (fromEnd) {
                cSt -= cSize;
            }
            cw.setDimBounds(cSt, cSize, isHor);
        }
    }

    private static UnitValue correctAlign(CC cc, UnitValue rowAlign, boolean isHor, boolean fromEnd) {
        UnitValue align = (isHor ? cc.getHorizontal() : cc.getVertical()).getAlign();
        if (align == null) {
            align = rowAlign;
        }
        if (align == UnitValue.BASELINE_IDENTITY) {
            align = UnitValue.CENTER;
        }
        if (!fromEnd) {
            return align;
        }
        if (align == UnitValue.LEFT) {
            return UnitValue.RIGHT;
        }
        if (align == UnitValue.RIGHT) {
            return UnitValue.LEFT;
        }
        return align;
    }

    /* access modifiers changed from: private */
    public static class AboveBelow {
        int maxAbove;
        int maxBelow;

        AboveBelow(int maxAbove2, int maxBelow2) {
            this.maxAbove = maxAbove2;
            this.maxBelow = maxBelow2;
        }

        /* access modifiers changed from: package-private */
        public int sum() {
            return this.maxAbove + this.maxBelow;
        }
    }

    /* access modifiers changed from: private */
    public static AboveBelow getBaselineAboveBelow(ArrayList<CompWrap> compWraps, int sType, boolean centerBaseline) {
        int maxAbove = Integer.MIN_VALUE;
        int maxBelow = Integer.MIN_VALUE;
        Iterator<CompWrap> it = compWraps.iterator();
        while (it.hasNext()) {
            CompWrap cw = it.next();
            int height2 = cw.getSize(sType, false);
            if (height2 >= 2097051) {
                return new AboveBelow(1048525, 1048525);
            }
            int baseline = cw.getBaseline(sType);
            maxAbove = Math.max(baseline + cw.getGapBefore(sType, false), maxAbove);
            maxBelow = Math.max((height2 - baseline) + cw.getGapAfter(sType, false), maxBelow);
            if (centerBaseline) {
                cw.setDimBounds(-baseline, height2, false);
            }
        }
        return new AboveBelow(maxAbove, maxBelow);
    }

    /* access modifiers changed from: private */
    public static int getTotalSizeParallel(ArrayList<CompWrap> compWraps, int sType, boolean isHor) {
        int size = sType == 2 ? 2097051 : 0;
        Iterator<CompWrap> it = compWraps.iterator();
        while (it.hasNext()) {
            int cwSize = it.next().getSizeInclGaps(sType, isHor);
            if (cwSize >= 2097051) {
                return LayoutUtil.INF;
            }
            if (sType == 2) {
                if (cwSize >= size) {
                }
            } else if (cwSize <= size) {
            }
            size = cwSize;
        }
        return constrainSize(size);
    }

    /* access modifiers changed from: private */
    public static int getTotalSizeSerial(ArrayList<CompWrap> compWraps, int sType, boolean isHor) {
        int totSize = 0;
        int iSz = compWraps.size();
        int lastGapAfter = 0;
        for (int i = 0; i < iSz; i++) {
            CompWrap wrap = compWraps.get(i);
            int gapBef = wrap.getGapBefore(sType, isHor);
            if (gapBef > lastGapAfter) {
                totSize += gapBef - lastGapAfter;
            }
            lastGapAfter = wrap.getGapAfter(sType, isHor);
            totSize = totSize + wrap.getSize(sType, isHor) + lastGapAfter;
            if (totSize >= 2097051) {
                return LayoutUtil.INF;
            }
        }
        return constrainSize(totSize);
    }

    private static int getTotalGroupsSizeParallel(ArrayList<LinkedDimGroup> groups, int sType, boolean countSpanning) {
        int size = sType == 2 ? 2097051 : 0;
        Iterator<LinkedDimGroup> it = groups.iterator();
        while (it.hasNext()) {
            LinkedDimGroup group = it.next();
            if (countSpanning || group.span == 1) {
                int grpSize = group.getMinPrefMax()[sType];
                if (grpSize >= 2097051) {
                    return LayoutUtil.INF;
                }
                if (sType == 2) {
                    if (grpSize >= size) {
                    }
                } else if (grpSize <= size) {
                }
                size = grpSize;
            }
        }
        return constrainSize(size);
    }

    private static int[][] getComponentSizes(ArrayList<CompWrap> compWraps, boolean isHor) {
        int[][] compSizes = new int[compWraps.size()][];
        for (int i = 0; i < compSizes.length; i++) {
            compSizes[i] = compWraps.get(i).getSizes(isHor);
        }
        return compSizes;
    }

    private static FlowSizeSpec mergeSizesGapsAndResConstrs(ResizeConstraint[] resConstr, boolean[] gapPush, int[][] minPrefMaxSizes, int[][] gapSizes) {
        ResizeConstraint resizeConstraint;
        int[][] sizes = new int[((minPrefMaxSizes.length << 1) + 1)][];
        ResizeConstraint[] resConstsInclGaps = new ResizeConstraint[sizes.length];
        sizes[0] = gapSizes[0];
        int i = 0;
        int crIx = 1;
        while (i < minPrefMaxSizes.length) {
            resConstsInclGaps[crIx] = resConstr[i];
            sizes[crIx] = minPrefMaxSizes[i];
            sizes[crIx + 1] = gapSizes[i + 1];
            if (sizes[crIx - 1] != null) {
                resConstsInclGaps[crIx - 1] = gapPush[i < gapPush.length ? i : gapPush.length + -1] ? GAP_RC_CONST_PUSH : GAP_RC_CONST;
            }
            if (i == minPrefMaxSizes.length - 1 && sizes[crIx + 1] != null) {
                int i2 = crIx + 1;
                if (gapPush[i + 1 < gapPush.length ? i + 1 : gapPush.length - 1]) {
                    resizeConstraint = GAP_RC_CONST_PUSH;
                } else {
                    resizeConstraint = GAP_RC_CONST;
                }
                resConstsInclGaps[i2] = resizeConstraint;
            }
            i++;
            crIx += 2;
        }
        for (int i3 = 0; i3 < sizes.length; i3++) {
            if (sizes[i3] == null) {
                sizes[i3] = new int[3];
            }
        }
        return new FlowSizeSpec(sizes, resConstsInclGaps);
    }

    private static int[] mergeSizes(int[] oldValues, int[] newValues) {
        if (oldValues == null) {
            return newValues;
        }
        if (newValues == null) {
            return oldValues;
        }
        int[] ret = new int[oldValues.length];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = mergeSizes(oldValues[i], newValues[i], true);
        }
        return ret;
    }

    private static int mergeSizes(int oldValue, int newValue, boolean toMax) {
        if (oldValue == -2147471302 || oldValue == newValue) {
            return newValue;
        }
        if (newValue == -2147471302) {
            return oldValue;
        }
        return toMax == (oldValue > newValue) ? oldValue : newValue;
    }

    /* access modifiers changed from: private */
    public static int constrainSize(int s) {
        if (s > 0) {
            return s < 2097051 ? s : LayoutUtil.INF;
        }
        return 0;
    }

    /* access modifiers changed from: private */
    public static void correctMinMax(int[] s) {
        if (s[0] > s[2]) {
            s[0] = s[2];
        }
        if (s[1] < s[0]) {
            s[1] = s[0];
        }
        if (s[1] > s[2]) {
            s[1] = s[2];
        }
    }

    /* access modifiers changed from: private */
    public static final class FlowSizeSpec {
        private final ResizeConstraint[] resConstsInclGaps;
        private final int[][] sizes;

        private FlowSizeSpec(int[][] sizes2, ResizeConstraint[] resConstsInclGaps2) {
            this.sizes = sizes2;
            this.resConstsInclGaps = resConstsInclGaps2;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private int expandSizes(DimConstraint[] specs, Float[] defGrow, int targetSize, int fromIx, int len, int sizeType, int eagerness) {
            ResizeConstraint[] resConstr = new ResizeConstraint[len];
            int[][] sizesToExpand = new int[len][];
            for (int i = 0; i < len; i++) {
                int[] minPrefMax = this.sizes[i + fromIx];
                sizesToExpand[i] = new int[]{minPrefMax[sizeType], minPrefMax[1], minPrefMax[2]};
                if (eagerness <= 1 && i % 2 == 0) {
                    BoundSize sz = ((DimConstraint) LayoutUtil.getIndexSafe(specs, ((i + fromIx) - 1) >> 1)).getSize();
                    if (sizeType == 0) {
                        if (!(sz.getMin() == null || sz.getMin().getUnit() == 13)) {
                        }
                    }
                    if (!(sizeType != 1 || sz.getPreferred() == null || sz.getPreferred().getUnit() == 14)) {
                    }
                }
                resConstr[i] = (ResizeConstraint) LayoutUtil.getIndexSafe(this.resConstsInclGaps, i + fromIx);
            }
            int[] newSizes = LayoutUtil.calculateSerial(sizesToExpand, resConstr, (eagerness == 1 || eagerness == 3) ? Grid.extractSubArray(specs, defGrow, fromIx, len) : null, 1, targetSize);
            int newSize = 0;
            for (int i2 = 0; i2 < len; i2++) {
                int s = newSizes[i2];
                this.sizes[i2 + fromIx][sizeType] = s;
                newSize += s;
            }
            return newSize;
        }
    }

    /* access modifiers changed from: private */
    public static Float[] extractSubArray(DimConstraint[] specs, Float[] arr, int ix, int len) {
        if (arr == null || arr.length < ix + len) {
            Float[] growLastArr = new Float[len];
            for (int i = (ix + len) - 1; i >= 0; i -= 2) {
                if (specs[i >> 1] != DOCK_DIM_CONSTRAINT) {
                    growLastArr[i - ix] = ResizeConstraint.WEIGHT_100;
                    return growLastArr;
                }
            }
            return growLastArr;
        }
        Float[] newArr = new Float[len];
        System.arraycopy(arr, ix, newArr, 0, len);
        return newArr;
    }

    private static synchronized void putSizesAndIndexes(Object parComp, int[] sizes, int[] ixArr, boolean isRows) {
        char c = 0;
        synchronized (Grid.class) {
            if (PARENT_ROWCOL_SIZES_MAP == null) {
                PARENT_ROWCOL_SIZES_MAP = new WeakHashMap[]{new WeakHashMap<>(4), new WeakHashMap<>(4)};
            }
            WeakHashMap<Object, int[][]>[] weakHashMapArr = PARENT_ROWCOL_SIZES_MAP;
            if (!isRows) {
                c = 1;
            }
            weakHashMapArr[c].put(parComp, new int[][]{ixArr, sizes});
        }
    }

    static synchronized int[][] getSizesAndIndexes(Object parComp, boolean isRows) {
        int[][] iArr;
        synchronized (Grid.class) {
            if (PARENT_ROWCOL_SIZES_MAP == null) {
                iArr = null;
            } else {
                iArr = PARENT_ROWCOL_SIZES_MAP[isRows ? (char) 0 : 1].get(parComp);
            }
        }
        return iArr;
    }

    private static synchronized void saveGrid(ComponentWrapper parComp, LinkedHashMap<Integer, Cell> grid2) {
        synchronized (Grid.class) {
            if (PARENT_GRIDPOS_MAP == null) {
                PARENT_GRIDPOS_MAP = new WeakHashMap<>(4);
            }
            ArrayList<WeakCell> weakCells = new ArrayList<>(grid2.size());
            for (Map.Entry<Integer, Cell> e : grid2.entrySet()) {
                Cell cell = e.getValue();
                Integer xyInt = e.getKey();
                if (xyInt != null) {
                    int x = (xyInt.intValue() << 16) >> 16;
                    int y = xyInt.intValue() >> 16;
                    Iterator it = cell.compWraps.iterator();
                    while (it.hasNext()) {
                        weakCells.add(new WeakCell(((CompWrap) it.next()).comp.getComponent(), x, y, cell.spanx, cell.spany));
                    }
                }
            }
            PARENT_GRIDPOS_MAP.put(parComp.getComponent(), weakCells);
        }
    }

    static synchronized HashMap<Object, int[]> getGridPositions(Object parComp) {
        ArrayList<WeakCell> weakCells;
        HashMap<Object, int[]> retMap = null;
        synchronized (Grid.class) {
            if (PARENT_GRIDPOS_MAP != null) {
                weakCells = PARENT_GRIDPOS_MAP.get(parComp);
            } else {
                weakCells = null;
            }
            if (weakCells != null) {
                retMap = new HashMap<>();
                Iterator<WeakCell> it = weakCells.iterator();
                while (it.hasNext()) {
                    WeakCell wc = it.next();
                    Object component = wc.componentRef.get();
                    if (component != null) {
                        retMap.put(component, new int[]{wc.x, wc.y, wc.spanX, wc.spanY});
                    }
                }
            }
        }
        return retMap;
    }

    /* access modifiers changed from: private */
    public static class WeakCell {
        private final WeakReference<Object> componentRef;
        private final int spanX;
        private final int spanY;
        private final int x;
        private final int y;

        private WeakCell(Object component, int x2, int y2, int spanX2, int spanY2) {
            this.componentRef = new WeakReference<>(component);
            this.x = x2;
            this.y = y2;
            this.spanX = spanX2;
            this.spanY = spanY2;
        }
    }
}
