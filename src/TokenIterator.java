import java.util.Iterator;
import java.util.List;

/**
 * TokenIterator Object.
 *
 *  Allows a List of Tokens to be iterated through easily.
 *
 * @author Tom Kelly
 * @Version 1.0
 */
public class TokenIterator implements Iterator {
    private List<Token> tokenList;

    public TokenIterator(List<Token> tokenList) {
        this.tokenList = tokenList;
    }

    @Override
    public boolean hasNext() {
        return !tokenList.isEmpty();
    }

    @Override
    public Token next() {
        return tokenList.remove(0);
    }

    public Token peek() {
        return tokenList.get(0);
    }

    //Looks into the list removing nothing and searches for a token type before an end of line token
    //If it is there return it else return null
    public Token peekAhead(Token.Type type) {
        for (Token token : tokenList) {
            if (token.getType() == type)
                return token;

            if(token.getType() == Token.Type.EOL)
                return null;
        }

        return null;
    }

    public List<Token> getAll() {
        return this.tokenList;
    }
}
