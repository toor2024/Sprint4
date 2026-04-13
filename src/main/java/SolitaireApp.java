import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * JavaFX GUI for Peg Solitaire. This class handles only the user interface
 * and delegates all game logic to the Game class hierarchy.
 */
public class SolitaireApp extends Application {

    private Game game;
    private ManualGame manualGame;
    private AutomatedGame automatedGame;
    private GridPane boardGrid;
    private TextField sizeField;
    private ToggleGroup typeGroup;
    private Label statusLabel;
    private Button autoplayBtn;
    private Button randomizeBtn;
    private Button newGameBtn;
    private Button replayBtn;
    private CheckBox recordCheckBox;
    private int selectedRow = -1;
    private int selectedCol = -1;
    private Timeline autoplayTimeline;
    private boolean isAutoplayMode = false;
    private GameRecorder recorder;
    private boolean isReplayMode = false;
    private Stage primaryStage;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;
        this.recorder = new GameRecorder();

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        // Top: Board size input
        HBox topBox = new HBox(10);
        topBox.setAlignment(Pos.CENTER_RIGHT);
        topBox.setPadding(new Insets(0, 0, 10, 0));
        Label sizeLabel = new Label("Board size");
        sizeField = new TextField("7");
        sizeField.setPrefWidth(50);
        topBox.getChildren().addAll(sizeLabel, sizeField);
        root.setTop(topBox);

        // Left: Board type radio buttons
        VBox leftBox = new VBox(10);
        leftBox.setPadding(new Insets(0, 15, 0, 0));
        Label typeLabel = new Label("Board Type");
        typeGroup = new ToggleGroup();

        RadioButton englishBtn = new RadioButton("English");
        englishBtn.setToggleGroup(typeGroup);
        englishBtn.setSelected(true);
        englishBtn.setUserData(BoardType.ENGLISH);

        RadioButton hexagonBtn = new RadioButton("Hexagon");
        hexagonBtn.setToggleGroup(typeGroup);
        hexagonBtn.setUserData(BoardType.HEXAGON);

        RadioButton diamondBtn = new RadioButton("Diamond");
        diamondBtn.setToggleGroup(typeGroup);
        diamondBtn.setUserData(BoardType.DIAMOND);

        // Record game checkbox
        recordCheckBox = new CheckBox("Record game");

        leftBox.getChildren().addAll(typeLabel, englishBtn, hexagonBtn, diamondBtn,
            new Region(), recordCheckBox);
        root.setLeft(leftBox);

        // Center: Board display
        boardGrid = new GridPane();
        boardGrid.setAlignment(Pos.CENTER);
        boardGrid.setHgap(2);
        boardGrid.setVgap(2);
        root.setCenter(boardGrid);

        // Right: Buttons
        VBox rightBox = new VBox(10);
        rightBox.setPadding(new Insets(0, 0, 0, 15));
        rightBox.setAlignment(Pos.TOP_CENTER);

        replayBtn = new Button("Replay");
        replayBtn.setOnAction(e -> startReplay());

        newGameBtn = new Button("New Game");
        newGameBtn.setOnAction(e -> startNewGame());

        autoplayBtn = new Button("Autoplay");
        autoplayBtn.setOnAction(e -> startAutoplay());

        randomizeBtn = new Button("Randomize");
        randomizeBtn.setOnAction(e -> randomizeBoard());

        rightBox.getChildren().addAll(replayBtn, newGameBtn, autoplayBtn, randomizeBtn);
        root.setRight(rightBox);

        // Bottom: Status message
        statusLabel = new Label("Select board size and type, then click New Game.");
        statusLabel.setPadding(new Insets(10, 0, 0, 0));
        root.setBottom(statusLabel);

        Scene scene = new Scene(root, 650, 600);
        stage.setTitle("Peg Solitaire");
        stage.setScene(scene);
        stage.show();
    }

    private void startNewGame() {
        stopAutoplay();
        stopReplay();

        int size;
        try {
            size = Integer.parseInt(sizeField.getText().trim());
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid board size. Enter an odd number >= 5.");
            return;
        }

        BoardType type = (BoardType) typeGroup.getSelectedToggle().getUserData();

        if (!Board.isValidSize(size, type)) {
            statusLabel.setText("Invalid board size. Enter an odd number >= 5.");
            return;
        }

        manualGame = new ManualGame();
        manualGame.newGame(size, type);
        game = manualGame;
        isAutoplayMode = false;

        // Start recording if checkbox is checked
        if (recordCheckBox.isSelected()) {
            recorder.startRecording(size, type, "MANUAL");
        } else {
            recorder.stopRecording();
        }

        selectedRow = -1;
        selectedCol = -1;
        updateBoard();
        statusLabel.setText("Manual game started. Click a peg to select, then click a hole.");
    }

    private void startAutoplay() {
        stopAutoplay();
        stopReplay();

        int size;
        try {
            size = Integer.parseInt(sizeField.getText().trim());
        } catch (NumberFormatException e) {
            statusLabel.setText("Invalid board size. Enter an odd number >= 5.");
            return;
        }

        BoardType type = (BoardType) typeGroup.getSelectedToggle().getUserData();

        if (!Board.isValidSize(size, type)) {
            statusLabel.setText("Invalid board size. Enter an odd number >= 5.");
            return;
        }

        automatedGame = new AutomatedGame();
        automatedGame.newGame(size, type);
        game = automatedGame;
        isAutoplayMode = true;

        // Start recording if checkbox is checked
        if (recordCheckBox.isSelected()) {
            recorder.startRecording(size, type, "AUTOMATED");
        } else {
            recorder.stopRecording();
        }

        selectedRow = -1;
        selectedCol = -1;
        updateBoard();
        statusLabel.setText("Autoplay started...");

        autoplayTimeline = new Timeline(new KeyFrame(Duration.millis(400), e -> {
            Move move = automatedGame.autoplayMove();
            if (move != null && recorder.isRecording()) {
                recorder.recordMove(move.getFromRow(), move.getFromCol(),
                    move.getToRow(), move.getToCol());
            }
            if (move == null || automatedGame.isGameOver()) {
                stopAutoplay();
                updateBoard();
                // Save recording if active
                if (recorder.isRecording()) {
                    saveRecording();
                }
                Board board = automatedGame.getBoard();
                if (automatedGame.hasWon()) {
                    statusLabel.setText("Autoplay finished. Won! Rating: "
                        + automatedGame.getRating());
                } else {
                    statusLabel.setText("Autoplay finished. Pegs left: "
                        + board.getPegCount() + ". Rating: "
                        + automatedGame.getRating());
                }
                return;
            }
            updateBoard();
            statusLabel.setText("Autoplay... Pegs remaining: "
                + automatedGame.getBoard().getPegCount());
        }));
        autoplayTimeline.setCycleCount(Timeline.INDEFINITE);
        autoplayTimeline.play();
    }

    private void stopAutoplay() {
        if (autoplayTimeline != null) {
            autoplayTimeline.stop();
            autoplayTimeline = null;
        }
    }

    private void randomizeBoard() {
        if (isAutoplayMode) {
            statusLabel.setText("Cannot randomize during autoplay.");
            return;
        }
        if (isReplayMode) {
            statusLabel.setText("Cannot randomize during replay.");
            return;
        }
        if (manualGame == null || manualGame.getBoard() == null) {
            statusLabel.setText("Start a new game first.");
            return;
        }
        if (manualGame.isGameOver()) {
            statusLabel.setText("Game is over. Start a new game first.");
            return;
        }

        manualGame.randomize();

        // Record the randomize event with full board state
        if (recorder.isRecording()) {
            recorder.recordRandomize(manualGame.getBoard());
        }

        selectedRow = -1;
        selectedCol = -1;
        updateBoard();
        statusLabel.setText("Board randomized! Pegs remaining: "
            + manualGame.getBoard().getPegCount());
    }

    private void startReplay() {
        stopAutoplay();
        stopReplay();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Recorded Game");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file == null) return;

        GameRecorder loaded;
        try {
            loaded = GameRecorder.loadFromFile(file.getAbsolutePath());
        } catch (IOException e) {
            statusLabel.setText("Error loading file: " + e.getMessage());
            return;
        }

        // Set up the game based on recorded settings
        BoardType type = loaded.getBoardType();
        int size = loaded.getBoardSize();
        String mode = loaded.getGameMode();

        manualGame = new ManualGame();
        manualGame.newGame(size, type);
        game = manualGame;
        isAutoplayMode = false;
        isReplayMode = true;

        sizeField.setText(String.valueOf(size));
        // Select the correct radio button
        typeGroup.getToggles().forEach(toggle -> {
            if (toggle.getUserData() == type) {
                toggle.setSelected(true);
            }
        });

        selectedRow = -1;
        selectedCol = -1;
        updateBoard();
        statusLabel.setText("Replaying " + mode.toLowerCase() + " game...");

        List<String> events = loaded.getEvents();
        final int[] index = {0};

        Timeline replayTimeline = new Timeline(new KeyFrame(Duration.millis(600), e -> {
            if (index[0] >= events.size()) {
                stopReplay();
                Board board = game.getBoard();
                statusLabel.setText("Replay finished. Pegs left: "
                    + board.getPegCount() + ". Rating: " + game.getRating());
                return;
            }

            String event = events.get(index[0]);
            if (event.startsWith("MOVE:")) {
                Move move = GameRecorder.parseMove(event);
                game.makeMove(move.getFromRow(), move.getFromCol(),
                    move.getToRow(), move.getToCol());
                updateBoard();
                statusLabel.setText("Replaying... Move " + (index[0] + 1)
                    + " of " + events.size() + ". Pegs: "
                    + game.getBoard().getPegCount());
            } else if (event.startsWith("RANDOMIZE:")) {
                GameRecorder.applyRandomizeEvent(event, game.getBoard());
                updateBoard();
                statusLabel.setText("Replaying... Randomize applied. Pegs: "
                    + game.getBoard().getPegCount());
            }
            index[0]++;
        }));
        autoplayTimeline = replayTimeline;
        replayTimeline.setCycleCount(Timeline.INDEFINITE);
        replayTimeline.play();
    }

    private void stopReplay() {
        isReplayMode = false;
        if (autoplayTimeline != null) {
            autoplayTimeline.stop();
            autoplayTimeline = null;
        }
    }

    private void saveRecording() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Recorded Game");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialFileName("solitaire_game.txt");
        File file = fileChooser.showSaveDialog(primaryStage);
        if (file == null) {
            recorder.stopRecording();
            return;
        }

        try {
            recorder.saveToFile(file.getAbsolutePath());
            recorder.stopRecording();
            statusLabel.setText(statusLabel.getText() + " Game saved to file.");
        } catch (IOException e) {
            statusLabel.setText("Error saving: " + e.getMessage());
        }
    }

    private void updateBoard() {
        boardGrid.getChildren().clear();
        Board board = game != null ? game.getBoard() : null;
        if (board == null) return;

        int size = board.getSize();
        double cellSize = Math.min(400.0 / size, 45);
        double radius = cellSize * 0.38;

        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                int state = board.getCellState(r, c);
                if (state == Board.INVALID) {
                    Region spacer = new Region();
                    spacer.setPrefSize(cellSize, cellSize);
                    boardGrid.add(spacer, c, r);
                    continue;
                }

                StackPane cell = new StackPane();
                cell.setPrefSize(cellSize, cellSize);

                Circle circle = new Circle(radius);
                if (state == Board.PEG) {
                    if (r == selectedRow && c == selectedCol) {
                        circle.setFill(Color.RED);
                    } else {
                        circle.setFill(Color.DARKBLUE);
                    }
                    circle.setStroke(Color.BLACK);
                } else {
                    circle.setFill(Color.WHITE);
                    circle.setStroke(Color.GRAY);
                }

                final int row = r;
                final int col = c;
                cell.setOnMouseClicked(e -> handleCellClick(row, col));

                cell.getChildren().add(circle);
                boardGrid.add(cell, c, r);
            }
        }
    }

    private void handleCellClick(int row, int col) {
        if (isAutoplayMode || isReplayMode) return;
        if (game == null || game.isGameOver()) {
            statusLabel.setText("Game over! Click New Game to play again.");
            return;
        }

        Board board = game.getBoard();
        if (board == null) return;

        int state = board.getCellState(row, col);

        if (selectedRow == -1) {
            if (state == Board.PEG) {
                selectedRow = row;
                selectedCol = col;
                updateBoard();
                statusLabel.setText("Peg selected at (" + row + "," + col
                    + "). Click a hole to move.");
            }
        } else {
            if (state == Board.PEG) {
                selectedRow = row;
                selectedCol = col;
                updateBoard();
                statusLabel.setText("Peg selected at (" + row + "," + col
                    + "). Click a hole to move.");
            } else if (state == Board.EMPTY) {
                if (game.makeMove(selectedRow, selectedCol, row, col)) {
                    // Record the move
                    if (recorder.isRecording()) {
                        recorder.recordMove(selectedRow, selectedCol, row, col);
                    }

                    selectedRow = -1;
                    selectedCol = -1;
                    updateBoard();
                    if (game.isGameOver()) {
                        // Save recording if active
                        if (recorder.isRecording()) {
                            saveRecording();
                        }
                        if (game.hasWon()) {
                            statusLabel.setText("You won! Rating: "
                                + game.getRating());
                        } else {
                            statusLabel.setText("Game over. No more moves. Pegs left: "
                                + board.getPegCount() + ". Rating: "
                                + game.getRating());
                        }
                    } else {
                        statusLabel.setText("Move applied. Pegs remaining: "
                            + board.getPegCount());
                    }
                } else {
                    statusLabel.setText("Invalid move. Select a peg and jump to a hole.");
                    selectedRow = -1;
                    selectedCol = -1;
                    updateBoard();
                }
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
