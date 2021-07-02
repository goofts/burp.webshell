package com.kitfox.svg.animation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class TimeBase {
    static final Matcher matchIndefinite = Pattern.compile("\\s*indefinite\\s*").matcher("");
    static final Matcher matchUnitTime = Pattern.compile("\\s*([-+]?((\\d*\\.\\d+)|(\\d+))([-+]?[eE]\\d+)?)\\s*(h|min|s|ms)?\\s*").matcher("");

    public abstract double evalTime();

    protected static TimeBase parseTimeComponent(String text) {
        matchIndefinite.reset(text);
        if (matchIndefinite.matches()) {
            return new TimeIndefinite();
        }
        matchUnitTime.reset(text);
        if (!matchUnitTime.matches()) {
            return null;
        }
        String val = matchUnitTime.group(1);
        String units = matchUnitTime.group(6);
        double time = 0.0d;
        try {
            time = Double.parseDouble(val);
        } catch (Exception e) {
        }
        if (units.equals("ms")) {
            time *= 0.001d;
        } else if (units.equals("min")) {
            time *= 60.0d;
        } else if (units.equals("h")) {
            time *= 3600.0d;
        }
        return new TimeDiscrete(time);
    }

    public void setParentElement(AnimationElement ele) {
    }
}
