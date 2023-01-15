public class BooleanNode extends Node {
    private boolean value;

    public BooleanNode(boolean value) {
        this.value = value;
    }

    public boolean getValue() {
        return this.value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Boolean(" + this.value + ")";
    }
}
