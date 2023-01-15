import java.util.List;

/**
 * BuiltInFunctionRealToInteger Object.
 *
 * BuiltInFunctionRealToInteger
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class BuiltInFunctionRealToInteger extends BuiltInFunctions {

    public BuiltInFunctionRealToInteger() {
        this.name = "realToInteger";
        this.variadic = false;
    }

    @Override
    public void execute(List<InterpreterDataType> dataTypes) throws InvalidFunctionCallException {

        //Make sure the size is correct
        if(dataTypes.size() != 2)
            throw new InvalidFunctionCallException();

        //Make sure first float is an int
        if(dataTypes.get(0).getClass() != FloatDataType.class)
            throw new InvalidFunctionCallException();

        //Make sure second parameter is a float
        if(dataTypes.get(1).getClass() != IntDataType.class)
            throw new InvalidFunctionCallException();

        //Set the second data type
        dataTypes.get(1).fromString((int)(Float.parseFloat(dataTypes.get(0).toString())) + "");
    }
}
