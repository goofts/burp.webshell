package javassist.bytecode;

import javassist.CannotCompileException;

public class DuplicateMemberException extends CannotCompileException {
    private static final long serialVersionUID = 1;

    public DuplicateMemberException(String msg) {
        super(msg);
    }
}
