public class CartridgeRefillStrategy implements RefillStrategy {
    private static final int MAX_INK_LEVEL = 100;

    @Override
    public int refill(int currentLevel, int amount) {
        // Cartridge replacement always fills to max regardless of amount
        System.out.println("Cartridge replaced. Ink level: " + MAX_INK_LEVEL + "%");
        return MAX_INK_LEVEL;
    }
}
