import java.util.*;
public interface StudentRepo {
    void save(StudentRecord rec);
    int count();
    List<StudentRecord> all();
}
