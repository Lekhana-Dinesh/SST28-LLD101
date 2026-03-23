import java.util.Random;

public class Dice {
    private final Random random;
    public Dice() {
        this.random = new Random();
    }

    public int genRandNo() {
        return random.nextInt(6) + 1;
    }
}