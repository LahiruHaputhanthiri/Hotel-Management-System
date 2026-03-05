
import org.mindrot.jbcrypt.BCrypt;

public class BCryptTest {
    public static void main(String[] args) {
        String password = "Admin@123";
        String hash = "$2a$12$LJ3m4ys3LzSodX3Q/MIJaOJ3fFqSINoz9P8DCXE3CjXhl2bRhEzHe";

        try {
            boolean match = BCrypt.checkpw(password, hash);
            System.out.println("VERIFY_START");
            System.out.println("MATCH_RESULT=" + match);

            String newHash = BCrypt.hashpw(password, BCrypt.gensalt(12));
            System.out.println("NEW_HASH=" + newHash);
            System.out.println("VERIFY_END");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
