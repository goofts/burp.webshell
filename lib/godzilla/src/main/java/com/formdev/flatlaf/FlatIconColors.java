package com.formdev.flatlaf;

public enum FlatIconColors {
    ACTIONS_RED(14375008, "Actions.Red", true, false),
    ACTIONS_RED_DARK(13063248, "Actions.Red", false, true),
    ACTIONS_YELLOW(15573504, "Actions.Yellow", true, false),
    ACTIONS_YELLOW_DARK(15771442, "Actions.Yellow", false, true),
    ACTIONS_GREEN(5875817, "Actions.Green", true, false),
    ACTIONS_GREEN_DARK(4824148, "Actions.Green", false, true),
    ACTIONS_BLUE(3710934, "Actions.Blue", true, false),
    ACTIONS_BLUE_DARK(3510980, "Actions.Blue", false, true),
    ACTIONS_GREY(7237230, "Actions.Grey", true, false),
    ACTIONS_GREY_DARK(11514291, "Actions.Grey", false, true),
    ACTIONS_GREYINLINE(8358801, "Actions.GreyInline", true, false),
    ACTIONS_GREYINLINE_DARK(8358801, "Actions.GreyInline", false, true),
    OBJECTS_GREY(10135472, "Objects.Grey"),
    OBJECTS_BLUE(4241120, "Objects.Blue"),
    OBJECTS_GREEN(6468931, "Objects.Green"),
    OBJECTS_YELLOW(16035645, "Objects.Yellow"),
    OBJECTS_YELLOW_DARK(14263107, "Objects.YellowDark"),
    OBJECTS_PURPLE(12164088, "Objects.Purple"),
    OBJECTS_PINK(16354206, "Objects.Pink"),
    OBJECTS_RED(15885602, "Objects.Red"),
    OBJECTS_RED_STATUS(14701909, "Objects.RedStatus"),
    OBJECTS_GREEN_ANDROID(10798649, "Objects.GreenAndroid"),
    OBJECTS_BLACK_TEXT(2301728, "Objects.BlackText");
    
    public final boolean dark;
    public final String key;
    public final boolean light;
    public final int rgb;

    private FlatIconColors(int rgb2, String key2) {
        this(rgb2, key2, true, true);
    }

    private FlatIconColors(int rgb2, String key2, boolean light2, boolean dark2) {
        this.rgb = rgb2;
        this.key = key2;
        this.light = light2;
        this.dark = dark2;
    }
}
