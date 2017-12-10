package javamultithreading.livelock;

import java.util.concurrent.*;
import java.util.logging.Logger;

/**
 * Example of livelock in message queue pattern. Producer fails on fifth message and tries to restart processing of
 * this message again and again. The Poison message has higher priority, so new messages never processed by consumer.
 * Producer will keep flooding the queue with messages.
 */
public class Livelock {

	private final static Logger log = Logger.getLogger(Livelock.class.getName());

	private static BlockingQueue<OrderedTask> queue = new PriorityBlockingQueue<>(1000);

	public static void main(String[] args) throws InterruptedException {
		Thread producer = new Thread(() -> {
			try {
				for (int i = 0; i < 1000; i++) {
					if (i == 5){
						log.info("Adding POISON message product in producer, queue size is " + queue.size());
						queue.put(new OrderedTask(i, () -> {
							log.info("Processing POISON Message product from consumer, throwing exception");
							throw new RuntimeException();
						}));
					} else {
						log.info("Adding a normal product in producer, queue size is " + queue.size());
						queue.put(new OrderedTask(i, () -> {
							log.info("Processing a normal product from consumer");
						}));
					}
					sleepFor(2);
				}
			} catch (InterruptedException ignored) {
			}
		});

		Thread consumer = new Thread(() -> {
			try {
				while (true) {
					OrderedTask taken = queue.take();
					Thread thread = new Thread(taken.task);
					thread.setUncaughtExceptionHandler((t, e) -> {
						log.info("Adding failed message back into the queue");
						queue.add(taken);
					});
					thread.start();
					sleepFor(2);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});

		consumer.start();
		producer.start();
	}

	static class OrderedTask implements Comparable<OrderedTask> {
		final Runnable task;
		final int index;

		OrderedTask(int index, Runnable task) {
			this.index = index;
			this.task = task;
		}

		@Override
		public int compareTo(OrderedTask o) {
			return Integer.compare(index, o.index);
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
