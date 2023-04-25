package lesson;

import java.util.List;

public class Human {
    public String name;
    public Integer age; //используем классы обертки, чтобы там было по дефолту null
    public Boolean isClever;
    public List<String> hobbies;
    public Passport passport;

    public static class Passport {
        public Long number;
        public String issueDate;
    }
}
