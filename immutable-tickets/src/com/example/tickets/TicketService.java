package com.example.tickets;
import java.util.ArrayList;
import java.util.List;
public class TicketService {
    public IncidentTicket createTicket(String id, String reporterEmail, String title) {
        List<String> tags = new ArrayList<>();
        tags.add("NEW");
        return IncidentTicket.builder().id(id).reporterEmail(reporterEmail)
        .title(title).priority("MEDIUM").source("CLI")
        .customerVisible(false).tags(tags).build();
    }

    public IncidentTicket escalateToCritical(IncidentTicket t) {
        List<String> updatedTags = new ArrayList<>(t.getTags());
        updatedTags.add("ESCALATED");
        return t.toBuilder().priority("CRITICAL").tags(updatedTags).build();
    }

    public IncidentTicket assign(IncidentTicket t, String assigneeEmail) {
        return t.toBuilder().assigneeEmail(assigneeEmail).build();
    }
}