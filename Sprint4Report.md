# Sprint 4 Report

GitHub Repository: https://github.com/toor2024/Sprint3

---

## 1. Demonstration (10 points)

YouTube/Panopto link: *(TODO: Add video link)*

Features demonstrated in the video:

| # | Feature |
|---|---------|
| 1 | A complete manual game without using the "randomize" function is recorded |
| 2 | A complete manual game without using the "randomize" function is replayed |
| 3 | A complete manual game that sporadically uses the "randomize" function is recorded |
| 4 | A complete manual game that sporadically uses the "randomize" function is replayed |
| 5 | A complete automated game is recorded |
| 6 | A complete automated game is replayed |

---

## 2. User Stories and Acceptance Criteria for the Record/Replay Requirements (5 points)

### User Stories

| ID | User Story Name | User Story Description | Priority | Estimated effort (hours) |
|---|---|---|---|---|
| 20 | Record a manual game | As a player, I want to record my manual game moves to a text file so that I can save my game and replay it later. | High | 3 |
| 21 | Record an automated game | As a player, I want to record an automated game to a text file so that I can replay the computer's game later. | High | 2 |
| 22 | Replay a recorded game | As a player, I want to replay a previously recorded game from a text file so that I can watch how the game was played. | High | 3 |
| 23 | Record a game with randomization | As a player, I want the recording to capture randomize events along with moves so that replaying the game reproduces the exact same board states. | Medium | 2 |
| 24 | Toggle recording on/off | As a player, I want a "Record game" checkbox so that I can choose whether to record my current game session. | Medium | 1 |

### Acceptance Criteria

| User Story ID and Name | AC ID | Scenario Description | Given | When | Then | Status |
|---|---|---|---|---|---|---|
| 20. Record a manual game | 20.1 | Record moves during manual game | Given the "Record game" checkbox is checked and a manual game is in progress | When the player makes a valid move by clicking a peg and then a hole | Then the move is recorded as a MOVE event with from/to coordinates | Complete |
| 20. Record a manual game | 20.2 | Save recorded manual game to file | Given a manual game has been recorded and the game is over | When the game ends | Then a file save dialog appears and the player can save the recording to a text file | Complete |
| 21. Record an automated game | 21.1 | Record moves during automated game | Given the "Record game" checkbox is checked and an automated game is in progress | When the computer makes an automated move | Then the move is recorded as a MOVE event with from/to coordinates | Complete |
| 21. Record an automated game | 21.2 | Save recorded automated game to file | Given an automated game has been recorded and the game is over | When autoplay finishes | Then a file save dialog appears and the player can save the recording to a text file | Complete |
| 22. Replay a recorded game | 22.1 | Load a recorded game file | Given the player clicks the "Replay" button | When a file chooser dialog appears and the player selects a recorded game file | Then the game file is loaded and the board is initialized to the recorded board type and size | Complete |
| 22. Replay a recorded game | 22.2 | Animate replay of recorded moves | Given a recorded game file has been loaded | When the replay begins | Then each recorded move is replayed on the board at 600ms intervals with the board updating after each move | Complete |
| 22. Replay a recorded game | 22.3 | Replay shows final result | Given a replay is in progress | When all recorded moves have been replayed | Then the replay stops and the final peg count and rating are displayed | Complete |
| 23. Record a game with randomization | 23.1 | Record randomize events with board state | Given the "Record game" checkbox is checked and a manual game is in progress | When the player clicks "Randomize" | Then the full board state after randomization is recorded as a RANDOMIZE event | Complete |
| 23. Record a game with randomization | 23.2 | Replay restores randomized board state | Given a recorded game with randomize events is being replayed | When a RANDOMIZE event is encountered during replay | Then the board state is restored to the exact state that was recorded after randomization | Complete |
| 24. Toggle recording on/off | 24.1 | Enable recording via checkbox | Given the game setup screen is visible | When the player checks the "Record game" checkbox and starts a new game | Then the game session begins recording all moves and randomize events | Complete |
| 24. Toggle recording on/off | 24.2 | Disable recording via checkbox | Given the "Record game" checkbox is unchecked | When the player starts a new game | Then the game session does not record any events | Complete |

### Acceptance Criteria mapped to Production Code

| User Story ID and Name | AC ID | Class Name(s) | Method Name(s) | Status | Notes |
|---|---|---|---|---|---|
| 20. Record a manual game | 20.1 | GameRecorder, SolitaireApp | GameRecorder.recordMove(), SolitaireApp.handleCellClick() | Complete | Move recorded after successful makeMove() |
| 20. Record a manual game | 20.2 | GameRecorder, SolitaireApp | GameRecorder.saveToFile(), SolitaireApp.saveRecording() | Complete | FileChooser dialog for saving |
| 21. Record an automated game | 21.1 | GameRecorder, SolitaireApp | GameRecorder.recordMove(), SolitaireApp.startAutoplay() | Complete | Each autoplayMove() recorded in Timeline |
| 21. Record an automated game | 21.2 | GameRecorder, SolitaireApp | GameRecorder.saveToFile(), SolitaireApp.saveRecording() | Complete | Save dialog shown when autoplay finishes |
| 22. Replay a recorded game | 22.1 | GameRecorder, SolitaireApp | GameRecorder.loadFromFile(), SolitaireApp.startReplay() | Complete | FileChooser dialog for loading |
| 22. Replay a recorded game | 22.2 | GameRecorder, SolitaireApp | GameRecorder.parseMove(), SolitaireApp.startReplay() | Complete | Timeline with 600ms KeyFrame |
| 22. Replay a recorded game | 22.3 | SolitaireApp | SolitaireApp.startReplay() | Complete | Displays peg count and rating at end |
| 23. Record a game with randomization | 23.1 | GameRecorder, SolitaireApp | GameRecorder.recordRandomize(), SolitaireApp.randomizeBoard() | Complete | Full board state serialized |
| 23. Record a game with randomization | 23.2 | GameRecorder, SolitaireApp | GameRecorder.applyRandomizeEvent(), SolitaireApp.startReplay() | Complete | Board state restored from recorded data |
| 24. Toggle recording on/off | 24.1 | GameRecorder, SolitaireApp | GameRecorder.startRecording(), SolitaireApp.startNewGame() | Complete | CheckBox.isSelected() checked on game start |
| 24. Toggle recording on/off | 24.2 | GameRecorder, SolitaireApp | GameRecorder.stopRecording(), SolitaireApp.startNewGame() | Complete | Recording not started when unchecked |

---

## 3. Summary of Source Code

| Source code file name | Production code or test code? | # lines of code |
|---|---|---|
| Game.java | Production | 117 |
| ManualGame.java | Production | 65 |
| AutomatedGame.java | Production | 34 |
| GameRecorder.java | Production | 178 |
| Board.java | Production | 131 |
| BoardType.java | Production | 8 |
| GameLogic.java | Production | 11 |
| Move.java | Production | 47 |
| SolitaireApp.java | Production | 499 |
| BoardTest.java | Test | 128 |
| GameLogicTest.java | Test | 220 |
| ManualGameTest.java | Test | 222 |
| AutomatedGameTest.java | Test | 152 |
| GameRecorderTest.java | Test | 264 |
| **Total** | | **2076** |

All source code is located in `Solitaire/src/main/java/` (production) and `Solitaire/src/test/java/` (test).

---

## 4. Tests vs User Stories/Acceptance Criteria

### 4.1 Automated tests directly corresponding to some acceptance criteria

| User Story ID | Acceptance Criterion ID | Class Name(s) of the Test Code | Method Name(s) of the Test Code | Status (Pass/Fail) |
|---|---|---|---|---|
| 20 | 20.1 | GameRecorderTest | recordMove_addsEventWhenRecording(), recordMultipleMoves_addsAllEvents() | Pass |
| 20 | 20.2 | GameRecorderTest | saveAndLoad_preservesMoveEvents(), saveAndLoad_preservesBoardConfig() | Pass |
| 21 | 21.1 | GameRecorderTest | recordMove_addsEventWhenRecording(), saveAndLoad_automatedGame() | Pass |
| 21 | 21.2 | GameRecorderTest | saveAndLoad_automatedGame() | Pass |
| 22 | 22.1 | GameRecorderTest | saveAndLoad_preservesBoardConfig() | Pass |
| 22 | 22.2 | GameRecorderTest | replayMoves_onActualGame(), parseMove_createsCorrectMoveObject() | Pass |
| 22 | 22.3 | GameRecorderTest | replayMoves_onActualGame() | Pass |
| 23 | 23.1 | GameRecorderTest | recordRandomize_addsEventWithBoardState(), saveAndLoad_preservesRandomizeEvent() | Pass |
| 23 | 23.2 | GameRecorderTest | applyRandomizeEvent_restoresBoardState() | Pass |
| 24 | 24.1 | GameRecorderTest | startRecording_setsRecordingTrue(), startRecording_storesBoardTypeAndSize() | Pass |
| 24 | 24.2 | GameRecorderTest | recordMove_ignoredWhenNotRecording(), newRecorder_isNotRecording() | Pass |

### 4.2 Manual tests directly corresponding to some acceptance criteria (if any)

| User Story ID | Acceptance Criterion ID | Test Case Input | Test Oracle (Expected Output) | Notes |
|---|---|---|---|---|
| 20 | 20.1 | Check "Record game", click New Game, make moves | Moves recorded internally | Verified via GUI |
| 20 | 20.2 | Play until game over with recording on | File save dialog appears, game saved to .txt file | Verified via GUI |
| 21 | 21.1 | Check "Record game", click Autoplay | Automated moves recorded | Verified via GUI |
| 21 | 21.2 | Let autoplay finish with recording on | File save dialog appears, game saved to .txt file | Verified via GUI |
| 22 | 22.1 | Click Replay, select a .txt file | Board initializes to recorded type/size | Verified via GUI |
| 22 | 22.2 | Load a recorded game file | Moves replay at 600ms intervals on the board | Verified via GUI |
| 23 | 23.1 | Record game, click Randomize during play | Randomize event saved in recording file | Verified via GUI and file inspection |
| 23 | 23.2 | Replay a game file with randomize events | Board state matches original randomized state | Verified via GUI |

### 4.3 Other automated tests not corresponding to the acceptance criteria (if any)

| Number | Test Input | Expected Result | Class Name of the Test Code | Method Name of the Test Code |
|---|---|---|---|---|
| 1 | Stop recording | isRecording() returns false | GameRecorderTest | stopRecording_setsRecordingFalse() |
| 2 | Record randomize when not recording | No events added | GameRecorderTest | recordRandomize_ignoredWhenNotRecording() |
| 3 | Start new recording | Old events cleared | GameRecorderTest | startRecording_clearsOldEvents() |
| 4 | Save/load mixed events (moves + randomize) | All events preserved in order | GameRecorderTest | saveAndLoad_mixedEvents() |
| 5 | Save/load hexagon board game | Board type preserved | GameRecorderTest | saveAndLoad_hexagonBoard() |
| 6 | getEvents() return value | Returns defensive copy | GameRecorderTest | getEvents_returnsDefensiveCopy() |
