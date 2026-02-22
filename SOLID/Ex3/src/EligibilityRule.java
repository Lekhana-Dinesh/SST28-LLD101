public interface EligibilityRule {
    boolean fails(StudentProfile s);
    String reason();
}
