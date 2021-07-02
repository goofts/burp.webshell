package com.jgoodies.forms.layout;

import com.formdev.flatlaf.FlatClientProperties;
import com.jgoodies.common.base.Preconditions;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.FormSpec;
import java.awt.Component;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Locale;
import java.util.StringTokenizer;

public final class CellConstraints implements Cloneable, Serializable {
    public static final Alignment BOTTOM = new Alignment("bottom", 1);
    public static final Alignment CENTER = new Alignment(FlatClientProperties.TABBED_PANE_ALIGN_CENTER, 2);
    public static final Alignment DEFAULT = new Alignment("default", 2);
    private static final Insets EMPTY_INSETS = new Insets(0, 0, 0, 0);
    public static final Alignment FILL = new Alignment(FlatClientProperties.TABBED_PANE_ALIGN_FILL, 2);
    public static final Alignment LEFT = new Alignment("left", 0);
    public static final Alignment RIGHT = new Alignment("right", 0);
    public static final Alignment TOP = new Alignment("top", 1);
    private static final Alignment[] VALUES = {DEFAULT, FILL, LEFT, RIGHT, CENTER, TOP, BOTTOM};
    public int gridHeight;
    public int gridWidth;
    public int gridX;
    public int gridY;
    public Alignment hAlign;
    public Boolean honorsVisibility;
    public Insets insets;
    public Alignment vAlign;

    public CellConstraints() {
        this(1, 1);
    }

    public CellConstraints(int gridX2, int gridY2) {
        this(gridX2, gridY2, 1, 1);
    }

    public CellConstraints(int gridX2, int gridY2, Alignment hAlign2, Alignment vAlign2) {
        this(gridX2, gridY2, 1, 1, hAlign2, vAlign2, EMPTY_INSETS);
    }

    public CellConstraints(int gridX2, int gridY2, int gridWidth2, int gridHeight2) {
        this(gridX2, gridY2, gridWidth2, gridHeight2, DEFAULT, DEFAULT);
    }

    public CellConstraints(int gridX2, int gridY2, int gridWidth2, int gridHeight2, Alignment hAlign2, Alignment vAlign2) {
        this(gridX2, gridY2, gridWidth2, gridHeight2, hAlign2, vAlign2, EMPTY_INSETS);
    }

    public CellConstraints(int gridX2, int gridY2, int gridWidth2, int gridHeight2, Alignment hAlign2, Alignment vAlign2, Insets insets2) {
        this.gridX = gridX2;
        this.gridY = gridY2;
        this.gridWidth = gridWidth2;
        this.gridHeight = gridHeight2;
        this.hAlign = hAlign2;
        this.vAlign = vAlign2;
        this.insets = insets2;
        if (gridX2 <= 0) {
            throw new IndexOutOfBoundsException("The grid x must be a positive number.");
        } else if (gridY2 <= 0) {
            throw new IndexOutOfBoundsException("The grid y must be a positive number.");
        } else if (gridWidth2 <= 0) {
            throw new IndexOutOfBoundsException("The grid width must be a positive number.");
        } else if (gridHeight2 <= 0) {
            throw new IndexOutOfBoundsException("The grid height must be a positive number.");
        } else {
            Preconditions.checkNotNull(hAlign2, "The horizontal alignment must not be null.");
            Preconditions.checkNotNull(vAlign2, "The vertical alignment must not be null.");
            ensureValidOrientations(hAlign2, vAlign2);
        }
    }

    public CellConstraints(String encodedConstraints) {
        this();
        initFromConstraints(encodedConstraints);
    }

    public CellConstraints translate(int dx, int dy) {
        return new CellConstraints(this.gridX + dx, this.gridY + dy, this.gridWidth, this.gridHeight, this.hAlign, this.vAlign, this.insets);
    }

    public CellConstraints xy(int col, int row) {
        return xywh(col, row, 1, 1);
    }

    public CellConstraints xy(int col, int row, String encodedAlignments) {
        return xywh(col, row, 1, 1, encodedAlignments);
    }

    public CellConstraints xy(int col, int row, Alignment colAlign, Alignment rowAlign) {
        return xywh(col, row, 1, 1, colAlign, rowAlign);
    }

    public CellConstraints xyw(int col, int row, int colSpan) {
        return xywh(col, row, colSpan, 1, DEFAULT, DEFAULT);
    }

    public CellConstraints xyw(int col, int row, int colSpan, String encodedAlignments) {
        return xywh(col, row, colSpan, 1, encodedAlignments);
    }

    public CellConstraints xyw(int col, int row, int colSpan, Alignment colAlign, Alignment rowAlign) {
        return xywh(col, row, colSpan, 1, colAlign, rowAlign);
    }

    public CellConstraints xywh(int col, int row, int colSpan, int rowSpan) {
        return xywh(col, row, colSpan, rowSpan, DEFAULT, DEFAULT);
    }

    public CellConstraints xywh(int col, int row, int colSpan, int rowSpan, String encodedAlignments) {
        CellConstraints result = xywh(col, row, colSpan, rowSpan);
        result.setAlignments(encodedAlignments, true);
        return result;
    }

    public CellConstraints xywh(int col, int row, int colSpan, int rowSpan, Alignment colAlign, Alignment rowAlign) {
        this.gridX = col;
        this.gridY = row;
        this.gridWidth = colSpan;
        this.gridHeight = rowSpan;
        this.hAlign = colAlign;
        this.vAlign = rowAlign;
        ensureValidOrientations(this.hAlign, this.vAlign);
        return this;
    }

    public CellConstraints rc(int row, int col) {
        return rchw(row, col, 1, 1);
    }

    public CellConstraints rc(int row, int col, String encodedAlignments) {
        return rchw(row, col, 1, 1, encodedAlignments);
    }

    public CellConstraints rc(int row, int col, Alignment rowAlign, Alignment colAlign) {
        return rchw(row, col, 1, 1, rowAlign, colAlign);
    }

    public CellConstraints rcw(int row, int col, int colSpan) {
        return rchw(row, col, 1, colSpan, DEFAULT, DEFAULT);
    }

    public CellConstraints rcw(int row, int col, int colSpan, String encodedAlignments) {
        return rchw(row, col, 1, colSpan, encodedAlignments);
    }

    public CellConstraints rcw(int row, int col, int colSpan, Alignment rowAlign, Alignment colAlign) {
        return rchw(row, col, 1, colSpan, rowAlign, colAlign);
    }

    public CellConstraints rchw(int row, int col, int rowSpan, int colSpan) {
        return rchw(row, col, rowSpan, colSpan, DEFAULT, DEFAULT);
    }

    public CellConstraints rchw(int row, int col, int rowSpan, int colSpan, String encodedAlignments) {
        CellConstraints result = rchw(row, col, rowSpan, colSpan);
        result.setAlignments(encodedAlignments, false);
        return result;
    }

    public CellConstraints rchw(int row, int col, int rowSpan, int colSpan, Alignment rowAlign, Alignment colAlign) {
        return xywh(col, row, colSpan, rowSpan, colAlign, rowAlign);
    }

    private void initFromConstraints(String encodedConstraints) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5 = true;
        StringTokenizer tokenizer = new StringTokenizer(encodedConstraints, " ,");
        int argCount = tokenizer.countTokens();
        if (argCount == 2 || argCount == 4 || argCount == 6) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "You must provide 2, 4 or 6 arguments.");
        Integer nextInt = decodeInt(tokenizer.nextToken());
        if (nextInt != null) {
            z2 = true;
        } else {
            z2 = false;
        }
        Preconditions.checkArgument(z2, "First cell constraint element must be a number.");
        this.gridX = nextInt.intValue();
        if (this.gridX > 0) {
            z3 = true;
        } else {
            z3 = false;
        }
        Preconditions.checkArgument(z3, "The grid x must be a positive number.");
        Integer nextInt2 = decodeInt(tokenizer.nextToken());
        if (nextInt2 != null) {
            z4 = true;
        } else {
            z4 = false;
        }
        Preconditions.checkArgument(z4, "Second cell constraint element must be a number.");
        this.gridY = nextInt2.intValue();
        if (this.gridY <= 0) {
            z5 = false;
        }
        Preconditions.checkArgument(z5, "The grid y must be a positive number.");
        if (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            Integer nextInt3 = decodeInt(token);
            if (nextInt3 != null) {
                this.gridWidth = nextInt3.intValue();
                if (this.gridWidth <= 0) {
                    throw new IndexOutOfBoundsException("The grid width must be a positive number.");
                }
                Integer nextInt4 = decodeInt(tokenizer.nextToken());
                if (nextInt4 == null) {
                    throw new IllegalArgumentException("Fourth cell constraint element must be like third.");
                }
                this.gridHeight = nextInt4.intValue();
                if (this.gridHeight <= 0) {
                    throw new IndexOutOfBoundsException("The grid height must be a positive number.");
                } else if (tokenizer.hasMoreTokens()) {
                    token = tokenizer.nextToken();
                } else {
                    return;
                }
            }
            this.hAlign = decodeAlignment(token);
            this.vAlign = decodeAlignment(tokenizer.nextToken());
            ensureValidOrientations(this.hAlign, this.vAlign);
        }
    }

    private void setAlignments(String encodedAlignments, boolean horizontalThenVertical) {
        Alignment alignment;
        StringTokenizer tokenizer = new StringTokenizer(encodedAlignments, " ,");
        Alignment first = decodeAlignment(tokenizer.nextToken());
        Alignment second = decodeAlignment(tokenizer.nextToken());
        if (horizontalThenVertical) {
            alignment = first;
        } else {
            alignment = second;
        }
        this.hAlign = alignment;
        if (!horizontalThenVertical) {
            second = first;
        }
        this.vAlign = second;
        ensureValidOrientations(this.hAlign, this.vAlign);
    }

    private static Integer decodeInt(String token) {
        try {
            return Integer.decode(token);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static Alignment decodeAlignment(String encodedAlignment) {
        return Alignment.valueOf(encodedAlignment);
    }

    /* access modifiers changed from: package-private */
    public void ensureValidGridBounds(int colCount, int rowCount) {
        if (this.gridX <= 0) {
            throw new IndexOutOfBoundsException("The column index " + this.gridX + " must be positive.");
        } else if (this.gridX > colCount) {
            throw new IndexOutOfBoundsException("The column index " + this.gridX + " must be less than or equal to " + colCount + ".");
        } else if ((this.gridX + this.gridWidth) - 1 > colCount) {
            throw new IndexOutOfBoundsException("The grid width " + this.gridWidth + " must be less than or equal to " + ((colCount - this.gridX) + 1) + ".");
        } else if (this.gridY <= 0) {
            throw new IndexOutOfBoundsException("The row index " + this.gridY + " must be positive.");
        } else if (this.gridY > rowCount) {
            throw new IndexOutOfBoundsException("The row index " + this.gridY + " must be less than or equal to " + rowCount + ".");
        } else if ((this.gridY + this.gridHeight) - 1 > rowCount) {
            throw new IndexOutOfBoundsException("The grid height " + this.gridHeight + " must be less than or equal to " + ((rowCount - this.gridY) + 1) + ".");
        }
    }

    private static void ensureValidOrientations(Alignment horizontalAlignment, Alignment verticalAlignment) {
        if (!horizontalAlignment.isHorizontal()) {
            throw new IllegalArgumentException("The horizontal alignment must be one of: left, center, right, fill, default.");
        } else if (!verticalAlignment.isVertical()) {
            throw new IllegalArgumentException("The vertical alignment must be one of: top, center, bottom, fill, default.");
        }
    }

    /* access modifiers changed from: package-private */
    public void setBounds(Component c, FormLayout layout, Rectangle cellBounds, FormLayout.Measure minWidthMeasure, FormLayout.Measure minHeightMeasure, FormLayout.Measure prefWidthMeasure, FormLayout.Measure prefHeightMeasure) {
        ColumnSpec colSpec = this.gridWidth == 1 ? layout.getColumnSpec(this.gridX) : null;
        RowSpec rowSpec = this.gridHeight == 1 ? layout.getRowSpec(this.gridY) : null;
        Alignment concreteHAlign = concreteAlignment(this.hAlign, colSpec);
        Alignment concreteVAlign = concreteAlignment(this.vAlign, rowSpec);
        Insets concreteInsets = this.insets != null ? this.insets : EMPTY_INSETS;
        int cellX = cellBounds.x + concreteInsets.left;
        int cellY = cellBounds.y + concreteInsets.top;
        int cellW = (cellBounds.width - concreteInsets.left) - concreteInsets.right;
        int cellH = (cellBounds.height - concreteInsets.top) - concreteInsets.bottom;
        int compW = componentSize(c, colSpec, cellW, minWidthMeasure, prefWidthMeasure);
        int compH = componentSize(c, rowSpec, cellH, minHeightMeasure, prefHeightMeasure);
        c.setBounds(origin(concreteHAlign, cellX, cellW, compW), origin(concreteVAlign, cellY, cellH, compH), extent(concreteHAlign, cellW, compW), extent(concreteVAlign, cellH, compH));
    }

    private static Alignment concreteAlignment(Alignment cellAlignment, FormSpec formSpec) {
        if (formSpec == null) {
            return cellAlignment == DEFAULT ? FILL : cellAlignment;
        }
        return usedAlignment(cellAlignment, formSpec);
    }

    private static Alignment usedAlignment(Alignment cellAlignment, FormSpec formSpec) {
        if (cellAlignment != DEFAULT) {
            return cellAlignment;
        }
        FormSpec.DefaultAlignment defaultAlignment = formSpec.getDefaultAlignment();
        if (defaultAlignment == FormSpec.FILL_ALIGN) {
            return FILL;
        }
        if (defaultAlignment == ColumnSpec.LEFT) {
            return LEFT;
        }
        if (defaultAlignment == FormSpec.CENTER_ALIGN) {
            return CENTER;
        }
        if (defaultAlignment == ColumnSpec.RIGHT) {
            return RIGHT;
        }
        if (defaultAlignment == RowSpec.TOP) {
            return TOP;
        }
        return BOTTOM;
    }

    private static int componentSize(Component component, FormSpec formSpec, int cellSize, FormLayout.Measure minMeasure, FormLayout.Measure prefMeasure) {
        if (formSpec == null) {
            return prefMeasure.sizeOf(component);
        }
        if (formSpec.getSize() == Sizes.MINIMUM) {
            return minMeasure.sizeOf(component);
        }
        if (formSpec.getSize() == Sizes.PREFERRED) {
            return prefMeasure.sizeOf(component);
        }
        return Math.min(cellSize, prefMeasure.sizeOf(component));
    }

    private static int origin(Alignment alignment, int cellOrigin, int cellSize, int componentSize) {
        if (alignment == RIGHT || alignment == BOTTOM) {
            return (cellOrigin + cellSize) - componentSize;
        }
        if (alignment == CENTER) {
            return cellOrigin + ((cellSize - componentSize) / 2);
        }
        return cellOrigin;
    }

    private static int extent(Alignment alignment, int cellSize, int componentSize) {
        return alignment == FILL ? cellSize : componentSize;
    }

    @Override // java.lang.Object
    public Object clone() {
        try {
            CellConstraints c = (CellConstraints) super.clone();
            c.insets = (Insets) this.insets.clone();
            return c;
        } catch (CloneNotSupportedException e) {
            throw new InternalError();
        }
    }

    public String toString() {
        StringBuffer buffer = new StringBuffer("CellConstraints");
        buffer.append("[x=");
        buffer.append(this.gridX);
        buffer.append("; y=");
        buffer.append(this.gridY);
        buffer.append("; w=");
        buffer.append(this.gridWidth);
        buffer.append("; h=");
        buffer.append(this.gridHeight);
        buffer.append("; hAlign=");
        buffer.append(this.hAlign);
        buffer.append("; vAlign=");
        buffer.append(this.vAlign);
        if (!EMPTY_INSETS.equals(this.insets)) {
            buffer.append("; insets=");
            buffer.append(this.insets);
        }
        buffer.append("; honorsVisibility=");
        buffer.append(this.honorsVisibility);
        buffer.append(']');
        return buffer.toString();
    }

    public String toShortString() {
        return toShortString(null);
    }

    public String toShortString(FormLayout layout) {
        RowSpec rowSpec;
        StringBuffer buffer = new StringBuffer("(");
        buffer.append(formatInt(this.gridX));
        buffer.append(", ");
        buffer.append(formatInt(this.gridY));
        buffer.append(", ");
        buffer.append(formatInt(this.gridWidth));
        buffer.append(", ");
        buffer.append(formatInt(this.gridHeight));
        buffer.append(", \"");
        buffer.append(this.hAlign.abbreviation());
        if (this.hAlign == DEFAULT && layout != null) {
            buffer.append('=');
            buffer.append(concreteAlignment(this.hAlign, this.gridWidth == 1 ? layout.getColumnSpec(this.gridX) : null).abbreviation());
        }
        buffer.append(", ");
        buffer.append(this.vAlign.abbreviation());
        if (this.vAlign == DEFAULT && layout != null) {
            buffer.append('=');
            if (this.gridHeight == 1) {
                rowSpec = layout.getRowSpec(this.gridY);
            } else {
                rowSpec = null;
            }
            buffer.append(concreteAlignment(this.vAlign, rowSpec).abbreviation());
        }
        buffer.append("\"");
        if (!EMPTY_INSETS.equals(this.insets)) {
            buffer.append(", ");
            buffer.append(this.insets);
        }
        if (this.honorsVisibility != null) {
            buffer.append(this.honorsVisibility.booleanValue() ? "honors visibility" : "ignores visibility");
        }
        buffer.append(')');
        return buffer.toString();
    }

    public static final class Alignment implements Serializable {
        private static final int BOTH = 2;
        private static final int HORIZONTAL = 0;
        private static final int VERTICAL = 1;
        private static int nextOrdinal = 0;
        private final transient String name;
        private final int ordinal;
        private final transient int orientation;

        private Alignment(String name2, int orientation2) {
            int i = nextOrdinal;
            nextOrdinal = i + 1;
            this.ordinal = i;
            this.name = name2;
            this.orientation = orientation2;
        }

        static Alignment valueOf(String nameOrAbbreviation) {
            String str = nameOrAbbreviation.toLowerCase(Locale.ENGLISH);
            if (str.equals("d") || str.equals("default")) {
                return CellConstraints.DEFAULT;
            }
            if (str.equals("f") || str.equals(FlatClientProperties.TABBED_PANE_ALIGN_FILL)) {
                return CellConstraints.FILL;
            }
            if (str.equals("c") || str.equals(FlatClientProperties.TABBED_PANE_ALIGN_CENTER)) {
                return CellConstraints.CENTER;
            }
            if (str.equals("l") || str.equals("left")) {
                return CellConstraints.LEFT;
            }
            if (str.equals("r") || str.equals("right")) {
                return CellConstraints.RIGHT;
            }
            if (str.equals("t") || str.equals("top")) {
                return CellConstraints.TOP;
            }
            if (str.equals("b") || str.equals("bottom")) {
                return CellConstraints.BOTTOM;
            }
            throw new IllegalArgumentException("Invalid alignment " + nameOrAbbreviation + ". Must be one of: left, center, right, top, bottom, " + "fill, default, l, c, r, t, b, f, d.");
        }

        public String toString() {
            return this.name;
        }

        public char abbreviation() {
            return this.name.charAt(0);
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private boolean isHorizontal() {
            return this.orientation != 1;
        }

        /* access modifiers changed from: private */
        /* access modifiers changed from: public */
        private boolean isVertical() {
            return this.orientation != 0;
        }

        private Object readResolve() {
            return CellConstraints.VALUES[this.ordinal];
        }
    }

    private static String formatInt(int number) {
        String str = Integer.toString(number);
        return number < 10 ? " " + str : str;
    }
}
