package wordy.ast;

import java.util.Map;
import java.util.Objects;
import java.io.PrintWriter;
import java.lang.Math;

import wordy.interpreter.EvaluationContext;

import static wordy.ast.Utils.orderedMap;

/**
 * Two expressions joined by an operator (e.g. “x plus y”) in a Wordy abstract syntax tree.
 */
public class BinaryExpressionNode extends ExpressionNode {
    public enum Operator {
        ADDITION, SUBTRACTION, MULTIPLICATION, DIVISION, EXPONENTIATION
    }

    private final Operator operator;
    private final ExpressionNode lhs, rhs;

    public BinaryExpressionNode(Operator operator, ExpressionNode lhs, ExpressionNode rhs) {
        this.operator = operator;
        this.lhs = lhs;
        this.rhs = rhs;
    }

    @Override
    public void compile(PrintWriter out) {
        if (operator == Operator.ADDITION) {
            out.print("context." + lhs + " + context." + rhs);
        } else if (operator == Operator.SUBTRACTION) {
            out.print("context." + lhs + " - context." + rhs);
        } else if (operator == Operator.MULTIPLICATION) {
            out.print("context." + lhs + " * context." + rhs);
        } else if (operator == Operator.DIVISION) {
            out.print("context." + lhs + " / context." + rhs);
        } else if (operator == Operator.EXPONENTIATION) {
            out.print("Math.pow(context." + lhs + ", " + rhs + ")");
        }
    }

    @Override
    protected double doEvaluate(EvaluationContext context) {
        var leftValue = lhs.evaluate(context);
        var rightValue = rhs.evaluate(context);
        if (operator == Operator.ADDITION) {
            return rightValue + leftValue;
        } else if (operator == Operator.SUBTRACTION) {
            return leftValue - rightValue;
        } else if (operator == Operator.MULTIPLICATION) {
            return rightValue * leftValue;
        } else if (operator == Operator.DIVISION) {
            return leftValue / rightValue;
        } else if (operator == Operator.EXPONENTIATION) {
            return Math.pow(leftValue, rightValue);
        }
        throw new UnsupportedOperationException("Unknown Operator Expression. See BinaryExpressionNode.java.");
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        return orderedMap(
            "lhs", lhs,
            "rhs", rhs);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        BinaryExpressionNode that = (BinaryExpressionNode) o;
        return this.operator == that.operator
            && this.lhs.equals(that.lhs)
            && this.rhs.equals(that.rhs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(operator, lhs, rhs);
    }

    @Override
    public String toString() {
        return "BinaryExpressionNode{"
            + "operator=" + operator
            + ", lhs=" + lhs
            + ", rhs=" + rhs
            + '}';
    }

    @Override
    protected String describeAttributes() {
        return "(operator=" + operator + ')';
    }
}
