package javassist.compiler;

import javassist.bytecode.Opcode;

public class Lex implements TokenId {
    private static final int[] equalOps = {TokenId.NEQ, 0, 0, 0, TokenId.MOD_E, TokenId.AND_E, 0, 0, 0, TokenId.MUL_E, TokenId.PLUS_E, 0, TokenId.MINUS_E, 0, TokenId.DIV_E, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, TokenId.LE, TokenId.EQ, TokenId.GE, 0};
    private static final KeywordTable ktable = new KeywordTable();
    private Token currentToken = new Token();
    private String input;
    private int lastChar = -1;
    private int lineNumber;
    private Token lookAheadTokens = null;
    private int maxlen;
    private int position;
    private StringBuffer textBuffer = new StringBuffer();

    public Lex(String s) {
        this.input = s;
        this.position = 0;
        this.maxlen = s.length();
        this.lineNumber = 0;
    }

    public int get() {
        if (this.lookAheadTokens == null) {
            return get(this.currentToken);
        }
        Token t = this.lookAheadTokens;
        this.currentToken = t;
        this.lookAheadTokens = this.lookAheadTokens.next;
        return t.tokenId;
    }

    public int lookAhead() {
        return lookAhead(0);
    }

    public int lookAhead(int i) {
        Token tk = this.lookAheadTokens;
        if (tk == null) {
            tk = this.currentToken;
            this.lookAheadTokens = tk;
            tk.next = null;
            get(tk);
        }
        while (true) {
            i--;
            if (i > 0) {
                if (tk.next == null) {
                    Token tk2 = new Token();
                    tk.next = tk2;
                    get(tk2);
                }
                tk = tk.next;
            } else {
                this.currentToken = tk;
                return tk.tokenId;
            }
        }
    }

    public String getString() {
        return this.currentToken.textValue;
    }

    public long getLong() {
        return this.currentToken.longValue;
    }

    public double getDouble() {
        return this.currentToken.doubleValue;
    }

    private int get(Token token) {
        int t;
        do {
            t = readLine(token);
        } while (t == 10);
        token.tokenId = t;
        return t;
    }

    private int readLine(Token token) {
        int c = getNextNonWhiteChar();
        if (c < 0) {
            return c;
        }
        if (c == 10) {
            this.lineNumber++;
            return 10;
        } else if (c == 39) {
            return readCharConst(token);
        } else {
            if (c == 34) {
                return readStringL(token);
            }
            if (48 <= c && c <= 57) {
                return readNumber(c, token);
            }
            if (c == 46) {
                int c2 = getc();
                if (48 > c2 || c2 > 57) {
                    ungetc(c2);
                    return readSeparator(46);
                }
                StringBuffer tbuf = this.textBuffer;
                tbuf.setLength(0);
                tbuf.append('.');
                return readDouble(tbuf, c2, token);
            } else if (Character.isJavaIdentifierStart((char) c)) {
                return readIdentifier(c, token);
            } else {
                return readSeparator(c);
            }
        }
    }

    private int getNextNonWhiteChar() {
        int c;
        do {
            c = getc();
            if (c == 47) {
                int c2 = getc();
                if (c2 == 47) {
                    do {
                        c = getc();
                        if (c == 10 || c == 13) {
                            break;
                        }
                    } while (c != -1);
                } else if (c2 == 42) {
                    while (true) {
                        c = getc();
                        if (c == -1) {
                            break;
                        } else if (c == 42) {
                            int c3 = getc();
                            if (c3 == 47) {
                                c = 32;
                                break;
                            }
                            ungetc(c3);
                        }
                    }
                } else {
                    ungetc(c2);
                    c = 47;
                }
            }
        } while (isBlank(c));
        return c;
    }

    private int readCharConst(Token token) {
        int value = 0;
        while (true) {
            int c = getc();
            if (c == 39) {
                token.longValue = (long) value;
                return TokenId.CharConstant;
            } else if (c == 92) {
                value = readEscapeChar();
            } else if (c < 32) {
                if (c == 10) {
                    this.lineNumber++;
                }
                return TokenId.BadToken;
            } else {
                value = c;
            }
        }
    }

    private int readEscapeChar() {
        int c = getc();
        if (c == 110) {
            return 10;
        }
        if (c == 116) {
            return 9;
        }
        if (c == 114) {
            return 13;
        }
        if (c == 102) {
            return 12;
        }
        if (c != 10) {
            return c;
        }
        this.lineNumber++;
        return c;
    }

    private int readStringL(Token token) {
        int c;
        StringBuffer tbuf = this.textBuffer;
        tbuf.setLength(0);
        while (true) {
            int c2 = getc();
            if (c2 != 34) {
                if (c2 == 92) {
                    c2 = readEscapeChar();
                } else if (c2 == 10 || c2 < 0) {
                    this.lineNumber++;
                }
                tbuf.append((char) c2);
            } else {
                while (true) {
                    c = getc();
                    if (c == 10) {
                        this.lineNumber++;
                    } else if (!isBlank(c)) {
                        break;
                    }
                }
                if (c != 34) {
                    ungetc(c);
                    token.textValue = tbuf.toString();
                    return TokenId.StringL;
                }
            }
        }
        this.lineNumber++;
        return TokenId.BadToken;
    }

    private int readNumber(int c, Token token) {
        int c2;
        int c3;
        long value = 0;
        int c22 = getc();
        if (c == 48) {
            if (c22 == 88 || c22 == 120) {
                while (true) {
                    c2 = getc();
                    if (48 <= c2 && c2 <= 57) {
                        value = (16 * value) + ((long) (c2 - 48));
                    } else if (65 <= c2 && c2 <= 70) {
                        value = (16 * value) + ((long) ((c2 - 65) + 10));
                    } else if (97 > c2 || c2 > 102) {
                        token.longValue = value;
                    } else {
                        value = (16 * value) + ((long) ((c2 - 97) + 10));
                    }
                }
                token.longValue = value;
                if (c2 == 76 || c2 == 108) {
                    return TokenId.LongConstant;
                }
                ungetc(c2);
                return TokenId.IntConstant;
            } else if (48 <= c22 && c22 <= 55) {
                long value2 = (long) (c22 - 48);
                while (true) {
                    c3 = getc();
                    if (48 > c3 || c3 > 55) {
                        token.longValue = value2;
                    } else {
                        value2 = (8 * value2) + ((long) (c3 - 48));
                    }
                }
                token.longValue = value2;
                if (c3 == 76 || c3 == 108) {
                    return TokenId.LongConstant;
                }
                ungetc(c3);
                return TokenId.IntConstant;
            }
        }
        long value3 = (long) (c - 48);
        while (48 <= c22 && c22 <= 57) {
            value3 = ((10 * value3) + ((long) c22)) - 48;
            c22 = getc();
        }
        token.longValue = value3;
        if (c22 == 70 || c22 == 102) {
            token.doubleValue = (double) value3;
            return TokenId.FloatConstant;
        } else if (c22 == 69 || c22 == 101 || c22 == 68 || c22 == 100 || c22 == 46) {
            StringBuffer tbuf = this.textBuffer;
            tbuf.setLength(0);
            tbuf.append(value3);
            return readDouble(tbuf, c22, token);
        } else if (c22 == 76 || c22 == 108) {
            return TokenId.LongConstant;
        } else {
            ungetc(c22);
            return TokenId.IntConstant;
        }
    }

    private int readDouble(StringBuffer sbuf, int c, Token token) {
        if (c != 69 && c != 101 && c != 68 && c != 100) {
            sbuf.append((char) c);
            while (true) {
                c = getc();
                if (48 > c || c > 57) {
                    break;
                }
                sbuf.append((char) c);
            }
        }
        if (c == 69 || c == 101) {
            sbuf.append((char) c);
            c = getc();
            if (c == 43 || c == 45) {
                sbuf.append((char) c);
                c = getc();
            }
            while (48 <= c && c <= 57) {
                sbuf.append((char) c);
                c = getc();
            }
        }
        try {
            token.doubleValue = Double.parseDouble(sbuf.toString());
            if (c == 70 || c == 102) {
                return TokenId.FloatConstant;
            }
            if (!(c == 68 || c == 100)) {
                ungetc(c);
            }
            return TokenId.DoubleConstant;
        } catch (NumberFormatException e) {
            return TokenId.BadToken;
        }
    }

    static {
        ktable.append("abstract", TokenId.ABSTRACT);
        ktable.append("boolean", TokenId.BOOLEAN);
        ktable.append("break", TokenId.BREAK);
        ktable.append("byte", TokenId.BYTE);
        ktable.append("case", TokenId.CASE);
        ktable.append("catch", TokenId.CATCH);
        ktable.append("char", TokenId.CHAR);
        ktable.append("class", TokenId.CLASS);
        ktable.append("const", TokenId.CONST);
        ktable.append("continue", TokenId.CONTINUE);
        ktable.append("default", TokenId.DEFAULT);
        ktable.append("do", TokenId.DO);
        ktable.append("double", TokenId.DOUBLE);
        ktable.append("else", TokenId.ELSE);
        ktable.append("extends", TokenId.EXTENDS);
        ktable.append("false", TokenId.FALSE);
        ktable.append("final", TokenId.FINAL);
        ktable.append("finally", TokenId.FINALLY);
        ktable.append("float", TokenId.FLOAT);
        ktable.append("for", TokenId.FOR);
        ktable.append("goto", TokenId.GOTO);
        ktable.append("if", TokenId.IF);
        ktable.append("implements", TokenId.IMPLEMENTS);
        ktable.append("import", TokenId.IMPORT);
        ktable.append("instanceof", TokenId.INSTANCEOF);
        ktable.append("int", TokenId.INT);
        ktable.append("interface", TokenId.INTERFACE);
        ktable.append("long", TokenId.LONG);
        ktable.append("native", TokenId.NATIVE);
        ktable.append("new", TokenId.NEW);
        ktable.append("null", TokenId.NULL);
        ktable.append("package", TokenId.PACKAGE);
        ktable.append("private", TokenId.PRIVATE);
        ktable.append("protected", TokenId.PROTECTED);
        ktable.append("public", TokenId.PUBLIC);
        ktable.append("return", TokenId.RETURN);
        ktable.append("short", TokenId.SHORT);
        ktable.append("static", TokenId.STATIC);
        ktable.append("strictfp", TokenId.STRICT);
        ktable.append("super", TokenId.SUPER);
        ktable.append("switch", TokenId.SWITCH);
        ktable.append("synchronized", TokenId.SYNCHRONIZED);
        ktable.append("this", TokenId.THIS);
        ktable.append("throw", TokenId.THROW);
        ktable.append("throws", TokenId.THROWS);
        ktable.append("transient", TokenId.TRANSIENT);
        ktable.append("true", TokenId.TRUE);
        ktable.append("try", TokenId.TRY);
        ktable.append("void", TokenId.VOID);
        ktable.append("volatile", TokenId.VOLATILE);
        ktable.append("while", TokenId.WHILE);
    }

    private int readSeparator(int c) {
        int c2;
        if (33 <= c && c <= 63) {
            int t = equalOps[c - 33];
            if (t == 0) {
                return c;
            }
            c2 = getc();
            if (c == c2) {
                switch (c) {
                    case Opcode.DLOAD_0:
                        return TokenId.ANDAND;
                    case Opcode.ALOAD_1:
                        return TokenId.PLUSPLUS;
                    case 45:
                        return TokenId.MINUSMINUS;
                    case Opcode.ISTORE_1:
                        int c3 = getc();
                        if (c3 == 61) {
                            return TokenId.LSHIFT_E;
                        }
                        ungetc(c3);
                        return TokenId.LSHIFT;
                    case Opcode.ISTORE_2:
                        return TokenId.EQ;
                    case Opcode.ISTORE_3:
                        int c32 = getc();
                        if (c32 == 61) {
                            return TokenId.RSHIFT_E;
                        }
                        if (c32 == 62) {
                            int c33 = getc();
                            if (c33 == 61) {
                                return TokenId.ARSHIFT_E;
                            }
                            ungetc(c33);
                            return TokenId.ARSHIFT;
                        }
                        ungetc(c32);
                        return TokenId.RSHIFT;
                }
            } else if (c2 == 61) {
                return t;
            }
        } else if (c == 94) {
            c2 = getc();
            if (c2 == 61) {
                return TokenId.EXOR_E;
            }
        } else if (c != 124) {
            return c;
        } else {
            c2 = getc();
            if (c2 == 61) {
                return TokenId.OR_E;
            }
            if (c2 == 124) {
                return TokenId.OROR;
            }
        }
        ungetc(c2);
        return c;
    }

    private int readIdentifier(int c, Token token) {
        StringBuffer tbuf = this.textBuffer;
        tbuf.setLength(0);
        do {
            tbuf.append((char) c);
            c = getc();
        } while (Character.isJavaIdentifierPart((char) c));
        ungetc(c);
        String name = tbuf.toString();
        int t = ktable.lookup(name);
        if (t >= 0) {
            return t;
        }
        token.textValue = name;
        return TokenId.Identifier;
    }

    private static boolean isBlank(int c) {
        return c == 32 || c == 9 || c == 12 || c == 13 || c == 10;
    }

    private static boolean isDigit(int c) {
        return 48 <= c && c <= 57;
    }

    private void ungetc(int c) {
        this.lastChar = c;
    }

    public String getTextAround() {
        int begin = this.position - 10;
        if (begin < 0) {
            begin = 0;
        }
        int end = this.position + 10;
        if (end > this.maxlen) {
            end = this.maxlen;
        }
        return this.input.substring(begin, end);
    }

    private int getc() {
        if (this.lastChar >= 0) {
            int i = this.lastChar;
            this.lastChar = -1;
            return i;
        } else if (this.position >= this.maxlen) {
            return -1;
        } else {
            String str = this.input;
            int i2 = this.position;
            this.position = i2 + 1;
            return str.charAt(i2);
        }
    }
}
