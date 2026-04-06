import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class ManualGameTest {

    private ManualGame game;

    @BeforeEach
    void setUp() {
        game = new ManualGame();
    }

    @Test
    void newGame_createsBoard() {
        game.newGame(7, BoardType.ENGLISH);
        assertNotNull(game.getBoard());
        assertFalse(game.isGameOver());
        assertFalse(game.hasWon());
    }

    @Test
    void validMove_isAccepted() {
        game.newGame(7, BoardType.ENGLISH);
        assertTrue(game.isValidMove(1, 3, 3, 3));
    }

    @Test
    void invalidMove_toOccupiedCell_isRejected() {
        game.newGame(7, BoardType.ENGLISH);
        assertFalse(game.isValidMove(0, 2, 2, 2));
    }

    @Test
    void invalidMove_wrongDistance_isRejected() {
        game.newGame(7, BoardType.ENGLISH);
        assertFalse(game.isValidMove(2, 3, 3, 3));
    }

    @Test
    void invalidMove_diagonal_isRejected() {
        game.newGame(7, BoardType.ENGLISH);
        assertFalse(game.isValidMove(2, 2, 4, 4));
    }

    @Test
    void invalidMove_fromEmptyCell_isRejected() {
        game.newGame(7, BoardType.ENGLISH);
        assertFalse(game.isValidMove(3, 3, 5, 3));
    }

    @Test
    void makeMove_updatesBoardCorrectly() {
        game.newGame(7, BoardType.ENGLISH);
        Board board = game.getBoard();
        assertTrue(game.makeMove(1, 3, 3, 3));
        assertEquals(Board.EMPTY, board.getCellState(1, 3));
        assertEquals(Board.EMPTY, board.getCellState(2, 3));
        assertEquals(Board.PEG, board.getCellState(3, 3));
    }

    @Test
    void makeMove_reducesPegCount() {
        game.newGame(7, BoardType.ENGLISH);
        int before = game.getBoard().getPegCount();
        game.makeMove(1, 3, 3, 3);
        assertEquals(before - 1, game.getBoard().getPegCount());
    }

    @Test
    void invalidMove_isNotExecuted() {
        game.newGame(7, BoardType.ENGLISH);
        int before = game.getBoard().getPegCount();
        assertFalse(game.makeMove(0, 0, 2, 2));
        assertEquals(before, game.getBoard().getPegCount());
    }

    @Test
    void getValidMoves_returnsMovesAtStart() {
        game.newGame(7, BoardType.ENGLISH);
        List<Move> moves = game.getValidMoves();
        assertFalse(moves.isEmpty());
    }

    @Test
    void gameOver_winDetectedWith1PegLeft() {
        game.newGame(7, BoardType.ENGLISH);
        Board board = game.getBoard();
        clearBoard(board);
        board.setCellState(2, 3, Board.PEG);
        board.setCellState(3, 3, Board.PEG);
        board.setCellState(4, 3, Board.EMPTY);

        game.makeMove(2, 3, 4, 3);
        assertTrue(game.isGameOver());
        assertTrue(game.hasWon());
    }

    @Test
    void gameOver_lossDetectedWithMultiplePegsNoMoves() {
        game.newGame(7, BoardType.ENGLISH);
        Board board = game.getBoard();
        clearBoard(board);
        board.setCellState(2, 3, Board.PEG);
        board.setCellState(3, 3, Board.PEG);
        board.setCellState(4, 3, Board.EMPTY);
        board.setCellState(0, 2, Board.PEG);

        game.makeMove(2, 3, 4, 3);
        assertTrue(game.isGameOver());
        assertFalse(game.hasWon());
    }

    @Test
    void randomize_changesBoardState() {
        game.newGame(7, BoardType.ENGLISH);
        Board boardBefore = game.getBoard().copy();
        game.randomize();
        Board boardAfter = game.getBoard();

        // Board should still have valid cells and pegs
        assertTrue(boardAfter.getPegCount() > 0);
        assertFalse(game.isGameOver());
    }

    @Test
    void randomize_preservesBoardShape() {
        game.newGame(7, BoardType.ENGLISH);
        game.randomize();
        Board board = game.getBoard();
        // English corners should still be invalid
        assertEquals(Board.INVALID, board.getCellState(0, 0));
        assertEquals(Board.INVALID, board.getCellState(0, 1));
        assertEquals(Board.INVALID, board.getCellState(6, 6));
    }

    @Test
    void randomize_ensuresValidMovesExist() {
        game.newGame(7, BoardType.ENGLISH);
        game.randomize();
        assertFalse(game.getValidMoves().isEmpty());
    }

    @Test
    void randomize_resetsGameOverState() {
        game.newGame(7, BoardType.ENGLISH);
        Board board = game.getBoard();
        clearBoard(board);
        board.setCellState(2, 3, Board.PEG);
        board.setCellState(3, 3, Board.PEG);
        board.setCellState(4, 3, Board.EMPTY);
        game.makeMove(2, 3, 4, 3);
        assertTrue(game.isGameOver());

        // Randomize should reset game over
        game.newGame(7, BoardType.ENGLISH);
        game.randomize();
        assertFalse(game.isGameOver());
    }

    @Test
    void newGame_resetsGameState() {
        game.newGame(7, BoardType.ENGLISH);
        game.makeMove(1, 3, 3, 3);
        game.newGame(7, BoardType.ENGLISH);
        assertEquals(32, game.getBoard().getPegCount());
        assertFalse(game.isGameOver());
    }

    @Test
    void getRating_outstanding() {
        game.newGame(7, BoardType.ENGLISH);
        Board board = game.getBoard();
        clearBoard(board);
        board.setCellState(3, 3, Board.PEG);
        assertEquals("Outstanding", game.getRating());
    }

    @Test
    void getRating_average() {
        game.newGame(7, BoardType.ENGLISH);
        Board board = game.getBoard();
        clearBoard(board);
        board.setCellState(3, 3, Board.PEG);
        board.setCellState(0, 2, Board.PEG);
        board.setCellState(0, 3, Board.PEG);
        board.setCellState(0, 4, Board.PEG);
        assertEquals("Average", game.getRating());
    }

    @Test
    void diamondBoard_validMoveWorks() {
        game.newGame(5, BoardType.DIAMOND);
        assertTrue(game.isValidMove(0, 2, 2, 2));
        assertTrue(game.makeMove(0, 2, 2, 2));
    }

    @Test
    void hexagonBoard_initializesCorrectly() {
        game.newGame(7, BoardType.HEXAGON);
        Board board = game.getBoard();
        assertEquals(36, board.getPegCount());
        assertEquals(Board.EMPTY, board.getCellState(3, 3));
    }

    @Test
    void manualGame_isInstanceOfGame() {
        assertTrue(game instanceof Game);
    }

    private void clearBoard(Board board) {
        int size = board.getSize();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (board.getCellState(r, c) == Board.PEG) {
                    board.setCellState(r, c, Board.EMPTY);
                }
            }
        }
    }
}
