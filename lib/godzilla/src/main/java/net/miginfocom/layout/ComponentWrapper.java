package net.miginfocom.layout;

public interface ComponentWrapper {
    public static final int TYPE_BUTTON = 5;
    public static final int TYPE_CHECK_BOX = 16;
    public static final int TYPE_COMBO_BOX = 11;
    public static final int TYPE_CONTAINER = 1;
    public static final int TYPE_IMAGE = 9;
    public static final int TYPE_LABEL = 2;
    public static final int TYPE_LIST = 6;
    public static final int TYPE_PANEL = 10;
    public static final int TYPE_PROGRESS_BAR = 14;
    public static final int TYPE_SCROLL_BAR = 17;
    public static final int TYPE_SCROLL_PANE = 8;
    public static final int TYPE_SEPARATOR = 18;
    public static final int TYPE_SLIDER = 12;
    public static final int TYPE_SPINNER = 13;
    public static final int TYPE_TABBED_PANE = 19;
    public static final int TYPE_TABLE = 7;
    public static final int TYPE_TEXT_AREA = 4;
    public static final int TYPE_TEXT_FIELD = 3;
    public static final int TYPE_TREE = 15;
    public static final int TYPE_UNKNOWN = 0;
    public static final int TYPE_UNSET = -1;

    int getBaseline(int i, int i2);

    Object getComponent();

    int getComponentType(boolean z);

    int getContentBias();

    int getHeight();

    int getHorizontalScreenDPI();

    int getLayoutHashCode();

    String getLinkId();

    int getMaximumHeight(int i);

    int getMaximumWidth(int i);

    int getMinimumHeight(int i);

    int getMinimumWidth(int i);

    ContainerWrapper getParent();

    float getPixelUnitFactor(boolean z);

    int getPreferredHeight(int i);

    int getPreferredWidth(int i);

    int getScreenHeight();

    int getScreenLocationX();

    int getScreenLocationY();

    int getScreenWidth();

    int getVerticalScreenDPI();

    int[] getVisualPadding();

    int getWidth();

    int getX();

    int getY();

    boolean hasBaseline();

    boolean isVisible();

    void paintDebugOutline(boolean z);

    void setBounds(int i, int i2, int i3, int i4);
}
