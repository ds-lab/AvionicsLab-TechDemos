package mutex.monitor;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class LockQueueTest {
	private LockQueue<Integer> queue;

	@Before
	public void setUp() {
		queue = new LockQueue<>();
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
}
