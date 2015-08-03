package mutex.monitor;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

public abstract class ThreadSafeBlockingQueueTestBase {
	protected ThreadSafeBlockingQueue<Integer> queue;

	@Test
	public void testTakeOnEmptyQueueBlocks() throws InterruptedException {
		final ExecutorService es = Executors.newSingleThreadExecutor();
		es.execute(() -> {
			try {
				queue.take();
			} catch (InterruptedException e) {
			}
		});
		es.shutdown();

		final boolean terminated = es.awaitTermination(250, TimeUnit.MILLISECONDS);

		assertFalse(terminated);
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

}