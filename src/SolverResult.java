public class SolverResult {
    private boolean solutionFound;
    private Board updatedBoard;

    public SolverResult(boolean solutionFound, Board updatedBoard) {
        this.solutionFound = solutionFound;
        this.updatedBoard = updatedBoard;
    }

    public boolean isSolutionFound() {
        return solutionFound;
    }

    public Board getUpdatedBoard() {
        return updatedBoard;
    }
}
