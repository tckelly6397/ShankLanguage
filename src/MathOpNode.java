/**
 * MathOpNode Object.
 *
 * MathOpNode Object extends Node and contains values corresponding to a
 * Math expression, two terms and a operator
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class MathOpNode extends Node {
    private OPType type;
    private Node leftOperand;
    private Node rightOperand;

    public enum OPType {
        PLUS,
        MINUS,
        TIMES,
        DIVIDE,
        MOD
    }

    public MathOpNode(OPType type, Node leftOperand, Node rightOperand) {
        this.type = type;
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public OPType getType() {
        return this.type;
    }

    public Node getLeftOperand() {
        return this.leftOperand;
    }

    public Node getRightOperand() {
        return this.rightOperand;
    }

    @Override
    public String toString() {
        return "MathOpNode(" + this.type + "," + this.leftOperand + "," + this.rightOperand + ")";
    }
}
