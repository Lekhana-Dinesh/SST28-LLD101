public class MinCredits implements EligibilityRule {
    private final int minCredits;
    public MinCredits(int minCredits) {
        this.minCredits = minCredits;
    }
    @Override
    public boolean fails(StudentProfile s) {
        return s.earnedCredits < minCredits;
    }
    @Override
    public String reason() {
        return "credits below 20";
    }
    
}
