import java.util.List;

/**
 * Abstract CallableNode Object.
 *
 * CallableNode
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public abstract class CallableNode extends Node {
    protected String name;
    protected List<ParameterNode> parameterVariables;

    protected String getName() {
        return this.name;
    }

    protected List<ParameterNode> getParameterVariables() {
        return this.parameterVariables;
    }
}
