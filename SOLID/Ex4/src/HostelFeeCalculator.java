import java.util.*;

public class HostelFeeCalculator {
    private final FakeBookingRepo repo;
    private final MonthlyCharge roomCharge;
    public HostelFeeCalculator(FakeBookingRepo repo, MonthlyCharge roomCharge) { this.repo = repo; this.roomCharge = roomCharge; }

    public void process(BookingRequest req) {
        Money monthly = roomCharge.monthly(req);
        for (AddOn a : req.addOns) {
            monthly = monthly.plus(new AddOnCharge(a).monthly(req));
        }
        Money deposit = new Money(5000.00);

        ReceiptPrinter.print(req, monthly, deposit);

        String bookingId = "H-" + (7000 + new Random(1).nextInt(1000)); 
        repo.save(bookingId, req, monthly, deposit);
    }
}
