package com.kitfox.svg.animation.parser;

import java.io.IOException;
import javassist.bytecode.Opcode;

public class AnimTimeParserTokenManager implements AnimTimeParserConstants {
    public static final int[] jjnewLexState = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
    static final int[] jjnextStates = {17, 7, 1, 8, 3, 7, 1, 8, 3, 13, 15, 4, 5};
    public static final String[] jjstrLiteralImages = {"", null, null, null, null, null, null, null, null, null, "indefinite", "mouseover", "whenNotActive", null, null, ";", "+", "-", ":", ".", "(", ")"};
    static final long[] jjtoMore = {0};
    static final long[] jjtoSkip = {62};
    static final long[] jjtoSpecial = {0};
    static final long[] jjtoToken = {4194049};
    public static final String[] lexStateNames = {"DEFAULT"};
    protected int curChar;
    int curLexState;
    int defaultLexState;
    private StringBuilder image;
    protected SimpleCharStream input_stream;
    private final StringBuilder jjimage;
    private int jjimageLen;
    int jjmatchedKind;
    int jjmatchedPos;
    int jjnewStateCnt;
    int jjround;
    private final int[] jjrounds;
    private final int[] jjstateSet;
    private int lengthOfMatch;

    private final int jjStopStringLiteralDfa_0(int pos, long active0) {
        switch (pos) {
            case 0:
                if ((196608 & active0) != 0) {
                    return 6;
                }
                if ((2048 & active0) != 0) {
                    this.jjmatchedKind = 14;
                    return 13;
                } else if ((5120 & active0) != 0) {
                    this.jjmatchedKind = 14;
                    return 11;
                } else if ((524288 & active0) != 0) {
                    return 2;
                } else {
                    return -1;
                }
            case 1:
                if ((7168 & active0) == 0) {
                    return -1;
                }
                this.jjmatchedKind = 14;
                this.jjmatchedPos = 1;
                return 11;
            case 2:
                if ((7168 & active0) == 0) {
                    return -1;
                }
                this.jjmatchedKind = 14;
                this.jjmatchedPos = 2;
                return 11;
            case 3:
                if ((7168 & active0) == 0) {
                    return -1;
                }
                this.jjmatchedKind = 14;
                this.jjmatchedPos = 3;
                return 11;
            case 4:
                if ((7168 & active0) == 0) {
                    return -1;
                }
                this.jjmatchedKind = 14;
                this.jjmatchedPos = 4;
                return 11;
            case 5:
                if ((7168 & active0) == 0) {
                    return -1;
                }
                this.jjmatchedKind = 14;
                this.jjmatchedPos = 5;
                return 11;
            case 6:
                if ((7168 & active0) == 0) {
                    return -1;
                }
                this.jjmatchedKind = 14;
                this.jjmatchedPos = 6;
                return 11;
            case 7:
                if ((7168 & active0) == 0) {
                    return -1;
                }
                this.jjmatchedKind = 14;
                this.jjmatchedPos = 7;
                return 11;
            case 8:
                if ((2048 & active0) != 0) {
                    return 11;
                }
                if ((5120 & active0) == 0) {
                    return -1;
                }
                this.jjmatchedKind = 14;
                this.jjmatchedPos = 8;
                return 11;
            case 9:
                if ((4096 & active0) == 0) {
                    return (1024 & active0) == 0 ? -1 : 11;
                }
                this.jjmatchedKind = 14;
                this.jjmatchedPos = 9;
                return 11;
            case 10:
                if ((4096 & active0) == 0) {
                    return -1;
                }
                this.jjmatchedKind = 14;
                this.jjmatchedPos = 10;
                return 11;
            case 11:
                if ((4096 & active0) == 0) {
                    return -1;
                }
                this.jjmatchedKind = 14;
                this.jjmatchedPos = 11;
                return 11;
            default:
                return -1;
        }
    }

    private final int jjStartNfa_0(int pos, long active0) {
        return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
    }

    private int jjStopAtPos(int pos, int kind) {
        this.jjmatchedKind = kind;
        this.jjmatchedPos = pos;
        return pos + 1;
    }

    private int jjMoveStringLiteralDfa0_0() {
        switch (this.curChar) {
            case Opcode.DLOAD_2 /*{ENCODED_INT: 40}*/:
                return jjStopAtPos(0, 20);
            case Opcode.DLOAD_3 /*{ENCODED_INT: 41}*/:
                return jjStopAtPos(0, 21);
            case Opcode.ALOAD_1 /*{ENCODED_INT: 43}*/:
                return jjStartNfaWithStates_0(0, 16, 6);
            case 45:
                return jjStartNfaWithStates_0(0, 17, 6);
            case 46:
                return jjStartNfaWithStates_0(0, 19, 2);
            case Opcode.ASTORE /*{ENCODED_INT: 58}*/:
                return jjStopAtPos(0, 18);
            case Opcode.ISTORE_0 /*{ENCODED_INT: 59}*/:
                return jjStopAtPos(0, 15);
            case 105:
                return jjMoveStringLiteralDfa1_0(1024);
            case Opcode.LDIV /*{ENCODED_INT: 109}*/:
                return jjMoveStringLiteralDfa1_0(2048);
            case Opcode.DNEG /*{ENCODED_INT: 119}*/:
                return jjMoveStringLiteralDfa1_0(4096);
            default:
                return jjMoveNfa_0(0, 0);
        }
    }

    private int jjMoveStringLiteralDfa1_0(long active0) {
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 104:
                    return jjMoveStringLiteralDfa2_0(active0, 4096);
                case Opcode.FDIV /*{ENCODED_INT: 110}*/:
                    return jjMoveStringLiteralDfa2_0(active0, 1024);
                case Opcode.DDIV /*{ENCODED_INT: 111}*/:
                    return jjMoveStringLiteralDfa2_0(active0, 2048);
                default:
                    return jjStartNfa_0(0, active0);
            }
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(0, active0);
            return 1;
        }
    }

    private int jjMoveStringLiteralDfa2_0(long old0, long active0) {
        long active02 = active0 & old0;
        if (active02 == 0) {
            return jjStartNfa_0(0, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 100:
                    return jjMoveStringLiteralDfa3_0(active02, 1024);
                case 101:
                    return jjMoveStringLiteralDfa3_0(active02, 4096);
                case Opcode.LNEG /*{ENCODED_INT: 117}*/:
                    return jjMoveStringLiteralDfa3_0(active02, 2048);
                default:
                    return jjStartNfa_0(1, active02);
            }
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(1, active02);
            return 2;
        }
    }

    private int jjMoveStringLiteralDfa3_0(long old0, long active0) {
        long active02 = active0 & old0;
        if (active02 == 0) {
            return jjStartNfa_0(1, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 101:
                    return jjMoveStringLiteralDfa4_0(active02, 1024);
                case Opcode.FDIV /*{ENCODED_INT: 110}*/:
                    return jjMoveStringLiteralDfa4_0(active02, 4096);
                case Opcode.DREM /*{ENCODED_INT: 115}*/:
                    return jjMoveStringLiteralDfa4_0(active02, 2048);
                default:
                    return jjStartNfa_0(2, active02);
            }
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(2, active02);
            return 3;
        }
    }

    private int jjMoveStringLiteralDfa4_0(long old0, long active0) {
        long active02 = active0 & old0;
        if (active02 == 0) {
            return jjStartNfa_0(2, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case Opcode.ASTORE_3 /*{ENCODED_INT: 78}*/:
                    return jjMoveStringLiteralDfa5_0(active02, 4096);
                case 101:
                    return jjMoveStringLiteralDfa5_0(active02, 2048);
                case 102:
                    return jjMoveStringLiteralDfa5_0(active02, 1024);
                default:
                    return jjStartNfa_0(3, active02);
            }
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(3, active02);
            return 4;
        }
    }

    private int jjMoveStringLiteralDfa5_0(long old0, long active0) {
        long active02 = active0 & old0;
        if (active02 == 0) {
            return jjStartNfa_0(3, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 105:
                    return jjMoveStringLiteralDfa6_0(active02, 1024);
                case Opcode.DDIV /*{ENCODED_INT: 111}*/:
                    return jjMoveStringLiteralDfa6_0(active02, 6144);
                default:
                    return jjStartNfa_0(4, active02);
            }
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(4, active02);
            return 5;
        }
    }

    private int jjMoveStringLiteralDfa6_0(long old0, long active0) {
        long active02 = active0 & old0;
        if (active02 == 0) {
            return jjStartNfa_0(4, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case Opcode.FDIV /*{ENCODED_INT: 110}*/:
                    return jjMoveStringLiteralDfa7_0(active02, 1024);
                case Opcode.INEG /*{ENCODED_INT: 116}*/:
                    return jjMoveStringLiteralDfa7_0(active02, 4096);
                case Opcode.FNEG /*{ENCODED_INT: 118}*/:
                    return jjMoveStringLiteralDfa7_0(active02, 2048);
                default:
                    return jjStartNfa_0(5, active02);
            }
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(5, active02);
            return 6;
        }
    }

    private int jjMoveStringLiteralDfa7_0(long old0, long active0) {
        long active02 = active0 & old0;
        if (active02 == 0) {
            return jjStartNfa_0(5, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 65:
                    return jjMoveStringLiteralDfa8_0(active02, 4096);
                case 101:
                    return jjMoveStringLiteralDfa8_0(active02, 2048);
                case 105:
                    return jjMoveStringLiteralDfa8_0(active02, 1024);
                default:
                    return jjStartNfa_0(6, active02);
            }
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(6, active02);
            return 7;
        }
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    private int jjMoveStringLiteralDfa8_0(long old0, long active0) {
        long active02 = active0 & old0;
        if (active02 == 0) {
            return jjStartNfa_0(6, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case Opcode.DADD /*{ENCODED_INT: 99}*/:
                    return jjMoveStringLiteralDfa9_0(active02, 4096);
                case Opcode.FREM /*{ENCODED_INT: 114}*/:
                    if ((2048 & active02) != 0) {
                        return jjStartNfaWithStates_0(8, 11, 11);
                    }
                    break;
                case Opcode.INEG /*{ENCODED_INT: 116}*/:
                    return jjMoveStringLiteralDfa9_0(active02, 1024);
            }
            return jjStartNfa_0(7, active02);
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(7, active02);
            return 8;
        }
    }

    private int jjMoveStringLiteralDfa9_0(long old0, long active0) {
        long active02 = active0 & old0;
        if (active02 == 0) {
            return jjStartNfa_0(7, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case Opcode.INEG /*{ENCODED_INT: 116}*/:
                    return jjMoveStringLiteralDfa10_0(active02, 4096);
                case 101:
                    if ((1024 & active02) != 0) {
                        return jjStartNfaWithStates_0(9, 10, 11);
                    }
                    break;
            }
            return jjStartNfa_0(8, active02);
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(8, active02);
            return 9;
        }
    }

    private int jjMoveStringLiteralDfa10_0(long old0, long active0) {
        long active02 = active0 & old0;
        if (active02 == 0) {
            return jjStartNfa_0(8, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 105:
                    return jjMoveStringLiteralDfa11_0(active02, 4096);
                default:
                    return jjStartNfa_0(9, active02);
            }
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(9, active02);
            return 10;
        }
    }

    private int jjMoveStringLiteralDfa11_0(long old0, long active0) {
        long active02 = active0 & old0;
        if (active02 == 0) {
            return jjStartNfa_0(9, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case Opcode.FNEG /*{ENCODED_INT: 118}*/:
                    return jjMoveStringLiteralDfa12_0(active02, 4096);
                default:
                    return jjStartNfa_0(10, active02);
            }
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(10, active02);
            return 11;
        }
    }

    private int jjMoveStringLiteralDfa12_0(long old0, long active0) {
        long active02 = active0 & old0;
        if (active02 == 0) {
            return jjStartNfa_0(10, old0);
        }
        try {
            this.curChar = this.input_stream.readChar();
            switch (this.curChar) {
                case 101:
                    if ((4096 & active02) != 0) {
                        return jjStartNfaWithStates_0(12, 12, 11);
                    }
                    break;
            }
            return jjStartNfa_0(11, active02);
        } catch (IOException e) {
            jjStopStringLiteralDfa_0(11, active02);
            return 12;
        }
    }

    private int jjStartNfaWithStates_0(int pos, int kind, int state) {
        this.jjmatchedKind = kind;
        this.jjmatchedPos = pos;
        try {
            this.curChar = this.input_stream.readChar();
            return jjMoveNfa_0(state, pos + 1);
        } catch (IOException e) {
            return pos + 1;
        }
    }

    private int jjMoveNfa_0(int startState, int curPos) {
        int startsAt = 0;
        this.jjnewStateCnt = 18;
        int i = 1;
        this.jjstateSet[0] = startState;
        int kind = Integer.MAX_VALUE;
        while (true) {
            int i2 = this.jjround + 1;
            this.jjround = i2;
            if (i2 == Integer.MAX_VALUE) {
                ReInitRounds();
            }
            if (this.curChar < 64) {
                long l = 1 << this.curChar;
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 8) {
                                    kind = 8;
                                }
                                jjCheckNAddStates(0, 4);
                                continue;
                            } else if ((43980465111040L & l) != 0) {
                                jjCheckNAddTwoStates(1, 6);
                                continue;
                            } else if (this.curChar == 46) {
                                jjCheckNAdd(2);
                                continue;
                            } else {
                                continue;
                            }
                        case 1:
                            if (this.curChar == 46) {
                                jjCheckNAdd(2);
                                continue;
                            } else {
                                continue;
                            }
                        case 2:
                            if ((287948901175001088L & l) == 0) {
                                continue;
                            } else {
                                if (kind > 9) {
                                    kind = 9;
                                }
                                jjCheckNAddTwoStates(2, 3);
                                continue;
                            }
                        case 4:
                            if ((43980465111040L & l) != 0) {
                                jjCheckNAdd(5);
                                continue;
                            } else {
                                continue;
                            }
                        case 5:
                            if ((287948901175001088L & l) == 0) {
                                continue;
                            } else {
                                if (kind > 9) {
                                    kind = 9;
                                }
                                jjCheckNAdd(5);
                                continue;
                            }
                        case 6:
                            if ((287948901175001088L & l) != 0) {
                                if (kind > 9) {
                                    kind = 9;
                                }
                                jjCheckNAddStates(5, 8);
                                continue;
                            } else if (this.curChar == 46) {
                                jjCheckNAdd(2);
                                continue;
                            } else {
                                continue;
                            }
                        case 7:
                            if ((287948901175001088L & l) != 0) {
                                jjCheckNAddTwoStates(7, 1);
                                continue;
                            } else {
                                continue;
                            }
                        case 8:
                            if ((287948901175001088L & l) == 0) {
                                continue;
                            } else {
                                if (kind > 9) {
                                    kind = 9;
                                }
                                jjCheckNAddTwoStates(8, 3);
                                continue;
                            }
                        case 11:
                        case 13:
                            if ((287984085547089920L & l) == 0) {
                                continue;
                            } else {
                                if (kind > 14) {
                                    kind = 14;
                                }
                                jjCheckNAdd(11);
                                continue;
                            }
                        case 16:
                            if ((287948901175001088L & l) == 0) {
                                continue;
                            } else {
                                if (kind > 8) {
                                    kind = 8;
                                }
                                jjCheckNAddStates(0, 4);
                                continue;
                            }
                        case 17:
                            if ((287948901175001088L & l) == 0) {
                                continue;
                            } else {
                                if (kind > 8) {
                                    kind = 8;
                                }
                                jjCheckNAdd(17);
                                continue;
                            }
                    }
                } while (i != startsAt);
            } else if (this.curChar < 128) {
                long l2 = 1 << (this.curChar & 63);
                do {
                    i--;
                    switch (this.jjstateSet[i]) {
                        case 0:
                            if ((576460743847706622L & l2) != 0) {
                                if (kind > 14) {
                                    kind = 14;
                                }
                                jjCheckNAdd(11);
                            }
                            if ((2252899325313024L & l2) != 0) {
                                if (kind > 13) {
                                    kind = 13;
                                    continue;
                                } else {
                                    continue;
                                }
                            } else if (this.curChar == 109) {
                                jjAddStates(9, 10);
                                continue;
                            } else {
                                continue;
                            }
                        case 3:
                            if ((137438953504L & l2) != 0) {
                                jjAddStates(11, 12);
                                continue;
                            } else {
                                continue;
                            }
                        case 9:
                            if ((2252899325313024L & l2) != 0 && kind > 13) {
                                kind = 13;
                                continue;
                            }
                        case 10:
                            if ((576460743847706622L & l2) == 0) {
                                continue;
                            } else {
                                if (kind > 14) {
                                    kind = 14;
                                }
                                jjCheckNAdd(11);
                                continue;
                            }
                        case 11:
                            if ((576460745995190270L & l2) == 0) {
                                continue;
                            } else {
                                if (kind > 14) {
                                    kind = 14;
                                }
                                jjCheckNAdd(11);
                                continue;
                            }
                        case 12:
                            if (this.curChar == 109) {
                                jjAddStates(9, 10);
                                continue;
                            } else {
                                continue;
                            }
                        case 13:
                            if ((576460745995190270L & l2) != 0) {
                                if (kind > 14) {
                                    kind = 14;
                                }
                                jjCheckNAdd(11);
                            }
                            if (this.curChar == 105) {
                                int[] iArr = this.jjstateSet;
                                int i3 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i3 + 1;
                                iArr[i3] = 14;
                                continue;
                            } else if (this.curChar == 115 && kind > 13) {
                                kind = 13;
                                continue;
                            }
                        case 14:
                            if (this.curChar == 110 && kind > 13) {
                                kind = 13;
                                continue;
                            }
                        case 15:
                            if (this.curChar == 105) {
                                int[] iArr2 = this.jjstateSet;
                                int i4 = this.jjnewStateCnt;
                                this.jjnewStateCnt = i4 + 1;
                                iArr2[i4] = 14;
                                continue;
                            } else {
                                continue;
                            }
                    }
                } while (i != startsAt);
            } else {
                int i5 = (this.curChar & 255) >> 6;
                long j = 1 << (this.curChar & 63);
                do {
                    i--;
                    int i6 = this.jjstateSet[i];
                } while (i != startsAt);
            }
            if (kind != Integer.MAX_VALUE) {
                this.jjmatchedKind = kind;
                this.jjmatchedPos = curPos;
                kind = Integer.MAX_VALUE;
            }
            curPos++;
            i = this.jjnewStateCnt;
            this.jjnewStateCnt = startsAt;
            startsAt = 18 - this.jjnewStateCnt;
            if (i != startsAt) {
                try {
                    this.curChar = this.input_stream.readChar();
                } catch (IOException e) {
                }
            }
            return curPos;
        }
    }

    /* access modifiers changed from: protected */
    public Token jjFillToken() {
        String curTokenImage;
        String im = jjstrLiteralImages[this.jjmatchedKind];
        if (im == null) {
            curTokenImage = this.input_stream.getImage();
        } else {
            curTokenImage = im;
        }
        int beginLine = this.input_stream.getBeginLine();
        int beginColumn = this.input_stream.getBeginColumn();
        int endLine = this.input_stream.getEndLine();
        int endColumn = this.input_stream.getEndColumn();
        Token t = Token.newToken(this.jjmatchedKind);
        t.kind = this.jjmatchedKind;
        t.image = curTokenImage;
        t.beginLine = beginLine;
        t.endLine = endLine;
        t.beginColumn = beginColumn;
        t.endColumn = endColumn;
        return t;
    }

    public Token getNextToken() {
        while (true) {
            try {
                this.curChar = this.input_stream.beginToken();
                try {
                    this.input_stream.backup(0);
                    while (this.curChar <= 32 && (4294981120L & (1 << this.curChar)) != 0) {
                        this.curChar = this.input_stream.beginToken();
                    }
                    this.jjmatchedKind = Integer.MAX_VALUE;
                    this.jjmatchedPos = 0;
                    int curPos = jjMoveStringLiteralDfa0_0();
                    if (this.jjmatchedKind != Integer.MAX_VALUE) {
                        if (this.jjmatchedPos + 1 < curPos) {
                            this.input_stream.backup((curPos - this.jjmatchedPos) - 1);
                        }
                        if ((jjtoToken[this.jjmatchedKind >> 6] & (1 << (this.jjmatchedKind & 63))) != 0) {
                            return jjFillToken();
                        }
                    } else {
                        int error_line = this.input_stream.getEndLine();
                        int error_column = this.input_stream.getEndColumn();
                        String error_after = null;
                        boolean EOFSeen = false;
                        try {
                            this.input_stream.readChar();
                            this.input_stream.backup(1);
                        } catch (IOException e) {
                            EOFSeen = true;
                            error_after = curPos <= 1 ? "" : this.input_stream.getImage();
                            if (this.curChar == 10 || this.curChar == 13) {
                                error_line++;
                                error_column = 0;
                            } else {
                                error_column++;
                            }
                        }
                        if (!EOFSeen) {
                            this.input_stream.backup(1);
                            error_after = curPos <= 1 ? "" : this.input_stream.getImage();
                        }
                        throw new TokenMgrException(EOFSeen, this.curLexState, error_line, error_column, error_after, this.curChar, 0);
                    }
                } catch (IOException e2) {
                }
            } catch (Exception e3) {
                this.jjmatchedKind = 0;
                this.jjmatchedPos = -1;
                return jjFillToken();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void SkipLexicalActions(Token matchedToken) {
        int i = this.jjmatchedKind;
    }

    /* access modifiers changed from: package-private */
    public void MoreLexicalActions() {
        int i = this.jjimageLen;
        int i2 = this.jjmatchedPos + 1;
        this.lengthOfMatch = i2;
        this.jjimageLen = i + i2;
        int i3 = this.jjmatchedKind;
    }

    /* access modifiers changed from: package-private */
    public void TokenLexicalActions(Token matchedToken) {
        int i = this.jjmatchedKind;
    }

    private void jjCheckNAdd(int state) {
        if (this.jjrounds[state] != this.jjround) {
            int[] iArr = this.jjstateSet;
            int i = this.jjnewStateCnt;
            this.jjnewStateCnt = i + 1;
            iArr[i] = state;
            this.jjrounds[state] = this.jjround;
        }
    }

    private void jjAddStates(int start, int end) {
        while (true) {
            int[] iArr = this.jjstateSet;
            int i = this.jjnewStateCnt;
            this.jjnewStateCnt = i + 1;
            iArr[i] = jjnextStates[start];
            int start2 = start + 1;
            if (start != end) {
                start = start2;
            } else {
                return;
            }
        }
    }

    private void jjCheckNAddTwoStates(int state1, int state2) {
        jjCheckNAdd(state1);
        jjCheckNAdd(state2);
    }

    private void jjCheckNAddStates(int start, int end) {
        while (true) {
            jjCheckNAdd(jjnextStates[start]);
            int start2 = start + 1;
            if (start != end) {
                start = start2;
            } else {
                return;
            }
        }
    }

    public AnimTimeParserTokenManager(SimpleCharStream stream) {
        this.curLexState = 0;
        this.defaultLexState = 0;
        this.jjrounds = new int[18];
        this.jjstateSet = new int[36];
        this.jjimage = new StringBuilder();
        this.image = this.jjimage;
        this.input_stream = stream;
    }

    public AnimTimeParserTokenManager(SimpleCharStream stream, int lexState) {
        this.curLexState = 0;
        this.defaultLexState = 0;
        this.jjrounds = new int[18];
        this.jjstateSet = new int[36];
        this.jjimage = new StringBuilder();
        this.image = this.jjimage;
        ReInit(stream);
        SwitchTo(lexState);
    }

    public void ReInit(SimpleCharStream stream) {
        this.jjnewStateCnt = 0;
        this.jjmatchedPos = 0;
        this.curLexState = this.defaultLexState;
        this.input_stream = stream;
        ReInitRounds();
    }

    private void ReInitRounds() {
        this.jjround = -2147483647;
        int i = 18;
        while (true) {
            i--;
            if (i > 0) {
                this.jjrounds[i] = Integer.MIN_VALUE;
            } else {
                return;
            }
        }
    }

    public void ReInit(SimpleCharStream stream, int lexState) {
        ReInit(stream);
        SwitchTo(lexState);
    }

    public void SwitchTo(int lexState) {
        if (lexState >= 1 || lexState < 0) {
            throw new TokenMgrException("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", 2);
        }
        this.curLexState = lexState;
    }
}
