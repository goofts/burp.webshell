package com.kitfox.svg.pathcmd;

import java.awt.geom.GeneralPath;

public class Terminal extends PathCommand {
    public String toString() {
        return "Z";
    }

    @Override // com.kitfox.svg.pathcmd.PathCommand
    public void appendPath(GeneralPath path, BuildHistory hist) {
        path.closePath();
        hist.setLastPoint(hist.startPoint.x, hist.startPoint.y);
        hist.setLastKnot(hist.startPoint.x, hist.startPoint.y);
    }

    @Override // com.kitfox.svg.pathcmd.PathCommand
    public int getNumKnotsAdded() {
        return 0;
    }
}
