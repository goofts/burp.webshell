package com.kitfox.svg.pathcmd;

import java.awt.geom.GeneralPath;

public class Horizontal extends PathCommand {
    public float x = 0.0f;

    public Horizontal() {
    }

    public String toString() {
        return "H " + this.x;
    }

    public Horizontal(boolean isRelative, float x2) {
        super(isRelative);
        this.x = x2;
    }

    @Override // com.kitfox.svg.pathcmd.PathCommand
    public void appendPath(GeneralPath path, BuildHistory hist) {
        float offx = this.isRelative ? hist.lastPoint.x : 0.0f;
        float offy = hist.lastPoint.y;
        path.lineTo(this.x + offx, offy);
        hist.setLastPoint(this.x + offx, offy);
        hist.setLastKnot(this.x + offx, offy);
    }

    @Override // com.kitfox.svg.pathcmd.PathCommand
    public int getNumKnotsAdded() {
        return 2;
    }
}
