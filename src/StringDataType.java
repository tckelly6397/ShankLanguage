public class StringDataType extends InterpreterDataType {
    private String value;

    public StringDataType() {
    }

    public StringDataType(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public void fromString(String input) {
        this.value = input;
    }
}
