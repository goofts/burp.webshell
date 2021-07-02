package com.kitfox.svg.util;

import com.kitfox.svg.Font;
import com.kitfox.svg.FontFace;
import com.kitfox.svg.Glyph;
import com.kitfox.svg.MissingGlyph;
import java.awt.Canvas;
import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphMetrics;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

public class FontSystem extends Font {
    static HashSet<String> sysFontNames = new HashSet<>();
    FontMetrics fm;
    HashMap<String, Glyph> glyphCache = new HashMap<>();
    java.awt.Font sysFont;

    public static boolean checkIfSystemFontExists(String fontName) {
        if (sysFontNames.isEmpty()) {
            for (String name : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(Locale.ENGLISH)) {
                sysFontNames.add(name);
            }
        }
        return sysFontNames.contains(fontName);
    }

    public static FontSystem createFont(String fontFamily, int fontStyle, int fontWeight, float fontSize) {
        for (String fontName : fontFamily.split(",")) {
            String javaFontName = mapJavaFontName(fontName);
            if (checkIfSystemFontExists(javaFontName)) {
                return new FontSystem(javaFontName, fontStyle, fontWeight, fontSize);
            }
        }
        return null;
    }

    private static String mapJavaFontName(String fontName) {
        if ("serif".equals(fontName)) {
            return "Serif";
        }
        if ("sans-serif".equals(fontName)) {
            return "SansSerif";
        }
        if ("monospace".equals(fontName)) {
            return "Monospaced";
        }
        return fontName;
    }

    private FontSystem(String fontFamily, int fontStyle, int fontWeight, float fontSize) {
        int style;
        int weight;
        switch (fontStyle) {
            case 1:
                style = 2;
                break;
            default:
                style = 0;
                break;
        }
        switch (fontWeight) {
            case 1:
            case 2:
                weight = 1;
                break;
            default:
                weight = 0;
                break;
        }
        this.sysFont = new java.awt.Font(fontFamily, style | weight, 1).deriveFont(fontSize);
        this.fm = new Canvas().getFontMetrics(this.sysFont);
        FontFace face = new FontFace();
        face.setAscent(this.fm.getAscent());
        face.setDescent(this.fm.getDescent());
        face.setUnitsPerEm(this.fm.charWidth('M'));
        setFontFace(face);
    }

    @Override // com.kitfox.svg.Font
    public MissingGlyph getGlyph(String unicode) {
        GlyphVector vec = this.sysFont.createGlyphVector(new FontRenderContext((AffineTransform) null, true, true), unicode);
        Glyph glyph = this.glyphCache.get(unicode);
        if (glyph != null) {
            return glyph;
        }
        Glyph glyph2 = new Glyph();
        glyph2.setPath(vec.getGlyphOutline(0));
        GlyphMetrics gm = vec.getGlyphMetrics(0);
        glyph2.setHorizAdvX(gm.getAdvanceX());
        glyph2.setVertAdvY(gm.getAdvanceY());
        glyph2.setVertOriginX(0.0f);
        glyph2.setVertOriginY(0.0f);
        this.glyphCache.put(unicode, glyph2);
        return glyph2;
    }
}
