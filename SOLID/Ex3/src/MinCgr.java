public class MinCgr implements EligibilityRule {
    private final double minCgr;
    public MinCgr(double minCgr) {
        this.minCgr = minCgr;
    }
    @Override
    public boolean fails(StudentProfile s) {
        return s.cgr < minCgr;
    }
    @Override
    public String reason() {
        return "CGR below 8.0";
    }
    
}
