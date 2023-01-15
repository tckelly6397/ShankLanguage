/**
 * AssignmentNode Object.
 *
 * AssignmentNode Object. This object is just an ast that
 * contains a variable reference and an expression.
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class AssignmentNode extends StatementNode {
    private VariableReferenceNode target;
    private Node expression;

    public AssignmentNode(VariableReferenceNode target, Node expression) {
        super();
        this.target = target;
        this.expression = expression;
    }

    public VariableReferenceNode getTarget() {
        return target;
    }

    public Node getExpression() {
        return expression;
    }

    public String toString() {
        return "Assignment(Target: " + this.target + ", Expression: " + this.expression + ")";
    }
}
