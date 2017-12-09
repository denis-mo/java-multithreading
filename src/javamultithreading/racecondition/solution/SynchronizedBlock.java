package javamultithreading.racecondition.solution;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronizedBlock {

    static int counter = 0;
    static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        Thread fastThread = new Thread(() -> {
            synchronized (lock) {
                int i = counter;
                sleepFor(1);
                counter = i + 1;
            }
        });
        Thread slowThread = new Thread(() -> {
            synchronized (lock) {
                int i = counter;
                sleepFor(3);
                counter = i + 1;
            }
        });

        slowThread.start();
        fastThread.start();

        slowThread.join();
        fastThread.join();

        if (counter == 2) {
            System.out.println("Counter is correctly set to 2. One of the threads enters the synchronized block with " +
                    "public lock being hold and kept this lock until the action finished. Then another thread did the " +
                    "same. The result is correct but concurrency is missing");
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
