import java.util.List;

public interface SlotStrategy {
    ParkingSlot findAndReserveSlot(List<ParkingLevel> levels, Gate entryGate, VehicleType vehicleType, SlotType requestedSlotType);
}