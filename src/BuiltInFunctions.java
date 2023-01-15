import java.util.List;

/**
 * Abstract BuiltInFunctions Object.
 *
 * BuiltInFunctions
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public abstract class BuiltInFunctions extends CallableNode {
    protected boolean variadic;

    public abstract void execute(List<InterpreterDataType> dataTypes) throws InvalidFunctionCallException;

    public boolean isVariadic() {
        return this.variadic;
    }

    public String toString() {
        String parametersText = "";
        for(ParameterNode variable : this.parameterVariables) {
            parametersText += "\n\t\t" + variable;
        }

        return "BUILTINFUNCTION: " + this.name + ", isVariadic: " + this.variadic + "\n\tparameters: " + parametersText;
    }
}
