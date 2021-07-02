package net.miginfocom.layout;

public abstract class UnitConverter {
    public static final int UNABLE = -87654312;

    public abstract int convertToPixels(float f, String str, boolean z, float f2, ContainerWrapper containerWrapper, ComponentWrapper componentWrapper);
}
