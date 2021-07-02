package com.formdev.flatlaf.json;

public class Location {
    public final int column;
    public final int line;
    public final int offset;

    Location(int offset2, int line2, int column2) {
        this.offset = offset2;
        this.column = column2;
        this.line = line2;
    }

    public String toString() {
        return this.line + ":" + this.column;
    }

    public int hashCode() {
        return this.offset;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Location other = (Location) obj;
        return this.offset == other.offset && this.column == other.column && this.line == other.line;
    }
}
