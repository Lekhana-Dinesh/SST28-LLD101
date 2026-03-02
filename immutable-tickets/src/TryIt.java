import com.example.tickets.IncidentTicket;
import com.example.tickets.TicketService;
import java.util.List;

public class TryIt {
    public static void main(String[] args) {
        TicketService service = new TicketService();
        IncidentTicket t = service.createTicket("TCK-1001", "reporter@example.com", "Payment failing on checkout");
        System.out.println("Created: " + t);
        IncidentTicket assigned = service.assign(t, "agent@example.com");
        IncidentTicket escalated = service.escalateToCritical(assigned);
        System.out.println("\nOriginal ticket remains unchanged: " + t);
        System.out.println("\nAfter assign: " + assigned);
        System.out.println("\nAfter escalate: " + escalated);
        List<String> tags = escalated.getTags();
        try {
            tags.add("HACKED_FROM_OUTSIDE");
        } catch (UnsupportedOperationException e) {
            System.out.println("\nExternal tag mutation blocked: " + e);
        }
        System.out.println("\nFinal ticket after outside attempt: " + escalated);
    }
}