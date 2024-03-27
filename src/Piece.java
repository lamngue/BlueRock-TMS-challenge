public class Piece {
    char[][] shape;

    Piece(char[][] shape) {
        this.shape = shape;
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
