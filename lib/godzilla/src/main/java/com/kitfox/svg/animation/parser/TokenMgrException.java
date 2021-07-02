package com.kitfox.svg.animation.parser;

import javassist.bytecode.Opcode;

public class TokenMgrException extends RuntimeException {
    public static final int INVALID_LEXICAL_STATE = 2;
    public static final int LEXICAL_ERROR = 0;
    public static final int LOOP_DETECTED = 3;
    public static final int STATIC_LEXER_ERROR = 1;
    private static final long serialVersionUID = 1;
    int errorCode;

    protected static final String addEscapes(String str) {
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

    protected static String LexicalErr(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, int curChar) {
        String str;
        char curChar1 = (char) curChar;
        StringBuilder append = new StringBuilder().append("Lexical error at line ").append(errorLine).append(", column ").append(errorColumn).append(".  Encountered: ");
        if (EOFSeen) {
            str = "<EOF> ";
        } else {
            str = "\"" + addEscapes(String.valueOf(curChar1)) + "\"" + " (" + curChar + "), ";
        }
        return append.append(str).append("after : \"").append(addEscapes(errorAfter)).append("\"").toString();
    }

    public String getMessage() {
        return super.getMessage();
    }

    public TokenMgrException() {
    }

    public TokenMgrException(String message, int reason) {
        super(message);
        this.errorCode = reason;
    }

    public TokenMgrException(boolean EOFSeen, int lexState, int errorLine, int errorColumn, String errorAfter, int curChar, int reason) {
        this(LexicalErr(EOFSeen, lexState, errorLine, errorColumn, errorAfter, curChar), reason);
    }
}
