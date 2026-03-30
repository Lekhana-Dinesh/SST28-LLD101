public class PaymentProcessor {
    public boolean pay(Payment payment) {
        payment.setStatus(PaymentStatus.SUCCESS);
        return true;
    }

    public void refund(Payment payment) {
        payment.setStatus(PaymentStatus.REFUNDED);
    }
}
