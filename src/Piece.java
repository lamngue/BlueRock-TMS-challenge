public class Piece {
    char[][] shape;

    Piece(char[][] shape) {
        this.shape = shape;
    }

    private int calculateCoverageArea(Piece piece) {
        int area = 0;
        for (int i = 0; i < piece.shape.length; i++) {
            for (int j = 0; j < piece.shape[0].length; j++) {
                if (piece.shape[i][j] != '.') {
                    area++; // Increment area for each non-empty cell
                }
            }
        }
        return area;
    }
    public void print() {
        for (char[] chars : shape) {
            for (int j = 0; j < shape[0].length; j++) {
                System.out.print(chars[j]);
            }
            System.out.println();
        }
    }
}
