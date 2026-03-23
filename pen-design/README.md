# Pen Design — LLD

Models a pen with `click()`, `write()`, `refill()`, and `close()` operations. Different pen types vary in how they open/close (click vs uncap) and how they refill (top-up vs cartridge replacement). These behavioural differences are handled via the Strategy pattern rather than subclass overrides.

---

## Design Patterns

### Strategy — `RefillStrategy` and `OpenCloseStrategy`
Pen behaviour for refilling and opening/closing is injected at construction rather than hardcoded. This means adding a new pen mechanism (e.g. a twist-cap) only requires a new strategy class — no changes to `Pen` or existing pen types.

- **`RefillStrategy`** — `StandardRefillStrategy` tops up ink by a given amount; `CartridgeRefillStrategy` always fills to 100% regardless of amount passed.
- **`OpenCloseStrategy`** — `ClickStrategy` extends/retracts the tip; `UncapStrategy` removes/replaces the cap.

### Inheritance — `Pen` (abstract) → `BallPen`, `GelPen`, `InkPen`
Each concrete pen wires up the right strategies in its constructor. Shared logic (ink tracking, state guards, write mechanics) lives once in `Pen`.

---

## Class Structure

```
Pen (abstract)
├── has-a  RefillStrategy
│          ├── StandardRefillStrategy
│          └── CartridgeRefillStrategy
├── has-a  OpenCloseStrategy
│          ├── ClickStrategy
│          └── UncapStrategy
├── BallPen   (Click + Standard refill)
├── GelPen    (Click + Cartridge refill)
└── InkPen    (Uncap + Standard refill)

Interfaces: Writable, Refillable, Closable
Enums:      InkColor, PenState
```

---

## Compile & Run

```bash
cd pen-design/src
javac *.java
java Main
```

Requires Java 8+.

---

## Sample Output

```
=== Ball Pen (click + standard refill) ===
Click! Pen tip extended.
Writing: Hello from a ball pen!
Ink remaining: 65%
Refilled by 25%. Ink level: 90%
Click! Pen tip retracted.

=== Gel Pen (click + cartridge refill) ===
Click! Pen tip extended.
Writing: Writing with a gel pen.
Ink remaining: 35%
Refill amount must be greater than 0.
Cartridge replaced. Ink level: 100%
Click! Pen tip retracted.

=== Ink Pen (uncap + standard refill) ===
Cap removed. Pen is ready to write.
Writing: Elegant writing with an ink pen.
Ink remaining: 40%
Cap placed back on pen.
Cannot write. Open the pen first.
```
