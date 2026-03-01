public class DriverAllocator implements DriverAllocatorInterface{
    public String allocate(String studentId) {
        // fake deterministic driver
        return "DRV-17";
    }
}
