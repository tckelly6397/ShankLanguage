import java.util.ArrayList;
import java.util.List;

/**
 * Parser Class.
 *
 * Parser Class contains methods used to understand Tokens
 * and parse them into their corresponding Nodes.
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class Parser {
    private TokenIterator tokenIterator;

    public Parser(List<Token> tokenList) {
        this.tokenIterator = new TokenIterator(tokenList);
    }

    public Node parse() throws InvalidSyntaxException {
        Node finalNode = functionDefinition();

        return finalNode;
    }

    public Node expression() throws InvalidSyntaxException {
        //Store the initial term
        Node termOne = term();

        //If there is an operator set termOne to the MathOpNode of the first and the second term
        //Keep checking until there is no other operators left
        Token plus = matchAndRemove(Token.Type.PLUS);
        Token minus = matchAndRemove(Token.Type.MINUS);
        while(plus != null || minus != null) {
            Node termTwo = term();
            if(termTwo == null) throw new InvalidSyntaxException();

            if(plus != null) {
                termOne = new MathOpNode(MathOpNode.OPType.PLUS, termOne, termTwo);
            } else if(minus != null) {
                termOne = new MathOpNode(MathOpNode.OPType.MINUS, termOne, termTwo);
            }

            plus = matchAndRemove(Token.Type.PLUS);
            minus = matchAndRemove(Token.Type.MINUS);
        }

        //Boolean expression stuff
        //All the conditions
        Token.Type conditions[] = {Token.Type.EQUALS, Token.Type.NOTEQUAL, Token.Type.LESS,
                Token.Type.LESSEQUAL, Token.Type.GREATER, Token.Type.GREATEREQUAL};

        //See if the next token is one of the conditions
        Token condition = null;
        for(int i = 0; i < conditions.length; i++) {
            if((condition = matchAndRemove(conditions[i])) != null) {
                break;
            }
        }

        //Condition found
        if(condition != null) {
            //Get the second term
            Node termTwo = expression();

            //Build the boolean expression
            termOne = new BooleanExpression(termOne, termTwo, condition.getType(), BooleanExpression.getBooleanExpressionType(termOne));
        }

        //If there is no operator return the first Factor
        return termOne;
    }

    public Node term() throws InvalidSyntaxException {
        //Store the initial factor
        Node factorOne = factor();

        //If there is an operator set termOne to the MathOpNode of the first and the second term
        //Keep checking until there is no other operators left
        Token times = matchAndRemove(Token.Type.TIMES);
        Token divide = matchAndRemove(Token.Type.DIVIDE);
        Token mod = matchAndRemove(Token.Type.MOD);
        while(times != null || divide != null || mod != null) {
            Node factorTwo = term();
            if(factorTwo == null) throw new InvalidSyntaxException();

            if(times != null) {
                factorOne = new MathOpNode(MathOpNode.OPType.TIMES, factorOne, factorTwo);
            } else if(divide != null) {
                factorOne = new MathOpNode(MathOpNode.OPType.DIVIDE, factorOne, factorTwo);
            } else if(mod != null) {
                factorOne = new MathOpNode(MathOpNode.OPType.MOD, factorOne, factorTwo);
            }

            //Check if there's another operator
            times = matchAndRemove(Token.Type.TIMES);
            divide = matchAndRemove(Token.Type.DIVIDE);
            mod = matchAndRemove(Token.Type.MOD);
        }

        //If there is no operator return the first Factor
        return factorOne;
    }

    public Node factor() throws InvalidSyntaxException {

        //If there is a parenthesis then assume an Expression
        if(matchAndRemove(Token.Type.LEFTPAREN) != null) {
            Node expressionNode = expression();
            if(expressionNode == null) throw new InvalidSyntaxException();
            if(matchAndRemove(Token.Type.RIGHTPAREN) == null) throw new InvalidSyntaxException();
            return expressionNode;
        }

        //Return the corresponding type of node based on the data found so that
        //the tokens can be identified
        Token variableToken;
        if((variableToken = matchAndRemove(Token.Type.NUMBER)) != null) {
            String value = variableToken.getValue();
            if(value.contains(".")) {
                return new FloatNode(Float.parseFloat(value));
            } else {
                return new IntegerNode(Integer.parseInt(value));
            }
        } else if((variableToken = matchAndRemove(Token.Type.IDENTIFIER)) != null) {
            String value = variableToken.getValue();
            return new VariableReferenceNode(value);
        } else if(matchAndRemove(Token.Type.TRUE) != null) {
            return new BooleanNode(true);
        } else if(matchAndRemove(Token.Type.FALSE) != null) {
            return new BooleanNode(false);
        } else if((variableToken = matchAndRemove(Token.Type.STRING)) != null) {
            String value = variableToken.getValue();
            return new StringNode(value);
        } else if((variableToken = matchAndRemove(Token.Type.CHARACTER)) != null) {
            char value = variableToken.getValue().charAt(0);
            return new CharacterNode(value);
        }

        return null;
    }

    public Node functionDefinition() throws InvalidSyntaxException {
        String name;
        List<ParameterNode> parameters = new ArrayList<>();
        List<VariableNode> localVariables = new ArrayList<>();

        //Define
        //If define is not found then there is no function so return null
        if(matchAndRemove(Token.Type.DEFINE) == null)
            return null;

        //Name
        Token nameToken = matchAndRemove(Token.Type.IDENTIFIER);
        if(nameToken == null) throw new InvalidSyntaxException();
        name = nameToken.getValue();

        if(matchAndRemove(Token.Type.LEFTPAREN) == null) throw new InvalidSyntaxException();

        //Parameters
        parameters = getParameters();

        if(matchAndRemove(Token.Type.RIGHTPAREN) == null) throw new InvalidSyntaxException();
        removeEndOfLine();

        //After header
        if(matchAndRemove(Token.Type.CONSTANTS) != null) {
            removeEndOfLine();
            localVariables = getConstants();
        }

        if(matchAndRemove(Token.Type.VARIABLES) != null) {
            removeEndOfLine();
            localVariables.addAll(getVariables());
        }

        //Body
        List<StatementNode> statements = bodyFunction();

        return new FunctionNode(name, parameters, localVariables, statements);
    }

    //A recursive function to get all the parameters within parenthesis
    public List<ParameterNode> getParameters() throws InvalidSyntaxException {
        //varName, var2name : integer; varname, i, j : real
        List<ParameterNode> nodes = new ArrayList<>();
        List<String> names = new ArrayList<>();
        VariableNode.DataType dataType;
        boolean isVar;

        if(matchAndRemove(Token.Type.VAR) != null)
            isVar = true;
        else
            isVar = false;

        //While there is another identifier add its name to the list of Strings
        while(tokenIterator.hasNext() && tokenIterator.peek().getType() == Token.Type.IDENTIFIER) {
            names.add(tokenIterator.next().getValue());

            matchAndRemove(Token.Type.COMMA);
        }

        //This if statement handles if there are no parameters
        if(names.size() != 0) {
            if (matchAndRemove(Token.Type.COLON) == null) throw new InvalidSyntaxException();

            //Set the data type
            Token dataTypeToken = tokenIterator.next();
            if (dataTypeToken.getType() == Token.Type.INTEGER) {
                dataType = VariableNode.DataType.INTEGER;
            } else if (dataTypeToken.getType() == Token.Type.REAL) {
                dataType = VariableNode.DataType.REAL;
            } else if (dataTypeToken.getType() == Token.Type.STRING) {
                dataType = VariableNode.DataType.STRING;
            } else if (dataTypeToken.getType() == Token.Type.CHARACTER) {
                dataType = VariableNode.DataType.CHARACTER;
            } else if (dataTypeToken.getType() == Token.Type.BOOLEAN) {
                dataType = VariableNode.DataType.BOOLEAN;
            } else {
                throw new InvalidSyntaxException();
            }

            //Add all the names with the correct datatype
            for (String name : names) {
                nodes.add(new ParameterNode(new VariableNode(name, dataType), isVar));
            }

            //If there is a semicolon then recursively call parameters to grab the next one
            if (matchAndRemove(Token.Type.SEMICOLON) != null)
                nodes.addAll(getParameters());
        }

        return nodes;
    }

    //A recursive function to get all the variables
    public List<VariableNode> getVariables() throws InvalidSyntaxException {
        List<VariableNode> nodes = new ArrayList<>();
        List<String> names = new ArrayList<>();
        VariableNode.DataType dataType;

        //While there is an identifier add its name to a list of Strings
        while(tokenIterator.hasNext() && tokenIterator.peek().getType() == Token.Type.IDENTIFIER) {
            names.add(tokenIterator.next().getValue());

            matchAndRemove(Token.Type.COMMA);
        }

        if(matchAndRemove(Token.Type.COLON) == null) throw new InvalidSyntaxException();

        //Get the datatype
        Token dataTypeToken = tokenIterator.next();
        if(dataTypeToken.getType() == Token.Type.INTEGER) {
            dataType = VariableNode.DataType.INTEGER;
        } else if(dataTypeToken.getType() == Token.Type.REAL) {
            dataType = VariableNode.DataType.REAL;
        } else if (dataTypeToken.getType() == Token.Type.STRING) {
            dataType = VariableNode.DataType.STRING;
        } else if (dataTypeToken.getType() == Token.Type.CHARACTER) {
            dataType = VariableNode.DataType.CHARACTER;
        } else if (dataTypeToken.getType() == Token.Type.BOOLEAN) {
            dataType = VariableNode.DataType.BOOLEAN;
        } else {
            throw new InvalidSyntaxException();
        }

        //Set all the names to the correct datatype
        for(String name : names) {
            nodes.add(new VariableNode(name, dataType));
        }

        //Remove the end of line token
        removeEndOfLine();

        //If there is another identifier then call this function again
        if(tokenIterator.peek().getType() == Token.Type.IDENTIFIER)
            nodes.addAll(getVariables());

        return nodes;
    }

    public List<VariableNode> getConstants() throws InvalidSyntaxException {
        List<VariableNode> nodes = new ArrayList<>();

        //While there is an identifier add it to the list of nodes
        while(tokenIterator.hasNext() && tokenIterator.peek().getType() == Token.Type.IDENTIFIER) {
            nodes.add(processConstants());

            //Remove end of line token
            removeEndOfLine();
        }

        return nodes;
    }

    public VariableNode processConstants() throws InvalidSyntaxException {
        String name = tokenIterator.next().getValue();

        if(matchAndRemove(Token.Type.EQUALS) == null) throw new InvalidSyntaxException();

        if(tokenIterator.peek().getType() == Token.Type.NUMBER) {
            //Process Number
            float value = Float.parseFloat(tokenIterator.next().getValue());

            //Create the node with the corresponding number node type based on the value
            //Must be an integer
            if (value == (int) value) {
                return new VariableNode(name, VariableNode.DataType.INTEGER, new IntegerNode((int) value), true);
            } else {
                return new VariableNode(name, VariableNode.DataType.REAL, new FloatNode(value), true);
            }
        } else if(tokenIterator.peek().getType() == Token.Type.STRING) {
            //Process String
            String value = tokenIterator.next().getValue();
            return new VariableNode(name, VariableNode.DataType.STRING, new StringNode(value), true);
        } else if(tokenIterator.peek().getType() == Token.Type.CHARACTER) {
            //Process Character
            char value = tokenIterator.next().getValue().charAt(0);
            return new VariableNode(name, VariableNode.DataType.CHARACTER, new CharacterNode(value), true);
        } else if(tokenIterator.peek().getType() == Token.Type.TRUE) {
            //Process true boolean
            tokenIterator.next();
            return new VariableNode(name, VariableNode.DataType.BOOLEAN, new BooleanNode(true), true);
        } else if(tokenIterator.peek().getType() == Token.Type.FALSE) {
            //Process false boolean
            tokenIterator.next();
            return new VariableNode(name, VariableNode.DataType.BOOLEAN, new BooleanNode(false), true);
        } else {
            throw new AssignmentException();
        }
    }

    public List<StatementNode> bodyFunction() throws InvalidSyntaxException {
        //Body
        if (matchAndRemove(Token.Type.BEGIN) == null) throw new InvalidSyntaxException();
        removeEndOfLine();

        List<StatementNode> statements = statements();

        //End
        if (matchAndRemove(Token.Type.END) == null) throw new InvalidSyntaxException();
        removeEndOfLine();

        return statements;
    }

    //Returns a list of statement nodes used by body function, loops and if statements
    public List<StatementNode> statements() throws InvalidSyntaxException {
        List<StatementNode> statementNodes = new ArrayList<>();
        StatementNode statementNode = statement();

        while(statementNode != null) {
            statementNodes.add(statementNode);
            statementNode = statement();
        }

        return statementNodes;
    }

    //Calls all types of statements, if they return null then don't
    //add then don't return it
    public StatementNode statement() throws InvalidSyntaxException {
        StatementNode statement = null;

        statement = assignment();
        if(statement != null) {
            return statement;
        }

        statement = getWhileStatement();
        if(statement != null) {
            return statement;
        }

        statement = getRepeatStatement();
        if(statement != null) {
            return statement;
        }

        statement = getIfStatement(true);
        if(statement != null) {
            return statement;
        }

        statement = getForStatement();
        if(statement != null) {
            return statement;
        }

        statement = getFunctionCallStatement();
        if(statement != null) {
            return statement;
        }

        return null;
    }

    //Looks for an identifier and an expression, returning an assignment node
    public AssignmentNode assignment() throws InvalidSyntaxException {
        if(tokenIterator.peekAhead(Token.Type.ASSIGN) == null)
            return null;

        //Get the identifier
        Token identifier = matchAndRemove(Token.Type.IDENTIFIER);
        if(identifier == null) throw new InvalidSyntaxException();
        VariableReferenceNode target = new VariableReferenceNode(identifier.getValue());

        //Remove the assign
        if(matchAndRemove(Token.Type.ASSIGN) == null) throw new InvalidSyntaxException();

        //Get the expression
        Node expression = expression();

        removeEndOfLine();

        return new AssignmentNode(target, expression);
    }

    //Looks for corresponding tokens to build a while statement
    public WhileStatement getWhileStatement() throws InvalidSyntaxException {
        List<StatementNode> statements;
        Node booleanExp;

        //Get the while
        if(matchAndRemove(Token.Type.WHILE) == null)
            return null;

        //Get the boolean expression;
        booleanExp = expression();

        //Remove the end of line(s)
        removeEndOfLine();

        //Get the body
        statements = bodyFunction();

        return new WhileStatement(booleanExp, statements);
    }

    //Looks for corresponding tokens to build a repeat statement
    public RepeatStatement getRepeatStatement() throws InvalidSyntaxException {
        List<StatementNode> statements;
        Node booleanExp;

        //Get the while
        if(matchAndRemove(Token.Type.REPEAT) == null)
            return null;

        //Remove the end of line(s)
        removeEndOfLine();

        //Get the body
        statements = bodyFunction();

        //Remove the end token
        if(matchAndRemove(Token.Type.UNTIL) == null)
            throw new InvalidSyntaxException();

        //Get the boolean expression;
        booleanExp = expression();

        removeEndOfLine();

        return new RepeatStatement(booleanExp, statements);
    }

    //Looks for corresponding tokens to build a for statement
    public ForStatement getForStatement() throws InvalidSyntaxException {
        //Initial values to be filled
        VariableReferenceNode variable = null;
        Node begin = null;
        Node end = null;
        List<StatementNode> statements;

        //Remove the for token, if there isn't one then return null
        if(matchAndRemove(Token.Type.FOR) == null)
            return null;

        //Find the identifier
        Token identifier = matchAndRemove(Token.Type.IDENTIFIER);
        if(identifier == null) throw new InvalidSyntaxException();
        variable = new VariableReferenceNode(identifier.getValue());

        //Remove the from token
        if(matchAndRemove(Token.Type.FROM) == null) throw new InvalidSyntaxException();

        //Get the first value and convert it to an int
        begin = new IntegerNode(Integer.parseInt(tokenIterator.next().getValue()));

        //Remove the to token
        if(matchAndRemove(Token.Type.TO) == null) throw new InvalidSyntaxException();

        //Get the second value and convert it to an int
        end = new IntegerNode(Integer.parseInt(tokenIterator.next().getValue()));
        removeEndOfLine();

        //Get the body
        statements = bodyFunction();

        return new ForStatement(variable, begin, end, statements);
    }

    //Looks for corresponding tokens to build an if statement
    //If firstIf is true then it will look for an IF token rather
    //than a ELSIF or ELSE token
    public IfNode getIfStatement(boolean firstIf) throws InvalidSyntaxException {
        //The initial values
        Node booleanExp = null;
        List<StatementNode> statements;

        //If this is the first time the function is called look for IF token
        if(firstIf) {
            if(matchAndRemove(Token.Type.IF) == null)
                return null;
        }

        //This if statement also looks for a boolean expression because
        //it's an if and else if statement rather than an else statement
        //which takes no boolean expression
        if(firstIf || matchAndRemove(Token.Type.ELSEIF) != null) {
            //Get the boolean expression
            booleanExp = expression();

            matchAndRemove(Token.Type.THEN);
            removeEndOfLine();

            //Get the body
            statements = bodyFunction();

            return new IfNode(booleanExp, getIfStatement(false), statements);
        } else if(matchAndRemove(Token.Type.ELSE) != null) {
            removeEndOfLine();

            //Get the body
            statements = bodyFunction();

            return new IfNode(null, null, statements);
        }

        return null;
    }

    //Gets an identifier then all the parameters and creates a functionCallStatement node
    public FunctionCallStatement getFunctionCallStatement() throws InvalidSyntaxException {
        //Define the variables
        String name;
        List<ParameterNode> parameters = new ArrayList<>();

        //Check if the next token is an identifier
        Token nameToken = matchAndRemove(Token.Type.IDENTIFIER);
        if(nameToken == null) {
            return null;
        }

        //If it is an identifier then set the name to the value of the identifier token
        name = nameToken.getValue();

        //While the next token is not end of line, grab the next parameter
        while(tokenIterator.peek().getType() != Token.Type.EOL) {
            ParameterNode parameter = null;

            if(matchAndRemove(Token.Type.VAR) != null) {
                //Var stuff
                //Check if the next token is an identifier
                Token parameterToken = matchAndRemove(Token.Type.IDENTIFIER);
                if(parameterToken == null)
                    return null;

                //If it is an identifier then set the name to the value of the identifier token
                String parameterName = parameterToken.getValue();

                parameter = new ParameterNode(new VariableReferenceNode(parameterName), true);
            } else if(tokenIterator.peek().getType() == Token.Type.IDENTIFIER) {
                //Variable stuff
                //Get the identifier token
                Token parameterToken = matchAndRemove(Token.Type.IDENTIFIER);
                String parameterName = parameterToken.getValue();

                parameter = new ParameterNode(new VariableReferenceNode(parameterName), false);
            } else if(tokenIterator.peek().getType() == Token.Type.NUMBER) {
                //Number stuff
                Token parameterToken = matchAndRemove(Token.Type.NUMBER);
                String value = parameterToken.getValue();

                //It's an int if the float value is equal to the integer
                if(!value.contains(".")) {
                    parameter = new ParameterNode(new IntegerNode(Integer.parseInt(value)), false);
                } else {
                    parameter = new ParameterNode(new FloatNode(Float.parseFloat(value)), false);
                }
            } else if(tokenIterator.peek().getType() == Token.Type.CHARACTER) {
                parameter = new ParameterNode(new CharacterNode(tokenIterator.next().getValue().charAt(0)), false);
            } else if(tokenIterator.peek().getType() == Token.Type.STRING) {
                parameter = new ParameterNode(new StringNode(tokenIterator.next().getValue()), false);
            } else if(tokenIterator.peek().getType() == Token.Type.TRUE) {
                parameter = new ParameterNode(new BooleanNode(true), false);
            } else if(tokenIterator.peek().getType() == Token.Type.FALSE) {
                parameter = new ParameterNode(new BooleanNode(false), false);
            }

            parameters.add(parameter);
            matchAndRemove(Token.Type.COMMA);
        }

        removeEndOfLine();
        return new FunctionCallStatement(name, parameters);
    }

    //Removes the end of line token and removes any trailing ones as well
    public void removeEndOfLine() throws InvalidSyntaxException {
        if(matchAndRemove(Token.Type.EOL) == null) throw new InvalidSyntaxException();

        //Keep removing end of line token
        while(matchAndRemove(Token.Type.EOL) != null);
    }

    //Checks if the next Token in the list is the passed in type, if so return and remove it, else return null
    public Token matchAndRemove(Token.Type type) {

        if(tokenIterator.hasNext() && tokenIterator.peek().getType() == type) {
            //Return and remove the token
            return tokenIterator.next();
        }

        return null;
    }
}
