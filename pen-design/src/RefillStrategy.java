public interface RefillStrategy {
    // Returns the new ink level after refilling
    int refill(int currentLevel, int amount);
}
