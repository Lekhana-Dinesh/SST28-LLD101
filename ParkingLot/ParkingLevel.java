import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

public class ParkingLevel {
    private final String levelId;
    private final Map<SlotType, List<ParkingSlot>> slotMapping;

    public ParkingLevel(String levelId, Map<SlotType, List<ParkingSlot>> slotMapping) {
        this.levelId = levelId;

        Map<SlotType, List<ParkingSlot>> temp = new EnumMap<>(SlotType.class);
        for (Map.Entry<SlotType, List<ParkingSlot>> entry : slotMapping.entrySet()) {
            temp.put(entry.getKey(), Collections.unmodifiableList(new ArrayList<>(entry.getValue())));
        }
        this.slotMapping = Collections.unmodifiableMap(temp);
    }

    public String getLevelId() {
        return levelId;
    }

    public List<ParkingSlot> getSlotsByType(SlotType slotType) {
        return slotMapping.getOrDefault(slotType, Collections.emptyList());
    }

    public Map<SlotType, List<ParkingSlot>> getSlotMapping() {
        return slotMapping;
    }
}