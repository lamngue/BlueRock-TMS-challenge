import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Solve {
    private final int depth;
    private final char[][] grid;

    private Piece[] prioritizePiecesByShape(Piece[] pieces) {
        Arrays.sort(pieces, Comparator.comparingInt(this::countXCells).reversed());
        return pieces;
    }


    private int countXCells(Piece piece) {
        int count = 0;
        for (char[] row : piece.shape) {
            for (char cell : row) {
                if (cell == 'X') {
                    count++;
                }
            }
        }
        return count;
    }


    public Solve(Board board) {
        this.depth = board.getDepth();
        this.grid = board.getGrid();
    }

    private boolean isValidPartialSolution(Board board, Piece piece, int row, int col) { // pruning
        char[][] currentGrid = board.getGrid();
        int depth = board.getDepth();

        for (int i = row; i < row + piece.shape.length; i++) {
            for (int j = col; j < col + piece.shape[0].length; j++) {
                if (i < 0 || i >= currentGrid.length || j < 0 || j >= currentGrid[0].length) {
                    // Skip if the piece exceeds the boundaries of the board
                    return false;
                }
                if (currentGrid[i][j] != '0' && Character.getNumericValue(currentGrid[i][j]) >= depth) {
                    // Skip if the piece overlaps with cells already at or beyond the maximum depth
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid() {
        for (char[] chars : this.grid) {
            for (int j = 0; j < this.grid[0].length; j++) {
                if (chars[j] != '0') {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean solve(Board board, Piece[] pieces, int index) {

        return backtrack(board, prioritizePiecesByShape(pieces), index);
    }
    public boolean backtrack(Board board, Piece[] pieces, int index) {
        if (index == pieces.length) {
            return this.isValid();
        }

        Piece currentPiece = pieces[index];
        int pieceRows = currentPiece.shape.length;
        int pieceCols = currentPiece.shape[0].length;
        for (int row = 0; row < this.grid.length; row += 1) {
            for (int col = 0; col < this.grid[0].length; col += 1) {
                if (row + pieceRows <= board.getGrid().length && col + pieceCols <= board.getGrid()[0].length) {
                    if (board.canPlace(pieces[index], row, col) && isValidPartialSolution(board, pieces[index], row, col)) {
                        if (isCompatibleWithPlacedPieces(board, pieces[index], row, col)) {
                            board.placePiece(pieces[index], row, col, depth);
                            if (backtrack(board, pieces, index + 1)) {
                                return true;
                            }
                            board.removePiece(pieces[index], row, col, depth);
                        }
                    }
                }
            }
        }
        return false;
    }
    private String generateKey(Board board, int index) {
        return index + ":" + Arrays.deepToString(board.getGrid());
    }
    public boolean isCompatibleWithPlacedPieces(Board board, Piece piece, int row, int col) {
        for (Piece placedPiece : board.getPlacedPieces()) {
            if (arePiecesCompatible(piece, row, col, placedPiece, placedPiece.getRowPlacement(), placedPiece.getColPlacement())) {
                return false;
            }
        }
        return true;
    }

    private boolean arePiecesCompatible(Piece piece1, int row1, int col1, Piece piece2, int row2, int col2) {
        char[][] shape1 = piece1.getShape();
        char[][] shape2 = piece2.getShape();

        // Calculate relative positions of the two pieces
        int relRow = row2 - row1;
        int relCol = col2 - col1;

        // Check each cell of shape1 against the corresponding cell in shape2
        for (int i = 0; i < shape1.length; i++) {
            for (int j = 0; j < shape1[0].length; j++) {
                if (isValidCell(i + relRow, j + relCol, shape2.length, shape2[0].length)) {
                    char cell1 = shape1[i][j];
                    char cell2 = shape2[i + relRow][j + relCol];

                    // If both cells are occupied, pieces are not compatible
                    if (cell1 == 'X' && cell2 == 'X') {
                        return false;
                    }
                } else {
                    // If shape1 cell extends beyond the bounds of shape2, pieces are not compatible
                    if (shape1[i][j] == 'X') {
                        return false;
                    }
                }
            }
        }
        // All cells are compatible
        return true;
    }

    private boolean isValidCell(int row, int col, int numRows, int numCols) {
        return row >= 0 && row < numRows && col >= 0 && col < numCols;
    }

}