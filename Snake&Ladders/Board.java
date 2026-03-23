import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Board {
    private final int dimension;
    private final int size;
    private final Map<Integer, JumpStrategy> specialCells;

    public Board(int dimension, Map<Integer, JumpStrategy> specialCells) {
        if (dimension <= 1) {
            throw new IllegalArgumentException("Board dimension must be greater than 1");
        }
        this.dimension = dimension;
        this.size = dimension * dimension;
        this.specialCells = Collections.unmodifiableMap(new HashMap<>(specialCells));
    }

    public int getDimension() {
        return dimension;
    }

    public int getSize() {
        return size;
    }

    public int resolvePosition(int position) {
        JumpStrategy jumpStrategy = specialCells.get(position);
        if (jumpStrategy != null) {
            return jumpStrategy.jump();
        }
        return position;
    }

    public Map<Integer, JumpStrategy> getSpecialCells() {
        return specialCells;
    }
}