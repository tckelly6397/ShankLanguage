import java.util.List;
import java.util.Scanner;

/**
 * BuiltInFunctionFunctionRead Object.
 *
 * BuiltInFunctionFunctionRead
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class BuiltInFunctionRead extends BuiltInFunctions {

    public BuiltInFunctionRead() {
        this.name = "read";
        this.variadic = true;
    }

    @Override
    public void execute(List<InterpreterDataType> dataTypes) {

        //Loop through all the parameters and get user input for each
        for(int i = 0; i < dataTypes.size(); i++) {
            InterpreterDataType dataType = dataTypes.get(i);
            //Catch scanner exception
            try (Scanner input = new Scanner(System.in)) {
                dataType.fromString(input.next());
            }
        }
    }
}
