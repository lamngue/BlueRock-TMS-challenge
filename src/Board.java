import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private final int depth;
    private int[][] grid;
    private List<int[]> placed;
    public int getDepth() {
        return depth;
    }

    public Board(int depth, int m, int n) {
        this.depth = depth;
        this.grid = new int[m][n];
        this.placed = new ArrayList<>(); // coordinates for the pieces
    }


    public int[][] getGrid() {
        return grid;
    }

    public void setGrid(int[][] board) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = board[i][j];
            }
        }
    }

    public synchronized List<int[]> getPlacedCoordinates() {
        return placed;
    }

    public void setPlacedCoordinates(List<int[]> placed) {
        this.placed = placed;
    }

    public void print() {
        for (int[] chars : grid) {
            for (int j = 0; j < grid[0].length; j++) {
                System.out.print(chars[j]);
            }
            System.out.println();
        }
    }

    public boolean canPlace(Piece piece, int row, int col) {

        int m = piece.shape.length;
        int n = piece.shape[0].length;

        return row + m <= grid.length && col + n <= grid[0].length;
    }

    public synchronized void placePiece(Piece piece, int row, int col, int depth) {
        int m = piece.shape.length;
        int n = piece.shape[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (piece.shape[i][j] == 'X') {
                    if (grid[row + i][col + j] == depth - 1) {
                        grid[row + i][col + j] = 0;
                    } else {
                        grid[row + i][col + j]++;
                    }
                }
            }
        }
        placed.add(new int[]{row, col});
    }

    public synchronized void removePiece(Piece piece, int row, int col, int depth) {
        int m = piece.shape.length;
        int n = piece.shape[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (piece.shape[i][j] == 'X') {
                    if (grid[row + i][col + j] == 0) {
                        grid[row + i][col + j] = depth - 1;
                    } else {
                        grid[row + i][col + j]--;
                    }
                }
            }
        }
        placed.remove(placed.size() -  1);
    }

    public Board copy() {
        int depth = this.depth;
        int m = this.grid.length;
        int n = this.grid[0].length;

        // Create a new grid and copy the values from the original grid
        int[][] newGrid = new int[m][n];
        for (int i = 0; i < m; i++) {
            System.arraycopy(this.grid[i], 0, newGrid[i], 0, n);
        }

        // Create a new board with the copied grid
        Board copy = new Board(depth, m, n);
        List<int[]> newPlaced = new ArrayList<>(this.placed.size());
        for (int[] coordinates : this.placed) {
            int row = coordinates[0];
            int col = coordinates[1];
            newPlaced.add(new int[]{row, col});
        }

        // Create a new board with the copied grid and placed array
        copy.setGrid(newGrid);
        copy.setPlacedCoordinates(newPlaced);
        return copy;
    }
}