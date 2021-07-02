package javassist.bytecode.annotation;

public class NoSuchClassError extends Error {
    private static final long serialVersionUID = 1;
    private String className;

    public NoSuchClassError(String className2, Error cause) {
        super(cause.toString(), cause);
        this.className = className2;
    }

    public String getClassName() {
        return this.className;
    }
}
