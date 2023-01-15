/**
 * VariableNode Class.
 *
 * FunctionNode class holds data based on a function definition.
 * Used to hold data for a function.
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class VariableNode extends Node {
    private String name;
    private boolean isConstant;
    private DataType dataType;
    private Node value;

    public enum DataType {
        INTEGER,
        REAL,
        STRING,
        CHARACTER,
        BOOLEAN
    }

    public VariableNode(String name, DataType dataType, Node value, boolean isConstant) {
        this.name = name;
        this.dataType = dataType;
        this.value = value;
        this.isConstant = isConstant;
    }

    public VariableNode(String name, DataType dataType) {
        this.name = name;
        this.dataType = dataType;
        this.value = null;
        this.isConstant = false;
    }

    public String getName() {
        return this.name;
    }

    public boolean isConstant() {
        return this.isConstant;
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public Node getValue() {
        return this.value;
    }

    public void setValue(Node value) {
        this.value = value;
    }

    public String toString() {
        return "Variable(name: " + this.name + ", isConstant: " + this.isConstant + ", DataType: " +
                this.dataType + ", value: " + this.value + ")";
    }
}
