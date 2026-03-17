# Sprint 2 Report

GitHub Repository: https://github.com/toor2024/Sprint2

---

## 1. Demonstration (8 points)

YouTube/Panopto link: https://youtu.be/ZHf1uEf6Mns

Features demonstrated in the video:

| # | Feature |
|---|---------|
| 1 | Choose board size and type |
| 2 | Start a new game of the chosen board size and type |
| 3 | Making a move |
| 4 | Checking the end of game |
| 5 | Automated unit tests |

---

## 2. Summary of Source Code (1 point)

| Source code file name | Production code or test code? | # lines of code |
|---|---|---|
| BoardType.java | Production | 8 |
| Board.java | Production | 131 |
| Move.java | Production | 47 |
| GameLogic.java | Production | 116 |
| SolitaireApp.java | Production | 225 |
| BoardTest.java | Test | 128 |
| GameLogicTest.java | Test | 220 |
| **Total** | | **875** |

All source code is located in `Solitaire/src/main/java/` (production) and `Solitaire/src/test/java/` (test).

---

## 3. Production Code vs User Stories/Acceptance Criteria (3 points)

### User Stories

| User Story ID | User Story |
|---|---|
| 1 | Choose board size and type |
| 2 | Start a new game of the chosen board size and type |
| 3 | Making a move |
| 4 | Checking the end of game |

### Updated User Story Descriptions

| ID | User Story Name | User Story Description | Priority | Estimated effort (hours) |
|---|---|---|---|---|
| 1 | Choose board size and type | As a player, I want to choose the board size and board type (English, Hexagon, or Diamond) before starting a game so that I can play at my preferred complexity level and layout. | High | 2 |
| 2 | Start a new game | As a player, I want to start a new game using my selected board size and board type so that the board is initialized correctly for play. | High | 3 |
| 3 | Making a move | As a player, I want to make a valid move by jumping one peg over another into an empty slot so that I can progress toward solving the puzzle. | High | 5 |
| 4 | Checking the end of game | As a player, I want the game to detect when no more valid moves exist or when one peg remains so that I know whether I have won or lost. | High | 3 |

### Acceptance Criteria mapped to Production Code

| User Story ID and Name | AC ID | Class Name(s) | Method Name(s) | Status (complete or not) | Notes |
|---|---|---|---|---|---|
| 1. Choose board size and type | 1.1 Select a valid board size | Board, SolitaireApp | Board.isValidSize(), SolitaireApp.startNewGame() | Complete | Board size entered in TextField; validated before game starts |
| 1. Choose board size and type | 1.2 Reject invalid board size | Board, SolitaireApp | Board.isValidSize(), SolitaireApp.startNewGame() | Complete | Non-numeric and out-of-range inputs rejected with error message |
| 1. Choose board size and type | 1.3 Select board type | SolitaireApp | SolitaireApp.startNewGame() | Complete | Radio buttons for English, Hexagon, Diamond |
| 1. Choose board size and type | 1.4 Change board type before starting | SolitaireApp | SolitaireApp.startNewGame() | Complete | Latest radio button selection used when New Game clicked |
| 2. Start a new game | 2.1 Start new game with selected setup | GameLogic, Board | GameLogic.newGame(), Board constructor | Complete | Creates new Board with chosen size and type |
| 2. Start a new game | 2.2 Reset board state on new game | GameLogic, SolitaireApp | GameLogic.newGame(), SolitaireApp.startNewGame() | Complete | Previous board replaced with fresh initial board |
| 3. Making a move | 3.1 Execute a valid move | GameLogic | GameLogic.isValidMove(), GameLogic.makeMove() | Complete | Peg jumps, jumped peg removed, board updated |
| 3. Making a move | 3.2 Prevent an invalid move | GameLogic, SolitaireApp | GameLogic.isValidMove(), GameLogic.makeMove() | Complete | Invalid moves rejected, board unchanged, error shown |
| 4. Checking end of game | 4.1 Detect a solved game (win) | GameLogic | GameLogic.checkGameOver(), GameLogic.hasWon() | Complete | Win detected when 1 peg remains |
| 4. Checking end of game | 4.2 Detect no-moves game-over (loss) | GameLogic | GameLogic.checkGameOver(), GameLogic.getValidMoves() | Complete | Loss detected when no valid moves exist and >1 peg |

---

## 4. Tests vs User Stories/Acceptance Criteria (3 points)

### User Stories

| User Story ID | User Story |
|---|---|
| 1 | Choose board size and type |
| 2 | Start a new game of the chosen board size and type |
| 3 | Making a move |
| 4 | Checking the end of game |

### 4.1 Automated tests directly corresponding to the acceptance criteria

| User Story ID and Name | Acceptance Criterion ID | Class Name(s) of the Test Code | Method Name(s) of the Test Code | Status (Pass/Fail) |
|---|---|---|---|---|
| 1. Choose board size and type | 1.1 | BoardTest | isValidSize_acceptsValidSizes() | Pass |
| 1. Choose board size and type | 1.2 | BoardTest | invalidSize_throwsException(), isValidSize_rejectsEvenNumbers(), isValidSize_rejectsTooSmall() | Pass |
| 1. Choose board size and type | 1.3 | GameLogicTest | hexagonBoard_initializesCorrectly(), diamondBoard_validMoveWorks() | Pass |
| 2. Start a new game | 2.1 | BoardTest, GameLogicTest | englishBoardSize7_hasCorrectPegCount(), englishBoardSize7_centerIsEmpty(), newGame_createsBoard() | Pass |
| 2. Start a new game | 2.2 | GameLogicTest | newGame_resetsGameState() | Pass |
| 3. Making a move | 3.1 | GameLogicTest | validMove_isAccepted(), makeMove_updatesBoardCorrectly(), makeMove_reducesPegCount() | Pass |
| 3. Making a move | 3.2 | GameLogicTest | invalidMove_toOccupiedCell_isRejected(), invalidMove_wrongDistance_isRejected(), invalidMove_diagonal_isRejected(), invalidMove_fromEmptyCell_isRejected(), invalidMove_isNotExecuted() | Pass |
| 4. Checking end of game | 4.1 | GameLogicTest | gameOver_winDetectedWith1PegLeft() | Pass |
| 4. Checking end of game | 4.2 | GameLogicTest | gameOver_lossDetectedWithMultiplePegsNoMoves() | Pass |

### 4.2 Manual tests directly corresponding to the acceptance criteria

| User Story ID and Name | Acceptance Criterion ID | Test Case Input | Test Oracle (Expected Output) | Notes |
|---|---|---|---|---|
| 1. Choose board size and type | 1.1 | Enter "7" in board size field, select English | Board size field shows "7", English radio selected | Verified via GUI |
| 1. Choose board size and type | 1.2 | Enter "abc" in board size field, click New Game | Error message displayed: "Invalid board size" | Verified via GUI |
| 1. Choose board size and type | 1.4 | Select Diamond, then switch to Hexagon, click New Game | Hexagon board displayed | Verified via GUI |
| 2. Start a new game | 2.1 | Select size 7, English, click New Game | Board displays 7x7 English cross with center empty | Verified via GUI |
| 3. Making a move | 3.1 | Click peg adjacent to center, click center hole | Peg moves, jumped peg removed | Verified via GUI |
| 3. Making a move | 3.2 | Click a peg, click a non-adjacent hole | Status shows "Invalid move" | Verified via GUI |
| 4. Checking end of game | 4.1 | Play until 1 peg remains | Status shows "You won! Rating: Outstanding" | Verified via GUI |
| 4. Checking end of game | 4.2 | Play until no moves remain with >1 peg | Status shows "Game over" with peg count and rating | Verified via GUI |

### 4.3 Other automated tests not corresponding to the acceptance criteria

| Number | Test Input | Expected Result | Class Name of the Test Code | Method Name of the Test Code |
|---|---|---|---|---|
| 1 | English board size 7 | Corners (0,0), (0,1), etc. are INVALID | BoardTest | englishBoardSize7_cornersAreInvalid() |
| 2 | English board size 5 | 20 pegs on board | BoardTest | englishBoardSize5_hasCorrectPegCount() |
| 3 | Diamond board size 5 | 12 pegs on board | BoardTest | diamondBoardSize5_hasCorrectPegCount() |
| 4 | Diamond board size 7 | Center (3,3) is EMPTY | BoardTest | diamondBoardSize7_centerIsEmpty() |
| 5 | Diamond board size 5 | Corner cells are INVALID | BoardTest | diamondBoardSize5_cornersAreInvalid() |
| 6 | Hexagon board size 7 | 36 pegs on board | BoardTest | hexagonBoardSize7_hasCorrectPegCount() |
| 7 | Hexagon board size 7 | Corner cells are INVALID | BoardTest | hexagonBoardSize7_cornersAreInvalid() |
| 8 | Board copy | Modifying copy does not affect original | BoardTest | boardCopy_isIndependent() |
| 9 | Out of bounds access | Returns INVALID | BoardTest | getCellState_outOfBounds_returnsInvalid() |
| 10 | English board size 9 | 44 pegs on board | BoardTest | englishBoardSize9_hasCorrectPegCount() |
| 11 | Get valid moves at start | Non-empty list of moves | GameLogicTest | getValidMoves_returnsMovesAtStart() |
| 12 | Rating with 1 peg | "Outstanding" | GameLogicTest | getRating_outstanding() |
| 13 | Rating with 2 pegs | "Very Good" | GameLogicTest | getRating_veryGood() |
| 14 | Rating with 3 pegs | "Good" | GameLogicTest | getRating_good() |
| 15 | Rating with 4+ pegs | "Average" | GameLogicTest | getRating_average() |

---

## 5. LLM Evidence for Unit Tests

Two unit tests were generated using ChatGPT. Screenshots of prompts and responses are provided below.

### Screenshot Files
| Item | File |
|---|---|
| LLM prompt/response for test 1 | `Sprint2Evidence/test1_prompt_response.png` |
| LLM prompt/response for test 2 | `Sprint2Evidence/test2_prompt_response.png` |

### Prompt for Test 1
```text
I have a Peg Solitaire game in Java. I have a GameLogic class with methods:
newGame(int size, BoardType type), isValidMove(int fromRow, int fromCol, int toRow, int toCol),
makeMove(int fromRow, int fromCol, int toRow, int toCol), isGameOver(), hasWon(), getValidMoves().

I also have a Board class with getCellState(row, col) that returns Board.PEG, Board.EMPTY, or Board.INVALID,
and getPegCount(). BoardType is an enum with ENGLISH, HEXAGON, DIAMOND.

Write a JUnit 5 test that verifies making a valid move on a size 7 English board reduces the peg count by 1
and updates the board cells correctly.
```

### Prompt for Test 2
```text
Using the same Solitaire game classes described above, write a JUnit 5 test that verifies
starting a new game on a size 5 Diamond board initializes the board with 12 pegs and the
center cell (2,2) is empty.
```

### Errors Corrected from LLM Output
- **Test 1 correction:** ChatGPT created a new `GameLogicTest` class, but the project already has a `GameLogicTest.java` file. The generated test method was added to the existing class instead of creating a duplicate. ChatGPT also used `assertAll` with lambda grouping, which is valid but was simplified to individual `assertEquals` calls for consistency with the rest of the test file.
- **Test 2 correction:** Same class duplication issue — the test method was merged into the existing `GameLogicTest.java`. No logic errors were found; the expected peg count (12) and center position (2,2) were both correct.
