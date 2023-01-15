import java.util.List;

public class FunctionCallStatement extends StatementNode {
    private String name;
    private List<ParameterNode> parameters;

    public FunctionCallStatement(String name, List<ParameterNode> parameters) {
        this.name = name;
        this.parameters = parameters;
    }

    public String getName() {
        return name;
    }

    public List<ParameterNode> getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        String parameterText = "";
        for(Node parameter : this.parameters) {
            parameterText += "\n\t\t" + parameter;
        }

        return "FunctionCallStatement(Name: " + this.name + ")\n\t\tparameters:" + parameterText;
    }
}
