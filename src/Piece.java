public class Piece {
    private final int index;

    char[][] shape;

    public Piece(char[][] shape, int index) {
        this.index = index;
        this.shape = shape;
    }


    public int getIndex() {
        return index;
    }

}
