/**
 * FloatNode Object.
 *
 * FloatNode Object holds values pertaining to a float
 * encapsulated inside a Node.
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class FloatNode extends Node {
    private float number;

    public FloatNode(float number) {
        this.number = number;
    }

    public float getNumber() {
        return this.number;
    }

    public void setNumber(float number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Float(" + this.number + ")";
    }
}
