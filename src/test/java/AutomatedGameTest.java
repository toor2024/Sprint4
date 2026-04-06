import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AutomatedGameTest {

    private AutomatedGame game;

    @BeforeEach
    void setUp() {
        game = new AutomatedGame();
    }

    @Test
    void newGame_createsBoard() {
        game.newGame(7, BoardType.ENGLISH);
        assertNotNull(game.getBoard());
        assertFalse(game.isGameOver());
        assertFalse(game.hasWon());
    }

    @Test
    void autoplayMove_makesOneMove() {
        game.newGame(7, BoardType.ENGLISH);
        int before = game.getBoard().getPegCount();
        Move move = game.autoplayMove();
        assertNotNull(move);
        assertEquals(before - 1, game.getBoard().getPegCount());
    }

    @Test
    void autoplayMove_returnsValidMove() {
        game.newGame(7, BoardType.ENGLISH);
        Move move = game.autoplayMove();
        assertNotNull(move);
        // The destination should now have a peg
        assertEquals(Board.PEG, game.getBoard().getCellState(
            move.getToRow(), move.getToCol()));
        // The source should now be empty
        assertEquals(Board.EMPTY, game.getBoard().getCellState(
            move.getFromRow(), move.getFromCol()));
        // The jumped cell should now be empty
        assertEquals(Board.EMPTY, game.getBoard().getCellState(
            move.getJumpedRow(), move.getJumpedCol()));
    }

    @Test
    void autoplayMove_returnsNullWhenGameOver() {
        game.newGame(7, BoardType.ENGLISH);
        Board board = game.getBoard();
        int size = board.getSize();
        // Clear board and set up game-over state
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (board.getCellState(r, c) == Board.PEG) {
                    board.setCellState(r, c, Board.EMPTY);
                }
            }
        }
        // Place one isolated peg — no moves possible
        board.setCellState(3, 3, Board.PEG);

        Move move = game.autoplayMove();
        assertNull(move);
    }

    @Test
    void autoplayUntilGameOver_endsGame() {
        game.newGame(7, BoardType.ENGLISH);
        int maxMoves = 100;
        int moves = 0;
        while (!game.isGameOver() && moves < maxMoves) {
            game.autoplayMove();
            moves++;
        }
        assertTrue(game.isGameOver());
        assertTrue(game.getBoard().getPegCount() >= 1);
    }

    @Test
    void autoplayUntilGameOver_pegCountDecreases() {
        game.newGame(7, BoardType.ENGLISH);
        int initialPegs = game.getBoard().getPegCount();
        while (!game.isGameOver()) {
            game.autoplayMove();
        }
        assertTrue(game.getBoard().getPegCount() < initialPegs);
    }

    @Test
    void autoplayMove_reducesPegCountByOne() {
        game.newGame(7, BoardType.ENGLISH);
        int before = game.getBoard().getPegCount();
        game.autoplayMove();
        assertEquals(before - 1, game.getBoard().getPegCount());
    }

    @Test
    void autoplay_worksOnDiamondBoard() {
        game.newGame(5, BoardType.DIAMOND);
        Move move = game.autoplayMove();
        assertNotNull(move);
        assertEquals(11, game.getBoard().getPegCount());
    }

    @Test
    void autoplay_worksOnHexagonBoard() {
        game.newGame(7, BoardType.HEXAGON);
        Move move = game.autoplayMove();
        assertNotNull(move);
        assertEquals(35, game.getBoard().getPegCount());
    }

    @Test
    void autoplay_completesGameOnAllBoardTypes() {
        for (BoardType type : BoardType.values()) {
            game.newGame(7, type);
            while (!game.isGameOver()) {
                game.autoplayMove();
            }
            assertTrue(game.isGameOver());
        }
    }

    @Test
    void automatedGame_isInstanceOfGame() {
        assertTrue(game instanceof Game);
    }

    @Test
    void newGame_resetsGameState() {
        game.newGame(7, BoardType.ENGLISH);
        while (!game.isGameOver()) {
            game.autoplayMove();
        }
        assertTrue(game.isGameOver());
        game.newGame(7, BoardType.ENGLISH);
        assertFalse(game.isGameOver());
        assertEquals(32, game.getBoard().getPegCount());
    }

    @Test
    void getRating_afterAutoplay() {
        game.newGame(7, BoardType.ENGLISH);
        while (!game.isGameOver()) {
            game.autoplayMove();
        }
        String rating = game.getRating();
        assertNotNull(rating);
        assertFalse(rating.isEmpty());
    }
}
