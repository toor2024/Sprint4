import java.util.List;
import java.util.Random;

/**
 * Automated game mode where the computer plays by selecting random valid moves.
 * Extends Game with the ability to automatically make moves.
 */
public class AutomatedGame extends Game {

    private final Random random = new Random();

    public AutomatedGame() {
        super();
    }

    /**
     * Makes one automated move by selecting a random valid move.
     * Returns the move that was made, or null if no moves are available.
     */
    public Move autoplayMove() {
        if (board == null || gameOver) return null;

        List<Move> moves = getValidMoves();
        if (moves.isEmpty()) {
            checkGameOver();
            return null;
        }

        Move move = moves.get(random.nextInt(moves.size()));
        makeMove(move.getFromRow(), move.getFromCol(),
                 move.getToRow(), move.getToCol());
        return move;
    }
}
