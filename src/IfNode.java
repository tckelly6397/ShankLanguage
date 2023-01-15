import java.util.List;

/**
 * IfStatement Object.
 *
 * IfStatement Object. This object stores
 * data pertaining to a if statement
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class IfNode extends StatementNode {
    private Node condition;
    private IfNode nextIf;
    private List<StatementNode> statements;

    public IfNode(Node condition, IfNode nextIf, List<StatementNode> statements) {
        this.condition = condition;
        this.nextIf = nextIf;
        this.statements = statements;
    }

    public Node getCondition() {
        return condition;
    }

    public IfNode getNextIf() {
        return nextIf;
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

        return "IfStatement(condition: " + condition + ")\n\t\tstatements:" + statementsText + "\n\t\tnextIf: " + nextIf;
    }
}
