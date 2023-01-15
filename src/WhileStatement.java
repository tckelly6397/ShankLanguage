import java.util.List;

/**
 * WhileStatement Object.
 *
 * WhileStatement Object. This object stores
 * data pertaining to a while statement
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class WhileStatement extends StatementNode {
    private Node condition;
    private List<StatementNode> statements;

    public WhileStatement(Node condition, List<StatementNode> statements) {
        this.condition = condition;
        this.statements = statements;
    }

    public Node getCondition() {
        return condition;
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

        return "WhileStatement(condition: " + condition + ")\n\t\tstatements:" + statementsText;
    }
}
