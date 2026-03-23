public class Pen implements Refillable, Writable, Closable {
    private final String brand;
    private final InkColor inkColor;
    private int inkLevel;
    private PenState state;
    private static final int MAX_INK_LEVEL = 100;
    private static final int INK_CONSUMPTION_PER_10_CHARS = 5;

    public Pen(String brand, InkColor inkColor, int inkLevel) {
        if (brand == null || brand.trim().isEmpty()) {
            throw new IllegalArgumentException("Brand cannot be null or empty.");
        }
        if (inkLevel < 0 || inkLevel > MAX_INK_LEVEL) {
            throw new IllegalArgumentException("Ink level must be between 0 and 100.");
        }

        this.brand = brand;
        this.inkColor = inkColor;
        this.inkLevel = inkLevel;
        this.state = PenState.CLOSED;
    }
    public void start() {
        if (state == PenState.OPEN) {
            System.out.println("Pen is already started and ready to write.");
            return;
        }
        state = PenState.OPEN;
        System.out.println("Pen started: " + brand + " pen is ready to write.");
    }

    @Override
    public void write(String text) {
        if (state != PenState.OPEN) {
            System.out.println("Cannot write. Start the pen first.");
            return;
        }

        if (text == null || text.trim().isEmpty()) {
            System.out.println("Nothing to write.");
            return;
        }
        int requiredInk = consumeInk(text);

        if (inkLevel <= 0) {
            System.out.println("Cannot write. Ink is empty. Please refill the pen.");
            return;
        }

        if (requiredInk > inkLevel) {
            System.out.println("Cannot write full text. Not enough ink. Please refill the pen.");
            return;
        }

        inkLevel -= requiredInk;
        System.out.println("Writing: " + text);
        System.out.println("Ink remaining: " + inkLevel + "%");
    }

    @Override
    public void close() {
        if (state == PenState.CLOSED) {
            System.out.println("Pen is already closed.");
            return;
        }
        state = PenState.CLOSED;
        System.out.println("Pen closed: " + brand + " pen is now closed.");
    }

    @Override
    public void refill(int amount) {
        if (amount <= 0) {
            System.out.println("Refill amount must be greater than 0.");
            return;
        }

        if (inkLevel == MAX_INK_LEVEL) {
            System.out.println("Pen is already full.");
            return;
        }

        inkLevel = Math.min(MAX_INK_LEVEL, inkLevel + amount);
        System.out.println("Refilled pen by " + amount + "%. Current ink level: " + inkLevel + "%");
    }

    private int consumeInk(String text) {
        int length = text.length();
        return Math.max(1, (int) Math.ceil(length / 10.0) * INK_CONSUMPTION_PER_10_CHARS);
    }

    public String getBrand() {
        return brand;
    }

    public InkColor getInkColor() {
        return inkColor;
    }

    public int getInkLevel() {
        return inkLevel;
    }

    public PenState getState() {
        return state;
    }
}