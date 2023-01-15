import javax.swing.plaf.nimbus.State;
import java.util.List;

/**
 * FunctionNode Class.
 *
 * FunctionNode class holds data based on a function definition.
 * Used to hold data for a function.
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class FunctionNode extends CallableNode {
    private List<VariableNode> localVariables;
    private List<StatementNode> statements;

    public FunctionNode(String name, List<ParameterNode> parameterVariables, List<VariableNode> localVariables, List<StatementNode> statements) {
        this.name = name;
        this.parameterVariables = parameterVariables;
        this.localVariables = localVariables;
        this.statements = statements;
    }

    public String getName() {
        return this.name;
    }

    public List<ParameterNode> getParameterVariables() {
        return this.parameterVariables;
    }

    public List<VariableNode> getLocalVariables() {
        return this.localVariables;
    }

    public List<StatementNode> getStatements() {
        return this.statements;
    }

    public String toString() {
        String parametersText = "";
        String variablesText = "";
        String statementsText = "";
        for(ParameterNode variable : this.parameterVariables) {
            parametersText += "\n\t\t" + variable;
        }
        for(VariableNode variable : this.localVariables) {
            variablesText += "\n\t\t" + variable;
        }
        for(StatementNode statement : this.statements) {
            statementsText += "\n\t\t" + statement;
        }

        return "FUNCTION: " + this.name + "\n\tparameters: " + parametersText
                + "\n\tvariables: " + variablesText
                + "\n\tstatements: " + statementsText;
    }
}
