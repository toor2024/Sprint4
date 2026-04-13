import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class GameRecorderTest {

    private GameRecorder recorder;

    @BeforeEach
    void setUp() {
        recorder = new GameRecorder();
    }

    @Test
    void startRecording_setsRecordingTrue() {
        recorder.startRecording(7, BoardType.ENGLISH, "MANUAL");
        assertTrue(recorder.isRecording());
    }

    @Test
    void stopRecording_setsRecordingFalse() {
        recorder.startRecording(7, BoardType.ENGLISH, "MANUAL");
        recorder.stopRecording();
        assertFalse(recorder.isRecording());
    }

    @Test
    void newRecorder_isNotRecording() {
        assertFalse(recorder.isRecording());
    }

    @Test
    void recordMove_addsEventWhenRecording() {
        recorder.startRecording(7, BoardType.ENGLISH, "MANUAL");
        recorder.recordMove(1, 3, 3, 3);
        List<String> events = recorder.getEvents();
        assertEquals(1, events.size());
        assertEquals("MOVE:1,3,3,3", events.get(0));
    }

    @Test
    void recordMove_ignoredWhenNotRecording() {
        recorder.recordMove(1, 3, 3, 3);
        assertTrue(recorder.getEvents().isEmpty());
    }

    @Test
    void recordMultipleMoves_addsAllEvents() {
        recorder.startRecording(7, BoardType.ENGLISH, "MANUAL");
        recorder.recordMove(1, 3, 3, 3);
        recorder.recordMove(3, 1, 3, 3);
        recorder.recordMove(5, 3, 3, 3);
        assertEquals(3, recorder.getEvents().size());
    }

    @Test
    void recordRandomize_addsEventWithBoardState() {
        recorder.startRecording(7, BoardType.ENGLISH, "MANUAL");
        Board board = new Board(7, BoardType.ENGLISH);
        recorder.recordRandomize(board);
        List<String> events = recorder.getEvents();
        assertEquals(1, events.size());
        assertTrue(events.get(0).startsWith("RANDOMIZE:"));
    }

    @Test
    void recordRandomize_ignoredWhenNotRecording() {
        Board board = new Board(7, BoardType.ENGLISH);
        recorder.recordRandomize(board);
        assertTrue(recorder.getEvents().isEmpty());
    }

    @Test
    void startRecording_storesBoardTypeAndSize() {
        recorder.startRecording(7, BoardType.HEXAGON, "MANUAL");
        assertEquals(BoardType.HEXAGON, recorder.getBoardType());
        assertEquals(7, recorder.getBoardSize());
        assertEquals("MANUAL", recorder.getGameMode());
    }

    @Test
    void startRecording_clearsOldEvents() {
        recorder.startRecording(7, BoardType.ENGLISH, "MANUAL");
        recorder.recordMove(1, 3, 3, 3);
        recorder.startRecording(5, BoardType.DIAMOND, "AUTOMATED");
        assertTrue(recorder.getEvents().isEmpty());
    }

    @Test
    void saveAndLoad_preservesBoardConfig(@TempDir Path tempDir) throws IOException {
        recorder.startRecording(7, BoardType.ENGLISH, "MANUAL");
        recorder.recordMove(1, 3, 3, 3);
        recorder.recordMove(3, 1, 3, 3);

        String filePath = tempDir.resolve("test_game.txt").toString();
        recorder.saveToFile(filePath);

        GameRecorder loaded = GameRecorder.loadFromFile(filePath);
        assertEquals(BoardType.ENGLISH, loaded.getBoardType());
        assertEquals(7, loaded.getBoardSize());
        assertEquals("MANUAL", loaded.getGameMode());
    }

    @Test
    void saveAndLoad_preservesMoveEvents(@TempDir Path tempDir) throws IOException {
        recorder.startRecording(7, BoardType.ENGLISH, "MANUAL");
        recorder.recordMove(1, 3, 3, 3);
        recorder.recordMove(3, 1, 3, 3);

        String filePath = tempDir.resolve("test_game.txt").toString();
        recorder.saveToFile(filePath);

        GameRecorder loaded = GameRecorder.loadFromFile(filePath);
        List<String> events = loaded.getEvents();
        assertEquals(2, events.size());
        assertEquals("MOVE:1,3,3,3", events.get(0));
        assertEquals("MOVE:3,1,3,3", events.get(1));
    }

    @Test
    void saveAndLoad_preservesRandomizeEvent(@TempDir Path tempDir) throws IOException {
        recorder.startRecording(7, BoardType.ENGLISH, "MANUAL");
        Board board = new Board(7, BoardType.ENGLISH);
        recorder.recordRandomize(board);

        String filePath = tempDir.resolve("test_game.txt").toString();
        recorder.saveToFile(filePath);

        GameRecorder loaded = GameRecorder.loadFromFile(filePath);
        List<String> events = loaded.getEvents();
        assertEquals(1, events.size());
        assertTrue(events.get(0).startsWith("RANDOMIZE:"));
    }

    @Test
    void parseMove_createsCorrectMoveObject() {
        Move move = GameRecorder.parseMove("MOVE:1,3,3,3");
        assertEquals(1, move.getFromRow());
        assertEquals(3, move.getFromCol());
        assertEquals(3, move.getToRow());
        assertEquals(3, move.getToCol());
        assertEquals(2, move.getJumpedRow());
        assertEquals(3, move.getJumpedCol());
    }

    @Test
    void applyRandomizeEvent_restoresBoardState() {
        Board original = new Board(7, BoardType.ENGLISH);
        // Modify board state
        original.setCellState(0, 2, Board.EMPTY);
        original.setCellState(0, 3, Board.EMPTY);
        original.setCellState(3, 3, Board.PEG);

        // Record the state
        recorder.startRecording(7, BoardType.ENGLISH, "MANUAL");
        recorder.recordRandomize(original);
        String event = recorder.getEvents().get(0);

        // Create a fresh board and apply the recorded state
        Board fresh = new Board(7, BoardType.ENGLISH);
        GameRecorder.applyRandomizeEvent(event, fresh);

        // Verify the states match for valid cells
        for (int r = 0; r < 7; r++) {
            for (int c = 0; c < 7; c++) {
                if (original.isValidCell(r, c)) {
                    assertEquals(original.getCellState(r, c),
                        fresh.getCellState(r, c),
                        "Cell (" + r + "," + c + ") mismatch");
                }
            }
        }
    }

    @Test
    void saveAndLoad_automatedGame(@TempDir Path tempDir) throws IOException {
        recorder.startRecording(5, BoardType.DIAMOND, "AUTOMATED");
        recorder.recordMove(0, 2, 2, 2);
        recorder.recordMove(2, 0, 2, 2);

        String filePath = tempDir.resolve("auto_game.txt").toString();
        recorder.saveToFile(filePath);

        GameRecorder loaded = GameRecorder.loadFromFile(filePath);
        assertEquals(BoardType.DIAMOND, loaded.getBoardType());
        assertEquals(5, loaded.getBoardSize());
        assertEquals("AUTOMATED", loaded.getGameMode());
        assertEquals(2, loaded.getEvents().size());
    }

    @Test
    void saveAndLoad_mixedEvents(@TempDir Path tempDir) throws IOException {
        recorder.startRecording(7, BoardType.ENGLISH, "MANUAL");
        recorder.recordMove(1, 3, 3, 3);
        Board board = new Board(7, BoardType.ENGLISH);
        recorder.recordRandomize(board);
        recorder.recordMove(3, 1, 3, 3);

        String filePath = tempDir.resolve("mixed_game.txt").toString();
        recorder.saveToFile(filePath);

        GameRecorder loaded = GameRecorder.loadFromFile(filePath);
        List<String> events = loaded.getEvents();
        assertEquals(3, events.size());
        assertTrue(events.get(0).startsWith("MOVE:"));
        assertTrue(events.get(1).startsWith("RANDOMIZE:"));
        assertTrue(events.get(2).startsWith("MOVE:"));
    }

    @Test
    void replayMoves_onActualGame() {
        // Record a game
        recorder.startRecording(7, BoardType.ENGLISH, "MANUAL");
        ManualGame game = new ManualGame();
        game.newGame(7, BoardType.ENGLISH);

        game.makeMove(1, 3, 3, 3);
        recorder.recordMove(1, 3, 3, 3);
        game.makeMove(4, 3, 2, 3);
        recorder.recordMove(4, 3, 2, 3);
        int finalPegCount = game.getBoard().getPegCount();

        // Replay on a fresh game
        ManualGame replayGame = new ManualGame();
        replayGame.newGame(7, BoardType.ENGLISH);

        for (String event : recorder.getEvents()) {
            if (event.startsWith("MOVE:")) {
                Move move = GameRecorder.parseMove(event);
                replayGame.makeMove(move.getFromRow(), move.getFromCol(),
                    move.getToRow(), move.getToCol());
            }
        }

        assertEquals(finalPegCount, replayGame.getBoard().getPegCount());
    }

    @Test
    void getEvents_returnsDefensiveCopy() {
        recorder.startRecording(7, BoardType.ENGLISH, "MANUAL");
        recorder.recordMove(1, 3, 3, 3);
        List<String> events = recorder.getEvents();
        events.clear();
        assertEquals(1, recorder.getEvents().size());
    }

    @Test
    void saveAndLoad_hexagonBoard(@TempDir Path tempDir) throws IOException {
        recorder.startRecording(7, BoardType.HEXAGON, "MANUAL");
        recorder.recordMove(1, 3, 3, 3);

        String filePath = tempDir.resolve("hex_game.txt").toString();
        recorder.saveToFile(filePath);

        GameRecorder loaded = GameRecorder.loadFromFile(filePath);
        assertEquals(BoardType.HEXAGON, loaded.getBoardType());
    }
}
