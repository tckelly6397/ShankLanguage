import java.util.List;

/**
 * ForStatement Object.
 *
 * ForStatement Object. This object stores
 * data pertaining to a for statement
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class ForStatement extends StatementNode {
    private VariableReferenceNode variable;
    private Node beginNode;
    private Node endNode;
    private List<StatementNode> statements;

    public ForStatement(VariableReferenceNode variable, Node beginNode, Node endNode, List<StatementNode> statements) {
        this.variable = variable;
        this.beginNode = beginNode;
        this.endNode = endNode;
        this.statements = statements;
    }

    public VariableReferenceNode getVariable() {
        return variable;
    }

    public Node getBeginNode() {
        return beginNode;
    }

    public Node getEndNode() {
        return endNode;
    }

    public List<StatementNode> getStatements() {
        return statements;
    }

    @Override
    public String toString() {
        String statementsText = "";
        for(StatementNode statement : this.statements) {
            statementsText += "\n\t\t" + statement;
        }

        return "ForStatement(" +
                "variable: " + variable +
                ", beginNode: " + beginNode +
                ", endNode: " + endNode +
                ", \n\t\tstatements: " + statementsText +
                ')';
    }
}
