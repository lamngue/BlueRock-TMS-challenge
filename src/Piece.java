public class Piece {
    private final int index;
    private int rowPlacement;

    private int colPlacement;

    char[][] shape;

    public void setShape(char[][] shape) {
        this.shape = shape;
    }

    public Piece(char[][] shape, int index, int rowPlacement, int colPlacement) {
        this.index = index;
        this.shape = shape;
        this.rowPlacement = rowPlacement;
        this.colPlacement = colPlacement;
    }

    public int getRowPlacement() {
        return rowPlacement;
    }

    public int getColPlacement() {
        return colPlacement;
    }

    public char[][] getShape() {
        return shape;
    }

    public int getIndex() {
        return index;
    }

    public void setRowPlacement(int rowPlacement) {
        this.rowPlacement = rowPlacement;
    }

    public void setColPlacement(int colPlacement) {
        this.colPlacement = colPlacement;
    }

}
