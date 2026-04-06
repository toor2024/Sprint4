# Sprint 3 Report

GitHub Repository: https://github.com/toor2024/Sprint3

---

## 1. Demonstration (9 points)

YouTube/Panopto link: *(TODO: Add video link)*

Features demonstrated in the video:

| # | Feature |
|---|---------|
| (a) | A manual game until the game is over for English board |
| (b) | A manual game until the game is over for Hexagon board |
| (c) | An automated game until the game is over |
| (d) | A manual game with periodic randomization of states until the game is over |
| (e) | Some automated unit tests for the manual game mode |
| (f) | Some automated unit tests for the automated game mode |

---

## 2. Summary of Source Code (1 point)

| Source code file name | Production code or test code? | # lines of code |
|---|---|---|
| Game.java | Production | 117 |
| ManualGame.java | Production | 65 |
| AutomatedGame.java | Production | 34 |
| Board.java | Production | 131 |
| BoardType.java | Production | 8 |
| GameLogic.java | Production | 11 |
| Move.java | Production | 47 |
| SolitaireApp.java | Production | 329 |
| BoardTest.java | Test | 128 |
| GameLogicTest.java | Test | 220 |
| ManualGameTest.java | Test | 222 |
| AutomatedGameTest.java | Test | 152 |
| **Total** | | **1464** |

All source code is located in `Solitaire/src/main/java/` (production) and `Solitaire/src/test/java/` (test).

---

## 3. Production Code vs User Stories/Acceptance Criteria (3 points)

### User Stories

| User Story ID | User Story Name |
|---|---|
| 1 | Choose a board size and type |
| 2 | Choose the game mode of a chosen board (manual/automated) |
| 3 | Start a new game of the chosen board size and game mode |
| 4 | Make a move in a manual game |
| 5 | A manual game is over |
| 6 | Make a move in an automated game |
| 7 | An automated game is over |
| 8 | Randomize the state of the board during a manual game |

### User Story Descriptions

| ID | User Story Name | User Story Description | Priority | Estimated effort (hours) |
|---|---|---|---|---|
| 1 | Choose a board size and type | As a player, I want to choose the board size and board type (English, Hexagon, or Diamond) before starting a game so that I can play at my preferred complexity level and layout. | High | 2 |
| 2 | Choose the game mode | As a player, I want to choose between a manual game mode and an automated game mode so that I can either play myself or watch the computer play. | High | 2 |
| 3 | Start a new game | As a player, I want to start a new game using my selected board size, type, and game mode so that the board is initialized correctly for play. | High | 2 |
| 4 | Make a move in a manual game | As a player, I want to make a valid move by clicking a peg and then clicking an empty hole so that I can progress toward solving the puzzle. | High | 3 |
| 5 | A manual game is over | As a player, I want the game to detect when no more valid moves exist or when one peg remains so that I know whether I have won or lost. | High | 2 |
| 6 | Make a move in an automated game | As a player, I want the computer to automatically select and execute valid moves so that I can watch the game play itself. | High | 3 |
| 7 | An automated game is over | As a player, I want the automated game to stop and display the result when no more valid moves exist so that I can see how the computer performed. | High | 2 |
| 8 | Randomize the board | As a player, I want to randomize the board state during a manual game so that I can continue playing from a different configuration. | Medium | 2 |

### Acceptance Criteria

| User Story ID and Name | AC ID | Scenario Description | Given | When | Then | Status |
|---|---|---|---|---|---|---|
| 1. Choose a board size and type | 1.1 | Select a valid board size | Given the game setup screen is visible with a board size text field | When the player enters board size `7` | Then the board size is accepted and stored as the active setup value | Complete |
| 1. Choose a board size and type | 1.2 | Reject invalid board size | Given the game setup screen is visible | When the player enters an invalid board size (non-numeric, even, or < 5) | Then the game rejects the input and displays an error message | Complete |
| 1. Choose a board size and type | 1.3 | Select board type | Given the game setup screen is visible with board type radio buttons | When the player selects a board type (English, Hexagon, or Diamond) | Then the selected board type is stored as the active setup value | Complete |
| 2. Choose the game mode | 2.1 | Start a manual game | Given the setup screen is visible | When the player clicks `New Game` | Then a manual game begins and the player can click pegs to make moves | Complete |
| 2. Choose the game mode | 2.2 | Start an automated game | Given the setup screen is visible | When the player clicks `Autoplay` | Then an automated game begins and the computer makes moves automatically | Complete |
| 3. Start a new game | 3.1 | Start new game with selected setup | Given a valid board size and board type are selected | When the player clicks `New Game` or `Autoplay` | Then a new board is generated using the selected size and type with the center cell empty | Complete |
| 3. Start a new game | 3.2 | Reset board state on new game | Given a game is already in progress | When the player clicks `New Game` or `Autoplay` again | Then the current board state is cleared and replaced with a fresh initial board | Complete |
| 4. Make a move in a manual game | 4.1 | Execute a valid move | Given a manual game is in progress and at least one legal jump exists | When the player selects a peg and clicks a valid destination hole | Then the move is applied, the jumped peg is removed, and the board is updated | Complete |
| 4. Make a move in a manual game | 4.2 | Prevent an invalid move | Given a manual game is in progress | When the player attempts an illegal move | Then the move is rejected, the board state remains unchanged, and an error is shown | Complete |
| 5. A manual game is over | 5.1 | Detect a solved game (win) | Given a manual game has progressed to one remaining peg | When the game evaluates end-state conditions after a move | Then the game reports a win state with a rating | Complete |
| 5. A manual game is over | 5.2 | Detect no-moves game-over (loss) | Given a manual game has progressed to no legal moves with more than one peg | When the game evaluates end-state conditions after a move | Then the game reports a game-over (loss) state with peg count and rating | Complete |
| 6. Make a move in an automated game | 6.1 | Computer selects a valid move | Given an automated game is in progress and valid moves exist | When the autoplay timer fires | Then the computer selects a random valid move and executes it | Complete |
| 6. Make a move in an automated game | 6.2 | Board updates after automated move | Given an automated move has been made | When the move is executed | Then the board display updates to reflect the new state | Complete |
| 7. An automated game is over | 7.1 | Autoplay stops when no moves remain | Given an automated game is in progress | When no more valid moves exist | Then autoplay stops and the final result is displayed with peg count and rating | Complete |
| 8. Randomize the board | 8.1 | Randomize board state | Given a manual game is in progress | When the player clicks `Randomize` | Then the pegs are randomly redistributed across valid board positions | Complete |
| 8. Randomize the board | 8.2 | Randomized board has valid moves | Given the board has been randomized | When the randomization completes | Then at least one valid move exists so the game can continue | Complete |
| 8. Randomize the board | 8.3 | Randomize preserves board shape | Given the board has been randomized | When the randomization completes | Then the board shape (invalid cells) remains unchanged | Complete |

### Acceptance Criteria mapped to Production Code

| User Story ID and Name | AC ID | Class Name(s) | Method Name(s) | Status | Notes |
|---|---|---|---|---|---|
| 1. Choose a board size and type | 1.1 | Board, SolitaireApp | Board.isValidSize(), SolitaireApp.startNewGame() | Complete | Board size entered in TextField; validated before game starts |
| 1. Choose a board size and type | 1.2 | Board, SolitaireApp | Board.isValidSize(), SolitaireApp.startNewGame() | Complete | Non-numeric and out-of-range inputs rejected with error message |
| 1. Choose a board size and type | 1.3 | SolitaireApp | SolitaireApp.startNewGame() | Complete | Radio buttons for English, Hexagon, Diamond |
| 2. Choose the game mode | 2.1 | ManualGame, SolitaireApp | SolitaireApp.startNewGame() | Complete | New Game button creates ManualGame instance |
| 2. Choose the game mode | 2.2 | AutomatedGame, SolitaireApp | SolitaireApp.startAutoplay() | Complete | Autoplay button creates AutomatedGame instance |
| 3. Start a new game | 3.1 | Game, ManualGame, AutomatedGame | Game.newGame() | Complete | Creates new Board with chosen size and type |
| 3. Start a new game | 3.2 | Game, SolitaireApp | Game.newGame(), SolitaireApp.startNewGame() | Complete | Previous board replaced with fresh initial board |
| 4. Make a move in a manual game | 4.1 | ManualGame, Game | Game.isValidMove(), Game.makeMove() | Complete | Player clicks peg then hole; move validated and executed |
| 4. Make a move in a manual game | 4.2 | ManualGame, Game, SolitaireApp | Game.isValidMove(), SolitaireApp.handleCellClick() | Complete | Invalid moves rejected, error shown |
| 5. A manual game is over | 5.1 | Game | Game.checkGameOver(), Game.hasWon() | Complete | Win detected when 1 peg remains |
| 5. A manual game is over | 5.2 | Game | Game.checkGameOver(), Game.getValidMoves() | Complete | Loss detected when no valid moves and >1 peg |
| 6. Make a move in an automated game | 6.1 | AutomatedGame | AutomatedGame.autoplayMove() | Complete | Picks random valid move from getValidMoves() |
| 6. Make a move in an automated game | 6.2 | AutomatedGame, SolitaireApp | AutomatedGame.autoplayMove(), SolitaireApp.updateBoard() | Complete | Timeline updates board every 400ms |
| 7. An automated game is over | 7.1 | AutomatedGame, SolitaireApp | Game.isGameOver(), SolitaireApp.stopAutoplay() | Complete | Timeline stops, result displayed |
| 8. Randomize the board | 8.1 | ManualGame | ManualGame.randomize() | Complete | Randomly assigns PEG/EMPTY to valid cells |
| 8. Randomize the board | 8.2 | ManualGame | ManualGame.randomize() | Complete | Retries until valid moves exist |
| 8. Randomize the board | 8.3 | ManualGame | ManualGame.randomize() | Complete | Only modifies valid cells, invalid cells unchanged |

---

## 4. Tests vs User Stories/Acceptance Criteria (2 points)

### User Stories

| User Story ID | User Story Name |
|---|---|
| 1 | Choose a board size and type |
| 2 | Choose the game mode of a chosen board (manual/automated) |
| 3 | Start a new game of the chosen board size and game mode |
| 4 | Make a move in a manual game |
| 5 | A manual game is over |
| 6 | Make a move in an automated game |
| 7 | An automated game is over |
| 8 | Randomize the state of the board during a manual game |

### 4.1 Automated tests directly corresponding to some acceptance criteria

| User Story ID | Acceptance Criterion ID | Class Name(s) of the Test Code | Method Name(s) of the Test Code | Status (Pass/Fail) |
|---|---|---|---|---|
| 1 | 1.1 | BoardTest | isValidSize_acceptsValidSizes() | Pass |
| 1 | 1.2 | BoardTest | invalidSize_throwsException(), isValidSize_rejectsEvenNumbers(), isValidSize_rejectsTooSmall() | Pass |
| 2 | 2.1 | ManualGameTest | manualGame_isInstanceOfGame(), newGame_createsBoard() | Pass |
| 2 | 2.2 | AutomatedGameTest | automatedGame_isInstanceOfGame(), newGame_createsBoard() | Pass |
| 3 | 3.1 | ManualGameTest, AutomatedGameTest | newGame_createsBoard() | Pass |
| 3 | 3.2 | ManualGameTest, AutomatedGameTest | newGame_resetsGameState() | Pass |
| 4 | 4.1 | ManualGameTest | validMove_isAccepted(), makeMove_updatesBoardCorrectly(), makeMove_reducesPegCount() | Pass |
| 4 | 4.2 | ManualGameTest | invalidMove_toOccupiedCell_isRejected(), invalidMove_wrongDistance_isRejected(), invalidMove_diagonal_isRejected(), invalidMove_fromEmptyCell_isRejected(), invalidMove_isNotExecuted() | Pass |
| 5 | 5.1 | ManualGameTest | gameOver_winDetectedWith1PegLeft() | Pass |
| 5 | 5.2 | ManualGameTest | gameOver_lossDetectedWithMultiplePegsNoMoves() | Pass |
| 6 | 6.1 | AutomatedGameTest | autoplayMove_makesOneMove(), autoplayMove_returnsValidMove() | Pass |
| 6 | 6.2 | AutomatedGameTest | autoplayMove_returnsValidMove() | Pass |
| 7 | 7.1 | AutomatedGameTest | autoplayUntilGameOver_endsGame(), autoplayMove_returnsNullWhenGameOver() | Pass |
| 8 | 8.1 | ManualGameTest | randomize_changesBoardState() | Pass |
| 8 | 8.2 | ManualGameTest | randomize_ensuresValidMovesExist() | Pass |
| 8 | 8.3 | ManualGameTest | randomize_preservesBoardShape() | Pass |

### 4.2 Manual tests directly corresponding to some acceptance criteria (if any)

| User Story ID | Acceptance Criterion ID | Test Case Input | Test Oracle (Expected Output) | Notes |
|---|---|---|---|---|
| 1 | 1.1 | Enter "7" in board size field, select English | Board size accepted, game starts | Verified via GUI |
| 1 | 1.2 | Enter "abc" in board size field, click New Game | Error message: "Invalid board size" | Verified via GUI |
| 2 | 2.1 | Click New Game | Manual game starts, can click pegs | Verified via GUI |
| 2 | 2.2 | Click Autoplay | Automated game starts, moves play automatically | Verified via GUI |
| 4 | 4.1 | Click peg near center, click center hole | Peg jumps, jumped peg removed | Verified via GUI |
| 4 | 4.2 | Click peg, click non-adjacent hole | Status shows "Invalid move" | Verified via GUI |
| 8 | 8.1 | Start manual game, click Randomize | Board state changes, pegs redistributed | Verified via GUI |

### 4.3 Other automated tests not corresponding to the acceptance criteria (if any)

| Number | Test Input | Expected Result | Class Name of the Test Code | Method Name of the Test Code |
|---|---|---|---|---|
| 1 | English board size 7 | Corners are INVALID | BoardTest | englishBoardSize7_cornersAreInvalid() |
| 2 | English board size 5 | 20 pegs | BoardTest | englishBoardSize5_hasCorrectPegCount() |
| 3 | Diamond board size 5 | 12 pegs | BoardTest | diamondBoardSize5_hasCorrectPegCount() |
| 4 | Diamond board size 7 | Center is EMPTY | BoardTest | diamondBoardSize7_centerIsEmpty() |
| 5 | Hexagon board size 7 | 36 pegs | BoardTest | hexagonBoardSize7_hasCorrectPegCount() |
| 6 | Board copy | Independent copy | BoardTest | boardCopy_isIndependent() |
| 7 | Out of bounds | Returns INVALID | BoardTest | getCellState_outOfBounds_returnsInvalid() |
| 8 | English board size 9 | 44 pegs | BoardTest | englishBoardSize9_hasCorrectPegCount() |
| 9 | Rating with 1 peg | "Outstanding" | ManualGameTest | getRating_outstanding() |
| 10 | Rating with 4+ pegs | "Average" | ManualGameTest | getRating_average() |
| 11 | Diamond board move | Valid move works | ManualGameTest | diamondBoard_validMoveWorks() |
| 12 | Hexagon board init | 36 pegs, center empty | ManualGameTest | hexagonBoard_initializesCorrectly() |
| 13 | Autoplay on Diamond | Move succeeds, 11 pegs left | AutomatedGameTest | autoplay_worksOnDiamondBoard() |
| 14 | Autoplay on Hexagon | Move succeeds, 35 pegs left | AutomatedGameTest | autoplay_worksOnHexagonBoard() |
| 15 | Autoplay all board types | Game completes on all types | AutomatedGameTest | autoplay_completesGameOnAllBoardTypes() |
| 16 | Autoplay peg count | Decreases after each move | AutomatedGameTest | autoplayMove_reducesPegCountByOne() |
| 17 | Autoplay peg decrease | Final count < initial | AutomatedGameTest | autoplayUntilGameOver_pegCountDecreases() |
| 18 | Rating after autoplay | Non-empty rating string | AutomatedGameTest | getRating_afterAutoplay() |
| 19 | Randomize resets game over | Game over flag cleared | ManualGameTest | randomize_resetsGameOverState() |

---

## 5. Class Hierarchy and Design Principles

### Part 1: Class Hierarchy Description (3 points)

The Peg Solitaire game uses a class hierarchy to handle the common and different requirements of the Manual Game and the Automated Game. At the top of the hierarchy is the abstract class `Game`, which contains all shared game logic. Below it, `ManualGame` and `AutomatedGame` extend `Game` with mode-specific behavior.

The `Game` base class encapsulates the core functionality that both game modes share. This includes managing the `Board` object, validating moves through `isValidMove()`, executing moves through `makeMove()`, computing all valid moves through `getValidMoves()`, and detecting game-over conditions through `checkGameOver()`. It also provides shared utility methods like `getRating()`, `isGameOver()`, `hasWon()`, and `getBoard()`. By placing this logic in the abstract base class, code duplication is eliminated — both game modes use the exact same move validation and execution logic, ensuring consistent behavior regardless of how a move is initiated.

The `ManualGame` subclass extends `Game` to support human-driven gameplay. It inherits all base class functionality for move validation and execution, which the GUI calls when the player clicks pegs and holes. ManualGame adds the `randomize()` method, which is unique to the manual game mode. This method randomly redistributes pegs across valid board positions while preserving the board shape and ensuring at least one valid move exists afterward. Randomization is only meaningful in a manual game because the human player needs the ability to change the board configuration and continue playing from a new state.

The `AutomatedGame` subclass extends `Game` to support computer-driven gameplay. It adds the `autoplayMove()` method, which selects a random valid move from the list returned by `getValidMoves()` and executes it using the inherited `makeMove()` method. This method is called repeatedly by a JavaFX `Timeline` in the GUI to animate the automated game. The automated game does not need randomization since the computer is already making random moves, so this method is appropriately absent from this subclass.

The `SolitaireApp` GUI class interacts with the game through the `Game` base class reference for all common operations (checking game state, getting the board, making moves). It only references `ManualGame` or `AutomatedGame` directly when it needs mode-specific behavior: calling `randomize()` on ManualGame or `autoplayMove()` on AutomatedGame. This polymorphic design means the GUI code for board rendering, status display, and cell click handling works identically for both game modes without conditional logic to distinguish between them.

The existing `GameLogic` class has been preserved as a backwards-compatible subclass of `ManualGame`, ensuring that previously written tests continue to pass without modification. This demonstrates how the class hierarchy supports extensibility — new game modes could be added by creating additional subclasses of `Game` without modifying existing code.

### Part 2: LLM Analysis of Design Principles (2 points)

*(TODO: Ask ChatGPT to analyze the code for modularity, cohesion, coupling, and encapsulation. Provide screenshots and write 1/2 page discussing the results.)*

**Prompt suggestions to use with ChatGPT:**

Prompt 1:
```text
Analyze the following Java class hierarchy for a Peg Solitaire game in terms of modularity, cohesion, coupling, and encapsulation:

- Game (abstract base class): manages Board, validates moves, executes moves, detects game over
- ManualGame (extends Game): adds randomize() method for randomizing board state
- AutomatedGame (extends Game): adds autoplayMove() method for computer-driven play
- Board: manages the grid state (PEG, EMPTY, INVALID cells)
- SolitaireApp: JavaFX GUI that delegates all logic to Game subclasses

How well does this design adhere to each of these four principles?
```

Prompt 2 (follow-up):
```text
Based on your analysis, what specific improvements could be made to the code to better adhere to these design principles? Are there any violations of these principles in the current design?
```

Screenshot files:

| Item | File |
|---|---|
| LLM prompt/response for design analysis 1 | `Sprint3Evidence/design_analysis1.png` |
| LLM prompt/response for design analysis 2 | `Sprint3Evidence/design_analysis2.png` |

*(TODO: Write 1/2 page discussing the LLM's feedback and any changes made.)*
