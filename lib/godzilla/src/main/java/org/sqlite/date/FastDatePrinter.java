package org.sqlite.date;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javassist.bytecode.Opcode;

public class FastDatePrinter implements DatePrinter, Serializable {
    public static final int FULL = 0;
    public static final int LONG = 1;
    public static final int MEDIUM = 2;
    public static final int SHORT = 3;
    private static final ConcurrentMap<TimeZoneDisplayKey, String> cTimeZoneDisplayCache = new ConcurrentHashMap(7);
    private static final long serialVersionUID = 1;
    private final Locale mLocale;
    private transient int mMaxLengthEstimate;
    private final String mPattern;
    private transient Rule[] mRules;
    private final TimeZone mTimeZone;

    /* access modifiers changed from: private */
    public interface NumberRule extends Rule {
        void appendTo(StringBuffer stringBuffer, int i);
    }

    /* access modifiers changed from: private */
    public interface Rule {
        void appendTo(StringBuffer stringBuffer, Calendar calendar);

        int estimateLength();
    }

    protected FastDatePrinter(String pattern, TimeZone timeZone, Locale locale) {
        this.mPattern = pattern;
        this.mTimeZone = timeZone;
        this.mLocale = locale;
        init();
    }

    private void init() {
        List<Rule> rulesList = parsePattern();
        this.mRules = (Rule[]) rulesList.toArray(new Rule[rulesList.size()]);
        int len = 0;
        int i = this.mRules.length;
        while (true) {
            i--;
            if (i >= 0) {
                len += this.mRules[i].estimateLength();
            } else {
                this.mMaxLengthEstimate = len;
                return;
            }
        }
    }

    /* access modifiers changed from: protected */
    public List<Rule> parsePattern() {
        Rule rule;
        String[] strArr;
        DateFormatSymbols symbols = new DateFormatSymbols(this.mLocale);
        List<Rule> rules = new ArrayList<>();
        String[] ERAs = symbols.getEras();
        String[] months = symbols.getMonths();
        String[] shortMonths = symbols.getShortMonths();
        String[] weekdays = symbols.getWeekdays();
        String[] shortWeekdays = symbols.getShortWeekdays();
        String[] AmPmStrings = symbols.getAmPmStrings();
        int length = this.mPattern.length();
        int[] indexRef = new int[1];
        int i = 0;
        while (i < length) {
            indexRef[0] = i;
            String token = parseToken(this.mPattern, indexRef);
            int i2 = indexRef[0];
            int tokenLen = token.length();
            if (tokenLen == 0) {
                return rules;
            }
            switch (token.charAt(0)) {
                case Opcode.DLOAD_1:
                    String sub = token.substring(1);
                    if (sub.length() != 1) {
                        rule = new StringLiteral(sub);
                        break;
                    } else {
                        rule = new CharacterLiteral(sub.charAt(0));
                        break;
                    }
                case 'D':
                    rule = selectNumberRule(6, tokenLen);
                    break;
                case 'E':
                    if (tokenLen < 4) {
                        strArr = shortWeekdays;
                    } else {
                        strArr = weekdays;
                    }
                    rule = new TextField(7, strArr);
                    break;
                case Opcode.FSTORE_3:
                    rule = selectNumberRule(8, tokenLen);
                    break;
                case Opcode.DSTORE_0:
                    rule = new TextField(0, ERAs);
                    break;
                case Opcode.DSTORE_1:
                    rule = selectNumberRule(11, tokenLen);
                    break;
                case Opcode.ASTORE_0:
                    rule = selectNumberRule(10, tokenLen);
                    break;
                case Opcode.ASTORE_2:
                    if (tokenLen < 4) {
                        if (tokenLen != 3) {
                            if (tokenLen != 2) {
                                rule = UnpaddedMonthField.INSTANCE;
                                break;
                            } else {
                                rule = TwoDigitMonthField.INSTANCE;
                                break;
                            }
                        } else {
                            rule = new TextField(2, shortMonths);
                            break;
                        }
                    } else {
                        rule = new TextField(2, months);
                        break;
                    }
                case Opcode.AASTORE:
                    rule = selectNumberRule(14, tokenLen);
                    break;
                case Opcode.POP:
                    rule = selectNumberRule(4, tokenLen);
                    break;
                case Opcode.POP2:
                    rule = Iso8601_Rule.getRule(tokenLen);
                    break;
                case Opcode.DUP_X1:
                    if (tokenLen != 1) {
                        if (tokenLen != 2) {
                            rule = TimeZoneNumberRule.INSTANCE_COLON;
                            break;
                        } else {
                            rule = TimeZoneNumberRule.INSTANCE_ISO_8601;
                            break;
                        }
                    } else {
                        rule = TimeZoneNumberRule.INSTANCE_NO_COLON;
                        break;
                    }
                case Opcode.LADD:
                    rule = new TextField(9, AmPmStrings);
                    break;
                case 'd':
                    rule = selectNumberRule(5, tokenLen);
                    break;
                case 'h':
                    rule = new TwelveHourField(selectNumberRule(10, tokenLen));
                    break;
                case 'k':
                    rule = new TwentyFourHourField(selectNumberRule(11, tokenLen));
                    break;
                case Opcode.LDIV:
                    rule = selectNumberRule(12, tokenLen);
                    break;
                case Opcode.DREM:
                    rule = selectNumberRule(13, tokenLen);
                    break;
                case Opcode.DNEG:
                    rule = selectNumberRule(3, tokenLen);
                    break;
                case Opcode.LSHL:
                    if (tokenLen != 2) {
                        if (tokenLen < 4) {
                            tokenLen = 4;
                        }
                        rule = selectNumberRule(1, tokenLen);
                        break;
                    } else {
                        rule = TwoDigitYearField.INSTANCE;
                        break;
                    }
                case Opcode.ISHR:
                    if (tokenLen < 4) {
                        rule = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 0);
                        break;
                    } else {
                        rule = new TimeZoneNameRule(this.mTimeZone, this.mLocale, 1);
                        break;
                    }
                default:
                    throw new IllegalArgumentException("Illegal pattern component: " + token);
            }
            rules.add(rule);
            i = i2 + 1;
        }
        return rules;
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0069, code lost:
        r2 = r2 - 1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String parseToken(java.lang.String r13, int[] r14) {
        /*
        // Method dump skipped, instructions count: 118
        */
        throw new UnsupportedOperationException("Method not decompiled: org.sqlite.date.FastDatePrinter.parseToken(java.lang.String, int[]):java.lang.String");
    }

    /* access modifiers changed from: protected */
    public NumberRule selectNumberRule(int field, int padding) {
        switch (padding) {
            case 1:
                return new UnpaddedNumberField(field);
            case 2:
                return new TwoDigitNumberField(field);
            default:
                return new PaddedNumberField(field, padding);
        }
    }

    @Override // org.sqlite.date.DatePrinter
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
        String name;
        if (obj instanceof Date) {
            return format((Date) obj, toAppendTo);
        }
        if (obj instanceof Calendar) {
            return format((Calendar) obj, toAppendTo);
        }
        if (obj instanceof Long) {
            return format(((Long) obj).longValue(), toAppendTo);
        }
        StringBuilder append = new StringBuilder().append("Unknown class: ");
        if (obj == null) {
            name = "<null>";
        } else {
            name = obj.getClass().getName();
        }
        throw new IllegalArgumentException(append.append(name).toString());
    }

    @Override // org.sqlite.date.DatePrinter
    public String format(long millis) {
        Calendar c = newCalendar();
        c.setTimeInMillis(millis);
        return applyRulesToString(c);
    }

    private String applyRulesToString(Calendar c) {
        return applyRules(c, new StringBuffer(this.mMaxLengthEstimate)).toString();
    }

    private GregorianCalendar newCalendar() {
        return new GregorianCalendar(this.mTimeZone, this.mLocale);
    }

    @Override // org.sqlite.date.DatePrinter
    public String format(Date date) {
        Calendar c = newCalendar();
        c.setTime(date);
        return applyRulesToString(c);
    }

    @Override // org.sqlite.date.DatePrinter
    public String format(Calendar calendar) {
        return format(calendar, new StringBuffer(this.mMaxLengthEstimate)).toString();
    }

    @Override // org.sqlite.date.DatePrinter
    public StringBuffer format(long millis, StringBuffer buf) {
        return format(new Date(millis), buf);
    }

    @Override // org.sqlite.date.DatePrinter
    public StringBuffer format(Date date, StringBuffer buf) {
        Calendar c = newCalendar();
        c.setTime(date);
        return applyRules(c, buf);
    }

    @Override // org.sqlite.date.DatePrinter
    public StringBuffer format(Calendar calendar, StringBuffer buf) {
        return applyRules(calendar, buf);
    }

    /* access modifiers changed from: protected */
    public StringBuffer applyRules(Calendar calendar, StringBuffer buf) {
        for (Rule rule : this.mRules) {
            rule.appendTo(buf, calendar);
        }
        return buf;
    }

    @Override // org.sqlite.date.DatePrinter
    public String getPattern() {
        return this.mPattern;
    }

    @Override // org.sqlite.date.DatePrinter
    public TimeZone getTimeZone() {
        return this.mTimeZone;
    }

    @Override // org.sqlite.date.DatePrinter
    public Locale getLocale() {
        return this.mLocale;
    }

    public int getMaxLengthEstimate() {
        return this.mMaxLengthEstimate;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof FastDatePrinter)) {
            return false;
        }
        FastDatePrinter other = (FastDatePrinter) obj;
        if (!this.mPattern.equals(other.mPattern) || !this.mTimeZone.equals(other.mTimeZone) || !this.mLocale.equals(other.mLocale)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.mPattern.hashCode() + ((this.mTimeZone.hashCode() + (this.mLocale.hashCode() * 13)) * 13);
    }

    public String toString() {
        return "FastDatePrinter[" + this.mPattern + "," + this.mLocale + "," + this.mTimeZone.getID() + "]";
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        init();
    }

    /* access modifiers changed from: private */
    public static void appendDigits(StringBuffer buffer, int value) {
        buffer.append((char) ((value / 10) + 48));
        buffer.append((char) ((value % 10) + 48));
    }

    /* access modifiers changed from: private */
    public static class CharacterLiteral implements Rule {
        private final char mValue;

        CharacterLiteral(char value) {
            this.mValue = value;
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public int estimateLength() {
            return 1;
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            buffer.append(this.mValue);
        }
    }

    /* access modifiers changed from: private */
    public static class StringLiteral implements Rule {
        private final String mValue;

        StringLiteral(String value) {
            this.mValue = value;
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public int estimateLength() {
            return this.mValue.length();
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            buffer.append(this.mValue);
        }
    }

    /* access modifiers changed from: private */
    public static class TextField implements Rule {
        private final int mField;
        private final String[] mValues;

        TextField(int field, String[] values) {
            this.mField = field;
            this.mValues = values;
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public int estimateLength() {
            int max = 0;
            int i = this.mValues.length;
            while (true) {
                i--;
                if (i < 0) {
                    return max;
                }
                int len = this.mValues[i].length();
                if (len > max) {
                    max = len;
                }
            }
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            buffer.append(this.mValues[calendar.get(this.mField)]);
        }
    }

    /* access modifiers changed from: private */
    public static class UnpaddedNumberField implements NumberRule {
        private final int mField;

        UnpaddedNumberField(int field) {
            this.mField = field;
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public int estimateLength() {
            return 4;
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(this.mField));
        }

        @Override // org.sqlite.date.FastDatePrinter.NumberRule
        public final void appendTo(StringBuffer buffer, int value) {
            if (value < 10) {
                buffer.append((char) (value + 48));
            } else if (value < 100) {
                FastDatePrinter.appendDigits(buffer, value);
            } else {
                buffer.append(value);
            }
        }
    }

    /* access modifiers changed from: private */
    public static class UnpaddedMonthField implements NumberRule {
        static final UnpaddedMonthField INSTANCE = new UnpaddedMonthField();

        UnpaddedMonthField() {
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public int estimateLength() {
            return 2;
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(2) + 1);
        }

        @Override // org.sqlite.date.FastDatePrinter.NumberRule
        public final void appendTo(StringBuffer buffer, int value) {
            if (value < 10) {
                buffer.append((char) (value + 48));
            } else {
                FastDatePrinter.appendDigits(buffer, value);
            }
        }
    }

    /* access modifiers changed from: private */
    public static class PaddedNumberField implements NumberRule {
        private final int mField;
        private final int mSize;

        PaddedNumberField(int field, int size) {
            if (size < 3) {
                throw new IllegalArgumentException();
            }
            this.mField = field;
            this.mSize = size;
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public int estimateLength() {
            return this.mSize;
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(this.mField));
        }

        @Override // org.sqlite.date.FastDatePrinter.NumberRule
        public final void appendTo(StringBuffer buffer, int value) {
            for (int digit = 0; digit < this.mSize; digit++) {
                buffer.append('0');
            }
            int index = buffer.length();
            while (value > 0) {
                index--;
                buffer.setCharAt(index, (char) ((value % 10) + 48));
                value /= 10;
            }
        }
    }

    /* access modifiers changed from: private */
    public static class TwoDigitNumberField implements NumberRule {
        private final int mField;

        TwoDigitNumberField(int field) {
            this.mField = field;
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public int estimateLength() {
            return 2;
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(this.mField));
        }

        @Override // org.sqlite.date.FastDatePrinter.NumberRule
        public final void appendTo(StringBuffer buffer, int value) {
            if (value < 100) {
                FastDatePrinter.appendDigits(buffer, value);
            } else {
                buffer.append(value);
            }
        }
    }

    /* access modifiers changed from: private */
    public static class TwoDigitYearField implements NumberRule {
        static final TwoDigitYearField INSTANCE = new TwoDigitYearField();

        TwoDigitYearField() {
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public int estimateLength() {
            return 2;
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(1) % 100);
        }

        @Override // org.sqlite.date.FastDatePrinter.NumberRule
        public final void appendTo(StringBuffer buffer, int value) {
            FastDatePrinter.appendDigits(buffer, value);
        }
    }

    /* access modifiers changed from: private */
    public static class TwoDigitMonthField implements NumberRule {
        static final TwoDigitMonthField INSTANCE = new TwoDigitMonthField();

        TwoDigitMonthField() {
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public int estimateLength() {
            return 2;
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            appendTo(buffer, calendar.get(2) + 1);
        }

        @Override // org.sqlite.date.FastDatePrinter.NumberRule
        public final void appendTo(StringBuffer buffer, int value) {
            FastDatePrinter.appendDigits(buffer, value);
        }
    }

    /* access modifiers changed from: private */
    public static class TwelveHourField implements NumberRule {
        private final NumberRule mRule;

        TwelveHourField(NumberRule rule) {
            this.mRule = rule;
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public int estimateLength() {
            return this.mRule.estimateLength();
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            int value = calendar.get(10);
            if (value == 0) {
                value = calendar.getLeastMaximum(10) + 1;
            }
            this.mRule.appendTo(buffer, value);
        }

        @Override // org.sqlite.date.FastDatePrinter.NumberRule
        public void appendTo(StringBuffer buffer, int value) {
            this.mRule.appendTo(buffer, value);
        }
    }

    /* access modifiers changed from: private */
    public static class TwentyFourHourField implements NumberRule {
        private final NumberRule mRule;

        TwentyFourHourField(NumberRule rule) {
            this.mRule = rule;
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public int estimateLength() {
            return this.mRule.estimateLength();
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            int value = calendar.get(11);
            if (value == 0) {
                value = calendar.getMaximum(11) + 1;
            }
            this.mRule.appendTo(buffer, value);
        }

        @Override // org.sqlite.date.FastDatePrinter.NumberRule
        public void appendTo(StringBuffer buffer, int value) {
            this.mRule.appendTo(buffer, value);
        }
    }

    static String getTimeZoneDisplay(TimeZone tz, boolean daylight, int style, Locale locale) {
        TimeZoneDisplayKey key = new TimeZoneDisplayKey(tz, daylight, style, locale);
        String value = cTimeZoneDisplayCache.get(key);
        if (value != null) {
            return value;
        }
        String value2 = tz.getDisplayName(daylight, style, locale);
        String prior = cTimeZoneDisplayCache.putIfAbsent(key, value2);
        return prior != null ? prior : value2;
    }

    /* access modifiers changed from: private */
    public static class TimeZoneNameRule implements Rule {
        private final String mDaylight;
        private final Locale mLocale;
        private final String mStandard;
        private final int mStyle;

        TimeZoneNameRule(TimeZone timeZone, Locale locale, int style) {
            this.mLocale = locale;
            this.mStyle = style;
            this.mStandard = FastDatePrinter.getTimeZoneDisplay(timeZone, false, style, locale);
            this.mDaylight = FastDatePrinter.getTimeZoneDisplay(timeZone, true, style, locale);
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public int estimateLength() {
            return Math.max(this.mStandard.length(), this.mDaylight.length());
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            TimeZone zone = calendar.getTimeZone();
            if (calendar.get(16) != 0) {
                buffer.append(FastDatePrinter.getTimeZoneDisplay(zone, true, this.mStyle, this.mLocale));
            } else {
                buffer.append(FastDatePrinter.getTimeZoneDisplay(zone, false, this.mStyle, this.mLocale));
            }
        }
    }

    /* access modifiers changed from: private */
    public static class TimeZoneNumberRule implements Rule {
        static final TimeZoneNumberRule INSTANCE_COLON = new TimeZoneNumberRule(true, false);
        static final TimeZoneNumberRule INSTANCE_ISO_8601 = new TimeZoneNumberRule(true, true);
        static final TimeZoneNumberRule INSTANCE_NO_COLON = new TimeZoneNumberRule(false, false);
        final boolean mColon;
        final boolean mISO8601;

        TimeZoneNumberRule(boolean colon, boolean iso8601) {
            this.mColon = colon;
            this.mISO8601 = iso8601;
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public int estimateLength() {
            return 5;
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            if (!this.mISO8601 || !calendar.getTimeZone().getID().equals("UTC")) {
                int offset = calendar.get(15) + calendar.get(16);
                if (offset < 0) {
                    buffer.append('-');
                    offset = -offset;
                } else {
                    buffer.append('+');
                }
                int hours = offset / 3600000;
                FastDatePrinter.appendDigits(buffer, hours);
                if (this.mColon) {
                    buffer.append(':');
                }
                FastDatePrinter.appendDigits(buffer, (offset / 60000) - (hours * 60));
                return;
            }
            buffer.append("Z");
        }
    }

    /* access modifiers changed from: private */
    public static class Iso8601_Rule implements Rule {
        static final Iso8601_Rule ISO8601_HOURS = new Iso8601_Rule(3);
        static final Iso8601_Rule ISO8601_HOURS_COLON_MINUTES = new Iso8601_Rule(6);
        static final Iso8601_Rule ISO8601_HOURS_MINUTES = new Iso8601_Rule(5);
        final int length;

        static Iso8601_Rule getRule(int tokenLen) {
            switch (tokenLen) {
                case 1:
                    return ISO8601_HOURS;
                case 2:
                    return ISO8601_HOURS_MINUTES;
                case 3:
                    return ISO8601_HOURS_COLON_MINUTES;
                default:
                    throw new IllegalArgumentException("invalid number of X");
            }
        }

        Iso8601_Rule(int length2) {
            this.length = length2;
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public int estimateLength() {
            return this.length;
        }

        @Override // org.sqlite.date.FastDatePrinter.Rule
        public void appendTo(StringBuffer buffer, Calendar calendar) {
            int zoneOffset = calendar.get(15);
            if (zoneOffset == 0) {
                buffer.append("Z");
                return;
            }
            int offset = zoneOffset + calendar.get(16);
            if (offset < 0) {
                buffer.append('-');
                offset = -offset;
            } else {
                buffer.append('+');
            }
            int hours = offset / 3600000;
            FastDatePrinter.appendDigits(buffer, hours);
            if (this.length >= 5) {
                if (this.length == 6) {
                    buffer.append(':');
                }
                FastDatePrinter.appendDigits(buffer, (offset / 60000) - (hours * 60));
            }
        }
    }

    /* access modifiers changed from: private */
    public static class TimeZoneDisplayKey {
        private final Locale mLocale;
        private final int mStyle;
        private final TimeZone mTimeZone;

        TimeZoneDisplayKey(TimeZone timeZone, boolean daylight, int style, Locale locale) {
            this.mTimeZone = timeZone;
            if (daylight) {
                this.mStyle = Integer.MIN_VALUE | style;
            } else {
                this.mStyle = style;
            }
            this.mLocale = locale;
        }

        public int hashCode() {
            return (((this.mStyle * 31) + this.mLocale.hashCode()) * 31) + this.mTimeZone.hashCode();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof TimeZoneDisplayKey)) {
                return false;
            }
            TimeZoneDisplayKey other = (TimeZoneDisplayKey) obj;
            return this.mTimeZone.equals(other.mTimeZone) && this.mStyle == other.mStyle && this.mLocale.equals(other.mLocale);
        }
    }
}
