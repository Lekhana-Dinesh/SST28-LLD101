public abstract class NotificationSender {
    protected final AuditLog audit;
    protected NotificationSender(AuditLog audit) {
        this.audit = audit;
    }
    public final void send(Notification n) {
        Notification norm = normalize(n);
        doSend(norm);
    }
    protected abstract void doSend(Notification n);
    protected Notification normalize(Notification n) {
        if (n == null) {
            throw new IllegalArgumentException("notification is null");
        }
        String subject = (n.subject == null) ? "" : n.subject;
        String body = (n.body == null) ? "" : n.body;
        String email = (n.email == null) ? "" : n.email;
        String phone = (n.phone == null) ? "" : n.phone;
        return new Notification(subject, body, email, phone);
    }
}
