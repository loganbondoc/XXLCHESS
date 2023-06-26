# XXLChess

XXLChess, puts a spin on the regular game of chess as players now have access to new pieces on a bigger 14x14 tile board. This game was created using the processing library in Java and the dependencies manager Gradle as a part of the unit INFO1113 (Object Oriented Programming), where I developed my skills in inheritence, abstract classes and interfaces.

### To run the application use ```gradle run``` when navigated in the XXLChess folder

## Adjusting the game
The settings of the XXLChess game can be adjusted within the **config.json** file. Through this you can adjust:
- *"seconds"* = total amount of time for each player
- *"increment"* = the amount of seconds added after each move is made
- *"difficulty"* = the difficulty of the bot, which can be set to either "easy" or "hard"

The starting layout can also be adjusted within the **level1.txt** file. Each letter is correlated to a specific piece, while capital letters are used for black pieces and lower case letters for white pieces.
The file that is read when when generating piece layout at the start of a game can also be changed under *"layout"* in **config.json**

## Difficulty settings
As a part of an extension on the task, I implemented two difficulties for the AI of the game: **easy** and **hard**

### Easy
The easy difficulty utilises randomness to calculate the move it would take. Through this setting, the AI scans the board for all pieces that can move and picks a random piece, from this random piece it will then scan all possible moves that the piece can make on the board and select one at random.

### Hard
In contrast, the hard difficulty considers logic and piece value when choosing a move to make. For every piece on the board, the AI scans every single move that can be made and sorts them into four categories:
- Attacking moves, which are moves where the opponent can take one of the players pieces, without being threatened by another piece.
- Trading moves, where the opponent can take one of the player’s pieces however, by doing so they are threatened by another piece.
- Neutral moves, where the opponent moves to an empty position that is not threatened by another piece
- Bad moves, empty positions that the opponent can move to that are at threat by another piece

Prior to selecting from these categories, the moves in each would be sorted according to value of the pieces involved:
Attacking moves are sorted in descending order according to the player’s attacked piece value
Trading will take the value of the AI’s piece and minus the value of the player’s piece, and sort them in descending order. This prevents the AI from making negative trades for its pieces
Neutral moves and bad moves are selected at random.

Following the sorting, the AI will make its move, prioritising attacking moves, then trading moves, then neutral moves and finally bad moves.

