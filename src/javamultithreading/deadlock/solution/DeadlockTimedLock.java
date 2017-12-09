package javamultithreading.deadlock.solution;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

public class DeadlockTimedLock {

	private final static Logger log = Logger.getLogger(DeadlockTimedLock.class.getName());

	static Lock lock1 = new ReentrantLock();
	static Lock lock2 = new ReentrantLock();

	public static void main(String[] args) {
		Thread thread = new Thread(() -> {
			log.info("Acquiring lock1 from first thread");
			lock1.lock();
			sleepFor(1);

			try {
				log.info("Trying to acquire lock2 from first thread");
				if (!lock2.tryLock(2, TimeUnit.SECONDS)){
					log.info("lock2 acquiring timed out in first thread");
				}
			} catch (InterruptedException ignored) {
			} finally {
				lock1.unlock();
			}

		});
		Thread invertedLockThread = new Thread(() -> {
			log.info("Acquiring lock2 from second thread");
			lock2.lock();
			sleepFor(1);

			log.info("Trying to acquire lock1 from second thread");
			lock1.lock();
			log.info("lock1 successfully acquired from second thread");
		});
		log.info("Two threads will enter into deadlock state, however as first thread has timeout for lock acquiring " +
				"second will eventually proceed with both locks. First thread may try to retry the same action again");
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
