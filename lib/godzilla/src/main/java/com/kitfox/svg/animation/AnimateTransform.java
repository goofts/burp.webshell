package com.kitfox.svg.animation;

import com.kitfox.svg.SVGElement;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGLoaderHelper;
import com.kitfox.svg.animation.parser.AnimTimeParser;
import com.kitfox.svg.xml.StyleAttribute;
import com.kitfox.svg.xml.XMLParseUtil;
import java.awt.geom.AffineTransform;
import java.util.regex.Pattern;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class AnimateTransform extends AnimateXform {
    public static final int AT_REPLACE = 0;
    public static final int AT_SUM = 1;
    public static final String TAG_NAME = "animateTransform";
    public static final int TR_INVALID = 5;
    public static final int TR_ROTATE = 1;
    public static final int TR_SCALE = 2;
    public static final int TR_SKEWX = 4;
    public static final int TR_SKEWY = 3;
    public static final int TR_TRANSLATE = 0;
    private int additive = 0;
    private double[] keyTimes;
    private double[][] values;
    private int xformType = 5;

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    @Override // com.kitfox.svg.animation.AnimationElement, com.kitfox.svg.animation.AnimateXform, com.kitfox.svg.SVGElement, com.kitfox.svg.animation.AnimateBase
    public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent) throws SAXException {
        super.loaderStartElement(helper, attrs, parent);
        String type = attrs.getValue("type").toLowerCase();
        if (type.equals("translate")) {
            this.xformType = 0;
        }
        if (type.equals("rotate")) {
            this.xformType = 1;
        }
        if (type.equals("scale")) {
            this.xformType = 2;
        }
        if (type.equals("skewx")) {
            this.xformType = 4;
        }
        if (type.equals("skewy")) {
            this.xformType = 3;
        }
        String fromStrn = attrs.getValue("from");
        String toStrn = attrs.getValue("to");
        if (!(fromStrn == null || toStrn == null)) {
            this.values = new double[][]{validate(XMLParseUtil.parseDoubleList(fromStrn)), validate(XMLParseUtil.parseDoubleList(toStrn))};
            this.keyTimes = new double[]{0.0d, 1.0d};
        }
        String keyTimeStrn = attrs.getValue("keyTimes");
        String valuesStrn = attrs.getValue("values");
        if (!(keyTimeStrn == null || valuesStrn == null)) {
            this.keyTimes = XMLParseUtil.parseDoubleList(keyTimeStrn);
            String[] valueList = Pattern.compile(";").split(valuesStrn);
            this.values = new double[valueList.length][];
            for (int i = 0; i < valueList.length; i++) {
                this.values[i] = validate(XMLParseUtil.parseDoubleList(valueList[i]));
            }
        }
        String additive2 = attrs.getValue("additive");
        if (additive2 != null && additive2.equals("sum")) {
            this.additive = 1;
        }
    }

    private double[] validate(double[] paramList) {
        switch (this.xformType) {
            case 2:
                if (paramList == null) {
                    return new double[]{1.0d, 1.0d};
                }
                if (paramList.length != 1) {
                    return paramList;
                }
                return new double[]{paramList[0], paramList[0]};
            default:
                return paramList;
        }
    }

    @Override // com.kitfox.svg.animation.AnimateXform
    public AffineTransform eval(AffineTransform xform, double interp) {
        int idx = 0;
        while (true) {
            if (idx >= this.keyTimes.length - 1) {
                break;
            } else if (interp >= this.keyTimes[idx]) {
                idx--;
                if (idx < 0) {
                    idx = 0;
                }
            } else {
                idx++;
            }
        }
        double spanStartTime = this.keyTimes[idx];
        double interp2 = (interp - spanStartTime) / (this.keyTimes[idx + 1] - spanStartTime);
        double[] fromValue = this.values[idx];
        double[] toValue = this.values[idx + 1];
        switch (this.xformType) {
            case 0:
                xform.setToTranslation(lerp(fromValue.length >= 1 ? fromValue[0] : 0.0d, toValue.length >= 1 ? toValue[0] : 0.0d, interp2), lerp(fromValue.length >= 2 ? fromValue[1] : 0.0d, toValue.length >= 2 ? toValue[1] : 0.0d, interp2));
                break;
            case 1:
                xform.setToRotation(Math.toRadians(lerp(fromValue[0], toValue[0], interp2)), lerp(fromValue.length == 3 ? fromValue[1] : 0.0d, toValue.length == 3 ? toValue[1] : 0.0d, interp2), lerp(fromValue.length == 3 ? fromValue[2] : 0.0d, toValue.length == 3 ? toValue[2] : 0.0d, interp2));
                break;
            case 2:
                xform.setToScale(lerp(fromValue.length >= 1 ? fromValue[0] : 1.0d, toValue.length >= 1 ? toValue[0] : 1.0d, interp2), lerp(fromValue.length >= 2 ? fromValue[1] : 1.0d, toValue.length >= 2 ? toValue[1] : 1.0d, interp2));
                break;
            case 3:
                xform.setToShear(0.0d, Math.toRadians(lerp(fromValue[0], toValue[0], interp2)));
                break;
            case 4:
                xform.setToShear(Math.toRadians(lerp(fromValue[0], toValue[0], interp2)), 0.0d);
                break;
            default:
                xform.setToIdentity();
                break;
        }
        return xform;
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.animation.AnimationElement, com.kitfox.svg.animation.AnimateBase
    public void rebuild(AnimTimeParser animTimeParser) throws SVGException {
        super.rebuild(animTimeParser);
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("type"))) {
            String strn = sty.getStringValue().toLowerCase();
            if (strn.equals("translate")) {
                this.xformType = 0;
            }
            if (strn.equals("rotate")) {
                this.xformType = 1;
            }
            if (strn.equals("scale")) {
                this.xformType = 2;
            }
            if (strn.equals("skewx")) {
                this.xformType = 4;
            }
            if (strn.equals("skewy")) {
                this.xformType = 3;
            }
        }
        String fromStrn = null;
        if (getPres(sty.setName("from"))) {
            fromStrn = sty.getStringValue();
        }
        String toStrn = null;
        if (getPres(sty.setName("to"))) {
            toStrn = sty.getStringValue();
        }
        if (!(fromStrn == null || toStrn == null)) {
            this.values = new double[][]{validate(XMLParseUtil.parseDoubleList(fromStrn)), validate(XMLParseUtil.parseDoubleList(toStrn))};
        }
        String keyTimeStrn = null;
        if (getPres(sty.setName("keyTimes"))) {
            keyTimeStrn = sty.getStringValue();
        }
        String valuesStrn = null;
        if (getPres(sty.setName("values"))) {
            valuesStrn = sty.getStringValue();
        }
        if (!(keyTimeStrn == null || valuesStrn == null)) {
            this.keyTimes = XMLParseUtil.parseDoubleList(keyTimeStrn);
            String[] valueList = Pattern.compile(";").split(valuesStrn);
            this.values = new double[valueList.length][];
            for (int i = 0; i < valueList.length; i++) {
                this.values[i] = validate(XMLParseUtil.parseDoubleList(valueList[i]));
            }
        }
        if (getPres(sty.setName("additive")) && sty.getStringValue().toLowerCase().equals("sum")) {
            this.additive = 1;
        }
    }

    public double[][] getValues() {
        return this.values;
    }

    public void setValues(double[][] values2) {
        this.values = values2;
    }

    public double[] getKeyTimes() {
        return this.keyTimes;
    }

    public void setKeyTimes(double[] keyTimes2) {
        this.keyTimes = keyTimes2;
    }

    public int getAdditive() {
        return this.additive;
    }

    public void setAdditive(int additive2) {
        this.additive = additive2;
    }

    public int getXformType() {
        return this.xformType;
    }

    public void setXformType(int xformType2) {
        this.xformType = xformType2;
    }
}
