package com.jgoodies.forms.layout;

import com.jgoodies.common.base.Preconditions;
import com.jgoodies.forms.layout.FormSpec;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class RowSpec extends FormSpec {
    public static final FormSpec.DefaultAlignment BOTTOM = FormSpec.BOTTOM_ALIGN;
    private static final Map<String, RowSpec> CACHE = new HashMap();
    public static final FormSpec.DefaultAlignment CENTER = FormSpec.CENTER_ALIGN;
    public static final FormSpec.DefaultAlignment DEFAULT = CENTER;
    public static final FormSpec.DefaultAlignment FILL = FormSpec.FILL_ALIGN;
    public static final FormSpec.DefaultAlignment TOP = FormSpec.TOP_ALIGN;

    public RowSpec(FormSpec.DefaultAlignment defaultAlignment, Size size, double resizeWeight) {
        super(defaultAlignment, size, resizeWeight);
    }

    public RowSpec(Size size) {
        super(DEFAULT, size, 0.0d);
    }

    private RowSpec(String encodedDescription) {
        super(DEFAULT, encodedDescription);
    }

    public static RowSpec createGap(ConstantSize gapHeight) {
        return new RowSpec(DEFAULT, gapHeight, 0.0d);
    }

    public static RowSpec decode(String encodedRowSpec) {
        return decode(encodedRowSpec, LayoutMap.getRoot());
    }

    public static RowSpec decode(String encodedRowSpec, LayoutMap layoutMap) {
        Preconditions.checkNotBlank(encodedRowSpec, "The encoded row specification must not be null, empty or whitespace.");
        Preconditions.checkNotNull(layoutMap, "The LayoutMap must not be null.");
        return decodeExpanded(layoutMap.expand(encodedRowSpec.trim().toLowerCase(Locale.ENGLISH), false));
    }

    static RowSpec decodeExpanded(String expandedTrimmedLowerCaseSpec) {
        RowSpec spec = CACHE.get(expandedTrimmedLowerCaseSpec);
        if (spec != null) {
            return spec;
        }
        RowSpec spec2 = new RowSpec(expandedTrimmedLowerCaseSpec);
        CACHE.put(expandedTrimmedLowerCaseSpec, spec2);
        return spec2;
    }

    public static RowSpec[] decodeSpecs(String encodedRowSpecs) {
        return decodeSpecs(encodedRowSpecs, LayoutMap.getRoot());
    }

    public static RowSpec[] decodeSpecs(String encodedRowSpecs, LayoutMap layoutMap) {
        return FormSpecParser.parseRowSpecs(encodedRowSpecs, layoutMap);
    }

    /* access modifiers changed from: protected */
    @Override // com.jgoodies.forms.layout.FormSpec
    public boolean isHorizontal() {
        return false;
    }
}
