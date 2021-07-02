package com.kitfox.svg.animation;

import java.awt.geom.Point2D;

public class Bezier {
    double[] coord;
    double length;

    public Bezier(double sx, double sy, double[] coords, int numCoords) {
        setCoords(sx, sy, coords, numCoords);
    }

    public void setCoords(double sx, double sy, double[] coords, int numCoords) {
        this.coord = new double[((numCoords * 2) + 2)];
        this.coord[0] = sx;
        this.coord[1] = sy;
        for (int i = 0; i < numCoords; i++) {
            this.coord[(i * 2) + 2] = coords[i * 2];
            this.coord[(i * 2) + 3] = coords[(i * 2) + 1];
        }
        calcLength();
    }

    public double getLength() {
        return this.length;
    }

    private void calcLength() {
        this.length = 0.0d;
        for (int i = 2; i < this.coord.length; i += 2) {
            this.length = lineLength(this.coord[i - 2], this.coord[i - 1], this.coord[i], this.coord[i + 1]) + this.length;
        }
    }

    private double lineLength(double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        return Math.sqrt((dx * dx) + (dy * dy));
    }

    public Point2D.Double getFinalPoint(Point2D.Double point) {
        point.x = this.coord[this.coord.length - 2];
        point.y = this.coord[this.coord.length - 1];
        return point;
    }

    public Point2D.Double eval(double param, Point2D.Double point) {
        point.x = 0.0d;
        point.y = 0.0d;
        int numKnots = this.coord.length / 2;
        for (int i = 0; i < numKnots; i++) {
            double scale = bernstein(numKnots - 1, i, param);
            point.x += this.coord[i * 2] * scale;
            point.y += this.coord[(i * 2) + 1] * scale;
        }
        return point;
    }

    private double bernstein(int numKnots, int knotNo, double param) {
        double iParam = 1.0d - param;
        switch (numKnots) {
            case 0:
                return 1.0d;
            case 1:
                switch (knotNo) {
                    case 0:
                        return iParam;
                    case 1:
                        return param;
                }
            case 2:
                switch (knotNo) {
                    case 0:
                        return iParam * iParam;
                    case 1:
                        return 2.0d * iParam * param;
                    case 2:
                        return param * param;
                }
            case 3:
                switch (knotNo) {
                    case 0:
                        return iParam * iParam * iParam;
                    case 1:
                        return 3.0d * iParam * iParam * param;
                    case 2:
                        return 3.0d * iParam * param * param;
                    case 3:
                        return param * param * param;
                }
        }
        double retVal = 1.0d;
        for (int i = 0; i < knotNo; i++) {
            retVal *= param;
        }
        for (int i2 = 0; i2 < numKnots - knotNo; i2++) {
            retVal *= iParam;
        }
        return retVal * ((double) choose(numKnots, knotNo));
    }

    private int choose(int num, int denom) {
        int denom2 = num - denom;
        if (denom < denom2) {
            denom = denom2;
            denom2 = denom;
        }
        int prod = 1;
        for (int i = num; i > denom; i--) {
            prod *= num;
        }
        for (int i2 = 2; i2 <= denom2; i2++) {
            prod /= i2;
        }
        return prod;
    }
}
