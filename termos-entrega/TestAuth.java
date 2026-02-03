
import org.mindrot.jbcrypt.BCrypt;

public class TestAuth {
    public static void main(String[] args) {
        String password = "Admin@123";
        String hash = "$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy";
        
        System.out.println("Senha: " + password);
        System.out.println("Hash: " + hash);
        System.out.println("Match: " + BCrypt.checkpw(password, hash));
    }
}
