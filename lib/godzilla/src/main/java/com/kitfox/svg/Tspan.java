package com.kitfox.svg;

import com.kitfox.svg.util.FontSystem;
import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class Tspan extends ShapeElement {
    public static final String TAG_NAME = "tspan";
    float[] dx = null;
    float[] dy = null;
    float[] rotate = null;
    private String text = "";
    float[] x = null;
    float[] y = null;

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    @Override // com.kitfox.svg.SVGElement
    public void loaderAddText(SVGLoaderHelper helper, String text2) {
        this.text += text2;
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.RenderableElement, com.kitfox.svg.SVGElement, com.kitfox.svg.TransformableElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("x"))) {
            this.x = sty.getFloatList();
        }
        if (getPres(sty.setName("y"))) {
            this.y = sty.getFloatList();
        }
        if (getPres(sty.setName("dx"))) {
            this.dx = sty.getFloatList();
        }
        if (getPres(sty.setName("dy"))) {
            this.dy = sty.getFloatList();
        }
        if (getPres(sty.setName("rotate"))) {
            this.rotate = sty.getFloatList();
            for (int i = 0; i < this.rotate.length; i++) {
                this.rotate[i] = (float) Math.toRadians((double) this.rotate[i]);
            }
        }
    }

    public void appendToShape(GeneralPath addShape, Point2D cursor) throws SVGException {
        StyleAttribute sty = new StyleAttribute();
        String fontFamily = null;
        if (getStyle(sty.setName("font-family"))) {
            fontFamily = sty.getStringValue();
        }
        float fontSize = 12.0f;
        if (getStyle(sty.setName("font-size"))) {
            fontSize = sty.getFloatValueWithUnits();
        }
        float letterSpacing = 0.0f;
        if (getStyle(sty.setName("letter-spacing"))) {
            letterSpacing = sty.getFloatValueWithUnits();
        }
        int fontStyle = 0;
        if (getStyle(sty.setName("font-style"))) {
            String s = sty.getStringValue();
            if ("normal".equals(s)) {
                fontStyle = 0;
            } else if ("italic".equals(s)) {
                fontStyle = 1;
            } else if ("oblique".equals(s)) {
                fontStyle = 2;
            }
        } else {
            fontStyle = 0;
        }
        int fontWeight = 0;
        if (getStyle(sty.setName("font-weight"))) {
            String s2 = sty.getStringValue();
            if ("normal".equals(s2)) {
                fontWeight = 0;
            } else if ("bold".equals(s2)) {
                fontWeight = 1;
            }
        } else {
            fontWeight = 0;
        }
        Font font = this.diagram.getUniverse().getFont(fontFamily);
        if (font == null && fontFamily != null) {
            font = FontSystem.createFont(fontFamily, fontStyle, fontWeight, fontSize);
        }
        if (font == null) {
            font = FontSystem.createFont("Serif", fontStyle, fontWeight, fontSize);
        }
        AffineTransform xform = new AffineTransform();
        float cursorX = (float) cursor.getX();
        float cursorY = (float) cursor.getY();
        String drawText = this.text.trim();
        for (int i = 0; i < drawText.length(); i++) {
            if (this.x != null && i < this.x.length) {
                cursorX = this.x[i];
            } else if (this.dx != null && i < this.dx.length) {
                cursorX += this.dx[i];
            }
            if (this.y != null && i < this.y.length) {
                cursorY = this.y[i];
            } else if (this.dy != null && i < this.dy.length) {
                cursorY += this.dy[i];
            }
            xform.setToIdentity();
            xform.setToTranslation((double) cursorX, (double) cursorY);
            if (this.rotate != null) {
                xform.rotate((double) this.rotate[i]);
            }
            MissingGlyph glyph = font.getGlyph(drawText.substring(i, i + 1));
            Shape path = glyph.getPath();
            if (path != null) {
                addShape.append(xform.createTransformedShape(path), false);
            }
            cursorX += glyph.getHorizAdvX() + letterSpacing;
        }
        cursor.setLocation((double) cursorX, (double) cursorY);
        this.strokeWidthScalar = 1.0f;
    }

    @Override // com.kitfox.svg.ShapeElement, com.kitfox.svg.RenderableElement
    public void render(Graphics2D g) throws SVGException {
        float cursorX = 0.0f;
        float cursorY = 0.0f;
        if (this.x != null) {
            cursorX = this.x[0];
            cursorY = this.y[0];
        } else if (this.dx != null) {
            cursorX = 0.0f + this.dx[0];
            cursorY = 0.0f + this.dy[0];
        }
        StyleAttribute sty = new StyleAttribute();
        String fontFamily = null;
        if (getPres(sty.setName("font-family"))) {
            fontFamily = sty.getStringValue();
        }
        float fontSize = 12.0f;
        if (getPres(sty.setName("font-size"))) {
            fontSize = sty.getFloatValueWithUnits();
        }
        Font font = this.diagram.getUniverse().getFont(fontFamily);
        if (font == null) {
            System.err.println("Could not load font");
            renderSysFont(g, new Font(fontFamily, 0, (int) fontSize));
            return;
        }
        float fontScale = fontSize / ((float) font.getFontFace().getAscent());
        AffineTransform oldXform = g.getTransform();
        AffineTransform xform = new AffineTransform();
        this.strokeWidthScalar = 1.0f / fontScale;
        int posPtr = 1;
        for (int i = 0; i < this.text.length(); i++) {
            xform.setToTranslation((double) cursorX, (double) cursorY);
            xform.scale((double) fontScale, (double) fontScale);
            g.transform(xform);
            MissingGlyph glyph = font.getGlyph(this.text.substring(i, i + 1));
            Shape path = glyph.getPath();
            if (path != null) {
                renderShape(g, path);
            } else {
                glyph.render(g);
            }
            if (this.x != null && posPtr < this.x.length) {
                cursorX = this.x[posPtr];
                cursorY = this.y[posPtr];
                posPtr++;
            } else if (this.dx != null && posPtr < this.dx.length) {
                cursorX += this.dx[posPtr];
                cursorY += this.dy[posPtr];
                posPtr++;
            }
            cursorX += glyph.getHorizAdvX() * fontScale;
            g.setTransform(oldXform);
        }
        this.strokeWidthScalar = 1.0f;
    }

    /* access modifiers changed from: protected */
    public void renderSysFont(Graphics2D g, Font font) throws SVGException {
        FontRenderContext frc = g.getFontRenderContext();
        renderShape(g, font.createGlyphVector(frc, this.text).getOutline(0.0f, 0.0f));
        float cursorX = 0.0f + ((float) font.getStringBounds(this.text, frc).getWidth());
    }

    @Override // com.kitfox.svg.ShapeElement
    public Shape getShape() {
        return null;
    }

    @Override // com.kitfox.svg.RenderableElement
    public Rectangle2D getBoundingBox() {
        return null;
    }

    @Override // com.kitfox.svg.SVGElement, com.kitfox.svg.TransformableElement
    public boolean updateTime(double curTime) throws SVGException {
        return false;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text2) {
        this.text = text2;
    }
}
