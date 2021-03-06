package mutex;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.function.IntFunction;

public class UtilConcurrentDemo {
	public static void main(String[] args) {
		final BlockingQueue<String> queue = new LinkedBlockingQueue<>();
		final ExecutorService es = Executors.newFixedThreadPool(10);

		/* Use a lambda so we can access the queue */
		final IntFunction<Runnable> makeProducer = index -> {
			return () -> {
				try {
					Thread.sleep((long) (Math.random() * 1000));
					queue.put("Producer " + index);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			};
		};

		final Runnable consumer = () -> {
			try {
				while (!Thread.interrupted()) {
					System.out.println("Got element from queue: " + queue.take());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};

		/* Create a bunch of producer threads */
		for (int i = 0; i < 5; ++i) {
			es.submit(makeProducer.apply(i));
		}

		/* Create a single consumer thread */
		es.submit(consumer);

		try {
			es.shutdown();
			es.awaitTermination(3, TimeUnit.SECONDS);
			es.shutdownNow();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
