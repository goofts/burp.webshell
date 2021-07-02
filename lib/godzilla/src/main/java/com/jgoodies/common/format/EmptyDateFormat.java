package com.jgoodies.common.format;

import com.jgoodies.common.base.Objects;
import com.jgoodies.common.base.Strings;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Date;

public final class EmptyDateFormat extends AbstractWrappedDateFormat {
    private final Date emptyValue;

    public EmptyDateFormat(DateFormat delegate) {
        this(delegate, null);
    }

    public EmptyDateFormat(DateFormat delegate, Date emptyValue2) {
        super(delegate);
        this.emptyValue = emptyValue2;
    }

    @Override // com.jgoodies.common.format.AbstractWrappedDateFormat, java.text.DateFormat
    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition pos) {
        return Objects.equals(date, this.emptyValue) ? toAppendTo : this.delegate.format(date, toAppendTo, pos);
    }

    @Override // com.jgoodies.common.format.AbstractWrappedDateFormat
    public Date parse(String source, ParsePosition pos) {
        if (!Strings.isBlank(source)) {
            return this.delegate.parse(source, pos);
        }
        pos.setIndex(1);
        return this.emptyValue;
    }
}
