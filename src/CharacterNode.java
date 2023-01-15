public class CharacterNode extends Node {
    private char value;

    public CharacterNode(char value) {
        this.value = value;
    }

    public char getValue() {
        return this.value;
    }

    public void setValue(char value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Character(" + this.value + ")";
    }
}
