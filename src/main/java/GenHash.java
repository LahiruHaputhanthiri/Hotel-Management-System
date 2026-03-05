
import org.mindrot.jbcrypt.BCrypt;
import java.io.*;

public class GenHash {
    public static void main(String[] args) throws Exception {
        String pass = "Admin@123";
        String hash = BCrypt.hashpw(pass, BCrypt.gensalt(12));
        PrintWriter pw = new PrintWriter(new FileWriter("hash.txt"));
        pw.println(hash);
        pw.close();
        System.out.println("HASH_GENERATED");
    }
}
