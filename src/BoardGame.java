import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

public class BoardGame extends JFrame implements ActionListener {
    JButton btn;
    JTextArea textArea;

    public BoardGame() {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("BlueRock TMS challenge");
        this.setLayout(new BorderLayout());

        btn = new JButton("Upload File");
        btn.setFocusable(false);
        btn.addActionListener(this);
        this.add(btn, BorderLayout.NORTH);

        textArea = new JTextArea(20, 30);
        JScrollPane scrollPane = new JScrollPane(textArea);
        this.add(scrollPane, BorderLayout.CENTER);

        this.pack();
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btn) {
            JFileChooser file_upload = new JFileChooser();
            file_upload.setCurrentDirectory(new File("./src/test_cases"));
            int res = file_upload.showOpenDialog(null);
            if (res == JFileChooser.APPROVE_OPTION) {
                File file_path = new File(file_upload.getSelectedFile().getAbsolutePath());
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
            }
        }
    }

    public char[][] displayBoardAfterPuttingPiece(char[][] board, Piece piece, int row, int col, int depth) {
        int m = piece.shape.length;
        int n = piece.shape[0].length;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (piece.shape[i][j] == 'X') {
                    if (Character.getNumericValue(board[row + i][col + j]) == depth - 1) {
                        board[row + i][col + j] = '0';
                    } else {
                        char newVal = String.valueOf(Character.getNumericValue(board[row + i][col + j]) + 1).charAt(0);
                        board[row + i][col + j] = newVal;
                    }
                }
            }
        }
        return board;
    }

    private void appendBoard(StringBuilder builder, char[][] board) {
        for (char[] row : board) {
            for (char cell : row) {
                builder.append(cell);
            }
            builder.append("\n");
        }
    }

    private void processFileContent(String line1, String line2, String line3) {
        String[] rows = line2.split(",");
        char[][] board = new char[rows.length][rows[0].length()];

        for (int i = 0; i < rows.length; i++) {
            for (int j = 0; j < rows[0].length(); j++) {
                board[i][j] = rows[i].charAt(j);
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
            pieces[i] = new Piece(piece);
        }

        int m = board.length;
        int n = board[0].length;

        Board gameBoard = new Board(depth, m, n);
        gameBoard.setGrid(board);
        final Solve solver = new Solve(gameBoard);
        StringBuilder res = new StringBuilder();
        int idx_piece = 0;
        char[][] cur_board = board;
        StringBuilder board_stage = new StringBuilder();
        board_stage.append("Initial Board").append("\n");
        appendBoard(board_stage, board);

        if (solver.solve(gameBoard, pieces, 0)) {
            res.append("Output coordinates: ").append("\n");
            for (int[] coordinates : gameBoard.getPlacedCoordinates()) {
                cur_board = displayBoardAfterPuttingPiece(cur_board, pieces[idx_piece], coordinates[0], coordinates[1], depth);
                board_stage.append("Board after putting piece ").append(idx_piece + 1).append(" at ").append(coordinates[1]).append(",").append(coordinates[0]).append("\n");
                appendBoard(board_stage, board);
                res.append(coordinates[1]).append(",").append(coordinates[0]).append(" ");
                idx_piece++;
            }
            textArea.append(board_stage.toString());
            textArea.append(res + "\n");
        } else {
            textArea.append("No solution found.\n");
        }
        textArea.append("-----------------------------------------------------\n");
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(BoardGame::new);
    }
}