package javassist.compiler.ast;

import javassist.compiler.CompileError;

public class ArrayInit extends ASTList {
    private static final long serialVersionUID = 1;

    public ArrayInit(ASTree firstElement) {
        super(firstElement);
    }

    public int size() {
        int s = length();
        if (s == 1 && head() == null) {
            return 0;
        }
        return s;
    }

    @Override // javassist.compiler.ast.ASTList, javassist.compiler.ast.ASTree
    public void accept(Visitor v) throws CompileError {
        v.atArrayInit(this);
    }

    @Override // javassist.compiler.ast.ASTree
    public String getTag() {
        return "array";
    }
}
