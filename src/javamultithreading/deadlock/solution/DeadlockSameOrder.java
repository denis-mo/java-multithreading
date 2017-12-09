package javamultithreading.deadlock.solution;

import java.util.logging.Logger;

public class DeadlockSameOrder {

	private final static Logger log = Logger.getLogger(DeadlockSameOrder.class.getName());

	static Object lock1 = new Object();
	static Object lock2 = new Object();

	public static void main(String[] args) {
		Thread thread = new Thread(() -> {
			log.info("Acquiring lock1 from first thread");
			synchronized(lock1) {
				sleepFor(1);
				log.info("Acquiring lock2 from first thread");
				synchronized (lock2) {
					log.info("First thread enters the synchronized block");
				}
			}
		});
		Thread invertedLockThread = new Thread(() -> {
			log.info("Acquiring lock1 from second thread");
			synchronized (lock1) {
				sleepFor(1);
				log.info("Acquiring lock2 from second thread");
				synchronized(lock2) {
					log.info("Second thread enters the synchronized block");
				}
			}
		});
		log.info("Two threads will never enter deadlock state, as locks acquired the same order");
		thread.start();
		invertedLockThread.start();
	}

	static void sleepFor(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
