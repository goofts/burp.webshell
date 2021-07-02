package com.kitfox.svg.animation;

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

public abstract class AnimateBase extends AnimationElement {
    private double repeatCount = Double.NaN;
    private TimeBase repeatDur;

    @Override // com.kitfox.svg.animation.AnimationElement
    public void evalParametric(AnimationTimeEval state, double curTime) {
        evalParametric(state, curTime, this.repeatCount, this.repeatDur == null ? Double.NaN : this.repeatDur.evalTime());
    }

    @Override // com.kitfox.svg.animation.AnimationElement, com.kitfox.svg.SVGElement
    public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent) throws SAXException {
        super.loaderStartElement(helper, attrs, parent);
        String repeatDurTime = attrs.getValue("repeatDur");
        if (repeatDurTime != null) {
            try {
                helper.animTimeParser.ReInit(new StringReader(repeatDurTime));
                this.repeatDur = helper.animTimeParser.Expr();
                this.repeatDur.setParentElement(this);
            } catch (Exception e) {
                throw new SAXException(e);
            }
        }
        String strn = attrs.getValue("repeatCount");
        if (strn == null) {
            this.repeatCount = 1.0d;
        } else if ("indefinite".equals(strn)) {
            this.repeatCount = Double.POSITIVE_INFINITY;
        } else {
            try {
                this.repeatCount = Double.parseDouble(strn);
            } catch (Exception e2) {
                this.repeatCount = Double.NaN;
            }
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.animation.AnimationElement
    public void rebuild(AnimTimeParser animTimeParser) throws SVGException {
        String strn;
        super.rebuild(animTimeParser);
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("repeatDur")) && (strn = sty.getStringValue()) != null) {
            animTimeParser.ReInit(new StringReader(strn));
            try {
                this.repeatDur = animTimeParser.Expr();
            } catch (ParseException ex) {
                Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not parse '" + strn + "'", (Throwable) ex);
            }
        }
        if (getPres(sty.setName("repeatCount"))) {
            String strn2 = sty.getStringValue();
            if (strn2 == null) {
                this.repeatCount = 1.0d;
            } else if ("indefinite".equals(strn2)) {
                this.repeatCount = Double.POSITIVE_INFINITY;
            } else {
                try {
                    this.repeatCount = Double.parseDouble(strn2);
                } catch (Exception e) {
                    this.repeatCount = Double.NaN;
                }
            }
        }
    }

    public double getRepeatCount() {
        return this.repeatCount;
    }

    public void setRepeatCount(double repeatCount2) {
        this.repeatCount = repeatCount2;
    }

    public TimeBase getRepeatDur() {
        return this.repeatDur;
    }

    public void setRepeatDur(TimeBase repeatDur2) {
        this.repeatDur = repeatDur2;
    }
}
