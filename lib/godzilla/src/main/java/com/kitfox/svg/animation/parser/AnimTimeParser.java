package com.kitfox.svg.animation.parser;

import com.kitfox.svg.SVGConst;
import com.kitfox.svg.animation.TimeBase;
import com.kitfox.svg.animation.TimeDiscrete;
import com.kitfox.svg.animation.TimeIndefinite;
import com.kitfox.svg.animation.TimeLookup;
import com.kitfox.svg.animation.TimeSum;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AnimTimeParser implements AnimTimeParserTreeConstants, AnimTimeParserConstants {
    private static int[] jj_la1_0;
    private final JJCalls[] jj_2_rtns;
    private int jj_endpos;
    private List<int[]> jj_expentries;
    private int[] jj_expentry;
    private int jj_gc;
    private int jj_gen;
    SimpleCharStream jj_input_stream;
    private int jj_kind;
    private int jj_la;
    private final int[] jj_la1;
    private Token jj_lastpos;
    private int[] jj_lasttokens;
    private final LookaheadSuccess jj_ls;
    public Token jj_nt;
    private int jj_ntk;
    private boolean jj_rescan;
    private Token jj_scanpos;
    protected JJTAnimTimeParserState jjtree;
    public Token token;
    public AnimTimeParserTokenManager token_source;

    public static void main(String[] args) throws ParseException {
        AnimTimeParser parser = new AnimTimeParser(new StringReader("1:30 + 5ms"));
        System.err.println("AnimTimeParser eval to " + parser.Expr().evalTime());
        parser.ReInit(new StringReader("19"));
        System.err.println("AnimTimeParser eval to " + parser.Expr().evalTime());
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0081  */
    /* JADX WARNING: Removed duplicated region for block: B:46:0x00ac  */
    /* JADX WARNING: Removed duplicated region for block: B:49:0x00bb  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final com.kitfox.svg.animation.TimeBase Expr() throws com.kitfox.svg.animation.parser.ParseException {
        /*
        // Method dump skipped, instructions count: 252
        */
        throw new UnsupportedOperationException("Method not decompiled: com.kitfox.svg.animation.parser.AnimTimeParser.Expr():com.kitfox.svg.animation.TimeBase");
    }

    public final TimeBase Sum() throws ParseException {
        int i;
        ASTSum jjtn000 = new ASTSum(1);
        this.jjtree.openNodeScope(jjtn000);
        Token t = null;
        TimeBase t2 = null;
        try {
            TimeBase t1 = Term();
            if (this.jj_ntk == -1) {
                i = jj_ntk_f();
            } else {
                i = this.jj_ntk;
            }
            switch (i) {
                case 16:
                case 17:
                    switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                        case 16:
                            t = jj_consume_token(16);
                            break;
                        case 17:
                            t = jj_consume_token(17);
                            break;
                        default:
                            this.jj_la1[2] = this.jj_gen;
                            jj_consume_token(-1);
                            throw new ParseException();
                    }
                    t2 = Term();
                    break;
                default:
                    this.jj_la1[3] = this.jj_gen;
                    break;
            }
            this.jjtree.closeNodeScope((Node) jjtn000, true);
            if (t2 != null || "" == 0) {
                if (t.image.equals("-")) {
                    if ("" != 0) {
                        TimeBase t12 = new TimeSum(t1, t2, false);
                        if (0 != 0) {
                            this.jjtree.closeNodeScope((Node) jjtn000, true);
                        }
                        return t12;
                    }
                } else if ("" != 0) {
                    TimeBase t13 = new TimeSum(t1, t2, true);
                    if (0 != 0) {
                        this.jjtree.closeNodeScope((Node) jjtn000, true);
                    }
                    return t13;
                }
                if (0 != 0) {
                    this.jjtree.closeNodeScope((Node) jjtn000, true);
                }
                throw new IllegalStateException("Missing return statement in function");
            } else if (0 == 0) {
                return t1;
            } else {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
                return t1;
            }
        } catch (Throwable th) {
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
            throw th;
        }
    }

    public final TimeBase Term() throws ParseException {
        int i;
        boolean jjtc000;
        ASTTerm jjtn000 = new ASTTerm(2);
        this.jjtree.openNodeScope(jjtn000);
        try {
            if (this.jj_ntk == -1) {
                i = jj_ntk_f();
            } else {
                i = this.jj_ntk;
            }
            switch (i) {
                case 8:
                case 9:
                    TimeBase base = LiteralTime();
                    this.jjtree.closeNodeScope((Node) jjtn000, true);
                    jjtc000 = false;
                    if ("" != 0) {
                        if (0 != 0) {
                            this.jjtree.closeNodeScope((Node) jjtn000, true);
                        }
                        return base;
                    }
                    break;
                case 10:
                    TimeBase base2 = IndefiniteTime();
                    this.jjtree.closeNodeScope((Node) jjtn000, true);
                    jjtc000 = false;
                    if ("" != 0) {
                        if (0 != 0) {
                            this.jjtree.closeNodeScope((Node) jjtn000, true);
                        }
                        return base2;
                    }
                    break;
                case 11:
                case 12:
                    TimeBase base3 = EventTime();
                    this.jjtree.closeNodeScope((Node) jjtn000, true);
                    jjtc000 = false;
                    if ("" != 0) {
                        if (0 != 0) {
                            this.jjtree.closeNodeScope((Node) jjtn000, true);
                        }
                        return base3;
                    }
                    break;
                case 13:
                default:
                    this.jj_la1[4] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
                case 14:
                    TimeBase base4 = LookupTime();
                    this.jjtree.closeNodeScope((Node) jjtn000, true);
                    jjtc000 = false;
                    if ("" != 0) {
                        if (0 != 0) {
                            this.jjtree.closeNodeScope((Node) jjtn000, true);
                        }
                        return base4;
                    }
                    break;
            }
            if (jjtc000) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
            throw new IllegalStateException("Missing return statement in function");
        } catch (Throwable th) {
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
            throw th;
        }
    }

    public final TimeIndefinite IndefiniteTime() throws ParseException {
        ASTIndefiniteTime jjtn000 = new ASTIndefiniteTime(3);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        try {
            jj_consume_token(10);
            this.jjtree.closeNodeScope((Node) jjtn000, true);
            jjtc000 = false;
            if ("" != 0) {
                return new TimeIndefinite();
            }
            if (jjtc000) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
            throw new IllegalStateException("Missing return statement in function");
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
        }
    }

    public final TimeDiscrete EventTime() throws ParseException {
        ASTEventTime jjtn000 = new ASTEventTime(4);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        try {
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 11:
                    jj_consume_token(11);
                    break;
                case 12:
                    jj_consume_token(12);
                    break;
                default:
                    this.jj_la1[5] = this.jj_gen;
                    jj_consume_token(-1);
                    throw new ParseException();
            }
            this.jjtree.closeNodeScope((Node) jjtn000, true);
            jjtc000 = false;
            if ("" != 0) {
                return new TimeDiscrete(0.0d);
            }
            if (jjtc000) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
            throw new IllegalStateException("Missing return statement in function");
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
        }
    }

    public final TimeDiscrete LiteralTime() throws ParseException {
        int i;
        int i2;
        ASTLiteralTime jjtn000 = new ASTLiteralTime(5);
        this.jjtree.openNodeScope(jjtn000);
        double t3 = Double.NaN;
        try {
            double t1 = Number();
            double value = t1;
            if (this.jj_ntk == -1) {
                i = jj_ntk_f();
            } else {
                i = this.jj_ntk;
            }
            switch (i) {
                case 13:
                case 18:
                    if (this.jj_ntk == -1) {
                        i2 = jj_ntk_f();
                    } else {
                        i2 = this.jj_ntk;
                    }
                    switch (i2) {
                        case 13:
                            Token t = jj_consume_token(13);
                            if (t.image.equals("ms")) {
                                value = t1 / 1000.0d;
                            }
                            if (t.image.equals("min")) {
                                value = t1 * 60.0d;
                            }
                            if (t.image.equals("h")) {
                                value = t1 * 3600.0d;
                                break;
                            }
                            break;
                        case 18:
                            jj_consume_token(18);
                            double t2 = Number();
                            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                                case 18:
                                    jj_consume_token(18);
                                    t3 = Number();
                                    break;
                                default:
                                    this.jj_la1[6] = this.jj_gen;
                                    break;
                            }
                            if (!Double.isNaN(t3)) {
                                value = (3600.0d * t1) + (60.0d * t2) + t3;
                                break;
                            } else {
                                value = (60.0d * t1) + t2;
                                break;
                            }
                        default:
                            this.jj_la1[7] = this.jj_gen;
                            jj_consume_token(-1);
                            throw new ParseException();
                    }
                default:
                    this.jj_la1[8] = this.jj_gen;
                    break;
            }
            this.jjtree.closeNodeScope((Node) jjtn000, true);
            if ("" != 0) {
                TimeDiscrete timeDiscrete = new TimeDiscrete(value);
                if (0 != 0) {
                    this.jjtree.closeNodeScope((Node) jjtn000, true);
                }
                return timeDiscrete;
            }
            if (0 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
            throw new IllegalStateException("Missing return statement in function");
        } catch (Throwable th) {
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
            throw th;
        }
    }

    public final TimeLookup LookupTime() throws ParseException {
        ASTLookupTime jjtn000 = new ASTLookupTime(6);
        this.jjtree.openNodeScope(jjtn000);
        double paramNum = 0.0d;
        try {
            Token node = jj_consume_token(14);
            jj_consume_token(19);
            Token event = jj_consume_token(14);
            switch (this.jj_ntk == -1 ? jj_ntk_f() : this.jj_ntk) {
                case 20:
                    paramNum = ParamList();
                    break;
                default:
                    this.jj_la1[9] = this.jj_gen;
                    break;
            }
            this.jjtree.closeNodeScope((Node) jjtn000, true);
            if ("" != 0) {
                TimeLookup timeLookup = new TimeLookup(null, node.image, event.image, "" + paramNum);
                if (0 != 0) {
                    this.jjtree.closeNodeScope((Node) jjtn000, true);
                }
                return timeLookup;
            }
            if (0 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
            throw new IllegalStateException("Missing return statement in function");
        } catch (Throwable th) {
            if (1 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
            throw th;
        }
    }

    public final double ParamList() throws ParseException {
        ASTParamList jjtn000 = new ASTParamList(7);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        try {
            jj_consume_token(20);
            double num = Number();
            jj_consume_token(21);
            this.jjtree.closeNodeScope((Node) jjtn000, true);
            jjtc000 = false;
            if ("" != 0) {
                if (0 != 0) {
                    this.jjtree.closeNodeScope((Node) jjtn000, true);
                }
                return num;
            }
            if (0 != 0) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
            throw new IllegalStateException("Missing return statement in function");
        } catch (Throwable th) {
            if (jjtc000) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
            throw th;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x00e1  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final double Number() throws com.kitfox.svg.animation.parser.ParseException {
        /*
        // Method dump skipped, instructions count: 246
        */
        throw new UnsupportedOperationException("Method not decompiled: com.kitfox.svg.animation.parser.AnimTimeParser.Number():double");
    }

    public final int Integer() throws ParseException {
        int i;
        ASTInteger jjtn000 = new ASTInteger(9);
        boolean jjtc000 = true;
        this.jjtree.openNodeScope(jjtn000);
        try {
            Token t = jj_consume_token(8);
            this.jjtree.closeNodeScope((Node) jjtn000, true);
            jjtc000 = false;
            if ("" != 0) {
                try {
                    i = Integer.parseInt(t.image);
                } catch (Exception e) {
                    Logger.getLogger(SVGConst.SVG_LOGGER).log(Level.WARNING, "Could not parse int '" + t.image + "'", (Throwable) e);
                }
                return i;
            }
            if ("" != 0) {
                i = 0;
                if (jjtc000) {
                    this.jjtree.closeNodeScope((Node) jjtn000, true);
                }
                return i;
            }
            if (jjtc000) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
            throw new IllegalStateException("Missing return statement in function");
        } finally {
            if (jjtc000) {
                this.jjtree.closeNodeScope((Node) jjtn000, true);
            }
        }
    }

    private boolean jj_2_1(int xla) {
        boolean z = true;
        this.jj_la = xla;
        this.jj_scanpos = this.token;
        this.jj_lastpos = this.token;
        try {
            if (jj_3_1()) {
                z = false;
            }
        } catch (LookaheadSuccess e) {
        } finally {
            jj_save(0, xla);
        }
        return z;
    }

    private boolean jj_3R_3() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_4()) {
            this.jj_scanpos = xsp;
            if (jj_3R_5()) {
                this.jj_scanpos = xsp;
                if (jj_3R_6()) {
                    this.jj_scanpos = xsp;
                    if (jj_3R_7()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean jj_3R_4() {
        if (jj_3R_8()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_9() {
        if (jj_3R_12()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_5() {
        if (jj_3R_9()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_12() {
        Token xsp = this.jj_scanpos;
        if (jj_3R_13()) {
            this.jj_scanpos = xsp;
            if (jj_3R_14()) {
                return true;
            }
        }
        return false;
    }

    private boolean jj_3R_13() {
        if (jj_scan_token(9)) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_6() {
        if (jj_3R_10()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_7() {
        if (jj_3R_11()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_2() {
        if (jj_3R_3()) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_8() {
        if (jj_scan_token(10)) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_10() {
        if (jj_scan_token(14)) {
            return true;
        }
        return false;
    }

    private boolean jj_3_1() {
        if (!jj_scan_token(15) && !jj_3R_2()) {
            return false;
        }
        return true;
    }

    private boolean jj_3R_14() {
        if (jj_scan_token(8)) {
            return true;
        }
        return false;
    }

    private boolean jj_3R_11() {
        Token xsp = this.jj_scanpos;
        if (jj_scan_token(11)) {
            this.jj_scanpos = xsp;
            if (jj_scan_token(12)) {
                return true;
            }
        }
        return false;
    }

    static {
        jj_la1_init_0();
    }

    private static void jj_la1_init_0() {
        jj_la1_0 = new int[]{24320, 32768, 196608, 196608, 24320, 6144, 262144, 270336, 270336, 1048576, 768};
    }

    public AnimTimeParser(InputStream stream) {
        this(stream, null);
    }

    public AnimTimeParser(InputStream stream, Charset encoding) {
        this.jjtree = new JJTAnimTimeParserState();
        this.jj_la1 = new int[11];
        this.jj_2_rtns = new JJCalls[1];
        this.jj_rescan = false;
        this.jj_gc = 0;
        this.jj_ls = new LookaheadSuccess();
        this.jj_expentries = new ArrayList();
        this.jj_kind = -1;
        this.jj_lasttokens = new int[100];
        this.jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1);
        this.token_source = new AnimTimeParserTokenManager(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 11; i++) {
            this.jj_la1[i] = -1;
        }
        for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
            this.jj_2_rtns[i2] = new JJCalls();
        }
    }

    public void ReInit(InputStream stream) {
        ReInit(stream, null);
    }

    public void ReInit(InputStream stream, Charset encoding) {
        this.jj_input_stream.reInit(stream, encoding, 1, 1);
        this.token_source.ReInit(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 11; i++) {
            this.jj_la1[i] = -1;
        }
        for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
            this.jj_2_rtns[i2] = new JJCalls();
        }
    }

    public AnimTimeParser(Reader stream) {
        this.jjtree = new JJTAnimTimeParserState();
        this.jj_la1 = new int[11];
        this.jj_2_rtns = new JJCalls[1];
        this.jj_rescan = false;
        this.jj_gc = 0;
        this.jj_ls = new LookaheadSuccess();
        this.jj_expentries = new ArrayList();
        this.jj_kind = -1;
        this.jj_lasttokens = new int[100];
        this.jj_input_stream = new SimpleCharStream(stream, 1, 1);
        this.token_source = new AnimTimeParserTokenManager(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 11; i++) {
            this.jj_la1[i] = -1;
        }
        for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
            this.jj_2_rtns[i2] = new JJCalls();
        }
    }

    public void ReInit(Reader stream) {
        if (this.jj_input_stream == null) {
            this.jj_input_stream = new SimpleCharStream(stream, 1, 1);
        } else {
            this.jj_input_stream.reInit(stream, 1, 1);
        }
        if (this.token_source == null) {
            this.token_source = new AnimTimeParserTokenManager(this.jj_input_stream);
        }
        this.token_source.ReInit(this.jj_input_stream);
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 11; i++) {
            this.jj_la1[i] = -1;
        }
        for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
            this.jj_2_rtns[i2] = new JJCalls();
        }
    }

    public AnimTimeParser(AnimTimeParserTokenManager tm) {
        this.jjtree = new JJTAnimTimeParserState();
        this.jj_la1 = new int[11];
        this.jj_2_rtns = new JJCalls[1];
        this.jj_rescan = false;
        this.jj_gc = 0;
        this.jj_ls = new LookaheadSuccess();
        this.jj_expentries = new ArrayList();
        this.jj_kind = -1;
        this.jj_lasttokens = new int[100];
        this.token_source = tm;
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 11; i++) {
            this.jj_la1[i] = -1;
        }
        for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
            this.jj_2_rtns[i2] = new JJCalls();
        }
    }

    public void ReInit(AnimTimeParserTokenManager tm) {
        this.token_source = tm;
        this.token = new Token();
        this.jj_ntk = -1;
        this.jj_gen = 0;
        for (int i = 0; i < 11; i++) {
            this.jj_la1[i] = -1;
        }
        for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
            this.jj_2_rtns[i2] = new JJCalls();
        }
    }

    private Token jj_consume_token(int kind) throws ParseException {
        Token oldToken = this.token;
        if (this.token.next != null) {
            this.token = this.token.next;
        } else {
            this.token.next = this.token_source.getNextToken();
            this.token = this.token.next;
        }
        this.jj_ntk = -1;
        if (this.token.kind == kind) {
            this.jj_gen++;
            int i = this.jj_gc + 1;
            this.jj_gc = i;
            if (i > 100) {
                this.jj_gc = 0;
                for (int i2 = 0; i2 < this.jj_2_rtns.length; i2++) {
                    for (JJCalls c = this.jj_2_rtns[i2]; c != null; c = c.next) {
                        if (c.gen < this.jj_gen) {
                            c.first = null;
                        }
                    }
                }
            }
            return this.token;
        }
        this.token = oldToken;
        this.jj_kind = kind;
        throw generateParseException();
    }

    /* access modifiers changed from: private */
    public static final class LookaheadSuccess extends IllegalStateException {
        private LookaheadSuccess() {
        }
    }

    private boolean jj_scan_token(int kind) {
        if (this.jj_scanpos == this.jj_lastpos) {
            this.jj_la--;
            if (this.jj_scanpos.next == null) {
                Token token2 = this.jj_scanpos;
                Token nextToken = this.token_source.getNextToken();
                token2.next = nextToken;
                this.jj_scanpos = nextToken;
                this.jj_lastpos = nextToken;
            } else {
                Token token3 = this.jj_scanpos.next;
                this.jj_scanpos = token3;
                this.jj_lastpos = token3;
            }
        } else {
            this.jj_scanpos = this.jj_scanpos.next;
        }
        if (this.jj_rescan) {
            int i = 0;
            Token tok = this.token;
            while (tok != null && tok != this.jj_scanpos) {
                i++;
                tok = tok.next;
            }
            if (tok != null) {
                jj_add_error_token(kind, i);
            }
        }
        if (this.jj_scanpos.kind != kind) {
            return true;
        }
        if (this.jj_la != 0 || this.jj_scanpos != this.jj_lastpos) {
            return false;
        }
        throw this.jj_ls;
    }

    public final Token getNextToken() {
        if (this.token.next != null) {
            this.token = this.token.next;
        } else {
            Token token2 = this.token;
            Token nextToken = this.token_source.getNextToken();
            token2.next = nextToken;
            this.token = nextToken;
        }
        this.jj_ntk = -1;
        this.jj_gen++;
        return this.token;
    }

    public final Token getToken(int index) {
        Token t = this.token;
        for (int i = 0; i < index; i++) {
            if (t.next == null) {
                t.next = this.token_source.getNextToken();
            }
            t = t.next;
        }
        return t;
    }

    private int jj_ntk_f() {
        this.jj_nt = this.token.next;
        if (this.jj_nt == null) {
            this.token.next = this.token_source.getNextToken();
            this.jj_ntk = this.token.next.kind;
            return this.jj_ntk;
        }
        this.jj_ntk = this.jj_nt.kind;
        return this.jj_ntk;
    }

    private void jj_add_error_token(int kind, int pos) {
        if (pos < 100) {
            if (pos == this.jj_endpos + 1) {
                int[] iArr = this.jj_lasttokens;
                int i = this.jj_endpos;
                this.jj_endpos = i + 1;
                iArr[i] = kind;
            } else if (this.jj_endpos != 0) {
                this.jj_expentry = new int[this.jj_endpos];
                for (int i2 = 0; i2 < this.jj_endpos; i2++) {
                    this.jj_expentry[i2] = this.jj_lasttokens[i2];
                }
                Iterator<int[]> it = this.jj_expentries.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        break;
                    }
                    int[] oldentry = it.next();
                    if (oldentry.length == this.jj_expentry.length) {
                        boolean isMatched = true;
                        int i3 = 0;
                        while (true) {
                            if (i3 >= this.jj_expentry.length) {
                                break;
                            } else if (oldentry[i3] != this.jj_expentry[i3]) {
                                isMatched = false;
                                break;
                            } else {
                                i3++;
                            }
                        }
                        if (isMatched) {
                            this.jj_expentries.add(this.jj_expentry);
                            break;
                        }
                    }
                }
                if (pos != 0) {
                    this.jj_endpos = pos;
                    this.jj_lasttokens[this.jj_endpos - 1] = kind;
                }
            }
        }
    }

    public ParseException generateParseException() {
        this.jj_expentries.clear();
        boolean[] la1tokens = new boolean[22];
        if (this.jj_kind >= 0) {
            la1tokens[this.jj_kind] = true;
            this.jj_kind = -1;
        }
        for (int i = 0; i < 11; i++) {
            if (this.jj_la1[i] == this.jj_gen) {
                for (int j = 0; j < 32; j++) {
                    if ((jj_la1_0[i] & (1 << j)) != 0) {
                        la1tokens[j] = true;
                    }
                }
            }
        }
        for (int i2 = 0; i2 < 22; i2++) {
            if (la1tokens[i2]) {
                this.jj_expentry = new int[1];
                this.jj_expentry[0] = i2;
                this.jj_expentries.add(this.jj_expentry);
            }
        }
        this.jj_endpos = 0;
        jj_rescan_token();
        jj_add_error_token(0, 0);
        int[][] exptokseq = new int[this.jj_expentries.size()][];
        for (int i3 = 0; i3 < this.jj_expentries.size(); i3++) {
            exptokseq[i3] = this.jj_expentries.get(i3);
        }
        return new ParseException(this.token, exptokseq, tokenImage);
    }

    public final boolean trace_enabled() {
        return false;
    }

    public final void enable_tracing() {
    }

    public final void disable_tracing() {
    }

    private void jj_rescan_token() {
        this.jj_rescan = true;
        for (int i = 0; i < 1; i++) {
            try {
                JJCalls p = this.jj_2_rtns[i];
                do {
                    if (p.gen > this.jj_gen) {
                        this.jj_la = p.arg;
                        this.jj_scanpos = p.first;
                        this.jj_lastpos = p.first;
                        switch (i) {
                            case 0:
                                jj_3_1();
                                break;
                        }
                    }
                    p = p.next;
                } while (p != null);
            } catch (LookaheadSuccess e) {
            }
        }
        this.jj_rescan = false;
    }

    private void jj_save(int index, int xla) {
        JJCalls p = this.jj_2_rtns[index];
        while (true) {
            if (p.gen <= this.jj_gen) {
                break;
            } else if (p.next == null) {
                p.next = new JJCalls();
                p = p.next;
                break;
            } else {
                p = p.next;
            }
        }
        p.gen = (this.jj_gen + xla) - this.jj_la;
        p.first = this.token;
        p.arg = xla;
    }

    /* access modifiers changed from: package-private */
    public static final class JJCalls {
        int arg;
        Token first;
        int gen;
        JJCalls next;

        JJCalls() {
        }
    }
}
