# Snakes & Ladders — LLD

A turn-based Snakes and Ladders game played on a randomly generated n×n board. The board is populated with exactly n snakes and n ladders, placed without creating jump cycles. Players take turns rolling a dice and moving; the first to land exactly on the last cell wins.

---

## Requirements Covered

- **n×n board** — dimension is user-supplied; board has `n²` cells.
- **n snakes and n ladders** — `BoardFactory` randomly places exactly n of each; heads/tails and starts are never reused.
- **Turn-based play** — players cycle through a queue; each rolls once per turn.
- **Difficulty** — `easy`: overshooting the board keeps the player in place. `hard`: overshooting loses the turn (same effect, stricter framing).
- **Win condition** — a player wins by landing exactly on the last cell; they are removed from the queue and ranked.
- **No-cycle guarantee** — before placing any snake or ladder, `BoardFactory` traces the chain from the destination and rejects the placement if it would create a loop.

---

## Design Patterns

### Strategy — `IMakeMoveStrategy`
Move logic (overshoot handling, logging) is behind an interface, making `Easy` and `Hard` interchangeable without touching `Game`. New difficulty variants can be added with no changes to existing classes.

### Strategy — `JumpStrategy`
`Snake` and `Ladder` both implement `JumpStrategy` with a single `jump()` method. `Board` stores them in a unified `Map<Integer, JumpStrategy>`, so position resolution is one call regardless of what kind of special cell it is.

### Factory — `BoardFactory` and `GameFactory`
Construction complexity (random placement, cycle checking, wiring players/dice/strategy) is isolated from `Main`. `GameFactory` keeps `Game` construction behind a single call; `BoardFactory` encapsulates the full board generation algorithm.

---

## Class Structure

```
Game
├── has-many  Player              (position, name)
├── has-a     Board
│             └── Map<Integer, JumpStrategy>
│                 ├── Snake      (jumps down)
│                 └── Ladder     (jumps up)
├── has-a     Dice
└── has-a     IMakeMoveStrategy
              ├── Easy
              └── Hard

BoardFactory  → produces Board
GameFactory   → produces Game
```

---

## Compile & Run

```bash
cd "Snake&Ladders"
javac *.java
java Main
```

**Example input:**
```
Enter board dimension n (board will be n x n): 5
Enter number of players: 2
Enter difficulty level (easy/hard): easy
```

Requires Java 8+.

---

## Sample Output

```
Generated board details:
Board size: 5 x 5 = 25 cells
Snakes and ladders count each: 5
Ladder : 3  -> 11
Ladder : 7  -> 20
Snake  : 17 -> 4
Snake  : 22 -> 9
...

Game started on a 5x5 board.

P1 rolled: 4
P1 climbed a ladder from 4 to 14
P1 moved to: 14

P2 rolled: 6
P2 moved to: 6

...

P1 rolled: 3
P1 finished at rank 1!

Game over.
Winners order:
1. P1
```
