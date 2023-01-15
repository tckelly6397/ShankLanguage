import java.util.List;

/**
 * BuiltInFunctionGetRandom Object.
 *
 * BuiltInFunctionGetRandom
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class BuiltInFunctionGetRandom extends BuiltInFunctions {

    public BuiltInFunctionGetRandom() {
        this.name = "getRandom";
        this.variadic = false;
    }

    //Generates a random value between 0 and 99 and sets it to the first parameter
    @Override
    public void execute(List<InterpreterDataType> dataTypes) throws InvalidFunctionCallException {

        //Make sure the size is correct
        if(dataTypes.size() != 1)
            throw new InvalidFunctionCallException();

        //Make sure first float is an int
        if(!(dataTypes.get(0) instanceof IntDataType))
            throw new InvalidFunctionCallException();

        dataTypes.get(0).fromString((int)(Math.random() * 100) + "");
    }
}
