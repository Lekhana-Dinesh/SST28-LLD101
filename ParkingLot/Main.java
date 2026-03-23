import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Gate g1 = new Gate("G1");
        Gate g2 = new Gate("G2");

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

        Vehicle bike = new Vehicle("KA01AB1234", VehicleType.BIKE);
        Vehicle car = new Vehicle("KA02CD5678", VehicleType.CAR);

        Ticket t1 = parkingLot.park(bike, g1, SlotType.SMALL);
        System.out.println(t1);

        Ticket t2 = parkingLot.park(car, g2, SlotType.MEDIUM);
        System.out.println(t2);

        System.out.println(parkingLot.status());

        double fee = parkingLot.exit(t1.getTicketId());
        System.out.println("Exit fee for " + t1.getTicketId() + " = " + fee);

        System.out.println(parkingLot.status());
    }
}
