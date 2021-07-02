package net.miginfocom.swing;

import com.formdev.flatlaf.FlatClientProperties;
import com.jgoodies.forms.util.DefaultUnitConverter;
import java.awt.BasicStroke;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.HeadlessException;
import java.awt.Insets;
import java.awt.Label;
import java.awt.List;
import java.awt.Point;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.TextComponent;
import java.awt.TextField;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.IdentityHashMap;
import java.util.StringTokenizer;
import javassist.bytecode.Opcode;
import javax.swing.AbstractButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;
import net.miginfocom.layout.ComponentWrapper;
import net.miginfocom.layout.ContainerWrapper;
import net.miginfocom.layout.PlatformDefaults;

public class SwingComponentWrapper implements ComponentWrapper {
    private static final Color DB_COMP_OUTLINE = new Color(0, 0, (int) Opcode.GOTO_W);
    private static final IdentityHashMap<FontMetrics, Point2D.Float> FM_MAP = new IdentityHashMap<>(4);
    private static final Font SUBST_FONT = new Font("sansserif", 0, 11);
    private static final String VISUAL_PADDING_PROPERTY = PlatformDefaults.VISUAL_PADDING_PROPERTY;
    private static boolean isJava9orLater;
    private static boolean maxSet = false;
    private static boolean vp = true;
    private Boolean bl = null;
    private final Component c;
    private int compType = -1;
    private boolean prefCalled = false;

    static {
        boolean z = true;
        try {
            if (Integer.parseInt(new StringTokenizer(System.getProperty("java.version"), "._-+").nextToken()) < 9) {
                z = false;
            }
            isJava9orLater = z;
        } catch (Exception e) {
        }
    }

    public SwingComponentWrapper(Component c2) {
        this.c = c2;
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final int getBaseline(int width, int height) {
        int h = height;
        int[] visPad = getVisualPadding();
        if (h < 0) {
            h = this.c.getHeight();
        } else if (visPad != null) {
            h = visPad[0] + height + visPad[2];
        }
        Component component = this.c;
        if (width < 0) {
            width = this.c.getWidth();
        }
        int baseLine = component.getBaseline(Math.max(0, width), Math.max(0, h));
        if (baseLine == -1 || visPad == null) {
            return baseLine;
        }
        return baseLine - visPad[0];
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final Object getComponent() {
        return this.c;
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final float getPixelUnitFactor(boolean isHor) {
        float scaleFactor;
        int verticalScreenDPI;
        float screenScale;
        switch (PlatformDefaults.getLogicalPixelBase()) {
            case 100:
                Font font = this.c.getFont();
                Component component = this.c;
                if (font == null) {
                    font = SUBST_FONT;
                }
                FontMetrics fm = component.getFontMetrics(font);
                Point2D.Float p = FM_MAP.get(fm);
                if (p == null) {
                    Rectangle2D r = fm.getStringBounds(DefaultUnitConverter.OLD_AVERAGE_CHARACTER_TEST_STRING, this.c.getGraphics());
                    p = new Point2D.Float(((float) r.getWidth()) / 6.0f, ((float) r.getHeight()) / 13.277344f);
                    FM_MAP.put(fm, p);
                }
                if (isHor) {
                    return p.x;
                }
                return p.y;
            case 101:
                Float s = isHor ? PlatformDefaults.getHorizontalScaleFactor() : PlatformDefaults.getVerticalScaleFactor();
                if (s != null) {
                    scaleFactor = s.floatValue();
                } else {
                    scaleFactor = 1.0f;
                }
                Object lafScaleFactorObj = UIManager.get("laf.scaleFactor");
                if (lafScaleFactorObj instanceof Number) {
                    return scaleFactor * ((Number) lafScaleFactorObj).floatValue();
                }
                if (isJava9orLater) {
                    screenScale = 1.0f;
                } else {
                    if (isHor) {
                        verticalScreenDPI = getHorizontalScreenDPI();
                    } else {
                        verticalScreenDPI = getVerticalScreenDPI();
                    }
                    screenScale = ((float) verticalScreenDPI) / ((float) PlatformDefaults.getDefaultDPI());
                }
                return scaleFactor * screenScale;
            default:
                return 1.0f;
        }
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final int getX() {
        return this.c.getX();
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final int getY() {
        return this.c.getY();
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final int getHeight() {
        return this.c.getHeight();
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final int getWidth() {
        return this.c.getWidth();
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final int getScreenLocationX() {
        Point p = new Point();
        SwingUtilities.convertPointToScreen(p, this.c);
        return p.x;
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final int getScreenLocationY() {
        Point p = new Point();
        SwingUtilities.convertPointToScreen(p, this.c);
        return p.y;
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final int getMinimumHeight(int sz) {
        if (!this.prefCalled) {
            this.c.getPreferredSize();
            this.prefCalled = true;
        }
        return this.c.getMinimumSize().height;
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final int getMinimumWidth(int sz) {
        if (!this.prefCalled) {
            this.c.getPreferredSize();
            this.prefCalled = true;
        }
        return this.c.getMinimumSize().width;
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final int getPreferredHeight(int sz) {
        if (this.c.getWidth() == 0 && this.c.getHeight() == 0 && sz != -1) {
            this.c.setBounds(this.c.getX(), this.c.getY(), sz, 1);
        }
        return this.c.getPreferredSize().height;
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final int getPreferredWidth(int sz) {
        if (this.c.getWidth() == 0 && this.c.getHeight() == 0 && sz != -1) {
            this.c.setBounds(this.c.getX(), this.c.getY(), 1, sz);
        }
        return this.c.getPreferredSize().width;
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final int getMaximumHeight(int sz) {
        if (!isMaxSet(this.c)) {
            return Integer.MAX_VALUE;
        }
        return this.c.getMaximumSize().height;
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final int getMaximumWidth(int sz) {
        if (!isMaxSet(this.c)) {
            return Integer.MAX_VALUE;
        }
        return this.c.getMaximumSize().width;
    }

    private boolean isMaxSet(Component c2) {
        return c2.isMaximumSizeSet();
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final ContainerWrapper getParent() {
        Container p = this.c.getParent();
        if (p != null) {
            return new SwingContainerWrapper(p);
        }
        return null;
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final int getHorizontalScreenDPI() {
        try {
            return this.c.getToolkit().getScreenResolution();
        } catch (HeadlessException e) {
            return PlatformDefaults.getDefaultDPI();
        }
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final int getVerticalScreenDPI() {
        try {
            return this.c.getToolkit().getScreenResolution();
        } catch (HeadlessException e) {
            return PlatformDefaults.getDefaultDPI();
        }
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final int getScreenWidth() {
        try {
            return this.c.getToolkit().getScreenSize().width;
        } catch (HeadlessException e) {
            return 1024;
        }
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final int getScreenHeight() {
        try {
            return this.c.getToolkit().getScreenSize().height;
        } catch (HeadlessException e) {
            return 768;
        }
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final boolean hasBaseline() {
        if (this.bl == null) {
            try {
                if (!(this.c instanceof JLabel) || this.c.getClientProperty("html") == null) {
                    this.bl = Boolean.valueOf(getBaseline(8192, 8192) > -1);
                } else {
                    this.bl = Boolean.FALSE;
                }
            } catch (Throwable th) {
                this.bl = Boolean.FALSE;
            }
        }
        return this.bl.booleanValue();
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final String getLinkId() {
        return this.c.getName();
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final void setBounds(int x, int y, int width, int height) {
        this.c.setBounds(x, y, width, height);
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public boolean isVisible() {
        return this.c.isVisible();
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final int[] getVisualPadding() {
        String classID;
        Object size;
        Object padding = null;
        if (!isVisualPaddingEnabled() || !(this.c instanceof JComponent)) {
            return null;
        }
        JComponent component = this.c;
        Object padValue = component.getClientProperty(VISUAL_PADDING_PROPERTY);
        if (padValue instanceof int[]) {
            padding = (int[]) padValue;
        } else if (padValue instanceof Insets) {
            Insets padInsets = (Insets) padValue;
            padding = new int[]{padInsets.top, padInsets.left, padInsets.bottom, padInsets.right};
        }
        if (padding != null) {
            return padding;
        }
        switch (getComponentType(false)) {
            case 0:
                classID = "Other";
                break;
            case 1:
                classID = "Container";
                break;
            case 2:
                classID = "Label";
                break;
            case 3:
                Border border = component.getBorder();
                if (!component.isOpaque() && border != null && border.getClass().getSimpleName().equals("AquaTextFieldBorder")) {
                    classID = "TextField";
                    break;
                } else {
                    classID = "";
                    break;
                }
            case 4:
                classID = "TextArea";
                break;
            case 5:
                Border border2 = component.getBorder();
                if (border2 != null && border2.getClass().getName().startsWith("com.apple.laf.AquaButtonBorder")) {
                    if (PlatformDefaults.getPlatform() != 1) {
                        classID = "Button";
                        break;
                    } else {
                        Object buttonType = component.getClientProperty(FlatClientProperties.BUTTON_TYPE);
                        if (buttonType == null) {
                            classID = component.getHeight() < 33 ? "Button" : "Button.bevel";
                        } else {
                            classID = "Button." + buttonType;
                        }
                        if (((AbstractButton) component).getIcon() != null) {
                            classID = classID + ".icon";
                            break;
                        }
                    }
                } else {
                    classID = "";
                    break;
                }
                break;
            case 6:
                classID = "List";
                break;
            case 7:
                classID = "Table";
                break;
            case 8:
                classID = "ScrollPane";
                break;
            case 9:
                classID = "Image";
                break;
            case 10:
                classID = "Panel";
                break;
            case 11:
                if (PlatformDefaults.getPlatform() == 1) {
                    if (!((JComboBox) component).isEditable()) {
                        Object isSquare = component.getClientProperty("JComboBox.isSquare");
                        Object isPopDown = component.getClientProperty("JComboBox.isPopDown");
                        if (isSquare == null || !isSquare.toString().equals("true")) {
                            if (isPopDown != null && isPopDown.toString().equals("true")) {
                                classID = "ComboBox.isPopDown";
                                break;
                            } else {
                                classID = "ComboBox";
                                break;
                            }
                        } else {
                            classID = "ComboBox.isSquare";
                            break;
                        }
                    } else {
                        Object isSquare2 = component.getClientProperty("JComboBox.isSquare");
                        if (isSquare2 != null && isSquare2.toString().equals("true")) {
                            classID = "ComboBox.editable.isSquare";
                            break;
                        } else {
                            classID = "ComboBox.editable";
                            break;
                        }
                    }
                } else {
                    classID = "ComboBox";
                    break;
                }
                break;
            case 12:
                classID = "Slider";
                break;
            case 13:
                classID = "Spinner";
                break;
            case 14:
                classID = "ProgressBar";
                break;
            case 15:
                classID = "Tree";
                break;
            case 16:
                Border border3 = component.getBorder();
                if (border3 != null && border3.getClass().getName().startsWith("com.apple.laf.AquaButtonBorder")) {
                    Object size2 = component.getClientProperty("JComponent.sizeVariant");
                    if (size2 == null || size2.toString().equals("regular")) {
                        size = "";
                    } else {
                        size = "." + size2;
                    }
                    if (!(component instanceof JRadioButton)) {
                        if (!(component instanceof JCheckBox)) {
                            classID = "ToggleButton" + size;
                            break;
                        } else {
                            classID = "CheckBox" + size;
                            break;
                        }
                    } else {
                        classID = "RadioButton" + size;
                        break;
                    }
                } else {
                    classID = "";
                    break;
                }
                break;
            case 17:
                classID = "ScrollBar";
                break;
            case 18:
                classID = "Separator";
                break;
            case 19:
                classID = "TabbedPane";
                break;
            default:
                classID = "";
                break;
        }
        int[] padValue2 = PlatformDefaults.getDefaultVisualPadding(classID + "." + VISUAL_PADDING_PROPERTY);
        if (padValue2 instanceof int[]) {
            return padValue2;
        }
        if (!(padValue2 instanceof Insets)) {
            return padding;
        }
        Insets padInsets2 = (Insets) padValue2;
        return new int[]{padInsets2.top, padInsets2.left, padInsets2.bottom, padInsets2.right};
    }

    public static boolean isMaxSizeSetOn1_4() {
        return maxSet;
    }

    public static void setMaxSizeSetOn1_4(boolean b) {
        maxSet = b;
    }

    public static boolean isVisualPaddingEnabled() {
        return vp;
    }

    public static void setVisualPaddingEnabled(boolean b) {
        vp = b;
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public final void paintDebugOutline(boolean showVisualPadding) {
        Graphics2D g;
        int[] padding;
        if (this.c.isShowing() && (g = this.c.getGraphics()) != null) {
            g.setPaint(DB_COMP_OUTLINE);
            g.setStroke(new BasicStroke(1.0f, 2, 0, 10.0f, new float[]{2.0f, 4.0f}, 0.0f));
            g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
            if (showVisualPadding && isVisualPaddingEnabled() && (padding = getVisualPadding()) != null) {
                g.setColor(Color.GREEN);
                g.drawRect(padding[1], padding[0], (getWidth() - 1) - (padding[1] + padding[3]), (getHeight() - 1) - (padding[2] + padding[0]));
            }
        }
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public int getComponentType(boolean disregardScrollPane) {
        if (this.compType == -1) {
            this.compType = checkType(disregardScrollPane);
        }
        return this.compType;
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public int getLayoutHashCode() {
        Dimension d = this.c.getMaximumSize();
        int hash = d.width + (d.height << 5);
        Dimension d2 = this.c.getPreferredSize();
        int hash2 = hash + (d2.width << 10) + (d2.height << 15);
        Dimension d3 = this.c.getMinimumSize();
        int hash3 = hash2 + (d3.width << 20) + (d3.height << 25);
        if (this.c.isVisible()) {
            hash3 += 1324511;
        }
        String id = getLinkId();
        if (id != null) {
            return hash3 + id.hashCode();
        }
        return hash3;
    }

    private int checkType(boolean disregardScrollPane) {
        Component c2 = this.c;
        if (disregardScrollPane) {
            if (c2 instanceof JScrollPane) {
                c2 = ((JScrollPane) c2).getViewport().getView();
            } else if (c2 instanceof ScrollPane) {
                c2 = ((ScrollPane) c2).getComponent(0);
            }
        }
        if ((c2 instanceof JTextField) || (c2 instanceof TextField)) {
            return 3;
        }
        if ((c2 instanceof JLabel) || (c2 instanceof Label)) {
            return 2;
        }
        if ((c2 instanceof JCheckBox) || (c2 instanceof JRadioButton) || (c2 instanceof Checkbox)) {
            return 16;
        }
        if ((c2 instanceof AbstractButton) || (c2 instanceof Button)) {
            return 5;
        }
        if ((c2 instanceof JComboBox) || (c2 instanceof Choice)) {
            return 11;
        }
        if ((c2 instanceof JTextComponent) || (c2 instanceof TextComponent)) {
            return 4;
        }
        if ((c2 instanceof JPanel) || (c2 instanceof Canvas)) {
            return 10;
        }
        if ((c2 instanceof JList) || (c2 instanceof List)) {
            return 6;
        }
        if (c2 instanceof JTable) {
            return 7;
        }
        if (c2 instanceof JSeparator) {
            return 18;
        }
        if (c2 instanceof JSpinner) {
            return 13;
        }
        if (c2 instanceof JTabbedPane) {
            return 19;
        }
        if (c2 instanceof JProgressBar) {
            return 14;
        }
        if (c2 instanceof JSlider) {
            return 12;
        }
        if (c2 instanceof JScrollPane) {
            return 8;
        }
        if ((c2 instanceof JScrollBar) || (c2 instanceof Scrollbar)) {
            return 17;
        }
        if (c2 instanceof Container) {
            return 1;
        }
        return 0;
    }

    public final int hashCode() {
        return getComponent().hashCode();
    }

    public final boolean equals(Object o) {
        if (!(o instanceof ComponentWrapper)) {
            return false;
        }
        return this.c.equals(((ComponentWrapper) o).getComponent());
    }

    @Override // net.miginfocom.layout.ComponentWrapper
    public int getContentBias() {
        return ((this.c instanceof JTextArea) || (this.c instanceof JEditorPane) || ((this.c instanceof JComponent) && Boolean.TRUE.equals(this.c.getClientProperty("migLayout.dynamicAspectRatio")))) ? 0 : -1;
    }
}
