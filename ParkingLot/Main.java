import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        // Setup gates
        Gate g1 = new Gate("G1");
        Gate g2 = new Gate("G2");

        // Setup slots with distances to each gate
        Map<String, Double> s1Dist = Map.of("G1", 2.0, "G2", 8.0);
        Map<String, Double> s2Dist = Map.of("G1", 4.0, "G2", 6.0);
        Map<String, Double> m1Dist = Map.of("G1", 5.0, "G2", 3.0);
        Map<String, Double> l1Dist = Map.of("G1", 7.0, "G2", 2.0);

        ParkingSlot s1 = new ParkingSlot("S1", SlotType.SMALL, s1Dist);
        ParkingSlot s2 = new ParkingSlot("S2", SlotType.SMALL, s2Dist);
        ParkingSlot m1 = new ParkingSlot("M1", SlotType.MEDIUM, m1Dist);
        ParkingSlot l1 = new ParkingSlot("L1", SlotType.LARGE, l1Dist);

        Map<SlotType, List<ParkingSlot>> level1Map = new EnumMap<>(SlotType.class);
        level1Map.put(SlotType.SMALL, Arrays.asList(s1, s2));
        level1Map.put(SlotType.MEDIUM, Arrays.asList(m1));
        level1Map.put(SlotType.LARGE, Arrays.asList(l1));

        ParkingLevel level1 = new ParkingLevel("L1", level1Map);

        // Setup fee rates
        Map<SlotType, Double> rates = new EnumMap<>(SlotType.class);
        rates.put(SlotType.SMALL, 20.0);
        rates.put(SlotType.MEDIUM, 40.0);
        rates.put(SlotType.LARGE, 80.0);

        FeeCalculator feeCalculator = new FeeCalculator(rates);
        SlotStrategy slotStrategy = new NearestSlotStrategy();

        ParkingLot parkingLot = ParkingLot.getInstance(
                Arrays.asList(level1),
                Arrays.asList(g1, g2),
                feeCalculator,
                slotStrategy
        );

        LocalDateTime now = LocalDateTime.now();

        // --- Normal parking ---
        Vehicle bike = new Vehicle("KA01AB1234", VehicleType.BIKE);
        Vehicle car  = new Vehicle("KA02CD5678", VehicleType.CAR);

        // API: park(vehicleDetails, entryTime, requestedSlotType, entryGateID)
        Ticket t1 = parkingLot.park(bike, now, SlotType.SMALL, "G1");
        System.out.println("Parked: " + t1);

        Ticket t2 = parkingLot.park(car, now, SlotType.MEDIUM, "G2");
        System.out.println("Parked: " + t2);

        // API: status()
        System.out.println(parkingLot.status());

        // --- Fallback: fill both SMALL slots, then park a bike → should get MEDIUM ---
        Vehicle bike2 = new Vehicle("KA03EF9999", VehicleType.BIKE);
        Ticket t3 = parkingLot.park(bike2, now, SlotType.SMALL, "G1");
        System.out.println("\nAll SMALL slots occupied now.");
        System.out.println(parkingLot.status());

        Vehicle bike3 = new Vehicle("KA04GH0001", VehicleType.BIKE);
        Ticket t4 = parkingLot.park(bike3, now, SlotType.SMALL, "G1"); // no SMALL left → assigned MEDIUM
        System.out.println("\nBike requested SMALL but no slot available → assigned larger slot:");
        System.out.println("Parked: " + t4);

        System.out.println(parkingLot.status());

        // --- Exit ---
        LocalDateTime exitTime = now.plusHours(2);

        // API: exit(parkingTicket, exitTime)
        double fee1 = parkingLot.exit(t1, exitTime);
        System.out.println("\nExit: " + t1.getTicketId() + " | Slot type: " + t1.getParkingSlot().getSlotType() + " | Fee = " + fee1);

        double fee4 = parkingLot.exit(t4, exitTime);
        System.out.println("Exit: " + t4.getTicketId() + " | Slot type: " + t4.getParkingSlot().getSlotType() + " (bike in larger slot) | Fee = " + fee4);

        System.out.println("\n" + parkingLot.status());
    }
}
