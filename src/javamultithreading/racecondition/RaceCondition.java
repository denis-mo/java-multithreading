package javamultithreading.racecondition;

public class RaceCondition {

    static int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread fastThread = new Thread(() -> {
            int i = counter;
            RaceCondition.sleep(1000);
            counter = i + 1;
        });
        Thread slowThread = new Thread(() -> {
            int i = counter;
            RaceCondition.sleep(3000);
            counter = i + 1;
        });

        slowThread.start();
        fastThread.start();

        slowThread.join();
        fastThread.join();

        if (counter != 2) {
            System.out.println("Race condition has occurred. Counter supposed to be 2, but turned 1 " +
                    "then threads were ended because slow thread missed update from fast thread");
        }
    }

    static void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
