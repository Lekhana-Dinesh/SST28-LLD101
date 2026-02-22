import java.util.*;
public interface DiscountPolicy {
    double discountAmount(String customerType, double subtotal, int lineCount, List<OrderLine> lines);
}
