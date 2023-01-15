/**
 * BooleanExpression Object.
 *
 * BooleanExpression Object. This object stores
 * data pertaining to a boolean expression
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class BooleanExpression extends StatementNode {
    private Node leftExpression;
    private Node rightExpression;
    private Token.Type condition;
    private BooleanType type;

    public enum BooleanType {
        NUMBER,
        BOOLEAN,
        STRING
    }

    public static BooleanExpression.BooleanType getBooleanExpressionType(Node term) {
        while(term instanceof MathOpNode) {
            term = ((MathOpNode) term).getLeftOperand();
        }

        if(term instanceof FloatNode || term instanceof IntegerNode) {
            return BooleanType.NUMBER;
        } else if(term instanceof BooleanNode) {
            return BooleanType.BOOLEAN;
        } else if(term instanceof CharacterNode || term instanceof StringNode) {
            return BooleanType.STRING;
        } else {
            return null;
        }
    }

    public BooleanExpression(Node leftExpression, Node rightExpression, Token.Type condition, BooleanType type) {
        this.leftExpression = leftExpression;
        this.rightExpression = rightExpression;
        this.condition = condition;
        this.type = type;
    }

    public Node getLeftExpression() {
        return leftExpression;
    }

    public Node getRightExpression() {
        return rightExpression;
    }

    public Token.Type getCondition() {
        return condition;
    }
    public BooleanType getType() {
        return type;
    }

    public void setType(BooleanType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BooleanExpression(leftExpression: " + leftExpression + ", rightExpression: " + rightExpression
                + ", condition: " + condition + ")";
    }
}
