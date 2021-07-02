package javassist.bytecode;

/* access modifiers changed from: package-private */
/* compiled from: ExceptionTable */
public class ExceptionTableEntry {
    int catchType;
    int endPc;
    int handlerPc;
    int startPc;

    ExceptionTableEntry(int start, int end, int handle, int type) {
        this.startPc = start;
        this.endPc = end;
        this.handlerPc = handle;
        this.catchType = type;
    }
}
