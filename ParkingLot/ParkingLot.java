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

    public synchronized Ticket park(Vehicle vehicle, Gate entryGate, SlotType slotType) {
        ParkingSlot slot = slotStrategy.findAndReserveSlot(levels, entryGate, slotType);

        if (slot == null) {
            throw new NoSlotAvailableException("No slot available for type: " + slotType);
        }

        String ticketId = "TICKET-" + System.nanoTime();
        Ticket ticket = new Ticket(ticketId, vehicle, slot, entryGate, LocalDateTime.now());
        activeTickets.put(ticketId, ticket);

        return ticket;
    }

    public double exit(String ticketId) {
        Ticket ticket = activeTickets.remove(ticketId);

        if (ticket == null) {
            throw new InvalidTicketException("Invalid or already used ticket: " + ticketId);
        }

        ticket.getParkingSlot().release();

        return feeCalculator.calculateFee(
                ticket.getParkingSlot().getSlotType(),
                ticket.getEntryTime(),
                LocalDateTime.now()
        );
    }

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

    public List<ParkingLevel> getLevels() {
        return levels;
    }

    public List<Gate> getGates() {
        return gates;
    }
}