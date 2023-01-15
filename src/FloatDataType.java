/**
 * FloatDataType Object.
 *
 * FloatDataType
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class FloatDataType extends InterpreterDataType {
    private float number;

    public FloatDataType() {
    }

    public FloatDataType(float number) {
        this.number = number;
    }

    public float getNumber() {
        return this.number;
    }

    @Override
    public String toString() {
        return "" + this.number;
    }

    @Override
    public void fromString(String input) {
        this.number = Float.parseFloat(input);
    }
}
