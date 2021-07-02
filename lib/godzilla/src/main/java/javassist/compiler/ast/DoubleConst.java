package javassist.compiler.ast;

import javassist.bytecode.Opcode;
import javassist.compiler.CompileError;
import javassist.compiler.TokenId;

public class DoubleConst extends ASTree {
    private static final long serialVersionUID = 1;
    protected int type;
    protected double value;

    public DoubleConst(double v, int tokenId) {
        this.value = v;
        this.type = tokenId;
    }

    public double get() {
        return this.value;
    }

    public void set(double v) {
        this.value = v;
    }

    public int getType() {
        return this.type;
    }

    @Override // javassist.compiler.ast.ASTree
    public String toString() {
        return Double.toString(this.value);
    }

    @Override // javassist.compiler.ast.ASTree
    public void accept(Visitor v) throws CompileError {
        v.atDoubleConst(this);
    }

    public ASTree compute(int op, ASTree right) {
        if (right instanceof IntConst) {
            return compute0(op, (IntConst) right);
        }
        if (right instanceof DoubleConst) {
            return compute0(op, (DoubleConst) right);
        }
        return null;
    }

    private DoubleConst compute0(int op, DoubleConst right) {
        int newType;
        if (this.type == 405 || right.type == 405) {
            newType = TokenId.DoubleConstant;
        } else {
            newType = TokenId.FloatConstant;
        }
        return compute(op, this.value, right.value, newType);
    }

    private DoubleConst compute0(int op, IntConst right) {
        return compute(op, this.value, (double) right.value, this.type);
    }

    private static DoubleConst compute(int op, double value1, double value2, int newType) {
        double newValue;
        switch (op) {
            case Opcode.FLOAD_3:
                newValue = value1 % value2;
                break;
            case Opcode.DLOAD_0:
            case Opcode.DLOAD_1:
            case Opcode.DLOAD_2:
            case Opcode.DLOAD_3:
            case Opcode.ALOAD_2:
            case 46:
            default:
                return null;
            case Opcode.ALOAD_0:
                newValue = value1 * value2;
                break;
            case Opcode.ALOAD_1:
                newValue = value1 + value2;
                break;
            case 45:
                newValue = value1 - value2;
                break;
            case 47:
                newValue = value1 / value2;
                break;
        }
        return new DoubleConst(newValue, newType);
    }
}
