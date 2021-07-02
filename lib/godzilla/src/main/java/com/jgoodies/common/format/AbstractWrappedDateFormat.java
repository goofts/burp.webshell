package com.jgoodies.common.format;

import com.jgoodies.common.base.Preconditions;
import com.jgoodies.common.internal.Messages;
import java.text.AttributedCharacterIterator;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public abstract class AbstractWrappedDateFormat extends DateFormat {
    protected final DateFormat delegate;

    @Override // java.text.DateFormat
    public abstract StringBuffer format(Date date, StringBuffer stringBuffer, FieldPosition fieldPosition);

    public abstract Date parse(String str, ParsePosition parsePosition);

    public AbstractWrappedDateFormat(DateFormat delegate2) {
        this.delegate = (DateFormat) Preconditions.checkNotNull(delegate2, Messages.MUST_NOT_BE_NULL, "delegate format");
    }

    public Calendar getCalendar() {
        return this.delegate.getCalendar();
    }

    public void setCalendar(Calendar newCalendar) {
        this.delegate.setCalendar(newCalendar);
    }

    public NumberFormat getNumberFormat() {
        return this.delegate.getNumberFormat();
    }

    public void setNumberFormat(NumberFormat newNumberFormat) {
        this.delegate.setNumberFormat(newNumberFormat);
    }

    public TimeZone getTimeZone() {
        return this.delegate.getTimeZone();
    }

    public void setTimeZone(TimeZone zone) {
        this.delegate.setTimeZone(zone);
    }

    public boolean isLenient() {
        return this.delegate.isLenient();
    }

    public void setLenient(boolean lenient) {
        this.delegate.setLenient(lenient);
    }

    public AttributedCharacterIterator formatToCharacterIterator(Object obj) {
        return this.delegate.formatToCharacterIterator(obj);
    }
}
