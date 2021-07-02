package com.jgoodies.common.format;

import com.jgoodies.common.base.Objects;
import com.jgoodies.common.base.Preconditions;
import com.jgoodies.common.base.Strings;
import com.jgoodies.common.internal.Messages;
import java.text.AttributedCharacterIterator;
import java.text.FieldPosition;
import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

public class EmptyFormat extends Format {
    private final Format delegate;
    private final Object emptyValue;

    public EmptyFormat(Format delegate2) {
        this(delegate2, null);
    }

    public EmptyFormat(Format delegate2, Object emptyValue2) {
        this.delegate = (Format) Preconditions.checkNotNull(delegate2, Messages.MUST_NOT_BE_NULL, "delegate format");
        this.emptyValue = emptyValue2;
    }

    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        return Objects.equals(obj, this.emptyValue) ? toAppendTo : this.delegate.format(obj, toAppendTo, pos);
    }

    @Override // java.text.Format
    public Object parseObject(String source) throws ParseException {
        return Strings.isBlank(source) ? this.emptyValue : super.parseObject(source);
    }

    public final Object parseObject(String source, ParsePosition pos) {
        return this.delegate.parseObject(source, pos);
    }

    public final AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        return this.delegate.formatToCharacterIterator(obj);
    }
}
