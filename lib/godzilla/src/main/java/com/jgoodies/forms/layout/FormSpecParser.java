package com.jgoodies.forms.layout;

import com.jgoodies.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FormSpecParser {
    private static final Pattern DIGIT_PATTERN = Pattern.compile("-?\\d+");
    private static final Pattern MULTIPLIER_PREFIX_PATTERN = Pattern.compile("-?\\d+\\s*\\*\\s*\\(");
    private final LayoutMap layoutMap;
    private final String source;

    private FormSpecParser(String source2, String description, LayoutMap layoutMap2, boolean horizontal) {
        Preconditions.checkNotNull(source2, "The %S must not be null.", description);
        Preconditions.checkNotNull(layoutMap2, "The LayoutMap must not be null.");
        this.layoutMap = layoutMap2;
        this.source = this.layoutMap.expand(source2, horizontal);
    }

    static ColumnSpec[] parseColumnSpecs(String encodedColumnSpecs, LayoutMap layoutMap2) {
        return new FormSpecParser(encodedColumnSpecs, "encoded column specifications", layoutMap2, true).parseColumnSpecs();
    }

    static RowSpec[] parseRowSpecs(String encodedRowSpecs, LayoutMap layoutMap2) {
        return new FormSpecParser(encodedRowSpecs, "encoded row specifications", layoutMap2, false).parseRowSpecs();
    }

    private ColumnSpec[] parseColumnSpecs() {
        List encodedColumnSpecs = split(this.source, 0);
        int columnCount = encodedColumnSpecs.size();
        ColumnSpec[] columnSpecs = new ColumnSpec[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnSpecs[i] = ColumnSpec.decodeExpanded(encodedColumnSpecs.get(i));
        }
        return columnSpecs;
    }

    private RowSpec[] parseRowSpecs() {
        List encodedRowSpecs = split(this.source, 0);
        int rowCount = encodedRowSpecs.size();
        RowSpec[] rowSpecs = new RowSpec[rowCount];
        for (int i = 0; i < rowCount; i++) {
            rowSpecs[i] = RowSpec.decodeExpanded(encodedRowSpecs.get(i));
        }
        return rowSpecs;
    }

    private List<String> split(String expression, int offset) {
        List<String> encodedSpecs = new ArrayList<>();
        int parenthesisLevel = 0;
        int bracketLevel = 0;
        int quoteLevel = 0;
        int length = expression.length();
        int specStart = 0;
        boolean lead = true;
        for (int i = 0; i < length; i++) {
            char c = expression.charAt(i);
            if (!lead || !Character.isWhitespace(c)) {
                lead = false;
                if (c == ',' && parenthesisLevel == 0 && bracketLevel == 0 && quoteLevel == 0) {
                    addSpec(encodedSpecs, expression.substring(specStart, i), offset + specStart);
                    specStart = i + 1;
                    lead = true;
                } else if (c == '(') {
                    if (bracketLevel > 0) {
                        fail(offset + i, "illegal '(' in [...]");
                    }
                    parenthesisLevel++;
                } else if (c == ')') {
                    if (bracketLevel > 0) {
                        fail(offset + i, "illegal ')' in [...]");
                    }
                    parenthesisLevel--;
                    if (parenthesisLevel < 0) {
                        fail(offset + i, "missing '('");
                    }
                } else if (c == '[') {
                    if (bracketLevel > 0) {
                        fail(offset + i, "too many '['");
                    }
                    bracketLevel++;
                } else if (c == ']') {
                    bracketLevel--;
                    if (bracketLevel < 0) {
                        fail(offset + i, "missing '['");
                    }
                } else if (c == '\'') {
                    if (quoteLevel == 0) {
                        quoteLevel++;
                    } else if (quoteLevel == 1) {
                        quoteLevel--;
                    }
                }
            } else {
                specStart++;
            }
        }
        if (parenthesisLevel > 0) {
            fail(offset + length, "missing ')'");
        }
        if (bracketLevel > 0) {
            fail(offset + length, "missing ']");
        }
        if (specStart < length) {
            addSpec(encodedSpecs, expression.substring(specStart), offset + specStart);
        }
        return encodedSpecs;
    }

    private void addSpec(List<String> encodedSpecs, String expression, int offset) {
        String trimmedExpression = expression.trim();
        Multiplier multiplier = multiplier(trimmedExpression, offset);
        if (multiplier == null) {
            encodedSpecs.add(trimmedExpression);
            return;
        }
        List<String> subTokenList = split(multiplier.expression, multiplier.offset + offset);
        for (int i = 0; i < multiplier.multiplier; i++) {
            encodedSpecs.addAll(subTokenList);
        }
    }

    private Multiplier multiplier(String expression, int offset) {
        Matcher matcher = MULTIPLIER_PREFIX_PATTERN.matcher(expression);
        if (!matcher.find()) {
            return null;
        }
        if (matcher.start() > 0) {
            fail(matcher.start() + offset, "illegal multiplier position");
        }
        Matcher digitMatcher = DIGIT_PATTERN.matcher(expression);
        if (!digitMatcher.find()) {
            return null;
        }
        String digitStr = expression.substring(0, digitMatcher.end());
        if (digitStr.startsWith("-")) {
            fail(offset, "illegal negative multiplier designation");
        }
        int number = 0;
        try {
            number = Integer.parseInt(digitStr);
        } catch (NumberFormatException ex) {
            fail(offset, ex);
        }
        if (number < 0) {
            fail(offset, "illegal negative multiplier");
        }
        return new Multiplier(number, expression.substring(matcher.end(), expression.length() - 1), matcher.end());
    }

    public static void fail(String source2, int index, String description) {
        throw new FormLayoutParseException(message(source2, index, description));
    }

    private void fail(int index, String description) {
        throw new FormLayoutParseException(message(this.source, index, description));
    }

    private void fail(int index, NumberFormatException cause) {
        throw new FormLayoutParseException(message(this.source, index, "Invalid multiplier"), cause);
    }

    private static String message(String source2, int index, String description) {
        StringBuffer buffer = new StringBuffer(10);
        buffer.append('\n');
        buffer.append(source2);
        buffer.append('\n');
        for (int i = 0; i < index; i++) {
            buffer.append(' ');
        }
        buffer.append('^');
        buffer.append(description);
        throw new FormLayoutParseException(buffer.toString());
    }

    public static final class FormLayoutParseException extends RuntimeException {
        FormLayoutParseException(String message) {
            super(message);
        }

        FormLayoutParseException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /* access modifiers changed from: package-private */
    public static final class Multiplier {
        final String expression;
        final int multiplier;
        final int offset;

        Multiplier(int multiplier2, String expression2, int offset2) {
            this.multiplier = multiplier2;
            this.expression = expression2;
            this.offset = offset2;
        }
    }
}
