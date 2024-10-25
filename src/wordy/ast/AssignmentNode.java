package wordy.ast;

import java.io.PrintWriter;
import java.util.Map;
import java.util.Objects;

import wordy.interpreter.EvaluationContext;

import static wordy.ast.Utils.orderedMap;

/**
 * An assignment statement (“Set <variable> to <expression>”) in a Wordy abstract syntax tree.
 */
public class AssignmentNode extends StatementNode {
    /**
     * The left-hand side (LHS) of the assignment, the variable whose value will be updated.
     */
    private final VariableNode variable;

    /**
     * The right-hand side (RHS) of the assignment, the expression whose value will be assigned to
     * the LHS variable.
     */
    private final ExpressionNode expression;

    public AssignmentNode(VariableNode variable, ExpressionNode expression) {
        this.variable = variable;
        this.expression = expression;
    }

    @Override
    public void compile(PrintWriter out) {
        variable.compile(out);
        out.println("=");
        expression.compile(out);
        out.println(";");
    }

    @Override
    protected void doRun(EvaluationContext context) {
        var rightValue = expression.evaluate(context);
        var variableName = variable.getName();
        context.set(variableName, rightValue);
    }

    @Override
    public Map<String, ASTNode> getChildren() {
        return orderedMap(
            "lhs", variable,
            "rhs", expression);
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        if(o == null || getClass() != o.getClass())
            return false;
        AssignmentNode that = (AssignmentNode) o;
        return variable.equals(that.variable)
            && expression.equals(that.expression);
    }

    @Override
    public int hashCode() {
        return Objects.hash(variable, expression);
    }

    @Override
    public String toString() {
        return "AssignmentStatement{"
            + "variable='" + variable + '\''
            + ", expression=" + expression
            + '}';
    }
}
