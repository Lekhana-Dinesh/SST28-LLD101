public class Main {
    public static void main(String[] args) {
        System.out.println("=== Student Onboarding ===");
        StudentRepo repo = new FakeDb();
        Parser parser = new Parser();
        Validator validator = new Validator();
        Printer printer = new Printer();
        OnboardingService svc = new OnboardingService(repo,parser,validator,printer);
        String raw = "name=Riya;email=riya@sst.edu;phone=9876543210;program=CSE";
        svc.registerFromRawInput(raw);
    }
}
