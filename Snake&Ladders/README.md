# Snakes & Ladders — LLD

A simple turn-based **Snakes and Ladders** game on a random `n × n` board.
The board has exactly **n snakes** and **n ladders**, placed without creating cycles.
Players roll a dice, move in turns, and must land **exactly** on the last cell to win.

## Features

- User input for:
  - board dimension `n`
  - number of players
  - difficulty: `easy` / `hard`
- Board size = `n²`
- Exactly `n` snakes and `n` ladders
- No jump cycles
- Queue-based turn handling
- Exact win condition
- Strategy pattern for difficulty
- Factory pattern for board and game creation

## Main Classes

- `Main` → takes input and starts the game
- `Game` → controls turns and winners
- `Board` → stores board size and snakes/ladders
- `BoardFactory` → creates random valid board
- `GameFactory` → creates `Game`
- `Player` → stores name and position
- `Dice` → rolls `1` to `6`
- `Snake`, `Ladder` → special jumps
- `IMakeMoveStrategy` → move rule interface
- `Easy`, `Hard` → difficulty strategies
- `JumpStrategy` → common interface for snake/ladder

## Mermaid UML Diagram

```mermaid
classDiagram
    class Main {
        +main(String[] args)
    }

    class Game {
        -Queue~Player~ activePlayers
        -List~Player~ winners
        -Board board
        -Dice dice
        -IMakeMoveStrategy moveStrategy
        +start()
    }

    class Board {
        -int dimension
        -int size
        -Map~Integer, JumpStrategy~ specialCells
        +getDimension() int
        +getSize() int
        +resolvePosition(int) int
    }

    class BoardFactory {
        +createRandomBoard(int) Board
    }

    class GameFactory {
        +createGame(List~Player~, Board, Dice, IMakeMoveStrategy) Game
    }

    class Player {
        -String name
        -int position
        +getName() String
        +getPosition() int
        +setPosition(int) void
        +win(int) boolean
    }

    class Dice {
        +genRandNo() int
    }

    class JumpStrategy {
        <<interface>>
        +jump() int
    }

    class Snake {
        -int start
        -int end
        +jump() int
    }

    class Ladder {
        -int start
        -int end
        +jump() int
    }

    class IMakeMoveStrategy {
        <<interface>>
        +makeMove(Player, Board, Dice) void
    }

    class Easy {
        +makeMove(Player, Board, Dice) void
    }

    class Hard {
        +makeMove(Player, Board, Dice) void
    }

    Main --> BoardFactory
    Main --> GameFactory
    GameFactory --> Game
    BoardFactory --> Board
    Game --> Player
    Game --> Board
    Game --> Dice
    Game --> IMakeMoveStrategy
    Board --> JumpStrategy
    Snake ..|> JumpStrategy
    Ladder ..|> JumpStrategy
    Easy ..|> IMakeMoveStrategy
    Hard ..|> IMakeMoveStrategy
```

## Run

```bash
javac *.java
java Main
```

## Example Input

```text
Enter board dimension n (board will be n x n): 5
Enter number of players: 2
Enter difficulty level (easy/hard): easy
```

## Notes

- Players start at `0`
- A player wins only by reaching exactly `n²`
- Overshooting does not move the player
- Winners are ranked in order
