import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class BookingSystem {
    private final Map<String, Movie> movies = new HashMap<>();
    private final Map<String, Theatre> theatres = new HashMap<>();
    private final Map<String, Show> shows = new ConcurrentHashMap<>();
    private final Map<String, Booking> bookings = new ConcurrentHashMap<>();
    private final Map<String, Payment> paymentsByBookingId = new ConcurrentHashMap<>();

    private final RuleBasedTotalAmountCalculator amountCalculator;
    private final PaymentProcessor paymentProcessor;

    public BookingSystem(RuleBasedTotalAmountCalculator amountCalculator, PaymentProcessor paymentProcessor) {
        this.amountCalculator = amountCalculator;
        this.paymentProcessor = paymentProcessor;
    }

    public Movie createMovie(User user, String title, int durationInMinutes, String language) {
        validateAdmin(user);
        Movie movie = new Movie(IdGenerator.newId("MOV"), title, durationInMinutes, language);
        movies.put(movie.getId(), movie);
        return movie;
    }

    public Theatre createTheatre(User user, String name, String city) {
        validateAdmin(user);
        Theatre theatre = new Theatre(IdGenerator.newId("THR"), name, city);
        theatres.put(theatre.getId(), theatre);
        return theatre;
    }

    public Screen createScreen(User user, Theatre theatre, String screenName) {
        validateAdmin(user);
        Screen screen = new Screen(IdGenerator.newId("SCR"), screenName);
        theatre.addScreen(screen);
        return screen;
    }

    public void addSeatToScreen(User user, Screen screen, Seat seat) {
        validateAdmin(user);
        screen.addSeat(seat);
    }

    public Show createShow(User user, String movieId, String theatreId, String screenId, LocalDateTime startTime) {
        validateAdmin(user);
        Movie movie = requireMovie(movieId);
        Theatre theatre = requireTheatre(theatreId);
        Screen screen = theatre.getScreens()
                .stream()
                .filter(s -> s.getId().equals(screenId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Screen not found in given theatre"));

        Show show = new Show(IdGenerator.newId("SHW"), movie, theatre, screen, startTime);
        shows.put(show.getId(), show);
        return show;
    }

    public List<Theatre> showTheatres(String city) {
        return theatres.values().stream()
                .filter(theatre -> theatre.getCity().equalsIgnoreCase(city))
                .sorted(Comparator.comparing(Theatre::getName))
                .collect(Collectors.toList());
    }

    public List<Movie> showMovies(String city) {
        Set<Movie> movieSet = new LinkedHashSet<>();
        for (Show show : shows.values()) {
            if (show.getTheatre().getCity().equalsIgnoreCase(city)) {
                movieSet.add(show.getMovie());
            }
        }
        return new ArrayList<>(movieSet);
    }

    public List<Show> listShows(String city, String movieId) {
        return shows.values().stream()
                .filter(show -> show.getTheatre().getCity().equalsIgnoreCase(city))
                .filter(show -> show.getMovie().getId().equals(movieId))
                .sorted(Comparator.comparing(Show::getStartTime))
                .collect(Collectors.toList());
    }

    public synchronized Booking bookTicket(User user, String showId, List<String> seatIds, PaymentMode paymentMode) {
        if (user.getRole() != UserRole.CUSTOMER) {
            throw new IllegalArgumentException("Only customer can book tickets");
        }

        Show show = requireShow(showId);
        List<Seat> seatsToBook = new ArrayList<>();

        for (String seatId : seatIds) {
            ShowSeat showSeat = show.getShowSeat(seatId);
            if (showSeat == null) {
                throw new IllegalArgumentException("Seat not found: " + seatId);
            }
            if (showSeat.isBooked()) {
                throw new IllegalStateException("Seat already booked: " + seatId);
            }
            seatsToBook.add(showSeat.getSeat());
        }

        for (String seatId : seatIds) {
            show.getShowSeat(seatId).markBooked();
        }

        Ticket ticket = new Ticket(IdGenerator.newId("TKT"), show, seatsToBook);
        Booking booking = new Booking(IdGenerator.newId("BKG"), user, show, seatsToBook, ticket);

        double totalAmount = amountCalculator.total(show, seatsToBook);
        booking.setTotalAmount(totalAmount);

        Payment payment = new Payment(IdGenerator.newId("PAY"), booking.getId(), paymentMode, totalAmount);
        boolean paid = paymentProcessor.pay(payment);

        if (!paid) {
            for (String seatId : seatIds) {
                show.getShowSeat(seatId).markAvailable();
            }
            throw new IllegalStateException("Payment failed");
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        bookings.put(booking.getId(), booking);
        paymentsByBookingId.put(booking.getId(), payment);
        return booking;
    }

    public synchronized double cancelBooking(User user, String bookingId) {
        Booking booking = requireBooking(bookingId);
        if (!booking.getUser().getId().equals(user.getId()) && !user.isAdmin()) {
            throw new IllegalArgumentException("You cannot cancel someone else's booking");
        }
        if (booking.getStatus() == BookingStatus.CANCELLED || booking.getStatus() == BookingStatus.REFUNDED) {
            throw new IllegalStateException("Booking already cancelled");
        }

        for (Seat seat : booking.getSeats()) {
            booking.getShow().getShowSeat(seat.getId()).markAvailable();
        }

        Payment payment = paymentsByBookingId.get(bookingId);
        RefundAmountCalculator refundCalculator = payment.getMode() == PaymentMode.UPI
                ? new UpiRefundAmountCalculator()
                : new CardRefundAmountCalculator();

        double refundAmount = refundCalculator.refund(booking);
        paymentProcessor.refund(payment);
        booking.setStatus(BookingStatus.REFUNDED);
        return refundAmount;
    }

    public void printAvailableSeats(String showId) {
        Show show = requireShow(showId);
        System.out.println("Available seats for show: " + show.getId());
        for (ShowSeat showSeat : show.getAllShowSeats()) {
            String status = showSeat.isBooked() ? "BOOKED" : "AVAILABLE";
            System.out.println(showSeat.getSeat() + " -> " + status);
        }
    }

    private void validateAdmin(User user) {
        if (user == null || !user.isAdmin()) {
            throw new IllegalArgumentException("Only admin can perform this operation");
        }
    }

    private Movie requireMovie(String movieId) {
        Movie movie = movies.get(movieId);
        if (movie == null) {
            throw new IllegalArgumentException("Movie not found: " + movieId);
        }
        return movie;
    }

    private Theatre requireTheatre(String theatreId) {
        Theatre theatre = theatres.get(theatreId);
        if (theatre == null) {
            throw new IllegalArgumentException("Theatre not found: " + theatreId);
        }
        return theatre;
    }

    private Show requireShow(String showId) {
        Show show = shows.get(showId);
        if (show == null) {
            throw new IllegalArgumentException("Show not found: " + showId);
        }
        return show;
    }

    private Booking requireBooking(String bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("Booking not found: " + bookingId);
        }
        return booking;
    }
}
