import com.sun.jdi.BooleanType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Interpreter Class.
 *
 * The Interpreter accepts a Node and will evaluate it
 * to a float value.
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class Interpreter {

    //List of function names to function calls
    public static HashMap<String, CallableNode> functions = new HashMap<>() {{
        put("write", new BuiltInFunctionWrite());
        put("read", new BuiltInFunctionRead());
        put("getRandom", new BuiltInFunctionGetRandom());
        put("integerToReal", new BuiltInFunctionIntegerToReal());
        put("realToInteger", new BuiltInFunctionRealToInteger());
        put("squareRoot", new BuiltInFunctionSquareRoot());
        put("left", new BuiltInFunctionLeft());
        put("right", new BuiltInFunctionRight());
        put("substring", new BuiltInFunctionSubstring());
        put("wait", new BuiltInFunctionWait());
    }};

    public static float resolveNumber(Node node, HashMap<String, InterpreterDataType> variables) throws InvalidSyntaxException {

        if (node instanceof VariableReferenceNode) {
            node = getNodeFromDataType(variables.get(((VariableReferenceNode) node).getName()));
        }

        if (node instanceof FloatNode) {
            return ((FloatNode) node).getNumber();
        } else if (node instanceof IntegerNode) {
            return (float) ((IntegerNode) node).getNumber();
        } else if (node instanceof MathOpNode) {
            MathOpNode operatorNode = ((MathOpNode) node);

            //Use recursion to get right and left values
            float leftValue = resolveNumber(operatorNode.getLeftOperand(), variables);
            float rightValue = resolveNumber(operatorNode.getRightOperand(), variables);

            //Get the operator type
            MathOpNode.OPType operator = operatorNode.getType();

            //Apply maths based on operator type
            if (operator == MathOpNode.OPType.PLUS) {
                return leftValue + rightValue;
            } else if (operator == MathOpNode.OPType.MINUS) {
                return leftValue - rightValue;
            } else if (operator == MathOpNode.OPType.TIMES) {
                return leftValue * rightValue;
            } else if (operator == MathOpNode.OPType.DIVIDE) {
                return leftValue / rightValue;
            } else if (operator == MathOpNode.OPType.MOD) {
                return leftValue % rightValue;
            } else {
                throw new InvalidSyntaxException(operator + "");
            }
        } else {
            throw new InvalidSyntaxException();
        }
    }

    public static boolean resolveBoolean(Node node, HashMap<String, InterpreterDataType> variables) throws InvalidSyntaxException {
        if (node instanceof VariableReferenceNode) {
            node = getNodeFromDataType(variables.get(((VariableReferenceNode) node).getName()));
        }

        return ((BooleanNode)node).getValue();
    }

    public static String resolveString(Node node, HashMap<String, InterpreterDataType> variables) throws InvalidSyntaxException {
        if (node instanceof VariableReferenceNode) {
            node = getNodeFromDataType(variables.get(((VariableReferenceNode) node).getName()));
        }

        if (node instanceof StringNode) {
            return ((StringNode) node).getValue();
        } else if (node instanceof CharacterNode) {
            return "" + ((CharacterNode) node).getValue();
        } else if (node instanceof MathOpNode) {
            MathOpNode operatorNode = ((MathOpNode) node);

            //Use recursion to get right and left values
            String leftValue = resolveString(operatorNode.getLeftOperand(), variables);
            String rightValue = resolveString(operatorNode.getRightOperand(), variables);

            //Get the operator type
            MathOpNode.OPType operator = operatorNode.getType();

            //Apply maths based on operator type
            if (operator == MathOpNode.OPType.PLUS) {
                return leftValue + rightValue;
            }
        }

        return ((StringNode)node).getValue();
    }

    public static char resolveCharacter(Node node, HashMap<String, InterpreterDataType> variables) throws InvalidSyntaxException {
        if (node instanceof VariableReferenceNode) {
            node = getNodeFromDataType(variables.get(((VariableReferenceNode) node).getName()));
        }

        return ((CharacterNode)node).getValue();
    }

    public static boolean evaluateNumberExpression(BooleanExpression booleanExpression, HashMap<String, InterpreterDataType> variables) throws InvalidSyntaxException {
        float leftExpression = resolveNumber(booleanExpression.getLeftExpression(), variables);
        float rightExpression = resolveNumber(booleanExpression.getRightExpression(), variables);
        Token.Type condition = booleanExpression.getCondition();

        if(condition == Token.Type.GREATER && leftExpression > rightExpression) {
            return true;
        } else if(condition == Token.Type.GREATEREQUAL && leftExpression >= rightExpression) {
            return true;
        } else if(condition == Token.Type.LESS && leftExpression < rightExpression) {
            return true;
        } else if(condition == Token.Type.LESSEQUAL && leftExpression <= rightExpression) {
            return true;
        } else if(condition == Token.Type.EQUALS && leftExpression == rightExpression) {
            return true;
        } else if(condition == Token.Type.NOTEQUAL && leftExpression != rightExpression) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean evaluateBooleanNodeExpression(BooleanExpression booleanExpression, HashMap<String, InterpreterDataType> variables) throws InvalidSyntaxException {
        boolean leftExpression = resolveBoolean(booleanExpression.getLeftExpression(), variables);
        boolean rightExpression = resolveBoolean(booleanExpression.getRightExpression(), variables);
        Token.Type condition = booleanExpression.getCondition();

        if(condition == Token.Type.EQUALS && leftExpression == rightExpression) {
            return true;
        } else if(condition == Token.Type.NOTEQUAL && leftExpression != rightExpression) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean evaluateStringExpression(BooleanExpression booleanExpression, HashMap<String, InterpreterDataType> variables) throws InvalidSyntaxException {
        String leftExpression = resolveString(booleanExpression.getLeftExpression(), variables);
        String rightExpression = resolveString(booleanExpression.getRightExpression(), variables);
        Token.Type condition = booleanExpression.getCondition();

        if(condition == Token.Type.EQUALS && leftExpression.equals(rightExpression)) {
            return true;
        } else if(condition == Token.Type.NOTEQUAL && !(leftExpression.equals(rightExpression))) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean evaluateBooleanExpression(Node expression, HashMap<String, InterpreterDataType> variables) throws InvalidSyntaxException {
        if(expression instanceof VariableReferenceNode) {
            String name = ((VariableReferenceNode)expression).getName();

            if(variables.get(name) instanceof BooleanDataType) {
                return ((BooleanDataType)variables.get(name)).getValue();
            }
        }

        if(expression instanceof BooleanNode) {
            return ((BooleanNode) expression).getValue();
        }

        BooleanExpression booleanExpression = (BooleanExpression) expression;

        //Check the type of boolean expression and call the corresponding evaluate function
        if(booleanExpression.getType() == BooleanExpression.BooleanType.NUMBER) {
            return evaluateNumberExpression(booleanExpression, variables);
        } else if(booleanExpression.getType() == BooleanExpression.BooleanType.BOOLEAN) {
            return evaluateBooleanNodeExpression(booleanExpression, variables);
        } else if(booleanExpression.getType() == BooleanExpression.BooleanType.STRING) {
            return evaluateStringExpression(booleanExpression, variables);
        } else {
            throw new InvalidSyntaxException();
        }
    }

    /*
        This function takes in a function definition as well as a collection of dataTypes which are the dataTypes
        of the passed in parameters. We use the function definition to grab all of its variables and put all of the
        parameters and the dataTypes into a hashmap so that these values can be used within the code execution. This
        function basically sorts out the data we have gathered.

        Key thing to remember here, the values in variables will always be references, so rather than replacing
        a whole data type you replace the value within the dataType container, thus keeping the variables that
        are needed to be shared between functions alive.
        -Written by a very sad Thomas who spent many hours debugging only to figure this simple problem out
     */
    public static void interpretFunction(FunctionNode functionNode, List<InterpreterDataType> parameterDataTypes) throws InvalidFunctionCallException, InvalidSyntaxException {
        //Hashmap of all variables
        HashMap<String, InterpreterDataType> variables = new HashMap<>();

        //The parameters and the local variables of the function we are working with
        List<ParameterNode> parameters = functionNode.getParameterVariables();
        List<VariableNode> localVariables = functionNode.getLocalVariables();

        //Loop through all the parameters and insert it into a hashmap with the corresponding dataType
        for(int i = 0; i < parameters.size(); i++) {
            String variableName = ((VariableNode)parameters.get(i).getValue()).getName();
            variables.put(variableName, parameterDataTypes.get(i));
        }

        //Loop through all the variables, setting the data type to the corresponding type
        //If it's a constant set the value of that data type to the value
        //Insert it into the hashmap
        for(int i = 0; i < localVariables.size(); i++) {
            VariableNode variable = localVariables.get(i);
            InterpreterDataType dataType = null;

            //Create a hashmap of the VariableNode data types to their corresponding Interpreter data types.
            HashMap<VariableNode.DataType, InterpreterDataType> dataTypes = new HashMap<>();
            dataTypes.put(VariableNode.DataType.INTEGER, new IntDataType());
            dataTypes.put(VariableNode.DataType.REAL, new FloatDataType());
            dataTypes.put(VariableNode.DataType.BOOLEAN, new BooleanDataType());
            dataTypes.put(VariableNode.DataType.STRING, new StringDataType());
            dataTypes.put(VariableNode.DataType.CHARACTER, new CharacterDataType());

            //Set the data type based on the hashmap
            dataType = dataTypes.get(variable.getDataType());

            //If the variable is a constant set it's value based on the type
            if(variable.isConstant()) {
                if(variable.getDataType() == VariableNode.DataType.INTEGER) {
                    int value = ((IntegerNode)variable.getValue()).getNumber();
                    dataType.fromString("" + value);
                } else if(variable.getDataType() == VariableNode.DataType.REAL) {
                    float value = ((FloatNode)variable.getValue()).getNumber();
                    dataType.fromString("" + value);
                } else if(variable.getDataType() == VariableNode.DataType.BOOLEAN) {
                    boolean value = ((BooleanNode)variable.getValue()).getValue();
                    dataType.fromString("" + value);
                } else if(variable.getDataType() == VariableNode.DataType.STRING) {
                    String value = ((StringNode)variable.getValue()).getValue();
                    dataType.fromString(value);
                } else if(variable.getDataType() == VariableNode.DataType.CHARACTER) {
                    char value = ((CharacterNode)variable.getValue()).getValue();
                    dataType.fromString("" + value);
                }
            }

            //Put the variable name, and it's dataType into the hashmap
            variables.put(variable.getName(), dataType);
        }

        interpretBlock(functionNode.getStatements(), variables);
    }

    //Take in a list of statements and a hashmap of variables, loop through the statements and interpret each statement
    //Variables is a list of dataTypes taken from the variables within the function, these variables will be altered
    //through this function whether it be a function all statement or an assignment
    public static void interpretBlock(List<StatementNode> statements, HashMap<String, InterpreterDataType> variables) throws InvalidFunctionCallException, InvalidSyntaxException {

        for(StatementNode statement : statements) {

            //The statement is a functionCall statement
            if(statement instanceof FunctionCallStatement) {
                //Locate the function
                //Gather information to be used for parameters and function data
                FunctionCallStatement callStatement = ((FunctionCallStatement)statement);
                CallableNode function = functions.get(callStatement.getName());

                //If the function is not variadic and builtin check if the size of the parameters are equal
                if(!(function instanceof BuiltInFunctions && ((BuiltInFunctions)function).isVariadic())) {
                    //If the parameters size do not match the calls parameters throw an exception
                    if(function instanceof FunctionNode && function.getParameterVariables().size() != callStatement.getParameters().size()) {
                        throw new ParameterException();
                    }
                }

                //Create a collection of data types, add each parameter as a datatype to the collection
                List<InterpreterDataType> dataTypes = new ArrayList<>();
                for(ParameterNode parameter : callStatement.getParameters()) {
                    Node node = parameter.getValue();
                    InterpreterDataType dataType;

                    //If the node is a variable then get the type from there
                    //If not then it's either a FloatNode or IntegerNode so gather the data from it
                    if(node instanceof VariableReferenceNode) {
                        //Get the variable from the passed in list of variables
                        InterpreterDataType tempDataType = variables.get(((VariableReferenceNode)node).getName());
                        if(tempDataType instanceof IntDataType) {
                            dataType = new IntDataType();
                        } else if(tempDataType instanceof FloatDataType) {
                            dataType = new FloatDataType();
                        } else if(tempDataType instanceof BooleanDataType) {
                            dataType = new BooleanDataType();
                        } else if(tempDataType instanceof StringDataType) {
                            dataType = new StringDataType();
                        } else if(tempDataType instanceof CharacterDataType) {
                            dataType = new CharacterDataType();
                        } else {
                            throw new InvalidSyntaxException();
                        }

                        dataType.fromString(tempDataType.toString());
                    } else {
                        //Creates a new InterpreterDataType from the node
                        dataType = getDataTypeByNode(node);
                    }

                    dataTypes.add(dataType);
                }

                //Interpret the function if user defined or execute the function if the function is built in, passing
                //in the collection of data types
                //Changes the data types to be used later
                if(function instanceof FunctionNode) {
                    interpretFunction((FunctionNode)function, dataTypes);
                } else if(function instanceof BuiltInFunctions) {
                    ((BuiltInFunctions)function).execute(dataTypes);
                } else {
                    throw new RuntimeException("Invalid Function Type");
                }

                //Finally, loop through all the parameters, if a parameter and variable is var, change the variable
                //The data types are changed so check the parameters and make sure they're a var to be changed to the
                //new data
                List<ParameterNode> parameters = callStatement.getParameters();
                for(int i = 0; i < parameters.size(); i++) {
                    ParameterNode parameter = parameters.get(i);

                    //If the parameter is not a Var then go to the next one
                    if (!parameter.isVar())
                        continue;

                    //Replaces the data within the interpreterDataType in the variables hashmap with the new data
                    if (function instanceof BuiltInFunctions) {
                        variables.get(((VariableReferenceNode)parameter.getValue()).getName()).fromString(dataTypes.get(i).toString());
                    } else if (function instanceof FunctionNode && function.getParameterVariables().get(i).isVar()) {
                        variables.get(((VariableReferenceNode)parameter.getValue()).getName()).fromString(dataTypes.get(i).toString());
                    }
                }
            } else if(statement instanceof AssignmentNode) {
                //Gather the data for the assignment
                AssignmentNode assignment = (AssignmentNode)statement;
                String name = assignment.getTarget().getName();
                InterpreterDataType currentDataType = variables.get(name);

                //Define a string and fill it with the resolve method based on the data type that
                //the value is being stored in
                String finalValue;
                if(currentDataType instanceof IntDataType || currentDataType instanceof FloatDataType) {
                    finalValue = "" + resolveNumber(assignment.getExpression(), variables);
                } else if(currentDataType instanceof BooleanDataType) {
                    finalValue = "" + resolveBoolean(assignment.getExpression(), variables);
                } else if(currentDataType instanceof StringDataType) {
                    finalValue = "" + resolveString(assignment.getExpression(), variables);
                } else if(currentDataType instanceof CharacterDataType) {
                    finalValue = "" + resolveCharacter(assignment.getExpression(), variables);
                } else {
                    System.out.println(currentDataType);
                    throw new InvalidSyntaxException();
                }

                currentDataType.fromString(finalValue);
            } else if(statement instanceof WhileStatement) {
                //Gather the while data
                WhileStatement whileStatement = (WhileStatement)statement;

                //Execute  while with the condition
                while(evaluateBooleanExpression(whileStatement.getCondition(), variables)) {
                    interpretBlock(whileStatement.getStatements(), variables);
                }
            } else if(statement instanceof RepeatStatement) {
                //Gather the data from the repeat statement
                RepeatStatement whileStatement = (RepeatStatement)statement;

                //Execute do while with the condition
                do {
                    interpretBlock(whileStatement.getStatements(), variables);
                } while(!evaluateBooleanExpression(whileStatement.getCondition(), variables));
            } else if(statement instanceof IfNode) {
                //Gather the data from the if statement
                IfNode ifNode = (IfNode)statement;

                //first part checks to see if it's, second part checks to see if the condition of the current if
                //is false. If both are true then set the if to the next
                while(ifNode != null && ifNode.getCondition() != null && !evaluateBooleanExpression(ifNode.getCondition(), variables)) {
                    ifNode = ifNode.getNextIf();
                }

                if(ifNode != null)
                    interpretBlock(ifNode.getStatements(), variables);
            } else if(statement instanceof ForStatement) {
                //Gather the data from the for loop
                ForStatement forStatement = (ForStatement)statement;
                InterpreterDataType variable = variables.get(forStatement.getVariable().getName());

                //If either the begin node or end node is not an IntegerNode throw an exception
                if(!(forStatement.getBeginNode() instanceof IntegerNode) || !(forStatement.getEndNode() instanceof IntegerNode))
                    throw new ParameterException();

                int beginValue = ((IntegerNode)forStatement.getBeginNode()).getNumber();
                int endValue = ((IntegerNode)forStatement.getEndNode()).getNumber();

                //If the variable is not an Integer then throw exception
                if(!(variable instanceof IntDataType))
                    throw new ParameterException();

                if(beginValue <= endValue) {
                    for(int i = beginValue; i <= endValue; i++) {
                        variable.fromString(i + "");
                        interpretBlock(forStatement.getStatements(), variables);
                    }
                } else if(beginValue > endValue) {
                    for(int i = beginValue; i >= endValue; i--) {
                        variable.fromString(i + "");
                        interpretBlock(forStatement.getStatements(), variables);
                    }
                }

            }
        }
    }

    //Get a passed in variable, either a IntDataType, FloatDataType, or VariableNode and get
    //its corresponding dataType.
    //Sets its value as well
    private static InterpreterDataType getDataTypeByNode(Node node) {

        if(node instanceof VariableNode) {
            return getDataTypeByNode(((VariableNode)node).getValue());
        } else if(node instanceof IntegerNode) {
            return new IntDataType(((IntegerNode)node).getNumber());
        } else if(node instanceof FloatNode) {
            return new FloatDataType(((FloatNode)node).getNumber());
        } else if(node instanceof BooleanNode) {
            return new BooleanDataType(((BooleanNode)node).getValue());
        } else if(node instanceof StringNode) {
            return new StringDataType(((StringNode)node).getValue());
        } else if(node instanceof CharacterNode) {
            return new CharacterDataType(((CharacterNode)node).getValue());
        } else {
            return null;
        }
    }

    private static Node getNodeFromDataType(InterpreterDataType dataType) {
        if(dataType instanceof IntDataType) {
            return new IntegerNode(((IntDataType) dataType).getNumber());
        } else if (dataType instanceof FloatDataType) {
            return new FloatNode(((FloatDataType) dataType).getNumber());
        } else if (dataType instanceof BooleanDataType) {
            return new BooleanNode(((BooleanDataType) dataType).getValue());
        } else if (dataType instanceof StringDataType) {
            return new StringNode(((StringDataType) dataType).getValue());
        } else if (dataType instanceof CharacterDataType) {
            return new CharacterNode(((CharacterDataType) dataType).getValue());
        } else {
            return null;
        }
    }
}

class ParameterException extends RuntimeException {

    public ParameterException() {
        super("Parameters do not match");
    }
}

class AssignmentException extends RuntimeException {
    public AssignmentException() {
        super("Assignment mismatch");
    }
}
