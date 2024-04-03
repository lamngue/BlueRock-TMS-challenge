import java.util.*;

public class Solve {
    private final int depth;
    private final char[][] grid;
    private final Map<String, Boolean> memoization; // Memoization table

    // Method to prioritize pieces based on their shape area
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
        this.memoization =  new LinkedHashMap<String, Boolean>(1000, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Boolean> eldest) {
                return size() > 1000; // Adjust the cache size as needed
            }
        };
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

    private boolean isValidPartialSolution(Board board) { // pruning
        char[][] currentGrid = board.getGrid();
        int depth = board.getDepth();

        // Check if any cell has a value greater than the depth
        for (char[] row : currentGrid) {
            for (char cell : row) {
                if (cell != '0' && Character.getNumericValue(cell) >= depth) {
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
        if (!isValidPartialSolution(board)) {
            System.out.println("Invalid partial solution");
            return false;
        }
        String key = generateKey(board, pieces, index);
        if (memoization.containsKey(key)) {
            System.out.println("Key exists");
            return memoization.get(key); // Return memoized result
        }
        for (int row = 0; row < board.getGrid().length; row++) {
            for (int col = 0; col < board.getGrid()[0].length; col++) {
                if (board.canPlace(pieces[index], row, col)) {
                    board.placePiece(pieces[index], row, col, depth);
                    if (backtrack(board, pieces, index + 1)) {
                        memoization.put(key, true);
                        return true;
                    }
                    board.removePiece(pieces[index], row, col, depth);
                }
            }
        }
        memoization.put(key, false);
        return false;
    }
    private String generateKey(Board board, Piece[] pieces, int index) {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(index).append(":").append(Arrays.deepToString(board.getGrid()));
        return keyBuilder.toString();
    }
}
