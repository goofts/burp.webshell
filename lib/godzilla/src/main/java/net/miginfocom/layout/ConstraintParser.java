package net.miginfocom.layout;

import com.formdev.flatlaf.FlatClientProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javassist.bytecode.Opcode;

public final class ConstraintParser {
    private ConstraintParser() {
    }

    public static LC parseLayoutConstraint(String s) {
        UnitValue alignX;
        int ix;
        int ix2;
        int i;
        int len;
        LC lc = new LC();
        if (!s.isEmpty()) {
            String[] parts = toTrimmedTokens(s, ',');
            for (int i2 = 0; i2 < parts.length; i2++) {
                String part = parts[i2];
                if (part != null && ((len = part.length()) == 3 || len == 11)) {
                    if (part.equals("ltr") || part.equals("rtl") || part.equals("lefttoright") || part.equals("righttoleft")) {
                        lc.setLeftToRight(part.charAt(0) == 'l' ? Boolean.TRUE : Boolean.FALSE);
                        parts[i2] = null;
                    }
                    if (part.equals("ttb") || part.equals("btt") || part.equals("toptobottom") || part.equals("bottomtotop")) {
                        lc.setTopToBottom(part.charAt(0) == 't');
                        parts[i2] = null;
                    }
                }
            }
            int length = parts.length;
            for (int i3 = 0; i3 < length; i3++) {
                String part2 = parts[i3];
                if (!(part2 == null || part2.length() == 0)) {
                    try {
                        char c = part2.charAt(0);
                        if (c == 'w' || c == 'h') {
                            int ix3 = startsWithLenient(part2, "wrap", -1, true);
                            if (ix3 > -1) {
                                String num = part2.substring(ix3).trim();
                                if (num.length() != 0) {
                                    i = Integer.parseInt(num);
                                } else {
                                    i = 0;
                                }
                                lc.setWrapAfter(i);
                            } else {
                                boolean isHor = c == 'w';
                                if (isHor && (part2.startsWith("w ") || part2.startsWith("width "))) {
                                    lc.setWidth(parseBoundSize(part2.substring(part2.charAt(1) == ' ' ? 2 : 6).trim(), false, true));
                                } else if (isHor || (!part2.startsWith("h ") && !part2.startsWith("height "))) {
                                    if (part2.length() > 5) {
                                        String sz = part2.substring(5).trim();
                                        if (part2.startsWith("wmin ")) {
                                            lc.minWidth(sz);
                                        } else if (part2.startsWith("wmax ")) {
                                            lc.maxWidth(sz);
                                        } else if (part2.startsWith("hmin ")) {
                                            lc.minHeight(sz);
                                        } else if (part2.startsWith("hmax ")) {
                                            lc.maxHeight(sz);
                                        }
                                    }
                                    if (part2.startsWith("hidemode ")) {
                                        lc.setHideMode(Integer.parseInt(part2.substring(9)));
                                    }
                                } else {
                                    lc.setHeight(parseBoundSize(part2.substring(part2.charAt(1) == ' ' ? 2 : 7).trim(), false, false));
                                }
                            }
                        }
                        if (c == 'g') {
                            if (part2.startsWith("gapx ")) {
                                lc.setGridGapX(parseBoundSize(part2.substring(5).trim(), true, true));
                            } else if (part2.startsWith("gapy ")) {
                                lc.setGridGapY(parseBoundSize(part2.substring(5).trim(), true, false));
                            } else if (part2.startsWith("gap ")) {
                                String[] gaps = toTrimmedTokens(part2.substring(4).trim(), ' ');
                                lc.setGridGapX(parseBoundSize(gaps[0], true, true));
                                lc.setGridGapY(gaps.length > 1 ? parseBoundSize(gaps[1], true, false) : lc.getGridGapX());
                            }
                        }
                        if (c != 'd' || (ix2 = startsWithLenient(part2, "debug", 5, true)) <= -1) {
                            if (c == 'n') {
                                if (part2.equals("nogrid")) {
                                    lc.setNoGrid(true);
                                } else if (part2.equals("nocache")) {
                                    lc.setNoCache(true);
                                } else if (part2.equals("novisualpadding")) {
                                    lc.setVisualPadding(false);
                                }
                            }
                            if (c == 'f') {
                                if (part2.equals(FlatClientProperties.TABBED_PANE_ALIGN_FILL) || part2.equals("fillx") || part2.equals("filly")) {
                                    lc.setFillX(part2.length() == 4 || part2.charAt(4) == 'x');
                                    lc.setFillY(part2.length() == 4 || part2.charAt(4) == 'y');
                                } else if (part2.equals("flowy")) {
                                    lc.setFlowX(false);
                                } else if (part2.equals("flowx")) {
                                    lc.setFlowX(true);
                                }
                            }
                            if (c != 'i' || (ix = startsWithLenient(part2, "insets", 3, true)) <= -1) {
                                if (c == 'a') {
                                    int ix4 = startsWithLenient(part2, new String[]{"aligny", "ay"}, new int[]{6, 2}, true);
                                    if (ix4 > -1) {
                                        UnitValue align = parseUnitValueOrAlign(part2.substring(ix4).trim(), false, null);
                                        if (align == UnitValue.BASELINE_IDENTITY) {
                                            throw new IllegalArgumentException("'baseline' can not be used to align the whole component group.");
                                        }
                                        lc.setAlignY(align);
                                    } else {
                                        int ix5 = startsWithLenient(part2, new String[]{"alignx", "ax"}, new int[]{6, 2}, true);
                                        if (ix5 > -1) {
                                            lc.setAlignX(parseUnitValueOrAlign(part2.substring(ix5).trim(), true, null));
                                        } else {
                                            int ix6 = startsWithLenient(part2, "align", 2, true);
                                            if (ix6 > -1) {
                                                String[] gaps2 = toTrimmedTokens(part2.substring(ix6).trim(), ' ');
                                                lc.setAlignX(parseUnitValueOrAlign(gaps2[0], true, null));
                                                if (gaps2.length <= 1) {
                                                    continue;
                                                } else {
                                                    UnitValue align2 = parseUnitValueOrAlign(gaps2[1], false, null);
                                                    if (align2 == UnitValue.BASELINE_IDENTITY) {
                                                        throw new IllegalArgumentException("'baseline' can not be used to align the whole component group.");
                                                    }
                                                    lc.setAlignY(align2);
                                                }
                                            }
                                        }
                                    }
                                }
                                if (c == 'p') {
                                    if (part2.startsWith("packalign ")) {
                                        String[] packs = toTrimmedTokens(part2.substring(10).trim(), ' ');
                                        lc.setPackWidthAlign(packs[0].length() > 0 ? Float.parseFloat(packs[0]) : 0.5f);
                                        if (packs.length > 1) {
                                            lc.setPackHeightAlign(Float.parseFloat(packs[1]));
                                        }
                                    } else if (part2.startsWith("pack ") || part2.equals("pack")) {
                                        String ps = part2.substring(4).trim();
                                        if (ps.length() <= 0) {
                                            ps = "pref pref";
                                        }
                                        String[] packs2 = toTrimmedTokens(ps, ' ');
                                        lc.setPackWidth(parseBoundSize(packs2[0], false, true));
                                        if (packs2.length > 1) {
                                            lc.setPackHeight(parseBoundSize(packs2[1], false, false));
                                        }
                                    }
                                }
                                if (lc.getAlignX() != null || (alignX = parseAlignKeywords(part2, true)) == null) {
                                    UnitValue alignY = parseAlignKeywords(part2, false);
                                    if (alignY != null) {
                                        lc.setAlignY(alignY);
                                    } else {
                                        throw new IllegalArgumentException("Unknown Constraint: '" + part2 + "'\n");
                                    }
                                } else {
                                    lc.setAlignX(alignX);
                                }
                            } else {
                                String insStr = part2.substring(ix).trim();
                                UnitValue[] ins = parseInsets(insStr, true);
                                LayoutUtil.putCCString(ins, insStr);
                                lc.setInsets(ins);
                            }
                        } else {
                            String millis = part2.substring(ix2).trim();
                            lc.setDebugMillis(millis.length() > 0 ? Integer.parseInt(millis) : 1000);
                        }
                    } catch (Exception ex) {
                        throw new IllegalArgumentException("Illegal Constraint: '" + part2 + "'\n" + ex.getMessage());
                    }
                }
            }
        }
        return lc;
    }

    public static AC parseRowConstraints(String s) {
        return parseAxisConstraint(s, false);
    }

    public static AC parseColumnConstraints(String s) {
        return parseAxisConstraint(s, true);
    }

    private static AC parseAxisConstraint(String s, boolean isCols) {
        String s2 = s.trim();
        if (s2.length() == 0) {
            return new AC();
        }
        ArrayList<String> parts = getRowColAndGapsTrimmed(s2.toLowerCase());
        BoundSize[] gaps = new BoundSize[((parts.size() >> 1) + 1)];
        int i = 0;
        int iSz = parts.size();
        int gIx = 0;
        while (i < iSz) {
            gaps[gIx] = parseBoundSize(parts.get(i), true, isCols);
            i += 2;
            gIx++;
        }
        DimConstraint[] colSpecs = new DimConstraint[(parts.size() >> 1)];
        int i2 = 0;
        int gIx2 = 0;
        while (i2 < colSpecs.length) {
            if (gIx2 >= gaps.length - 1) {
                gIx2 = gaps.length - 2;
            }
            colSpecs[i2] = parseDimConstraint(parts.get((i2 << 1) + 1), gaps[gIx2], gaps[gIx2 + 1], isCols);
            i2++;
            gIx2++;
        }
        AC ac = new AC();
        ac.setConstaints(colSpecs);
        return ac;
    }

    private static DimConstraint parseDimConstraint(String s, BoundSize gapBefore, BoundSize gapAfter, boolean isCols) {
        int ix;
        DimConstraint dimConstraint = new DimConstraint();
        dimConstraint.setGapBefore(gapBefore);
        dimConstraint.setGapAfter(gapAfter);
        String[] parts = toTrimmedTokens(s, ',');
        for (String part : parts) {
            try {
                if (part.length() != 0) {
                    if (part.equals(FlatClientProperties.TABBED_PANE_ALIGN_FILL)) {
                        dimConstraint.setFill(true);
                    } else if (part.equals("nogrid")) {
                        dimConstraint.setNoGrid(true);
                    } else {
                        char c = part.charAt(0);
                        if (c == 's') {
                            int ix2 = startsWithLenient(part, new String[]{"sizegroup", "sg"}, new int[]{5, 2}, true);
                            if (ix2 > -1) {
                                dimConstraint.setSizeGroup(part.substring(ix2).trim());
                            } else {
                                int ix3 = startsWithLenient(part, new String[]{"shrinkprio", "shp"}, new int[]{10, 3}, true);
                                if (ix3 > -1) {
                                    dimConstraint.setShrinkPriority(Integer.parseInt(part.substring(ix3).trim()));
                                } else {
                                    int ix4 = startsWithLenient(part, "shrink", 6, true);
                                    if (ix4 > -1) {
                                        dimConstraint.setShrink(parseFloat(part.substring(ix4).trim(), ResizeConstraint.WEIGHT_100));
                                    }
                                }
                            }
                        }
                        if (c == 'g') {
                            int ix5 = startsWithLenient(part, new String[]{"growpriority", "gp"}, new int[]{5, 2}, true);
                            if (ix5 > -1) {
                                dimConstraint.setGrowPriority(Integer.parseInt(part.substring(ix5).trim()));
                            } else {
                                int ix6 = startsWithLenient(part, "grow", 4, true);
                                if (ix6 > -1) {
                                    dimConstraint.setGrow(parseFloat(part.substring(ix6).trim(), ResizeConstraint.WEIGHT_100));
                                }
                            }
                        }
                        if (c != 'a' || (ix = startsWithLenient(part, "align", 2, true)) <= -1) {
                            UnitValue align = parseAlignKeywords(part, isCols);
                            if (align != null) {
                                dimConstraint.setAlign(align);
                            } else {
                                dimConstraint.setSize(parseBoundSize(part, false, isCols));
                            }
                        } else {
                            dimConstraint.setAlign(parseUnitValueOrAlign(part.substring(ix).trim(), isCols, null));
                        }
                    }
                }
            } catch (Exception ex) {
                throw new IllegalArgumentException("Illegal constraint: '" + part + "'\n" + ex.getMessage());
            }
        }
        return dimConstraint;
    }

    public static Map<ComponentWrapper, CC> parseComponentConstraints(Map<ComponentWrapper, String> constrMap) {
        HashMap<ComponentWrapper, CC> flowConstrMap = new HashMap<>();
        for (Map.Entry<ComponentWrapper, String> entry : constrMap.entrySet()) {
            flowConstrMap.put(entry.getKey(), parseComponentConstraint(entry.getValue()));
        }
        return flowConstrMap;
    }

    public static CC parseComponentConstraint(String s) {
        int ix;
        UnitValue min;
        int ix2;
        int ix3;
        char c2;
        CC cc = new CC();
        if (s != null && !s.isEmpty()) {
            String[] parts = toTrimmedTokens(s, ',');
            int length = parts.length;
            for (int i = 0; i < length; i++) {
                String part = parts[i];
                try {
                    if (part.length() != 0) {
                        char c = part.charAt(0);
                        if (c == 'n') {
                            if (part.equals("north")) {
                                cc.setDockSide(0);
                            } else if (part.equals("newline")) {
                                cc.setNewline(true);
                            } else if (part.startsWith("newline ")) {
                                cc.setNewlineGapSize(parseBoundSize(part.substring(7).trim(), true, true));
                            }
                        }
                        if (c != 'f' || (!part.equals("flowy") && !part.equals("flowx"))) {
                            if (c == 's') {
                                int ix4 = startsWithLenient(part, "skip", 4, true);
                                if (ix4 > -1) {
                                    String num = part.substring(ix4).trim();
                                    cc.setSkip(num.length() != 0 ? Integer.parseInt(num) : 1);
                                } else {
                                    int ix5 = startsWithLenient(part, "split", 5, true);
                                    if (ix5 > -1) {
                                        String split = part.substring(ix5).trim();
                                        cc.setSplit(split.length() > 0 ? Integer.parseInt(split) : LayoutUtil.INF);
                                    } else if (part.equals("south")) {
                                        cc.setDockSide(2);
                                    } else {
                                        int ix6 = startsWithLenient(part, new String[]{"spany", "sy"}, new int[]{5, 2}, true);
                                        if (ix6 > -1) {
                                            cc.setSpanY(parseSpan(part.substring(ix6).trim()));
                                        } else {
                                            int ix7 = startsWithLenient(part, new String[]{"spanx", "sx"}, new int[]{5, 2}, true);
                                            if (ix7 > -1) {
                                                cc.setSpanX(parseSpan(part.substring(ix7).trim()));
                                            } else {
                                                int ix8 = startsWithLenient(part, "span", 4, true);
                                                if (ix8 > -1) {
                                                    String[] spans = toTrimmedTokens(part.substring(ix8).trim(), ' ');
                                                    cc.setSpanX(spans[0].length() > 0 ? Integer.parseInt(spans[0]) : LayoutUtil.INF);
                                                    cc.setSpanY(spans.length > 1 ? Integer.parseInt(spans[1]) : 1);
                                                } else {
                                                    int ix9 = startsWithLenient(part, "shrinkx", 7, true);
                                                    if (ix9 > -1) {
                                                        cc.getHorizontal().setShrink(parseFloat(part.substring(ix9).trim(), ResizeConstraint.WEIGHT_100));
                                                    } else {
                                                        int ix10 = startsWithLenient(part, "shrinky", 7, true);
                                                        if (ix10 > -1) {
                                                            cc.getVertical().setShrink(parseFloat(part.substring(ix10).trim(), ResizeConstraint.WEIGHT_100));
                                                        } else {
                                                            int ix11 = startsWithLenient(part, "shrink", 6, false);
                                                            if (ix11 > -1) {
                                                                String[] shrinks = toTrimmedTokens(part.substring(ix11).trim(), ' ');
                                                                cc.getHorizontal().setShrink(parseFloat(shrinks[0], ResizeConstraint.WEIGHT_100));
                                                                if (shrinks.length > 1) {
                                                                    cc.getVertical().setShrink(parseFloat(shrinks[1], ResizeConstraint.WEIGHT_100));
                                                                }
                                                            } else {
                                                                int ix12 = startsWithLenient(part, new String[]{"shrinkprio", "shp"}, new int[]{10, 3}, true);
                                                                if (ix12 > -1) {
                                                                    String sp = part.substring(ix12).trim();
                                                                    if (sp.startsWith("x") || sp.startsWith("y")) {
                                                                        (sp.startsWith("x") ? cc.getHorizontal() : cc.getVertical()).setShrinkPriority(Integer.parseInt(sp.substring(2)));
                                                                    } else {
                                                                        String[] shrinks2 = toTrimmedTokens(sp, ' ');
                                                                        cc.getHorizontal().setShrinkPriority(Integer.parseInt(shrinks2[0]));
                                                                        if (shrinks2.length > 1) {
                                                                            cc.getVertical().setShrinkPriority(Integer.parseInt(shrinks2[1]));
                                                                        }
                                                                    }
                                                                } else {
                                                                    int ix13 = startsWithLenient(part, new String[]{"sizegroupx", "sizegroupy", "sgx", "sgy"}, new int[]{9, 9, 2, 2}, true);
                                                                    if (ix13 > -1) {
                                                                        String sg = part.substring(ix13).trim();
                                                                        char lc = part.charAt(ix13 - 1);
                                                                        if (lc != 'y') {
                                                                            cc.getHorizontal().setSizeGroup(sg);
                                                                        }
                                                                        if (lc != 'x') {
                                                                            cc.getVertical().setSizeGroup(sg);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (c == 'g') {
                                int ix14 = startsWithLenient(part, "growx", 5, true);
                                if (ix14 > -1) {
                                    cc.getHorizontal().setGrow(parseFloat(part.substring(ix14).trim(), ResizeConstraint.WEIGHT_100));
                                } else {
                                    int ix15 = startsWithLenient(part, "growy", 5, true);
                                    if (ix15 > -1) {
                                        cc.getVertical().setGrow(parseFloat(part.substring(ix15).trim(), ResizeConstraint.WEIGHT_100));
                                    } else {
                                        int ix16 = startsWithLenient(part, "grow", 4, false);
                                        if (ix16 > -1) {
                                            String[] grows = toTrimmedTokens(part.substring(ix16).trim(), ' ');
                                            cc.getHorizontal().setGrow(parseFloat(grows[0], ResizeConstraint.WEIGHT_100));
                                            cc.getVertical().setGrow(parseFloat(grows.length > 1 ? grows[1] : "", ResizeConstraint.WEIGHT_100));
                                        } else {
                                            int ix17 = startsWithLenient(part, new String[]{"growprio", "gp"}, new int[]{8, 2}, true);
                                            if (ix17 > -1) {
                                                String gp = part.substring(ix17).trim();
                                                char c0 = gp.length() > 0 ? gp.charAt(0) : ' ';
                                                if (c0 == 'x' || c0 == 'y') {
                                                    (c0 == 'x' ? cc.getHorizontal() : cc.getVertical()).setGrowPriority(Integer.parseInt(gp.substring(2)));
                                                } else {
                                                    String[] grows2 = toTrimmedTokens(gp, ' ');
                                                    cc.getHorizontal().setGrowPriority(Integer.parseInt(grows2[0]));
                                                    if (grows2.length > 1) {
                                                        cc.getVertical().setGrowPriority(Integer.parseInt(grows2[1]));
                                                    }
                                                }
                                            } else if (part.startsWith("gap")) {
                                                BoundSize[] gaps = parseGaps(part);
                                                if (gaps[0] != null) {
                                                    cc.getVertical().setGapBefore(gaps[0]);
                                                }
                                                if (gaps[1] != null) {
                                                    cc.getHorizontal().setGapBefore(gaps[1]);
                                                }
                                                if (gaps[2] != null) {
                                                    cc.getVertical().setGapAfter(gaps[2]);
                                                }
                                                if (gaps[3] != null) {
                                                    cc.getHorizontal().setGapAfter(gaps[3]);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (c == 'a') {
                                int ix18 = startsWithLenient(part, new String[]{"aligny", "ay"}, new int[]{6, 2}, true);
                                if (ix18 > -1) {
                                    cc.getVertical().setAlign(parseUnitValueOrAlign(part.substring(ix18).trim(), false, null));
                                } else {
                                    int ix19 = startsWithLenient(part, new String[]{"alignx", "ax"}, new int[]{6, 2}, true);
                                    if (ix19 > -1) {
                                        cc.getHorizontal().setAlign(parseUnitValueOrAlign(part.substring(ix19).trim(), true, null));
                                    } else {
                                        int ix20 = startsWithLenient(part, "align", 2, true);
                                        if (ix20 > -1) {
                                            String[] gaps2 = toTrimmedTokens(part.substring(ix20).trim(), ' ');
                                            cc.getHorizontal().setAlign(parseUnitValueOrAlign(gaps2[0], true, null));
                                            if (gaps2.length > 1) {
                                                cc.getVertical().setAlign(parseUnitValueOrAlign(gaps2[1], false, null));
                                            }
                                        }
                                    }
                                }
                            }
                            if ((c == 'x' || c == 'y') && part.length() > 2 && ((c2 = part.charAt(1)) == ' ' || (c2 == '2' && part.charAt(2) == ' '))) {
                                if (cc.getPos() == null) {
                                    cc.setPos(new UnitValue[4]);
                                } else if (!cc.isBoundsInGrid()) {
                                    throw new IllegalArgumentException("Cannot combine 'position' with 'x/y/x2/y2' keywords.");
                                }
                                int edge = (c == 'x' ? 0 : 1) + (c2 == '2' ? 2 : 0);
                                UnitValue[] pos = cc.getPos();
                                pos[edge] = parseUnitValue(part.substring(2).trim(), null, c == 'x');
                                cc.setPos(pos);
                                cc.setBoundsInGrid(true);
                            } else if (c != 'c' || (ix3 = startsWithLenient(part, "cell", 4, true)) <= -1) {
                                if (c == 'p') {
                                    int ix21 = startsWithLenient(part, "pos", 3, true);
                                    if (ix21 <= -1) {
                                        int ix22 = startsWithLenient(part, "pad", 3, true);
                                        if (ix22 > -1) {
                                            UnitValue[] p = parseInsets(part.substring(ix22).trim(), false);
                                            UnitValue[] unitValueArr = new UnitValue[4];
                                            unitValueArr[0] = p[0];
                                            unitValueArr[1] = p.length > 1 ? p[1] : null;
                                            unitValueArr[2] = p.length > 2 ? p[2] : null;
                                            unitValueArr[3] = p.length > 3 ? p[3] : null;
                                            cc.setPadding(unitValueArr);
                                        } else {
                                            int ix23 = startsWithLenient(part, "pushx", 5, true);
                                            if (ix23 > -1) {
                                                cc.setPushX(parseFloat(part.substring(ix23).trim(), ResizeConstraint.WEIGHT_100));
                                            } else {
                                                int ix24 = startsWithLenient(part, "pushy", 5, true);
                                                if (ix24 > -1) {
                                                    cc.setPushY(parseFloat(part.substring(ix24).trim(), ResizeConstraint.WEIGHT_100));
                                                } else {
                                                    int ix25 = startsWithLenient(part, "push", 4, false);
                                                    if (ix25 > -1) {
                                                        String[] pushs = toTrimmedTokens(part.substring(ix25).trim(), ' ');
                                                        cc.setPushX(parseFloat(pushs[0], ResizeConstraint.WEIGHT_100));
                                                        cc.setPushY(parseFloat(pushs.length > 1 ? pushs[1] : "", ResizeConstraint.WEIGHT_100));
                                                    }
                                                }
                                            }
                                        }
                                    } else if (cc.getPos() == null || !cc.isBoundsInGrid()) {
                                        String[] pos2 = toTrimmedTokens(part.substring(ix21).trim(), ' ');
                                        UnitValue[] bounds = new UnitValue[4];
                                        for (int j = 0; j < pos2.length; j++) {
                                            bounds[j] = parseUnitValue(pos2[j], null, j % 2 == 0);
                                        }
                                        if ((bounds[0] == null && bounds[2] == null) || (bounds[1] == null && bounds[3] == null)) {
                                            throw new IllegalArgumentException("Both x and x2 or y and y2 can not be null!");
                                        }
                                        cc.setPos(bounds);
                                        cc.setBoundsInGrid(false);
                                    } else {
                                        throw new IllegalArgumentException("Can not combine 'pos' with 'x/y/x2/y2' keywords.");
                                    }
                                }
                                if (c != 't' || (ix2 = startsWithLenient(part, "tag", 3, true)) <= -1) {
                                    if (c == 'w' || c == 'h') {
                                        if (part.equals("wrap")) {
                                            cc.setWrap(true);
                                        } else if (part.startsWith("wrap ")) {
                                            cc.setWrapGapSize(parseBoundSize(part.substring(5).trim(), true, true));
                                        } else {
                                            boolean isHor = c == 'w';
                                            if (isHor && (part.startsWith("w ") || part.startsWith("width "))) {
                                                cc.getHorizontal().setSize(parseBoundSize(part.substring(part.charAt(1) == ' ' ? 2 : 6).trim(), false, true));
                                            } else if (isHor || (!part.startsWith("h ") && !part.startsWith("height "))) {
                                                if (part.startsWith("wmin ") || part.startsWith("wmax ") || part.startsWith("hmin ") || part.startsWith("hmax ")) {
                                                    String uvStr = part.substring(5).trim();
                                                    if (uvStr.length() > 0) {
                                                        UnitValue uv = parseUnitValue(uvStr, null, isHor);
                                                        boolean isMin = part.charAt(3) == 'n';
                                                        DimConstraint dc = isHor ? cc.getHorizontal() : cc.getVertical();
                                                        if (isMin) {
                                                            min = uv;
                                                        } else {
                                                            min = dc.getSize().getMin();
                                                        }
                                                        UnitValue preferred = dc.getSize().getPreferred();
                                                        if (isMin) {
                                                            uv = dc.getSize().getMax();
                                                        }
                                                        dc.setSize(new BoundSize(min, preferred, uv, uvStr));
                                                    }
                                                }
                                                if (part.equals("west")) {
                                                    cc.setDockSide(1);
                                                } else if (part.startsWith("hidemode ")) {
                                                    cc.setHideMode(Integer.parseInt(part.substring(9)));
                                                }
                                            } else {
                                                cc.getVertical().setSize(parseBoundSize(part.substring(part.charAt(1) == ' ' ? 2 : 7).trim(), false, false));
                                            }
                                        }
                                    }
                                    if (c != 'i' || !part.startsWith("id ")) {
                                        if (c == 'e') {
                                            if (part.equals("east")) {
                                                cc.setDockSide(3);
                                            } else if (part.equals("external")) {
                                                cc.setExternal(true);
                                            } else {
                                                int ix26 = startsWithLenient(part, new String[]{"endgroupx", "endgroupy", "egx", "egy"}, new int[]{-1, -1, -1, -1}, true);
                                                if (ix26 > -1) {
                                                    (part.charAt(ix26 + -1) == 'x' ? cc.getHorizontal() : cc.getVertical()).setEndGroup(part.substring(ix26).trim());
                                                }
                                            }
                                        }
                                        if (c == 'd') {
                                            if (part.equals("dock north")) {
                                                cc.setDockSide(0);
                                            } else if (part.equals("dock west")) {
                                                cc.setDockSide(1);
                                            } else if (part.equals("dock south")) {
                                                cc.setDockSide(2);
                                            } else if (part.equals("dock east")) {
                                                cc.setDockSide(3);
                                            } else if (part.equals("dock center")) {
                                                cc.getHorizontal().setGrow(Float.valueOf(100.0f));
                                                cc.getVertical().setGrow(Float.valueOf(100.0f));
                                                cc.setPushX(Float.valueOf(100.0f));
                                                cc.setPushY(Float.valueOf(100.0f));
                                            }
                                        }
                                        if (c != 'v' || (ix = startsWithLenient(part, new String[]{"visualpadding", "vp"}, new int[]{3, 2}, true)) <= -1) {
                                            UnitValue horAlign = parseAlignKeywords(part, true);
                                            if (horAlign != null) {
                                                cc.getHorizontal().setAlign(horAlign);
                                            } else {
                                                UnitValue verAlign = parseAlignKeywords(part, false);
                                                if (verAlign != null) {
                                                    cc.getVertical().setAlign(verAlign);
                                                } else {
                                                    throw new IllegalArgumentException("Unknown keyword.");
                                                }
                                            }
                                        } else {
                                            UnitValue[] p2 = parseInsets(part.substring(ix).trim(), false);
                                            UnitValue[] unitValueArr2 = new UnitValue[4];
                                            unitValueArr2[0] = p2[0];
                                            unitValueArr2[1] = p2.length > 1 ? p2[1] : null;
                                            unitValueArr2[2] = p2.length > 2 ? p2[2] : null;
                                            unitValueArr2[3] = p2.length > 3 ? p2[3] : null;
                                            cc.setVisualPadding(unitValueArr2);
                                        }
                                    } else {
                                        cc.setId(part.substring(3).trim());
                                        int dIx = cc.getId().indexOf(46);
                                        if (dIx == 0 || dIx == cc.getId().length() - 1) {
                                            throw new IllegalArgumentException("Dot must not be first or last!");
                                        }
                                    }
                                } else {
                                    cc.setTag(part.substring(ix2).trim());
                                }
                            } else {
                                String[] grs = toTrimmedTokens(part.substring(ix3).trim(), ' ');
                                if (grs.length < 2) {
                                    throw new IllegalArgumentException("At least two integers must follow " + part);
                                }
                                cc.setCellX(Integer.parseInt(grs[0]));
                                cc.setCellY(Integer.parseInt(grs[1]));
                                if (grs.length > 2) {
                                    cc.setSpanX(Integer.parseInt(grs[2]));
                                }
                                if (grs.length > 3) {
                                    cc.setSpanY(Integer.parseInt(grs[3]));
                                }
                            }
                        } else {
                            cc.setFlowX(part.charAt(4) == 'x' ? Boolean.TRUE : Boolean.FALSE);
                        }
                    }
                } catch (Exception ex) {
                    throw new IllegalArgumentException("Error parsing Constraint: '" + part + "'", ex);
                }
            }
        }
        return cc;
    }

    public static UnitValue[] parseInsets(String s, boolean acceptPanel) {
        if (s.length() != 0 && !s.equals("dialog") && !s.equals("panel")) {
            String[] insS = toTrimmedTokens(s, ' ');
            UnitValue[] ins = new UnitValue[4];
            int j = 0;
            while (j < 4) {
                UnitValue insSz = parseUnitValue(insS[j < insS.length ? j : insS.length - 1], UnitValue.ZERO, j % 2 == 1);
                if (insSz == null) {
                    insSz = PlatformDefaults.getPanelInsets(j);
                }
                ins[j] = insSz;
                j++;
            }
            return ins;
        } else if (!acceptPanel) {
            throw new IllegalArgumentException("Insets now allowed: " + s + "\n");
        } else {
            boolean isPanel = s.startsWith("p");
            UnitValue[] ins2 = new UnitValue[4];
            for (int j2 = 0; j2 < 4; j2++) {
                ins2[j2] = isPanel ? PlatformDefaults.getPanelInsets(j2) : PlatformDefaults.getDialogInsets(j2);
            }
            return ins2;
        }
    }

    private static BoundSize[] parseGaps(String s) {
        boolean x;
        char c;
        char c2;
        boolean z = false;
        BoundSize[] ret = new BoundSize[4];
        int ix = startsWithLenient(s, "gaptop", -1, true);
        if (ix > -1) {
            ret[0] = parseBoundSize(s.substring(ix).trim(), true, false);
        } else {
            int ix2 = startsWithLenient(s, "gapleft", -1, true);
            if (ix2 > -1) {
                ret[1] = parseBoundSize(s.substring(ix2).trim(), true, true);
            } else {
                int ix3 = startsWithLenient(s, "gapbottom", -1, true);
                if (ix3 > -1) {
                    ret[2] = parseBoundSize(s.substring(ix3).trim(), true, false);
                } else {
                    int ix4 = startsWithLenient(s, "gapright", -1, true);
                    if (ix4 > -1) {
                        ret[3] = parseBoundSize(s.substring(ix4).trim(), true, true);
                    } else {
                        int ix5 = startsWithLenient(s, "gapbefore", -1, true);
                        if (ix5 > -1) {
                            ret[1] = parseBoundSize(s.substring(ix5).trim(), true, true);
                        } else {
                            int ix6 = startsWithLenient(s, "gapafter", -1, true);
                            if (ix6 > -1) {
                                ret[3] = parseBoundSize(s.substring(ix6).trim(), true, true);
                            } else {
                                int ix7 = startsWithLenient(s, new String[]{"gapx", "gapy"}, (int[]) null, true);
                                if (ix7 > -1) {
                                    if (s.charAt(3) == 'x') {
                                        x = true;
                                    } else {
                                        x = false;
                                    }
                                    String[] gaps = toTrimmedTokens(s.substring(ix7).trim(), ' ');
                                    if (x) {
                                        c = 1;
                                    } else {
                                        c = 0;
                                    }
                                    ret[c] = parseBoundSize(gaps[0], true, x);
                                    if (gaps.length > 1) {
                                        if (x) {
                                            c2 = 3;
                                        } else {
                                            c2 = 2;
                                        }
                                        String str = gaps[1];
                                        if (!x) {
                                            z = true;
                                        }
                                        ret[c2] = parseBoundSize(str, true, z);
                                    }
                                } else {
                                    int ix8 = startsWithLenient(s, "gap ", 1, true);
                                    if (ix8 > -1) {
                                        String[] gaps2 = toTrimmedTokens(s.substring(ix8).trim(), ' ');
                                        ret[1] = parseBoundSize(gaps2[0], true, true);
                                        if (gaps2.length > 1) {
                                            ret[3] = parseBoundSize(gaps2[1], true, false);
                                            if (gaps2.length > 2) {
                                                ret[0] = parseBoundSize(gaps2[2], true, true);
                                                if (gaps2.length > 3) {
                                                    ret[2] = parseBoundSize(gaps2[3], true, false);
                                                }
                                            }
                                        }
                                    } else {
                                        throw new IllegalArgumentException("Unknown Gap part: '" + s + "'");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }

    private static int parseSpan(String s) {
        return s.length() > 0 ? Integer.parseInt(s) : LayoutUtil.INF;
    }

    private static Float parseFloat(String s, Float nullVal) {
        return s.length() > 0 ? new Float(Float.parseFloat(s)) : nullVal;
    }

    public static BoundSize parseBoundSize(String s, boolean isGap, boolean isHor) {
        if (s.length() == 0 || s.equals("null") || s.equals("n")) {
            return null;
        }
        boolean push = false;
        if (s.endsWith("push")) {
            push = true;
            s = s.substring(0, s.length() - (s.endsWith(":push") ? 5 : 4));
            if (s.length() == 0) {
                return new BoundSize(null, null, null, true, s);
            }
        }
        String[] sizes = toTrimmedTokens(s, ':');
        String s0 = sizes[0];
        if (sizes.length == 1) {
            boolean hasEM = s0.endsWith("!");
            if (hasEM) {
                s0 = s0.substring(0, s0.length() - 1);
            }
            UnitValue uv = parseUnitValue(s0, null, isHor);
            return new BoundSize((isGap || hasEM) ? uv : null, uv, hasEM ? uv : null, push, s);
        } else if (sizes.length == 2) {
            return new BoundSize(parseUnitValue(s0, null, isHor), parseUnitValue(sizes[1], null, isHor), null, push, s);
        } else {
            if (sizes.length == 3) {
                return new BoundSize(parseUnitValue(s0, null, isHor), parseUnitValue(sizes[1], null, isHor), parseUnitValue(sizes[2], null, isHor), push, s);
            }
            throw new IllegalArgumentException("Min:Preferred:Max size section must contain 0, 1 or 2 colons. '" + s + "'");
        }
    }

    public static UnitValue parseUnitValueOrAlign(String s, boolean isHor, UnitValue emptyReplacement) {
        if (s.length() == 0) {
            return emptyReplacement;
        }
        UnitValue align = parseAlignKeywords(s, isHor);
        return align != null ? align : parseUnitValue(s, emptyReplacement, isHor);
    }

    public static UnitValue parseUnitValue(String s, boolean isHor) {
        return parseUnitValue(s, null, isHor);
    }

    private static UnitValue parseUnitValue(String s, UnitValue emptyReplacement, boolean isHor) {
        String[] uvs;
        char delim;
        if (s == null || s.length() == 0) {
            return emptyReplacement;
        }
        char c0 = s.charAt(0);
        if (c0 == '(' && s.charAt(s.length() - 1) == ')') {
            s = s.substring(1, s.length() - 1);
        }
        if (c0 == 'n' && (s.equals("null") || s.equals("n"))) {
            return null;
        }
        if (c0 == 'i' && s.equals("inf")) {
            return UnitValue.INF;
        }
        int oper = getOper(s);
        boolean inline = oper == 101 || oper == 102 || oper == 103 || oper == 104;
        if (oper != 100) {
            if (!inline) {
                String sub = s.substring(4, s.length() - 1).trim();
                uvs = toTrimmedTokens(sub, ',');
                if (uvs.length == 1) {
                    return parseUnitValue(sub, null, isHor);
                }
            } else {
                if (oper == 101) {
                    delim = '+';
                } else if (oper == 102) {
                    delim = '-';
                } else if (oper == 103) {
                    delim = '*';
                } else {
                    delim = '/';
                }
                uvs = toTrimmedTokens(s, delim);
                if (uvs.length > 2) {
                    String last = uvs[uvs.length - 1];
                    uvs = new String[]{s.substring(0, (s.length() - last.length()) - 1), last};
                }
            }
            if (uvs.length != 2) {
                throw new IllegalArgumentException("Malformed UnitValue: '" + s + "'");
            }
            UnitValue sub1 = parseUnitValue(uvs[0], null, isHor);
            UnitValue sub2 = parseUnitValue(uvs[1], null, isHor);
            if (sub1 != null && sub2 != null) {
                return new UnitValue(isHor, oper, sub1, sub2, s);
            }
            throw new IllegalArgumentException("Malformed UnitValue. Must be two sub-values: '" + s + "'");
        }
        try {
            String[] numParts = getNumTextParts(s);
            return new UnitValue(numParts[0].length() > 0 ? Float.parseFloat(numParts[0]) : 1.0f, numParts[1], isHor, oper, s);
        } catch (Exception e) {
            throw new IllegalArgumentException("Malformed UnitValue: '" + s + "'", e);
        }
    }

    static UnitValue parseAlignKeywords(String s, boolean isHor) {
        if (startsWithLenient(s, FlatClientProperties.TABBED_PANE_ALIGN_CENTER, 1, false) != -1) {
            return UnitValue.CENTER;
        }
        if (isHor) {
            if (startsWithLenient(s, "left", 1, false) != -1) {
                return UnitValue.LEFT;
            }
            if (startsWithLenient(s, "right", 1, false) != -1) {
                return UnitValue.RIGHT;
            }
            if (startsWithLenient(s, FlatClientProperties.TABBED_PANE_ALIGN_LEADING, 4, false) != -1) {
                return UnitValue.LEADING;
            }
            if (startsWithLenient(s, "trailing", 5, false) != -1) {
                return UnitValue.TRAILING;
            }
            if (startsWithLenient(s, "label", 5, false) != -1) {
                return UnitValue.LABEL;
            }
        } else if (startsWithLenient(s, "baseline", 4, false) != -1) {
            return UnitValue.BASELINE_IDENTITY;
        } else {
            if (startsWithLenient(s, "top", 1, false) != -1) {
                return UnitValue.TOP;
            }
            if (startsWithLenient(s, "bottom", 1, false) != -1) {
                return UnitValue.BOTTOM;
            }
        }
        return null;
    }

    private static String[] getNumTextParts(String s) {
        int iSz = s.length();
        for (int i = 0; i < iSz; i++) {
            char c = s.charAt(i);
            if (c == ' ') {
                throw new IllegalArgumentException("Space in UnitValue: '" + s + "'");
            } else if ((c < '0' || c > '9') && c != '.' && c != '-') {
                return new String[]{s.substring(0, i).trim(), s.substring(i).trim()};
            }
        }
        return new String[]{s, ""};
    }

    private static int getOper(String s) {
        int len = s.length();
        if (len < 3) {
            return 100;
        }
        if (len > 5 && s.charAt(3) == '(' && s.charAt(len - 1) == ')') {
            if (s.startsWith("min(")) {
                return 105;
            }
            if (s.startsWith("max(")) {
                return 106;
            }
            if (s.startsWith("mid(")) {
                return 107;
            }
        }
        for (int j = 0; j < 2; j++) {
            int p = 0;
            for (int i = len - 1; i > 0; i--) {
                char c = s.charAt(i);
                if (c == ')') {
                    p++;
                } else if (c == '(') {
                    p--;
                } else if (p != 0) {
                    continue;
                } else if (j == 0) {
                    if (c == '+') {
                        return 101;
                    }
                    if (c == '-') {
                        return 102;
                    }
                } else if (c == '*') {
                    return 103;
                } else {
                    if (c == '/') {
                        return 104;
                    }
                }
            }
        }
        return 100;
    }

    private static int startsWithLenient(String s, String[] matches, int[] minChars, boolean acceptTrailing) {
        for (int i = 0; i < matches.length; i++) {
            int ix = startsWithLenient(s, matches[i], minChars != null ? minChars[i] : -1, acceptTrailing);
            if (ix > -1) {
                return ix;
            }
        }
        return -1;
    }

    private static int startsWithLenient(String s, String match, int minChars, boolean acceptTrailing) {
        if (s.charAt(0) != match.charAt(0)) {
            return -1;
        }
        if (minChars == -1) {
            minChars = match.length();
        }
        int sSz = s.length();
        if (sSz < minChars) {
            return -1;
        }
        int mSz = match.length();
        int sIx = 0;
        for (int mIx = 0; mIx < mSz; mIx++) {
            while (sIx < sSz && (s.charAt(sIx) == ' ' || s.charAt(sIx) == '_')) {
                sIx++;
            }
            if (sIx >= sSz || s.charAt(sIx) != match.charAt(mIx)) {
                if (mIx < minChars || ((!acceptTrailing && sIx < sSz) || (sIx < sSz && s.charAt(sIx - 1) != ' '))) {
                    sIx = -1;
                }
                return sIx;
            }
            sIx++;
        }
        if (sIx >= sSz || acceptTrailing || s.charAt(sIx) == ' ') {
            return sIx;
        }
        return -1;
    }

    private static String[] toTrimmedTokens(String s, char sep) {
        int pNr;
        int toks = 0;
        int sSize = s.length();
        boolean disregardDoubles = sep == ' ';
        int p = 0;
        int i = 0;
        while (i < sSize) {
            char c = s.charAt(i);
            if (c == '(') {
                p++;
            } else if (c == ')') {
                p--;
            } else if (p == 0 && c == sep) {
                toks++;
                while (disregardDoubles && i < sSize - 1 && s.charAt(i + 1) == ' ') {
                    i++;
                }
            }
            if (p < 0) {
                throw new IllegalArgumentException("Unbalanced parentheses: '" + s + "'");
            }
            i++;
        }
        if (p != 0) {
            throw new IllegalArgumentException("Unbalanced parentheses: '" + s + "'");
        } else if (toks == 0) {
            return new String[]{s.trim()};
        } else {
            String[] retArr = new String[(toks + 1)];
            int st = 0;
            int p2 = 0;
            int i2 = 0;
            int pNr2 = 0;
            while (i2 < sSize) {
                char c2 = s.charAt(i2);
                if (c2 == '(') {
                    p2++;
                    pNr = pNr2;
                } else if (c2 == ')') {
                    p2--;
                    pNr = pNr2;
                } else if (p2 == 0 && c2 == sep) {
                    pNr = pNr2 + 1;
                    retArr[pNr2] = s.substring(st, i2).trim();
                    st = i2 + 1;
                    while (disregardDoubles && i2 < sSize - 1 && s.charAt(i2 + 1) == ' ') {
                        i2++;
                    }
                } else {
                    pNr = pNr2;
                }
                i2++;
                pNr2 = pNr;
            }
            int pNr3 = pNr2 + 1;
            retArr[pNr2] = s.substring(st, sSize).trim();
            return retArr;
        }
    }

    private static ArrayList<String> getRowColAndGapsTrimmed(String s) {
        if (s.indexOf(Opcode.IUSHR) != -1) {
            s = s.replaceAll("\\|", "][");
        }
        ArrayList<String> retList = new ArrayList<>(Math.max(s.length() >> 3, 3));
        int s0 = 0;
        int s1 = 0;
        int st = 0;
        int iSz = s.length();
        for (int i = 0; i < iSz; i++) {
            char c = s.charAt(i);
            if (c == '[') {
                s0++;
            } else if (c == ']') {
                s1++;
            } else {
                continue;
            }
            if (!(s0 == s1 || s0 - 1 == s1)) {
                break;
            }
            retList.add(s.substring(st, i).trim());
            st = i + 1;
        }
        if (s0 != s1) {
            throw new IllegalArgumentException("'[' and ']' mismatch in row/column format string: " + s);
        }
        if (s0 == 0) {
            retList.add("");
            retList.add(s);
            retList.add("");
        } else if (retList.size() % 2 == 0) {
            retList.add(s.substring(st, s.length()));
        }
        return retList;
    }

    public static String prepare(String s) {
        return s != null ? s.trim().toLowerCase() : "";
    }
}
