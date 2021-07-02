package com.kitfox.svg.animation;

import com.formdev.flatlaf.FlatClientProperties;
import com.kitfox.svg.SVGConst;
import com.kitfox.svg.SVGElement;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGLoaderHelper;
import com.kitfox.svg.animation.parser.AnimTimeParser;
import com.kitfox.svg.animation.parser.ParseException;
import com.kitfox.svg.xml.StyleAttribute;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public abstract class AnimationElement extends SVGElement {
    public static final int AC_REPLACE = 0;
    public static final int AC_SUM = 1;
    public static final int AD_REPLACE = 0;
    public static final int AD_SUM = 1;
    public static final int AT_AUTO = 2;
    public static final int AT_CSS = 0;
    public static final int AT_XML = 1;
    public static final int FT_AUTO = 4;
    public static final int FT_DEFAULT = 5;
    public static final int FT_FREEZE = 1;
    public static final int FT_HOLD = 2;
    public static final int FT_REMOVE = 0;
    public static final int FT_TRANSITION = 3;
    private int accumulateType = 0;
    private int additiveType = 0;
    protected String attribName;
    protected int attribType = 2;
    private TimeBase beginTime;
    private TimeBase durTime;
    private TimeBase endTime;
    private int fillType = 4;

    public static String animationElementToString(int attrValue) {
        switch (attrValue) {
            case 0:
                return "CSS";
            case 1:
                return "XML";
            case 2:
                return "AUTO";
            default:
                throw new RuntimeException("Unknown element type");
        }
    }

    @Override // com.kitfox.svg.SVGElement
    public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent) throws SAXException {
        super.loaderStartElement(helper, attrs, parent);
        this.attribName = attrs.getValue("attributeName");
        String attribType2 = attrs.getValue("attributeType");
        if (attribType2 != null) {
            String attribType3 = attribType2.toLowerCase();
            if (attribType3.equals("css")) {
                this.attribType = 0;
            } else if (attribType3.equals("xml")) {
                this.attribType = 1;
            }
        }
        String beginTime2 = attrs.getValue("begin");
        String durTime2 = attrs.getValue("dur");
        String endTime2 = attrs.getValue("end");
        if (beginTime2 != null) {
            try {
                helper.animTimeParser.ReInit(new StringReader(beginTime2));
                this.beginTime = helper.animTimeParser.Expr();
                this.beginTime.setParentElement(this);
            } catch (Exception e) {
                throw new SAXException(e);
            }
        }
        if (durTime2 != null) {
            helper.animTimeParser.ReInit(new StringReader(durTime2));
            this.durTime = helper.animTimeParser.Expr();
            this.durTime.setParentElement(this);
        }
        if (endTime2 != null) {
            helper.animTimeParser.ReInit(new StringReader(endTime2));
            this.endTime = helper.animTimeParser.Expr();
            this.endTime.setParentElement(this);
        }
        String fill = attrs.getValue(FlatClientProperties.TABBED_PANE_ALIGN_FILL);
        if (fill != null) {
            if (fill.equals("remove")) {
                this.fillType = 0;
            }
            if (fill.equals("freeze")) {
                this.fillType = 1;
            }
            if (fill.equals("hold")) {
                this.fillType = 2;
            }
            if (fill.equals("transiton")) {
                this.fillType = 3;
            }
            if (fill.equals("auto")) {
                this.fillType = 4;
            }
            if (fill.equals("default")) {
                this.fillType = 5;
            }
        }
        String additiveStrn = attrs.getValue("additive");
        if (additiveStrn != null) {
            if (additiveStrn.equals("replace")) {
                this.additiveType = 0;
            }
            if (additiveStrn.equals("sum")) {
                this.additiveType = 1;
            }
        }
        String accumulateStrn = attrs.getValue("accumulate");
        if (accumulateStrn != null) {
            if (accumulateStrn.equals("replace")) {
                this.accumulateType = 0;
            }
            if (accumulateStrn.equals("sum")) {
                this.accumulateType = 1;
            }
        }
    }

    public String getAttribName() {
        return this.attribName;
    }

    public int getAttribType() {
        return this.attribType;
    }

    public int getAdditiveType() {
        return this.additiveType;
    }

    public int getAccumulateType() {
        return this.accumulateType;
    }

    public void evalParametric(AnimationTimeEval state, double curTime) {
        evalParametric(state, curTime, Double.NaN, Double.NaN);
    }

    /* access modifiers changed from: protected */
    public void evalParametric(AnimationTimeEval state, double curTime, double repeatCount, double repeatDur) {
        double end;
        double repeat;
        double finishTime;
        double begin = this.beginTime == null ? 0.0d : this.beginTime.evalTime();
        if (Double.isNaN(begin) || begin > curTime) {
            state.set(Double.NaN, 0);
            return;
        }
        double dur = this.durTime == null ? Double.NaN : this.durTime.evalTime();
        if (Double.isNaN(dur)) {
            state.set(Double.NaN, 0);
            return;
        }
        if (this.endTime == null) {
            end = Double.NaN;
        } else {
            end = this.endTime.evalTime();
        }
        if (!Double.isNaN(repeatCount) || !Double.isNaN(repeatDur)) {
            double d = Double.isNaN(repeatCount) ? Double.POSITIVE_INFINITY : dur * repeatCount;
            if (Double.isNaN(repeatDur)) {
                repeatDur = Double.POSITIVE_INFINITY;
            }
            repeat = Math.min(d, repeatDur);
        } else {
            repeat = Double.NaN;
        }
        if (Double.isNaN(repeat) && Double.isNaN(end)) {
            end = begin + dur;
        }
        if (Double.isNaN(end)) {
            finishTime = begin + repeat;
        } else if (Double.isNaN(repeat)) {
            finishTime = end;
        } else {
            finishTime = Math.min(end, repeat);
        }
        double evalTime = Math.min(curTime, finishTime);
        double ratio = (evalTime - begin) / dur;
        int rep = (int) ratio;
        double interp = ratio - ((double) rep);
        if (interp < 1.0E-5d) {
            interp = 0.0d;
        }
        if (curTime == evalTime) {
            state.set(interp, rep);
            return;
        }
        switch (this.fillType) {
            case 1:
            case 2:
            case 3:
                if (interp == 0.0d) {
                    interp = 1.0d;
                }
                state.set(interp, rep);
                return;
            default:
                state.set(Double.NaN, rep);
                return;
        }
    }

    /* access modifiers changed from: package-private */
    public double evalStartTime() {
        if (this.beginTime == null) {
            return Double.NaN;
        }
        return this.beginTime.evalTime();
    }

    /* access modifiers changed from: package-private */
    public double evalDurTime() {
        if (this.durTime == null) {
            return Double.NaN;
        }
        return this.durTime.evalTime();
    }

    /* access modifiers changed from: package-private */
    public double evalEndTime() {
        if (this.endTime == null) {
            return Double.NaN;
        }
        return this.endTime.evalTime();
    }

    /* access modifiers changed from: package-private */
    public boolean hasEndTime() {
        return this.endTime != null;
    }

    @Override // com.kitfox.svg.SVGElement
    public boolean updateTime(double curTime) {
        return false;
    }

    public void rebuild() throws SVGException {
        rebuild(new AnimTimeParser(new StringReader("")));
    }

    /* access modifiers changed from: protected */
    public void rebuild(AnimTimeParser animTimeParser) throws SVGException {
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("begin"))) {
            String newVal = sty.getStringValue();
            animTimeParser.ReInit(new StringReader(newVal));
            try {
                this.beginTime = animTimeParser.Expr();
            } catch (ParseException ex) {
                Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not parse '" + newVal + "'", (Throwable) ex);
            }
        }
        if (getPres(sty.setName("dur"))) {
            String newVal2 = sty.getStringValue();
            animTimeParser.ReInit(new StringReader(newVal2));
            try {
                this.durTime = animTimeParser.Expr();
            } catch (ParseException ex2) {
                Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not parse '" + newVal2 + "'", (Throwable) ex2);
            }
        }
        if (getPres(sty.setName("end"))) {
            String newVal3 = sty.getStringValue();
            animTimeParser.ReInit(new StringReader(newVal3));
            try {
                this.endTime = animTimeParser.Expr();
            } catch (ParseException ex3) {
                Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not parse '" + newVal3 + "'", (Throwable) ex3);
            }
        }
        if (getPres(sty.setName(FlatClientProperties.TABBED_PANE_ALIGN_FILL))) {
            String newVal4 = sty.getStringValue();
            if (newVal4.equals("remove")) {
                this.fillType = 0;
            }
            if (newVal4.equals("freeze")) {
                this.fillType = 1;
            }
            if (newVal4.equals("hold")) {
                this.fillType = 2;
            }
            if (newVal4.equals("transiton")) {
                this.fillType = 3;
            }
            if (newVal4.equals("auto")) {
                this.fillType = 4;
            }
            if (newVal4.equals("default")) {
                this.fillType = 5;
            }
        }
        if (getPres(sty.setName("additive"))) {
            String newVal5 = sty.getStringValue();
            if (newVal5.equals("replace")) {
                this.additiveType = 0;
            }
            if (newVal5.equals("sum")) {
                this.additiveType = 1;
            }
        }
        if (getPres(sty.setName("accumulate"))) {
            String newVal6 = sty.getStringValue();
            if (newVal6.equals("replace")) {
                this.accumulateType = 0;
            }
            if (newVal6.equals("sum")) {
                this.accumulateType = 1;
            }
        }
    }

    public TimeBase getBeginTime() {
        return this.beginTime;
    }

    public void setBeginTime(TimeBase beginTime2) {
        this.beginTime = beginTime2;
    }

    public TimeBase getDurTime() {
        return this.durTime;
    }

    public void setDurTime(TimeBase durTime2) {
        this.durTime = durTime2;
    }

    public TimeBase getEndTime() {
        return this.endTime;
    }

    public void setEndTime(TimeBase endTime2) {
        this.endTime = endTime2;
    }

    public int getFillType() {
        return this.fillType;
    }

    public void setFillType(int fillType2) {
        this.fillType = fillType2;
    }

    public void setAdditiveType(int additiveType2) {
        this.additiveType = additiveType2;
    }

    public void setAccumulateType(int accumulateType2) {
        this.accumulateType = accumulateType2;
    }
}
