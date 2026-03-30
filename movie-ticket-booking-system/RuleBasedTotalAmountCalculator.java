import java.util.ArrayList;
import java.util.List;

public class RuleBasedTotalAmountCalculator extends TotalAmountCalculator {
    private final List<PricingRule> pricingRules = new ArrayList<>();

    public void addRule(PricingRule rule) {
        pricingRules.add(rule);
    }

    @Override
    public double total(Show show, List<Seat> seats) {
        double amount = 0.0;
        for (Seat seat : seats) {
            amount += seat.getSeatType().getBasePrice();
        }

        double finalMultiplier = 1.0;
        for (PricingRule rule : pricingRules) {
            if (rule.applies(show.getStartTime().getDayOfWeek())) {
                finalMultiplier *= rule.getMultiplier();
            }
        }
        return amount * finalMultiplier;
    }
}
