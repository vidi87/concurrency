import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Account {
    private int balance;
    private Lock lock;
    private AtomicInteger failCounter = new AtomicInteger(0);

    void incFailedTransferCount() {
        failCounter.incrementAndGet();
    }

    Account(int balance) {
        this.balance = balance;
        lock = new ReentrantLock();
    }

    void withdraw(int amount) {
        balance -= amount;
    }

    void deposit(int amount) {
        balance += amount;
    }

    int getBalance() {
        return balance;
    }

    Lock getLock() {
        return lock;
    }

    AtomicInteger getFailCounter() {
        return failCounter;
    }
}
