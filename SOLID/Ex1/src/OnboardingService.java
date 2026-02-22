import java.util.*;

public class OnboardingService {
    private final StudentRepo repo;
    private final Parser parser;
    private final Validator validator;
    private final Printer printer;

    public OnboardingService(StudentRepo repo, Parser parser, Validator validator, Printer printer) {
        this.repo = repo;
        this.parser = parser;
        this.validator = validator;
        this.printer = printer;
    }

    // Orchestrates onboarding steps using parser/validator/repo/printer.
    public void registerFromRawInput(String raw) {
        System.out.println("INPUT: " + raw);
        Map<String,String> kv = parser.parse(raw);
        List<String> errors = validator.validate(kv);
        if (!errors.isEmpty()) {
            printer.printErrors(errors);
            return;
        }
        String name = kv.getOrDefault("name", "");
        String email = kv.getOrDefault("email", "");
        String phone = kv.getOrDefault("phone", "");
        String program = kv.getOrDefault("program", "");
        String id = IdUtil.nextStudentId(repo.count());
        StudentRecord rec = new StudentRecord(id, name, email, phone, program);
        repo.save(rec);
        printer.printsuccess(rec, repo.count());
        System.out.println();
        System.out.println("-- DB DUMP --");
        System.out.print(TextTable.render3(repo));
    }
}
