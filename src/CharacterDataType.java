public class CharacterDataType extends InterpreterDataType {
    private char value;

    public CharacterDataType() {
    }

    public CharacterDataType(char value) {
        this.value = value;
    }

    public char getValue() {
        return this.value;
    }

    @Override
    public String toString() {
        return "" + this.value;
    }

    @Override
    public void fromString(String input) {
        this.value = input.charAt(0);
    }
}
