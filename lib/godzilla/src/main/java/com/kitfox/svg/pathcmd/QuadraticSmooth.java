package com.kitfox.svg.pathcmd;

import java.awt.geom.GeneralPath;

public class QuadraticSmooth extends PathCommand {
    public float x = 0.0f;
    public float y = 0.0f;

    public QuadraticSmooth() {
    }

    public String toString() {
        return "T " + this.x + " " + this.y;
    }

    public QuadraticSmooth(boolean isRelative, float x2, float y2) {
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
        float oldKx = hist.lastKnot.x;
        float oldKy = hist.lastKnot.y;
        float kx = (hist.lastPoint.x * 2.0f) - oldKx;
        float ky = (hist.lastPoint.y * 2.0f) - oldKy;
        path.quadTo(kx, ky, this.x + offx, this.y + offy);
        hist.setLastPoint(this.x + offx, this.y + offy);
        hist.setLastKnot(kx, ky);
    }

    @Override // com.kitfox.svg.pathcmd.PathCommand
    public int getNumKnotsAdded() {
        return 4;
    }
}
