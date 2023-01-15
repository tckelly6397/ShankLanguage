import java.util.ArrayList;
import java.util.List;

/**
 * Lexer Class.
 *
 * This class is used to check if the assignments have the correct data types as well as if the
 * conditions within statements have the correct data types.
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class SemanticAnalysis {

    //expression is the expression to be checked
    //type is the type that is necessary
    //name is the name of the variable, used for debugging
    //function is necessary so if a variable reference is passed we can get its type from the function
    private static void checkTypes(Node expression, VariableNode.DataType type, String text, FunctionNode function) {
        //It can either be a VariableReferenceNode
        //A data type
        //A MathOpNode
        //A BooleanExpression Node

        //If it's a variable reference type check the corresponding variable type
        //If it's not check what the data type is and if they are not applicable give a reason
        if(expression instanceof VariableReferenceNode) {
            String variableName = ((VariableReferenceNode)expression).getName();
            VariableNode.DataType dataType = getTypeOfVariableInFunction(variableName, function);

            if(dataType != type) {
                throw new InvalidAssignmentTypes(text + " of type: " + type + " is not applicable for data type: " + dataType);
            }

        } else if(expression instanceof IntegerNode && type != VariableNode.DataType.INTEGER) {
            throw new InvalidAssignmentTypes(text + " of type: " + type + " is not applicable for data type: " + VariableNode.DataType.INTEGER);
        } else if(expression instanceof FloatNode && type != VariableNode.DataType.REAL) {
            throw new InvalidAssignmentTypes(text + " of type: " + type + " is not applicable for data type: " + VariableNode.DataType.REAL);
        } else if(expression instanceof BooleanNode && type != VariableNode.DataType.BOOLEAN) {
            throw new InvalidAssignmentTypes(text + " of type: " + type + " is not applicable for data type: " + VariableNode.DataType.BOOLEAN);
        } else if(expression instanceof StringNode && type != VariableNode.DataType.STRING) {
            throw new InvalidAssignmentTypes(text + " of type: " + type + " is not applicable for data type: " + VariableNode.DataType.STRING);
        } else if(expression instanceof CharacterNode && (type != VariableNode.DataType.CHARACTER && type != VariableNode.DataType.STRING)) {
            throw new InvalidAssignmentTypes(text + " of type: " + type + " is not applicable for data type: " + VariableNode.DataType.CHARACTER);
        } else if(expression instanceof MathOpNode) {
            MathOpNode mathNode = (MathOpNode)expression;
            checkTypes(mathNode.getLeftOperand(), type, text, function);
            checkTypes(mathNode.getRightOperand(), type, text, function);
        } else if(expression instanceof BooleanExpression) {
            BooleanExpression booleanExpression = (BooleanExpression)expression;
            checkTypes(booleanExpression.getLeftExpression(), type, text, function);
            checkTypes(booleanExpression.getRightExpression(), type, text, function);
        }
    }

    //Pass in functions to check if their assignments are correct
    public static void checkAssignments(List<FunctionNode> functions) {

        //Loop through all the functions
        for(FunctionNode function : functions) {
            ArrayList<StatementNode> assignmentStatements = new ArrayList<>();

            //Loop through all the functions statements
            List<StatementNode> functionStatements = function.getStatements();

            for(StatementNode statement : functionStatements) {
                assignmentStatements.addAll(getStatementsByType(statement, AssignmentNode.class));
            }

            //Check each individual assignment
            for(StatementNode statement : assignmentStatements) {
                //Get the data type from each variable name then check the assignment
                AssignmentNode assignment = (AssignmentNode)statement;
                String name = assignment.getTarget().getName();
                VariableNode.DataType assignmentType = getTypeOfVariableInFunction(name, function);

                //Check the assignment
                checkTypes(assignment.getExpression(), assignmentType, "Variable:" + name, function);
            }
        }
    }

    //Get the type of a variable from a function using the variable name
    private static VariableNode.DataType getTypeOfVariableInFunction(String name, FunctionNode function) {
        VariableNode.DataType type = null;

        //Loop through the parameters and check
        for(ParameterNode node : function.getParameterVariables()) {
            Node varNode = node.getValue();

            //If the parameter is a variable node and the names are equal
            if(varNode instanceof VariableNode && name.equals(((VariableNode) varNode).getName())) {
                type = ((VariableNode) varNode).getDataType();
            }
        }

        //Loop through all the local variables
        for(VariableNode node : function.getLocalVariables()) {

            //If the names are equal
            if(name.equals(node.getName())) {
                type = node.getDataType();
            }
        }

        if(type == null) {
            throw new VariableNotFoundException("Variable: " + name + " is not found.");
        }

        return type;
    }

    //Get all the statements within a statements, including within loops, looking for a certain
    //statement type
    private static ArrayList<StatementNode> getStatementsByType(StatementNode statement, Class<?> statementType) {
        //The current list of statements of the type
        ArrayList<StatementNode> statements = new ArrayList<>();

        //Check the statement type
        //Get the statements based off it and recursively call this function
        if(statement.getClass() == statementType) {
            statements.add(statement);
        }

        if(statement.getClass() == WhileStatement.class || statement.getClass() == RepeatStatement.class || statement.getClass() == ForStatement.class) {
            List<StatementNode> loopStatements = new ArrayList<>();

            //Get the statements based on the loop
            if(statement.getClass() == WhileStatement.class) {
                loopStatements = ((WhileStatement) statement).getStatements();
            } else if(statement.getClass() == RepeatStatement.class) {
                loopStatements = ((RepeatStatement) statement).getStatements();
            } else if(statement.getClass() == ForStatement.class) {
                loopStatements = ((ForStatement) statement).getStatements();
            }

            for(StatementNode loopStatement : loopStatements) {
                statements.addAll(getStatementsByType(loopStatement, statementType));
            }
        } else if(statement.getClass() == IfNode.class) {
            //Create a temporary if
            IfNode currentIf = (IfNode)statement;

            //While the if isn't null loop through its statements and set
            //the temporary if to the next if
            while(currentIf != null) {

                for(StatementNode loopStatement : currentIf.getStatements()) {
                    statements.addAll(getStatementsByType(loopStatement, statementType));
                }

                currentIf = currentIf.getNextIf();
            }
        }

        return statements;
    }

    //Gets the data type from any type of valid expression
    private static VariableNode.DataType getLeftMostDataType(Node expression, FunctionNode function) throws InvalidSyntaxException {

        if(expression instanceof VariableReferenceNode) {
            return getTypeOfVariableInFunction(((VariableReferenceNode) expression).getName(), function);
        } else if(expression instanceof BooleanNode) {
            return VariableNode.DataType.BOOLEAN;
        } else if(expression instanceof IntegerNode) {
            return VariableNode.DataType.INTEGER;
        } else if(expression instanceof FloatNode) {
            return VariableNode.DataType.REAL;
        } else if(expression instanceof StringNode) {
            return VariableNode.DataType.STRING;
        } else if(expression instanceof CharacterNode) {
            return VariableNode.DataType.CHARACTER;
        } else if(expression instanceof BooleanExpression) {
            return getLeftMostDataType(((BooleanExpression) expression).getLeftExpression(), function);
        } else if(expression instanceof MathOpNode) {
            return getLeftMostDataType(((MathOpNode) expression).getLeftOperand(), function);
        } else {
            //If it's none of these then big problem
            throw new InvalidSyntaxException(expression.toString());
        }
    }

    public static void checkConditions(List<FunctionNode> functions) throws InvalidSyntaxException {

        //Loop through all the functions
        for(FunctionNode function : functions) {
            ArrayList<StatementNode> loopStatements = new ArrayList<>();

            //Loop through all the functions statements
            List<StatementNode> functionStatements = function.getStatements();

            for(StatementNode statement : functionStatements) {
                loopStatements.addAll(getStatementsByType(statement, WhileStatement.class));
                loopStatements.addAll(getStatementsByType(statement, RepeatStatement.class));
                loopStatements.addAll(getStatementsByType(statement, IfNode.class));
            }

            //Check each individual assignment
            for(StatementNode statement : loopStatements) {
                Node expression = null;

                //Get the condition
                if(statement instanceof WhileStatement) {
                    expression = ((WhileStatement) statement).getCondition();
                } else if(statement instanceof RepeatStatement) {
                    expression = ((RepeatStatement) statement).getCondition();
                } else if(statement instanceof IfNode) {
                    //If it's an else then go to next in for loop
                    if(((IfNode) statement).getCondition() == null) {
                        continue;
                    }

                    expression = ((IfNode) statement).getCondition();
                }

                //If the condition is not found then there's a big error
                if(expression == null) {
                    throw new InvalidSyntaxException(statement.toString());
                }

                //Check the assignment
                VariableNode.DataType leftType = getLeftMostDataType(expression, function);
                checkTypes(expression, leftType, "DataType", function);

                if(expression instanceof BooleanExpression) {
                    BooleanExpression.BooleanType booleanType;

                    if(leftType == VariableNode.DataType.BOOLEAN) {
                        booleanType = BooleanExpression.BooleanType.BOOLEAN;
                    } else if(leftType == VariableNode.DataType.INTEGER) {
                        booleanType = BooleanExpression.BooleanType.NUMBER;
                    } else {
                        booleanType = BooleanExpression.BooleanType.STRING;
                    }

                    ((BooleanExpression) expression).setType(booleanType);
                }
            }
        }
    }
}

class VariableNotFoundException extends RuntimeException {

    public VariableNotFoundException(String message) {
        super(message);
    }
}

class InvalidAssignmentTypes extends RuntimeException {

    public InvalidAssignmentTypes(String message) {
        super(message);
    }
}
