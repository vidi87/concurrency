import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Operations {

    public static void main(String[] args) {
        final Account a = new Account(1000);
        final Account b = new Account(2000);

        ExecutorService service = Executors.newFixedThreadPool(3);
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            service.submit(new Transfer(a, b, random.nextInt(400)));
        }
        service.shutdown();

    }
}
