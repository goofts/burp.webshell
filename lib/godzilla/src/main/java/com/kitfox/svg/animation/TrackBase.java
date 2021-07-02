package com.kitfox.svg.animation;

import com.kitfox.svg.SVGElement;
import com.kitfox.svg.SVGElementException;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.xml.StyleAttribute;
import java.util.ArrayList;

public abstract class TrackBase {
    final ArrayList<AnimationElement> animEvents;
    protected final String attribName;
    protected final int attribType;
    protected final SVGElement parent;

    public abstract boolean getValue(StyleAttribute styleAttribute, double d) throws SVGException;

    public TrackBase(SVGElement parent2, AnimationElement ele) throws SVGElementException {
        this(parent2, ele.getAttribName(), ele.getAttribType());
    }

    public TrackBase(SVGElement parent2, String attribName2, int attribType2) throws SVGElementException {
        this.animEvents = new ArrayList<>();
        this.parent = parent2;
        this.attribName = attribName2;
        this.attribType = attribType2;
        if (attribType2 == 2 && !parent2.hasAttribute(attribName2, 0) && !parent2.hasAttribute(attribName2, 1)) {
            parent2.addAttribute(attribName2, 0, "");
        } else if (!parent2.hasAttribute(attribName2, attribType2)) {
            parent2.addAttribute(attribName2, attribType2, "");
        }
    }

    public String getAttribName() {
        return this.attribName;
    }

    public int getAttribType() {
        return this.attribType;
    }

    public void addElement(AnimationElement ele) {
        this.animEvents.add(ele);
    }
}
