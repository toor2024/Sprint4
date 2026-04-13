import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Records and replays Peg Solitaire games to/from text files.
 * Stores board configuration and all moves (including randomize events)
 * so that a game can be replayed exactly as it was played.
 */
public class GameRecorder {

    private boolean recording;
    private List<String> events;
    private BoardType boardType;
    private int boardSize;
    private String gameMode;

    public GameRecorder() {
        this.recording = false;
        this.events = new ArrayList<>();
    }

    /**
     * Starts recording a new game session.
     */
    public void startRecording(int boardSize, BoardType boardType, String gameMode) {
        this.recording = true;
        this.boardSize = boardSize;
        this.boardType = boardType;
        this.gameMode = gameMode;
        this.events = new ArrayList<>();
    }

    /**
     * Stops recording.
     */
    public void stopRecording() {
        this.recording = false;
    }

    /**
     * Returns true if currently recording.
     */
    public boolean isRecording() {
        return recording;
    }

    /**
     * Records a move event.
     */
    public void recordMove(int fromRow, int fromCol, int toRow, int toCol) {
        if (!recording) return;
        events.add("MOVE:" + fromRow + "," + fromCol + "," + toRow + "," + toCol);
    }

    /**
     * Records a randomize event with the full board state afterward.
     */
    public void recordRandomize(Board board) {
        if (!recording) return;
        StringBuilder sb = new StringBuilder("RANDOMIZE:");
        int size = board.getSize();
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                if (c > 0) sb.append(",");
                sb.append(board.getCellState(r, c));
            }
            if (r < size - 1) sb.append(";");
        }
        events.add(sb.toString());
    }

    /**
     * Returns the list of recorded events.
     */
    public List<String> getEvents() {
        return new ArrayList<>(events);
    }

    /**
     * Returns the recorded board type.
     */
    public BoardType getBoardType() {
        return boardType;
    }

    /**
     * Returns the recorded board size.
     */
    public int getBoardSize() {
        return boardSize;
    }

    /**
     * Returns the recorded game mode.
     */
    public String getGameMode() {
        return gameMode;
    }

    /**
     * Saves the recorded game to a text file.
     */
    public void saveToFile(String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("BOARD_TYPE:" + boardType.name());
            writer.newLine();
            writer.write("BOARD_SIZE:" + boardSize);
            writer.newLine();
            writer.write("GAME_MODE:" + gameMode);
            writer.newLine();
            for (String event : events) {
                writer.write(event);
                writer.newLine();
            }
        }
    }

    /**
     * Loads a recorded game from a text file.
     * Returns a GameRecorder populated with the file's data.
     */
    public static GameRecorder loadFromFile(String filePath) throws IOException {
        GameRecorder recorder = new GameRecorder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.startsWith("BOARD_TYPE:")) {
                    recorder.boardType = BoardType.valueOf(line.substring(11));
                } else if (line.startsWith("BOARD_SIZE:")) {
                    recorder.boardSize = Integer.parseInt(line.substring(11));
                } else if (line.startsWith("GAME_MODE:")) {
                    recorder.gameMode = line.substring(10);
                } else if (line.startsWith("MOVE:") || line.startsWith("RANDOMIZE:")) {
                    recorder.events.add(line);
                }
            }
        }
        return recorder;
    }

    /**
     * Parses a MOVE event string into a Move object.
     */
    public static Move parseMove(String event) {
        String data = event.substring(5); // remove "MOVE:"
        String[] parts = data.split(",");
        int fromRow = Integer.parseInt(parts[0]);
        int fromCol = Integer.parseInt(parts[1]);
        int toRow = Integer.parseInt(parts[2]);
        int toCol = Integer.parseInt(parts[3]);
        return new Move(fromRow, fromCol, toRow, toCol);
    }

    /**
     * Parses a RANDOMIZE event string and applies the board state.
     */
    public static void applyRandomizeEvent(String event, Board board) {
        String data = event.substring(10); // remove "RANDOMIZE:"
        String[] rows = data.split(";");
        for (int r = 0; r < rows.length; r++) {
            String[] cols = rows[r].split(",");
            for (int c = 0; c < cols.length; c++) {
                int state = Integer.parseInt(cols[c]);
                if (state != Board.INVALID) {
                    board.setCellState(r, c, state);
                }
            }
        }
    }
}
