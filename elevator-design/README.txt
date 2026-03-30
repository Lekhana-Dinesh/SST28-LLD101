# Elevator System

This project is a **Low Level Design (LLD)** of a **multiple elevator system**.

It supports:
- Multiple elevators
- Only **2 outside buttons** on each floor: **UP** and **DOWN**
- Separate inside panel for each elevator
- Inside panel buttons for:
  - destination floors
  - open door
  - close door
  - alarm
- Different elevator assignment strategies
- Different elevator scheduling strategies
- Global alarm handling
- Sensor-based current floor updates

---

## Main Idea

When a user presses an outside floor button, an **external request** is created.

The **ElevatorController** receives the request and uses an **assignment strategy** to select one elevator.

After that, the selected elevator uses its own **scheduling strategy** to decide the stop order.

This keeps:
- **assignment logic separate**
- **movement logic separate**

So different algorithms can be added easily.

---

## Features

- Multiple elevator cars
- Outside floor panel with only **UP** and **DOWN**
- Inside elevator panel with:
  - floor buttons
  - door open button
  - door close button
  - alarm button
- Assignment strategy can be changed
- Scheduling strategy can be changed
- Alarm stops all elevators
- Maintenance / emergency state support
- Sensor abstraction for current floor

---

## Important Design Points

### 1. Elevator assignment is separate
The controller does not directly decide which elevator should take the request.

That logic is handled by:

- `ElevatorAssignmentStrategy`

Examples:
- `NearestCarAssignmentStrategy`
- `FirstComeFirstServeAssignmentStrategy`
- `DualDirectionAssignmentStrategy`

### 2. Elevator stop order is separate
Once an elevator is assigned, that elevator decides its next stop using:

- `ElevatorSchedulingStrategy`

Examples:
- `SCANSchedulingStrategy`
- `SimpleNearestStopSchedulingStrategy`

### 3. Controller only coordinates
`ElevatorController`:
- receives external requests
- asks strategy to choose an elevator
- steps all elevators
- handles global alarm

### 4. Elevator manages its own movement
Each `Elevator` manages:
- current floor
- direction
- state
- pending stops
- door operations
- alarm state
- sensor updates

---

## Request Flow

### Outside button press
1. User presses **UP** or **DOWN** on a floor
2. `FloorPanel` creates an `ExternalRequest`
3. `ElevatorController` receives it
4. Assignment strategy selects one elevator
5. That elevator adds the request
6. Elevator moves and serves the floor

### Inside button press
1. User presses a destination floor inside elevator
2. `ElevatorPanel` creates an `InternalRequest`
3. Elevator adds destination stop
4. Scheduling strategy decides stop order
5. Elevator moves

### Alarm button press
1. User presses alarm button inside an elevator
2. Controller triggers global alarm
3. All elevators stop
4. Alarm rings

---

## Project Files

- `Main.java`  
  Entry point to run the program

- `Building.java`  
  Creates floors, elevators, panels, and controller

- `ElevatorSystem.java`  
  Runs the system using `tick()`

- `Elevator.java`  
  Core class for one elevator

- `ElevatorController.java`  
  Coordinates external requests and global operations

- `Floor.java`  
  Represents one floor

- `FloorPanel.java`  
  Outside panel with UP and DOWN buttons

- `ElevatorPanel.java`  
  Inside panel with floor/open/close/alarm buttons

- `Request.java`  
  Base request class

- `ExternalRequest.java`  
  Request from floor panel

- `InternalRequest.java`  
  Request from elevator panel

- `Direction.java`  
  Defines `UP`, `DOWN`, `IDLE`

- `ElevatorState.java`  
  Defines states like moving, idle, emergency stop

- `Button.java` and button subclasses  
  Represents different button types

- `Door.java`, `Display.java`, `Alarm.java`, `FloorSensor.java`  
  Supporting device classes

- Strategy classes  
  Used for assignment and scheduling algorithms

---

## How to Run

Compile all files:

```bash
javac *.java
```

Run the program:

```bash
java Main
```

---

## Mermaid UML Diagram

```mermaid
classDiagram

class Building {
  - List~Floor~ floors
  - List~Elevator~ elevators
  - List~ElevatorPanel~ elevatorPanels
  - ElevatorController controller
}

class Floor {
  - int floorNumber
  - FloorPanel floorPanel
}

class ElevatorSystem {
  - Building building
  + tick()
}

class ElevatorController {
  - List~Elevator~ elevators
  - Queue~ExternalRequest~ pendingRequests
  - ElevatorAssignmentStrategy assignmentStrategy
  + submitExternalRequest(request)
  + assignPendingRequests()
  + stepAllElevators()
  + triggerGlobalAlarm()
  + resetGlobalAlarm()
  + setAssignmentStrategy(strategy)
}

class Elevator {
  - int id
  - int currentFloor
  - Direction direction
  - ElevatorState state
  - int capacityKg
  - int currentLoadKg
  - TreeSet~Integer~ upStops
  - TreeSet~Integer~ downStops
  - Door door
  - Display display
  - Alarm alarm
  - FloorSensor floorSensor
  - ElevatorSchedulingStrategy schedulingStrategy
  + addExternalRequest(request)
  + addInternalRequest(request)
  + step()
  + openDoor()
  + closeDoor()
  + emergencyStop()
  + resetEmergency()
  + updateCurrentFloorFromSensor(floor)
}

class FloorPanel {
  - int floorNumber
  - FloorButton upButton
  - FloorButton downButton
  - ElevatorController elevatorController
  + pressUpButton()
  + pressDownButton()
}

class ElevatorPanel {
  - Elevator elevator
  - ElevatorController controller
  - Map~Integer, CabinButton~ floorButtons
  - DoorOpenButton doorOpenButton
  - DoorCloseButton doorCloseButton
  - AlarmButton alarmButton
  + pressFloorButton(destinationFloor)
  + pressDoorOpen()
  + pressDoorClose()
  + pressAlarm()
}

class Request {
  <<abstract>>
  - int sourceFloor
  - long timestamp
}

class ExternalRequest {
  - Direction direction
}

class InternalRequest {
  - int destinationFloor
  - int elevatorId
}

class Button {
  <<abstract>>
  - boolean pressed
  - ButtonType buttonType
  + press()
  + reset()
}

class FloorButton {
  - Direction direction
}

class CabinButton {
  - int floorNumber
}

class DoorOpenButton
class DoorCloseButton
class AlarmButton

class Door {
  - boolean open
  + open()
  + close()
}

class Display {
  + show(elevatorId, floor, direction, state)
}

class Alarm {
  - boolean active
  + ring()
  + stop()
}

class FloorSensor {
  - int sensedFloor
  + getSensedFloor()
  + updateSensedFloor(floor)
}

class ElevatorAssignmentStrategy {
  <<interface>>
  + assignElevator(elevators, request)
}

class NearestCarAssignmentStrategy
class FirstComeFirstServeAssignmentStrategy
class DualDirectionAssignmentStrategy

class ElevatorSchedulingStrategy {
  <<interface>>
  + nextStop(elevator)
}

class SCANSchedulingStrategy
class SimpleNearestStopSchedulingStrategy

class Direction {
  <<enumeration>>
  UP
  DOWN
  IDLE
}

class ElevatorState {
  <<enumeration>>
  MOVING_UP
  MOVING_DOWN
  IDLE
  DOOR_OPEN
  UNDER_MAINTENANCE
  EMERGENCY_STOP
}

class ButtonType {
  <<enumeration>>
  FLOOR_UP
  FLOOR_DOWN
  CABIN_FLOOR
  DOOR_OPEN
  DOOR_CLOSE
  ALARM
}

Request <|-- ExternalRequest
Request <|-- InternalRequest

Button <|-- FloorButton
Button <|-- CabinButton
Button <|-- DoorOpenButton
Button <|-- DoorCloseButton
Button <|-- AlarmButton

ElevatorAssignmentStrategy <|.. NearestCarAssignmentStrategy
ElevatorAssignmentStrategy <|.. FirstComeFirstServeAssignmentStrategy
ElevatorAssignmentStrategy <|.. DualDirectionAssignmentStrategy

ElevatorSchedulingStrategy <|.. SCANSchedulingStrategy
ElevatorSchedulingStrategy <|.. SimpleNearestStopSchedulingStrategy

Building *-- Floor
Building *-- Elevator
Building *-- ElevatorPanel
Building *-- ElevatorController

Floor *-- FloorPanel

ElevatorController o-- Elevator
ElevatorController ..> ElevatorAssignmentStrategy

Elevator *-- Door
Elevator *-- Display
Elevator *-- Alarm
Elevator *-- FloorSensor
Elevator ..> ElevatorSchedulingStrategy
Elevator ..> ExternalRequest
Elevator ..> InternalRequest

FloorPanel --> ElevatorController
ElevatorPanel --> Elevator
ElevatorPanel --> ElevatorController

ElevatorSystem *-- Building
```

---

## Conclusion

This project shows a clean object-oriented design for a multiple elevator system.

It separates:
- request assignment
- elevator movement logic
- buttons and panels
- supporting devices

This makes the design easier to understand and easy to extend in future.
