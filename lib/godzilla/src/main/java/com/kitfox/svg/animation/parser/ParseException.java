package com.kitfox.svg.animation.parser;

import com.jgoodies.common.base.Strings;
import javassist.bytecode.Opcode;

public class ParseException extends Exception {
    protected static final String EOL = System.getProperty("line.separator", "\n");
    public Token currentToken;
    public int[][] expectedTokenSequences;
    public String[] tokenImage;

    public ParseException(Token currentTokenVal, int[][] expectedTokenSequencesVal, String[] tokenImageVal) {
        super(_initialise(currentTokenVal, expectedTokenSequencesVal, tokenImageVal));
        this.currentToken = currentTokenVal;
        this.expectedTokenSequences = expectedTokenSequencesVal;
        this.tokenImage = tokenImageVal;
    }

    public ParseException() {
    }

    public ParseException(String message) {
        super(message);
    }

    private static String _initialise(Token currentToken2, int[][] expectedTokenSequences2, String[] tokenImage2) {
        StringBuilder expected = new StringBuilder();
        int maxSize = 0;
        for (int i = 0; i < expectedTokenSequences2.length; i++) {
            if (maxSize < expectedTokenSequences2[i].length) {
                maxSize = expectedTokenSequences2[i].length;
            }
            for (int j = 0; j < expectedTokenSequences2[i].length; j++) {
                expected.append(tokenImage2[expectedTokenSequences2[i][j]]).append(' ');
            }
            if (expectedTokenSequences2[i][expectedTokenSequences2[i].length - 1] != 0) {
                expected.append(Strings.NO_ELLIPSIS_STRING);
            }
            expected.append(EOL).append("    ");
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Encountered \"");
        Token tok = currentToken2.next;
        int i2 = 0;
        while (true) {
            if (i2 >= maxSize) {
                break;
            }
            String escapedTokenText = add_escapes(tok.image);
            if (i2 != 0) {
                sb.append(' ');
            }
            if (tok.kind == 0) {
                sb.append(tokenImage2[0]);
                break;
            }
            sb.append(" " + tokenImage2[tok.kind]);
            sb.append(" \"");
            sb.append(escapedTokenText);
            sb.append("\"");
            tok = tok.next;
            i2++;
        }
        sb.append("\" at line ").append(currentToken2.next.beginLine).append(", column ").append(currentToken2.next.beginColumn);
        sb.append(".").append(EOL);
        if (expectedTokenSequences2.length != 0) {
            sb.append(EOL).append("Was expecting").append(expectedTokenSequences2.length == 1 ? ":" : " one of:").append(EOL).append(EOL).append((CharSequence) expected);
        }
        return sb.toString();
    }

    static String add_escapes(String str) {
        StringBuilder retval = new StringBuilder();
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            switch (ch) {
                case '\b':
                    retval.append("\\b");
                    break;
                case '\t':
                    retval.append("\\t");
                    break;
                case '\n':
                    retval.append("\\n");
                    break;
                case '\f':
                    retval.append("\\f");
                    break;
                case '\r':
                    retval.append("\\r");
                    break;
                case Opcode.FLOAD_0 /*{ENCODED_INT: 34}*/:
                    retval.append("\\\"");
                    break;
                case Opcode.DLOAD_1 /*{ENCODED_INT: 39}*/:
                    retval.append("\\'");
                    break;
                case Opcode.DUP2 /*{ENCODED_INT: 92}*/:
                    retval.append("\\\\");
                    break;
                default:
                    if (ch < ' ' || ch > '~') {
                        String s = "0000" + Integer.toString(ch, 16);
                        retval.append("\\u").append(s.substring(s.length() - 4, s.length()));
                        break;
                    } else {
                        retval.append(ch);
                        break;
                    }
                    break;
            }
        }
        return retval.toString();
    }
}
