package com.kitfox.svg;

import com.kitfox.svg.xml.StyleAttribute;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.net.URI;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class Gradient extends FillElement {
    public static final int GU_OBJECT_BOUNDING_BOX = 0;
    public static final int GU_USER_SPACE_ON_USE = 1;
    public static final int SM_PAD = 0;
    public static final int SM_REFLECT = 2;
    public static final int SM_REPEAT = 1;
    public static final String TAG_NAME = "gradient";
    protected AffineTransform gradientTransform = null;
    protected int gradientUnits = 0;
    int spreadMethod = 0;
    Color[] stopColors;
    float[] stopFractions;
    URI stopRef = null;
    ArrayList<Stop> stops = new ArrayList<>();

    @Override // com.kitfox.svg.SVGElement
    public String getTagName() {
        return TAG_NAME;
    }

    @Override // com.kitfox.svg.SVGElement
    public void loaderAddChild(SVGLoaderHelper helper, SVGElement child) throws SVGElementException {
        super.loaderAddChild(helper, child);
        if (child instanceof Stop) {
            appendStop((Stop) child);
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.kitfox.svg.SVGElement
    public void build() throws SVGException {
        super.build();
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("spreadMethod"))) {
            String strn = sty.getStringValue().toLowerCase();
            if (strn.equals("repeat")) {
                this.spreadMethod = 1;
            } else if (strn.equals("reflect")) {
                this.spreadMethod = 2;
            } else {
                this.spreadMethod = 0;
            }
        }
        if (getPres(sty.setName("gradientUnits"))) {
            if (sty.getStringValue().toLowerCase().equals("userspaceonuse")) {
                this.gradientUnits = 1;
            } else {
                this.gradientUnits = 0;
            }
        }
        if (getPres(sty.setName("gradientTransform"))) {
            this.gradientTransform = parseTransform(sty.getStringValue());
        }
        if (this.gradientTransform == null) {
            this.gradientTransform = new AffineTransform();
        }
        if (getPres(sty.setName("xlink:href"))) {
            try {
                this.stopRef = sty.getURIValue(getXMLBase());
            } catch (Exception e) {
                throw new SVGException("Could not resolve relative URL in Gradient: " + sty.getStringValue() + ", " + getXMLBase(), e);
            }
        }
    }

    private void buildStops() {
        ArrayList<Stop> stopList = new ArrayList<>(this.stops);
        stopList.sort(new Comparator<Stop>() {
            /* class com.kitfox.svg.Gradient.AnonymousClass1 */

            public int compare(Stop o1, Stop o2) {
                return Float.compare(o1.offset, o2.offset);
            }
        });
        for (int i = stopList.size() - 2; i > 0; i--) {
            if (stopList.get(i + 1).offset == stopList.get(i).offset) {
                stopList.remove(i + 1);
            }
        }
        this.stopFractions = new float[stopList.size()];
        this.stopColors = new Color[stopList.size()];
        int idx = 0;
        Iterator<Stop> it = stopList.iterator();
        while (it.hasNext()) {
            Stop stop = it.next();
            int stopColorVal = stop.color.getRGB();
            this.stopColors[idx] = new Color((stopColorVal >> 16) & 255, (stopColorVal >> 8) & 255, stopColorVal & 255, clamp((int) (stop.opacity * 255.0f), 0, 255));
            this.stopFractions[idx] = stop.offset;
            idx++;
        }
    }

    public float[] getStopFractions() {
        if (this.stopRef != null) {
            return ((Gradient) this.diagram.getUniverse().getElement(this.stopRef)).getStopFractions();
        }
        if (this.stopFractions != null) {
            return this.stopFractions;
        }
        buildStops();
        return this.stopFractions;
    }

    public Color[] getStopColors() {
        if (this.stopRef != null) {
            return ((Gradient) this.diagram.getUniverse().getElement(this.stopRef)).getStopColors();
        }
        if (this.stopColors != null) {
            return this.stopColors;
        }
        buildStops();
        return this.stopColors;
    }

    private int clamp(int val, int min, int max) {
        if (val < min) {
            return min;
        }
        return val > max ? max : val;
    }

    public void setStopRef(URI grad) {
        this.stopRef = grad;
    }

    public void appendStop(Stop stop) {
        this.stops.add(stop);
    }

    @Override // com.kitfox.svg.SVGElement
    public boolean updateTime(double curTime) throws SVGException {
        AffineTransform newVal;
        int newVal2;
        int newVal3;
        boolean stateChange = false;
        StyleAttribute sty = new StyleAttribute();
        if (getPres(sty.setName("spreadMethod"))) {
            String strn = sty.getStringValue().toLowerCase();
            if (strn.equals("repeat")) {
                newVal3 = 1;
            } else if (strn.equals("reflect")) {
                newVal3 = 2;
            } else {
                newVal3 = 0;
            }
            if (this.spreadMethod != newVal3) {
                this.spreadMethod = newVal3;
                stateChange = true;
            }
        }
        if (getPres(sty.setName("gradientUnits"))) {
            if (sty.getStringValue().toLowerCase().equals("userspaceonuse")) {
                newVal2 = 1;
            } else {
                newVal2 = 0;
            }
            if (newVal2 != this.gradientUnits) {
                this.gradientUnits = newVal2;
                stateChange = true;
            }
        }
        if (getPres(sty.setName("gradientTransform")) && (newVal = parseTransform(sty.getStringValue())) != null && newVal.equals(this.gradientTransform)) {
            this.gradientTransform = newVal;
            stateChange = true;
        }
        if (getPres(sty.setName("xlink:href"))) {
            try {
                URI newVal4 = sty.getURIValue(getXMLBase());
                if ((newVal4 == null && this.stopRef != null) || !newVal4.equals(this.stopRef)) {
                    this.stopRef = newVal4;
                    stateChange = true;
                }
            } catch (Exception e) {
                Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not parse xlink:href", (Throwable) e);
            }
        }
        Iterator<Stop> it = this.stops.iterator();
        while (it.hasNext()) {
            if (it.next().updateTime(curTime)) {
                stateChange = true;
                this.stopFractions = null;
                this.stopColors = null;
            }
        }
        return stateChange;
    }
}
