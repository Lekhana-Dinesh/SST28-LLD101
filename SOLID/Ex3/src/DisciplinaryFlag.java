public class DisciplinaryFlag implements EligibilityRule {
    @Override
    public boolean fails(StudentProfile s) {
        return s.disciplinaryFlag != LegacyFlags.NONE;
    }

    @Override
    public String reason() {
        return "disciplinary flag present";
    }
    
}
