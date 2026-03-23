public class StandardRefillStrategy implements RefillStrategy {
    private static final int MAX_INK_LEVEL = 100;

    @Override
    public int refill(int currentLevel, int amount) {
        int newLevel = Math.min(MAX_INK_LEVEL, currentLevel + amount);
        System.out.println("Refilled by " + amount + "%. Ink level: " + newLevel + "%");
        return newLevel;
    }
}
