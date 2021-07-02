package com.kitfox.svg.pathcmd;

import java.awt.geom.GeneralPath;

public class MoveTo extends PathCommand {
    public float x = 0.0f;
    public float y = 0.0f;

    public MoveTo() {
    }

    public MoveTo(boolean isRelative, float x2, float y2) {
        super(isRelative);
        this.x = x2;
        this.y = y2;
    }

    @Override // com.kitfox.svg.pathcmd.PathCommand
    public void appendPath(GeneralPath path, BuildHistory hist) {
        float offx;
        float offy;
        if (this.isRelative) {
            offx = hist.lastPoint.x;
        } else {
            offx = 0.0f;
        }
        if (this.isRelative) {
            offy = hist.lastPoint.y;
        } else {
            offy = 0.0f;
        }
        path.moveTo(this.x + offx, this.y + offy);
        hist.setStartPoint(this.x + offx, this.y + offy);
        hist.setLastPoint(this.x + offx, this.y + offy);
        hist.setLastKnot(this.x + offx, this.y + offy);
    }

    @Override // com.kitfox.svg.pathcmd.PathCommand
    public int getNumKnotsAdded() {
        return 2;
    }

    public String toString() {
        return "M " + this.x + " " + this.y;
    }
}
