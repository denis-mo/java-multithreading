package javamultithreading.racecondition.solution;

public class OptimisticLocking {

    static int counter = 0;

    public static void main(String[] args) throws InterruptedException {
        Thread fastThread = new Thread(() -> {
            int i = counter;
            sleepFor(1000);
            if (i != counter) {
                System.out.println("Illegal state for counter variable. Aborting the action");
                return;
            }
            counter = i + 1;
        });
        Thread slowThread = new Thread(() -> {
            int i = counter;
            sleepFor(3000);
            if (i != counter) {
                System.out.println("Illegal state for counter variable. Aborting the action");
                return;
            }
            counter = i + 1;
        });

        slowThread.start();
        fastThread.start();

        slowThread.join();
        fastThread.join();

        if (counter == 1) {
            System.out.println("Counter is correctly set to 1 because one of the threads notices that the value was " +
                    "changed in another thread, thus making the action processing invalid and aborting it");
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
