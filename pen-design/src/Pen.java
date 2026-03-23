public abstract class Pen implements Refillable, Writable, Closable {
    private static final int MAX_INK_LEVEL = 100;
    private static final int INK_PER_10_CHARS = 5;

    private final String brand;
    private final InkColor inkColor;
    private int inkLevel;
    private PenState state;

    private final RefillStrategy refillStrategy;
    private final OpenCloseStrategy openCloseStrategy;

    public Pen(String brand, InkColor inkColor, int inkLevel,
               RefillStrategy refillStrategy, OpenCloseStrategy openCloseStrategy) {
        this.brand = brand;
        this.inkColor = inkColor;
        this.inkLevel = inkLevel;
        this.state = PenState.CLOSED;
        this.refillStrategy = refillStrategy;
        this.openCloseStrategy = openCloseStrategy;
    }

    // Opens the pen (click for retractable, uncap for capped pens)
    public void click() {
        if (state == PenState.OPEN) {
            System.out.println(brand + " pen is already open.");
            return;
        }
        openCloseStrategy.open();
        state = PenState.OPEN;
    }

    @Override
    public void write(String text) {
        if (state != PenState.OPEN) {
            System.out.println("Cannot write. Open the pen first.");
            return;
        }
        if (text == null || text.trim().isEmpty()) {
            System.out.println("Nothing to write.");
            return;
        }
        int inkNeeded = Math.max(1, (int) Math.ceil(text.length() / 10.0) * INK_PER_10_CHARS);
        if (inkLevel <= 0) {
            System.out.println("Cannot write. Ink is empty. Please refill.");
            return;
        }
        if (inkNeeded > inkLevel) {
            System.out.println("Not enough ink. Please refill.");
            return;
        }
        inkLevel -= inkNeeded;
        System.out.println("Writing: " + text);
        System.out.println("Ink remaining: " + inkLevel + "%");
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
        inkLevel = refillStrategy.refill(inkLevel, amount);
    }

    @Override
    public void close() {
        if (state == PenState.CLOSED) {
            System.out.println(brand + " pen is already closed.");
            return;
        }
        openCloseStrategy.close();
        state = PenState.CLOSED;
    }

    public String getBrand()      { return brand; }
    public InkColor getInkColor() { return inkColor; }
    public int getInkLevel()      { return inkLevel; }
    public PenState getState()    { return state; }
}
