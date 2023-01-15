public class ParameterNode extends Node {
    private Node value;
    private boolean isVar;

    public ParameterNode(Node value, boolean isVar) {
        this.value = value;
        this.isVar = isVar;
    }

    public Node getValue() {
        return value;
    }

    public boolean isVar() {
        return isVar;
    }

    @Override
    public String toString() {
        return "ParameterNode(isVar: " + this.isVar + ", value: " + this.value + ")";
    }
}
