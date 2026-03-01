import java.util.*;

public class DeviceRegistry {
    private final List<Object> devices = new ArrayList<>();

    public void add(Object d) { devices.add(d); }

    public <T> T getFirstOfType(String simpleName, Class<T> asType) {
        for (Object d : devices) {
            if (d.getClass().getSimpleName().equals(simpleName)) {
                return asType.cast(d);
            }
        }
        throw new IllegalStateException("Missing: " + simpleName);
    }
}