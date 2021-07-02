package com.jgoodies.forms.layout;

import com.formdev.flatlaf.FlatClientProperties;
import com.jgoodies.common.base.Preconditions;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.Sizes;
import java.awt.Container;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

public abstract class FormSpec implements Serializable {
    static final DefaultAlignment BOTTOM_ALIGN = new DefaultAlignment("bottom");
    private static final Pattern BOUNDS_SEPARATOR_PATTERN = Pattern.compile("\\s*,\\s*");
    static final DefaultAlignment CENTER_ALIGN = new DefaultAlignment(FlatClientProperties.TABBED_PANE_ALIGN_CENTER);
    public static final double DEFAULT_GROW = 1.0d;
    static final DefaultAlignment FILL_ALIGN = new DefaultAlignment(FlatClientProperties.TABBED_PANE_ALIGN_FILL);
    static final DefaultAlignment LEFT_ALIGN = new DefaultAlignment("left");
    static final DefaultAlignment NO_ALIGN = new DefaultAlignment("none");
    public static final double NO_GROW = 0.0d;
    static final DefaultAlignment RIGHT_ALIGN = new DefaultAlignment("right");
    private static final Pattern TOKEN_SEPARATOR_PATTERN = Pattern.compile(":");
    static final DefaultAlignment TOP_ALIGN = new DefaultAlignment("top");
    private static final DefaultAlignment[] VALUES = {LEFT_ALIGN, RIGHT_ALIGN, TOP_ALIGN, BOTTOM_ALIGN, CENTER_ALIGN, FILL_ALIGN, NO_ALIGN};
    private DefaultAlignment defaultAlignment;
    private boolean defaultAlignmentExplicitlySet;
    private double resizeWeight;
    private Size size;

    /* access modifiers changed from: package-private */
    public abstract boolean isHorizontal();

    protected FormSpec(DefaultAlignment defaultAlignment2, Size size2, double resizeWeight2) {
        Preconditions.checkNotNull(size2, "The size must not be null.");
        Preconditions.checkArgument(resizeWeight2 >= 0.0d, "The resize weight must be non-negative.");
        this.defaultAlignment = defaultAlignment2;
        this.size = size2;
        this.resizeWeight = resizeWeight2;
    }

    protected FormSpec(DefaultAlignment defaultAlignment2, String encodedDescription) {
        this(defaultAlignment2, Sizes.DEFAULT, 0.0d);
        parseAndInitValues(encodedDescription.toLowerCase(Locale.ENGLISH));
    }

    public final DefaultAlignment getDefaultAlignment() {
        return this.defaultAlignment;
    }

    public final boolean getDefaultAlignmentExplictlySet() {
        return this.defaultAlignmentExplicitlySet;
    }

    public final Size getSize() {
        return this.size;
    }

    public final double getResizeWeight() {
        return this.resizeWeight;
    }

    /* access modifiers changed from: package-private */
    public final boolean canGrow() {
        return getResizeWeight() != 0.0d;
    }

    /* access modifiers changed from: package-private */
    public void setDefaultAlignment(DefaultAlignment defaultAlignment2) {
        this.defaultAlignment = defaultAlignment2;
        this.defaultAlignmentExplicitlySet = true;
    }

    /* access modifiers changed from: package-private */
    public void setSize(Size size2) {
        this.size = size2;
    }

    /* access modifiers changed from: package-private */
    public void setResizeWeight(double resizeWeight2) {
        this.resizeWeight = resizeWeight2;
    }

    private void parseAndInitValues(String encodedDescription) {
        boolean z;
        int nextIndex;
        boolean z2 = true;
        Preconditions.checkNotBlank(encodedDescription, "The encoded form specification must not be null, empty or whitespace.");
        String[] token = TOKEN_SEPARATOR_PATTERN.split(encodedDescription);
        if (token.length > 0) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "The form spec must not be empty.");
        int nextIndex2 = 0 + 1;
        String next = token[0];
        DefaultAlignment alignment = DefaultAlignment.valueOf(next, isHorizontal());
        if (alignment != null) {
            setDefaultAlignment(alignment);
            if (token.length <= 1) {
                z2 = false;
            }
            Preconditions.checkArgument(z2, "The form spec must provide a size.");
            nextIndex = nextIndex2 + 1;
            next = token[nextIndex2];
        } else {
            nextIndex = nextIndex2;
        }
        setSize(parseSize(next));
        if (nextIndex < token.length) {
            setResizeWeight(parseResizeWeight(token[nextIndex]));
        }
    }

    private Size parseSize(String token) {
        if (token.startsWith("[") && token.endsWith("]")) {
            return parseBoundedSize(token);
        }
        if (token.startsWith("max(") && token.endsWith(")")) {
            return parseOldBoundedSize(token, false);
        }
        if (!token.startsWith("min(") || !token.endsWith(")")) {
            return parseAtomicSize(token);
        }
        return parseOldBoundedSize(token, true);
    }

    private Size parseBoundedSize(String token) {
        String[] subtoken = BOUNDS_SEPARATOR_PATTERN.split(token.substring(1, token.length() - 1));
        Size basis = null;
        Size lower = null;
        Size upper = null;
        if (subtoken.length == 2) {
            Size size1 = parseAtomicSize(subtoken[0]);
            Size size2 = parseAtomicSize(subtoken[1]);
            if (!isConstant(size1)) {
                basis = size1;
                upper = size2;
            } else if (isConstant(size2)) {
                lower = size1;
                basis = size2;
                upper = size2;
            } else {
                lower = size1;
                basis = size2;
            }
        } else if (subtoken.length == 3) {
            lower = parseAtomicSize(subtoken[0]);
            basis = parseAtomicSize(subtoken[1]);
            upper = parseAtomicSize(subtoken[2]);
        }
        if ((lower == null || isConstant(lower)) && (upper == null || isConstant(upper))) {
            return new BoundedSize(basis, lower, upper);
        }
        throw new IllegalArgumentException("Illegal bounded size '" + token + "'. Must be one of:" + "\n[<constant size>,<logical size>]                 // lower bound" + "\n[<logical size>,<constant size>]                 // upper bound" + "\n[<constant size>,<logical size>,<constant size>] // lower and upper bound." + "\nExamples:" + "\n[50dlu,pref]                                     // lower bound" + "\n[pref,200dlu]                                    // upper bound" + "\n[50dlu,pref,200dlu]                              // lower and upper bound.");
    }

    private Size parseOldBoundedSize(String token, boolean setMax) {
        Size size2;
        int semicolonIndex = token.indexOf(59);
        String sizeToken1 = token.substring(4, semicolonIndex);
        String sizeToken2 = token.substring(semicolonIndex + 1, token.length() - 1);
        Size size1 = parseAtomicSize(sizeToken1);
        Size size22 = parseAtomicSize(sizeToken2);
        if (isConstant(size1)) {
            if (size22 instanceof Sizes.ComponentSize) {
                if (setMax) {
                    size2 = null;
                } else {
                    size2 = size1;
                }
                if (!setMax) {
                    size1 = null;
                }
                return new BoundedSize(size22, size2, size1);
            }
            throw new IllegalArgumentException("Bounded sizes must not be both constants.");
        } else if (isConstant(size22)) {
            Size size3 = setMax ? null : size22;
            if (!setMax) {
                size22 = null;
            }
            return new BoundedSize(size1, size3, size22);
        } else {
            throw new IllegalArgumentException("Bounded sizes must not be both logical.");
        }
    }

    private Size parseAtomicSize(String token) {
        String trimmedToken = token.trim();
        if (!trimmedToken.startsWith("'") || !trimmedToken.endsWith("'")) {
            Sizes.ComponentSize componentSize = Sizes.ComponentSize.valueOf(trimmedToken);
            return componentSize == null ? ConstantSize.valueOf(trimmedToken, isHorizontal()) : componentSize;
        }
        int length = trimmedToken.length();
        if (length >= 2) {
            return new PrototypeSize(trimmedToken.substring(1, length - 1));
        }
        throw new IllegalArgumentException("Missing closing \"'\" for prototype.");
    }

    private static double parseResizeWeight(String token) {
        if (token.equals("g") || token.equals("grow")) {
            return 1.0d;
        }
        if (token.equals("n") || token.equals("nogrow") || token.equals("none")) {
            return 0.0d;
        }
        if ((token.startsWith("grow(") || token.startsWith("g(")) && token.endsWith(")")) {
            return Double.parseDouble(token.substring(token.indexOf(40) + 1, token.indexOf(41)));
        }
        throw new IllegalArgumentException("The resize argument '" + token + "' is invalid. " + " Must be one of: grow, g, none, n, grow(<double>), g(<double>)");
    }

    private static boolean isConstant(Size aSize) {
        return (aSize instanceof ConstantSize) || (aSize instanceof PrototypeSize);
    }

    public final String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.defaultAlignment);
        buffer.append(":");
        buffer.append(this.size.toString());
        buffer.append(':');
        if (this.resizeWeight == 0.0d) {
            buffer.append("noGrow");
        } else if (this.resizeWeight == 1.0d) {
            buffer.append("grow");
        } else {
            buffer.append("grow(");
            buffer.append(this.resizeWeight);
            buffer.append(')');
        }
        return buffer.toString();
    }

    public final String toShortString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(this.defaultAlignment.abbreviation());
        buffer.append(":");
        buffer.append(this.size.toString());
        buffer.append(':');
        if (this.resizeWeight == 0.0d) {
            buffer.append("n");
        } else if (this.resizeWeight == 1.0d) {
            buffer.append("g");
        } else {
            buffer.append("g(");
            buffer.append(this.resizeWeight);
            buffer.append(')');
        }
        return buffer.toString();
    }

    public final String encode() {
        StringBuffer buffer = new StringBuffer();
        if (!(isHorizontal() ? ColumnSpec.DEFAULT : RowSpec.DEFAULT).equals(this.defaultAlignment)) {
            buffer.append(this.defaultAlignment.abbreviation());
            buffer.append(":");
        }
        buffer.append(this.size.encode());
        if (this.resizeWeight != 0.0d) {
            if (this.resizeWeight == 1.0d) {
                buffer.append(':');
                buffer.append("g");
            } else {
                buffer.append(':');
                buffer.append("g(");
                buffer.append(this.resizeWeight);
                buffer.append(')');
            }
        }
        return buffer.toString();
    }

    /* access modifiers changed from: package-private */
    public final int maximumSize(Container container, List components, FormLayout.Measure minMeasure, FormLayout.Measure prefMeasure, FormLayout.Measure defaultMeasure) {
        return this.size.maximumSize(container, components, minMeasure, prefMeasure, defaultMeasure);
    }

    public static final class DefaultAlignment implements Serializable {
        private static int nextOrdinal = 0;
        private final transient String name;
        private final int ordinal;

        private DefaultAlignment(String name2) {
            int i = nextOrdinal;
            nextOrdinal = i + 1;
            this.ordinal = i;
            this.name = name2;
        }

        /* access modifiers changed from: private */
        public static DefaultAlignment valueOf(String str, boolean isHorizontal) {
            if (str.equals("f") || str.equals(FlatClientProperties.TABBED_PANE_ALIGN_FILL)) {
                return FormSpec.FILL_ALIGN;
            }
            if (str.equals("c") || str.equals(FlatClientProperties.TABBED_PANE_ALIGN_CENTER)) {
                return FormSpec.CENTER_ALIGN;
            }
            if (isHorizontal) {
                if (str.equals("r") || str.equals("right")) {
                    return FormSpec.RIGHT_ALIGN;
                }
                if (str.equals("l") || str.equals("left")) {
                    return FormSpec.LEFT_ALIGN;
                }
                if (str.equals("none")) {
                    return FormSpec.NO_ALIGN;
                }
                return null;
            } else if (str.equals("t") || str.equals("top")) {
                return FormSpec.TOP_ALIGN;
            } else {
                if (str.equals("b") || str.equals("bottom")) {
                    return FormSpec.BOTTOM_ALIGN;
                }
                return null;
            }
        }

        public String toString() {
            return this.name;
        }

        public char abbreviation() {
            return this.name.charAt(0);
        }

        private Object readResolve() {
            return FormSpec.VALUES[this.ordinal];
        }
    }
}
