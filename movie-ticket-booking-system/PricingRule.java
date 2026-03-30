import java.time.DayOfWeek;

public class PricingRule {
    private final String name;
    private final DayOfWeek dayOfWeek;
    private final double multiplier;

    public PricingRule(String name, DayOfWeek dayOfWeek, double multiplier) {
        this.name = name;
        this.dayOfWeek = dayOfWeek;
        this.multiplier = multiplier;
    }

    public boolean applies(DayOfWeek actualDay) {
        return dayOfWeek == actualDay;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public String getName() {
        return name;
    }
}
