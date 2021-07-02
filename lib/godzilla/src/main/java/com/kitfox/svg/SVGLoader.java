package com.kitfox.svg;

import com.kitfox.svg.animation.Animate;
import com.kitfox.svg.animation.AnimateColor;
import com.kitfox.svg.animation.AnimateMotion;
import com.kitfox.svg.animation.AnimateTransform;
import com.kitfox.svg.animation.SetSmil;
import java.net.URI;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class SVGLoader extends DefaultHandler {
    final LinkedList<SVGElement> buildStack;
    final SVGDiagram diagram;
    final SVGLoaderHelper helper;
    final HashSet<String> ignoreClasses;
    int indent;
    final HashMap<String, Class<?>> nodeClasses;
    int skipNonSVGTagDepth;
    final boolean verbose;

    public SVGLoader(URI xmlBase, SVGUniverse universe) {
        this(xmlBase, universe, false);
    }

    public SVGLoader(URI xmlBase, SVGUniverse universe, boolean verbose2) {
        this.nodeClasses = new HashMap<>();
        this.buildStack = new LinkedList<>();
        this.ignoreClasses = new HashSet<>();
        this.skipNonSVGTagDepth = 0;
        this.indent = 0;
        this.verbose = verbose2;
        this.diagram = new SVGDiagram(xmlBase, universe);
        this.nodeClasses.put(A.TAG_NAME, A.class);
        this.nodeClasses.put(Animate.TAG_NAME, Animate.class);
        this.nodeClasses.put("animatecolor", AnimateColor.class);
        this.nodeClasses.put("animatemotion", AnimateMotion.class);
        this.nodeClasses.put("animatetransform", AnimateTransform.class);
        this.nodeClasses.put(Circle.TAG_NAME, Circle.class);
        this.nodeClasses.put(ClipPath.TAG_NAME, ClipPath.class);
        this.nodeClasses.put(Defs.TAG_NAME, Defs.class);
        this.nodeClasses.put(Desc.TAG_NAME, Desc.class);
        this.nodeClasses.put(Ellipse.TAG_NAME, Ellipse.class);
        this.nodeClasses.put(Filter.TAG_NAME, Filter.class);
        this.nodeClasses.put(Font.TAG_NAME, Font.class);
        this.nodeClasses.put("font-face", FontFace.class);
        this.nodeClasses.put("g", Group.class);
        this.nodeClasses.put("glyph", Glyph.class);
        this.nodeClasses.put(Hkern.TAG_NAME, Hkern.class);
        this.nodeClasses.put(ImageSVG.TAG_NAME, ImageSVG.class);
        this.nodeClasses.put(Line.TAG_NAME, Line.class);
        this.nodeClasses.put(LinearGradient.TAG_NAME, LinearGradient.class);
        this.nodeClasses.put(Marker.TAG_NAME, Marker.class);
        this.nodeClasses.put(Metadata.TAG_NAME, Metadata.class);
        this.nodeClasses.put("missing-glyph", MissingGlyph.class);
        this.nodeClasses.put(Path.TAG_NAME, Path.class);
        this.nodeClasses.put(PatternSVG.TAG_NAME, PatternSVG.class);
        this.nodeClasses.put(Polygon.TAG_NAME, Polygon.class);
        this.nodeClasses.put(Polyline.TAG_NAME, Polyline.class);
        this.nodeClasses.put(RadialGradient.TAG_NAME, RadialGradient.class);
        this.nodeClasses.put(Rect.TAG_NAME, Rect.class);
        this.nodeClasses.put(SetSmil.TAG_NAME, SetSmil.class);
        this.nodeClasses.put("shape", ShapeElement.class);
        this.nodeClasses.put(Stop.TAG_NAME, Stop.class);
        this.nodeClasses.put(Style.TAG_NAME, Style.class);
        this.nodeClasses.put(SVGRoot.TAG_NAME, SVGRoot.class);
        this.nodeClasses.put(Symbol.TAG_NAME, Symbol.class);
        this.nodeClasses.put(Text.TAG_NAME, Text.class);
        this.nodeClasses.put(Title.TAG_NAME, Title.class);
        this.nodeClasses.put(Tspan.TAG_NAME, Tspan.class);
        this.nodeClasses.put(Use.TAG_NAME, Use.class);
        this.ignoreClasses.add("midpointstop");
        this.helper = new SVGLoaderHelper(xmlBase, universe, this.diagram);
    }

    private String printIndent(int indent2, String indentStrn) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < indent2; i++) {
            sb.append(indentStrn);
        }
        return sb.toString();
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String sName, String qName, Attributes attrs) throws SAXException {
        if (this.verbose) {
            System.err.println(printIndent(this.indent, " ") + "Starting parse of tag " + sName + ": " + namespaceURI);
        }
        this.indent++;
        if (this.skipNonSVGTagDepth != 0 || (!namespaceURI.equals("") && !namespaceURI.equals(SVGElement.SVG_NS))) {
            this.skipNonSVGTagDepth++;
            return;
        }
        String sName2 = sName.toLowerCase();
        Object obj = this.nodeClasses.get(sName2);
        if (obj != null) {
            try {
                SVGElement svgEle = (SVGElement) ((Class) obj).newInstance();
                SVGElement parent = null;
                if (this.buildStack.size() != 0) {
                    parent = this.buildStack.getLast();
                }
                svgEle.loaderStartElement(this.helper, attrs, parent);
                this.buildStack.addLast(svgEle);
            } catch (Exception e) {
                Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not load", (Throwable) e);
                throw new SAXException(e);
            }
        } else if (!this.ignoreClasses.contains(sName2) && this.verbose) {
            System.err.println("SVGLoader: Could not identify tag '" + sName2 + "'");
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String namespaceURI, String sName, String qName) throws SAXException {
        this.indent--;
        if (this.verbose) {
            System.err.println(printIndent(this.indent, " ") + "Ending parse of tag " + sName + ": " + namespaceURI);
        }
        if (this.skipNonSVGTagDepth != 0) {
            this.skipNonSVGTagDepth--;
            return;
        }
        if (this.nodeClasses.get(sName.toLowerCase()) != null) {
            try {
                SVGElement svgEle = this.buildStack.removeLast();
                svgEle.loaderEndElement(this.helper);
                SVGElement parent = null;
                if (this.buildStack.size() != 0) {
                    parent = this.buildStack.getLast();
                }
                if (parent != null) {
                    parent.loaderAddChild(this.helper, svgEle);
                } else {
                    this.diagram.setRoot((SVGRoot) svgEle);
                }
            } catch (Exception e) {
                Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not parse", (Throwable) e);
                throw new SAXException(e);
            }
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] buf, int offset, int len) throws SAXException {
        if (this.skipNonSVGTagDepth == 0 && this.buildStack.size() != 0) {
            this.buildStack.getLast().loaderAddText(this.helper, new String(buf, offset, len));
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
    }

    public SVGDiagram getLoadedDiagram() {
        return this.diagram;
    }
}
