import javax.naming.InsufficientResourcesException;
import java.util.concurrent.TimeUnit;

public class Operations {

    public static void main(String[] args) {
        final Account a = new Account(1000);
        final Account b = new Account(2000);

        new Thread(() -> {
            try {
                transfer(a, b, 200);
            } catch (InsufficientResourcesException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        try {
            transfer(b, a, 500);
        } catch (InsufficientResourcesException| InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static void transfer(Account fromAcc, Account toAcc, int amount) throws InsufficientResourcesException, InterruptedException {
        long WAIT_SEC = 1000;

        if (fromAcc.getBalance() < amount) {
            throw new InsufficientResourcesException();
        }

        if (fromAcc.getLock().tryLock(WAIT_SEC, TimeUnit.SECONDS)) {
            try {
                if (toAcc.getLock().tryLock(WAIT_SEC, TimeUnit.SECONDS)) {
                    try {
                        fromAcc.withdraw(amount);
                        toAcc.deposit(amount);
                    } finally {
                        toAcc.getLock().unlock();
                    }
                }
            } finally {
                fromAcc.getLock().unlock();
            }

        }

        System.out.println("acc1= " + fromAcc.getBalance() + "\nacc2= " + toAcc.getBalance());
    }

}
