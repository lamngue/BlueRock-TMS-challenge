import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Solve {
    private final int depth;
    private final int[][] grid;

    private final ExecutorService executor;
    private final int numThreads = 1;

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
        this.executor = Executors.newFixedThreadPool(numThreads);
    }

    public Board solveParallel(Board board, Piece[] pieces, int index) {
        List<Future<SolverResult>> futures = new ArrayList<>();
        Piece[] piecesPrioritized = prioritizePiecesByShape(pieces);

        for (int i = 0; i < numThreads; i++) {
            Board copyOfBoard = board.copy();
            Future<SolverResult> future = executor.submit(() -> {
                boolean result = backtrack(copyOfBoard, piecesPrioritized, index);
                return new SolverResult(result, copyOfBoard);
            });
            futures.add(future);
        }

        boolean solutionFound = false;
        Board updatedBoard = null;

        for (Future<SolverResult> future : futures) {
            try {
                SolverResult result = future.get();
                if (result.isSolutionFound()) {
                    solutionFound = true;
                    updatedBoard = result.getUpdatedBoard();
                    break; // Stop processing other threads if a solution is found
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return solutionFound ? updatedBoard : null;
    }

    private boolean isValidPartialSolution(Board board, Piece piece, int row, int col) { // pruning
        int[][] currentGrid = board.getGrid();
        int depth = board.getDepth();

        for (int i = row; i < row + piece.shape.length; i++) {
            for (int j = col; j < col + piece.shape[0].length; j++) {
                if (i < 0 || i >= currentGrid.length || j < 0 || j >= currentGrid[0].length) {
                    return false;
                }
                if (currentGrid[i][j] != 0 && currentGrid[i][j] >= depth) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(Board board) {
        int[][] grid  = board.getGrid();
        for (int[] chars : grid) {
            for (int j = 0; j < grid[0].length; j++) {
                if (chars[j] != 0) {
                    return false;
                }
            }
        }
        return true;
    }
    public boolean backtrack(Board board, Piece[] pieces, int index) {
        if (index == pieces.length) {
            return this.isValid(board);
        }

        Piece currentPiece = pieces[index];
        int pieceRows = currentPiece.shape.length;
        int pieceCols = currentPiece.shape[0].length;
        int numRows = board.getGrid().length;
        int numCols = board.getGrid()[0].length;
        for (int row = 0; row < numRows - pieceRows + 1; row++) {
            for (int col = 0; col < numCols - pieceCols + 1; col++) {
                if (row + pieceRows <= board.getGrid().length && col + pieceCols <= board.getGrid()[0].length) {
                    if (board.canPlace(pieces[index], row, col) && isValidPartialSolution(board, pieces[index], row, col)) {
                        board.placePiece(pieces[index], row, col, depth);
                        if (backtrack(board, pieces, index + 1)) {
                            return true;
                        }
                        board.removePiece(pieces[index], row, col, depth);
                    }
                }
            }
        }
        return false;
    }
}