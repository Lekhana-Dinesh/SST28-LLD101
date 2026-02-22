import java.util.*;
public class DefaultDiscountPolicy implements DiscountPolicy {
    @Override
    public double discountAmount(String customerType, double subtotal, int lineCount, List<OrderLine> lines) {
        return DiscountRules.discountAmount(customerType, subtotal, lineCount);
    }
}
