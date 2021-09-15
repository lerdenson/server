import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import utilities.Decryptor;

import java.util.Scanner;

public class Server {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {
        System.out.println("Enter the PORT: ");
        Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        int port = 0;
        try {
            port = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            logger.warn("Wrong PORT - Server.main()");
            System.exit(1);
        }
        logger.info("Port is accepted - {}", Server.class.getSimpleName());
        Decryptor decryptor = new Decryptor(port);
    }
}
