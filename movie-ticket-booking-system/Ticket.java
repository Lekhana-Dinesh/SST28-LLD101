import java.util.List;

public class Ticket {
    private final String id;
    private final Show show;
    private final List<Seat> seats;

    public Ticket(String id, Show show, List<Seat> seats) {
        this.id = id;
        this.show = show;
        this.seats = seats;
    }

    public String getId() {
        return id;
    }

    public Show getShow() {
        return show;
    }

    public List<Seat> getSeats() {
        return seats;
    }

    @Override
    public String toString() {
        return "Ticket{id='" + id + "', movie='" + show.getMovie().getTitle() + "', theatre='" + show.getTheatre().getName() + "', showTime=" + show.getStartTime() + ", seats=" + seats + "}";
    }
}
