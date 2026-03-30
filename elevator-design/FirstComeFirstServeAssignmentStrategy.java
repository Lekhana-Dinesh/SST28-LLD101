import java.util.Comparator;
import java.util.List;

public class FirstComeFirstServeAssignmentStrategy implements ElevatorAssignmentStrategy {
    @Override
    public Elevator assignElevator(List<Elevator> elevators, ExternalRequest request) {
        return elevators.stream()
                .filter(Elevator::isOperational)
                .min(Comparator.comparingInt(Elevator::getActiveLoad))
                .orElse(null);
    }
}
