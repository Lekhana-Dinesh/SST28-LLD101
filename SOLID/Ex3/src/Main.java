import java.util.*;
public class Main {
    public static void main(String[] args) {
        System.out.println("=== Placement Eligibility ===");
        StudentProfile s = new StudentProfile("23BCS1001", "Ayaan", 8.10, 72, 18, LegacyFlags.NONE);
        RuleInput cfg = new RuleInput();
        List<EligibilityRule> rules = List.of(new DisciplinaryFlag(),new MinCgr(cfg.minCgr),new MinAttendance(cfg.minAttendance),new MinCredits(cfg.minCredits));
        EligibilityEngine engine = new EligibilityEngine(new FakeEligibilityStore(), rules);
        engine.runAndPrint(s);
    }
}
