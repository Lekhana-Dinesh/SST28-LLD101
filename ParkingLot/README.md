# Parking Lot — LLD

A multilevel parking lot system with multiple gates. It assigns the nearest compatible slot, generates tickets, and calculates fees by **allocated slot type**.

## Features
- **Slot types**: Small, Medium, Large
- **Nearest slot allocation** from the entry gate
- **Larger-slot fallback**:
  - Bike → Small / Medium / Large
  - Car → Medium / Large
  - Bus → Large only
- **Billing by allocated slot type**
- **Multiple gates** with per-slot distance mapping
- **Ticket tracking** with vehicle, slot, slot type, gate, and entry time

## Design Patterns
- **Singleton**: `ParkingLot`
- **Strategy**: `SlotStrategy`
  - `NearestSlotStrategy`
  - `RandomSlotStrategy`

## Mermaid UML
```mermaid
classDiagram
    class ParkingLot {
        -List~ParkingLevel~ levels
        -List~Gate~ gates
        -FeeCalculator feeCalculator
        -SlotStrategy slotStrategy
        -Map~String, Ticket~ activeTickets
        +park(Vehicle, LocalDateTime, SlotType, String) Ticket
        +status() String
        +exit(Ticket, LocalDateTime) double
    }

    class ParkingLevel {
        -String levelId
        -Map~SlotType, List~ParkingSlot~~ slotMapping
        +getSlotsByType(SlotType) List~ParkingSlot~
    }

    class ParkingSlot {
        -String slotId
        -SlotType slotType
        -Map~String, Double~ distanceToGateMap
        -boolean occupied
        +reserve() boolean
        +release() void
        +getDistanceFromGate(String) double
    }

    class Gate {
        -String gateId
    }

    class Vehicle {
        -String vehicleNumber
        -VehicleType vehicleType
    }

    class Ticket {
        -String ticketId
        -Vehicle vehicle
        -ParkingSlot parkingSlot
        -SlotType allocatedSlotType
        -Gate entryGate
        -LocalDateTime entryTime
    }

    class FeeCalculator {
        -Map~SlotType, Double~ hourlyRateMap
        +calculateFee(SlotType, LocalDateTime, LocalDateTime) double
    }

    class SlotStrategy {
        <<interface>>
        +findAndReserveSlot(List~ParkingLevel~, VehicleType, SlotType, Gate) ParkingSlot
    }

    class NearestSlotStrategy {
        +findAndReserveSlot(List~ParkingLevel~, VehicleType, SlotType, Gate) ParkingSlot
    }

    class RandomSlotStrategy {
        +findAndReserveSlot(List~ParkingLevel~, VehicleType, SlotType, Gate) ParkingSlot
    }

    class SlotType {
        <<enum>>
        SMALL
        MEDIUM
        LARGE
    }

    class VehicleType {
        <<enum>>
        BIKE
        CAR
        BUS
    }

    ParkingLot --> ParkingLevel
    ParkingLot --> Gate
    ParkingLot --> FeeCalculator
    ParkingLot --> SlotStrategy
    ParkingLot --> Ticket
    ParkingLevel --> ParkingSlot
    Ticket --> Vehicle
    Ticket --> ParkingSlot
    Ticket --> Gate
    ParkingSlot --> SlotType
    Vehicle --> VehicleType
    NearestSlotStrategy ..|> SlotStrategy
    RandomSlotStrategy ..|> SlotStrategy
```

## Main Classes
- `ParkingLot` → main APIs: `park`, `status`, `exit`
- `ParkingLevel` → one floor of slots
- `ParkingSlot` → one slot with type, occupancy, and gate distances
- `Gate` → entry/exit point
- `Vehicle` → vehicle details
- `Ticket` → parking record
- `FeeCalculator` → computes bill
- `SlotStrategy` → slot allocation rule

## API
```java
Ticket ticket = parkingLot.park(vehicle, entryTime, requestedSlotType, entryGateId);
String availability = parkingLot.status();
double fee = parkingLot.exit(ticket, exitTime);
```

## Run
```bash
cd ParkingLot
javac *.java
java Main
```
