/**
 * IntDataType Object.
 *
 * IntDataType
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class IntDataType extends InterpreterDataType {
    private int number;

    public IntDataType() {
    }

    public IntDataType(int number) {
        this.number = number;
    }

    public int getNumber() {
        return this.number;
    }

    @Override
    public String toString() {
        return "" + this.number;
    }

    @Override
    public void fromString(String input) {
        float floatNumber = Float.parseFloat(input);
        int intNumber = (int)floatNumber;

        this.number = intNumber;
    }
}
