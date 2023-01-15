/**
 * Token Object.
 *
 * Token Object contains a String as well as an enum
 * defining what type of Token it is. This is used
 * as an object to hold data of parsed syntax, so it
 * can be easily understood by another program
 * later on.
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class Token {
    private String value;
    private Type type;

    public enum Type {
        NUMBER,
        PLUS,
        MINUS,
        DIVIDE,
        TIMES,
        LEFTPAREN,
        RIGHTPAREN,
        EOL,
        IDENTIFIER,
        DEFINE,
        INTEGER,
        REAL,
        BEGIN,
        END,
        SEMICOLON,
        COLON,
        EQUALS,
        COMMA,
        VARIABLES,
        CONSTANTS,
        ASSIGN,
        IF,
        THEN,
        ELSE,
        ELSEIF,
        FOR,
        FROM,
        TO,
        WHILE,
        REPEAT,
        UNTIL,
        MOD,
        LESS,
        GREATER,
        LESSEQUAL,
        GREATEREQUAL,
        NOTEQUAL,
        VAR,
        STRING,
        CHARACTER,
        BOOLEAN,
        TRUE,
        FALSE
    }

    public Token(String value, Type type) {
        this.value = value;
        this.type = type;
    }

    public Token(Type type) {
        this.type = type;
        this.value = "";
    }

    public Token.Type getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    public String toString() {
        //Based on whether its Type is NUMBER or EOL to display
        //the corresponding text and make it readable by the user
        if(type == Type.NUMBER || type == Type.IDENTIFIER || type == Type.STRING || type == Type.CHARACTER) {
            return this.type + "(" + this.value + ")";
        } else if(type == Type.EOL) {
            return "EndOfLine";
        }

        return this.type + "";
    }
}
