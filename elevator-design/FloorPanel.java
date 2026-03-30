public class FloorPanel {
    private final int floorNumber;
    private final FloorButton upButton;
    private final FloorButton downButton;
    private final ElevatorController elevatorController;

    public FloorPanel(int floorNumber, ElevatorController elevatorController) {
        this.floorNumber = floorNumber;
        this.elevatorController = elevatorController;
        this.upButton = new FloorButton(Direction.UP);
        this.downButton = new FloorButton(Direction.DOWN);
    }

    public void pressUpButton() {
        upButton.press();
        System.out.println("Floor " + floorNumber + " UP button pressed");
        elevatorController.submitExternalRequest(new ExternalRequest(floorNumber, Direction.UP));
        upButton.reset();
    }

    public void pressDownButton() {
        downButton.press();
        System.out.println("Floor " + floorNumber + " DOWN button pressed");
        elevatorController.submitExternalRequest(new ExternalRequest(floorNumber, Direction.DOWN));
        downButton.reset();
    }
}
