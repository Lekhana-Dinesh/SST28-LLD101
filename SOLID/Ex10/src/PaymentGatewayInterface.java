public interface PaymentGatewayInterface {
    String charge(String studentId, double amount);
}