Elevator System

This project is a Low Level Design of a multiple elevator system.

Features:
- Multiple elevators
- Only 2 outside buttons on each floor: UP and DOWN
- Inside elevator panel with floor buttons, open, close, and alarm
- Different elevator assignment strategies
- Different elevator scheduling strategies
- Alarm stops all elevators
- Sensor-based current floor update

Main idea:
When a user presses an outside floor button, an external request is created.
The controller receives the request and uses an assignment strategy to choose one elevator.
After that, the selected elevator uses its own scheduling strategy to decide stop order.

This design keeps assignment logic and movement logic separate, so different algorithms can be used easily.

How to run:
1. Compile all files:
   javac *.java
2. Run:
   java Main
