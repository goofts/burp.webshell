package com.jgoodies.forms.layout;

import com.jgoodies.common.base.Preconditions;
import com.jgoodies.forms.layout.FormSpec;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class ColumnSpec extends FormSpec {
    private static final Map<String, ColumnSpec> CACHE = new HashMap();
    public static final FormSpec.DefaultAlignment CENTER = FormSpec.CENTER_ALIGN;
    public static final FormSpec.DefaultAlignment DEFAULT = FILL;
    public static final FormSpec.DefaultAlignment FILL = FormSpec.FILL_ALIGN;
    public static final FormSpec.DefaultAlignment LEFT = FormSpec.LEFT_ALIGN;
    public static final FormSpec.DefaultAlignment NONE = FormSpec.NO_ALIGN;
    public static final FormSpec.DefaultAlignment RIGHT = FormSpec.RIGHT_ALIGN;

    public ColumnSpec(FormSpec.DefaultAlignment defaultAlignment, Size size, double resizeWeight) {
        super(defaultAlignment, size, resizeWeight);
    }

    public ColumnSpec(Size size) {
        super(DEFAULT, size, 0.0d);
    }

    private ColumnSpec(String encodedDescription) {
        super(DEFAULT, encodedDescription);
    }

    public static ColumnSpec createGap(ConstantSize gapWidth) {
        return new ColumnSpec(DEFAULT, gapWidth, 0.0d);
    }

    public static ColumnSpec decode(String encodedColumnSpec) {
        return decode(encodedColumnSpec, LayoutMap.getRoot());
    }

    public static ColumnSpec decode(String encodedColumnSpec, LayoutMap layoutMap) {
        Preconditions.checkNotBlank(encodedColumnSpec, "The encoded column specification must not be null, empty or whitespace.");
        Preconditions.checkNotNull(layoutMap, "The LayoutMap must not be null.");
        return decodeExpanded(layoutMap.expand(encodedColumnSpec.trim().toLowerCase(Locale.ENGLISH), true));
    }

    static ColumnSpec decodeExpanded(String expandedTrimmedLowerCaseSpec) {
        ColumnSpec spec = CACHE.get(expandedTrimmedLowerCaseSpec);
        if (spec != null) {
            return spec;
        }
        ColumnSpec spec2 = new ColumnSpec(expandedTrimmedLowerCaseSpec);
        CACHE.put(expandedTrimmedLowerCaseSpec, spec2);
        return spec2;
    }

    public static ColumnSpec[] decodeSpecs(String encodedColumnSpecs) {
        return decodeSpecs(encodedColumnSpecs, LayoutMap.getRoot());
    }

    public static ColumnSpec[] decodeSpecs(String encodedColumnSpecs, LayoutMap layoutMap) {
        return FormSpecParser.parseColumnSpecs(encodedColumnSpecs, layoutMap);
    }

    /* access modifiers changed from: protected */
    @Override // com.jgoodies.forms.layout.FormSpec
    public boolean isHorizontal() {
        return true;
    }
}
