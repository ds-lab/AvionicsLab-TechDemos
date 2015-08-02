package mutex.monitor;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

public class MonitorQueueTest {
	private MonitorQueue<Integer> queue;

	@Before
	public void setUp() {
		queue = new MonitorQueue<>();
	}

	@Test(timeout = 500)
	public void testTakeOnEmptyQueueBlocks() throws InterruptedException {
		queue.take();
	}

	@Test
	public void testQueueOrder() throws InterruptedException {
		for (int i = 0; i < 10; ++i) {
			queue.put(i);
		}

		for (Integer i = 0; i < 10; ++i) {
			assertEquals(i, queue.take());
		}
	}

	@Test
	public void testConcurrentAccess() throws InterruptedException {
		final ExecutorService es = Executors.newFixedThreadPool(4);

		final Collection<Callable<Integer>> tasks = new ArrayList<>();
		for (int i = 0; i < 100; ++i) {
			tasks.add(() -> {
				queue.put(0);
				return 0;
			});
			tasks.add(() -> {
				return queue.take();
			});
		}

		es.invokeAll(tasks);
		es.shutdown();
		es.awaitTermination(1, TimeUnit.SECONDS);

		assertEquals(0, queue.size());
	}
}
