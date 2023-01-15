import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Shank Class.
 *
 * Shank Class is the beginning class in which the Java
 * project depends on. It takes in a file path to a text
 * document in which it passes each line into a Lexer so
 * that it can get a List of corresponding Tokens making
 * it easier for the Computer to understand.
 *
 * Note: In the assignment document it states to call this
 * file Shank.java in which I did while on the rubric it
 * states to be called Basic.java
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class Shank {
    public static Color color = new Color(0, 0, 0);
    public static ArrayList<Ellipse2D> circles = new ArrayList<>();
    public static ArrayList<Rectangle2D> rectangles = new ArrayList<>();

    //Accepts one argument in which it should be the file path to a text document
    public static void main(String args[]) throws Exception {
        //Invalid amount of arguments
        if(args.length != 1) {
            throw new Exception("Expected one argument, got " + args.length);
        }

        //Variables
        Lexer lexer = new Lexer();
        List<String> lines = new ArrayList<>();
        List<Token> tokens = new ArrayList<>();

        //Initialize lines to a list of the lines within the passed in file path
        try {
            lines = Files.readAllLines(Paths.get(args[0]));
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Lexing
        try {
            for(String line : lines) {
                tokens.addAll(lexer.lex(line));
            }
        } catch (InvalidSyntaxException e) {
            e.printStackTrace();
        }

        //Print out each token
        for(Token token : tokens) {
            System.out.print(token + " ");

            //New line in console if EndOfLine token is found so make a new line in the console
            if(token.getType() == Token.Type.EOL) {
                System.out.println();
            }
        }

        //Parsing
        Parser parser = new Parser(tokens);

        //Loop through all the functions in the text file
        FunctionNode functionNode = (FunctionNode)parser.parse();
        while(functionNode != null) {
            //Print out the function
            System.out.println(functionNode);

            //Add the function to the hashmap
            Interpreter.functions.put(functionNode.getName(), functionNode);

            functionNode = (FunctionNode)parser.parse();
        }

        FunctionNode startFunction = (FunctionNode)Interpreter.functions.get("start");
        if(startFunction == null) {
            throw new FunctionNotFoundException("Start function not found");
        }

        //Semantic Analysis
        ArrayList<FunctionNode> userFunctions = new ArrayList<>();
        for(CallableNode function : Interpreter.functions.values()) {

            if(function instanceof FunctionNode) {
                userFunctions.add((FunctionNode) function);
            }
        }
        //Check the assignments
        SemanticAnalysis.checkAssignments(userFunctions);
        SemanticAnalysis.checkConditions(userFunctions);

        System.out.println("\n\nOutput:");
        Interpreter.interpretFunction(startFunction, new ArrayList<>());
    }

}

class FunctionNotFoundException extends Exception {

    public FunctionNotFoundException(String message) {
        super(message);
    }
}
