import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RefactorDAOs {
    public static void main(String[] args) throws IOException {
        String[] daos = { "UserDAO.java", "RoomDAO.java", "ReservationDAO.java", "PaymentDAO.java" };
        String basePath = "c:/Users/lahir/OneDrive/Desktop/Ocean Resort/OceanViewResort/src/main/java/com/oceanview/dao/";

        for (String dao : daos) {
            Path path = Paths.get(basePath + dao);
            String content = new String(Files.readAllBytes(path));

            // Regex explanation:
            // Match: Connection conn = null; \n try { \n conn =
            // DBConnection.getInstance().getConnection(); \n PreparedStatement ps =
            // conn.prepareStatement(???);
            // Replace with: try (Connection conn =
            // DBConnection.getInstance().getConnection(); PreparedStatement ps =
            // conn.prepareStatement(???)) {

            String regex = "Connection\\s+conn\\s*=\\s*null;\\s*try\\s*\\{\\s*conn\\s*=\\s*DBConnection\\.getInstance\\(\\)\\.getConnection\\(\\);\\s*PreparedStatement\\s+ps\\s*=\\s*conn\\.prepareStatement\\(([^\\)]+)\\);";
            String replacement = "try (Connection conn = DBConnection.getInstance().getConnection();\n             PreparedStatement ps = conn.prepareStatement($1)) {";

            String modified = content.replaceAll(regex, replacement);

            // Also remove the finally block:
            // Match: } finally { \n DBConnection.closeConnection(conn); \n }
            String finallyRegex = "\\}\\s*finally\\s*\\{\\s*DBConnection\\.closeConnection\\(conn\\);\\s*\\}";
            modified = modified.replaceAll(finallyRegex, "}");

            Files.write(path, modified.getBytes());
            System.out.println("Modified " + dao);
        }
    }
}
