import java.util.*;
public class Pricing {
    public double subtotal(Map<String, MenuItem> menu, List<OrderLine> lines, StringBuilder out) {
        double subtotal = 0.0;
        for (OrderLine l : lines) {
            MenuItem item = menu.get(l.itemId);
            double lineTotal = item.price * l.qty;
            subtotal += lineTotal;
            out.append(String.format("- %s x%d = %.2f\n", item.name, l.qty, lineTotal));
        }
        return subtotal;
    }
}
