package com.kitfox.svg.animation;

import com.kitfox.svg.SVGConst;
import com.kitfox.svg.SVGElement;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGLoaderHelper;
import com.kitfox.svg.animation.parser.AnimTimeParser;
import com.kitfox.svg.xml.ColorTable;
import com.kitfox.svg.xml.StyleAttribute;
import com.kitfox.svg.xml.XMLParseUtil;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class Animate extends AnimateBase implements AnimateColorIface {
    public static final int DT_COLOR = 1;
    public static final int DT_PATH = 2;
    public static final int DT_REAL = 0;
    public static final String TAG_NAME = "animate";
    private double byValue = Double.NaN;
    int dataType = 0;
    private Color fromColor = null;
    private GeneralPath fromPath = null;
    private double fromValue = Double.NaN;
    private Color toColor = null;
    private GeneralPath toPath = null;
    private double toValue = Double.NaN;
    private double[] valuesValue;

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    public int getDataType() {
        return this.dataType;
    }

    @Override // com.kitfox.svg.animation.AnimationElement, com.kitfox.svg.SVGElement, com.kitfox.svg.animation.AnimateBase
    public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent) throws SAXException {
        super.loaderStartElement(helper, attrs, parent);
        String strn = attrs.getValue("from");
        if (strn != null) {
            if (XMLParseUtil.isDouble(strn)) {
                this.fromValue = XMLParseUtil.parseDouble(strn);
            } else {
                this.fromColor = ColorTable.parseColor(strn);
                if (this.fromColor == null) {
                    this.fromPath = buildPath(strn, 0);
                    this.dataType = 2;
                } else {
                    this.dataType = 1;
                }
            }
        }
        String strn2 = attrs.getValue("to");
        if (strn2 != null) {
            if (XMLParseUtil.isDouble(strn2)) {
                this.toValue = XMLParseUtil.parseDouble(strn2);
            } else {
                this.toColor = ColorTable.parseColor(strn2);
                if (this.toColor == null) {
                    this.toPath = buildPath(strn2, 0);
                    this.dataType = 2;
                } else {
                    this.dataType = 1;
                }
            }
        }
        String strn3 = attrs.getValue("by");
        if (strn3 != null) {
            try {
                this.byValue = XMLParseUtil.parseDouble(strn3);
            } catch (Exception e) {
            }
        }
        String strn4 = attrs.getValue("values");
        if (strn4 != null) {
            try {
                this.valuesValue = XMLParseUtil.parseDoubleList(strn4);
            } catch (Exception e2) {
            }
        }
    }

    public double eval(double interp) {
        boolean fromExists = !Double.isNaN(this.fromValue);
        boolean toExists = !Double.isNaN(this.toValue);
        boolean byExists = !Double.isNaN(this.byValue);
        if (this.valuesValue != null) {
            double sp = interp * ((double) this.valuesValue.length);
            int ip = (int) sp;
            double fp = sp - ((double) ip);
            int i1 = ip + 1;
            if (ip < 0) {
                return this.valuesValue[0];
            }
            if (i1 >= this.valuesValue.length) {
                return this.valuesValue[this.valuesValue.length - 1];
            }
            return (this.valuesValue[ip] * (1.0d - fp)) + (this.valuesValue[i1] * fp);
        } else if (fromExists && toExists) {
            return (this.toValue * interp) + (this.fromValue * (1.0d - interp));
        } else {
            if (fromExists && byExists) {
                return this.fromValue + (this.byValue * interp);
            }
            if (toExists && byExists) {
                return this.toValue - (this.byValue * (1.0d - interp));
            }
            if (byExists) {
                return this.byValue * interp;
            }
            if (toExists) {
                StyleAttribute style = new StyleAttribute(getAttribName());
                try {
                    getParent().getStyle(style, true, false);
                } catch (SVGException ex) {
                    Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not get from value", (Throwable) ex);
                }
                return (this.toValue * interp) + ((1.0d - interp) * style.getDoubleValue());
            }
            throw new RuntimeException("Animate tag could not be evalutated - insufficient arguements");
        }
    }

    @Override // com.kitfox.svg.animation.AnimateColorIface
    public Color evalColor(double interp) {
        if (this.fromColor == null && this.toColor != null) {
            float[] toCol = new float[3];
            this.toColor.getColorComponents(toCol);
            return new Color(toCol[0] * ((float) interp), toCol[1] * ((float) interp), toCol[2] * ((float) interp));
        } else if (this.fromColor == null || this.toColor == null) {
            throw new RuntimeException("Animate tag could not be evalutated - insufficient arguements");
        } else {
            float nInterp = 1.0f - ((float) interp);
            float[] fromCol = new float[3];
            float[] toCol2 = new float[3];
            this.fromColor.getColorComponents(fromCol);
            this.toColor.getColorComponents(toCol2);
            return new Color((fromCol[0] * nInterp) + (toCol2[0] * ((float) interp)), (fromCol[1] * nInterp) + (toCol2[1] * ((float) interp)), (fromCol[2] * nInterp) + (toCol2[2] * ((float) interp)));
        }
    }

    public GeneralPath evalPath(double interp) {
        GeneralPath midPath;
        if (this.fromPath == null && this.toPath != null) {
            PathIterator itTo = this.toPath.getPathIterator(new AffineTransform());
            midPath = new GeneralPath();
            float[] coordsTo = new float[6];
            while (!itTo.isDone()) {
                switch (itTo.currentSegment(coordsTo)) {
                    case 0:
                        midPath.moveTo((float) (((double) coordsTo[0]) * interp), (float) (((double) coordsTo[1]) * interp));
                        break;
                    case 1:
                        midPath.lineTo((float) (((double) coordsTo[0]) * interp), (float) (((double) coordsTo[1]) * interp));
                        break;
                    case 2:
                        midPath.quadTo((float) (((double) coordsTo[0]) * interp), (float) (((double) coordsTo[1]) * interp), (float) (((double) coordsTo[2]) * interp), (float) (((double) coordsTo[3]) * interp));
                        break;
                    case 3:
                        midPath.curveTo((float) (((double) coordsTo[0]) * interp), (float) (((double) coordsTo[1]) * interp), (float) (((double) coordsTo[2]) * interp), (float) (((double) coordsTo[3]) * interp), (float) (((double) coordsTo[4]) * interp), (float) (((double) coordsTo[5]) * interp));
                        break;
                    case 4:
                        midPath.closePath();
                        break;
                }
                itTo.next();
            }
        } else if (this.toPath != null) {
            PathIterator itFrom = this.fromPath.getPathIterator(new AffineTransform());
            PathIterator itTo2 = this.toPath.getPathIterator(new AffineTransform());
            midPath = new GeneralPath();
            float[] coordsFrom = new float[6];
            float[] coordsTo2 = new float[6];
            while (!itFrom.isDone()) {
                int segFrom = itFrom.currentSegment(coordsFrom);
                if (segFrom != itTo2.currentSegment(coordsTo2)) {
                    throw new RuntimeException("Path shape mismatch");
                }
                switch (segFrom) {
                    case 0:
                        midPath.moveTo((float) ((((double) coordsFrom[0]) * (1.0d - interp)) + (((double) coordsTo2[0]) * interp)), (float) ((((double) coordsFrom[1]) * (1.0d - interp)) + (((double) coordsTo2[1]) * interp)));
                        break;
                    case 1:
                        midPath.lineTo((float) ((((double) coordsFrom[0]) * (1.0d - interp)) + (((double) coordsTo2[0]) * interp)), (float) ((((double) coordsFrom[1]) * (1.0d - interp)) + (((double) coordsTo2[1]) * interp)));
                        break;
                    case 2:
                        midPath.quadTo((float) ((((double) coordsFrom[0]) * (1.0d - interp)) + (((double) coordsTo2[0]) * interp)), (float) ((((double) coordsFrom[1]) * (1.0d - interp)) + (((double) coordsTo2[1]) * interp)), (float) ((((double) coordsFrom[2]) * (1.0d - interp)) + (((double) coordsTo2[2]) * interp)), (float) ((((double) coordsFrom[3]) * (1.0d - interp)) + (((double) coordsTo2[3]) * interp)));
                        break;
                    case 3:
                        midPath.curveTo((float) ((((double) coordsFrom[0]) * (1.0d - interp)) + (((double) coordsTo2[0]) * interp)), (float) ((((double) coordsFrom[1]) * (1.0d - interp)) + (((double) coordsTo2[1]) * interp)), (float) ((((double) coordsFrom[2]) * (1.0d - interp)) + (((double) coordsTo2[2]) * interp)), (float) ((((double) coordsFrom[3]) * (1.0d - interp)) + (((double) coordsTo2[3]) * interp)), (float) ((((double) coordsFrom[4]) * (1.0d - interp)) + (((double) coordsTo2[4]) * interp)), (float) ((((double) coordsFrom[5]) * (1.0d - interp)) + (((double) coordsTo2[5]) * interp)));
                        break;
                    case 4:
                        midPath.closePath();
                        break;
                }
                itFrom.next();
                itTo2.next();
            }
        } else {
            throw new RuntimeException("Animate tag could not be evalutated - insufficient arguements");
        }
        return midPath;
    }

    public double repeatSkipSize(int reps) {
        boolean fromExists;
        boolean toExists;
        boolean byExists;
        if (!Double.isNaN(this.fromValue)) {
            fromExists = true;
        } else {
            fromExists = false;
        }
        if (!Double.isNaN(this.toValue)) {
            toExists = true;
        } else {
            toExists = false;
        }
        if (!Double.isNaN(this.byValue)) {
            byExists = true;
        } else {
            byExists = false;
        }
        if (fromExists && toExists) {
            return (this.toValue - this.fromValue) * ((double) reps);
        }
        if (fromExists && byExists) {
            return (this.fromValue + this.byValue) * ((double) reps);
        }
        if (toExists && byExists) {
            return this.toValue * ((double) reps);
        }
        if (byExists) {
            return this.byValue * ((double) reps);
        }
        return 0.0d;
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.animation.AnimationElement, com.kitfox.svg.animation.AnimateBase
    public void rebuild(AnimTimeParser animTimeParser) throws SVGException {
        String strn;
        String strn2;
        super.rebuild(animTimeParser);
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("from"))) {
            String strn3 = sty.getStringValue();
            if (XMLParseUtil.isDouble(strn3)) {
                this.fromValue = XMLParseUtil.parseDouble(strn3);
            } else {
                this.fromColor = ColorTable.parseColor(strn3);
                if (this.fromColor == null) {
                    this.fromPath = buildPath(strn3, 0);
                    this.dataType = 2;
                } else {
                    this.dataType = 1;
                }
            }
        }
        if (getPres(sty.setName("to"))) {
            String strn4 = sty.getStringValue();
            if (XMLParseUtil.isDouble(strn4)) {
                this.toValue = XMLParseUtil.parseDouble(strn4);
            } else {
                this.toColor = ColorTable.parseColor(strn4);
                if (this.toColor == null) {
                    this.toPath = buildPath(strn4, 0);
                    this.dataType = 2;
                } else {
                    this.dataType = 1;
                }
            }
        }
        if (getPres(sty.setName("by")) && (strn2 = sty.getStringValue()) != null) {
            this.byValue = XMLParseUtil.parseDouble(strn2);
        }
        if (getPres(sty.setName("values")) && (strn = sty.getStringValue()) != null) {
            this.valuesValue = XMLParseUtil.parseDoubleList(strn);
        }
    }

    public double getFromValue() {
        return this.fromValue;
    }

    public void setFromValue(double fromValue2) {
        this.fromValue = fromValue2;
    }

    public double getToValue() {
        return this.toValue;
    }

    public void setToValue(double toValue2) {
        this.toValue = toValue2;
    }

    public double getByValue() {
        return this.byValue;
    }

    public void setByValue(double byValue2) {
        this.byValue = byValue2;
    }

    public double[] getValuesValue() {
        return this.valuesValue;
    }

    public void setValuesValue(double[] valuesValue2) {
        this.valuesValue = valuesValue2;
    }

    public Color getFromColor() {
        return this.fromColor;
    }

    public void setFromColor(Color fromColor2) {
        this.fromColor = fromColor2;
    }

    public Color getToColor() {
        return this.toColor;
    }

    public void setToColor(Color toColor2) {
        this.toColor = toColor2;
    }

    public GeneralPath getFromPath() {
        return this.fromPath;
    }

    public void setFromPath(GeneralPath fromPath2) {
        this.fromPath = fromPath2;
    }

    public GeneralPath getToPath() {
        return this.toPath;
    }

    public void setToPath(GeneralPath toPath2) {
        this.toPath = toPath2;
    }
}
