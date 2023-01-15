import java.util.List;

/**
 * BuiltInFunctionLeft Object.
 *
 * BuiltInFunctionLeft
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class BuiltInFunctionLeft extends BuiltInFunctions {

    public BuiltInFunctionLeft() {
        this.name = "left";
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

        //Make sure second parameter is a string
        if(dataTypes.get(1).getClass() != StringDataType.class)
            throw new InvalidFunctionCallException();

        //Set the second data type
        int length = ((IntDataType) dataTypes.get(0)).getNumber();
        String firstString = ((StringDataType) dataTypes.get(1)).getValue();
        String endString = firstString.substring(0, length);
        dataTypes.get(1).fromString(endString);
    }
}
