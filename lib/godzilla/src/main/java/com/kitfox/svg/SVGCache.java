package com.kitfox.svg;

public class SVGCache {
    private static final SVGUniverse svgUniverse = new SVGUniverse();

    private SVGCache() {
    }

    public static SVGUniverse getSVGUniverse() {
        return svgUniverse;
    }
}
