import org.mindrot.jbcrypt.BCrypt;

public class GenerateHash {
    public static void main(String[] args) {
        String password = "Admin@123";
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println(hash);
        
        // Testar
        if (BCrypt.checkpw(password, hash)) {
            System.out.println("VALID");
        } else {
            System.out.println("INVALID");
        }
    }
}
