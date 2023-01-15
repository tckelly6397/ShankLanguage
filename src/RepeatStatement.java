import java.util.List;

/**
 * RepeatStatement Object.
 *
 * RepeatStatement Object. This object stores
 * data pertaining to a repeat statement
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class RepeatStatement extends StatementNode {
    private Node condition;
    private List<StatementNode> statements;

    public RepeatStatement(Node condition, List<StatementNode> statements) {
        this.condition = condition;
        this.statements = statements;
    }

    public Node getCondition() {
        return condition;
    }

    public List<StatementNode> getStatements() {
        return statements;
    }

    public String toString() {
        String statementsText = "";
        for(StatementNode statement : this.statements) {
            statementsText += "\n\t\t" + statement;
        }

        return "RepeatStatement(condition: " + condition + ")\n\t\tstatements:" + statementsText;
    }
}
