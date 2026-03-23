import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NearestSlotStrategy implements SlotStrategy {

    @Override
    public ParkingSlot findAndReserveSlot(List<ParkingLevel> levels, Gate entryGate, SlotType slotType) {
        List<ParkingSlot> candidates = new ArrayList<>();

        for (ParkingLevel level : levels) {
            candidates.addAll(level.getSlotsByType(slotType));
        }

        candidates.sort(Comparator.comparingDouble(slot -> slot.getDistanceFromGate(entryGate.getGateId())));

        for (ParkingSlot slot : candidates) {
            if (slot.reserve()) {
                return slot;
            }
        }

        return null;
    }
}