/**
 * IntegerNode Object.
 *
 * IntegerNode Object holds values pertaining to a Integer
 * encapsulated inside a Node.
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class IntegerNode extends Node {
    private int number;

    public IntegerNode(int number) {
        this.number = number;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Integer(" + this.number + ")";
    }
}
