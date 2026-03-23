import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RandomSlotStrategy implements SlotStrategy {
    private final Random random = new Random();

    @Override
    public ParkingSlot findAndReserveSlot(List<ParkingLevel> levels, Gate entryGate, VehicleType vehicleType, SlotType requestedSlotType) {
        List<ParkingSlot> candidates = new ArrayList<>();

        for (ParkingLevel level : levels) {
            candidates.addAll(level.getSlotsByType(requestedSlotType));
        }

        Collections.shuffle(candidates, random);

        for (ParkingSlot slot : candidates) {
            if (slot.reserve()) {
                return slot;
            }
        }

        return null;
    }
}
