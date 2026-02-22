public class MinAttendance implements EligibilityRule {
    private final int minAttendance;
    public MinAttendance(int minAttendance) {
        this.minAttendance = minAttendance;
    }
    @Override
    public boolean fails(StudentProfile s) {
        return s.attendancePct < minAttendance;
    }
    @Override
    public String reason() {
        return "attendance below 75";
    }
}
