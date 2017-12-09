package javamultithreading.racecondition.solution;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PessimisticLocking {

    static int counter = 0;
    static Lock counterLock = new ReentrantLock();

    public static void main(String[] args) throws InterruptedException {
        Thread fastThread = new Thread(() -> {
            counterLock.lock();
            int i = counter;
            sleepFor(1);
            if (i != counter) {
                System.out.println("Illegal state for counter variable. Aborting the action");
                return;
            }
            counter = i + 1;
            counterLock.unlock();
        });
        Thread slowThread = new Thread(() -> {
            counterLock.lock();
            int i = counter;
            sleepFor(3);
            if (i != counter) {
                System.out.println("Illegal state for counter variable. Aborting the action");
                return;
            }
            counter = i + 1;
            counterLock.unlock();
        });

        slowThread.start();
        fastThread.start();

        slowThread.join();
        fastThread.join();

        if (counter == 2) {
            System.out.println("Counter is correctly set to 2. One of the threads acquired a lock and kept it until " +
                    "the action finished. Then another did the same. The result is correct but concurrency is missing");
        }
    }

    static void sleepFor(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
