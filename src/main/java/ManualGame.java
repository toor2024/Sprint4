import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Manual game mode where the player makes moves by clicking pegs.
 * Extends Game with the ability to randomize the board state.
 */
public class ManualGame extends Game {

    private final Random random = new Random();

    public ManualGame() {
        super();
    }

    /**
     * Randomizes the positions of pegs and empty holes on the board.
     * Preserves the board shape (invalid cells stay invalid).
     * Ensures at least one valid move exists after randomization.
     */
    public void randomize() {
        if (board == null || gameOver) return;

        int size = board.getSize();
        List<int[]> validCells = new ArrayList<>();

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (board.isValidCell(r, c)) {
                    validCells.add(new int[]{r, c});
                }
            }
        }

        // Try randomizing until we get a state with valid moves
        int attempts = 0;
        while (attempts < 100) {
            // Randomly assign PEG or EMPTY to each valid cell
            for (int[] cell : validCells) {
                board.setCellState(cell[0], cell[1],
                    random.nextBoolean() ? Board.PEG : Board.EMPTY);
            }

            // Ensure at least one peg and one empty exist
            if (board.getPegCount() == 0) {
                int[] cell = validCells.get(random.nextInt(validCells.size()));
                board.setCellState(cell[0], cell[1], Board.PEG);
            }
            if (board.getPegCount() == validCells.size()) {
                int[] cell = validCells.get(random.nextInt(validCells.size()));
                board.setCellState(cell[0], cell[1], Board.EMPTY);
            }

            if (!getValidMoves().isEmpty()) {
                break;
            }
            attempts++;
        }

        gameOver = false;
        won = false;
    }
}
