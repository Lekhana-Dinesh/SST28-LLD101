public class ShowSeat {
    private final Seat seat;
    private volatile boolean booked;

    public ShowSeat(Seat seat) {
        this.seat = seat;
        this.booked = false;
    }

    public Seat getSeat() {
        return seat;
    }

    public boolean isBooked() {
        return booked;
    }

    public void markBooked() {
        this.booked = true;
    }

    public void markAvailable() {
        this.booked = false;
    }
}
