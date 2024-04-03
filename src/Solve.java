import java.util.*;

public class Solve {
    private final int depth;
    private final char[][] grid;

    // Method to prioritize pieces based on their shape
    private Piece[] prioritizePiecesByShape(Piece[] pieces) {
        Arrays.sort(pieces, Comparator.comparingInt(this::calculateCoverageArea).reversed());
        return pieces;
    }

    // Method to calculate the coverage area of a piece
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

    // Inner class to store piece and its coverage area
    private class PieceCoverage implements Comparable<PieceCoverage> {
        Piece piece;
        int coverageArea;

        PieceCoverage(Piece piece, int coverageArea) {
            this.piece = piece;
            this.coverageArea = coverageArea;
        }

        @Override
        public int compareTo(PieceCoverage other) {
            // Compare based on coverage area
            return Integer.compare(this.coverageArea, other.coverageArea);
        }
    }

    public Solve(Board board) {
        this.depth = board.getDepth();
        this.grid = board.getGrid();
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
        Piece[] prioritizedPieces = prioritizePiecesByShape(pieces);

        return backtrack(board, prioritizedPieces, 0);
    }

    private boolean backtrack(Board board, Piece[] pieces, int index) {
        if (index == pieces.length) {
            return this.isValid();
        }

        for (int row = 0; row < board.getGrid().length; row++) {
            for (int col = 0; col < board.getGrid()[0].length; col++) {
                if (board.canPlace(pieces[index], row, col)) {
                    board.placePiece(pieces[index], row, col, depth);
                    if (backtrack(board, pieces, index + 1)) {
                        return true;
                    }
                    board.removePiece(pieces[index], row, col, depth);
                }
            }
        }

        return false;
    }
//    public boolean isValidPartialSolution(Board board) {
//        char[][] grid = board.getGrid();
//        int depth = board.getDepth();
//
//        for (int[] coords : board.getPlacedCoordinates()) {
//            int row = coords[0];
//            int col = coords[1];
//            if (Character.getNumericValue(grid[row][col]) > depth - 1) {
//                return false;
//            }
//        }
//        return true;
//    }
}
