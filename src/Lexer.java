import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Lexer Object.
 *
 * Lexer is used to parse a String into corresponding Tokens.
 * Throws exceptions if invalid syntax is found.
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class Lexer {
    //Is comment activated
    private static boolean isCommenting = false;
    //Is string activated
    private static boolean isString = false;
    //Is character activated
    private static boolean isCharacter = false;
    private static boolean oneCharacterFound = false;

    //Hashmap containing specific words for corresponding tokens
    private HashMap<String, Token.Type> reservedText = new HashMap<String, Token.Type>()
    {{
        put("integer", Token.Type.INTEGER);
        put("real", Token.Type.REAL);
        put("begin", Token.Type.BEGIN);
        put("end", Token.Type.END);
        put("variables", Token.Type.VARIABLES);
        put("constants", Token.Type.CONSTANTS);
        put("define", Token.Type.DEFINE);
        put("+", Token.Type.PLUS);
        put("-", Token.Type.MINUS);
        put("*", Token.Type.TIMES);
        put("/", Token.Type.DIVIDE);
        put(",", Token.Type.COMMA);
        put("=", Token.Type.EQUALS);
        put(":", Token.Type.COLON);
        put(":=", Token.Type.ASSIGN);
        put(";", Token.Type.SEMICOLON);
        put("if", Token.Type.IF);
        put("then", Token.Type.THEN);
        put("else", Token.Type.ELSE);
        put("elsif", Token.Type.ELSEIF);
        put("for", Token.Type.FOR);
        put("from", Token.Type.FROM);
        put("to", Token.Type.TO);
        put("while", Token.Type.WHILE);
        put("repeat", Token.Type.REPEAT);
        put("until", Token.Type.UNTIL);
        put("mod", Token.Type.MOD);
        put("<", Token.Type.LESS);
        put(">", Token.Type.GREATER);
        put("<=", Token.Type.LESSEQUAL);
        put(">=", Token.Type.GREATEREQUAL);
        put("<>", Token.Type.NOTEQUAL);
        put("var", Token.Type.VAR);
        put("true", Token.Type.TRUE);
        put("false", Token.Type.FALSE);
        put("string", Token.Type.STRING);
        put("boolean", Token.Type.BOOLEAN);
        put("character", Token.Type.CHARACTER);
    }};

    //States
    private final int INITIAL = 0;
    private final int DECIMAL1 = 1;
    private final int NUMBER1 = 2;
    private final int NUMBER2 = 3;
    private final int NUMBER3 = 4;
    private final int SPACE = 5;
    private final int TEXT = 6;
    private final int RIGHTPAREN = 7;
    private final int LEFTPAREN = 8;
    private final int COMMENT = 9;
    private final int SPECIAL = 10;
    private final int STRING = 11;
    private final int CHARACTER = 12;

    /*
        Used to tokenize a line into a consistent readable state.

        Accepts a line and loops through all of its character by
        character and tokenizes it to their corresponding values.
     */
    public List<Token> lex(String line) throws InvalidSyntaxException {
        List<Token> tokenList = new ArrayList<>();
        Pattern numberPattern = Pattern.compile("[0-9]");
        Pattern textPattern = Pattern.compile("[A-Za-z]");
        Pattern specialPattern = Pattern.compile("[-+*/<>;:,=]");
        int state = INITIAL;
        String lexValue = "";

        //If isCommenting is true then it must still be
        //within a comment block
        if(isCommenting) {
            state = COMMENT;
        }

        for(int i = 0; i < line.length(); i++) {
            char currentCharacter = line.charAt(i);

            if(isString) {
                if(currentCharacter != '"')
                    lexValue += currentCharacter;
            } else if(isCharacter) {
                if(currentCharacter != '\'')
                    lexValue += currentCharacter;
            } else if(currentCharacter != ' ') {
                lexValue += currentCharacter;
            }

            switch (state) {
                case INITIAL:
                    switch (currentCharacter) {
                        case ' ':
                            break;
                        case '.':
                            state = DECIMAL1;
                            break;
                        case '(':
                            state = LEFTPAREN;
                            break;
                        case '"':
                            state = STRING;
                            lexValue = "";
                            isString = true;
                            break;
                        case '\'':
                            state = CHARACTER;
                            lexValue = "";
                            isCharacter = true;
                            break;
                        default:
                            if(numberPattern.matcher(currentCharacter + "").matches()) {
                                state = NUMBER1;
                            } else if(textPattern.matcher(currentCharacter + "").matches()) {
                                state = TEXT;
                            } else if(specialPattern.matcher(currentCharacter + "").matches()) {
                                state = SPECIAL;
                            } else {
                                throw new InvalidSyntaxException(currentCharacter + "");
                            }

                            break;
                    }

                    break;

                case DECIMAL1:
                    if(numberPattern.matcher(currentCharacter + "").matches()) {
                        state = NUMBER3;
                    } else {
                        throw new InvalidSyntaxException(currentCharacter + "");
                    }

                    break;

                case NUMBER1:
                    if(numberPattern.matcher(currentCharacter + "").matches()) {
                        break;
                    } else if(currentCharacter == '.') {
                        state = NUMBER3;
                    } else if(currentCharacter == ')') {
                        state = RIGHTPAREN;
                        tokenList.add(new Token(lexValue, Token.Type.NUMBER));
                        lexValue = "";
                    } else if (currentCharacter == ' ') {
                        state = SPACE;
                        tokenList.add(new Token(lexValue, Token.Type.NUMBER));
                        lexValue = "";
                    } else if(specialPattern.matcher(currentCharacter + "").matches()) {
                        state = SPECIAL;
                        tokenList.add(new Token(lexValue.substring(0, lexValue.length() - 1), Token.Type.NUMBER));
                        lexValue = currentCharacter + "";
                    } else {
                        throw new InvalidSyntaxException(currentCharacter + "");
                    }

                    break;

                case NUMBER2:
                    if(numberPattern.matcher(currentCharacter + "").matches()) {
                        state = NUMBER1;
                    } else if(currentCharacter == '.') {
                        state = DECIMAL1;
                    } else{
                        throw new InvalidSyntaxException(currentCharacter + "");
                    }

                    break;

                case NUMBER3:
                    if(numberPattern.matcher(currentCharacter + "").matches()) {
                        break;
                    } else if(currentCharacter == ')') {
                        state = RIGHTPAREN;
                        tokenList.add(new Token(lexValue, Token.Type.NUMBER));
                        lexValue = "";
                    } else if (currentCharacter == ' ') {
                        state = SPACE;
                        tokenList.add(new Token(lexValue, Token.Type.NUMBER));
                        lexValue = "";
                    } else if(specialPattern.matcher(currentCharacter + "").matches()) {
                        tokenList.add(new Token(lexValue.substring(0, lexValue.length() - 1), Token.Type.NUMBER));
                        state = SPECIAL;
                        lexValue = ",";
                    } else {
                        throw new InvalidSyntaxException(currentCharacter + "");
                    }

                    break;

                case SPACE:

                    switch (currentCharacter) {
                        case ' ':
                            break;
                        default:
                            if(textPattern.matcher(currentCharacter + "").matches()) {
                                state = TEXT;
                            } else if(specialPattern.matcher(currentCharacter + "").matches()) {
                                state = SPECIAL;
                            } else if(numberPattern.matcher(currentCharacter + "").matches()) {
                                state = NUMBER1;
                            } else if(currentCharacter == '(') {
                                state = LEFTPAREN;
                            } else if(currentCharacter == ')') {
                                state = RIGHTPAREN;
                            } else if(currentCharacter == '"') {
                                state = STRING;
                                lexValue = "";
                                isString = true;
                            } else if(currentCharacter == '\'') {
                                state = CHARACTER;
                                lexValue = "";
                                isCharacter = true;
                            }

                            break;
                    }
                    break;

                case TEXT:
                    if(textPattern.matcher(currentCharacter + "").matches() || numberPattern.matcher(currentCharacter + "").matches()) {
                        break;
                    } else if (currentCharacter == ' ') {
                        state = SPACE;
                        tokenList.add(getTypeFromMap(lexValue));
                        lexValue = "";
                    } else if(specialPattern.matcher(currentCharacter + "").matches()) {
                        state = SPECIAL;

                        //Remove the special character from the end of the string
                        tokenList.add(getTypeFromMap(lexValue.substring(0, lexValue.length() - 1)));

                        //Reset lexvalue and set it to the special character
                        lexValue = "";
                        lexValue += currentCharacter;
                    } else if(currentCharacter == ')') {
                        state = RIGHTPAREN;
                        tokenList.add(getTypeFromMap(lexValue.substring(0, lexValue.length() - 1)));
                        lexValue = "";
                    } else if(currentCharacter == '(') {
                        state = LEFTPAREN;
                        tokenList.add(getTypeFromMap(lexValue.substring(0, lexValue.length() - 1)));
                        lexValue = "";
                    } else {
                        throw new InvalidSyntaxException(currentCharacter + "");
                    }
                    break;
                case LEFTPAREN:

                    if(textPattern.matcher(currentCharacter + "").matches()) {
                        state = TEXT;
                        tokenList.add(new Token(Token.Type.LEFTPAREN));
                    } else if(numberPattern.matcher(currentCharacter + "").matches()) {
                        state = NUMBER1;
                        tokenList.add(new Token(Token.Type.LEFTPAREN));
                    } else if(currentCharacter == '+' || currentCharacter == '-') {
                        state = NUMBER2;
                        tokenList.add(new Token(Token.Type.LEFTPAREN));
                    } else if(currentCharacter == '.') {
                        state = DECIMAL1;
                        tokenList.add(new Token(Token.Type.LEFTPAREN));
                    } else if (currentCharacter == ' ') {
                        state = SPACE;
                        tokenList.add(new Token(Token.Type.LEFTPAREN));
                    } else if (currentCharacter == '*') {
                        state = COMMENT;
                    } else if (currentCharacter == ')') {
                        state = RIGHTPAREN;
                        tokenList.add(new Token(Token.Type.LEFTPAREN));
                    } else {
                        throw new InvalidSyntaxException();
                    }
                    break;
                case RIGHTPAREN:
                    if(currentCharacter == ' ') {
                        state = SPACE;
                        tokenList.add(new Token(Token.Type.RIGHTPAREN));
                        lexValue = "";
                    } else {
                        throw new InvalidSyntaxException(currentCharacter + "");
                    }
                    break;

                case COMMENT:
                    isCommenting = true;

                    if(lexValue.equals("*)")) {
                        state = INITIAL;
                        lexValue = "";
                        isCommenting = false;
                    }
                    if(currentCharacter != '*') {
                        lexValue = "";
                    }
                    break;

                case SPECIAL:

                    state = INITIAL;
                    if(currentCharacter == '=') {
                        tokenList.add(getTypeFromMap(lexValue));
                        lexValue = "";
                    } else if(currentCharacter == '>') {
                        tokenList.add(getTypeFromMap(lexValue));
                        lexValue = "";
                    } else if(textPattern.matcher(currentCharacter + "").matches()) {
                        state = TEXT;

                        //Remove the special character from the end of the string
                        tokenList.add(getTypeFromMap(lexValue.substring(0, lexValue.length() - 1)));

                        //Reset lexvalue and set it to the special character
                        lexValue = "";
                        lexValue += currentCharacter;
                    } else if(numberPattern.matcher(currentCharacter + "").matches()) {
                        state = NUMBER1;

                        //Remove the special character from the end of the string
                        tokenList.add(getTypeFromMap(lexValue.substring(0, lexValue.length() - 1)));

                        //Reset lexvalue and set it to the special character
                        lexValue = "";
                        lexValue += currentCharacter;
                    } else if(currentCharacter == '+' || currentCharacter == '-') {
                        state = NUMBER2;

                        //Remove the special character from the end of the string
                        tokenList.add(getTypeFromMap(lexValue.substring(0, lexValue.length() - 1)));

                        //Reset lexvalue and set it to the special character
                        lexValue = "";
                        lexValue += currentCharacter;
                    } else if(currentCharacter == '.') {
                        state = DECIMAL1;

                        //Remove the special character from the end of the string
                        tokenList.add(getTypeFromMap(lexValue.substring(0, lexValue.length() - 1)));

                        //Reset lexvalue and set it to the special character
                        lexValue = "";
                        lexValue += currentCharacter;
                    } else if (currentCharacter == ' ') {
                        state = SPACE;
                        tokenList.add(getTypeFromMap(lexValue));
                        lexValue = "";
                    } else if(currentCharacter == '"') {
                        tokenList.add(getTypeFromMap(lexValue.substring(0, lexValue.length() - 1)));
                        state = STRING;
                        isString = true;
                        lexValue = "";
                    } else if(currentCharacter == '\'') {
                        tokenList.add(getTypeFromMap(lexValue.substring(0, lexValue.length() - 1)));
                        state = CHARACTER;
                        isCharacter = true;
                        lexValue = "";
                    }

                    break;

                case STRING:
                    if(currentCharacter == '"') {
                        state = INITIAL;
                        tokenList.add(new Token(lexValue, Token.Type.STRING));
                        isString = false;
                        lexValue = "";
                    }

                    break;

                case CHARACTER:
                    if(currentCharacter == '\'') {
                        state = INITIAL;
                        tokenList.add(new Token(lexValue, Token.Type.CHARACTER));
                        isCharacter = false;
                        oneCharacterFound = false;
                        lexValue = "";
                    } else if(oneCharacterFound) {
                        throw new InvalidSyntaxException();
                    } else {
                        oneCharacterFound = true;
                    }

                    break;
            }
        }

        //Handle which state it finishes on
        switch (state) {
            case NUMBER1:
            case NUMBER3:
                tokenList.add(new Token(lexValue, Token.Type.NUMBER));
                break;
            case TEXT:
                tokenList.add(getTypeFromMap(lexValue));
                break;
            case RIGHTPAREN:
                tokenList.add(new Token(Token.Type.RIGHTPAREN));
                break;
            case COMMENT:
                isCommenting = true;
        }

        //Add the EndOfLine token to the end of each line
        tokenList.add(new Token(Token.Type.EOL));

        return tokenList;
    }

    //Returns a token with its corresponding type from the reservedText hashMap.
    //If the token type is not found then the token is stated to be an identifier.
    public Token getTypeFromMap(String value) {
        Token.Type type = reservedText.get(value);
        if(type != null) {
            return new Token(value, type);
        }

        return new Token(value, Token.Type.IDENTIFIER);
    }
}

/**
 * InvalidSyntaxException Object.
 *
 * Custom Exception thrown if an invalid character is found.
 *
 * @author Tom Kelly
 * @Version 1.0
 */
class InvalidSyntaxException extends Exception {

    public InvalidSyntaxException(String message) {
        super("Invalid syntax found: " + message);
    }
    public InvalidSyntaxException() {
        super("Invalid syntax found");
    }
}