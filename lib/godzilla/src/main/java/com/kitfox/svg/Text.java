package com.kitfox.svg;

import com.kitfox.svg.util.FontSystem;
import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class Text extends ShapeElement {
    public static final String TAG_NAME = "text";
    public static final int TXAN_END = 2;
    public static final int TXAN_MIDDLE = 1;
    public static final int TXAN_START = 0;
    public static final int TXST_ITALIC = 1;
    public static final int TXST_NORMAL = 0;
    public static final int TXST_OBLIQUE = 2;
    public static final int TXWE_100 = 4;
    public static final int TXWE_200 = 5;
    public static final int TXWE_300 = 6;
    public static final int TXWE_400 = 7;
    public static final int TXWE_500 = 8;
    public static final int TXWE_600 = 9;
    public static final int TXWE_700 = 10;
    public static final int TXWE_800 = 11;
    public static final int TXWE_900 = 12;
    public static final int TXWE_BOLD = 1;
    public static final int TXWE_BOLDER = 2;
    public static final int TXWE_LIGHTER = 3;
    public static final int TXWE_NORMAL = 0;
    LinkedList<Serializable> content = new LinkedList<>();
    String fontFamily;
    float fontSize;
    int fontStyle;
    int fontWeight;
    String lengthAdjust = "spacing";
    int textAnchor = 0;
    float textLength = -1.0f;
    Shape textShape;
    AffineTransform transform = null;
    float x = 0.0f;
    float y = 0.0f;

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    public void clearContent() {
        this.content.clear();
    }

    public void appendText(String text) {
        this.content.addLast(text);
    }

    public void appendTspan(Tspan tspan) throws SVGElementException {
        super.loaderAddChild(null, tspan);
        this.content.addLast(tspan);
    }

    public void rebuild() throws SVGException {
        build();
    }

    public List<Serializable> getContent() {
        return this.content;
    }

    @Override // com.kitfox.svg.SVGElement
    public void loaderAddChild(SVGLoaderHelper helper, SVGElement child) throws SVGElementException {
        super.loaderAddChild(helper, child);
        this.content.addLast(child);
    }

    @Override // com.kitfox.svg.SVGElement
    public void loaderAddText(SVGLoaderHelper helper, String text) {
        if (!Pattern.compile("\\s*").matcher(text).matches()) {
            this.content.addLast(text);
        }
    }

    @Override // com.kitfox.svg.RenderableElement, com.kitfox.svg.SVGElement, com.kitfox.svg.TransformableElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("x"))) {
            this.x = sty.getFloatValueWithUnits();
        }
        if (getPres(sty.setName("y"))) {
            this.y = sty.getFloatValueWithUnits();
        }
        if (getStyle(sty.setName("font-family"))) {
            this.fontFamily = sty.getStringValue();
        } else {
            this.fontFamily = "SansSerif";
        }
        if (getStyle(sty.setName("font-size"))) {
            this.fontSize = sty.getFloatValueWithUnits();
        } else {
            this.fontSize = 12.0f;
        }
        if (getStyle(sty.setName("textLength"))) {
            this.textLength = sty.getFloatValueWithUnits();
        } else {
            this.textLength = -1.0f;
        }
        if (getStyle(sty.setName("lengthAdjust"))) {
            this.lengthAdjust = sty.getStringValue();
        } else {
            this.lengthAdjust = "spacing";
        }
        if (getStyle(sty.setName("font-style"))) {
            String s = sty.getStringValue();
            if ("normal".equals(s)) {
                this.fontStyle = 0;
            } else if ("italic".equals(s)) {
                this.fontStyle = 1;
            } else if ("oblique".equals(s)) {
                this.fontStyle = 2;
            }
        } else {
            this.fontStyle = 0;
        }
        if (getStyle(sty.setName("font-weight"))) {
            String s2 = sty.getStringValue();
            if ("normal".equals(s2)) {
                this.fontWeight = 0;
            } else if ("bold".equals(s2)) {
                this.fontWeight = 1;
            }
        } else {
            this.fontWeight = 0;
        }
        if (getStyle(sty.setName("text-anchor"))) {
            String s3 = sty.getStringValue();
            if (s3.equals("middle")) {
                this.textAnchor = 1;
            } else if (s3.equals("end")) {
                this.textAnchor = 2;
            } else {
                this.textAnchor = 0;
            }
        } else {
            this.textAnchor = 0;
        }
        buildText();
    }

    /* access modifiers changed from: protected */
    public void buildText() throws SVGException {
        String[] families = this.fontFamily.split(",");
        Font font = null;
        int i = 0;
        while (i < families.length && (font = this.diagram.getUniverse().getFont(families[i])) == null) {
            i++;
        }
        if (font == null) {
            font = FontSystem.createFont(this.fontFamily, this.fontStyle, this.fontWeight, this.fontSize);
        }
        if (font == null) {
            Logger.getLogger(Text.class.getName()).log(Level.WARNING, "Could not create font " + this.fontFamily);
            font = FontSystem.createFont("Serif", this.fontStyle, this.fontWeight, this.fontSize);
        }
        GeneralPath textPath = new GeneralPath();
        this.textShape = textPath;
        float cursorX = this.x;
        float cursorY = this.y;
        AffineTransform xform = new AffineTransform();
        Iterator<Serializable> it = this.content.iterator();
        while (it.hasNext()) {
            Serializable obj = it.next();
            if (obj instanceof String) {
                String text = (String) obj;
                if (text != null) {
                    text = text.trim();
                }
                for (int i2 = 0; i2 < text.length(); i2++) {
                    xform.setToIdentity();
                    xform.setToTranslation((double) cursorX, (double) cursorY);
                    MissingGlyph glyph = font.getGlyph(text.substring(i2, i2 + 1));
                    Shape path = glyph.getPath();
                    if (path != null) {
                        textPath.append(xform.createTransformedShape(path), false);
                    }
                    cursorX += glyph.getHorizAdvX();
                }
                this.strokeWidthScalar = 1.0f;
            } else if (obj instanceof Tspan) {
                Point2D cursor = new Point2D.Float(cursorX, cursorY);
                ((Tspan) obj).appendToShape(textPath, cursor);
                cursorX = (float) cursor.getX();
                cursorY = (float) cursor.getY();
            }
        }
        switch (this.textAnchor) {
            case 1:
                AffineTransform at = new AffineTransform();
                at.translate((-textPath.getBounds().getWidth()) / 2.0d, 0.0d);
                textPath.transform(at);
                return;
            case 2:
                AffineTransform at2 = new AffineTransform();
                at2.translate(-textPath.getBounds().getWidth(), 0.0d);
                textPath.transform(at2);
                return;
            default:
                return;
        }
    }

    @Override // com.kitfox.svg.ShapeElement, com.kitfox.svg.RenderableElement
    public void render(Graphics2D g) throws SVGException {
        beginLayer(g);
        renderShape(g, this.textShape);
        finishLayer(g);
    }

    @Override // com.kitfox.svg.ShapeElement
    public Shape getShape() {
        return shapeToParent(this.textShape);
    }

    @Override // com.kitfox.svg.RenderableElement
    public Rectangle2D getBoundingBox() throws SVGException {
        return boundsToParent(includeStrokeInBounds(this.textShape.getBounds2D()));
    }

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.TransformableElement
    public boolean updateTime(double curTime) throws SVGException {
        boolean changeState = super.updateTime(curTime);
        StyleAttribute sty = new StyleAttribute();
        boolean shapeChange = false;
        if (getPres(sty.setName("x"))) {
            float newVal = sty.getFloatValueWithUnits();
            if (newVal != this.x) {
                this.x = newVal;
                shapeChange = true;
            }
        }
        if (getPres(sty.setName("y"))) {
            float newVal2 = sty.getFloatValueWithUnits();
            if (newVal2 != this.y) {
                this.y = newVal2;
                shapeChange = true;
            }
        }
        if (getStyle(sty.setName("textLength"))) {
            this.textLength = sty.getFloatValueWithUnits();
        } else {
            this.textLength = -1.0f;
        }
        if (getStyle(sty.setName("lengthAdjust"))) {
            this.lengthAdjust = sty.getStringValue();
        } else {
            this.lengthAdjust = "spacing";
        }
        if (getPres(sty.setName("font-family"))) {
            String newVal3 = sty.getStringValue();
            if (!newVal3.equals(this.fontFamily)) {
                this.fontFamily = newVal3;
                shapeChange = true;
            }
        }
        if (getPres(sty.setName("font-size"))) {
            float newVal4 = sty.getFloatValueWithUnits();
            if (newVal4 != this.fontSize) {
                this.fontSize = newVal4;
                shapeChange = true;
            }
        }
        if (getStyle(sty.setName("font-style"))) {
            String s = sty.getStringValue();
            int newVal5 = this.fontStyle;
            if ("normal".equals(s)) {
                newVal5 = 0;
            } else if ("italic".equals(s)) {
                newVal5 = 1;
            } else if ("oblique".equals(s)) {
                newVal5 = 2;
            }
            if (newVal5 != this.fontStyle) {
                this.fontStyle = newVal5;
                shapeChange = true;
            }
        }
        if (getStyle(sty.setName("font-weight"))) {
            String s2 = sty.getStringValue();
            int newVal6 = this.fontWeight;
            if ("normal".equals(s2)) {
                newVal6 = 0;
            } else if ("bold".equals(s2)) {
                newVal6 = 1;
            }
            if (newVal6 != this.fontWeight) {
                this.fontWeight = newVal6;
                shapeChange = true;
            }
        }
        if (shapeChange) {
            build();
        }
        if (changeState || shapeChange) {
            return true;
        }
        return false;
    }
}
