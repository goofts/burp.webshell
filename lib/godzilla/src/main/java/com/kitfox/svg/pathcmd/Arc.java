package com.kitfox.svg.pathcmd;

import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.GeneralPath;

public class Arc extends PathCommand {
    public boolean largeArc = false;
    public float rx = 0.0f;
    public float ry = 0.0f;
    public boolean sweep = false;
    public float x = 0.0f;
    public float xAxisRot = 0.0f;
    public float y = 0.0f;

    public Arc() {
    }

    public Arc(boolean isRelative, float rx2, float ry2, float xAxisRot2, boolean largeArc2, boolean sweep2, float x2, float y2) {
        super(isRelative);
        this.rx = rx2;
        this.ry = ry2;
        this.xAxisRot = xAxisRot2;
        this.largeArc = largeArc2;
        this.sweep = sweep2;
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
        arcTo(path, this.rx, this.ry, this.xAxisRot, this.largeArc, this.sweep, this.x + offx, this.y + offy, hist.lastPoint.x, hist.lastPoint.y);
        hist.setLastPoint(this.x + offx, this.y + offy);
        hist.setLastKnot(this.x + offx, this.y + offy);
    }

    @Override // com.kitfox.svg.pathcmd.PathCommand
    public int getNumKnotsAdded() {
        return 6;
    }

    public void arcTo(GeneralPath path, float rx2, float ry2, float angle, boolean largeArcFlag, boolean sweepFlag, float x2, float y2, float x0, float y0) {
        Arc2D arc;
        if (rx2 == 0.0f || ry2 == 0.0f) {
            path.lineTo(x2, y2);
        } else if ((x0 != x2 || y0 != y2) && (arc = computeArc((double) x0, (double) y0, (double) rx2, (double) ry2, (double) angle, largeArcFlag, sweepFlag, (double) x2, (double) y2)) != null) {
            path.append(AffineTransform.getRotateInstance(Math.toRadians((double) angle), arc.getCenterX(), arc.getCenterY()).createTransformedShape(arc), true);
        }
    }

    public static Arc2D computeArc(double x0, double y0, double rx2, double ry2, double angle, boolean largeArcFlag, boolean sweepFlag, double x2, double y2) {
        double dx2 = (x0 - x2) / 2.0d;
        double dy2 = (y0 - y2) / 2.0d;
        double angle2 = Math.toRadians(angle % 360.0d);
        double cosAngle = Math.cos(angle2);
        double sinAngle = Math.sin(angle2);
        double x1 = (cosAngle * dx2) + (sinAngle * dy2);
        double y1 = ((-sinAngle) * dx2) + (cosAngle * dy2);
        double rx3 = Math.abs(rx2);
        double ry3 = Math.abs(ry2);
        double Prx = rx3 * rx3;
        double Pry = ry3 * ry3;
        double Px1 = x1 * x1;
        double Py1 = y1 * y1;
        double radiiCheck = (Px1 / Prx) + (Py1 / Pry);
        if (radiiCheck > 1.0d) {
            rx3 *= Math.sqrt(radiiCheck);
            ry3 *= Math.sqrt(radiiCheck);
            Prx = rx3 * rx3;
            Pry = ry3 * ry3;
        }
        double sign = largeArcFlag == sweepFlag ? -1.0d : 1.0d;
        double sq = (((Prx * Pry) - (Prx * Py1)) - (Pry * Px1)) / ((Prx * Py1) + (Pry * Px1));
        if (sq < 0.0d) {
            sq = 0.0d;
        }
        double coef = sign * Math.sqrt(sq);
        double cx1 = coef * ((rx3 * y1) / ry3);
        double cy1 = coef * (-((ry3 * x1) / rx3));
        double cx = ((x0 + x2) / 2.0d) + ((cosAngle * cx1) - (sinAngle * cy1));
        double cy = ((y0 + y2) / 2.0d) + (sinAngle * cx1) + (cosAngle * cy1);
        double ux = (x1 - cx1) / rx3;
        double uy = (y1 - cy1) / ry3;
        double vx = ((-x1) - cx1) / rx3;
        double vy = ((-y1) - cy1) / ry3;
        double angleStart = Math.toDegrees(Math.acos(ux / Math.sqrt((ux * ux) + (uy * uy))) * (uy < 0.0d ? -1.0d : 1.0d));
        double angleExtent = Math.toDegrees(Math.acos(((ux * vx) + (uy * vy)) / Math.sqrt(((ux * ux) + (uy * uy)) * ((vx * vx) + (vy * vy)))) * ((ux * vy) - (uy * vx) < 0.0d ? -1.0d : 1.0d));
        if (!sweepFlag && angleExtent > 0.0d) {
            angleExtent -= 360.0d;
        } else if (sweepFlag && angleExtent < 0.0d) {
            angleExtent += 360.0d;
        }
        Arc2D.Double arc = new Arc2D.Double();
        arc.x = cx - rx3;
        arc.y = cy - ry3;
        arc.width = 2.0d * rx3;
        arc.height = 2.0d * ry3;
        arc.start = -(angleStart % 360.0d);
        arc.extent = -(angleExtent % 360.0d);
        return arc;
    }

    public String toString() {
        return "A " + this.rx + " " + this.ry + " " + this.xAxisRot + " " + this.largeArc + " " + this.sweep + " " + this.x + " " + this.y;
    }
}
