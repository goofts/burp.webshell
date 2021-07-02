package com.kitfox.svg;

import com.kitfox.svg.animation.AnimationElement;
import com.kitfox.svg.animation.TrackBase;
import com.kitfox.svg.animation.TrackManager;
import com.kitfox.svg.pathcmd.Arc;
import com.kitfox.svg.pathcmd.BuildHistory;
import com.kitfox.svg.pathcmd.Cubic;
import com.kitfox.svg.pathcmd.CubicSmooth;
import com.kitfox.svg.pathcmd.Horizontal;
import com.kitfox.svg.pathcmd.LineTo;
import com.kitfox.svg.pathcmd.MoveTo;
import com.kitfox.svg.pathcmd.PathCommand;
import com.kitfox.svg.pathcmd.Quadratic;
import com.kitfox.svg.pathcmd.QuadraticSmooth;
import com.kitfox.svg.pathcmd.Terminal;
import com.kitfox.svg.pathcmd.Vertical;
import com.kitfox.svg.xml.StyleAttribute;
import com.kitfox.svg.xml.StyleSheet;
import com.kitfox.svg.xml.XMLParseUtil;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.io.Serializable;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javassist.bytecode.Opcode;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public abstract class SVGElement implements Serializable {
    private static final Pattern COMMAND_PATTERN = Pattern.compile("([MmLlHhVvAaQqTtCcSsZz])|([-+]?((\\d*\\.\\d+)|(\\d+))([eE][-+]?\\d+)?)");
    public static final String SVG_NS = "http://www.w3.org/2000/svg";
    private static final Pattern TRANSFORM_PATTERN = Pattern.compile("\\w+\\([^)]*\\)");
    private static final Pattern WORD_PATTERN = Pattern.compile("([a-zA-Z]+|-?\\d+(\\.\\d+)?(e-?\\d+)?|-?\\.\\d+(e-?\\d+)?)");
    public static final long serialVersionUID = 0;
    protected final ArrayList<SVGElement> children;
    LinkedList<SVGElement> contexts;
    protected String cssClass;
    protected SVGDiagram diagram;
    boolean dirty;
    protected String id;
    protected final HashMap<String, StyleAttribute> inlineStyles;
    protected SVGElement parent;
    protected final HashMap<String, StyleAttribute> presAttribs;
    protected final TrackManager trackManager;
    protected URI xmlBase;

    public abstract String getTagName();

    public abstract boolean updateTime(double d) throws SVGException;

    public SVGElement() {
        this(null, null, null);
    }

    public SVGElement(String id2, SVGElement parent2) {
        this(id2, null, parent2);
    }

    public SVGElement(String id2, String cssClass2, SVGElement parent2) {
        this.parent = null;
        this.children = new ArrayList<>();
        this.id = null;
        this.cssClass = null;
        this.inlineStyles = new HashMap<>();
        this.presAttribs = new HashMap<>();
        this.xmlBase = null;
        this.trackManager = new TrackManager();
        this.dirty = true;
        this.contexts = new LinkedList<>();
        this.id = id2;
        this.cssClass = cssClass2;
        this.parent = parent2;
    }

    public SVGElement getParent() {
        return this.parent;
    }

    /* access modifiers changed from: package-private */
    public void setParent(SVGElement parent2) {
        this.parent = parent2;
    }

    public List<SVGElement> getPath(List<SVGElement> retVec) {
        if (retVec == null) {
            retVec = new ArrayList<>();
        }
        if (this.parent != null) {
            this.parent.getPath(retVec);
        }
        retVec.add(this);
        return retVec;
    }

    public List<SVGElement> getChildren(List<SVGElement> retVec) {
        if (retVec == null) {
            retVec = new ArrayList<>();
        }
        retVec.addAll(this.children);
        return retVec;
    }

    public SVGElement getChild(String id2) {
        Iterator<SVGElement> it = this.children.iterator();
        while (it.hasNext()) {
            SVGElement ele = it.next();
            String eleId = ele.getId();
            if (eleId != null && eleId.equals(id2)) {
                return ele;
            }
        }
        return null;
    }

    public int indexOfChild(SVGElement child) {
        return this.children.indexOf(child);
    }

    public void swapChildren(int i, int j) throws SVGException {
        if (this.children != null && i >= 0 && i < this.children.size() && j >= 0 && j < this.children.size()) {
            this.children.set(i, this.children.get(j));
            this.children.set(j, this.children.get(i));
            build();
        }
    }

    public void loaderStartElement(SVGLoaderHelper helper, Attributes attrs, SVGElement parent2) throws SAXException {
        this.parent = parent2;
        this.diagram = helper.diagram;
        this.id = attrs.getValue("id");
        if (this.id != null && !this.id.equals("")) {
            this.id = this.id.intern();
            this.diagram.setElement(this.id, this);
        }
        String className = attrs.getValue("class");
        this.cssClass = (className == null || className.equals("")) ? null : className.intern();
        String style = attrs.getValue(Style.TAG_NAME);
        if (style != null) {
            XMLParseUtil.parseStyle(style, this.inlineStyles);
        }
        String base = attrs.getValue("xml:base");
        if (base != null && !base.equals("")) {
            try {
                this.xmlBase = new URI(base);
            } catch (Exception e) {
                throw new SAXException(e);
            }
        }
        int numAttrs = attrs.getLength();
        for (int i = 0; i < numAttrs; i++) {
            String name = attrs.getQName(i).intern();
            String value = attrs.getValue(i);
            this.presAttribs.put(name, new StyleAttribute(name, value == null ? null : value.intern()));
        }
    }

    public void removeAttribute(String name, int attribType) {
        switch (attribType) {
            case 0:
                this.inlineStyles.remove(name);
                return;
            case 1:
                this.presAttribs.remove(name);
                return;
            default:
                return;
        }
    }

    public void addAttribute(String name, int attribType, String value) throws SVGElementException {
        if (hasAttribute(name, attribType)) {
            throw new SVGElementException(this, "Attribute " + name + "(" + AnimationElement.animationElementToString(attribType) + ") already exists");
        }
        if ("id".equals(name)) {
            if (this.diagram != null) {
                this.diagram.removeElement(this.id);
                this.diagram.setElement(value, this);
            }
            this.id = value;
        }
        switch (attribType) {
            case 0:
                this.inlineStyles.put(name, new StyleAttribute(name, value));
                return;
            case 1:
                this.presAttribs.put(name, new StyleAttribute(name, value));
                return;
            default:
                throw new SVGElementException(this, "Invalid attribute type " + attribType);
        }
    }

    public boolean hasAttribute(String name, int attribType) throws SVGElementException {
        switch (attribType) {
            case 0:
                return this.inlineStyles.containsKey(name);
            case 1:
                return this.presAttribs.containsKey(name);
            case 2:
                return this.inlineStyles.containsKey(name) || this.presAttribs.containsKey(name);
            default:
                throw new SVGElementException(this, "Invalid attribute type " + attribType);
        }
    }

    public Set<String> getInlineAttributes() {
        return this.inlineStyles.keySet();
    }

    public Set<String> getPresentationAttributes() {
        return this.presAttribs.keySet();
    }

    public void loaderAddChild(SVGLoaderHelper helper, SVGElement child) throws SVGElementException {
        this.children.add(child);
        child.parent = this;
        child.setDiagram(this.diagram);
        if (child instanceof AnimationElement) {
            this.trackManager.addTrackElement((AnimationElement) child);
        }
    }

    /* access modifiers changed from: protected */
    public void setDiagram(SVGDiagram diagram2) {
        this.diagram = diagram2;
        diagram2.setElement(this.id, this);
        Iterator<SVGElement> it = this.children.iterator();
        while (it.hasNext()) {
            it.next().setDiagram(diagram2);
        }
    }

    public void removeChild(SVGElement child) throws SVGElementException {
        if (!this.children.contains(child)) {
            throw new SVGElementException(this, "Element does not contain child " + child);
        }
        this.children.remove(child);
    }

    public void loaderAddText(SVGLoaderHelper helper, String text) {
    }

    public void loaderEndElement(SVGLoaderHelper helper) throws SVGParseException {
    }

    /* access modifiers changed from: protected */
    public void build() throws SVGException {
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("id"))) {
            String newId = sty.getStringValue();
            if (!newId.equals(this.id)) {
                this.diagram.removeElement(this.id);
                this.id = newId;
                this.diagram.setElement(this.id, this);
            }
        }
        if (getPres(sty.setName("class"))) {
            this.cssClass = sty.getStringValue();
        }
        if (getPres(sty.setName("xml:base"))) {
            this.xmlBase = sty.getURIValue();
        }
        for (int i = 0; i < this.children.size(); i++) {
            this.children.get(i).build();
        }
    }

    public URI getXMLBase() {
        if (this.xmlBase != null) {
            return this.xmlBase;
        }
        return this.parent != null ? this.parent.getXMLBase() : this.diagram.getXMLBase();
    }

    public String getId() {
        return this.id;
    }

    /* access modifiers changed from: protected */
    public void pushParentContext(SVGElement context) {
        this.contexts.addLast(context);
    }

    /* access modifiers changed from: protected */
    public SVGElement popParentContext() {
        return this.contexts.removeLast();
    }

    /* access modifiers changed from: protected */
    public SVGElement getParentContext() {
        if (this.contexts.isEmpty()) {
            return null;
        }
        return this.contexts.getLast();
    }

    public SVGRoot getRoot() {
        if (this.parent == null) {
            return null;
        }
        return this.parent.getRoot();
    }

    public boolean getStyle(StyleAttribute attrib) throws SVGException {
        return getStyle(attrib, true);
    }

    public void setAttribute(String name, int attribType, String value) throws SVGElementException {
        StyleAttribute styAttr;
        switch (attribType) {
            case 0:
                styAttr = this.inlineStyles.get(name);
                break;
            case 1:
                styAttr = this.presAttribs.get(name);
                break;
            case 2:
                styAttr = this.inlineStyles.get(name);
                if (styAttr == null) {
                    styAttr = this.presAttribs.get(name);
                    break;
                }
                break;
            default:
                throw new SVGElementException(this, "Invalid attribute type " + attribType);
        }
        if (styAttr == null) {
            throw new SVGElementException(this, "Could not find attribute " + name + "(" + AnimationElement.animationElementToString(attribType) + ").  Make sure to create attribute before setting it.");
        }
        if ("id".equals(styAttr.getName())) {
            if (this.diagram != null) {
                this.diagram.removeElement(this.id);
                this.diagram.setElement(value, this);
            }
            this.id = value;
        }
        styAttr.setStringValue(value);
    }

    public boolean getStyle(StyleAttribute attrib, boolean recursive) throws SVGException {
        return getStyle(attrib, recursive, true);
    }

    public boolean getStyle(StyleAttribute attrib, boolean recursive, boolean evalAnimation) throws SVGException {
        StyleSheet ss;
        TrackBase track;
        TrackBase track2;
        String styName = attrib.getName();
        StyleAttribute styAttr = this.inlineStyles.get(styName);
        attrib.setStringValue(styAttr == null ? "" : styAttr.getStringValue());
        if (evalAnimation && (track2 = this.trackManager.getTrack(styName, 0)) != null) {
            track2.getValue(attrib, this.diagram.getUniverse().getCurTime());
            return true;
        } else if (styAttr != null) {
            return true;
        } else {
            StyleAttribute presAttr = this.presAttribs.get(styName);
            attrib.setStringValue(presAttr == null ? "" : presAttr.getStringValue());
            if (evalAnimation && (track = this.trackManager.getTrack(styName, 1)) != null) {
                track.getValue(attrib, this.diagram.getUniverse().getCurTime());
                return true;
            } else if (presAttr != null) {
                return true;
            } else {
                SVGRoot root = getRoot();
                if (root != null && (ss = root.getStyleSheet()) != null) {
                    return ss.getStyle(attrib, getTagName(), this.cssClass);
                }
                if (recursive) {
                    SVGElement parentContext = getParentContext();
                    if (parentContext != null) {
                        return parentContext.getStyle(attrib, true);
                    }
                    if (this.parent != null) {
                        return this.parent.getStyle(attrib, true);
                    }
                }
                return false;
            }
        }
    }

    public StyleAttribute getStyleAbsolute(String styName) {
        return this.inlineStyles.get(styName);
    }

    public boolean getPres(StyleAttribute attrib) throws SVGException {
        String presName = attrib.getName();
        StyleAttribute presAttr = this.presAttribs.get(presName);
        attrib.setStringValue(presAttr == null ? "" : presAttr.getStringValue());
        TrackBase track = this.trackManager.getTrack(presName, 1);
        if (track != null) {
            track.getValue(attrib, this.diagram.getUniverse().getCurTime());
            return true;
        } else if (presAttr != null) {
            return true;
        } else {
            return false;
        }
    }

    public StyleAttribute getPresAbsolute(String styName) {
        return this.presAttribs.get(styName);
    }

    protected static AffineTransform parseTransform(String val) throws SVGException {
        Matcher matchExpression = TRANSFORM_PATTERN.matcher("");
        AffineTransform retXform = new AffineTransform();
        matchExpression.reset(val);
        while (matchExpression.find()) {
            retXform.concatenate(parseSingleTransform(matchExpression.group()));
        }
        return retXform;
    }

    public static AffineTransform parseSingleTransform(String val) throws SVGException {
        Matcher matchWord = WORD_PATTERN.matcher("");
        AffineTransform retXform = new AffineTransform();
        matchWord.reset(val);
        if (matchWord.find()) {
            String function = matchWord.group().toLowerCase();
            LinkedList<String> termList = new LinkedList<>();
            while (matchWord.find()) {
                termList.add(matchWord.group());
            }
            double[] terms = new double[termList.size()];
            Iterator<String> it = termList.iterator();
            int count = 0;
            while (it.hasNext()) {
                terms[count] = XMLParseUtil.parseDouble(it.next());
                count++;
            }
            if (function.equals("matrix")) {
                retXform.setTransform(terms[0], terms[1], terms[2], terms[3], terms[4], terms[5]);
            } else if (function.equals("translate")) {
                if (terms.length == 1) {
                    retXform.setToTranslation(terms[0], 0.0d);
                } else {
                    retXform.setToTranslation(terms[0], terms[1]);
                }
            } else if (function.equals("scale")) {
                if (terms.length > 1) {
                    retXform.setToScale(terms[0], terms[1]);
                } else {
                    retXform.setToScale(terms[0], terms[0]);
                }
            } else if (function.equals("rotate")) {
                if (terms.length > 2) {
                    retXform.setToRotation(Math.toRadians(terms[0]), terms[1], terms[2]);
                } else {
                    retXform.setToRotation(Math.toRadians(terms[0]));
                }
            } else if (function.equals("skewx")) {
                retXform.setToShear(Math.toRadians(terms[0]), 0.0d);
            } else if (function.equals("skewy")) {
                retXform.setToShear(0.0d, Math.toRadians(terms[0]));
            } else {
                throw new SVGException("Unknown transform type");
            }
        }
        return retXform;
    }

    protected static float nextFloat(LinkedList<String> l) {
        return Float.parseFloat(l.removeFirst());
    }

    protected static PathCommand[] parsePathList(String list) {
        PathCommand cmd;
        Matcher matchPathCmd = COMMAND_PATTERN.matcher(list);
        LinkedList<String> tokens = new LinkedList<>();
        while (matchPathCmd.find()) {
            tokens.addLast(matchPathCmd.group());
        }
        LinkedList<PathCommand> cmdList = new LinkedList<>();
        char curCmd = 'Z';
        while (tokens.size() != 0) {
            String curToken = tokens.removeFirst();
            char initChar = curToken.charAt(0);
            if ((initChar < 'A' || initChar > 'Z') && (initChar < 'a' || initChar > 'z')) {
                tokens.addFirst(curToken);
            } else {
                curCmd = initChar;
            }
            switch (curCmd) {
                case 'A':
                    cmd = new Arc(false, nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens) == 1.0f, nextFloat(tokens) == 1.0f, nextFloat(tokens), nextFloat(tokens));
                    break;
                case 'C':
                    cmd = new Cubic(false, nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens));
                    break;
                case Opcode.DSTORE_1 /*{ENCODED_INT: 72}*/:
                    cmd = new Horizontal(false, nextFloat(tokens));
                    break;
                case 'L':
                    cmd = new LineTo(false, nextFloat(tokens), nextFloat(tokens));
                    break;
                case Opcode.ASTORE_2 /*{ENCODED_INT: 77}*/:
                    cmd = new MoveTo(false, nextFloat(tokens), nextFloat(tokens));
                    curCmd = 'L';
                    break;
                case Opcode.FASTORE /*{ENCODED_INT: 81}*/:
                    cmd = new Quadratic(false, nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens));
                    break;
                case Opcode.AASTORE /*{ENCODED_INT: 83}*/:
                    cmd = new CubicSmooth(false, nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens));
                    break;
                case Opcode.BASTORE /*{ENCODED_INT: 84}*/:
                    cmd = new QuadraticSmooth(false, nextFloat(tokens), nextFloat(tokens));
                    break;
                case Opcode.SASTORE /*{ENCODED_INT: 86}*/:
                    cmd = new Vertical(false, nextFloat(tokens));
                    break;
                case Opcode.DUP_X1 /*{ENCODED_INT: 90}*/:
                case Opcode.ISHR /*{ENCODED_INT: 122}*/:
                    cmd = new Terminal();
                    break;
                case Opcode.LADD /*{ENCODED_INT: 97}*/:
                    cmd = new Arc(true, nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens) == 1.0f, nextFloat(tokens) == 1.0f, nextFloat(tokens), nextFloat(tokens));
                    break;
                case Opcode.DADD /*{ENCODED_INT: 99}*/:
                    cmd = new Cubic(true, nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens));
                    break;
                case 'h':
                    cmd = new Horizontal(true, nextFloat(tokens));
                    break;
                case Opcode.IDIV /*{ENCODED_INT: 108}*/:
                    cmd = new LineTo(true, nextFloat(tokens), nextFloat(tokens));
                    break;
                case Opcode.LDIV /*{ENCODED_INT: 109}*/:
                    cmd = new MoveTo(true, nextFloat(tokens), nextFloat(tokens));
                    curCmd = 'l';
                    break;
                case Opcode.LREM /*{ENCODED_INT: 113}*/:
                    cmd = new Quadratic(true, nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens));
                    break;
                case Opcode.DREM /*{ENCODED_INT: 115}*/:
                    cmd = new CubicSmooth(true, nextFloat(tokens), nextFloat(tokens), nextFloat(tokens), nextFloat(tokens));
                    break;
                case Opcode.INEG /*{ENCODED_INT: 116}*/:
                    cmd = new QuadraticSmooth(true, nextFloat(tokens), nextFloat(tokens));
                    break;
                case Opcode.FNEG /*{ENCODED_INT: 118}*/:
                    cmd = new Vertical(true, nextFloat(tokens));
                    break;
                default:
                    throw new RuntimeException("Invalid path element");
            }
            cmdList.add(cmd);
            boolean defaultRelative = cmd.isRelative;
        }
        PathCommand[] retArr = new PathCommand[cmdList.size()];
        cmdList.toArray(retArr);
        return retArr;
    }

    protected static GeneralPath buildPath(String text, int windingRule) {
        PathCommand[] commands = parsePathList(text);
        int numKnots = 2;
        for (PathCommand pathCommand : commands) {
            numKnots += pathCommand.getNumKnotsAdded();
        }
        GeneralPath path = new GeneralPath(windingRule, numKnots);
        BuildHistory hist = new BuildHistory();
        for (PathCommand cmd : commands) {
            cmd.appendPath(path, hist);
        }
        return path;
    }

    public int getNumChildren() {
        return this.children.size();
    }

    public SVGElement getChild(int i) {
        return this.children.get(i);
    }

    public double lerp(double t0, double t1, double alpha) {
        return ((1.0d - alpha) * t0) + (alpha * t1);
    }
}
