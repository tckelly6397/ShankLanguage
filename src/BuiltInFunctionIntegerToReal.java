import java.util.List;

/**
 * BuiltInFunctionIntegerToReal Object.
 *
 * BuiltInFunctionIntegerToReal
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class BuiltInFunctionIntegerToReal extends BuiltInFunctions {

    public BuiltInFunctionIntegerToReal() {
        this.name = "integerToReal";
        this.variadic = false;
    }

    @Override
    public void execute(List<InterpreterDataType> dataTypes) throws InvalidFunctionCallException {

        //Make sure the size is correct
        if(dataTypes.size() != 2)
            throw new InvalidFunctionCallException();

        //Make sure first float is an int
        if(dataTypes.get(0).getClass() != IntDataType.class)
            throw new InvalidFunctionCallException();

        //Make sure second parameter is a float
        if(dataTypes.get(1).getClass() != FloatDataType.class)
            throw new InvalidFunctionCallException();

        //Set the second data type
        dataTypes.get(1).fromString(dataTypes.get(0).toString());
    }
}
