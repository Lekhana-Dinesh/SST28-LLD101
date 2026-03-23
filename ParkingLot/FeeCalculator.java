import java.time.Duration;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.Map;

public class FeeCalculator {
    private final Map<SlotType, Double> hourlyRateMap;

    public FeeCalculator(Map<SlotType, Double> hourlyRateMap) {
        this.hourlyRateMap = new EnumMap<>(hourlyRateMap);
    }

    public double calculateFee(SlotType slotType, LocalDateTime entryTime, LocalDateTime exitTime) {
        long minutes = Duration.between(entryTime, exitTime).toMinutes();
        long hours = (minutes + 59) / 60; // ceil
        hours = Math.max(hours, 1);
        return hourlyRateMap.get(slotType) * hours;
    }
}