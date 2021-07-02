package com.formdev.flatlaf.json;

public class ParseException extends RuntimeException {
    private final Location location;

    ParseException(String message, Location location2) {
        super(message + " at " + location2);
        this.location = location2;
    }

    public Location getLocation() {
        return this.location;
    }

    @Deprecated
    public int getOffset() {
        return this.location.offset;
    }

    @Deprecated
    public int getLine() {
        return this.location.line;
    }

    @Deprecated
    public int getColumn() {
        return this.location.column;
    }
}
