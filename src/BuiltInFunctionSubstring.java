import java.util.List;

/**
 * BuiltInFunctionSubstring Object.
 *
 * BuiltInFunctionSubstring
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class BuiltInFunctionSubstring extends BuiltInFunctions {

    public BuiltInFunctionSubstring() {
        this.name = "substring";
        this.variadic = false;
    }

    @Override
    public void execute(List<InterpreterDataType> dataTypes) throws InvalidFunctionCallException {

        //Make sure the size is correct
        if(dataTypes.size() != 3)
            throw new InvalidFunctionCallException();

        //Make sure first parameter is an int
        if(dataTypes.get(0).getClass() != IntDataType.class)
            throw new InvalidFunctionCallException();

        //Make sure second parameter is an int
        if(dataTypes.get(1).getClass() != IntDataType.class)
            throw new InvalidFunctionCallException();

        //Make sure third parameter is a string
        if(dataTypes.get(2).getClass() != StringDataType.class)
            throw new InvalidFunctionCallException();

        //Set the second data type
        int startIndex = ((IntDataType) dataTypes.get(0)).getNumber();
        int length = ((IntDataType) dataTypes.get(1)).getNumber();
        String firstString = ((StringDataType) dataTypes.get(2)).getValue();
        String endString = firstString.substring(startIndex, length);
        dataTypes.get(2).fromString(endString);
    }
}
