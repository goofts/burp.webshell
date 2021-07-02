package com.kitfox.svg;

import com.kitfox.svg.animation.parser.AnimTimeParser;
import java.io.StringReader;
import java.net.URI;

public class SVGLoaderHelper {
    public final AnimTimeParser animTimeParser = new AnimTimeParser(new StringReader(""));
    public final SVGDiagram diagram;
    public final SVGUniverse universe;
    public final URI xmlBase;

    public SVGLoaderHelper(URI xmlBase2, SVGUniverse universe2, SVGDiagram diagram2) {
        this.xmlBase = xmlBase2;
        this.universe = universe2;
        this.diagram = diagram2;
    }
}
