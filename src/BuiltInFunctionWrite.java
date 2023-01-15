import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * BuiltInFunctionWrite Object.
 *
 * BuiltInFunctionWrite
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class BuiltInFunctionWrite extends BuiltInFunctions {

    public BuiltInFunctionWrite() {
        this.name = "write";
        this.variadic = true;
    }

    @Override
    public void execute(List<InterpreterDataType> dataTypes) {
        String text = "";

        //Get all the values and separate them by spaces
        for(int i = 0; i < dataTypes.size(); i++) {
            text += dataTypes.get(i);

            if(i != dataTypes.size() - 1)
                text += " ";
        }

        //Display the text
        System.out.println(text);
    }
}
