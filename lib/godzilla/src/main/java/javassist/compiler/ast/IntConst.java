package javassist.compiler.ast;

import javassist.bytecode.Opcode;
import javassist.compiler.CompileError;
import javassist.compiler.TokenId;

public class IntConst extends ASTree {
    private static final long serialVersionUID = 1;
    protected int type;
    protected long value;

    public IntConst(long v, int tokenId) {
        this.value = v;
        this.type = tokenId;
    }

    public long get() {
        return this.value;
    }

    public void set(long v) {
        this.value = v;
    }

    public int getType() {
        return this.type;
    }

    @Override // javassist.compiler.ast.ASTree
    public String toString() {
        return Long.toString(this.value);
    }

    @Override // javassist.compiler.ast.ASTree
    public void accept(Visitor v) throws CompileError {
        v.atIntConst(this);
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

    private IntConst compute0(int op, IntConst right) {
        int newType;
        long newValue;
        int type1 = this.type;
        int type2 = right.type;
        if (type1 == 403 || type2 == 403) {
            newType = TokenId.LongConstant;
        } else if (type1 == 401 && type2 == 401) {
            newType = TokenId.CharConstant;
        } else {
            newType = TokenId.IntConstant;
        }
        long value1 = this.value;
        long value2 = right.value;
        switch (op) {
            case Opcode.FLOAD_3:
                newValue = value1 % value2;
                break;
            case Opcode.DLOAD_0:
                newValue = value1 & value2;
                break;
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
            case Opcode.DUP2_X2:
                newValue = value1 ^ value2;
                break;
            case Opcode.IUSHR:
                newValue = value1 | value2;
                break;
            case TokenId.LSHIFT:
                newValue = this.value << ((int) value2);
                newType = type1;
                break;
            case TokenId.RSHIFT:
                newValue = this.value >> ((int) value2);
                newType = type1;
                break;
            case TokenId.ARSHIFT:
                newValue = this.value >>> ((int) value2);
                newType = type1;
                break;
            default:
                return null;
        }
        return new IntConst(newValue, newType);
    }

    private DoubleConst compute0(int op, DoubleConst right) {
        double newValue;
        double value1 = (double) this.value;
        double value2 = right.value;
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
        return new DoubleConst(newValue, right.type);
    }
}
