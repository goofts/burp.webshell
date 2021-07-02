package net.miginfocom.layout;

public interface ContainerWrapper extends ComponentWrapper {
    int getComponentCount();

    ComponentWrapper[] getComponents();

    Object getLayout();

    boolean isLeftToRight();

    void paintDebugCell(int i, int i2, int i3, int i4);
}
