import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParkingLot {
    private static volatile ParkingLot instance;

    private final List<ParkingLevel> levels;
    private final List<Gate> gates;
    private final FeeCalculator feeCalculator;
    private volatile SlotStrategy slotStrategy;
    private final Map<String, Ticket> activeTickets;

    private ParkingLot(List<ParkingLevel> levels,
                       List<Gate> gates,
                       FeeCalculator feeCalculator,
                       SlotStrategy slotStrategy) {
        this.levels = Collections.unmodifiableList(new ArrayList<>(levels));
        this.gates = Collections.unmodifiableList(new ArrayList<>(gates));
        this.feeCalculator = feeCalculator;
        this.slotStrategy = slotStrategy;
        this.activeTickets = new ConcurrentHashMap<>();
    }

    public static ParkingLot getInstance(List<ParkingLevel> levels,
                                         List<Gate> gates,
                                         FeeCalculator feeCalculator,
                                         SlotStrategy slotStrategy) {
        if (instance == null) {
            synchronized (ParkingLot.class) {
                if (instance == null) {
                    instance = new ParkingLot(levels, gates, feeCalculator, slotStrategy);
                }
            }
        }
        return instance;
    }

    public void setSlotStrategy(SlotStrategy slotStrategy) {
        this.slotStrategy = slotStrategy;
    }

    // API: park(vehicleDetails, entryTime, requestedSlotType, entryGateID)
    public synchronized Ticket park(Vehicle vehicle, LocalDateTime entryTime, SlotType requestedSlotType, String entryGateId) {
        Gate gate = findGate(entryGateId);
        ParkingSlot slot = slotStrategy.findAndReserveSlot(levels, gate, vehicle.getVehicleType(), requestedSlotType);

        if (slot == null) {
            throw new NoSlotAvailableException("No compatible slot available for: " + vehicle.getVehicleType());
        }

        String ticketId = "TICKET-" + System.nanoTime();
        Ticket ticket = new Ticket(ticketId, vehicle, slot, gate, entryTime);
        activeTickets.put(ticketId, ticket);

        return ticket;
    }

    // API: exit(parkingTicket, exitTime)
    public double exit(Ticket ticket, LocalDateTime exitTime) {
        return exit(ticket.getTicketId(), exitTime);
    }

    public double exit(String ticketId, LocalDateTime exitTime) {
        Ticket ticket = activeTickets.remove(ticketId);

        if (ticket == null) {
            throw new InvalidTicketException("Invalid or already used ticket: " + ticketId);
        }

        ticket.getParkingSlot().release();

        return feeCalculator.calculateFee(
                ticket.getParkingSlot().getSlotType(),
                ticket.getEntryTime(),
                exitTime
        );
    }

    // API: status()
    public String status() {
        Map<SlotType, Integer> freeCounts = new ConcurrentHashMap<>();
        freeCounts.put(SlotType.SMALL, 0);
        freeCounts.put(SlotType.MEDIUM, 0);
        freeCounts.put(SlotType.LARGE, 0);

        for (ParkingLevel level : levels) {
            for (SlotType slotType : SlotType.values()) {
                int count = 0;
                for (ParkingSlot slot : level.getSlotsByType(slotType)) {
                    if (!slot.isOccupied()) {
                        count++;
                    }
                }
                freeCounts.put(slotType, freeCounts.get(slotType) + count);
            }
        }

        return "Status -> SMALL: " + freeCounts.get(SlotType.SMALL) +
                ", MEDIUM: " + freeCounts.get(SlotType.MEDIUM) +
                ", LARGE: " + freeCounts.get(SlotType.LARGE);
    }

    private Gate findGate(String gateId) {
        for (Gate gate : gates) {
            if (gate.getGateId().equals(gateId)) return gate;
        }
        throw new IllegalArgumentException("Gate not found: " + gateId);
    }

    public List<ParkingLevel> getLevels() {
        return levels;
    }

    public List<Gate> getGates() {
        return gates;
    }
}
