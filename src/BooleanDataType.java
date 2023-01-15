public class BooleanDataType extends InterpreterDataType {
    private boolean value;

    public BooleanDataType() {

    }

    public BooleanDataType(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "" + this.value;
    }

    @Override
    public void fromString(String input) {
        if(input.equals("true")) {
            this.value = true;
        } else if(input .equals("false")) {
            this.value = false;
        }
    }
}
