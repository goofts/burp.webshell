package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class MethodDecl extends ASTList {
    public static final String initName = "<init>";
    private static final long serialVersionUID = 1;

    public MethodDecl(ASTree _head, ASTList _tail) {
        super(_head, _tail);
    }

    public boolean isConstructor() {
        Symbol sym = getReturn().getVariable();
        return sym != null && "<init>".equals(sym.get());
    }

    public ASTList getModifiers() {
        return (ASTList) getLeft();
    }

    public Declarator getReturn() {
        return (Declarator) tail().head();
    }

    public ASTList getParams() {
        return (ASTList) sublist(2).head();
    }

    public ASTList getThrows() {
        return (ASTList) sublist(3).head();
    }

    public Stmnt getBody() {
        return (Stmnt) sublist(4).head();
    }

    @Override // javassist.compiler.ast.ASTList, javassist.compiler.ast.ASTree
    public void accept(Visitor v) throws CompileError {
        v.atMethodDecl(this);
    }
}
