package javassist.compiler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import javassist.bytecode.Bytecode;
import javassist.bytecode.Opcode;
import javassist.compiler.ast.ASTList;
import javassist.compiler.ast.ASTree;
import javassist.compiler.ast.ArrayInit;
import javassist.compiler.ast.AssignExpr;
import javassist.compiler.ast.BinExpr;
import javassist.compiler.ast.CallExpr;
import javassist.compiler.ast.CastExpr;
import javassist.compiler.ast.CondExpr;
import javassist.compiler.ast.Declarator;
import javassist.compiler.ast.DoubleConst;
import javassist.compiler.ast.Expr;
import javassist.compiler.ast.FieldDecl;
import javassist.compiler.ast.InstanceOfExpr;
import javassist.compiler.ast.IntConst;
import javassist.compiler.ast.Keyword;
import javassist.compiler.ast.Member;
import javassist.compiler.ast.MethodDecl;
import javassist.compiler.ast.NewExpr;
import javassist.compiler.ast.Pair;
import javassist.compiler.ast.Stmnt;
import javassist.compiler.ast.StringL;
import javassist.compiler.ast.Symbol;
import javassist.compiler.ast.Variable;
import javassist.compiler.ast.Visitor;

public abstract class CodeGen extends Visitor implements Opcode, TokenId {
    private static final int P_DOUBLE = 0;
    private static final int P_FLOAT = 1;
    private static final int P_INT = 3;
    private static final int P_LONG = 2;
    private static final int P_OTHER = -1;
    static final int[] binOp = {43, 99, 98, 97, 96, 45, 103, 102, 101, 100, 42, 107, 106, 105, 104, 47, Opcode.DDIV, Opcode.FDIV, Opcode.LDIV, Opcode.IDIV, 37, Opcode.DREM, Opcode.FREM, Opcode.LREM, Opcode.IREM, Opcode.IUSHR, 0, 0, Opcode.LOR, 128, 94, 0, 0, Opcode.LXOR, Opcode.IXOR, 38, 0, 0, Opcode.LAND, Opcode.IAND, TokenId.LSHIFT, 0, 0, Opcode.LSHL, Opcode.ISHL, TokenId.RSHIFT, 0, 0, Opcode.LSHR, Opcode.ISHR, TokenId.ARSHIFT, 0, 0, Opcode.LUSHR, Opcode.IUSHR};
    private static final int[] castOp = {0, Opcode.D2F, Opcode.D2L, Opcode.D2I, Opcode.F2D, 0, Opcode.F2L, Opcode.F2I, Opcode.L2D, Opcode.L2F, 0, Opcode.L2I, Opcode.I2D, Opcode.I2F, Opcode.I2L, 0};
    private static final int[] ifOp = {TokenId.EQ, Opcode.IF_ICMPEQ, Opcode.IF_ICMPNE, TokenId.NEQ, Opcode.IF_ICMPNE, Opcode.IF_ICMPEQ, TokenId.LE, Opcode.IF_ICMPLE, Opcode.IF_ICMPGT, TokenId.GE, Opcode.IF_ICMPGE, Opcode.IF_ICMPLT, 60, Opcode.IF_ICMPLT, Opcode.IF_ICMPGE, 62, Opcode.IF_ICMPGT, Opcode.IF_ICMPLE};
    private static final int[] ifOp2 = {TokenId.EQ, Opcode.IFEQ, Opcode.IFNE, TokenId.NEQ, Opcode.IFNE, Opcode.IFEQ, TokenId.LE, Opcode.IFLE, Opcode.IFGT, TokenId.GE, Opcode.IFGE, Opcode.IFLT, 60, Opcode.IFLT, Opcode.IFGE, 62, Opcode.IFGT, Opcode.IFLE};
    static final String javaLangObject = "java.lang.Object";
    static final String javaLangString = "java.lang.String";
    static final String jvmJavaLangObject = "java/lang/Object";
    static final String jvmJavaLangString = "java/lang/String";
    protected int arrayDim;
    protected List<Integer> breakList = null;
    protected Bytecode bytecode;
    protected String className;
    protected List<Integer> continueList = null;
    protected int exprType;
    protected boolean hasReturned = false;
    public boolean inStaticMethod = false;
    protected ReturnHook returnHooks = null;
    private int tempVar = -1;
    TypeChecker typeChecker = null;

    @Override // javassist.compiler.ast.Visitor
    public abstract void atArrayInit(ArrayInit arrayInit) throws CompileError;

    /* access modifiers changed from: protected */
    public abstract void atArrayVariableAssign(ArrayInit arrayInit, int i, int i2, String str) throws CompileError;

    @Override // javassist.compiler.ast.Visitor
    public abstract void atCallExpr(CallExpr callExpr) throws CompileError;

    /* access modifiers changed from: protected */
    public abstract void atFieldAssign(Expr expr, int i, ASTree aSTree, ASTree aSTree2, boolean z) throws CompileError;

    /* access modifiers changed from: protected */
    public abstract void atFieldPlusPlus(int i, boolean z, ASTree aSTree, Expr expr, boolean z2) throws CompileError;

    /* access modifiers changed from: protected */
    public abstract void atFieldRead(ASTree aSTree) throws CompileError;

    @Override // javassist.compiler.ast.Visitor
    public abstract void atMember(Member member) throws CompileError;

    @Override // javassist.compiler.ast.Visitor
    public abstract void atNewExpr(NewExpr newExpr) throws CompileError;

    /* access modifiers changed from: protected */
    public abstract String getSuperName() throws CompileError;

    /* access modifiers changed from: protected */
    public abstract String getThisName();

    /* access modifiers changed from: protected */
    public abstract void insertDefaultSuperCall() throws CompileError;

    /* access modifiers changed from: protected */
    public abstract String resolveClassName(String str) throws CompileError;

    /* access modifiers changed from: protected */
    public abstract String resolveClassName(ASTList aSTList) throws CompileError;

    /* access modifiers changed from: protected */
    public static abstract class ReturnHook {
        ReturnHook next;

        /* access modifiers changed from: protected */
        public abstract boolean doit(Bytecode bytecode, int i);

        protected ReturnHook(CodeGen gen) {
            this.next = gen.returnHooks;
            gen.returnHooks = this;
        }

        /* access modifiers changed from: protected */
        public void remove(CodeGen gen) {
            gen.returnHooks = this.next;
        }
    }

    public CodeGen(Bytecode b) {
        this.bytecode = b;
    }

    public void setTypeChecker(TypeChecker checker) {
        this.typeChecker = checker;
    }

    protected static void fatal() throws CompileError {
        throw new CompileError("fatal");
    }

    public static boolean is2word(int type, int dim) {
        return dim == 0 && (type == 312 || type == 326);
    }

    public int getMaxLocals() {
        return this.bytecode.getMaxLocals();
    }

    public void setMaxLocals(int n) {
        this.bytecode.setMaxLocals(n);
    }

    /* access modifiers changed from: protected */
    public void incMaxLocals(int size) {
        this.bytecode.incMaxLocals(size);
    }

    /* access modifiers changed from: protected */
    public int getTempVar() {
        if (this.tempVar < 0) {
            this.tempVar = getMaxLocals();
            incMaxLocals(2);
        }
        return this.tempVar;
    }

    /* access modifiers changed from: protected */
    public int getLocalVar(Declarator d) {
        int v = d.getLocalVar();
        if (v >= 0) {
            return v;
        }
        int v2 = getMaxLocals();
        d.setLocalVar(v2);
        incMaxLocals(1);
        return v2;
    }

    protected static String toJvmArrayName(String name, int dim) {
        if (name == null) {
            return null;
        }
        if (dim == 0) {
            return name;
        }
        StringBuffer sbuf = new StringBuffer();
        int d = dim;
        while (true) {
            d--;
            if (d > 0) {
                sbuf.append('[');
            } else {
                sbuf.append('L');
                sbuf.append(name);
                sbuf.append(';');
                return sbuf.toString();
            }
        }
    }

    protected static String toJvmTypeName(int type, int dim) {
        char c = 'I';
        switch (type) {
            case TokenId.BOOLEAN /*{ENCODED_INT: 301}*/:
                c = 'Z';
                break;
            case TokenId.BYTE /*{ENCODED_INT: 303}*/:
                c = 'B';
                break;
            case TokenId.CHAR /*{ENCODED_INT: 306}*/:
                c = 'C';
                break;
            case TokenId.DOUBLE /*{ENCODED_INT: 312}*/:
                c = 'D';
                break;
            case TokenId.FLOAT /*{ENCODED_INT: 317}*/:
                c = 'F';
                break;
            case TokenId.INT /*{ENCODED_INT: 324}*/:
                c = 'I';
                break;
            case TokenId.LONG /*{ENCODED_INT: 326}*/:
                c = 'J';
                break;
            case TokenId.SHORT /*{ENCODED_INT: 334}*/:
                c = 'S';
                break;
            case TokenId.VOID /*{ENCODED_INT: 344}*/:
                c = 'V';
                break;
        }
        StringBuffer sbuf = new StringBuffer();
        while (true) {
            dim--;
            if (dim > 0) {
                sbuf.append('[');
            } else {
                sbuf.append(c);
                return sbuf.toString();
            }
        }
    }

    public void compileExpr(ASTree expr) throws CompileError {
        doTypeCheck(expr);
        expr.accept(this);
    }

    public boolean compileBooleanExpr(boolean branchIf, ASTree expr) throws CompileError {
        doTypeCheck(expr);
        return booleanExpr(branchIf, expr);
    }

    public void doTypeCheck(ASTree expr) throws CompileError {
        if (this.typeChecker != null) {
            expr.accept(this.typeChecker);
        }
    }

    @Override // javassist.compiler.ast.Visitor
    public void atASTList(ASTList n) throws CompileError {
        fatal();
    }

    @Override // javassist.compiler.ast.Visitor
    public void atPair(Pair n) throws CompileError {
        fatal();
    }

    @Override // javassist.compiler.ast.Visitor
    public void atSymbol(Symbol n) throws CompileError {
        fatal();
    }

    @Override // javassist.compiler.ast.Visitor
    public void atFieldDecl(FieldDecl field) throws CompileError {
        field.getInit().accept(this);
    }

    @Override // javassist.compiler.ast.Visitor
    public void atMethodDecl(MethodDecl method) throws CompileError {
        ASTList mods = method.getModifiers();
        setMaxLocals(1);
        while (mods != null) {
            mods = mods.tail();
            if (((Keyword) mods.head()).get() == 335) {
                setMaxLocals(0);
                this.inStaticMethod = true;
            }
        }
        for (ASTList params = method.getParams(); params != null; params = params.tail()) {
            atDeclarator((Declarator) params.head());
        }
        atMethodBody(method.getBody(), method.isConstructor(), method.getReturn().getType() == 344);
    }

    public void atMethodBody(Stmnt s, boolean isCons, boolean isVoid) throws CompileError {
        if (s != null) {
            if (isCons && needsSuperCall(s)) {
                insertDefaultSuperCall();
            }
            this.hasReturned = false;
            s.accept(this);
            if (this.hasReturned) {
                return;
            }
            if (isVoid) {
                this.bytecode.addOpcode(Opcode.RETURN);
                this.hasReturned = true;
                return;
            }
            throw new CompileError("no return statement");
        }
    }

    private boolean needsSuperCall(Stmnt body) throws CompileError {
        ASTree expr;
        if (body.getOperator() == 66) {
            body = (Stmnt) body.head();
        }
        if (body != null && body.getOperator() == 69 && (expr = body.head()) != null && (expr instanceof Expr) && ((Expr) expr).getOperator() == 67) {
            ASTree target = ((Expr) expr).head();
            if (target instanceof Keyword) {
                int token = ((Keyword) target).get();
                if (token == 339 || token == 336) {
                    return false;
                }
                return true;
            }
        }
        return true;
    }

    @Override // javassist.compiler.ast.Visitor
    public void atStmnt(Stmnt st) throws CompileError {
        boolean z = true;
        if (st != null) {
            int op = st.getOperator();
            if (op == 69) {
                ASTree expr = st.getLeft();
                doTypeCheck(expr);
                if (expr instanceof AssignExpr) {
                    atAssignExpr((AssignExpr) expr, false);
                } else if (isPlusPlusExpr(expr)) {
                    Expr e = (Expr) expr;
                    atPlusPlus(e.getOperator(), e.oprand1(), e, false);
                } else {
                    expr.accept(this);
                    if (is2word(this.exprType, this.arrayDim)) {
                        this.bytecode.addOpcode(88);
                    } else if (this.exprType != 344) {
                        this.bytecode.addOpcode(87);
                    }
                }
            } else if (op == 68 || op == 66) {
                ASTList list = st;
                while (list != null) {
                    ASTree h = list.head();
                    list = list.tail();
                    if (h != null) {
                        h.accept(this);
                    }
                }
            } else if (op == 320) {
                atIfStmnt(st);
            } else if (op == 346 || op == 311) {
                if (op != 346) {
                    z = false;
                }
                atWhileStmnt(st, z);
            } else if (op == 318) {
                atForStmnt(st);
            } else if (op == 302 || op == 309) {
                if (op != 302) {
                    z = false;
                }
                atBreakStmnt(st, z);
            } else if (op == 333) {
                atReturnStmnt(st);
            } else if (op == 340) {
                atThrowStmnt(st);
            } else if (op == 343) {
                atTryStmnt(st);
            } else if (op == 337) {
                atSwitchStmnt(st);
            } else if (op == 338) {
                atSyncStmnt(st);
            } else {
                this.hasReturned = false;
                throw new CompileError("sorry, not supported statement: TokenId " + op);
            }
        }
    }

    private void atIfStmnt(Stmnt st) throws CompileError {
        boolean z = false;
        ASTree expr = st.head();
        Stmnt thenp = (Stmnt) st.tail().head();
        Stmnt elsep = (Stmnt) st.tail().tail().head();
        if (compileBooleanExpr(false, expr)) {
            this.hasReturned = false;
            if (elsep != null) {
                elsep.accept(this);
                return;
            }
            return;
        }
        int pc = this.bytecode.currentPc();
        int pc2 = 0;
        this.bytecode.addIndex(0);
        this.hasReturned = false;
        if (thenp != null) {
            thenp.accept(this);
        }
        boolean thenHasReturned = this.hasReturned;
        this.hasReturned = false;
        if (elsep != null && !thenHasReturned) {
            this.bytecode.addOpcode(Opcode.GOTO);
            pc2 = this.bytecode.currentPc();
            this.bytecode.addIndex(0);
        }
        this.bytecode.write16bit(pc, (this.bytecode.currentPc() - pc) + 1);
        if (elsep != null) {
            elsep.accept(this);
            if (!thenHasReturned) {
                this.bytecode.write16bit(pc2, (this.bytecode.currentPc() - pc2) + 1);
            }
            if (thenHasReturned && this.hasReturned) {
                z = true;
            }
            this.hasReturned = z;
        }
    }

    private void atWhileStmnt(Stmnt st, boolean notDo) throws CompileError {
        List<Integer> prevBreakList = this.breakList;
        List<Integer> prevContList = this.continueList;
        this.breakList = new ArrayList();
        this.continueList = new ArrayList();
        ASTree expr = st.head();
        Stmnt body = (Stmnt) st.tail();
        int pc = 0;
        if (notDo) {
            this.bytecode.addOpcode(Opcode.GOTO);
            pc = this.bytecode.currentPc();
            this.bytecode.addIndex(0);
        }
        int pc2 = this.bytecode.currentPc();
        if (body != null) {
            body.accept(this);
        }
        int pc3 = this.bytecode.currentPc();
        if (notDo) {
            this.bytecode.write16bit(pc, (pc3 - pc) + 1);
        }
        boolean alwaysBranch = compileBooleanExpr(true, expr);
        if (alwaysBranch) {
            this.bytecode.addOpcode(Opcode.GOTO);
            alwaysBranch = this.breakList.size() == 0;
        }
        this.bytecode.addIndex((pc2 - this.bytecode.currentPc()) + 1);
        patchGoto(this.breakList, this.bytecode.currentPc());
        patchGoto(this.continueList, pc3);
        this.continueList = prevContList;
        this.breakList = prevBreakList;
        this.hasReturned = alwaysBranch;
    }

    /* access modifiers changed from: protected */
    public void patchGoto(List<Integer> list, int targetPc) {
        for (Integer num : list) {
            int pc = num.intValue();
            this.bytecode.write16bit(pc, (targetPc - pc) + 1);
        }
    }

    private void atForStmnt(Stmnt st) throws CompileError {
        List<Integer> prevBreakList = this.breakList;
        List<Integer> prevContList = this.continueList;
        this.breakList = new ArrayList();
        this.continueList = new ArrayList();
        Stmnt init = (Stmnt) st.head();
        ASTList p = st.tail();
        ASTree expr = p.head();
        ASTList p2 = p.tail();
        Stmnt update = (Stmnt) p2.head();
        Stmnt body = (Stmnt) p2.tail();
        if (init != null) {
            init.accept(this);
        }
        int pc = this.bytecode.currentPc();
        int pc2 = 0;
        if (expr != null) {
            if (compileBooleanExpr(false, expr)) {
                this.continueList = prevContList;
                this.breakList = prevBreakList;
                this.hasReturned = false;
                return;
            }
            pc2 = this.bytecode.currentPc();
            this.bytecode.addIndex(0);
        }
        if (body != null) {
            body.accept(this);
        }
        int pc3 = this.bytecode.currentPc();
        if (update != null) {
            update.accept(this);
        }
        this.bytecode.addOpcode(Opcode.GOTO);
        this.bytecode.addIndex((pc - this.bytecode.currentPc()) + 1);
        int pc4 = this.bytecode.currentPc();
        if (expr != null) {
            this.bytecode.write16bit(pc2, (pc4 - pc2) + 1);
        }
        patchGoto(this.breakList, pc4);
        patchGoto(this.continueList, pc3);
        this.continueList = prevContList;
        this.breakList = prevBreakList;
        this.hasReturned = false;
    }

    private void atSwitchStmnt(Stmnt st) throws CompileError {
        long caseLabel;
        int ipairs;
        boolean isString = false;
        if (this.typeChecker != null) {
            doTypeCheck(st.head());
            isString = this.typeChecker.exprType == 307 && this.typeChecker.arrayDim == 0 && jvmJavaLangString.equals(this.typeChecker.className);
        }
        compileExpr(st.head());
        int tmpVar = -1;
        if (isString) {
            tmpVar = getMaxLocals();
            incMaxLocals(1);
            this.bytecode.addAstore(tmpVar);
            this.bytecode.addAload(tmpVar);
            this.bytecode.addInvokevirtual(jvmJavaLangString, "hashCode", "()I");
        }
        List<Integer> prevBreakList = this.breakList;
        this.breakList = new ArrayList();
        int opcodePc = this.bytecode.currentPc();
        this.bytecode.addOpcode(Opcode.LOOKUPSWITCH);
        int npads = 3 - (opcodePc & 3);
        while (true) {
            npads--;
            if (npads <= 0) {
                break;
            }
            this.bytecode.add(0);
        }
        Stmnt body = (Stmnt) st.tail();
        int npairs = 0;
        for (ASTList list = body; list != null; list = list.tail()) {
            if (((Stmnt) list.head()).getOperator() == 304) {
                npairs++;
            }
        }
        int opcodePc2 = this.bytecode.currentPc();
        this.bytecode.addGap(4);
        this.bytecode.add32bit(npairs);
        this.bytecode.addGap(npairs * 8);
        long[] pairs = new long[npairs];
        ArrayList<Integer> gotoDefaults = new ArrayList<>();
        int defaultPc = -1;
        ASTList list2 = body;
        int ipairs2 = 0;
        while (list2 != null) {
            Stmnt label = (Stmnt) list2.head();
            int op = label.getOperator();
            if (op == 310) {
                defaultPc = this.bytecode.currentPc();
                ipairs = ipairs2;
            } else if (op != 304) {
                fatal();
                ipairs = ipairs2;
            } else {
                int curPos = this.bytecode.currentPc();
                if (isString) {
                    caseLabel = (long) computeStringLabel(label.head(), tmpVar, gotoDefaults);
                } else {
                    caseLabel = (long) computeLabel(label.head());
                }
                ipairs = ipairs2 + 1;
                pairs[ipairs2] = (caseLabel << 32) + (((long) (curPos - opcodePc)) & -1);
            }
            this.hasReturned = false;
            ((Stmnt) label.tail()).accept(this);
            list2 = list2.tail();
            ipairs2 = ipairs;
        }
        Arrays.sort(pairs);
        int pc = opcodePc2 + 8;
        for (int i = 0; i < npairs; i++) {
            this.bytecode.write32bit(pc, (int) (pairs[i] >>> 32));
            this.bytecode.write32bit(pc + 4, (int) pairs[i]);
            pc += 8;
        }
        if (defaultPc < 0 || this.breakList.size() > 0) {
            this.hasReturned = false;
        }
        int endPc = this.bytecode.currentPc();
        if (defaultPc < 0) {
            defaultPc = endPc;
        }
        this.bytecode.write32bit(opcodePc2, defaultPc - opcodePc);
        Iterator<Integer> it = gotoDefaults.iterator();
        while (it.hasNext()) {
            int addr = it.next().intValue();
            this.bytecode.write16bit(addr, (defaultPc - addr) + 1);
        }
        patchGoto(this.breakList, endPc);
        this.breakList = prevBreakList;
    }

    private int computeLabel(ASTree expr) throws CompileError {
        doTypeCheck(expr);
        ASTree expr2 = TypeChecker.stripPlusExpr(expr);
        if (expr2 instanceof IntConst) {
            return (int) ((IntConst) expr2).get();
        }
        throw new CompileError("bad case label");
    }

    private int computeStringLabel(ASTree expr, int tmpVar, List<Integer> gotoDefaults) throws CompileError {
        doTypeCheck(expr);
        ASTree expr2 = TypeChecker.stripPlusExpr(expr);
        if (expr2 instanceof StringL) {
            String label = ((StringL) expr2).get();
            this.bytecode.addAload(tmpVar);
            this.bytecode.addLdc(label);
            this.bytecode.addInvokevirtual(jvmJavaLangString, "equals", "(Ljava/lang/Object;)Z");
            this.bytecode.addOpcode(Opcode.IFEQ);
            Integer pc = Integer.valueOf(this.bytecode.currentPc());
            this.bytecode.addIndex(0);
            gotoDefaults.add(pc);
            return label.hashCode();
        }
        throw new CompileError("bad case label");
    }

    private void atBreakStmnt(Stmnt st, boolean notCont) throws CompileError {
        if (st.head() != null) {
            throw new CompileError("sorry, not support labeled break or continue");
        }
        this.bytecode.addOpcode(Opcode.GOTO);
        Integer pc = Integer.valueOf(this.bytecode.currentPc());
        this.bytecode.addIndex(0);
        if (notCont) {
            this.breakList.add(pc);
        } else {
            this.continueList.add(pc);
        }
    }

    /* access modifiers changed from: protected */
    public void atReturnStmnt(Stmnt st) throws CompileError {
        atReturnStmnt2(st.getLeft());
    }

    /* access modifiers changed from: protected */
    public final void atReturnStmnt2(ASTree result) throws CompileError {
        int op;
        if (result == null) {
            op = Opcode.RETURN;
        } else {
            compileExpr(result);
            if (this.arrayDim > 0) {
                op = Opcode.ARETURN;
            } else {
                int type = this.exprType;
                if (type == 312) {
                    op = Opcode.DRETURN;
                } else if (type == 317) {
                    op = Opcode.FRETURN;
                } else if (type == 326) {
                    op = Opcode.LRETURN;
                } else if (isRefType(type)) {
                    op = Opcode.ARETURN;
                } else {
                    op = Opcode.IRETURN;
                }
            }
        }
        for (ReturnHook har = this.returnHooks; har != null; har = har.next) {
            if (har.doit(this.bytecode, op)) {
                this.hasReturned = true;
                return;
            }
        }
        this.bytecode.addOpcode(op);
        this.hasReturned = true;
    }

    private void atThrowStmnt(Stmnt st) throws CompileError {
        compileExpr(st.getLeft());
        if (this.exprType != 307 || this.arrayDim > 0) {
            throw new CompileError("bad throw statement");
        }
        this.bytecode.addOpcode(Opcode.ATHROW);
        this.hasReturned = true;
    }

    /* access modifiers changed from: protected */
    public void atTryStmnt(Stmnt st) throws CompileError {
        this.hasReturned = false;
    }

    private void atSyncStmnt(Stmnt st) throws CompileError {
        int nbreaks = getListSize(this.breakList);
        int ncontinues = getListSize(this.continueList);
        compileExpr(st.head());
        if (this.exprType == 307 || this.arrayDim != 0) {
            Bytecode bc = this.bytecode;
            final int var = bc.getMaxLocals();
            bc.incMaxLocals(1);
            bc.addOpcode(89);
            bc.addAstore(var);
            bc.addOpcode(Opcode.MONITORENTER);
            ReturnHook rh = new ReturnHook(this) {
                /* class javassist.compiler.CodeGen.AnonymousClass1 */

                /* access modifiers changed from: protected */
                @Override // javassist.compiler.CodeGen.ReturnHook
                public boolean doit(Bytecode b, int opcode) {
                    b.addAload(var);
                    b.addOpcode(Opcode.MONITOREXIT);
                    return false;
                }
            };
            int pc = bc.currentPc();
            Stmnt body = (Stmnt) st.tail();
            if (body != null) {
                body.accept(this);
            }
            int pc2 = bc.currentPc();
            int pc3 = 0;
            if (!this.hasReturned) {
                rh.doit(bc, 0);
                bc.addOpcode(Opcode.GOTO);
                pc3 = bc.currentPc();
                bc.addIndex(0);
            }
            if (pc < pc2) {
                int pc4 = bc.currentPc();
                rh.doit(bc, 0);
                bc.addOpcode(Opcode.ATHROW);
                bc.addExceptionHandler(pc, pc2, pc4, 0);
            }
            if (!this.hasReturned) {
                bc.write16bit(pc3, (bc.currentPc() - pc3) + 1);
            }
            rh.remove(this);
            if (getListSize(this.breakList) != nbreaks || getListSize(this.continueList) != ncontinues) {
                throw new CompileError("sorry, cannot break/continue in synchronized block");
            }
            return;
        }
        throw new CompileError("bad type expr for synchronized block");
    }

    private static int getListSize(List<Integer> list) {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    private static boolean isPlusPlusExpr(ASTree expr) {
        if (!(expr instanceof Expr)) {
            return false;
        }
        int op = ((Expr) expr).getOperator();
        if (op == 362 || op == 363) {
            return true;
        }
        return false;
    }

    @Override // javassist.compiler.ast.Visitor
    public void atDeclarator(Declarator d) throws CompileError {
        int size;
        d.setLocalVar(getMaxLocals());
        d.setClassName(resolveClassName(d.getClassName()));
        if (is2word(d.getType(), d.getArrayDim())) {
            size = 2;
        } else {
            size = 1;
        }
        incMaxLocals(size);
        ASTree init = d.getInitializer();
        if (init != null) {
            doTypeCheck(init);
            atVariableAssign(null, 61, null, d, init, false);
        }
    }

    @Override // javassist.compiler.ast.Visitor
    public void atAssignExpr(AssignExpr expr) throws CompileError {
        atAssignExpr(expr, true);
    }

    /* access modifiers changed from: protected */
    public void atAssignExpr(AssignExpr expr, boolean doDup) throws CompileError {
        int op = expr.getOperator();
        ASTree left = expr.oprand1();
        ASTree right = expr.oprand2();
        if (left instanceof Variable) {
            atVariableAssign(expr, op, (Variable) left, ((Variable) left).getDeclarator(), right, doDup);
        } else if (!(left instanceof Expr) || ((Expr) left).getOperator() != 65) {
            atFieldAssign(expr, op, left, right, doDup);
        } else {
            atArrayAssign(expr, op, (Expr) left, right, doDup);
        }
    }

    protected static void badAssign(Expr expr) throws CompileError {
        String msg;
        if (expr == null) {
            msg = "incompatible type for assignment";
        } else {
            msg = "incompatible type for " + expr.getName();
        }
        throw new CompileError(msg);
    }

    private void atVariableAssign(Expr expr, int op, Variable var, Declarator d, ASTree right, boolean doDup) throws CompileError {
        int varType = d.getType();
        int varArray = d.getArrayDim();
        String varClass = d.getClassName();
        int varNo = getLocalVar(d);
        if (op != 61) {
            atVariable(var);
        }
        if (expr != null || !(right instanceof ArrayInit)) {
            atAssignCore(expr, op, right, varType, varArray, varClass);
        } else {
            atArrayVariableAssign((ArrayInit) right, varType, varArray, varClass);
        }
        if (doDup) {
            if (is2word(varType, varArray)) {
                this.bytecode.addOpcode(92);
            } else {
                this.bytecode.addOpcode(89);
            }
        }
        if (varArray > 0) {
            this.bytecode.addAstore(varNo);
        } else if (varType == 312) {
            this.bytecode.addDstore(varNo);
        } else if (varType == 317) {
            this.bytecode.addFstore(varNo);
        } else if (varType == 326) {
            this.bytecode.addLstore(varNo);
        } else if (isRefType(varType)) {
            this.bytecode.addAstore(varNo);
        } else {
            this.bytecode.addIstore(varNo);
        }
        this.exprType = varType;
        this.arrayDim = varArray;
        this.className = varClass;
    }

    private void atArrayAssign(Expr expr, int op, Expr array, ASTree right, boolean doDup) throws CompileError {
        arrayAccess(array.oprand1(), array.oprand2());
        if (op != 61) {
            this.bytecode.addOpcode(92);
            this.bytecode.addOpcode(getArrayReadOp(this.exprType, this.arrayDim));
        }
        int aType = this.exprType;
        int aDim = this.arrayDim;
        String cname = this.className;
        atAssignCore(expr, op, right, aType, aDim, cname);
        if (doDup) {
            if (is2word(aType, aDim)) {
                this.bytecode.addOpcode(94);
            } else {
                this.bytecode.addOpcode(91);
            }
        }
        this.bytecode.addOpcode(getArrayWriteOp(aType, aDim));
        this.exprType = aType;
        this.arrayDim = aDim;
        this.className = cname;
    }

    /* access modifiers changed from: protected */
    public void atAssignCore(Expr expr, int op, ASTree right, int type, int dim, String cname) throws CompileError {
        if (op == 354 && dim == 0 && type == 307) {
            atStringPlusEq(expr, type, dim, cname, right);
        } else {
            right.accept(this);
            if (invalidDim(this.exprType, this.arrayDim, this.className, type, dim, cname, false) || (op != 61 && dim > 0)) {
                badAssign(expr);
            }
            if (op != 61) {
                int token = assignOps[op - 351];
                int k = lookupBinOp(token);
                if (k < 0) {
                    fatal();
                }
                atArithBinExpr(expr, token, k, type);
            }
        }
        if (op != 61 || (dim == 0 && !isRefType(type))) {
            atNumCastExpr(this.exprType, type);
        }
    }

    private void atStringPlusEq(Expr expr, int type, int dim, String cname, ASTree right) throws CompileError {
        if (!jvmJavaLangString.equals(cname)) {
            badAssign(expr);
        }
        convToString(type, dim);
        right.accept(this);
        convToString(this.exprType, this.arrayDim);
        this.bytecode.addInvokevirtual(javaLangString, "concat", "(Ljava/lang/String;)Ljava/lang/String;");
        this.exprType = TokenId.CLASS;
        this.arrayDim = 0;
        this.className = jvmJavaLangString;
    }

    private boolean invalidDim(int srcType, int srcDim, String srcClass, int destType, int destDim, String destClass, boolean isCast) {
        if (srcDim == destDim || srcType == 412) {
            return false;
        }
        if (destDim == 0 && destType == 307 && jvmJavaLangObject.equals(destClass)) {
            return false;
        }
        if (!isCast || srcDim != 0 || srcType != 307 || !jvmJavaLangObject.equals(srcClass)) {
            return true;
        }
        return false;
    }

    @Override // javassist.compiler.ast.Visitor
    public void atCondExpr(CondExpr expr) throws CompileError {
        if (booleanExpr(false, expr.condExpr())) {
            expr.elseExpr().accept(this);
            return;
        }
        int pc = this.bytecode.currentPc();
        this.bytecode.addIndex(0);
        expr.thenExpr().accept(this);
        int dim1 = this.arrayDim;
        this.bytecode.addOpcode(Opcode.GOTO);
        int pc2 = this.bytecode.currentPc();
        this.bytecode.addIndex(0);
        this.bytecode.write16bit(pc, (this.bytecode.currentPc() - pc) + 1);
        expr.elseExpr().accept(this);
        if (dim1 != this.arrayDim) {
            throw new CompileError("type mismatch in ?:");
        }
        this.bytecode.write16bit(pc2, (this.bytecode.currentPc() - pc2) + 1);
    }

    static int lookupBinOp(int token) {
        int[] code = binOp;
        int s = code.length;
        for (int k = 0; k < s; k += 5) {
            if (code[k] == token) {
                return k;
            }
        }
        return -1;
    }

    @Override // javassist.compiler.ast.Visitor
    public void atBinExpr(BinExpr expr) throws CompileError {
        int token = expr.getOperator();
        int k = lookupBinOp(token);
        if (k >= 0) {
            expr.oprand1().accept(this);
            ASTree right = expr.oprand2();
            if (right != null) {
                int type1 = this.exprType;
                int dim1 = this.arrayDim;
                String cname1 = this.className;
                right.accept(this);
                if (dim1 != this.arrayDim) {
                    throw new CompileError("incompatible array types");
                } else if (token == 43 && dim1 == 0 && (type1 == 307 || this.exprType == 307)) {
                    atStringConcatExpr(expr, type1, dim1, cname1);
                } else {
                    atArithBinExpr(expr, token, k, type1);
                }
            }
        } else {
            if (!booleanExpr(true, expr)) {
                this.bytecode.addIndex(7);
                this.bytecode.addIconst(0);
                this.bytecode.addOpcode(Opcode.GOTO);
                this.bytecode.addIndex(4);
            }
            this.bytecode.addIconst(1);
        }
    }

    private void atArithBinExpr(Expr expr, int token, int index, int type1) throws CompileError {
        int op;
        if (this.arrayDim != 0) {
            badTypes(expr);
        }
        int type2 = this.exprType;
        if (token != 364 && token != 366 && token != 370) {
            convertOprandTypes(type1, type2, expr);
        } else if (type2 == 324 || type2 == 334 || type2 == 306 || type2 == 303) {
            this.exprType = type1;
        } else {
            badTypes(expr);
        }
        int p = typePrecedence(this.exprType);
        if (p < 0 || (op = binOp[index + p + 1]) == 0) {
            badTypes(expr);
            return;
        }
        if (p == 3 && this.exprType != 301) {
            this.exprType = TokenId.INT;
        }
        this.bytecode.addOpcode(op);
    }

    private void atStringConcatExpr(Expr expr, int type1, int dim1, String cname1) throws CompileError {
        boolean type2IsString;
        int type2 = this.exprType;
        int dim2 = this.arrayDim;
        boolean type2Is2 = is2word(type2, dim2);
        if (type2 != 307 || !jvmJavaLangString.equals(this.className)) {
            type2IsString = false;
        } else {
            type2IsString = true;
        }
        if (type2Is2) {
            convToString(type2, dim2);
        }
        if (is2word(type1, dim1)) {
            this.bytecode.addOpcode(91);
            this.bytecode.addOpcode(87);
        } else {
            this.bytecode.addOpcode(95);
        }
        convToString(type1, dim1);
        this.bytecode.addOpcode(95);
        if (!type2Is2 && !type2IsString) {
            convToString(type2, dim2);
        }
        this.bytecode.addInvokevirtual(javaLangString, "concat", "(Ljava/lang/String;)Ljava/lang/String;");
        this.exprType = TokenId.CLASS;
        this.arrayDim = 0;
        this.className = jvmJavaLangString;
    }

    private void convToString(int type, int dim) throws CompileError {
        if (isRefType(type) || dim > 0) {
            this.bytecode.addInvokestatic(javaLangString, "valueOf", "(Ljava/lang/Object;)Ljava/lang/String;");
        } else if (type == 312) {
            this.bytecode.addInvokestatic(javaLangString, "valueOf", "(D)Ljava/lang/String;");
        } else if (type == 317) {
            this.bytecode.addInvokestatic(javaLangString, "valueOf", "(F)Ljava/lang/String;");
        } else if (type == 326) {
            this.bytecode.addInvokestatic(javaLangString, "valueOf", "(J)Ljava/lang/String;");
        } else if (type == 301) {
            this.bytecode.addInvokestatic(javaLangString, "valueOf", "(Z)Ljava/lang/String;");
        } else if (type == 306) {
            this.bytecode.addInvokestatic(javaLangString, "valueOf", "(C)Ljava/lang/String;");
        } else if (type == 344) {
            throw new CompileError("void type expression");
        } else {
            this.bytecode.addInvokestatic(javaLangString, "valueOf", "(I)Ljava/lang/String;");
        }
    }

    private boolean booleanExpr(boolean branchIf, ASTree expr) throws CompileError {
        boolean isAndAnd;
        boolean z;
        boolean z2 = true;
        int op = getCompOperator(expr);
        if (op == 358) {
            BinExpr bexpr = (BinExpr) expr;
            compareExpr(branchIf, bexpr.getOperator(), compileOprands(bexpr), bexpr);
        } else if (op == 33) {
            if (branchIf) {
                z2 = false;
            }
            return booleanExpr(z2, ((Expr) expr).oprand1());
        } else {
            if (op == 369) {
                isAndAnd = true;
            } else {
                isAndAnd = false;
            }
            if (isAndAnd || op == 368) {
                BinExpr bexpr2 = (BinExpr) expr;
                if (!isAndAnd) {
                    z = true;
                } else {
                    z = false;
                }
                if (booleanExpr(z, bexpr2.oprand1())) {
                    this.exprType = TokenId.BOOLEAN;
                    this.arrayDim = 0;
                    return true;
                }
                int pc = this.bytecode.currentPc();
                this.bytecode.addIndex(0);
                if (booleanExpr(isAndAnd, bexpr2.oprand2())) {
                    this.bytecode.addOpcode(Opcode.GOTO);
                }
                this.bytecode.write16bit(pc, (this.bytecode.currentPc() - pc) + 3);
                if (branchIf != isAndAnd) {
                    this.bytecode.addIndex(6);
                    this.bytecode.addOpcode(Opcode.GOTO);
                }
            } else if (isAlwaysBranch(expr, branchIf)) {
                this.exprType = TokenId.BOOLEAN;
                this.arrayDim = 0;
                return true;
            } else {
                expr.accept(this);
                if (this.exprType == 301 && this.arrayDim == 0) {
                    this.bytecode.addOpcode(branchIf ? Opcode.IFNE : Opcode.IFEQ);
                } else {
                    throw new CompileError("boolean expr is required");
                }
            }
        }
        this.exprType = TokenId.BOOLEAN;
        this.arrayDim = 0;
        return false;
    }

    private static boolean isAlwaysBranch(ASTree expr, boolean branchIf) {
        if (!(expr instanceof Keyword)) {
            return false;
        }
        int t = ((Keyword) expr).get();
        return branchIf ? t == 410 : t == 411;
    }

    static int getCompOperator(ASTree expr) throws CompileError {
        if (!(expr instanceof Expr)) {
            return 32;
        }
        Expr bexpr = (Expr) expr;
        int token = bexpr.getOperator();
        if (token == 33) {
            return 33;
        }
        if (!(bexpr instanceof BinExpr) || token == 368 || token == 369 || token == 38 || token == 124) {
            return token;
        }
        return TokenId.EQ;
    }

    private int compileOprands(BinExpr expr) throws CompileError {
        expr.oprand1().accept(this);
        int type1 = this.exprType;
        int dim1 = this.arrayDim;
        expr.oprand2().accept(this);
        if (dim1 != this.arrayDim) {
            if (type1 != 412 && this.exprType != 412) {
                throw new CompileError("incompatible array types");
            } else if (this.exprType == 412) {
                this.arrayDim = dim1;
            }
        }
        if (type1 == 412) {
            return this.exprType;
        }
        return type1;
    }

    private void compareExpr(boolean branchIf, int token, int type1, BinExpr expr) throws CompileError {
        int i = Opcode.IF_ACMPNE;
        int i2 = Opcode.IF_ACMPEQ;
        int i3 = 1;
        if (this.arrayDim == 0) {
            convertOprandTypes(type1, this.exprType, expr);
        }
        int p = typePrecedence(this.exprType);
        if (p == -1 || this.arrayDim > 0) {
            if (token == 358) {
                Bytecode bytecode2 = this.bytecode;
                if (!branchIf) {
                    i2 = 166;
                }
                bytecode2.addOpcode(i2);
            } else if (token == 350) {
                Bytecode bytecode3 = this.bytecode;
                if (!branchIf) {
                    i = 165;
                }
                bytecode3.addOpcode(i);
            } else {
                badTypes(expr);
            }
        } else if (p == 3) {
            int[] op = ifOp;
            for (int i4 = 0; i4 < op.length; i4 += 3) {
                if (op[i4] == token) {
                    this.bytecode.addOpcode(op[(branchIf ? 1 : 2) + i4]);
                    return;
                }
            }
            badTypes(expr);
        } else {
            if (p == 0) {
                if (token == 60 || token == 357) {
                    this.bytecode.addOpcode(Opcode.DCMPG);
                } else {
                    this.bytecode.addOpcode(Opcode.DCMPL);
                }
            } else if (p == 1) {
                if (token == 60 || token == 357) {
                    this.bytecode.addOpcode(Opcode.FCMPG);
                } else {
                    this.bytecode.addOpcode(Opcode.FCMPL);
                }
            } else if (p == 2) {
                this.bytecode.addOpcode(Opcode.LCMP);
            } else {
                fatal();
            }
            int[] op2 = ifOp2;
            for (int i5 = 0; i5 < op2.length; i5 += 3) {
                if (op2[i5] == token) {
                    Bytecode bytecode4 = this.bytecode;
                    if (!branchIf) {
                        i3 = 2;
                    }
                    bytecode4.addOpcode(op2[i5 + i3]);
                    return;
                }
            }
            badTypes(expr);
        }
    }

    protected static void badTypes(Expr expr) throws CompileError {
        throw new CompileError("invalid types for " + expr.getName());
    }

    protected static boolean isRefType(int type) {
        return type == 307 || type == 412;
    }

    private static int typePrecedence(int type) {
        if (type == 312) {
            return 0;
        }
        if (type == 317) {
            return 1;
        }
        if (type == 326) {
            return 2;
        }
        if (isRefType(type) || type == 344) {
            return -1;
        }
        return 3;
    }

    static boolean isP_INT(int type) {
        return typePrecedence(type) == 3;
    }

    static boolean rightIsStrong(int type1, int type2) {
        int type1_p = typePrecedence(type1);
        int type2_p = typePrecedence(type2);
        return type1_p >= 0 && type2_p >= 0 && type1_p > type2_p;
    }

    private void convertOprandTypes(int type1, int type2, Expr expr) throws CompileError {
        boolean rightStrong;
        int op;
        int result_type;
        int type1_p = typePrecedence(type1);
        int type2_p = typePrecedence(type2);
        if (type2_p >= 0 || type1_p >= 0) {
            if (type2_p < 0 || type1_p < 0) {
                badTypes(expr);
            }
            if (type1_p <= type2_p) {
                rightStrong = false;
                this.exprType = type1;
                op = castOp[(type2_p * 4) + type1_p];
                result_type = type1_p;
            } else {
                rightStrong = true;
                op = castOp[(type1_p * 4) + type2_p];
                result_type = type2_p;
            }
            if (rightStrong) {
                if (result_type == 0 || result_type == 2) {
                    if (type1_p == 0 || type1_p == 2) {
                        this.bytecode.addOpcode(94);
                    } else {
                        this.bytecode.addOpcode(93);
                    }
                    this.bytecode.addOpcode(88);
                    this.bytecode.addOpcode(op);
                    this.bytecode.addOpcode(94);
                    this.bytecode.addOpcode(88);
                } else if (result_type == 1) {
                    if (type1_p == 2) {
                        this.bytecode.addOpcode(91);
                        this.bytecode.addOpcode(87);
                    } else {
                        this.bytecode.addOpcode(95);
                    }
                    this.bytecode.addOpcode(op);
                    this.bytecode.addOpcode(95);
                } else {
                    fatal();
                }
            } else if (op != 0) {
                this.bytecode.addOpcode(op);
            }
        }
    }

    @Override // javassist.compiler.ast.Visitor
    public void atCastExpr(CastExpr expr) throws CompileError {
        String cname = resolveClassName(expr.getClassName());
        String toClass = checkCastExpr(expr, cname);
        int srcType = this.exprType;
        this.exprType = expr.getType();
        this.arrayDim = expr.getArrayDim();
        this.className = cname;
        if (toClass == null) {
            atNumCastExpr(srcType, this.exprType);
        } else {
            this.bytecode.addCheckcast(toClass);
        }
    }

    @Override // javassist.compiler.ast.Visitor
    public void atInstanceOfExpr(InstanceOfExpr expr) throws CompileError {
        this.bytecode.addInstanceof(checkCastExpr(expr, resolveClassName(expr.getClassName())));
        this.exprType = TokenId.BOOLEAN;
        this.arrayDim = 0;
    }

    private String checkCastExpr(CastExpr expr, String name) throws CompileError {
        ASTree oprand = expr.getOprand();
        int dim = expr.getArrayDim();
        int type = expr.getType();
        oprand.accept(this);
        int srcType = this.exprType;
        int srcDim = this.arrayDim;
        if (invalidDim(srcType, this.arrayDim, this.className, type, dim, name, true) || srcType == 344 || type == 344) {
            throw new CompileError("invalid cast");
        } else if (type == 307) {
            if (isRefType(srcType) || srcDim != 0) {
                return toJvmArrayName(name, dim);
            }
            throw new CompileError("invalid cast");
        } else if (dim > 0) {
            return toJvmTypeName(type, dim);
        } else {
            return null;
        }
    }

    /* access modifiers changed from: package-private */
    public void atNumCastExpr(int srcType, int destType) throws CompileError {
        int op;
        int op2;
        if (srcType != destType) {
            int stype = typePrecedence(srcType);
            int dtype = typePrecedence(destType);
            if (stype < 0 || stype >= 3) {
                op = 0;
            } else {
                op = castOp[(stype * 4) + dtype];
            }
            if (destType == 312) {
                op2 = Opcode.I2D;
            } else if (destType == 317) {
                op2 = Opcode.I2F;
            } else if (destType == 326) {
                op2 = Opcode.I2L;
            } else if (destType == 334) {
                op2 = Opcode.I2S;
            } else if (destType == 306) {
                op2 = Opcode.I2C;
            } else if (destType == 303) {
                op2 = Opcode.I2B;
            } else {
                op2 = 0;
            }
            if (op != 0) {
                this.bytecode.addOpcode(op);
            }
            if ((op == 0 || op == 136 || op == 139 || op == 142) && op2 != 0) {
                this.bytecode.addOpcode(op2);
            }
        }
    }

    @Override // javassist.compiler.ast.Visitor
    public void atExpr(Expr expr) throws CompileError {
        int token = expr.getOperator();
        ASTree oprand = expr.oprand1();
        if (token == 46) {
            if (((Symbol) expr.oprand2()).get().equals("class")) {
                atClassObject(expr);
            } else {
                atFieldRead(expr);
            }
        } else if (token == 35) {
            atFieldRead(expr);
        } else if (token == 65) {
            atArrayRead(oprand, expr.oprand2());
        } else if (token == 362 || token == 363) {
            atPlusPlus(token, oprand, expr, true);
        } else if (token == 33) {
            if (!booleanExpr(false, expr)) {
                this.bytecode.addIndex(7);
                this.bytecode.addIconst(1);
                this.bytecode.addOpcode(Opcode.GOTO);
                this.bytecode.addIndex(4);
            }
            this.bytecode.addIconst(0);
        } else if (token == 67) {
            fatal();
        } else {
            expr.oprand1().accept(this);
            int type = typePrecedence(this.exprType);
            if (this.arrayDim > 0) {
                badType(expr);
            }
            if (token == 45) {
                if (type == 0) {
                    this.bytecode.addOpcode(Opcode.DNEG);
                } else if (type == 1) {
                    this.bytecode.addOpcode(Opcode.FNEG);
                } else if (type == 2) {
                    this.bytecode.addOpcode(Opcode.LNEG);
                } else if (type == 3) {
                    this.bytecode.addOpcode(Opcode.INEG);
                    this.exprType = TokenId.INT;
                } else {
                    badType(expr);
                }
            } else if (token == 126) {
                if (type == 3) {
                    this.bytecode.addIconst(-1);
                    this.bytecode.addOpcode(Opcode.IXOR);
                    this.exprType = TokenId.INT;
                } else if (type == 2) {
                    this.bytecode.addLconst(-1);
                    this.bytecode.addOpcode(Opcode.LXOR);
                } else {
                    badType(expr);
                }
            } else if (token != 43) {
                fatal();
            } else if (type == -1) {
                badType(expr);
            }
        }
    }

    protected static void badType(Expr expr) throws CompileError {
        throw new CompileError("invalid type for " + expr.getName());
    }

    public void atClassObject(Expr expr) throws CompileError {
        ASTree op1 = expr.oprand1();
        if (!(op1 instanceof Symbol)) {
            throw new CompileError("fatal error: badly parsed .class expr");
        }
        String cname = ((Symbol) op1).get();
        if (cname.startsWith("[")) {
            int i = cname.indexOf("[L");
            if (i >= 0) {
                String name = cname.substring(i + 2, cname.length() - 1);
                String name2 = resolveClassName(name);
                if (!name.equals(name2)) {
                    String name22 = MemberResolver.jvmToJavaName(name2);
                    StringBuffer sbuf = new StringBuffer();
                    while (true) {
                        i--;
                        if (i < 0) {
                            break;
                        }
                        sbuf.append('[');
                    }
                    sbuf.append('L').append(name22).append(';');
                    cname = sbuf.toString();
                }
            }
        } else {
            cname = MemberResolver.jvmToJavaName(resolveClassName(MemberResolver.javaToJvmName(cname)));
        }
        atClassObject2(cname);
        this.exprType = TokenId.CLASS;
        this.arrayDim = 0;
        this.className = "java/lang/Class";
    }

    /* access modifiers changed from: protected */
    public void atClassObject2(String cname) throws CompileError {
        int start = this.bytecode.currentPc();
        this.bytecode.addLdc(cname);
        this.bytecode.addInvokestatic("java.lang.Class", "forName", "(Ljava/lang/String;)Ljava/lang/Class;");
        int end = this.bytecode.currentPc();
        this.bytecode.addOpcode(Opcode.GOTO);
        int pc = this.bytecode.currentPc();
        this.bytecode.addIndex(0);
        this.bytecode.addExceptionHandler(start, end, this.bytecode.currentPc(), "java.lang.ClassNotFoundException");
        this.bytecode.growStack(1);
        this.bytecode.addInvokestatic("javassist.runtime.DotClass", "fail", "(Ljava/lang/ClassNotFoundException;)Ljava/lang/NoClassDefFoundError;");
        this.bytecode.addOpcode(Opcode.ATHROW);
        this.bytecode.write16bit(pc, (this.bytecode.currentPc() - pc) + 1);
    }

    public void atArrayRead(ASTree array, ASTree index) throws CompileError {
        arrayAccess(array, index);
        this.bytecode.addOpcode(getArrayReadOp(this.exprType, this.arrayDim));
    }

    /* access modifiers changed from: protected */
    public void arrayAccess(ASTree array, ASTree index) throws CompileError {
        array.accept(this);
        int type = this.exprType;
        int dim = this.arrayDim;
        if (dim == 0) {
            throw new CompileError("bad array access");
        }
        String cname = this.className;
        index.accept(this);
        if (typePrecedence(this.exprType) != 3 || this.arrayDim > 0) {
            throw new CompileError("bad array index");
        }
        this.exprType = type;
        this.arrayDim = dim - 1;
        this.className = cname;
    }

    protected static int getArrayReadOp(int type, int dim) {
        if (dim > 0) {
            return 50;
        }
        switch (type) {
            case TokenId.BOOLEAN /*{ENCODED_INT: 301}*/:
            case TokenId.BYTE /*{ENCODED_INT: 303}*/:
                return 51;
            case TokenId.CHAR /*{ENCODED_INT: 306}*/:
                return 52;
            case TokenId.DOUBLE /*{ENCODED_INT: 312}*/:
                return 49;
            case TokenId.FLOAT /*{ENCODED_INT: 317}*/:
                return 48;
            case TokenId.INT /*{ENCODED_INT: 324}*/:
                return 46;
            case TokenId.LONG /*{ENCODED_INT: 326}*/:
                return 47;
            case TokenId.SHORT /*{ENCODED_INT: 334}*/:
                return 53;
            default:
                return 50;
        }
    }

    protected static int getArrayWriteOp(int type, int dim) {
        if (dim > 0) {
            return 83;
        }
        switch (type) {
            case TokenId.BOOLEAN /*{ENCODED_INT: 301}*/:
            case TokenId.BYTE /*{ENCODED_INT: 303}*/:
                return 84;
            case TokenId.CHAR /*{ENCODED_INT: 306}*/:
                return 85;
            case TokenId.DOUBLE /*{ENCODED_INT: 312}*/:
                return 82;
            case TokenId.FLOAT /*{ENCODED_INT: 317}*/:
                return 81;
            case TokenId.INT /*{ENCODED_INT: 324}*/:
                return 79;
            case TokenId.LONG /*{ENCODED_INT: 326}*/:
                return 80;
            case TokenId.SHORT /*{ENCODED_INT: 334}*/:
                return 86;
            default:
                return 83;
        }
    }

    private void atPlusPlus(int token, ASTree oprand, Expr expr, boolean doDup) throws CompileError {
        boolean isPost = oprand == null;
        if (isPost) {
            oprand = expr.oprand2();
        }
        if (oprand instanceof Variable) {
            Declarator d = ((Variable) oprand).getDeclarator();
            int t = d.getType();
            this.exprType = t;
            this.arrayDim = d.getArrayDim();
            int var = getLocalVar(d);
            if (this.arrayDim > 0) {
                badType(expr);
            }
            if (t == 312) {
                this.bytecode.addDload(var);
                if (doDup && isPost) {
                    this.bytecode.addOpcode(92);
                }
                this.bytecode.addDconst(1.0d);
                this.bytecode.addOpcode(token == 362 ? 99 : 103);
                if (doDup && !isPost) {
                    this.bytecode.addOpcode(92);
                }
                this.bytecode.addDstore(var);
            } else if (t == 326) {
                this.bytecode.addLload(var);
                if (doDup && isPost) {
                    this.bytecode.addOpcode(92);
                }
                this.bytecode.addLconst(1);
                this.bytecode.addOpcode(token == 362 ? 97 : 101);
                if (doDup && !isPost) {
                    this.bytecode.addOpcode(92);
                }
                this.bytecode.addLstore(var);
            } else if (t == 317) {
                this.bytecode.addFload(var);
                if (doDup && isPost) {
                    this.bytecode.addOpcode(89);
                }
                this.bytecode.addFconst(1.0f);
                this.bytecode.addOpcode(token == 362 ? 98 : 102);
                if (doDup && !isPost) {
                    this.bytecode.addOpcode(89);
                }
                this.bytecode.addFstore(var);
            } else if (t == 303 || t == 306 || t == 334 || t == 324) {
                if (doDup && isPost) {
                    this.bytecode.addIload(var);
                }
                int delta = token == 362 ? 1 : -1;
                if (var > 255) {
                    this.bytecode.addOpcode(Opcode.WIDE);
                    this.bytecode.addOpcode(Opcode.IINC);
                    this.bytecode.addIndex(var);
                    this.bytecode.addIndex(delta);
                } else {
                    this.bytecode.addOpcode(Opcode.IINC);
                    this.bytecode.add(var);
                    this.bytecode.add(delta);
                }
                if (doDup && !isPost) {
                    this.bytecode.addIload(var);
                }
            } else {
                badType(expr);
            }
        } else {
            if (oprand instanceof Expr) {
                Expr e = (Expr) oprand;
                if (e.getOperator() == 65) {
                    atArrayPlusPlus(token, isPost, e, doDup);
                    return;
                }
            }
            atFieldPlusPlus(token, isPost, oprand, expr, doDup);
        }
    }

    public void atArrayPlusPlus(int token, boolean isPost, Expr expr, boolean doDup) throws CompileError {
        arrayAccess(expr.oprand1(), expr.oprand2());
        int t = this.exprType;
        int dim = this.arrayDim;
        if (dim > 0) {
            badType(expr);
        }
        this.bytecode.addOpcode(92);
        this.bytecode.addOpcode(getArrayReadOp(t, this.arrayDim));
        atPlusPlusCore(is2word(t, dim) ? 94 : 91, doDup, token, isPost, expr);
        this.bytecode.addOpcode(getArrayWriteOp(t, dim));
    }

    /* access modifiers changed from: protected */
    public void atPlusPlusCore(int dup_code, boolean doDup, int token, boolean isPost, Expr expr) throws CompileError {
        int t = this.exprType;
        if (doDup && isPost) {
            this.bytecode.addOpcode(dup_code);
        }
        if (t == 324 || t == 303 || t == 306 || t == 334) {
            this.bytecode.addIconst(1);
            this.bytecode.addOpcode(token == 362 ? 96 : 100);
            this.exprType = TokenId.INT;
        } else if (t == 326) {
            this.bytecode.addLconst(1);
            this.bytecode.addOpcode(token == 362 ? 97 : 101);
        } else if (t == 317) {
            this.bytecode.addFconst(1.0f);
            this.bytecode.addOpcode(token == 362 ? 98 : 102);
        } else if (t == 312) {
            this.bytecode.addDconst(1.0d);
            this.bytecode.addOpcode(token == 362 ? 99 : 103);
        } else {
            badType(expr);
        }
        if (doDup && !isPost) {
            this.bytecode.addOpcode(dup_code);
        }
    }

    @Override // javassist.compiler.ast.Visitor
    public void atVariable(Variable v) throws CompileError {
        Declarator d = v.getDeclarator();
        this.exprType = d.getType();
        this.arrayDim = d.getArrayDim();
        this.className = d.getClassName();
        int var = getLocalVar(d);
        if (this.arrayDim > 0) {
            this.bytecode.addAload(var);
            return;
        }
        switch (this.exprType) {
            case TokenId.CLASS /*{ENCODED_INT: 307}*/:
                this.bytecode.addAload(var);
                return;
            case TokenId.DOUBLE /*{ENCODED_INT: 312}*/:
                this.bytecode.addDload(var);
                return;
            case TokenId.FLOAT /*{ENCODED_INT: 317}*/:
                this.bytecode.addFload(var);
                return;
            case TokenId.LONG /*{ENCODED_INT: 326}*/:
                this.bytecode.addLload(var);
                return;
            default:
                this.bytecode.addIload(var);
                return;
        }
    }

    @Override // javassist.compiler.ast.Visitor
    public void atKeyword(Keyword k) throws CompileError {
        this.arrayDim = 0;
        int token = k.get();
        switch (token) {
            case TokenId.SUPER /*{ENCODED_INT: 336}*/:
            case TokenId.THIS /*{ENCODED_INT: 339}*/:
                if (this.inStaticMethod) {
                    throw new CompileError("not-available: " + (token == 339 ? "this" : "super"));
                }
                this.bytecode.addAload(0);
                this.exprType = TokenId.CLASS;
                if (token == 339) {
                    this.className = getThisName();
                    return;
                } else {
                    this.className = getSuperName();
                    return;
                }
            case TokenId.TRUE /*{ENCODED_INT: 410}*/:
                this.bytecode.addIconst(1);
                this.exprType = TokenId.BOOLEAN;
                return;
            case TokenId.FALSE /*{ENCODED_INT: 411}*/:
                this.bytecode.addIconst(0);
                this.exprType = TokenId.BOOLEAN;
                return;
            case TokenId.NULL /*{ENCODED_INT: 412}*/:
                this.bytecode.addOpcode(1);
                this.exprType = TokenId.NULL;
                return;
            default:
                fatal();
                return;
        }
    }

    @Override // javassist.compiler.ast.Visitor
    public void atStringL(StringL s) throws CompileError {
        this.exprType = TokenId.CLASS;
        this.arrayDim = 0;
        this.className = jvmJavaLangString;
        this.bytecode.addLdc(s.get());
    }

    @Override // javassist.compiler.ast.Visitor
    public void atIntConst(IntConst i) throws CompileError {
        this.arrayDim = 0;
        long value = i.get();
        int type = i.getType();
        if (type == 402 || type == 401) {
            this.exprType = type == 402 ? TokenId.INT : TokenId.CHAR;
            this.bytecode.addIconst((int) value);
            return;
        }
        this.exprType = TokenId.LONG;
        this.bytecode.addLconst(value);
    }

    @Override // javassist.compiler.ast.Visitor
    public void atDoubleConst(DoubleConst d) throws CompileError {
        this.arrayDim = 0;
        if (d.getType() == 405) {
            this.exprType = TokenId.DOUBLE;
            this.bytecode.addDconst(d.get());
            return;
        }
        this.exprType = TokenId.FLOAT;
        this.bytecode.addFconst((float) d.get());
    }
}
