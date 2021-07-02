package com.jgoodies.common.format;

import com.jgoodies.common.base.Objects;
import com.jgoodies.common.base.Preconditions;
import com.jgoodies.common.base.Strings;
import com.jgoodies.common.internal.Messages;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.ParsePosition;

public final class EmptyNumberFormat extends NumberFormat {
    private final NumberFormat delegate;
    private final Number emptyValue;

    public EmptyNumberFormat(NumberFormat delegate2) {
        this(delegate2, (Number) null);
    }

    public EmptyNumberFormat(NumberFormat delegate2, int emptyValue2) {
        this(delegate2, Integer.valueOf(emptyValue2));
    }

    public EmptyNumberFormat(NumberFormat delegate2, Number emptyValue2) {
        this.delegate = (NumberFormat) Preconditions.checkNotNull(delegate2, Messages.MUST_NOT_BE_NULL, "delegate format");
        this.emptyValue = emptyValue2;
    }

    @Override // java.text.NumberFormat
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        return Objects.equals(obj, this.emptyValue) ? toAppendTo : this.delegate.format(obj, toAppendTo, pos);
    }

    @Override // java.text.NumberFormat
    public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
        return this.delegate.format(number, toAppendTo, pos);
    }

    @Override // java.text.NumberFormat
    public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
        return this.delegate.format(number, toAppendTo, pos);
    }

    @Override // java.text.Format
    public Object parseObject(String source) throws ParseException {
        return Strings.isBlank(source) ? this.emptyValue : super.parseObject(source);
    }

    @Override // java.text.NumberFormat
    public Number parse(String source) throws ParseException {
        return Strings.isBlank(source) ? this.emptyValue : super.parse(source);
    }

    public Number parse(String source, ParsePosition pos) {
        return this.delegate.parse(source, pos);
    }
}
