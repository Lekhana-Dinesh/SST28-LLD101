import java.util.*;

public class CafeteriaSystem {
    private final Map<String, MenuItem> menu = new LinkedHashMap<>();
    private final InvoiceStore store;
    private final TaxPolicy taxPolicy;
    private final DiscountPolicy discountPolicy;
    private final Pricing pricing;
    private final InvoicePrinter printer;
    public CafeteriaSystem(InvoiceStore store, TaxPolicy taxPolicy, DiscountPolicy discountPolicy, Pricing pricing, InvoicePrinter printer) { 
        this.store = store; 
        this.taxPolicy = taxPolicy;
        this.discountPolicy = discountPolicy;
        this.pricing = pricing;
        this.printer = printer;
    }
    private int invoiceSeq = 1000;
    public void addToMenu(MenuItem i) { menu.put(i.id, i); }

    public void checkout(String customerType, List<OrderLine> lines) {
        String invId = "INV-" + (++invoiceSeq);
        StringBuilder out = new StringBuilder();
        out.append("Invoice# ").append(invId).append("\n");
        double subtotal = pricing.subtotal(menu, lines, out);
        double taxPct = taxPolicy.taxPercent(customerType);
        double tax = subtotal * (taxPct / 100.0);
        double discount = discountPolicy.discountAmount(customerType, subtotal, lines.size(), lines);
        double total = subtotal + tax - discount;
        out.append(String.format("Subtotal: %.2f\n", subtotal));
        out.append(String.format("Tax(%.0f%%): %.2f\n", taxPct, tax));
        out.append(String.format("Discount: -%.2f\n", discount));
        out.append(String.format("TOTAL: %.2f\n", total));
        String printable = InvoiceFormatter.identityFormat(out.toString());
        printer.print(printable);
        store.save(invId, printable);
        System.out.println("Saved invoice: " + invId + " (lines=" + store.countLines(invId) + ")");
    }
}
