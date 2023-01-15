import java.util.List;

/**
 * BuiltInFunctionSquareRoot Object.
 *
 * BuiltInFunctionSquareRoot
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class BuiltInFunctionSquareRoot extends BuiltInFunctions {

    public BuiltInFunctionSquareRoot() {
        this.name = "squareRoot";
        this.variadic = false;
    }

    @Override
    public void execute(List<InterpreterDataType> dataTypes) throws InvalidFunctionCallException {

        //Make sure both variables are floats and only 2 variables
        if(dataTypes.size() != 2)
            throw new InvalidFunctionCallException();
        if(dataTypes.get(0).getClass() != FloatDataType.class)
            throw new InvalidFunctionCallException();
        if(dataTypes.get(1).getClass() != FloatDataType.class)
            throw new InvalidFunctionCallException();

        //Set the second data type
        dataTypes.get(0).fromString(Math.sqrt(Float.parseFloat(dataTypes.get(0).toString())) + "");
    }
}
