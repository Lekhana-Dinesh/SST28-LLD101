import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class NearestSlotStrategy implements SlotStrategy {

    // Returns slot types the vehicle can use, starting from requestedSlotType going up
    private List<SlotType> getCompatibleSlots(VehicleType vehicleType, SlotType requestedSlotType) {
        List<SlotType> compatible = new ArrayList<>();
        boolean startAdding = false;

        for (SlotType st : SlotType.values()) { // SMALL, MEDIUM, LARGE in order
            if (st == requestedSlotType) startAdding = true;
            if (startAdding && isCompatible(vehicleType, st)) {
                compatible.add(st);
            }
        }
        return compatible;
    }

    private boolean isCompatible(VehicleType vehicleType, SlotType slotType) {
        switch (vehicleType) {
            case BIKE: return true; // can park in any slot
            case CAR:  return slotType == SlotType.MEDIUM || slotType == SlotType.LARGE;
            case BUS:  return slotType == SlotType.LARGE;
            default:   return false;
        }
    }

    @Override
    public ParkingSlot findAndReserveSlot(List<ParkingLevel> levels, Gate entryGate, VehicleType vehicleType, SlotType requestedSlotType) {
        for (SlotType slotType : getCompatibleSlots(vehicleType, requestedSlotType)) {
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
        }
        return null;
    }
}
