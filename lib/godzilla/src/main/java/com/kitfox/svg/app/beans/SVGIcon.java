package com.kitfox.svg.app.beans;

import com.kitfox.svg.SVGCache;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGUniverse;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.net.URI;
import javax.swing.ImageIcon;

public class SVGIcon extends ImageIcon {
    public static final int AUTOSIZE_BESTFIT = 3;
    public static final int AUTOSIZE_HORIZ = 1;
    public static final int AUTOSIZE_NONE = 0;
    public static final int AUTOSIZE_STRETCH = 4;
    public static final int AUTOSIZE_VERT = 2;
    public static final int INTERP_BICUBIC = 2;
    public static final int INTERP_BILINEAR = 1;
    public static final int INTERP_NEAREST_NEIGHBOR = 0;
    public static final String PROP_AUTOSIZE = "PROP_AUTOSIZE";
    public static final long serialVersionUID = 1;
    private boolean antiAlias;
    private int autosize = 0;
    private final PropertyChangeSupport changes = new PropertyChangeSupport(this);
    private boolean clipToViewbox;
    private int interpolation = 0;
    Dimension preferredSize;
    AffineTransform scaleXform = new AffineTransform();
    URI svgURI;
    SVGUniverse svgUniverse = SVGCache.getSVGUniverse();

    public void addPropertyChangeListener(PropertyChangeListener p) {
        this.changes.addPropertyChangeListener(p);
    }

    public void removePropertyChangeListener(PropertyChangeListener p) {
        this.changes.removePropertyChangeListener(p);
    }

    public Image getImage() {
        BufferedImage bi = new BufferedImage(getIconWidth(), getIconHeight(), 2);
        paintIcon((Component) null, bi.getGraphics(), 0, 0);
        return bi;
    }

    public int getIconHeightIgnoreAutosize() {
        if (this.preferredSize != null && (this.autosize == 2 || this.autosize == 4 || this.autosize == 3)) {
            return this.preferredSize.height;
        }
        SVGDiagram diagram = this.svgUniverse.getDiagram(this.svgURI);
        if (diagram == null) {
            return 0;
        }
        return (int) diagram.getHeight();
    }

    public int getIconWidthIgnoreAutosize() {
        if (this.preferredSize != null && (this.autosize == 1 || this.autosize == 4 || this.autosize == 3)) {
            return this.preferredSize.width;
        }
        SVGDiagram diagram = this.svgUniverse.getDiagram(this.svgURI);
        if (diagram == null) {
            return 0;
        }
        return (int) diagram.getWidth();
    }

    private boolean isAutoSizeBestFitUseFixedHeight(int iconWidthIgnoreAutosize, int iconHeightIgnoreAutosize, SVGDiagram diagram) {
        return ((float) iconHeightIgnoreAutosize) / diagram.getHeight() < ((float) iconWidthIgnoreAutosize) / diagram.getWidth();
    }

    public int getIconWidth() {
        int iconWidthIgnoreAutosize = getIconWidthIgnoreAutosize();
        int iconHeightIgnoreAutosize = getIconHeightIgnoreAutosize();
        SVGDiagram diagram = this.svgUniverse.getDiagram(this.svgURI);
        if (this.preferredSize == null) {
            return iconWidthIgnoreAutosize;
        }
        if (this.autosize != 2 && (this.autosize != 3 || !isAutoSizeBestFitUseFixedHeight(iconWidthIgnoreAutosize, iconHeightIgnoreAutosize, diagram))) {
            return iconWidthIgnoreAutosize;
        }
        return (int) (((double) iconHeightIgnoreAutosize) / ((double) (diagram.getHeight() / diagram.getWidth())));
    }

    public int getIconHeight() {
        int iconWidthIgnoreAutosize = getIconWidthIgnoreAutosize();
        int iconHeightIgnoreAutosize = getIconHeightIgnoreAutosize();
        SVGDiagram diagram = this.svgUniverse.getDiagram(this.svgURI);
        if (this.preferredSize == null) {
            return iconHeightIgnoreAutosize;
        }
        if (this.autosize != 1 && (this.autosize != 3 || isAutoSizeBestFitUseFixedHeight(iconWidthIgnoreAutosize, iconHeightIgnoreAutosize, diagram))) {
            return iconHeightIgnoreAutosize;
        }
        return (int) (((double) iconWidthIgnoreAutosize) * ((double) (diagram.getHeight() / diagram.getWidth())));
    }

    public void paintIcon(Component comp, Graphics gg, int x, int y) {
        Graphics2D g = (Graphics2D) gg.create();
        paintIcon(comp, g, x, y);
        g.dispose();
    }

    private void paintIcon(Component comp, Graphics2D g, int x, int y) {
        Object oldAliasHint = g.getRenderingHint(RenderingHints.KEY_ANTIALIASING);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, this.antiAlias ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        Object oldInterpolationHint = g.getRenderingHint(RenderingHints.KEY_INTERPOLATION);
        switch (this.interpolation) {
            case 0:
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                break;
            case 1:
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                break;
            case 2:
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                break;
        }
        SVGDiagram diagram = this.svgUniverse.getDiagram(this.svgURI);
        if (diagram != null) {
            g.translate(x, y);
            diagram.setIgnoringClipHeuristic(!this.clipToViewbox);
            if (this.clipToViewbox) {
                g.setClip(new Rectangle2D.Float(0.0f, 0.0f, diagram.getWidth(), diagram.getHeight()));
            }
            if (this.autosize == 0) {
                try {
                    diagram.render(g);
                    g.translate(-x, -y);
                    g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAliasHint);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            } else {
                int width = getIconWidthIgnoreAutosize();
                int height = getIconHeightIgnoreAutosize();
                if (width != 0 && height != 0) {
                    double diaWidth = (double) diagram.getWidth();
                    double diaHeight = (double) diagram.getHeight();
                    double scaleW = 1.0d;
                    double scaleH = 1.0d;
                    if (this.autosize == 3) {
                        if (((double) height) / diaHeight < ((double) width) / diaWidth) {
                            scaleH = ((double) height) / diaHeight;
                        } else {
                            scaleH = ((double) width) / diaWidth;
                        }
                        scaleW = scaleH;
                    } else if (this.autosize == 1) {
                        scaleH = ((double) width) / diaWidth;
                        scaleW = scaleH;
                    } else if (this.autosize == 2) {
                        scaleH = ((double) height) / diaHeight;
                        scaleW = scaleH;
                    } else if (this.autosize == 4) {
                        scaleW = ((double) width) / diaWidth;
                        scaleH = ((double) height) / diaHeight;
                    }
                    this.scaleXform.setToScale(scaleW, scaleH);
                    AffineTransform oldXform = g.getTransform();
                    g.transform(this.scaleXform);
                    try {
                        diagram.render(g);
                        g.setTransform(oldXform);
                        g.translate(-x, -y);
                        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oldAliasHint);
                        if (oldInterpolationHint != null) {
                            g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oldInterpolationHint);
                        }
                    } catch (SVGException e2) {
                        throw new RuntimeException(e2);
                    }
                }
            }
        }
    }

    public SVGUniverse getSvgUniverse() {
        return this.svgUniverse;
    }

    public void setSvgUniverse(SVGUniverse svgUniverse2) {
        SVGUniverse old = this.svgUniverse;
        this.svgUniverse = svgUniverse2;
        this.changes.firePropertyChange("svgUniverse", old, svgUniverse2);
    }

    public URI getSvgURI() {
        return this.svgURI;
    }

    public void setSvgURI(URI svgURI2) {
        URI old = this.svgURI;
        this.svgURI = svgURI2;
        SVGDiagram diagram = this.svgUniverse.getDiagram(svgURI2);
        if (diagram != null) {
            Dimension size = getPreferredSize();
            if (size == null) {
                size = new Dimension((int) diagram.getRoot().getDeviceWidth(), (int) diagram.getRoot().getDeviceHeight());
            }
            diagram.setDeviceViewport(new Rectangle(0, 0, size.width, size.height));
        }
        this.changes.firePropertyChange("svgURI", old, svgURI2);
    }

    public void setSvgResourcePath(String resourcePath) {
        URI old = this.svgURI;
        try {
            this.svgURI = new URI(getClass().getResource(resourcePath).toString());
            this.changes.firePropertyChange("svgURI", old, this.svgURI);
            SVGDiagram diagram = this.svgUniverse.getDiagram(this.svgURI);
            if (diagram != null) {
                diagram.setDeviceViewport(new Rectangle(0, 0, this.preferredSize.width, this.preferredSize.height));
            }
        } catch (Exception e) {
            this.svgURI = old;
        }
    }

    public boolean isScaleToFit() {
        return this.autosize == 4;
    }

    public void setScaleToFit(boolean scaleToFit) {
        setAutosize(4);
    }

    public Dimension getPreferredSize() {
        SVGDiagram diagram;
        if (this.preferredSize == null && (diagram = this.svgUniverse.getDiagram(this.svgURI)) != null) {
            setPreferredSize(new Dimension((int) diagram.getWidth(), (int) diagram.getHeight()));
        }
        return new Dimension(this.preferredSize);
    }

    public void setPreferredSize(Dimension preferredSize2) {
        Dimension old = this.preferredSize;
        this.preferredSize = preferredSize2;
        SVGDiagram diagram = this.svgUniverse.getDiagram(this.svgURI);
        if (diagram != null) {
            diagram.setDeviceViewport(new Rectangle(0, 0, preferredSize2.width, preferredSize2.height));
        }
        this.changes.firePropertyChange("preferredSize", old, preferredSize2);
    }

    public boolean getUseAntiAlias() {
        return getAntiAlias();
    }

    public void setUseAntiAlias(boolean antiAlias2) {
        setAntiAlias(antiAlias2);
    }

    public boolean getAntiAlias() {
        return this.antiAlias;
    }

    public void setAntiAlias(boolean antiAlias2) {
        boolean old = this.antiAlias;
        this.antiAlias = antiAlias2;
        this.changes.firePropertyChange("antiAlias", old, antiAlias2);
    }

    public int getInterpolation() {
        return this.interpolation;
    }

    public void setInterpolation(int interpolation2) {
        int old = this.interpolation;
        this.interpolation = interpolation2;
        this.changes.firePropertyChange("interpolation", old, interpolation2);
    }

    public boolean isClipToViewbox() {
        return this.clipToViewbox;
    }

    public void setClipToViewbox(boolean clipToViewbox2) {
        this.clipToViewbox = clipToViewbox2;
    }

    public int getAutosize() {
        return this.autosize;
    }

    public void setAutosize(int autosize2) {
        int oldAutosize = this.autosize;
        this.autosize = autosize2;
        this.changes.firePropertyChange("PROP_AUTOSIZE", oldAutosize, autosize2);
    }
}
