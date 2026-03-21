import com.oceanview.util.EmailUtil;

public class TestEmail {
    public static void main(String[] args) {
        System.out.println("Sending welcome note to nayanatharahathurusinghe@gmail.com...");
        EmailUtil.sendWelcomeNote("nayanatharahathurusinghe@gmail.com", "Nayanathara");
        System.out.println("Finished test execution.");
    }
}
