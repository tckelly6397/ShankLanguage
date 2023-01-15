import java.util.List;

public class BuiltInFunctionWait extends BuiltInFunctions {

    public BuiltInFunctionWait() {
        this.name = "wait";
        this.variadic = false;
    }

    @Override
    public void execute(List<InterpreterDataType> dataTypes) throws InvalidFunctionCallException {
        //Make sure the size is correct
        if(dataTypes.size() != 1)
            throw new InvalidFunctionCallException();

        //Make sure first float is an int
        if(dataTypes.get(0).getClass() != IntDataType.class)
            throw new InvalidFunctionCallException();

        try {
            Thread.sleep(((IntDataType)dataTypes.get(0)).getNumber());
        } catch (Exception e) {
            System.out.println("Error");
        }
    }
}
