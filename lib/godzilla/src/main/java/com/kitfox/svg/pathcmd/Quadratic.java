package com.kitfox.svg.pathcmd;

import java.awt.geom.GeneralPath;

public class Quadratic extends PathCommand {
    public float kx = 0.0f;
    public float ky = 0.0f;
    public float x = 0.0f;
    public float y = 0.0f;

    public Quadratic() {
    }

    public String toString() {
        return "Q " + this.kx + " " + this.ky + " " + this.x + " " + this.y;
    }

    public Quadratic(boolean isRelative, float kx2, float ky2, float x2, float y2) {
        super(isRelative);
        this.kx = kx2;
        this.ky = ky2;
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
        path.quadTo(this.kx + offx, this.ky + offy, this.x + offx, this.y + offy);
        hist.setLastPoint(this.x + offx, this.y + offy);
        hist.setLastKnot(this.kx + offx, this.ky + offy);
    }

    @Override // com.kitfox.svg.pathcmd.PathCommand
    public int getNumKnotsAdded() {
        return 4;
    }
}
