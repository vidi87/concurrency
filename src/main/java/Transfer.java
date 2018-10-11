import javax.naming.InsufficientResourcesException;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class Transfer implements Callable<Boolean> {
    private Account accountFrom;
    private Account accountTo;
    private int amount;

    Transfer(Account accountFrom, Account accountTo, int amount) {
        this.accountFrom = accountFrom;
        this.accountTo = accountTo;
        this.amount = amount;
    }

    @Override
    public Boolean call() throws Exception {
        long WAIT_SEC = 1000;

        if (accountFrom.getLock().tryLock(WAIT_SEC, TimeUnit.SECONDS)) {
            try {
                if (accountTo.getLock().tryLock(WAIT_SEC, TimeUnit.SECONDS)) {
                    try {

                        if (accountFrom.getBalance() < amount) {
                            throw new InsufficientResourcesException();
                        }

                        accountFrom.withdraw(amount);
                        accountTo.deposit(amount);

                    } finally {
                        accountTo.getLock().unlock();
                    }
                } else {
                    accountTo.incFailedTransferCount();
                }
            } finally {
                accountFrom.getLock().unlock();
            }
            return true;
        } else {
            accountFrom.incFailedTransferCount();
            return false;
        }
    }


    public Account getAccountFrom() {
        return accountFrom;
    }

    public void setAccountFrom(Account accountFrom) {
        this.accountFrom = accountFrom;
    }

    public Account getAccountTo() {
        return accountTo;
    }

    public void setAccountTo(Account accountTo) {
        this.accountTo = accountTo;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
