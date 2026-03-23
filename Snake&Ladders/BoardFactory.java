import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class BoardFactory {
    private final Random random = new Random();

    public Board createRandomBoard(int dimension) {
        if (dimension <= 1) {
            throw new IllegalArgumentException("Board dimension must be greater than 1");
        }
        int size = dimension * dimension;
        int jumpCount = dimension;
        Map<Integer, JumpStrategy> specialCells = new HashMap<>();
        Set<Integer> usedStarts = new HashSet<>();
        int laddersAdded = 0;
        while (laddersAdded < jumpCount) {
            int start = randomInRange(2, size - 1);
            int end = randomInRange(start + 1, size - 1);
            if (usedStarts.contains(start)) {
                continue;
            }
            if (wouldCreateCycle(start, end, specialCells)) {
                continue;
            }
            Ladder ladder = new Ladder(start, end);
            specialCells.put(start, ladder);
            usedStarts.add(start);
            laddersAdded++;
        }
        int snakesAdded = 0;
        while (snakesAdded < jumpCount) {
            int start = randomInRange(2, size - 1);
            int end = randomInRange(1, start - 1);
            if (usedStarts.contains(start)) {
                continue;
            }
            if (wouldCreateCycle(start, end, specialCells)) {
                continue;
            }
            Snake snake = new Snake(start, end);
            specialCells.put(start, snake);
            usedStarts.add(start);
            snakesAdded++;
        }
        return new Board(dimension, specialCells);
    }

    private boolean wouldCreateCycle(int newStart, int newEnd, Map<Integer, JumpStrategy> specialCells) {
        Set<Integer> visited = new HashSet<>();
        int current = newEnd;
        while (specialCells.containsKey(current)) {
            if (!visited.add(current)) {
                return true;
            }
            int next = specialCells.get(current).jump();
            if (current == newStart || next == newStart) {
                return true;
            }
            current = next;
        }
        return false;
    }

    private int randomInRange(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }
}