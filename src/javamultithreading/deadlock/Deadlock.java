package javamultithreading.deadlock;

import java.util.logging.Logger;

public class Deadlock {

	private final static Logger lock = Logger.getLogger(Deadlock.class.getName());

	static Object lock1 = new Object();
	static Object lock2 = new Object();

	public static void main(String[] args) {
		Thread thread = new Thread(() -> {
			lock.info("Acquiring lock1 from first thread");
			synchronized(lock1) {
				sleepFor(1);
				lock.info("Acquiring lock2 from first thread, waiting for second thread");
				synchronized (lock2) {
					//some operations
				}
			}
		});
		Thread invertedLockThread = new Thread(() -> {
			lock.info("Acquiring lock2 from second thread");
			synchronized(lock2) {
				sleepFor(1);
				lock.info("Acquiring lock1 from second thread, waiting for first thread");
				synchronized (lock1) {
					//some operations
				}
			}
		});
		lock.info("Two threads will enter deadlock state, waiting for each other to finish");
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
