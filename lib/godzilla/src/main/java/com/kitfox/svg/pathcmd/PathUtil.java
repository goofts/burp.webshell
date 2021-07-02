package com.kitfox.svg.pathcmd;

import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

public class PathUtil {
    public static String buildPathString(GeneralPath path) {
        float[] coords = new float[6];
        StringBuffer sb = new StringBuffer();
        PathIterator pathIt = path.getPathIterator(new AffineTransform());
        while (!pathIt.isDone()) {
            switch (pathIt.currentSegment(coords)) {
                case 0:
                    sb.append(" M " + coords[0] + " " + coords[1]);
                    break;
                case 1:
                    sb.append(" L " + coords[0] + " " + coords[1]);
                    break;
                case 2:
                    sb.append(" Q " + coords[0] + " " + coords[1] + " " + coords[2] + " " + coords[3]);
                    break;
                case 3:
                    sb.append(" C " + coords[0] + " " + coords[1] + " " + coords[2] + " " + coords[3] + " " + coords[4] + " " + coords[5]);
                    break;
                case 4:
                    sb.append(" Z");
                    break;
            }
            pathIt.next();
        }
        return sb.toString();
    }
}
