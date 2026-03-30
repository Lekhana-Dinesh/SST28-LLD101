import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        RuleBasedTotalAmountCalculator amountCalculator = new RuleBasedTotalAmountCalculator();
        amountCalculator.addRule(new PricingRule("Weekend surge", DayOfWeek.SATURDAY, 1.2));
        amountCalculator.addRule(new PricingRule("Weekend surge", DayOfWeek.SUNDAY, 1.2));

        BookingSystem bookingSystem = new BookingSystem(amountCalculator, new PaymentProcessor());

        User admin = new User("U1", "Admin", "admin@bookmyshow.com", UserRole.ADMIN);
        User customer = new User("U2", "Lekhana", "lekhana@gmail.com", UserRole.CUSTOMER);

        Movie movie1 = bookingSystem.createMovie(admin, "Interstellar", 169, "English");
        Movie movie2 = bookingSystem.createMovie(admin, "Kantara", 148, "Kannada");

        Theatre theatre = bookingSystem.createTheatre(admin, "PVR Orion", "Bengaluru");
        Screen screen1 = bookingSystem.createScreen(admin, theatre, "Screen-1");

        bookingSystem.addSeatToScreen(admin, screen1, new Seat("A1", 1, 1, SeatType.GOLD));
        bookingSystem.addSeatToScreen(admin, screen1, new Seat("A2", 1, 2, SeatType.GOLD));
        bookingSystem.addSeatToScreen(admin, screen1, new Seat("B1", 2, 1, SeatType.PLATINUM));
        bookingSystem.addSeatToScreen(admin, screen1, new Seat("B2", 2, 2, SeatType.PLATINUM));
        bookingSystem.addSeatToScreen(admin, screen1, new Seat("C1", 3, 1, SeatType.RECLINER));

        Show show1 = bookingSystem.createShow(admin, movie1.getId(), theatre.getId(), screen1.getId(),
                LocalDateTime.of(2026, 4, 4, 9, 0));
        Show show2 = bookingSystem.createShow(admin, movie2.getId(), theatre.getId(), screen1.getId(),
                LocalDateTime.of(2026, 4, 4, 18, 0));

        System.out.println("Theatres in Bengaluru:");
        bookingSystem.showTheatres("Bengaluru").forEach(System.out::println);

        System.out.println("\nMovies running in Bengaluru:");
        bookingSystem.showMovies("Bengaluru").forEach(System.out::println);

        System.out.println("\nShows for movie: " + movie1.getTitle());
        bookingSystem.listShows("Bengaluru", movie1.getId()).forEach(System.out::println);

        System.out.println("\nBefore booking:");
        bookingSystem.printAvailableSeats(show1.getId());

        Booking booking = bookingSystem.bookTicket(customer, show1.getId(), Arrays.asList("A1", "B1"), PaymentMode.UPI);
        System.out.println("\nBooking successful");
        System.out.println("Booking ID: " + booking.getId());
        System.out.println("Ticket: " + booking.getTicket());
        System.out.println("Total amount: " + booking.getTotalAmount());

        System.out.println("\nAfter booking:");
        bookingSystem.printAvailableSeats(show1.getId());

        double refundAmount = bookingSystem.cancelBooking(customer, booking.getId());
        System.out.println("\nBooking cancelled. Refund amount: " + refundAmount);

        System.out.println("\nAfter cancellation:");
        bookingSystem.printAvailableSeats(show1.getId());

        System.out.println("\nSecond show created just to show admin can create movie/theatre/show directly:");
        System.out.println(show2);
    }
}
