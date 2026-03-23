# Parking Lot — LLD

A multilevel parking lot system supporting vehicle entry/exit across multiple gates. Assigns the nearest compatible slot based on the entry gate, calculates fees by slot type (not vehicle type), and handles vehicles parking in larger slots when their preferred size is unavailable.

---

## Functional Requirements Covered

- **Slot types** — Small (2-wheelers), Medium (cars), Large (buses) with separate hourly rates.
- **Nearest slot assignment** — slots are ranked by distance to the entry gate; the closest available slot is assigned.
- **Vehicle-in-larger-slot fallback** — if the preferred slot type is full, the system automatically assigns the next compatible larger slot (bike → Small/Medium/Large; car → Medium/Large; bus → Large only).
- **Fee calculation** — billed by the *allocated slot type*, not the vehicle type. A bike parked in a Large slot pays the Large rate.
- **Multi-gate support** — each gate has its own distance mapping to every slot, so nearest-slot logic works per gate.
- **Ticket tracking** — each ticket stores vehicle details, allocated slot, slot type, and entry time. Exit requires the ticket and an explicit exit time.

---

## Design Patterns

### Singleton — `ParkingLot`
There is exactly one parking lot instance shared across all gates and levels. Singleton with double-checked locking ensures this without re-initialising on every access.

### Strategy — `SlotStrategy`
Slot allocation logic is behind an interface (`SlotStrategy`), making it swappable without touching `ParkingLot`. `NearestSlotStrategy` picks the closest compatible slot; `RandomSlotStrategy` is provided as an alternative. Adding a new strategy (e.g. prioritise a specific level) requires no changes to existing classes.

### Factory-style setup (in `Main`)
Slots, levels, gates, rates, and strategies are assembled in `Main` before being handed to `ParkingLot.getInstance()`. This keeps construction logic separate from the parking lot's operational logic.

---

## Class Structure

```
ParkingLot (Singleton)
├── has-many  ParkingLevel
│             └── has-many  ParkingSlot  (SlotType, distance-to-gate map)
├── has-many  Gate
├── has-a     FeeCalculator              (hourly rates per SlotType)
└── has-a     SlotStrategy
              ├── NearestSlotStrategy    (sorts by gate distance, tries compatible slot types)
              └── RandomSlotStrategy

Ticket  — vehicle, slot, slot type, entry gate, entry time

Enums:      SlotType (SMALL, MEDIUM, LARGE)
            VehicleType (BIKE, CAR, BUS)
Exceptions: NoSlotAvailableException, InvalidTicketException
```

---

## API

```java
Ticket ticket = parkingLot.park(vehicle, entryTime, requestedSlotType, entryGateId);
String availability = parkingLot.status();
double fee = parkingLot.exit(ticket, exitTime);
```

---

## Compile & Run

```bash
cd ParkingLot
javac *.java
java Main
```

Requires Java 9+ (uses `Map.of`).

---

## Sample Output

```
Parked: Ticket{ticketId='TICKET-...', vehicle=KA01AB1234 (BIKE), slotId=S1, slotType=SMALL, entryGate=G1, entryTime=...}
Parked: Ticket{ticketId='TICKET-...', vehicle=KA02CD5678 (CAR), slotId=M1, slotType=MEDIUM, entryGate=G2, entryTime=...}
Status -> SMALL: 1, MEDIUM: 0, LARGE: 1

All SMALL slots occupied now.
Status -> SMALL: 0, MEDIUM: 0, LARGE: 1

Bike requested SMALL but no slot available → assigned larger slot:
Parked: Ticket{..., vehicle=KA04GH0001 (BIKE), slotId=L1, slotType=LARGE, ...}

Exit: TICKET-... | Slot type: SMALL | Fee = 40.0
Exit: TICKET-... | Slot type: LARGE (bike in larger slot) | Fee = 160.0

Status -> SMALL: 1, MEDIUM: 0, LARGE: 1
```
