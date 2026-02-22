public class AddOnCharge implements MonthlyCharge {
    private final AddOn addOn;
    public AddOnCharge(AddOn addOn) {
        this.addOn = addOn;
    }
    @Override
    public Money monthly(BookingRequest req) {
        return switch (addOn) {
            case MESS -> new Money(1000.0);
            case LAUNDRY -> new Money(500.0);
            case GYM -> new Money(300.0);
        };
    }
}
