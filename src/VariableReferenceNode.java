/**
 * VariableReferenceNode Object.
 *
 * VariableReferenceNode Object holds the name of a variable,
 * stores it as an ast Node.
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class VariableReferenceNode extends Node {
    private String name;

    public VariableReferenceNode(String name) {
        super();
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return "VariableReference(" + this.name + ")";
    }
}
