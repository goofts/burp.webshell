package net.miginfocom.swing;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics2D;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;

public final class SwingContainerWrapper extends SwingComponentWrapper implements ContainerWrapper {
    private static final Color DB_CELL_OUTLINE = new Color(255, 0, 0);

    public SwingContainerWrapper(Container c) {
        super(c);
    }

    @Override // net.miginfocom.layout.ContainerWrapper
    public ComponentWrapper[] getComponents() {
        Container c = (Container) getComponent();
        ComponentWrapper[] cws = new ComponentWrapper[c.getComponentCount()];
        for (int i = 0; i < cws.length; i++) {
            cws[i] = new SwingComponentWrapper(c.getComponent(i));
        }
        return cws;
    }

    @Override // net.miginfocom.layout.ContainerWrapper
    public int getComponentCount() {
        return ((Container) getComponent()).getComponentCount();
    }

    @Override // net.miginfocom.layout.ContainerWrapper
    public Object getLayout() {
        return ((Container) getComponent()).getLayout();
    }

    @Override // net.miginfocom.layout.ContainerWrapper
    public final boolean isLeftToRight() {
        return ((Container) getComponent()).getComponentOrientation().isLeftToRight();
    }

    @Override // net.miginfocom.layout.ContainerWrapper
    public final void paintDebugCell(int x, int y, int width, int height) {
        Graphics2D g;
        Component c = (Component) getComponent();
        if (c.isShowing() && (g = c.getGraphics()) != null) {
            g.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, new float[]{2.0f, 3.0f}, 0.0f));
            g.setPaint(DB_CELL_OUTLINE);
            g.drawRect(x, y, width - 1, height - 1);
        }
    }

    @Override // net.miginfocom.swing.SwingComponentWrapper, net.miginfocom.layout.ComponentWrapper
    public int getComponentType(boolean disregardScrollPane) {
        return 1;
    }

    @Override // net.miginfocom.swing.SwingComponentWrapper, net.miginfocom.layout.ComponentWrapper
    public int getLayoutHashCode() {
        System.nanoTime();
        int h = super.getLayoutHashCode();
        if (!isLeftToRight()) {
            return 0;
        }
        int h2 = h + 416343;
        return 0;
    }
}
