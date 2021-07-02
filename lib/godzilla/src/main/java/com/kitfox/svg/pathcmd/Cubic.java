package com.kitfox.svg.pathcmd;

import java.awt.geom.GeneralPath;

public class Cubic extends PathCommand {
    public float k1x = 0.0f;
    public float k1y = 0.0f;
    public float k2x = 0.0f;
    public float k2y = 0.0f;
    public float x = 0.0f;
    public float y = 0.0f;

    public Cubic() {
    }

    public String toString() {
        return "C " + this.k1x + " " + this.k1y + " " + this.k2x + " " + this.k2y + " " + this.x + " " + this.y;
    }

    public Cubic(boolean isRelative, float k1x2, float k1y2, float k2x2, float k2y2, float x2, float y2) {
        super(isRelative);
        this.k1x = k1x2;
        this.k1y = k1y2;
        this.k2x = k2x2;
        this.k2y = k2y2;
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
        path.curveTo(this.k1x + offx, this.k1y + offy, this.k2x + offx, this.k2y + offy, this.x + offx, this.y + offy);
        hist.setLastPoint(this.x + offx, this.y + offy);
        hist.setLastKnot(this.k2x + offx, this.k2y + offy);
    }

    @Override // com.kitfox.svg.pathcmd.PathCommand
    public int getNumKnotsAdded() {
        return 6;
    }
}
