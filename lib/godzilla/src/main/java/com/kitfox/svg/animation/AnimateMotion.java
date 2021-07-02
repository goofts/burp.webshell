package com.kitfox.svg.animation;

import com.kitfox.svg.Path;
import com.kitfox.svg.SVGElement;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGLoaderHelper;
import com.kitfox.svg.animation.parser.AnimTimeParser;
import com.kitfox.svg.xml.StyleAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class AnimateMotion extends AnimateXform {
    public static final int RT_ANGLE = 0;
    public static final int RT_AUTO = 1;
    public static final String TAG_NAME = "animateMotion";
    static final Matcher matchPoint = Pattern.compile("\\s*(\\d+)[^\\d]+(\\d+)\\s*").matcher("");
    final ArrayList<Bezier> bezierSegs = new ArrayList<>();
    double curveLength;
    private GeneralPath path;
    private double rotate;
    private int rotateType = 0;

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    @Override // com.kitfox.svg.animation.AnimationElement, com.kitfox.svg.animation.AnimateXform, com.kitfox.svg.SVGElement, com.kitfox.svg.animation.AnimateBase
    public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent) throws SAXException {
        super.loaderStartElement(helper, attrs, parent);
        if (this.attribName == null) {
            this.attribName = "transform";
            this.attribType = 2;
            setAdditiveType(1);
        }
        String path2 = attrs.getValue(Path.TAG_NAME);
        if (path2 != null) {
            this.path = buildPath(path2, 1);
        }
        String rotate2 = attrs.getValue("rotate");
        if (rotate2 != null) {
            if (rotate2.equals("auto")) {
                this.rotateType = 1;
            } else {
                try {
                    this.rotate = Math.toRadians((double) Float.parseFloat(rotate2));
                } catch (Exception e) {
                }
            }
        }
        buildPath(attrs.getValue("from"), attrs.getValue("to"));
    }

    protected static void setPoint(Point2D.Float pt, String x, String y) {
        try {
            pt.x = Float.parseFloat(x);
        } catch (Exception e) {
        }
        try {
            pt.y = Float.parseFloat(y);
        } catch (Exception e2) {
        }
    }

    private void buildPath(String from, String to) {
        if (!(from == null || to == null)) {
            Point2D.Float ptFrom = new Point2D.Float();
            Point2D.Float ptTo = new Point2D.Float();
            matchPoint.reset(from);
            if (matchPoint.matches()) {
                setPoint(ptFrom, matchPoint.group(1), matchPoint.group(2));
            }
            matchPoint.reset(to);
            if (matchPoint.matches()) {
                setPoint(ptFrom, matchPoint.group(1), matchPoint.group(2));
            }
            if (!(ptFrom == null || ptTo == null)) {
                this.path = new GeneralPath();
                this.path.moveTo(ptFrom.x, ptFrom.y);
                this.path.lineTo(ptTo.x, ptTo.y);
            }
        }
        paramaterizePath();
    }

    private void paramaterizePath() {
        this.bezierSegs.clear();
        this.curveLength = 0.0d;
        double[] coords = new double[6];
        double sx = 0.0d;
        double sy = 0.0d;
        PathIterator pathIt = this.path.getPathIterator(new AffineTransform());
        while (!pathIt.isDone()) {
            Bezier bezier = null;
            switch (pathIt.currentSegment(coords)) {
                case 0:
                    sx = coords[0];
                    sy = coords[1];
                    break;
                case 1:
                    bezier = new Bezier(sx, sy, coords, 1);
                    sx = coords[0];
                    sy = coords[1];
                    break;
                case 2:
                    bezier = new Bezier(sx, sy, coords, 2);
                    sx = coords[2];
                    sy = coords[3];
                    break;
                case 3:
                    bezier = new Bezier(sx, sy, coords, 3);
                    sx = coords[4];
                    sy = coords[5];
                    break;
            }
            if (bezier != null) {
                this.bezierSegs.add(bezier);
                this.curveLength += bezier.getLength();
            }
            pathIt.next();
        }
    }

    @Override // com.kitfox.svg.animation.AnimateXform
    public AffineTransform eval(AffineTransform xform, double interp) {
        Point2D.Double point = new Point2D.Double();
        if (interp >= 1.0d) {
            this.bezierSegs.get(this.bezierSegs.size() - 1).getFinalPoint(point);
            xform.setToTranslation(point.x, point.y);
        } else {
            double curLength = this.curveLength * interp;
            Iterator<Bezier> it = this.bezierSegs.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Bezier bez = it.next();
                double bezLength = bez.getLength();
                if (curLength < bezLength) {
                    bez.eval(curLength / bezLength, point);
                    break;
                }
                curLength -= bezLength;
            }
            xform.setToTranslation(point.x, point.y);
        }
        return xform;
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.animation.AnimationElement, com.kitfox.svg.animation.AnimateBase
    public void rebuild(AnimTimeParser animTimeParser) throws SVGException {
        super.rebuild(animTimeParser);
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName(Path.TAG_NAME))) {
            this.path = buildPath(sty.getStringValue(), 1);
        }
        if (getPres(sty.setName("rotate"))) {
            String strn = sty.getStringValue();
            if (strn.equals("auto")) {
                this.rotateType = 1;
            } else {
                try {
                    this.rotate = Math.toRadians((double) Float.parseFloat(strn));
                } catch (Exception e) {
                }
            }
        }
        String from = null;
        if (getPres(sty.setName("from"))) {
            from = sty.getStringValue();
        }
        String to = null;
        if (getPres(sty.setName("to"))) {
            to = sty.getStringValue();
        }
        buildPath(from, to);
    }

    public GeneralPath getPath() {
        return this.path;
    }

    public void setPath(GeneralPath path2) {
        this.path = path2;
    }

    public int getRotateType() {
        return this.rotateType;
    }

    public void setRotateType(int rotateType2) {
        this.rotateType = rotateType2;
    }

    public double getRotate() {
        return this.rotate;
    }

    public void setRotate(double rotate2) {
        this.rotate = rotate2;
    }
}
