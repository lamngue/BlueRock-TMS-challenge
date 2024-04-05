import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.List;
public class BoardGame {

    private static void processFileContent(String line1, String line2, String line3) {
        String[] rows = line2.split(",");
        int[][] board = new int[rows.length][rows[0].length()];

        for (int i = 0; i < rows.length; i++) {
            for (int j = 0; j < rows[0].length(); j++) {
                board[i][j] = Integer.parseInt(String.valueOf(rows[i].charAt(j)));
            }
        }

        int depth = Integer.parseInt(line1);

        String[] pieceStrings = line3.split(" ");
        int num_pieces = pieceStrings.length;
        Piece[] pieces = new Piece[num_pieces];

        for (int i = 0; i < num_pieces; i++) {
            String[] n_rows = pieceStrings[i].split(",");
            char[][] piece = new char[n_rows.length][n_rows[0].length()];
            for (int row = 0; row < n_rows.length; row++) {
                for (int col = 0; col < n_rows[0].length(); col++) {
                    piece[row][col] = n_rows[row].charAt(col);
                }
            }
            pieces[i] = new Piece(piece, i);
        }

        int m = board.length;
        int n = board[0].length;

        Board gameBoard = new Board(depth, m, n);
        gameBoard.setGrid(board);
        final Solve solver = new Solve(gameBoard);
        Board updatedBoard = solver.solveParallel(gameBoard, pieces, 0);

        if (updatedBoard != null) {
            List<int[]> placedCoordinates = updatedBoard.getPlacedCoordinates();

            for (int i = 0; i < placedCoordinates.size(); i++) {
                int[] coordinates = placedCoordinates.get(i);
                System.out.println("Piece " + (pieces[i].getIndex() + 1) + " placed at: (" + coordinates[1] + ", " + coordinates[0] + ")");
            }
        } else {
            System.out.println("No solution found.\n");
        }

        System.out.println("-------------------------------------------------");
        System.out.println("-------------------------------------------------");
    }
    public static void main(String[] args) {

        String outputFileName = "src/output/execution_times_model1.txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(outputFileName, true))) {
            // Process input files from "00.txt" to "09.txt"
            for (int i = 0; i <= 9; i++) {
                // Construct file path
                String fileNumber = String.format("%02d", i);
                String file_path = "src/test_cases/" + fileNumber + ".txt";

                long startTime = System.currentTimeMillis();

                try {
                    FileReader fileReader = new FileReader(file_path);
                    BufferedReader bufferedReader = new BufferedReader(fileReader);

                    String line1 = bufferedReader.readLine();
                    String line2 = bufferedReader.readLine();
                    String line3 = bufferedReader.readLine();
                    processFileContent(line1, line2, line3);
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                long endTime = System.currentTimeMillis();

                long executionTime = endTime - startTime;
                double executionTimeMinutes = (double) executionTime / 60000;

                // Append execution time to output file
                writer.println("File: " + fileNumber + ".txt, Execution time: " + executionTimeMinutes + " minutes\n");
                System.out.println("File: " + fileNumber + ".txt, Execution time: " + executionTimeMinutes + " minutes");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}