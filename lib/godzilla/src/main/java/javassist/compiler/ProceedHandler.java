package javassist.compiler;

import javassist.bytecode.Bytecode;
import javassist.compiler.ast.ASTList;

public interface ProceedHandler {
    void doit(JvstCodeGen jvstCodeGen, Bytecode bytecode, ASTList aSTList) throws CompileError;

    void setReturnType(JvstTypeChecker jvstTypeChecker, ASTList aSTList) throws CompileError;
}
