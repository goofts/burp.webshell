package com.formdev.flatlaf.extras;

import com.formdev.flatlaf.FlatIconColors;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.ui.FlatUIUtils;
import com.formdev.flatlaf.util.Graphics2DProxy;
import com.formdev.flatlaf.util.GrayFilter;
import com.formdev.flatlaf.util.MultiResolutionImageSupport;
import com.formdev.flatlaf.util.UIScale;
import com.kitfox.svg.SVGDiagram;
import com.kitfox.svg.SVGException;
import com.kitfox.svg.SVGUniverse;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RGBImageFilter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;

public class FlatSVGIcon extends ImageIcon implements FlatLaf.DisabledIconProvider {
    private static Boolean darkLaf;
    private static final SVGUniverse svgUniverse = new SVGUniverse();
    private final ClassLoader classLoader;
    private boolean dark;
    private SVGDiagram diagram;
    private final boolean disabled;
    private final int height;
    private final String name;
    private final float scale;
    private final int width;

    public FlatSVGIcon(String name2) {
        this(name2, -1, -1, 1.0f, false, null);
    }

    public FlatSVGIcon(String name2, ClassLoader classLoader2) {
        this(name2, -1, -1, 1.0f, false, classLoader2);
    }

    public FlatSVGIcon(String name2, int width2, int height2) {
        this(name2, width2, height2, 1.0f, false, null);
    }

    public FlatSVGIcon(String name2, int width2, int height2, ClassLoader classLoader2) {
        this(name2, width2, height2, 1.0f, false, classLoader2);
    }

    public FlatSVGIcon(String name2, float scale2) {
        this(name2, -1, -1, scale2, false, null);
    }

    public FlatSVGIcon(String name2, float scale2, ClassLoader classLoader2) {
        this(name2, -1, -1, scale2, false, classLoader2);
    }

    private FlatSVGIcon(String name2, int width2, int height2, float scale2, boolean disabled2, ClassLoader classLoader2) {
        this.name = name2;
        this.classLoader = classLoader2;
        this.width = width2;
        this.height = height2;
        this.scale = scale2;
        this.disabled = disabled2;
    }

    public FlatSVGIcon derive(int width2, int height2) {
        if (width2 == this.width && height2 == this.height) {
            return this;
        }
        FlatSVGIcon icon = new FlatSVGIcon(this.name, width2, height2, this.scale, false, this.classLoader);
        icon.diagram = this.diagram;
        icon.dark = this.dark;
        return icon;
    }

    public FlatSVGIcon derive(float scale2) {
        if (scale2 == this.scale) {
            return this;
        }
        FlatSVGIcon icon = new FlatSVGIcon(this.name, this.width, this.height, scale2, false, this.classLoader);
        icon.diagram = this.diagram;
        icon.dark = this.dark;
        return icon;
    }

    @Override // com.formdev.flatlaf.FlatLaf.DisabledIconProvider
    public Icon getDisabledIcon() {
        if (this.disabled) {
            return this;
        }
        FlatSVGIcon icon = new FlatSVGIcon(this.name, this.width, this.height, this.scale, true, this.classLoader);
        icon.diagram = this.diagram;
        icon.dark = this.dark;
        return icon;
    }

    private void update() {
        boolean z;
        if (this.dark != isDarkLaf() || this.diagram == null) {
            this.dark = isDarkLaf();
            URL url = getIconURL(this.name, this.dark);
            if (url == null) {
                z = true;
            } else {
                z = false;
            }
            if (z && this.dark) {
                url = getIconURL(this.name, false);
            }
            try {
                this.diagram = svgUniverse.getDiagram(url.toURI());
            } catch (URISyntaxException ex) {
                ex.printStackTrace();
            }
        }
    }

    private URL getIconURL(String name2, boolean dark2) {
        if (dark2) {
            int dotIndex = name2.lastIndexOf(46);
            name2 = name2.substring(0, dotIndex) + "_dark" + name2.substring(dotIndex);
        }
        return (this.classLoader != null ? this.classLoader : FlatSVGIcon.class.getClassLoader()).getResource(name2);
    }

    public boolean hasFound() {
        update();
        return this.diagram != null;
    }

    public int getIconWidth() {
        if (this.width > 0) {
            return scaleSize(this.width);
        }
        update();
        return scaleSize(this.diagram != null ? Math.round(this.diagram.getWidth()) : 16);
    }

    public int getIconHeight() {
        if (this.height > 0) {
            return scaleSize(this.height);
        }
        update();
        return scaleSize(this.diagram != null ? Math.round(this.diagram.getHeight()) : 16);
    }

    private int scaleSize(int size) {
        int scaledSize = UIScale.scale(size);
        if (this.scale != 1.0f) {
            return Math.round(((float) scaledSize) * this.scale);
        }
        return scaledSize;
    }

    public void paintIcon(Component c, Graphics g, int x, int y) {
        update();
        Rectangle clipBounds = g.getClipBounds();
        if (clipBounds == null || clipBounds.intersects(new Rectangle(x, y, getIconWidth(), getIconHeight()))) {
            RGBImageFilter grayFilter = null;
            if (this.disabled) {
                Object grayFilterObj = UIManager.get("Component.grayFilter");
                if (grayFilterObj instanceof RGBImageFilter) {
                    grayFilter = (RGBImageFilter) grayFilterObj;
                } else {
                    grayFilter = GrayFilter.createDisabledIconFilter(this.dark);
                }
            }
            Graphics2D g2 = new GraphicsFilter(g.create(), ColorFilter.getInstance(), grayFilter);
            try {
                FlatUIUtils.setRenderingHints(g2);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                paintSvg(g2, x, y);
            } finally {
                g2.dispose();
            }
        }
    }

    private void paintSvg(Graphics2D g, int x, int y) {
        double sx;
        double sy;
        if (this.diagram == null) {
            paintSvgError(g, x, y);
            return;
        }
        g.translate(x, y);
        g.clipRect(0, 0, getIconWidth(), getIconHeight());
        UIScale.scaleGraphics(g);
        if (this.width > 0 || this.height > 0) {
            if (this.width > 0) {
                sx = (double) (((float) this.width) / this.diagram.getWidth());
            } else {
                sx = 1.0d;
            }
            if (this.height > 0) {
                sy = (double) (((float) this.height) / this.diagram.getHeight());
            } else {
                sy = 1.0d;
            }
            if (!(sx == 1.0d && sy == 1.0d)) {
                g.scale(sx, sy);
            }
        }
        if (this.scale != 1.0f) {
            g.scale((double) this.scale, (double) this.scale);
        }
        this.diagram.setIgnoringClipHeuristic(true);
        try {
            this.diagram.render(g);
        } catch (SVGException e) {
            paintSvgError(g, 0, 0);
        }
    }

    private void paintSvgError(Graphics2D g, int x, int y) {
        g.setColor(Color.red);
        g.fillRect(x, y, getIconWidth(), getIconHeight());
    }

    /*  JADX ERROR: MOVE_RESULT instruction can be used only in fallback mode
        jadx.core.utils.exceptions.CodegenException: MOVE_RESULT instruction can be used only in fallback mode
        	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:604)
        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:542)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:230)
        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:119)
        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:103)
        	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:806)
        	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:746)
        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:367)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:230)
        	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:119)
        	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:103)
        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:313)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:249)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:217)
        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:110)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:56)
        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:93)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:59)
        	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:244)
        	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:237)
        	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:342)
        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:295)
        	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:264)
        	at java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:184)
        	at java.util.ArrayList.forEach(ArrayList.java:1257)
        	at java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:390)
        	at java.util.stream.Sink$ChainedReference.end(Sink.java:258)
        */
    public java.awt.Image getImage() {
        /*
            r9 = this;
            r8 = 0
            r9.update()
            int r2 = r9.getIconWidth()
            int r1 = r9.getIconHeight()
            r4 = 2
            java.awt.Dimension[] r0 = new java.awt.Dimension[r4]
            java.awt.Dimension r4 = new java.awt.Dimension
            r4.<init>(r2, r1)
            r0[r8] = r4
            r4 = 1
            java.awt.Dimension r5 = new java.awt.Dimension
            int r6 = r2 * 2
            int r7 = r1 * 2
            r5.<init>(r6, r7)
            r0[r4] = r5
            r3 = move-result
            java.awt.Image r4 = com.formdev.flatlaf.util.MultiResolutionImageSupport.create(r8, r0, r3)
            return r4
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.extras.FlatSVGIcon.getImage():java.awt.Image");
    }

    private /* synthetic */ Image lambda$getImage$0(int iconWidth, int iconHeight, Dimension size) {
        double sx;
        double sy;
        BufferedImage image = new BufferedImage(size.width, size.height, 2);
        Graphics2D g = image.createGraphics();
        try {
            if (size.width > 0) {
                sx = (double) (((float) size.width) / ((float) iconWidth));
            } else {
                sx = 1.0d;
            }
            if (size.height > 0) {
                sy = (double) (((float) size.height) / ((float) iconHeight));
            } else {
                sy = 1.0d;
            }
            if (!(sx == 1.0d && sy == 1.0d)) {
                g.scale(sx, sy);
            }
            paintIcon(null, g, 0, 0);
            return image;
        } finally {
            g.dispose();
        }
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [java.beans.PropertyChangeListener, void] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static boolean isDarkLaf() {
        /*
            java.lang.Boolean r0 = com.formdev.flatlaf.extras.FlatSVGIcon.darkLaf
            if (r0 != 0) goto L_0x000e
            void r0 = lafChanged()
            javax.swing.UIManager.addPropertyChangeListener(r0)
        L_0x000e:
            java.lang.Boolean r0 = com.formdev.flatlaf.extras.FlatSVGIcon.darkLaf
            boolean r0 = r0.booleanValue()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.formdev.flatlaf.extras.FlatSVGIcon.isDarkLaf():boolean");
    }

    /* access modifiers changed from: private */
    public static void lafChanged() {
        darkLaf = Boolean.valueOf(FlatLaf.isLafDark());
    }

    public static class ColorFilter {
        private static ColorFilter instance;
        private final Map<Color, Color> color2colorMap = new HashMap();
        private final Map<Integer, String> rgb2keyMap = new HashMap();

        public static ColorFilter getInstance() {
            if (instance == null) {
                instance = new ColorFilter();
            }
            return instance;
        }

        public ColorFilter() {
            FlatIconColors[] values = FlatIconColors.values();
            for (FlatIconColors c : values) {
                this.rgb2keyMap.put(Integer.valueOf(c.rgb), c.key);
            }
        }

        public void addAll(Map<Color, Color> from2toMap) {
            this.color2colorMap.putAll(from2toMap);
        }

        public void add(Color from, Color to) {
            this.color2colorMap.put(from, to);
        }

        public void remove(Color from) {
            this.color2colorMap.remove(from);
        }

        public Color filter(Color color) {
            Color color2;
            Color newColor = this.color2colorMap.get(color);
            if (newColor != null) {
                return newColor;
            }
            String colorKey = this.rgb2keyMap.get(Integer.valueOf(color.getRGB() & 16777215));
            if (colorKey == null) {
                return color;
            }
            Color newColor2 = UIManager.getColor(colorKey);
            if (newColor2 == null) {
                return color;
            }
            if (newColor2.getAlpha() != color.getAlpha()) {
                color2 = new Color((newColor2.getRGB() & 16777215) | (color.getRGB() & -16777216));
            } else {
                color2 = newColor2;
            }
            return color2;
        }
    }

    /* access modifiers changed from: private */
    public static class GraphicsFilter extends Graphics2DProxy {
        private final ColorFilter colorFilter;
        private final RGBImageFilter grayFilter;

        public GraphicsFilter(Graphics2D delegate, ColorFilter colorFilter2, RGBImageFilter grayFilter2) {
            super(delegate);
            this.colorFilter = colorFilter2;
            this.grayFilter = grayFilter2;
        }

        @Override // com.formdev.flatlaf.util.Graphics2DProxy
        public void setColor(Color c) {
            super.setColor(filterColor(c));
        }

        @Override // com.formdev.flatlaf.util.Graphics2DProxy
        public void setPaint(Paint paint) {
            if (paint instanceof Color) {
                paint = filterColor((Color) paint);
            }
            super.setPaint(paint);
        }

        private Color filterColor(Color color) {
            int oldRGB;
            int newRGB;
            if (this.colorFilter != null) {
                color = this.colorFilter.filter(color);
            }
            if (this.grayFilter == null || (newRGB = this.grayFilter.filterRGB(0, 0, (oldRGB = color.getRGB()))) == oldRGB) {
                return color;
            }
            return new Color(newRGB, true);
        }
    }
}
