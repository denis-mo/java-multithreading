package javamultithreading.racecondition;

import java.util.logging.Logger;

public class RaceCondition {

    private final static Logger log = Logger.getLogger(RaceCondition.class.getName());

    static int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread fastThread = new Thread(() -> {
            int i = counter;
            sleepFor(1000);
            counter = i + 1;
        });
        Thread slowThread = new Thread(() -> {
            int i = counter;
            sleepFor(3000);
            counter = i + 1;
        });

        slowThread.start();
        fastThread.start();

        slowThread.join();
        fastThread.join();

        if (counter != 2) {
            log.info("Race condition has occurred. Counter supposed to be 2, but turned 1 " +
                    "then threads were ended because slow thread missed update from fast thread");
        }
    }

    static void sleepFor(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
