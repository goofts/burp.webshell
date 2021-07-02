package com.kitfox.svg.pathcmd;

import java.awt.geom.GeneralPath;

public abstract class PathCommand {
    public boolean isRelative = false;

    public abstract void appendPath(GeneralPath generalPath, BuildHistory buildHistory);

    public abstract int getNumKnotsAdded();

    public PathCommand() {
    }

    public PathCommand(boolean isRelative2) {
        this.isRelative = isRelative2;
    }
}
